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

import com.google.common.base.Optional;

import pe.gob.congreso.model.CentroCostoSubCategoria;
import pe.gob.congreso.model.SubCategoria;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.SubCategoriaService;
import pe.gob.congreso.service.UsuarioService;

@Controller
@RequestMapping(Urls.SubCategoria.BASE)
public class SubCategoriaController {

    @Autowired
    SubCategoriaService subCategoriaService;
    
    @Autowired
    UsuarioService usuarioService;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List getSubCategorias() throws Exception {
        return subCategoriaService.findBy(Optional.fromNullable(""));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    SubCategoria getSubCategoriaId(@PathVariable("id") Integer id) throws Exception {
        return subCategoriaService.getSubCategoriaId(id);
    }

    @RequestMapping(value = "/categoria/{categoriaId}", method = RequestMethod.GET)
    public @ResponseBody
    List getSubcategoriaCategoria(@PathVariable("categoriaId") String categoriaId) throws Exception {
        return subCategoriaService.findBy(Optional.fromNullable(categoriaId));
    }

    @RequestMapping(value = "/categoria/{categoriaId}/inputSelect", method = RequestMethod.GET)
    public @ResponseBody
    List getSubCategoriasInputSelect(@PathVariable("categoriaId") String categoriaId) throws Exception {
        return subCategoriaService.getSubCategoríasInputSelect(Optional.fromNullable(categoriaId));
    }

    @RequestMapping(value = "/centroCosto/{centroCostoId}", method = RequestMethod.GET)
    public @ResponseBody
    List getSubCategoriasByCC(@PathVariable("centroCostoId") String centroCostoId) throws Exception {
        return subCategoriaService.getSubCategoriasByCC(centroCostoId);
    }

    @RequestMapping(value = "/centroCosto", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object CentroCostoSubCategoria(HttpServletRequest request, @RequestBody CentroCostoSubCategoria t, @QueryParam("operacion") String operacion) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario)
    		return subCategoriaService.createCentroCostoSubCategoria((Usuario) datosSession, t, operacion);
    	else
    		return datosSession;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object createCategoria(HttpServletRequest request, @RequestBody SubCategoria t, @QueryParam("operacion") String operacion) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario)
    		return subCategoriaService.createCategoria((Usuario) datosSession, t, operacion);
    	else
    		return datosSession;
    }
}
