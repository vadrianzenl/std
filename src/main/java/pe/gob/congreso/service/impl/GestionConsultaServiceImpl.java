package pe.gob.congreso.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.SimpleFileResolver;
import pe.gob.congreso.dao.AdjuntoDao;
import pe.gob.congreso.dao.CanalAsociadoEnvioDao;
import pe.gob.congreso.dao.DerivaDao;
import pe.gob.congreso.dao.EnviadoExternoDao;
import pe.gob.congreso.dao.FichaDocumentoDao;
import pe.gob.congreso.dao.FichaProveidoDao;
import pe.gob.congreso.dao.GrupoCentroCostoDao;
import pe.gob.congreso.model.Adjunto;
import pe.gob.congreso.model.CentroCosto;
import pe.gob.congreso.model.Deriva;
import pe.gob.congreso.model.EnviadoExterno;
import pe.gob.congreso.model.EnvioMultiple;
import pe.gob.congreso.model.FichaDocumento;
import pe.gob.congreso.model.FichaProveido;
import pe.gob.congreso.model.GrupoCentroCosto;
import pe.gob.congreso.model.MpCanalAsociadoEnvio;
import pe.gob.congreso.model.Tipo;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.DerivaUtil;
import pe.gob.congreso.model.util.EnviadoUtil;
import pe.gob.congreso.model.util.FichaUtil;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.model.util.Util;
import pe.gob.congreso.service.DerivaService;
import pe.gob.congreso.service.EnvioMultipleService;
import pe.gob.congreso.service.GestionConsultaService;
import pe.gob.congreso.service.GestionEnvioService;
import pe.gob.congreso.util.Constantes;
import pe.gob.congreso.util.FichaConsultas;
import pe.gob.congreso.util.FichaPedientes;
import pe.gob.congreso.util.Reporte;

@Repository("gestionConsultaService")
@Transactional
public class GestionConsultaServiceImpl implements GestionConsultaService {

	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	FichaProveidoDao fichaProveidoDao;
	
	@Autowired
	FichaDocumentoDao fichaDocumentoDao;
	
	@Autowired
	DerivaDao derivaDao;
	
	@Autowired
	EnviadoExternoDao enviadoExternoDao;
	
	@Autowired
	CanalAsociadoEnvioDao canalAsociadoEnvioDao;
	
	@Autowired
	AdjuntoDao adjuntoDao;
	
    @Autowired
    GrupoCentroCostoDao grupoCentroCostoDao;
    
    @Autowired
    DerivaService derivaService;
    
    @Autowired
    EnvioMultipleService envioMultipleService;
    
    @Autowired
    GestionEnvioService gestionEnvioService;
	
	
	@Override
	public Object getConsultaEnvios(Usuario datosSession, FichaConsultas fc, Optional<String> pag, Optional<String> pagLength) throws Exception {
		List<InputSelectUtil> listDependencias = new ArrayList<>();
		if ( fc.getGrupoDestinoId() != null && !fc.getGrupoDestinoId().isEmpty() && fc.getDependenciaDestinoId().isEmpty() ) {
    		listDependencias = grupoCentroCostoDao.getCentrosCostoByGrupo(fc.getGrupoDestinoId());
        }
		return fichaDocumentoDao.getFichaDocumentoEstafeta(fc, pag, pagLength, listDependencias);			
	}
	
	private boolean contieneSumillaMP(String sumillaProveido, String sumilla) {
		Boolean filtro = false;
		if( sumilla != null && !sumilla.isEmpty() ) {
			filtro = Util.contienePalabras(sumillaProveido, sumilla);
		}
		return filtro;
	}

	private Boolean contieneNumeroMP(String numeroProveido, String numero) {
		Boolean filtro = false;
		if( numero != null ) {
			filtro = numeroProveido.equals(numero);
		}
		return filtro;
	}

	private Boolean filtrarPorRemitenteODirigido(FichaConsultas fc, List<Integer> centroCostoDestinosIds) {
		Boolean fitro = false;
		if ( fc.getRemitidoPor() != null && !fc.getRemitidoPor().isEmpty() ) {
			fitro = true;
		}
		if ( centroCostoDestinosIds != null && !centroCostoDestinosIds.isEmpty() ) {
			fitro = true;
		}
		if ( fc.getEmpleadoDestinoId() != null && StringUtils.isNumeric(fc.getEmpleadoDestinoId()) ) {
			fitro = true;
		}
		return fitro;
	}

