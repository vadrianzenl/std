package pe.gob.congreso.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import pe.gob.congreso.model.FichaDocumento;
import pe.gob.congreso.model.FichaProveido;
import pe.gob.congreso.model.MpCanalAsociadoEnvio;
import pe.gob.congreso.model.MpDetGestionEnvio;
import pe.gob.congreso.model.MpEtapaGestionEnvio;
import pe.gob.congreso.model.MpGestionEnvio;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.util.Constantes;
import pe.gob.congreso.util.Contenedor;
import pe.gob.congreso.util.FichaPedientes;
import pe.gob.congreso.util.Reporte;

public interface GestionEnvioService {

	public MpCanalAsociadoEnvio getCanalAsociado(String idGrupo, String idDependencia, String idEmpleado) throws Exception;

	public Object createGestionEnvio(Usuario datosSession, MpGestionEnvio reporte, String operacion) throws Exception;
	
	public Object createEtapaGestionEnvio(Usuario datosSession, MpEtapaGestionEnvio etapa, String operacion) throws Exception;

	public Object createDetalleGestionEnvio(Usuario datosSession, MpDetGestionEnvio cargo, String operacion) throws Exception;

	public Object updateEstafeta(Usuario datosSession, FichaDocumento fd, String operacion) throws Exception;

	public Object getEnviosPendientes(Usuario usuario, String indEstafeta, String pag, String pagLength, String centroCostoId, String reporteId) throws Exception;

	public Object getFichaProveidoByIdRU(Usuario datosSession, String id) throws Exception;

	public Object getReporteEnvio(HttpServletRequest request, Usuario usuario, List<FichaPedientes> detReporte, Reporte cabReporte) throws Exception;

	public Object generarReporteEnvio(Object datosSession, Contenedor c, String operacion, String tipoReporte, Integer grupoEnvio,  String centroCostoId) throws Exception;
	
	public String buscarRemitido(FichaDocumento doc) throws Exception;
	
	public String buscarEnviadoA(FichaDocumento fd, List<MpCanalAsociadoEnvio> centrosCostosAsociados, FichaPedientes f, Map<String, String> mapEnviadoA) throws Exception;
	
	public Boolean buscarEnviosMultiples(FichaPedientes f, Map<String, String> mapEnviadoA, List<MpCanalAsociadoEnvio> centrosCostosAsociados, Boolean estado) throws Exception;

	public List<FichaProveido> getFichaProveidoByNumeroMP(String numero, String sumilla, String ppIni, String ppFin) throws Exception;
	
}
