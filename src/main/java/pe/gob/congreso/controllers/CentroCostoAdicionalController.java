package pe.gob.congreso.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.gob.congreso.model.CentroCostoAdicional;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.CentroCostoAdicionalService;
import pe.gob.congreso.service.UsuarioService;

@Controller
@RequestMapping(Urls.CentroCostoAdicional.BASE)
public class CentroCostoAdicionalController {

    @Autowired
    CentroCostoAdicionalService centroCostoAdicionalService;
    
    @Autowired
    UsuarioService usuarioService;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List getCentrosCostoAdicional() throws Exception {
        return centroCostoAdicionalService.findBy();
    }
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object createCentroCostoAdicional(HttpServletRequest request,@RequestBody  CentroCostoAdicional cc, @QueryParam("operacion") String operacion) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario)
    		return centroCostoAdicionalService.create((Usuario) datosSession, cc, operacion);
    	else
    		return datosSession;
    }
}
