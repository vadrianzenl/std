
package pe.gob.congreso.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
import pe.gob.congreso.model.PerfilMenu.PerfilMenuPK;

@NoArgsConstructor
@Data
@Entity
@IdClass(value = EmpleadoCentroCosto.EmpleadoCentroCostoPK.class)
@Table(name = "STD_EMPLEADO_CENTRO_COSTO")
public class EmpleadoCentroCosto {
    @Id    
    private Integer empleadoId;
    
    @Id 
    private String centroCostoId;
    
    @Column(name = "stdecc_bhabilitado")
    private boolean habilitado;
    
    @Column(name = "stdecc_activo")
    private boolean activo;
    
    @Column(name = "stdecc_tipo")
    private String tipo;
    
    @Column(name = "stdecca_id")
    private int centroCostoActualId;
    
    @MapsId("empleadoId")
    @ManyToOne
    @JoinColumn(name="stde_id", nullable=true)
    @JsonIgnore
    private Empleado empleado;

    @MapsId("centroCostoId")
    @ManyToOne
    @JoinColumn(name="stdcc_id", nullable=true)
    private CentroCosto centroCosto;
    
    @Data
    static class EmpleadoCentroCostoPK implements Serializable{
        @Column(name="stde_id")
        private Integer empleadoId;
        @Column(name="stdcc_id")
        private String centroCostoId;
    }

    @JsonProperty
    public void setEmpleado(Empleado e) {
        this.empleado= e;
    }

    @JsonIgnore
    public Empleado getEmpleado() {
        return empleado;
    }
}
