package pe.gob.congreso.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Data
@Entity
@Table(name = "STD_PLANTILLA_DOCUMENTO")
@SequenceGenerator(name = "SEQ_STD_PLANTILLA_DOCUMENTO", sequenceName = "SEQ_STD_PLANTILLA_DOCUMENTO", allocationSize = 1, initialValue= 1)
public class PlantillaDocumento {
    @Id
    @Column(name = "stdpd_id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_STD_PLANTILLA_DOCUMENTO")
    private Integer id;

    @ManyToOne
    @JoinColumn(name="stdpla_id")
    private Plantilla plantilla;

    @ManyToOne
    @JoinColumn(name="stdt_id")
    private Tipo tipoDocumento;

    @Column(name = "stdpd_bhabilitado")
    private boolean habilitado;

    @Column(name = "aud_cusuario_crea")
    private String usuarioCrea;

    @Column(name = "aud_dfecha_crea")
    private Date fechaCrea;

    @Column(name = "aud_cusuario_modifica")
    private String usuarioModifica;

    @Column(name = "aud_dfecha_modifica")
    private Date fechaModifica;
}
