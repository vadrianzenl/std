package pe.gob.congreso.dao;

import java.util.List;
import pe.gob.congreso.model.Categoria;
import pe.gob.congreso.model.util.InputSelectUtil;

public interface CategoriaDao {

    public Categoria create(Categoria d) throws Exception;

    public List<Categoria> findBy() throws Exception;

    public Categoria getCategoriaId(String id) throws Exception;

    public List<InputSelectUtil> getCategoriasInputSelect() throws Exception;
}
