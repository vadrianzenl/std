package pe.gob.congreso.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import pe.gob.congreso.dao.EtapaGestionEnvioDao;
import pe.gob.congreso.model.MpEtapaGestionEnvio;
import pe.gob.congreso.util.Constantes;

@Repository("etapaGestionEnvioDao")
public class EtapaGestionEnvioDaoImpl extends AbstractDao<Integer, MpEtapaGestionEnvio> implements EtapaGestionEnvioDao {

	
	@Override
	@Transactional(readOnly = true)
	public List<MpEtapaGestionEnvio> getListById(String gestionEnvioId, String fichaDocumentoId) throws Exception {
		Criteria criteria = createEntityCriteria();
        if (gestionEnvioId != null && !gestionEnvioId.isEmpty()) {
            criteria.add(Restrictions.eq("gestionEnvioId", Integer.valueOf(gestionEnvioId)));
        }
        if (fichaDocumentoId != null && !fichaDocumentoId.isEmpty()) {
            criteria.add(Restrictions.eq("fichaDocumentoId", Integer.valueOf(fichaDocumentoId)));
        }
        criteria.add(Restrictions.eq("habilitado", true ));
        criteria.addOrder(Order.desc("fechaCrea"));
		return criteria.list();
		
	}

	@Override
	public List<MpEtapaGestionEnvio> getListEtapaEnvio(Integer fichaDocumentoId) throws Exception {
		List<MpEtapaGestionEnvio> list = new ArrayList<>();
		Criteria criteria = createEntityCriteria();		
        if (fichaDocumentoId != null && fichaDocumentoId > 0) {
            criteria.add(Restrictions.eq("fichaDocumentoId", fichaDocumentoId));
            criteria.add(Restrictions.eq("habilitado", true ));
            criteria.addOrder(Order.desc("fechaCrea"));
            list = criteria.list();
        }       
		return list;
	}

	@Override
	public MpEtapaGestionEnvio create(MpEtapaGestionEnvio etapa) throws Exception {
		saveOrUpdate(etapa);        
        return etapa;
	}

	@Override
	public int verificarRegistros(String reporteId) throws Exception {
		int result = 0;
    	if (Optional.fromNullable(reporteId).isPresent()) {
            if (!reporteId.equals("")) {
            	Query query = getSession().createSQLQuery("SELECT COUNT(*) FROM STD_ETAPA_GESTION_ENVIO ege WHERE ege.stdge_id :reporteId")
                		.setParameter("reporteId", Integer.valueOf(reporteId));            	
            	result = (int) query.uniqueResult();
            }
        }
        
        return result;
	}


	@Override
	public List<MpEtapaGestionEnvio> getContenidoReporteById(Optional<String> id) throws Exception {
		Criteria criteria = createEntityCriteria();	
		
		criteria.createAlias("gestionEnvio","ge");
		
        if (id != null && !id.get().isEmpty()) {
            criteria.add(Restrictions.eq("ge.id", Integer.valueOf(id.get())));
            criteria.add(Restrictions.eq("habilitado",Constantes.HABILITADO));
            criteria.add(Restrictions.eq("estado","1"));
   
            List detEtaGestionEnvio = criteria.list();
            
            return detEtaGestionEnvio;
        }     
		
		return null;
	}

	@Override
	public Integer updateMpDetGestionEnvio(MpEtapaGestionEnvio etapaGesionEnvio) throws Exception {
		Query query = this.getSession().createSQLQuery("update STD_ETAPA_GESTION_ENVIO set stdege_bhabilitado = :habilitado, aud_cusuario_modifica = :usuarioModifica, aud_dfecha_modifica = :fechaModifica " +
                " where stdge_id = :gestionEnvioId and stdf_id= :fichaDocumentoId");
	   query.setParameter("habilitado", Constantes.NO_HABILITADO);
       query.setParameter("gestionEnvioId", etapaGesionEnvio.getGestionEnvioId());
       query.setParameter("fichaDocumentoId",etapaGesionEnvio.getFichaDocumentoId() );
       query.setParameter("usuarioModifica", etapaGesionEnvio.getUsuarioModifica());
       query.setDate("fechaModifica", etapaGesionEnvio.getFechaModifica());
        int result = query.executeUpdate();
       return result;
	}

}
