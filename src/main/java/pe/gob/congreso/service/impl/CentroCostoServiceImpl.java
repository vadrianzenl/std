
package pe.gob.congreso.service.impl;

import com.google.common.base.Optional;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.congreso.dao.CentroCostoDao;
import pe.gob.congreso.dao.GrupoDao;
import pe.gob.congreso.model.CentroCosto;
import pe.gob.congreso.model.Grupo;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.service.CentroCostoService;

@Service("centroCostoService")
@Transactional
public class CentroCostoServiceImpl implements CentroCostoService{

    @Autowired
    CentroCostoDao centroCostoDao;

    @Autowired
    GrupoDao grupoDao;
    
    @Override
    public Map<String, Object> findBy(Optional<String> descripcion, Optional<String> pag, Optional<String> pagLength) throws Exception {
        return centroCostoDao.findBy(descripcion, pag, pagLength);
    }

    @Override
    public CentroCosto getCentroCostoId(String id) throws Exception {
        return centroCostoDao.getCentroCostoId(id);
    }

    @Override
    public List<InputSelectUtil> getCentrosCostoInputSelect() throws Exception {
        return centroCostoDao.getCentrosCostoInputSelect();
    }

    @Override
    public Grupo getGrupoId(String id) throws Exception {
        return grupoDao.getGrupoId(id);
    }
    
}
