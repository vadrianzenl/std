package pe.gob.congreso.dao;

import java.util.List;
import pe.gob.congreso.model.Relaciona;

public interface RelacionaDao {

    public Relaciona create(Relaciona d) throws Exception;

    public List<Relaciona> findRelacionados(String fichaDocumentoId) throws Exception;

    public List<Relaciona> findAsociados(String fichaDocumentoId) throws Exception;

    public List<Integer> findFichaIdRelacionados(String fichaDocumentoId) throws Exception;
    
    public List<Relaciona> findByTipo(String fichaDocumentoId, Integer tipo) throws Exception;
}
