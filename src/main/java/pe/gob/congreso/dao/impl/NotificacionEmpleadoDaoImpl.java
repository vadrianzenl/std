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
import pe.gob.congreso.dao.NotificacionEmpleadoDao;
import pe.gob.congreso.model.FichaDocumento;
import pe.gob.congreso.model.NotificacionEmpleado;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.util.PaginationHelper;

@Repository("notificacionEmpleadoDao")
public class NotificacionEmpleadoDaoImpl extends AbstractDao<Integer, NotificacionEmpleado> implements NotificacionEmpleadoDao {

	
	@Override
    public NotificacionEmpleado create(NotificacionEmpleado t) throws Exception {
        saveOrUpdate(t);
        return t;
    }
	
    @Override
    @SuppressWarnings("unchecked")
    public List<NotificacionEmpleado> findBy(Integer idEmpleado) throws Exception {

        Criteria criteria = createEntityCriteria();
        criteria.createAlias("empleado", "em");
        criteria.add(Restrictions.eq("em.id", idEmpleado));
        List notificacionesList = criteria.list();

        return notificacionesList;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public NotificacionEmpleado findBy(Integer idEmpleado, Integer idNotificacion) throws Exception {

        Criteria criteria = createEntityCriteria();
        criteria.createAlias("empleado", "em");
        criteria.createAlias("notificacion", "no");
        criteria.add(Restrictions.eq("em.id", idEmpleado));
        criteria.add(Restrictions.eq("no.id", idNotificacion));
        criteria.add(Restrictions.eq("habilitado", true));
        NotificacionEmpleado notificacion = (NotificacionEmpleado) criteria.uniqueResult();

        return notificacion;
    }

}
