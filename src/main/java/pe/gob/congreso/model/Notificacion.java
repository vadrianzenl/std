
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
@Table(name = "STD_NOTIFICACION")
@SequenceGenerator(name = "SEQ_STD_NOTIFICACION", sequenceName = "SEQ_STD_NOTIFICACION", allocationSize = 1, initialValue= 1)
public class Notificacion{
       
    @Id
    @Column(name="stdn_id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_STD_NOTIFICACION")
    private Integer id;

    @Column(name="stdn_descripcion")
    private String descripcion;

    @Column(name="stdn_bhabilitado")
    private boolean habilitado;//bit

    @Column(name = "aud_cusuario_crea")
    private String usuarioCreacion;

    @Column(name = "aud_dfecha_crea")
    private Date fechaCreacion;

    @Column(name = "aud_cusuario_modifica")
    private String usuarioModificacion;

    @Column(name = "aud_dfecha_modifica")
    private Date fechaModificacion;
    
    @JsonIgnore
    @OneToMany(mappedBy = "notificacion")
    private List<NotificacionEmpleado> listNotificacionEmpleado;

}
