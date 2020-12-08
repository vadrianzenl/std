package pe.gob.congreso.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.congreso.dao.FichaDocumentoDao;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.service.DependenciaService;
import pe.gob.congreso.util.Constantes;

@Service("dependenciaService")
@Transactional
public class DependenciaServiceImpl implements DependenciaService {

	@Autowired
	FichaDocumentoDao fichaDocumentoDao;

	protected final Log log = LogFactory.getLog(getClass());

	@Override
	public List<InputSelectUtil> findDependenciasRecibidos(String centroCostoId) throws Exception {
		Map parametros = new HashMap<>();
		parametros.put("habilitado", Constantes.HABILITADO);
		parametros.put("idCentroCosto", centroCostoId);
		return fichaDocumentoDao.getListDependenciasRecibidos(parametros);
	}

	@Override
	public List<InputSelectUtil> findDependenciasEnviados(String centroCostoId) throws Exception {
		Map parametros = new HashMap<>();
		parametros.put("habilitado", Constantes.HABILITADO);
		parametros.put("idCentroCosto", centroCostoId);
		return fichaDocumentoDao.getListDependenciasEnviados(parametros);
	}
	

}
