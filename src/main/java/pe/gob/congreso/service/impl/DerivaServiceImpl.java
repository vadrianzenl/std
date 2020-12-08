package pe.gob.congreso.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codemonkey.simplejavamail.Email;
import javax.mail.internet.MimeMessage.RecipientType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.google.common.base.Optional;

import pe.gob.congreso.crypto.Hex;
import pe.gob.congreso.dao.AdjuntoDao;
import pe.gob.congreso.dao.DerivaDao;
import pe.gob.congreso.dao.EmpleadoDao;
import pe.gob.congreso.dao.EnviadoExternoDao;
import pe.gob.congreso.dao.EtapaGestionEnvioDao;
import pe.gob.congreso.dao.FichaDocumentoDao;
import pe.gob.congreso.dao.FichaProveidoDao;
import pe.gob.congreso.dao.FisicoDao;
import pe.gob.congreso.dao.NotasDao;
import pe.gob.congreso.dao.NotificacionEmpleadoDao;
import pe.gob.congreso.dao.TipoDao;
import pe.gob.congreso.dao.UsuarioDao;
import pe.gob.congreso.model.AnioLegislativo;
import pe.gob.congreso.model.Bitacora;
import pe.gob.congreso.model.Deriva;
import pe.gob.congreso.model.Empleado;
import pe.gob.congreso.model.EnviadoExterno;
import pe.gob.congreso.model.FichaProveido;
import pe.gob.congreso.model.MpCanalAsociadoEnvio;
import pe.gob.congreso.model.MpEtapaGestionEnvio;
import pe.gob.congreso.model.Notas;
import pe.gob.congreso.model.NotificacionEmpleado;
import pe.gob.congreso.model.SeguimientoFisico;
import pe.gob.congreso.model.Tipo;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.DerivaUtil;
import pe.gob.congreso.model.util.EnviadoUtil;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.service.ActividadService;
import pe.gob.congreso.service.AnioLegislativoService;
import pe.gob.congreso.service.BitacoraService;
import pe.gob.congreso.service.CanalAsociadoEnvioService;
import pe.gob.congreso.service.DerivaService;
import pe.gob.congreso.service.EnvioMultipleService;
import pe.gob.congreso.service.GestionConsultaService;
import pe.gob.congreso.util.Constantes;
import pe.gob.congreso.util.DateHelper;
import pe.gob.congreso.util.EmailHelper;
import pe.gob.congreso.util.FichaConsultas;

@Service("derivaService")
@Transactional
public class DerivaServiceImpl implements DerivaService {

	private final static Integer ESTADO_REGISTRADO = 7;
	// private final static Integer ESTADO_ENVIADO = 8;
	// private final static Integer ESTADO_LEIDO = 9;
	// private final static Integer ESTADO_FINALIZADO = 10;
	// private final static Integer ESTADO_ANULADO = 11;
	// private final static Integer ESTADO_CERRADO = 21;
	// private final static Integer ESTADO_DEVUELTO = 22;

	@Autowired
	DerivaDao derivaDao;

	@Autowired
	TipoDao tipoDao;

	@Autowired
	FichaDocumentoDao fichaDocumentoDao;

	@Autowired
	EmpleadoDao empleadoDao;

	@Autowired
	ActividadService actividadService;

	@Autowired
	BitacoraService bitacoraService;

	@Autowired
	EnviadoExternoDao enviadoExternoDao;

	@Autowired
	NotificacionEmpleadoDao notificacionEmpleadoDao;

	@Autowired
	UsuarioDao usuarioDao;

	@Autowired
	AdjuntoDao adjuntoDao;

	// Mesa de Partes
	@Autowired
	EtapaGestionEnvioDao etapaGestionEnvioDao;

	@Autowired
	EnvioMultipleService envioMultipleService;

	@Autowired
	CanalAsociadoEnvioService canalAsociadoEnvioService;

	@Autowired
	GestionConsultaService gestionConsultaService;

	@Autowired
	FichaProveidoDao fichaProveidoDao;
	// Fin Mesa de Partes

	@Autowired
	NotasDao notasDao;

	// Inicio STD-V.2.3.0
	@Autowired
	FisicoDao fisicoDao;
	// Fin STD-V.2.3.0

	@Autowired
	AnioLegislativoService anioLegislativoService;

	private final EmailHelper emailHelper = new EmailHelper();

	private final Hex hex = new Hex();

	protected final Log log = LogFactory.getLog(getClass());

	public List test() throws Exception {
		return derivaDao.test();
	}

	@Override
	public List<Deriva> findDerivados(String fichaDocumentoId) throws Exception {
		return derivaDao.findDerivados(fichaDocumentoId);
	}

	@Override
	public List<Deriva> findDirigidos(String fichaDocumentoId) throws Exception {
		return derivaDao.findDirigidos(fichaDocumentoId);
	}

