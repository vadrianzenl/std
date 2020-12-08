package pe.gob.congreso.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;

import pe.gob.congreso.dao.FichaDocumentoDao;
import pe.gob.congreso.model.FichaDocumento;
import pe.gob.congreso.model.SpSgdValidaDespacho;
import pe.gob.congreso.model.util.InputSelectUtil;
import pe.gob.congreso.model.util.Util;
import pe.gob.congreso.util.Constantes;
import pe.gob.congreso.util.CriteriaDebug;
import pe.gob.congreso.util.FichaConsultas;
import pe.gob.congreso.util.FichaPedientes;
import pe.gob.congreso.util.PaginationHelper;

@Repository("fichaDocumentoDao")
public class FichaDocumentoDaoImpl extends AbstractDao<Integer, FichaDocumento> implements FichaDocumentoDao {

	private final PaginationHelper paginationHelper = new PaginationHelper();
	protected final Log log = LogFactory.getLog(getClass());

	public void setFetchMode(Criteria criteria) {
		criteria.setFetchMode("tipoRegistro", FetchMode.JOIN);
		criteria.setFetchMode("tipoDocumento", FetchMode.JOIN);
		criteria.setFetchMode("tipoEstado", FetchMode.JOIN);
		criteria.setFetchMode("anioLegislativo", FetchMode.JOIN);
		criteria.setFetchMode("legislatura", FetchMode.JOIN);
		criteria.setFetchMode("empleado", FetchMode.JOIN);
		criteria.setFetchMode("motivo", FetchMode.JOIN);
		criteria.setFetchMode("ubigeo", FetchMode.JOIN);
	}

	@Override
	public FichaDocumento create(FichaDocumento fd) throws Exception {
		saveOrUpdate(fd);
		return fd;
	}

