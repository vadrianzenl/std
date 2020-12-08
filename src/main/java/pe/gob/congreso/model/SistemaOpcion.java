
package pe.gob.congreso.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "STD_SISTEMA_OPCION")
public class SistemaOpcion {
    @Id
    @Column(name = "stdso_id")
    private String id;

    @Column(name = "stdso_vdescripcion") 
    private String descripcion;

    @Column(name = "stdso_vcodopcion") 
    private String codOpcion;    
    
    @Column(name="aud_cusuario_crea")    
    private String usuarioCrea;     
    
    @Column(name = "aud_dfecha_crea")
    private Date fechaCrea;
    
    @Column(name = "aud_cusuario_modifica")
    private String usuarioModifica;
    
    @Column(name = "aud_dfecha_modifica")
    private Date fechaModifica;
}
