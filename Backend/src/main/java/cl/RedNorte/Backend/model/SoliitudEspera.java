package cl.RedNorte.Backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SolicitudEspera")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoliitudEspera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    
    @JoinColumn(nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Especialidad especialidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAtencion tipoAtencion;

    @Column(nullable = false)
    private Integer prioridad; // 1 (Baja) a 5 (Extrema)

    private LocalDateTime fechaIngreso;

    @Column(nullable = false)
    private String estado; // PENDIENTE, ASIGNADO, CANCELADO [cite: 7]

    private String observaciones;

    @PrePersist
    protected void onCreate() {
        this.fechaIngreso = LocalDateTime.now();
        if (this.estado == null) this.estado = "PENDIENTE";
    }
}