package pe.gob.congreso.dao;

import java.util.List;

import pe.gob.congreso.model.util.EnviadoUtil;

public interface DerivaMPDao {

    public List<EnviadoUtil> findEnviados(Integer fichaDocumentoId) throws Exception;

}
