package pe.gob.congreso.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.congreso.dao.EstadoDerivaDao;
import pe.gob.congreso.model.EstadoDeriva;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.ActividadService;
import pe.gob.congreso.service.EstadoDerivaService;

@Service("estadoDerivaService")
@Transactional
public class EstadoDerivaServiceImpl implements EstadoDerivaService {

    @Autowired
    EstadoDerivaDao estadoDerivaDao;

    @Autowired
    ActividadService actividadService;

    @Override
    public List<EstadoDeriva> getEstadoDerivaCentroCosto(String id) throws Exception {
        return estadoDerivaDao.getEstadoDerivaCentroCosto(id);
    }

    @Override
    public Object create(Usuario usuario, EstadoDeriva estadoDeriva, String operacion) throws Exception {
    	actividadService.create(usuario, estadoDeriva, operacion);
        return estadoDerivaDao.create(estadoDeriva);
    }

}
