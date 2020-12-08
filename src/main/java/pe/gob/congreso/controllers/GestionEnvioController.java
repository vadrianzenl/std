package pe.gob.congreso.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.gob.congreso.dao.FichaProveidoDao;
import pe.gob.congreso.model.Deriva;
import pe.gob.congreso.model.FichaDocumento;
import pe.gob.congreso.model.FichaProveido;
import pe.gob.congreso.model.MpCanalAsociadoEnvio;
import pe.gob.congreso.model.MpDetGestionEnvio;
import pe.gob.congreso.model.MpEtapaGestionEnvio;
import pe.gob.congreso.model.MpGestionEnvio;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.DerivaUtil;
import pe.gob.congreso.service.DerivaService;
import pe.gob.congreso.service.GestionCargoService;
import pe.gob.congreso.service.GestionEnvioService;
import pe.gob.congreso.service.UsuarioService;
import pe.gob.congreso.service.impl.Status;
import pe.gob.congreso.util.Constantes;
import pe.gob.congreso.util.Contenedor;
import pe.gob.congreso.util.FichaPedientes;
import pe.gob.congreso.util.Reporte;

@Controller
@RequestMapping(Urls.GestionEnvio.BASE)
public class GestionEnvioController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	GestionEnvioService gestionEnvioService;
    
    @Autowired
    UsuarioService usuarioService;
    
    @Autowired
    FichaProveidoDao fichaProveidoDao;
    
    @Autowired
    GestionCargoService gestionCargoService;
    
    @Autowired
    DerivaService derivaService;
    
	
	@RequestMapping(value = "/canal/idGrupo/{idGrupo}/idDependencia/{idDependencia}/idEmpleado/{idEmpleado}", method = RequestMethod.GET)
	public @ResponseBody MpCanalAsociadoEnvio getCanalAsociado(@PathVariable("idGrupo") String idGrupo,
			@PathVariable("idDependencia") String idDependencia, @PathVariable("idEmpleado") String idEmpleado)
			throws Exception {		
		return gestionEnvioService.getCanalAsociado(idGrupo, idDependencia, idEmpleado);
		
	}
		
	
	@RequestMapping(value = "/registraReporte", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object createGestionEnvio(HttpServletRequest request, @RequestBody MpGestionEnvio reporte, @QueryParam("operacion") String operacion) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario)
    		return gestionEnvioService.createGestionEnvio((Usuario) datosSession, reporte, operacion);
    	else
    		return 	datosSession;
    }
	
	
	@RequestMapping(value = "/registraEtapa", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object createEtapaGestionEnvio(HttpServletRequest request, @RequestBody MpEtapaGestionEnvio etapa, @QueryParam("operacion") String operacion) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario)
    		return gestionEnvioService.createEtapaGestionEnvio((Usuario) datosSession, etapa, operacion);
    	else
    		return 	datosSession;
    }
	
	
	@RequestMapping(value = "/registraCargo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object createDetalleGestionEnvio(HttpServletRequest request, @RequestBody MpDetGestionEnvio cargo, @QueryParam("operacion") String operacion) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario)
    		return gestionEnvioService.createDetalleGestionEnvio((Usuario) datosSession, cargo, operacion);
    	else
    		return 	datosSession;
    }
	
	
	@RequestMapping(value = "/estafeta", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object updateEstafeta(HttpServletRequest request, @RequestBody FichaDocumento fd, @QueryParam("operacion") String operacion) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario) {
    		return gestionEnvioService.updateEstafeta((Usuario) datosSession, fd, operacion);
    	} else {
    		return 	datosSession;
    	}
    }
	
	
    @RequestMapping(value = "/pendientes/indEstafeta/{indEstafeta}/pag/{pag}/pagLength/{pagLength}", method = RequestMethod.GET)
    public @ResponseBody
    Object getEnviosPendientes(HttpServletRequest request, @PathVariable("indEstafeta") String indEstafeta, @PathVariable("pag") String pag, @PathVariable("pagLength") String pagLength) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario) {
    		return gestionEnvioService.getEnviosPendientes((Usuario) datosSession, indEstafeta, pag, pagLength, Constantes.VACIO, Constantes.VACIO);  		
    	} else {
    		return 	datosSession;
    	}    	
    	
    }
    
    
    @RequestMapping(value = "/fichaProveidoByIdRU/id/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Object getFichaProveidoByIdRU(HttpServletRequest request, @PathVariable("id") String id) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario) {
    		
    		FichaProveido fp = (FichaProveido) gestionEnvioService.getFichaProveidoByIdRU((Usuario) datosSession, id);
    	    DerivaUtil du = new DerivaUtil();
	        FichaDocumento fichaDocumento = new FichaDocumento();
	        fichaDocumento.setId(fp.getFichaDocumentoId());
	        fichaDocumento.setFechaCrea(fp.getFechaCrea());
	        List<Deriva> listDeriva = new ArrayList<Deriva>();
	        listDeriva = derivaService.findDerivados(fichaDocumento.getId().toString());
			fichaDocumento.setListDeriva(listDeriva);
	        du.setFichaDocumento(fichaDocumento);
	        du = derivaService.asignarEtapaIndicadorMesaPartes(du);	              
	        Map mp = new HashMap<>();
	        mp.put("fichaProveido", fp);
	        mp.put("Etapa", du);
	       return mp;
    	} else {
    		return 	datosSession;
    	}    	
    	
    }
    
    /**
     * Genera el reporte de envio de los registros seleccionados
     * @param request
     * @param c
     * @param operacion
     * @param tipoReporte, para estafeta por defecto, para casillero es el casillero en particular, por ejemplo Presidencia 
     * @param grupoEnvio, es el grupo de envio perteneciente al canal de envio
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/reporteEnvio", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object generaReporteEnvio(HttpServletRequest request, @RequestBody Contenedor c, BindingResult result, @QueryParam("operacion") String operacion, @QueryParam("tipoReporte") String tipoReporte, @QueryParam("grupoEnvio") Integer grupoEnvio, @QueryParam("centroCostoId") String centroCostoId) throws Exception {
        if (result.hasErrors()) {  
            Status status = new Status();
            status.getErrores(result);
            return status;
        }
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario) {
    		return gestionEnvioService.generarReporteEnvio(datosSession, c, operacion, tipoReporte, grupoEnvio, centroCostoId);
    		
    	} else {
    		return 	datosSession;
    	} 

    }
    
    @RequestMapping(value = "/reporteEnvio/pdf/reporteId/{reporteId}", method = RequestMethod.GET, produces = "application/pdf")
    public @ResponseBody
    Object  reporteEnvio(HttpServletRequest request, @PathVariable("reporteId")  String reporteId) throws Exception {
    	Object datosSession = null;
    	try {
			datosSession = usuarioService.getUsuarioInfo(request);
		
	    	if (datosSession instanceof Usuario) {
	    		
	    		
	    		Map<String,Object> contenido = (Map<String, Object>) gestionCargoService.getContenidoReporte((Usuario) datosSession,reporteId,"1","10");
	    		Reporte cabReporte =  (Reporte) contenido.get("cabeceraReporte");
	    		List<FichaPedientes>  detReporte =  (List<FichaPedientes>) contenido.get("contenidoReporte");
	            byte[] contents;
				HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.parseMediaType("application/pdf"));
	            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
	            String dateString=sdf.format(new Date());
	            String filename = "Mesa_de_partes_pendientes_"+ dateString +".pdf";
	            headers.setContentDispositionFormData(filename, filename);
	            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	            //contents=  (byte[]) gestionEnvioService.getReporteEnvio(request, (Usuario) datosSession, list);				
	           // ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
	            return gestionEnvioService.getReporteEnvio(request, (Usuario) datosSession, detReporte, cabReporte);
	    	}
		} catch (Exception e) {
			log.error(e, e);
		}
		return null;

    }
    
    
    @RequestMapping(value = "/fichaProveidoByNumeroMP/numero/{numero}/ppIni/{ppIni}/ppFin/{ppFin}", method = RequestMethod.GET)
    public @ResponseBody
    Object getFichaProveidoByNumeroMP(HttpServletRequest request, @PathVariable("numero") String numero, @PathVariable("ppIni") String ppIni, @PathVariable("ppFin") String ppFin) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario) {
    		return gestionEnvioService.getFichaProveidoByNumeroMP(numero, "", ppIni, ppFin);
    	    
    	} else {
    		return 	datosSession;
    	}    	
    	
    }
	
}
