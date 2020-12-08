
package pe.gob.congreso.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.congreso.dao.GrupoDao;
import pe.gob.congreso.model.Grupo;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.service.ActividadService;
import pe.gob.congreso.service.GrupoService;

@Service("grupoService")
@Transactional
public class GrupoServiceImpl implements GrupoService{

    @Autowired
    GrupoDao grupoDao;
    
    @Autowired
    ActividadService actividadService;
    
    @Override
    public Object create(Usuario usuario, Grupo grupo, String operacion) throws Exception {
    	actividadService.create(usuario, grupo, operacion);
        return grupoDao.create(grupo);
    }

    @Override
    public List<Grupo> findBy() throws Exception {
        return grupoDao.findBy();
    }

    @Override
    public List<InputSelectUtil> find() throws Exception {
        return grupoDao.find();
    }

    @Override
    public Grupo getGrupoId(String id) throws Exception {
        return grupoDao.getGrupoId(id);
    }
    
}
