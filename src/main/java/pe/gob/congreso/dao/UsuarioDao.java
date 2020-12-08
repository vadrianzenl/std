package pe.gob.congreso.dao;

import com.google.common.base.Optional;
import java.util.List;
import pe.gob.congreso.model.Usuario;

public interface UsuarioDao {

    public List<Usuario> findBy(Optional<String> nombre) throws Exception;

    public Optional<Usuario> findByNameUsuario(String nombreUsuario) throws Exception;
    
    public Optional<Usuario> findByIdEmpleado(Integer idEmpleado) throws Exception;

}
