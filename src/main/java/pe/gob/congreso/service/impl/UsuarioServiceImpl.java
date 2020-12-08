package pe.gob.congreso.service.impl;

import com.google.common.base.Optional;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.congreso.dao.PerfilMenuDao;
import pe.gob.congreso.dao.UsuarioDao;
import pe.gob.congreso.model.PerfilMenu;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.UsuarioService;

@Service("usuarioService")
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	UsuarioDao usuarioDao;

	@Autowired
	PerfilMenuDao perfilMenuDao;

	protected final Log log = LogFactory.getLog(getClass());

	@Override
	public Usuario findByNameUsuario(String nombreUsuario) throws Exception {
		Optional<Usuario> optionalUser = usuarioDao.findByNameUsuario(nombreUsuario);
		Usuario user = optionalUser.get();
		return user;
	}

	@Override
	public List<PerfilMenu> findModules(String perfilId) throws Exception {
		return perfilMenuDao.findModules(perfilId);
	}

	@Override
	public List<PerfilMenu> findMenus(String perfilId, String padreId) throws Exception {
		return perfilMenuDao.findMenus(perfilId, padreId);
	}

	@Override
	public Object getUsuarioInfo(HttpServletRequest request) throws Exception {

		try {

			Optional<Usuario> optionalUser = (Optional<Usuario>) request.getSession().getAttribute("usuario");

			if (optionalUser.isPresent()) {
				Usuario usuario = optionalUser.get();
				log.info("X-FORWARDED-FOR: " + request.getHeader("X-FORWARDED-FOR"));
				System.out.println("X-FORWARDED-FOR: " + request.getHeader("X-FORWARDED-FOR"));
				log.info("remote ip: " + request.getRemoteAddr());
				System.out.println("remote ip: " + request.getRemoteAddr());
				String ipAddress = Optional.fromNullable(request.getHeader("X-FORWARDED-FOR")).or(request.getRemoteAddr());
				log.info("ipAddress: " + ipAddress);
				System.out.println("ipAddress: " + ipAddress);
				usuario.setIpAddress(ipAddress);
				return optionalUser.get();
			} else {
				return new Status(404, "Objeto no Encontrado");
			}
		} catch (Exception e) {
			return new Status(401, "Usuario no Autorizado");
		}
	}

	@Override
	public Object updateUsuarioInfo(HttpServletRequest request) throws Exception {
		log.info("Update User");
		try {

			Optional<Usuario> optionalUser = (Optional<Usuario>) request.getSession().getAttribute("usuario");

			if (optionalUser.isPresent()) {
				log.info("Centro de costo1: " + optionalUser.get().getEmpleado().getCentroCosto().getId());
				Optional<Usuario> usuario = usuarioDao.findByNameUsuario(optionalUser.get().getNombreUsuario());
				log.info("Centro de costo2: " + usuario.get().getEmpleado().getCentroCosto().getId());

				log.info("X-FORWARDED-FOR: " + request.getHeader("X-FORWARDED-FOR"));
				System.out.println("X-FORWARDED-FOR: " + request.getHeader("X-FORWARDED-FOR"));
				log.info("remote ip: " + request.getRemoteAddr());
				System.out.println("remote ip: " + request.getRemoteAddr());
				String ipAddress = Optional.fromNullable(request.getHeader("X-FORWARDED-FOR")).or(request.getRemoteAddr());
				log.info("ipAddress: " + ipAddress);
				System.out.println("ipAddress: " + ipAddress);

				usuario.get().setIpAddress(ipAddress);
				request.getSession(true).setAttribute("usuario", usuario);
				return usuario.get();
			} else {
				return new Status(404, "Objeto no Encontrado");
			}
		} catch (Exception e) {
			return new Status(401, "Usuario no Autorizado");
		}
	}

}
