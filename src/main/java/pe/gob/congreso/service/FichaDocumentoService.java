package pe.gob.congreso.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Optional;

import pe.gob.congreso.model.FichaDocumento;
import pe.gob.congreso.model.FichaProveido;
import pe.gob.congreso.model.FichaSubcategoria;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.util.ReportType;
import pe.gob.congreso.vo.SpSgdValidaDespachoVO;

public interface FichaDocumentoService {

    public Map<String, Object> findBy(Usuario usuario,Optional<String> tipoRegistro, Optional<String> empleadoId, Optional<String> id, Optional<String> numeroDoc, Optional<String> asunto, Optional<String> fechaIniCrea, Optional<String> fechaFinCrea, Optional<String> tipoDocumento, Optional<String> tipoEstado, Optional<String> centroCosto, Optional<String> privado, Optional<String> indTipRep, Optional<String> remitidoDes, Optional<String> texto, Optional<String> referencia, Optional<String> observaciones, Boolean indMP, Optional<String> pag, Optional<String> pagLength, Optional<String> numeroMp, Optional<String> dependenciaId, Optional<String> anioLegislativo) throws Exception;

    public FichaDocumento getFichaDocumentoId(Integer id) throws Exception;

    public SpSgdValidaDespachoVO validaparadespacho(Integer id) throws Exception;

    public List<FichaSubcategoria> findSubCategorias(String fichaDocumentoId) throws Exception;

    public Map<String, Object> getFichaDocumentoAnioLegislativo(String id) throws Exception;

    public Map<String, Object> findFichasCategoria(Optional<String> categoriaId, Optional<String> codigo) throws Exception;
    
    public Map<String, Object> getFichaDocumentoSubcategoria(Optional<String> subCategoriaId, Optional<String> codigo) throws Exception;

    public Map<String, Object> findLegislatura(Optional<String> subCategoriaId, Optional<String> codigo) throws Exception;

    public List<FichaProveido> getFichaDocumentoProveidoAnioLegislativo(Optional<String> proveidoId, Optional<String> codigo) throws Exception;

    public List<FichaDocumento> getFichaDocumentoEmpleado(String empleadoId) throws Exception;

    public Object create(Usuario usuario, FichaDocumento fd, String operacion, List<String> listaCambios) throws Exception;

    public Object validarFinaliza(String id) throws Exception;

    public Object finalizar(Usuario usuario, FichaDocumento fd, String operacion, String motivo) throws Exception;

    public Object createFichaProveido(Usuario usuario, FichaProveido fp, String operacion) throws Exception;

    public Object createDirigido(Usuario usuario, FichaSubcategoria fs, String operacion) throws Exception;

    public Map<String, Object> getFichaDocumentoEstafeta(Optional<String> tipoRegistro, Optional<String> centroCostoDestinoId, Optional<String> empleadoDestinoId, Optional<String> id, Optional<String> numeroDoc, Optional<String> numeroMp, Optional<String> asunto, Optional<String> centroCostoOrigenId, Optional<String> empleadoOrigenId, Optional<String> fechaIniCrea, Optional<String> fechaFinCrea, Optional<String> tipoDocumento, Optional<String> tipoEstado, Optional<String> pag, Optional<String> pagLength) throws Exception;

    public Object reporteEnviados(HttpServletRequest request, String usuario, String ru, String numeroDoc, String asunto, String tipoDoc, String estado, String elaboradoPor, String fechaInicio, String fechaFin, String indtipo, String tipoReg, String observaciones, String referencia, String remitido, String anioLegislativo, ReportType.FILE fileType) throws Exception;
    
    public Object reporteRecibidos(HttpServletRequest request, String usuario, String ru, String numeroDoc, String asunto, String tipoDoc, String estado, String elaboradoPor, String fechaInicio, String fechaFin, String observaciones, String referencia, String remitido, String anioLegislativo, String dependenciaId, String esResponsable, String rc, ReportType.FILE fileType) throws Exception;

    public List<String> obtenerCambiosFichaDocumento(FichaDocumento fd) throws Exception;
    
    public FichaDocumento getFichaDocumentoId(Usuario usuario, Integer id) throws Exception;
    
    public List<FichaDocumento> getListFichaDocumentosEnviadosMp(String centroCostoId, String empleadoId, String indEstafeta) throws Exception;
    
    public FichaDocumento getFichaDocumentoId(String centroCostoId, Integer id) throws Exception;

}
