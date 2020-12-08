package pe.gob.congreso.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@Entity
@Table(name = "CONTROL_FIRMA")
public class ControlFirma {

	@Id
    @Column(name = "id_control_firma")
    private Integer id;
      
    @Column(name = "id_empleado")
    private String idEmpleado;
       
    @Column(name = "nombre_empleado")
    private String nombreEmpleado;  

    @Column(name = "estado")
    private String estado; 
    
    
}
