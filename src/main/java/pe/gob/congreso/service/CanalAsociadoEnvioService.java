package pe.gob.congreso.service;

import java.util.List;

import pe.gob.congreso.model.GrupoCentroCosto;
import pe.gob.congreso.model.MpCanalAsociadoEnvio;

public interface CanalAsociadoEnvioService {

		public List<MpCanalAsociadoEnvio> getCanalAsociado(String idGrupo, String idDependencia, String idEmpleado) throws Exception;
		
		public List<MpCanalAsociadoEnvio> getCanalAsociadoByIndCanal(String indCanal, String estado, Boolean habilitado) throws Exception;
		
		public MpCanalAsociadoEnvio create(GrupoCentroCosto gcc, String operacion) throws Exception;

}
