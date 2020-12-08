package pe.gob.congreso.dao;

import com.google.common.base.Optional;
import java.util.List;
import java.util.Map;
import pe.gob.congreso.model.CentroCosto;
import pe.gob.congreso.model.util.InputSelectUtil;

public interface CentroCostoDao {

    public Map<String, Object> findBy(Optional<String> descripcion, Optional<String> pag, Optional<String> pagLength) throws Exception;

    public CentroCosto getCentroCostoId(String id) throws Exception;

    public List<InputSelectUtil> getCentrosCostoInputSelect() throws Exception;
}
