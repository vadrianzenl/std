package pe.gob.congreso.service;

import com.google.common.base.Optional;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import pe.gob.congreso.model.PerfilMenu;
import pe.gob.congreso.model.Usuario;

public interface UsuarioService {

    public Usuario findByNameUsuario(String nombreUsuario) throws Exception;
    
    public List<PerfilMenu> findModules(String perfilId) throws Exception;

    public List<PerfilMenu> findMenus(String perfilId, String padreId) throws Exception;
    
    public Object getUsuarioInfo(HttpServletRequest request) throws Exception;
    
    public Object updateUsuarioInfo(HttpServletRequest request) throws Exception;
}
