package pe.gob.congreso.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import pe.gob.congreso.dao.EnvioMultipleDao;
import pe.gob.congreso.dao.FichaProveidoDao;
import pe.gob.congreso.dao.TipoDao;
import pe.gob.congreso.model.Deriva;
import pe.gob.congreso.model.EnvioMultiple;
import pe.gob.congreso.model.FichaProveido;
import pe.gob.congreso.model.MpCanalAsociadoEnvio;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.model.util.Util;
import pe.gob.congreso.service.ActividadService;
import pe.gob.congreso.service.CanalAsociadoEnvioService;
import pe.gob.congreso.service.EnvioMultipleService;
import pe.gob.congreso.util.Constantes;

@Service("envioMultipleService")
@Transactional
public class EnvioMultipleServiceImpl implements EnvioMultipleService {

	@Autowired
	EnvioMultipleDao envioMultipleDao;

	@Autowired
	TipoDao tipoDao;

	@Autowired
	ActividadService actividadService;
	
	@Autowired
	CanalAsociadoEnvioService canalAsociadoEnvioService;
	
	@Autowired
	FichaProveidoDao fichaProveidoDao;
	
	
	protected final Log log = LogFactory.getLog(getClass());


	@Override
	public List<EnvioMultiple> getByIdFk(Optional<String> fichaDocumentoId, Optional<String> proveidoId)
			throws Exception {
		return envioMultipleDao.getByIdFk(fichaDocumentoId, proveidoId);
	}

	@Override
	public Object createEnvioMultiple(Usuario usuario, EnvioMultiple em, String operacion) throws Exception {
		Date fecha = new Date();
		if ( !em.getHabilitado() ) {
			actividadService.create(usuario, em, Constantes.OPERACION_ACTUALIZAR);
			em.setUsuarioModifica(usuario.getNombreUsuario());
			em.setFechaModifica(fecha);
		} else {
			if ( Constantes.OPERACION_ACTUALIZAR.equals(operacion) ) {
				actividadService.create(usuario, em, operacion);
				em.setUsuarioModifica(usuario.getNombreUsuario());
				em.setFechaModifica(fecha);
			} else {
				actividadService.create(usuario, em, operacion);
				em.setUsuarioCrea(usuario.getNombreUsuario());
				em.setFechaCrea(fecha);
			}
		}		
		return envioMultipleDao.createEnvioMultiple(em);
	}
	
	@Override
	public Object realizarEnvioMultiple(Usuario usuario, EnvioMultiple[] enviosMultiples, String operacion) throws Exception {
		EnvioMultiple primerEnvio = Util.validarIdEnvioMultiple(enviosMultiples);
		List<EnvioMultiple> list = getByIdFk(Optional.fromNullable(primerEnvio.getFichaDocumentoId().toString()), Optional.fromNullable(primerEnvio.getProveidoId().toString()));
 	    if ( list.isEmpty() ) {
 	    	list = new ArrayList<>();
 	    	operacion = Constantes.OPERACION_CREAR;
 	 	    for ( EnvioMultiple em : enviosMultiples) {
 	 		    String centroCostoId = em.getCentroCostoId().toString().trim();
				em.setCanalEnvio( bucarCanalEnvioXCentroCosto(centroCostoId) );
 	 		    em.setHabilitado(Constantes.HABILITADO);
 		        list.add((EnvioMultiple) createEnvioMultiple(usuario, em, operacion)); //Crea todos
 	 	    }
 	    } else {
 	    	for ( EnvioMultiple em : enviosMultiples) {
 	    		EnvioMultiple emBuscarXCentroCosto = Util.buscarEnviosAnterioresXCentroCosto(list, em);
 	 		   if ( emBuscarXCentroCosto == null ) {
 	 			   operacion = Constantes.OPERACION_CREAR;
 	 			   String centroCostoId = em.getCentroCostoId().toString().trim();
 				   em.setCanalEnvio( bucarCanalEnvioXCentroCosto(centroCostoId) );
  	 		       em.setHabilitado(Constantes.HABILITADO);
 	 			   createEnvioMultiple(usuario, em, operacion); // Lo crea
 	 		   } else {
 	 			   if ( Constantes.SIN_REPORTE.equals(emBuscarXCentroCosto.getReporte()) ) {
 	 				   operacion = Constantes.OPERACION_ACTUALIZAR;
 	 				   emBuscarXCentroCosto.setHabilitado(Constantes.NO_HABILITADO);
 		    		   createEnvioMultiple(usuario, emBuscarXCentroCosto, operacion); // Lo quita
 	 			   }
 	 		   }
 	    	}
 	    }	
	    return list;
	}

