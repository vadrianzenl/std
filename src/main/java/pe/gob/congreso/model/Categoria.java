
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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@Entity
@Table(name = "STD_CATEGORIA")
@SequenceGenerator(name = "SEQ_STD_CATEGORIA", sequenceName = "SEQ_STD_CATEGORIA", allocationSize = 1, initialValue= 1)
public class Categoria {
    @Id
    @Column(name = "stdc_id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_STD_CATEGORIA") 
    private Integer id;
      
    @Column(name = "stdc_vnombre")
    private String nombre;
    
    @Column(name = "stdc_vdescripcion")
    private String descripcion;

    @Column(name = "stdc_bhabilitado")
    private Boolean habilitado;
    
    @Column(name = "aud_cusuario_crea")
    private String usuarioCrea;
    
    @Column(name = "aud_dfecha_crea")
    private Date fechaCrea;
    
    @Column(name = "aud_cusuario_modifica")
    private String usuarioModifica;
    
    @Column(name = "aud_dfecha_modifica")
    private Date fechaModifica;
    
    @JsonIgnore
    @OneToMany(mappedBy = "categoria")   
    private List<SubCategoria> listSubCategoria;
    
    //STD_CENTRO_COSTO_CATEGORIA
    //JsonIgnore
    //@ManyToMany(mappedBy="listaCategoria")
    //private List<CentroCosto> listaCentroCosto = new ArrayList<CentroCosto>();
}
