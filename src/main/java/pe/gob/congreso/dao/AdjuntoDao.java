package pe.gob.congreso.dao;

import java.util.List;
import pe.gob.congreso.model.Adjunto;

public interface AdjuntoDao {

    public Adjunto create(Adjunto d) throws Exception;

    public List<Adjunto> findAdjuntos(String fichaDocumentoId) throws Exception;

    public int verificadAdjuntos(String fichaDocumentoId) throws Exception;

    public Adjunto findById(Integer id) throws Exception;

    public int updateUUID(String nameFile, String uuid) throws Exception;

    public Adjunto findAdjuntoDocx(Integer fichaDocumentoId, Integer tipoArchivo, Integer tipoAnexo, boolean indicadorFirma) throws Exception;

    public Integer getFichaDocRecibidoByPdfOfDependency(Integer empId, String cc, String pdf) throws Exception;

    public Integer getFichaDocRecibidoByPdfOfPerson(Integer empId, String cc, String pdf) throws Exception;

    public Integer getFichaDocEnviadoByPdfOfDependency(String cc, String pdf) throws Exception;

    public Integer getFichaDocEnviadoByPdfOfPerson(Integer empId, String cc, String pdf) throws Exception;

    public Integer getFichaDocRecibidoByPdfOfMesa(String cc, String pdf) throws Exception;
}
