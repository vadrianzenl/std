package pe.gob.congreso.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import pe.gob.congreso.dao.DerivaDao;
import pe.gob.congreso.dao.FichaDocumentoDao;
import pe.gob.congreso.dao.FisicoDao;
import pe.gob.congreso.dao.NotasDao;
import pe.gob.congreso.dao.TipoDao;
import pe.gob.congreso.model.Bitacora;
import pe.gob.congreso.model.Deriva;
import pe.gob.congreso.model.FichaDocumento;
import pe.gob.congreso.model.SeguimientoFisico;
import pe.gob.congreso.model.Tipo;
import pe.gob.congreso.model.Usuario;
import pe.gob.congreso.service.ActividadService;
import pe.gob.congreso.service.BitacoraService;
import pe.gob.congreso.service.FisicoService;
import pe.gob.congreso.util.Constantes;
import pe.gob.congreso.util.SeguimientoFisicoUtil;

@Service("fisicoService")
@Transactional
public class FisicoServiceImpl implements FisicoService {

	@Autowired
	DerivaDao derivaDao;

	@Autowired
	FisicoDao fisicoDao;

	@Autowired
	TipoDao tipoDao;

	@Autowired
	FichaDocumentoDao fichaDocumentoDao;

	@Autowired
	ActividadService actividadService;

	@Autowired
	NotasDao notasDao;

	@Autowired
	BitacoraService bitacoraService;

	protected final Log log = LogFactory.getLog(getClass());

	@Override
	public Object recibiConforme(Usuario usuario, SeguimientoFisico d, String operacion, String motivo, String tipo)
			throws Exception {
		// TODO Auto-generated method stub
		// log.info("FisicoServiceImpl :: /recibiConforme -> OPERACION: " + operacion +
		// ", MOTIVO: " + motivo + ", TIPO: " + tipo);
		actividadService.create(usuario, d, operacion);
		List<SeguimientoFisico> ls = new ArrayList<>();
		Integer idDocumento = d.getDeriva().getFichaDocumento().getId();
		List<Deriva> list = new ArrayList<>();
		if (tipo == null) {
			list = derivaDao.getDerivadosToValidar(idDocumento, d.getDeriva().getEmpleadoOrigen().getId(),
					d.getDeriva().getEmpleadoDestino().getCentroCosto().getId());
		} else {
			if (Constantes.PERFIL_RESPONSABLE_DEL_AREA.equals(usuario.getPerfil().getId().trim())) {
				// list =
				// derivaDao.getDerivadosByCCOrigen(Integer.parseInt(idDocumento.toString()),d.getDeriva().getEmpleadoOrigen()
				// .getCentroCosto().getId().toString().trim());
				list = derivaDao.getDerivadosByCC(Integer.parseInt(idDocumento.toString()),
						usuario.getEmpleado().getCentroCosto().getId().trim());
			} else {
				if (Constantes.PERFIL_RESPONSABLE_MESA_PARTES.equals(usuario.getPerfil().getId().trim())) {
					list = derivaDao.getDerivadosByCCOrigen(Integer.parseInt(idDocumento.toString()),
							d.getDeriva().getEmpleadoOrigen().getCentroCosto().getId().toString().trim());
				} else {
					list = derivaDao.getDerivadosToValidar(idDocumento, d.getDeriva().getEmpleadoOrigen().getId(),
							d.getDeriva().getCentroCostoDestino().getId().trim());
				}

			}

		}
		log.info("Lista de Derivados");
		log.info(list.size());
		SeguimientoFisico f;
		Date fechaActual = new Date();
		for (Deriva der : list) {

			f = new SeguimientoFisico();
			SeguimientoFisico fisico = new SeguimientoFisico();
			String estado = Constantes.ESTADO_RECIBIR_DOC_FISICO.get("01");
			fisico.setFechaCrea(fechaActual);
			fisico.setFechaRecep(d.getFechaRecep());
			fisico.setUsuarioCrea(usuario.getNombreUsuario().toString().trim());
			fisico.setHabilitado(Constantes.HABILITADO);
			fisico.setDerivaId(der.getId());
			fisico.setEstado(estado);
			fisico.setCentroCostoId(der.getCentroCostoDestino().getId().toString());
			d.setEstado(estado);
			if (Constantes.PERFIL_USUARIO_DEL_STD.equals(usuario.getPerfil().getId().trim())
					|| Constantes.PERFIL_ADMINISTRADOR_DEL_STD.equals(usuario.getPerfil().getId().trim())) {
				if (tipo == null) {
					if (der.isEsResponsable() && der.getId().equals(d.getDeriva().getId())) {
						f = fisicoDao.create(fisico);
						ls.add(f);
					}
				} else {
					f = fisicoDao.create(fisico);
					ls.add(f);
				}
			} else if (Constantes.PERFIL_RESPONSABLE_DEL_AREA.equals(usuario.getPerfil().getId().trim())
					&& der.getCentroCostoDestino().getId().trim().equals(usuario.getEmpleado().getCentroCosto().getId().trim())) {
				if (Constantes.RECIBIDO_USUARIO.equals(tipo.toString().trim())) {
					if (der.isEsResponsable() && der.getId().equals(d.getDeriva().getId())) {
						f = fisicoDao.create(fisico);
						ls.add(f);
					}
				} else {
					f = fisicoDao.create(fisico);
					ls.add(f);
				}
			} else if (Constantes.PERFIL_RESPONSABLE_MESA_PARTES.equals(usuario.getPerfil().getId().trim())
					&& der.getCentroCostoDestino().getId().trim().equals(usuario.getEmpleado().getCentroCosto().getId().trim())) {
				f = fisicoDao.create(fisico);
				ls.add(f);
			}

		}

		Bitacora b = new Bitacora();
		b.setFecha(new Date());
		b.setEmpleado(usuario.getEmpleado());
		b.setCentroCosto(usuario.getEmpleado().getCentroCosto());
		b.setDescripcion("RECIBÍ CONFORME EL DOCUMENTO FÍSICO");
		b.setIndicaciones(motivo);
		b.setEstado(d.getDeriva().getEstado());
		b.setFichaDocumento(d.getDeriva().getFichaDocumento());
		bitacoraService.create(usuario, b, operacion);
		return ls;
	}

