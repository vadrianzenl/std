package pe.gob.congreso.service;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Optional;

import pe.gob.congreso.model.MpDetGestionEnvio;
import pe.gob.congreso.model.MpEtapaGestionEnvio;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.util.Reporte;

public interface GestionCargoService {

	public Map<String, Object> getReportesGenerados(Usuario usuario,Optional<String> fechaIniCrea, Optional<String> fechaFinCrea,Optional<String> tipoReporte, Optional<String> pag, Optional<String> pagLength) throws Exception;
	
    public Object createAdjunto(Usuario usuario, MpDetGestionEnvio a, String operacion) throws Exception;
    
    public Object retirarAdjuntoCargo(Usuario datosSession, MpDetGestionEnvio a, String operacion) throws Exception;
    
    public Object getContenidoReporte(Usuario usuario, String reporteId, String pag, String pagLength) throws Exception;

	public byte[] getReporteCargo(HttpServletRequest request, Usuario datosSession, List<Reporte> listCargo) throws Exception;
	
	public Object retirarContenidoReporte(Usuario datosSession, MpEtapaGestionEnvio etapa, String operacion) throws Exception;

}
