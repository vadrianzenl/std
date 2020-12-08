package pe.gob.congreso.dao;

import java.util.List;
import pe.gob.congreso.model.Bitacora;
import pe.gob.congreso.model.util.SeguimientoUtil;

public interface BitacoraDao {

    public Bitacora create(Bitacora d) throws Exception;

    public List<Bitacora> findBitacoras(String fichaDocumentoId) throws Exception;
    
    public List<SeguimientoUtil> getSeguimiento(String fichaDocumentoId) throws Exception;
}
