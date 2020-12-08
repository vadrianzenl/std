package pe.gob.congreso.dao;

import java.util.List;

import pe.gob.congreso.model.SpKpiRecibidos;

public interface SpKpiRecibidosDao {

	List<SpKpiRecibidos> getKpiRecibidos(String idempleado, String ccosto, String fecha) throws Exception;

}
