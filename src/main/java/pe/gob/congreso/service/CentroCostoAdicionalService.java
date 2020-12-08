package pe.gob.congreso.service;

import java.util.List;

import pe.gob.congreso.model.CentroCostoAdicional;
import pe.gob.congreso.model.Usuario;

public interface CentroCostoAdicionalService {

    public Object create(Usuario usuario, CentroCostoAdicional grupo, String operacion) throws Exception;

    public List<CentroCostoAdicional> findBy() throws Exception;
}
