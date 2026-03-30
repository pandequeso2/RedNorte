package cl.RedNorte.Backend.service;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.RedNorte.Backend.events.CitaCanceladaEvent;
import cl.RedNorte.Backend.model.espera.SolicitudEspera;
import cl.RedNorte.Backend.model.reasignaciones.ReasignacionCita;
import cl.RedNorte.Backend.repository.CitaMedicaRepository;
import cl.RedNorte.Backend.repository.espera.SolicitudEsperaRepository;
import cl.RedNorte.Backend.repository.reasignacion.ReasignacionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReasignacionService {

    private final SolicitudEsperaRepository solicitudRepository;
    private final ReasignacionRepository reasignacionRepository;
    // Necesitamos este para obtener la especialidad de la cita cancelada
    private final CitaMedicaRepository citaMedicaRepository; 

    @Transactional
    public void procesarCancelacion(Long citaId) {
        // 1. Obtener los datos de la cita que se canceló
        var cita = citaMedicaRepository.findById(citaId)
            .orElseThrow(() -> new RuntimeException("Cita no encontrada: " + citaId));

        // 2. Ejecutar la lógica de reasignación usando la especialidad de esa cita
        ejecutarLogicaReasignacion(cita.getId(), cita.getEspecialidad().getId());
    }

    @EventListener
    @Transactional
    public void manejarCitaCancelada(CitaCanceladaEvent evento) {
        // Este método sigue funcionando para eventos automáticos
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
            nuevaAsignacion.setPacienteId(beneficiado.getPaciente().getId());
            nuevaAsignacion.setNotificacionExitosa(true);
            
            reasignacionRepository.save(nuevaAsignacion);

            beneficiado.setEstado("ASIGNADO");
            solicitudRepository.save(beneficiado);
            
            System.out.println("Éxito: Paciente " + beneficiado.getPaciente().getId() + " reasignado.");
        } else {
            System.out.println("No hay pacientes en lista de espera para esta especialidad.");
        }
    }
}