package pe.gob.congreso.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Data
@Entity
@Table(name = "STD_CARGO")
@SequenceGenerator(name = "SEQ_STD_CARGO", sequenceName = "SEQ_STD_CARGO", allocationSize = 1, initialValue= 1)
public class Cargo {
    @Id
    @Column(name = "stdca_id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_STD_CARGO")
    private Integer id;

    @Column(name = "stdca_vdescripcion_masculino")
    private String descripcionMasculino;

    @Column(name = "stdca_vdescripcion_femenino")
    private String descripcionFemenino;

    @Column(name = "stdca_genero")
    private Boolean genero;

    @Column(name = "stda_bhabilitado")
    private Boolean habilitado;

    @Column(name = "aud_cusuario_crea")
    private String usuarioCrea;

    @Column(name = "aud_dfecha_crea")
    private Date fechaCrea;

    @Column(name = "aud_cusuario_modifica")
    private String usuarioModifica;

    @Column(name = "aud_dfecha_modifica")
    private Date fechaModifica;
}
