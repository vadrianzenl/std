package pe.gob.congreso.dao;

import java.util.List;
import pe.gob.congreso.model.EstadoDeriva;

public interface EstadoDerivaDao {

    public List<EstadoDeriva> findBy() throws Exception;

    public List<EstadoDeriva> getEstadoDerivaCentroCosto(String id) throws Exception;

    public EstadoDeriva create(EstadoDeriva estadoDeriva) throws Exception;
}
