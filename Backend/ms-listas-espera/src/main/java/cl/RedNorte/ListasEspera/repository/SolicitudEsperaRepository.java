package cl.RedNorte.ListasEspera.repository;

import cl.RedNorte.ListasEspera.model.SolicitudEspera;
import cl.RedNorte.ListasEspera.model.TipoAtencion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SolicitudEsperaRepository extends JpaRepository<SolicitudEspera, Long> {

    List<SolicitudEspera> findAllByOrderByFechaIngresoAsc();

    // FIX: antes era findByPaciente_Id — ahora el campo es pacienteId directo
    List<SolicitudEspera> findByPacienteId(Long pacienteId);

    // FIX: antes era findByEspecialidad_Id — ahora el campo es especialidadId directo
    List<SolicitudEspera> findByEspecialidadIdAndEstadoOrderByPrioridadDesc(Long especialidadId, String estado);

    List<SolicitudEspera> findByTipoAtencion(TipoAtencion tipoAtencion);
}