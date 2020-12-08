package pe.gob.congreso.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import pe.gob.congreso.dao.DerivaDao;
import pe.gob.congreso.dto.DerivaDTO;
import pe.gob.congreso.model.CentroCosto;
import pe.gob.congreso.model.Deriva;
import pe.gob.congreso.model.Empleado;
import pe.gob.congreso.model.EstadoDeriva;
import pe.gob.congreso.model.FichaDocumento;
import pe.gob.congreso.model.MotivoDeriva;
import pe.gob.congreso.model.Tipo;
import pe.gob.congreso.model.TipoDocumento;
import pe.gob.congreso.model.util.DerivaUtil;
import pe.gob.congreso.model.util.EnviadoUtil;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.model.util.Util;
import pe.gob.congreso.model.util.sp;
import pe.gob.congreso.util.Constantes;
import pe.gob.congreso.util.CriteriaDebug;
import pe.gob.congreso.util.FichaConsultas;
import pe.gob.congreso.util.PaginationHelper;

@Repository("derivaDao")
public class DerivaDaoImpl extends AbstractDao<Integer, Deriva> implements DerivaDao {
	// protected final Log log = LogFactory.getLog(getClass());
	private final PaginationHelper paginationHelper = new PaginationHelper();

	public List test() throws Exception {

		Query query = getSession().createSQLQuery("EXEC sp_kpi_recibidos :idempleado, :ccosto, :fecha").addEntity(sp.class)
				.setParameter("idempleado", "73596").setParameter("ccosto", "0000001382").setParameter("fecha", "2019-10-03");

		List result = query.list();

		return result;
	}

	public void setFetchMode(Criteria criteria) {
		criteria.setFetchMode("fichaDocumento", FetchMode.JOIN);
		criteria.setFetchMode("empleadoOrigen", FetchMode.JOIN);
		criteria.setFetchMode("empleadoDestino", FetchMode.JOIN);
		criteria.setFetchMode("motivoDeriva", FetchMode.JOIN);
		criteria.setFetchMode("estadoDeriva", FetchMode.JOIN);
	}

	@Override
	public Deriva create(Deriva d) throws Exception {
		saveOrUpdate(d);
		return d;
	}

