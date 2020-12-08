package pe.gob.congreso.dao;

import java.util.List;
import pe.gob.congreso.model.Motivo;

public interface MotivoDao {

    public List<Motivo> findBy() throws Exception;

    public Motivo getMotivoId(Integer id) throws Exception;

    public List<Motivo> getMotivoCentroCosto(String id) throws Exception;

    public Motivo create(Motivo motivo) throws Exception;
}
