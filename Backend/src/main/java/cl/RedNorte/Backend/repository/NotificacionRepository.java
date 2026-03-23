package cl.RedNorte.Backend.repository;

import cl.RedNorte.Backend.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    // Obtener mensajes no leídos para el portal del paciente [cite: 18]
    List<Notificacion> findByPacienteIdAndLeidaFalseOrderByFechaEnvioDesc(Long pacienteId);
}