	@Override
	public Map<String, Object> findBy(Optional<String> tipoRegistro, Optional<String> empleadoId, Optional<String> id,
			Optional<String> numeroDoc, Optional<String> asunto, Optional<String> fechaIniCrea, Optional<String> fechaFinCrea,
			Optional<String> tipoDocumento, Optional<String> tipoEstado, Optional<String> centroCosto,
			Optional<String> privado, Optional<String> remitidoDes, Optional<String> texto, Optional<String> referencia,
			Optional<String> observaciones, Optional<String> esResponsable, Optional<String> esRecibido, Optional<String> pag,
			Optional<String> pagLength, Boolean esMesaPartes, Optional<String> numeroMp, Optional<String> dependenciaId,
			Optional<String> anioLegislativo, Optional<String> esRecibiConforme) throws Exception {

		Criteria criteria = createEntityCriteria();

		this.setFetchMode(criteria);
		criteria.setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("id"), "id")))
				.setResultTransformer(Transformers.aliasToBean(Deriva.class));

		criteria.createAlias("fichaDocumento", "f");
		criteria.createAlias("empleadoDestino", "ed");
		criteria.createAlias("centroCostoDestino", "ccd");
		criteria.createAlias("centroCostoOrigen", "cco");
		criteria.createAlias("f.centroCostoRemitido", "ccremitido");

		Criteria sfCriteria = criteria.createCriteria("seguimientoFisico", JoinType.LEFT_OUTER_JOIN);
		// criteria.createAlias("seguimientoFisico", "sf");

		// criteria.add(Restrictions.eq("f.tipoRegistro.id", 1 ));
		criteria.add(Restrictions.sqlRestriction(" {alias}.stdd_iestado not in  (?) ", 7, IntegerType.INSTANCE));
		System.out.println("stdd_iestado:" + 7);

		criteria.add(Restrictions.eq("habilitado", true));
		System.out.println("stdd_bhabilitado:" + "1");

		// Filtros de Búsqueda
		if (dependenciaId.isPresent() && !dependenciaId.get().isEmpty()) {
			criteria.add(Restrictions.eq("ccd.id", centroCosto.get()));
			System.out.println("ccd.stdcc_id:" + centroCosto.get());

			// criteria.add(Restrictions.eq("cco.id", dependenciaId.get())); //Comentado AEP
			// 09.09.2019
			// criteria.add(Restrictions.eq("ccremitido.id", dependenciaId.get()));
			// //Agregado AEP 09.09.2019 //Comentado AEP 10.09.2019
			// Agregado AEP 10.09.2019
			criteria.add(Restrictions.or(Restrictions.eq("cco.id", dependenciaId.get()),
					Restrictions.eq("ccremitido.id", dependenciaId.get())));

			System.out.println("cco.stdcc_id:" + dependenciaId.get());
			System.out.println("ccremitido.stdcc_id:" + dependenciaId.get());
		}

		if (esMesaPartes) {
			criteria.add(Restrictions.in("f.estafeta", new String[] { "1" }));
			System.out.println("f.stdf_cestafeta:" + 1);
		}

		if (numeroMp.isPresent() && !numeroMp.get().isEmpty()) {
			criteria.add(Restrictions.eq("f.numeroMp", Integer.parseInt(numeroMp.get())));
			System.out.println("f.stdfp_inumero_mp:" + numeroMp.get());
		}

		if (privado.isPresent() && !privado.get().isEmpty()) {
			criteria.add(Restrictions.eq("f.privado", Boolean.valueOf(privado.get())));
			System.out.println("f.stdf_bprivado:" + privado.get());
		}

		if (empleadoId.isPresent() && !empleadoId.get().isEmpty()) {
			criteria.add(Restrictions.eq("ed.id", Integer.valueOf(empleadoId.get())));
			System.out.println("ed.stde_id_destino:" + empleadoId.get());
		}

		if (centroCosto.isPresent() && !centroCosto.get().isEmpty()) {
			criteria.add(Restrictions.eq("ccd.id", centroCosto.get()));
			System.out.println("ccd.stdcc_id:" + centroCosto.get());

			criteria.add(Restrictions.eq("centroCostoDestinoId", Integer.parseInt(centroCosto.get())));
			System.out.println("stdcc_id_idestino:" + centroCosto.get());
		}

		if (remitidoDes.isPresent() && !remitidoDes.get().isEmpty()) {
			// Comentado AEP 28.08.2019
			// criteria.add(Restrictions.like("f.remitidoDes",
			// Util.replaceAllTildes(remitidoDes.get()), MatchMode.ANYWHERE));
			// Agregado AEP 28.08.2019
			criteria.add(Restrictions.like("cco.descripcion", Util.replaceAllTildes(remitidoDes.get()), MatchMode.ANYWHERE));
			System.out.println("cco.stdcc_cdescripcion:" + remitidoDes.get());
		}

		if (id.isPresent() && !id.get().isEmpty()) {
			criteria.add(Restrictions.eq("f.id", Integer.valueOf(id.get())));
			System.out.println("f.stdf_id:" + id.get());
		}

		if (numeroDoc.isPresent() && !numeroDoc.get().isEmpty()) {
			criteria.add(Restrictions.like("f.numeroDoc", Util.replaceAllTildes(numeroDoc.get()), MatchMode.ANYWHERE));
			System.out.println("f.stdf_cnumero_doc:" + numeroDoc.get());
		}

		if (asunto.isPresent() && !asunto.get().isEmpty()) {
			criteria.add(Restrictions.like("f.asunto", Util.replaceAllTildes(asunto.get()), MatchMode.ANYWHERE));
			System.out.println("f.stdf_vasunto:" + numeroDoc.get());
		}

		if (referencia.isPresent() && !referencia.get().isEmpty()) {
			criteria.add(Restrictions.like("f.referencia", Util.replaceAllTildes(referencia.get()), MatchMode.ANYWHERE));
			System.out.println("f.stdf_vreferencia:" + referencia.get());
		}

		if (observaciones.isPresent() && !observaciones.get().isEmpty()) {
			criteria
					.add(Restrictions.like("f.observaciones", Util.replaceAllTildes(observaciones.get()), MatchMode.ANYWHERE));
			System.out.println("f.stdef_vobservaciones:" + observaciones.get());
		}

		// esRecibiConforme = Optional.fromNullable(true);

		Boolean a = esRecibiConforme.isPresent() && !esRecibiConforme.get().isEmpty()
				&& Boolean.valueOf(esRecibiConforme.get()) == true;

		System.out.println("esRecibiConforme: " + a);

		if (a == true) {
			// si se ingreso la fecha inicio y fin, buscar en un rango
			if ((fechaIniCrea.isPresent() && !fechaIniCrea.get().isEmpty())
					&& (fechaFinCrea.isPresent() && !fechaFinCrea.get().isEmpty())) {
				sfCriteria.add(Restrictions.sqlRestriction("{alias}.stdsf_dfecha_recep >= CONVERT(datetime, ? ,120) ",
						fechaIniCrea.get().replace('T', ' ').replace('P', ':').substring(0, 19), StringType.INSTANCE));
				System.out.println("sf.stdsf_dfecha_recep:" + fechaIniCrea.get());

				sfCriteria.add(Restrictions.sqlRestriction("{alias}.stdsf_dfecha_recep <= CONVERT(datetime, ? ,120) ",
						fechaFinCrea.get().replace('T', ' ').replace('P', ':').substring(0, 17) + "59", StringType.INSTANCE));
				System.out.println("sf.stdsf_dfecha_recep:" + fechaFinCrea.get());
			} else {
				// Si se ingreso fecha inicio o fecha fin
				if ((fechaIniCrea.isPresent() && !fechaIniCrea.get().isEmpty())
						|| (fechaFinCrea.isPresent() && !fechaFinCrea.get().isEmpty())) {
					String fecha = new String();
					// si se ingreso fecha de inicio, buscar por esa fecha, sino buscar por fecha
					// fin
					if (fechaIniCrea.isPresent() && !fechaIniCrea.get().isEmpty()) {
						fecha = fechaIniCrea.get().substring(0, 10);
					} else {
						fecha = fechaFinCrea.get().substring(0, 10);
					}

					System.out.println("sf.stdsf_dfecha_recep:" + fecha);
					sfCriteria.add(Restrictions.sqlRestriction("convert(varchar,{alias}.stdsf_dfecha_recep,23)= ?", fecha,
							StringType.INSTANCE));
				}
			}

		} else {
			if (fechaIniCrea.isPresent() && !fechaIniCrea.get().isEmpty()) {
				if (fechaFinCrea.isPresent()) {
					if (fechaFinCrea.get().isEmpty()) {
						// criteria.add(Restrictions.sqlRestriction("
						// convert(varchar,{alias}.stdd_dfecha_deriva,23) = ? ", fechaIniCrea.get() ,
						// StringType.INSTANCE));
						criteria.add(Restrictions.sqlRestriction(" convert(varchar,{alias}.stdd_dfecha_deriva,23) = ? ",
								fechaIniCrea.get().substring(0, 10), StringType.INSTANCE));
						System.out.println("stdd_dfecha_deriva:" + fechaIniCrea.get());
					}
				} else {
					// criteria.add(Restrictions.sqlRestriction("
					// convert(varchar,{alias}.stdd_dfecha_deriva,23) = ? ", fechaIniCrea.get() ,
					// StringType.INSTANCE));
					criteria.add(Restrictions.sqlRestriction(" convert(varchar,{alias}.stdd_dfecha_deriva,23) = ? ",
							fechaIniCrea.get().substring(0, 10), StringType.INSTANCE));
					System.out.println("stdd_dfecha_deriva:" + fechaIniCrea.get());
				}
			}

			if (fechaIniCrea.isPresent() && !fechaIniCrea.get().isEmpty()) {
				if (fechaFinCrea.isPresent()) {
					if (!fechaFinCrea.get().isEmpty()) {
						System.out.println("Fecha Inicio con valor ** fechaIniCrea.get() -> " + fechaIniCrea.get()
								+ " CONVERT 120 -> " + fechaIniCrea.get().replace('T', ' ').replace('P', ':').substring(0, 19));
						System.out.println("Fecha Final  con valor ** fechaFinCrea.get() -> " + fechaFinCrea.get()
								+ " CONVERT 120 -> " + fechaFinCrea.get().replace('T', ' ').replace('P', ':').substring(0, 17) + "59");

						criteria.add(Restrictions.sqlRestriction("{alias}.stdd_dfecha_deriva >= CONVERT(datetime, ? ,120) ",
								fechaIniCrea.get().replace('T', ' ').replace('P', ':').substring(0, 19), StringType.INSTANCE));
						System.out.println("stdd_dfecha_deriva:" + fechaIniCrea.get());

						// criteria.add(Restrictions.sqlRestriction("{alias}.stdd_dfecha_deriva <
						// DATEADD(DAY,1,CONVERT(datetime, ? ,120)) ", fechaFinCrea.get(),
						// StringType.INSTANCE));

						criteria.add(Restrictions.sqlRestriction("{alias}.stdd_dfecha_deriva <= CONVERT(datetime, ? ,120) ",
								fechaFinCrea.get().replace('T', ' ').replace('P', ':').substring(0, 17) + "59", StringType.INSTANCE));
						System.out.println("stdd_dfecha_deriva:" + fechaFinCrea.get());

						criteria.add(Restrictions.in("anio", new Integer[] { Integer.parseInt(fechaIniCrea.get().substring(0, 4)),
								Integer.parseInt(fechaFinCrea.get().substring(0, 4)) }));

						System.out.println("stde_anio:" + fechaIniCrea.get());
						System.out.println("stde_anio:" + fechaFinCrea.get());
					}
				}
			}

			if (!fechaIniCrea.isPresent() && !fechaFinCrea.isPresent()) {
				Calendar fecha = Calendar.getInstance();
				int anio = fecha.get(Calendar.YEAR);
				criteria.add(Restrictions.in("anio", new Integer[] { anio }));

				System.out.println("stde_anio:" + anio);
			}
		}

		criteria.createAlias("f.tipoDocumento", "tipDoc");
		criteria.createAlias("tipDoc.tipo", "tipDocumento");
		criteria.createAlias("estado", "estado");

		if (tipoDocumento.isPresent() && !tipoDocumento.get().isEmpty()) {
			criteria.add(Restrictions.eq("tipDoc.tipo.id", Integer.valueOf(tipoDocumento.get())));
			System.out.println("tipDoc.tipo.stdt_id:" + tipoDocumento.get());
		}

		if (tipoEstado.isPresent() && !tipoEstado.get().isEmpty()) {
			if (tipoEstado.get().equals("0")) {
				criteria.add(Restrictions.sqlRestriction(" {alias}.stdd_iestado in  (8,9) "));
			} else {
				criteria.add(Restrictions.eq("estado.id", Integer.valueOf(tipoEstado.get())));
				System.out.println("stdd_iestado:" + tipoEstado.get());
			}
		}

		if (texto.isPresent() && !texto.get().isEmpty()) {
			String[] palabras = texto.get().trim().split(" ");
			for (String pal : palabras) {
				// criteria.add(Restrictions.sqlRestriction("ISNULL(f1_.stdf_cnumero_doc,'') +
				// ISNULL(f1_.stdf_vasunto,'') + ISNULL(f1_.stdf_vreferencia,'') +
				// ISNULL(f1_.stdef_vobservaciones,'') + ISNULL(f1_.stdf_remitidodes,'') +
				// ISNULL(ed2_.stde_vapellidos,'') + ISNULL(ed2_.stde_vnombres,'') +
				// ISNULL(tipdocumen6_.stdt_vdescripcion,'') +
				// ISNULL(estado7_.stdt_vdescripcion,'') LIKE
				// '%"+Util.replaceAllTildes(pal.trim())+"%'"));
				criteria.add(Restrictions.sqlRestriction(
						"ISNULL(f1_.stdf_cnumero_doc,'') + ISNULL(f1_.stdf_vasunto,'') + ISNULL(f1_.stdf_vreferencia,'') + ISNULL(f1_.stdef_vobservaciones,'') + ISNULL(f1_.stdf_remitidodes,'') + ISNULL(ed2_.stde_vapellidos,'') + ISNULL(ed2_.stde_vnombres,'') + ISNULL(tipdocumen8_.stdt_vdescripcion,'') + ISNULL(estado9_.stdt_vdescripcion,'') LIKE  '%"
								+ Util.replaceAllTildes(pal.trim()) + "%'"));
			}
		}

		if (esResponsable.isPresent() && !esResponsable.get().isEmpty()) {
			criteria.add(Restrictions.eq("esResponsable", Boolean.valueOf(esResponsable.get())));
			System.out.println("stde_bresponsable:" + esResponsable.get());
		}

		if (esRecibido.isPresent() && !esRecibido.get().isEmpty()) {
			if (centroCosto.isPresent() && !centroCosto.get().isEmpty()) {
				criteria.add(Restrictions.or(Restrictions.ne("cco.id", centroCosto.get()), Restrictions.eq("dirigido", 3)));
				System.out.println("cco.stdcc_id:" + centroCosto.get());
				System.out.println("stdd_bdirigido:" + 3);
			}
		}

		criteria.createAlias("f.anioLegislativo", "anioLegis");

		if (anioLegislativo.isPresent() && !anioLegislativo.get().isEmpty()) {
			criteria.add(Restrictions.eq("anioLegis.id", Integer.parseInt(anioLegislativo.get())));
			System.out.println("anioLegis.stdal_codigo:" + anioLegislativo.get());
		}

		CriteriaDebug.debug(criteria);

		// List<Deriva> x = criteria.list();
		// Deriva y = x.get(0);
		// System.out.println("listado: " + y.toString());

		// Pagination
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map = paginationHelper.getPaginationDeriva(criteria, "id", pag, pagLength, esMesaPartes);

		List<Deriva> listaFichas = (List<Deriva>) map.get("lista");
		// System.out.println("listado2: " + listaFichas.get(0).getSeguimientoFisico());
		// Deriva primerDoc = (Deriva) listaFichas.get(0);
		// List<SeguimientoFisico> sf = primerDoc.getSeguimientoFisico();
		// System.out.println("cant segimientos:" + primerDoc.toString());

		result.put("documentos", listaFichas);
		result.put("totalPage", map.get("totalPage"));
		result.put("totalResultCount", map.get("totalResultCount"));

		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Deriva> findDerivados(String fichaDocumentoId) throws Exception {
		Criteria criteria = createEntityCriteria();

		this.setFetchMode(criteria);
		criteria.createAlias("fichaDocumento", "f");
		criteria.add(Restrictions.eq("f.id", Integer.valueOf(fichaDocumentoId)));
		criteria.add(Restrictions.eq("habilitado", true));
		criteria.addOrder(Order.desc("id"));

		List derivaList = criteria.list();
		return derivaList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EnviadoUtil> findEnviados(Integer fichaDocumentoId, Boolean esMesaPartes) throws Exception {
		Criteria criteria = createEntityCriteria();
		List derivaList = new ArrayList<>();
		if (esMesaPartes) {
			this.setFetchMode(criteria);
			criteria.createAlias("fichaDocumento", "f");
			criteria.add(Restrictions.eq("f.id", fichaDocumentoId));
			criteria.add(Restrictions.eq("habilitado", Constantes.HABILITADO));
			criteria.add(Restrictions.eq("dirigido", Constantes.ENVIADOS)); // MP
			criteria.add(Restrictions.eq("esResponsable", Constantes.RESPONSABLE)); // MP
			criteria.createAlias("empleadoDestino", "empleado");
			criteria.createAlias("centroCostoDestino", "centroCosto");
			criteria
					.setProjection(Projections.distinct(
							Projections.projectionList().add((Projections.property("empleado.descripcion")), "empleadoDescripcion")
									.add((Projections.property("empleado.id")), "empleadoId")
									.add(Projections.property("centroCosto.descripcion"), "centroCosto")
									.add(Projections.property("centroCosto.id"), "centroCostoId")
									.add(Projections.property("dirigido"), "dirigido").add(Projections.property("ccopia"), "ccopia")
									.add(Projections.property("esResponsable"), "esResponsable")))
					.setResultTransformer(Transformers.aliasToBean(EnviadoUtil.class));
			derivaList = criteria.list();
		} else {
			this.setFetchMode(criteria);
			criteria.createAlias("fichaDocumento", "f");
			criteria.add(Restrictions.eq("f.id", fichaDocumentoId));
			criteria.add(Restrictions.eq("habilitado", Constantes.HABILITADO));
			criteria.createAlias("empleadoDestino", "empleado");
			criteria.createAlias("centroCostoDestino", "centroCosto");
			criteria
					.setProjection(Projections.distinct(
							Projections.projectionList().add((Projections.property("empleado.descripcion")), "empleadoDescripcion")
									.add((Projections.property("empleado.id")), "empleadoId")
									.add(Projections.property("centroCosto.descripcion"), "centroCosto")
									.add(Projections.property("centroCosto.id"), "centroCostoId")
									.add(Projections.property("dirigido"), "dirigido").add(Projections.property("ccopia"), "ccopia")
									.add(Projections.property("esResponsable"), "esResponsable")))
					.setResultTransformer(Transformers.aliasToBean(EnviadoUtil.class));
			derivaList = criteria.list();

			Criteria criteriaAdic = createEntityCriteria();

			this.setFetchMode(criteriaAdic);
			criteriaAdic.createAlias("fichaDocumento", "f");
			criteriaAdic.add(Restrictions.eq("f.id", fichaDocumentoId));
			criteriaAdic.add(Restrictions.eq("habilitado", Constantes.HABILITADO));

			criteriaAdic.createAlias("enviadoExterno", "externo");
			criteriaAdic
					.setProjection(Projections.distinct(Projections.projectionList()
							.add(Projections.property("externo.externoDescripcion"), "empleadoDescripcion")
							.add(Projections.property("dirigido"), "dirigido").add(Projections.property("ccopia"), "ccopia")
							.add(Projections.property("esResponsable"), "esResponsable")))
					.setResultTransformer(Transformers.aliasToBean(EnviadoUtil.class));
			derivaList.addAll(criteriaAdic.list());
		}
		return derivaList;
	}

	@Override
	public Deriva findDerivado(String id) throws Exception {
		Criteria criteria = createEntityCriteria();

		this.setFetchMode(criteria);
		criteria.add(Restrictions.eq("id", Integer.valueOf(id)));

		Deriva deriva = (Deriva) criteria.uniqueResult();
		return deriva;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Deriva> findDirigidos(String fichaDocumentoId) throws Exception {
		Criteria criteria = createEntityCriteria();

		this.setFetchMode(criteria);
		criteria.createAlias("fichaDocumento", "f");
		criteria.add(Restrictions.eq("f.id", Integer.valueOf(fichaDocumentoId)));
		criteria.add(Restrictions.eq("habilitado", true));
		criteria.add(Restrictions.or(Restrictions.eq("dirigido", 1), Restrictions.eq("dirigido", 2)));
		criteria.addOrder(Order.desc("id"));

		List derivaList = criteria.list();
		return derivaList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Deriva> findDirigidosResponsable(String fichaDocumentoId) throws Exception {
		Criteria criteria = createEntityCriteria();

		this.setFetchMode(criteria);
		criteria.createAlias("fichaDocumento", "f");
		criteria.add(Restrictions.eq("f.id", Integer.valueOf(fichaDocumentoId)));
		criteria.add(Restrictions.eq("habilitado", true));
		criteria.add(Restrictions.eq("dirigido", 1));
		criteria.add(Restrictions.eq("esResponsable", true));
		criteria.addOrder(Order.desc("id"));

		List derivaList = criteria.list();
		return derivaList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Deriva> getOnlyDerivados(String fichaDocumentoId) throws Exception {
		Criteria criteria = createEntityCriteria();

		this.setFetchMode(criteria);
		criteria.createAlias("fichaDocumento", "f");
		criteria.add(Restrictions.eq("f.id", Integer.valueOf(fichaDocumentoId)));
		criteria.add(Restrictions.eq("habilitado", true));
		criteria.add(Restrictions.eq("dirigido", 0));
		criteria.addOrder(Order.desc("id"));

		List derivaList = criteria.list();
		return derivaList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Deriva> getOnlyDerivados(String fichaDocumentoId, String centroCostoOrigen, Integer idDeriva,
			Integer estado, Integer empleadoId) throws Exception {
		Criteria criteria = createEntityCriteria();

		this.setFetchMode(criteria);
		criteria.createAlias("fichaDocumento", "f");
		criteria.createAlias("centroCostoOrigen", "cco");
		criteria.createAlias("empleadoOrigen", "emo");
		criteria.createAlias("estado", "es");
		criteria.add(Restrictions.eq("f.id", Integer.valueOf(fichaDocumentoId)));
		criteria.add(Restrictions.eq("cco.id", centroCostoOrigen));
		criteria.add(Restrictions.gt("id", idDeriva));
		criteria.add(Restrictions.ne("es.id", estado));
		criteria.add(Restrictions.ne("emo.id", empleadoId));
		criteria.add(Restrictions.eq("habilitado", true));
		criteria.add(Restrictions.eq("dirigido", 0));
		criteria.addOrder(Order.desc("id"));

		List derivaList = criteria.list();
		return derivaList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Deriva> getDerivadosToValidar(Integer fichaDocumentoId, Integer empleadoOrigen, String centroCostoDestino)
			throws Exception {
		Criteria criteria = createEntityCriteria();

		this.setFetchMode(criteria);
		criteria.createAlias("fichaDocumento", "f");
		criteria.createAlias("empleadoOrigen", "emOrigen");
		criteria.createAlias("empleadoDestino", "emDestino");
		criteria.add(Restrictions.eq("f.id", fichaDocumentoId));
		criteria.add(Restrictions.eq("emOrigen.id", empleadoOrigen));
		criteria.add(Restrictions.eq("emDestino.centroCosto.id", centroCostoDestino));
		criteria.add(Restrictions.eq("habilitado", true));

		List derivaList = criteria.list();
		return derivaList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Deriva> getEnviadosLessParciales(Integer fichaDocumentoId) throws Exception {
		Criteria criteria = createEntityCriteria();

		this.setFetchMode(criteria);
		criteria.createAlias("fichaDocumento", "f");
		criteria.add(Restrictions.eq("f.id", Integer.valueOf(fichaDocumentoId)));
		criteria.add(Restrictions.eq("habilitado", true));
		criteria.add(Restrictions.sqlRestriction("stdd_bdirigido <> ? ", 2, IntegerType.INSTANCE));
		criteria.addOrder(Order.desc("id"));

		List derivaList = criteria.list();
		return derivaList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Deriva> getEnviadosParciales(Integer fichaDocumentoId) throws Exception {
		Criteria criteria = createEntityCriteria();

		this.setFetchMode(criteria);
		criteria.createAlias("fichaDocumento", "f");
		criteria.add(Restrictions.eq("f.id", Integer.valueOf(fichaDocumentoId)));
		criteria.add(Restrictions.eq("habilitado", true));
		criteria.add(Restrictions.sqlRestriction("stdd_bdirigido = ? ", 2, IntegerType.INSTANCE));
		criteria.addOrder(Order.desc("id"));

		List derivaList = criteria.list();
		return derivaList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Deriva> getDerivadosByCC(Integer fichaDocumentoId, String centroCostoDestino) throws Exception {
		Criteria criteria = createEntityCriteria();

		this.setFetchMode(criteria);
		criteria.createAlias("fichaDocumento", "f");
		criteria.createAlias("centroCostoDestino", "cc");
		criteria.add(Restrictions.eq("f.id", fichaDocumentoId));
		criteria.add(Restrictions.eq("cc.id", centroCostoDestino));
		criteria.add(Restrictions.eq("habilitado", true));
		criteria.add(Restrictions.eq("f.habilitado", true));

		List derivaList = criteria.list();
		return derivaList;
	}

	@Override
	@Transactional(readOnly = true)
	public Integer updateRecibidoFisico(FichaDocumento fd) throws Exception {
		Query query = this.getSession()
				.createSQLQuery("update STD_ENVIADO set stdd_iestado = 21 " + " where stdf_id = :id and stdd_bdirigido = 3");
		query.setParameter("id", fd.getId());
		int result = query.executeUpdate();
		return result;
	}

	@Override
	public Deriva getDerivadoToFichaIdEmpleadoCC(Integer fichaDocumentoId, Integer empleadoDestino,
			String centroCostoDestino) throws Exception {
		// TODO Auto-generated method stub
		Criteria criteria = createEntityCriteria();
		this.setFetchMode(criteria);
		criteria.createAlias("fichaDocumento", "f");
		criteria.createAlias("centroCostoDestino", "cc");
		criteria.createAlias("empleadoDestino", "ed");
		criteria.add(Restrictions.eq("f.id", fichaDocumentoId));
		criteria.add(Restrictions.eq("cc.id", centroCostoDestino));
		criteria.add(Restrictions.eq("habilitado", true));
		Deriva deriva = (Deriva) criteria.uniqueResult();
		return deriva;
	}

	@Override
	public Map<String, Object> getFichaDerivaEstafeta(FichaConsultas fc, Optional<String> pag, Optional<String> pagLength,
			List<InputSelectUtil> listDependencias, Map parametros) throws Exception {
		StringBuffer consulta = new StringBuffer();
		consulta.append(" SELECT * FROM ( ");
		consulta.append(" SELECT ROW_NUMBER() OVER(ORDER BY m.numeroMp DESC) as rowNr, ");
		consulta.append("        m.*, ");
		if ((fc.getNumeroMp() != null && !fc.getNumeroMp().isEmpty()) || (fc.getId() != null && !fc.getId().isEmpty())
				|| (fc.getSumilla() != null && !fc.getSumilla().isEmpty())
				|| (fc.getRemitidoPor() != null && !fc.getRemitidoPor().isEmpty())) {
			consulta.append("	      COUNT(m.idFicha) over() as total, ");
			consulta.append("	''	  as descSemana");
		} else {
			consulta.append("		  (SELECT COUNT(*) as total ");
			consulta.append("         FROM   std_ficha_documento ");
			consulta.append("         WHERE  stdcc_iid = :centroCostoId ");
			consulta.append("       	 AND stdf_bhabilitado = :habilitado ");
			consulta.append("       	 AND stdf_cestafeta = :estafeta) as total, ");
			consulta.append(
					" ' SEMANA DEL LUNES '+replace(convert(NVARCHAR, DATEADD(wk,DATEDIFF(wk,7 * ( :pag - 1 ),GETDATE()),0), 103), ' ', '/') ");
			consulta.append(
					"		  + ' AL VIERNES '+replace(convert(NVARCHAR, DATEADD(wk,DATEDIFF(wk,7 * ( :pag - 1 ),GETDATE()),4), 103), ' ', '/') ");
			consulta.append("		  as descSemana ");
		}
		consulta.append("     FROM ( SELECT ");
		consulta.append("	en.stdd_id				as idDeriva, ");
		consulta.append("	en.stdf_id				as idFicha, ");
		consulta.append("	en.stde_id_origen		as empleadoOrigen, ");
		consulta.append("	en.stde_id_destino		as empleadoDestino, ");
		consulta.append("	en.stdd_vindicaciones	as indicaciones, ");
		consulta.append("	en.stdd_dfecha_deriva	as fechaDeriva, ");
		consulta.append("	en.stdmod_id			as motivoDeriva, ");
		consulta.append("	en.stded_id				as estadoDeriva, ");
		consulta.append("	en.aud_cusuario_modifica as usuarioModificacion, ");
		consulta.append("	en.aud_dfecha_modifica	as fechaModificacion, ");
		consulta.append("	en.stdd_bdirigido		as dirigido, ");
		consulta.append("	en.stdd_bhabilitado		as habilitadoDeriva, ");
		consulta.append("	en.stde_vgrupo			as grupo, ");
		consulta.append("	en.stde_vcargo			as cargo, ");
		consulta.append("	en.stdd_iestado			as estado, ");
		consulta.append("	en.stdf_id_respuesta	as fichaRespuesta, ");
		consulta.append("	en.stde_id_destino_externo as enviadoExterno, ");
		consulta.append("	en.stdcc_id_origen		as centroCostoOrigen, ");
		consulta.append("	en.stdcc_id_destino		as centroCostoDestino, ");
		consulta.append("	en.stde_bcopia			as ccopia, ");
		consulta.append("	en.stde_bresponsable	as esResponsable, ");
		consulta.append("	en.stdcc_id_idestino	as centroCostoDestinoId, ");
		consulta.append("	en.stde_anio			as anioDeriva, ");
		consulta.append("	fd.stdfp_inumero_mp		as numeroMp, ");
		consulta.append("	fd.stdf_id				as id, ");
		consulta.append("	fd.aud_dfecha_crea		as fechaCrea, ");
		consulta.append("	fd.stdf_cnumero_doc		as numeroDoc, ");
		consulta.append("	fd.stdf_inumero_folios	as numeroFolios, ");
		consulta.append("     ( SELECT Rtrim(cc.stdcc_cdescripcion) + '@@' AS '*' ");
		consulta.append("      FROM std_enviado envio ");
		consulta.append("      INNER JOIN std_centro_costo cc ON (cc.stdcc_id = envio.stdcc_id_destino) ");
		consulta.append("      WHERE envio.stdd_bhabilitado = :habilitado ");
		consulta.append("        AND envio.stdf_id = fd.stdf_id ");
		consulta.append("    	  AND envio.stde_bresponsable = :esResponsable ");
		consulta.append("    	  AND envio.stdd_bdirigido != :dirigido ");
		consulta.append("      GROUP BY envio.stdf_id, ");
		consulta.append("               cc.stdcc_cdescripcion ");
		consulta.append("      FOR xml path('') )  as enviadoADes, ");
		consulta.append("	(SELECT DISTINCT ");
		consulta.append("			CASE WHEN RTRIM(ee.stdee_vorigen) != RTRIM('') THEN ee.stdee_vorigen + ', ' ELSE '' END + ");
		consulta.append("			ee.stdee_vnombres + ' ' + ee.stdee_vapellidos + '@@' AS '*' ");
		consulta.append("			FROM std_enviado_externo ee ");
		consulta.append("			WHERE ee.stdf_id = fd.stdf_id ");
		consulta.append("			  FOR xml path('') ) as remitidoPor, ");
		consulta.append("	fd.stdf_vasunto			as asunto, ");
		consulta.append("	(SELECT Rtrim(stde_vnombres) + ' ' ");
		consulta.append("			+ Rtrim(stde_vapellidos) ");
		consulta.append("	 FROM   std_empleado ");
		consulta.append("	 WHERE  stde_id = fd.stde_id) as        registradoPorDes, ");
		consulta.append("	( SELECT 1 ");
		consulta.append(
				"				WHERE EXISTS (select * from STD_ETAPA_GESTION_ENVIO et where et.stdf_id = fd.stdf_id and et.stdege_bhabilitado = :habilitado ) ");
		consulta.append("			) as  tieneReporte,");
		consulta.append("	fd.stdf_cestafeta		as estafeta, ");
		consulta.append("	fd.stdt_itipo_registro	as tipoRegistro, ");
		consulta.append("	fd.stde_id				as empleadoId, ");
		consulta.append("	fd.stdcc_id				as centroCosto, ");
		consulta.append("	fd.stdcc_iid			as centroCostoId, ");
		consulta.append("	tipo.stdt_vdescripcion	as tipoDoc, ");
		consulta.append("	fd.stdt_iestado			as tipoEstado, ");
		consulta.append("	( SELECT distinct cc.stdcc_id + '@@' AS '*' ");
		consulta.append("    	FROM std_enviado envio ");
		consulta.append("    	INNER JOIN std_centro_costo cc ON (cc.stdcc_id = envio.stdcc_id_destino) ");
		consulta.append("    	WHERE envio.stdd_bhabilitado = :habilitado ");
		consulta.append("    	AND envio.stdf_id = fd.stdf_id ");
		consulta.append("    	AND envio.stde_bresponsable = :esResponsable ");
		consulta.append("    	AND envio.stdd_bdirigido != :dirigido ");
		consulta.append("    	GROUP BY envio.stdf_id, ");
		consulta.append("    	         cc.stdcc_id ");
		consulta.append("    	FOR xml path('') ) as enviadoA, ");
		consulta.append(
				"	    (SELECT stdcc_cdescripcion FROM std_centro_costo WHERE stdcc_id = en.stdcc_id_origen) as centroCostoOrigenDes, ");
		consulta.append(
				"	    (SELECT stdcc_cdescripcion FROM std_centro_costo WHERE stdcc_id = en.stdcc_id_destino) as centroCostoDestinoDes, ");
		consulta.append(
				"	    (SELECT stdt_iestado FROM std_ficha_documento WHERE stdf_id = en.stdf_id_respuesta)	as tipoEstadoFichaRespuesta, ");
		consulta.append("	    fd.stdf_anio							   AS anioFicha  ");
		consulta.append(" FROM STD_ENVIADO en ");
		consulta.append("	    INNER JOIN STD_FICHA_DOCUMENTO fd ON en.stdf_id = fd.stdf_id ");
		consulta.append("	    INNER JOIN STD_TIPO tr ON fd.stdt_itipo_registro = tr.stdt_id ");
		consulta.append("	    INNER JOIN STD_TIPO_DOCUMENTO td ON fd.stdtd_id = td.stdtd_id ");
		consulta.append("	    INNER JOIN STD_TIPO te ON fd.stdt_iestado = te.stdt_id ");
		consulta.append("	    INNER JOIN STD_TIPO tipo ON td.stdt_id = tipo.stdt_id ");
		consulta.append("	    INNER JOIN STD_EMPLEADO e ON fd.stde_id = e.stde_id ");
		consulta.append("	    INNER JOIN STD_CENTRO_COSTO cc ON fd.stdcc_id = cc.stdcc_id ");
		consulta.append("		) m ");
		consulta.append("  	WHERE ");
		consulta.append("  	 	m.anioDeriva  in (:ppini , :ppfin) ");
		consulta.append("  	 	AND m.centroCostoDestinoId = :centroCostoId ");
		consulta.append("  	 	AND m.anioFicha in (:ppini , :ppfin) ");

		if (fc.getNumeroMp() != null && !fc.getNumeroMp().isEmpty()) {
			consulta.append("  AND m.numeroMp = :numeroMp ");
		}
		if (fc.getRegistradoPor() != null && !fc.getRegistradoPor().isEmpty()) {
			consulta.append("  AND m.empleadoId = :empleadoId ");
		}
		if (fc.getDependenciaOrigenId() != null && !fc.getDependenciaOrigenId().isEmpty()) {
			consulta.append("  AND m.centroCostoId = :centroCostoId ");
		}
		if (fc.getId() != null && !fc.getId().isEmpty()) {
			consulta.append("  AND m.idFicha = :id ");
		}
		if (fc.getNumeroDoc() != null && !fc.getNumeroDoc().isEmpty()) {
			consulta.append("  AND m.numeroDoc = :numeroDoc ");
		}
		if (fc.getFechaIni() != null && !fc.getFechaIni().isEmpty() && fc.getFechaFin() == null) {
			consulta.append("  AND m.fechaCrea = CONVERT(datetime, :fechaIni, 120) ");
		}
		if (fc.getFechaIni() != null && !fc.getFechaIni().isEmpty() && fc.getFechaFin() != null
				&& !fc.getFechaFin().isEmpty()) {
			consulta.append("  AND m.fechaCrea >= CONVERT(datetime, :fechaIni, 120) ");
		}
		if (fc.getFechaIni() != null && !fc.getFechaIni().isEmpty() && fc.getFechaFin() != null
				&& !fc.getFechaFin().isEmpty()) {
			consulta.append("  AND m.fechaCrea < DATEADD(DAY, 1, CONVERT(datetime, :fechaFin, 120)) ");
		}
		if (fc.getTipoDoc() != null && !fc.getTipoDoc().isEmpty()) {
			consulta.append("  AND m.tipoDoc = :tipoDoc ");
		}
		if (fc.getTipoEstado() != null && !fc.getTipoEstado().isEmpty()) {
			consulta.append("  AND m.tipoEstado = :tipoEstado ");
		}
		if (listDependencias != null && !listDependencias.isEmpty()) {
			int i = 0;
			String dependencias = "AND ( ";
			for (InputSelectUtil d : listDependencias) {
				String enviadoA = ":enviadoA" + i;
				dependencias += " m.enviadoA LIKE '%' + " + enviadoA + " + '%' ";
				if (i <= listDependencias.size() - 2) {
					dependencias += " OR ";
				} else if (i == listDependencias.size() - 1) {
					dependencias += " ) ";
				}
				i++;
			}
			consulta.append(dependencias);
		}
		if (fc.getDependenciaDestinoId() != null && !fc.getDependenciaDestinoId().isEmpty()) {
			consulta.append("  AND m.enviadoA LIKE '%' + :enviadoA + '%' ");
		}
		if (fc.getRemitidoPor() != null && !fc.getRemitidoPor().isEmpty()) {
			String busqTildes = Util.replaceAllTildes(fc.getRemitidoPor());
			fc.setRemitidoPor(busqTildes);
			consulta.append("  AND m.remitidoPor LIKE '%' + :remitidoPor + '%' ");
		}
		if (fc.getSumilla() != null && !fc.getSumilla().isEmpty()) {
			String busqTildes = Util.replaceAllTildes(fc.getSumilla());
			fc.setSumilla(busqTildes);
			consulta.append("  AND m.asunto LIKE '%' + :asunto + '%'");
		}
		consulta.append("  	   AND m.estafeta = :estafeta ");
		if (fc.getTipoRegistro() != null && !fc.getTipoRegistro().isEmpty()) {
			consulta.append("  AND m.tipoRegistro = :tipoRegistro ");
		}
		consulta.append("  	   AND m.dirigido = :dirigido ");
		consulta.append("  	   AND m.esResponsable = :esResponsable ");
		if ((fc.getNumeroMp() == null || fc.getNumeroMp().isEmpty()) && (fc.getId() == null || fc.getId().isEmpty())
				&& (fc.getSumilla() == null || fc.getSumilla().isEmpty())
				&& (fc.getRemitidoPor() == null || fc.getRemitidoPor().isEmpty())) {
			consulta.append("  	  AND m.fechaCrea >= DATEADD(wk,DATEDIFF(wk,7 * ( :pag - 1 ),GETDATE()),0) ");
			consulta.append("     AND m.fechaCrea < DATEADD(wk,DATEDIFF(wk,7 * ( :pag - 1 ),GETDATE()),7) ");
		}
		consulta.append("  )  T  ");
		Query query = this.getSession().createSQLQuery(consulta.toString()).addScalar("rowNr", new IntegerType())
				.addScalar("idDeriva", new IntegerType()).addScalar("idFicha", new IntegerType())
				.addScalar("empleadoOrigen", new IntegerType()).addScalar("empleadoDestino", new IntegerType())
				.addScalar("indicaciones", new StringType()).addScalar("fechaDeriva", new TimestampType())
				.addScalar("motivoDeriva", new IntegerType()).addScalar("estadoDeriva", new IntegerType())
				.addScalar("usuarioModificacion", new StringType()).addScalar("fechaModificacion", new TimestampType())
				.addScalar("dirigido", new IntegerType()).addScalar("habilitadoDeriva", new BooleanType())
				.addScalar("grupo", new StringType()).addScalar("cargo", new StringType())
				.addScalar("estado", new IntegerType()).addScalar("fichaRespuesta", new IntegerType())
				.addScalar("enviadoExterno", new IntegerType()).addScalar("centroCostoOrigen", new StringType())
				.addScalar("centroCostoDestino", new StringType()).addScalar("ccopia", new BooleanType())
				.addScalar("esResponsable", new BooleanType()).addScalar("centroCostoDestinoId", new IntegerType())
				.addScalar("anioDeriva", new IntegerType()).addScalar("numeroMp", new IntegerType())
				.addScalar("id", new IntegerType()).addScalar("fechaCrea", new TimestampType())
				.addScalar("numeroDoc", new StringType()).addScalar("numeroFolios", new IntegerType())
				.addScalar("enviadoADes", new StringType()).addScalar("remitidoPor", new StringType())
				.addScalar("asunto", new StringType()).addScalar("registradoPorDes", new StringType())
				.addScalar("tieneReporte", new StringType()).addScalar("estafeta", new StringType())
				.addScalar("tipoRegistro", new IntegerType()).addScalar("empleadoId", new IntegerType())
				.addScalar("centroCosto", new StringType()).addScalar("centroCostoId", new IntegerType())
				.addScalar("tipoDoc", new StringType()).addScalar("tipoEstado", new IntegerType())
				.addScalar("enviadoA", new StringType()).addScalar("centroCostoOrigenDes", new StringType())
				.addScalar("centroCostoDestinoDes", new StringType()).addScalar("tipoEstadoFichaRespuesta", new StringType())
				.addScalar("anioFicha", new IntegerType()).addScalar("total", new IntegerType())
				.addScalar("descSemana", new StringType());

		query.setParameter("ppini", parametros.get("ppini"));
		System.out.println("ppini: " + parametros.get("ppini"));

		query.setParameter("ppfin", parametros.get("ppfin"));
		System.out.println("ppfin: " + parametros.get("ppfin"));

		query.setParameter("habilitado", Constantes.HABILITADO);
		System.out.println("habilitado: " + Constantes.HABILITADO);

		if ((fc.getNumeroMp() == null || fc.getNumeroMp().isEmpty()) && (fc.getId() == null || fc.getId().isEmpty())
				&& (fc.getSumilla() == null || fc.getSumilla().isEmpty())
				&& (fc.getRemitidoPor() == null || fc.getRemitidoPor().isEmpty())) {
			query.setParameter("pag", Integer.parseInt(pag.get()));
			System.out.println("pag: " + Integer.parseInt(pag.get()));
		}

		query.setParameter("estafeta", Constantes.DOCUMENTO_REGISTRADO_POR_MESA_DE_PARTES);
		System.out.println("estafeta: " + Constantes.DOCUMENTO_REGISTRADO_POR_MESA_DE_PARTES);

		query.setParameter("dirigido", Constantes.DOCUMENTO_RECIBIDO_FISICO);
		System.out.println("dirigido: " + Constantes.DOCUMENTO_RECIBIDO_FISICO);

		query.setParameter("esResponsable", Constantes.ENVIADO_AL_RESPONSABLE);
		System.out.println("esResponsable: " + Constantes.ENVIADO_AL_RESPONSABLE);

		if (fc.getNumeroMp() != null && !fc.getNumeroMp().isEmpty()) {
			query.setParameter("numeroMp", fc.getNumeroMp());
			System.out.println("numeroMp: " + fc.getNumeroMp());
		}
		if (fc.getId() != null && !fc.getId().isEmpty()) {
			query.setParameter("id", fc.getId());
			System.out.println("id: " + fc.getId());
		}
		if (fc.getTipoRegistro() != null && !fc.getTipoRegistro().isEmpty()) {
			query.setParameter("tipoRegistro", fc.getTipoRegistro());
			System.out.println("tipoRegistro: " + fc.getTipoRegistro());
		}
		if (fc.getRegistradoPor() != null && !fc.getRegistradoPor().isEmpty()) {
			query.setParameter("empleadoId", fc.getRegistradoPor());
			System.out.println("empleadoId: " + fc.getRegistradoPor());
		}
		if (fc.getDependenciaOrigenId() != null && !fc.getDependenciaOrigenId().isEmpty()) {
			query.setParameter("centroCostoId", Integer.parseInt(fc.getDependenciaOrigenId()));
			System.out.println("centroCostoId: " + fc.getDependenciaOrigenId());
		}
		if (fc.getNumeroDoc() != null && !fc.getNumeroDoc().isEmpty()) {
			query.setParameter("numeroDoc", fc.getNumeroDoc());
			System.out.println("numeroDoc: " + fc.getNumeroDoc());
		}
		if (fc.getFechaIni() != null && !fc.getFechaIni().isEmpty()) {
			query.setParameter("fechaIni", fc.getFechaIni());
			System.out.println("fechaIni: " + fc.getFechaIni());
		}
		if (fc.getFechaFin() != null && !fc.getFechaFin().isEmpty()) {
			query.setParameter("fechaFin", fc.getFechaFin());
			System.out.println("fechaFin: " + fc.getFechaFin());
		}
		if (fc.getTipoDoc() != null && !fc.getTipoDoc().isEmpty()) {
			query.setParameter("tipoDoc", fc.getTipoDoc());
			System.out.println("tipoDoc: " + fc.getTipoDoc());
		}
		if (fc.getTipoEstado() != null && !fc.getTipoEstado().isEmpty()) {
			query.setParameter("tipoEstado", fc.getTipoEstado());
			System.out.println("tipoEstado: " + fc.getTipoEstado());
		}
		if (listDependencias != null && !listDependencias.isEmpty()) {
			int i = 0;
			for (InputSelectUtil d : listDependencias) {
				String enviadoA = "enviadoA" + i;
				query.setParameter(enviadoA, d.getValue().toString());
				System.out.println(enviadoA + ": " + d.getValue().toString());
				i++;
			}
		}
		if (fc.getDependenciaDestinoId() != null && !fc.getDependenciaDestinoId().isEmpty()) {
			query.setParameter("enviadoA", fc.getDependenciaDestinoId());
			System.out.println("enviadoA: " + fc.getDependenciaDestinoId());
		}
		if (fc.getRemitidoPor() != null && !fc.getRemitidoPor().isEmpty()) {
			query.setParameter("remitidoPor", fc.getRemitidoPor().replaceAll(" ", "%"));
			System.out.println("remitidoPor: " + fc.getRemitidoPor().replaceAll(" ", "%"));
		}
		if (fc.getSumilla() != null && !fc.getSumilla().isEmpty()) {
			query.setParameter("asunto", fc.getSumilla().replaceAll(" ", "%"));
			System.out.println("asunto: " + fc.getSumilla().replaceAll(" ", "%"));
		}

		System.out.println(query.getQueryString());

		List listaQuery = query.list();
		List<DerivaUtil> documentos = new ArrayList<DerivaUtil>();
		if (listaQuery.size() == 0) {
			if ((fc.getNumeroMp() == null || fc.getNumeroMp().isEmpty()) && (fc.getId() == null || fc.getId().isEmpty())
					&& (fc.getSumilla() == null || fc.getSumilla().isEmpty())
					&& (fc.getRemitidoPor() == null || fc.getRemitidoPor().isEmpty())) {
				consulta = new StringBuffer();
				consulta.append("		  SELECT COUNT(*) as total, ");
				consulta.append(
						" 		  ' SEMANA DEL LUNES '+replace(convert(NVARCHAR, DATEADD(wk,DATEDIFF(wk,7 * ( :pag - 1 ),GETDATE()),0), 103), ' ', '/') ");
				consulta.append(
						"		  + ' AL VIERNES '+replace(convert(NVARCHAR, DATEADD(wk,DATEDIFF(wk,7 * ( :pag - 1 ),GETDATE()),4), 103), ' ', '/') ");
				consulta.append("		  as descSemana ");
				consulta.append("         FROM   std_ficha_documento ");
				consulta.append("         WHERE  stdcc_iid = :centroCostoId ");
				consulta.append("       	 AND stdf_bhabilitado = :habilitado ");
				consulta.append("       	 AND stdf_cestafeta = :estafeta ");
				query = this.getSession().createSQLQuery(consulta.toString()).addScalar("total", new IntegerType())
						.addScalar("descSemana", new StringType());
				query.setParameter("centroCostoId", Integer.parseInt(fc.getDependenciaOrigenId()));
				query.setParameter("habilitado", Constantes.HABILITADO);
				query.setParameter("estafeta", Constantes.DOCUMENTO_REGISTRADO_POR_MESA_DE_PARTES);
				query.setParameter("pag", Integer.parseInt(pag.get()));
				Object obj = query.uniqueResult();
				Calendar calendar = Calendar.getInstance();
				int total = calendar.get(Calendar.WEEK_OF_YEAR);
				if (calendar.get(Calendar.YEAR) > Constantes.ANIO_INICIO_MODULO_DE_MESA_PARTES) {
					total += (calendar.get(Calendar.YEAR) - Constantes.ANIO_INICIO_MODULO_DE_MESA_PARTES)
							* Constantes.TOTAL_SEMANAS_EN_UN_ANIO;
				}
				Object[] array = (Object[]) obj;
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("documentos", documentos);
				result.put("descSemana",
						array[1] != Constantes.NULO ? array[1].toString() + " TIENE: " + Constantes.CERO + " REGISTROS"
								: Constantes.VACIO);
				result.put("totalPage", total);
				result.put("totalResultCount",
						array[0] != Constantes.NULO ? Integer.parseInt(array[0].toString()) : Constantes.CERO);
				return result;
			}
		}
		Map fichas = new HashMap<>();
		if (Constantes.CLASE_DEVIRA_UTIL.equals(fc.getIndBusqueda())) {
			for (Object obj : listaQuery) {
				Object[] array = (Object[]) obj;
				DerivaUtil doc = new DerivaUtil();
				doc.setId(Integer.parseInt(array[2].toString().trim()));
				doc.setIndicaciones(array[5] != Constantes.NULO ? array[5].toString() : Constantes.VACIO);
				doc.setDirigido(Integer.parseInt(array[11].toString().trim()));
				doc.setHabilitado(Boolean.parseBoolean(array[12].toString()));
				doc.setGrupo(array[13] != Constantes.NULO ? array[13].toString() : Constantes.VACIO);
				doc.setCargo(array[14] != Constantes.NULO ? array[14].toString() : Constantes.VACIO);
				doc.setNumeroMesaPartes(Integer.parseInt(array[24].toString().trim()));
				doc.setEnviadoADes(
						array[29] != Constantes.NULO ? array[29].toString() : Constantes.CODIGO_SEPARADOR.concat(Constantes.VACIO));
				doc.setDerivados(
						doc.getEnviadoADes() != null && !doc.getEnviadoADes().isEmpty() && doc.getEnviadoADes().contains("@@")
								? doc.getEnviadoADes().split("\\@@").length
								: 0);
				Tipo estado = new Tipo();
				estado.setId(Integer.parseInt(array[40].toString()));
				doc.setEstado(estado);
				FichaDocumento fichaDocumento = new FichaDocumento();
				fichaDocumento.setId(doc.getId());
				fichaDocumento.setNumeroMp(doc.getNumeroMesaPartes());
				fichaDocumento.setAsunto(array[31] != Constantes.NULO ? array[31].toString() : Constantes.VACIO);
				fichaDocumento.setEstafeta(array[34] != Constantes.NULO ? array[34].toString() : Constantes.VACIO);
				fichaDocumento.setTipoEstado(estado);
				fichaDocumento.setNumeroDoc(array[27] != Constantes.NULO ? array[27].toString() : Constantes.VACIO);
				Date fechaCrea = array[26] != Constantes.NULO ? (Date) array[26] : new Date();
				fichaDocumento.setFechaCrea(fechaCrea);
				Date fechaDeriva = array[6] != Constantes.NULO ? (Date) array[6] : new Date();
				doc.setFechaDeriva(fechaDeriva);
				TipoDocumento tipoDoc = new TipoDocumento();
				Tipo tipo = new Tipo();
				tipo.setDescripcion(array[39] != Constantes.NULO ? array[39].toString() : Constantes.VACIO);
				tipoDoc.setTipo(tipo);
				fichaDocumento.setTipoDocumento(tipoDoc);
				doc.setFichaDocumento(fichaDocumento);
				String remitidoPor = array[30] != Constantes.NULO
						? array[30].toString().replaceAll("@@", Constantes.VACIO).trim()
						: Constantes.CODIGO_SEPARADOR.concat(Constantes.VACIO);
				doc.setRemitente(remitidoPor.endsWith(",") ? remitidoPor.substring(0, remitidoPor.length() - 1) : remitidoPor);
				Empleado empleadoOrigen = new Empleado();
				empleadoOrigen.setId(array[3] != Constantes.NULO ? Integer.parseInt(array[3].toString()) : Constantes.CERO);
				empleadoOrigen.setDescripcion(array[32] != Constantes.NULO ? array[32].toString() : Constantes.VACIO);
				doc.setEmpleadoOrigen(empleadoOrigen);
				CentroCosto centroCostoOrigen = new CentroCosto();
				centroCostoOrigen.setId(array[18] != Constantes.NULO ? array[18].toString() : Constantes.VACIO);
				centroCostoOrigen.setDescripcion(array[42] != Constantes.NULO ? array[43].toString() : Constantes.VACIO);
				doc.setCentroCostoOrigen(centroCostoOrigen);
				Empleado empleadoDestino = new Empleado();
				empleadoDestino.setId(array[4] != Constantes.NULO ? Integer.parseInt(array[4].toString()) : Constantes.CERO);
				empleadoDestino.setDescripcion(array[29] != Constantes.NULO ? array[29].toString() : Constantes.VACIO);
				doc.setEmpleadoDestino(empleadoDestino);
				CentroCosto centroCostoDestino = new CentroCosto();
				centroCostoDestino.setId(array[19] != Constantes.NULO ? array[19].toString() : Constantes.VACIO);
				centroCostoDestino.setDescripcion(array[43] != Constantes.NULO ? array[43].toString() : Constantes.VACIO);
				doc.setCentroCostoDestino(centroCostoDestino);
				if (array[7] != Constantes.NULO) {
					MotivoDeriva motivoDeriva = new MotivoDeriva();
					motivoDeriva.setId(Integer.parseInt(array[7].toString()));
					doc.setMotivoDeriva(motivoDeriva);
				}
				if (array[8] != Constantes.NULO) {
					EstadoDeriva estadoDeriva = new EstadoDeriva();
					estadoDeriva.setId(Integer.parseInt(array[8].toString()));
					doc.setEstadoDeriva(estadoDeriva);
				}
				if (array[16] != Constantes.NULO) {
					FichaDocumento fichaRespuesta = new FichaDocumento();
					fichaRespuesta.setId(Integer.parseInt(array[16].toString()));
					Tipo estadoFichaRespuesta = new Tipo();
					estadoFichaRespuesta.setId(Integer.parseInt(array[44].toString()));
					fichaRespuesta.setTipoEstado(estadoFichaRespuesta);
					doc.setFichaRespuesta(fichaRespuesta);
				}
				doc.setTotal(array[46] != Constantes.NULO ? Integer.parseInt(array[46].toString()) : Constantes.CERO);
				doc.setDescSemana(array[47] != Constantes.NULO ? array[47].toString().trim() : Constantes.VACIO);
				doc.setEtapaMesaPartes(Constantes.ESTADO_DOCUMENTO_ENVIADO.equals(doc.getEstado().getId())
						|| Constantes.ESTADO_LEIDO.equals(doc.getEstado().getId()) ? Constantes.ETAPA_EN_BANDEJA_DE_ENVIO
								: Constantes.ETAPA_REGISTRADO);
				doc.setEtapaMesaPartes(array[33] != Constantes.NULO
						? Constantes.TIENE_REPORTE_EMITIDO.equals(array[33].toString().trim()) ? Constantes.ETAPA_EN_REPORTE
								: doc.getEtapaMesaPartes()
						: doc.getEtapaMesaPartes());
				doc.setEtapaDesMesaPartes(Constantes.ETAPA_ENVIO_MESA_PARTES.get(doc.getEtapaMesaPartes()));
				if (!fichas.containsKey(doc.getId())) {
					documentos.add(doc);
				}
				fichas.put(doc.getId(), doc.getId());
			}
		}
		Integer totalResultCount = 0;
		Integer total = 0;
		String descSemana = Constantes.VACIO;
		if ((fc.getNumeroMp() != null && !fc.getNumeroMp().isEmpty()) || (fc.getId() != null && !fc.getId().isEmpty())
				|| (fc.getSumilla() != null && !fc.getSumilla().isEmpty())
				|| (fc.getRemitidoPor() != null && !fc.getRemitidoPor().isEmpty())) {
			totalResultCount = documentos.size() > 0 ? documentos.get(0).getTotal() : 0;
			total = totalResultCount / Integer.parseInt(pagLength.get());
			if ((totalResultCount % Integer.parseInt(pagLength.get())) > 0) {
				total = total + 1;
			}
			if (totalResultCount > 0) {
				documentos.get(0).setDescSemana(Constantes.VACIO);
			}
		} else {
			totalResultCount = documentos.size() > 0 ? documentos.get(0).getTotal() : 0;
			Calendar calendar = Calendar.getInstance();
			total = calendar.get(Calendar.WEEK_OF_YEAR);
			if (calendar.get(Calendar.YEAR) > Constantes.ANIO_INICIO_MODULO_DE_MESA_PARTES) {
				total += (calendar.get(Calendar.YEAR) - Constantes.ANIO_INICIO_MODULO_DE_MESA_PARTES)
						* Constantes.TOTAL_SEMANAS_EN_UN_ANIO;
			}
			if (totalResultCount > 0) {
				documentos.get(0).setDescSemana(documentos.get(0).getDescSemana() + " TIENE: " + fichas.size() + " REGISTROS");
			}
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("documentos", documentos);
		result.put("descSemana", documentos.size() > 0 ? documentos.get(0).getDescSemana() : Constantes.VACIO);
		result.put("totalPage", total);
		result.put("totalResultCount", totalResultCount);
		return result;
	}

	@Override
	public List<Deriva> getDerivadosByCCOrigen(Integer id, String centroCostoIdOrigen) throws Exception {
		// TODO Auto-generated method stub
		Criteria criteria = createEntityCriteria();

		this.setFetchMode(criteria);
		criteria.createAlias("fichaDocumento", "f");
		criteria.createAlias("centroCostoOrigen", "cco");
		criteria.add(Restrictions.eq("f.id", id));
		criteria.add(Restrictions.eq("cco.id", centroCostoIdOrigen));
		criteria.add(Restrictions.eq("habilitado", true));
		criteria.add(Restrictions.eq("f.habilitado", true));
		criteria.add(Restrictions.eq("esResponsable", true));

		@SuppressWarnings("unchecked")
		List<Deriva> derivaList = criteria.list();
		return derivaList;

	}

	@Override
	public List<Deriva> findDirigidosTemplate(String fichaDocumentoId) throws Exception {
		Criteria criteria = createEntityCriteria();
		this.setFetchMode(criteria);
		criteria.createAlias("fichaDocumento", "f");
		criteria.add(Restrictions.eq("f.id", Integer.valueOf(fichaDocumentoId)));
		criteria.add(Restrictions.eq("habilitado", true));
		criteria.add(Restrictions.eq("dirigido", 1));
		criteria.add(Restrictions.eq("esResponsable", true));
		List<Deriva> derivaList = criteria.list();
		return derivaList;
	}
}
