package pe.gob.congreso.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.CategoriaDao;
import pe.gob.congreso.model.Categoria;
import pe.gob.congreso.model.util.InputSelectUtil;

@Repository("categoriaDao")
public class CategoriaDaoImpl extends AbstractDao<Integer, Categoria> implements CategoriaDao {

    @Override
    public Categoria create(Categoria d) throws Exception {
        saveOrUpdate(d);
        return d;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Categoria> findBy() throws Exception {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("habilitado", true));
        List categoriasList = criteria.list();
        return categoriasList;
    }

    @Override
    public Categoria getCategoriaId(String id) throws Exception {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("id", Integer.parseInt(id)));
        Categoria categoria = (Categoria) criteria.uniqueResult();
        return categoria;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<InputSelectUtil> getCategoriasInputSelect() throws Exception {
        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.eq("habilitado", true));
        criteria.addOrder(Order.asc("descripcion"));
        criteria.setProjection(Projections.distinct(Projections.projectionList()
                .add(Projections.property("id"), "value")
                .add(Projections.property("descripcion"), "label")
        )).setResultTransformer(Transformers.aliasToBean(InputSelectUtil.class));

        List categoriasList = criteria.list();
        return categoriasList;
    }

}