	private boolean contieneEmpleadoDirigido(String empleadoId, List<EnviadoUtil> enviados) {
		boolean encontrado = false;
		if ( empleadoId == null ) {
			return encontrado;
		} 
		if ( empleadoId.isEmpty() ) {
			return encontrado;
		}
		if (Optional.fromNullable(enviados).isPresent()) {
			if (enviados.size() > 0) {
				for ( EnviadoUtil e : enviados ) {
					if ( empleadoId.equals(e.getEmpleadoId().toString()) ) {
						encontrado = true;
					}
				}
			}
		}
		return encontrado;
	}

	private boolean contiene(String empleadoId, List<Integer> centroCostoDestinosIds, List<EnviadoUtil> enviados) {
		boolean encontrado = false;		
		if ( centroCostoDestinosIds == null && empleadoId == null ) {
			return encontrado;
		}
		if (Optional.fromNullable(enviados).isPresent()) {
			if (enviados.size() > 0) {
				for ( EnviadoUtil e : enviados ) {
					if ( centroCostoDestinosIds != null && centroCostoDestinosIds.contains( Integer.parseInt(e.getCentroCostoId()) ) ) {
						encontrado = true;
						break;
					}
					if ( empleadoId != null && !empleadoId.isEmpty() && empleadoId.equals(e.getEmpleadoId().toString()) ) {
						encontrado = true;
						break;
					}
				}
			}
		}
		return encontrado;
	}

	private boolean contienePalabras(String loQueQuieroBuscar, String cadenaDondeBuscar) {
		if ( cadenaDondeBuscar == null ) {
			return false;
		}
		Boolean busFp = false;
		String[] palabras = loQueQuieroBuscar.split("\\s+");
		for (String palabra : palabras) {
		    if (cadenaDondeBuscar.contains(palabra)) {
		        busFp = true;
		    }
		}
		if ( !busFp ) {
			palabras = loQueQuieroBuscar.split("\\W+");
			for (String palabra : palabras) {
			    if (cadenaDondeBuscar.contains(palabra)) {
			        busFp = true;
			    }
			}
		}
		return busFp;
	}

	private boolean contieneRemitenteMesaPartes(String remitidoPor, String remitente) {
		if ( remitidoPor != null && !remitidoPor.isEmpty() ) {
			return contienePalabras(remitidoPor, remitente);	
		}
		return false;
	}


	private boolean contieneSumillaMesaPartes(String sumilla, String sumillaFichaProveido) {
		if ( sumilla != null && !sumilla.isEmpty() ) {
			return contienePalabras(sumilla, sumillaFichaProveido);	
		}
		return false;
	}


	private boolean contieneNumeroMesaPartes(String numeroMp, Integer numero) {		
		return numeroMp != null && numeroMp.equals(numero.toString());
	}


	private String getRemitidoPor(FichaUtil doc) throws Exception {
		String remitente = "";
		switch (doc.getTipoRegistro().getId()) {
			case 1:
				remitente = doc.getCentroCosto();
				break;
			case 2:
				EnviadoExterno ee = enviadoExternoDao.findEnviadoPor(String.valueOf(doc.getId()));
				if (Optional.fromNullable(ee).isPresent()) {
					switch (ee.getTipoEnviado().getId()) {
					case 24:
						if (Optional.fromNullable(ee.getEmpleado()).isPresent()) {
							remitente = ee.getCentroCosto().getDescripcion();
						}
						break;
					case 25:
	
						if (!ee.getOrigen().equals("")) {
							remitente = ee.getOrigen();
						} else {
							if (!ee.getNombres().equals("")) {
								remitente = ee.getNombres() + " ";
							}
							if (!ee.getApellidos().equals("")) {
								remitente = remitente + ee.getApellidos();
							}
						}
						break;
					default:
						break;
					}
				}
				break;
		}
		return remitente;
	}


