package pe.gob.congreso.dao;

import java.util.Map;
import com.google.common.base.Optional;

import pe.gob.congreso.model.MpGestionEnvio;

public interface GestionEnvioDao {

	public MpGestionEnvio getById(String id) throws Exception;
	
	public MpGestionEnvio create(MpGestionEnvio reporte) throws Exception;
	
	public  Map <String, Object>  getReportesBy(Optional<String> fechaIniCrea, Optional<String> fechaFinCrea, Optional<String> tipoReporte, Optional<String> pag, Optional<String> pagLength) throws Exception;
	
	public Integer updateMpGestionEnvio(MpGestionEnvio gestionEnvio) throws Exception;
	
	public Integer updateEstadoMpGestionEnvio(MpGestionEnvio gestionEnvio) throws Exception;

}
