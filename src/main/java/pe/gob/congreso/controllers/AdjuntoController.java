package pe.gob.congreso.controllers;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.gob.congreso.model.Adjunto;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.AdjuntoService;
import pe.gob.congreso.service.UsuarioService;

@Controller
@RequestMapping(Urls.Adjunto.BASE)
public class AdjuntoController {

    @Autowired
    AdjuntoService adjuntoService;

    @Autowired
    UsuarioService usuarioService;

    protected final Log log = LogFactory.getLog(getClass());

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public @ResponseBody Object getCache() throws Exception {
        List<Adjunto> list = adjuntoService.getAdjuntosCache();

        return list;
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Object getAdjuntos(@QueryParam("fichaDocumentoId") String fichaDocumentoId) throws Exception {
        List<Adjunto> list = adjuntoService.findAdjuntos(fichaDocumentoId);
        adjuntoService.setAdjuntosCache(list);

        return list;
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody Object createAdjunto(HttpServletRequest request, @RequestBody Adjunto a,
            @QueryParam("operacion") String operacion) throws Exception {
        Object datosSession = usuarioService.getUsuarioInfo(request);
        if (datosSession instanceof Usuario)

            return adjuntoService.createAdjunto((Usuario) datosSession, a, operacion);
        else
            return datosSession;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA)
    public @ResponseBody Object uploadAdjunto(@FormDataParam("file") InputStream inputStream,
            @QueryParam("nameFile") String nameFile) throws Exception {
        return adjuntoService.uploadAdjunto(inputStream, nameFile);
    }

    @RequestMapping(value = "/pdf/{documento:.+}", method = RequestMethod.GET, produces = "application/pdf")
    public @ResponseBody Object getAdjunto(HttpServletRequest request, @PathVariable("documento") String documento)
            throws Exception {
        Object datosSession = usuarioService.getUsuarioInfo(request);
        if (datosSession instanceof Usuario) {
            Adjunto adjunto = adjuntoService.getAdjuntoCache(documento);

            if (adjunto != null) {
                System.out.println("tiene acceso al pdf: " + adjunto.getNombreArchivo());
                return adjuntoService.getAdjunto(request, documento);
            } else {
                System.out.println("no tiene acceso al pdf");
                return adjuntoService.showPdfUnAuthorized(request);
            }
        } else {
            return adjuntoService.showPdfUnAuthorized(request);
        }
    }

    // @RequestMapping(value = "/pdf/{documento:.+}", method = RequestMethod.GET,
    // produces = "application/pdf")
    // public @ResponseBody Object getAdjunto(HttpServletRequest request,
    // @PathVariable("documento") String documento)
    // throws Exception {
    // Object datosSession = usuarioService.getUsuarioInfo(request);
    // if (datosSession instanceof Usuario) {
    // Usuario usu = (Usuario) datosSession;
    // String cc = usu.getEmpleado().getCentroCosto().getId();
    // Integer empId = usu.getEmpleado().getId();
    // String perfil = usu.getPerfil().getId();

    // System.out.println("centro de costros: " + cc);
    // System.out.println("perfil: " + perfil);
    // System.out.println("codigo de empleado: " + empId);
    // System.out.println("documento: " + documento);

    // Boolean isAccessPdf = adjuntoService.checkAccess(documento, cc, empId,
    // perfil.trim());
    // System.out.println("tiene acceso al pdf: " + isAccessPdf);

    // if (isAccessPdf) {
    // return adjuntoService.getAdjunto(request, documento);
    // } else {
    // return adjuntoService.showPdfUnAuthorized(request);
    // }
    // } else {
    // return adjuntoService.showPdfUnAuthorized(request);
    // }
    // }
}
