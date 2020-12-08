package pe.gob.congreso.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;
import pe.gob.congreso.dao.EnviadoExternoDao;
import pe.gob.congreso.model.Empleado;
import pe.gob.congreso.model.EnviadoExterno;

@Repository("enviadoExternoDao")
public class EnviadoExternoDaoImpl extends AbstractDao<Integer, EnviadoExterno> implements EnviadoExternoDao {

	protected final Log log = LogFactory.getLog(getClass());
	
    public void setFetchMode(Criteria criteria) {
        criteria.setFetchMode("fichaDocumento", FetchMode.JOIN);
        criteria.setFetchMode("empleado", FetchMode.JOIN);
        criteria.setFetchMode("tipoEnviado", FetchMode.JOIN);
    }

    @Override
    public EnviadoExterno create(EnviadoExterno d) throws Exception {
        saveOrUpdate(d);
        return d;
    }

    @Override
    public EnviadoExterno findEnviadoPor(String fichaDocumentoId) throws Exception {

        Criteria criteria = createEntityCriteria();

        this.setFetchMode(criteria);
        criteria.createAlias("fichaDocumento", "f");
        criteria.createAlias("tipoEnviado", "t");
        criteria.add(Restrictions.eq("f.id", Integer.valueOf(fichaDocumentoId)));
        criteria.add(Restrictions.eq("t.nombre","TIPO_ENVIADO_POR"));
        
        List<EnviadoExterno> lista = criteria.list();
        EnviadoExterno enviadoExterno = null;
        if(lista.size()>0){
        	if(lista.size()==1){
        		enviadoExterno = (EnviadoExterno) lista.get(0);
        	}else{
        		boolean encontro= false;
        		for(EnviadoExterno em : lista){
        			if(!encontro){
        				switch(em.getTipoEnviado().getId()){
            			case 24:
            				if(Optional.fromNullable(em.getEmpleado()).isPresent()){
            					enviadoExterno = em;
            					encontro= true;
            				}
            				break;
            			case 25:
            				if(Optional.fromNullable(em.getApellidos()).isPresent() || Optional.fromNullable(em.getNombres()).isPresent() || Optional.fromNullable(em.getOrigen()).isPresent()){
            					enviadoExterno = em;
            					encontro= true;
            				}
            				break;
            			default:
            				break;        					
            			}
        			}
        			
        		}
        	}
        }        
                
        return enviadoExterno;
    }

	@Override
	public List<EnviadoExterno> findEnviadoA(String fichaDocumentoId) throws Exception {
		Criteria criteria = createEntityCriteria();

        this.setFetchMode(criteria);
        criteria.createAlias("fichaDocumento", "f");
        criteria.createAlias("tipoEnviado", "t");
        criteria.add(Restrictions.eq("f.id", Integer.valueOf(fichaDocumentoId)));
        criteria.add(Restrictions.eq("t.nombre","TIPO_ENVIADO_A"));

        List enviadosExterno = criteria.list();
        return enviadosExterno;
	}

	@Override
	public EnviadoExterno findById(Integer id) throws Exception {
		log.debug("findById()");
        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.eq("id", id));

        EnviadoExterno externo = (EnviadoExterno) criteria.uniqueResult();
        return externo;
	}

}
