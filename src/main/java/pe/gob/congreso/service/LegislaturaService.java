package pe.gob.congreso.service;

import com.google.common.base.Optional;
import java.util.List;
import pe.gob.congreso.model.Legislatura;

public interface LegislaturaService {

    public List<Legislatura> findBy(Optional<String> codigo) throws Exception;

    public Legislatura getLegislaturaId(String codigo) throws Exception;

    public Legislatura getLegislaturaActual() throws Exception;
}
