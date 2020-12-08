package pe.gob.congreso.service.impl;


import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import pe.gob.congreso.dao.DetGestionEnvioDao;
import pe.gob.congreso.dao.EmpleadoDao;
import pe.gob.congreso.dao.EnviadoExternoDao;
import pe.gob.congreso.dao.EnvioMultipleDao;
import pe.gob.congreso.dao.EtapaGestionEnvioDao;
import pe.gob.congreso.dao.FichaProveidoDao;
import pe.gob.congreso.dao.GestionEnvioDao;
import pe.gob.congreso.dao.GrupoCentroCostoDao;
import pe.gob.congreso.model.Adjunto;
import pe.gob.congreso.model.Empleado;
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
import pe.gob.congreso.model.util.FichaUtil;
import pe.gob.congreso.model.util.Util;
import pe.gob.congreso.service.EnvioMultipleService;
import pe.gob.congreso.service.GestionCargoService;
import pe.gob.congreso.service.GestionEnvioService;
import pe.gob.congreso.util.CargoReporte;
import pe.gob.congreso.util.Constantes;
import pe.gob.congreso.util.FichaPedientes;
import pe.gob.congreso.util.Reporte;



@Repository("gestionCargoService")
@Transactional
public class GestionCargoServiceImpl implements GestionCargoService {
	
	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	GestionEnvioDao gestionEnvioDao;
	
	@Autowired
	DetGestionEnvioDao detGestionEnvioDao;
	
	@Autowired
	EtapaGestionEnvioDao etapaGestionEnvioDao;
	
	@Autowired
	EnviadoExternoDao enviadoExternoDao;
	
	@Autowired
	EmpleadoDao empleadoDao;
	
	@Autowired
	FichaProveidoDao fichaProveidoDao;
	
	@Autowired
	DerivaDao derivaDao;
	
    @Autowired
    GrupoCentroCostoDao grupoCentroCostoDao;
	
    @Autowired
    private Environment env;
    
    @Autowired
    GestionEnvioService gestionEnvioService;
    
    @Autowired
    CanalAsociadoEnvioDao canalAsociadoEnvioDao;
    
    @Autowired
    EnvioMultipleDao envioMultipleDao;
    
    @Autowired
    EnvioMultipleService envioMultipleService;
	