	@Override
	public Object create(Usuario usuario, Deriva d, String operacion) throws Exception {
		Integer fichaId = Optional.fromNullable(d.getFichaDocumento().getId()).isPresent() ? d.getFichaDocumento().getId()
				: 0;
		String destinoId = "";
		if (Optional.fromNullable(d.getEmpleadoDestino()).isPresent()
				&& Optional.fromNullable(d.getEmpleadoDestino().getId()).isPresent())
			destinoId = " - EmpDestinoId: " + d.getEmpleadoDestino().getId();
		if (Optional.fromNullable(d.getEnviadoExterno()).isPresent()
				&& Optional.fromNullable(d.getEnviadoExterno().getId()).isPresent())
			destinoId = " - ExternoId: " + d.getEnviadoExterno().getId();

		log.info("create() - fichaId:" + fichaId + destinoId);
		if (d.getEstado().getId() == 8 && d.isHabilitado() == true
				&& (d.getEmpleadoDestino() != null || d.getEnviadoExterno() != null) && !d.isIndicaDirigido()) {
			String em = "";
			if (d.getEmpleadoDestino() != null) {
				Empleado e = empleadoDao.findById(d.getEmpleadoDestino().getId());

				if (Optional.fromNullable(e).isPresent()) {
					// em = e.getDescripcion() + " - " + e.getCentroCosto().getDescripcion();
					em = e.getDescripcion() + " - " + d.getCentroCostoDestino().getDescripcion();
					NotificacionEmpleado ne = notificacionEmpleadoDao.findBy(e.getId(), 1);
					if (Optional.fromNullable(ne).isPresent()) {
						this.enviarEmail(d, e, usuario);
					}
				} else {
					em = d.getEmpleadoDestino().getDescripcion() + " - " + d.getCentroCostoDestino().getDescripcion();
				}
			} else if (d.getEnviadoExterno() != null) {
				EnviadoExterno ex = enviadoExternoDao.findById(d.getEnviadoExterno().getId());

				if (Optional.fromNullable(ex).isPresent()) {
					em = (!StringUtils.isEmpty(ex.getApellidos()) ? ex.getApellidos() : "")
							+ (!StringUtils.isEmpty(ex.getNombres()) ? " " + ex.getNombres() : "")
							+ (!StringUtils.isEmpty(ex.getCargoExterno()) ? " - " + ex.getCargoExterno() : "");
					if (!StringUtils.isEmpty(ex.getOrigen())) {
						em = ex.getOrigen() + (!StringUtils.isEmpty(em) ? " - " + em : "");
					}
				} else {
					em = "ENTIDAD O PERSONA EXTERNA";
				}
			}

			Bitacora b = new Bitacora();
			b.setFecha(new Date());
			b.setEmpleado(usuario.getEmpleado());
			b.setCentroCosto(usuario.getEmpleado().getCentroCosto());

			if (d.getDirigido() == 1) {
				if (d.isCcopia()) {
					b.setDescripcion("COPIÓ EL DOCUMENTO A " + em);
				} else {
					b.setDescripcion("DIRIGIÓ EL DOCUMENTO A " + em);
				}
			} else if (d.getDirigido() == 0) {
				b.setDescripcion("DERIVÓ EL DOCUMENTO A " + em);
			} else if (d.getDirigido() == 2) {
				b.setDescripcion("DERIVÓ PARCIALMENTE A " + em);
			}

			b.setIndicaciones(d.getIndicaciones());
			b.setEstado(d.getEstado());
			b.setFichaDocumento(d.getFichaDocumento());

			bitacoraService.create(usuario, b, operacion);

		}

		if (Optional.fromNullable(d.getCentroCostoDestino()).isPresent()) {
			Integer ccd = Integer.parseInt(d.getCentroCostoDestino().getId().trim());
			d.setCentroCostoDestinoId(ccd);
		} else {
			d.setCentroCostoDestinoId(0);
		}
		if (Optional.fromNullable(d.getFechaDeriva()).isPresent()) {
			String an = d.getFechaDeriva().substring(0, 4);
			d.setAnio(Integer.parseInt(an));
		} else {
			Calendar fecha = Calendar.getInstance();
			int anio = fecha.get(Calendar.YEAR);
			d.setAnio(anio);
		}
		Deriva deriva = derivaDao.create(d);
		actividadService.create(usuario, d, operacion);
		return deriva;

	}

	public void enviarEmail(Deriva d, Empleado e, Usuario usuario) throws Exception {
		if (Optional.fromNullable(e.getEmail()).isPresent()) {
			if (!e.getEmail().equals("")) {
				Tipo cuentaEmail = tipoDao.findByNombre("EMAIL_CUENTA");
				Tipo passwordEmail = tipoDao.findByNombre("EMAIL_PASSWORD");
				Tipo user = tipoDao.findByNombre("EMAIL_USER");
				Tipo nombreEnvio = tipoDao.findByNombre("EMAIL_NOMBRE_ENVIO");
				Tipo urlStd = tipoDao.findByNombre("URL_STD");

				try {
					final Email email = new Email();
					email.setFromAddress(nombreEnvio.getDescripcion().trim(), cuentaEmail.getDescripcion().trim());

					email.addRecipient(e.getApellidos().trim() + ", " + e.getNombres().trim(), e.getEmail().trim(),
							RecipientType.TO);

					email.setSubject("[STD] Nuevo Documento Recibido - Reg. Único : " + d.getFichaDocumento().getId());
					email.setText("");

					String body = "<font face=\"Calibri\" size=\"3\" color=\"#1F497D\"><p><b>Estimado Usuario (a):</b><br/>";
					body = body + e.getNombres().trim() + " " + e.getApellidos().trim() + "</p>";
					body = body
							+ "<p>Nos dirigimos a Usted, para informarle que ha recibido un nuevo documento en el Sistema de Trámite Documentario con Número de Registro Único: "
							+ d.getFichaDocumento().getId() + "</p>";
					body = body
							+ "<table border = \"0\"><tr><td><font face=\"Calibri\" size=\"3\" color=\"#1F497D\"><b>Enviado por </b></font></td><td><font face=\"Calibri\" size=\"3\" color=\"#1F497D\"><b>:</b></font></td><td><font face=\"Calibri\" size=\"3\" color=\"#1F497D\">"
							+ usuario.getEmpleado().getNombres().trim() + " " + usuario.getEmpleado().getApellidos().trim()
							+ "</font></td></tr>";
					body = body
							+ "<tr><td><font face=\"Calibri\" size=\"3\" color=\"#1F497D\"><b>Asunto </b><font></td><td><font face=\"Calibri\" size=\"3\" color=\"#1F497D\"><b>:</b></font></td><td><font face=\"Calibri\" size=\"3\" color=\"#1F497D\"> "
							+ d.getFichaDocumento().getAsunto().trim() + "</font></td></tr></table>";
					body = body + "<p>Para Ingresar al Sistema, use el siguiente enlace : " + urlStd.getDescripcion().trim()
							+ "</p>";
					body = body
							+ "<p>Si requiere apoyo técnico en el Sistema de Trámite Documentario, favor llame al anexo: 6200.</p>";
					body = body + "<p>Atentamente,<br/>";
					body = body + "SISTEMA DE TRÁMITE DOCUMENTARIO<br/>";
					body = body + "Oficina de Tecnologías de la Información</p><br/></font>";

					email.setTextHTML(body);

					emailHelper.send(email, user.getDescripcion().trim(), hex.decodeSiga(passwordEmail.getDescripcion().trim()));

				} catch (Exception ex) {
					log.info(" " + ex);
				}

			}

		}
	}

