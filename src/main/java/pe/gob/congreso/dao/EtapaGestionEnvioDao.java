package pe.gob.congreso.dao;

import java.util.List;

import com.google.common.base.Optional;

import pe.gob.congreso.model.MpEtapaGestionEnvio;


public interface EtapaGestionEnvioDao {

	public List<MpEtapaGestionEnvio> getListById(String gestionEnvioId, String fichaDocumentoId) throws Exception;

	public List<MpEtapaGestionEnvio> getListEtapaEnvio(Integer fichaDocumentoId) throws Exception;
	
	public MpEtapaGestionEnvio create(MpEtapaGestionEnvio etapa) throws Exception;
	
	public int verificarRegistros(String reporteId) throws Exception;
	
	public List<MpEtapaGestionEnvio> getContenidoReporteById(Optional<String> id) throws Exception;
	
	public Integer updateMpDetGestionEnvio(MpEtapaGestionEnvio etapaGesionEnvio) throws Exception;
	
	

}
