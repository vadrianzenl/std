package pe.gob.congreso.service;

import org.springframework.ui.Model;
import pe.gob.congreso.model.UsuarioSesion;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AuthService {

    public List<UsuarioSesion> findAll() throws Exception;

    public String signIn(HttpServletRequest request, Model model) throws Exception;
    
    public String signOut(HttpServletRequest request) throws Exception;
}
