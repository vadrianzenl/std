
package pe.gob.congreso.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import pe.gob.congreso.dao.ActividadDao;
import pe.gob.congreso.model.Actividad;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.ActividadService;

@Service("actividadService")
@Transactional
public class ActividadServiceImpl implements ActividadService {

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	ActividadDao actividadDao;

	@Override
	public List<Actividad> findBy(Optional<Integer> usuarioId, Optional<String> nombreTabla, Optional<String> operacion)
			throws Exception {
		return actividadDao.findBy(usuarioId, nombreTabla, operacion);
	}

	public void create(Usuario usuario, Object obj, String op) throws Exception {
		log.debug("create()");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ipAddress", usuario.getIpAddress());
		parameters.put("op", op);
		log.debug(parameters);
		try {
			Actividad a = actividadDao.create(usuario, obj, parameters);
		} catch (Exception e) {
			log.error(e, e);
		}

	}

	@Override
	public Object auditar(Object obj) throws Exception {
		Actividad a = actividadDao.auditar(obj);
		return a;
	}
}
