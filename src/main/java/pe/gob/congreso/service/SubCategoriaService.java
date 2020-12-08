package pe.gob.congreso.service;

import java.util.List;

import com.google.common.base.Optional;

import pe.gob.congreso.model.CentroCostoSubCategoria;
import pe.gob.congreso.model.SubCategoria;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.InputSelectUtil;

public interface SubCategoriaService {

    public Object createCategoria(Usuario usuario, SubCategoria sd, String operacion) throws Exception;

    public List<SubCategoria> findBy(Optional<String> categoriaId) throws Exception;

    public SubCategoria getSubCategoriaId(Integer id) throws Exception;

    public List<InputSelectUtil> getSubCategoríasInputSelect(Optional<String> categoriaId) throws Exception;

    public Object createCentroCostoSubCategoria(Usuario usuario, CentroCostoSubCategoria fd, String operacion ) throws Exception;

    public List<CentroCostoSubCategoria> getSubCategoriasByCC(String centroCostoId) throws Exception;
}
