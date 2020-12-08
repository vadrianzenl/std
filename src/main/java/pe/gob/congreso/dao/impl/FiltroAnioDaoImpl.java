package pe.gob.congreso.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import pe.gob.congreso.dao.FiltroAnioDao;
import pe.gob.congreso.model.FiltroAnio;

@Repository("filtroAnioDao")
public class FiltroAnioDaoImpl extends AbstractDao<Integer, FiltroAnio> implements FiltroAnioDao {

	@Override
	public FiltroAnio buscarPermisos(String centroCosto, Integer tipoAnio) throws Exception {
		Criteria criteria = createEntityCriteria();
		criteria.createAlias("centroCosto", "cc");
		criteria.createAlias("tipoAnio", "t");
		criteria.add(Restrictions.eq("cc.id", centroCosto.trim()));
		criteria.add(Restrictions.eq("t.id", tipoAnio));
		criteria.add(Restrictions.eq("habilitado", true));
		FiltroAnio filtro = (FiltroAnio) criteria.uniqueResult();
		return filtro;
	}
}
