package pe.gob.congreso.dao;

import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.List;
import pe.gob.congreso.model.FichaProveido;

public interface FichaProveidoDao {

    public List<FichaProveido> find(Optional<String> proveidoId, Optional<String> codigo) throws Exception;

    public FichaProveido create(FichaProveido fp) throws Exception;
    
    public List<FichaProveido> findByIdRU(Optional<String> documentoId, Optional<String> proveidoId) throws Exception;
    
    public List<FichaProveido> findByNumeroMP(Optional<String> numeroMP, Optional<String> sumilla, Optional<String> ppIni, Optional<String> ppFin) throws Exception;
    
    public Integer updateEstadoFichaProveido(FichaProveido fichaProveido) throws Exception;
    
    public List<FichaProveido> findByEstadoMP(Optional<ArrayList<String>> estado, Optional<String> ppIni, Optional<String> ppFin) throws Exception;
    
}