	@Override
	public Map<String, Object> getReportesGenerados(Usuario usuario,Optional<String> fechaIniCrea, Optional<String> fechaFinCrea,Optional<String> tipoReporte, Optional<String> pag, Optional<String> pagLength) throws Exception {
		// TODO Auto-generated method stub
		List<Reporte> lsReportesGenerados = new ArrayList<>();
		Integer totalRegistros = 0;
		Integer totalDocumentosFisicos = 0;
				
		List<MpGestionEnvio> lsMpGestionEnvio = null;
		
		Map<String, Object> result = new HashMap<String, Object>();
		result =  gestionEnvioDao.getReportesBy(fechaIniCrea, fechaFinCrea, tipoReporte, pag, pagLength);
		
		lsMpGestionEnvio = (List<MpGestionEnvio>) result.get("reportes");
		
		for(MpGestionEnvio mpGestionEnvio : lsMpGestionEnvio){
			Reporte reporte = new Reporte();
			reporte.setId(mpGestionEnvio.getId());
			reporte.setFechaGenera(mpGestionEnvio.getFechaGeneracionFormatoMP());
			totalRegistros = totalRegistros + mpGestionEnvio.getCantidadRegistros();
			reporte.setNumRegistros(mpGestionEnvio.getCantidadRegistros());
			
			reporte.setNumDocFisicos(mpGestionEnvio.getCantidadFisicos());
			totalDocumentosFisicos = totalDocumentosFisicos + mpGestionEnvio.getCantidadFisicos();
			
			// *Obtenemos los datos del usuario que genero el reporte*//
			String remitente = "";
			Empleado empleado = empleadoDao.findByNameUsuario(mpGestionEnvio.getUsuarioCrea());
			if ( empleado != null ) {
				remitente = new StringBuffer().append(empleado.getNombres() != null ? empleado.getNombres().trim() : Constantes.VACIO)
						.append(" ").append(empleado.getApellidos() != null ? empleado.getApellidos().trim() : Constantes.VACIO).
						append(" - ").append(empleado.getCentroCosto() != null ? (empleado.getCentroCosto().getDescripcion() != null ? empleado.getCentroCosto().getDescripcion().trim() : Constantes.VACIO) : Constantes.VACIO).toString();
			} else {
				log.info("getReportesGenerados - Empleado con usuario :" + mpGestionEnvio.getUsuarioCrea() + " no encontrado.");
			}
			reporte.setRegistradoPor(remitente);
			
			// *Definimos le estado del reporte*//
			
			reporte.setEstadoReporte(mpGestionEnvio.getEstado().trim());
			reporte.setDesEstadoReporte(new StringBuilder().append(Constantes.ESTADO_REPORTE.get(mpGestionEnvio.getEstado().toString().trim())).
					append(" ").append(Constantes.GRUPO_REPORTES.get(mpGestionEnvio.getTipoReporte().toString().trim())).toString());
			
			//*Definimos la fecha del registro del cargo*//
			Optional<String> id = Optional.fromNullable(mpGestionEnvio.getId().toString().trim());
			MpDetGestionEnvio detGestionEnvio = detGestionEnvioDao.getById(id);
			if(detGestionEnvio!=null){
				reporte.setIndCargo(true);
				CargoReporte cargo = new CargoReporte();
					cargo.setId(detGestionEnvio.getId());
					cargo.setNombreArchivo(detGestionEnvio.getNombreArchivo());
					cargo.setDescripcion(detGestionEnvio.getDescripcion());
					cargo.setFechaAdjunto(detGestionEnvio.getFechaAdjunto());
					cargo.setDerivado(detGestionEnvio.getDerivado());
					cargo.setEstado(detGestionEnvio.getEstado());
					cargo.setHabilitado(detGestionEnvio.getHabilitado());
					cargo.setPublico(detGestionEnvio.getPublico());
					cargo.setSubsanado(detGestionEnvio.getSubsanado());
					cargo.setUsuarioCrea(detGestionEnvio.getUsuarioCrea());
					Empleado empleadoCargo = empleadoDao.findByNameUsuario(detGestionEnvio.getUsuarioCrea());
					String remitenteCargo = "";
					remitenteCargo = new StringBuffer().append(empleadoCargo.getNombres().trim()).append(" ").append(empleadoCargo.getApellidos().trim()).toString();
					cargo.setDesUsuarioCrea(remitenteCargo);
					
					String fechaCrea = "";
			        if(detGestionEnvio.getFechaCrea()!=null){
			        	fechaCrea = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(detGestionEnvio.getFechaCrea());
			        } 
					cargo.setFechaCrea(fechaCrea);
					cargo.setUsuarioModifica(detGestionEnvio.getUsuarioModifica());
					String fechaModifica = "";
			        if(detGestionEnvio.getFechaModifica()!=null){
			        	fechaModifica = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(detGestionEnvio.getFechaModifica());
			        } 
					cargo.setFechaModifica(fechaModifica);
					reporte.setCargoReporte(cargo);
				
			}else{
				reporte.setCargoReporte(null);
				reporte.setIndCargo(false);
			}
			
			lsReportesGenerados.add(reporte);

		}
		result.put("reportes",lsReportesGenerados);
		

		return result;
	}


	@Override
	public Object createAdjunto(Usuario usuario, MpDetGestionEnvio adjunto, String operacion) throws Exception {
		// TODO Auto-generated method stub
		log.debug("createAdjunto()");
		Date fechaActual = new Date();
		adjunto.setFechaAdjunto(fechaActual);
		adjunto.setFechaCrea(fechaActual);
		
		 MpGestionEnvio gestionEnvio = new MpGestionEnvio();
	      gestionEnvio.setId(adjunto.getGestionEnvio().getId());
	      gestionEnvio.setEstado(Constantes.ESTADO_CARGO_CONFIRMADO);
	      gestionEnvio.setUsuarioModifica(usuario.getNombreUsuario());
	      gestionEnvio.setFechaModifica(new Date());
//		  MpGestionEnvio actual = gestionEnvioDao.getById(gestionEnvio.getId().toString());
//		  gestionEnvio = Util.compararGestionEnvio(actual, gestionEnvio);
	      Integer ge = gestionEnvioDao.updateEstadoMpGestionEnvio(gestionEnvio);
	     
	      List<MpEtapaGestionEnvio>listEtapaActual = (List<MpEtapaGestionEnvio>) etapaGestionEnvioDao.getListById(adjunto.getGestionEnvio().getId().toString(), "");
	      
	      for ( MpEtapaGestionEnvio etapa : listEtapaActual ) {
	    	  etapa.setCodigo(Constantes.ETAPA_CONFIRMADO);
	    	  etapa.setUsuarioModifica(usuario.getNombreUsuario());
	    	  etapa.setFechaModifica(new Date());
	    	  etapaGestionEnvioDao.create(etapa);
	      }
	      
          return  detGestionEnvioDao.create(adjunto);
	}


