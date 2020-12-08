package pe.gob.congreso.service.impl;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import pe.gob.congreso.dao.AdjuntoDao;
import pe.gob.congreso.dao.DerivaDao;
import pe.gob.congreso.dao.PlantillaDocumentoDao;
import pe.gob.congreso.model.*;
import pe.gob.congreso.service.*;
import pe.gob.congreso.util.Constantes;
import pe.gob.congreso.util.Constantes.Cargo.*;
import pe.gob.congreso.util.TemplateHelper;
import pe.gob.congreso.dto.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service("templateService")
@Transactional
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    AdjuntoDao adjuntoDao;

    @Autowired
    AdjuntoService adjuntoService;

    @Autowired
    FichaDocumentoService fichaDocumentoService;

    @Autowired
    TipoService tipoService;

    @Autowired
    GrupoCentroCostoService grupoCentroCostoService;

    @Autowired
    DerivaDao derivaDao;

    @Autowired
    EnviadoExternoService enviadoExternoService;

    @Autowired
    PlantillaDocumentoDao plantillaDocumentoDao;

    @Autowired
    ServletContext servletContext;

    protected final Log log = LogFactory.getLog(getClass());

    @Override
    public Object openTemplate(Usuario usuario, Integer fichaDocumentoId) throws Exception {
        Adjunto adjunto = adjuntoService.findAdjuntoDocumento(fichaDocumentoId, Constantes.ARCHIVO_WORD,
                Constantes.DOCUMENTO_PRINCIPAL, Constantes.ARCHIVO_SIN_FIRMA);
        if (Optional.fromNullable(adjunto).isPresent()) {
            if (Optional.fromNullable(adjunto.getUuid()).isPresent()) {
                return this.openDocumentFromAlfresco(usuario, adjunto, fichaDocumentoId);
            } else {
                return this.openDocumentFromTemplate(usuario, adjunto);
            }
        } else {
            return this.openDocumentFromTemplate(usuario, fichaDocumentoId);
        }
    }

    @Override
    public Object uploadTemplate(Usuario usuario, Integer fichaDocumentoId) throws Exception {
        Adjunto adjunto = adjuntoService.findAdjuntoDocumento(fichaDocumentoId, Constantes.ARCHIVO_WORD, Constantes.DOCUMENTO_PRINCIPAL, Constantes.ARCHIVO_SIN_FIRMA);
        Object response = null;
        if (Optional.fromNullable(adjunto).isPresent()) {
            TemplateDTO templateDTO = new TemplateDTO();
            templateDTO.setNombreArchivo(adjunto.getNombreArchivo());
            AlfrescoCredentialDTO alfrecoCredential = this.getAlfrescoCredentials();
            templateDTO.setAlfrescoCredentials(alfrecoCredential);
            Status status = this.postListenerApi(usuario, templateDTO, Constantes.UPLOAD_TEMPLATE_API);
            String uuid = String.valueOf(status.getObject());
            adjuntoService.updateUUID(adjunto.getNombreArchivo(), uuid);
            return status;
        }
        return response;
    }

    @Override
    public Object getPDFDocumento(HttpServletRequest request, Integer fichaDocumentoId) throws Exception {
        FichaDocumento fichaDocumento = new FichaDocumento();
        fichaDocumento = fichaDocumentoService.getFichaDocumentoId(fichaDocumentoId);
        Object response = adjuntoService.getPDFDefault(request);
        Adjunto adjunto;

        if (Optional.fromNullable(fichaDocumento).isPresent()) {
            if (fichaDocumento.getTipoEstado().getId().equals(Constantes.ESTADO_ANULADO)) {
                adjunto = adjuntoService.findAdjuntoDocumento(fichaDocumentoId, Constantes.ARCHIVO_PDF, Constantes.DOCUMENTO_PRINCIPAL, Constantes.ARCHIVO_SIN_FIRMA);
                if (Optional.fromNullable(adjunto).isPresent()) {
                    response = adjuntoService.getAdjunto(request, adjunto.getUuid());
                }
            } else {
                adjunto = adjuntoService.findAdjuntoDocumento(fichaDocumentoId, Constantes.ARCHIVO_WORD, Constantes.DOCUMENTO_PRINCIPAL, Constantes.ARCHIVO_SIN_FIRMA);
                if (Optional.fromNullable(adjunto).isPresent()) {
                    InputStream inputDocx = adjuntoService.getAdjuntoDocx(adjunto.getUuid());
                    String rutaOut = servletContext.getRealPath(Constantes.RUTA_ASSETS + adjunto.getUuid());
                    Map<String, Object> contextMap = new HashMap<String, Object>();
                    contextMap.put("numerodoc", "");
                    contextMap.put("fechadoc", "");
                    IXDocReport report = XDocReportRegistry.getRegistry().loadReport(inputDocx, TemplateEngineKind.Freemarker);
                    OutputStream out = new FileOutputStream(new File(rutaOut));
                    Options options = Options.getTo(ConverterTypeTo.PDF);
                    report.convert(contextMap, options, out);
                    Path path = Paths.get(rutaOut);
                    byte[] contents = Files.readAllBytes(path);
                    response = contents;
                    File docum = new File(rutaOut);
                    docum.delete();
                }
            }
        }
        return response;
    }

    /*
     * Funcionalidad Abrir Documentos: Métodos en STD
     */
    public Object openDocumentFromTemplate(Usuario usuario, Integer fichaDocumentoId)
            throws FileNotFoundException, XDocReportException, IOException, Exception {
        FichaDocumento fichaDocumento = fichaDocumentoService.getFichaDocumentoId(fichaDocumentoId);
        String nombreArchivoDocx = this.generarAdjuntoDocx(usuario, fichaDocumento);
        return this.generateTemplateListenerAPI(usuario, fichaDocumento, nombreArchivoDocx);
    }

    public Object openDocumentFromTemplate(Usuario usuario, Adjunto adjunto)
            throws FileNotFoundException, XDocReportException, IOException, Exception {
        FichaDocumento fichaDocumento = adjunto.getFichaDocumento();
        String nombreArchivoDocx = adjunto.getNombreArchivo();
        return this.generateTemplateListenerAPI(usuario, fichaDocumento, nombreArchivoDocx);
    }

    public Object openDocumentFromAlfresco(Usuario usuario, Adjunto adjunto, Integer fichaDocumentoId)
            throws Exception {
        String nombrePDF = "";
        Status response;
        if (Optional.fromNullable(adjunto).isPresent()) {
            nombrePDF = adjunto.getNombreArchivo();
            response = (Status) this.generateAlfrescoTemplateListenerAPI(usuario, adjunto);
            if (response.getCode() == 200) {
                return new Status(200, "OK", nombrePDF);
            } else {
                return new Status(404, "EROR: Plantilla no generada", nombrePDF);
            }
        }
        return new Status(200, "OK", nombrePDF);
    }

    public String generarAdjuntoDocx(Usuario usuario, FichaDocumento fichaDocumento)
            throws FileNotFoundException, XDocReportException, IOException, Exception {
        String nombreAdjuntoDocx = "";
        nombreAdjuntoDocx = TemplateHelper.generateFileName(fichaDocumento.getTipoDocumento().getId(),
                fichaDocumento.getId(), ".docx");
        Adjunto adjunto = new Adjunto();
        adjunto.setNombreArchivo(nombreAdjuntoDocx);
        adjunto.setDescripcion("DOCUMENTO WORD DEL RU: " + fichaDocumento.getId());
        adjunto.setFechaAdjunto(new Date());
        adjunto.setFichaDocumento(fichaDocumento);
        adjunto.setEmpleado(fichaDocumento.getEmpleado());
        Tipo tipo = tipoService.findByCodigo(String.valueOf(Constantes.TIPO_ADJUNTO_ARCHIVO));
        adjunto.setTipo(tipo);
        adjunto.setDerivado(false);
        adjunto.setHabilitado(true);
        adjunto.setPublico(false);
        adjunto.setCentroCosto(fichaDocumento.getCentroCosto());
        Tipo tipoAnexo = tipoService.findByCodigo(String.valueOf(Constantes.DOCUMENTO_PRINCIPAL));
        Tipo tipoArchivo = tipoService.findByCodigo(String.valueOf(Constantes.ARCHIVO_WORD));
        adjunto.setTipoAnexo(tipoAnexo);
        adjunto.setTipoArchivo(tipoArchivo);
        adjunto.setIndicadorFirma(false);

        adjuntoService.createAdjunto(usuario, adjunto, "CREAR");

        return nombreAdjuntoDocx;
    }

    /*
     * Funcionalidad Abrir Documentos: Métodos en API Local .jar
     */
    public Object generateTemplateListenerAPI(Usuario usuario, FichaDocumento fichaDocumento, String nombreArchivoDocx)
            throws Exception {
        TemplateDTO templateDTO = new TemplateDTO();
        templateDTO.setRegistroUnico(String.valueOf(fichaDocumento.getId()));
        templateDTO.setNumeroDocumento(fichaDocumento.getTipoDocumento().getTipo().getDescripcion().trim() + " N° "
                + fichaDocumento.getNumeroDoc().trim());

        List<Deriva> dirigidos = derivaDao.findDirigidosTemplate(String.valueOf(fichaDocumento.getId()));
        Map<String, Object> dirigidosTemplate = this.getDirigidos(dirigidos);
        templateDTO.setDirigidos((List<DerivaDTO>) dirigidosTemplate.get("dirigidos"));
        templateDTO.setConCopia((String) dirigidosTemplate.get("conCopia"));

        EnviadoExterno enviadoExterno = enviadoExternoService.findEnviadoPor(String.valueOf(fichaDocumento.getId()));
        DerivaDTO origen = new DerivaDTO();
        origen.setEmpleado(enviadoExterno.getEmpleado().getDescripcion().trim());
        origen.setCentroCosto(enviadoExterno.getCentroCosto().getDescripcion().trim());
        origen.setCargo(enviadoExterno.getCargo().trim());
        templateDTO.setOrigen(origen);

        templateDTO.setAsunto(fichaDocumento.getAsunto().trim());
        templateDTO.setReferencia(fichaDocumento.getReferencia().trim());
        templateDTO.setFechaDocumento(TemplateHelper.getDateTemplate(fichaDocumento.getFechaDocumento()));
        templateDTO.setNombreArchivo(nombreArchivoDocx);

        Tipo nombreAnio = tipoService.findByNombre("NOMBRE_ANIO");
        templateDTO.setNombreAnio(nombreAnio.getDescripcion().trim());

        PlantillaDocumento plantillaDocumento = plantillaDocumentoDao.findByTipoDocumento(fichaDocumento.getTipoDocumento().getTipo().getId());
        if (origen.getCentroCosto().length() <= Constantes.MAX_HEADER_TEMPLATE) {
            templateDTO.setTemplateUuid(plantillaDocumento.getPlantilla().getUuid());
        } else {
            templateDTO.setTemplateUuid(plantillaDocumento.getPlantilla().getUuidLarge());
        }

        templateDTO.setExist(false);
        AlfrescoCredentialDTO alfrecoCredential = this.getAlfrescoCredentials();
        templateDTO.setAlfrescoCredentials(alfrecoCredential);

        System.out.println("generateTemplateListenerAPI: " + Constantes.OPEN_TEMPLATE_API);

        Status result = this.postListenerApi(usuario, templateDTO, Constantes.OPEN_TEMPLATE_API);
        return result;
    }

    public Map<String, Object> getDirigidos(List<Deriva> dirigidos) throws Exception {
        Map<String, Object> dirigidosTemplate = new HashMap<String, Object>();
        List<DerivaDTO> dirigidosDTO = new ArrayList<DerivaDTO>();
        DerivaDTO derivaDTO;
        String conCopia = "";
        for (Deriva dirigido : dirigidos) {
            if (dirigido.isCcopia()) {
                if (conCopia.equals("")) {
                    conCopia += this.evaluateConCopia(dirigido);
                } else {
                    conCopia += "," + this.evaluateConCopia(dirigido);
                }
            } else {
                derivaDTO = this.evaluateDirigido(dirigido);
                dirigidosDTO.add(derivaDTO);
            }
        }
        dirigidosTemplate.put("dirigidos", dirigidosDTO);
        dirigidosTemplate.put("conCopia", conCopia);
        return dirigidosTemplate;
    }

    public String evaluateConCopia(Deriva dirigido) throws Exception {
        String conCopia="";
        if(Optional.fromNullable(dirigido.getEmpleadoDestino().getId()).isPresent() &&
                Optional.fromNullable(dirigido.getCentroCostoDestino().getId()).isPresent()) {
            if(dirigido.getCargo().equals("EMPLEADO")) {
                conCopia = TemplateHelper.getAbreviaturas(dirigido.getEmpleadoDestino().getDescripcion());
            } else {
                GrupoCentroCosto grupoCentroCosto = grupoCentroCostoService
                        .getCentroCostoActual(dirigido.getCentroCostoDestino().getId());
                conCopia = grupoCentroCosto.getSigla().trim();
            }
        } else {
            if(Optional.fromNullable(dirigido.getEnviadoExterno().getId()).isPresent()){
                conCopia = dirigido.getEnviadoExterno().getNombres() + " " + dirigido.getEnviadoExterno().getApellidos();
            }
        }
        return conCopia;
    }

    public DerivaDTO evaluateDirigido(Deriva dirigido) throws Exception {
        DerivaDTO derivaDTO = new DerivaDTO();

        if(Optional.fromNullable(dirigido.getEmpleadoDestino().getId()).isPresent() &&
                Optional.fromNullable(dirigido.getCentroCostoDestino().getId()).isPresent()) {
            derivaDTO.setEmpleado(dirigido.getEmpleadoDestino().getDescripcion().trim());
            derivaDTO.setCentroCosto(dirigido.getCentroCostoDestino().getDescripcion().trim());
            if(dirigido.getCargo().equals("EMPLEADO")) {
                String cargo = dirigido.getEmpleadoDestino().getSexo().equals("M") ? "EMPLEADO" : "EMPLEADA";
                derivaDTO.setCargo(cargo + " DEL CONGRESO DE LA REPÚBLICA");
            } else {
                GrupoCentroCosto grupoCentroCosto = grupoCentroCostoService
                        .getCentroCostoActual(dirigido.getCentroCostoDestino().getId());
                String cargo = "";
                switch (grupoCentroCosto.getCargoResponsable().getId()){
                    case Constantes.CARGO_CONGRESISTA:
                        derivaDTO.setCargo("CONGRESISTA DE LA REPÚBLICA");
                        break;
                    case Constantes.CARGO_OFICIAL_MAYOR:
                        derivaDTO.setCargo("OFICIAL MAYOR DEL CONGRESO DE LA REPÚBLICA");
                        break;
                    case Constantes.CARGO_RESPONSABLE:
                        cargo = "RESPONSABLE " + grupoCentroCosto.getConector().trim() + grupoCentroCosto.getDescripcion().trim();
                        derivaDTO.setCargo(cargo);
                        break;
                    case Constantes.CARGO_DIRECTIVO_PORTAVOZ:
                    case Constantes.CARGO_DIRECTOR:
                    case Constantes.CARGO_JEFE:
                    case Constantes.CARGO_PRESIDENTE:
                    case Constantes.CARGO_VICEPRESIDENTE:
                        cargo =
                                dirigido.getEmpleadoDestino().getSexo().equals("M")
                                        ? grupoCentroCosto.getCargoResponsable().getDescripcionMasculino().trim()
                                        : grupoCentroCosto.getCargoResponsable().getDescripcionMasculino().trim();
                        cargo += " " + grupoCentroCosto.getConector().trim() + grupoCentroCosto.getDescripcion().trim();
                        derivaDTO.setCargo(cargo);
                        break;
                    default:
                        derivaDTO.setCargo(cargo);
                        break;
                }
            }
        } else {
            if(Optional.fromNullable(dirigido.getEnviadoExterno().getId()).isPresent()){
                derivaDTO.setEmpleado(dirigido.getEnviadoExterno().getNombres() + " " + dirigido.getEnviadoExterno().getApellidos());
                derivaDTO.setCentroCosto(dirigido.getEnviadoExterno().getOrigen());
                derivaDTO.setCargo(dirigido.getEnviadoExterno().getCargoExterno());
            }
        }

        return derivaDTO;
    }

    public AlfrescoCredentialDTO getAlfrescoCredentials() throws Exception {
        Map<String, Object> params = adjuntoService.getParametrosAlfresco();
        AlfrescoCredentialDTO alfrecoCredential = new AlfrescoCredentialDTO();
        alfrecoCredential.setServer(String.valueOf(params.get("RUTA")));
        alfrecoCredential.setSite(String.valueOf(params.get("SITE")));
        alfrecoCredential.setSpaceStore(String.valueOf(params.get("SPACESTORE")));
        alfrecoCredential.setUser(String.valueOf(params.get("USER")));
        alfrecoCredential.setPassword(String.valueOf(params.get("PASSWORD")));
        return alfrecoCredential;
    }

    public Object generateAlfrescoTemplateListenerAPI(Usuario usuario, Adjunto adjunto)
            throws IOException, XDocReportException, Exception {
        TemplateDTO templateDTO = new TemplateDTO();
        templateDTO.setUuid(adjunto.getUuid());
        templateDTO.setNombreArchivo(adjunto.getNombreArchivo());
        AlfrescoCredentialDTO alfrecoCredential = this.getAlfrescoCredentials();
        templateDTO.setAlfrescoCredentials(alfrecoCredential);
        templateDTO.setExist(true);
        Status result = this.postListenerApi(usuario, templateDTO, Constantes.OPEN_TEMPLATE_API);
        return result;
    }

    public Status postListenerApi(Usuario usuario, TemplateDTO templateDTO, String api) {
        String ipAddress = usuario.getIpAddress();
        Status response = new Status();
        final String uri = Constantes.LISTENER_PROTOCOL + ipAddress + Constantes.LISTENER_PORT + api;
        Gson gson = new Gson();
        String json = gson.toJson(templateDTO);
        RestTemplate restTemplate = new RestTemplate();
        response = restTemplate.postForObject(uri, templateDTO, Status.class);
        return response;
    }
}
