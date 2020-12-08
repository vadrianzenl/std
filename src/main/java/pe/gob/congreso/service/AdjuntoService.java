package pe.gob.congreso.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import pe.gob.congreso.model.Adjunto;
import pe.gob.congreso.model.Usuario;

public interface AdjuntoService {

    public void setAdjuntosCache(List<Adjunto> list) throws Exception;

    public List<Adjunto> getAdjuntosCache() throws Exception;

    public Adjunto getAdjuntoCache(String documento) throws Exception;

    public List<Adjunto> findAdjuntos(String fichaDocumentoId) throws Exception;

    public Object createAdjunto(Usuario usuario, Adjunto a, String operacion) throws Exception;

    public Object uploadAdjunto(InputStream inputStream, String nameFile) throws Exception;

    public Object getAdjunto(HttpServletRequest request, String documento) throws Exception;

    public Object updateUUID(String nameFile, String uuid) throws Exception;

    public Adjunto findAdjuntoDocumento(Integer fichaDocumentoId, Integer tipoArchivo, Integer tipoAnexo, boolean indicadorFirma) throws Exception;

    public Object uploadFile(String path, String nameFile, String mimeType) throws Exception;

    public InputStream getAdjuntoDocx(String documento) throws Exception;

    public byte[] getPDFDefault(HttpServletRequest request) throws IOException;

    public Map<String, Object> getParametrosAlfresco() throws Exception;

    public Object showPdfDefault(HttpServletRequest request) throws Exception;

    public Object showPdfUnAuthorized(HttpServletRequest request) throws Exception;

    public Boolean checkAccess(String pdf, String CentroCostro, Integer empId, String perfil) throws Exception;
}
