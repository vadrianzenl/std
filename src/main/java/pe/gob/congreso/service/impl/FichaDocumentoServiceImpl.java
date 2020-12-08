package pe.gob.congreso.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.MimeMessage.RecipientType;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codemonkey.simplejavamail.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.util.SimpleFileResolver;
import pe.gob.congreso.crypto.Hex;
import pe.gob.congreso.dao.AdjuntoDao;
import pe.gob.congreso.dao.AnioLegislativoDao;
import pe.gob.congreso.dao.DerivaDao;
import pe.gob.congreso.dao.EmpleadoDao;
import pe.gob.congreso.dao.EnviadoExternoDao;
import pe.gob.congreso.dao.FichaDocumentoDao;
import pe.gob.congreso.dao.FichaProveidoDao;
import pe.gob.congreso.dao.FichaSubcategoriaDao;
import pe.gob.congreso.dao.FisicoDao;
import pe.gob.congreso.dao.NotasDao;
import pe.gob.congreso.dao.NotificacionEmpleadoDao;
import pe.gob.congreso.dao.TipoDao;
import pe.gob.congreso.dao.TipoDocumentoDao;
import pe.gob.congreso.dao.UsuarioDao;
import pe.gob.congreso.model.Adjunto;
import pe.gob.congreso.model.Bitacora;
import pe.gob.congreso.model.Deriva;
import pe.gob.congreso.model.Empleado;
import pe.gob.congreso.model.EnviadoExterno;
import pe.gob.congreso.model.FichaDocumento;
import pe.gob.congreso.model.FichaProveido;
import pe.gob.congreso.model.FichaSubcategoria;
import pe.gob.congreso.model.Notas;
import pe.gob.congreso.model.NotificacionEmpleado;
import pe.gob.congreso.model.SeguimientoFisico;
import pe.gob.congreso.model.SpSgdValidaDespacho;
import pe.gob.congreso.model.Tipo;
import pe.gob.congreso.model.TipoDocumento;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.model.util.EnviadoUtil;
import pe.gob.congreso.model.util.FichaUtil;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.service.ActividadService;
import pe.gob.congreso.service.BitacoraService;
import pe.gob.congreso.service.DerivaService;
import pe.gob.congreso.service.FichaDocumentoService;
import pe.gob.congreso.util.Constantes;
import pe.gob.congreso.util.EmailHelper;
import pe.gob.congreso.util.FichaConsultas;
import pe.gob.congreso.util.ReportHelper;
import pe.gob.congreso.util.ReportType;
import pe.gob.congreso.vo.SpSgdValidaDespachoVO;

@Service("fichaDocumentoService")
@PropertySource("classpath:application.properties")
@Transactional
public class FichaDocumentoServiceImpl implements FichaDocumentoService {

	private static final Integer TIPO_ENVIO_FINAL_ID = 144;
	private static final Integer TIPO_ENVIO_PARCIAL_ID = 145;

	@Autowired
	FichaDocumentoDao fichaDocumentoDao;

	@Autowired
	FichaSubcategoriaDao fichaSubCategoriaDao;

	@Autowired
	FichaProveidoDao fichaProveidoDao;

	@Autowired
	EmpleadoDao empleadoDao;

	@Autowired
	DerivaDao derivaDao;

	@Autowired
	TipoDao tipoDao;

	@Autowired
	ActividadService actividadService;

	@Autowired
	BitacoraService bitacoraService;

	@Autowired
	DerivaService derivaService;

	@Autowired
	UsuarioDao usuarioDao;

	@Autowired
	TipoDocumentoDao tipoDocumentoDao;

	@Autowired
	EnviadoExternoDao enviadoExternoDao;

	@Autowired
	AdjuntoDao adjuntoDao;

	@Autowired
	AnioLegislativoDao anioLegislativoDao;

	@Autowired
	NotificacionEmpleadoDao notificacionEmpleadoDao;

	@Autowired
	FisicoDao fisicoDao;

	@Autowired
	private Environment env;

	@Autowired
	NotasDao notasDao;

	private final ReportHelper reportHelper = new ReportHelper();

	protected final Log log = LogFactory.getLog(getClass());

	private final Hex hex = new Hex();

	private final EmailHelper emailHelper = new EmailHelper();

	@Override
	public Map<String, Object> findBy(Usuario usuario, Optional<String> tipoRegistro, Optional<String> empleadoId,
			Optional<String> id, Optional<String> numeroDoc, Optional<String> asunto, Optional<String> fechaIniCrea,
			Optional<String> fechaFinCrea, Optional<String> tipoDocumento, Optional<String> tipoEstado,
			Optional<String> centroCosto, Optional<String> privado, Optional<String> indTipRep,
			Optional<String> remitidoDes, Optional<String> texto, Optional<String> referencia,
			Optional<String> observaciones, Boolean indMP, Optional<String> pag, Optional<String> pagLength,
			Optional<String> numeroMp, Optional<String> dependenciaId, Optional<String> anioLegislativo)
			throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result = fichaDocumentoDao.findBy(tipoRegistro, empleadoId, id, numeroDoc, asunto, fechaIniCrea, fechaFinCrea,
				tipoDocumento, tipoEstado, centroCosto, privado, indTipRep, remitidoDes, texto, referencia,
				observaciones, pag, pagLength, numeroMp, dependenciaId, anioLegislativo);

