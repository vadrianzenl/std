package pe.gob.congreso.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import pe.gob.congreso.dao.FichaProveidoDao;
import pe.gob.congreso.model.FichaProveido;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.ActividadService;
import pe.gob.congreso.service.FichaProveidoService;
import pe.gob.congreso.util.Constantes;


@Service("fichaProveidoService")
@Transactional(readOnly = true)
public class FichaProveidoServiceImpl implements FichaProveidoService {

	@Autowired
	FichaProveidoDao fichaProveidoDao;
	
	@Autowired
	ActividadService actividadService;
	
	@Override
	public List<FichaProveido> getByIdRU(Optional<String> documentoId, Optional<String> proveidoId) throws Exception {
		return fichaProveidoDao.findByIdRU(documentoId, proveidoId);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Object createFichaProveido(Usuario usuario, FichaProveido fp, String operacion)
			throws Exception {
		if ( Constantes.OPERACION_ACTUALIZAR.equals(operacion) ) {
			actividadService.create(usuario, fp, operacion);
			fp.setUsuarioModifica(usuario.getNombreUsuario());
			fp.setFechaModifica(new Date());
		} else {
			actividadService.create(usuario, fp, operacion);
			fp.setUsuarioCrea(usuario.getNombreUsuario());
			fp.setFechaCrea(new Date());
		}
		return fichaProveidoDao.create(fp);
	}

}