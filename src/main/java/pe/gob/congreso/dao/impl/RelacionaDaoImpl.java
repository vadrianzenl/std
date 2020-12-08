package pe.gob.congreso.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.RelacionaDao;
import pe.gob.congreso.model.Relaciona;

@Repository("relacionaDao")
public class RelacionaDaoImpl extends AbstractDao<Integer, Relaciona> implements RelacionaDao {

    public void setFetchMode(Criteria criteria) {
        criteria.setFetchMode("fichaDocumento", FetchMode.JOIN);
        criteria.setFetchMode("fichaRelacionada", FetchMode.JOIN);
        criteria.setFetchMode("usuarioRelaciona", FetchMode.JOIN);
    }

    @Override
    public Relaciona create(Relaciona d) throws Exception {
        saveOrUpdate(d);
        return d;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Relaciona> findRelacionados(String fichaDocumentoId) throws Exception {

        Criteria criteria = createEntityCriteria();

        this.setFetchMode(criteria);
        criteria.createAlias("fichaDocumento", "f");
        criteria.add(Restrictions.eq("f.id", Integer.valueOf(fichaDocumentoId)));
        criteria.add(Restrictions.eq("habilitado", true));

        List relacionadosList = criteria.list();

        return relacionadosList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Relaciona> findAsociados(String fichaDocumentoId) throws Exception {

        Criteria criteria = createEntityCriteria();

        this.setFetchMode(criteria);
        criteria.createAlias("fichaRelacionada", "f");
        criteria.add(Restrictions.eq("f.id", Integer.valueOf(fichaDocumentoId)));
        criteria.add(Restrictions.eq("habilitado", true));

        List relacionadosList = criteria.list();

        return relacionadosList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Integer> findFichaIdRelacionados(String fichaDocumentoId) throws Exception {

        List<Integer> lstFichaRelId = new ArrayList<Integer>();
                
        Query query = getSession().createSQLQuery(
        		"SELECT distinct rel0.ficha FROM fn_std_obt_relacionados(:fichaId) rel0 ")
        		.setParameter("fichaId", Integer.valueOf(fichaDocumentoId));
        /*List<Object[]> result = (List<Object[]>) query.list();
        for(Object[] relacion: result){
        	lstFichaRelId.add((Integer)relacion[0]);
        	lstFichaRelId.add((Integer)relacion[1]);
        }*/
        
        lstFichaRelId = (List<Integer>) query.list();
        
        return lstFichaRelId;
    }

	@Override
	public List<Relaciona> findByTipo(String fichaDocumentoId, Integer tipo) throws Exception {
		Criteria criteria = createEntityCriteria();

        this.setFetchMode(criteria);
        criteria.createAlias("fichaDocumento", "f");
        criteria.createAlias("tipoRelacion", "tr");
        criteria.add(Restrictions.eq("f.id", Integer.valueOf(fichaDocumentoId)));
        criteria.add(Restrictions.eq("tr.id", tipo));
        criteria.add(Restrictions.eq("habilitado", true));
        criteria.addOrder(Order.desc("fechaRelaciona"));

        List relacionadosList = criteria.list();

        return relacionadosList;
	}

}
