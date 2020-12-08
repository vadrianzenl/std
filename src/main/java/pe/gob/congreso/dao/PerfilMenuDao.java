package pe.gob.congreso.dao;

import java.util.List;
import pe.gob.congreso.model.PerfilMenu;

public interface PerfilMenuDao {

    public List<PerfilMenu> findModules(String perfilId) throws Exception;

    public List<PerfilMenu> findMenus(String perfilId, String padreId) throws Exception;
}
