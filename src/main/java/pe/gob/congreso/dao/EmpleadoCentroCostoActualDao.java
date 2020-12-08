package pe.gob.congreso.dao;

import java.util.List;

import pe.gob.congreso.model.EmpleadoCentroCosto;
import pe.gob.congreso.model.EmpleadoCentroCostoActual;
import pe.gob.congreso.model.Usuario;

public interface EmpleadoCentroCostoActualDao {

    public EmpleadoCentroCostoActual create(EmpleadoCentroCostoActual ecca) throws Exception;

    public List<EmpleadoCentroCostoActual> getEmpleadoCentroCostoActual() throws Exception;
    
    public Integer updateCCActivo(Usuario usuario, EmpleadoCentroCosto ecca) throws Exception;
    
    public Integer update(Usuario usuario, EmpleadoCentroCostoActual ecca) throws Exception;
}
