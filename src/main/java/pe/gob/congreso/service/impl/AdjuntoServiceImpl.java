package pe.gob.congreso.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import com.google.common.base.Optional;

import pe.gob.congreso.crypto.Hex;
import pe.gob.congreso.dao.AdjuntoDao;
import pe.gob.congreso.dao.FichaDocumentoDao;
import pe.gob.congreso.dao.TipoDao;
import pe.gob.congreso.model.Adjunto;
import pe.gob.congreso.model.Tipo;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.ActividadService;
import pe.gob.congreso.service.AdjuntoService;
import pe.gob.congreso.util.FileHelper;

@Service("adjuntoService")
@PropertySource("classpath:application.properties")
@Transactional
public class AdjuntoServiceImpl implements AdjuntoService {

    @Autowired
    AdjuntoDao adjuntoDao;

    @Autowired
    ActividadService actividadService;

    @Autowired
    FichaDocumentoDao fichaDocumentoDao;

    @Autowired
    TipoDao tipoDao;

    @Autowired
    private Environment env;

    private final FileHelper fileHelper = new FileHelper();

    private final Hex hex = new Hex();

    protected final Log log = LogFactory.getLog(getClass());

    @Override
    public void setAdjuntosCache(List<Adjunto> list) throws Exception {
        CacheManager cm = CacheManager.getInstance();
        Cache cache = cm.getCache("adjuntoscache");
        // cache.removeAll();

        if (list != null) {
            for (Adjunto adjunto : list) {
                Element element = new Element(adjunto.getUuid(), adjunto);
                cache.put(element);
            }
        }
    }

    @Override
    public List<Adjunto> getAdjuntosCache() throws Exception {
        CacheManager cm = CacheManager.getInstance();
        Cache cache = cm.getCache("adjuntoscache");
        Element element = cache.get("adjuntosList");
        List<Adjunto> list = new ArrayList<>();
        if (element != null) {
            list = (List<Adjunto>) element.getObjectValue();
        }

        return list;
    }

    @Override
    public Adjunto getAdjuntoCache(String documento) throws Exception {
        CacheManager cm = CacheManager.getInstance();
        Cache cache = cm.getCache("adjuntoscache");
        Element element = cache.get(documento);

        Adjunto adjunto = null;

        if (element != null) {
            adjunto = (Adjunto) element.getObjectValue();
        }

        return adjunto;
    }

    @Override
    public List<Adjunto> findAdjuntos(String fichaDocumentoId) throws Exception {
        return adjuntoDao.findAdjuntos(fichaDocumentoId);
    }

    @Override
    public Object createAdjunto(Usuario usuario, Adjunto a, String operacion) throws Exception {
        log.debug("createAdjunto()");
        actividadService.create(usuario, a, operacion);
        return adjuntoDao.create(a);
    }

    @Override
    public Object uploadAdjunto(InputStream inputStream, String nameFile) throws Exception {
        log.info("uploadAdjunto()" + " nameFile: "+ nameFile);
        Map<String, Object> params = this.getParametrosAlfresco();
        try {
            fileHelper.uploadFileAlfresco(inputStream, String.valueOf(params.get("RUTA")), String.valueOf(params.get("SITE")), nameFile, String.valueOf(params.get("USER")), String.valueOf(params.get("PASSWORD")));
            String uuid = fileHelper.getUUID();
            this.updateUUID(nameFile, uuid);
            return new Status(200, "OK");
        } catch (Exception e) {
            log.error(e);
            return new Status(404, "ERROR");
        }
    }

    @Override
    public Object uploadFile(String path, String nameFile, String mimeType) throws Exception {
        Map<String, Object> params = this.getParametrosAlfresco();
        try {
            fileHelper.uploadFile(String.valueOf(params.get("RUTA")), String.valueOf(params.get("SITE")), String.valueOf(params.get("USER")), String.valueOf(params.get("PASSWORD")), path,  nameFile, mimeType);
            String uuid = fileHelper.getUUID();
            this.updateUUID(nameFile, uuid);
            return new Status(200, "OK", uuid);
        } catch (Exception e) {
            log.error(e);
            return new Status(404, "ERROR");
        }
    }

    @Override
    public Object getAdjunto(HttpServletRequest request, String documento) throws Exception {
        Map<String, Object> params = this.getParametrosAlfresco();
        byte[] bytes = null;
        try {
            bytes = fileHelper.getFileAlfresco(String.valueOf(params.get("RUTA")), String.valueOf(params.get("SPACESTORE")), String.valueOf(params.get("SITE")), String.valueOf(params.get("USER")), String.valueOf(params.get("PASSWORD")), documento);
            if (!Optional.fromNullable(bytes).isPresent()) {
                bytes = this.getPDFDefault(request);
            }
        } catch (Exception e) {
            bytes = this.getPDFDefault(request);
        }

        return bytes;
    }

