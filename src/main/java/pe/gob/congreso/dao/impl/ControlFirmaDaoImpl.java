package pe.gob.congreso.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import pe.gob.congreso.dao.ControlFirmaDao;
import pe.gob.congreso.model.ControlFirma;


@Repository("controlFirmaDao")
public class ControlFirmaDaoImpl extends AbstractDao<Integer, ControlFirma> implements ControlFirmaDao {

	protected final Log log = LogFactory.getLog(getClass());
	
	
	@Override
	public ControlFirma findByIdEmpleado(String idEmpleado) throws Exception {
		//System.out.println("AEP--Dentro de ControlFirmaDaoImpl - findByIdEmpleado(idEmpleado='"+idEmpleado+"')");
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("idEmpleado", idEmpleado));
		ControlFirma control = (ControlFirma) criteria.uniqueResult();
		//Verificamos si es nulo (AEP 23.07.2019)
		if (control == null){
			log.info("control == null -> completamos control");
			control = new ControlFirma();
			control.setId(0);
			control.setIdEmpleado(idEmpleado);
			control.setNombreEmpleado(idEmpleado);
			control.setEstado("N");
		}
		return control;
	}

	
	
}



