package pe.gob.congreso.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.NDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.gob.congreso.model.EntidadExterna;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.EntidadesExternasService;
import pe.gob.congreso.service.UsuarioService;
import pe.gob.congreso.service.impl.Status;

@Controller
@RequestMapping(Urls.EntidadesExternas.BASE)
public class EntidadesExternasController {

    @Autowired
    EntidadesExternasService entidadesExternasService;
    
    @Autowired
    UsuarioService usuarioService;
    
    protected final Log log = LogFactory.getLog(getClass());

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    EntidadExterna getEntidadesExternasById(@PathVariable("id") String id) throws Exception {
        return entidadesExternasService.getEntidadesExternasById(id);
    }

    @RequestMapping(value = "/activas", method = RequestMethod.GET)
    public @ResponseBody
    List getEntidadesExternas() throws Exception {
        return entidadesExternasService.getEntidadesExternas();
    }

	@RequestMapping(value = "/crearEntidadExterna", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Object createEntidadExterna(HttpServletRequest request,@RequestBody @Valid EntidadExterna entidad, BindingResult result, @QueryParam("operacion") String operacion) throws Exception {
       if (result.hasErrors()) {
            Status status = new Status();
            status.getErrores(result);	
            return status;
       }
       Object datosSession = usuarioService.getUsuarioInfo(request);
       if (datosSession instanceof Usuario) {    	   
    	   Object resultado = new Object();
	   		try {
	   			resultado = entidadesExternasService.createEntidadExterna((Usuario) datosSession, entidad, operacion);
	   		} catch (Exception e) {
	   			log.error(e,e);
	   		}finally{
	   			NDC.pop();
	   			NDC.remove();
	   		}
	   		return resultado;    	   
       } else {
    		return 	datosSession;
       }
	         
	}
    
    
}
