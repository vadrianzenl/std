
package pe.gob.congreso.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@Entity
@Table(name = "STD_TIPO_DOCUMENTO")
@SequenceGenerator(name = "SEQ_STD_TIPO_DOCUMENTO", sequenceName = "SEQ_STD_TIPO_DOCUMENTO", allocationSize = 1, initialValue= 1)
public class TipoDocumento {
    @Id
    @Column(name = "stdtd_id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_STD_TIPO_DOCUMENTO")
    private Integer id;

    @Column(name = "stdtd_bhabilitado") 
    private boolean habilitado;//bit
    
    @Column(name = "aud_cusuario_crea")
    private String usuarioCrea;
    
    @Column(name = "aud_dfecha_crea")
    private Date fechaCrea;
    
    @Column(name = "aud_cusuario_modifica")
    private String usuarioModifica;
    
    @Column(name = "aud_dfecha_modifica")
    private Date fechaModifica;

    @ManyToOne
    @JoinColumn(name="stdcc_id", nullable=true)    
    private CentroCosto centroCosto;

    @ManyToOne
    @JoinColumn(name="stdt_id", nullable=true)    
    private Tipo tipo;
    
    @JsonIgnore
    @OneToMany(mappedBy = "tipoDocumento")   
    private List<FichaDocumento> listFichaDocumento;
}
