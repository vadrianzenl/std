package pe.gob.congreso.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.MotivoDao;
import pe.gob.congreso.model.Motivo;

@Repository("motivoDao")
public class MotivoDaoImpl extends AbstractDao<Integer, Motivo> implements MotivoDao {

    @Override
    public List<Motivo> findBy() throws Exception {

        Criteria criteria = createEntityCriteria();

        List motivosList = criteria.list();

        return motivosList;
    }

    @Override
    public Motivo getMotivoId(Integer id) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.eq("id", id));//Integer.parseInt(id)

        Motivo motivo = (Motivo) criteria.uniqueResult();

        return motivo;
    }

    @Override
    public List<Motivo> getMotivoCentroCosto(String id) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.createAlias("centroCosto", "cc");
        criteria.createAlias("tipo", "t");
        criteria.add(Restrictions.eq("cc.id", id).ignoreCase());//Integer.parseInt(id)
        criteria.addOrder(Order.asc("t.descripcion"));
        criteria.add(Restrictions.eq("habilitado", true));

        List motivosList = criteria.list();

        return motivosList;
    }

    @Override
    public Motivo create(Motivo motivo) throws Exception {
        saveOrUpdate(motivo);
        return motivo;
    }

}
