package cl.RedNorte.Backend.repository.reasignacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.RedNorte.Backend.model.reasignaciones.ReasignacionCita;

import java.util.List;

@Repository
public interface ReasignacionRepository extends JpaRepository<ReasignacionCita, Long> {
    // Historial de reasignaciones por paciente
    List<ReasignacionCita> findByPacienteId(Long pacienteId);
    
    // Ver efectividad de las reasignaciones (Notificaciones exitosas)
    List<ReasignacionCita> findByNotificacionExitosaTrue();
}
