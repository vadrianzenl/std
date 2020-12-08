
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
@Table(name = "STD_PROVEIDO")
@SequenceGenerator(name = "SEQ_STD_PROVEIDO", sequenceName = "SEQ_STD_PROVEIDO", allocationSize = 1, initialValue= 1)
public class Proveido {
    @Id
    @Column(name = "stdp_id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_STD_PROVEIDO")
    private Integer id;
      
    @Column(name = "stdp_vnombre")
    private String nombre;
       
    @Column(name = "stdp_bhabilitado")
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
    
    @JsonIgnore
    @OneToMany(mappedBy = "proveido")   
    private List<FichaProveido> listFichasProveido;

    //STD_FICHA_PROVEIDO
    /*@JsonIgnore
    @ManyToMany(mappedBy="listaProveido")
    private List<FichaDocumento> listaFichaDocumento = new ArrayList<FichaDocumento>();*/
    
}
