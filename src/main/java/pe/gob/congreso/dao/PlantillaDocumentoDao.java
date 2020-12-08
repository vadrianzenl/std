package pe.gob.congreso.dao;

import pe.gob.congreso.model.PlantillaDocumento;

public interface PlantillaDocumentoDao {
    public PlantillaDocumento findByTipoDocumento(Integer tipoDocumento) throws Exception;
}
