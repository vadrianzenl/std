package pe.gob.congreso.service;

import java.util.List;

import pe.gob.congreso.model.Grupo;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.InputSelectUtil;

public interface GrupoService {

    public Object create(Usuario usuario, Grupo grupo, String operacion) throws Exception;

    public List<Grupo> findBy() throws Exception;

    public List<InputSelectUtil> find() throws Exception;

    public Grupo getGrupoId(String id) throws Exception;
}