	@Override
	public Object create(Deriva d) throws Exception {
		if (Optional.fromNullable(d.getCentroCostoDestino()).isPresent()) {
			Integer ccd = Integer.parseInt(d.getCentroCostoDestino().getId().trim());
			d.setCentroCostoDestinoId(ccd);
		}
		if (Optional.fromNullable(d.getFechaDeriva()).isPresent()) {
			String an = d.getFechaDeriva().substring(0, 4);
			d.setAnio(Integer.parseInt(an));
		}
		Deriva deriva = derivaDao.create(d);
		return deriva;
	}

	@Override
	public Object editDerivaRespuesta(Usuario usuario, Deriva d, String operacion) throws Exception {
		log.debug("editDerivaRespuesta()");
		if (Optional.fromNullable(d.getCentroCostoDestino()).isPresent()) {
			Integer ccd = Integer.parseInt(d.getCentroCostoDestino().getId().trim());
			d.setCentroCostoDestinoId(ccd);
		}
		if (Optional.fromNullable(d.getFechaDeriva()).isPresent()) {
			String an = d.getFechaDeriva().substring(0, 4);
			d.setAnio(Integer.parseInt(an));
		}
		actividadService.create(usuario, d, operacion);
		return derivaDao.create(d);
	}

	@Override
	public Deriva findDerivado(String id) throws Exception {

		Deriva deriva = derivaDao.findDerivado(id);
		// *Obtenemos el número de adjuntos*//
		int adjuntos = adjuntoDao.verificadAdjuntos(String.valueOf(deriva.getFichaDocumento().getId()));
		if (adjuntos > 0) {
			deriva.getFichaDocumento().setAdjuntos(true);
		} else {
			deriva.getFichaDocumento().setAdjuntos(false);
		}

		return deriva;
	}

