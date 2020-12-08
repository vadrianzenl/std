package pe.gob.congreso.dao;

import java.util.List;
import pe.gob.congreso.model.MotivoDeriva;

public interface MotivoDerivaDao {

    public List<MotivoDeriva> findBy() throws Exception;

    public List<MotivoDeriva> getMotivoDerivaCentroCosto(String id) throws Exception;

    public MotivoDeriva create(MotivoDeriva motivoDeriva) throws Exception;

}
