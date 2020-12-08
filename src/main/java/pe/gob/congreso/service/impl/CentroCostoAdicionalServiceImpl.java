
package pe.gob.congreso.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.congreso.dao.CentroCostoAdicionalDao;
import pe.gob.congreso.model.CentroCostoAdicional;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.ActividadService;
import pe.gob.congreso.service.CentroCostoAdicionalService;

@Service("centroCostoAdicionalService")
@Transactional
public class CentroCostoAdicionalServiceImpl implements CentroCostoAdicionalService{

    @Autowired
    CentroCostoAdicionalDao centroCostoAdicionalDao;
    
    @Autowired
    ActividadService actividadService;
    
    @Override
    public Object create(Usuario usuario, CentroCostoAdicional cc, String operacion) throws Exception {
    	actividadService.create(usuario, cc, operacion);
        return centroCostoAdicionalDao.create(cc);
    }

    @Override
    public List<CentroCostoAdicional> findBy() throws Exception {
        return centroCostoAdicionalDao.findBy();
    }
    
}
