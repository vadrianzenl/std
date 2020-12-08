package pe.gob.congreso.dao;

import java.util.List;

import com.google.common.base.Optional;
import java.util.Map;
import pe.gob.congreso.model.NotificacionEmpleado;
import pe.gob.congreso.model.util.InputSelectUtil;

public interface NotificacionEmpleadoDao {
	
	public NotificacionEmpleado create(NotificacionEmpleado ne) throws Exception;

    public List<NotificacionEmpleado> findBy(Integer idEmpleado) throws Exception;
    
    public NotificacionEmpleado findBy(Integer idEmpleado, Integer idNotificacion) throws Exception;

}
