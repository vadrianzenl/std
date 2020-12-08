
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
@Table(name = "STD_SUBCATEGORIA")
@SequenceGenerator(name = "SEQ_STD_SUBCATEGORIA", sequenceName = "SEQ_STD_SUBCATEGORIA", allocationSize = 1, initialValue= 1)
public class SubCategoria {
    @Id
    @Column(name = "stdsu_id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_STD_SUBCATEGORIA")
    private Integer id;
      
    @Column(name = "stdsu_vdescripcion")
    private String descripcion;
       
    @Column(name = "stdsu_vtema")
    private String tema;   
    
    @Column(name = "stdsu_bpublicar")
    private boolean publicar;//bit
    
    @Column(name = "stdsu_bhabilitado")
    private boolean habilitado;//bit
    
    @Column(name = "stdsu_bprivado")
    private boolean privado;//bit
    
    @Column(name = "aud_cusuario_crea")
    private String usuarioCrea;
    
    @Column(name = "aud_dfecha_crea")
    private Date fechaCrea;
    
    @Column(name = "aud_cusuario_modifica")
    private String usuarioModifica;
    
    @Column(name = "aud_dfecha_modifica")
    private Date fechaModifica;
    
    @ManyToOne
    @JoinColumn(name="stdc_id", nullable=true)    
    private Categoria categoria;
    
    //STD_FICHA_SUBCATEGORIA
    /*@JsonIgnore
    @ManyToMany(mappedBy="listaSubCategoria")
    private List<FichaDocumento> listaFichaDocumento = new ArrayList<FichaDocumento>();*/
}
