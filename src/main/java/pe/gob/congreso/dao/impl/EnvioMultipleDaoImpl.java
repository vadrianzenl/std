package pe.gob.congreso.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import pe.gob.congreso.dao.EnvioMultipleDao;
import pe.gob.congreso.model.EnvioMultiple;
import pe.gob.congreso.model.util.InputSelectUtil;

@Repository("envioMultipleDao")
public class EnvioMultipleDaoImpl extends AbstractDao<Integer, EnvioMultiple> implements EnvioMultipleDao {

	protected final Log log = LogFactory.getLog(getClass());
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EnvioMultiple> getByIdFk(Optional<String> fichaDocumentoId, Optional<String> proveidoId)
			throws Exception {
		Criteria criteria = createEntityCriteria();		
		if (fichaDocumentoId.isPresent() && !fichaDocumentoId.get().isEmpty()) {
            criteria.add(Restrictions.eq("fichaDocumentoId", Integer.parseInt(fichaDocumentoId.get())));
        }
		if (proveidoId.isPresent() && !proveidoId.get().isEmpty()) {
            criteria.add(Restrictions.eq("proveidoId", Integer.parseInt(proveidoId.get())));
        }
		criteria.add(Restrictions.eq("habilitado", true ));
		return criteria.list();
	}

	
	@Override
	@Transactional(readOnly = true)
	public Object createEnvioMultiple(EnvioMultiple em) throws Exception {
		saveOrUpdate(em);        
        return em;
	}


	@Override
	public List<EnvioMultiple> getByIdGestionEnvio(Optional<String> gestionEnvioId,Optional<String> fichaDocumentoId) throws Exception {
		// TODO Auto-generated method stub
		Criteria criteria = createEntityCriteria();		
		if (gestionEnvioId.isPresent() && !gestionEnvioId.get().isEmpty()) {
            criteria.add(Restrictions.eq("gestionEnvioId", Integer.parseInt(gestionEnvioId.get())));
        }
		if (fichaDocumentoId.isPresent() && !fichaDocumentoId.get().isEmpty()) {
            criteria.add(Restrictions.eq("fichaDocumentoId", Integer.parseInt(fichaDocumentoId.get())));
        }
		criteria.add(Restrictions.eq("reporte", true ));
		criteria.add(Restrictions.eq("habilitado", true ));
		
		return criteria.list();
	}


