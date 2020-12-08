package pe.gob.congreso.service;

import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;

import pe.gob.congreso.model.Notificacion;
import pe.gob.congreso.model.NotificacionEmpleado;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.InputSelectUtil;

public interface NotificacionService {

    public List<Notificacion> findBy() throws Exception;
    
    public Object create(Usuario usuario, NotificacionEmpleado t, String operacion) throws Exception;
	
    public List<NotificacionEmpleado> findBy(String idEmpleado) throws Exception;
    
    public NotificacionEmpleado findBy(String idEmpleado, String idNotificacion) throws Exception;

}
