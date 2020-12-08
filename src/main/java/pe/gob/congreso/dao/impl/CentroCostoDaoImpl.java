package pe.gob.congreso.dao.impl;

import com.google.common.base.Optional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.CentroCostoDao;
import pe.gob.congreso.model.CentroCosto;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.util.PaginationHelper;

@Repository("centroCostoDao")
public class CentroCostoDaoImpl extends AbstractDao<String, CentroCosto> implements CentroCostoDao {

    private final PaginationHelper paginationHelper = new PaginationHelper();

    @Override
    public Map<String, Object> findBy(Optional<String> descripcion, Optional<String> pag, Optional<String> pagLength) throws Exception {

        Criteria criteria = createEntityCriteria();

        if (descripcion.isPresent() && !descripcion.get().isEmpty()) {
            criteria.add(Restrictions.like("descripcion", descripcion.get(), MatchMode.ANYWHERE));
        }

        //Pagination
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> map = paginationHelper.getPagination(criteria, "id", pag, pagLength);

        List<CentroCosto> listaCentroCostos = (List<CentroCosto>) map.get("lista");
        result.put("centrosCosto", listaCentroCostos);
        result.put("totalPage", map.get("totalPage"));

        return result;
    }

    @Override
    public CentroCosto getCentroCostoId(String id) throws Exception {
        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.eq("id", id).ignoreCase());

        CentroCosto tipo = (CentroCosto) criteria.uniqueResult();
        return tipo;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<InputSelectUtil> getCentrosCostoInputSelect() throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.addOrder(Order.asc("descripcion"));
        criteria.setProjection(Projections.distinct(Projections.projectionList()
                .add(Projections.property("id"), "value")
                .add(Projections.property("descripcion"), "label")
        )).setResultTransformer(Transformers.aliasToBean(InputSelectUtil.class));

        List centrosList = criteria.list();
        return centrosList;
    }

}
