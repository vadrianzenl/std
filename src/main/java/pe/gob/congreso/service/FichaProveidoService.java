package pe.gob.congreso.service;

import java.util.List;

import com.google.common.base.Optional;

import pe.gob.congreso.model.FichaProveido;
import pe.gob.congreso.model.Usuario;

public interface FichaProveidoService {

	public List<FichaProveido> getByIdRU(Optional<String> documentoId, Optional<String> proveidoId) throws Exception;

	public Object createFichaProveido(Usuario datosSession, FichaProveido fichaProveido, String operacion) throws Exception;

}
