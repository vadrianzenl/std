package pe.gob.congreso.dao;

import java.util.List;


import pe.gob.congreso.model.SeguimientoFisico;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.util.SeguimientoFisicoUtil;

public interface FisicoDao {

    public SeguimientoFisico create(SeguimientoFisico fisico) throws Exception;
    public List<SeguimientoFisico> findRecibiFisico(String derivaId,String estado) throws Exception;
    public List<SeguimientoFisicoUtil> findDirigidosByFichaId(String fichaDocumentoId);
    public List<SeguimientoFisico> findRecibiFisicoFichaId(String fichaDocumentoId,String estado) throws Exception;
    public List<SeguimientoFisico> findRecibiFisicoFichaIdAndEmpleado(String fichaDocumentoId,String centroCosto) throws Exception;
    public List<SeguimientoFisico> findRecibiFisicoById(String derivaId) throws Exception;
}
