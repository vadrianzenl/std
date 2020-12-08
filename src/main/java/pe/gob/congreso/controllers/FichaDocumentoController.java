package pe.gob.congreso.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.NDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Optional;

import pe.gob.congreso.model.Deriva;
import pe.gob.congreso.model.FichaDocumento;
import pe.gob.congreso.model.FichaProveido;
import pe.gob.congreso.model.FichaSubcategoria;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.DerivaUtil;
import pe.gob.congreso.model.util.Util;
import pe.gob.congreso.service.DerivaService;
import pe.gob.congreso.service.FichaDocumentoService;
import pe.gob.congreso.service.FichaProveidoService;
import pe.gob.congreso.service.ProveidoService;
import pe.gob.congreso.service.UsuarioService;
import pe.gob.congreso.service.impl.Status;
import pe.gob.congreso.util.Constantes;
import pe.gob.congreso.util.ReportType;

@Controller
@RequestMapping(Urls.FichaDocumento.BASE)
public class FichaDocumentoController {

    protected final Log log = LogFactory.getLog(getClass());

    @Autowired
    FichaDocumentoService fichaDocumentoService;

    @Autowired
    FichaProveidoService fichaProveidoService;

    @Autowired
    ProveidoService proveidoService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    DerivaService derivaService;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Map getFichaDocumentos(HttpServletRequest request,
            @QueryParam("tipoRegistro") String tipoRegistro, @QueryParam("empleadoId") String empleadoId,
            @QueryParam("id") String id, @QueryParam("numeroDoc") String numeroDoc, @QueryParam("asunto") String asunto,
            @QueryParam("fechaIniCrea") String fechaIniCrea, @QueryParam("fechaFinCrea") String fechaFinCrea,
            @QueryParam("tipoDocumento") String tipoDocumento, @QueryParam("tipoEstado") String tipoEstado,
            @QueryParam("centroCosto") String centroCosto, @QueryParam("privado") String privado,
            @QueryParam("indTipRep") String indTipRep, @QueryParam("remitidoDes") String remitidoDes,
            @QueryParam("texto") String texto, @QueryParam("referencia") String referencia,
            @QueryParam("observaciones") String observaciones, @QueryParam("pag") String pag,
            @QueryParam("pagLength") String pagLength, @QueryParam("numeroMp") String numeroMp,
            @QueryParam("dependenciaId") String dependenciaId, @QueryParam("anioLegislativo") String anioLegislativo)
            throws Exception {
        Object datosSession = usuarioService.getUsuarioInfo(request);
        if (datosSession instanceof Usuario) {
            Usuario usuario = (Usuario) datosSession;
            Boolean indMP = false;
            if (Constantes.PERFIL_RESPONSABLE_MESA_PARTES.equals(usuario.getPerfil().getId().trim())
                    || Constantes.PERFIL_OPERADOR_MESA_PARTES.equals(usuario.getPerfil().getId().trim())) {
                indMP = true;
            }
            return fichaDocumentoService.findBy(usuario, Optional.fromNullable(tipoRegistro),
                    Optional.fromNullable(empleadoId), Optional.fromNullable(id), Optional.fromNullable(numeroDoc),
                    Optional.fromNullable(asunto), Optional.fromNullable(fechaIniCrea),
                    Optional.fromNullable(fechaFinCrea), Optional.fromNullable(tipoDocumento),
                    Optional.fromNullable(tipoEstado), Optional.fromNullable(centroCosto),
                    Optional.fromNullable(privado), Optional.fromNullable(indTipRep),
                    Optional.fromNullable(remitidoDes), Optional.fromNullable(texto), Optional.fromNullable(referencia),
                    Optional.fromNullable(observaciones), indMP, Optional.fromNullable(pag),
                    Optional.fromNullable(pagLength), Optional.fromNullable(numeroMp),
                    Optional.fromNullable(dependenciaId), Optional.fromNullable(anioLegislativo));
        }
        return null;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody FichaDocumento getFichaDocumentoId(@PathVariable("id") Integer id) throws Exception {
        return fichaDocumentoService.getFichaDocumentoId(id);
    }

    @RequestMapping(value = "/validaparadespacho", method = RequestMethod.GET)
    public @ResponseBody Object validaparadespacho(@QueryParam("id") Integer id) throws Exception {
        log.info("validaparadespacho {" + id + "}");
        return fichaDocumentoService.validaparadespacho(id);
    } 

    @RequestMapping(value = "/{id}/validaRelacionar", method = RequestMethod.GET)
    public @ResponseBody FichaDocumento getFichaDocumentoARelacionar(HttpServletRequest request,
            @PathVariable("id") Integer id) throws Exception {
        FichaDocumento resultado = null;
        Object datosSession = usuarioService.getUsuarioInfo(request);
        if (datosSession instanceof Usuario) {
            Usuario usuario = (Usuario) datosSession;
            NDC.push(usuario.getNombreUsuario().trim());
            try {
                resultado = fichaDocumentoService.getFichaDocumentoId(usuario, id);
            } catch (Exception e) {
                log.error(e, e);
            } finally {
                NDC.pop();
                NDC.remove();
            }
            return resultado;
        }
        return resultado;
    }

    @RequestMapping(value = "/{id}/subCategorias", method = RequestMethod.GET)
    public @ResponseBody List getSubCategorias(@PathVariable("id") String id) throws Exception {
        return fichaDocumentoService.findSubCategorias(id);
    }

    @RequestMapping(value = "/anioLegislativo/{codigo}", method = RequestMethod.GET)
    public @ResponseBody Map getFichaDocumentoAnioLegislativo(@PathVariable("codigo") String Id) throws Exception {
        return fichaDocumentoService.getFichaDocumentoAnioLegislativo(Id);
    }

    @RequestMapping(value = "/categoria/{categoriaId}", method = RequestMethod.GET)
    public @ResponseBody Map getFichaDocumentoCategoria(@PathVariable("categoriaId") String categoriaId)
            throws Exception {
        return fichaDocumentoService.findFichasCategoria(Optional.fromNullable(categoriaId), Optional.fromNullable(""));
    }

    @RequestMapping(value = "/categoria/{categoriaId}/anioLegislativo/{codigo}", method = RequestMethod.GET)
    public @ResponseBody Map getFichaDocumentoCategoriaAnioLegislativo(@PathVariable("categoriaId") String categoriaId,
            @PathVariable("codigo") String codigo) throws Exception {
        return fichaDocumentoService.findFichasCategoria(Optional.fromNullable(categoriaId),
                Optional.fromNullable(codigo));
    }

    @RequestMapping(value = "/subcategoria/{subCategoriaId}", method = RequestMethod.GET)
    public @ResponseBody Map getFichaDocumentoSubcategoria(@PathVariable("subCategoriaId") String subCategoriaId)
            throws Exception {
        return fichaDocumentoService.getFichaDocumentoSubcategoria(Optional.fromNullable(subCategoriaId),
                Optional.fromNullable(""));
    }

    @RequestMapping(value = "/subcategoria/{subCategoriaId}/anioLegislativo/{codigo}", method = RequestMethod.GET)
    public @ResponseBody Map getFichaDocumentoSubcategoriaAnioLegislativo(
            @PathVariable("subCategoriaId") String subCategoriaId, @PathVariable("codigo") String codigo)
            throws Exception {
        return fichaDocumentoService.getFichaDocumentoSubcategoria(Optional.fromNullable(subCategoriaId),
                Optional.fromNullable(codigo));
    }

    @RequestMapping(value = "/subcategoria/{subCategoriaId}/legislatura/{codigo}", method = RequestMethod.GET)
    public @ResponseBody Map getFichaDocumentoSubcategoriaLegislaturaa(
            @PathVariable("subCategoriaId") String subCategoriaId, @PathVariable("codigo") String codigo)
            throws Exception {
        return fichaDocumentoService.findLegislatura(Optional.fromNullable(subCategoriaId),
                Optional.fromNullable(codigo));
    }

    @RequestMapping(value = "/proveido/{proveidoId}", method = RequestMethod.GET)
    public @ResponseBody List getFichaDocumentoProveido(@PathVariable("proveidoId") String proveidoId)
            throws Exception {
        return fichaDocumentoService.getFichaDocumentoProveidoAnioLegislativo(Optional.fromNullable(proveidoId),
                Optional.fromNullable(""));
    }

    @RequestMapping(value = "/proveido/{proveidoId}/anioLegislativo/{codigo}", method = RequestMethod.GET)
    public @ResponseBody List getFichaDocumentoProveidoAnioLegislativo(@PathVariable("proveidoId") String proveidoId,
            @PathVariable("codigo") String codigo) throws Exception {
        return fichaDocumentoService.getFichaDocumentoProveidoAnioLegislativo(Optional.fromNullable(proveidoId),
                Optional.fromNullable(codigo));
    }

    @RequestMapping(value = "/empleado/{empleadoId}", method = RequestMethod.GET)
    public @ResponseBody List getFichaDocumentoEmpleado(@PathVariable("empleadoId") String empleadoId)
            throws Exception {
        return fichaDocumentoService.getFichaDocumentoEmpleado(empleadoId);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object createFichaDocumento(HttpServletRequest request, @RequestBody @Valid FichaDocumento fd,
            BindingResult result, @QueryParam("operacion") String operacion) throws Exception {
        if (result.hasErrors()) {
            Status status = new Status();
            status.getErrores(result);
            return status;
        }
        Object datosSession = usuarioService.getUsuarioInfo(request);
        if (datosSession instanceof Usuario) {
            // Obtener lista de cambios
            List<String> listaCambios = new ArrayList<String>();
            if (Optional.fromNullable(fd.getId()).isPresent() && operacion.trim().equals("EDITAR")) {
                listaCambios = fichaDocumentoService.obtenerCambiosFichaDocumento(fd);
            }
            Usuario usuario = (Usuario) datosSession;
            NDC.push(usuario.getNombreUsuario().trim());

            Object resultado = new Object();
            try {
                resultado = fichaDocumentoService.create(usuario, fd, operacion, listaCambios);
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

    @RequestMapping(value = "/{id}/validarFinaliza", method = RequestMethod.GET)
    public @ResponseBody Object validarFinaliza(@PathVariable("id") String id) throws Exception {
        return fichaDocumentoService.validarFinaliza(id);
    }

    @RequestMapping(value = "/finalizar", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object finalizarFichaDocumento(HttpServletRequest request, @RequestBody FichaDocumento fd,
            @QueryParam("operacion") String operacion, @QueryParam("motivo") String motivo) throws Exception {
        Object datosSession = usuarioService.getUsuarioInfo(request);
        if (datosSession instanceof Usuario)
            return fichaDocumentoService.finalizar((Usuario) datosSession, fd, operacion, motivo);
        else
            return datosSession;
    }

    @RequestMapping(value = "/proveido", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object createFichaProveido(HttpServletRequest request, @RequestBody FichaProveido fp,
            @QueryParam("operacion") String operacion) throws Exception {
        Boolean crea = fp.getFichaDocumentoId() != null && fp.getProveidoId() != null;
        Object datosSession = usuarioService.getUsuarioInfo(request);
        if (datosSession instanceof Usuario && crea) {
            List<FichaProveido> list = fichaProveidoService.getByIdRU(
                    Optional.fromNullable(fp.getFichaDocumentoId().toString()),
                    Optional.fromNullable(fp.getProveidoId().toString()));
            if (list.size() > 0) {
                if (Util.indicadorCambioCampos(fp, list.get(0))) {
                    operacion = Constantes.OPERACION_ACTUALIZAR;
                    fp = Util.cambiarCamposNulosPorActuales(fp, list.get(0));
                } else {
                    return null;
                }
            } else {
                operacion = Constantes.OPERACION_CREAR;
            }
            fp = (FichaProveido) fichaProveidoService.createFichaProveido((Usuario) datosSession, fp, operacion);
            DerivaUtil du = new DerivaUtil();
            FichaDocumento fichaDocumento = new FichaDocumento();
            fichaDocumento.setId(fp.getFichaDocumentoId());
            fichaDocumento.setFechaCrea(fp.getFechaCrea());
            List<Deriva> listDeriva = new ArrayList<Deriva>();
            listDeriva = derivaService.findDerivados(fichaDocumento.getId().toString());
            fichaDocumento.setListDeriva(listDeriva);
            du.setFichaDocumento(fichaDocumento);
            du = derivaService.asignarEtapaIndicadorMesaPartes(du);
            Map mp = new HashMap<>();
            mp.put("fichaProveido", fp);
            mp.put("Etapa", du);
            return mp;
        } else {
            return datosSession;
        }
    }

    @RequestMapping(value = "/relacionado", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object createDirigido(HttpServletRequest request, @RequestBody FichaSubcategoria fs,
            @QueryParam("operacion") String operacion) throws Exception {
        Object datosSession = usuarioService.getUsuarioInfo(request);
        if (datosSession instanceof Usuario) {
            Usuario usuario = (Usuario) datosSession;
            NDC.push(usuario.getNombreUsuario().trim());

            Object resultado = new Object();
            try {
                resultado = fichaDocumentoService.createDirigido(usuario, fs, operacion);
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

    @RequestMapping(value = "/estafeta", method = RequestMethod.GET)
    public @ResponseBody Map getFichaDocumentoEstafeta(HttpServletRequest request,
            @QueryParam("tipoRegistro") String tipoRegistro,
            @QueryParam("centroCostoDestinoId") String centroCostoDestinoId,
            @QueryParam("empleadoDestinoId") String empleadoDestinoId, @QueryParam("id") String id,
            @QueryParam("numeroDoc") String numeroDoc, @QueryParam("numeroProveido") String numeroMp,
            @QueryParam("asunto") String asunto, @QueryParam("empleadoCreaId") String empleadoCreaId,
            @QueryParam("fechaIniCrea") String fechaIniCrea, @QueryParam("fechaFinCrea") String fechaFinCrea,
            @QueryParam("tipoDocumento") String tipoDocumento, @QueryParam("tipoEstado") String tipoEstado,
            @QueryParam("pag") String pag, @QueryParam("pagLength") String pagLength) throws Exception {
        Map map = new HashMap<>();
        Object datosSession = usuarioService.getUsuarioInfo(request);
        if (datosSession instanceof Usuario) {
            Usuario usuario = (Usuario) datosSession;
            return fichaDocumentoService.getFichaDocumentoEstafeta(Optional.fromNullable(tipoRegistro),
                    Optional.fromNullable(centroCostoDestinoId), Optional.fromNullable(empleadoDestinoId),
                    Optional.fromNullable(id), Optional.fromNullable(numeroDoc), Optional.fromNullable(numeroMp),
                    Optional.fromNullable(asunto),
                    Optional.fromNullable(usuario.getEmpleado().getCentroCosto().getId()),
                    Optional.fromNullable(empleadoCreaId), Optional.fromNullable(fechaIniCrea),
                    Optional.fromNullable(fechaFinCrea), Optional.fromNullable(tipoDocumento),
                    Optional.fromNullable(tipoEstado), Optional.fromNullable(pag), Optional.fromNullable(pagLength));

        } else
            return map;

    }

    @RequestMapping(value = "/reporte/enviados/pdf/usuario/{usuario}/ru/{ru}/numeroDoc/{numeroDoc}/asunto/{asunto}/tipoDoc/{tipoDoc}/estado/{estado}/elaboradoPor/{elaboradoPor}/fechaInicio/{fechaInicio}/fechaFin/{fechaFin}/tipo/{indtipo}/tipoReg/{tipoReg}/observaciones/{observaciones}/referencia/{referencia}/remitido/{remitido}/anioLegislativo/{anioLegislativo}", method = RequestMethod.GET, produces = "application/pdf")
    public @ResponseBody Object reporteEnviados(HttpServletRequest request, @PathVariable("usuario") String usuario,
            @PathVariable("ru") String ru, @PathVariable("numeroDoc") String numeroDoc,
            @PathVariable("asunto") String asunto, @PathVariable("tipoDoc") String tipoDoc,
            @PathVariable("estado") String estado, @PathVariable("elaboradoPor") String elaboradoPor,
            @PathVariable("fechaInicio") String fechaInicio, @PathVariable("fechaFin") String fechaFin,
            @PathVariable("indtipo") String indtipo, @PathVariable("tipoReg") String tipoReg,
            @PathVariable("observaciones") String observaciones, @PathVariable("referencia") String referencia,
            @PathVariable("remitido") String remitido, @PathVariable("anioLegislativo") String anioLegislativo)
            throws Exception {
        return fichaDocumentoService.reporteEnviados(request, usuario, ru, numeroDoc, asunto, tipoDoc, estado,
                elaboradoPor, fechaInicio, fechaFin, indtipo, tipoReg, observaciones, referencia, remitido,
                anioLegislativo, ReportType.FILE.PDF);
    }

    // Agregado AEP 19.08.2019 - Exportacion a Excel ENVIADOS
    @RequestMapping(value = "/reporte/enviados/xls/usuario/{usuario}/ru/{ru}/numeroDoc/{numeroDoc}/asunto/{asunto}/tipoDoc/{tipoDoc}/estado/{estado}/elaboradoPor/{elaboradoPor}/fechaInicio/{fechaInicio}/fechaFin/{fechaFin}/tipo/{indtipo}/tipoReg/{tipoReg}/observaciones/{observaciones}/referencia/{referencia}/remitido/{remitido}/anioLegislativo/{anioLegislativo}", method = RequestMethod.GET, produces = "application/vnd.ms-excel")
    public @ResponseBody Object reporteEnviadosXls(HttpServletRequest request, HttpServletResponse response,
            @PathVariable("usuario") String usuario, @PathVariable("ru") String ru,
            @PathVariable("numeroDoc") String numeroDoc, @PathVariable("asunto") String asunto,
            @PathVariable("tipoDoc") String tipoDoc, @PathVariable("estado") String estado,
            @PathVariable("elaboradoPor") String elaboradoPor, @PathVariable("fechaInicio") String fechaInicio,
            @PathVariable("fechaFin") String fechaFin, @PathVariable("indtipo") String indtipo,
            @PathVariable("tipoReg") String tipoReg, @PathVariable("observaciones") String observaciones,
            @PathVariable("referencia") String referencia, @PathVariable("remitido") String remitido,
            @PathVariable("anioLegislativo") String anioLegislativo) throws Exception {
        response.setHeader("Content-disposition", "attachment; filename= reporteEnviados.xls");
        return fichaDocumentoService.reporteEnviados(request, usuario, ru, numeroDoc, asunto, tipoDoc, estado,
                elaboradoPor, fechaInicio, fechaFin, indtipo, tipoReg, observaciones, referencia, remitido,
                anioLegislativo, ReportType.FILE.XLS);
    }

    @RequestMapping(value = "/reporte/recibidos/pdf/usuario/{usuario}/ru/{ru}/numeroDoc/{numeroDoc}/asunto/{asunto}/tipoDoc/{tipoDoc}/estado/{estado}/elaboradoPor/{elaboradoPor}/fechaInicio/{fechaInicio}/fechaFin/{fechaFin}/observaciones/{observaciones}/referencia/{referencia}/remitido/{remitido}/anioLegislativo/{anioLegislativo}/dependenciaId/{dependenciaId}/esResponsable/{esResponsable}/rc/{rc}", method = RequestMethod.GET, produces = "application/pdf")
    public @ResponseBody Object reporteRecibidos(HttpServletRequest request, @PathVariable("usuario") String usuario,
            @PathVariable("ru") String ru, @PathVariable("numeroDoc") String numeroDoc,
            @PathVariable("asunto") String asunto, @PathVariable("tipoDoc") String tipoDoc,
            @PathVariable("estado") String estado, @PathVariable("elaboradoPor") String elaboradoPor,
            @PathVariable("fechaInicio") String fechaInicio, @PathVariable("fechaFin") String fechaFin,
            @PathVariable("observaciones") String observaciones, @PathVariable("referencia") String referencia,
            @PathVariable("remitido") String remitido, @PathVariable("anioLegislativo") String anioLegislativo,
            @PathVariable("dependenciaId") String dependenciaId, @PathVariable("esResponsable") String esResponsable,
            @PathVariable("rc") String rc) throws Exception {
        return fichaDocumentoService.reporteRecibidos(request, usuario, ru, numeroDoc, asunto, tipoDoc, estado,
                elaboradoPor, fechaInicio, fechaFin, observaciones, referencia, remitido, anioLegislativo,
                dependenciaId, esResponsable, rc, ReportType.FILE.PDF);
    }

    // Agregado AEP 19.08.2019 - Exportacion a Excel RECIBIDOS
    @RequestMapping(value = "/reporte/recibidos/xls/usuario/{usuario}/ru/{ru}/numeroDoc/{numeroDoc}/asunto/{asunto}/tipoDoc/{tipoDoc}/estado/{estado}/elaboradoPor/{elaboradoPor}/fechaInicio/{fechaInicio}/fechaFin/{fechaFin}/observaciones/{observaciones}/referencia/{referencia}/remitido/{remitido}/anioLegislativo/{anioLegislativo}/dependenciaId/{dependenciaId}/esResponsable/{esResponsable}/rc/{rc}", method = RequestMethod.GET, produces = "application/vnd.ms-excel")
    public @ResponseBody Object reporteRecibidosXls(HttpServletRequest request, HttpServletResponse response,
            @PathVariable("usuario") String usuario, @PathVariable("ru") String ru,
            @PathVariable("numeroDoc") String numeroDoc, @PathVariable("asunto") String asunto,
            @PathVariable("tipoDoc") String tipoDoc, @PathVariable("estado") String estado,
            @PathVariable("elaboradoPor") String elaboradoPor, @PathVariable("fechaInicio") String fechaInicio,
            @PathVariable("fechaFin") String fechaFin, @PathVariable("observaciones") String observaciones,
            @PathVariable("referencia") String referencia, @PathVariable("remitido") String remitido,
            @PathVariable("anioLegislativo") String anioLegislativo,
            @PathVariable("dependenciaId") String dependenciaId, @PathVariable("esResponsable") String esResponsable,
            @PathVariable("rc") String rc) throws Exception {
        response.setHeader("Content-disposition", "attachment; filename= reporteRecibidos.xls");
        return fichaDocumentoService.reporteRecibidos(request, usuario, ru, numeroDoc, asunto, tipoDoc, estado,
                elaboradoPor, fechaInicio, fechaFin, observaciones, referencia, remitido, anioLegislativo,
                dependenciaId, esResponsable, rc, ReportType.FILE.XLS);
    }

    @RequestMapping(value = "/{id}/validaRelacionarComi", method = RequestMethod.GET)
    public @ResponseBody FichaDocumento getValidaFichaDocumentoARelacionar(@PathVariable("id") Integer id,
            @QueryParam("centroCostoId") String centroCostoId) throws Exception {
        FichaDocumento resultado = null;
        try {
            resultado = fichaDocumentoService.getFichaDocumentoId(centroCostoId, id);
        } catch (Exception e) {
            log.error(e, e);
        } finally {
            NDC.pop();
            NDC.remove();
        }
        return resultado;
    }
}
