package pe.gob.congreso.dao.impl;

import com.google.common.base.Optional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.NotificacionDao;
import pe.gob.congreso.model.Notificacion;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.util.PaginationHelper;

@Repository("notificacionDao")
public class NotificacionDaoImpl extends AbstractDao<Integer, Notificacion> implements NotificacionDao {

    @Override
    @SuppressWarnings("unchecked")
    public List<Notificacion> findBy() throws Exception {

        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("habilitado", true));

        List notificacionesList = criteria.list();

        return notificacionesList;
    }

}
