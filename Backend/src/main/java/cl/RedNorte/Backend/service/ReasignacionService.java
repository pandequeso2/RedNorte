package cl.RedNorte.Backend.service;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.RedNorte.Backend.events.CitaCanceladaEvent;
import cl.RedNorte.Backend.model.espera.SolicitudEspera;
import cl.RedNorte.Backend.model.reasignaciones.ReasignacionCita;
import cl.RedNorte.Backend.repository.espera.SolicitudEsperaRepository;
import cl.RedNorte.Backend.repository.primary.CitaMedicaRepository;
import cl.RedNorte.Backend.repository.reasignacion.ReasignacionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReasignacionService {

    private final SolicitudEsperaRepository solicitudRepository;
    private final ReasignacionRepository reasignacionRepository;
    private final CitaMedicaRepository citaMedicaRepository;

    @Transactional
    public void procesarCancelacion(Long citaId) {
        var cita = citaMedicaRepository.findById(citaId)
            .orElseThrow(() -> new RuntimeException("Cita no encontrada: " + citaId));
        ejecutarLogicaReasignacion(cita.getId(), cita.getEspecialidad().getId());
    }

    @EventListener
    @Transactional
    public void manejarCitaCancelada(CitaCanceladaEvent evento) {
        ejecutarLogicaReasignacion(evento.getCitaId(), evento.getEspecialidadId());
    }

    private void ejecutarLogicaReasignacion(Long citaId, Long especialidadId) {
        System.out.println("Buscando candidatos para especialidad ID: " + especialidadId);

        List<SolicitudEspera> candidatos = solicitudRepository
                .findByEspecialidadIdAndEstadoOrderByPrioridadDesc(especialidadId, "PENDIENTE");

        if (!candidatos.isEmpty()) {
            SolicitudEspera beneficiado = candidatos.get(0);

            ReasignacionCita nuevaAsignacion = new ReasignacionCita();
            nuevaAsignacion.setCitaCanceladaId(citaId);
            nuevaAsignacion.setSolicitudEsperaId(beneficiado.getId());
            // FIX: antes era beneficiado.getPaciente().getId() → NullPointerException
            // ahora SolicitudEspera tiene pacienteId como Long directamente
            nuevaAsignacion.setPacienteId(beneficiado.getPacienteId());
            nuevaAsignacion.setNotificacionExitosa(true);

            reasignacionRepository.save(nuevaAsignacion);

            beneficiado.setEstado("ASIGNADO");
            solicitudRepository.save(beneficiado);

            System.out.println("Éxito: Paciente " + beneficiado.getPacienteId() + " reasignado.");
        } else {
            System.out.println("No hay pacientes en lista de espera para esta especialidad.");
        }
    }
}