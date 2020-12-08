package pe.gob.congreso.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.util.SimpleFileResolver;
import pe.gob.congreso.dao.AdjuntoDao;
import pe.gob.congreso.dao.DerivaDao;
import pe.gob.congreso.dao.FichaDocumentoDao;
import pe.gob.congreso.dao.RelacionaDao;
import pe.gob.congreso.dao.TipoDao;
import pe.gob.congreso.model.Adjunto;
import pe.gob.congreso.model.Bitacora;
import pe.gob.congreso.model.Deriva;
import pe.gob.congreso.model.FichaDocumento;
import pe.gob.congreso.model.Relaciona;
import pe.gob.congreso.model.Tipo;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.ActividadService;
import pe.gob.congreso.service.BitacoraService;
import pe.gob.congreso.service.RelacionaService;
import pe.gob.congreso.util.ReportHelper;
import pe.gob.congreso.util.ReportType;

@Service("relacionaService")
@Transactional
public class RelacionaServiceImpl implements RelacionaService {

    @Autowired
    RelacionaDao relacionaDao;
    
    @Autowired
    FichaDocumentoDao fichaDocumentoDao;

    @Autowired
    ActividadService actividadService;
    
    @Autowired
	BitacoraService bitacoraService;
    
    @Autowired
    TipoDao tipoDao;
    
    @Autowired
    AdjuntoDao adjuntoDao;
    
    @Autowired
    DerivaDao derivaDao;
    
    protected final Log log = LogFactory.getLog(getClass());
    
    private final ReportHelper reportHelper = new ReportHelper();

    @Override
    public Object create(Usuario usuario, Relaciona d, String operacion) throws Exception {
    	actividadService.create(usuario, d, operacion);
    	
    	log.info("create()");
		Integer id = d.getId();						
		relacionaDao.create(d);
		Tipo t1 = tipoDao.findByNombre("ESTADO_RELACIONADO");

		if (!Optional.fromNullable(id).isPresent()) {			
			Bitacora b = new Bitacora();
			b.setFecha(new Date());
			b.setEmpleado(d.getUsuarioRelaciona().getEmpleado());
			b.setCentroCosto(usuario.getEmpleado().getCentroCosto());
			if (RelacionaService.TIPO_RELACION_RESPUESTA.equals(d.getTipoRelacion().getId())){
				b.setDescripcion("CONFIGURÓ EN RESPUESTA AL RU "+ d.getFichaRelacionada().getId());
			} else {
				b.setDescripcion("CONFIGURÓ EN REFERENCIA AL RU "+ d.getFichaRelacionada().getId());
			}			
			b.setIndicaciones("CREACIÓN DE RELACION CORRECTA");
			b.setEstado(t1);
			b.setFichaDocumento(d.getFichaDocumento());

			//log.debug("Creando Bitacora FichaDocumento :"+ b.toString());
			bitacoraService.create(usuario, b, operacion);
			
			b = new Bitacora();
			b.setFecha(new Date());
			b.setEmpleado(d.getUsuarioRelaciona().getEmpleado());
			b.setCentroCosto(usuario.getEmpleado().getCentroCosto());
			if (RelacionaService.TIPO_RELACION_RESPUESTA.equals(d.getTipoRelacion().getId())){
				b.setDescripcion("RESPONDIDO POR EL RU "+ d.getFichaDocumento().getId());
			} else {
				b.setDescripcion("REFERENCIADO POR EL RU "+ d.getFichaDocumento().getId());
			}			
			b.setIndicaciones("CREACIÓN DE RELACION CORRECTA");
			b.setEstado(t1);
			b.setFichaDocumento(d.getFichaRelacionada());

			//log.debug("Creando Bitacora FichaRelacionada :"+ b.toString());
			bitacoraService.create(usuario, b, operacion);
		}
		log.debug("Relaciona = " + d.toString());
    	
        return d;
    }

    @Override
    public List<Relaciona> findRelacionados(String fichaDocumentoId) throws Exception {
        return relacionaDao.findRelacionados(fichaDocumentoId);
    }

    @Override
    public List<Relaciona> findAsociados(String fichaDocumentoId) throws Exception {
        return relacionaDao.findAsociados(fichaDocumentoId);
    }

