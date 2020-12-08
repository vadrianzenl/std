package pe.gob.congreso.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pe.gob.congreso.service.AuthService;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(Urls.Auth.BASE)
public class AuthController {

    @Autowired
    AuthService authService;

    protected final Log log = LogFactory.getLog(getClass());

    @RequestMapping(value = Urls.Auth.SESSIONS, method = RequestMethod.GET)
    public @ResponseBody
    List getSessions() throws Exception {
        return authService.findAll();
    }

    @RequestMapping(value = Urls.Auth.SIGNIN, method = RequestMethod.GET)
    public String signIn(HttpServletRequest request, Model model) throws Exception {
        return authService.signIn(request, model);
    }

    @RequestMapping(value = Urls.Auth.SIGNOUT, method = RequestMethod.GET)
    public String signOut(HttpServletRequest request) throws Exception{
        return authService.signOut(request);
    }

}
