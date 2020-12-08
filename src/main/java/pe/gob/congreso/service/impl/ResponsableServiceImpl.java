
package pe.gob.congreso.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.congreso.dao.ResponsableDao;
import pe.gob.congreso.model.Responsable;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.ActividadService;
import pe.gob.congreso.service.ResponsableService;

@Service("responsableService")
@Transactional
public class ResponsableServiceImpl implements ResponsableService{

    @Autowired
    ResponsableDao responsableDao;

    @Autowired
    ActividadService actividadService;
    
    @Override
    public Object create(Usuario usuario, Responsable t, String operacion) throws Exception {
    	actividadService.create(usuario, t, operacion);
        return responsableDao.create(t);
    }

    @Override
    public List<Responsable> findResponsables(String centroCostoId) throws Exception {
        return responsableDao.findResponsables(centroCostoId);
    }

    @Override
    public List<Responsable> findResponsablesGrupo(String grupoId) throws Exception {
        return responsableDao.findResponsablesGrupo(grupoId);
    }
    
}
