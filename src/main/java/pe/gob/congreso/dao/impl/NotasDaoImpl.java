package pe.gob.congreso.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import pe.gob.congreso.dao.NotasDao;
import pe.gob.congreso.model.Notas;

@Repository("notasDao")
public class NotasDaoImpl extends AbstractDao<Integer, Notas> implements NotasDao {

    public void setFetchMode(Criteria criteria) {
        criteria.setFetchMode("fichaDocumento", FetchMode.JOIN);
        criteria.setFetchMode("empleado", FetchMode.JOIN);
        criteria.setFetchMode("estado", FetchMode.JOIN);
    }

    @Override
    public Notas create(Notas d) throws Exception {
        saveOrUpdate(d);
        return d;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Notas> findNotas(String fichaDocumentoId) throws Exception {
        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.eq("fichaId", Integer.valueOf(fichaDocumentoId)));
        criteria.addOrder(Order.desc("fechaCrea"));

        List Notas = criteria.list();
        return Notas;
    }
    
    
    @Override
    @SuppressWarnings("unchecked")
    public Notas getNotas(String fichaDocumentoId, String id) throws Exception {
        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.eq("id", Integer.valueOf(id)));
        criteria.add(Restrictions.eq("fichaId", Integer.valueOf(fichaDocumentoId)));

        return (Notas) criteria.uniqueResult();
    }

	@Override
	public List<Notas> findNotas(String fichaDocumentoId, String centroCostoId, String usuarioId) throws Exception {
		// TODO Auto-generated method stub
		 Criteria criteria = createEntityCriteria();

	        criteria.add(Restrictions.eq("fichaId", Integer.valueOf(fichaDocumentoId)));
	        if (!centroCostoId.isEmpty()) {
	        	criteria.add(Restrictions.eq("centroCostoId", Integer.valueOf(centroCostoId)));
	        }
	        if (!usuarioId.isEmpty()) {
	        	criteria.add(Restrictions.eq("usuarioCrea", usuarioId));
	        }
	        criteria.addOrder(Order.desc("fechaCrea"));

	        List Notas = criteria.list();
	        return Notas;
	}




}
