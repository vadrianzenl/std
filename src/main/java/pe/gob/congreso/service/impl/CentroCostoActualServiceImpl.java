
package pe.gob.congreso.service.impl;

import com.google.common.base.Optional;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.congreso.dao.CentroCostoActualDao;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.service.CentroCostoActualService;

@Service("centroCostoActualService")
@Transactional
public class CentroCostoActualServiceImpl implements CentroCostoActualService{

    @Autowired
    CentroCostoActualDao centroCostoActualDao;
    
    @Override
    public Map<String, Object> find(Optional<String> descripcion, Optional<String> pag, Optional<String> pagLength) throws Exception {
        return centroCostoActualDao.find(descripcion, pag, pagLength);
    }

    @Override
    public List<InputSelectUtil> getCentroCostoActualInputSelect() throws Exception {
        return centroCostoActualDao.getCentroCostoActualInputSelect();
    }
    
}
