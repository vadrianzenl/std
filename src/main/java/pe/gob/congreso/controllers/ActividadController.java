package pe.gob.congreso.controllers;

import com.google.common.base.Optional;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.ActividadService;
import pe.gob.congreso.service.UsuarioService;

@Controller
@RequestMapping(Urls.Actividad.BASE)
public class ActividadController {

    protected final Log log = LogFactory.getLog(getClass());

    @Autowired
    ActividadService actividadService;

    @Autowired
    UsuarioService usuarioService;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List getActividades(@QueryParam("usuarioId") Integer usuarioId,
            @QueryParam("nombreTabla") String nombreTabla, @QueryParam("operacion") String operacion) throws Exception {
        return actividadService.findBy(Optional.fromNullable(usuarioId), Optional.fromNullable(nombreTabla),
                Optional.fromNullable(operacion));
    }

    @RequestMapping(value = "/auditar", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object auditar(HttpServletRequest request, @RequestBody Object obj) throws Exception {
        log.info(obj.toString());
        Object datosSession = usuarioService.getUsuarioInfo(request);
        if (datosSession instanceof Usuario)
            return actividadService.auditar(obj);
        else
            return datosSession;
    }
}