    @Override
    public InputStream getAdjuntoDocx(String documento) throws Exception {
        Map<String, Object> params = this.getParametrosAlfresco();
        InputStream input = null;

        try {
            input = fileHelper.getFile(String.valueOf(params.get("RUTA")), String.valueOf(params.get("SPACESTORE")), String.valueOf(params.get("SITE")), String.valueOf(params.get("USER")), String.valueOf(params.get("PASSWORD")), documento);
        } catch (Exception e) {
            log.info(e);
        }

        return input;
    }

    @Override
    public byte[] getPDFDefault(HttpServletRequest request) throws IOException {
        byte[] bytes;
        File file;
        String path = request.getSession().getServletContext().getRealPath("/assets/default.pdf");
        file = new File(path);
        bytes = fileHelper.toFileByte(file);
        return bytes;
    }

    @Override
    public Map<String, Object> getParametrosAlfresco() throws Exception{
        Map<String, Object> params = new HashMap<String, Object>();
        Tipo t1 = tipoDao.findByNombre("ALFRESCO_SERVER");
        Tipo t2 = tipoDao.findByNombre("ALFRESCO_SPACESTORE");
        Tipo t3 = tipoDao.findByNombre("ALFRESCO_SITE");
        Tipo t4 = tipoDao.findByNombre("ALFRESCO_USER");
        Tipo t5 = tipoDao.findByNombre("ALFRESCO_PASSWORD");

        if (Optional.fromNullable(t1).isPresent()) {
            params.put("RUTA", t1.getDescripcion().trim());
        }

        if (Optional.fromNullable(t2).isPresent()) {
            params.put("SPACESTORE", t2.getDescripcion().trim());
        }

        if (Optional.fromNullable(t3).isPresent()) {
            params.put("SITE", t3.getDescripcion().trim());
        }

        if (Optional.fromNullable(t4).isPresent()) {
            params.put("USER", t4.getDescripcion().trim());
        }

        if (Optional.fromNullable(t5).isPresent()) {
            params.put("PASSWORD", hex.decodeSiga(t5.getDescripcion().trim()));
        }

        return params;
    }

    @Override
    public Object updateUUID(String nameFile, String uuid){
        try{
            adjuntoDao.updateUUID(nameFile, uuid);
            return new Status(200, "OK");
        }catch(Exception e){
            return new Status(404, "ERROR");
        }
    }

    @Override
    public Adjunto findAdjuntoDocumento(Integer fichaDocumentoId, Integer tipoArchivo, Integer tipoAnexo, boolean indicadorFirma) throws Exception{
        return adjuntoDao.findAdjuntoDocx(fichaDocumentoId, tipoArchivo, tipoAnexo,indicadorFirma);
    }

    @Override
    public byte[] showPdfDefault(HttpServletRequest request) throws Exception {
        byte[] bytes = null;
        File file;

        String path = request.getSession().getServletContext().getRealPath("/assets/default.pdf");
        file = new File(path);
        bytes = fileHelper.toFileByte(file);

        return bytes;
    }

    @Override
    public byte[] showPdfUnAuthorized(HttpServletRequest request) throws Exception {
        byte[] bytes = null;
        File file;

        String path = request.getSession().getServletContext().getRealPath("/assets/unauthorized.pdf");
        file = new File(path);
        bytes = fileHelper.toFileByte(file);

        return bytes;
    }

    @Override
    public Boolean checkAccess(String pdf, String CentroCostro, Integer empId, String perfil) throws Exception {
        Integer fichaDocRecibido = null;

        if (perfil.equals("STD-RESMEP")) {
            fichaDocRecibido = adjuntoDao.getFichaDocRecibidoByPdfOfMesa(CentroCostro, pdf);
            System.out.println("pdf encontrado en los documentos recibidos del area: " + fichaDocRecibido);
        } else if (perfil.equals("STD-ADMRES") || perfil.equals("STD-CONSULTA")) {
            fichaDocRecibido = adjuntoDao.getFichaDocRecibidoByPdfOfDependency(empId, CentroCostro, pdf);
            System.out.println("pdf encontrado en los documentos recibidos del area: " + fichaDocRecibido);
        } else {
            fichaDocRecibido = adjuntoDao.getFichaDocRecibidoByPdfOfPerson(empId, CentroCostro, pdf);
            System.out.println("pdf encontrado en los documentos recibidos personales: " + fichaDocRecibido);
        }

        Integer fichaDocEnviado = null;

        if (perfil.equals("STD-ADMRES")) {
            fichaDocEnviado = adjuntoDao.getFichaDocEnviadoByPdfOfDependency(CentroCostro, pdf);
            System.out.println("pdf encontrado en los documentos enviados del area: " + fichaDocRecibido);
        } else {
            fichaDocEnviado = adjuntoDao.getFichaDocEnviadoByPdfOfPerson(empId, CentroCostro, pdf);
            System.out.println("pdf encontrado en los documentos enviados personales: " + fichaDocRecibido);
        }

        Boolean isValid = fichaDocRecibido != null || fichaDocEnviado != null;
        System.out.println("tiene acceso al pdf: " + isValid);
        return isValid;
    }
}
