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

import pe.gob.congreso.model.EstadoDeriva;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.EstadoDerivaService;
import pe.gob.congreso.service.UsuarioService;

@Controller
@RequestMapping(Urls.EstadoDeriva.BASE)
public class EstadoDerivaController {

    @Autowired
    EstadoDerivaService estadoDerivaService;
    
    @Autowired
    UsuarioService usuarioService;

    @RequestMapping(value = "/centroCosto/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List getEstadoDerivaCentroCosto(@PathVariable("id") String id) throws Exception {
        return estadoDerivaService.getEstadoDerivaCentroCosto(id);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object createCentroCostoEstadoDeriva(HttpServletRequest request, @RequestBody EstadoDeriva estadoDeriva, @QueryParam("operacion") String operacion) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario)
    		return estadoDerivaService.create((Usuario) datosSession, estadoDeriva, operacion);
    	else
    		return datosSession;
    }

}
