package pe.gob.congreso.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import pe.gob.congreso.dao.TipoDao;
import pe.gob.congreso.dao.UsuarioDao;
import pe.gob.congreso.dao.UsuarioSesionDao;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.UsuarioSesion;
import pe.gob.congreso.service.ActividadService;
import pe.gob.congreso.service.AuthService;
import pe.gob.congreso.util.ResponseRedirect;
import pe.gob.congreso.util.SignIn;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service("authService")
@Transactional
@PropertySource(value = { "classpath:application.properties" })
public class AuthServiceImpl implements AuthService {

    @Autowired
    UsuarioSesionDao usuarioSesionDao;

    @Autowired
    TipoDao tipoDao;

    @Autowired
    UsuarioDao usuarioDao;

    @Autowired
    ActividadService actividadService;

    @Autowired
    private Environment environment;

    protected final Log log = LogFactory.getLog(getClass());
    private String mensajeError = "ok";

    @Override
    public List<UsuarioSesion> findAll() throws Exception {
        return usuarioSesionDao.findAll();
    }

    @Override
    public String signIn(HttpServletRequest request, Model model) throws Exception {
        String response = "login";
        String ipAddress = Optional.fromNullable(request.getHeader("X-FORWARDED-FOR")).or(request.getRemoteAddr());
        String authorizationKey = Optional.fromNullable(request.getHeader("AUTHORIZATION")).or("");
        SignIn signIn = new SignIn();
        signIn.decodeAuthorizationKey(authorizationKey);

        final String uri = environment.getRequiredProperty("sgi.url") + "/usuarios/validar?usuario="
                + signIn.getUsuario() + "&sessionKey=" + signIn.getSessionKey() + "&ipAddress=" + ipAddress;
        RestTemplate restTemplate = new RestTemplate();
        Status result = restTemplate.getForObject(uri, Status.class);

        if (result.getCode() == 200) {
            String sessionId = request.getSession().getId();
            Optional<UsuarioSesion> s = createSesion(signIn.getUsuario(), sessionId);
            if (s.isPresent()) {
                s.get().getUsuario().setIpAddress(ipAddress);
                actividadService.create(s.get().getUsuario(), s.get(), "CREAR");
                Optional<Usuario> usuariosga = usuarioDao.findByNameUsuario(signIn.getUsuario());
                request.getSession(true).setAttribute("usuario", usuariosga);
                ResponseRedirect responseRedirect = new ResponseRedirect();
                responseRedirect.setUrl(environment.getRequiredProperty("std.url"));
                ObjectMapper mapper = new ObjectMapper();
                model.addAttribute("responseRedirect", mapper.writeValueAsString(responseRedirect));
                response = "responseRedirect";
            } else {
                response = "login";
            }
        } else {
            response = "login";
        }
        return response;
    }

    private Optional<UsuarioSesion> createSesion(String username, String sessionId) throws Exception {
        Optional<Usuario> optionalUser = usuarioDao.findByNameUsuario(username);
        UsuarioSesion newSesion = null;
        if (optionalUser.isPresent()) {
            Usuario user = optionalUser.get();
            if (user.getHabilitado() == 1) {
                newSesion = usuarioSesionDao.create(user, sessionId);
                log.info("Sesion key:" + newSesion.getSesionKey());
            } else {
                this.mensajeError = "Usuario SIGA está Inactivo";
            }
        } else {
            this.mensajeError = "Usuario no tiene permisos en el SIGA para acceder al Sistema";
        }
        return Optional.fromNullable(newSesion);
    }

    @Override
    public String signOut(HttpServletRequest request) throws Exception {
        CacheManager cm = CacheManager.getInstance();
        Cache cache = cm.getCache("adjuntoscache");
        cache.removeAll();

        String response = "login";
        String sesionKey = request.getSession().getId();
        log.info("X-FORWARDED-FOR: " + request.getHeader("X-FORWARDED-FOR"));
        System.out.println("X-FORWARDED-FOR: " + request.getHeader("X-FORWARDED-FOR"));
        log.info("remote ip: " + request.getRemoteAddr());
        System.out.println("remote ip: " + request.getRemoteAddr());
        String ipAddress = Optional.fromNullable(request.getHeader("X-FORWARDED-FOR")).or(request.getRemoteAddr());
        log.info("ipAddress: " + ipAddress);
        System.out.println("ipAddress: " + ipAddress);
        if (Optional.fromNullable(sesionKey).isPresent()) {
            Optional<Usuario> optionalUser = (Optional<Usuario>) request.getSession().getAttribute("usuario");
            if (Optional.fromNullable(optionalUser).isPresent()) {
                Usuario usuario = optionalUser.get();
                usuario.setIpAddress(ipAddress);
                actividadService.create(usuario, usuario, "ELIMINAR");
                request.getSession().removeAttribute("usuario");
            }

            request.getSession().invalidate();
        }

        return response;
    }

}
