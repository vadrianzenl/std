package pe.gob.congreso.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.TipoDocumentoDao;
import pe.gob.congreso.model.TipoDocumento;

@Repository("tipoDocumentoDao")
public class TipoDocumentoDaoImpl extends AbstractDao<Integer, TipoDocumento> implements TipoDocumentoDao {

    @Override
    public TipoDocumento getTipoDocumentoId(Integer id) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.eq("id", id));//Integer.parseInt(id)

        TipoDocumento tipoDocumento = (TipoDocumento) criteria.uniqueResult();

        return tipoDocumento;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TipoDocumento> getTipoDocumentoCentroCosto(String id) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.createAlias("centroCosto", "cc");
        criteria.createAlias("tipo", "t");
        criteria.add(Restrictions.eq("cc.id", id).ignoreCase());//Integer.parseInt(id)
        criteria.add(Restrictions.eq("habilitado", true));
        criteria.addOrder(Order.asc("t.descripcion"));

        List tiposDocumentoList = criteria.list();

        return tiposDocumentoList;
    }

    @Override
    public TipoDocumento create(TipoDocumento tipoDocumento) throws Exception {
        saveOrUpdate(tipoDocumento);
        return tipoDocumento;
    }

}
