
package pe.gob.congreso.service.impl;

import com.google.common.base.Optional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.congreso.dao.LegislaturaDao;
import pe.gob.congreso.model.Legislatura;
import pe.gob.congreso.service.LegislaturaService;

@Service("legislaturaService")
@Transactional
public class LegislaturaServiceImpl implements LegislaturaService{

    @Autowired
    LegislaturaDao legislaturaDao;
    
    
    @Override
    public List<Legislatura> findBy(Optional<String> codigo) throws Exception {
        return legislaturaDao.findBy(codigo);
    }

    @Override
    public Legislatura getLegislaturaId(String codigo) throws Exception {
        return legislaturaDao.getLegislaturaId(codigo);
    }

    @Override
    public Legislatura getLegislaturaActual() throws Exception {
        return legislaturaDao.getLegislaturaActual();
    }
    
}
