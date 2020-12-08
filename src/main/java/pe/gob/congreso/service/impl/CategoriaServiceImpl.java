package pe.gob.congreso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import pe.gob.congreso.dao.CategoriaDao;
import pe.gob.congreso.dao.CentroCostoSubCategoriaDao;
import pe.gob.congreso.dao.SubCategoriaDao;
import pe.gob.congreso.model.Categoria;
import pe.gob.congreso.model.SubCategoria;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.service.ActividadService;
import pe.gob.congreso.service.CategoriaService;

@Service("categoriaService")
@Transactional
public class CategoriaServiceImpl implements CategoriaService {
    
    @Autowired
    CategoriaDao categoriaDao;
    
    @Autowired
    SubCategoriaDao subCategoriaDao;
    
    @Autowired
    CentroCostoSubCategoriaDao centroCostoSubCategoriaDao;
    
    @Autowired
    ActividadService actividadService;
    
    @Override
    public List<Categoria> getCategorias() throws Exception {
        return categoriaDao.findBy();
    }
    
    @Override
    public Object getCategoriaId(String id) throws Exception {
        return categoriaDao.getCategoriaId(id);
    }
    
    @Override
    public List<InputSelectUtil> getCategoriasInputSelect() throws Exception {
        return categoriaDao.getCategoriasInputSelect();
    }
    
    @Override
    public Map<String, Object> getCategoriaCentroCosto(String centroCostoId) throws Exception {
        return centroCostoSubCategoriaDao.find(Optional.fromNullable(centroCostoId), Optional.fromNullable(""));
    }
    
    @Override
    public Object createCategoria(Usuario usuario, Categoria c, String operacion) throws Exception {
        Categoria ca = new Categoria();
        actividadService.create(usuario, c, operacion);
        ca = categoriaDao.create(c);
        
        SubCategoria s = new SubCategoria();
        s.setDescripcion(ca.getDescripcion());
        s.setTema(ca.getDescripcion());
        s.setCategoria(ca);
        s.setHabilitado(true);
        s.setPublicar(true);
        s.setUsuarioCrea(usuario.getNombreUsuario());
        s.setFechaCrea(new Date());
        s.setPrivado(false);
        
        actividadService.create(usuario, s, operacion);
        subCategoriaDao.create(s);
        
        return ca;
    }
    
    @Override
    public Object editCategoria(Usuario usuario, Categoria c, String operacion) throws Exception {               
        actividadService.create(usuario, c, operacion);
        c.setHabilitado(false);                
        return categoriaDao.create(c);
    }
    
}
