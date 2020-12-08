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

import pe.gob.congreso.model.Notificacion;
import pe.gob.congreso.model.NotificacionEmpleado;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.NotificacionService;
import pe.gob.congreso.service.UsuarioService;

@Controller
@RequestMapping(Urls.Notificacion.BASE)
public class NotificacionController {

    @Autowired
    NotificacionService notificacionService;

    @Autowired
    UsuarioService usuarioService;
    
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List getNotificaciones() throws Exception {
        return notificacionService.findBy();
    }

    @RequestMapping(value = "/empleado", method = RequestMethod.GET)
    public @ResponseBody
    List getNotificacionesEmpleado(@QueryParam("idEmpleado") String idEmpleado) throws Exception {
        return notificacionService.findBy(idEmpleado);
    }
    
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public @ResponseBody
    Object getNotificacion(@QueryParam("idEmpleado") String idEmpleado,@QueryParam("idNotificacion") String idNotificacion) throws Exception {
        return notificacionService.findBy(idEmpleado,idNotificacion);
    }
    
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object createNotificacionEmpleado(HttpServletRequest request,@RequestBody NotificacionEmpleado t, @QueryParam("operacion") String operacion) throws Exception {
    	Object datosSession = usuarioService.getUsuarioInfo(request);
    	if (datosSession instanceof Usuario)
    		return notificacionService.create((Usuario) datosSession, t, operacion);
    	else
    		return datosSession;
    }

   
}
