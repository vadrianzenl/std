package pe.gob.congreso.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.congreso.dao.FiltroAnioDao;
import pe.gob.congreso.service.FiltroAnioService;

@Service("filtroAnioService")
@Transactional
public class FiltroAnioServiceImpl implements FiltroAnioService {

	@Autowired
    FiltroAnioDao filtroAnioDao;
	
	@Override
	public Object buscarPermisos(String centroCosto, Integer tipoAnio) throws Exception {
		return filtroAnioDao.buscarPermisos(centroCosto, tipoAnio);
	}

}
