
package pe.gob.congreso.model;

import java.text.SimpleDateFormat;
import java.util.Date;

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
@Table(name = "STD_RELACIONA")
@SequenceGenerator(name = "SEQ_STD_RELACIONA", sequenceName = "SEQ_STD_RELACIONA", allocationSize = 1, initialValue= 1)
public class Relaciona {
    @Id
    @Column(name = "stdr_id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_STD_RELACIONA") 
    private Integer id;
          
    @Column(name = "stdr_bhabilitado")
    private boolean habilitado;

    @Column(name = "stdr_dfecha_relaciona")
    private Date fechaRelaciona;

    @Column(name = "aud_cusuario_modifica")
    private String usuarioModifica;
    
    @Column(name = "aud_dfecha_modifica")
    private Date fechaModifica;
    
    @ManyToOne
    @JoinColumn(name="stdr_cusuario_relaciona", nullable=true)    
    private Usuario usuarioRelaciona; 
    
    @ManyToOne
    @JoinColumn(name="stdcc_id", nullable=true)    
    private CentroCosto centroCosto;

    @ManyToOne
    @JoinColumn(name="stdf_id", nullable=true)    
    private FichaDocumento fichaDocumento;

    @ManyToOne
    @JoinColumn(name="stdr_ificha_relacionada", nullable=true)    
    private FichaDocumento fichaRelacionada;
    
    @ManyToOne
    @JoinColumn(name="stdr_itipo_relacion", nullable=true)   
    private Tipo tipoRelacion;
    
    @Transient
    private boolean adjuntos;
    
    @Transient
    private String listDirigidos;
    
    public void setListDirigidos(String listDirigidos) {
        this.listDirigidos= listDirigidos;
    }   
    
    public void setAdjuntos(boolean adjuntos) {
        this.adjuntos= adjuntos;
    }   

    public void setFechaRelaciona(Date fechaRelaciona) {
        this.fechaRelaciona= fechaRelaciona;
    }    

    public String getFechaRelaciona() {
        String fecha = "";
        if(this.fechaRelaciona!=null){
            fecha = new SimpleDateFormat("yyyy-MM-dd").format(this.fechaRelaciona);
        }       
        
        return fecha;
    }
}
