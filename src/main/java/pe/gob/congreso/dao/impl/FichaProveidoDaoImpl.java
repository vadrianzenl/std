package pe.gob.congreso.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import pe.gob.congreso.dao.FichaProveidoDao;
import pe.gob.congreso.model.FichaProveido;

@Repository("fichaProveidoDao")
public class FichaProveidoDaoImpl extends AbstractDao<Integer, FichaProveido> implements FichaProveidoDao {

    @Override
    @SuppressWarnings("unchecked")
    public List<FichaProveido> find(Optional<String> proveidoId, Optional<String> codigo) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.createAlias("proveido", "p");
        criteria.createAlias("fichaDocumento", "f");

        if (proveidoId.isPresent() && !proveidoId.get().isEmpty()) {
            criteria.add(Restrictions.eq("p.id", Integer.valueOf(proveidoId.get())));
        }
        if (codigo.isPresent() && !codigo.get().isEmpty()) {
            criteria.add(Restrictions.eq("f.anioLegislativo.codigo", Integer.valueOf(codigo.get())));
        }
        criteria.add(Restrictions.eq("habilitado", true ));
        List fichasList = criteria.list();

        return fichasList;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<FichaProveido> findByIdRU(Optional<String> documentoId, Optional<String> proveidoId) throws Exception {
        Criteria criteria = createEntityCriteria();
        if (documentoId.isPresent() && !documentoId.get().isEmpty()) {
            criteria.add(Restrictions.eq("fichaDocumentoId", Integer.valueOf(documentoId.get())));
        }
        if (proveidoId.isPresent() && !proveidoId.get().isEmpty()) {
            criteria.add(Restrictions.eq("proveidoId", Integer.valueOf(proveidoId.get())));
        }
        criteria.add(Restrictions.eq("habilitado", true ));
        List fichasList = criteria.list();
        return fichasList;
    }

    @Override
    @Transactional( propagation = Propagation.REQUIRED, rollbackFor = Exception.class )
    public FichaProveido create(FichaProveido fp) throws Exception {
    	saveOrUpdate(fp);
        return fp;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<FichaProveido> findByNumeroMP(Optional<String> numeroMP, Optional<String> sumilla, Optional<String> ppIni, Optional<String> ppFin) throws Exception {

        Criteria criteria = createEntityCriteria();

        if (numeroMP.isPresent() && !numeroMP.get().isEmpty()) {
            criteria.add(Restrictions.eq("numero", Integer.valueOf(numeroMP.get())));
        }
        if (sumilla.isPresent() && !sumilla.get().isEmpty()) {
            criteria.add(Restrictions.like("sumilla", sumilla.get(), MatchMode.ANYWHERE));
        }
        if (ppIni.isPresent() && !ppIni.get().isEmpty()) {
        	criteria.add(Restrictions.eq("ppinicio", Integer.valueOf(ppIni.get())));
        }
        if (ppFin.isPresent() && !ppFin.get().isEmpty()) {
        	criteria.add(Restrictions.eq("ppfin", Integer.valueOf(ppFin.get())));
        }
        criteria.add(Restrictions.eq("habilitado", true ));
        List fichasList = criteria.list();

        return fichasList;
    }

	@Override
	public Integer updateEstadoFichaProveido(FichaProveido fichaProveido) throws Exception {
		Query query = this.getSession().createSQLQuery("update STD_FICHA_PROVEIDO set stdfp_cestado = :estado,  aud_cusuario_modifica = :usuarioModifica, aud_dfecha_modifica = :fechaModifica " +
                " where stdf_id = :fichaDocumentoId");
	   query.setParameter("estado",fichaProveido.getEstado());
       query.setParameter("fichaDocumentoId", fichaProveido.getFichaDocumentoId());
       query.setParameter("usuarioModifica", fichaProveido.getUsuarioModifica());
       query.setDate("fechaModifica",fichaProveido.getFechaModifica());
        int result = query.executeUpdate();
       return result;
	}
	
	
    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<FichaProveido> findByEstadoMP(Optional<ArrayList<String>> estados, Optional<String> ppIni, Optional<String> ppFin) throws Exception {

        Criteria criteria = createEntityCriteria();

        if (estados.isPresent() && !estados.get().isEmpty()) {
            criteria.add(Restrictions.in( "estado", estados.get().toArray(new String[estados.get().size()]) ) );
        }
        if (ppIni.isPresent() && !ppIni.get().isEmpty()) {
        	criteria.add(Restrictions.eq("ppinicio", Integer.valueOf(ppIni.get())));
        }
        if (ppFin.isPresent() && !ppFin.get().isEmpty()) {
        	criteria.add(Restrictions.eq("ppfin", Integer.valueOf(ppFin.get())));
        }
        criteria.add(Restrictions.eq("habilitado", true ));
        List fichasList = criteria.list();

        return fichasList;
    }

}
