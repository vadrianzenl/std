
package pe.gob.congreso.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "STD_SEGUIMIENTO_FISICO")
@SequenceGenerator(name = "SEQ_STD_SEGUIMIENTO_FISICO", sequenceName = "SEQ_STD_SEGUIMIENTO_FISICO", allocationSize = 1, initialValue = 1)
public class SeguimientoFisico {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "stdsf_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STD_SEGUIMIENTO_FISICO")
    @JsonProperty
    private Integer id;

    // @Id
    @Column(name = "stdd_id")
    @JsonProperty
    private Integer derivaId;

    @Column(name = "stdsf_cestado")
    @JsonProperty
    private String estado;

    @Column(name = "stdsf_dfecha_recep")
    @JsonProperty
    private Date fechaRecep;

    @Column(name = "stdsf_cc_id")
    @JsonProperty
    private String centroCostoId;

    @Column(name = "stdsf_bhabilitado")
    @JsonProperty
    private boolean habilitado;

    @Column(name = "aud_cusuario_crea")
    @JsonProperty
    private String usuarioCrea;

    @Column(name = "aud_dfecha_crea")
    @JsonProperty
    private Date fechaCrea;

    @Column(name = "aud_cusuario_modifica")
    @JsonProperty
    private String usuarioModifica;

    @Column(name = "aud_dfecha_modifica")
    @JsonProperty
    private Date fechaModifica;

    @Transient
    private Deriva deriva;

    @Transient
    private Date fechaFisico;

    public String getFechaFisico() {
        String fecha = "";
        if (this.fechaRecep != null) {
            fecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss aa").format(this.fechaRecep);
        }

        return fecha;
    }
}