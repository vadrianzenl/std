package pe.gob.congreso.service;

import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;

import pe.gob.congreso.model.Deriva;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.DerivaUtil;
import pe.gob.congreso.model.util.EnviadoUtil;

public interface DerivaService {
        public List test() throws Exception;

        public List<Deriva> findDerivados(String fichaDocumentoId) throws Exception;

        public List<Deriva> findDirigidos(String fichaDocumentoId) throws Exception;

        public Object create(Usuario usuario, Deriva d, String operacion) throws Exception;

        public Object create(Deriva d) throws Exception;

        public Object editDerivaRespuesta(Usuario usuario, Deriva d, String operacion) throws Exception;

        public Deriva findDerivado(String id) throws Exception;

        public Map<String, Object> findBy(Usuario usuario, Optional<String> tipoRegistro, Optional<String> empleadoId,
                        Optional<String> id, Optional<String> numeroDoc, Optional<String> asunto,
                        Optional<String> fechaIniCrea, Optional<String> fechaFinCrea, Optional<String> tipoDocumento,
                        Optional<String> tipoEstado, Optional<String> centroCosto, Optional<String> privado,
                        Optional<String> remitidoDes, Optional<String> texto, Optional<String> referencia,
                        Optional<String> observaciones, Optional<String> esResponsable, Optional<String> esRecibido,
                        Optional<String> pag, Optional<String> pagLength, Boolean esMesaPartes,
                        Optional<String> numeroMp, Optional<String> dependenciaId, Optional<String> anioLegislativo,
                        Optional<String> esRecibiConforme) throws Exception;

        public Object validarDevolver(String id, String empleadoId, String idDeriva) throws Exception;

        public Object devolverDerivado(Usuario usuario, Deriva d, String operacion, String motivo) throws Exception;

        public Object leerDerivado(Usuario usuario, Deriva d, String operacion) throws Exception;

        public Object validarCierre(String id, String empleadoId, String idDeriva) throws Exception;

        public Object cerrarDerivado(Usuario usuario, Deriva d, String operacion, String motivo) throws Exception;

        public void compruebaCambiosValidos(Deriva d) throws Exception;

        public Object leerDerivadoParcial(Usuario usuario, String fichaDocumentoId) throws Exception;

        public List<EnviadoUtil> findEnviados(Integer id) throws Exception;

        public DerivaUtil asignarEtapaIndicadorMesaPartes(DerivaUtil doc) throws Exception;

        public Object getDerivaIdByFichaIdCC(Usuario usuario, String fichaDocumentoId) throws Exception;

        public Map<String, Object> getFichaDerivaEstafeta(Optional<String> tipoRegistro,
                        Optional<String> centroCostoDestinoId, Optional<String> empleadoDestinoId, Optional<String> id,
                        Optional<String> numeroDoc, Optional<String> numeroMp, Optional<String> asunto,
                        Optional<String> centroCostoOrigenId, Optional<String> empleadoOrigenId,
                        Optional<String> fechaIniCrea, Optional<String> fechaFinCrea, Optional<String> tipoDocumento,
                        Optional<String> tipoEstado, Optional<String> pag, Optional<String> pagLength,
                        Optional<String> remitidoDes) throws Exception;

}
