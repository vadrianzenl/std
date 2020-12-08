package pe.gob.congreso.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.util.StringUtil;
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
import pe.gob.congreso.dao.CanalAsociadoEnvioDao;
import pe.gob.congreso.dao.DerivaDao;
import pe.gob.congreso.dao.DerivaMPDao;
import pe.gob.congreso.dao.DetGestionEnvioDao;
import pe.gob.congreso.dao.EnviadoExternoDao;
import pe.gob.congreso.dao.EtapaGestionEnvioDao;
import pe.gob.congreso.dao.FichaDocumentoDao;
import pe.gob.congreso.dao.FichaProveidoDao;
import pe.gob.congreso.dao.GestionEnvioDao;
import pe.gob.congreso.dao.GrupoCentroCostoDao;
import pe.gob.congreso.dao.TipoDocumentoDao;
import pe.gob.congreso.model.AnioLegislativo;
import pe.gob.congreso.model.EnviadoExterno;
import pe.gob.congreso.model.EnvioMultiple;
import pe.gob.congreso.model.FichaDocumento;
import pe.gob.congreso.model.FichaProveido;
import pe.gob.congreso.model.GrupoCentroCosto;
import pe.gob.congreso.model.MpCanalAsociadoEnvio;
import pe.gob.congreso.model.MpDetGestionEnvio;
import pe.gob.congreso.model.MpEtapaGestionEnvio;
import pe.gob.congreso.model.MpGestionEnvio;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.EnviadoUtil;
import pe.gob.congreso.model.util.Util;
import pe.gob.congreso.service.ActividadService;
import pe.gob.congreso.service.AnioLegislativoService;
import pe.gob.congreso.service.EnvioMultipleService;
import pe.gob.congreso.service.GestionEnvioService;
import pe.gob.congreso.util.Constantes;
import pe.gob.congreso.util.Contenedor;
import pe.gob.congreso.util.DateHelper;
import pe.gob.congreso.util.FichaPedientes;
import pe.gob.congreso.util.Item;
import pe.gob.congreso.util.ReportHelper;
import pe.gob.congreso.util.ReportType;
import pe.gob.congreso.util.Reporte;

@Repository("gestionEnvioService")
@Transactional
public class GestionEnvioServiceImpl implements GestionEnvioService {
	 
	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	CanalAsociadoEnvioDao canalAsociadoEnvioDao;
	
	@Autowired
	GestionEnvioDao gestionEnvioDao;
	
	@Autowired
	EtapaGestionEnvioDao etapaGestionEnvioDao;
	
	@Autowired
	DetGestionEnvioDao detGestionEnvioDao;
	
    @Autowired
    ActividadService actividadService;
    
    @Autowired
    FichaDocumentoDao fichaDocumentoDao;
    
    @Autowired
    FichaProveidoDao fichaProveidoDao;
    
    @Autowired
    TipoDocumentoDao tipoDocumentoDao;
    
	@Autowired
	DerivaMPDao derivaMPDao;
	
	@Autowired
	DerivaDao derivaDao;
	
    @Autowired
    GrupoCentroCostoDao grupoCentroCostoDao;
    
    @Autowired
    EnvioMultipleService envioMultipleService;
    
    @Autowired
    EnviadoExternoDao enviadoExternoDao;
    