	@Override
	public Map<String, Object> findBy(Usuario usuario, Optional<String> tipoRegistro, Optional<String> empleadoId,
			Optional<String> id, Optional<String> numeroDoc, Optional<String> asunto, Optional<String> fechaIniCrea,
			Optional<String> fechaFinCrea, Optional<String> tipoDocumento, Optional<String> tipoEstado,
			Optional<String> centroCosto, Optional<String> privado, Optional<String> remitidoDes, Optional<String> texto,
			Optional<String> referencia, Optional<String> observaciones, Optional<String> esResponsable,
			Optional<String> esRecibido, Optional<String> pag, Optional<String> pagLength, Boolean esMesaPartes,
			Optional<String> numeroMp, Optional<String> dependenciaId, Optional<String> anioLegislativo,
			Optional<String> esRecibiConforme) throws Exception {

		Map<String, Object> result = new HashMap<String, Object>();
		result = derivaDao.findBy(tipoRegistro, empleadoId, id, numeroDoc, asunto, fechaIniCrea, fechaFinCrea,
				tipoDocumento, tipoEstado, centroCosto, privado, remitidoDes, texto, referencia, observaciones, esResponsable,
				esRecibido, pag, pagLength, esMesaPartes, numeroMp, dependenciaId, anioLegislativo, esRecibiConforme);

		if (Optional.fromNullable(result).isPresent() && Optional.fromNullable(pag).isPresent()
				&& Optional.fromNullable(pagLength).isPresent()) {
			List<DerivaUtil> documentos = new ArrayList<>();
			try {
				documentos = (List<DerivaUtil>) result.get("documentos");
				System.out.println("Get documentos");
			} catch (Exception e) {
				System.out.println("Error convertir derivaUtil:" + e.getMessage());
			}

			if (documentos.size() > 0) {
				for (DerivaUtil doc : documentos) {
					String remitente = "";

					// *Obtenemos el número de adjuntos*//
					int adjuntos = adjuntoDao.verificadAdjuntos(String.valueOf(doc.getFichaDocumento().getId()));
					if (adjuntos > 0) {
						doc.setAdjuntos(adjuntos);
					}

					// *Obtenemos el número de notas*//
					List<Notas> lstNotas = null;
					if (Constantes.PERFIL_USUARIO_DEL_STD.equals(usuario.getPerfil().getId().trim())) {
						lstNotas = notasDao.findNotas(doc.getFichaDocumento().getId().toString(),
								usuario.getEmpleado().getCentroCosto().getId().trim(), usuario.getNombreUsuario().trim());
					} else if (Constantes.PERFIL_RESPONSABLE_DEL_AREA.equals(usuario.getPerfil().getId().trim())) {
						lstNotas = notasDao.findNotas(doc.getFichaDocumento().getId().toString(),
								usuario.getEmpleado().getCentroCosto().getId().trim(), Constantes.VACIO);
					}
					if (lstNotas != null) {
						if (lstNotas.size() > 0) {
							doc.setNotas(true);
						} else {
							doc.setNotas(false);
						}
					}

					// Obtenemos los derivados
					int derivados = 0;
					if (Optional.fromNullable(doc.getFichaDocumento().getId()).isPresent()) {
						List<Deriva> list = derivaDao.getOnlyDerivados(String.valueOf(doc.getFichaDocumento().getId()),
								centroCosto.get().trim(), doc.getId(), 22, doc.getEmpleadoOrigen().getId());
						if (list.size() > 0) {
							derivados = list.size();
						}
					}
					doc.setDerivados(derivados);

					// Obtenemos los dirigidos
					int dirigidos = 0;
					if (Optional.fromNullable(doc.getFichaDocumento().getId()).isPresent()) {
						List<Deriva> list = derivaDao.findDirigidosResponsable(String.valueOf(doc.getFichaDocumento().getId()));
						if (list.size() > 0) {
							dirigidos = list.size();
						}
					}
					doc.setDirigidos(dirigidos);

					int recibiConforme = 0;
					if (Constantes.DOCUMENTO_RECIBIDO_FISICO.equals(doc.getDirigido())) {
						List<SeguimientoFisico> ls = fisicoDao.findRecibiFisicoFichaId(doc.getFichaDocumento().getId().toString(),
								Constantes.ACTIVO_RECIBI_CONFORME);
						if (ls != null) {
							if (ls.size() > 0) {
								recibiConforme = ls.size();
							}
						}
					} else {
						if (Optional.fromNullable(doc.getFichaDocumento().getId()).isPresent()) {
							List<SeguimientoFisico> list = fisicoDao.findRecibiFisico(doc.getId().toString(),
									Constantes.ACTIVO_RECIBI_CONFORME);
							if (list != null) {
								if (list.size() > 0) {
									recibiConforme = list.size();
									doc.setSegFisico(list.get(Constantes.CERO));
								}
							}
						}
					}
					doc.setRecibiConforme(recibiConforme);

					switch (doc.getFichaDocumento().getTipoRegistro().getId()) {
					case 1:
						remitente = doc.getFichaDocumento().getCentroCosto().getDescripcion();
						doc.setRemitente(remitente);
						break;
					case 2:
						EnviadoExterno ee = enviadoExternoDao.findEnviadoPor(String.valueOf(doc.getFichaDocumento().getId()));
						// EnviadoExterno ee = null;
						if (Optional.fromNullable(ee).isPresent()) {
							switch (ee.getTipoEnviado().getId()) {
							case 24:
								if (Optional.fromNullable(ee.getEmpleado()).isPresent()) {
									remitente = ee.getCentroCosto().getDescripcion();
								}
								break;
							case 25:
								if ( ee.getOrigen()!=null && !ee.getOrigen().equals("") ) {
									remitente = ee.getOrigen();
									if ( ee.getNombres()!=null && !ee.getNombres().equals("") ) {
										remitente = remitente + " / " + ee.getNombres();
										if ( ee.getApellidos()!=null && !ee.getApellidos().equals("")) {
											remitente = remitente + " " + ee.getApellidos();
										}
									}
								} else {
									if ( ee.getNombres()!=null && !ee.getNombres().equals("") ) {
										remitente = ee.getNombres() + " ";
									}
									if ( ee.getApellidos()!=null && !ee.getApellidos().equals("")) {
										remitente = remitente + ee.getApellidos();
									}
								}								
								break;
							default:
								break;
							}
						}
						doc.setRemitente(remitente);
						break;
					default:
						doc.setRemitente(remitente);
						break;
					}

					// Mesa de partes
					if (doc.getFichaDocumento().getListFichasProveido() != null
							&& !doc.getFichaDocumento().getListFichasProveido().isEmpty()) {
						FichaProveido fp = doc.getFichaDocumento().getListFichasProveido().get(0);
						if (fp != null && Constantes.ID_PROVEIDO_MESA_DE_PARTES.equals(fp.getProveidoId())) {
							doc.setNumeroMesaPartes(fp.getNumero());
							doc.setSumillaMesaPartes(fp.getSumilla());
							doc = asignarEtapaIndicadorMesaPartes(doc);
							// Enviado a
							List<EnviadoUtil> enviados = derivaDao.findEnviados(doc.getFichaDocumento().getId(),
									Constantes.ES_MESA_DE_PARTES);
							doc.setEnviadoADes(gestionConsultaService.getEnviados(enviados));
						}
					}
					// Fin Mesa de partes
				}
				// Mesa de partes
			}
		}

		return result;
	}

	@Override
	public Object validarDevolver(String id, String empleadoId, String idDeriva) throws Exception {
		Integer idDocumento = Integer.parseInt(id);
		Integer idDer = Integer.parseInt(idDeriva);
		Boolean isLeido = true;
		String response = "";
		Integer code = 0;

		if (Optional.fromNullable(idDocumento).isPresent()) {
			List<Deriva> list = derivaDao.getOnlyDerivados(String.valueOf(idDocumento));

			for (Deriva der : list) {
				if (der.getEmpleadoOrigen().getId() == Integer.parseInt(empleadoId)) {
					if (der.getId() > idDer) {
						if (der.getEstado().getId() != 22 && der.getEstado().getId() != 21) {
							isLeido = false;
						}
					}
				}
			}
			if (isLeido) {
				code = 200;
				response = "OK";
			} else {
				code = 400;
				response = "ERROR";
			}

		}
		return new Status(code, response);
	}

