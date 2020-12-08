package pe.gob.congreso.dao;

import com.google.common.base.Optional;
import java.util.List;
import java.util.Map;
import pe.gob.congreso.model.FichaSubcategoria;

public interface FichaSubcategoriaDao {

    public FichaSubcategoria create(FichaSubcategoria fs) throws Exception;

    public Map<String, Object> find(Optional<String> subCategoriaId, Optional<String> codigo) throws Exception;

    public Map<String, Object> findFichasCategoria(Optional<String> categoriaId, Optional<String> codigo) throws Exception;

    public Map<String, Object> findLegislatura(Optional<String> subCategoriaId, Optional<String> codigo) throws Exception;

    public List<FichaSubcategoria> findSubCategorias(String fichaDocumentoId) throws Exception;
}
