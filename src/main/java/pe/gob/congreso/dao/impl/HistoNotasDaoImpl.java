package pe.gob.congreso.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.congreso.dao.HistoNotasDao;
import pe.gob.congreso.model.HistoNotas;


@Repository("histoNotasDao")
public class HistoNotasDaoImpl extends AbstractDao<Integer, HistoNotas> implements HistoNotasDao {

    public void setFetchMode(Criteria criteria) {
        criteria.setFetchMode("fichaDocumento", FetchMode.JOIN);
        criteria.setFetchMode("empleado", FetchMode.JOIN);
        criteria.setFetchMode("estado", FetchMode.JOIN);
    }

    @Override
	@Transactional(readOnly = true)
    public HistoNotas create(HistoNotas d) throws Exception {
        saveOrUpdate(d);
        return d;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<HistoNotas> findHistoNotas(String fichaDocumentoId, String notasId) throws Exception {
        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.eq("fichaId", Integer.valueOf(fichaDocumentoId)));
        criteria.add(Restrictions.eq("notasId", Integer.valueOf(notasId)));
        criteria.addOrder(Order.desc("fechaCrea"));

        List Notas = criteria.list();
        return Notas;
    }
    
    
    @Override
    @SuppressWarnings("unchecked")
    public HistoNotas getHistoNotas(String fichaDocumentoId, String notasId, String histoNotasId) throws Exception {
        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.eq("id", Integer.valueOf(histoNotasId)));
        criteria.add(Restrictions.eq("notasId", Integer.valueOf(notasId)));
        criteria.add(Restrictions.eq("fichaId", Integer.valueOf(fichaDocumentoId)));

        return (HistoNotas) criteria.uniqueResult();
    }

    
}
