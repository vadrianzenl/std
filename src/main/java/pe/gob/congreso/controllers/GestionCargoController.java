package pe.gob.congreso.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Optional;

import pe.gob.congreso.dao.FichaDocumentoDao;
import pe.gob.congreso.model.MpDetGestionEnvio;
import pe.gob.congreso.model.MpEtapaGestionEnvio;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.GestionCargoService;
import pe.gob.congreso.service.GestionEnvioService;
import pe.gob.congreso.service.UsuarioService;
import pe.gob.congreso.util.Contenedor;
import pe.gob.congreso.util.FichaPedientes;
import pe.gob.congreso.util.Reporte;

@Controller
@RequestMapping(Urls.GestionCargo.BASE)
public class GestionCargoController {
	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	GestionCargoService gestionCargoService;
    
    @Autowired
    UsuarioService usuarioService;
   
    @Autowired
    FichaDocumentoDao fichaDocumentoDao;
    
    @Autowired
    GestionEnvioService gestionEnvioService;
    
    
    @RequestMapping(value = "/reporteCargos/pdf", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/pdf")
    public @ResponseBody
    Object reporteEnvio(HttpServletRequest request, @RequestBody List<Reporte> listCargo) throws Exception {
    	Object datosSession;
		try {
			datosSession = usuarioService.getUsuarioInfo(request);
		
	    	if (datosSession instanceof Usuario) {
	            byte[] contents;
				contents = (byte[]) gestionCargoService.getReporteCargo(request, (Usuario) datosSession, listCargo);				
	    		HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.parseMediaType("application/pdf"));
	            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
	            String dateString=sdf.format(new Date());
	            String filename = "Mesa_de_partes_cargos_"+ dateString +".pdf";
	            headers.setContentDispositionFormData(filename, filename);
	            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	            ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
	            return response;
	    	}
		} catch (Exception e) {
			log.error(e, e);
		}
		return null;

    }  
    
    @RequestMapping(value = "/cargos", method = RequestMethod.GET)
    public @ResponseBody
    Object getReportesGenerados(HttpServletRequest request,@QueryParam("fechaIniCrea") String fechaIniCrea,@QueryParam("fechaFinCrea") String fechaFinCrea,@QueryParam("tipoReporte") String tipoReporte, @QueryParam("pag") String pag, @QueryParam("pagLength") String pagLength) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario) {
    		return gestionCargoService.getReportesGenerados((Usuario) datosSession,Optional.fromNullable(fechaIniCrea),Optional.fromNullable(fechaFinCrea),Optional.fromNullable(tipoReporte),Optional.fromNullable(pag),Optional.fromNullable(pagLength));  		
    	} else {
    		return 	datosSession;
    	}    	
    	
    }
    
    @RequestMapping(value = "/cargos/contenido/reporteId/{reporteId}/pag/{pag}/pagLength/{pagLength}", method = RequestMethod.GET)
    public @ResponseBody
    Object getContenidoReporte(HttpServletRequest request, @PathVariable("reporteId") String reporteId, @PathVariable("pag") String pag, @PathVariable("pagLength") String pagLength) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario) {
    		return gestionCargoService.getContenidoReporte((Usuario) datosSession,reporteId, pag, pagLength);  		
    	} else {
    		return 	datosSession;
    	}    	
    	
    }
    
    @RequestMapping(value = "/cargos/contenido/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object retirarContenidoReporte(HttpServletRequest request, @RequestBody MpEtapaGestionEnvio etapa, @QueryParam("operacion") String operacion) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario) {
    		return gestionCargoService.retirarContenidoReporte((Usuario) datosSession, etapa, operacion);
    	} else {
    		return 	datosSession;
    	}
    }
    
    @RequestMapping(value = "/adjunto" , method = RequestMethod.POST)
    public @ResponseBody
    Object createAdjunto(HttpServletRequest request, @RequestBody MpDetGestionEnvio a, @QueryParam("operacion") String operacion) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario)
    		return gestionCargoService.createAdjunto((Usuario) datosSession, a, operacion);
    	else
    		return datosSession;
    }
    
    @RequestMapping(value = "/adjunto/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object retirarAdjuntoCargo(HttpServletRequest request, @RequestBody MpDetGestionEnvio a, @QueryParam("operacion") String operacion) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario) {
    		return gestionCargoService.retirarAdjuntoCargo((Usuario) datosSession, a, operacion);
    	} else {
    		return 	datosSession;
    	}
    }
    
    
    public void das(){
    	
    }
    
	
}
