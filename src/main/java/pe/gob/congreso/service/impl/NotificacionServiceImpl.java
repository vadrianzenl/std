package pe.gob.congreso.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import pe.gob.congreso.dao.NotificacionDao;
import pe.gob.congreso.dao.NotificacionEmpleadoDao;
import pe.gob.congreso.model.Notificacion;
import pe.gob.congreso.model.NotificacionEmpleado;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.service.ActividadService;
import pe.gob.congreso.service.NotificacionService;

@Service("notificacionService")
@Transactional
public class NotificacionServiceImpl implements NotificacionService {

    @Autowired
    NotificacionDao notificacionDao;
    
    @Autowired
    NotificacionEmpleadoDao notificacionEmpleadoDao;
    
    @Autowired
    ActividadService actividadService;

    @Override
    public List<Notificacion> findBy() throws Exception {
        return notificacionDao.findBy();
    }

	@Override
	public Object create(Usuario usuario, NotificacionEmpleado t, String operacion) throws Exception {
		actividadService.create(usuario, t, operacion);
        return notificacionEmpleadoDao.create(t);
	}

	@Override
	public List<NotificacionEmpleado> findBy(String idEmpleado) throws Exception {
		return notificacionEmpleadoDao.findBy(Integer.parseInt(idEmpleado));
	}

	@Override
	public NotificacionEmpleado findBy(String idEmpleado, String idNotificacion) throws Exception {
		return notificacionEmpleadoDao.findBy(Integer.parseInt(idEmpleado), Integer.parseInt(idNotificacion));
	}

}
