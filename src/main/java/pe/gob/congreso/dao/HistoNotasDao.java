package pe.gob.congreso.dao;

import java.util.List;

import pe.gob.congreso.model.HistoNotas;

public interface HistoNotasDao {

    public HistoNotas create(HistoNotas d) throws Exception;

    public List<HistoNotas> findHistoNotas(String fichaDocumentoId, String notasId) throws Exception;
    
    public HistoNotas getHistoNotas(String fichaDocumentoId, String notasId, String histoNotasId) throws Exception;
}
