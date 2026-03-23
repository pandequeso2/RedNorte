package cl.RedNorte.Backend.service;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.RedNorte.Backend.events.CitaCanceladaEvent;
import cl.RedNorte.Backend.model.ReasignacionCita;
import cl.RedNorte.Backend.model.SolicitudEspera;
import cl.RedNorte.Backend.repository.ReasignacionRepository;
import cl.RedNorte.Backend.repository.SolicitudEsperaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReasignacionService {

    // ¡Fíjate que eliminamos CitaMedicaRepository! Ya no dependemos de esa base de datos.
    private final SolicitudEsperaRepository solicitudRepository;
    private final ReasignacionRepository reasignacionRepository;

    @EventListener // Esta anotación hace la magia orientada a eventos
    @Transactional
    public void manejarCitaCancelada(CitaCanceladaEvent evento) {
        
        System.out.println("Evento recibido: Reasignando cita cancelada ID: " + evento.getCitaId());

        // 1. Buscar al paciente con mayor prioridad en lista de espera usando los datos del evento
        List<SolicitudEspera> candidatos = solicitudRepository
                .findByEspecialidadIdAndEstadoOrderByPrioridadDesc(
                    evento.getEspecialidadId(), "PENDIENTE");

        if (!candidatos.isEmpty()) {
            SolicitudEspera beneficiado = candidatos.get(0);

            // 2. Crear registro de reasignación 
            ReasignacionCita nuevaAsignacion = new ReasignacionCita();
            nuevaAsignacion.setCitaCanceladaId(evento.getCitaId());
            nuevaAsignacion.setSolicitudEsperaId(beneficiado.getId());
            nuevaAsignacion.setPacienteId(beneficiado.getPaciente().getId());
            nuevaAsignacion.setNotificacionExitosa(true);
            
            reasignacionRepository.save(nuevaAsignacion);

            // 3. Actualizar estado de la solicitud
            beneficiado.setEstado("ASIGNADO");
            solicitudRepository.save(beneficiado);
            
            System.out.println("Éxito: Paciente " + beneficiado.getPaciente().getId() + " reasignado a la cita.");
        }
    }
}