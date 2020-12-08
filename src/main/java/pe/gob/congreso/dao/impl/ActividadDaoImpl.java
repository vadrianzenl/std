package pe.gob.congreso.dao.impl;

import com.google.common.base.Optional;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pe.gob.congreso.dao.ActividadDao;
import pe.gob.congreso.model.Actividad;
import pe.gob.congreso.model.Usuario;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.Date;
import java.util.HashMap;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import pe.gob.congreso.model.Actividad.Operacion;

@Repository("actividadDao")
public class ActividadDaoImpl extends AbstractDao<Integer, Actividad> implements ActividadDao {

    protected final Log log = LogFactory.getLog(getClass());

    private final ObjectWriter ow = new ObjectMapper().writer();

    @Override
    @SuppressWarnings("unchecked")
    public List<Actividad> findBy(Optional<Integer> usuarioId, Optional<String> nombreTabla, Optional<String> operacion)
            throws Exception {
        log.debug("findBy()");
        Criteria criteria = createEntityCriteria();

        if (usuarioId.isPresent()) {
            criteria.add(Restrictions.eq("usuario.id", usuarioId.get()));
        }
        if (nombreTabla.isPresent()) {
            criteria.add(Restrictions.eq("nombreTabla", nombreTabla.get()));
        }
        if (operacion.isPresent()) {
            criteria.add(Restrictions.eq("operacion", operacion.get()));
        }
        criteria.addOrder(Order.desc("fechaHora"));

        List actividadesList = criteria.list();
        return actividadesList;
    }

    public Actividad create(Usuario usuario, Object obj, Map<String, Object> params) throws DataAccessException {
        log.info("create()");
        // log.debug("create()");
        String ipAddress = (String) params.get("ipAddress");
        String op = (String) params.get("op");
        Actividad a = new Actividad();
        a.setNombreTabla(obj.getClass().getSimpleName());
        a.setOperacion(Operacion.valueOf(op).toString());
        a.setContenido(objToJson(obj));
        a.setUsuario(new Usuario());
        a.getUsuario().setNombreUsuario(usuario.getNombreUsuario());
        a.setFechaHora(new Date());
        a.setNombreEquipo(ipAddress);

        saveOrUpdate(a);

        return a;
    }

    private String objToJson(Object obj) {
        String json = "";
        try {
            json = ow.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
        return json;
    }

    @Override
    public Actividad auditar(Object obj) throws Exception {

        log.info(objToJson(obj));
        Map<String, String> map = new HashMap<>();
        map = (HashMap<String, String>) obj;

        Actividad a = new Actividad();
        a.setUsuario(new Usuario());
        a.getUsuario().setNombreUsuario(map.get("usuario"));
        a.setNombreTabla(map.get("nombreTabla"));
        a.setOperacion(map.get("operacion"));
        a.setContenido(map.get("contenido"));
        a.setNombreEquipo(map.get("nombreEquipo"));
        a.setObservaciones(map.get("observaciones"));
        a.setFechaHora(new Date());
        a.setCcosto(map.get("ccosto"));
        a.setPerfil(map.get("perfil"));

        log.info(objToJson(a));

        saveOrUpdate(a);
        return a;
    }

}
