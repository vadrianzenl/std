package pe.gob.congreso.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import pe.gob.congreso.dao.SpKpiRecibidosDao;
import pe.gob.congreso.model.SpKpiRecibidos;

@Repository("spKpiRecibidosDao")
public class SpKpiRecibidosDaoImpl extends AbstractDao<Integer, SpKpiRecibidos> implements SpKpiRecibidosDao {

	public List<SpKpiRecibidos> getKpiRecibidos(String idempleado, String ccosto, String fecha) throws Exception {

		Session s = getSession();
		Query query = s.createSQLQuery("EXEC sp_kpi_recibidos :idempleado,:ccosto, :fecha").addEntity(SpKpiRecibidos.class)
				.setParameter("idempleado", idempleado).setParameter("ccosto", ccosto).setParameter("fecha", fecha);

		List<SpKpiRecibidos> result = query.list();
		return result;
	}

}
