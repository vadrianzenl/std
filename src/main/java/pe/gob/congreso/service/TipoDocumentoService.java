package pe.gob.congreso.service;

import java.util.List;

import pe.gob.congreso.model.TipoDocumento;
import pe.gob.congreso.model.Usuario;

public interface TipoDocumentoService {

    public TipoDocumento getTipoDocumentoId(Integer id) throws Exception;

    public List<TipoDocumento> getTipoDocumentoCentroCosto(String id) throws Exception;

    public Object create(Usuario usuario, TipoDocumento t, String operacion) throws Exception;
}