    @Override
    public List<FichaDocumento> findFichaRelacionados(String fichaDocumentoId) throws Exception {
    	
    	List<FichaDocumento> lstFichaDocumento = new ArrayList<FichaDocumento>();
    	
    	List<Integer> lstFichaId = relacionaDao.findFichaIdRelacionados(fichaDocumentoId);
    	
    	if (lstFichaId.size() > 1){
    		lstFichaDocumento = fichaDocumentoDao.getFichaDocumentoByListId(lstFichaId, fichaDocumentoId);
    		for(FichaDocumento f : lstFichaDocumento){
    			List<Adjunto> listAdjunto = adjuntoDao.findAdjuntos(f.getId().toString());
    			if(listAdjunto!=null){
    				if(listAdjunto.size()> 0){
            			f.setAdjuntos(true);
            		}else{
            			f.setAdjuntos(false);
            		}
    			}
        		
        		List<Deriva> listDeriva =  derivaDao.getOnlyDerivados(f.getId().toString());
        		if(listDeriva!=null){
    				if(listDeriva.size()> 0){
    					String centrosCosto = "";
    		        	for(Deriva d : listDeriva) {
    		        		centrosCosto += d.getCentroCostoDestino().getId().trim() + "," + d.getCentroCostoOrigen().getId().trim() + ",";
    		        	}
    		        	f.setListDirigidos(centrosCosto);
            		}else{
            		}
    			}	
    		}
    		
    	}
    	
    	
    	
        return lstFichaDocumento;
    }
    
    @Override
    public List<Relaciona> findRespuestaA(String fichaDocumentoId) throws Exception {
    	
    	List<Relaciona> lsRelaciona = relacionaDao.findByTipo(fichaDocumentoId, RelacionaService.TIPO_RELACION_RESPUESTA);
    	
    	for(Relaciona relaciona : lsRelaciona){
    		
    		List<Adjunto> listAdjunto = adjuntoDao.findAdjuntos(relaciona.getFichaRelacionada().getId().toString());
    		if(listAdjunto.size()> 0){
    			relaciona.setAdjuntos(true);
    		}else{
    			relaciona.setAdjuntos(false);
    		}
    		
    		List<Deriva> listDeriva = derivaDao.findDirigidosResponsable(relaciona.getFichaRelacionada().getId().toString());
        	String centrosCosto = "";
        	for(Deriva d : listDeriva) {
        		if(d.getCentroCostoDestino()!=null && d.getCentroCostoOrigen()!=null){
        			centrosCosto += d.getCentroCostoDestino().getId().trim() + "," + d.getCentroCostoOrigen().getId().trim() + ",";
        		}
        	}
        	relaciona.setListDirigidos(centrosCosto);
    	}
    	
        return lsRelaciona;
    }
    
    @Override
    public List<Relaciona> findReferencia(String fichaDocumentoId) throws Exception {
        return relacionaDao.findByTipo(fichaDocumentoId, RelacionaService.TIPO_RELACION_REFERENCIA);
    }

	@Override
	public Object reporteRelacionados(HttpServletRequest request, Usuario usuario, String ru) throws Exception {
		String centroCostoDes = "";
		String usuarioDes = "";
		String tipoDocumento = "";
		String numeroDocumento = "";
		if (Optional.fromNullable(usuario).isPresent()) {
			centroCostoDes = usuario.getEmpleado().getCentroCosto().getDescripcion();
			usuarioDes = usuario.getNombreUsuario();
		}
		
		FichaDocumento ficha = fichaDocumentoDao.getFichaDocumentoId(Integer.parseInt(ru));
		if (Optional.fromNullable(ficha).isPresent()) {
			tipoDocumento = ficha.getTipoDocumento().getTipo().getDescripcion();
			numeroDocumento = ficha.getNumeroDoc();
		}
		
		File reportsDir = new File(request.getSession().getServletContext().getRealPath("/assets/"));
		if (!reportsDir.exists()) {
			try {
				throw new FileNotFoundException(String.valueOf(reportsDir));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("usuario", usuarioDes);
		map.put("ru", Integer.valueOf(ru));
		map.put("centroCostoDes", centroCostoDes);
		map.put("tipoDocumento", tipoDocumento);
		map.put("numeroDoc", numeroDocumento);
		map.put(JRParameter.REPORT_LOCALE, Locale.US);
		map.put(JRParameter.REPORT_FILE_RESOLVER, new SimpleFileResolver(reportsDir));

		String path = request.getSession().getServletContext().getRealPath("/assets/rptRelacionados.jrxml");

		ByteArrayOutputStream baos = reportHelper.generateDataSource(path, map, ReportType.FILE.PDF,
				ReportType.DATASOURCE.WITH);
		byte[] bytes = baos.toByteArray();
		return bytes;
	}

}
