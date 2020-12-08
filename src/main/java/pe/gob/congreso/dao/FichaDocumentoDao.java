package pe.gob.congreso.dao;

import com.google.common.base.Optional;
import java.util.List;
import java.util.Map;
import pe.gob.congreso.model.FichaDocumento;
import pe.gob.congreso.model.SpSgdValidaDespacho;
import pe.gob.congreso.util.FichaConsultas;
import pe.gob.congreso.util.FichaPedientes;
import pe.gob.congreso.model.util.InputSelectUtil;

public interface FichaDocumentoDao {

    public FichaDocumento create(FichaDocumento fd) throws Exception;

    public Map<String, Object> findBy(Optional<String> tipoRegistro, Optional<String> empleadoId, Optional<String> id,
            Optional<String> numeroDoc, Optional<String> asunto, Optional<String> fechaIniCrea,
            Optional<String> fechaFinCrea, Optional<String> tipoDocumento, Optional<String> tipoEstado,
            Optional<String> centroCosto, Optional<String> privado, Optional<String> indTipRep,
            Optional<String> remitidoDes, Optional<String> texto, Optional<String> referencia,
            Optional<String> observaciones, Optional<String> pag, Optional<String> pagLength, Optional<String> numeroMp,
            Optional<String> dependenciaId, Optional<String> anioLegislativo) throws Exception;

    public FichaDocumento getFichaDocumentoId(Integer id) throws Exception;

    public List<SpSgdValidaDespacho> validaparadespacho(Integer id) throws Exception;

    public List<FichaPedientes> getListEnviosPendientes(Map parametros) throws Exception;

    public Map<String, Object> getFichaDocumentoAnioLegislativo(String id) throws Exception;

    public List<FichaDocumento> getFichaDocumentoEmpleado(String empleadoId) throws Exception;

    public Map<String, Object> getFichaDocumentoEstafeta(FichaConsultas fc, Optional<String> pag,
            Optional<String> pagLength, List<InputSelectUtil> listDependencias) throws Exception;

    public List<FichaDocumento> getFichaDocumentoByListId(List<Integer> listId, String fichaDocumentoId)
            throws Exception;

    public FichaDocumento getFichaDocumentoId(Integer id, String centroCostoId) throws Exception;

    public Integer updateEstafeta(FichaDocumento fd) throws Exception;

    public List<FichaDocumento> getListFichaDocumentosEnviadosMp(String centroCostoId, String empleadoId,
            String indEstafeta) throws Exception;

    public List<FichaDocumento> getListFichaDocumentosEnviadosMpExcluyendoEmpleado(String centroCostoId,
            String empleadoId, String indEstafeta) throws Exception;

    public List<InputSelectUtil> getListDependenciasRecibidos(Map parametros) throws Exception;

    public List<InputSelectUtil> getListDependenciasEnviados(Map parametros) throws Exception;

}
