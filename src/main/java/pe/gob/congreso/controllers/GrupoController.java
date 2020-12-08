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

import pe.gob.congreso.dao.CanalAsociadoEnvioDao;
import pe.gob.congreso.model.Grupo;
import pe.gob.congreso.model.MpCanalAsociadoEnvio;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.service.GrupoService;
import pe.gob.congreso.service.UsuarioService;
import pe.gob.congreso.util.Constantes;

@Controller
@RequestMapping(Urls.Grupo.BASE)
public class GrupoController {

    @Autowired
    GrupoService grupoService;
    
    @Autowired
    UsuarioService usuarioService;
    
    @Autowired
    CanalAsociadoEnvioDao canalAsociadoEnvioDao;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List getGrupos() throws Exception {
        return grupoService.findBy();
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public @ResponseBody
    List getGruposUtil(HttpServletRequest request) throws Exception {
    	List<InputSelectUtil> grupos = grupoService.find(); 
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario) {
    		Usuario usuario = (Usuario) datosSession;
	    	if (Constantes.PERFIL_RESPONSABLE_MESA_PARTES.equals(usuario.getPerfil().getId().trim())
	    		|| Constantes.PERFIL_OPERADOR_MESA_PARTES.equals(usuario.getPerfil().getId().trim()) ) {
	    	    for (int i = grupos.size() - 1; i >= 0; i--) {
	    	    	List<MpCanalAsociadoEnvio> canales = canalAsociadoEnvioDao.getCanalAsociado(grupos.get(i).getValue().toString(), "", "");
	    			if ( canales.isEmpty() ) {
	    				grupos.remove(i);
	    			}
	    	    }	    		
	    	}
	    	return grupos;
    	} else {
    		return 	grupos;
    	}
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Grupo getGrupoId(@PathVariable("id") String id) throws Exception {
        return grupoService.getGrupoId(id);
    }
    
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object createGrupo(HttpServletRequest request,@RequestBody  Grupo grupo, @QueryParam("operacion") String operacion) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario)
    		return grupoService.create((Usuario) datosSession, grupo, operacion);
    	else
    		return datosSession;
    }
}
