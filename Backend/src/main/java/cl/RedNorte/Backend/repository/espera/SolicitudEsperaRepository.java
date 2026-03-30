package cl.RedNorte.Backend.repository.espera;


import cl.RedNorte.Backend.model.espera.SolicitudEspera;
import cl.RedNorte.Backend.model.espera.TipoAtencion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SolicitudEsperaRepository extends JpaRepository<SolicitudEspera, Long> {
    
    // Problema: Pacientes con largos periodos de espera [cite: 15]
    List<SolicitudEspera> findAllByOrderByFechaIngresoAsc();

    // Para el Portal del Paciente: Ver estado de sus solicitudes 
    List<SolicitudEspera> findByPacienteId(Long pacienteId);

    // Filtrar por especialidad y alta prioridad para reasignación automática [cite: 23]
    List<SolicitudEspera> findByEspecialidadIdAndEstadoOrderByPrioridadDesc(Long especialidadId, String estado);
    
    // Buscar por tipo (Ej: Cirugías) para reportes de gestión [cite: 13]
    List<SolicitudEspera> findByTipoAtencion(TipoAtencion tipoAtencion);
}