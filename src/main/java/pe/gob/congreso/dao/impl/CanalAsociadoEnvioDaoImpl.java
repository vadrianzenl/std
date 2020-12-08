package pe.gob.congreso.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.congreso.dao.CanalAsociadoEnvioDao;
import pe.gob.congreso.model.MpCanalAsociadoEnvio;
import pe.gob.congreso.model.Tipo;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;


@Repository("canalAsociadoEnvioDao")
public class CanalAsociadoEnvioDaoImpl extends AbstractDao<Integer, MpCanalAsociadoEnvio> implements CanalAsociadoEnvioDao{

    @Override
    public MpCanalAsociadoEnvio create(MpCanalAsociadoEnvio ce) throws Exception {
        saveOrUpdate(ce);
        return ce;
    }
    
	@Override
	@Transactional(readOnly = true)
	public List<MpCanalAsociadoEnvio> getCanalAsociado(String idGrupo, String idDependencia, String idEmpleado)	throws Exception {
		Criteria criteria = createEntityCriteria();		
        if (idGrupo != null && !idGrupo.isEmpty()) {
            criteria.add(Restrictions.eq("grupoId", Integer.valueOf(idGrupo.trim())));
        }
        if (idDependencia != null && !idDependencia.isEmpty() && NumberUtils.isNumber(idDependencia.trim())) {
            criteria.add(Restrictions.eq("dependenciaId", Integer.valueOf(idDependencia.trim())));
        }
        if (idEmpleado != null && !idEmpleado.isEmpty() && NumberUtils.isNumber(idEmpleado)) {
            criteria.add(Restrictions.eq("empleadoId", Integer.valueOf(idEmpleado.trim())));
        }
        criteria.add(Restrictions.eq("habilitado", true ));
		return criteria.list();
		
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<MpCanalAsociadoEnvio> getCanalAsociadoByIndCanal(String indCanal, String estado, Boolean habilitado) throws Exception {
		Criteria criteria = createEntityCriteria();		
        if (indCanal != null && !indCanal.isEmpty()) {
            criteria.add(Restrictions.eq("indCanal", indCanal));
        }
        if (estado != null && !estado.isEmpty() && NumberUtils.isNumber(estado)) {
            criteria.add(Restrictions.eq("estado", estado));
        }
        if (habilitado != null) {
            criteria.add(Restrictions.eq("habilitado", habilitado ));
        }
		return criteria.list();
		
	}

}
