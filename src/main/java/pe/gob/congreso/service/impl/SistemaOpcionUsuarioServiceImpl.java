package pe.gob.congreso.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import pe.gob.congreso.dao.SistemaOpcionUsuarioDao;
import pe.gob.congreso.model.SistemaOpcionUsuario;
import pe.gob.congreso.service.SistemaOpcionUsuarioService;

@Service("sistemaOpcionUsuarioService")
@Transactional
public class SistemaOpcionUsuarioServiceImpl implements SistemaOpcionUsuarioService {

	@Autowired
	SistemaOpcionUsuarioDao sistemaOpcionUsuarioDao;
	
	@Override
	public List<SistemaOpcionUsuario> findByEmpleadoId(Optional<String> empleadoId) {
		return sistemaOpcionUsuarioDao.findByEmpleadoId(empleadoId);
	}

}
