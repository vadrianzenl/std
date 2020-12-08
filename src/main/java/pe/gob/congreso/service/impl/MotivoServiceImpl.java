package pe.gob.congreso.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.congreso.dao.MotivoDao;
import pe.gob.congreso.dao.MotivoDerivaDao;
import pe.gob.congreso.model.Motivo;
import pe.gob.congreso.model.MotivoDeriva;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.ActividadService;
import pe.gob.congreso.service.MotivoService;

@Service("motivoService")
@Transactional
public class MotivoServiceImpl implements MotivoService {

    @Autowired
    ActividadService actividadService;

    @Autowired
    MotivoDerivaDao motivoDerivaDao;

    @Autowired
    MotivoDao motivoDao;

    @Override
    public List<Motivo> findBy() throws Exception {
       return motivoDao.findBy();
    }

    @Override
    public Motivo getMotivoId(Integer id) throws Exception {
        return motivoDao.getMotivoId(id);
    }

    @Override
    public List<Motivo> getMotivoCentroCosto(String id) throws Exception {
        return motivoDao.getMotivoCentroCosto(id);
    }

    @Override
    public Object create(Usuario usuario, Motivo motivo, String operacion) throws Exception {
    	actividadService.create(usuario, motivo, operacion);
        return motivoDao.create(motivo);
    }

    @Override
    public Object createMotivoDeriva(Usuario usuario, MotivoDeriva motivoDeriva, String operacion) throws Exception {
        actividadService.create(usuario, motivoDeriva, operacion);
        return motivoDerivaDao.create(motivoDeriva);
    }

    @Override
    public List<MotivoDeriva> getMotivoDerivaCentroCosto(String id) throws Exception {
        return motivoDerivaDao.getMotivoDerivaCentroCosto(id);
    }

}
