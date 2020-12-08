
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
@Table(name = "STD_SISTEMA_OPCION_USUARIO")
public class SistemaOpcionUsuario {
    @Id
    @Column(name = "stdou_id")
    private String id;

    @Column(name = "stdou_valordefecto") 
    private String valorPorDefecto;

    @ManyToOne
    @JoinColumn(name="stdso_id", nullable=false)    
    private SistemaOpcion opcion; 

    @ManyToOne(optional=true)
    @JoinColumn(name="stde_id", nullable=true)    
    private Empleado empleado; 
    
    @Column(name = "stdou_bhabilitado")
    private boolean habilitado;
    
    @Column(name="aud_cusuario_crea")    
    private String usuarioCrea;     
    
    @Column(name = "aud_dfecha_crea")
    private Date fechaCrea;
    
    @Column(name = "aud_cusuario_modifica")
    private String usuarioModifica;
    
    @Column(name = "aud_dfecha_modifica")
    private Date fechaModifica;
}
