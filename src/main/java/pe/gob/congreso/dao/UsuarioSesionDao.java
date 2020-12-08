package pe.gob.congreso.dao;

import com.google.common.base.Optional;
import java.util.List;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.UsuarioSesion;

public interface UsuarioSesionDao {

    public UsuarioSesion create(Usuario usuario, String sessionId) throws Exception;

    public Optional<UsuarioSesion> findByKey(String sesionKey) throws Exception;

    public List<UsuarioSesion> findAll() throws Exception;

    public void delete(UsuarioSesion s) throws Exception;
}
