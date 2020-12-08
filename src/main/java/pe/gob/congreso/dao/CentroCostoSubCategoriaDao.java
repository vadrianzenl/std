package pe.gob.congreso.dao;

import com.google.common.base.Optional;
import java.util.List;
import java.util.Map;
import pe.gob.congreso.model.CentroCostoSubCategoria;

public interface CentroCostoSubCategoriaDao {

    public CentroCostoSubCategoria create(CentroCostoSubCategoria fd) throws Exception;

    public Map<String, Object> find(Optional<String> centroCostoId, Optional<String> id) throws Exception;

    public List<CentroCostoSubCategoria> getSubCategoriasByCC(String centroCostoId) throws Exception;
}
