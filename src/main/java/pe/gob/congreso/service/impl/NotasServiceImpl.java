
package pe.gob.congreso.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.congreso.dao.EmpleadoDao;
import pe.gob.congreso.dao.HistoNotasDao;
import pe.gob.congreso.dao.NotasDao;
import pe.gob.congreso.model.Empleado;
import pe.gob.congreso.model.HistoNotas;
import pe.gob.congreso.model.Notas;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.ActividadService;
import pe.gob.congreso.service.NotasService;
import pe.gob.congreso.util.Constantes;

@Service("notasService")
@Transactional
public class NotasServiceImpl implements NotasService{

    @Autowired
    ActividadService actividadService;
    
    @Autowired
    NotasDao notasDao;
    
    @Autowired
    HistoNotasDao histoNotasDao;
    
    @Autowired
    EmpleadoDao empleadoDao;
    
    
    @Override
    public Object create(Usuario usuario, Notas b, String operacion) throws Exception {    	
    	actividadService.create(usuario, b, operacion);
    	Date fechaActual = new Date();
    	if ( Constantes.OPERACION_CREAR.equals(operacion.trim()) ) {
			b.setUsuarioCrea(usuario.getNombreUsuario());
			b.setFechaCrea(fechaActual);
		} else if ( Constantes.OPERACION_ACTUALIZAR.equals(operacion.trim()) ) {
			b.setUsuarioModifica(usuario.getNombreUsuario());
			b.setFechaModifica(fechaActual);
		}
        Notas nota = notasDao.create(b);
        Empleado empleado = empleadoDao.findByNameUsuario(nota.getUsuarioCrea());
 		String usuarioDescripcion = "";
 		usuarioDescripcion = new StringBuffer().append(empleado.getNombres().trim()).append(" ").append(empleado.getApellidos().trim()).toString();
 		nota.setUsuarioDescripcion(usuarioDescripcion);
 		return nota;
    }

    @Override
    public List<Notas> findNotas(Usuario usuario, String fichaDocumentoId) throws Exception {
    	List<Notas> lista =  notasDao.findNotas(fichaDocumentoId);
    	List<Notas> listaFiltro = new ArrayList<>();
    	if (Constantes.PERFIL_RESPONSABLE_DEL_AREA.equals(usuario.getPerfil().getId().trim())
    			|| Constantes.PERFIL_RESPONSABLE_MESA_PARTES.equals(usuario.getPerfil().getId().trim()) ){ 
        	for (Notas nota:lista) {
        		Empleado empleado = empleadoDao.findByNameUsuario(nota.getUsuarioCrea());
        		String usuarioDescripcion = "";
        		usuarioDescripcion = new StringBuffer().append(empleado.getNombres().trim()).append(" ").append(empleado.getApellidos().trim()).toString();
        		nota.setUsuarioDescripcion(usuarioDescripcion);
        		Boolean visualiza = true; 
        		if ( empleado.getCentroCosto().getId().equals(usuario.getEmpleado().getCentroCosto().getId())  ) {
        			nota.setIndVis(Constantes.PUEDE_VISUALIZAR_NOTA);
        		} else {
        			nota.setIndVis(Constantes.NO_PUEDE_VISUALIZAR_NOTA);
        			visualiza = false;
        		}
        		if ( usuario.getNombreUsuario().trim().equals(nota.getUsuarioCrea().trim()) ) {
        			nota.setIndEdi(Constantes.PUEDE_EDITAR_NOTA);
        		} else {
        			nota.setIndEdi(Constantes.NO_PUEDE_EDITAR_NOTA);
        		}
        		if ( visualiza ) {
        			listaFiltro.add(nota);
        		}
        	}
		} else if ( Constantes.PERFIL_USUARIO_DEL_STD.equals(usuario.getPerfil().getId().trim()) ) {
			for (Notas nota:lista) {
        		Empleado empleado = empleadoDao.findByNameUsuario(nota.getUsuarioCrea());    		
        		if ( usuario.getNombreUsuario().trim().equals(nota.getUsuarioCrea().trim()) ) {
        			String usuarioDescripcion = "";
            		usuarioDescripcion = new StringBuffer().append(empleado.getNombres().trim()).append(" ").append(empleado.getApellidos().trim()).toString();
            		nota.setUsuarioDescripcion(usuarioDescripcion);
            		nota.setIndVis(Constantes.PUEDE_VISUALIZAR_NOTA);
            		nota.setIndEdi(Constantes.PUEDE_EDITAR_NOTA);            		
            		listaFiltro.add(nota);
        		}
        	}
		}

    	return listaFiltro;
    }
    
	public HistoNotas createHistoNotas(Notas n) throws Exception {
		Notas b = notasDao.getNotas(n.getFichaId().toString(), n.getId().toString());
		HistoNotas h = new HistoNotas();
		h.setNotasId(b.getId());
		h.setFichaId(b.getFichaId());
		h.setCentroCostoId(b.getCentroCostoId());
		h.setEmpleadoId(b.getEmpleadoId());
		h.setDescripcion(b.getDescripcion());
		h.setEstado(b.getEstado());
		h.setPrioridad(b.getPrioridad());
		h.setUsuarioCrea(b.getUsuarioCrea());
		h.setFechaCrea(b.getFechaCrea());
		h.setUsuarioModifica(b.getUsuarioModifica());
		h.setFechaModifica(b.getFechaModifica());
		return histoNotasDao.create(h);
	}
	
}
