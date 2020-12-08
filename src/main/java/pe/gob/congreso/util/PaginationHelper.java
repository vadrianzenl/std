package pe.gob.congreso.util;

import pe.gob.congreso.model.util.FichaUtil;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.hibernate.transform.Transformers;

import com.google.common.base.Optional;
import pe.gob.congreso.model.util.DerivaUtil;

public class PaginationHelper {

    public Map<String, Object> getPagination(Criteria criteria, String id, Optional<String> pag,
            Optional<String> pagLength) {
        Map<String, Object> result = new HashMap<String, Object>();
        List lista = criteria.list();

        Integer totalResultCount = lista.size();

        Integer total = 0;

        if (lista.size() > 0) {
            total = 1;
            if (pag.isPresent() && !pag.get().isEmpty() && pagLength.isPresent() && !pagLength.get().isEmpty()) {
                criteria.setProjection(Projections.distinct(Projections.property("id")));
                criteria.addOrder(Order.desc("id"));
                criteria.setFirstResult(Integer.parseInt(pagLength.get()) * (Integer.parseInt(pag.get()) - 1));
                criteria.setMaxResults(Integer.parseInt(pagLength.get()));
                List uniqueSubList = criteria.list();

                criteria.setProjection(null);
                criteria.setFirstResult(0);
                criteria.setMaxResults(Integer.MAX_VALUE);
                criteria.add(Restrictions.in("id", uniqueSubList));
                criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

                lista = criteria.list();

                total = totalResultCount / Integer.parseInt(pagLength.get());
                if ((totalResultCount % Integer.parseInt(pagLength.get())) > 0) {
                    total = total + 1;
                }
            }
        }

        result.put("lista", lista);
        result.put("totalPage", total);
        result.put("totalResultCount", totalResultCount);

        return result;
    }

    public Map<String, Object> getPaginationDeriva(Criteria criteria, String id, Optional<String> pag,
            Optional<String> pagLength, Boolean esMesaPartes) {
        Map<String, Object> result = new HashMap<String, Object>();
        List lista = criteria.list();

        Integer totalResultCount = lista.size();

        Integer total = 0;

        if (lista.size() > 0) {
            total = 1;
            if (pag.isPresent() && !pag.get().isEmpty() && pagLength.isPresent() && !pagLength.get().isEmpty()) {
                // criteria.setProjection(Projections.distinct(Projections.projectionList()
                // .add(Projections.property("id"), "id")
                // .add(Projections.property("fechaDeriva"), "fechaDeriva")
                // ));
                if (esMesaPartes) {
                    // criteria.setProjection(Projections.groupProperty("id"));
                    criteria.setProjection(Projections.groupProperty("f.numeroMp"));
                    criteria.addOrder(Order.desc("f.numeroMp"));
                    criteria.setFirstResult(Integer.parseInt(pagLength.get()) * (Integer.parseInt(pag.get()) - 1));
                    criteria.setMaxResults(Integer.parseInt(pagLength.get()));
                    List uniqueSubList = criteria.list();

                    criteria.setProjection(null);
                    criteria.setProjection(Projections.projectionList().add(Projections.property("id"), "id")
                            .add(Projections.property("indicaciones"), "indicaciones")
                            .add(Projections.property("fechaDeriva"), "fechaDeriva")
                            .add(Projections.property("dirigido"), "dirigido")
                            .add(Projections.property("habilitado"), "habilitado")
                            .add(Projections.property("grupo"), "grupo").add(Projections.property("cargo"), "cargo")
                            .add(Projections.property("estado"), "estado")
                            .add(Projections.property("fichaDocumento"), "fichaDocumento")
                            .add(Projections.property("empleadoOrigen"), "empleadoOrigen")
                            .add(Projections.property("centroCostoOrigen"), "centroCostoOrigen")
                            .add(Projections.property("empleadoDestino"), "empleadoDestino")
                            .add(Projections.property("centroCostoDestino"), "centroCostoDestino")
                            .add(Projections.property("motivoDeriva"), "motivoDeriva")
                            .add(Projections.property("estadoDeriva"), "estadoDeriva")
                            .add(Projections.property("fichaRespuesta"), "fichaRespuesta"));

                    criteria.setFirstResult(0);
                    criteria.setMaxResults(Integer.MAX_VALUE);
                    criteria.add(Restrictions.in("f.numeroMp", uniqueSubList));
                } else {
                    criteria.setProjection(Projections.distinct(Projections.property("id")));
                    criteria.addOrder(Order.desc("id"));
                    criteria.setFirstResult(Integer.parseInt(pagLength.get()) * (Integer.parseInt(pag.get()) - 1));
                    criteria.setMaxResults(Integer.parseInt(pagLength.get()));
                    List uniqueSubList = criteria.list();

                    criteria.setProjection(null);
                    criteria.setProjection(Projections.distinct(Projections.projectionList()
                            .add(Projections.property("id"), "id")
                            .add(Projections.property("indicaciones"), "indicaciones")
                            .add(Projections.property("fechaDeriva"), "fechaDeriva")
                            .add(Projections.property("dirigido"), "dirigido")
                            .add(Projections.property("habilitado"), "habilitado")
                            .add(Projections.property("grupo"), "grupo").add(Projections.property("cargo"), "cargo")
                            .add(Projections.property("estado"), "estado")
                            .add(Projections.property("fichaDocumento"), "fichaDocumento")
                            .add(Projections.property("empleadoOrigen"), "empleadoOrigen")
                            .add(Projections.property("centroCostoOrigen"), "centroCostoOrigen")
                            .add(Projections.property("empleadoDestino"), "empleadoDestino")
                            .add(Projections.property("centroCostoDestino"), "centroCostoDestino")
                            .add(Projections.property("motivoDeriva"), "motivoDeriva")
                            .add(Projections.property("estadoDeriva"), "estadoDeriva")
                            .add(Projections.property("fichaRespuesta"), "fichaRespuesta")));

                    criteria.setFirstResult(0);
                    criteria.setMaxResults(Integer.MAX_VALUE);
                    criteria.add(Restrictions.in("id", uniqueSubList));
                }

                criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

                criteria.setResultTransformer(Transformers.aliasToBean(DerivaUtil.class));

                lista = criteria.list();

                total = totalResultCount / Integer.parseInt(pagLength.get());
                if ((totalResultCount % Integer.parseInt(pagLength.get())) > 0) {
                    total = total + 1;
                }
            }
        }

        result.put("lista", lista);
        result.put("totalPage", total);
        result.put("totalResultCount", totalResultCount);

        return result;
    }

