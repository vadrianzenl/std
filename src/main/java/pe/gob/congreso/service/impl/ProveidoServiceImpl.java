
package pe.gob.congreso.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.congreso.dao.ProveidoDao;
import pe.gob.congreso.model.Proveido;
import pe.gob.congreso.service.ProveidoService;

@Service("proveidoService")
@Transactional
public class ProveidoServiceImpl implements ProveidoService{
    
    @Autowired
    ProveidoDao proveidoDao;

    @Override
    public List<Proveido> findBy() throws Exception {
        return proveidoDao.findBy();
    }

    @Override
    public Proveido getProveidoId(Integer id) throws Exception {
        return proveidoDao.getProveidoId(id);
    }

    @Override
    public Proveido getProveidoCentroCosto(String id) throws Exception {
        return proveidoDao.getProveidoCentroCosto(id);
    }
    
}
