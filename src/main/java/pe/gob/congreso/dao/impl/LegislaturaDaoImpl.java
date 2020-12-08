package pe.gob.congreso.dao.impl;

import com.google.common.base.Optional;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.LegislaturaDao;
import pe.gob.congreso.model.Legislatura;

@Repository("legislaturaDao")
public class LegislaturaDaoImpl extends AbstractDao<String, Legislatura> implements LegislaturaDao {

    @Override
    @SuppressWarnings("unchecked")
    public List<Legislatura> findBy(Optional<String> codigo) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.createAlias("periodoLegislativo", "pl");
        if (codigo.isPresent() && !codigo.get().isEmpty()) {
            criteria.add(Restrictions.eq("pl.codigo", Integer.parseInt(codigo.get())));//Integer.parseInt(id)
        }
        List legisladurasList = criteria.list();

        return legisladurasList;
    }

    @Override
    public Legislatura getLegislaturaId(String codigo) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.eq("codigo", codigo));//Integer.parseInt(id)

        Legislatura legislatura = (Legislatura) criteria.uniqueResult();

        return legislatura;
    }

    @Override
    public Legislatura getLegislaturaActual() throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.sqlRestriction(" GETDATE() BETWEEN stdl_dfecha_inicio AND DATEADD(DAY,1,stdl_dfecha_fin) "));

        Legislatura legislatura = (Legislatura) criteria.uniqueResult();

        return legislatura;
    }

}
