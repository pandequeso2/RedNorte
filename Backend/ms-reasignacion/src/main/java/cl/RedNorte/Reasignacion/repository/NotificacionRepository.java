package cl.RedNorte.Reasignacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.RedNorte.ListasEspera.model.notificaciones.Notificacion;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    // Obtener mensajes no leídos para el portal del paciente [cite: 18]
    List<Notificacion> findByPacienteIdAndLeidaFalseOrderByFechaEnvioDesc(Long pacienteId);
}