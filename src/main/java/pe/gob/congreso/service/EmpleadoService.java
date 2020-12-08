package pe.gob.congreso.service;

import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;

import pe.gob.congreso.model.Empleado;
import pe.gob.congreso.model.EmpleadoCentroCosto;
import pe.gob.congreso.model.EmpleadoCentroCostoActual;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.InputSelectUtil;

public interface EmpleadoService {

    public Map<String, Object> findBy(Optional<String> codigo, Optional<String> descripcion, Optional<String> pag, Optional<String> pagLength) throws Exception;

    public Empleado findById(Integer id) throws Exception;

    public List<InputSelectUtil> getEmpleadoCentroCosto(String id) throws Exception;

    public List<InputSelectUtil> getEmpleadoInputSelect() throws Exception;

    public Object create(Usuario usuario, EmpleadoCentroCostoActual ecca, String operacion) throws Exception;
    
    public Object updateCCActivo(Usuario usuario, EmpleadoCentroCosto ecca, String operacion) throws Exception;

    public List<EmpleadoCentroCostoActual> getEmpleadoCentroCostoActual() throws Exception;
    
    public List<EmpleadoCentroCosto> getEmpleadoCentroCosto(Integer empleadoId) throws Exception;  
    
    public List<EmpleadoCentroCosto> getEmpleadoCentroCostoAll(Integer empleadoId) throws Exception;  
    
    public Object updateCCActual(Usuario usuario, EmpleadoCentroCostoActual ecca, String operacion) throws Exception;
    
}
