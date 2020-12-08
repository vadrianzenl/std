package pe.gob.congreso.service;

import java.util.List;
import java.util.Map;

import pe.gob.congreso.model.Categoria;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.InputSelectUtil;

public interface CategoriaService {

    public List<Categoria> getCategorias() throws Exception;

    public Object getCategoriaId(String id) throws Exception;
    
    public List<InputSelectUtil> getCategoriasInputSelect() throws Exception;
    
    public Map<String, Object> getCategoriaCentroCosto(String centroCostoId) throws Exception;
    
    public Object createCategoria(Usuario usuario, Categoria c, String operacion) throws Exception;
    
    public Object editCategoria(Usuario usuario, Categoria c, String operacion) throws Exception;
    
    
}
