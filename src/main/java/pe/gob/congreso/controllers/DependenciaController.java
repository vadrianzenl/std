package pe.gob.congreso.controllers;

import java.util.List;

import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.gob.congreso.service.DependenciaService;
import pe.gob.congreso.service.UsuarioService;

@Controller
@RequestMapping(Urls.Dependencias.BASE)
public class DependenciaController {
	
	protected final Log log = LogFactory.getLog(getClass());

    @Autowired
    DependenciaService dependenciaService;
    
    @Autowired
    UsuarioService usuarioService;

    @RequestMapping(value = "/recibidos", method = RequestMethod.GET)
    public @ResponseBody
    List findDependenciasRecibidos(@QueryParam("centroCostoId") String centroCostoId) throws Exception {
        return dependenciaService.findDependenciasRecibidos(centroCostoId);
    }

    @RequestMapping(value = "/enviados", method = RequestMethod.GET)
    public @ResponseBody
    List findDependenciasEnviados(@QueryParam("centroCostoId") String centroCostoId) throws Exception {
        return dependenciaService.findDependenciasEnviados(centroCostoId);
    }

}