    @Autowired
    AnioLegislativoService anioLegislativoService;
    
    
	private final ReportHelper reportHelper = new ReportHelper();
    
	
	@Override
	public MpCanalAsociadoEnvio getCanalAsociado(String idGrupo, String idDependencia, String idEmpleado)
			throws Exception {
		List<MpCanalAsociadoEnvio> canales = canalAsociadoEnvioDao.getCanalAsociado(idGrupo, idDependencia, idEmpleado);
		if ( !canales.isEmpty() ) {
			return (MpCanalAsociadoEnvio) canales.get(0);
		} else {
			return null;
		}
		
	}

	
	@Override
	public Object createGestionEnvio(Usuario usuario, MpGestionEnvio reporte, String operacion) throws Exception {
		actividadService.create(usuario, reporte, operacion);
		if ( reporte.getUsuarioCrea() != null && !reporte.getUsuarioCrea().isEmpty() ) {
			reporte.setUsuarioCrea(usuario.getNombreUsuario());
			reporte.setFechaCrea(new Date());
		} else if ( reporte.getUsuarioModifica() != null && !reporte.getUsuarioModifica().isEmpty() ) {
			reporte.setUsuarioModifica(usuario.getNombreUsuario());
			reporte.setFechaModifica(new Date());
		}
		return gestionEnvioDao.create(reporte);
	}
	
	
	@Override
	public Object createEtapaGestionEnvio(Usuario usuario, MpEtapaGestionEnvio etapa, String operacion)
			throws Exception {
		actividadService.create(usuario, etapa, operacion);
		if ( etapa.getUsuarioCrea() != null && !etapa.getUsuarioCrea().isEmpty() ) {
			etapa.setUsuarioCrea(usuario.getNombreUsuario());
			etapa.setFechaCrea(new Date());
		} else if ( etapa.getUsuarioModifica() != null && !etapa.getUsuarioModifica().isEmpty() ) {
			etapa.setUsuarioModifica(usuario.getNombreUsuario());
			etapa.setFechaModifica(new Date());
		}
		return etapaGestionEnvioDao.create(etapa);
	}


	@Override
	public Object createDetalleGestionEnvio(Usuario usuario, MpDetGestionEnvio cargo, String operacion)
			throws Exception {
		actividadService.create(usuario, cargo, operacion);
		if (cargo.getUsuarioCrea() != null && !cargo.getUsuarioCrea().isEmpty() ) {
			cargo.setUsuarioCrea(usuario.getNombreUsuario());
			cargo.setFechaCrea(new Date());
		} else if ( cargo.getUsuarioModifica() != null && !cargo.getUsuarioModifica().isEmpty() ) {
			cargo.setUsuarioModifica(usuario.getNombreUsuario());
			cargo.setFechaModifica(new Date());
		}
		return detGestionEnvioDao.create(cargo);
	}


	@Override
	public Object updateEstafeta(Usuario usuario, FichaDocumento fd, String operacion) throws Exception {
		actividadService.create(usuario, fd, operacion);
		fd.setUsuarioModifica(usuario.getNombreUsuario());
		fd.setFechaModifica(new Date());
		return fichaDocumentoDao.updateEstafeta(fd);
		
	}

	/**
	 * Retorna la lista de fichas de documentos recibidos por mesa de partes para el envio a estafeta o casillero o como envio multiple.
	 */
	@Override
	public Object getEnviosPendientes(Usuario usuario, String indEstafeta, String pag, String pagLength, String centroCostoId, String reporteId) throws Exception {
		List<FichaPedientes> listFicha = new ArrayList<>();
		StringBuffer pendientes = new StringBuffer();
		pendientes.append(Constantes.FP_PENDIENTE_DE_REPORTE).append(",").append(Constantes.FP_CON_REPORTE).append(",").append(Constantes.FP_CON_REPORTE_MULTIPLE_ESTAFETA).append(",").append(Constantes.FP_CON_REPORTE_MULTIPLE_CASILLERO);
		StringBuffer estados = new StringBuffer();
		estados.append(Constantes.ESTADO_ENVIADO).append(",").append(Constantes.ESTADO_LEIDO);
		AnioLegislativo anioActual = anioLegislativoService.getAnioActual();
		Map parametros = new HashMap<String, Object>();
		parametros.put("canalEnvio", Integer.parseInt(indEstafeta));
		parametros.put("pendientes", pendientes.toString());
		parametros.put("habilitado", Constantes.ESTADO_ACTIVO);
		parametros.put("ppini", anioActual.getCodigo().intValue() - 1);
		parametros.put("ppfin", anioActual.getCodigo().intValue() + 1);
		parametros.put("estados", estados.toString());
		parametros.put("proveidoId", Constantes.ID_PROVEIDO_MESA_DE_PARTES);
		parametros.put("reporte", Constantes.FP_PENDIENTE_DE_REPORTE );
		parametros.put("usuario", usuario.getNombreUsuario().trim() );
		if ( centroCostoId != null && !centroCostoId.isEmpty() ) {
			parametros.put("centroCostoId", centroCostoId);
		}
		if ( reporteId != null && !reporteId.isEmpty() ) {
			parametros.put("reporteId", reporteId);
			parametros.put("reporte", Constantes.FP_CON_REPORTE );
		}
		listFicha = fichaDocumentoDao.getListEnviosPendientes(parametros);
		return listFicha;
	}


