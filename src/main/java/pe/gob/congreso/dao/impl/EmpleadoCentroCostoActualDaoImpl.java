package pe.gob.congreso.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.EmpleadoCentroCostoActualDao;
import pe.gob.congreso.model.EmpleadoCentroCosto;
import pe.gob.congreso.model.EmpleadoCentroCostoActual;
import pe.gob.congreso.model.Usuario;

import org.hibernate.Query;

@Repository("empleadoCentroCostoActualDao")
public class EmpleadoCentroCostoActualDaoImpl extends AbstractDao<Integer, EmpleadoCentroCostoActual> implements EmpleadoCentroCostoActualDao {

	protected final Log log = LogFactory.getLog(getClass());
	
	@Override
    public EmpleadoCentroCostoActual create(EmpleadoCentroCostoActual ecca) throws Exception {
        saveOrUpdate(ecca);
        return ecca;
    }
      

    @Override
    @SuppressWarnings("unchecked")
    public List<EmpleadoCentroCostoActual> getEmpleadoCentroCostoActual() throws Exception {
        Criteria criteria = createEntityCriteria();

        criteria.createAlias("empleado", "e");
        criteria.addOrder(Order.asc("e.descripcion"));
        criteria.add(Restrictions.eq("habilitado", true));

        List empleadosCentroCostoList = criteria.list();
        return empleadosCentroCostoList;
    }


	@Override
	public Integer updateCCActivo(Usuario usuario, EmpleadoCentroCosto ecca) throws Exception {
		Query query = null;
		int result =1;
		query = getSession().createSQLQuery("UPDATE STD_EMPLEADO_CENTRO_COSTO_ACTUAL SET stdcca_activo = 1, aud_cusuario_modifica = :usuarioModifica, aud_dfecha_modifica = GETDATE() WHERE stdecca_id = :ccActualId ")
				.setParameter("ccActualId", ecca.getCentroCostoActualId() )
				.setParameter("usuarioModifica", usuario.getNombreUsuario());
			result = query.executeUpdate();
		query = getSession().createSQLQuery("UPDATE STD_EMPLEADO_CENTRO_COSTO_ACTUAL SET stdcca_activo = 0, aud_cusuario_modifica = :usuarioModifica, aud_dfecha_modifica = GETDATE() WHERE stde_id = :id AND stdecca_id <> :ccActualId  ")
				.setParameter("id", usuario.getEmpleado().getId() )
				.setParameter("ccActualId", ecca.getCentroCostoActualId() )
				.setParameter("usuarioModifica", usuario.getNombreUsuario());
			result = query.executeUpdate();		
		return result;
	}
	
	public Integer update(Usuario usuario,  EmpleadoCentroCostoActual ecca) throws Exception{
		log.info("Daooo");
		Query query = null;
		int result =1;
		query = getSession().createSQLQuery("UPDATE STD_EMPLEADO_CENTRO_COSTO_ACTUAL SET stdcc_id = :stdcc_id, stdecca_bhabilitado = :stdecca_bhabilitado, stdcca_activo = :stdcca_activo, aud_cusuario_modifica = :usuarioModifica, aud_dfecha_modifica = GETDATE() WHERE stdecca_id = :ccActualId  ")
				.setParameter("stdcc_id", ecca.getCentroCosto().getId() )
				.setParameter("stdecca_bhabilitado", ecca.isHabilitado() )
				.setParameter("stdcca_activo", ecca.isActivo() )
				.setParameter("usuarioModifica", usuario.getNombreUsuario())
				.setParameter("ccActualId", ecca.getId() );
		result = query.executeUpdate();	
		return result;
	}

}