	@Override
	public List<SeguimientoFisico> findRecibiFisico(Usuario usuario, String derivaId, String estado) throws Exception {
		// TODO Auto-generated method stub
		if (derivaId.equals(Constantes.VACIO)) {
			return new ArrayList<>();
		} else {
			return fisicoDao.findRecibiFisico(derivaId, estado);
		}
	}

	@Override
	public List<SeguimientoFisicoUtil> findDirigidosByFichaId(String fichaId) {
		// TODO Auto-generated method stub
		if (fichaId.equals(Constantes.VACIO)) {
			return new ArrayList<>();
		} else {
			return fisicoDao.findDirigidosByFichaId(fichaId);
		}
	}

	@Override
	public List<SeguimientoFisico> findRecibiFisicoFichaId(Usuario usuario, String FichaDocumentoId, String estado)
			throws Exception {
		// TODO Auto-generated method stub
		if (FichaDocumentoId.equals(Constantes.VACIO)) {
			return new ArrayList<>();
		} else {
			return fisicoDao.findRecibiFisicoFichaId(FichaDocumentoId, estado);
		}
	}

	@Override
	public Object updateRecibiConforme(Usuario usuario, SeguimientoFisico d, String operacion, String motivo, String tipo,
			String fichaId) throws Exception {
		// TODO Auto-generated method stub

		Date fechaActual = new Date();
		d.setFechaModifica(fechaActual);
		d.setUsuarioModifica(usuario.getNombreUsuario().toString().trim());
		SeguimientoFisico fisico = fisicoDao.create(d);
		return fisico;
	}

	@Override
	public List<SeguimientoFisico> findRecibiFisicoFichaIdEmpleadoId(Usuario usuario, String fichaDocumentoId,
			String empleadoId) throws Exception {
		// TODO Auto-generated method stub
		return fisicoDao.findRecibiFisicoFichaIdAndEmpleado(fichaDocumentoId, empleadoId);
	}

	@Override
	public Object updateRecibiConformeBitacora(Usuario usuario, SeguimientoFisico d, String operacion, String motivo,
			String tipo, String fichaId) throws Exception {
		// TODO Auto-generated method stub
		Bitacora b = new Bitacora();
		b.setFecha(new Date());
		b.setEmpleado(usuario.getEmpleado());
		b.setCentroCosto(usuario.getEmpleado().getCentroCosto());
		b.setDescripcion("ACTUALIZAR FECHA RECIBI CONFORME");
		b.setIndicaciones(motivo);
		Tipo estado = new Tipo();
		estado.setId(7);
		estado.setNombre("ESTADO_DOCUMENTO");
		estado.setDescripcion("REGISTRADO");
		b.setEstado(estado);
		FichaDocumento ficha = new FichaDocumento();
		ficha.setId(Integer.parseInt(fichaId));
		b.setFichaDocumento(ficha);
		bitacoraService.create(usuario, b, operacion);
		return b;
	}

	@Override
	public List<SeguimientoFisico> findRecibiFisicoById(Usuario usuario, String derivaId) throws Exception {
		// TODO Auto-generated method stub
		return fisicoDao.findRecibiFisicoById(derivaId);
	}

}
