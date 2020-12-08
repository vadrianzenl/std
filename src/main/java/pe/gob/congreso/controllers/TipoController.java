package pe.gob.congreso.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Optional;

import pe.gob.congreso.model.Tipo;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.TipoService;
import pe.gob.congreso.service.UsuarioService;

@Controller
@RequestMapping(Urls.Tipo.BASE)
public class TipoController {

    @Autowired
    TipoService tipoService;

    @Autowired
    UsuarioService usuarioService;
    
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List getTipos(@QueryParam("id") String id, @QueryParam("nombre") String nombre) throws Exception {
        return tipoService.findBy(Optional.fromNullable(id), Optional.fromNullable(nombre));
    }

    @RequestMapping(value = "/orden", method = RequestMethod.GET)
    public @ResponseBody
    List getTiposOrden(@QueryParam("id") String id, @QueryParam("nombre") String nombre) throws Exception {
        return tipoService.findByOrden(Optional.fromNullable(id), Optional.fromNullable(nombre));
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public @ResponseBody
    Map getTiposFind(@QueryParam("nombre") String nombre, @QueryParam("descripcion") String descripcion, @QueryParam("pag") String pag, @QueryParam("pagLength") String pagLength) throws Exception {
        return tipoService.find(Optional.fromNullable(nombre), Optional.fromNullable(descripcion), Optional.fromNullable(pag), Optional.fromNullable(pagLength));
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object createTipo(HttpServletRequest request,@RequestBody Tipo t, @QueryParam("operacion") String operacion) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario)
    		return tipoService.create((Usuario) datosSession, t, operacion);
    	else
    		return datosSession;
    }

    @RequestMapping(value = "/inputSelect", method = RequestMethod.GET)
    public @ResponseBody
    List getTiposInputSelect() throws Exception {
        return tipoService.getTiposInputSelect();
    }

    @RequestMapping(value = "/validar", method = RequestMethod.GET)
    public @ResponseBody
    Tipo getValidaTipo(@QueryParam("id") String id) throws Exception {
        return tipoService.findByCodigo(id);
    }

    @RequestMapping(value = "/findAlfresco", method = RequestMethod.GET)
    public @ResponseBody
    List findByAlfresco() throws Exception {
        return tipoService.findByAlfresco();
    }

    @RequestMapping(value = "/findCorreo", method = RequestMethod.GET)
    public @ResponseBody
    List findByCorreo() throws Exception {
        return tipoService.findByCorreo();
    }
}
