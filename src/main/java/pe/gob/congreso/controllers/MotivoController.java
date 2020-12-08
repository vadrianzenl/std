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

import pe.gob.congreso.model.Motivo;
import pe.gob.congreso.model.MotivoDeriva;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.MotivoService;
import pe.gob.congreso.service.UsuarioService;

@Controller
@RequestMapping(Urls.Motivo.BASE)
public class MotivoController {

    @Autowired
    MotivoService motivoService;
    
    @Autowired
    UsuarioService usuarioService;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List getMotivos() throws Exception {
        return motivoService.findBy();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Motivo getMotivoId(@PathVariable("id") Integer id) throws Exception {
        return motivoService.getMotivoId(id);
    }

    @RequestMapping(value = "/centroCosto/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List getMotivoCentroCosto(@PathVariable("id") String id) throws Exception {
        return motivoService.getMotivoCentroCosto(id);
    }

    @RequestMapping(value = "/deriva", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object createCentroCostoMotivoDeriva(HttpServletRequest request, @RequestBody MotivoDeriva motivoDeriva, @QueryParam("operacion") String operacion) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario)
    		return motivoService.createMotivoDeriva((Usuario) datosSession, motivoDeriva, operacion);
    	else
    		return datosSession;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object createCentroCostoMotivoDeriva(HttpServletRequest request, @RequestBody Motivo motivo, @QueryParam("operacion") String operacion) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario)
    		return motivoService.create((Usuario) datosSession, motivo, operacion);
    	else
    		return datosSession;
    }

    @RequestMapping(value="/deriva/centroCosto/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List getMotivosDerivaCentroCosto(@PathVariable("id") String id) throws Exception {
        return motivoService.getMotivoDerivaCentroCosto(id);
    }
}
