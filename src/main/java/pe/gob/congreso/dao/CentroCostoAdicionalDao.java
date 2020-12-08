package pe.gob.congreso.dao;

import java.util.List;
import pe.gob.congreso.model.CentroCostoAdicional;

public interface CentroCostoAdicionalDao {

    public CentroCostoAdicional create(CentroCostoAdicional cc) throws Exception;

    public List<CentroCostoAdicional> findBy() throws Exception;
    
}
