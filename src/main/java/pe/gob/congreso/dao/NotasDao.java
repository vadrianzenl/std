package pe.gob.congreso.dao;

import java.util.List;
import pe.gob.congreso.model.Notas;
import pe.gob.congreso.model.util.SeguimientoUtil;

public interface NotasDao {

    public Notas create(Notas d) throws Exception;

    public List<Notas> findNotas(String fichaDocumentoId) throws Exception;
    
    public List<Notas> findNotas(String fichaDocumentoId,String centroCostoId,String usuarioId) throws Exception;
    
    public Notas getNotas(String fichaDocumentoId, String id) throws Exception;
}
