
package pe.gob.congreso.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "STD_TIPO")
@SequenceGenerator(name = "SEQ_STD_TIPO", sequenceName = "SEQ_STD_TIPO", allocationSize = 1, initialValue= 1)
public class Tipo{
       
    @Id
    @Column(name="stdt_id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_STD_TIPO")
    private Integer id;

    @Column(name="stdt_vnombre")
    private String nombre;

    @Column(name="stdt_vdescripcion")
    @NotBlank(message = "No debe ser vacío")
    private String descripcion;

    @Column(name="stdt_bhabilitado")
    private boolean habilitado;//bit

    @Column(name="stdt_iorden")
    private Integer orden;

    @Column(name = "aud_cusuario_crea")
    private String usuarioCreacion;

    @Column(name = "aud_dfecha_crea")
    private Date fechaCreacion;

    @Column(name = "aud_cusuario_modifica")
    private String usuarioModificacion;

    @Column(name = "aud_dfecha_modifica")
    private Date fechaModificacion;

    @Column(name = "stdt_bconfigurable")
    private boolean configurable;

    @JsonIgnore
    @OneToMany(mappedBy = "tipoRegistro")
    private List<FichaDocumento> listFichaDocumentoR;

    @JsonIgnore
    @OneToMany(mappedBy = "tipoEstado")
    private List<FichaDocumento> listFichaDocumentoE;
    
    @JsonIgnore
    @OneToMany(mappedBy = "tipoEnvio")
    private List<FichaDocumento> listFichaDocumentoEn;

    @JsonIgnore
    @OneToMany(mappedBy = "tipo")
    private List<Adjunto> listAdjuntos;

    @JsonIgnore
    @OneToMany(mappedBy = "tipo")
    private List<Motivo> listMotivos;

    @JsonIgnore
    @OneToMany(mappedBy = "estado")
    private List<Deriva> listEnviados;

    @JsonIgnore
    @OneToMany(mappedBy = "estado")
    private List<Deriva> listBitacoras;
    
    @JsonIgnore
    @OneToMany(mappedBy = "tipoRelacion")
    private List<Relaciona> listRelaciones;
    
    @JsonIgnore
    @OneToMany(mappedBy = "tipoAnio")
    private List<FiltroAnio> listFiltroAnio;
}
