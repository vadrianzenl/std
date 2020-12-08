
package pe.gob.congreso.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import pe.gob.congreso.model.HistoNotas;
import pe.gob.congreso.model.Notas;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.SeguimientoUtil;

public interface NotasService {
    public Object create(Usuario usuario, Notas b, String operacion) throws Exception;

    public List<Notas> findNotas(Usuario usuario, String fichaDocumentoId) throws Exception;
    
    public HistoNotas createHistoNotas(Notas n) throws Exception;
}