	@Override
	public Integer updateEnvioMultiple(EnvioMultiple envioMultiple) throws Exception {
		Query query = this.getSession().createSQLQuery("update STD_ENVIO_MULTIPLE set stddem_breporte = :reporte,  aud_cusuario_modifica = :usuarioModifica, aud_dfecha_modifica = :fechaModifica " +
                " where stddem_gestionEnvioId = :gestionEnvioId and stdf_id =:fichaDocumentoId");
	   query.setParameter("reporte",envioMultiple.getReporte());
       query.setParameter("usuarioModifica",envioMultiple.getUsuarioModifica());
       query.setDate("fechaModifica", envioMultiple.getFechaModifica());
       query.setParameter("gestionEnvioId",envioMultiple.getGestionEnvioId());
       query.setParameter("fichaDocumentoId",envioMultiple.getFichaDocumentoId());
       int result = query.executeUpdate();
       
       return result; 
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EnvioMultiple> getByIdDocumentoIdReporte(Optional<String> fichaDocumentoId, Optional<String> gestionEnvioId) throws Exception {
		Criteria criteria = createEntityCriteria();		
		if (fichaDocumentoId.isPresent() && !fichaDocumentoId.get().isEmpty()) {
            criteria.add(Restrictions.eq("fichaDocumentoId", Integer.parseInt(fichaDocumentoId.get())));
        }
		if (gestionEnvioId.isPresent() && !gestionEnvioId.get().isEmpty()) {
            criteria.add(Restrictions.eq("gestionEnvioId", Integer.parseInt(gestionEnvioId.get())));
        }
		return criteria.list();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<InputSelectUtil> getCantidadCasillerosPendientes(Map parametros) throws Exception {
		List<InputSelectUtil> lista = new ArrayList<>();
		StringBuffer  consulta = new StringBuffer();
		consulta.append("SELECT DISTINCT envio.stdf_id                         fichaId, ");
		consulta.append("		envio.stddem_idcentrocosto                     idcentrocosto, ");
		consulta.append("       (SELECT stdcc_cdescripcion ");
		consulta.append("        FROM   std_centro_costo ");
		consulta.append("        WHERE  stdcc_id = envio.stddem_idcentrocosto) descripcion ");		
		consulta.append("FROM   std_envio_multiple envio ");
		consulta.append("       INNER JOIN std_ficha_documento fd ");
		consulta.append("               ON envio.stdf_id = fd.stdf_id ");
		consulta.append("WHERE  envio.stddem_idcentrocosto IN (SELECT ");
		consulta.append("       Substring(tipo.stdt_vdescripcion, 1, 10) ");
		consulta.append("                                      FROM   std_tipo tipo ");
		consulta.append("                                      WHERE ");
		consulta.append("       tipo.stdt_vnombre = 'TIPO_CASILLERO' ");
		consulta.append("       AND tipo.stdt_bhabilitado = :habilitado ) ");
		consulta.append("       AND envio.stddem_bhabilitado = :habilitado ");
		consulta.append("       AND envio.stddem_gestionenvioid IS NULL ");
		consulta.append("       AND envio.stddem_ccanal_envio = :canalEnvio ");
		consulta.append("       AND fd.stdf_bhabilitado = :habilitado ");
		consulta.append("       AND fd.stdt_iestado IN ( :estados ) ");		
		Query query = this.getSession().createSQLQuery(consulta.toString())
			  .addScalar("fichaId", new IntegerType())
			  .addScalar("idCentroCosto", new StringType())
			  .addScalar("descripcion", new StringType());
		query.setParameter("canalEnvio", parametros.get("canalEnvio") );
        query.setParameter("habilitado", parametros.get("habilitado") );
        query.setParameterList("estados", (Object[]) parametros.get("estados").toString().split(","), new StringType() );
        List listaQuery = query.list();
        Map<String, Integer> conteo = new HashMap<String, Integer>();
        Map<String, String> descripcion = new HashMap<String, String>();
        for (Object obj : listaQuery) {
        	Object[] array = (Object[]) obj;        	
        	if ( conteo.containsKey(array[1].toString()) ) {
        		conteo.put(array[1].toString(), Integer.parseInt(String.valueOf(conteo.get(array[1].toString()))) + 1 );        		
        	} else {
        		conteo.put(array[1].toString(), 1 );
        		descripcion.put(array[1].toString(), array[2].toString());
        	}
        }
        for (Map.Entry<String, Integer> entry : conteo.entrySet()) {
        	InputSelectUtil i = new InputSelectUtil();
    		i.setValue(entry.getKey());
    		StringBuffer  sb = new StringBuffer();
    		sb.append(descripcion.get(entry.getKey()) ).append(" ( ").append(entry.getValue()).append(" ) ");
    		i.setLabel(sb.toString());
    		lista.add(i);
        }
        return lista;
    }


	@Override
	@Transactional(readOnly = true)
	public List<EnvioMultiple> getByIdCentroCosto(Optional<String> fichaDocumentoId, Optional<String> proveidoId,
		Optional<String> centroCostoId, Optional<String> canalEnvio) throws Exception {
		Criteria criteria = createEntityCriteria();
		if (fichaDocumentoId.isPresent() && !fichaDocumentoId.get().isEmpty()) {
            criteria.add(Restrictions.eq("fichaDocumentoId", Integer.parseInt(fichaDocumentoId.get())));
        }
		if (proveidoId.isPresent() && !proveidoId.get().isEmpty()) {
            criteria.add(Restrictions.eq("proveidoId", Integer.parseInt(proveidoId.get())));
        }
		if (centroCostoId.isPresent() && !centroCostoId.get().isEmpty()) {
            criteria.add(Restrictions.eq("centroCostoId", centroCostoId.get()));
        }
		if (canalEnvio.isPresent() && !canalEnvio.get().isEmpty()) {
            criteria.add(Restrictions.eq("canalEnvio", canalEnvio.get()));
        }
		criteria.add(Restrictions.eq("habilitado", true ));
		return criteria.list();
	}


}