	private FichaProveido getFichaProveido(Integer id) throws Exception {
		List<FichaProveido> list = (List<FichaProveido>) fichaProveidoDao.findByIdRU(Optional.of( id.toString() ), Optional.of( "" ));
		if ( list != null && list.size() > 0 ) {
			return list.get(0);
		} else {
			return null;
		}
	}


	public String getEnviados(List<EnviadoUtil> enviados) throws Exception {
		String enviadoTotal = "";
		if (Optional.fromNullable(enviados).isPresent()) {
			if (enviados.size() > 0) {
				for ( EnviadoUtil e : enviados ) {
					String enviadoA = "";
					GrupoCentroCosto gcc = grupoCentroCostoDao.getCentroCostoActual(e.getCentroCostoId());
					if ( Constantes.GRUPO_CENTRO_COSTO_DESPACHOS_CONGRESALES.equals(gcc.getGrupo().getId().toString()) ) {
						enviadoA = Constantes.CONGRESISTA + e.getEmpleadoDescripcion() + "@@";
					} else {
						if(Constantes.COMISIONES.get(gcc.getGrupo().getId().toString())!=null){
							enviadoA = e.getCentroCosto().trim() + "@@";
						} else if(Constantes.CONGRESISTAS_PARLAMENTO_ANDINO.get(gcc.getGrupo().getId().toString())!=null){
							enviadoA =  gcc.getGrupo().getDescripcion().trim() + " - "+e.getCentroCosto().trim() + "@@";
					    }					
						else{
							enviadoA = e.getCentroCosto().trim() + "@@";	
						}
					}
					enviadoTotal += enviadoA;
				}
			}
		}
		return enviadoTotal;
	}
	
	@Override
	public Object getReporteConsultas(HttpServletRequest request, Usuario usuario, List<FichaPedientes> detReporte, Reporte cabReporte) throws Exception {
		File reportsDir = new File(request.getSession().getServletContext().getRealPath("/assets/"));
		if (!reportsDir.exists()) {
			try {
				throw new FileNotFoundException(String.valueOf(reportsDir));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tituloReporte", Constantes.TITULO_REPORTE_CONSULTA);
		map.put("centroCostoDes", usuario.getEmpleado().getCentroCosto().getDescripcion() );
		map.put("usuario", usuario.getNombreUsuario());
		map.put("totalRegistros", 0);
		map.put("totalDocFisicos", 0);
		map.put("registradoPor", usuario.getNombres().trim() + " " + usuario.getApellidos().trim() );

		List registros = new ArrayList<>();
		Integer totalRegistros = 0;
		Integer totalDocFisicos = 0;		
		for ( FichaPedientes f : detReporte ) {
			totalRegistros++;
			totalDocFisicos = totalDocFisicos + f.getNumDerivados();
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("numero", f.getNumeroMp());
			data.put("regUnico", f.getId());
			data.put("fechaCrea", f.getFechaCrea());
			data.put("documento", f.getNumeroDoc());
			data.put("tipoDoc", f.getTipoDoc());
			data.put("numeroFisicos", f.getNumDerivados());
			data.put("remitidoPor", f.getRemitidoPor());
			data.put("enviadoA", f.getEnviadoADes());
			data.put("sumilla", f.getAsuntoMp());
			data.put("registrado", f.getRegistradoPorDes());
			data.put("fechaReporte", f.getFechaEnvioReporte());
			registros.add(data);
		}
		map.put("pendientes", registros);
		map.put("totalRegistros", totalRegistros);
		map.put("totalDocFisicos", totalDocFisicos);
		map.put("fechaCreaReporte", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss aa").format(new Date()));
		map.put(JRParameter.REPORT_LOCALE, Locale.US);
		map.put(JRParameter.REPORT_FILE_RESOLVER, new SimpleFileResolver(reportsDir));

		byte[] reportePdf = null;		

		String ruta= request.getSession().getServletContext().getRealPath("/assets/rptConsultasMesaPartes.jrxml");
		
		JasperReport reporte= JasperCompileManager.compileReport(ruta);
		JasperPrint print = JasperFillManager.fillReport(reporte, map, new JREmptyDataSource());
		reportePdf = JasperExportManager.exportReportToPdf(print);
		
		return reportePdf;
		
	}
	
}