	private FichaProveido buscarFichaProveido(List fichaProveido, Optional<String> documentoId) {
		int i = 0;
		while ( i < fichaProveido.size() ) {
			FichaProveido objFichaProveido = (FichaProveido) fichaProveido.get(i);
			if ( objFichaProveido.getFichaDocumentoId().equals(Integer.parseInt(documentoId.get())) ) {
				return objFichaProveido;
			}
			i++;
		}
		return null;
	}


	private void corresponderCanalEnvioFicha(List<MpCanalAsociadoEnvio> centrosCostosAsociados, FichaPedientes f, Map<String, String> mapEnviadoA, List<FichaPedientes> listFicha) throws Exception {
		if ( f.getIndEnvioMultiple().booleanValue() ) {
			Boolean encontroEM = buscarEnviosMultiples(f, mapEnviadoA, centrosCostosAsociados,  Constantes.REPORTE_PENDIENTE_ENVIO_MULTIPLE);
			if ( encontroEM.booleanValue() && f.getNumDerivados().intValue() > 0 ) {
				f.setCentroCostoDirigidosId(mapEnviadoA);
				listFicha.add(f);
			}
		} else {
			listFicha.add(f);
		}
	}


	public Boolean buscarEnviosMultiples(FichaPedientes f, Map<String, String> mapEnviadoA, List<MpCanalAsociadoEnvio> centrosCostosAsociados, Boolean estado) throws Exception {
		Boolean tieneReportePendiente = false;
		Optional<String> proveidoId = Optional.of( Constantes.ID_PROVEIDO_MESA_DE_PARTES.toString() );
		Optional<String> documentoId = Optional.of( f.getId().toString() );
		List<EnvioMultiple> listEnvioMultiple = envioMultipleService.getByIdFk(documentoId, proveidoId);
		for ( EnvioMultiple em : listEnvioMultiple) {
			MpCanalAsociadoEnvio canalAsociado = buscaCentroCosto(centrosCostosAsociados, em.getCentroCostoId().trim());
			if ( canalAsociado != null ) {
				if ( estado.equals(em.getReporte()) ) {
					tieneReportePendiente = true;
				} else {
					Integer totalDirigidosCanalIndEstafeta = f.getTotalDirigidosCanalIndEstafeta().intValue() - 1;
					f.setTotalDirigidosCanalIndEstafeta(totalDirigidosCanalIndEstafeta);
					mapEnviadoA.remove(em.getCentroCostoId().trim());
				}
			}
		}
		if ( tieneReportePendiente ) {
			f.setNumDerivados(f.getTotalDirigidosCanalIndEstafeta());
			String enviadoA = "";
			for (String value : mapEnviadoA.values()) {				
				enviadoA +=  value;
			}
			f.setEnviadoADes(enviadoA);
		}
		return tieneReportePendiente;
	}
	
	
	public String buscarEnviadoA(FichaDocumento fd, List<MpCanalAsociadoEnvio> centrosCostosAsociados, FichaPedientes f, Map<String, String> mapEnviadoA) throws Exception {		
		String enviadoTotal = "";
		List<EnviadoUtil> enviados = derivaDao.findEnviados(fd.getId(), Constantes.ES_MESA_DE_PARTES);
		if (Optional.fromNullable(enviados).isPresent()) {
			if (enviados.size() > 0) {
				for ( EnviadoUtil e : enviados ) {
					Integer totalDirigidos = f.getTotalDirigidos().intValue() + 1;
					f.setTotalDirigidos(totalDirigidos);
					MpCanalAsociadoEnvio canalAsociado = buscaCentroCosto(centrosCostosAsociados, e.getCentroCostoId().trim());
					if ( canalAsociado != null ) {
						GrupoCentroCosto gcc = grupoCentroCostoDao.getCentroCostoActual(e.getCentroCostoId().trim());
						String enviadoA = "";
						if ( Constantes.GRUPO_CENTRO_COSTO_DESPACHOS_CONGRESALES.equals(gcc.getGrupo().getId().toString()) ) {
							enviadoA = Constantes.CONGRESISTA+e.getEmpleadoDescripcion() + "@@";
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
						mapEnviadoA.put(e.getCentroCostoId().trim(), enviadoA);
						enviadoTotal += enviadoA;
						Integer totalDirigidosCanalIndEstafeta = f.getTotalDirigidosCanalIndEstafeta().intValue() + 1;
						f.setTotalDirigidosCanalIndEstafeta(totalDirigidosCanalIndEstafeta);
					}
				}
			}
		}			
		return enviadoTotal;
	}


	public String buscarRemitido(FichaDocumento doc) throws Exception {
		String remitente = "";
		switch (doc.getTipoRegistro().getId()) {
			case 1:
				remitente = doc.getCentroCosto().getDescripcion();
				break;
			case 2:
				EnviadoExterno ee = enviadoExternoDao.findEnviadoPor(String.valueOf(doc.getId()));
				//EnviadoExterno ee = null;
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
			default:
				break;
			}
		return remitente;
	}


	private MpCanalAsociadoEnvio buscaCentroCosto(List<MpCanalAsociadoEnvio> centrosCostosAsociados, String centroCostoId) {
		MpCanalAsociadoEnvio result = null;
		for (MpCanalAsociadoEnvio c : centrosCostosAsociados ) {
			if ( c.getDependenciaId().equals(Integer.parseInt(centroCostoId.trim())) ) {
				result = c;
				break;
			}
		}
		return result;
	}


	@Override
	public Object getFichaProveidoByIdRU(Usuario usuario, String id) throws Exception {		
		Optional<String> documentoId =  Optional.of( id );
		Optional<String> proveidoId = Optional.of( Constantes.ID_PROVEIDO_MESA_DE_PARTES.toString() );
		List<FichaProveido> lfp = (List<FichaProveido>) fichaProveidoDao.findByIdRU(documentoId, proveidoId);
		if ( lfp != null && lfp.size() > 0) {
			return lfp.get(0);	
		}
		return null;
	}


	@Override
	public Object getReporteEnvio(HttpServletRequest request, Usuario usuario, List<FichaPedientes> detReporte, Reporte cabReporte) throws Exception {
		File reportsDir = new File(request.getSession().getServletContext().getRealPath("/assets/"));
		if (!reportsDir.exists()) {
			try {
				throw new FileNotFoundException(String.valueOf(reportsDir));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		String[] titulo = cabReporte.getTituloReporte().split("\\|");
		if (Constantes.ENVIO_PARA_ESTAFETA.toString().equals(titulo[1]) ) {
			map.put("tituloReporte", titulo[0].concat(Constantes.GRUPO_REPORTES.get(Constantes.ENVIO_PARA_ESTAFETA.toString())));
		} else {
			String enviadoA = detReporte != null && !detReporte.isEmpty() ? detReporte.get(0).getEnviadoADes().replaceAll("@@", Constantes.VACIO) : Constantes.VACIO;
			map.put("tituloReporte", titulo[0].concat(enviadoA));
		}
		map.put("centroCostoDes", cabReporte.getCentroCostoDes());
		map.put("usuario", usuario.getNombreUsuario());
		map.put("totalRegistros", cabReporte.getTotalRegistros());
		map.put("totalDocFisicos", cabReporte.getTotalDocFisicos());
		map.put("registradoPor", cabReporte.getRegistradoPor() );

		List registros = new ArrayList<>();
		Integer totalRegistros = 0;
		Integer totalDocFisicos = 0;		
		for ( FichaPedientes f : detReporte ) {
			totalRegistros++;
			totalDocFisicos = totalDocFisicos + f.getNumDerivados();
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("numero", f.getNumeroMp());
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			String dateInString = f.getFechaCrea();
	        Map mapDate = Util.deStringADate( dateInString, formatter);
			data.put("regUnico", f.getId());
			data.put("fechaCrea", !mapDate.containsKey(Constantes.MENSAJE_DE_ERROR) ? Util.deDateAString((Date) mapDate.get(Constantes.OBTENER_FECHA_DATE), Constantes.FORMATO_FECHA_PARA_REPORTE_MP) : Constantes.VACIO);
			data.put("documento", f.getNumeroDoc());
			data.put("tipoDoc", f.getTipoDoc());
			data.put("numeroFisicos", f.getNumDerivados());
						
			String remitidoPor = Constantes.VACIO;
			if( f.getRemitidoPor().contains("@@") ) {
				String[] list = f.getRemitidoPor().split("@@");
				for(String e : list ) {
					String c = e.endsWith(",") ? e.substring(0, remitidoPor.length()-1): e;
					if ( Constantes.VACIO.equals(c.trim()) ) {
						remitidoPor = Constantes.VACIO;
					} else {
						remitidoPor += Constantes.GUION.concat(Constantes.ESPACIO_EN_BLANCO).concat(c).concat(Constantes.SEPARADOR_REPORTE);	
					}					
				}
			}
			String enviadoA = Constantes.VACIO;
			if( f.getEnviadoADes().contains("@@") ) {
				String[] list = f.getEnviadoADes().split("@@");
				String[] listCode = f.getEnviadoA().split("@@");
				for(int i = 0; i < list.length; i++ ) {
					String e = list[i];
					String cc = listCode[i];
					String c = e.endsWith(",") ? e.substring(0, remitidoPor.length()-1): e;
					enviadoA += Constantes.VINIETA_PUNTO.concat(Constantes.ESPACIO_EN_BLANCO);
					GrupoCentroCosto gcc = grupoCentroCostoDao.getCentroCostoActual(cc);
					if ( Constantes.GRUPO_CENTRO_COSTO_DESPACHOS_CONGRESALES.equals(gcc.getGrupo().getId().toString()) ) {
						enviadoA += Constantes.CONGRESISTA.concat(Constantes.ESPACIO_EN_BLANCO).concat(c).concat(Constantes.SEPARADOR_REPORTE);
					} else {
						if( Constantes.COMISIONES.get( gcc.getGrupo().getId().toString() ) != null){
							enviadoA += c.concat(Constantes.SEPARADOR_REPORTE);
						} else if( Constantes.CONGRESISTAS_PARLAMENTO_ANDINO.get( gcc.getGrupo().getId().toString() ) != null ){
							enviadoA +=  gcc.getGrupo().getDescripcion().trim()
									.concat(Constantes.ESPACIO_EN_BLANCO)
									.concat(Constantes.GUION)
									.concat(Constantes.ESPACIO_EN_BLANCO)
									.concat(c).concat(Constantes.SEPARADOR_REPORTE);
					    } else{
							enviadoA += c.concat(Constantes.SEPARADOR_REPORTE);
						}
					}
				}
			}
			data.put("remitidoPor", remitidoPor);
			data.put("enviadoA", enviadoA);
			data.put("sumilla", f.getAsuntoMp());
			registros.add(data);			
		}
		map.put("pendientes", registros);
		map.put("totalRegistros", totalRegistros);
		map.put("totalDocFisicos", totalDocFisicos);
		map.put("fechaCreaReporte", cabReporte.getFechaGenera());
		map.put(JRParameter.REPORT_LOCALE, Locale.US);
		map.put(JRParameter.REPORT_FILE_RESOLVER, new SimpleFileResolver(reportsDir));

		byte[] reportePdf = null;		

		String ruta= Constantes.VACIO;
		if (Constantes.ENVIO_PARA_ESTAFETA.toString().equals(titulo[1]) ) {
			ruta = request.getSession().getServletContext().getRealPath(Constantes.UBICACION_Y_NOMBRE_REPORTE_PARA_ESTAFETA);
		} else {
			ruta = request.getSession().getServletContext().getRealPath(Constantes.UBICACION_Y_NOMBRE_REPORTE_PARA_CASILLERO);
		}
		JasperReport reporte= JasperCompileManager.compileReport(ruta);
		JasperPrint print = JasperFillManager.fillReport(reporte, map, new JREmptyDataSource());
		reportePdf = JasperExportManager.exportReportToPdf(print);
		
		return reportePdf;
		
	}


	@Override
	public Object generarReporteEnvio(Object datosSession, Contenedor c, String operacion, String tipoReporte, Integer grupoEnvio, String centroCostoId)
			throws Exception {
		Usuario usuario = (Usuario) datosSession;
		String usuarioCrea = usuario.getNombreUsuario();
		HashSet<FichaPedientes> listFichasEnReporte = new HashSet();
		List<FichaPedientes> listFichasPendientes = (List<FichaPedientes>) getEnviosPendientes(usuario, c.getFicha().getIndEstafeta(), Constantes.VACIO, Constantes.VACIO, Constantes.VACIO, Constantes.VACIO);
		Item[] seleccionadosReporte = c.getFicha().getItem();
		Integer totalFisicos = 0;
		
        MpGestionEnvio reporte = null;
		if ( seleccionadosReporte != null && seleccionadosReporte.length > 0  ) {
        	int totalCantidadFisicos = 0;
        	for (Item s : seleccionadosReporte ) {
				for (FichaPedientes p : listFichasPendientes ) {
					if ( s.getId().toString().equals(p.getId().toString()) ) {
							totalCantidadFisicos += p.getNumDerivados();
							listFichasEnReporte.add(p);
					}
				}
			}
    		reporte = new MpGestionEnvio();
			Date fechaActual = new Date();
			reporte.setFechaGeneracion(fechaActual);
			reporte.setUsuarioGeneracion(usuarioCrea);
			reporte.setSubsanado(Constantes.NO_SUBSANADO);
			reporte.setCantidadRegistros(seleccionadosReporte.length);
			reporte.setCantidadFisicos(totalCantidadFisicos);
			reporte.setTipoReporte(tipoReporte);
			reporte.setGrupoEnvio(grupoEnvio);
			reporte.setEstado(Constantes.ESTADO_CARGO_ENVIADO);
			reporte.setHabilitado(Constantes.HABILITADO);
			reporte.setObservaciones(Constantes.REPORTE_CREADO);
			reporte.setUsuarioCrea(usuarioCrea);
			reporte.setFechaCrea(fechaActual);
			reporte = (MpGestionEnvio) createGestionEnvio(usuario, reporte, operacion);
    		for (FichaPedientes p : listFichasEnReporte ) {
    			MpEtapaGestionEnvio etapa = new MpEtapaGestionEnvio();
				etapa.setGestionEnvioId(reporte.getId());
				etapa.setFichaDocumentoId(p.getId());
				etapa.setUsuarioCrea(usuarioCrea);
				etapa.setFechaCrea(fechaActual);
				etapa.setCodigo(Constantes.ETAPA_EN_REPORTE);
				etapa.setMotivo(Constantes.ETAPA_CREADA);
				etapa.setEstado(Constantes.ESTADO_ACTIVO);
				etapa.setHabilitado(Constantes.HABILITADO);
    			etapa = (MpEtapaGestionEnvio) createEtapaGestionEnvio(usuario, etapa, operacion);		        				
				Optional<String> documentoId = Optional.of( p.getId().toString() );
				Optional<String> proveidoId = Optional.of( Constantes.ID_PROVEIDO_MESA_DE_PARTES.toString() );
				List<FichaProveido> lfp = (List<FichaProveido>) fichaProveidoDao.findByIdRU(documentoId, proveidoId);
				FichaProveido fp = lfp.size() > 0 ? lfp.get(0) : null;
				if ( fp != null && fp.getEstado() != null ) {
					if (  !p.getIndEnvioMultiple().booleanValue() ) {
						List<EnvioMultiple> listEnvioMultiple = envioMultipleService.getByIdFk(documentoId, proveidoId);
						if ( Constantes.ENVIO_PARA_ESTAFETA.toString().equals(grupoEnvio.toString()) ) {
							fp.setEstado(Constantes.FP_CON_REPORTE);
							for ( EnvioMultiple envidoIndividual : listEnvioMultiple) {
								if (  Constantes.ENVIO_PARA_ESTAFETA.toString().equals( envidoIndividual.getCanalEnvio().trim() ) && envidoIndividual.getTipoReporte() == null && envidoIndividual.getGestionEnvioId() == null ) {
									totalFisicos++;
									envidoIndividual.setReporte(Constantes.REPORTE_REALIZADO_ENVIO_MULTIPLE);
									envidoIndividual.setTipoReporte(tipoReporte);
									envidoIndividual.setGestionEnvioId(reporte.getId());
									envidoIndividual.setUsuarioModifica(usuario.getNombreUsuario());
									envidoIndividual.setFechaModifica(fechaActual);
									envioMultipleService.createEnvioMultiple(usuario, envidoIndividual, Constantes.OPERACION_ACTUALIZAR);
								}
							}
						} else if ( Constantes.ENVIO_PARA_CASILLERO.toString().equals(grupoEnvio.toString()) ) {
							if(listEnvioMultiple.size()>1){
								fp.setEstado(Constantes.FP_CON_REPORTE_MULTIPLE_CASILLERO);
							}else{
								fp.setEstado(Constantes.FP_CON_REPORTE);
							}
							for ( EnvioMultiple envidoIndividual : listEnvioMultiple) { 
								if (  envidoIndividual.getCentroCostoId().trim().equals(centroCostoId.trim()) && envidoIndividual.getTipoReporte() == null && envidoIndividual.getGestionEnvioId() == null ) {
									totalFisicos++;
									envidoIndividual.setReporte(Constantes.REPORTE_REALIZADO_ENVIO_MULTIPLE);
									envidoIndividual.setTipoReporte(tipoReporte);
									envidoIndividual.setGestionEnvioId(reporte.getId());
									envidoIndividual.setUsuarioModifica(usuario.getNombreUsuario());
									envidoIndividual.setFechaModifica(fechaActual);
									envioMultipleService.createEnvioMultiple(usuario, envidoIndividual, Constantes.OPERACION_ACTUALIZAR);
								}
							}
						}
    				} else {
    					List<EnvioMultiple> listEnvioMultiple = envioMultipleService.getByIdFk(documentoId, proveidoId);
    					if ( Constantes.ENVIO_PARA_ESTAFETA.toString().equals(grupoEnvio.toString()) ) {
							fp.setEstado(Constantes.FP_CON_REPORTE_MULTIPLE_ESTAFETA);
							for ( EnvioMultiple em : listEnvioMultiple) {
								if (  Constantes.ENVIO_PARA_ESTAFETA.toString().equals( em.getCanalEnvio().trim() ) && em.getTipoReporte() == null && em.getGestionEnvioId() == null ) {
									totalFisicos++;
									em.setReporte(Constantes.REPORTE_REALIZADO_ENVIO_MULTIPLE);
									em.setTipoReporte(tipoReporte);
									em.setGestionEnvioId(reporte.getId());
									em.setUsuarioModifica(usuario.getNombreUsuario());
									em.setFechaModifica(fechaActual);
									envioMultipleService.createEnvioMultiple(usuario, em, operacion);
								}
							}
						} else if ( Constantes.ENVIO_PARA_CASILLERO.toString().equals(grupoEnvio.toString()) ) {
							fp.setEstado(Constantes.FP_CON_REPORTE_MULTIPLE_CASILLERO);
							for ( EnvioMultiple em : listEnvioMultiple) {
								if (  em.getCentroCostoId().trim().equals(centroCostoId.trim()) && em.getTipoReporte() == null && em.getGestionEnvioId() == null ) {
									totalFisicos++;
									em.setReporte(Constantes.REPORTE_REALIZADO_ENVIO_MULTIPLE);
									em.setTipoReporte(tipoReporte);
									em.setGestionEnvioId(reporte.getId());
									em.setUsuarioModifica(usuario.getNombreUsuario());
									em.setFechaModifica(fechaActual);
									envioMultipleService.createEnvioMultiple(usuario, em, operacion);
								}
							}
						}
						
					}
					fp.setUsuarioModifica(usuario.getNombreUsuario() );
					fp.setFechaModifica(fechaActual);
					fichaProveidoDao.create(fp);
				}
        	}
    		reporte.setCantidadFisicos(totalFisicos);
    		reporte = (MpGestionEnvio) createGestionEnvio(usuario, reporte, operacion);
        }
        return reporte;
	}


	@Override
	public List<FichaProveido> getFichaProveidoByNumeroMP(String numero, String sumilla, String ppIni, String ppFin) throws Exception {
		return fichaProveidoDao.findByNumeroMP(Optional.of(numero), Optional.of( sumilla ), Optional.of( ppIni ), Optional.of( ppFin ));
	}
	
}
