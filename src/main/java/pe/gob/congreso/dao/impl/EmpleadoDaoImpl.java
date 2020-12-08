package pe.gob.congreso.dao.impl;

import com.google.common.base.Optional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.EmpleadoDao;
import pe.gob.congreso.model.Empleado;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.util.PaginationHelper;
import static pe.gob.congreso.util.ConcatenableIlikeCriterion.ilike;

@Repository("empleadoDao")
public class EmpleadoDaoImpl extends AbstractDao<Integer, Empleado> implements EmpleadoDao {
	
	protected final Log log = LogFactory.getLog(getClass());

    private final PaginationHelper paginationHelper = new PaginationHelper();

    public void setFetchMode(Criteria criteria) {
        criteria.setFetchMode("centroCosto", FetchMode.JOIN);
        criteria.setFetchMode("usuario", FetchMode.JOIN);
    }

    @Override
    public Map<String, Object> findBy(Optional<String> codigo, Optional<String> descripcion, Optional<String> pag, Optional<String> pagLength) throws Exception {
        Criteria criteria = createEntityCriteria();

        if (codigo.isPresent() && !codigo.get().isEmpty()) {
            criteria.add(Restrictions.eq("id", Integer.parseInt(codigo.get())));
        }
        if (descripcion.isPresent() && !descripcion.get().isEmpty()) {
            criteria.add(ilike(descripcion.get(), MatchMode.ANYWHERE, "nombres", "apellidos"));
        }

        criteria.add(Restrictions.sqlRestriction(" stde_cestado = ? ", "A", StringType.INSTANCE));
        criteria.add(Restrictions.sqlRestriction(" stde_cestadoEmpleado <> ? ", "2", StringType.INSTANCE));

        //Pagination
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> map = paginationHelper.getPagination(criteria, "id", pag, pagLength);

        List<Empleado> listaEmpleados = (List<Empleado>) map.get("lista");
        result.put("empleados", listaEmpleados);
        result.put("totalPage", map.get("total"));

        return result;
    }

    @Override
    public Empleado findById(Integer id) throws Exception {
    	log.debug("findById()");
        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.eq("id", id));
        criteria.add(Restrictions.sqlRestriction(" stde_cestado = ? ", "A", StringType.INSTANCE));
        criteria.add(Restrictions.sqlRestriction(" stde_cestadoEmpleado <> ? ", "2", StringType.INSTANCE));

        Empleado empleado = (Empleado) criteria.uniqueResult();
        return empleado;
    }

    @Override
    @SuppressWarnings("unchecked")
    //@Cacheable("empleadocache") 
    public List<InputSelectUtil> getEmpleadoCentroCosto(String id) throws Exception {
    	log.info("getEmpleadoCentroCosto() id = " + id);
        Criteria criteria = createEntityCriteria();

        this.setFetchMode(criteria);
        criteria.createAlias("centroCosto", "cc");
        criteria.add(Restrictions.eq("cc.id", id));
        criteria.add(Restrictions.sqlRestriction(" stde_cestado = ? ", "A", StringType.INSTANCE));
        criteria.add(Restrictions.sqlRestriction(" stde_cestadoEmpleado <> ? ", "2", StringType.INSTANCE));
        criteria.addOrder(Order.asc("descripcion"));
        criteria.setProjection(Projections.distinct(Projections.projectionList()
                .add(Projections.property("id"), "value")
                .add(Projections.property("descripcion"), "label")
        )).setResultTransformer(Transformers.aliasToBean(InputSelectUtil.class));

        List empleadosList = criteria.list();

        return empleadosList;
    }

    @Override
    @SuppressWarnings("unchecked")
    //@Cacheable("usuariocache")
    public List<InputSelectUtil> getEmpleadoInputSelect() throws Exception {
    	log.info("getEmpleadoInputSelect() ");
        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.sqlRestriction(" stde_cestado = ? ", "A", StringType.INSTANCE));
        criteria.add(Restrictions.sqlRestriction(" stde_cestadoEmpleado <> ? ", "2", StringType.INSTANCE));
        criteria.addOrder(Order.asc("descripcion"));
//        criteria.add(Restrictions.eq("configurable", true ));
        criteria.setProjection(Projections.distinct(Projections.projectionList()
                .add(Projections.property("id"), "value")
                .add(Projections.property("descripcion"), "label")
        )).setResultTransformer(Transformers.aliasToBean(InputSelectUtil.class));

        List empleadosList = criteria.list();

        return empleadosList;
    }
    
    @Override
    public Empleado findByNameUsuario(String nombreUsuario) throws Exception {
    	log.info("findByNameUsuario()");

        Criteria criteria = createEntityCriteria();
        criteria.createAlias("usuario", "u");
	    this.setFetchMode(criteria);
	    criteria.add(Restrictions.eq("u.nombreUsuario", nombreUsuario.trim().toUpperCase()));
	    criteria.add(Restrictions.eq("u.habilitado", 1));
        Empleado empleado = (Empleado) criteria.uniqueResult();
        return empleado;
    }

}
