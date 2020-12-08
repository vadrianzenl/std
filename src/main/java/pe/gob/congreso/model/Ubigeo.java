
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
@Table(name = "STD_UBIGEO")
public class Ubigeo {
    @Id
    @Column(name = "stdub_codigo")
    private String codigo;
      
    @Column(name = "stdub_vdescripcion")
    private String descripcion;
    
}