    public Map<String, Object> getPaginationDocumento(Criteria criteria, String id, Optional<String> pag,
            Optional<String> pagLength, Boolean esMesaPartes) {
        Map<String, Object> result = new HashMap<String, Object>();
        List lista = criteria.list();

        Integer totalResultCount = lista.size();

        Integer total = 0;

        if (lista.size() > 0) {
            total = 1;
            if (pag.isPresent() && !pag.get().isEmpty() && pagLength.isPresent() && !pagLength.get().isEmpty()) {

                if (esMesaPartes) {
                    criteria.setProjection(Projections.groupProperty("numeroMp"));
                    criteria.addOrder(Order.desc("numeroMp"));
                    criteria.setFirstResult(Integer.parseInt(pagLength.get()) * (Integer.parseInt(pag.get()) - 1));
                    criteria.setMaxResults(Integer.parseInt(pagLength.get()));
                    List uniqueSubList = criteria.list();
                    criteria.setProjection(null);
                    criteria.createAlias("e.centroCosto", "cen");
                    criteria.setProjection(Projections.projectionList().add(Projections.property("id"), "id")
                            .add(Projections.property("fechaCrea"), "fechaCrea")
                            .add(Projections.property("numeroDoc"), "numeroDoc")
                            .add(Projections.property("tipo.descripcion"), "tipoDocumento")
                            .add(Projections.property("te.descripcion"), "tipoEstado")
                            .add(Projections.property("tipoRegistro"), "tipoRegistro")
                            .add(Projections.property("asunto"), "asunto")
                            .add(Projections.property("e.descripcion"), "empleado")
                            .add(Projections.property("cc.descripcion"), "centroCosto")
                            .add(Projections.property("referencia"), "referencia")
                            .add(Projections.property("numeroFolios"), "numeroFolios"));
                    criteria.setFirstResult(0);
                    criteria.setMaxResults(Integer.MAX_VALUE);

                    criteria.add(Restrictions.in("numeroMp", uniqueSubList));
                } else {
                    criteria.setProjection(Projections.distinct(Projections.property("id")));
                    criteria.addOrder(Order.desc("id"));
                    criteria.setFirstResult(Integer.parseInt(pagLength.get()) * (Integer.parseInt(pag.get()) - 1));
                    criteria.setMaxResults(Integer.parseInt(pagLength.get()));
                    List uniqueSubList = criteria.list();
                    criteria.setProjection(null);
                    criteria.createAlias("e.centroCosto", "cen");
                    criteria.setProjection(Projections.distinct(Projections.projectionList()
                            .add(Projections.property("id"), "id").add(Projections.property("fechaCrea"), "fechaCrea")
                            .add(Projections.property("numeroDoc"), "numeroDoc")
                            .add(Projections.property("tipo.descripcion"), "tipoDocumento")
                            .add(Projections.property("te.descripcion"), "tipoEstado")
                            .add(Projections.property("tipoRegistro"), "tipoRegistro")
                            .add(Projections.property("asunto"), "asunto")
                            .add(Projections.property("e.descripcion"), "empleado")
                            .add(Projections.property("cc.descripcion"), "centroCosto")
                            .add(Projections.property("referencia"), "referencia")
                            .add(Projections.property("numeroFolios"), "numeroFolios")));
                    criteria.setFirstResult(0);
                    criteria.setMaxResults(Integer.MAX_VALUE);

                    criteria.add(Restrictions.in("id", uniqueSubList));
                }

                criteria.setResultTransformer(Transformers.aliasToBean(FichaUtil.class));

                lista = criteria.list();

                total = totalResultCount / Integer.parseInt(pagLength.get());
                if ((totalResultCount % Integer.parseInt(pagLength.get())) > 0) {
                    total = total + 1;
                }
            }
        }

        result.put("lista", lista);
        result.put("totalPage", total);
        result.put("totalResultCount", totalResultCount);

        return result;
    }

}
