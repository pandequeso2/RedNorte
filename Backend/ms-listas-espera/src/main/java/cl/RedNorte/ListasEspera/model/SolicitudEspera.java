package cl.RedNorte.ListasEspera.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class SolicitudEspera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FIX: reemplazado @ManyToOne Paciente por Long pacienteId
    // JPA no puede hacer JOIN entre distintas bases de datos.
    // Paciente vive en rednortedb y SolicitudEspera en rednorte_espera.
    @Column(nullable = false)
    private Long pacienteId;

    // FIX: igual para Especialidad — era @ManyToOne cruzando datasources
    @Column(nullable = false)
    private Long especialidadId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAtencion tipoAtencion;

    @Column(nullable = false)
    private Integer prioridad; // 1 (Baja) a 5 (Extrema)

    private LocalDateTime fechaIngreso;

    @Column(nullable = false)
    private String estado; // PENDIENTE, ASIGNADO, CANCELADO

    private String observaciones;

    @PrePersist
    protected void onCreate() {
        this.fechaIngreso = LocalDateTime.now();
        if (this.estado == null) this.estado = "PENDIENTE";
    }
}