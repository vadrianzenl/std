package pe.gob.congreso.controllers;

import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.core.XDocReportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pe.gob.congreso.service.TemplateService;
import pe.gob.congreso.service.UsuarioService;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.impl.Status;
import org.apache.log4j.NDC;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pe.gob.congreso.util.Constantes;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
@RequestMapping(Urls.Template.BASE)
public class TemplateController {

    @Autowired
    TemplateService templateService;

    @Autowired
    UsuarioService usuarioService;

    protected final Log log = LogFactory.getLog(getClass());

    @RequestMapping(value = "/open", method = RequestMethod.GET)
    public @ResponseBody
    Object openTemplate(HttpServletRequest request, @QueryParam("fichaDocumentoId") String fichaDocumentoId) throws FileNotFoundException, XDocConverterException, IOException, Exception {
        Object datosSession = usuarioService.getUsuarioInfo(request);
        Status resultado=new Status(200,"OK");
        if (datosSession instanceof Usuario) {
            Usuario usuario = (Usuario) datosSession;
            NDC.push(usuario.getNombreUsuario().trim());
            try {
                resultado = (Status) templateService.openTemplate(usuario, Integer.valueOf(fichaDocumentoId));
            } catch (Exception e) {
                log.error(e, e);
            } finally {
                NDC.pop();
                NDC.remove();
            }
            return resultado;
        } else {
            return datosSession;
        }
    }

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public @ResponseBody
    Object uploadTemplate(HttpServletRequest request, @QueryParam("fichaDocumentoId") String fichaDocumentoId) throws FileNotFoundException, XDocReportException, IOException, Exception {
        Object datosSession = usuarioService.getUsuarioInfo(request);
        Status resultado=new Status(200,"OK");
        if (datosSession instanceof Usuario) {
            Usuario usuario = (Usuario) datosSession;
            NDC.push(usuario.getNombreUsuario().trim());
            //Comprobamos si el estado anterior ya ha sido enviado para no volver a dirigirlo
            try {
                resultado = (Status) templateService.uploadTemplate(usuario, Integer.valueOf(fichaDocumentoId));
            } catch (Exception e) {
                log.error(e, e);
            } finally {
                NDC.pop();
                NDC.remove();
            }
            return resultado;
        } else {
            return datosSession;
        }
    }

    @RequestMapping(value="/pdf" ,method = RequestMethod.GET, produces= Constantes.MIME_TYPE_PDF)
    public @ResponseBody
    Object getPDFDocumento(HttpServletRequest request, @QueryParam("fichaDocumentoId") String fichaDocumentoId) throws Exception {
        return templateService.getPDFDocumento(request, Integer.valueOf(fichaDocumentoId));
    }

}
