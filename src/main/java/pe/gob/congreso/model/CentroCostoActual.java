
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
@Table(name = "STD_CENTRO_COSTO_ACTUAL")
public class CentroCostoActual {
    @Id
    @Column(name = "stdcca_id")
    private String id;

    @Column(name = "stdcca_vdescripcion") 
    private String descripcion;
}
