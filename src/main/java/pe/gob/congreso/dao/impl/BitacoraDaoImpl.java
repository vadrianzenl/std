package pe.gob.congreso.dao.impl;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.BitacoraDao;
import pe.gob.congreso.model.Bitacora;
import pe.gob.congreso.model.util.SeguimientoUtil;

@Repository("bitacoraDao")
public class BitacoraDaoImpl extends AbstractDao<Integer, Bitacora> implements BitacoraDao {

    public void setFetchMode(Criteria criteria) {
        criteria.setFetchMode("fichaDocumento", FetchMode.JOIN);
        criteria.setFetchMode("empleado", FetchMode.JOIN);
        criteria.setFetchMode("estado", FetchMode.JOIN);
    }

    @Override
    public Bitacora create(Bitacora d) throws Exception {
        saveOrUpdate(d);
        return d;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Bitacora> findBitacoras(String fichaDocumentoId) throws Exception {
        Criteria criteria = createEntityCriteria();

        this.setFetchMode(criteria);
        criteria.createAlias("fichaDocumento", "f");
        criteria.add(Restrictions.eq("f.id", Integer.valueOf(fichaDocumentoId)));
        criteria.addOrder(Order.desc("fecha"));

        List bitacoraList = criteria.list();
        return bitacoraList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SeguimientoUtil> getSeguimiento(String fichaDocumentoId) throws Exception {

        List<SeguimientoUtil> lista = new ArrayList<SeguimientoUtil>();

        Query query = getSession().createSQLQuery(
        		"SELECT S.CENTRO_COSTO, CASE WHEN S.NIVEL > 1 THEN 'RECIBIDO EL ' + S.FECHA_DOCUMENTO ELSE 'FECHA DEL DOCUMENTO: ' + S.FECHA_DOCUMENTO END AS FECHA_DOCUMENTO, CASE WHEN S.NIVEL > 1 AND S.ESTADO = 8 THEN 'ESTADO: PENDIENTE' ELSE 'ESTADO: ' + T.stdt_vdescripcion END AS ESTADO, S.RECEPTORES, S.OBSERVACIONES, S.NIVEL FROM fn_std_obt_seguimiento(:fichaId) S LEFT JOIN STD_TIPO T ON S.ESTADO = T.stdt_id ")
        		.setParameter("fichaId", Integer.valueOf(fichaDocumentoId));

        SeguimientoUtil su;
        List<Object[]> result = (List<Object[]>) query.list();
        for(Object[] seg: result){
        	su = new SeguimientoUtil();
        	su.setCentroCosto((String)seg[0]);
        	su.setFechaDocumento((String)seg[1]);
        	su.setEstado((String)seg[2]);
        	su.setReceptores((String)seg[3]);
        	su.setObservaciones((String)seg[4]);
        	su.setNivel((Integer)seg[5]);
        	lista.add(su);
        }

        return lista;
    }

}
