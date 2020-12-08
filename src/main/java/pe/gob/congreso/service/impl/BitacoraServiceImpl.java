
package pe.gob.congreso.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.util.SimpleFileResolver;
import pe.gob.congreso.dao.BitacoraDao;
import pe.gob.congreso.dao.DerivaDao;
import pe.gob.congreso.model.Bitacora;
import pe.gob.congreso.model.Deriva;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.SeguimientoUtil;
import pe.gob.congreso.service.ActividadService;
import pe.gob.congreso.service.BitacoraService;
import pe.gob.congreso.util.ReportHelper;
import pe.gob.congreso.util.ReportType;

@Service("bitacoraService")
@Transactional
public class BitacoraServiceImpl implements BitacoraService{

    @Autowired
    ActividadService actividadService;
    
    @Autowired
    BitacoraDao bitacoraDao;
    
    @Autowired
    DerivaDao derivaDao;
    
    private final ReportHelper reportHelper = new ReportHelper();
    
    @Override
    public Object create(Usuario usuario, Bitacora b, String operacion) throws Exception {    	
    	actividadService.create(usuario, b, "CREAR");
        return bitacoraDao.create(b);
    }

    @Override
    public List<Bitacora> findBitacoras(String fichaDocumentoId) throws Exception {
    	List<Bitacora> lista = bitacoraDao.findBitacoras(fichaDocumentoId);
    	List<Deriva> listaVacia = new ArrayList<Deriva>();
        for (Bitacora b : lista) {
        	List<Deriva> listDeriva = derivaDao.findDirigidosResponsable(b.getFichaDocumento().getId().toString());
        	String centrosCosto = "";
        	for(Deriva d : listDeriva) {
        		if(Optional.fromNullable(d.getCentroCostoDestino()).isPresent() && 
        				Optional.fromNullable(d.getCentroCostoOrigen()).isPresent()){
        			centrosCosto += d.getCentroCostoDestino().getId().trim() + "," + d.getCentroCostoOrigen().getId().trim() + ",";
        		}        		
        	}
        	b.setListDeriva(centrosCosto);
        }
    	return lista;
    }

	@Override
	public Object reporteAcciones(HttpServletRequest request,Usuario usuario, String ru) throws Exception {
		String centroCostoDes = "";
		String usuarioDes = "";
		if (Optional.fromNullable(usuario).isPresent()) {
			centroCostoDes = usuario.getEmpleado().getCentroCosto().getDescripcion();
			usuarioDes = usuario.getNombreUsuario();
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
		map.put(JRParameter.REPORT_LOCALE, Locale.US);
		map.put(JRParameter.REPORT_FILE_RESOLVER, new SimpleFileResolver(reportsDir));

		String path = request.getSession().getServletContext().getRealPath("/assets/rptSeguimiento.jrxml");

		ByteArrayOutputStream baos = reportHelper.generateDataSource(path, map, ReportType.FILE.PDF,
				ReportType.DATASOURCE.WITH);
		byte[] bytes = baos.toByteArray();
		return bytes;
	}

	@Override
	public Object reporteModificaciones(HttpServletRequest request,Usuario usuario, String ru) throws Exception {
		String centroCostoDes = "";
		String usuarioDes = "";
		if (Optional.fromNullable(usuario).isPresent()) {
			centroCostoDes = usuario.getEmpleado().getCentroCosto().getDescripcion();
			usuarioDes = usuario.getNombreUsuario();
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
		map.put(JRParameter.REPORT_LOCALE, Locale.US);
		map.put(JRParameter.REPORT_FILE_RESOLVER, new SimpleFileResolver(reportsDir));

		String path = request.getSession().getServletContext().getRealPath("/assets/rptModificaciones.jrxml");

		ByteArrayOutputStream baos = reportHelper.generateDataSource(path, map, ReportType.FILE.PDF,
				ReportType.DATASOURCE.WITH);
		byte[] bytes = baos.toByteArray();
		return bytes;
	}

	@Override
	public List<SeguimientoUtil> getSeguimiento(String fichaDocumentoId) throws Exception {		
		return bitacoraDao.getSeguimiento(fichaDocumentoId);
	}
    
}
