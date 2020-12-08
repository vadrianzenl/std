package pe.gob.congreso.service;

import java.util.List;

import pe.gob.congreso.model.EnviadoExterno;
import pe.gob.congreso.model.Usuario;

public interface EnviadoExternoService {

    public Object create(Usuario usuario, EnviadoExterno ee, String operacion) throws Exception;

    public EnviadoExterno findEnviadoPor(String fichaDocumentoId) throws Exception;
    
    public List<EnviadoExterno> findEnviadoA(String fichaDocumentoId) throws Exception;
}
