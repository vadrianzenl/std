package pe.gob.congreso.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.google.common.base.Optional;

import pe.gob.congreso.model.Actividad;
import pe.gob.congreso.model.Usuario;

public interface ActividadDao {

    public List<Actividad> findBy(Optional<Integer> usuarioId, Optional<String> nombreTabla, Optional<String> operacion)
            throws Exception;

    public Actividad create(Usuario usuario, Object obj, Map<String, Object> params) throws DataAccessException;

    public Actividad auditar(Object obj) throws Exception;

}