	@Override
	public Object devolverDerivado(Usuario usuario, Deriva d, String operacion, String motivo) throws Exception {
		Integer idDocumento = d.getFichaDocumento().getId();

		List<Deriva> list = derivaDao.getDerivadosToValidar(idDocumento, d.getEmpleadoOrigen().getId(),
				d.getEmpleadoDestino().getCentroCosto().getId());
		for (Deriva der : list) {
			Tipo tipoDevuelto = tipoDao.findByCodigo("22");
			if (Optional.fromNullable(tipoDevuelto).isPresent()) {
				der.setEstado(tipoDevuelto);
				derivaDao.create(der);
			}
		}

		Bitacora b = new Bitacora();
		b.setFecha(new Date());
		b.setEmpleado(usuario.getEmpleado());
		b.setCentroCosto(usuario.getEmpleado().getCentroCosto());
		b.setDescripcion("DEVOLUCIÓN DEL DOCUMENTO");
		b.setIndicaciones(motivo);
		b.setEstado(d.getEstado());
		b.setFichaDocumento(d.getFichaDocumento());

		bitacoraService.create(usuario, b, operacion);

		Empleado e = empleadoDao.findById(d.getEmpleadoOrigen().getId());
		if (Optional.fromNullable(e).isPresent()) {
			NotificacionEmpleado ne = notificacionEmpleadoDao.findBy(d.getEmpleadoOrigen().getId(), 4);
			if (Optional.fromNullable(ne).isPresent()) {
				this.enviarEmailDevolver(d, e, usuario, motivo);
			}
		}

		return d;
	}

	public void enviarEmailDevolver(Deriva d, Empleado e, Usuario usuario, String motivo) throws Exception {
		if (Optional.fromNullable(e.getEmail()).isPresent()) {
			if (!e.getEmail().equals("")) {
				Tipo cuentaEmail = tipoDao.findByNombre("EMAIL_CUENTA");
				Tipo passwordEmail = tipoDao.findByNombre("EMAIL_PASSWORD");
				Tipo user = tipoDao.findByNombre("EMAIL_USER");
				Tipo nombreEnvio = tipoDao.findByNombre("EMAIL_NOMBRE_ENVIO");
				Tipo urlStd = tipoDao.findByNombre("URL_STD");

				try {
					final Email email = new Email();
					email.setFromAddress(nombreEnvio.getDescripcion().trim(), cuentaEmail.getDescripcion().trim());

					email.addRecipient(e.getApellidos().trim() + ", " + e.getNombres().trim(), e.getEmail().trim(),
							RecipientType.TO);

					email.setSubject("[STD] Devolución del Documento con RU : " + d.getFichaDocumento().getId());
					email.setText("");

					String body = "<font face=\"Calibri\" size=\"3\" color=\"#1F497D\"><p><b>Estimado Usuario (a):</b><br/>";
					body = body + e.getNombres().trim() + " " + e.getApellidos().trim() + "</p>";
					body = body
							+ "<p>Nos dirigimos a Usted, para informarle que le han devuelto el documento con Número de Registro Único: "
							+ d.getFichaDocumento().getId() + "</p>";
					body = body
							+ "<table border = \"0\"><tr><td><font face=\"Calibri\" size=\"3\" color=\"#1F497D\"><b>Devuelto por </b></font></td><td><font face=\"Calibri\" size=\"3\" color=\"#1F497D\"><b>:</b></font></td><td><font face=\"Calibri\" size=\"3\" color=\"#1F497D\">"
							+ usuario.getEmpleado().getNombres().trim() + " " + usuario.getEmpleado().getApellidos().trim()
							+ "</font></td></tr>";
					body = body
							+ "<tr><td><font face=\"Calibri\" size=\"3\" color=\"#1F497D\"><b>Asunto </b><font></td><td><font face=\"Calibri\" size=\"3\" color=\"#1F497D\"><b>:</b></font></td><td><font face=\"Calibri\" size=\"3\" color=\"#1F497D\"> "
							+ d.getFichaDocumento().getAsunto().trim() + "</font></td></tr>";
					body = body
							+ "<tr><td><font face=\"Calibri\" size=\"3\" color=\"#1F497D\"><b>Motivo de Devolución </b><font></td><td><font face=\"Calibri\" size=\"3\" color=\"#1F497D\"><b>:</b></font></td><td><font face=\"Calibri\" size=\"3\" color=\"#1F497D\"> "
							+ motivo.trim() + "</font></td></tr></table>";
					body = body + "<p>Para Ingresar al Sistema, use el siguiente enlace : " + urlStd.getDescripcion().trim()
							+ "</p>";
					body = body
							+ "<p>Si requiere apoyo técnico en el Sistema de Trámite Documentario, favor llame al anexo: 6200.</p>";
					body = body + "<p>Atentamente,<br/>";
					body = body + "SISTEMA DE TRÁMITE DOCUMENTARIO<br/>";
					body = body + "Oficina de Tecnologías de la Información</p><br/></font>";

					email.setTextHTML(body);

					emailHelper.send(email, user.getDescripcion().trim(), hex.decodeSiga(passwordEmail.getDescripcion().trim()));

				} catch (Exception ex) {
					log.info(" " + ex);
				}

			}

		}
	}

	@Override
	public Object leerDerivado(Usuario usuario, Deriva d, String operacion) throws Exception {
		if (Optional.fromNullable(d.getCentroCostoDestino()).isPresent()) {
			Integer ccd = Integer.parseInt(d.getCentroCostoDestino().getId().trim());
			d.setCentroCostoDestinoId(ccd);
		}
		if (Optional.fromNullable(d.getFechaDeriva()).isPresent()) {
			String an = d.getFechaDeriva().substring(0, 4);
			d.setAnio(Integer.parseInt(an));
		}
		Deriva deriv = derivaDao.create(d);

		Bitacora b = new Bitacora();
		b.setFecha(new Date());
		b.setEmpleado(usuario.getEmpleado());
		b.setCentroCosto(usuario.getEmpleado().getCentroCosto());
		b.setDescripcion("LEYÓ EL DOCUMENTO");
		b.setIndicaciones("CAMBIO DE ESTADO CORRECTO");
		b.setEstado(d.getEstado());
		b.setFichaDocumento(d.getFichaDocumento());

		bitacoraService.create(usuario, b, operacion);

		return deriv;
	}

