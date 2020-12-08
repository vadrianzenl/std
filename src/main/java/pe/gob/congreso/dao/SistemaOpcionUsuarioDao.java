package pe.gob.congreso.dao;

import java.util.List;

import com.google.common.base.Optional;

import pe.gob.congreso.model.SistemaOpcionUsuario;

public interface SistemaOpcionUsuarioDao {
	
	public List<SistemaOpcionUsuario> findByEmpleadoId(Optional<String> empleadoId);

}
