package pe.gob.congreso.dao;

import java.util.List;
import pe.gob.congreso.model.Responsable;

public interface ResponsableDao {

    public Responsable create(Responsable d) throws Exception;

    public List<Responsable> findResponsables(String centroCostoId) throws Exception;

    public List<Responsable> findResponsablesGrupo(String grupoId) throws Exception;
}