	@Override
	public Object validarCierre(String id, String empleadoId, String idDeriva) throws Exception {
		Integer idDocumento = Integer.parseInt(id);
		Integer idDer = Integer.parseInt(idDeriva);
		Boolean isLeido = true;
		String response = "";
		Integer code = 0;

		if (Optional.fromNullable(idDocumento).isPresent()) {
			List<Deriva> list = derivaDao.getOnlyDerivados(String.valueOf(idDocumento));

			for (Deriva der : list) {
				if (der.getEmpleadoOrigen().getId() == Integer.parseInt(empleadoId)) {
					if (der.getId() > idDer) {
						if (der.getEstado().getId() != 22 && der.getEstado().getId() != 21 && der.getDirigido() != 3) {
							isLeido = false;
						}
					}
				}
			}

			if (isLeido) {
				code = 200;
				response = "OK";
			} else {
				code = 400;
				response = "ERROR";
			}

		}
		return new Status(code, response);
	}

	@Override
	public Object cerrarDerivado(Usuario usuario, Deriva d, String operacion, String motivo) throws Exception {
		actividadService.create(usuario, d, operacion);

		Integer idDocumento = d.getFichaDocumento().getId();
		List<Deriva> list = derivaDao.getDerivadosToValidar(idDocumento, d.getEmpleadoOrigen().getId(),
				d.getEmpleadoDestino().getCentroCosto().getId());
		log.info("Lista de Derivados");
		log.info(list.size());
		for (Deriva der : list) {
			Tipo tipoCerrado = tipoDao.findByCodigo("21");
			if (Optional.fromNullable(tipoCerrado).isPresent()) {
				if (!der.isEsResponsable() && !der.getId().equals(d.getId())) {
					der.setEstado(tipoCerrado);
					derivaDao.create(der);
				}
				if (der.getId().equals(d.getId())) {
					der.setEstado(tipoCerrado);
					derivaDao.create(der);
				}
			}
		}

		// derivaDao.create(d);

		Bitacora b = new Bitacora();
		b.setFecha(new Date());
		b.setEmpleado(usuario.getEmpleado());
		b.setCentroCosto(usuario.getEmpleado().getCentroCosto());
		b.setDescripcion("ATENDIÓ EL DOCUMENTO");
		b.setIndicaciones(motivo);
		b.setEstado(d.getEstado());
		b.setFichaDocumento(d.getFichaDocumento());

		bitacoraService.create(usuario, b, operacion);

		Empleado e = empleadoDao.findById(d.getEmpleadoOrigen().getId());
		if (Optional.fromNullable(e).isPresent()) {
			NotificacionEmpleado ne = notificacionEmpleadoDao.findBy(d.getEmpleadoOrigen().getId(), 3);
			if (Optional.fromNullable(ne).isPresent()) {
				this.enviarEmailCerrar(d, e, usuario, motivo);
			}
		}

		return d;
	}

	public void enviarEmailCerrar(Deriva d, Empleado e, Usuario usuario, String motivo) throws Exception {
		if (Optional.fromNullable(e.getEmail()).isPresent()) {
			if (!e.getEmail().equals("")) {
				Tipo cuentaEmail = tipoDao.findByNombre("EMAIL_CUENTA");
				Tipo passwordEmail = tipoDao.findByNombre("EMAIL_PASSWORD");
				Tipo user = tipoDao.findByNombre("EMAIL_USER");
				Tipo nombreEnvio = tipoDao.findByNombre("EMAIL_NOMBRE_ENVIO");
				Tipo urlStd = tipoDao.findByNombre("URL_STD");

				try {
					final Email email = new Email();
					email.setFromAddress(nombreEnvio.getDescripcion().trim(), cuentaEmail.getDescripcion().trim());

					email.addRecipient(e.getApellidos().trim() + ", " + e.getNombres().trim(), e.getEmail().trim(),
							RecipientType.TO);

					email.setSubject("[STD] Cierre del Documento con RU : " + d.getFichaDocumento().getId());
					email.setText("");

					String body = "<font face=\"Calibri\" size=\"3\" color=\"#1F497D\"><p><b>Estimado Usuario (a):</b><br/>";
					body = body + e.getNombres().trim() + " " + e.getApellidos().trim() + "</p>";
					body = body
							+ "<p>Nos dirigimos a Usted, para informarle que se ha atendido el documento con Número de Registro Único: "
							+ d.getFichaDocumento().getId() + "</p>";
					body = body
							+ "<table border = \"0\"><tr><td><font face=\"Calibri\" size=\"3\" color=\"#1F497D\"><b>Atendido por </b></font></td><td><font face=\"Calibri\" size=\"3\" color=\"#1F497D\"><b>:</b></font></td><td><font face=\"Calibri\" size=\"3\" color=\"#1F497D\">"
							+ usuario.getEmpleado().getNombres().trim() + " " + usuario.getEmpleado().getApellidos().trim()
							+ "</font></td></tr>";
					body = body
							+ "<tr><td><font face=\"Calibri\" size=\"3\" color=\"#1F497D\"><b>Asunto </b><font></td><td><font face=\"Calibri\" size=\"3\" color=\"#1F497D\"><b>:</b></font></td><td><font face=\"Calibri\" size=\"3\" color=\"#1F497D\"> "
							+ d.getFichaDocumento().getAsunto().trim() + "</font></td></tr>";
					body = body
							+ "<tr><td><font face=\"Calibri\" size=\"3\" color=\"#1F497D\"><b>Motivo de Cierre </b><font></td><td><font face=\"Calibri\" size=\"3\" color=\"#1F497D\"><b>:</b></font></td><td><font face=\"Calibri\" size=\"3\" color=\"#1F497D\"> "
							+ motivo.trim() + "</font></td></tr></table>";
					body = body + "<p>Para Ingresar al Sistema, use el siguiente enlace : " + urlStd.getDescripcion().trim()
							+ "</p>";
					body = body
							+ "<p>Si requiere apoyo técnico en el Sistema de Trámite Documentario, favor llame al anexo: 6200.</p>";
					body = body + "<p>Atentamente,<br/>";
					body = body + "SISTEMA DE TRÁMITE DOCUMENTARIO<br/>";
					body = body + "Oficina de Tecnologías de la Información</p><br/></font>";

					email.setTextHTML(body);

					emailHelper.send(email, user.getDescripcion().trim(), hex.decodeSiga(passwordEmail.getDescripcion().trim()));

				} catch (Exception ex) {
					log.info(" " + ex);
				}

			}

		}
	}

