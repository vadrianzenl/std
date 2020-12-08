
package pe.gob.congreso.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.congreso.dao.AnioLegislativoDao;
import pe.gob.congreso.model.AnioLegislativo;
import pe.gob.congreso.service.AnioLegislativoService;

@Service("anioLegislativoService")
@Transactional
public class AnioLegislativoServiceImpl implements AnioLegislativoService{

    @Autowired
    AnioLegislativoDao anioLegislativoDao;
    
    @Override
    public List<AnioLegislativo> findBy() throws Exception {
        return anioLegislativoDao.findBy();
    }

    @Override
    public AnioLegislativo getAnioLegislativoId(String codigo) throws Exception {
        return anioLegislativoDao.getAnioLegislativoId(codigo);
    }

    @Override
    public List<AnioLegislativo> getAnioLegislativoPeriodoLegislativo(String id) throws Exception {
        return anioLegislativoDao.getAnioLegislativoPeriodoLegislativo(id);
    }

    @Override
    public AnioLegislativo getAnioActual() throws Exception {
        return anioLegislativoDao.getAnioActual();
    }
    
}
