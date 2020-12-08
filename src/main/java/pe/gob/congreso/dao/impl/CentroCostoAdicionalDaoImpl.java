package pe.gob.congreso.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.CentroCostoAdicionalDao;
import pe.gob.congreso.model.CentroCostoAdicional;
import pe.gob.congreso.model.EmpleadoCentroCostoActual;
import pe.gob.congreso.model.Usuario;

@Repository("centroCostoAdicionalDao")
public class CentroCostoAdicionalDaoImpl extends AbstractDao<Integer, CentroCostoAdicional> implements CentroCostoAdicionalDao {

    @Override
    public CentroCostoAdicional create(CentroCostoAdicional cc) throws Exception {
    	if(cc.getId().equals("0000000000")) cc.setId(this.getIdMax());
        saveOrUpdate(cc);
        return cc;
    }
    
	public String getIdMax() throws Exception{
		Query query = null;
		String result ="";
		query = getSession().createSQLQuery("SELECT MAX(stdcc_id) FROM STD_CENTRO_COSTO_ADICIONAL ");
		List<String> list = (List<String>) query.list();		
		
		String idMax="";
		Integer max=0;
		for(String aux: list){
			idMax = aux;
        }
		
		if(!idMax.equals("")){
			max = Integer.parseInt(idMax) + 1;
			result = String.valueOf(max);
		}		
		
		return result;
	}

    @Override
    @SuppressWarnings("unchecked")
    public List<CentroCostoAdicional> findBy() throws Exception {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("habilitado", true));
        List ccList = criteria.list();
        return ccList;
    }

}
