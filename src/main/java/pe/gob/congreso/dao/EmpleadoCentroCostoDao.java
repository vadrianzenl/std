package pe.gob.congreso.dao;

import java.util.List;

import pe.gob.congreso.model.EmpleadoCentroCosto;
import pe.gob.congreso.model.util.InputSelectUtil;

public interface EmpleadoCentroCostoDao {
	
	public List<EmpleadoCentroCosto> getEmpleadoCentroCosto(Integer empleadoId) throws Exception;
	
	public List<EmpleadoCentroCosto> getEmpleadoCentroCostoAll(Integer empleadoId) throws Exception;
	
	public List<InputSelectUtil> getEmpleadosCentroCosto(String id) throws Exception;

}
