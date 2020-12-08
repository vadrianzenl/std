package pe.gob.congreso.service;

import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;

import pe.gob.congreso.model.Deriva;
import pe.gob.congreso.model.EnvioMultiple;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.InputSelectUtil;

public interface EnvioMultipleService {

	public List<EnvioMultiple> getByIdFk(Optional<String> fichaDocumentoId, Optional<String> proveidoId) throws Exception;

	public Object createEnvioMultiple(Usuario datosSession, EnvioMultiple em, String operacion) throws Exception;
	
	public Object realizarEnvioMultiple(Usuario usuario, EnvioMultiple[] enviosMultiples, String operacion) throws Exception;

	public Map<String,Object> validaEnvioMultipleEnReporte(EnvioMultiple envioMultiple) throws Exception;
	
	public List<EnvioMultiple> getByIdDocumentoIdReporte(Optional<String> fichaDocumentoId, Optional<String> gestionEnvioId) throws Exception;
	
	public List<InputSelectUtil> getCantidadCasillerosPendientes(Map parametros) throws Exception;
	
	public List<EnvioMultiple> getByIdCentroCosto(Optional<String> fichaDocumentoId, Optional<String> proveidoId, Optional<String> centroCostoId, Optional<String> canalEnvio) throws Exception;
	
	public Object creaEnvioMultipleLastFichaProveido(Usuario usuario, String operacion, Deriva d, String canalEnvio) throws Exception;
	
	public Object actualizaEnvioMultipleLastFichaProveido(Usuario usuario, String operacion, Deriva d, EnvioMultiple envioMultiple, String canalEnvio) throws Exception;

	public String bucarCanalEnvioXCentroCosto(String centroCostoId) throws Exception;

	public EnvioMultiple crearEnvio(Usuario usuario, String operacion, Deriva d) throws Exception;

}
