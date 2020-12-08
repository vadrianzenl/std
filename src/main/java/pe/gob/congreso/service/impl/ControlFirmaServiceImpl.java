package pe.gob.congreso.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.congreso.dao.ControlFirmaDao;
import pe.gob.congreso.model.ControlFirma;
import pe.gob.congreso.service.ControlFirmaService;

@Service("controlFirmaService")
@Transactional
public class ControlFirmaServiceImpl implements ControlFirmaService {

	@Autowired
	ControlFirmaDao controlFirmaDao;
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@Override
	public ControlFirma findByIdEmpleado(String idEmpleado) throws Exception {
		//System.out.println("AEP--ControlFirmaServiceImpl.findByIdEmpleado(" + idEmpleado+")" );	
		ControlFirma controlFirma;
		controlFirma = controlFirmaDao.findByIdEmpleado(idEmpleado);
		log.info(idEmpleado + "-" + controlFirma.getNombreEmpleado() + "-"+controlFirma.getEstado());
		//System.out.println("AEP--Resultado: " + controlFirma.getNombreEmpleado() + "-"+controlFirma.getEstado() );
		return controlFirma;
		
	}
	
	
}