	@Override
	public Map<String, Object> findBy(Optional<String> tipoRegistro, Optional<String> empleadoId, Optional<String> id,
			Optional<String> numeroDoc, Optional<String> asunto, Optional<String> fechaIniCrea, Optional<String> fechaFinCrea,
			Optional<String> tipoDocumento, Optional<String> tipoEstado, Optional<String> centroCosto,
			Optional<String> privado, Optional<String> indTipRep, Optional<String> remitidoDes, Optional<String> texto,
			Optional<String> referencia, Optional<String> observaciones, Optional<String> pag, Optional<String> pagLength,
			Optional<String> numeroMp, Optional<String> dependenciaId, Optional<String> anioLegislativo) throws Exception {
		Criteria criteria = createEntityCriteria();
		this.setFetchMode(criteria);
		criteria.setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("id"), "id")))
				.setResultTransformer(Transformers.aliasToBean(FichaDocumento.class));
		criteria.createAlias("tipoRegistro", "tr");
		criteria.createAlias("tipoDocumento", "td");
		criteria.createAlias("tipoEstado", "te");
		criteria.createAlias("empleado", "e");
		criteria.createAlias("centroCosto", "cc");
		if (numeroMp.isPresent() && !numeroMp.get().isEmpty()) {
			criteria.add(Restrictions.eq("numeroMp", Integer.parseInt(numeroMp.get())));
			System.out.println("numeroMp: " + numeroMp.get());
		}
		if (privado.isPresent() && !privado.get().isEmpty()) {
			criteria.add(Restrictions.eq("privado", Boolean.valueOf(privado.get())));
			System.out.println("privado: " + privado.get());
		}
		if (tipoRegistro.isPresent() && !tipoRegistro.get().isEmpty()) {
			criteria.add(Restrictions.eq("tr.id", Integer.valueOf(tipoRegistro.get())));
			System.out.println("tr.id: " + tipoRegistro.get());
		}
		if (empleadoId.isPresent() && !empleadoId.get().isEmpty()) {
			criteria.add(Restrictions.eq("e.id", Integer.valueOf(empleadoId.get())));
			System.out.println("e.id: " + empleadoId.get());
		}
		if (centroCosto.isPresent() && !centroCosto.get().isEmpty()) {
			criteria.add(Restrictions.eq("cc.id", centroCosto.get()));
			criteria.add(Restrictions.eq("centroCostoId", Integer.parseInt(centroCosto.get())));

			System.out.println("cc.id: " + centroCosto.get());
			System.out.println("centroCostoId: " + centroCosto.get());
		}
		if (remitidoDes.isPresent() && !remitidoDes.get().isEmpty()) {
			criteria.add(Restrictions.like("remitidoDes", Util.replaceAllTildes(remitidoDes.get()), MatchMode.ANYWHERE));
			System.out.println("remitidoDes: " + remitidoDes.get());
		}
		if (id.isPresent() && !id.get().isEmpty()) {
			criteria.add(Restrictions.eq("id", Integer.valueOf(id.get())));
			System.out.println("id: " + id.get());
		}
		if (indTipRep.isPresent() && !indTipRep.get().isEmpty() && (!indTipRep.get().equals("0"))) {
			criteria.add(Restrictions.eq("envioFinal", Integer.valueOf(indTipRep.get())));
			System.out.println("envioFinal: " + indTipRep.get());
		}
		if (numeroDoc.isPresent() && !numeroDoc.get().isEmpty()) {
			criteria.add(Restrictions.like("numeroDoc", numeroDoc.get(), MatchMode.ANYWHERE));
			System.out.println("numeroDoc: " + numeroDoc.get());
		}
		if (asunto.isPresent() && !asunto.get().isEmpty()) {
			criteria.add(Restrictions.like("asunto", Util.replaceAllTildes(asunto.get()), MatchMode.ANYWHERE));
			System.out.println("asunto: " + asunto.get());
		}
		if (referencia.isPresent() && !referencia.get().isEmpty()) {
			criteria.add(Restrictions.like("referencia", Util.replaceAllTildes(referencia.get()), MatchMode.ANYWHERE));
			System.out.println("referencia: " + referencia.get());
		}
		if (observaciones.isPresent() && !observaciones.get().isEmpty()) {
			criteria.add(Restrictions.like("observaciones", Util.replaceAllTildes(observaciones.get()), MatchMode.ANYWHERE));
			System.out.println("observaciones: " + observaciones.get());
		}
		if (fechaIniCrea.isPresent() && !fechaIniCrea.get().isEmpty()) {
			if (fechaFinCrea.isPresent()) {
				if (fechaFinCrea.get().isEmpty()) {
					criteria.add(Restrictions.sqlRestriction(" convert(varchar,{alias}.aud_dfecha_crea,23) = ? ",
							fechaIniCrea.get(), StringType.INSTANCE));
							System.out.println("aud_dfecha_crea: " + fechaIniCrea.get());
				}
			} else {
				criteria.add(Restrictions.sqlRestriction(" convert(varchar,{alias}.aud_dfecha_crea,23) = ? ",
						fechaIniCrea.get(), StringType.INSTANCE));
						System.out.println("aud_dfecha_crea: " + fechaIniCrea.get());
			}
		}
		if (fechaIniCrea.isPresent() && !fechaIniCrea.get().isEmpty()) {
			if (fechaFinCrea.isPresent()) {
				if (!fechaFinCrea.get().isEmpty()) {
					criteria.add(Restrictions.sqlRestriction("{alias}.aud_dfecha_crea >= CONVERT(datetime, ? ,120) ",
							fechaIniCrea.get(), StringType.INSTANCE));
							System.out.println("aud_dfecha_crea: " + fechaIniCrea.get());
					criteria
							.add(Restrictions.sqlRestriction("{alias}.aud_dfecha_crea < DATEADD(DAY,1,CONVERT(datetime, ? ,120)) ",
									fechaFinCrea.get(), StringType.INSTANCE));
									System.out.println("aud_dfecha_crea: " + fechaFinCrea.get());
					criteria.add(Restrictions.in("anio", new Integer[] { Integer.parseInt(fechaIniCrea.get().substring(0, 4)),
							Integer.parseInt(fechaFinCrea.get().substring(0, 4)) }));
							System.out.println("anio: " + fechaIniCrea.get().substring(0, 4));
							System.out.println("anio: " + fechaFinCrea.get().substring(0, 4));
				}
			}
		}
		if (!fechaIniCrea.isPresent() && !fechaFinCrea.isPresent()) {
			Calendar fecha = Calendar.getInstance();
			int anio = fecha.get(Calendar.YEAR);
			criteria.add(Restrictions.in("anio", new Integer[] { anio }));
			System.out.println("anio: " + anio);
		}
		criteria.createAlias("td.tipo", "tipo");
		if (tipoDocumento.isPresent() && !tipoDocumento.get().isEmpty()) {
			criteria.add(Restrictions.eq("tipo.id", Integer.valueOf(tipoDocumento.get())));
			System.out.println("tipo.id: " + tipoDocumento.get());
		}
		if (tipoEstado.isPresent() && !tipoEstado.get().isEmpty()) {
			criteria.add(Restrictions.eq("te.id", Integer.valueOf(tipoEstado.get())));
			System.out.println("te.id: " + tipoEstado.get());
		}
		if (texto.isPresent() && !texto.get().isEmpty()) {
			String[] palabras = texto.get().trim().split(" ");
			for (String pal : palabras) {
				log.info(pal);
				criteria.add(Restrictions.sqlRestriction(
						"ISNULL({alias}.stdf_cnumero_doc,'') + ISNULL({alias}.stdf_vasunto,'') + ISNULL({alias}.stdf_vreferencia,'') + ISNULL({alias}.stdef_vobservaciones,'') + ISNULL({alias}.stdf_remitidodes,'') + ISNULL(e4_.stde_vapellidos,'') + ISNULL(e4_.stde_vnombres,'') + ISNULL(tipo6_.stdt_vdescripcion,'') + ISNULL(te3_.stdt_vdescripcion,'') LIKE  '%"
								+ Util.replaceAllTildes(pal.trim()) + "%'"));
				System.out.println("all: " + pal);
			}
		}
		if (dependenciaId.isPresent() && !dependenciaId.get().isEmpty()) {
			criteria.createAlias("listDeriva", "ld");
			criteria.createAlias("ld.centroCostoDestino", "ccd");
			criteria.createAlias("ld.centroCostoOrigen", "cco");
			criteria.add(Restrictions.eq("ccd.id", dependenciaId.get()));
			System.out.println("ccd.id: " + dependenciaId.get());
			criteria.add(Restrictions.eq("cco.id", centroCosto.get()));
			System.out.println("cco.id: " + centroCosto.get());
		}
		criteria.createAlias("anioLegislativo", "anioLegis");
		if (anioLegislativo.isPresent() && !anioLegislativo.get().isEmpty()) {
			criteria.add(Restrictions.eq("anioLegis.id", Integer.parseInt(anioLegislativo.get())));
			System.out.println("anioLegis.id: " + anioLegislativo.get());
		}
		CriteriaDebug.debug(criteria);
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = paginationHelper.getPaginationDocumento(criteria, "id", pag, pagLength,
				Constantes.NO_ES_MESA_DE_PARTES);
		List<FichaDocumento> listaFichas = (List<FichaDocumento>) map.get("lista");
		result.put("documentos", listaFichas);
		result.put("totalPage", map.get("totalPage"));
		result.put("totalResultCount", map.get("totalResultCount"));
		return result;
	}

	@Override
	public FichaDocumento getFichaDocumentoId(Integer id) throws Exception {

		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("id", id));// Integer.parseInt(id)
		// criteria.add(Restrictions.eq("habilitado", true));

		FichaDocumento fichaDocumento = (FichaDocumento) criteria.uniqueResult();

		return fichaDocumento;
	}

	@Override
	public List<SpSgdValidaDespacho> validaparadespacho(Integer id) throws Exception {

		Session s = getSession();
		Query query = s.createSQLQuery("EXEC sp_sgd_valida_para_despacho :id").addEntity(SpSgdValidaDespacho.class)
				.setParameter("id", id);

		List<SpSgdValidaDespacho> result = query.list();
		return result;
	}

	@Override
	public Map<String, Object> getFichaDocumentoAnioLegislativo(String id) throws Exception {

		Criteria criteria = createEntityCriteria();

		criteria.createAlias("anioLegislativo", "al");
		criteria.add(Restrictions.eq("al.codigo", Integer.parseInt(id)));// Integer.parseInt(id)

		Map<String, Object> result = new HashMap<String, Object>();
		List<FichaDocumento> listaFichas = criteria.list();
		result.put("documentos", listaFichas);
		result.put("totalResultCount", listaFichas.size());

		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<FichaDocumento> getFichaDocumentoEmpleado(String empleadoId) throws Exception {

		Criteria criteria = createEntityCriteria();

		criteria.createAlias("empleado", "e");
		criteria.add(Restrictions.eq("e.id", Integer.parseInt(empleadoId)));// Integer.parseInt(id)

		List documentosList = criteria.list();

		return documentosList;
	}

	@Override
	public Map<String, Object> getFichaDocumentoEstafeta(FichaConsultas fc, Optional<String> pag,
			Optional<String> pagLength, List<InputSelectUtil> listDependencias) throws Exception {
		StringBuffer consulta = new StringBuffer();
		consulta.append("SELECT * FROM ( ");
		consulta.append("SELECT ");
		consulta.append("	    ROW_NUMBER() OVER(ORDER BY m.stdfp_inumero_mp DESC) as rowNr, ");
		consulta.append("	    m.stdfp_inumero_mp		as numeroMp, ");
		consulta.append("       m.stdf_id				as id, ");
		consulta.append("       m.aud_dfecha_crea		as fechaCrea, ");
		consulta.append("       m.stdf_cnumero_doc		as numeroDoc, ");
		consulta.append("       m.stdf_inumero_folios	as numeroFolios, ");
		consulta.append("       m.enviadoADes			as enviadoADes, ");
		consulta.append("       m.remitidoPor			as remitidoPor, ");
		consulta.append("       m.stdf_vasunto			as asunto, ");
		consulta.append("       m.registradoPorDes		as registradoPorDes, ");
		consulta.append("       m.fechaReporte			as fechaReporte, ");
		consulta.append("       m.stdf_cestafeta		as estafeta, ");
		consulta.append("       m.stdt_itipo_registro	as tipoRegistro, ");
		consulta.append("       m.stde_id				as empleadoId, ");
		consulta.append("       m.stdcc_id				as centroCosto, ");
		consulta.append("       m.stdcc_iid				as centroCostoId, ");
		consulta.append("       m.stdtd_id				as tipoDoc, ");
		consulta.append("       m.stdt_iestado			as tipoEstado, ");
		consulta.append("	    m.enviadoA				as enviadoA, ");
		consulta.append("	    COUNT(m.stdf_id) over() as total ");
		consulta.append("FROM ");
		consulta.append("  ( SELECT ");
		consulta.append("           fd.stdfp_inumero_mp, ");
		consulta.append("           fd.stdf_id, ");
		consulta.append("           fd.aud_dfecha_crea, ");
		consulta.append("           fd.stdf_cnumero_doc, ");
		consulta.append("           fd.stdf_inumero_folios, ");
		consulta.append(" ");
		consulta.append("     ( SELECT Rtrim(cc.stdcc_cdescripcion) + '@@' AS '*' ");
		consulta.append("      FROM std_enviado envio ");
		consulta.append("      INNER JOIN std_centro_costo cc ON (cc.stdcc_id = envio.stdcc_id_destino) ");
		consulta.append("      WHERE envio.stdd_bhabilitado = :habilitado ");
		consulta.append("        AND envio.stdf_id = fd.stdf_id ");
		consulta.append("    	 AND envio.stde_bresponsable = :esResponsable ");
		consulta.append("    	 AND envio.stdd_bdirigido != :dirigido ");
		consulta.append("      GROUP BY envio.stdf_id, ");
		consulta.append("               cc.stdcc_cdescripcion ");
		consulta.append("      FOR xml path('') )  as enviadoADes, ");
		consulta.append(" ");
		consulta.append("       (SELECT DISTINCT ");
		consulta
				.append("					CASE WHEN RTRIM(ee.stdee_vorigen) != RTRIM('') THEN ee.stdee_vorigen + ', ' ELSE '' END + ");
		consulta.append("					ee.stdee_vnombres + ' ' + ee.stdee_vapellidos + '@@' AS '*' ");
		consulta.append("    	      FROM std_enviado_externo ee ");
		consulta.append("    	      WHERE ee.stdf_id = fd.stdf_id ");
		consulta.append("    	        FOR xml path('') ) as remitidoPor,");
		consulta.append("           fd.stdf_vasunto, ");
		consulta.append(" ");
		consulta.append("     ( SELECT Rtrim(stde_vnombres) + ' ' + Rtrim(stde_vapellidos) ");
		consulta.append("      FROM std_empleado ");
		consulta.append("      WHERE stde_id = fd.stde_id ) registradoPorDes, ");
		consulta.append(" ");
		consulta.append("     ( SELECT T.fechaReporte + '@@' AS '*' ");
		consulta.append("      FROM ( ");
		consulta.append("              (SELECT DISTINCT em.stdf_id, ");
		consulta.append(
				"                               'ESTAFETA' + ': ' + Convert(varchar , em.aud_dfecha_modifica, 103) + ' ' + SUBSTRING(Convert(varchar , em.aud_dfecha_modifica, 108), 0, 6) + ' ' + SUBSTRING(CONVERT(varchar(20),em.aud_dfecha_modifica, 22), 18, 20) AS fechaReporte ");
		consulta.append("               FROM std_envio_multiple em ");
		consulta.append("               WHERE em.stddem_ccanal_envio = :canalEstafeta ");
		consulta.append("                 AND em.stddem_bhabilitado = :habilitado ");
		consulta.append("                 AND em.stdf_id = fd.stdf_id ) ");
		consulta.append("            UNION ALL ");
		consulta.append("              (SELECT DISTINCT em.stdf_id, ");
		consulta.append(
				"                               Rtrim(cc.stdcc_cdescripcion) + ': ' + Convert(varchar , em.aud_dfecha_modifica, 103) + ' ' + SUBSTRING(Convert(varchar , em.aud_dfecha_modifica, 108), 0, 6) + ' ' + SUBSTRING(CONVERT(varchar(20), em.aud_dfecha_modifica, 22), 18, 20) AS fechaReporte ");
		consulta.append("               FROM std_envio_multiple em ");
		consulta.append("               INNER JOIN std_centro_costo cc ON (cc.stdcc_id = stddem_idcentrocosto) ");
		consulta.append("               WHERE em.stddem_ccanal_envio = :canalCasillero ");
		consulta.append("                 AND em.stddem_bhabilitado = :habilitado ");
		consulta.append("                 AND em.stdf_id = fd.stdf_id )) T ");
		consulta.append("      FOR xml path('')) fechaReporte, ");
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
		consulta.append("           fd.stdf_cestafeta, ");
		consulta.append("           fd.stdt_itipo_registro, ");
		consulta.append("           fd.stde_id, ");
		consulta.append("           fd.stdcc_id, ");
		consulta.append("           fd.stdcc_iid, ");
		consulta.append("           fd.stdtd_id, ");
		consulta.append("           fd.stdt_iestado ");
		consulta.append("   FROM STD_FICHA_DOCUMENTO fd ");
		consulta.append("   INNER JOIN STD_TIPO tr ON fd.stdt_itipo_registro = tr.stdt_id ");
		consulta.append("   INNER JOIN STD_TIPO_DOCUMENTO td ON fd.stdtd_id = td.stdtd_id ");
		consulta.append("   INNER JOIN STD_TIPO te ON fd.stdt_iestado = te.stdt_id ");
		consulta.append("   INNER JOIN STD_TIPO tipo ON td.stdt_id = tipo.stdt_id ");
		consulta.append("   INNER JOIN STD_EMPLEADO e ON fd.stde_id = e.stde_id ");
		consulta.append("   INNER JOIN STD_CENTRO_COSTO cc ON fd.stdcc_id = cc.stdcc_id) m ");
		consulta.append("WHERE ");
		consulta.append("  m.stdf_cestafeta = :estafeta ");
		if (fc.getNumeroMp() != null && !fc.getNumeroMp().isEmpty()) {
			consulta.append("  AND m.stdfp_inumero_mp = :numeroMp ");
		}
		if (fc.getTipoRegistro() != null && !fc.getTipoRegistro().isEmpty()) {
			consulta.append("  AND m.stdt_itipo_registro = :tipoRegistro ");
		}
		if (fc.getRegistradoPor() != null && !fc.getRegistradoPor().isEmpty()) {
			consulta.append("  AND m.stde_id = :empleadoId ");
		}
		if (fc.getDependenciaOrigenId() != null && !fc.getDependenciaOrigenId().isEmpty()) {
			consulta.append("  AND m.stdcc_id = :centroCosto ");
			consulta.append("  AND m.stdcc_iid = :centroCostoId ");
		}
		if (fc.getId() != null && !fc.getId().isEmpty()) {
			consulta.append("  AND m.stdf_id = :id ");
		}
		if (fc.getNumeroDoc() != null && !fc.getNumeroDoc().isEmpty()) {
			consulta.append("  AND m.stdf_cnumero_doc = :numeroDoc ");
		}
		if (fc.getFechaIni() != null && !fc.getFechaIni().isEmpty() && fc.getFechaFin() == null) {
			consulta.append("  AND m.aud_dfecha_crea = CONVERT(datetime, :fechaIni, 120) ");
		}
		if (fc.getFechaIni() != null && !fc.getFechaIni().isEmpty() && fc.getFechaFin() != null
				&& !fc.getFechaFin().isEmpty()) {
			consulta.append("  AND m.aud_dfecha_crea >= CONVERT(datetime, :fechaIni, 120) ");
		}
		if (fc.getFechaIni() != null && !fc.getFechaIni().isEmpty() && fc.getFechaFin() != null
				&& !fc.getFechaFin().isEmpty()) {
			consulta.append("  AND m.aud_dfecha_crea < DATEADD(DAY, 1, CONVERT(datetime, :fechaFin, 120)) ");
		}
		if (fc.getTipoDoc() != null && !fc.getTipoDoc().isEmpty()) {
			consulta.append("  AND m.stdtd_id = :tipoDoc ");
		}
		if (fc.getTipoEstado() != null && !fc.getTipoEstado().isEmpty()) {
			consulta.append("  AND m.stdt_iestado = :tipoEstado ");
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
			consulta.append("  AND m.stdf_vasunto LIKE '%' + :asunto + '%'");
		}
		consulta.append("  ) T ");
		consulta.append("  WHERE ");
		consulta.append("  T.rowNr BETWEEN (((:pag - 1) * :pagLength) + 1) AND (:pag * :pagLength)");

		Query query = this.getSession().createSQLQuery(consulta.toString()).addScalar("rowNr", new IntegerType())
				.addScalar("numeroMp", new IntegerType()).addScalar("id", new IntegerType())
				.addScalar("fechaCrea", new TimestampType()).addScalar("numeroDoc", new StringType())
				.addScalar("numeroFolios", new IntegerType()).addScalar("enviadoADes", new StringType())
				.addScalar("remitidoPor", new StringType()).addScalar("asunto", new StringType())
				.addScalar("registradoPorDes", new StringType()).addScalar("fechaReporte", new StringType())
				.addScalar("estafeta", new StringType()).addScalar("tipoRegistro", new IntegerType())
				.addScalar("empleadoId", new IntegerType()).addScalar("centroCosto", new StringType())
				.addScalar("centroCostoId", new IntegerType()).addScalar("tipoDoc", new IntegerType())
				.addScalar("tipoEstado", new IntegerType()).addScalar("enviadoA", new StringType())
				.addScalar("total", new IntegerType());
		query.setParameter("habilitado", Constantes.HABILITADO);
		query.setParameter("canalEstafeta", Constantes.ENVIO_PARA_ESTAFETA);
		query.setParameter("canalCasillero", Constantes.ENVIO_PARA_CASILLERO);
		query.setParameter("pag", Integer.parseInt(pag.get()));
		query.setParameter("pagLength", Integer.parseInt(pagLength.get()));
		query.setParameter("estafeta", Constantes.DOCUMENTO_REGISTRADO_POR_MESA_DE_PARTES);
		query.setParameter("esResponsable", Constantes.ENVIADO_AL_RESPONSABLE);
		query.setParameter("dirigido", Constantes.DOCUMENTO_RECIBIDO_FISICO);
		if (fc.getNumeroMp() != null && !fc.getNumeroMp().isEmpty()) {
			query.setParameter("numeroMp", fc.getNumeroMp());
		}
		if (fc.getId() != null && !fc.getId().isEmpty()) {
			query.setParameter("id", fc.getId());
		}
		if (fc.getTipoRegistro() != null && !fc.getTipoRegistro().isEmpty()) {
			query.setParameter("tipoRegistro", fc.getTipoRegistro());
		}
		if (fc.getRegistradoPor() != null && !fc.getRegistradoPor().isEmpty()) {
			query.setParameter("empleadoId", fc.getRegistradoPor());
		}
		if (fc.getDependenciaOrigenId() != null && !fc.getDependenciaOrigenId().isEmpty()) {
			query.setParameter("centroCosto", fc.getDependenciaOrigenId());
			query.setParameter("centroCostoId", Integer.parseInt(fc.getDependenciaOrigenId()));
		}
		if (fc.getNumeroDoc() != null && !fc.getNumeroDoc().isEmpty()) {
			query.setParameter("numeroDoc", fc.getNumeroDoc());
		}
		if (fc.getFechaIni() != null && !fc.getFechaIni().isEmpty()) {
			query.setParameter("fechaIni", fc.getFechaIni());
		}
		if (fc.getFechaFin() != null && !fc.getFechaFin().isEmpty()) {
			query.setParameter("fechaFin", fc.getFechaFin());
		}
		if (fc.getTipoDoc() != null && !fc.getTipoDoc().isEmpty()) {
			query.setParameter("tipoDoc", fc.getTipoDoc());
		}
		if (fc.getTipoEstado() != null && !fc.getTipoEstado().isEmpty()) {
			query.setParameter("tipoEstado", fc.getTipoEstado());
		}
		if (listDependencias != null && !listDependencias.isEmpty()) {
			int i = 0;
			for (InputSelectUtil d : listDependencias) {
				String enviadoA = "enviadoA" + i;
				query.setParameter(enviadoA, d.getValue().toString());
				i++;
			}
		}
		if (fc.getDependenciaDestinoId() != null && !fc.getDependenciaDestinoId().isEmpty()) {
			query.setParameter("enviadoA", fc.getDependenciaDestinoId());
		}
		if (fc.getRemitidoPor() != null && !fc.getRemitidoPor().isEmpty()) {
			query.setParameter("remitidoPor", fc.getRemitidoPor().replaceAll(" ", "%"));
		}
		if (fc.getSumilla() != null && !fc.getSumilla().isEmpty()) {
			query.setParameter("asunto", fc.getSumilla().replaceAll(" ", "%"));
		}

		List listaQuery = query.list();
		List<FichaPedientes> listFicha = new ArrayList<>();
		for (Object obj : listaQuery) {
			Object[] array = (Object[]) obj;
			FichaPedientes f = new FichaPedientes();
			f.setNumeroMp(array[1] != Constantes.NULO ? Integer.parseInt(array[1].toString().trim()) : Constantes.CERO);
			f.setId(array[2] != Constantes.NULO ? Integer.parseInt(array[2].toString().trim()) : Constantes.CERO);
			f.setFechaCrea(array[3] != Constantes.NULO
					? new SimpleDateFormat(Constantes.FORMATO_FECHA_USUARIO_DE_MESA_PARTES).format(array[3])
					: Constantes.VACIO);
			f.setNumeroDoc(array[4] != Constantes.NULO ? array[4].toString() : Constantes.VACIO);
			f.setNumDerivados(array[6] != Constantes.NULO && array[6].toString().split(Constantes.CODIGO_SEPARADOR).length > 0
					? array[6].toString().split(Constantes.CODIGO_SEPARADOR).length
					: Constantes.CERO);
			f.setEnviadoADes(
					array[6] != Constantes.NULO ? array[6].toString() : Constantes.CODIGO_SEPARADOR.concat(Constantes.VACIO));
			f.setRemitidoPor(
					array[7] != Constantes.NULO ? array[7].toString() : Constantes.CODIGO_SEPARADOR.concat(Constantes.VACIO));
			f.setAsuntoMp(array[8] != Constantes.NULO ? array[8].toString() : Constantes.VACIO);
			f.setRegistradoPorDes(array[9] != Constantes.NULO ? array[9].toString() : Constantes.VACIO);
			f.setFechaEnvioReporte(array[10] != Constantes.NULO ? array[10].toString() : Constantes.VACIO);
			f.setEstafeta(array[11] != Constantes.NULO ? array[11].toString() : Constantes.VACIO);
			f.setTipoRegistro(array[12] != Constantes.NULO ? array[12].toString() : Constantes.VACIO);
			f.setEmpleadoId(array[13] != Constantes.NULO ? array[13].toString() : Constantes.VACIO);
			f.setCentroCosto(array[14] != Constantes.NULO ? array[14].toString() : Constantes.VACIO);
			f.setCentroCostoId(array[15] != Constantes.NULO ? array[15].toString() : Constantes.VACIO);
			f.setTipoDoc(array[16] != Constantes.NULO ? array[16].toString() : Constantes.VACIO);
			f.setTipoEstado(array[17] != Constantes.NULO ? array[17].toString() : Constantes.VACIO);
			f.setEnviadoA(array[18] != Constantes.NULO ? array[18].toString() : Constantes.VACIO);
			f.setTotal(Integer.parseInt(array[19] != Constantes.NULO ? array[19].toString().trim() : Constantes.VACIO));
			listFicha.add(f);
		}

		Integer totalResultCount = listFicha.size() > 0 ? listFicha.get(0).getTotal() : 0;
		Integer total = totalResultCount / Integer.parseInt(pagLength.get());
		if ((totalResultCount % Integer.parseInt(pagLength.get())) > 0) {
			total = total + 1;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("listFicha", listFicha);
		result.put("totalPage", total);
		result.put("totalResultCount", totalResultCount);
		return result;
	}

	@Override
	public List<FichaDocumento> getFichaDocumentoByListId(List<Integer> listId, String fichaDocumentoId)
			throws Exception {

		Criteria criteria = createEntityCriteria();
		this.setFetchMode(criteria);

		criteria.add(Restrictions.in("id", listId));
		// criteria.add(Restrictions.ne("id", Integer.valueOf(fichaDocumentoId)));
		criteria.add(Restrictions.eq("habilitado", true));
		criteria.addOrder(Order.desc("id"));

		List<FichaDocumento> documentosList = criteria.list();

		return documentosList;
	}

	@Override
	public FichaDocumento getFichaDocumentoId(Integer id, String centroCostoId) throws Exception {

		Criteria criteria = createEntityCriteria();
		criteria.createAlias("centroCosto", "cc");
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("habilitado", true));
		criteria.add(Restrictions.eq("cc.id", centroCostoId.trim()));

		FichaDocumento fichaDocumento = (FichaDocumento) criteria.uniqueResult();

		return fichaDocumento;
	}

	@Override
	@Transactional(readOnly = true)
	public List<FichaPedientes> getListEnviosPendientes(Map parametros) throws Exception {
		List<FichaPedientes> lista = new ArrayList<>();
		StringBuffer consulta = new StringBuffer();
		consulta.append("SELECT fp.stdfp_inumero                        numeroMp, ");
		consulta.append("       fp.stdfp_vsumilla                       asuntoMp, ");
		consulta.append("       fd.stdf_id                              id, ");
		consulta.append("       fd.aud_dfecha_crea                      fechaCrea, ");
		consulta.append("       fd.stdf_cnumero_doc                     numeroDoc, ");
		consulta.append("       (SELECT tipo.stdt_vdescripcion ");
		consulta.append("        FROM   std_tipo tipo ");
		consulta.append("               INNER JOIN std_tipo_documento tipoDoc ");
		consulta.append("                       ON ( tipo.stdt_id = tipoDoc.stdt_id ) ");
		consulta.append("        WHERE  tipoDoc.stdtd_id = fd.stdtd_id) tipoDoc, ");
		consulta.append("       fd.stdf_inumero_folios                  numeroFolios, ");
		consulta.append("       (SELECT DISTINCT ");
		consulta
				.append("					CASE WHEN RTRIM(ee.stdee_vorigen) != RTRIM('') THEN ee.stdee_vorigen + ', ' ELSE '' END + ");
		consulta.append("					ee.stdee_vnombres + ' ' + ee.stdee_vapellidos + '@@' AS '*' ");
		consulta.append("    	      FROM std_enviado_externo ee ");
		consulta.append("    	      WHERE ee.stdf_id = fd.stdf_id ");
		consulta.append("    	        FOR xml path('') ) as remitidoPor,");
		consulta.append("       (SELECT Rtrim(cc.stdcc_cdescripcion) + '@@' AS '*' ");
		consulta.append("        FROM   std_envio_multiple envio ");
		consulta.append("               INNER JOIN std_centro_costo cc ");
		consulta.append("                       ON ( cc.stdcc_id = envio.stddem_idcentrocosto ) ");
		consulta.append("               INNER JOIN std_enviado enviadoMaster ");
		consulta.append("                       ON ( envio.stdf_id = enviadoMaster.stdf_id ) ");
		consulta.append("        WHERE  envio.stddem_bhabilitado = :habilitado ");
		consulta.append("               AND envio.stddem_breporte = :reporte ");
		consulta.append("               AND envio.stdf_id = fd.stdf_id ");
		consulta.append("               AND envio.stddem_ccanal_envio = :canalEnvio ");
		consulta.append("               AND enviadoMaster.stde_bresponsable = :esResponsable ");
		consulta.append("    			AND enviadoMaster.stdd_bdirigido != :dirigido ");
		if (parametros.containsKey("centroCostoId")) {
			consulta.append("               AND envio.stddem_idcentrocosto = :centroCostoId ");
		}
		consulta.append("        GROUP  BY envio.stdf_id, cc.stdcc_id, ");
		consulta.append("                  cc.stdcc_cdescripcion ");
		consulta.append("        FOR xml path(''))                      enviadoADes, ");
		consulta.append("       (SELECT Rtrim(cc.stdcc_id) + '@@' AS '*' ");
		consulta.append("        FROM   std_envio_multiple envio ");
		consulta.append("               INNER JOIN std_centro_costo cc ");
		consulta.append("                       ON ( cc.stdcc_id = envio.stddem_idcentrocosto ) ");
		consulta.append("               INNER JOIN std_enviado enviadoMaster ");
		consulta.append("                       ON ( envio.stdf_id = enviadoMaster.stdf_id ) ");
		consulta.append("        WHERE  envio.stddem_bhabilitado = :habilitado ");
		consulta.append("               AND envio.stddem_breporte = :reporte ");
		consulta.append("               AND envio.stdf_id = fd.stdf_id ");
		consulta.append("               AND envio.stddem_ccanal_envio = :canalEnvio ");
		consulta.append("               AND enviadoMaster.stde_bresponsable = :esResponsable ");
		consulta.append("    			AND enviadoMaster.stdd_bdirigido != :dirigido ");
		if (parametros.containsKey("centroCostoId")) {
			consulta.append("               AND envio.stddem_idcentrocosto = :centroCostoId ");
		}
		consulta.append("        GROUP  BY envio.stdf_id, cc.stdcc_id, ");
		consulta.append("                  cc.stdcc_cdescripcion ");
		consulta.append("        FOR xml path(''))                      enviadoA, ");
		consulta.append("       (SELECT Rtrim(stde_vnombres) + ' ' ");
		consulta.append("               + Rtrim(stde_vapellidos) ");
		consulta.append("        FROM   std_empleado ");
		consulta.append("        WHERE  stde_id = fd.stde_id)           registradoPorDes, ");
		consulta.append("       '0'                                     seleccion, ");
		consulta.append("       (SELECT Count(stdem_id) ");
		consulta.append("        FROM   std_envio_multiple ");
		consulta.append("        WHERE  stdf_id = fd.stdf_id ");
		if (parametros.containsKey("reporteId")) {
			consulta.append("               AND stddem_gestionenvioid IS NOT NULL ");
		} else {
			consulta.append("               AND stddem_gestionenvioid IS NULL ");
		}
		consulta.append("               AND stddem_ccanal_envio = :canalEnvio ");
		if (parametros.containsKey("centroCostoId")) {
			consulta.append("               AND stddem_idcentrocosto = :centroCostoId ");
		}
		consulta.append("               AND stddem_bhabilitado = :habilitado)     numDerivados, ");
		consulta.append("       (SELECT CASE ");
		consulta.append("                 WHEN EXISTS (SELECT stdem_id ");
		consulta.append("                              FROM   std_envio_multiple ");
		consulta.append("                              WHERE  stdf_id = fd.stdf_id ");
		consulta.append("                                     AND stddem_ccanal_envio != :canalEnvio ");
		consulta.append("                                     AND stddem_bhabilitado = :habilitado) THEN 1 ");
		consulta.append("                 ELSE 0 ");
		consulta.append("               END)                            indEnvioMultiple ");
		consulta.append("FROM   std_ficha_proveido fp ");
		consulta.append("       INNER JOIN std_ficha_documento fd ");
		consulta.append("               ON ( fp.stdf_id = fd.stdf_id and fp.stdp_id = :proveidoId) ");
		if (parametros.containsKey("reporteId")) {
			consulta.append("       INNER JOIN std_etapa_gestion_envio et ");
			consulta.append("         ON ( fd.stdf_id = et.stdf_id ) ");
			consulta.append("       INNER JOIN std_gestion_envio ge ");
			consulta.append("         ON ( et.stdge_id = ge.stdge_id ) ");
		}
		consulta.append("WHERE ");
		if (parametros.containsKey("reporteId")) {
			consulta.append("ge.stdge_id = :reporteId ");
		} else {
			consulta.append("fp.stdfp_cestado IN ( :pendientes ) ");
		}
		consulta.append("       AND fp.stdfp_bhabilitado = :habilitado ");
		consulta.append("       AND fp.stdfp_ippini >= :ppini ");
		consulta.append("       AND fp.stdfp_ippfin <= :ppfin ");
		consulta.append("       AND fd.stdf_bhabilitado = :habilitado ");
		consulta.append("       AND fd.stdt_iestado IN ( :estados )");
		if (!parametros.containsKey("reporteId")) {
			if (Constantes.ENVIO_PARA_ESTAFETA.toString().equals(parametros.get("canalEnvio").toString().trim())) {
				consulta.append("       AND fd.aud_cusuario_crea = :usuario ");
				consulta.append("       AND  EXISTS (SELECT stdem_id ");
				consulta.append("                              FROM   std_envio_multiple ");
				consulta.append("                              WHERE  stdf_id = fd.stdf_id ");
				consulta.append("									 AND stddem_gestionenvioid IS NULL ");
				consulta.append("                                    AND stddem_ccanal_envio = :canalEnvio ");
				consulta.append("                                    AND stddem_bhabilitado = :habilitado ");
				consulta.append("									 AND aud_cusuario_crea = :usuario )");
			} else if (Constantes.ENVIO_PARA_CASILLERO.toString().equals(parametros.get("canalEnvio").toString().trim())) {
				consulta.append("       AND  EXISTS (SELECT stdem_id ");
				consulta.append("                              FROM   std_envio_multiple ");
				consulta.append("                              WHERE  stdf_id = fd.stdf_id ");
				consulta.append("									 AND stddem_gestionenvioid IS NULL ");
				consulta.append("                                    AND stddem_ccanal_envio = :canalEnvio  ");
				consulta.append("                                    AND stddem_bhabilitado = :habilitado ");
				if (parametros.containsKey("centroCostoId")) {
					consulta.append("									 AND stddem_idcentrocosto = :centroCostoId )");
				} else {
					consulta.append("                                    ) ");
				}
			}
		}
		consulta.append("ORDER BY  fp.stdfp_inumero  DESC");

		Query query = this.getSession().createSQLQuery(consulta.toString()).addScalar("numeroMp", new StringType())
				.addScalar("asuntoMp", new StringType()).addScalar("id", new IntegerType())
				.addScalar("fechaCrea", new TimestampType()).addScalar("numeroDoc", new StringType())
				.addScalar("tipoDoc", new StringType()).addScalar("numeroFolios", new IntegerType())
				.addScalar("remitidoPor", new StringType()).addScalar("enviadoADes", new StringType())
				.addScalar("enviadoA", new StringType()).addScalar("registradoPorDes", new StringType())
				.addScalar("seleccion", new BooleanType()).addScalar("numDerivados", new IntegerType())
				.addScalar("indEnvioMultiple", new BooleanType());
		query.setParameter("canalEnvio", parametros.get("canalEnvio"));
		if (parametros.containsKey("reporteId")) {
			query.setParameter("reporteId", parametros.get("reporteId"));
		} else {
			query.setParameterList("pendientes", (Object[]) parametros.get("pendientes").toString().split(","),
					new StringType());
		}
		query.setParameter("habilitado", parametros.get("habilitado"));
		query.setParameter("ppini", parametros.get("ppini"));
		query.setParameter("ppfin", parametros.get("ppfin"));
		query.setParameter("proveidoId", parametros.get("proveidoId"));
		query.setParameter("reporte", parametros.get("reporte"));
		query.setParameter("esResponsable", Constantes.ENVIADO_AL_RESPONSABLE);
		query.setParameter("dirigido", Constantes.DOCUMENTO_RECIBIDO_FISICO);
		query.setParameterList("estados", (Object[]) parametros.get("estados").toString().split(","), new StringType());
		if (Constantes.ENVIO_PARA_ESTAFETA.toString().equals(parametros.get("canalEnvio").toString().trim())) {
			if (!parametros.containsKey("reporteId")) {
				query.setParameter("usuario", parametros.get("usuario"));
			}

		}
		if (parametros.containsKey("centroCostoId")) {
			query.setParameter("centroCostoId", parametros.get("centroCostoId"));
		}

		List listaQuery = query.list();
		for (Object obj : listaQuery) {
			Object[] array = (Object[]) obj;
			if (array[8] != null) {
				FichaPedientes f = new FichaPedientes();
				f.setNumeroMp(array[0] != Constantes.NULO ? Integer.parseInt(array[0].toString()) : Constantes.CERO);
				f.setAsuntoMp(array[1] != Constantes.NULO ? array[1].toString() : Constantes.VACIO);
				f.setId(array[2] != Constantes.NULO ? Integer.parseInt(array[2].toString()) : Constantes.CERO);
				f.setFechaCrea(array[3] != Constantes.NULO ? array[3].toString() : Constantes.VACIO);
				f.setNumeroDoc(array[4] != Constantes.NULO ? array[4].toString() : Constantes.VACIO);
				f.setTipoDoc(array[5] != Constantes.NULO ? array[5].toString() : Constantes.VACIO);
				f.setNumeroFolios(array[6] != Constantes.NULO ? Integer.parseInt(array[6].toString()) : Constantes.CERO);
				f.setRemitidoPor(
						array[7] != Constantes.NULO ? array[7].toString() : Constantes.CODIGO_SEPARADOR.concat(Constantes.VACIO));
				f.setEnviadoADes(
						array[8] != Constantes.NULO ? array[8].toString() : Constantes.CODIGO_SEPARADOR.concat(Constantes.VACIO));
				f.setEnviadoA(
						array[9] != Constantes.NULO ? array[9].toString() : Constantes.CODIGO_SEPARADOR.concat(Constantes.VACIO));
				f.setRegistradoPorDes(array[10] != Constantes.NULO ? array[10].toString() : Constantes.VACIO);
				f.setSeleccion(array[11] != Constantes.NULO ? Boolean.parseBoolean(array[11].toString()) : Constantes.FALSO);
				f.setNumDerivados(array[12] != Constantes.NULO ? Integer.parseInt(array[12].toString()) : Constantes.CERO);
				f.setIndEnvioMultiple(
						array[13] != Constantes.NULO ? Boolean.parseBoolean(array[13].toString()) : Constantes.FALSO);
				lista.add(f);
			}
		}
		return lista;
	}

	@Override
	@Transactional(readOnly = true)
	public Integer updateEstafeta(FichaDocumento fd) throws Exception {
		Query query = this.getSession().createSQLQuery(
				"update STD_FICHA_DOCUMENTO set stdf_cestafeta = :estafeta, aud_cusuario_modifica = :usuarioModifica, aud_dfecha_modifica = :fechaModifica "
						+ " where stdf_id = :id");
		query.setParameter("id", fd.getId());
		query.setParameter("estafeta", fd.getEstafeta());
		query.setParameter("usuarioModifica", fd.getUsuarioModifica());
		query.setParameter("fechaModifica", fd.getFechaModifica());
		int result = query.executeUpdate();
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<FichaDocumento> getListFichaDocumentosEnviadosMp(String centroCostoId, String empleadoId,
			String indEstafeta) throws Exception {
		Criteria criteria = createEntityCriteria();
		if (centroCostoId != null) {
			criteria.createAlias("centroCosto", "cc");
			criteria.add(Restrictions.eq("cc.id", centroCostoId.trim()));
		}
		if (empleadoId != null) {
			criteria.createAlias("empleado", "e");
			criteria.add(Restrictions.eq("e.id", Integer.parseInt(empleadoId)));
		}
		criteria.add(Restrictions.eq("habilitado", true));
		criteria.add(Restrictions.eq("estafeta", indEstafeta));
		criteria.addOrder(Order.desc("id"));

		List documentosList = criteria.list();

		return documentosList;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<FichaDocumento> getListFichaDocumentosEnviadosMpExcluyendoEmpleado(String centroCostoId,
			String empleadoId, String indEstafeta) throws Exception {
		Criteria criteria = createEntityCriteria();
		if (centroCostoId != null) {
			criteria.createAlias("centroCosto", "cc");
			criteria.add(Restrictions.eq("cc.id", centroCostoId.trim()));
		}
		if (empleadoId != null) {
			criteria.createAlias("empleado", "e");
			criteria.add(Restrictions.ne("e.id", Integer.parseInt(empleadoId)));
		}
		criteria.add(Restrictions.eq("habilitado", true));
		criteria.add(Restrictions.eq("estafeta", indEstafeta));
		criteria.addOrder(Order.desc("id"));

		List documentosList = criteria.list();

		return documentosList;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<InputSelectUtil> getListDependenciasRecibidos(Map parametros) throws Exception {
		List<InputSelectUtil> lista = new ArrayList<>();
		StringBuffer consulta = new StringBuffer();
		// Comentado AEP 11.09.2019
		/*
		 * consulta.append("SELECT stdcc_id_origen								idCentroCosto, ");
		 * consulta.append("       (SELECT stdcc_cdescripcion ");
		 * consulta.append("        FROM   std_centro_costo ");
		 * consulta.append("        WHERE  stdcc_id = stdcc_id_origen) 		descripcion, "
		 * );
		 * consulta.append("       (SELECT CONVERT(VARCHAR(10), stdf_id) + ',' AS '*' "
		 * ); consulta.append("        FROM   std_ficha_documento ");
		 * consulta.append("        WHERE  stdcc_id = stdcc_id_origen ");
		 * consulta.append("               AND stdf_bhabilitado = :habilitado ");
		 * consulta.append("        ORDER  BY stdf_id DESC ");
		 * consulta.append("        FOR xml path(''))                  		fichasIds ");
		 * consulta.append("FROM   std_enviado ");
		 * consulta.append("WHERE  stdd_bhabilitado = :habilitado ");
		 * consulta.append("       AND stdcc_id_destino = :idCentroCosto ");
		 * consulta.append("GROUP  BY stdcc_id_origen");
		 */
		// Agregado AEP 11.09.2019
		consulta.append("SELECT stdcc_id idCentroCosto, ");
		consulta.append("       stdcc_cdescripcion descripcion, '00' fichasIds ");
		consulta.append("FROM   STD_GRUPO_CENTRO_COSTO ");
		consulta.append("WHERE  stdcc_bhabilitado = 1 ");
		consulta.append("ORDER BY  stdcc_cdescripcion asc");

		Query query = this.getSession().createSQLQuery(consulta.toString()).addScalar("idCentroCosto", new StringType())
				.addScalar("descripcion", new StringType()).addScalar("fichasIds", new StringType());
		// query.setParameter("idCentroCosto", parametros.get("idCentroCosto") );
		// //Comentado AEP 11.09.2019
		// query.setParameter("habilitado", parametros.get("habilitado") ); //Comentado
		// AEP 11.09.2019
		List listaQuery = query.list();
		for (Object obj : listaQuery) {
			Object[] array = (Object[]) obj;
			if (array[2] != null) {
				InputSelectUtil i = new InputSelectUtil();
				i.setValue(array[0].toString());
				StringBuffer sb = new StringBuffer();
				sb.append(array[1].toString());
				i.setLabel(sb.toString());
				lista.add(i);
			}
		}
		return lista;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<InputSelectUtil> getListDependenciasEnviados(Map parametros) throws Exception {
		List<InputSelectUtil> lista = new ArrayList<>();
		StringBuffer consulta = new StringBuffer();
		consulta.append("SELECT stdcc_id_destino								idCentroCosto, ");
		consulta.append("       (SELECT stdcc_cdescripcion ");
		consulta.append("        FROM   std_centro_costo ");
		consulta.append("        WHERE  stdcc_id = stdcc_id_destino) 			descripcion, ");
		consulta.append("       (SELECT CONVERT(VARCHAR(10), stdf_id) + ',' AS '*' ");
		consulta.append("        FROM   std_ficha_documento ");
		consulta.append("        WHERE  stdcc_id = stdcc_id_destino ");
		consulta.append("               AND stdf_bhabilitado = :habilitado ");
		consulta.append("        ORDER  BY stdf_id DESC ");
		consulta.append("        FOR xml path(''))                   			fichasIds ");
		consulta.append("FROM   std_enviado ");
		consulta.append("WHERE  stdd_bhabilitado = :habilitado ");
		consulta.append("       AND stdcc_id_origen = :idCentroCosto ");
		consulta.append("GROUP  BY stdcc_id_destino");
		Query query = this.getSession().createSQLQuery(consulta.toString()).addScalar("idCentroCosto", new StringType())
				.addScalar("descripcion", new StringType()).addScalar("fichasIds", new StringType());
		query.setParameter("idCentroCosto", parametros.get("idCentroCosto"));
		query.setParameter("habilitado", parametros.get("habilitado"));
		List listaQuery = query.list();
		for (Object obj : listaQuery) {
			Object[] array = (Object[]) obj;
			if (array[2] != null) {
				InputSelectUtil i = new InputSelectUtil();
				i.setValue(array[0].toString());
				StringBuffer sb = new StringBuffer();
				sb.append(array[1].toString());
				i.setLabel(sb.toString());
				lista.add(i);
			}
		}
		return lista;
	}

}
