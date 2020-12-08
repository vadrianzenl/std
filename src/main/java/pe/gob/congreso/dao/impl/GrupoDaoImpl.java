package pe.gob.congreso.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.GrupoDao;
import pe.gob.congreso.model.Grupo;
import pe.gob.congreso.model.util.InputSelectUtil;

@Repository("grupoDao")
public class GrupoDaoImpl extends AbstractDao<Integer, Grupo> implements GrupoDao {

    @Override
    public Grupo create(Grupo grupo) throws Exception {
        saveOrUpdate(grupo);
        return grupo;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Grupo> findBy() throws Exception {

        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("habilitado", true));
        List gruposList = criteria.list();

        return gruposList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<InputSelectUtil> find() throws Exception {
        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.eq("habilitado", true));
        criteria.addOrder(Order.asc("descripcion"));
        criteria.setProjection(Projections.distinct(Projections.projectionList()
                .add(Projections.property("id"), "value")
                .add(Projections.property("descripcion"), "label")
        )).setResultTransformer(Transformers.aliasToBean(InputSelectUtil.class));

        List gruposList = criteria.list();

        return gruposList;
    }

    @Override
    public Grupo getGrupoId(String id) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.eq("id", Integer.parseInt(id)));//Integer.parseInt(id)

        Grupo grupo = (Grupo) criteria.uniqueResult();

        return grupo;
    }

}
