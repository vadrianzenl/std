
package pe.gob.congreso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.congreso.dao.CanalAsociadoEnvioDao;
import pe.gob.congreso.model.GrupoCentroCosto;
import pe.gob.congreso.model.MpCanalAsociadoEnvio;
import pe.gob.congreso.model.Responsable;
import pe.gob.congreso.service.CanalAsociadoEnvioService;
import pe.gob.congreso.service.GrupoCentroCostoService;
import pe.gob.congreso.util.Constantes;

@Service("canalAsociadoEnvioService")
@Transactional
public class CanalAsociadoEnvioServiceImpl implements CanalAsociadoEnvioService{
	
	protected final Log log = LogFactory.getLog(getClass());

    @Autowired
    CanalAsociadoEnvioDao canalAsociadoEnvioServiceDao;
    
    @Autowired
    GrupoCentroCostoService grupoCentroCostoService;

	@Override
	public MpCanalAsociadoEnvio create(GrupoCentroCosto gcc, String operacion) throws Exception {
		MpCanalAsociadoEnvio ce = new MpCanalAsociadoEnvio();
		List<MpCanalAsociadoEnvio> canales = getCanalAsociado(gcc.getGrupo().getId().toString(), gcc.getId(),Constantes.VACIO);		
		if ( canales.size() > 0 ) {						
			for(MpCanalAsociadoEnvio c: canales){				
				ce = c;
				ce.setHabilitado(Constantes.NO_HABILITADO);
				ce.setUsuarioModifica(gcc.getUsuarioCrea());
				ce.setFechaModifica(new Date());
				canalAsociadoEnvioServiceDao.create(ce);					
			}
		}
		ce = new MpCanalAsociadoEnvio();
		ce.setGrupoId(gcc.getGrupo().getId());
		ce.setDependenciaId(Integer.parseInt(gcc.getId()));
		ce.setEmpleadoId(gcc.getEmpleado().getId());
		ce.setEstado(Constantes.ESTADO_ACTIVO_CANAL_ENVIO_ASOCIADO);
		ce.setCargo(gcc.getCargo());
		ce.setHabilitado(Constantes.HABILITADO);
		ce.setUsuarioCrea(gcc.getUsuarioCrea());
		ce.setFechaCrea(new Date());		
		if(gcc.isHabilitado()){
			if (gcc.getIndCanal()) {
				ce.setIndCanal(Constantes.ENVIO_PARA_ESTAFETA.toString());	
			} else {
				ce.setIndCanal(Constantes.ENVIO_PARA_CASILLERO.toString());
			}
		}	
		return canalAsociadoEnvioServiceDao.create(ce);
	}
    
	@Override
	public List<MpCanalAsociadoEnvio> getCanalAsociado(String idGrupo, String idDependencia, String idEmpleado)
			throws Exception {
		return canalAsociadoEnvioServiceDao.getCanalAsociado(idGrupo, idDependencia, idEmpleado);
	}

	@Override
	public List<MpCanalAsociadoEnvio> getCanalAsociadoByIndCanal(String indCanal, String estado, Boolean habilitado)
			throws Exception {
		return canalAsociadoEnvioServiceDao.getCanalAsociadoByIndCanal(indCanal, estado, habilitado);
	}
    
    
}
