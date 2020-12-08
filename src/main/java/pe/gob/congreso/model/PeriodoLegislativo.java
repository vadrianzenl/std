
package pe.gob.congreso.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.List;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@Entity
@Table(name = "STD_PERIODO_LEGISLATIVO")
public class PeriodoLegislativo {
    @Id
    @Column(name = "stdpl_codigo")
    private Integer codigo;
      
    @Column(name = "stdpl_vdescripcion")
    private String descripcion;
    
    @Column(name = "stdpl_vdescripcion_abrev")
    private String descripcionAbrev;
       
    @Column(name = "stdpl_dfecha_inicio")
    private Date fechaInicio;  
    
    @Column(name = "stdpl_dfecha_fin")
    private Date fechaFin;
    
    @JsonIgnore
    @OneToMany(mappedBy = "periodoLegislativo")   
    private List<AnioLegislativo> listAnioLegislativo;
    
    @JsonIgnore
    @OneToMany(mappedBy = "periodoLegislativo")   
    private List<Legislatura> listLegislatura;
}
