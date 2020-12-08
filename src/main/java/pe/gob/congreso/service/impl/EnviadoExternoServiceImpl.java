package pe.gob.congreso.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.congreso.dao.EnviadoExternoDao;
import pe.gob.congreso.model.EnviadoExterno;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.ActividadService;
import pe.gob.congreso.service.EnviadoExternoService;

@Service("enviadoExternoService")
@Transactional
public class EnviadoExternoServiceImpl implements EnviadoExternoService {

    @Autowired
    ActividadService actividadService;

    @Autowired
    EnviadoExternoDao enviadoExternoDao;

    @Override
    public Object create(Usuario usuario, EnviadoExterno ee, String operacion) throws Exception {
    	actividadService.create(usuario, ee, operacion);
        return enviadoExternoDao.create(ee);
    }

    @Override
    public EnviadoExterno findEnviadoPor(String fichaDocumentoId) throws Exception {
        return enviadoExternoDao.findEnviadoPor(fichaDocumentoId);
    }

	@Override
	public List<EnviadoExterno> findEnviadoA(String fichaDocumentoId) throws Exception {
		return enviadoExternoDao.findEnviadoA(fichaDocumentoId);
	}

}
