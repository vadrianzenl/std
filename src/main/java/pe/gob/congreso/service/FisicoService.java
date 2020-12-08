package pe.gob.congreso.service;

import java.util.List;

import pe.gob.congreso.model.SeguimientoFisico;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.util.SeguimientoFisicoUtil;

public interface FisicoService {

    public Object recibiConforme(Usuario usuario, SeguimientoFisico d, String operacion, String motivo,String tipo) throws Exception;
    public Object updateRecibiConforme(Usuario usuario, SeguimientoFisico d, String operacion, String motivo,String tipo,
    		String fichaId) throws Exception;
    public Object updateRecibiConformeBitacora(Usuario usuario, SeguimientoFisico d, String operacion, String motivo,String tipo,
    		String fichaId) throws Exception;
    public List<SeguimientoFisico> findRecibiFisico(Usuario usuario,String derivaId,String estado) throws Exception;
    public List<SeguimientoFisicoUtil> findDirigidosByFichaId(String fichaId);
    public List<SeguimientoFisico> findRecibiFisicoFichaId(Usuario usuario,String FichaDocumentoId,String estado) throws Exception;
    public List<SeguimientoFisico> findRecibiFisicoFichaIdEmpleadoId(Usuario usuario,String fichaDocumentoId,String centroCosto) throws Exception;
    public List<SeguimientoFisico> findRecibiFisicoById(Usuario usuario,String derivaId) throws Exception;
}
