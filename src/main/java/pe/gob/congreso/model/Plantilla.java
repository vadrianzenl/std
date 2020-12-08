package pe.gob.congreso.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Data
@Entity
@Table(name = "STD_PLANTILLA")
@SequenceGenerator(name = "SEQ_STD_PLANTILLA", sequenceName = "SEQ_STD_PLANTILLA", allocationSize = 1, initialValue= 1)
public class Plantilla {
    @Id
    @Column(name = "stdpla_id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_STD_PLANTILLA")
    private Integer id;

    @Column(name = "stdpla_vdescripcion")
    private String descripcion;

    @Column(name = "stdpla_vuuid")
    private String uuid;

    @Column(name = "stdpla_vuuid_large")
    private String uuidLarge;

    @Column(name = "stdpla_bhabilitado")
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
