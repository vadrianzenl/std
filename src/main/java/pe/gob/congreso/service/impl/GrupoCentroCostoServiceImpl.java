package pe.gob.congreso.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import pe.gob.congreso.dao.GrupoCentroCostoDao;
import pe.gob.congreso.model.GrupoCentroCosto;
import pe.gob.congreso.model.MpCanalAsociadoEnvio;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.service.ActividadService;
import pe.gob.congreso.service.CanalAsociadoEnvioService;
import pe.gob.congreso.service.GrupoCentroCostoService;
import pe.gob.congreso.util.Constantes;

@Service("grupoCentroCostoService")
@Transactional
public class GrupoCentroCostoServiceImpl implements GrupoCentroCostoService {

    @Autowired
    ActividadService actividadService;

    @Autowired
    GrupoCentroCostoDao grupoCentroCostoDao;
    
    @Autowired
    CanalAsociadoEnvioService canalAsociadoEnvioService;

    @Override
    public Map<String, Object> findBy(Optional<String> descripcion, Optional<String> pag, Optional<String> pagLength) throws Exception {
        return grupoCentroCostoDao.findBy(descripcion, pag, pagLength);
    }

    @Override
    public GrupoCentroCosto getCentroCostoActual(String id) throws Exception {
        return grupoCentroCostoDao.getCentroCostoActual(id);        
    }

    @Override
    public List<InputSelectUtil> getCentrosCostoByGrupo(String grupoId) throws Exception {
       return grupoCentroCostoDao.getCentrosCostoByGrupo(grupoId);
    }

    @Override
    public List<GrupoCentroCosto> getCentrosCosto(String grupoId) throws Exception {
        return grupoCentroCostoDao.getCentrosCosto(grupoId);
    }

    @Override
    public List<InputSelectUtil> getCentroCostoActualInputSelect() throws Exception {
        return grupoCentroCostoDao.getCentroCostoActualInputSelect();
    }

    @Override
    public Map<String, Object> find(Optional<String> grupo, Optional<String> dependencia, Optional<String> pag, Optional<String> pagLength) throws Exception {
        return grupoCentroCostoDao.find(grupo, dependencia, pag, pagLength);
    }

    @Override
    public List<GrupoCentroCosto> getGrupoCentroCosto() throws Exception {
    	List<GrupoCentroCosto> grupos = grupoCentroCostoDao.getGrupoCentroCosto();
    	for ( GrupoCentroCosto g : grupos ) {
			List<MpCanalAsociadoEnvio> canal = canalAsociadoEnvioService.getCanalAsociado(g.getGrupo().getId().toString(),g.getId().toString().trim(),g.getEmpleado().getId().toString());			
			if ( canal.size() > 0  ) {
				if ( Constantes.ENVIO_PARA_ESTAFETA.toString().equals(canal.get(0).getIndCanal().trim()) ) { 
					g.setIndCanal(Constantes.IND_CANAL_ESTAFETA); 
					g.setIndCambio(Constantes.IND_CANAL_ESTAFETA);
				} else {
					g.setIndCanal(Constantes.IND_CANAL_CASILLERO);
					g.setIndCambio(Constantes.IND_CANAL_CASILLERO);
				}
			}
    	}
    	return grupos;
    }

    @Override
    public Object create(Usuario usuario, GrupoCentroCosto gcc, String operacion) throws Exception {
    	actividadService.create(usuario, gcc, operacion);
        return grupoCentroCostoDao.create(gcc);
    }
    
    public List<GrupoCentroCosto> getCentrosCostoByGrupoNotIn(String[] listGruposId)  throws Exception {
    	return grupoCentroCostoDao.getCentrosCostoByGrupoNotIn(listGruposId);
    }

}
