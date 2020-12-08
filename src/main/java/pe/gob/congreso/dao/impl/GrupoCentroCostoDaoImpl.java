package pe.gob.congreso.dao.impl;

import com.google.common.base.Optional;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.GrupoCentroCostoDao;
import pe.gob.congreso.model.GrupoCentroCosto;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.util.PaginationHelper;

@Repository("grupoCentroCostoDao")
public class GrupoCentroCostoDaoImpl extends AbstractDao<String, GrupoCentroCosto> implements GrupoCentroCostoDao {

    private final PaginationHelper paginationHelper = new PaginationHelper();

    public void setFetchMode(Criteria criteria) {
        criteria.setFetchMode("grupo", FetchMode.JOIN);
        criteria.setFetchMode("empleado", FetchMode.JOIN);
    }

    @Override
    public Map<String, Object> findBy(Optional<String> descripcion, Optional<String> pag, Optional<String> pagLength) throws Exception {

        Criteria criteria = createEntityCriteria();

        if (descripcion.isPresent() && !descripcion.get().isEmpty()) {
            criteria.add(Restrictions.like("descripcion", descripcion.get(), MatchMode.ANYWHERE));
        }

        //Pagination
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> map = paginationHelper.getPagination(criteria, "id", pag, pagLength);

        List<GrupoCentroCosto> listaCentroCostoActual = (List<GrupoCentroCosto>) map.get("lista");
        result.put("centrosCosto", listaCentroCostoActual);
        result.put("totalPage", map.get("total"));

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<GrupoCentroCosto> findByAll() throws Exception {
        Criteria criteria = createEntityCriteria();
        criteria.addOrder(Order.asc("orden"));
        List gruposCentroCostoList = criteria.list();
        return gruposCentroCostoList;
    }

    @Override
    public GrupoCentroCosto getCentroCostoActual(String id) throws Exception {

        Criteria criteria = createEntityCriteria();

        this.setFetchMode(criteria);
        criteria.add(Restrictions.eq("id", id));

        GrupoCentroCosto grupoCentroCosto = (GrupoCentroCosto) criteria.uniqueResult();

        return grupoCentroCosto;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<InputSelectUtil> getCentrosCostoByGrupo(String grupoId) throws Exception {

        Criteria criteria = createEntityCriteria();

        this.setFetchMode(criteria);
        criteria.add(Restrictions.eq("grupo.id", Integer.parseInt(grupoId)));
        criteria.addOrder(Order.asc("descripcion"));
        criteria.setProjection(Projections.distinct(Projections.projectionList()
                .add(Projections.property("id"), "value")
                .add(Projections.property("descripcion"), "label")
        )).setResultTransformer(Transformers.aliasToBean(InputSelectUtil.class));
        criteria.add(Restrictions.eq("habilitado", true));

        List gruposCentroCostoList = criteria.list();

        return gruposCentroCostoList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<GrupoCentroCosto> getCentrosCosto(String grupoId) throws Exception {

        Criteria criteria = createEntityCriteria();

        this.setFetchMode(criteria);
        criteria.add(Restrictions.eq("grupo.id", Integer.parseInt(grupoId)));
        criteria.addOrder(Order.asc("descripcion"));

        List gruposCentroCostoList = criteria.list();

        return gruposCentroCostoList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<InputSelectUtil> getCentroCostoActualInputSelect() throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.addOrder(Order.asc("descripcion"));
//        criteria.add(Restrictions.eq("configurable", true ));
        criteria.setProjection(Projections.distinct(Projections.projectionList()
                .add(Projections.property("id"), "value")
                .add(Projections.property("descripcion"), "label")
        )).setResultTransformer(Transformers.aliasToBean(InputSelectUtil.class));
        criteria.add(Restrictions.eq("habilitado", true));

        List gruposCentroCostoList = criteria.list();

        return gruposCentroCostoList;
    }

    @Override
    public Map<String, Object> find(Optional<String> grupo, Optional<String> dependencia, Optional<String> pag, Optional<String> pagLength) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.createAlias("grupo", "g");
        if (grupo.isPresent() && !grupo.get().isEmpty()) {
            criteria.add(Restrictions.like("g.descripcion", grupo.get(), MatchMode.ANYWHERE));
        }
        if (dependencia.isPresent() && !dependencia.get().isEmpty()) {
            criteria.add(Restrictions.like("descripcion", dependencia.get(), MatchMode.ANYWHERE));
        }

        //Pagination
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> map = paginationHelper.getPagination(criteria, "id", pag, pagLength);

        List<GrupoCentroCosto> listaCentroCostoActual = (List<GrupoCentroCosto>) map.get("lista");
        result.put("centrosCosto", listaCentroCostoActual);
        result.put("totalPage", map.get("total"));

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<GrupoCentroCosto> getGrupoCentroCosto() throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.createAlias("empleado", "e");
        criteria.addOrder(Order.asc("e.descripcion"));
        criteria.add(Restrictions.eq("habilitado", true));

        List gruposCentroCostoList = criteria.list();

        return gruposCentroCostoList;
    }

    @Override
    public GrupoCentroCosto create(GrupoCentroCosto gcc) throws Exception {
        saveOrUpdate(gcc);
        return gcc;
    }

	@Override
	public List<GrupoCentroCosto> getCentrosCostoByGrupoNotIn(String[] listGruposId) throws Exception {
		Integer[] gruposIds = new Integer[listGruposId.length];
		int i = 0;
		for (String id : listGruposId ) {
			gruposIds[i] = Integer.parseInt(id.trim());
			i++;
		}
		Criteria criteria = createEntityCriteria();
        this.setFetchMode(criteria);
        criteria.add(Restrictions.not(
        	    Restrictions.in("grupo.id", gruposIds)
        	  ));
        criteria.addOrder(Order.asc("id"));
        criteria.add(Restrictions.eq("habilitado", true));        
        List gruposCentroCostoList = criteria.list();
        return gruposCentroCostoList;
	}

}
