package pe.gob.congreso.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import pe.gob.congreso.model.FichaDocumento;
import pe.gob.congreso.model.Relaciona;
import pe.gob.congreso.model.Usuario;

public interface RelacionaService {
	
	static final Integer TIPO_RELACION_REFERENCIA = 147;
	static final Integer TIPO_RELACION_RESPUESTA = 148;

    public Object create(Usuario usuario, Relaciona d, String operacion) throws Exception;

    public List<Relaciona> findRelacionados(String fichaDocumentoId) throws Exception;

    public List<Relaciona> findAsociados(String fichaDocumentoId) throws Exception;

    public List<FichaDocumento> findFichaRelacionados(String fichaDocumentoId) throws Exception;
    
    public List<Relaciona> findRespuestaA(String fichaDocumentoId) throws Exception;
    
    public List<Relaciona> findReferencia(String fichaDocumentoId) throws Exception;
    
    public Object reporteRelacionados(HttpServletRequest request,Usuario usuario, String ru) throws Exception;
}
