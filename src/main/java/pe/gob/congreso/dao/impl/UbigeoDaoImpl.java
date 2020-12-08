package pe.gob.congreso.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import pe.gob.congreso.dao.UbigeoDao;
import pe.gob.congreso.model.UbigeoMaestro;

@Repository("ubigeoDao")
public class UbigeoDaoImpl extends AbstractDao<String, UbigeoMaestro> implements UbigeoDao {

    @Override
    public UbigeoMaestro getUbigeo(String codigo) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.eq("codigo", codigo));

        UbigeoMaestro ubigeo = (UbigeoMaestro) criteria.uniqueResult();

        return ubigeo;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<UbigeoMaestro> getDepartamentos() throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.sqlRestriction(" right(tmu_ccodigo,4) = ? ", "0000", StringType.INSTANCE));
        criteria.addOrder(Order.asc("descripcion"));

        List ubigeosList = criteria.list();

        return ubigeosList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<UbigeoMaestro> getProvincias(String departamento) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.sqlRestriction(" LEFT(tmu_ccodigo,2) = ? ", departamento.substring(0, 2), StringType.INSTANCE));
        criteria.add(Restrictions.sqlRestriction(" tmu_ccodigo <> ? ", departamento, StringType.INSTANCE));
        criteria.add(Restrictions.sqlRestriction(" RIGHT(tmu_ccodigo,2) = ? ", "00", StringType.INSTANCE));
        criteria.addOrder(Order.asc("descripcion"));

        List ubigeosList = criteria.list();

        return ubigeosList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<UbigeoMaestro> getDistritos(String provincia) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.sqlRestriction(" LEFT(tmu_ccodigo,4) = ? ", provincia.substring(0, 4), StringType.INSTANCE));
        criteria.add(Restrictions.sqlRestriction(" tmu_ccodigo <> ? ", provincia, StringType.INSTANCE));
        criteria.addOrder(Order.asc("descripcion"));

        List ubigeosList = criteria.list();

        return ubigeosList;
    }

}
