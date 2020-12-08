package pe.gob.congreso.controllers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
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

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import pe.gob.congreso.model.Deriva;
import pe.gob.congreso.model.GrupoCentroCosto;
import pe.gob.congreso.model.Tipo;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.CanalAsociadoEnvioService;
import pe.gob.congreso.service.GrupoCentroCostoService;
import pe.gob.congreso.service.TipoService;
import pe.gob.congreso.service.UsuarioService;
import pe.gob.congreso.util.Constantes;

@Controller
@RequestMapping(Urls.GrupoCentroCosto.BASE)
public class GrupoCentroCostoController {

    @Autowired
    GrupoCentroCostoService grupoCentroCostoService;
    
    @Autowired
    UsuarioService usuarioService;
    
    @Autowired
    CanalAsociadoEnvioService canalAsociadoEnvioService;
    
    @Autowired
    TipoService tipoService;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Map getCentroCostoActuals(@QueryParam("descripcion") String descripcion, @QueryParam("pag") String pag, @QueryParam("pagLength") String pagLength) throws Exception {
        return grupoCentroCostoService.findBy(Optional.fromNullable(descripcion), Optional.fromNullable(pag), Optional.fromNullable(pagLength));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    GrupoCentroCosto getCentroCostoActual(@PathVariable("id") String id) throws Exception {
        return grupoCentroCostoService.getCentroCostoActual(id);
    }

    @RequestMapping(value = "/grupo/{grupoId}", method = RequestMethod.GET)
    public @ResponseBody
    List getCentroCostoActualByGrupo(@PathVariable("grupoId") String grupoId) throws Exception {
        return grupoCentroCostoService.getCentrosCostoByGrupo(grupoId);
    }

    @RequestMapping(value = "/grupo/{grupoId}/centrosCosto", method = RequestMethod.GET)
    public @ResponseBody
    List getCentrosCosto(@PathVariable("grupoId") String grupoId) throws Exception {
        return grupoCentroCostoService.getCentrosCosto(grupoId);
    }
    
    @RequestMapping(value = "/grupoNotIn/{grupoIds}/centrosCosto", method = RequestMethod.GET)
    public @ResponseBody
    List getCentrosCostoNotInGroup(@PathVariable("grupoIds") String[] grupoIds) throws Exception {
        return grupoCentroCostoService.getCentrosCostoByGrupoNotIn(grupoIds);
    }

    @RequestMapping(value = "/inputSelect", method = RequestMethod.GET)
    public @ResponseBody
    List getCentroCostoActualInputSelect() throws Exception {
        return grupoCentroCostoService.getCentroCostoActualInputSelect();
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public @ResponseBody
    Map getCentroCostoActualFind(@QueryParam("descripcion") String grupo, @QueryParam("descripcion") String dependencia, @QueryParam("pag") String pag, @QueryParam("pagLength") String pagLength) throws Exception {
        return grupoCentroCostoService.find(Optional.fromNullable(grupo), Optional.fromNullable(dependencia), Optional.fromNullable(pag), Optional.fromNullable(pagLength));
    }

    @RequestMapping(value = "/finds", method = RequestMethod.GET)
    public @ResponseBody
    List getGrupoCentroCosto() throws Exception {
        return grupoCentroCostoService.getGrupoCentroCosto();
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object createGrupoCentroCosto(HttpServletRequest request,@RequestBody GrupoCentroCosto gcc, @QueryParam("operacion") String operacion) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario) {
    		Usuario usuario = (Usuario) datosSession;
    		Object obj = grupoCentroCostoService.create(usuario, gcc, operacion);
    		canalAsociadoEnvioService.create(gcc, operacion);
    		tipoService.crearTipoCasillero(usuario, gcc, operacion);    		
    		return obj; 
    	} else {
    		return datosSession;
    	}
    }
}
