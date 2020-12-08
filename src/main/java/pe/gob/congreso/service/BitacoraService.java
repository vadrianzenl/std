
package pe.gob.congreso.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import pe.gob.congreso.model.Bitacora;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.SeguimientoUtil;

public interface BitacoraService {
    public Object create(Usuario usuario, Bitacora b, String operacion) throws Exception;

    public List<Bitacora> findBitacoras(String fichaDocumentoId) throws Exception;
    
    public Object reporteAcciones(HttpServletRequest request,Usuario usuario, String ru) throws Exception;
    
    public Object reporteModificaciones(HttpServletRequest request,Usuario usuario, String ru) throws Exception;
    
    public List<SeguimientoUtil> getSeguimiento(String fichaDocumentoId) throws Exception;
}
