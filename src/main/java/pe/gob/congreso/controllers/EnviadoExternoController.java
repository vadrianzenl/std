package pe.gob.congreso.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.gob.congreso.model.EnviadoExterno;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.EnviadoExternoService;
import pe.gob.congreso.service.UsuarioService;

@Controller
@RequestMapping(Urls.EnviadoExterno.BASE)
public class EnviadoExternoController {

    @Autowired
    EnviadoExternoService enviadoExternoService;
    
    @Autowired
    UsuarioService usuarioService;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Object getEnviadoExternos(@QueryParam("fichaDocumentoId") String fichaDocumentoId) throws Exception {
        return enviadoExternoService.findEnviadoPor(fichaDocumentoId);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object createEnviadoExterno(HttpServletRequest request, @RequestBody EnviadoExterno ee, @QueryParam("operacion") String operacion) throws Exception {
    	
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario)
    		return enviadoExternoService.create((Usuario) datosSession, ee, operacion);
    	else
    		return datosSession;
    }
    
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public @ResponseBody
    List getEnviadoAExternos(@QueryParam("fichaDocumentoId") String fichaDocumentoId) throws Exception {
        return enviadoExternoService.findEnviadoA(fichaDocumentoId);
    }

}
