package pe.gob.congreso.dao;

import java.util.List;

import pe.gob.congreso.model.MpCanalAsociadoEnvio;

public interface CanalAsociadoEnvioDao {

	public List<MpCanalAsociadoEnvio> getCanalAsociado(String idGrupo, String idDependencia, String idEmpleado) throws Exception;
	
	public List<MpCanalAsociadoEnvio> getCanalAsociadoByIndCanal(String indCanal, String estado, Boolean habilitado) throws Exception;
	
	public MpCanalAsociadoEnvio create(MpCanalAsociadoEnvio t) throws Exception;

}
