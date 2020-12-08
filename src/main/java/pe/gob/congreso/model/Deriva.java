
package pe.gob.congreso.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "STD_ENVIADO")
@SequenceGenerator(name = "SEQ_STD_ENVIADO", sequenceName = "SEQ_STD_ENVIADO", allocationSize = 1, initialValue = 1)
public class Deriva {
    @Id
    @Column(name = "stdd_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STD_ENVIADO")
    private Integer id;

    @Column(name = "stdd_vindicaciones")
    private String indicaciones;

    @Column(name = "stdd_dfecha_deriva")
    // @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date fechaDeriva;

    @Column(name = "aud_cusuario_modifica")
    private String usuarioModificacion;

    @Column(name = "aud_dfecha_modifica")
    private Date fechaModificacion;

    @Column(name = "stdd_bdirigido")
    private Integer dirigido;

    @Column(name = "stdd_bhabilitado")
    private boolean habilitado;// bit

    @Column(name = "stde_vgrupo")
    private String grupo;

    @Column(name = "stde_vcargo")
    private String cargo;

    @Column(name = "stde_bcopia")
    private boolean ccopia;

    @Column(name = "stdcc_id_idestino")
    private Integer centroCostoDestinoId;

    @Column(name = "stde_anio")
    private Integer anio;

    @Column(name = "stde_bresponsable")
    private boolean esResponsable;

    @ManyToOne
    @JoinColumn(name = "stdd_iestado", nullable = true)
    private Tipo estado;

    @ManyToOne
    @JoinColumn(name = "stdf_id", nullable = true)
    private FichaDocumento fichaDocumento;

    @ManyToOne(optional = true)
    @JoinColumn(name = "stde_id_origen", nullable = true)
    private Empleado empleadoOrigen;

    @ManyToOne
    @JoinColumn(name = "stdcc_id_origen", nullable = true)
    private CentroCosto centroCostoOrigen;

    @ManyToOne(optional = true)
    @JoinColumn(name = "stde_id_destino", nullable = true)
    private Empleado empleadoDestino;

    @ManyToOne
    @JoinColumn(name = "stdcc_id_destino", nullable = true)
    private CentroCosto centroCostoDestino;

    @ManyToOne
    @JoinColumn(name = "stdmod_id", nullable = true)
    private MotivoDeriva motivoDeriva;

    @ManyToOne
    @JoinColumn(name = "stded_id", nullable = true)
    private EstadoDeriva estadoDeriva;

    @ManyToOne
    @JoinColumn(name = "stdf_id_respuesta", nullable = true)
    private FichaDocumento fichaRespuesta;

    @ManyToOne
    @JoinColumn(name = "stde_id_destino_externo", nullable = true)
    private EnviadoExterno enviadoExterno;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "stdd_id", referencedColumnName = "stdd_id")
    @JsonIgnore
    private List<SeguimientoFisico> seguimientoFisico;

    @Transient
    private boolean indicaDirigido = false;

    @Transient
    private boolean indicaEncontroEM;

    @Transient
    private boolean recibiConforme;

    public void setRecibiConforme(boolean recibiConforme) {
        this.recibiConforme = recibiConforme;
    }

    public void setIndicaEncontroEM(boolean indicaEncontroEM) {
        this.indicaEncontroEM = indicaEncontroEM;
    }

    public void setFechaDeriva(Date fechaDeriva) {
        this.fechaDeriva = fechaDeriva;
    }

    public String getFechaDeriva() {
        String fecha = "";
        if (this.fechaDeriva != null) {
            fecha = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(this.fechaDeriva);
            // fecha = new SimpleDateFormat("yyyy-MM-dd").format(this.fechaDeriva);
        }

        return fecha;

    }

}
