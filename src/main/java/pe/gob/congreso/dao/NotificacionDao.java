package pe.gob.congreso.dao;

import java.util.List;

import com.google.common.base.Optional;
import java.util.Map;
import pe.gob.congreso.model.Notificacion;
import pe.gob.congreso.model.util.InputSelectUtil;

public interface NotificacionDao {

    public List<Notificacion> findBy() throws Exception;

}
