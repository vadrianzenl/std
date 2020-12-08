
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

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "STD_ADJUNTO")
@SequenceGenerator(name = "SEQ_STD_ADJUNTO", sequenceName = "SEQ_STD_ADJUNTO", allocationSize = 1, initialValue= 1)
public class Adjunto {
    @Id
    @Column(name = "stda_id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_STD_ADJUNTO") 
    private Integer id;
      
    @Column(name = "stda_vnombre_archivo")
    private String nombreArchivo;
       
    @Column(name = "stda_vdescripcion")
    private String descripcion;  

    @Column(name = "stda_dfecha_adjunto") 
    private Date fechaAdjunto;

    @ManyToOne
    @JoinColumn(name="stdf_id", nullable=true)    
    private FichaDocumento fichaDocumento;

    @ManyToOne(optional=true)
    @JoinColumn(name="stde_id", nullable=true)    
    private Empleado empleado;

    @ManyToOne
    @JoinColumn(name="stda_itipo", nullable=true)    
    private Tipo tipo;

    @Column(name = "stda_bderivado") 
    private Boolean derivado;

    @Column(name = "stda_bhabilitado") 
    private Boolean habilitado;

    @Column(name = "stda_bpublico") 
    private Boolean publico;

    @Column(name = "stda_vuuid")
    private String uuid;

    @ManyToOne
    @JoinColumn(name="stda_itipo_anexo", nullable=true)
    private Tipo tipoAnexo;

    @ManyToOne
    @JoinColumn(name="stda_itipo_archivo", nullable=true)
    private Tipo tipoArchivo;

    @Column(name="stda_ind_firma", nullable=true)
    private Boolean indicadorFirma;

    @ManyToOne
    @JoinColumn(name="stdcc_id", nullable=true)    
    private CentroCosto centroCosto;

    public void setFechaAdjunto(Date fechaAdjunto) {
        this.fechaAdjunto= fechaAdjunto;
    }    

    public String getFechaAdjunto() {
        String fecha = "";
        if(this.fechaAdjunto!=null){
            fecha = new SimpleDateFormat("yyyy-MM-dd").format(this.fechaAdjunto);
        }       
        
        return fecha;
    }
    
    public String getFechaString() {
        String fecha = "";
        if(this.fechaAdjunto!=null){
            fecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(this.fechaAdjunto);
        }       
        
        return fecha;
    }
    
    
}
