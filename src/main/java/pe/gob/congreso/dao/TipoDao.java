package pe.gob.congreso.dao;

import java.util.List;

import com.google.common.base.Optional;
import java.util.Map;
import pe.gob.congreso.model.Tipo;
import pe.gob.congreso.model.util.InputSelectUtil;

public interface TipoDao {

    public Tipo create(Tipo t) throws Exception;

    public Map<String, Object> find(Optional<String> nombre, Optional<String> descripcion, Optional<String> pag, Optional<String> pagLength) throws Exception;

    public List<Tipo> findBy(Optional<String> id, Optional<String> nombre) throws Exception;

    public List<Tipo> findByOrden(Optional<String> id, Optional<String> nombre) throws Exception;

    public Tipo findByTipo(String id, String nombre) throws Exception;

    public Tipo findByCodigo(String id) throws Exception;

    public Tipo findByNombre(String nombre) throws Exception;

    public Tipo findByTipoId(String descripcion, String nombre) throws Exception;

    public List<InputSelectUtil> getTiposInputSelect() throws Exception;

    public List<Tipo> findByAlfresco() throws Exception;

    public List<Tipo> findByCorreo() throws Exception;
    
    public Tipo findByTipoLike(String descripcion, String nombre) throws Exception;

}
