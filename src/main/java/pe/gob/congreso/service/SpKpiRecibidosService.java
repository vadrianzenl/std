package pe.gob.congreso.service;

import pe.gob.congreso.vo.SpKpiRecibidosVO;

public interface SpKpiRecibidosService {

	SpKpiRecibidosVO getKpiRecibidos(String idempleado, String ccosto, String fecha) throws Exception;

}
