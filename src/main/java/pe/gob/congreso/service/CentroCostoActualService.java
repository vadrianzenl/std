package pe.gob.congreso.service;

import com.google.common.base.Optional;
import java.util.List;
import java.util.Map;
import pe.gob.congreso.model.util.InputSelectUtil;

public interface CentroCostoActualService {

    public Map<String, Object> find(Optional<String> descripcion, Optional<String> pag, Optional<String> pagLength) throws Exception;

    public List<InputSelectUtil> getCentroCostoActualInputSelect() throws Exception;

}