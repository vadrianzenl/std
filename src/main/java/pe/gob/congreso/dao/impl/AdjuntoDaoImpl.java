package pe.gob.congreso.dao.impl;

import com.google.common.base.Optional;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.congreso.dao.AdjuntoDao;
import pe.gob.congreso.model.Adjunto;
import pe.gob.congreso.util.Constantes;

@Repository("adjuntoDao")
public class AdjuntoDaoImpl extends AbstractDao<Integer, Adjunto> implements AdjuntoDao {

    public void setFetchMode(Criteria criteria) {
        criteria.setFetchMode("fichaDocumento", FetchMode.JOIN);
        criteria.setFetchMode("empleado", FetchMode.JOIN);
        criteria.setFetchMode("tipo", FetchMode.JOIN);
    }

    @Override
    public Adjunto create(Adjunto a) throws Exception {
        saveOrUpdate(a);
        return a;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Adjunto> findAdjuntos(String fichaDocumentoId) throws Exception {
        Criteria criteria = createEntityCriteria();

        this.setFetchMode(criteria);
        criteria.createAlias("fichaDocumento", "f");
        criteria.createAlias("tipoAnexo", "tipoAnexo");
        if (Optional.fromNullable(fichaDocumentoId).isPresent()) {
            if (!fichaDocumentoId.equals("")) {
                criteria.add(Restrictions.eq("f.id", Integer.valueOf(fichaDocumentoId)));
            }
        }
        criteria.add(Restrictions.eq("tipoAnexo.id", Constantes.DOCUMENTO_ANEXO));
        criteria.add(Restrictions.eq("habilitado", true));


        List adjuntosList = criteria.list();
        return adjuntosList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int verificadAdjuntos(String fichaDocumentoId) throws Exception {
        int result = 0;
        if (Optional.fromNullable(fichaDocumentoId).isPresent()) {
            if (!fichaDocumentoId.equals("")) {
                Query query = getSession().createSQLQuery(
                        "SELECT COUNT(*) FROM STD_ADJUNTO ad WHERE ad.stda_bhabilitado = 1 AND ad.stdf_id = :fichaId")
                        .setParameter("fichaId", Integer.valueOf(fichaDocumentoId));
                result = (int) query.uniqueResult();
            }
        }

        return result;
    }

    @Override
    public Adjunto findById(Integer id) throws Exception {
        return getByKey(id);
    }

    @Override
    @Transactional(readOnly = true)
    public int updateUUID(String nameFile, String uuid)  throws Exception{
        int response=0;
        Query query = getSession().createSQLQuery("UPDATE STD_ADJUNTO SET stda_vuuid = :uuid WHERE stda_vnombre_archivo = :nameFile ")
                .setParameter("uuid", uuid)
                .setParameter("nameFile", nameFile);
        response = (int) query.executeUpdate();
        return response;
    }

    @Override
    public Adjunto findAdjuntoDocx(Integer fichaDocumentoId, Integer tipoArchivo, Integer tipoAnexo, boolean indicadorFirma) throws Exception{
        Criteria criteria = createEntityCriteria();
        this.setFetchMode(criteria);
        criteria.createAlias("fichaDocumento", "f");
        criteria.createAlias("tipoArchivo", "ta");
        criteria.createAlias("tipoAnexo", "tan");
        criteria.add(Restrictions.eq("f.id", fichaDocumentoId));
        criteria.add(Restrictions.eq("ta.id", tipoArchivo));
        criteria.add(Restrictions.eq("tan.id", tipoAnexo));
        criteria.add(Restrictions.eq("indicadorFirma", indicadorFirma));
        criteria.add(Restrictions.eq("habilitado", true));
        Adjunto adjunto = (Adjunto) criteria.uniqueResult();
        return adjunto;
    }

    @Override
    public Integer getFichaDocRecibidoByPdfOfDependency(Integer empId, String cc, String pdf) throws Exception {
        System.out.println(cc + " " + pdf);

        Query query = getSession().createSQLQuery("SELECT DISTINCT adj_.stda_id AS y0_ FROM STD_ENVIADO this_ "
                + "    INNER JOIN STD_CENTRO_COSTO ccd3_ ON this_.stdcc_id_destino= ccd3_.stdcc_id "
                + "    INNER JOIN STD_CENTRO_COSTO cco4_ ON this_.stdcc_id_origen= cco4_.stdcc_id "
                + "    INNER JOIN STD_EMPLEADO ed2_ ON this_.stde_id_destino= ed2_.stde_id "
                + "    INNER JOIN STD_TIPO estado9_ ON this_.stdd_iestado= estado9_.stdt_id "
                + "    INNER JOIN STD_FICHA_DOCUMENTO f1_ ON this_.stdf_id= f1_.stdf_id "
                + "    INNER JOIN STD_ANIO_LEGISLATIVO aniolegis10_ ON f1_.stdal_codigo= aniolegis10_.stdal_codigo "
                + "    INNER JOIN STD_CENTRO_COSTO ccremitido5_ ON f1_.stdf_remitidocc= ccremitido5_.stdcc_id "
                + "    INNER JOIN STD_TIPO_DOCUMENTO tipdoc7_ ON f1_.stdtd_id= tipdoc7_.stdtd_id "
                + "    INNER JOIN STD_TIPO tipdocumen8_ ON tipdoc7_.stdt_id= tipdocumen8_.stdt_id "
                + "    INNER JOIN STD_ADJUNTO adj_ ON this_.stdf_id= adj_.stdf_id  "
                + "    WHERE this_.stdd_iestado NOT IN ( 7 ) AND this_.stdd_bhabilitado=1  "
                + "    AND ( f1_.stdf_bprivado=0 OR this_.stde_id_destino=:empId )  "
                + "    AND this_.stdcc_id_destino=:cc AND this_.stdcc_id_idestino=:cc  "
                + "    AND adj_.stda_vnombre_archivo=:pdf ").setParameter("empId", Integer.valueOf(empId))
                .setParameter("cc", String.valueOf(cc)).setParameter("pdf", String.valueOf(pdf));
        return (Integer) query.uniqueResult();
    }

    @Override
    public Integer getFichaDocRecibidoByPdfOfPerson(Integer empId, String cc, String pdf) throws Exception {
        System.out.println(empId + " " + cc + " " + pdf);

        Query query = getSession().createSQLQuery("SELECT DISTINCT adj_.stda_id AS y0_  FROM STD_ENVIADO this_ "
                + "    INNER JOIN STD_CENTRO_COSTO ccd3_ ON this_.stdcc_id_destino= ccd3_.stdcc_id"
                + "    INNER JOIN STD_CENTRO_COSTO cco4_ ON this_.stdcc_id_origen= cco4_.stdcc_id"
                + "    INNER JOIN STD_EMPLEADO ed2_ ON this_.stde_id_destino= ed2_.stde_id"
                + "    INNER JOIN STD_TIPO estado9_ ON this_.stdd_iestado= estado9_.stdt_id"
                + "    INNER JOIN STD_FICHA_DOCUMENTO f1_ ON this_.stdf_id= f1_.stdf_id"
                + "    INNER JOIN STD_ANIO_LEGISLATIVO aniolegis10_ ON f1_.stdal_codigo= aniolegis10_.stdal_codigo"
                + "    INNER JOIN STD_CENTRO_COSTO ccremitido5_ ON f1_.stdf_remitidocc= ccremitido5_.stdcc_id"
                + "    INNER JOIN STD_TIPO_DOCUMENTO tipdoc7_ ON f1_.stdtd_id= tipdoc7_.stdtd_id"
                + "    INNER JOIN STD_TIPO tipdocumen8_ ON tipdoc7_.stdt_id= tipdocumen8_.stdt_id "
                + "    INNER JOIN STD_ADJUNTO adj_ ON this_.stdf_id= adj_.stdf_id " + "WHERE "
                + "    this_.stdd_iestado NOT IN ( 7 ) AND this_.stdd_bhabilitado=1  "
                + "    AND this_.stde_id_destino=:empId AND this_.stdcc_id_destino=:cc  "
                + "    AND this_.stdcc_id_idestino=:cc AND adj_.stda_vnombre_archivo = :pdf")
                .setParameter("empId", Integer.valueOf(empId)).setParameter("cc", String.valueOf(cc))
                .setParameter("pdf", String.valueOf(pdf));
        Integer fd = (Integer) query.uniqueResult();
        return fd;
    }

    @Override
    public Integer getFichaDocEnviadoByPdfOfDependency(String cc, String pdf) throws Exception {
        System.out.println(cc + " " + pdf);

        Query query = getSession().createSQLQuery("SELECT DISTINCT adj_.stda_id AS y0_ FROM STD_FICHA_DOCUMENTO this_  "
                + "    INNER JOIN STD_ANIO_LEGISLATIVO aniolegis7_ ON this_.stdal_codigo= aniolegis7_.stdal_codigo "
                + "    INNER JOIN STD_CENTRO_COSTO cc5_ ON this_.stdcc_id= cc5_.stdcc_id "
                + "    INNER JOIN STD_EMPLEADO e4_ ON this_.stde_id= e4_.stde_id "
                + "    INNER JOIN STD_TIPO_DOCUMENTO td2_ ON this_.stdtd_id= td2_.stdtd_id "
                + "    INNER JOIN STD_TIPO tipo6_ ON td2_.stdt_id= tipo6_.stdt_id "
                + "    INNER JOIN STD_TIPO te3_ ON this_.stdt_iestado= te3_.stdt_id "
                + "    INNER JOIN STD_TIPO tr1_ ON this_.stdt_itipo_registro= tr1_.stdt_id "
                + "    INNER JOIN STD_ADJUNTO adj_ ON this_.stdf_id= adj_.stdf_id " + "WHERE "
                + "    this_.stdt_itipo_registro=1  " + "    AND this_.stdcc_id=:cc  " + "    AND this_.stdcc_iid=:cc "
                + "    AND adj_.stda_vnombre_archivo = :pdf").setParameter("cc", String.valueOf(cc))
                .setParameter("pdf", String.valueOf(pdf));
        return (Integer) query.uniqueResult();
    }

    @Override
    public Integer getFichaDocEnviadoByPdfOfPerson(Integer empId, String cc, String pdf) throws Exception {
        System.out.println(empId + " " + cc + " " + pdf);

        Query query = getSession().createSQLQuery("SELECT DISTINCT adj_.stda_id AS y0_ FROM STD_FICHA_DOCUMENTO this_  "
                + "    INNER JOIN STD_ANIO_LEGISLATIVO aniolegis7_ ON this_.stdal_codigo= aniolegis7_.stdal_codigo "
                + "    INNER JOIN STD_CENTRO_COSTO cc5_ ON this_.stdcc_id= cc5_.stdcc_id "
                + "    INNER JOIN STD_EMPLEADO e4_ ON this_.stde_id= e4_.stde_id "
                + "    INNER JOIN STD_TIPO_DOCUMENTO td2_ ON this_.stdtd_id= td2_.stdtd_id "
                + "    INNER JOIN STD_TIPO tipo6_ ON td2_.stdt_id= tipo6_.stdt_id "
                + "    INNER JOIN STD_TIPO te3_ ON this_.stdt_iestado= te3_.stdt_id "
                + "    INNER JOIN STD_TIPO tr1_ ON this_.stdt_itipo_registro= tr1_.stdt_id "
                + "    INNER JOIN STD_ADJUNTO adj_ ON this_.stdf_id= adj_.stdf_id " + "WHERE "
                + "    this_.stdt_itipo_registro=1  " + "    AND this_.stde_id=:empId  "
                + "    AND this_.stdcc_id=:cc  " + "    AND this_.stdcc_iid=:cc "
                + "    AND adj_.stda_vnombre_archivo = :pdf").setParameter("empId", Integer.valueOf(empId))
                .setParameter("cc", String.valueOf(cc)).setParameter("pdf", String.valueOf(pdf));
        Integer fd = (Integer) query.uniqueResult();
        return fd;
    }

    @Override
    public Integer getFichaDocRecibidoByPdfOfMesa(String cc, String pdf) throws Exception {
        System.out.println(cc + " " + pdf);

        Query query = getSession()
                .createSQLQuery("SELECT DISTINCT adj_.stda_id AS y0_ FROM std_ficha_documento this_ "
                        + "    INNER JOIN STD_ADJUNTO adj_ ON this_.stdf_id= adj_.stdf_id WHERE "
                        + "    stdcc_iid = :cc AND stdf_cestafeta = 1 " + "    AND adj_.stda_vnombre_archivo = :pdf")
                .setParameter("cc", String.valueOf(cc)).setParameter("pdf", String.valueOf(pdf));
        return (Integer) query.uniqueResult();
    }

}
