package pe.gob.congreso.service;

import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;

import pe.gob.congreso.model.GrupoCentroCosto;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.InputSelectUtil;

public interface GrupoCentroCostoService {

    public Map<String, Object> findBy(Optional<String> descripcion, Optional<String> pag, Optional<String> pagLength) throws Exception;

    public GrupoCentroCosto getCentroCostoActual(String id) throws Exception;

    public List<InputSelectUtil> getCentrosCostoByGrupo(String grupoId) throws Exception;

    public List<GrupoCentroCosto> getCentrosCosto(String grupoId) throws Exception;

    public List<InputSelectUtil> getCentroCostoActualInputSelect() throws Exception;

    public Map<String, Object> find(Optional<String> grupo, Optional<String> dependencia, Optional<String> pag, Optional<String> pagLength) throws Exception;

    public List<GrupoCentroCosto> getGrupoCentroCosto() throws Exception;

    public Object create(Usuario usuario, GrupoCentroCosto gcc, String operacion) throws Exception;

	public List<GrupoCentroCosto> getCentrosCostoByGrupoNotIn(String[] listGruposId)  throws Exception;
}
