package pe.gob.congreso.service;

import com.google.common.base.Optional;
import java.util.List;
import pe.gob.congreso.model.Actividad;
import pe.gob.congreso.model.Usuario;

public interface ActividadService {

    public List<Actividad> findBy(Optional<Integer> usuarioId, Optional<String> nombreTabla, Optional<String> operacion)
            throws Exception;

    public void create(Usuario usuario, Object obj, String op) throws Exception;

    public Object auditar(Object obj) throws Exception;
}
