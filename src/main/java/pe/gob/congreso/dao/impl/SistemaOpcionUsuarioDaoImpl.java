package pe.gob.congreso.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;

import pe.gob.congreso.dao.SistemaOpcionUsuarioDao;
import pe.gob.congreso.model.SistemaOpcionUsuario;

@Repository("sistemaOpcionUsuarioDao")
public class SistemaOpcionUsuarioDaoImpl extends AbstractDao<Integer, SistemaOpcionUsuario> implements SistemaOpcionUsuarioDao {

	@Override
	public List<SistemaOpcionUsuario> findByEmpleadoId(Optional<String> empleadoId) {
		Criteria criteria = createEntityCriteria();
		
		//criteria.setFetchMode("empleado", FetchMode.JOIN);
        criteria.createAlias("empleado", "e");
        criteria.add(Restrictions.eq("e.id", Integer.valueOf(empleadoId.get())));

        List<SistemaOpcionUsuario> listSistOpcUsuario = (List<SistemaOpcionUsuario>) criteria.list();
        return listSistOpcUsuario;
	}

}
