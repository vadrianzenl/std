
package pe.gob.congreso.controllers;

import java.util.ArrayList;
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

import pe.gob.congreso.model.Notas;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.NotasService;
import pe.gob.congreso.service.UsuarioService;
import pe.gob.congreso.util.Constantes;

@Controller
@RequestMapping(Urls.Notas.BASE)
public class NotasController {
    
    @Autowired
    NotasService notasService;
    
    @Autowired
    UsuarioService usuarioService;
    
    @RequestMapping(value = "/id/{fichaDocumentoId}",method = RequestMethod.GET)
    public @ResponseBody
    List getNotas(HttpServletRequest request, @PathVariable("fichaDocumentoId") String fichaDocumentoId) throws Exception {
    	List<Notas> notasVacias = new ArrayList<>();
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario)
    		return notasService.findNotas((Usuario) datosSession, fichaDocumentoId);
    	else 
    		return notasVacias;
    }
    
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object createNotas(HttpServletRequest request,@RequestBody Notas b, @QueryParam("operacion") String operacion) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario) {
    		if ( Constantes.OPERACION_ACTUALIZAR.equals(operacion.trim()) ) {
    			notasService.createHistoNotas(b);
    		}
    		return notasService.create((Usuario) datosSession, b, operacion);

    	} else {
    		return datosSession;
    	}
    }
    
        
}
