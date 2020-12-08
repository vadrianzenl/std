package pe.gob.congreso.service;

import java.util.List;
import pe.gob.congreso.model.PeriodoLegislativo;

public interface PeriodoLegislativoService {

    public List<PeriodoLegislativo> findBy() throws Exception;

    public PeriodoLegislativo getPeriodoLegislativoId(String codigo) throws Exception;
}
