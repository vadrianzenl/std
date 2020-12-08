package pe.gob.congreso.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "STD_CORRELATIVO_DOCUMENTO")
@SequenceGenerator(name = "SEQ_STD_CORRELATIVO_DOCUMENTO", sequenceName = "SEQ_STD_CORRELATIVO_DOCUMENTO", allocationSize = 1, initialValue= 1)
public class CorrelativoDocumento {
    @Id
    @Column(name = "stdcd_id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_STD_CORRELATIVO_DOCUMENTO")
    private Integer id;

    @ManyToOne
    @JoinColumn(name="stdtd_id", nullable=true)
    private TipoDocumento tipoDocumento;

    @ManyToOne(optional=true)
    @JoinColumn(name="stde_id", nullable=true)
    private Empleado empleado;

    @Column(name = "stdcd_icorrelativo")
    private Integer correlativo;

    @ManyToOne
    @JoinColumn(name="stdt_itipo_firmado", nullable=true)
    private Tipo tipoFirmado;

    @ManyToOne
    @JoinColumn(name="stdcd_tipo_anio", nullable=true)
    private Tipo tipoAnio;

    @Column(name = "stdcd_cod_anio")
    private String codAnio;

    @Column(name = "stdcd_desc_anio")
    private String desAnio;

    @Column(name = "stdcd_vdescripcion")
    private String descripcion;

    @Column(name = "stdcd_bhabilitado")
    private boolean habilitado;//bit

    @Column(name = "aud_cusuario_crea")
    private String usuarioCreacion;

    @Column(name = "aud_dfecha_crea")
    private Date fechaCreacion;

    @Column(name = "aud_cusuario_modifica")
    private String usuarioModificacion;

    @Column(name = "aud_dfecha_modifica")
    private Date fechaModificacion;
}
