package pe.gob.congreso.model.util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import pe.gob.congreso.model.CentroCosto;
import pe.gob.congreso.model.Empleado;
import pe.gob.congreso.model.EstadoDeriva;
import pe.gob.congreso.model.FichaDocumento;
import pe.gob.congreso.model.MotivoDeriva;
import pe.gob.congreso.model.Tipo;
import pe.gob.congreso.model.SeguimientoFisico;

@NoArgsConstructor
@Data
public class DerivaUtil implements Serializable, Comparable<Object> {

    /**
     * Serial para guardar los estados de los atributos del objeto
     */
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String indicaciones;

    private Date fechaDeriva;

    private String usuarioModificacion;

    private Date fechaModificacion;

    private Integer dirigido;

    private boolean habilitado;

    private String grupo;

    private String cargo;

    private Tipo estado;

    private FichaDocumento fichaDocumento;

    private Empleado empleadoOrigen;

    private CentroCosto centroCostoOrigen;

    private Empleado empleadoDestino;

    private CentroCosto centroCostoDestino;

    private MotivoDeriva motivoDeriva;

    private EstadoDeriva estadoDeriva;

    private FichaDocumento fichaRespuesta;

    public String getFechaDeriva() {
        String fecha = "";
        if (this.fechaDeriva != null) {
            fecha = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(this.fechaDeriva);
            // fecha = new SimpleDateFormat("yyyy-MM-dd").format(this.fechaDeriva);
        }

        return fecha;
    }

    // private List<SeguimientoFisico> seguimientoFisico;

    private String remitente;

    private Integer adjuntos;

    private int derivados;

    private int dirigidos;

    // Mesa de Partes
    private Integer numeroMesaPartes;

    private String sumillaMesaPartes;

    private String etapaMesaPartes;

    private String etapaDesMesaPartes;

    private String colorSemaforoMesaPartes;

    private String codigoEtapaMesaPartes;

    private String enviadoADes;

    private Boolean notas;

    private Integer total;

    private int recibiConforme;

    private SeguimientoFisico segFisico;

    private String descSemana;

    /*
     * Realiza ordenamientos en objetos tipo Collection:
     * Collections.sort(objVector); Collections.sort(objVector,
     * objLLaveOrdenamiento);
     */
    @Override
    public int compareTo(Object obj) {
        if (!(obj instanceof DerivaUtil))
            throw new ClassCastException("Valor invalido");
        DerivaUtil tmp = (DerivaUtil) obj;
        // Ascendente
        // return (this.numeroMesaPartes.compareTo(tmp.numeroMesaPartes));
        // Descendente
        return (tmp.numeroMesaPartes.compareTo(this.numeroMesaPartes));
    }
    // Fin Mesa de Partes

}
