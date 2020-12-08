package pe.gob.congreso.controllers;

import com.google.common.base.Optional;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.UsuarioService;

@Controller
@RequestMapping(Urls.Usuario.BASE)
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Usuario getUsuarios(@QueryParam("nombre") String nombre) throws Exception {
        return usuarioService.findByNameUsuario(nombre);
    }
    
    @RequestMapping(value = Urls.Usuario.MODULES, method = RequestMethod.GET)
    public @ResponseBody
    List getModules(@QueryParam("perfilId") String perfilId) throws Exception {
        return usuarioService.findModules(perfilId);
    }
    
    @RequestMapping(value = Urls.Usuario.MENUS, method = RequestMethod.GET)
    public @ResponseBody
    List getMenus(@QueryParam("perfilId") String perfilId, @QueryParam("padreId") String padreId) throws Exception {
        return usuarioService.findMenus(perfilId, padreId);
    }
    
    @RequestMapping(value = Urls.Usuario.ME, method = RequestMethod.GET)
    public @ResponseBody
    Object getUsuarioInfo(HttpServletRequest request) throws Exception {
    	System.out.println("AEP--Dentro de UsuarioController - getUsuarioInfo(HttpServletRequest request)");
    	System.out.println("AEP--request.getRequestURI() : " + request.getRequestURI() );
        return usuarioService.getUsuarioInfo(request);
    }
    
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public @ResponseBody
    Object updateUsuarioInfo(HttpServletRequest request) throws Exception {
        return usuarioService.updateUsuarioInfo(request);
    }
    
}
