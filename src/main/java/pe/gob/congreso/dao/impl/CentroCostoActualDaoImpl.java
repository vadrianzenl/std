package pe.gob.congreso.dao.impl;

import com.google.common.base.Optional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.CentroCostoActualDao;
import pe.gob.congreso.model.CentroCostoActual;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.util.PaginationHelper;

@Repository("centroCostoActualDao")
public class CentroCostoActualDaoImpl extends AbstractDao<String, CentroCostoActual> implements CentroCostoActualDao {

    private final PaginationHelper paginationHelper = new PaginationHelper();

    @Override
    public Map<String, Object> find(Optional<String> descripcion, Optional<String> pag, Optional<String> pagLength) throws Exception {
        Criteria criteria = createEntityCriteria();

        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> map = paginationHelper.getPagination(criteria, "id", pag, pagLength);

        List<CentroCostoActual> listaCentroCostosActuales = (List<CentroCostoActual>) map.get("lista");
        result.put("centroCostosActuales", listaCentroCostosActuales);
        result.put("totalPage", map.get("totalPage"));
        return result;

    }

    @Override
    @SuppressWarnings("unchecked")
    public List<InputSelectUtil> getCentroCostoActualInputSelect() throws Exception {
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
