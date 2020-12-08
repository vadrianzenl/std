package pe.gob.congreso.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.ProveidoDao;
import pe.gob.congreso.model.Proveido;

@Repository("proveidoDao")
public class ProveidoDaoImpl extends AbstractDao<Integer, Proveido> implements ProveidoDao {

    @Override
    @SuppressWarnings("unchecked")
    public List<Proveido> findBy() throws Exception {

        Criteria criteria = createEntityCriteria();

        List proveidosList = criteria.list();

        return proveidosList;
    }

    @Override
    public Proveido getProveidoId(Integer id) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.eq("id", id));//Integer.parseInt(id)

        Proveido proveido = (Proveido) criteria.uniqueResult();

        return proveido;
    }

    @Override
    public Proveido getProveidoCentroCosto(String id) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.createAlias("centroCosto", "cc");
        criteria.add(Restrictions.eq("cc.id", id).ignoreCase());//Integer.parseInt(id)
        criteria.add(Restrictions.eq("habilitado", true));

        Proveido proveido = (Proveido) criteria.uniqueResult();

        return proveido;
    }

}
