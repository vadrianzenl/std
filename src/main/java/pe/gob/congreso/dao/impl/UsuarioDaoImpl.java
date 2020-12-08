package pe.gob.congreso.dao.impl;

import com.google.common.base.Optional;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.UsuarioDao;
import pe.gob.congreso.model.Usuario;

@Repository("usuarioDao")
public class UsuarioDaoImpl extends AbstractDao<Integer, Usuario> implements UsuarioDao {
	
	protected final Log log = LogFactory.getLog(getClass());

    @Override
    @SuppressWarnings("unchecked")
    public List<Usuario> findBy(Optional<String> nombre) throws DataAccessException {
    	log.debug("findBy()");
        Criteria criteria = createEntityCriteria();

        if (nombre.isPresent() && !nombre.get().isEmpty()) {
            criteria.add(Restrictions.like("nombres", nombre.get(), MatchMode.ANYWHERE));
        }

        List usuariosList = criteria.list();

        return usuariosList;
    }

    @Override
    public Optional<Usuario> findByNameUsuario(String nombreUsuario) throws DataAccessException {
    	log.info("findByNameUsuario()");

        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.eq("nombreUsuario", nombreUsuario));
        criteria.add(Restrictions.eq("habilitado", 1));

        Usuario usuario = (Usuario) criteria.uniqueResult();

        return Optional.fromNullable(usuario);
    }

	@Override
	public Optional<Usuario> findByIdEmpleado(Integer idEmpleado) throws Exception {
		Criteria criteria = createEntityCriteria();
		criteria.createAlias("empleado", "e");
        criteria.add(Restrictions.eq("e.id", idEmpleado));
        criteria.add(Restrictions.eq("habilitado", 1));
        Usuario usuario = (Usuario) criteria.uniqueResult();
        return Optional.fromNullable(usuario);
	}

}
