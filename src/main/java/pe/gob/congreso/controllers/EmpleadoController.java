package pe.gob.congreso.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Optional;

import pe.gob.congreso.model.EmpleadoCentroCosto;
import pe.gob.congreso.model.EmpleadoCentroCostoActual;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.EmpleadoService;
import pe.gob.congreso.service.UsuarioService;

@Controller
@RequestMapping(Urls.Empleado.BASE)
public class EmpleadoController {

    @Autowired
    EmpleadoService empleadoService;
    
    @Autowired
    UsuarioService usuarioService;
    
    protected final Log log = LogFactory.getLog(getClass());

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Map getEmpleados(@QueryParam("codigo") String codigo, @QueryParam("descripcion") String descripcion, @QueryParam("pag") String pag, @QueryParam("pagLength") String pagLength) throws Exception {
        return empleadoService.findBy(Optional.fromNullable(codigo), Optional.fromNullable(descripcion), Optional.fromNullable(pag), Optional.fromNullable(pagLength));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Object getEmpleado(@PathVariable("id") String id) throws Exception {
        return empleadoService.findById(Integer.parseInt(id));
    }

    @RequestMapping(value = "/centroCosto/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List getEmpleadoCentroCosto(@PathVariable("id") String id) throws Exception {
        return empleadoService.getEmpleadoCentroCosto(id);
    }

    @RequestMapping(value = "/inputSelect", method = RequestMethod.GET)
    public @ResponseBody
    List getEmpleadoInputSelect() throws Exception {
        return empleadoService.getEmpleadoInputSelect();
    }

    @RequestMapping(value = "/centroCostoActual", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object createEmpleadoCentroCostoActual(HttpServletRequest request, @RequestBody EmpleadoCentroCostoActual ecca, @QueryParam("operacion") String operacion) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario)
    		return empleadoService.create((Usuario) datosSession, ecca, operacion);
    	else
    		return datosSession;
    }
    
    @RequestMapping(value = "/centroCostoActivo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object updateCentroCostoActivo(HttpServletRequest request, @RequestBody EmpleadoCentroCosto ecca, @QueryParam("operacion") String operacion) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario){
    		return empleadoService.updateCCActivo((Usuario) datosSession, ecca, operacion);
    	}else
    		return datosSession;
    }

    @RequestMapping(value = "/centroCostoActual", method = RequestMethod.GET)
    public @ResponseBody
    List getEmpleadoCentroCostoActual() throws Exception {
        return empleadoService.getEmpleadoCentroCostoActual();
    }
    
    @RequestMapping(value = "/{id}/centrosCosto", method = RequestMethod.GET)
    public @ResponseBody
    List getCentrosCosto(@PathVariable("id") String id) throws Exception {
    	log.info(id);
        return empleadoService.getEmpleadoCentroCosto(Integer.parseInt(id));
    }
    
    @RequestMapping(value = "/{id}/centrosCostoAll", method = RequestMethod.GET)
    public @ResponseBody
    List getCentrosCostoAll(@PathVariable("id") String id) throws Exception {
    	log.info(id);
        return empleadoService.getEmpleadoCentroCostoAll(Integer.parseInt(id));
    }
    
    @RequestMapping(value = "/empleadoCCActual", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object updateCentroCostoActual(HttpServletRequest request, @RequestBody EmpleadoCentroCostoActual ecca, @QueryParam("operacion") String operacion) throws Exception {
    	log.info("Llegaa controller");
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario){
    		log.info("Controllerr");
    		return empleadoService.updateCCActual((Usuario) datosSession, ecca, operacion);
    	}else
    		return datosSession;
    }
}
