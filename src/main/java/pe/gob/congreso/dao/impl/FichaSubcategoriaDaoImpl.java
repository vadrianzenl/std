package pe.gob.congreso.dao.impl;

import com.google.common.base.Optional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.FichaSubcategoriaDao;
import pe.gob.congreso.model.FichaSubcategoria;
import pe.gob.congreso.model.util.JsonWeb;

@Repository("fichaSubcategoriaDao")
public class FichaSubcategoriaDaoImpl extends AbstractDao<Integer, FichaSubcategoria> implements FichaSubcategoriaDao {

    public void setFetchMode(Criteria criteria) {
        criteria.setFetchMode("subCategoria", FetchMode.JOIN);
        criteria.setFetchMode("fichaDocumento", FetchMode.JOIN);
    }

    @Override
    public FichaSubcategoria create(FichaSubcategoria fs) throws Exception {
        saveOrUpdate(fs);
        return fs;
    }

    @Override
    public Map<String, Object> find(Optional<String> subCategoriaId, Optional<String> codigo) throws Exception {

        Criteria criteria = createEntityCriteria();

        this.setFetchMode(criteria);
        criteria.createAlias("subCategoria", "sc");
        criteria.createAlias("fichaDocumento", "f");

        if (subCategoriaId.isPresent() && !subCategoriaId.get().isEmpty()) {
            criteria.add(Restrictions.eq("sc.id", Integer.valueOf(subCategoriaId.get())));
        }
        if (codigo.isPresent() && !codigo.get().isEmpty()) {
            criteria.add(Restrictions.eq("f.anioLegislativo.codigo", Integer.valueOf(codigo.get())));
        }

        criteria.addOrder(Order.desc("f.id"));

        criteria.setProjection(Projections.distinct(Projections.projectionList()
                .add(Projections.property("f.id"), "id")
                .add(Projections.property("f.asunto"), "asunto")
                .add(Projections.property("f.fechaDocumento"), "fechaDocumento")
                //.add(Projections.property("f.url"), "url")
                .add(Projections.property("sc.id"), "subCategoriaId")
                .add(Projections.property("sc.descripcion"), "subCategoriaDescripcion")
                .add(Projections.property("f.tipoAdjunto"), "tipoAdjunto")
        )).setResultTransformer(Transformers.aliasToBean(JsonWeb.class));

        Map<String, Object> result = new HashMap<String, Object>();
        List<JsonWeb> listaFichas = criteria.list();
        result.put("documentos", listaFichas);
        result.put("totalResultCount", listaFichas.size());

        return result;
    }

    @Override
    public Map<String, Object> findFichasCategoria(Optional<String> categoriaId, Optional<String> codigo) throws Exception {

        Criteria criteria = createEntityCriteria();

        this.setFetchMode(criteria);
        criteria.createAlias("subCategoria", "sc");
        criteria.createAlias("fichaDocumento", "f");

        if (categoriaId.isPresent() && !categoriaId.get().isEmpty()) {
            criteria.add(Restrictions.eq("sc.categoria.id", Integer.valueOf(categoriaId.get())));
        }
        if (codigo.isPresent() && !codigo.get().isEmpty()) {
            criteria.add(Restrictions.eq("f.anioLegislativo.codigo", Integer.valueOf(codigo.get())));
        }

        criteria.addOrder(Order.desc("f.id"));

        criteria.setProjection(Projections.distinct(Projections.projectionList()
                .add(Projections.property("f.id"), "id")
                .add(Projections.property("f.asunto"), "asunto")
                .add(Projections.property("f.fechaDocumento"), "fechaDocumento")
                //.add(Projections.property("f.url"), "url")
                .add(Projections.property("sc.id"), "subCategoriaId")
                .add(Projections.property("sc.descripcion"), "subCategoriaDescripcion")
                .add(Projections.property("f.tipoAdjunto"), "tipoAdjunto")
        )).setResultTransformer(Transformers.aliasToBean(JsonWeb.class));

        Map<String, Object> result = new HashMap<String, Object>();
        List<JsonWeb> listaFichas = criteria.list();
        result.put("documentos", listaFichas);
        result.put("totalResultCount", listaFichas.size());

        return result;
    }

    @Override
    public Map<String, Object> findLegislatura(Optional<String> subCategoriaId, Optional<String> codigo) throws Exception {

        Criteria criteria = createEntityCriteria();

        this.setFetchMode(criteria);
        criteria.createAlias("subCategoria", "sc");
        criteria.createAlias("fichaDocumento", "f");

        if (subCategoriaId.isPresent() && !subCategoriaId.get().isEmpty()) {
            criteria.add(Restrictions.eq("sc.id", Integer.valueOf(subCategoriaId.get())));
        }
        if (codigo.isPresent() && !codigo.get().isEmpty()) {
            criteria.add(Restrictions.eq("f.legislatura.codigo", codigo.get()));
        }

        criteria.addOrder(Order.desc("f.id"));

        criteria.setProjection(Projections.distinct(Projections.projectionList()
                .add(Projections.property("f.id"), "id")
                .add(Projections.property("f.asunto"), "asunto")
                .add(Projections.property("f.fechaDocumento"), "fechaDocumento")
                //.add(Projections.property("f.url"), "url")
                .add(Projections.property("sc.id"), "subCategoriaId")
                .add(Projections.property("sc.descripcion"), "subCategoriaDescripcion")
                .add(Projections.property("f.tipoAdjunto"), "tipoAdjunto")
        )).setResultTransformer(Transformers.aliasToBean(JsonWeb.class));

        Map<String, Object> result = new HashMap<String, Object>();
        List<JsonWeb> listaFichas = criteria.list();
        result.put("documentos", listaFichas);
        result.put("totalResultCount", listaFichas.size());

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<FichaSubcategoria> findSubCategorias(String fichaDocumentoId) throws Exception {

        Criteria criteria = createEntityCriteria();

        this.setFetchMode(criteria);
        criteria.createAlias("fichaDocumento", "f");
        criteria.add(Restrictions.eq("f.id", Integer.valueOf(fichaDocumentoId)));
        criteria.add(Restrictions.eq("habilitado", true));
        criteria.addOrder(Order.desc("fechaCrea"));

        List fichasSubCategoriaList = criteria.list();

        return fichasSubCategoriaList;
    }

}
