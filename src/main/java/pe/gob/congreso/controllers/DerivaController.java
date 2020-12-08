package pe.gob.congreso.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.NDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Optional;

import pe.gob.congreso.model.Deriva;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.DerivaService;
import pe.gob.congreso.service.FichaDocumentoService;
import pe.gob.congreso.service.UsuarioService;
import pe.gob.congreso.util.Constantes;
import pe.gob.congreso.util.JsonUtils;

@Controller
@RequestMapping(Urls.Deriva.BASE)
public class DerivaController {

    protected final Log log = LogFactory.getLog(getClass());

    @Autowired
    DerivaService derivaService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    FichaDocumentoService fichaDocumentoService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public @ResponseBody List test() throws Exception {
        return derivaService.test();
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List getDerivados(@QueryParam("fichaDocumentoId") String fichaDocumentoId) throws Exception {
        return derivaService.findDerivados(fichaDocumentoId);
    }

    @RequestMapping(value = "/dirigidos", method = RequestMethod.GET)
    public @ResponseBody List getDirigidos(@QueryParam("fichaDocumentoId") String fichaDocumentoId) throws Exception {
        return derivaService.findDirigidos(fichaDocumentoId);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object createDeriva(HttpServletRequest request, @RequestBody Deriva d,
            @QueryParam("operacion") String operacion) throws Exception {
        Object datosSession = usuarioService.getUsuarioInfo(request);
        if (datosSession instanceof Usuario) {
            Usuario usuario = (Usuario) datosSession;
            NDC.push(usuario.getNombreUsuario().trim());
            // Comprobamos si el estado anterior ya ha sido enviado para no volver a
            // dirigirlo
            derivaService.compruebaCambiosValidos(d);
            Object resultado = new Object();
            try {
                resultado = derivaService.create(usuario, d, operacion);
            } catch (Exception e) {
                log.error(e, e);
            } finally {
                NDC.pop();
                NDC.remove();
            }
            return resultado;
        } else
            return datosSession;
    }

    @RequestMapping(value = "/recibido", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object createDeriva(@RequestBody Deriva d) throws Exception {
        return derivaService.create(d);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object editDerivaRespuesta(HttpServletRequest request, @RequestBody Deriva d,
            @QueryParam("operacion") String operacion) throws Exception {
        Object datosSession = usuarioService.getUsuarioInfo(request);
        if (datosSession instanceof Usuario) {
            Usuario usuario = (Usuario) datosSession;
            NDC.push(usuario.getNombreUsuario().trim());
            Object resultado = new Object();

            try {
                resultado = derivaService.editDerivaRespuesta(usuario, d, operacion);
            } catch (Exception e) {
                log.error(e, e);
            } finally {
                NDC.pop();
                NDC.remove();
            }

            return resultado;
        } else
            return datosSession;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody Object getDerivado(@PathVariable("id") String id) throws Exception {
        return derivaService.findDerivado(id);
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public @ResponseBody Map getFichaDocumentos(HttpServletRequest request,
            @QueryParam("tipoRegistro") String tipoRegistro, @QueryParam("empleadoId") String empleadoId,
            @QueryParam("id") String id, @QueryParam("numeroDoc") String numeroDoc, @QueryParam("asunto") String asunto,
            @QueryParam("fechaIniCrea") String fechaIniCrea, @QueryParam("fechaFinCrea") String fechaFinCrea,
            @QueryParam("tipoDocumento") String tipoDocumento, @QueryParam("tipoEstado") String tipoEstado,
            @QueryParam("centroCosto") String centroCosto, @QueryParam("privado") String privado,
            @QueryParam("remitidoDes") String remitidoDes, @QueryParam("texto") String texto,
            @QueryParam("referencia") String referencia, @QueryParam("observaciones") String observaciones,
            @QueryParam("esResponsable") String esResponsable, @QueryParam("esRecibido") String esRecibido,
            @QueryParam("pag") String pag, @QueryParam("pagLength") String pagLength,
            @QueryParam("numeroMp") String numeroMp, @QueryParam("dependenciaId") String dependenciaId,
            @QueryParam("anioLegislativo") String anioLegislativo, @QueryParam("recibiConforme") String recibiConforme)
            throws Exception {

        System.out.println("recibiConforme: " + recibiConforme);

        Object datosSession = usuarioService.getUsuarioInfo(request);
        Map<String, Object> result = new HashMap<String, Object>();
        if (datosSession instanceof Usuario) {
            Usuario usuario = (Usuario) datosSession;
            String perfil = usuario.getPerfil().getId().trim();
            // perfil = "STD-RESMEP";

            if (Constantes.PERFIL_RESPONSABLE_MESA_PARTES.equals(perfil)
                    || Constantes.PERFIL_OPERADOR_MESA_PARTES.equals(perfil)) {
                pagLength = Constantes.PAGINADO_MESA_PARTES;
                result = derivaService.getFichaDerivaEstafeta(Optional.fromNullable(tipoRegistro),
                        Optional.fromNullable(dependenciaId),
                        Optional.fromNullable(empleadoId != null ? empleadoId : Constantes.VACIO),
                        Optional.fromNullable(id), Optional.fromNullable(numeroDoc), Optional.fromNullable(numeroMp),
                        Optional.fromNullable(asunto), Optional.fromNullable(centroCosto),
                        Optional.fromNullable(Constantes.VACIO), Optional.fromNullable(fechaIniCrea),
                        Optional.fromNullable(fechaFinCrea), Optional.fromNullable(tipoDocumento),
                        Optional.fromNullable(tipoEstado), Optional.fromNullable(pag), Optional.fromNullable(pagLength),
                        Optional.fromNullable(remitidoDes));
            } else {
                result = derivaService.findBy(usuario, Optional.fromNullable(tipoRegistro),
                        Optional.fromNullable(empleadoId), Optional.fromNullable(id), Optional.fromNullable(numeroDoc),
                        Optional.fromNullable(asunto), Optional.fromNullable(fechaIniCrea),
                        Optional.fromNullable(fechaFinCrea), Optional.fromNullable(tipoDocumento),
                        Optional.fromNullable(tipoEstado), Optional.fromNullable(centroCosto),
                        Optional.fromNullable(privado), Optional.fromNullable(remitidoDes),
                        Optional.fromNullable(texto), Optional.fromNullable(referencia),
                        Optional.fromNullable(observaciones), Optional.fromNullable(esResponsable),
                        Optional.fromNullable(esRecibido), Optional.fromNullable(pag), Optional.fromNullable(pagLength),
                        Constantes.NO_ES_MESA_DE_PARTES, Optional.fromNullable(numeroMp),
                        Optional.fromNullable(dependenciaId), Optional.fromNullable(anioLegislativo),
                        Optional.fromNullable(recibiConforme));
            }
        }

        System.out.println("cantidad: " + result.size());
        return result;
    }

    @RequestMapping(value = "/validarDevolver", method = RequestMethod.GET)
    public @ResponseBody Object validarDevolver(@QueryParam("id") String id,
            @QueryParam("empleadoId") String empleadoId, @QueryParam("idDeriva") String idDeriva) throws Exception {
        return derivaService.validarDevolver(id, empleadoId, idDeriva);
    }

    @RequestMapping(value = "/devolver", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object devolverDerivado(HttpServletRequest request, @RequestBody Deriva d,
            @QueryParam("operacion") String operacion, @QueryParam("motivo") String motivo) throws Exception {

        Object datosSession = usuarioService.getUsuarioInfo(request);
        if (datosSession instanceof Usuario) {
            return derivaService.devolverDerivado((Usuario) datosSession, d, operacion, motivo);
        } else
            return datosSession;
    }

    @RequestMapping(value = "/leer", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object leerDerivado(HttpServletRequest request, @RequestBody Deriva d,
            @QueryParam("operacion") String operacion) throws Exception {

        Object datosSession = usuarioService.getUsuarioInfo(request);
        if (datosSession instanceof Usuario)
            return derivaService.leerDerivado((Usuario) datosSession, d, operacion);
        else
            return datosSession;
    }

    @RequestMapping(value = "/validarCierre", method = RequestMethod.GET)
    public @ResponseBody Object validarCierre(@QueryParam("id") String id, @QueryParam("empleadoId") String empleadoId,
            @QueryParam("idDeriva") String idDeriva) throws Exception {
        return derivaService.validarCierre(id, empleadoId, idDeriva);
    }

    @RequestMapping(value = "/cerrar", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object cerrarDerivado(HttpServletRequest request, @RequestBody Deriva d,
            @QueryParam("operacion") String operacion, @QueryParam("motivo") String motivo) throws Exception {

        Object datosSession = usuarioService.getUsuarioInfo(request);
        if (datosSession instanceof Usuario)
            return derivaService.cerrarDerivado((Usuario) datosSession, d, operacion, motivo);
        else
            return datosSession;
    }

    @RequestMapping(value = "/leerParcial", method = RequestMethod.GET)
    public @ResponseBody Object leerDerivadoParcial(HttpServletRequest request,
            @QueryParam("fichaDocumentoId") String fichaDocumentoId) throws Exception {

        Object datosSession = usuarioService.getUsuarioInfo(request);
        if (datosSession instanceof Usuario)
            return derivaService.leerDerivadoParcial((Usuario) datosSession, fichaDocumentoId);
        else
            return datosSession;
    }

    @RequestMapping(value = "/validaJson", method = RequestMethod.GET)
    public @ResponseBody Map validaJson(@QueryParam("jsonInString") String jsonInString) throws Exception {
        Map result = new HashMap<>();
        boolean valida = JsonUtils.isJsonValidDeriva(jsonInString);
        result.put("valida", valida);
        if (valida) {
            result.put("msj", "OK");
        } else {
            result.put("msj", Constantes.MENSAJE_OBJETO_INCORRECTO);
        }
        return result;
    }

    @RequestMapping(value = "/validaEnviado", method = RequestMethod.GET)
    public @ResponseBody Object getDerivaIdByFichaIdCC(HttpServletRequest request,
            @QueryParam("fichaDocumentoId") String fichaDocumentoId) throws Exception {

        Object datosSession = usuarioService.getUsuarioInfo(request);
        if (datosSession instanceof Usuario)
            return derivaService.getDerivaIdByFichaIdCC((Usuario) datosSession, fichaDocumentoId);
        else
            return datosSession;
    }

    // Agregado AEP 16.08.2019
    @RequestMapping(value = "/reporte/enviados/xls", method = RequestMethod.GET, produces = "application/vnd.ms-excel")
    public @ResponseBody Object reporteEnviadosXls(HttpServletRequest request, HttpServletResponse response,
            @QueryParam("tipoRegistro") String tipoRegistro, @QueryParam("empleadoId") String empleadoId,
            @QueryParam("id") String id, @QueryParam("numeroDoc") String numeroDoc, @QueryParam("asunto") String asunto,
            @QueryParam("fechaIniCrea") String fechaIniCrea, @QueryParam("fechaFinCrea") String fechaFinCrea,
            @QueryParam("tipoDocumento") String tipoDocumento, @QueryParam("tipoEstado") String tipoEstado,
            @QueryParam("centroCosto") String centroCosto, @QueryParam("privado") String privado,
            @QueryParam("indTipRep") String indTipRep, @QueryParam("remitidoDes") String remitidoDes,
            @QueryParam("texto") String texto, @QueryParam("referencia") String referencia,
            @QueryParam("observaciones") String observaciones, @QueryParam("pag") String pag,
            @QueryParam("pagLength") String pagLength, @QueryParam("numeroMp") String numeroMp,
            @QueryParam("dependenciaId") String dependenciaId, @QueryParam("fechaIniCreaDoc") String fechaIniCreaDoc,
            @QueryParam("fechaFinCreaDoc") String fechaFinCreaDoc, @QueryParam("entidadExterna") String entidadExterna,
            @QueryParam("empleadoIdOrigen") String empleadoIdOrigen, @QueryParam("filtros") String filtros,
            @PathVariable("anioLegislativo") String anioLegislativo) throws Exception {

        Object datosSession = usuarioService.getUsuarioInfo(request);
        if (datosSession instanceof Usuario) {
            Usuario usuario = (Usuario) datosSession;
            Boolean indMP = false;
            response.setHeader("Content-disposition", "attachment; filename= reporteEnviados.xls");
            // return fichaDocumentoService.reporteEnviados(request, usuario,
            // Optional.fromNullable(tipoRegistro), Optional.fromNullable(empleadoId),
            // Optional.fromNullable(id), Optional.fromNullable(numeroDoc),
            // Optional.fromNullable(asunto), Optional.fromNullable(fechaIniCrea),
            // Optional.fromNullable(fechaFinCrea), Optional.fromNullable(tipoDocumento),
            // Optional.fromNullable(tipoEstado), Optional.fromNullable(centroCosto),
            // Optional.fromNullable(privado), Optional.fromNullable(indTipRep),
            // Optional.fromNullable(remitidoDes), Optional.fromNullable(texto),
            // Optional.fromNullable(referencia), Optional.fromNullable(observaciones),
            // indMP, Optional.fromNullable(pag), Optional.fromNullable(pagLength),
            // Optional.fromNullable(numeroMp), Optional.fromNullable(dependenciaId),
            // Optional.fromNullable(fechaIniCreaDoc),
            // Optional.fromNullable(fechaFinCreaDoc),
            // Optional.fromNullable(entidadExterna),
            // Optional.fromNullable(empleadoIdOrigen), Optional.fromNullable(filtros),
            // ReportType.FILE.XLS, Optional.fromNullable(anioLegislativo));
        }
        return null;

    }
}
