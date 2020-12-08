
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
@Table(name = "STD_GRUPO")
@SequenceGenerator(name = "SEQ_STD_GRUPO", sequenceName = "SEQ_STD_GRUPO", allocationSize = 1, initialValue= 1)
public class Grupo {
    @Id
    @Column(name = "stdg_id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_STD_GRUPO") 
    private Integer id;
      
    @Column(name = "stdg_vdescripcion")
    private String descripcion;
    
    @Column(name = "stdg_bhabilitado")
    private boolean habilitado;//bit
    
    @Column(name = "aud_cusuario_crea")
    private String usuarioCrea;
    
    @Column(name = "aud_dfecha_crea")
    private Date fechaCrea;
    
    @Column(name = "aud_cusuario_modifica")
    private String usuarioModifica;
    
    @Column(name = "aud_dfecha_modifica")
    private Date fechaModifica;

    @JsonIgnore
    @OneToMany(mappedBy = "grupo")   
    private List<GrupoCentroCosto> listCentroCosto;    

}
