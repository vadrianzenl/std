package pe.gob.congreso.dao.impl;

import com.google.common.base.Optional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.TipoDao;
import pe.gob.congreso.model.Tipo;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.util.PaginationHelper;

@Repository("tipoDao")
public class TipoDaoImpl extends AbstractDao<Integer, Tipo> implements TipoDao {

    private final PaginationHelper paginationHelper = new PaginationHelper();

    @Override
    public Tipo create(Tipo t) throws Exception {
        saveOrUpdate(t);
        return t;
    }

    @Override
    public Map<String, Object> find(Optional<String> nombre, Optional<String> descripcion, Optional<String> pag, Optional<String> pagLength) throws Exception {

        Criteria criteria = createEntityCriteria();
        if (nombre.isPresent() && !nombre.get().isEmpty()) {
            criteria.add(Restrictions.eq("nombre", nombre.get()).ignoreCase());
        }
        if (descripcion.isPresent()) {
            criteria.add(Restrictions.like("descripcion", descripcion.get(), MatchMode.ANYWHERE).ignoreCase());
        }
        criteria.add(Restrictions.eq("habilitado", true));
        criteria.addOrder(Order.asc("descripcion"));

        //Pagination
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> map = paginationHelper.getPagination(criteria, "id", pag, pagLength);

        List<Tipo> listaTipos = (List<Tipo>) map.get("lista");
        result.put("tipos", listaTipos);
        result.put("totalPage", map.get("total"));

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Tipo> findBy(Optional<String> id, Optional<String> nombre) throws Exception {

        Criteria criteria = createEntityCriteria();

        if (id.isPresent()) {
            criteria.add(Restrictions.eq("id", Integer.valueOf(id.get())));
        }
        if (nombre.isPresent() && !nombre.get().isEmpty()) {
            criteria.add(Restrictions.eq("nombre", nombre.get()).ignoreCase());
        }
        criteria.add(Restrictions.eq("habilitado", true));
        criteria.addOrder(Order.asc("descripcion"));

        List tiposList = criteria.list();

        return tiposList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Tipo> findByOrden(Optional<String> id, Optional<String> nombre) throws Exception {

        Criteria criteria = createEntityCriteria();

        if (id.isPresent() && !id.get().isEmpty()) {
            criteria.add(Restrictions.eq("id", Integer.valueOf(id.get())));
        }
        if (nombre.isPresent() && !nombre.get().isEmpty()) {
            criteria.add(Restrictions.eq("nombre", nombre.get()).ignoreCase());
        }
        criteria.add(Restrictions.eq("habilitado", true));
        criteria.addOrder(Order.asc("orden"));

        List tiposList = criteria.list();

        return tiposList;
    }

    @Override
    public Tipo findByTipo(String id, String nombre) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.eq("id", Integer.valueOf(id)));
        criteria.add(Restrictions.eq("nombre", nombre));
        criteria.add(Restrictions.eq("habilitado", true));

        Tipo tipo = (Tipo) criteria.uniqueResult();

        return tipo;
    }

    @Override
    public Tipo findByCodigo(String id) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.eq("id", Integer.valueOf(id)));
        criteria.add(Restrictions.eq("habilitado", true));

        Tipo tipo = (Tipo) criteria.uniqueResult();

        return tipo;
    }

    @Override
    public Tipo findByNombre(String nombre) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.eq("nombre", nombre));
        criteria.add(Restrictions.eq("habilitado", true));

        Tipo tipo = (Tipo) criteria.uniqueResult();

        return tipo;
    }

    @Override
    public Tipo findByTipoId(String descripcion, String nombre) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.eq("descripcion", descripcion));
        criteria.add(Restrictions.eq("nombre", nombre));
        criteria.add(Restrictions.eq("habilitado", true));

        Tipo tipo = (Tipo) criteria.uniqueResult();

        return tipo;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<InputSelectUtil> getTiposInputSelect() throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.addOrder(Order.asc("nombre"));
        criteria.add(Restrictions.eq("configurable", true));
        criteria.setProjection(Projections.distinct(Projections.projectionList()
                .add(Projections.property("nombre"), "value")
                .add(Projections.property("nombre"), "label")
        )).setResultTransformer(Transformers.aliasToBean(InputSelectUtil.class));

        List tiposList = criteria.list();

        return tiposList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Tipo> findByAlfresco() throws Exception {

        Criteria criteria = createEntityCriteria();

        Disjunction or = Restrictions.disjunction();
        or.add(Restrictions.eq("id", 12));
        or.add(Restrictions.eq("id", 13));
        or.add(Restrictions.eq("id", 14));
        or.add(Restrictions.eq("id", 15));
        or.add(Restrictions.eq("id", 16));
        or.add(Restrictions.eq("id", 23));
        criteria.add(or);

        criteria.add(Restrictions.eq("habilitado", true));

        List tiposList = criteria.list();

        return tiposList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Tipo> findByCorreo() throws Exception {

        Criteria criteria = createEntityCriteria();

        Disjunction or = Restrictions.disjunction();
        or.add(Restrictions.eq("id", 5));
        or.add(Restrictions.eq("id", 17));
        or.add(Restrictions.eq("id", 18));
        or.add(Restrictions.eq("id", 19));
        or.add(Restrictions.eq("id", 20));

        criteria.add(or);

        criteria.add(Restrictions.eq("habilitado", true));

        List tiposList = criteria.list();

        return tiposList;
    }
    
    @Override
    public Tipo findByTipoLike(String descripcion, String nombre) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.like("descripcion", descripcion, MatchMode.ANYWHERE).ignoreCase());
        criteria.add(Restrictions.like("nombre", nombre, MatchMode.ANYWHERE).ignoreCase());
        criteria.add(Restrictions.eq("habilitado", true));

        Tipo tipo = (Tipo) criteria.uniqueResult();

        return tipo;
    }

}
