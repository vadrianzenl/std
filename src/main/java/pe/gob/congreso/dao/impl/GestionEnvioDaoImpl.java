package pe.gob.congreso.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import pe.gob.congreso.dao.GestionEnvioDao;
import pe.gob.congreso.model.MpGestionEnvio;
import pe.gob.congreso.util.Constantes;
import pe.gob.congreso.util.PaginationHelper;

@Repository("gestionEnvioDao")
public class GestionEnvioDaoImpl extends AbstractDao<Integer, MpGestionEnvio> implements GestionEnvioDao {

	private final PaginationHelper paginationHelper = new PaginationHelper();
	protected final Log log = LogFactory.getLog(getClass());
	
	@Override
	@Transactional(readOnly = true)
	public MpGestionEnvio getById(String id)
			throws Exception {
		Criteria criteria = createEntityCriteria();		
        if (id != null && !id.isEmpty()) {
            criteria.add(Restrictions.eq("id", Integer.valueOf(id)));
            criteria.add(Restrictions.eq("habilitado", true ));
            return (MpGestionEnvio) criteria.uniqueResult();
        }       
		return null;
		
	}

	@Override
	public MpGestionEnvio create(MpGestionEnvio reporte) throws Exception {
		saveOrUpdate(reporte);        
        return reporte;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public  Map <String, Object> getReportesBy(Optional<String> fechaIniCrea, Optional<String> fechaFinCrea,
			Optional<String>tipoReporte,Optional<String> pag, Optional<String> pagLength) throws Exception {
		// TODO Auto-generated method stub
		 Criteria criteria = createEntityCriteria();
		 		 
		if (fechaIniCrea.isPresent() && !fechaIniCrea.get().isEmpty()) {
            if (fechaFinCrea.isPresent()) {
                if (fechaFinCrea.get().isEmpty()) {
                    criteria.add(Restrictions.sqlRestriction(" convert(varchar,aud_dfecha_crea,23) = ? ", fechaIniCrea.get(), StringType.INSTANCE));
                }
            } else {
                criteria.add(Restrictions.sqlRestriction(" convert(varchar,aud_dfecha_crea,23) = ? ", fechaIniCrea.get(), StringType.INSTANCE));
            }
        }

        if (fechaIniCrea.isPresent() && !fechaIniCrea.get().isEmpty()) {
            if (fechaFinCrea.isPresent()) {
                if (!fechaFinCrea.get().isEmpty()) {
                    criteria.add(Restrictions.sqlRestriction("aud_dfecha_crea >= CONVERT(datetime, ? ,120) ", fechaIniCrea.get(), StringType.INSTANCE));
                    criteria.add(Restrictions.sqlRestriction("aud_dfecha_crea < DATEADD(DAY,1,CONVERT(datetime, ? ,120)) ", fechaFinCrea.get(), StringType.INSTANCE));
                }
            }
        }
        
        if (tipoReporte.isPresent() && !tipoReporte.get().isEmpty()) {
            criteria.add(Restrictions.like("tipoReporte", tipoReporte.get(), MatchMode.ANYWHERE));
        }
        criteria.add(Restrictions.eq("habilitado", true ));
        //criteria.addOrder(Order.desc("id"));
        
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> map = paginationHelper.getPagination(criteria, "id", pag, pagLength);

        List listaMpGestionEnvio = criteria.list();;
        result.put("reportes", listaMpGestionEnvio);
        result.put("totalPage", map.get("totalPage"));
        result.put("totalResultCount", map.get("totalResultCount"));
		return result;
		
	}

	@Override
	@Transactional(readOnly = true)
	public Integer updateMpGestionEnvio(MpGestionEnvio gestionEnvio) throws Exception {
	    Query query = this.getSession().createSQLQuery("update STD_GESTION_ENVIO set stdge_isubsanado_reporte = :subsanado, stdge_dfecha_subsanado = :fechaSubsanado, stdge_cantidad_registros = :cantidadRegistros, stdge_cantidad_fisicos = :cantidadFisicos,  aud_cusuario_modifica = :usuarioModifica, aud_dfecha_modifica = :fechaModifica where stdge_id = :gestionEnvioId");
	    query.setParameter("subsanado",Constantes.HABILITADO);
	    query.setParameter("cantidadRegistros",gestionEnvio.getCantidadRegistros() );
	    query.setParameter("cantidadFisicos", gestionEnvio.getCantidadFisicos());
        query.setParameter("gestionEnvioId", gestionEnvio.getId());
        query.setParameter("usuarioModifica", gestionEnvio.getUsuarioModifica());
        query.setDate("fechaModifica", gestionEnvio.getFechaModifica() );
        query.setDate("fechaSubsanado", gestionEnvio.getFechaSubsanado() );
        int result = query.executeUpdate();
        return result;
	}

	@Override
	@Transactional(readOnly = true)
	public Integer updateEstadoMpGestionEnvio(MpGestionEnvio gestionEnvio) throws Exception {
	   Query query = this.getSession().createSQLQuery("update STD_GESTION_ENVIO set stdge_cestado = :estado,  aud_cusuario_modifica = :usuarioModifica, aud_dfecha_modifica = :fechaModifica " +
	            " where stdge_id = :gestionEnvioId");
	   query.setParameter("estado",gestionEnvio.getEstado());
	   query.setParameter("gestionEnvioId", gestionEnvio.getId());
	   query.setParameter("usuarioModifica", gestionEnvio.getUsuarioModifica());
	   query.setDate("fechaModifica", gestionEnvio.getFechaModifica());
	   int result = query.executeUpdate();
	   return result;
	}


}
