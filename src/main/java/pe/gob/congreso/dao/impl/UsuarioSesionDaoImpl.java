package pe.gob.congreso.dao.impl;

import com.google.common.base.Optional;
import java.util.List;
import java.util.UUID;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.UsuarioSesionDao;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.UsuarioSesion;
import org.hibernate.Query;

@Repository("usuarioSesionDao")
public class UsuarioSesionDaoImpl extends AbstractDao<String, UsuarioSesion> implements UsuarioSesionDao {

    @Override
    public UsuarioSesion create(Usuario usuario, String sessionId) throws Exception {
        UsuarioSesion s = new UsuarioSesion();
        //s.setSesionKey(UUID.randomUUID().toString());
        s.setSesionKey(sessionId);
        s.setUsuario(usuario);
        DateTime dt = new DateTime();
        DateTime added = dt.plusHours(12);
        s.setFechaExpiracion(added.toDate());

        //persist(s);

        return s;
    }

    @Override
    public Optional<UsuarioSesion> findByKey(String sesionKey) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.eq("sesionKey", sesionKey));

        UsuarioSesion usuarioSesion = (UsuarioSesion) criteria.uniqueResult();

        return Optional.fromNullable(usuarioSesion);

    }

    @Override
    public List<UsuarioSesion> findAll() throws Exception {

        Criteria criteria = createEntityCriteria();

        List<UsuarioSesion> listaUsuarioSesion = criteria.list();

        return listaUsuarioSesion;
    }

    @Override
    public void delete(UsuarioSesion s) {
//        Query query = getSession().createSQLQuery("delete from STD_USUARIO_SESION where stds_vsesion_key = :sesionKey");
//        query.setString("sesionKey", s.getSesionKey());
//        query.executeUpdate();
    }

}
