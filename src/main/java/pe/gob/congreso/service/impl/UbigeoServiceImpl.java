
package pe.gob.congreso.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.congreso.dao.UbigeoDao;
import pe.gob.congreso.model.UbigeoMaestro;
import pe.gob.congreso.service.UbigeoService;

@Service("ubigeoService")
@Transactional
public class UbigeoServiceImpl implements UbigeoService{
    
    @Autowired
    UbigeoDao ubigeoDao;

    @Override
    public UbigeoMaestro getUbigeo(String codigo) throws Exception {
        return ubigeoDao.getUbigeo(codigo);
    }

    @Override
    public List<UbigeoMaestro> getDepartamentos() throws Exception {
        return ubigeoDao.getDepartamentos();
    }

    @Override
    public List<UbigeoMaestro> getProvincias(String departamento) throws Exception {
        return ubigeoDao.getProvincias(departamento);
    }

    @Override
    public List<UbigeoMaestro> getDistritos(String provincia) throws Exception {
        return ubigeoDao.getDistritos(provincia);
    }
    
}
