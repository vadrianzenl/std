package pe.gob.congreso.service;

import java.util.List;

import pe.gob.congreso.model.EstadoDeriva;
import pe.gob.congreso.model.Usuario;

public interface EstadoDerivaService {

    public List<EstadoDeriva> getEstadoDerivaCentroCosto(String id) throws Exception;

    public Object create(Usuario usuario, EstadoDeriva estadoDeriva, String operacion) throws Exception;
}
