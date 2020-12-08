
package pe.gob.congreso.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import pe.gob.congreso.dao.CentroCostoSubCategoriaDao;
import pe.gob.congreso.dao.SubCategoriaDao;
import pe.gob.congreso.model.CentroCostoSubCategoria;
import pe.gob.congreso.model.SubCategoria;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.service.ActividadService;
import pe.gob.congreso.service.SubCategoriaService;

@Service("subCategoriaService")
@Transactional
public class SubCategoriaServiceImpl implements SubCategoriaService{

    @Autowired
    SubCategoriaDao subCategoriaDao;
    
    @Autowired
    CentroCostoSubCategoriaDao centroCostoSubCategoriaDao;

    @Autowired
    ActividadService actividadService;

    @Override
    public Object createCategoria(Usuario usuario, SubCategoria sd, String operacion) throws Exception{
		actividadService.create(usuario, sd, operacion);
	    return subCategoriaDao.create(sd);
    }

    @Override
    public List<SubCategoria> findBy(Optional<String> categoriaId) throws Exception{
        return subCategoriaDao.findBy(categoriaId);
    }

    @Override
    public SubCategoria getSubCategoriaId(Integer id) throws Exception{
        return subCategoriaDao.getSubCategoriaId(id);
    }

    @Override
    public List<InputSelectUtil> getSubCategoríasInputSelect(Optional<String> categoriaId) throws Exception{
        return subCategoriaDao.getSubCategoríasInputSelect(categoriaId);
    }

    @Override
    public Object createCentroCostoSubCategoria(Usuario usuario, CentroCostoSubCategoria fd, String operacion) throws Exception {
        actividadService.create(usuario, fd, operacion);
        return centroCostoSubCategoriaDao.create(fd);
    }

    @Override
    public List<CentroCostoSubCategoria> getSubCategoriasByCC(String centroCostoId) throws Exception {
        return centroCostoSubCategoriaDao.getSubCategoriasByCC(centroCostoId);
    }
        
}
