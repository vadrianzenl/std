package pe.gob.congreso.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import pe.gob.congreso.dao.EmpleadoCentroCostoActualDao;
import pe.gob.congreso.dao.EmpleadoCentroCostoDao;
import pe.gob.congreso.dao.EmpleadoDao;
import pe.gob.congreso.model.Empleado;
import pe.gob.congreso.model.EmpleadoCentroCosto;
import pe.gob.congreso.model.EmpleadoCentroCostoActual;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.service.ActividadService;
import pe.gob.congreso.service.EmpleadoService;

@Service("empleadoService")
@Transactional
public class EmpleadoServiceImpl implements EmpleadoService {

	@Autowired
	EmpleadoDao empleadoDao;

	@Autowired
	EmpleadoCentroCostoActualDao empleadoCentroCostoActualDao;

	@Autowired
	EmpleadoCentroCostoDao empleadoCentroCostoDao;

	@Autowired
	ActividadService actividadService;

	protected final Log log = LogFactory.getLog(getClass());
	
	@Override
	public Map<String, Object> findBy(Optional<String> codigo, Optional<String> descripcion, Optional<String> pag,
			Optional<String> pagLength) throws Exception {
		return empleadoDao.findBy(codigo, descripcion, pag, pagLength);
	}

	@Override
	public Empleado findById(Integer id) throws Exception {
		return empleadoDao.findById(id);
	}

	@Override
	public List<InputSelectUtil> getEmpleadoCentroCosto(String id) throws Exception {
		//return empleadoDao.getEmpleadoCentroCosto(id);
		return empleadoCentroCostoDao.getEmpleadosCentroCosto(id);
	}

	@Override
	public List<InputSelectUtil> getEmpleadoInputSelect() throws Exception {
		return empleadoDao.getEmpleadoInputSelect();
	}

	@Override
	public Object create(Usuario usuario, EmpleadoCentroCostoActual ecca, String operacion) throws Exception {
		actividadService.create(usuario, ecca, operacion);
		return empleadoCentroCostoActualDao.create(ecca);
	}

	@Override
	public List<EmpleadoCentroCostoActual> getEmpleadoCentroCostoActual() throws Exception {
		return empleadoCentroCostoActualDao.getEmpleadoCentroCostoActual();
	}

	@Override
	public List<EmpleadoCentroCosto> getEmpleadoCentroCosto(Integer empleadoId) throws Exception {
		return empleadoCentroCostoDao.getEmpleadoCentroCosto(empleadoId);
	}
	
	@Override
	public List<EmpleadoCentroCosto> getEmpleadoCentroCostoAll(Integer empleadoId) throws Exception {
		return empleadoCentroCostoDao.getEmpleadoCentroCostoAll(empleadoId);
	}

	@Override
	public Object updateCCActivo(Usuario usuario, EmpleadoCentroCosto ecca, String operacion) throws Exception {
		return empleadoCentroCostoActualDao.updateCCActivo(usuario, ecca);
	}

	@Override
	public Object updateCCActual(Usuario usuario, EmpleadoCentroCostoActual ecca, String operacion) throws Exception {
		//actividadService.create(usuario, ecca, operacion);
		log.info("Servicee");
		return empleadoCentroCostoActualDao.update(usuario, ecca);
	}

}
