package pe.gob.congreso.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import pe.gob.congreso.dao.DetGestionEnvioDao;
import pe.gob.congreso.model.MpDetGestionEnvio;
import pe.gob.congreso.util.Constantes;

@Repository("detGestionEnvioDao")
public class DetGestionEnvioDaoImpl extends AbstractDao<Integer, MpDetGestionEnvio> implements DetGestionEnvioDao {
	
	@Override
	public MpDetGestionEnvio getById(Optional<String> id) throws Exception {
		// TODO Auto-generated method stub
	

		Criteria criteria = createEntityCriteria();	
		
		criteria.createAlias("gestionEnvio","ge");
		
        if (id != null && !id.get().isEmpty()) {
            criteria.add(Restrictions.eq("ge.id", Integer.valueOf(id.get())));
            criteria.add(Restrictions.eq("habilitado",Constantes.HABILITADO));
            MpDetGestionEnvio detGestionEnvio = (MpDetGestionEnvio) criteria.uniqueResult();
            
            return detGestionEnvio;
        }     
  
		return null;
	}

	@Override
	public MpDetGestionEnvio create(MpDetGestionEnvio detcargo) throws Exception {
		saveOrUpdate(detcargo);        
        return detcargo;
	}

	@Override
	public Integer updateMpDetGestionEnvio(MpDetGestionEnvio cargo) throws Exception {
		// TODO Auto-generated method stub
		Query query = this.getSession().createSQLQuery("update STD_DET_GES_ENVIO set stddge_bhabilitado = :habilitado, aud_cusuario_modifica = :usuarioModifica, aud_dfecha_modifica = :fechaModifica " +
                " where stddge_id = :id");        
       query.setParameter("id", cargo.getId());
       query.setParameter("habilitado", Constantes.NO_HABILITADO);
       query.setParameter("usuarioModifica", cargo.getUsuarioModifica());
       query.setParameter("fechaModifica", cargo.getFechaModifica());
        int result = query.executeUpdate();
       return result;
	}


}
