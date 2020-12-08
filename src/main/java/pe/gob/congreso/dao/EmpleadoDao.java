package pe.gob.congreso.dao;

import com.google.common.base.Optional;
import java.util.List;
import java.util.Map;
import pe.gob.congreso.model.Empleado;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.InputSelectUtil;

public interface EmpleadoDao {

    public Map<String, Object> findBy(Optional<String> codigo, Optional<String> descripcion, Optional<String> pag, Optional<String> pagLength) throws Exception;

    public Empleado findById(Integer id) throws Exception;

    public List<InputSelectUtil> getEmpleadoCentroCosto(String id) throws Exception;

    public List<InputSelectUtil> getEmpleadoInputSelect() throws Exception;
    
    public Empleado findByNameUsuario(String nombreUsuario) throws Exception;

}
