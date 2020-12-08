package pe.gob.congreso.dao;

import com.google.common.base.Optional;
import java.util.List;
import java.util.Map;

import pe.gob.congreso.dto.DerivaDTO;
import pe.gob.congreso.model.Deriva;
import pe.gob.congreso.model.FichaDocumento;
import pe.gob.congreso.model.util.EnviadoUtil;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.util.FichaConsultas;

public interface DerivaDao {

        public List test() throws Exception;

        public Deriva create(Deriva d) throws Exception;

        public Map<String, Object> findBy(Optional<String> tipoRegistro, Optional<String> empleadoId,
                        Optional<String> id, Optional<String> numeroDoc, Optional<String> asunto,
                        Optional<String> fechaIniCrea, Optional<String> fechaFinCrea, Optional<String> tipoDocumento,
                        Optional<String> tipoEstado, Optional<String> centroCosto, Optional<String> privado,
                        Optional<String> remitidoDes, Optional<String> texto, Optional<String> referencia,
                        Optional<String> observaciones, Optional<String> esResponsable, Optional<String> esRecibido,
                        Optional<String> pag, Optional<String> pagLength, Boolean esMesaPartes,
                        Optional<String> numeroMp, Optional<String> dependenciaId, Optional<String> anioLegislativo,
                        Optional<String> esRecibiConforme) throws Exception;

        public List<Deriva> findDerivados(String fichaDocumentoId) throws Exception;

        public List<EnviadoUtil> findEnviados(Integer fichaDocumentoId, Boolean esMesaPartes) throws Exception;

        public Deriva findDerivado(String id) throws Exception;

        public List<Deriva> findDirigidos(String fichaDocumentoId) throws Exception;

        public List<Deriva> findDirigidosResponsable(String fichaDocumentoId) throws Exception;

        public List<Deriva> getOnlyDerivados(String fichaDocumentoId) throws Exception;

        public List<Deriva> getOnlyDerivados(String fichaDocumentoId, String centroCostoOrigen, Integer idDeriva,
                        Integer estado, Integer empleadoId) throws Exception;

        public List<Deriva> getDerivadosToValidar(Integer fichaDocumentoId, Integer empleadoOrigen,
                        String centroCostoDestino) throws Exception;

        public List<Deriva> getEnviadosLessParciales(Integer fichaDocumentoId) throws Exception;

        public List<Deriva> getEnviadosParciales(Integer fichaDocumentoId) throws Exception;

        public List<Deriva> getDerivadosByCC(Integer id, String centroCostoId) throws Exception;

        public Integer updateRecibidoFisico(FichaDocumento fd) throws Exception;

        public Deriva getDerivadoToFichaIdEmpleadoCC(Integer fichaDocumentoId, Integer empleadoDestino,
                        String centroCostoDestino) throws Exception;

        public Map<String, Object> getFichaDerivaEstafeta(FichaConsultas fc, Optional<String> pag,
                        Optional<String> pagLength, List<InputSelectUtil> listDependencias, Map parametros)
                        throws Exception;

        public List<Deriva> getDerivadosByCCOrigen(Integer id, String centroCostoIdOrigen) throws Exception;

        public List<Deriva> findDirigidosTemplate(String fichaDocumentoId) throws Exception;

}
