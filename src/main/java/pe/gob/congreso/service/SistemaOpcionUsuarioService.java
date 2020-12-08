package pe.gob.congreso.service;

import java.util.List;

import com.google.common.base.Optional;

import pe.gob.congreso.model.SistemaOpcionUsuario;

public interface SistemaOpcionUsuarioService {

	public List<SistemaOpcionUsuario> findByEmpleadoId(Optional<String> empleadoId);
	
}
