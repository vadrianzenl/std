package pe.gob.congreso.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import pe.gob.congreso.dao.EmpleadoCentroCostoDao;
import pe.gob.congreso.model.CentroCosto;
import pe.gob.congreso.model.EmpleadoCentroCosto;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.model.util.SeguimientoUtil;

@Repository("empleadoCentroCostoDao")
public class EmpleadoCentroCostoDaoImpl extends AbstractDao<Integer, EmpleadoCentroCosto> implements EmpleadoCentroCostoDao{

	protected final Log log = LogFactory.getLog(getClass());
	
	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoCentroCosto> getEmpleadoCentroCosto(Integer empleadoId) throws Exception {
		Criteria criteria = createEntityCriteria();
		criteria.createAlias("empleado", "e");
		criteria.add(Restrictions.eq("e.id", empleadoId));        
        criteria.add(Restrictions.eq("habilitado", true));
        criteria.addOrder(Order.desc("activo"));
        List empleadosCentroCostoList = criteria.list();
		return empleadosCentroCostoList;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoCentroCosto> getEmpleadoCentroCostoAll(Integer empleadoId) throws Exception {	
		List<EmpleadoCentroCosto> empleadosCentroCostoList = new ArrayList<EmpleadoCentroCosto>();
		Query query = getSession().createSQLQuery(
        		"SELECT [stde_id] AS empleadoId "
			      +",ECC.[stdcc_id] AS centroCostoId "
			      +",[stdecc_bhabilitado] AS habilitado "
			      +",[stdecc_activo] AS activo "
			      +",[stdecc_tipo] AS tipo "
			      +",[stdecca_id] AS centroCostoActualId "
			      +",CC.stdcc_cdescripcion "
			      +"FROM [dbo].[STD_EMPLEADO_CENTRO_COSTO] ECC "
			      +"LEFT JOIN [dbo].[STD_CENTRO_COSTO] CC ON ECC.stdcc_id = CC.stdcc_id "
			      +"WHERE [stde_id] = :emp "
			      +"ORDER BY [stdecc_activo] DESC")
        		.setParameter("emp", empleadoId);
	
		List<Object[]> result = (List<Object[]>) query.list();
		EmpleadoCentroCosto ecc;
		CentroCosto cc;
        for(Object[] ec: result){
        	ecc = new EmpleadoCentroCosto();
        	ecc.setEmpleadoId((Integer)ec[0]);
        	ecc.setCentroCostoId((String)ec[1]);
        	boolean hab = (boolean)ec[2];
        	ecc.setHabilitado(hab);
        	Integer act = (Integer)ec[3];
        	boolean resul = act==1?true:false;
        	ecc.setActivo(resul);
        	ecc.setTipo((String)ec[4]); 
        	ecc.setCentroCostoActualId((Integer)ec[5]);
        	cc = new CentroCosto();
        	cc.setId((String)ec[1]);
        	cc.setDescripcion((String)ec[6]);
        	ecc.setCentroCosto(cc);
        	empleadosCentroCostoList.add(ecc);
        }
		return empleadosCentroCostoList;	
	}
	
	
    @Override
    @SuppressWarnings("unchecked")
    @Cacheable("empleadocache") 
    public List<InputSelectUtil> getEmpleadosCentroCosto(String id) throws Exception {
    	log.info("Empleados Centro de Costoooo");
    	Criteria criteria = createEntityCriteria();

        criteria.createAlias("centroCosto", "cc");
        criteria.createAlias("empleado", "e");
        criteria.add(Restrictions.eq("cc.id", id));
        criteria.add(Restrictions.eq("e.estado", "A"));
        criteria.add(Restrictions.ne("e.estadoEmpleado", "2"));
        criteria.add(Restrictions.eq("habilitado", true));
        
        criteria.addOrder(Order.asc("e.descripcion"));
        criteria.setProjection(Projections.distinct(Projections.projectionList()
                .add(Projections.property("e.id"), "value")
                .add(Projections.property("e.descripcion"), "label")
        )).setResultTransformer(Transformers.aliasToBean(InputSelectUtil.class));

        List empleadosList = criteria.list();

        return empleadosList;
    }
}
