package cl.RedNorte.Reasignacion.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reasignaciones_automaticas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReasignacionCita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long citaCanceladaId; // ID de la cita que quedó vacante

    @Column(nullable = false)
    private Long solicitudEsperaId; // ID del registro de la lista de espera beneficiado

    @Column(nullable = false)
    private Long pacienteId; // Paciente que fue notificado

    private LocalDateTime fechaProcesamiento;

    private Boolean notificacionExitosa; // Si el paciente confirmó o recibió el aviso

    @PrePersist
    protected void onProcess() {
        this.fechaProcesamiento = LocalDateTime.now(); //Ve la fecha actual 
    }
}
