
package pe.gob.congreso.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.congreso.dao.TipoDocumentoDao;
import pe.gob.congreso.model.TipoDocumento;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.ActividadService;
import pe.gob.congreso.service.TipoDocumentoService;

@Service("tipoDocumentoService")
@Transactional
public class TipoDocumentoServiceImpl implements TipoDocumentoService{

    @Autowired
    TipoDocumentoDao tipoDocumentoDao;

    @Autowired
    ActividadService actividadService;
    
    @Override
    public TipoDocumento getTipoDocumentoId(Integer id) throws Exception {
        return tipoDocumentoDao.getTipoDocumentoId(id);
    }

    @Override
    public List<TipoDocumento> getTipoDocumentoCentroCosto(String id) throws Exception {
        return tipoDocumentoDao.getTipoDocumentoCentroCosto(id);
    }

    @Override
    public Object create(Usuario usuario, TipoDocumento t, String operacion) throws Exception {
    	actividadService.create(usuario, t, operacion);
        return tipoDocumentoDao.create(t);
    }
    
}
