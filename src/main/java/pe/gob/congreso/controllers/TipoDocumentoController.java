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

import pe.gob.congreso.model.TipoDocumento;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.TipoDocumentoService;
import pe.gob.congreso.service.UsuarioService;

@Controller
@RequestMapping(Urls.TipoDocumento.BASE)
public class TipoDocumentoController {

    @Autowired
    TipoDocumentoService tipoDocumentoService;
    
    @Autowired
    UsuarioService usuarioService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    TipoDocumento getTipoDocumentoId(@PathVariable("id") Integer id) throws Exception {
        return tipoDocumentoService.getTipoDocumentoId(id);
    }

    @RequestMapping(value = "/centroCosto/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List getTipoDocumentoCentroCosto(@PathVariable("id") String id) throws Exception {
        return tipoDocumentoService.getTipoDocumentoCentroCosto(id);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object createCentroCostoTipoDocumento(HttpServletRequest request, @RequestBody TipoDocumento t, @QueryParam("operacion") String operacion) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario)
    		return tipoDocumentoService.create((Usuario) datosSession, t, operacion);
    	else
    		return datosSession;
    }
}
