package pe.gob.congreso.controllers;

import javax.servlet.http.HttpServletRequest;
import com.google.common.base.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

@Controller
@RequestMapping(Urls.ROOT)
public class ViewController {

    @RequestMapping(method = RequestMethod.GET)
    public String root(HttpServletRequest request) {
        Object nombreUsuario = request.getSession().getAttribute("usuario");
        if (Optional.fromNullable(nombreUsuario).isPresent()) {
            return "main";
        } else {
            return "login";
        }
    }

    @RequestMapping(value = Urls.View.SIGNIN, method = RequestMethod.GET)
    public String signin(HttpServletRequest request) {
        Object nombreUsuario = request.getSession().getAttribute("usuario");
        if (Optional.fromNullable(nombreUsuario).isPresent()) {
            CacheManager cm = CacheManager.getInstance();
            Cache cache = cm.getCache("adjuntoscache");
            cache.removeAll();
            return "main";
        } else {
            return "login";
        }
    }

    @RequestMapping(value = Urls.View.MAIN, method = RequestMethod.GET)
    public String main(HttpServletRequest request) {
        Object nombreUsuario = request.getSession().getAttribute("usuario");
        if (Optional.fromNullable(nombreUsuario).isPresent()) {
            return "main";
        } else {
            return "login";
        }
    }

    @RequestMapping(value = Urls.View.VIEWER_PDF, method = RequestMethod.GET)
    public String viewer(HttpServletRequest request) {
        Object nombreUsuario = request.getSession().getAttribute("usuario");
        if (Optional.fromNullable(nombreUsuario).isPresent()) {
            return "viewer";
        } else {
            return "login";
        }
    }
}
