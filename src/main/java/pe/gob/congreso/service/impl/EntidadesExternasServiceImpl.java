
package pe.gob.congreso.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.congreso.dao.EntidadesExternasDao;
import pe.gob.congreso.dao.UbigeoDao;
import pe.gob.congreso.model.EntidadExterna;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.ActividadService;
import pe.gob.congreso.service.EntidadesExternasService;
import pe.gob.congreso.util.Constantes;

@Service("entidadesExternasService")
@Transactional
public class EntidadesExternasServiceImpl implements EntidadesExternasService {
    
    @Autowired
	ActividadService actividadService;
	
    @Autowired
    EntidadesExternasDao entidadesExternasDao;
	
    @Autowired
    UbigeoDao ubigeoDao;

	
    @Override
    public EntidadExterna getEntidadesExternasById(String id) throws Exception {
    	return entidadesExternasDao.getEntidadesExternasById(id);
    }
    
    @Override
    public List<EntidadExterna> getEntidadesExternasByName(String name) throws Exception {
    	return entidadesExternasDao.getEntidadesExternasByName(name);
    }


    @Override
    public List<EntidadExterna> getEntidadesExternas() throws Exception {
    	return entidadesExternasDao.getEntidadesExternas();
    }

	@Override
	public EntidadExterna createEntidadExterna(Usuario usuario, EntidadExterna entidad, String operacion)
			throws Exception {
		EntidadExterna entidadExterna = null;
		List<EntidadExterna> entidadesExternaByName = null;
		EntidadExterna entidadExternaByName = null;
		entidadesExternaByName = getEntidadesExternasByName(entidad.getEntidad().toString());
		entidadExternaByName = entidadesExternaByName != null && !entidadesExternaByName.isEmpty() ? entidadesExternaByName.get(0): null;		
		if( entidadExternaByName != null ) {
			if( !entidadExternaByName.getId().equals(entidad.getId())   ) {
				return null;
			}
		}
		if ( entidad.getId() != null ) {
			entidadExterna = getEntidadesExternasById(entidad.getId().toString());
		}		
		if (entidadExterna == null) {
 		   operacion = Constantes.OPERACION_CREAR;
 		   actividadService.create(usuario, entidad, operacion);
 		   entidad.setUsuarioCrea(usuario.getNombreUsuario());
		   entidad.setFechaCrea(new Date());		   
		} else {
		   operacion = Constantes.OPERACION_ACTUALIZAR;
		   if ( !entidadExterna.getNombreJefe().equals(entidad.getNombreJefe()) || !entidadExterna.getApellidoJefe().equals(entidad.getApellidoJefe()) ||
				   !entidadExterna.getDNI().equals(entidad.getDNI()) || !entidadExterna.getTelefono().equals(entidad.getTelefono()) ||
				   !entidadExterna.getCargoJefe().equals(entidad.getCargoJefe()) || !entidadExterna.getDireccion().equals(entidad.getDireccion()) ||
				   !entidadExterna.getEntidad().equals(entidad.getEntidad()) || !entidadExterna.getUbigeo().equals(entidad.getUbigeo()) ||
				   !entidadExterna.getRUC().equals(entidad.getRUC()) || !entidadExterna.getPoder().equals(entidad.getPoder()) ||
				   !entidadExterna.getSector().equals(entidad.getSector()) || !entidadExterna.getFax().equals(entidad.getFax()) ||
				   !entidadExterna.getPaginaWeb().equals(entidad.getPaginaWeb()) || !entidadExterna.getTelefonoJefe().equals(entidad.getTelefonoJefe()) ||
				   !entidadExterna.getCorreoJefe().equals(entidad.getCorreoJefe()) ) {
 			  entidadExterna.setNombreJefe(entidad.getNombreJefe());
 			  entidadExterna.setApellidoJefe(entidad.getApellidoJefe());
 			  entidadExterna.setDNI(entidad.getDNI());
			  entidadExterna.setTelefono(entidad.getTelefono());
 			  entidadExterna.setCargoJefe(entidad.getCargoJefe());
 			  entidadExterna.setDireccion(entidad.getDireccion());
 			  entidadExterna.setEntidad(entidad.getEntidad());
 			  entidadExterna.setUbigeo(entidad.getUbigeo());
 			  entidadExterna.setRUC(entidad.getRUC());
 			  entidadExterna.setPoder(entidad.getPoder());
 			  entidadExterna.setSector(entidad.getSector());
 			  entidadExterna.setFax(entidad.getFax());
 			  entidadExterna.setPaginaWeb(entidad.getPaginaWeb());
 			  entidadExterna.setTelefonoJefe(entidad.getTelefonoJefe());
 			  entidadExterna.setCorreoJefe(entidad.getCorreoJefe());
 			  entidadExterna.setUsuarioModifica(entidad.getUsuarioModifica());
 			  entidadExterna.setHabilitado(Constantes.HABILITADO);
 			  actividadService.create(usuario, entidad, operacion);
 			  entidadExterna.setUsuarioModifica(usuario.getNombreUsuario());
 			  entidadExterna.setFechaModifica(new Date());
 			  return entidadesExternasDao.createEntidadExterna(entidadExterna);
		   }
 		   
		}
		return entidadesExternasDao.createEntidadExterna(entidad);
	}

    
}
