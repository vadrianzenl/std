package pe.gob.congreso.dao;

import com.google.common.base.Optional;
import java.util.List;
import pe.gob.congreso.model.SubCategoria;
import pe.gob.congreso.model.util.InputSelectUtil;

public interface SubCategoriaDao {

    public SubCategoria create(SubCategoria sd);

    public List<SubCategoria> findBy(Optional<String> categoriaId);

    public SubCategoria getSubCategoriaId(Integer id);

    public List<InputSelectUtil> getSubCategoríasInputSelect(Optional<String> categoriaId);
}
