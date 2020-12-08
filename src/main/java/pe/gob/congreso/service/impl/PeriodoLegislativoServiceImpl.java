
package pe.gob.congreso.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.congreso.dao.PeriodoLegislativoDao;
import pe.gob.congreso.model.PeriodoLegislativo;
import pe.gob.congreso.service.PeriodoLegislativoService;

@Service("periodoLegislativoService")
@Transactional
public class PeriodoLegislativoServiceImpl implements PeriodoLegislativoService{

    
    @Autowired
    PeriodoLegislativoDao periodoLegislativoDao;
    
    @Override
    public List<PeriodoLegislativo> findBy() throws Exception {
        return periodoLegislativoDao.findBy();
    }

    @Override
    public PeriodoLegislativo getPeriodoLegislativoId(String codigo) throws Exception {
        return periodoLegislativoDao.getPeriodoLegislativoId(codigo);
    }
    
}
