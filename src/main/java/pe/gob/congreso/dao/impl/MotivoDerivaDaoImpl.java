package pe.gob.congreso.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.MotivoDerivaDao;
import pe.gob.congreso.model.MotivoDeriva;

@Repository("motivoDerivaDao")
public class MotivoDerivaDaoImpl extends AbstractDao<Integer, MotivoDeriva> implements MotivoDerivaDao {

    @Override
    @SuppressWarnings("unchecked")
    public List<MotivoDeriva> findBy() throws Exception {

        Criteria criteria = createEntityCriteria();

        List motivosDerivaList = criteria.list();

        return motivosDerivaList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<MotivoDeriva> getMotivoDerivaCentroCosto(String id) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.createAlias("centroCosto", "cc");
        criteria.createAlias("tipo", "t");
        criteria.add(Restrictions.eq("cc.id", id).ignoreCase());//Integer.parseInt(id)
        criteria.addOrder(Order.asc("t.descripcion"));

        List motivosDerivaList = criteria.list();

        return motivosDerivaList;
    }

    @Override
    public MotivoDeriva create(MotivoDeriva motivoDeriva) throws Exception {
        saveOrUpdate(motivoDeriva);
        return motivoDeriva;
    }

}
