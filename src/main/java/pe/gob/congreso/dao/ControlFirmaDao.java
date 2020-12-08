package pe.gob.congreso.dao;


import pe.gob.congreso.model.ControlFirma;

public interface ControlFirmaDao {

	public ControlFirma findByIdEmpleado(String idEmpleado) throws Exception;
	
}
