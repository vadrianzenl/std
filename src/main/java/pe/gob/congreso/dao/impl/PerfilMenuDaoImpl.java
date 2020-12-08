package pe.gob.congreso.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.PerfilMenuDao;
import pe.gob.congreso.model.PerfilMenu;

@Repository("perfilMenuDao")
public class PerfilMenuDaoImpl extends AbstractDao<String, PerfilMenu> implements PerfilMenuDao {

    @Override
    public List<PerfilMenu> findModules(String perfilId) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.createAlias("menu", "m");
        criteria.add(Restrictions.eq("perfil.id", perfilId));
        //criteria.add(Restrictions.eqProperty("m.id", "m.padre"));
        criteria.addOrder(Order.asc("m.id"));

        List perfilesMenuList = criteria.list();

        return perfilesMenuList;
    }

    @Override
    public List<PerfilMenu> findMenus(String perfilId, String padreId) throws Exception {

        Criteria criteria = createEntityCriteria();

        criteria.createAlias("menu", "m");
        criteria.add(Restrictions.eq("perfil.id", perfilId));
        criteria.add(Restrictions.eq("m.padre", padreId));
        criteria.addOrder(Order.asc("m.orden"));

        List perfilesMenuList = criteria.list();

        return perfilesMenuList;
    }

}