		if (Optional.fromNullable(result).isPresent() && Optional.fromNullable(pag).isPresent()
				&& Optional.fromNullable(pagLength).isPresent()) {
			List<FichaUtil> documentos = (List<FichaUtil>) result.get("documentos");
			if (documentos.size() > 0) {
				for (FichaUtil doc : documentos) {
					// Inicio - Mesa de Partes
					if (indMP.booleanValue()) {
						Optional<String> documentoId = Optional.of(doc.getId().toString());
						Optional<String> proveidoId = Optional.of(Constantes.ID_PROVEIDO_MESA_DE_PARTES.toString());
						List<FichaProveido> lfp = (List<FichaProveido>) fichaProveidoDao.findByIdRU(documentoId,
								proveidoId);
						FichaProveido fp = lfp != null && lfp.size() > 0 ? lfp.get(0) : null;
						if (fp != null) {
							doc.setNumeroProveido(fp.getNumero());
							doc.setSumillaProveido(fp.getSumilla());
						}
					}
					// Fin - Mesa de Partes
					List<EnviadoUtil> enviados = derivaDao.findEnviados(doc.getId(), Constantes.NO_ES_MESA_DE_PARTES);
					// List<EnviadoUtil> enviados = null;
					if (Optional.fromNullable(enviados).isPresent()) {
						if (enviados.size() > 0) {
							doc.setEnviados(enviados);
						}
					}

					// *Obtenemos el numero de adjuntos*//
					List<Adjunto> adjuntos = adjuntoDao.findAdjuntos(String.valueOf(doc.getId()));
					// List<Adjunto> adjuntos = null;
					if (Optional.fromNullable(adjuntos).isPresent()) {
						if (adjuntos.size() > 0) {
							doc.setAdjuntos(adjuntos.size());
						}
					}

					// *Obtenemos el número de notas*//

					List<Notas> lstNotas = null;
					if (Constantes.PERFIL_USUARIO_DEL_STD.equals(usuario.getPerfil().getId().trim())) {
						lstNotas = notasDao.findNotas(doc.getId().toString(),
								usuario.getEmpleado().getCentroCosto().getId().trim(),
								usuario.getNombreUsuario().trim());
					} else if (Constantes.PERFIL_RESPONSABLE_DEL_AREA.equals(usuario.getPerfil().getId().trim())) {
						lstNotas = notasDao.findNotas(doc.getId().toString(),
								usuario.getEmpleado().getCentroCosto().getId().trim(), Constantes.VACIO);
					}

					if (lstNotas != null) {
						if (lstNotas.size() > 0) {
							doc.setNotas(true);
						} else {
							doc.setNotas(false);
						}
					}

					int recibiConforme = 0;
					if (Optional.fromNullable(doc.getId()).isPresent()) {
						List<SeguimientoFisico> ls = fisicoDao.findRecibiFisicoFichaId(doc.getId().toString(),
								Constantes.ACTIVO_RECIBI_CONFORME);
						if (ls != null) {
							if (ls.size() > 0) {
								recibiConforme = ls.size();
							}
						}
					}
					doc.setRecibiConforme(recibiConforme);

					String remitente = "";
					switch (doc.getTipoRegistro().getId()) {
					case 1:
						remitente = doc.getCentroCosto();
						doc.setRemitente(remitente);
						break;
					case 2:
						EnviadoExterno ee = enviadoExternoDao.findEnviadoPor(String.valueOf(doc.getId()));
						// EnviadoExterno ee = null;
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
						doc.setRemitente(remitente);
						break;
					default:
						doc.setRemitente(remitente);
						break;
					}

				}
			}
		}

