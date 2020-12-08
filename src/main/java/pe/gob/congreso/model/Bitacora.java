
package pe.gob.congreso.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "STD_BITACORA")
@SequenceGenerator(name = "SEQ_STD_BITACORA", sequenceName = "SEQ_STD_BITACORA", allocationSize = 1, initialValue= 1)
public class Bitacora {
    @Id
    @Column(name = "stdb_id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_STD_BITACORA") 
    private Integer id;
      
    @Column(name = "stdb_vdescripcion")
    private String descripcion;
       
    @Column(name = "stdb_indicaciones")
    private String indicaciones;  

    @Column(name = "stdb_fecha") 
    private Date fecha;

    @ManyToOne
    @JoinColumn(name="stdb_ficha_documento", nullable=true)    
    private FichaDocumento fichaDocumento;

    @ManyToOne(optional=true)
    @JoinColumn(name="stdb_empleado", nullable=true)    
    private Empleado empleado;

    @ManyToOne
    @JoinColumn(name="stdb_estado", nullable=true)    
    private Tipo estado;    
    
    @Column(name = "stdb_bmodificado")
    private boolean modificado;
    
    @ManyToOne
    @JoinColumn(name="stdcc_id", nullable=true)    
    private CentroCosto centroCosto;
    
    @Transient
    private String listDeriva;
    
    public void setListDeriva(String listDeriva) {
        this.listDeriva= listDeriva;
    }    
    
    public void setFecha(Date fecha) {
        this.fecha= fecha;
    }    

    public String getFecha() {
        String fecha = "";
        String hora = "";
        if(this.fecha!=null){
            fecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(this.fecha);
        }       
        
        return fecha;
    }
    
    
}
