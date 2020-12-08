package pe.gob.congreso.service;

import java.util.List;

import pe.gob.congreso.model.EntidadExterna;
import pe.gob.congreso.model.Usuario;

public interface EntidadesExternasService {

    public EntidadExterna getEntidadesExternasById(String id) throws Exception;

    public List<EntidadExterna> getEntidadesExternas() throws Exception;

	public EntidadExterna createEntidadExterna(Usuario datosSession, EntidadExterna entidad, String operacion) throws Exception;
	
	public List<EntidadExterna> getEntidadesExternasByName(String name) throws Exception;
	
}
