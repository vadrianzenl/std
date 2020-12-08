package pe.gob.congreso.dao.impl;

import com.google.common.base.Optional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.CentroCostoSubCategoriaDao;
import pe.gob.congreso.model.CentroCostoSubCategoria;

@Repository("centroCostoSubCategoriaDao")
public class CentroCostoSubCategoriaDaoImpl extends AbstractDao<Integer, CentroCostoSubCategoria> implements CentroCostoSubCategoriaDao {

    public void setFetchMode(Criteria criteria) {
        criteria.setFetchMode("centroCosto", FetchMode.JOIN);
        criteria.setFetchMode("subCategoria", FetchMode.JOIN);
    }

    @Override
    public CentroCostoSubCategoria create(CentroCostoSubCategoria fd) throws Exception {
        saveOrUpdate(fd);
        return fd;
    }

    @Override
    public Map<String, Object> find(Optional<String> centroCostoId, Optional<String> id) throws Exception {
        Criteria criteria = createEntityCriteria();

        criteria.createAlias("centroCosto", "cc");
        criteria.createAlias("subCategoria", "su");

        if (centroCostoId.isPresent() && !centroCostoId.get().isEmpty()) {
            criteria.add(Restrictions.eq("cc.id", centroCostoId.get()));
        }
        if (id.isPresent() && !id.get().isEmpty()) {
            criteria.add(Restrictions.eq("su.categoria.id", Integer.valueOf(id.get())));
        }

        Map<String, Object> result = new HashMap<String, Object>();
        List<CentroCostoSubCategoria> listaCategoria = criteria.list();
        result.put("documentos", listaCategoria);
        result.put("totalResultCount", listaCategoria.size());
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CentroCostoSubCategoria> getSubCategoriasByCC(String centroCostoId) throws Exception {
        Criteria criteria = createEntityCriteria();

        this.setFetchMode(criteria);
        criteria.createAlias("centroCosto", "cc");
        criteria.createAlias("subCategoria", "su");
        criteria.createAlias("su.categoria", "cat");
        criteria.add(Restrictions.eq("cc.id", centroCostoId));
        criteria.add(Restrictions.eq("su.habilitado", true));
        criteria.addOrder(Order.asc("cat.descripcion"));
        criteria.addOrder(Order.asc("su.descripcion"));

        List centrosCostoList = criteria.list();
        return centrosCostoList;

    }

}
