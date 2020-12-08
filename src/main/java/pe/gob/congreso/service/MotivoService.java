package pe.gob.congreso.service;

import java.util.List;

import pe.gob.congreso.model.Motivo;
import pe.gob.congreso.model.MotivoDeriva;
import pe.gob.congreso.model.Usuario;

public interface MotivoService {

    public List<Motivo> findBy() throws Exception;

    public Motivo getMotivoId(Integer id) throws Exception;

    public List<Motivo> getMotivoCentroCosto(String id) throws Exception;

    public Object create(Usuario usuario, Motivo motivo, String operacion) throws Exception;

    public Object createMotivoDeriva(Usuario usuario, MotivoDeriva motivoDeriva, String operacion) throws Exception;

    public List<MotivoDeriva> getMotivoDerivaCentroCosto(String id) throws Exception;
}
