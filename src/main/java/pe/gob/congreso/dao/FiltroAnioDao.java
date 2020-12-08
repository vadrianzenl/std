package pe.gob.congreso.dao;

import pe.gob.congreso.model.FiltroAnio;

public interface FiltroAnioDao {

	public FiltroAnio buscarPermisos(String centroCosto, Integer tipoAnio) throws Exception;
}
