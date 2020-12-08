package pe.gob.congreso.dao;

import java.util.List;
import pe.gob.congreso.model.TipoDocumento;

public interface TipoDocumentoDao {

    public TipoDocumento getTipoDocumentoId(Integer id) throws Exception;

    public List<TipoDocumento> getTipoDocumentoCentroCosto(String id) throws Exception;

    public TipoDocumento create(TipoDocumento tipoDocumento) throws Exception;
}
