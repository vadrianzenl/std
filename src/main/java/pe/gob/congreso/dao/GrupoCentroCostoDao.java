package pe.gob.congreso.dao;

import com.google.common.base.Optional;
import java.util.List;
import java.util.Map;
import pe.gob.congreso.model.GrupoCentroCosto;
import pe.gob.congreso.model.util.InputSelectUtil;

public interface GrupoCentroCostoDao {

    public Map<String, Object> findBy(Optional<String> descripcion, Optional<String> pag, Optional<String> pagLength) throws Exception;

    public List<GrupoCentroCosto> findByAll() throws Exception;

    public GrupoCentroCosto getCentroCostoActual(String id) throws Exception;

    public List<InputSelectUtil> getCentrosCostoByGrupo(String grupoId) throws Exception;

    public List<GrupoCentroCosto> getCentrosCosto(String grupoId) throws Exception;

    public List<InputSelectUtil> getCentroCostoActualInputSelect() throws Exception;

    public Map<String, Object> find(Optional<String> grupo, Optional<String> dependencia, Optional<String> pag, Optional<String> pagLength) throws Exception;

    public List<GrupoCentroCosto> getGrupoCentroCosto() throws Exception;

    public GrupoCentroCosto create(GrupoCentroCosto gcc) throws Exception;

	public List<GrupoCentroCosto> getCentrosCostoByGrupoNotIn(String[] listGruposId) throws Exception;
}
