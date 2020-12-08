package pe.gob.congreso.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.PeriodoLegislativoDao;
import pe.gob.congreso.model.PeriodoLegislativo;

@Repository("periodoLegislativoDao")
public class PeriodoLegislativoDaoImpl extends AbstractDao<Integer, PeriodoLegislativo> implements PeriodoLegislativoDao {

    @Override
    @SuppressWarnings("unchecked")
    public List<PeriodoLegislativo> findBy() throws Exception {

        Criteria criteria = createEntityCriteria();
        criteria.addOrder(Order.desc("codigo"));
        List periodosList = criteria.list();

        return periodosList;
    }

    @Override
    public PeriodoLegislativo getPeriodoLegislativoId(String codigo) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.eq("codigo", Integer.parseInt(codigo)));//Integer.parseInt(id)

        PeriodoLegislativo periodoLegislativo = (PeriodoLegislativo) criteria.uniqueResult();

        return periodoLegislativo;
    }

}