	@Override
	public void compruebaCambiosValidos(Deriva d) throws Exception {
		if (Optional.fromNullable(d.getId()).isPresent()) {
			Deriva derivaActual = findDerivado(String.valueOf(d.getId()));

			// Comprobamos si el estado en base ya esta enviado
			if (!ESTADO_REGISTRADO.equals(derivaActual.getEstado().getId())) {

				// Comprobamos el estado anterior y actual
				if (derivaActual.getEstado().getId() == 22 && d.getEstado().getId() == 8) {
					Tipo tipoEstado = new Tipo();
					tipoEstado.setId(d.getEstado().getId());
					d.setEstado(tipoEstado);
				} else if (derivaActual.getEstado().getId() > d.getEstado().getId()) {
					Tipo tipoEstado = new Tipo();
					tipoEstado.setId(derivaActual.getEstado().getId());
					d.setEstado(tipoEstado);
				}

				d.setIndicaDirigido(true);
			}

		}
	}

	public Object leerDerivadoParcial(Usuario usuario, String fichaDocumentoId) throws Exception {
		boolean isParcial = false;
		if (Optional.fromNullable(fichaDocumentoId).isPresent()) {
			List<Deriva> list = derivaDao.getEnviadosParciales(Integer.valueOf(fichaDocumentoId));
			for (Deriva der : list) {
				if (der.getEmpleadoDestino().getId() == usuario.getEmpleado().getId()) {
					switch (der.getEstado().getId()) {
					case 8:
						Tipo estadoLeido = tipoDao.findByCodigo("9");
						if (Optional.fromNullable(estadoLeido).isPresent()) {
							der.setEstado(estadoLeido);
						}
						this.leerDerivado(usuario, der, "CREAR");
						isParcial = true;
						break;
					case 9:
						isParcial = true;
						break;
					default:
						isParcial = false;
						break;
					}
				}
			}

		} else {
			return new Status(400, "Ficha Documento no Encontrada");
		}

		if (isParcial) {
			return new Status(200, "PARCIAL");
		} else {
			return new Status(400, "NOPARCIAL");
		}

	}

	public List<EnviadoUtil> findEnviados(Integer id) throws Exception {
		return derivaDao.findEnviados(id, Constantes.NO_ES_MESA_DE_PARTES);
	}

