package pe.gob.congreso.dao;

import java.util.List;
import pe.gob.congreso.model.Proveido;

public interface ProveidoDao {

    public List<Proveido> findBy() throws Exception;

    public Proveido getProveidoId(Integer id) throws Exception;

    public Proveido getProveidoCentroCosto(String id) throws Exception;
}
