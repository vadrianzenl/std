package pe.gob.congreso.service;

import java.util.List;

import pe.gob.congreso.model.Responsable;
import pe.gob.congreso.model.Usuario;

public interface ResponsableService {

    public Object create(Usuario usuario, Responsable t, String operacion) throws Exception;

    public List<Responsable> findResponsables(String centroCostoId) throws Exception;

    public List<Responsable> findResponsablesGrupo(String grupoId) throws Exception;
}
