package pe.gob.congreso.dao.impl;

import com.google.common.base.Optional;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.model.SubCategoria;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.dao.SubCategoriaDao;

@Repository("subCategoriaDao")
public class SubCategoriaDaoImpl extends AbstractDao<Integer, SubCategoria> implements SubCategoriaDao {

    @Override
    public SubCategoria create(SubCategoria sd) {
        saveOrUpdate(sd);
        return sd;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SubCategoria> findBy(Optional<String> categoriaId) {

        Criteria criteria = createEntityCriteria();

        criteria.createAlias("categoria", "c");
        if (categoriaId.isPresent() && !categoriaId.get().isEmpty()) {
            criteria.add(Restrictions.eq("c.id", Integer.parseInt(categoriaId.get())));//Integer.parseInt(id)
        }
        criteria.add(Restrictions.eq("habilitado", true));

        List subCategoriasList = criteria.list();

        return subCategoriasList;
    }

    @Override
    public SubCategoria getSubCategoriaId(Integer id) {

        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.eq("id", id));//Integer.parseInt(id)
        criteria.add(Restrictions.eq("habilitado", true));

        SubCategoria subCategoria = (SubCategoria) criteria.uniqueResult();

        return subCategoria;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<InputSelectUtil> getSubCategoríasInputSelect(Optional<String> categoriaId) {

        Criteria criteria = createEntityCriteria();

        criteria.createAlias("categoria", "c");
        if (categoriaId.isPresent() && !categoriaId.get().isEmpty()) {
            criteria.add(Restrictions.eq("c.id", Integer.parseInt(categoriaId.get())));
        }
        criteria.add(Restrictions.eq("habilitado", true));
        criteria.addOrder(Order.asc("descripcion"));
        criteria.setProjection(Projections.distinct(Projections.projectionList()
                .add(Projections.property("id"), "value")
                .add(Projections.property("descripcion"), "label")
        )).setResultTransformer(Transformers.aliasToBean(InputSelectUtil.class));

        List subCategoriasList = criteria.list();

        return subCategoriasList;
    }

}