	public String bucarCanalEnvioXCentroCosto(String centroCostoId) throws Exception {
		List<MpCanalAsociadoEnvio> canalEnvios = canalAsociadoEnvioService.getCanalAsociado("", centroCostoId.trim(), "");
		MpCanalAsociadoEnvio canalEnvio = canalEnvios != null && canalEnvios.size() > 0 ? canalEnvios.get(0) : null;
		String canal = canalEnvio != null ? canalEnvio.getIndCanal() : Constantes.ENVIO_PARA_ESTAFETA.toString();
		return canal;
	}

	@Override
	public Map<String,Object> validaEnvioMultipleEnReporte(EnvioMultiple envioMultiple) throws Exception {
		Map<String,Object> result =new HashMap<>();
		Boolean enReporte = false;
		Integer reporteId = null;
		List<EnvioMultiple> list = getByIdFk(Optional.fromNullable(envioMultiple.getFichaDocumentoId().toString()), Optional.fromNullable(envioMultiple.getProveidoId().toString()));
	    EnvioMultiple emBuscarPorCentroCosto = Util.buscarEnviosAnterioresXCentroCosto(list, envioMultiple);
	    if ( emBuscarPorCentroCosto != null && Constantes.CON_REPORTE.equals(emBuscarPorCentroCosto.getReporte())) {
	    	enReporte = true;
	    	reporteId = emBuscarPorCentroCosto.getGestionEnvioId();
	    }
	    result.put("enReporte",enReporte);
	    result.put("reporteId",reporteId);
	    return result;
	}

	@Override
	public List<EnvioMultiple> getByIdDocumentoIdReporte(Optional<String> fichaDocumentoId,
			Optional<String> gestionEnvioId) throws Exception {
		return envioMultipleDao.getByIdDocumentoIdReporte(fichaDocumentoId, gestionEnvioId);
	}
	
	public List<InputSelectUtil> getCantidadCasillerosPendientes(Map parametros) throws Exception {
        return envioMultipleDao.getCantidadCasillerosPendientes(parametros);
    }
	