	public DerivaUtil asignarEtapaIndicadorMesaPartes(DerivaUtil doc) throws Exception {
		java.util.Date fechaHoy = new Date();
		List<MpEtapaGestionEnvio> listEtapaMesaPartes = etapaGestionEnvioDao.getListById("",
				doc.getFichaDocumento().getId().toString());
		MpEtapaGestionEnvio etapaMesaPartes = listEtapaMesaPartes.isEmpty() ? null : listEtapaMesaPartes.get(0);
		if (etapaMesaPartes != null) {
			if (DateHelper.diferenciasDeFechas(DateHelper.deStringToDate(doc.getFichaDocumento().getFechaCrea()),
					fechaHoy) == 0) {
				doc.setEtapaMesaPartes(etapaMesaPartes.getCodigo());
				doc.setColorSemaforoMesaPartes(Constantes.SEMAFORO_REGISTRO.get(etapaMesaPartes.getCodigo()));
			} else {
				doc.setEtapaMesaPartes(etapaMesaPartes.getCodigo());
				doc.setColorSemaforoMesaPartes(Constantes.SEMAFORO_REGISTRO_ROJO.get(etapaMesaPartes.getCodigo()));

			}
		} else {
			if (DateHelper.diferenciasDeFechas(DateHelper.deStringToDate(doc.getFichaDocumento().getFechaCrea()),
					fechaHoy) == 0) {
				doc.setEtapaMesaPartes(Constantes.ETAPA_REGISTRADO);
				doc.setColorSemaforoMesaPartes(Constantes.SEMAFORO_REGISTRO.get(Constantes.ETAPA_REGISTRADO));
				if (doc.getFichaDocumento().getListDeriva() != null) {
					for (Deriva deriva : doc.getFichaDocumento().getListDeriva()) {
						if (!Constantes.RECIBIDO_FISICO.equals(deriva.getDirigido())) {
							if (Constantes.ESTADO_DOCUMENTO_ENVIADO.equals(deriva.getEstado().getId())
									|| Constantes.ESTADO_LEIDO.equals(deriva.getEstado().getId())) {
								doc.setEtapaMesaPartes(Constantes.ETAPA_EN_BANDEJA_DE_ENVIO);
								doc.setColorSemaforoMesaPartes(Constantes.SEMAFORO_REGISTRO.get(Constantes.ETAPA_EN_BANDEJA_DE_ENVIO));
							}
						}
					}
				} else {
					if (Constantes.ESTADO_DOCUMENTO_ENVIADO.equals(doc.getEstado().getId())
							|| Constantes.ESTADO_LEIDO.equals(doc.getEstado().getId())) {
						doc.setEtapaMesaPartes(Constantes.ETAPA_EN_BANDEJA_DE_ENVIO);
						doc.setColorSemaforoMesaPartes(Constantes.SEMAFORO_REGISTRO_ROJO.get(Constantes.ETAPA_EN_BANDEJA_DE_ENVIO));
					}
				}
			} else {
				doc.setEtapaMesaPartes(Constantes.ETAPA_REGISTRADO);
				doc.setColorSemaforoMesaPartes(Constantes.SEMAFORO_REGISTRO_ROJO.get(Constantes.ETAPA_REGISTRADO));
				if (doc.getFichaDocumento().getListDeriva() != null) {
					for (Deriva deriva : doc.getFichaDocumento().getListDeriva()) {
						if (!Constantes.RECIBIDO_FISICO.equals(deriva.getDirigido())) {
							if (Constantes.ESTADO_DOCUMENTO_ENVIADO.equals(deriva.getEstado().getId())
									|| Constantes.ESTADO_LEIDO.equals(deriva.getEstado().getId())) {
								doc.setEtapaMesaPartes(Constantes.ETAPA_EN_BANDEJA_DE_ENVIO);
								doc.setColorSemaforoMesaPartes(
										Constantes.SEMAFORO_REGISTRO_ROJO.get(Constantes.ETAPA_EN_BANDEJA_DE_ENVIO));
							}
						}
					}
				} else {
					if (Constantes.ESTADO_DOCUMENTO_ENVIADO.equals(doc.getEstado().getId())
							|| Constantes.ESTADO_LEIDO.equals(doc.getEstado().getId())) {
						doc.setEtapaMesaPartes(Constantes.ETAPA_EN_BANDEJA_DE_ENVIO);
						doc.setColorSemaforoMesaPartes(Constantes.SEMAFORO_REGISTRO_ROJO.get(Constantes.ETAPA_EN_BANDEJA_DE_ENVIO));
					}
				}
			}
		}
		doc.setEtapaDesMesaPartes(Constantes.ETAPA_ENVIO_MESA_PARTES.get(doc.getEtapaMesaPartes()));
		return doc;
	}

	private String bucarCanalEnvioXCentroCosto(String centroCostoId) throws Exception {
		List<MpCanalAsociadoEnvio> canalEnvios = canalAsociadoEnvioService.getCanalAsociado("", centroCostoId.trim(), "");
		MpCanalAsociadoEnvio canalEnvio = canalEnvios != null && canalEnvios.size() > 0 ? canalEnvios.get(0) : null;
		String canal = canalEnvio != null ? canalEnvio.getIndCanal() : Constantes.ENVIO_PARA_CASILLERO.toString();
		return canal;
	}

	@Override
	public Object getDerivaIdByFichaIdCC(Usuario usuario, String fichaDocumentoId) throws Exception {
		// TODO Auto-generated method stub
		Integer idDocumento = Integer.parseInt(fichaDocumentoId);

		List<Deriva> lsDeriva = derivaDao.getDerivadosByCC(idDocumento,
				usuario.getEmpleado().getCentroCosto().getId().trim());
		for (Deriva d : lsDeriva) {
			if (d.isEsResponsable()) {
				return d;
			}
		}
		return null;
	}

	@Override
	public Map<String, Object> getFichaDerivaEstafeta(Optional<String> tipoRegistro,
			Optional<String> centroCostoDestinoId, Optional<String> empleadoDestinoId, Optional<String> id,
			Optional<String> numeroDoc, Optional<String> numeroMp, Optional<String> asunto,
			Optional<String> centroCostoOrigenId, Optional<String> empleadoOrigenId, Optional<String> fechaIniCrea,
			Optional<String> fechaFinCrea, Optional<String> tipoDocumento, Optional<String> tipoEstado, Optional<String> pag,
			Optional<String> pagLength, Optional<String> remitidoDes) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		FichaConsultas fc = new FichaConsultas();
		fc.setNumeroMp(numeroMp.get());
		fc.setTipoDoc(tipoDocumento.get());
		fc.setNumeroDoc(numeroDoc.get());
		fc.setId(id.get());
		fc.setSumilla(asunto.get());
		fc.setFechaIni(fechaIniCrea.get());
		fc.setFechaFin(fechaFinCrea.get());
		fc.setDependenciaDestinoId(centroCostoDestinoId.get());
		fc.setEmpleadoDestinoId(empleadoDestinoId.get());
		fc.setRegistradoPor(empleadoOrigenId.get());
		fc.setRemitidoPor(remitidoDes.get());
		fc.setDependenciaOrigenId(centroCostoOrigenId.get());
		fc.setTipoRegistro(Constantes.DOCUMENTO_RECIBIDO);
		fc.setIndBusqueda(Constantes.CLASE_DEVIRA_UTIL);
		List<InputSelectUtil> listDependencias = new ArrayList<>();
		AnioLegislativo anioActual = anioLegislativoService.getAnioActual();
		Map parametros = new HashMap<String, Object>();
		parametros.put("ppini", anioActual.getCodigo());
		parametros.put("ppfin", anioActual.getCodigo().intValue() + 1);
		result = derivaDao.getFichaDerivaEstafeta(fc, pag, pagLength, listDependencias, parametros);
		return result;
	}

}
