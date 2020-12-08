package pe.gob.congreso.dao.impl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import pe.gob.congreso.dao.DerivaMPDao;
import pe.gob.congreso.model.Deriva;
import pe.gob.congreso.model.util.EnviadoUtil;
import pe.gob.congreso.util.Constantes;

@Repository("derivaMPDao")
public class DerivaMPDaoImpl extends AbstractDao<Integer, Deriva> implements DerivaMPDao {

    @Override
    @SuppressWarnings("unchecked")
    public List<EnviadoUtil> findEnviados(Integer fichaDocumentoId) throws Exception {
        
    	
    	StringBuffer  sqlQuery = new StringBuffer();
    	sqlQuery.append("select empleado.stde_descripcion empleadoDescripcion, ");
    	sqlQuery.append("empleado.stde_id empleadoId, ");
    	sqlQuery.append("centroCosto.stdcc_cdescripcion centroCosto, ");
    	sqlQuery.append("centroCosto.stdcc_id centroCostoId, ");
    	sqlQuery.append("enviado.stdd_bdirigido dirigido, ");
    	sqlQuery.append("enviado.stde_bcopia ccopia, ");
    	sqlQuery.append("enviado.stde_bresponsable esResponsable ");
    	sqlQuery.append("from STD_ENVIADO enviado ");
    	sqlQuery.append("inner join STD_EMPLEADO empleado on enviado.stde_id_destino = empleado.stde_id ");
    	sqlQuery.append("inner join STD_CENTRO_COSTO centroCosto on empleado.stdcc_id = centroCosto.stdcc_id ");
    	sqlQuery.append("where ");
    	sqlQuery.append("stdf_id = :fichaDocumentoId ");
    	sqlQuery.append("and ");
    	sqlQuery.append("stdd_bdirigido != :dirigido ");
    	sqlQuery.append("and ");
    	sqlQuery.append("stde_bresponsable = :responsable");
    	
	    Query query = this.getSession().createSQLQuery(sqlQuery.toString());
	    query.setParameter("fichaDocumentoId",fichaDocumentoId);
	    query.setParameter("dirigido",Constantes.RECIBIDO_FISICO);
	    query.setParameter("responsable",Constantes.ES_RESPONSABLE);
	    
        List ListBean = query.list();
        List<EnviadoUtil> list = new ArrayList<>();
        for ( int i = 0; i < ListBean.size(); i++ ) {
        	EnviadoUtil enviado = new EnviadoUtil();
        	enviado.setEmpleadoDescripcion(Array.get(ListBean.get(i), 0).toString());
        	enviado.setEmpleadoId(Integer.parseInt(Array.get(ListBean.get(i), 1).toString()));
        	enviado.setCentroCosto(Array.get(ListBean.get(i), 2).toString());
        	enviado.setCentroCostoId(Array.get(ListBean.get(i), 3).toString());
        	enviado.setDirigido(Integer.parseInt(Array.get(ListBean.get(i), 4).toString()));
        	enviado.setCcopia(Boolean.parseBoolean(Array.get(ListBean.get(i), 5).toString()));
        	enviado.setEsResponsable(Boolean.parseBoolean(Array.get(ListBean.get(i), 6).toString()));
        	list.add(enviado);
        }
    	
    	return list;
    }

    
}
