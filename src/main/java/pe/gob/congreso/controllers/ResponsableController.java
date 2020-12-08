package pe.gob.congreso.controllers;

import java.util.Date;
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

import pe.gob.congreso.dao.impl.CanalAsociadoEnvioDaoImpl;
import pe.gob.congreso.model.GrupoCentroCosto;
import pe.gob.congreso.model.MpCanalAsociadoEnvio;
import pe.gob.congreso.model.Responsable;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.CanalAsociadoEnvioService;
import pe.gob.congreso.service.GrupoCentroCostoService;
import pe.gob.congreso.service.ResponsableService;
import pe.gob.congreso.service.TipoService;
import pe.gob.congreso.service.UsuarioService;
import pe.gob.congreso.util.Constantes;

@Controller
@RequestMapping(Urls.Responsable.BASE)
public class ResponsableController {

    @Autowired
    ResponsableService responsableService;
    
    @Autowired
    UsuarioService usuarioService;
    
    @Autowired
    TipoService tipoService;
    

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List getResponsables(@QueryParam("centroCostoId") String centroCostoId) throws Exception {
        return responsableService.findResponsables(centroCostoId);
    }

    @RequestMapping(value = "/grupo/{grupoId}", method = RequestMethod.GET)
    public @ResponseBody
    List getResponsablesGrupo(@PathVariable("grupoId") String grupoId) throws Exception {
        return responsableService.findResponsablesGrupo(grupoId);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object createResponsable(HttpServletRequest request, @RequestBody Responsable t, @QueryParam("operacion") String operacion) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario) {
    		return responsableService.create((Usuario) datosSession, t, operacion);
    	} else {
    		return datosSession;
    	}
    }
}