	@Override
	public List<EnvioMultiple> getByIdCentroCosto(Optional<String> fichaDocumentoId, Optional<String> proveidoId, Optional<String> centroCostoId, Optional<String> canalEnvio) throws Exception {
		return envioMultipleDao.getByIdCentroCosto(fichaDocumentoId, proveidoId, centroCostoId, canalEnvio);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Object creaEnvioMultipleLastFichaProveido(Usuario usuario, String operacion, Deriva d, String canal) throws Exception {
		EnvioMultiple em  = new EnvioMultiple();
		em.setFichaDocumentoId(d.getFichaDocumento().getId());
 	    em.setProveidoId(Constantes.ID_PROVEIDO_MESA_DE_PARTES);
 	    em.setMultiple(Constantes.HABILITADO);
 	    em.setReporte(Constantes.SIN_REPORTE);
 	    em.setEstado(Constantes.ESTADO_ACTIVO);
 	    em.setSubsanado(Constantes.ESTADO_INACTIVO);
 	    em.setCanalEnvio(canal.trim());
 	    em.setCentroCostoId(d.getCentroCostoDestino().getId().trim());
 		em.setHabilitado(d.isHabilitado());
 		em = (EnvioMultiple) createEnvioMultiple(usuario, em, operacion);
		return em;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Object actualizaEnvioMultipleLastFichaProveido(Usuario usuario, String operacion, Deriva d, EnvioMultiple actualizaEnvio, String canal) throws Exception {    	
		if ( Constantes.NO_HABILITADO.equals(d.isHabilitado()) ) {
			List<EnvioMultiple> envios = getByIdCentroCosto(Optional.fromNullable(d.getFichaDocumento().getId().toString()), Optional.of( Constantes.ID_PROVEIDO_MESA_DE_PARTES.toString() ), Optional.fromNullable(d.getCentroCostoDestino().getId().trim()), Optional.fromNullable(canal.trim()) );
			if ( envios != null && !envios.isEmpty() ) {
				actualizaEnvio = envios.get(0);
				actualizaEnvio.setHabilitado( Constantes.NO_HABILITADO);
				actualizaEnvio.setUsuarioModifica(usuario.getNombreUsuario());
				actualizaEnvio.setFechaModifica(new Date());
				actualizaEnvio = (EnvioMultiple) createEnvioMultiple(usuario, actualizaEnvio, operacion);
			}
		}
		return actualizaEnvio;
	}

	@Override
	public EnvioMultiple crearEnvio(Usuario usuario, String operacion, Deriva d) throws Exception {
		EnvioMultiple envioMultiple  = new EnvioMultiple();
		if ( Constantes.PERFIL_RESPONSABLE_MESA_PARTES.equals(usuario.getPerfil().getId().trim()) && d.isEsResponsable() && d.getDirigido().intValue() != 3 ) {
	    	EnvioMultiple em  = new EnvioMultiple();
	 	    em.setCanalEnvio(this.bucarCanalEnvioXCentroCosto(d.getCentroCostoDestino().getId().toString().trim()) );
 			if ( Constantes.NO_HABILITADO.equals(d.isHabilitado()) ) {
 				List<EnvioMultiple> envios = getByIdCentroCosto(Optional.fromNullable(d.getFichaDocumento().getId().toString()), Optional.of( Constantes.ID_PROVEIDO_MESA_DE_PARTES.toString() ), Optional.fromNullable(d.getCentroCostoDestino().getId().trim()), Optional.fromNullable(em.getCanalEnvio().trim()) );
 				if ( envios != null && !envios.isEmpty() ) {
 					EnvioMultiple actualizaEnvio = envios.get(0);
 	 				actualizaEnvio.setHabilitado( Constantes.NO_HABILITADO);
 	 				actualizaEnvio.setUsuarioModifica(usuario.getNombreUsuario());
 	 				actualizaEnvio.setFechaModifica(new Date());
 	 				envioMultiple = (EnvioMultiple) createEnvioMultiple(usuario, actualizaEnvio, operacion);	
 				} 				
 			} else {
 				List<EnvioMultiple> envios = getByIdCentroCosto(Optional.fromNullable(d.getFichaDocumento().getId().toString()), Optional.of( Constantes.ID_PROVEIDO_MESA_DE_PARTES.toString() ), Optional.fromNullable(d.getCentroCostoDestino().getId().trim()), Optional.fromNullable(em.getCanalEnvio().trim()) );
 				if ( envios == null || envios.isEmpty() ) {
 					em.setProveidoId(Constantes.ID_PROVEIDO_MESA_DE_PARTES);
 					em.setFichaDocumentoId(d.getFichaDocumento().getId());
 					em.setHabilitado( Constantes.HABILITADO);
 					em.setCentroCostoId(d.getCentroCostoDestino().getId().trim());
 					em.setReporte(Constantes.REPORTE_PENDIENTE_ENVIO_MULTIPLE);
 					em.setEstado(Constantes.ESTADO_ACTIVO);
 					em.setSubsanado(String.valueOf(Constantes.NO_SUBSANADO));
 	 				em.setUsuarioCrea(usuario.getNombreUsuario());
 	 				em.setFechaCrea(new Date());
 	 				envioMultiple = (EnvioMultiple) createEnvioMultiple(usuario, em, Constantes.OPERACION_CREAR);	
 				}
 			}
	    }
		return envioMultiple;
	}

}