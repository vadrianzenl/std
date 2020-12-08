package pe.gob.congreso.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import pe.gob.congreso.dao.EntidadesExternasDao;
import pe.gob.congreso.model.EntidadExterna;
import pe.gob.congreso.util.Constantes;

@Repository("entidadesExternasDao")
public class EntidadesExternasDaoImpl extends AbstractDao<Integer, EntidadExterna> implements EntidadesExternasDao {

    @Override
    public EntidadExterna getEntidadesExternasById(String id) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.eq("id", Integer.parseInt(id)));
        criteria.add(Restrictions.eq("habilitado", Constantes.HABILITADO));

        EntidadExterna entidades = (EntidadExterna) criteria.uniqueResult();

        return entidades;
    }
    
    @Override
    public List<EntidadExterna> getEntidadesExternasByName(String name) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.eq("entidad", name));
        criteria.add(Restrictions.eq("habilitado", Constantes.HABILITADO));

        List<EntidadExterna> entidades = criteria.list();

        return entidades;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<EntidadExterna> getEntidadesExternas() throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.eq("habilitado", Constantes.HABILITADO));
        criteria.addOrder(Order.asc("entidad"));

        List entidadesList = criteria.list();

        return entidadesList;
    }

	@Override
	public EntidadExterna createEntidadExterna(EntidadExterna entidad) throws Exception {
		saveOrUpdate(entidad);
        return entidad;
	}


}
