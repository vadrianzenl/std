package pe.gob.congreso.dao.impl;

import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.DateType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.AnioLegislativoDao;
import pe.gob.congreso.model.AnioLegislativo;

@Repository("anioLegislativoDao")
public class AnioLegislativoDaoImpl extends AbstractDao<Integer, AnioLegislativo> implements AnioLegislativoDao {


    @Override
    @SuppressWarnings("unchecked")
    public List<AnioLegislativo> findBy() throws Exception {
        Criteria criteria = createEntityCriteria();
        List aniosList = criteria.list();
        return aniosList;
    }

    @Override
    public AnioLegislativo getAnioLegislativoId(String codigo) throws Exception {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("codigo", Integer.parseInt(codigo)));//Integer.parseInt(id)
        AnioLegislativo anioLegislativo = (AnioLegislativo) criteria.uniqueResult();
        return anioLegislativo;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<AnioLegislativo> getAnioLegislativoPeriodoLegislativo(String id) throws Exception {
        Criteria criteria = createEntityCriteria();

        criteria.createAlias("periodoLegislativo", "pl");
        criteria.add(Restrictions.eq("pl.codigo", Integer.parseInt(id)));//Integer.parseInt(id)
        criteria.add(Restrictions.sqlRestriction(" GETDATE() >= stdal_dfecha_inicio  "));
        criteria.addOrder(Order.desc("codigo"));
        List aniosList = criteria.list();
        return aniosList;
    }

    @Override
    public AnioLegislativo getAnioActual() throws Exception {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.sqlRestriction(" GETDATE() BETWEEN stdal_dfecha_inicio AND DATEADD(DAY,1,stdal_dfecha_fin) "));
        AnioLegislativo anioLegislativo = (AnioLegislativo) criteria.uniqueResult();
        return anioLegislativo;
    }

}
