package pe.gob.congreso.controllers;

import java.util.List;
import java.util.Map;

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

import pe.gob.congreso.model.Categoria;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.CategoriaService;
import pe.gob.congreso.service.UsuarioService;

@Controller
@RequestMapping(Urls.Categoria.BASE)
public class CategoriaController {

    @Autowired
    CategoriaService categoriaService;
    
    @Autowired
    UsuarioService usuarioService;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List getCategorias() throws Exception {
        return categoriaService.getCategorias();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Object getCategoriaId(@PathVariable("id") String id) throws Exception {
        return categoriaService.getCategoriaId(id);
    }

    @RequestMapping(value = "/inputSelect", method = RequestMethod.GET)
    public @ResponseBody
    List getCategoriasInputSelect() throws Exception {
        return categoriaService.getCategoriasInputSelect();
    }

    @RequestMapping(value = "/centroCosto/{centroCostoId}", method = RequestMethod.GET)
    public @ResponseBody
    Map getCategoriaCentroCosto(@PathVariable("centroCostoId") String centroCostoId) throws Exception {
        return categoriaService.getCategoriaCentroCosto(centroCostoId);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object createCategoria(HttpServletRequest request, @RequestBody Categoria a, @QueryParam("operacion") String operacion) throws Exception {
    	
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario)
    		return categoriaService.createCategoria((Usuario) datosSession, a, operacion);
    	else
    		return datosSession;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object editCategoria(HttpServletRequest request, @RequestBody Categoria a, @QueryParam("operacion") String operacion) throws Exception {
    	
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario)    	
    		return categoriaService.editCategoria((Usuario) datosSession, a, operacion);
    	else
    		return datosSession;
    }

}
