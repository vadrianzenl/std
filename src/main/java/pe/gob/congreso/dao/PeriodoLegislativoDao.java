package pe.gob.congreso.dao;

import java.util.List;
import pe.gob.congreso.model.PeriodoLegislativo;

public interface PeriodoLegislativoDao {

    public List<PeriodoLegislativo> findBy() throws Exception;

    public PeriodoLegislativo getPeriodoLegislativoId(String codigo) throws Exception;
}
