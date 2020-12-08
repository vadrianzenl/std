package pe.gob.congreso.model;

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
@Table(name = "STD_ACTIVIDAD")
@SequenceGenerator(name = "SEQ_STD_ACTIVIDAD", sequenceName = "SEQ_STD_ACTIVIDAD", allocationSize = 1, initialValue= 1)
public class Actividad {

    public enum Operacion {
        CREAR,
        ACTUALIZAR,
        HABILITAR,
        DESHABILITAR,
        ELIMINAR
    }

    @Id
    @Column(name = "stda_id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_STD_ACTIVIDAD")
    private Integer id;

    @Column(name = "stda_vnombre_tabla")
    private String nombreTabla;

    @Column(name = "stda_voperacion")
    private String operacion;

    @Column(name = "stda_vcontenido")
    private String contenido;

    @Column(name = "stda_dfecha_hora")
    private Date fechaHora;

    @Column(name = "stda_vnombre_equipo")
    private String nombreEquipo;

    @Column(name = "stda_vobservaciones")
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "stdu_cusuario", nullable = false)
    private Usuario usuario;
    
    @Column(name = "stda_ccosto")
    private String ccosto;
    
    @Column(name = "stda_perfil")
    private String perfil;
    
}
