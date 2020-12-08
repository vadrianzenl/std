package pe.gob.congreso.dao;

import java.util.List;

import com.google.common.base.Optional;


import pe.gob.congreso.model.MpDetGestionEnvio;

public interface DetGestionEnvioDao {

	public MpDetGestionEnvio getById(Optional<String> id) throws Exception;
		
	public MpDetGestionEnvio create(MpDetGestionEnvio cargo) throws Exception;
	
	public Integer updateMpDetGestionEnvio(MpDetGestionEnvio cargo) throws Exception;
	

}
