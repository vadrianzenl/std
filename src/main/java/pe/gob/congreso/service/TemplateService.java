package pe.gob.congreso.service;

import pe.gob.congreso.model.Usuario;

import javax.servlet.http.HttpServletRequest;

public interface TemplateService {
    public Object openTemplate(Usuario usuario, Integer fichaDocumentoId) throws Exception;

    public Object uploadTemplate(Usuario usuario, Integer fichaDocumentoId) throws Exception;

    public Object getPDFDocumento(HttpServletRequest request, Integer fichaDocumentoId) throws Exception;
}
