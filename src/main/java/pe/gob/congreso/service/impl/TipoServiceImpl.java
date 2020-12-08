package pe.gob.congreso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import pe.gob.congreso.dao.TipoDao;
import pe.gob.congreso.model.GrupoCentroCosto;
import pe.gob.congreso.model.Tipo;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.service.ActividadService;
import pe.gob.congreso.service.TipoService;
import pe.gob.congreso.util.Constantes;

@Service("tipoService")
@Transactional
public class TipoServiceImpl implements TipoService {

    @Autowired
    TipoDao tipoDao;

    @Autowired
    ActividadService actividadService;

    @Override
    public Object create(Usuario usuario, Tipo t, String operacion) throws Exception {
    	actividadService.create(usuario, t, operacion);
        return tipoDao.create(t);
    }

    @Override
    public Map<String, Object> find(Optional<String> nombre, Optional<String> descripcion, Optional<String> pag, Optional<String> pagLength) throws Exception {
        return tipoDao.find(nombre, descripcion, pag, pagLength);
    }

    @Override
    public List<Tipo> findBy(Optional<String> id, Optional<String> nombre) throws Exception {
        return tipoDao.findBy(id, nombre);
    }

    @Override
    public List<Tipo> findByOrden(Optional<String> id, Optional<String> nombre) throws Exception {
        return tipoDao.findByOrden(id, nombre);
    }

    @Override
    public Tipo findByTipo(String id, String nombre) throws Exception {
        return tipoDao.findByTipo(id, nombre);
    }

    @Override
    public Tipo findByCodigo(String id) throws Exception {
        return tipoDao.findByCodigo(id);
    }

    @Override
    public Tipo findByNombre(String nombre) throws Exception {
        return tipoDao.findByNombre(nombre);
    }

    @Override
    public Tipo findByTipoId(String descripcion, String nombre) throws Exception {
        return tipoDao.findByTipoId(descripcion, nombre);
    }

    @Override
    public List<InputSelectUtil> getTiposInputSelect() throws Exception {
        return tipoDao.getTiposInputSelect();
    }

    @Override
    public List<Tipo> findByAlfresco() throws Exception {
        return tipoDao.findByAlfresco();
    }

    @Override
    public List<Tipo> findByCorreo() throws Exception {
        return tipoDao.findByCorreo();
    }
    
    @Override
    public Tipo findByTipoLike(String descripcion, String nombre) throws Exception {
        return tipoDao.findByTipoLike(descripcion, nombre);
    }

	@Override
	public Tipo crearTipoCasillero(Usuario usuario, GrupoCentroCosto gcc, String operacion) throws Exception {
		String descripcion =  gcc.getId().trim();
		Tipo t = findByTipoLike(descripcion, Constantes.TIPO_CASILLERO);
		if ( t == null ) {
			operacion = Constantes.OPERACION_CREAR;
			List<Tipo> tipos = findByOrden(Optional.of(Constantes.VACIO), Optional.of(Constantes.TIPO_CASILLERO));
			descripcion += Constantes.PALOTE + gcc.getDescripcion().trim();
			Tipo tipo = new Tipo();
			tipo.setNombre(Constantes.TIPO_CASILLERO);
			tipo.setDescripcion(descripcion);
			tipo.setHabilitado(Constantes.HABILITADO);
			tipo.setOrden(tipos.get(tipos.size() - 1 ).getOrden().intValue() + 1);
			tipo.setConfigurable(Constantes.NO_ES_CONFIGURABLE);
			tipo.setUsuarioCreacion(usuario.getNombreUsuario().trim());
			tipo.setFechaCreacion(new Date());
			t = (Tipo) create(usuario, tipo, operacion);
		} else {
			t.setHabilitado(Constantes.HABILITADO);
			t.setUsuarioModificacion(usuario.getNombreUsuario().trim());
			t.setFechaModificacion(new Date());
			t = (Tipo) create(usuario, t, operacion);
		}
		return t;
	}

}
