package pe.gob.congreso.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.EstadoDerivaDao;
import pe.gob.congreso.model.EstadoDeriva;

@Repository("estadoDerivaDao")
public class EstadoDerivaDaoImpl extends AbstractDao<Integer, EstadoDeriva> implements EstadoDerivaDao {

    @Override
    @SuppressWarnings("unchecked")
    public List<EstadoDeriva> findBy() throws Exception {
        Criteria criteria = createEntityCriteria();
        List estadosDerivaList = criteria.list();
        return estadosDerivaList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<EstadoDeriva> getEstadoDerivaCentroCosto(String id) throws Exception {
        Criteria criteria = createEntityCriteria();

        criteria.createAlias("centroCosto", "cc");
        criteria.createAlias("tipo", "t");
        criteria.add(Restrictions.eq("cc.id", id).ignoreCase());//Integer.parseInt(id)
        criteria.addOrder(Order.asc("t.descripcion"));

        List estadosDerivaList = criteria.list();
        return estadosDerivaList;
    }

    @Override
    public EstadoDeriva create(EstadoDeriva estadoDeriva) throws Exception {
        saveOrUpdate(estadoDeriva);
        return estadoDeriva;
    }

}
