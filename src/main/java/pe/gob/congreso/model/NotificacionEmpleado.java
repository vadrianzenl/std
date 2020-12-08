
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

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "STD_NOTIFICACION_EMPLEADO")
@SequenceGenerator(name = "SEQ_STD_NOTIFICACION_EMPLEADO", sequenceName = "SEQ_STD_NOTIFICACION_EMPLEADO", allocationSize = 1, initialValue= 1)
public class NotificacionEmpleado{
       
    @Id
    @Column(name="stdne_id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_STD_NOTIFICACION_EMPLEADO")
    private Integer id;

    @ManyToOne
    @JoinColumn(name="stdn_id")    
    private Notificacion notificacion;
    
    @ManyToOne
    @JoinColumn(name="stde_id")    
    private Empleado empleado;

    @Column(name="stdne_bhabilitado")
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
