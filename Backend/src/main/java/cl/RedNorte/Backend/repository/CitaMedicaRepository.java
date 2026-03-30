package cl.RedNorte.Backend.repository;

import cl.RedNorte.Backend.model.EstadoCita;
import cl.RedNorte.Backend.model.primary.CitaMedica;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CitaMedicaRepository extends JpaRepository<CitaMedica, Long> {
    
    // Detectar cancelaciones de último momento para reasignar [cite: 7, 23]
    List<CitaMedica> findByEstadoAndEspecialidadId(EstadoCita estado, Long especialidadId);

    // Listar citas disponibles para una especialidad específica
    List<CitaMedica> findByEspecialidadIdAndEstado(Long especialidadId, EstadoCita estado);

    // Contador para indicadores de pérdida de horas médicas [cite: 19]
    long countByEstado(EstadoCita estado);
}