	@Override
	public Object retirarAdjuntoCargo(Usuario datosSession, MpDetGestionEnvio a, String operacion) throws Exception {
		a.setUsuarioModifica(datosSession.getNombreUsuario());
		a.setFechaModifica(new Date());
		MpGestionEnvio gestionEnvio = new MpGestionEnvio();
	    gestionEnvio.setId(a.getGestionEnvio().getId());
	    gestionEnvio.setEstado(Constantes.ESTADO_CARGO_ENVIADO);
	    gestionEnvio.setUsuarioModifica(datosSession.getNombreUsuario());
	    gestionEnvio.setFechaModifica(new Date());
	    Integer ge = gestionEnvioDao.updateEstadoMpGestionEnvio(gestionEnvio);
	    
	      List<MpEtapaGestionEnvio>listEtapaActual = (List<MpEtapaGestionEnvio>) etapaGestionEnvioDao.getListById(a.getGestionEnvio().getId().toString(), "");
	      
	      for ( MpEtapaGestionEnvio etapa : listEtapaActual ) {
	    	  etapa.setCodigo(Constantes.ETAPA_EN_REPORTE);
	    	  etapa.setUsuarioModifica(datosSession.getNombreUsuario());
	    	  etapa.setFechaModifica(new Date());
	    	  etapaGestionEnvioDao.create(etapa);
	      }
		return detGestionEnvioDao.updateMpDetGestionEnvio(a);
		
	}


	@Override
	public Object getContenidoReporte(Usuario usuario, String reporteId, String pag, String pagLength)
			throws Exception {
		
		Map<String, Object> result = new HashMap<String, Object>();
		List<FichaPedientes> lsContenidoReporte = new ArrayList<>();
		log.debug("Inicio - getContenidoReporte()");
		List<MpEtapaGestionEnvio> lsReportesGenerados = new ArrayList<>();
		lsReportesGenerados = etapaGestionEnvioDao.getContenidoReporteById(Optional.fromNullable(reporteId));
		if (lsReportesGenerados.isEmpty()) {
			Reporte reporte = new Reporte();
			result.put("cabeceraReporte", reporte);
			result.put("contenidoReporte", lsContenidoReporte);
			return result;
		}
		/*Datos Cabecera Reporte*/
		Reporte reporte = new Reporte();
		MpGestionEnvio mpGestionEnvio = lsReportesGenerados.get(0).getGestionEnvio();
		
		reporte.setId(mpGestionEnvio.getId());
		reporte.setFechaGenera(mpGestionEnvio.getFechaGeneracionFormatoMP());
		reporte.setNumRegistros(mpGestionEnvio.getCantidadRegistros());
		reporte.setNumDocFisicos(mpGestionEnvio.getCantidadFisicos());
	
		reporte.setTituloReporte(Constantes.TITULO_REPORTE_GENERADO.concat("|").concat(mpGestionEnvio.getGrupoEnvio().toString()));
		reporte.setCentroCostoDes(usuario.getEmpleado().getCentroCosto().getDescripcion());
		reporte.setTotalRegistros(mpGestionEnvio.getCantidadRegistros().toString());
		reporte.setTotalDocFisicos(mpGestionEnvio.getCantidadFisicos().toString());
		
		Empleado empleadoCargo = empleadoDao.findByNameUsuario(mpGestionEnvio.getUsuarioGeneracion());
		String remitenteCargo = "";		
		remitenteCargo = new StringBuffer().append(empleadoCargo.getNombres().trim()).append(" ").append(empleadoCargo.getApellidos().trim()).toString();
		reporte.setRegistradoPor(remitenteCargo);
		
		// *Definimos le estado del reporte*//
		
		reporte.setEstadoReporte(mpGestionEnvio.getEstado().trim());
		reporte.setDesEstadoReporte(new StringBuilder().append(Constantes.ESTADO_REPORTE.get(mpGestionEnvio.getEstado().toString().trim())).
		append(" ").append(Constantes.GRUPO_REPORTES.get(mpGestionEnvio.getTipoReporte().toString().trim())).toString());
				
		/*Contenido Reporte*/
		List<EnvioMultiple> listEnvioMultiple = envioMultipleDao.getByIdGestionEnvio(Optional.fromNullable(reporteId.toString()),Optional.fromNullable(Constantes.VACIO));
		
		EnvioMultiple envio = listEnvioMultiple != null && !listEnvioMultiple.isEmpty() ? listEnvioMultiple.get(0): null;
		String tipoReporte = envio != null ? envio.getTipoReporte(): Constantes.VACIO;
			
		if ( tipoReporte != null && !tipoReporte.isEmpty() ) {
			if ( Constantes.ENVIO_PARA_ESTAFETA.toString().equals(tipoReporte.trim()) ) {
				lsContenidoReporte = (List<FichaPedientes>) gestionEnvioService.getEnviosPendientes(usuario, mpGestionEnvio.getGrupoEnvio().toString().trim(), pag, pagLength, Constantes.VACIO, reporteId);		
			} else {
				lsContenidoReporte = (List<FichaPedientes>) gestionEnvioService.getEnviosPendientes(usuario, mpGestionEnvio.getGrupoEnvio().toString().trim(), pag, pagLength, envio.getCentroCostoId().trim(), reporteId);
			}
			
		}
		result.put("cabeceraReporte", reporte);
		result.put("contenidoReporte", lsContenidoReporte);
		return result;
	}


