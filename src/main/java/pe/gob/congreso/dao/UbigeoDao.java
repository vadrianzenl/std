package pe.gob.congreso.dao;

import java.util.List;

import pe.gob.congreso.model.UbigeoMaestro;

public interface UbigeoDao {

    public UbigeoMaestro getUbigeo(String codigo) throws Exception;

    public List<UbigeoMaestro> getDepartamentos() throws Exception;

    public List<UbigeoMaestro> getProvincias(String departamento) throws Exception;

    public List<UbigeoMaestro> getDistritos(String provincia) throws Exception;
}
