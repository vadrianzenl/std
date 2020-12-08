package pe.gob.congreso.dao;

import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;

import pe.gob.congreso.model.EnvioMultiple;
import pe.gob.congreso.model.util.InputSelectUtil;

public interface EnvioMultipleDao {

	public List<EnvioMultiple> getByIdFk(Optional<String> fichaDocumentoId, Optional<String> proveidoId) throws Exception;

	public Object createEnvioMultiple(EnvioMultiple em) throws Exception;
	
	public List<EnvioMultiple> getByIdGestionEnvio(Optional<String> gestionEnvioId,Optional<String> fichaDocumentoId) throws Exception;
	
	public Integer updateEnvioMultiple(EnvioMultiple envioMultiple) throws Exception;
	
	public List<EnvioMultiple> getByIdDocumentoIdReporte(Optional<String> fichaDocumentoId, Optional<String> gestionEnvioId) throws Exception;
	
	public List<InputSelectUtil> getCantidadCasillerosPendientes(Map parametros) throws Exception;

	public List<EnvioMultiple> getByIdCentroCosto(Optional<String> fichaDocumentoId, Optional<String> proveidoId, Optional<String> centroCostoId, Optional<String> canalEnvio) throws Exception;
	
}
