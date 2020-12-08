package pe.gob.congreso.dao;

import java.util.List;

import pe.gob.congreso.model.EnviadoExterno;

public interface EnviadoExternoDao {

    public EnviadoExterno create(EnviadoExterno d) throws Exception;

    public EnviadoExterno findEnviadoPor(String fichaDocumentoId) throws Exception;
    
    public List<EnviadoExterno> findEnviadoA(String fichaDocumentoId) throws Exception;
    
    public EnviadoExterno findById(Integer id) throws Exception;

}