		return result;
	}

	@Override
	public FichaDocumento getFichaDocumentoId(Integer id) throws Exception {
		return fichaDocumentoDao.getFichaDocumentoId(id);
	}

	@Override
	public List<FichaSubcategoria> findSubCategorias(String fichaDocumentoId) throws Exception {
		return fichaSubCategoriaDao.findSubCategorias(fichaDocumentoId);
	}

	@Override
	public Map<String, Object> getFichaDocumentoAnioLegislativo(String id) throws Exception {
		return fichaDocumentoDao.getFichaDocumentoAnioLegislativo(id);
	}

	@Override
	public Map<String, Object> findFichasCategoria(Optional<String> categoriaId, Optional<String> codigo)
			throws Exception {
		return fichaSubCategoriaDao.findFichasCategoria(categoriaId, codigo);
	}

	public Map<String, Object> getFichaDocumentoSubcategoria(Optional<String> subCategoriaId, Optional<String> codigo)
			throws Exception {
		return fichaSubCategoriaDao.find(subCategoriaId, codigo);
	}

	@Override
	public Map<String, Object> findLegislatura(Optional<String> subCategoriaId, Optional<String> codigo)
			throws Exception {
		return fichaSubCategoriaDao.findLegislatura(subCategoriaId, codigo);
	}

	@Override
	public List<FichaProveido> getFichaDocumentoProveidoAnioLegislativo(Optional<String> proveidoId,
			Optional<String> codigo) throws Exception {
		return fichaProveidoDao.find(proveidoId, codigo);
	}

	@Override
	public List<FichaDocumento> getFichaDocumentoEmpleado(String empleadoId) throws Exception {
		return fichaDocumentoDao.getFichaDocumentoEmpleado(empleadoId);
	}

	@Override
	public Object create(Usuario usuario, FichaDocumento fd, String operacion, List<String> listaCambios)
			throws Exception {
		log.info("create()");
		Integer id = fd.getId();
		if (operacion.trim().equals("EDITAR")) {
			operacion = "ACTUALIZAR";
		}
		fichaDocumentoDao.create(fd);
		log.info("FichaId: " + fd.getId());
		log.debug("Creando Ficha Documento");
		if (!Optional.fromNullable(id).isPresent()) {
			Bitacora b = new Bitacora();
			b.setFecha(new Date());
			b.setEmpleado(fd.getEmpleado());
			b.setCentroCosto(fd.getCentroCosto());
			b.setDescripcion("CREÓ EL DOCUMENTO");
			b.setIndicaciones("CREACIÓN CORRECTA");
			b.setEstado(fd.getTipoEstado());
			b.setFichaDocumento(fd);

			log.debug("bitacora" + b.toString());
			bitacoraService.create(usuario, b, operacion);
		}
		log.debug("Ficha Documento = " + fd.toString());
		actividadService.create(usuario, fd, operacion);

		if (listaCambios.size() > 0) {
			log.debug("Ficha Documento Tiene Cambios");
			Bitacora bitacora = new Bitacora();
			bitacora.setDescripcion("MODIFICÓ EL DOCUMENTO");
			StringBuffer cambios = new StringBuffer();
			for (String cambio : listaCambios) {
				cambios.append(cambio.toUpperCase());
				cambios.append(", ");
			}
			bitacora.setIndicaciones("MODIFICÓ EL/LOS CAMPO(S): " + cambios.substring(0, cambios.length() - 2));
			bitacora.setEmpleado(usuario.getEmpleado());
			bitacora.setCentroCosto(fd.getCentroCosto());
			bitacora.setFecha(new Date());
			bitacora.setEstado(fd.getTipoEstado());
			bitacora.setFichaDocumento(fd);
			bitacora.setModificado(true);

			// log.debug("bitacora" + bitacora.toString()); No descomente esta linea Caused
			// by: java.lang.StackOverflowError
			bitacoraService.create(usuario, bitacora, operacion);

			try {
				enviarCorreoModificados(usuario, listaCambios, fd.getId());
			} catch (Exception e) {
				log.error("Error al momento de enviar el correo de modificaciones - " + e);
			}
		}

		if (Optional.fromNullable(fd.getTipoEstado().getId()).isPresent()) {
			if (fd.getTipoEstado().getId() == 11) {
				Bitacora b = new Bitacora();
				b.setFecha(new Date());
				b.setEmpleado(fd.getEmpleado());
				b.setCentroCosto(fd.getCentroCosto());
				b.setDescripcion("ANULÓ EL DOCUMENTO");
				b.setIndicaciones("ANULACIÓN CORRECTA");
				b.setEstado(fd.getTipoEstado());
				b.setFichaDocumento(fd);

				bitacoraService.create(usuario, b, operacion);
			}
		}

		return fd;
	}

	private void enviarCorreoModificados(Usuario usuario, List<String> listaCampos, Integer idFicha) throws Exception {
		log.debug("Envía Correo");
		if (listaCampos.size() > 0) {
			List<Deriva> listDerivadosFinales = derivaDao.getEnviadosLessParciales(idFicha);
			if (listDerivadosFinales.size() > 0) {
				log.info("Existen Derivados Finales");
			} else {
				return;
			}

			for (Deriva der : listDerivadosFinales) {
				// Envío de Email
				log.info("Ingresa al al envío de correo");
				if (Optional.fromNullable(der.getEmpleadoDestino()).isPresent()
						&& Optional.fromNullable(der.getEmpleadoDestino().getEmail()).isPresent()) {
					if (Optional.fromNullable(der.getEmpleadoDestino().getUsuario().getPerfil()).isPresent()) {
						NotificacionEmpleado ne = notificacionEmpleadoDao.findBy(der.getEmpleadoDestino().getId(), 2);
						if (Optional.fromNullable(ne).isPresent()) {
							if (!der.getEmpleadoDestino().getEmail().equals("")) {
								log.info("Tiene correo");
								Tipo cuentaEmail = tipoDao.findByNombre("EMAIL_CUENTA");
								Tipo passwordEmail = tipoDao.findByNombre("EMAIL_PASSWORD");
								Tipo user = tipoDao.findByNombre("EMAIL_USER");
								Tipo nombreEnvio = tipoDao.findByNombre("EMAIL_NOMBRE_ENVIO");
								Tipo urlStd = tipoDao.findByNombre("URL_STD");

								try {
									final Email email = new Email();
									email.setFromAddress(nombreEnvio.getDescripcion().trim(),
											cuentaEmail.getDescripcion().trim());

									email.addRecipient(
											der.getEmpleadoDestino().getApellidos().trim() + ", "
													+ der.getEmpleadoDestino().getNombres().trim(),
											der.getEmpleadoDestino().getEmail().trim(), RecipientType.TO);

									email.setSubject(
											"[STD] Modificación de los datos del Documento con RU : " + idFicha);
									email.setText("");

									String body = "<font face=\"Calibri\" size=\"3\" color=\"#1F497D\"><p><b>Estimado Usuario (a):</b><br/>";
									body = body + der.getEmpleadoDestino().getNombres().trim() + " "
											+ der.getEmpleadoDestino().getApellidos().trim() + "</p>";

									body = body + "<p>Nos dirigimos a Usted, para informarle que el Usuario "
											+ usuario.getNombres() + " " + usuario.getApellidos()
											+ " ha modificado los siguientes datos en el documento con Registro Único "
											+ idFicha + ":</p>";

									body = body + "<p>";

									for (String campo : listaCampos) {
										body = body + "-" + campo + "<br/>";
									}
									body = body + "</p>";

									body = body + "<p>Para Ingresar al Sistema, use el siguiente enlace : "
											+ urlStd.getDescripcion().trim() + "</p>";
									body = body
											+ "<p>Si requiere apoyo técnico en el Sistema de Trámite Documentario, favor llamar al anexo: 6200.</p>";
									body = body + "<p>Atentamente,<br/>";
									// body = body + "SISTEMA DE TRÁMITE DOCUMENTARIO<br/>";
									body = body + "Oficina de Tecnologías de la Información</p><br/></font>";

									email.setTextHTML(body);

									log.info("Previo al envío");
									emailHelper.send(email, user.getDescripcion().trim(),
											hex.decodeSiga(passwordEmail.getDescripcion().trim()));
									log.info("Luego del envío");
								} catch (Exception ex) {
									log.info(" " + ex);
								}

							}
						}

					}
				}
			}
		}
	}

	private List<String> compareFichas(FichaDocumento antigua, FichaDocumento nueva) {
		List<String> result = new ArrayList<String>();
		// Campos Obligatorios, se omite la validación de Null Pointer
		if (antigua.getTipoRegistro().getId().compareTo(nueva.getTipoRegistro().getId()) != 0) {
			result.add("Tipo de Registro");
		}
		if (antigua.getTipoDocumento().getId().compareTo(nueva.getTipoDocumento().getId()) != 0) {
			result.add("Tipo de Documento");
		}
		if (!antigua.getNumeroDoc().trim().equals(nueva.getNumeroDoc().trim())) {
			result.add("Número de Documento");
		}
		if (!antigua.getAsunto().trim().equals(nueva.getAsunto().trim())) {
			log.info("Asuntos Diferentes");
			result.add("Asunto");
		}
		// Campos No Obligatorios, se realiza la validación de Null Pointer
		// Valida Motivo
		boolean flagMotivo = false;
		if (Optional.fromNullable(antigua.getMotivo()).isPresent()
				&& Optional.fromNullable(nueva.getMotivo()).isPresent()) {
			if (!antigua.getMotivo().getId().equals(nueva.getMotivo().getId())) {
				flagMotivo = true;
			}
		}
		if (Optional.fromNullable(antigua.getMotivo()).isPresent()
				&& !Optional.fromNullable(nueva.getMotivo()).isPresent()) {
			flagMotivo = true;
		}
		if (!Optional.fromNullable(antigua.getMotivo()).isPresent()
				&& Optional.fromNullable(nueva.getMotivo()).isPresent()) {
			flagMotivo = true;
		}
		if (flagMotivo) {
			result.add("Motivo");
		}
		// Numero de Folios
		boolean flagNumFolios = false;
		if (Optional.fromNullable(antigua.getNumeroFolios()).isPresent()
				&& Optional.fromNullable(nueva.getNumeroFolios()).isPresent()) {
			if (!antigua.getNumeroFolios().equals(nueva.getNumeroFolios())) {
				flagNumFolios = true;
			}
		}
		if (Optional.fromNullable(antigua.getNumeroFolios()).isPresent()
				&& !Optional.fromNullable(nueva.getNumeroFolios()).isPresent()) {
			flagNumFolios = true;
		}
		if (!Optional.fromNullable(antigua.getNumeroFolios()).isPresent()
				&& Optional.fromNullable(nueva.getNumeroFolios()).isPresent()) {
			flagNumFolios = true;
		}
		if (flagNumFolios) {
			result.add("Número de Folios");
		}

		// Referencia
		boolean flagReferencia = false;
		if (Optional.fromNullable(antigua.getReferencia()).isPresent()
				&& Optional.fromNullable(nueva.getReferencia()).isPresent()) {
			if (!antigua.getReferencia().trim().equals(nueva.getReferencia().trim())) {
				flagReferencia = true;
			}
		}
		if (Optional.fromNullable(antigua.getReferencia()).isPresent()
				&& !Optional.fromNullable(nueva.getReferencia()).isPresent()) {
			flagReferencia = true;
		}
		if (!Optional.fromNullable(antigua.getReferencia()).isPresent()
				&& Optional.fromNullable(nueva.getReferencia()).isPresent()) {
			flagReferencia = true;
		}
		if (flagReferencia) {
			result.add("Referencia");
		}

		// Observaciones
		boolean flagObservaciones = false;
		if (Optional.fromNullable(antigua.getObservaciones()).isPresent()
				&& Optional.fromNullable(nueva.getObservaciones()).isPresent()) {
			if (!antigua.getObservaciones().trim().equals(nueva.getObservaciones().trim())) {
				flagObservaciones = true;
			}
		}

		if (Optional.fromNullable(antigua.getObservaciones()).isPresent()
				&& !Optional.fromNullable(nueva.getObservaciones()).isPresent()) {
			flagObservaciones = true;
		}
		if (!Optional.fromNullable(antigua.getObservaciones()).isPresent()
				&& Optional.fromNullable(nueva.getObservaciones()).isPresent()) {
			flagObservaciones = true;
		}
		if (flagObservaciones) {
			result.add("Observaciones");
		}

		// Fecha Documento
		boolean flagFechaDocumento = false;
		if (Optional.fromNullable(antigua.getFechaDocumento()).isPresent()
				&& Optional.fromNullable(nueva.getFechaDocumento()).isPresent()) {
			Calendar calAntigua = Calendar.getInstance();
			calAntigua.setTime(antigua.getFechaDocumento());
			Calendar calNueva = Calendar.getInstance();
			calNueva.setTime(nueva.getFechaDocumento());

			if ((calAntigua.get(Calendar.YEAR) != calNueva.get(Calendar.YEAR))
					|| (calAntigua.get(Calendar.MONTH) != calNueva.get(Calendar.MONTH))
					|| (calAntigua.get(Calendar.DAY_OF_MONTH) != calNueva.get(Calendar.DAY_OF_MONTH))) {
				flagFechaDocumento = true;
			}

			// if (!antigua.getFechaDocumento().equals(nueva.getFechaDocumento())) {
			// flagFechaDocumento = true;
			// }
		}
		if (Optional.fromNullable(antigua.getFechaDocumento()).isPresent()
				&& !Optional.fromNullable(nueva.getFechaDocumento()).isPresent()) {
			flagFechaDocumento = true;
		}
		if (!Optional.fromNullable(antigua.getFechaDocumento()).isPresent()
				&& Optional.fromNullable(nueva.getFechaDocumento()).isPresent()) {
			flagFechaDocumento = true;
		}
		if (flagFechaDocumento) {
			result.add("Fecha de Documento");
		}

		return result;
	}

	@Override
	public Object validarFinaliza(String id) throws Exception {
		Integer code = 0;
		String response = "";
		Integer idDocumento = Integer.parseInt(id);
		Boolean isCerrado = true;

		List<Deriva> list = derivaDao.findDerivados(String.valueOf(idDocumento));

		for (Deriva der : list) {
			if (!der.isCcopia()) {
				if (der.getEstado().getId() != 7 && der.getEstado().getId() != 21 && der.getEstado().getId() != 22
						&& der.getEnviadoExterno() == null && der.getDirigido() == 1) {
					isCerrado = false;
				}
			}
		}

		if (isCerrado) {
			code = 200;
			response = "OK";
		} else {
			code = 400;
			response = "ERROR";
		}
		return new Status(code, response);

	}

	@Override
	public Object finalizar(Usuario usuario, FichaDocumento fd, String operacion, String motivo) throws Exception {
		FichaDocumento f = null;
		actividadService.create(usuario, fd, operacion);
		f = fichaDocumentoDao.create(fd);

		if (fd.getTipoRegistro().getId().equals(2)) {
			derivaDao.updateRecibidoFisico(fd);
		}

		Bitacora b = new Bitacora();
		b.setFecha(new Date());
		b.setEmpleado(usuario.getEmpleado());
		b.setCentroCosto(usuario.getEmpleado().getCentroCosto());
		b.setDescripcion("ATENDIÓ EL DOCUMENTO");
		b.setIndicaciones(motivo);
		b.setEstado(f.getTipoEstado());
		b.setFichaDocumento(f);

		bitacoraService.create(usuario, b, operacion);

		return f;
	}

	@Override
	public Object createFichaProveido(Usuario usuario, FichaProveido fp, String operacion) throws Exception {
		actividadService.create(usuario, fp, operacion);
		fp.setUsuarioCrea(usuario.getNombreUsuario());
		fp.setFechaCrea(new Date());
		return fichaProveidoDao.create(fp);
	}

	@Override
	public Object createDirigido(Usuario usuario, FichaSubcategoria fs, String operacion) throws Exception {
		actividadService.create(usuario, fs, operacion);
		return fichaSubCategoriaDao.create(fs);
	}

	@Override
	public Map<String, Object> getFichaDocumentoEstafeta(Optional<String> tipoRegistro,
			Optional<String> centroCostoDestinoId, Optional<String> empleadoDestinoId, Optional<String> id,
			Optional<String> numeroDoc, Optional<String> numeroMp, Optional<String> asunto,
			Optional<String> centroCostoOrigenId, Optional<String> empleadoOrigenId, Optional<String> fechaIniCrea,
			Optional<String> fechaFinCrea, Optional<String> tipoDocumento, Optional<String> tipoEstado,
			Optional<String> pag, Optional<String> pagLength) throws Exception {
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
		fc.setDependenciaOrigenId(centroCostoOrigenId.get());
		fc.setTipoRegistro(Constantes.DOCUMENTO_RECIBIDO);
		fc.setIndBusqueda(Constantes.CLASE_DEVIRA_UTIL);
		List<InputSelectUtil> listDependencias = new ArrayList<>();
		result = fichaDocumentoDao.getFichaDocumentoEstafeta(fc, pag, pagLength, listDependencias);

		return result;
	}

	@Override
	public Object reporteEnviados(HttpServletRequest request, String usuario, String ru, String numeroDoc,
			String asunto, String tipoDoc, String estado, String elaboradoPor, String fechaInicio, String fechaFin,
			String indtipo, String tipoReg, String observaciones, String referencia, String remitido,
			String anioLegislativo, ReportType.FILE fileType) throws Exception {
		String centroCosto = "0";
		String centroCostoDes = "";
		String tipoDocumentoDes = "---";
		String tipoRegistroDes = "---";
		String elaboradoPorDes = "---";
		String estadoDes = "---";

		if (Optional.fromNullable(usuario).isPresent()) {
			Optional<Usuario> usu = usuarioDao.findByNameUsuario(String.valueOf(usuario));

			if (usu.isPresent()) {
				centroCosto = usu.get().getEmpleado().getCentroCosto().getId();
				centroCostoDes = usu.get().getEmpleado().getCentroCosto().getDescripcion();
			}
		}

		if (!tipoDoc.equals("0")) {
			TipoDocumento tip = tipoDocumentoDao.getTipoDocumentoId(Integer.valueOf(tipoDoc));
			if (Optional.fromNullable(tip).isPresent()) {
				tipoDocumentoDes = tip.getTipo().getDescripcion();
			}
		}

		if (!tipoReg.equals("0")) {
			switch (tipoReg) {
			case "1":
				tipoRegistroDes = "DOCUMENTO ENVIADO";
				break;
			case "2":
				tipoRegistroDes = "DOCUMENTO RECIBIDO";
				break;
			default:
				break;
			}
		}

		if (!elaboradoPor.equals("0")) {
			Empleado e = empleadoDao.findById(Integer.valueOf(elaboradoPor));
			if (Optional.fromNullable(e).isPresent()) {
				elaboradoPorDes = e.getDescripcion();
			}
		}

		if (!estado.equals("0")) {
			Tipo tip = tipoDao.findByCodigo(estado);
			if (Optional.fromNullable(tip).isPresent()) {
				estadoDes = tip.getDescripcion();
			}
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
		map.put("usuario", usuario);
		map.put("ru", Integer.valueOf(ru));
		map.put("numeroDoc", numeroDoc);
		map.put("asunto", asunto);
		map.put("tipoDoc", tipoDoc);
		map.put("estado", estado);
		map.put("elaboradoPor", Integer.valueOf(elaboradoPor));
		map.put("centroCosto", centroCosto);
		map.put("fechaInicio", fechaInicio);
		map.put("fechaFin", fechaFin);
		map.put("centroCostoDes", centroCostoDes);
		map.put("tipoDocDes", tipoDocumentoDes);
		map.put("elaboradoPorDes", elaboradoPorDes);
		map.put("estadoDes", estadoDes);
		map.put("indTipo", Integer.valueOf(indtipo));
		map.put("tipoReg", tipoReg);
		map.put("tipoRegDes", tipoRegistroDes);
		map.put("observaciones", observaciones);
		map.put("referencia", referencia);
		map.put("remitido", remitido);
		map.put("anioLegislativo", anioLegislativo);
		map.put(JRParameter.REPORT_LOCALE, Locale.US);
		map.put(JRParameter.REPORT_FILE_RESOLVER, new SimpleFileResolver(reportsDir));

		String path = request.getSession().getServletContext().getRealPath("/assets/rptEnviados.jrxml");

		ByteArrayOutputStream baos = reportHelper.generateDataSource(path, map, fileType, ReportType.DATASOURCE.WITH);
		byte[] bytes = baos.toByteArray();
		return bytes;
	}

	@Override
	public Object reporteRecibidos(HttpServletRequest request, String usuario, String ru, String numeroDoc,
			String asunto, String tipoDoc, String estado, String elaboradoPor, String fechaInicio, String fechaFin,
			String observaciones, String referencia, String remitido, String anioLegislativo, String dependenciaId,
			String esResponsable, String rc, ReportType.FILE fileType) throws Exception {
		String centroCosto = "0";
		String centroCostoDes = "";
		String tipoDocumentoDes = "---";
		String elaboradoPorDes = "---";
		String estadoDes = "---";

		if (Optional.fromNullable(usuario).isPresent()) {
			Optional<Usuario> usu = usuarioDao.findByNameUsuario(String.valueOf(usuario));

			if (usu.isPresent()) {
				centroCosto = usu.get().getEmpleado().getCentroCosto().getId();
				centroCostoDes = usu.get().getEmpleado().getCentroCosto().getDescripcion();
			}
		}

		if (!tipoDoc.equals("0")) {
			TipoDocumento tip = tipoDocumentoDao.getTipoDocumentoId(Integer.valueOf(tipoDoc));
			if (Optional.fromNullable(tip).isPresent()) {
				tipoDocumentoDes = tip.getTipo().getDescripcion();
			}
		}

		if (!elaboradoPor.equals("0")) {
			Empleado e = empleadoDao.findById(Integer.valueOf(elaboradoPor));
			if (Optional.fromNullable(e).isPresent()) {
				elaboradoPorDes = e.getDescripcion();
			}
		}

		if (!estado.equals("-1")) {
			if (estado.equals("0")) {
				estadoDes = "PENDIENTES";
			} else {
				Tipo tip = tipoDao.findByCodigo(estado);
				if (Optional.fromNullable(tip).isPresent()) {
					estadoDes = tip.getDescripcion();
				}
			}
		}

		File reportsDir = new File(request.getSession().getServletContext().getRealPath("/assets/"));
		if (!reportsDir.exists()) {
			try {
				throw new FileNotFoundException(String.valueOf(reportsDir));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		System.out.println("valor RC antes : " + rc);
		if (Optional.fromNullable(rc).isPresent()) {
			if (rc == "") {
				rc = "0";
			}
		} else {
			rc = "0";
		}
		System.out.println("valor RC despues : " + rc);

		// Agregado AEP - 29.08.2019
		if (fechaInicio.equals(fechaFin)) {
			fechaFin = "0";
		}

		// log.info("PDF -> fechaInicio: " + fechaInicio);
		if (!fechaInicio.equals("0")) {
			fechaInicio = fechaInicio.replace('T', ' ').replace('P', ':');
			log.info("PDF -> fechaInicio: " + fechaInicio);
		}

		// log.info("PDF -> fechaFin: " + fechaFin);
		if (!fechaFin.equals("0")) {
			fechaFin = fechaFin.replace('T', ' ').replace('P', ':').substring(0, 17) + "59";
			log.info("PDF -> fechaFin: " + fechaFin);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("usuario", usuario);
		map.put("ru", Integer.valueOf(ru));
		map.put("numeroDoc", numeroDoc);
		map.put("asunto", asunto);
		map.put("tipoDoc", tipoDoc);
		map.put("estado", estado);
		map.put("elaboradoPor", Integer.valueOf(elaboradoPor));
		map.put("centroCosto", centroCosto);
		map.put("fechaInicio", fechaInicio);
		map.put("fechaFin", fechaFin);
		map.put("centroCostoDes", centroCostoDes);
		map.put("tipoDocDes", tipoDocumentoDes);
		map.put("elaboradoPorDes", elaboradoPorDes);
		map.put("estadoDes", estadoDes);
		map.put("observaciones", observaciones);
		map.put("referencia", referencia);
		map.put("remitido", remitido);
		map.put("anioLegislativo", anioLegislativo);
		map.put("dependenciaId", dependenciaId);
		map.put("esResponsable", esResponsable);
		map.put("rc", rc);
		map.put(JRParameter.REPORT_LOCALE, Locale.US);
		map.put(JRParameter.REPORT_FILE_RESOLVER, new SimpleFileResolver(reportsDir));

		String path = request.getSession().getServletContext().getRealPath("/assets/rptRecibidos.jrxml");

		// ByteArrayOutputStream baos = reportHelper.generateDataSource(path, map,
		// ReportType.FILE.PDF,
		ByteArrayOutputStream baos = reportHelper.generateDataSource(path, map, fileType, ReportType.DATASOURCE.WITH);
		byte[] bytes = baos.toByteArray();
		return bytes;
	}

	public List<String> obtenerCambiosFichaDocumento(FichaDocumento fd) throws Exception {
		log.info("Comparando Ficha Actual y Nueva");
		List<String> listaCampos = new ArrayList<String>();
		FichaDocumento fichaAntigua = this.getFichaDocumentoId(fd.getId());

		// Restablecemos los cambios de tipo envio que no corresponden
		restablecerCambiosTipoEnvio(fichaAntigua, fd);

		if (fichaAntigua.getTipoEstado().getId() == 8 || fichaAntigua.getTipoEstado().getId() == 9) {
			listaCampos = this.compareFichas(fichaAntigua, fd);
			log.info("Numero de Cambios");
			log.info(listaCampos.size());
			log.info("Campos Actualizados");
			log.info(listaCampos.toString());
		}
		return listaCampos;
	}

	private void restablecerCambiosTipoEnvio(FichaDocumento fichaActual, FichaDocumento fichaNueva) throws Exception {
		log.info("Comparando Tipo Envio");
		if (TIPO_ENVIO_FINAL_ID.equals(fichaActual.getTipoEnvio().getId())
				&& TIPO_ENVIO_PARCIAL_ID.equals(fichaNueva.getTipoEnvio().getId())
				&& (fichaActual.getTipoEstado().getId() == 8 || fichaActual.getTipoEstado().getId() == 9)) {
			fichaNueva.setTipoEnvio(fichaActual.getTipoEnvio());
			fichaNueva.setEnvioFinal(fichaActual.getEnvioFinal());
		}
	}

	@Override
	public FichaDocumento getFichaDocumentoId(Usuario usuario, Integer id) throws Exception {
		FichaDocumento fichaResult = null;
		FichaDocumento fi = fichaDocumentoDao.getFichaDocumentoId(id,
				usuario.getEmpleado().getCentroCosto().getId().trim());
		if (Optional.fromNullable(fi).isPresent()) {
			fichaResult = fi;
			return fichaResult;
		}

		List<Deriva> lista = derivaDao.getDerivadosByCC(id, usuario.getEmpleado().getCentroCosto().getId().trim());
		if (Optional.fromNullable(lista).isPresent()) {
			if (lista.size() > 0) {
				fichaResult = lista.get(0).getFichaDocumento();
				return fichaResult;
			}
		}

		return fichaResult;
	}

	public List<FichaDocumento> getListFichaDocumentosEnviadosMp(String centroCostoId, String empleadoId,
			String indEstafeta) throws Exception {
		return fichaDocumentoDao.getListFichaDocumentosEnviadosMp(centroCostoId, empleadoId, indEstafeta);
	}

	@Override
	public FichaDocumento getFichaDocumentoId(String centroCostoId, Integer id) throws Exception {
		// TODO Auto-generated method stub
		FichaDocumento fichaResult = null;
		FichaDocumento fi = fichaDocumentoDao.getFichaDocumentoId(id, centroCostoId);
		if (Optional.fromNullable(fi).isPresent()) {
			fichaResult = fi;
			return fichaResult;
		}

		List<Deriva> lista = derivaDao.getDerivadosByCC(id, centroCostoId);
		if (Optional.fromNullable(lista).isPresent()) {
			if (lista.size() > 0) {
				fichaResult = lista.get(0).getFichaDocumento();
				return fichaResult;
			}
		}

		return fichaResult;
	}

	@Override
	public SpSgdValidaDespachoVO validaparadespacho(Integer id) throws Exception {
		List<SpSgdValidaDespacho> list = fichaDocumentoDao.validaparadespacho(id);
		SpSgdValidaDespachoVO obj = new SpSgdValidaDespachoVO();
		if (list != null) {
			SpSgdValidaDespacho entity = list.get(0);
			obj = toSpSgdValidaDespachoVO(entity);
		}
		return obj;
	}

	private SpSgdValidaDespachoVO toSpSgdValidaDespachoVO(SpSgdValidaDespacho spSgdValidaDespacho) {
		SpSgdValidaDespachoVO spSgdValidaDespachoVO = new SpSgdValidaDespachoVO();
		spSgdValidaDespachoVO.setId(spSgdValidaDespacho.getId());
		spSgdValidaDespachoVO.setResultado(spSgdValidaDespacho.getResultado());
		
		return spSgdValidaDespachoVO;
	}

}
