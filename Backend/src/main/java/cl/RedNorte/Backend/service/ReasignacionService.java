package cl.RedNorte.Backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.RedNorte.Backend.model.CitaMedica;
import cl.RedNorte.Backend.model.EstadoCita;
import cl.RedNorte.Backend.model.ReasignacionCita;
import cl.RedNorte.Backend.model.SolicitudEspera;
import cl.RedNorte.Backend.repository.CitaMedicaRepository;
import cl.RedNorte.Backend.repository.ReasignacionRepository;
import cl.RedNorte.Backend.repository.SolicitudEsperaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReasignacionService {

    private final CitaMedicaRepository citaRepository;
    private final SolicitudEsperaRepository solicitudRepository;
    private final ReasignacionRepository reasignacionRepository;

    @Transactional
    public void procesarCancelacion(Long citaId) {
        // 1. Buscar la cita cancelada [cite: 16]
        CitaMedica cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (cita.getEstado() != EstadoCita.CANCELADA) return;

        // 2. Buscar al paciente con mayor prioridad en lista de espera [cite: 7, 13]
        List<SolicitudEspera> candidatos = solicitudRepository
                .findByEspecialidadIdAndEstadoOrderByPrioridadDesc(
                    cita.getEspecialidad().getId(), "PENDIENTE");

        if (!candidatos.isEmpty()) {
            SolicitudEspera beneficiado = candidatos.get(0);

            // 3. Crear registro de reasignación 
            ReasignacionCita nuevaAsignacion = new ReasignacionCita();
            nuevaAsignacion.setCitaCanceladaId(cita.getId());
            nuevaAsignacion.setSolicitudEsperaId(beneficiado.getId());
            nuevaAsignacion.setPacienteId(beneficiado.getPaciente().getId());
            nuevaAsignacion.setNotificacionExitosa(true);
            
            reasignacionRepository.save(nuevaAsignacion);

            // 4. Actualizar estado de la solicitud
            beneficiado.setEstado("ASIGNADO");
            solicitudRepository.save(beneficiado);
        }
    }
}
