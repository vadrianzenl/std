package pe.gob.congreso.dao;

import java.util.List;
import pe.gob.congreso.model.Grupo;
import pe.gob.congreso.model.util.InputSelectUtil;

public interface GrupoDao {

    public Grupo create(Grupo grupo) throws Exception;

    public List<Grupo> findBy() throws Exception;

    public List<InputSelectUtil> find() throws Exception;

    public Grupo getGrupoId(String id) throws Exception;
}
