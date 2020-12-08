package pe.gob.congreso.service;

import java.util.List;

import pe.gob.congreso.model.util.InputSelectUtil;

public interface DependenciaService {

	public List<InputSelectUtil> findDependenciasRecibidos(String centroCostoId) throws Exception;

	public List<InputSelectUtil> findDependenciasEnviados(String centroCostoId) throws Exception;
}
