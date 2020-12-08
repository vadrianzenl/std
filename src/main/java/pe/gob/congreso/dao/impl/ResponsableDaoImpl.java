package pe.gob.congreso.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.ResponsableDao;
import pe.gob.congreso.model.Responsable;

@Repository("responsableDao")
public class ResponsableDaoImpl extends AbstractDao<Integer, Responsable> implements ResponsableDao {

    @Override
    public Responsable create(Responsable d) throws Exception {
        saveOrUpdate(d);
        return d;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Responsable> findResponsables(String centroCostoId) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.createAlias("centroCosto", "cc");
        criteria.add(Restrictions.eq("cc.id", centroCostoId));
        criteria.add(Restrictions.eq("habilitado", true));

        List responsbalesList = criteria.list();

        return responsbalesList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Responsable> findResponsablesGrupo(String grupoId) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.createAlias("grupo", "g");
        criteria.add(Restrictions.eq("g.id", Integer.parseInt(grupoId)));
        criteria.add(Restrictions.eq("habilitado", true));

        List responsbalesList = criteria.list();

        return responsbalesList;
    }

}
