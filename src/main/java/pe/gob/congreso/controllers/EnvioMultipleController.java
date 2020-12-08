package pe.gob.congreso.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.gob.congreso.model.Deriva;
import pe.gob.congreso.model.EnvioMultiple;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.EnvioMultipleService;
import pe.gob.congreso.service.UsuarioService;
import pe.gob.congreso.service.impl.Status;
import pe.gob.congreso.util.ContenedorEnvioMultiple;

@Controller
@RequestMapping(Urls.EnvioMultiple.BASE)
public class EnvioMultipleController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	
    @Autowired
    EnvioMultipleService envioMultipleService;
	   
    @Autowired
    UsuarioService usuarioService;
    

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	   Object createEnvioMultipleMp(HttpServletRequest request,@RequestBody @Valid ContenedorEnvioMultiple c, BindingResult result, @QueryParam("operacion") String operacion) throws Exception {
	       if (result.hasErrors()) {
	            Status status = new Status();
	            status.getErrores(result);	
	            return status;
	       }
	       Object datosSession = usuarioService.getUsuarioInfo(request);
	       if (datosSession instanceof Usuario) {
	    	   	return envioMultipleService.realizarEnvioMultiple((Usuario) datosSession, c.getEnviosMultiples(), operacion);
	       } else {
	    		return datosSession;
	       }
	 }
	
	
	@RequestMapping(value = "/valida", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	   Object validaEnvioMultipleEnReporteMp(HttpServletRequest request,@RequestBody @Valid ContenedorEnvioMultiple c, BindingResult result, @QueryParam("operacion") String operacion) throws Exception {
	       if (result.hasErrors()) {
	            Status status = new Status();
	            status.getErrores(result);	
	            return status;
	       }
	       Object datosSession = usuarioService.getUsuarioInfo(request);
	       if (datosSession instanceof Usuario) {
	    	   return envioMultipleService.validaEnvioMultipleEnReporte(c.getEnviosMultiples()[0]);
	       } else {
	    		return datosSession;
	       }
	 }
	
	
	@RequestMapping(value = "/createEnvioMultipleMp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Object createEnvioMultipleMp(HttpServletRequest request,@RequestBody Deriva d, BindingResult result, @QueryParam("cCostoId") String cCostoId, @QueryParam("operacion") String operacion) throws Exception {
	    if (result.hasErrors()) {
	         Status status = new Status();
	         status.getErrores(result);	
	         return status;
	    }
	    Object datosSession = usuarioService.getUsuarioInfo(request);
	    if (datosSession instanceof Usuario) {
	 	   Usuario usuario = (Usuario) datosSession;
		   EnvioMultiple em = new EnvioMultiple();
		   em = (EnvioMultiple) envioMultipleService.crearEnvio(usuario, operacion, d);
	       Map mp = new HashMap<>();
	       mp.put("enviosMultiples", em);
		   return mp;
	    } else {
	 		return 	datosSession;
	    }
	         
	 }
	
}