	@Override
	public byte[] getReporteCargo(HttpServletRequest request, Usuario usuario, List<Reporte> listCargo) throws Exception {
		File reportsDir = new File(request.getSession().getServletContext().getRealPath("/assets/"));
		if (!reportsDir.exists()) {
			try {
				throw new FileNotFoundException(String.valueOf(reportsDir));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("usuario", usuario.getNombreUsuario());
		map.put("centroCostoDes", usuario.getEmpleado().getCentroCosto().getDescripcion());
				
		List cargos = new ArrayList<>();
		int totalDocFisicos = 0;
		for ( Reporte f : listCargo ) {
			totalDocFisicos += f.getNumDocFisicos();
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("fecha_generacion", f.getFechaGenera());
			data.put("nro_registros", f.getNumRegistros());
			data.put("nro_docfisicos", f.getNumDocFisicos());
			data.put("emitido_por", f.getRegistradoPor());
			data.put("estado_reporte", f.getDesEstadoReporte());
			data.put("fec_cargo", f.getCargoReporte().getFechaAdjunto());
			cargos.add(data);			
		}
		map.put("totalRegistros", listCargo.size());
		map.put("totalDocFisicos", totalDocFisicos);
		map.put("cargos", cargos);
		
		map.put(JRParameter.REPORT_LOCALE, Locale.US);
		map.put(JRParameter.REPORT_FILE_RESOLVER, new SimpleFileResolver(reportsDir));

		byte[] reportePdf = null;

		String ruta= request.getSession().getServletContext().getRealPath("/assets/rptCargosMesaPartes.jrxml");
		
		JasperReport reporte= JasperCompileManager.compileReport(ruta);
		JasperPrint print = JasperFillManager.fillReport(reporte, map, new JREmptyDataSource());
		reportePdf = JasperExportManager.exportReportToPdf(print);
		
		return reportePdf;
	}


	@Override
	public Object retirarContenidoReporte(Usuario datosSession, MpEtapaGestionEnvio etapa, String operacion)
			throws Exception {
		Date fechaActual = new Date();
		etapa.setUsuarioModifica(datosSession.getNombreUsuario());
        etapa.setFechaModifica(fechaActual);
        MpGestionEnvio gestionEnvio = new MpGestionEnvio();
        gestionEnvio.setId(etapa.getGestionEnvioId());
        
        Optional<String> fichaDocumentoId = Optional.fromNullable(etapa.getFichaDocumentoId().toString());
		Optional<String> gestionEnvioId = Optional.fromNullable(etapa.getGestionEnvioId().toString());
		List<EnvioMultiple> listEnvios = envioMultipleDao.getByIdDocumentoIdReporte(fichaDocumentoId, gestionEnvioId);
        
        gestionEnvio.setCantidadRegistros(etapa.getGestionEnvio().getCantidadRegistros() - Constantes.UN_SOLO_REGISTRO_ELIMINADO);
        gestionEnvio.setCantidadFisicos(etapa.getGestionEnvio().getCantidadFisicos() - listEnvios.size());
        gestionEnvio.setUsuarioModifica(datosSession.getNombreUsuario());
        gestionEnvio.setFechaModifica(fechaActual);
        gestionEnvio.setFechaSubsanado(fechaActual);
        
       //actualizar cabecera

        Integer ge = gestionEnvioDao.updateMpGestionEnvio(gestionEnvio);
        
        //envioMultiple cambiar estado
//        EnvioMultiple envioMultiple = new EnvioMultiple();
//        envioMultiple.setReporte(Constantes.NO_HABILITADO);
//        envioMultiple.setUsuarioModifica(datosSession.getEmpleado().getId().toString());
//        envioMultiple.setFechaModifica(fechaActual);
//        envioMultiple.setGestionEnvioId(etapa.getGestionEnvioId());
//        envioMultiple.setFichaDocumentoId(etapa.getFichaDocumentoId());
//        Integer em = envioMultipleDao.updateEnvioMultiple(envioMultiple);
        
//        List<EnvioMultiple> listEnviosInsertar = new ArrayList<>();  
        //Actualiza la lista de envios multiples actuales a habilitado = false
    	for ( EnvioMultiple envio : listEnvios) {
		   operacion = Constantes.OPERACION_ACTUALIZAR;
		   envio.setHabilitado(Constantes.NO_HABILITADO);
		   envioMultipleService.createEnvioMultiple((Usuario) datosSession, envio, operacion); // Lo quita
		   EnvioMultiple envioNuevo = new EnvioMultiple();
		   envioNuevo.setHabilitado(Constantes.HABILITADO);
		   envioNuevo.setFichaDocumentoId(envio.getFichaDocumentoId());
		   envioNuevo.setProveidoId(envio.getProveidoId());
		   envioNuevo.setCanalEnvio(envio.getCanalEnvio());
		   envioNuevo.setMultiple(envio.getMultiple());
		   envioNuevo.setCentroCostoId(envio.getCentroCostoId());
		   envioNuevo.setReporte(Constantes.SIN_REPORTE);
		   envioNuevo.setEstado(envio.getEstado());
		   envioNuevo.setHabilitado(Constantes.HABILITADO);
		   envioNuevo.setSubsanado(envio.getSubsanado());
		   envioNuevo.setUsuarioCrea(datosSession.getNombreUsuario());
		   envioNuevo.setFechaCrea(new Date());
		   operacion = Constantes.OPERACION_CREAR;
 		   envioMultipleService.createEnvioMultiple((Usuario) datosSession, envioNuevo, operacion); // Lo crea
    	}
    	//Inserta la lista de envios multiples retirados con tipoReporte = null e gestionEnvioId = null (sin reporte)
//    	for (EnvioMultiple envio : listEnviosInsertar ) {
// 		   operacion = Constantes.OPERACION_CREAR;
// 		   envioMultipleService.createEnvioMultiple((Usuario) datosSession, envio, operacion); // Lo crea
//    	}
        
        //UPDATE FICHA PROVEIDO
        FichaProveido fichaProveido = new FichaProveido();
        fichaProveido.setEstado(Constantes.FP_PENDIENTE_DE_REPORTE);
        fichaProveido.setFichaDocumentoId(etapa.getFichaDocumentoId());
        fichaProveido.setUsuarioModifica(datosSession.getNombreUsuario());
        fichaProveido.setFechaModifica(fechaActual);
        Integer fp = fichaProveidoDao.updateEstadoFichaProveido(fichaProveido);

        return etapaGestionEnvioDao.updateMpDetGestionEnvio(etapa);
	}
	
}
