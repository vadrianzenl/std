package pe.gob.congreso.dao;

import java.util.List;

import pe.gob.congreso.model.EntidadExterna;

public interface EntidadesExternasDao {

	public EntidadExterna getEntidadesExternasById(String id) throws Exception;

    public List<EntidadExterna> getEntidadesExternas() throws Exception;

	public EntidadExterna createEntidadExterna(EntidadExterna entidad) throws Exception;
	
	public List<EntidadExterna> getEntidadesExternasByName(String name) throws Exception;
    
    
}
