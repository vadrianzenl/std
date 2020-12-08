package pe.gob.congreso.dao;

import com.google.common.base.Optional;
import java.util.List;
import pe.gob.congreso.model.Legislatura;

public interface LegislaturaDao {

    public List<Legislatura> findBy(Optional<String> codigo) throws Exception;

    public Legislatura getLegislaturaId(String codigo) throws Exception;

    public Legislatura getLegislaturaActual() throws Exception;
}
