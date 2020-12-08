package pe.gob.congreso.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.congreso.dao.SpKpiRecibidosDao;
import pe.gob.congreso.model.SpKpiRecibidos;
import pe.gob.congreso.service.SpKpiRecibidosService;
import pe.gob.congreso.vo.SpKpiRecibidosVO;

@Service("spKpiRecibidosService")
@Transactional
public class SpKpiRecibidosServiceImpl implements SpKpiRecibidosService {

	private final Logger log = LoggerFactory.getLogger(SpKpiRecibidosServiceImpl.class);

	@Autowired
	SpKpiRecibidosDao spKpiRecibidosDao;

	@Override
	public SpKpiRecibidosVO getKpiRecibidos(String idempleado, String ccosto, String fecha) throws Exception {
		List<SpKpiRecibidos> list = spKpiRecibidosDao.getKpiRecibidos(idempleado, ccosto, fecha);
		SpKpiRecibidosVO obj = new SpKpiRecibidosVO();
		if (list != null) {
			SpKpiRecibidos entity = list.get(0);
			obj = toSpKpiRecibidosVO(entity);
		}
		return obj;
	}

	private SpKpiRecibidosVO toSpKpiRecibidosVO(SpKpiRecibidos kpiRecibidos) {
		SpKpiRecibidosVO kpiRecibidosVO = new SpKpiRecibidosVO();
		kpiRecibidosVO.setId(kpiRecibidos.getId());
		kpiRecibidosVO.setCcconrec(kpiRecibidos.getCcconrec());
		kpiRecibidosVO.setCcsinrec(kpiRecibidos.getCcsinrec());
		kpiRecibidosVO.setUsuconrec(kpiRecibidos.getUsuconrec());
		kpiRecibidosVO.setUsusinrec(kpiRecibidos.getUsusinrec());

		return kpiRecibidosVO;
	}

}
