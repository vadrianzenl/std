package pe.gob.congreso.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.FisicoDao;
import pe.gob.congreso.model.SeguimientoFisico;
import pe.gob.congreso.util.Constantes;
import pe.gob.congreso.util.FichaPedientes;
import pe.gob.congreso.util.SeguimientoFisicoUtil;

@Repository("fisicoDao")
public class FisicoDaoImpl extends AbstractDao<Integer, SeguimientoFisico> implements FisicoDao {

    protected final Log log = LogFactory.getLog(getClass());
    
	@Override
	public SeguimientoFisico create(SeguimientoFisico fisico) throws Exception {
		// TODO Auto-generated method stub
		saveOrUpdate(fisico);
	    return fisico;
	}

	@Override
	public List<SeguimientoFisico> findRecibiFisico(String derivaId,String estado) throws Exception {
		// TODO Auto-generated method stub
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("derivaId",Integer.parseInt(derivaId)));
		criteria.add(Restrictions.eq("estado", estado.toString()));
		criteria.add(Restrictions.eq("habilitado",Constantes.HABILITADO));
	    List derivaList = criteria.list();
	    return derivaList;
	}

	@Override
	public List<SeguimientoFisicoUtil> findDirigidosByFichaId(String fichaDocumentoId) {
		// TODO Auto-generated method stub
		StringBuffer dirigidos = new StringBuffer();
		dirigidos.append(Constantes.INDICADOR_DIRIGIDO).append(",").append(Constantes.INDICADOR_DERIVADO);
		
		StringBuffer  consulta = new StringBuffer();
		consulta.append("SELECT ");
		consulta.append("  e.stdd_id AS derivaId, ");
		consulta.append("  CASE ");
		consulta.append("    WHEN EXISTS (SELECT 1 FROM STD_GRUPO_CENTRO_COSTO gcc ");
		consulta.append("      WHERE (gcc.stdcc_iresponsable = e.stde_id_destino)) ");
		consulta.append("		THEN (SELECT CASE e.stde_bcopia ");
		consulta.append("				WHEN 1 THEN 'cc:' +  gc.stdcc_cdescripcion ");
		consulta.append("			   ELSE '' +  gc.stdcc_cdescripcion ");
		consulta.append("			  END ");
		consulta.append("			  FROM STD_GRUPO_CENTRO_COSTO gc ");
		consulta.append("			  WHERE gc.stdcc_id = e.stdcc_id_idestino) ");
		consulta.append("		ELSE (SELECT ");
		consulta.append("        CASE e.stde_bcopia ");
		consulta.append("          WHEN 1 THEN 'CC:' ");
		consulta.append("          ELSE '' ");
		consulta.append("        END + (SELECT ");
		consulta.append("          RTRIM(cc.stdcc_cdescripcion) ");
		consulta.append("        FROM std_centro_costo cc ");
		consulta.append("        WHERE cc.stdcc_id = pe.stdcc_id) ");
		consulta.append("        + ' - ' + pe.stde_descripcion ");
		consulta.append("      FROM STD_EMPLEADO pe ");
		consulta.append("      WHERE pe.stde_id = e.stde_id_destino) ");
		consulta.append("  END AS dependencia, ");
		consulta.append("  e.stdcc_id_destino AS dependenciaId, ");
		consulta.append("  CASE ");
		consulta.append("    WHEN EXISTS (SELECT ");
		consulta.append("        sfa.stdsf_id ");
		consulta.append("      FROM std_seguimiento_fisico sfa ");
		consulta.append("      WHERE sfa.stdd_id = e.stdd_id) THEN 1 ");
		consulta.append("    ELSE 0 ");
		consulta.append("  END AS recibi, ");
		consulta.append("  (SELECT ");
		consulta.append("    sfb.stdsf_dfecha_recep ");
		consulta.append("  FROM std_seguimiento_fisico sfb ");
		consulta.append("  WHERE sfb.stdd_id = e.stdd_id) ");
		consulta.append("  AS fecha, ");
		consulta.append("  (SELECT ");
		consulta.append("    sfb.aud_cusuario_crea ");
		consulta.append("  FROM std_seguimiento_fisico sfb ");
		consulta.append("  WHERE sfb.stdd_id = e.stdd_id) ");
		consulta.append("  AS codigoUsuario, ");
		consulta.append("  (SELECT ");
		consulta.append("    RTRIM(u.stdu_vnombres) + ' ' + RTRIM(u.stdu_vapellidos) ");
		consulta.append("  FROM STD_USUARIO u ");
		consulta.append("  WHERE u.stdu_cusuario = (SELECT ");
		consulta.append("    sfb.aud_cusuario_crea ");
		consulta.append("  FROM std_seguimiento_fisico sfb ");
		consulta.append("  WHERE sfb.stdd_id = e.stdd_id)) ");
		consulta.append("  AS usuario, ");
		consulta.append("  (SELECT ");
		consulta.append("    sfb.stdsf_cestado ");
		consulta.append("  FROM std_seguimiento_fisico sfb ");
		consulta.append("  WHERE sfb.stdd_id = e.stdd_id) ");
		consulta.append("  AS estado, ");
		consulta.append("  * ");
		consulta.append("FROM std_enviado e ");
		consulta.append("WHERE e.stdf_id = :fichaDocumentoId ");
		consulta.append("AND e.stdd_bdirigido IN ( ");
		consulta.append(":dirigidos");
		consulta.append(") ");
		consulta.append("AND e.stde_bresponsable = :esResponsable ");
		consulta.append("AND e.stdd_bhabilitado = :habilitado ");
		consulta.append("ORDER BY e.stde_bcopia,dependencia");
		
		Query query = this.getSession().createSQLQuery(consulta.toString())
			  .addScalar("derivaId", new IntegerType())
  			  .addScalar("dependencia", new StringType())  
  			  .addScalar("dependenciaId", new IntegerType())
			  .addScalar("recibi", new IntegerType())
			  .addScalar("fecha", new TimestampType())
			  .addScalar("codigoUsuario", new StringType())
			  .addScalar("usuario", new StringType())
			  .addScalar("estado", new StringType());
		
		query.setParameter("fichaDocumentoId", fichaDocumentoId );
    	query.setParameterList("dirigidos", dirigidos.toString().split(",") );
    	query.setParameter("esResponsable", Constantes.VERDADERO );
    	query.setParameter("habilitado", Constantes.VERDADERO);
    	
    	List listaQuery = query.list();
    	List<SeguimientoFisicoUtil> listFisico = new ArrayList<>();
    	for (Object obj : listaQuery) {
    		Object[] array = (Object[]) obj;
    		SeguimientoFisicoUtil fisico = new SeguimientoFisicoUtil();
    		fisico.setDerivaId(array[0] != Constantes.NULO ? Integer.parseInt(array[0].toString()): Constantes.CERO);
    		fisico.setDependencia(array[1] != Constantes.NULO ? array[1].toString().trim(): Constantes.VACIO);
    		fisico.setDependenciaId(array[2] != Constantes.NULO ? array[2].toString().trim(): Constantes.VACIO);
    		fisico.setRecibiConforme(Constantes.CON_FISICO_RECIBIDO.toString().equals(array[3].toString())? Constantes.FALSO: Constantes.VERDADERO);
    		fisico.setFechaRecepcion(array[4] != Constantes.NULO ? new SimpleDateFormat(Constantes.FORMATO_FECHA_USUARIO_DE_MESA_PARTES).format(array[4]): Constantes.GUION);
    		fisico.setCodUsuario(array[5] != Constantes.NULO ? array[5].toString().trim(): Constantes.GUION);
    		fisico.setDesUsuario(array[6] != Constantes.NULO ? array[6].toString().trim(): Constantes.GUION);
    		fisico.setCodigoEstado(array[7] != Constantes.NULO ? array[7].toString().trim(): Constantes.ESTADO_RECIBIR_DOC_FISICO.get(Constantes.PENDIENTE_RECIBIR));
    		fisico.setEstado(array[7] != Constantes.NULO ? Constantes.DES_ESTADO_RECIBIR_DOC_FISICO.get(array[7].toString().trim()): Constantes.DES_ESTADO_RECIBIR_DOC_FISICO.get(Constantes.PENDIENTE_RECIBIR));
    		fisico.setFechaRecibiConforme((Date) (array[4] != Constantes.NULO ? array[4]: null));
    		listFisico.add(fisico);
    	}	  
		return listFisico;
	}

	@Override
	public List<SeguimientoFisico> findRecibiFisicoFichaId(String fichaDocumentoId, String estado) throws Exception {
		// TODO Auto-generated method stub
		
		StringBuffer  consulta = new StringBuffer();
		consulta.append("SELECT stdsf_id              AS segFisicoId, ");
		consulta.append("       stdd_id               AS derivaId, ");
		consulta.append("       stdsf_cestado         AS estado, ");
		consulta.append("       stdsf_dfecha_recep    AS fechaRecep, ");
		consulta.append("       stdsf_bhabilitado     AS habilitado, ");
		consulta.append("       stdsf_cc_id           AS centroCosto, ");
		consulta.append("       aud_cusuario_crea     AS usuarioCrea, ");
		consulta.append("       aud_dfecha_crea       AS fechaCrea, ");
		consulta.append("       aud_cusuario_modifica AS usuarioModifica, ");
		consulta.append("       aud_dfecha_modifica   AS fechaModifica ");
		consulta.append("FROM   std_seguimiento_fisico sf ");
		consulta.append("WHERE  sf.stdd_id IN (SELECT e.stdd_id ");
		consulta.append("                      FROM   std_enviado e ");
		consulta.append("                      WHERE  e.stdf_id = :fichaDocumentoId ");
		consulta.append("                             AND e.stdd_bhabilitado = :habilitado) ");
		consulta.append("       AND sf.stdsf_bhabilitado = :habilitado ");
		consulta.append("       AND sf.stdsf_cestado = :estado");
		
		
		Query query = this.getSession().createSQLQuery(consulta.toString())
	  			  .addScalar("segFisicoId", new IntegerType())  
	  			  .addScalar("derivaId", new IntegerType())
				  .addScalar("estado", new IntegerType())
				  .addScalar("fechaRecep", new TimestampType())
				  .addScalar("habilitado", new BooleanType())
				  .addScalar("centroCosto", new StringType())
				  .addScalar("usuarioCrea", new StringType())
				  .addScalar("fechaCrea", new TimestampType())
				  .addScalar("usuarioModifica", new StringType())
				  .addScalar("fechaModifica", new TimestampType());
			
			query.setParameter("fichaDocumentoId", fichaDocumentoId );
	    	query.setParameter("habilitado", Constantes.VERDADERO );
	    	query.setParameter("estado", estado);
		
	    	List listaQuery = query.list();
	    	List<SeguimientoFisico> listFisico = new ArrayList<>();
	    	for (Object obj : listaQuery) {
	    		Object[] array = (Object[]) obj;
	    		SeguimientoFisico fisico = new SeguimientoFisico();
	    		
	    		fisico.setId(array[0] != Constantes.NULO ? Integer.parseInt(array[0].toString()): Constantes.CERO);
	    		fisico.setDerivaId(array[1] != Constantes.NULO ? Integer.parseInt(array[1].toString()): Constantes.CERO);
	    		fisico.setEstado(array[2] != Constantes.NULO ? array[2].toString(): Constantes.VACIO);
	    		fisico.setFechaRecep(array[3] != Constantes.NULO ?  (Date)array[3]: Constantes.SIN_FECHA);
	    		fisico.setHabilitado(array[4] != Constantes.NULO ? Boolean.parseBoolean(array[4].toString()): Constantes.FALSO);
	    		fisico.setCentroCostoId(array[5] != Constantes.NULO ? array[5].toString(): Constantes.VACIO);
	    		fisico.setUsuarioCrea(array[6] != Constantes.NULO ? array[6].toString(): Constantes.VACIO);
	    		fisico.setFechaCrea(array[7] != Constantes.NULO ?  (Date)array[7]: Constantes.SIN_FECHA);
	    		fisico.setUsuarioModifica(array[8] != Constantes.NULO ? array[8].toString(): Constantes.VACIO);
	    		fisico.setFechaRecep(array[9] != Constantes.NULO ? (Date)array[9]: Constantes.SIN_FECHA);
	    		listFisico.add(fisico);
	    	}	  
			return listFisico;
	}

	@Override
	public List<SeguimientoFisico> findRecibiFisicoFichaIdAndEmpleado(String fichaDocumentoId, String empleadoId)
			throws Exception {
		// TODO Auto-generated method stub
		StringBuffer dirigidos = new StringBuffer();
		dirigidos.append(Constantes.INDICADOR_DIRIGIDO).append(",").append(Constantes.INDICADOR_FISICO);
		
		StringBuffer  consulta = new StringBuffer();
		consulta.append("SELECT stdsf_id              AS segFisicoId, ");
		consulta.append("       stdd_id               AS derivaId, ");
		consulta.append("       stdsf_cestado         AS estado, ");
		consulta.append("       stdsf_dfecha_recep    AS fechaRecep, ");
		consulta.append("       stdsf_bhabilitado     AS habilitado, ");
		consulta.append("       stdsf_cc_id           AS centroCosto, ");
		consulta.append("       aud_cusuario_crea     AS usuarioCrea, ");
		consulta.append("       aud_dfecha_crea       AS fechaCrea, ");
		consulta.append("       aud_cusuario_modifica AS usuarioModifica, ");
		consulta.append("       aud_dfecha_modifica   AS fechaModifica ");
		consulta.append("	FROM STD_SEGUIMIENTO_FISICO sf WHERE sf.stdd_id IN ( ");
		consulta.append("	SELECT e.stdd_id FROM STD_ENVIADO e ");
		consulta.append("	WHERE e.stdf_id = :fichaDocumentoId ");
		consulta.append("	AND e.stde_id_origen = :empleadoId ");
		consulta.append("    AND e.stdd_bdirigido IN ( ");
		consulta.append("        :dirigidos");
		consulta.append("    ) ");
		consulta.append("	AND e.stdd_bhabilitado = :habilitado ");
		consulta.append(")");
		
		Query query = this.getSession().createSQLQuery(consulta.toString())
				  .addScalar("segFisicoId", new IntegerType())  
	  			  .addScalar("derivaId", new IntegerType())
				  .addScalar("estado", new StringType())
				  .addScalar("fechaRecep", new TimestampType())
				  .addScalar("habilitado", new BooleanType())
				  .addScalar("centroCosto", new StringType())
				  .addScalar("usuarioCrea", new StringType())
				  .addScalar("fechaCrea", new TimestampType())
				  .addScalar("usuarioModifica", new StringType())
				  .addScalar("fechaModifica", new TimestampType());
		
		query.setParameter("fichaDocumentoId",Integer.parseInt(fichaDocumentoId ));
    	query.setParameter("empleadoId",Integer.parseInt(empleadoId));
    	query.setParameterList("dirigidos", dirigidos.toString().split(",") );
    	query.setParameter("habilitado", Constantes.VERDADERO );
				
    	@SuppressWarnings("rawtypes")
		List listaQuery = query.list();
    	List<SeguimientoFisico> listFisico = new ArrayList<>();
    	for (Object obj : listaQuery) {
    		Object[] array = (Object[]) obj;
    		SeguimientoFisico fisico = new SeguimientoFisico();
    		
    		fisico.setId(array[0] != Constantes.NULO ? Integer.parseInt(array[0].toString()): Constantes.CERO);
    		fisico.setDerivaId(array[1] != Constantes.NULO ? Integer.parseInt(array[1].toString()): Constantes.CERO);
    		fisico.setEstado(array[2] != Constantes.NULO ? array[2].toString(): Constantes.VACIO);
    		fisico.setFechaRecep(array[3] != Constantes.NULO ?  (Date)array[3]: Constantes.SIN_FECHA);
    		fisico.setHabilitado(array[4] != Constantes.NULO ? Boolean.parseBoolean(array[4].toString()): Constantes.FALSO);
    		fisico.setCentroCostoId(array[5] != Constantes.NULO ? array[5].toString(): Constantes.VACIO);
    		fisico.setUsuarioCrea(array[6] != Constantes.NULO ? array[6].toString(): Constantes.VACIO);
    		fisico.setFechaCrea(array[7] != Constantes.NULO ?  (Date)array[7]: Constantes.SIN_FECHA);
    		fisico.setUsuarioModifica(array[8] != Constantes.NULO ? array[8].toString(): Constantes.VACIO);
    		fisico.setFechaModifica(array[9] != Constantes.NULO ? (Date)array[9]: Constantes.SIN_FECHA);
    		listFisico.add(fisico);
    	}	  
    	
		return listFisico;
	}
	
	@Override
	public List<SeguimientoFisico> findRecibiFisicoById(String derivaId) throws Exception {
		// TODO Auto-generated method stub
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("derivaId", Integer.valueOf(derivaId)));
		List lsSegui = criteria.list();
		return lsSegui;
	}

   
}
