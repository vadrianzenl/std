package pe.gob.congreso.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import pe.gob.congreso.dao.PlantillaDocumentoDao;
import pe.gob.congreso.model.PlantillaDocumento;

@Repository("plantillaDocumentoDao")
public class PlantillaDocumentoDaoImpl extends AbstractDao<Integer, PlantillaDocumento> implements PlantillaDocumentoDao {

    @Override
    public PlantillaDocumento findByTipoDocumento(Integer tipoDocumento) throws Exception {
        Criteria criteria = createEntityCriteria();
        criteria.createAlias("tipoDocumento", "tipoDocumento");
        criteria.add(Restrictions.eq("tipoDocumento.id", tipoDocumento));
        criteria.add(Restrictions.eq("habilitado", true));
        PlantillaDocumento plantillaDocumento = (PlantillaDocumento) criteria.uniqueResult();
        return plantillaDocumento;
    }
}
