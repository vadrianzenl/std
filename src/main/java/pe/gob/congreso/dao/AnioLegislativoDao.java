package pe.gob.congreso.dao;

import java.util.Date;
import java.util.List;
import pe.gob.congreso.model.AnioLegislativo;

public interface AnioLegislativoDao {

    public List<AnioLegislativo> findBy() throws Exception;

    public AnioLegislativo getAnioLegislativoId(String codigo) throws Exception;

    public List<AnioLegislativo> getAnioLegislativoPeriodoLegislativo(String id) throws Exception;

    public AnioLegislativo getAnioActual() throws Exception;
    
}
