
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

import pe.gob.congreso.model.Bitacora;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.BitacoraService;
import pe.gob.congreso.service.UsuarioService;

@Controller
@RequestMapping(Urls.Bitacora.BASE)
public class BitacoraController {
    
    @Autowired
    BitacoraService bitacoraService;
    
    @Autowired
    UsuarioService usuarioService;
    
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List getBitacoras(@QueryParam("fichaDocumentoId") String fichaDocumentoId) throws Exception {
        return bitacoraService.findBitacoras(fichaDocumentoId);
    }
    
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object createBitacora(HttpServletRequest request,@RequestBody Bitacora b, @QueryParam("operacion") String operacion) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario)
    		return bitacoraService.create((Usuario) datosSession, b, operacion);
    	else
    		return datosSession;
    }
    
    @RequestMapping(value = "/reporte/acciones/pdf/ru/{ru}", method = RequestMethod.GET, produces = "application/pdf")
    public @ResponseBody
    Object reporteAcciones(HttpServletRequest request,@PathVariable("ru") String ru) throws Exception { 	
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario)    		
    		return bitacoraService.reporteAcciones(request, (Usuario) datosSession, ru);
    	else
    		return datosSession;
    }
    
    @RequestMapping(value = "/reporte/modificaciones/pdf/ru/{ru}", method = RequestMethod.GET, produces = "application/pdf")
    public @ResponseBody
    Object reporteModificaciones(HttpServletRequest request,@PathVariable("ru") String ru) throws Exception { 	
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario)    		
    		return bitacoraService.reporteModificaciones(request, (Usuario) datosSession, ru);
    	else
    		return datosSession;
    }
    
    @RequestMapping(value="/seguimiento", method = RequestMethod.GET)
    public @ResponseBody
    List getSeguimiento(@QueryParam("fichaDocumentoId") String fichaDocumentoId) throws Exception {
        return bitacoraService.getSeguimiento(fichaDocumentoId);
    }
        
}
