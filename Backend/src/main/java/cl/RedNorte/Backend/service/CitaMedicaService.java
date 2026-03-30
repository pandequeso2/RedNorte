package cl.RedNorte.Backend.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.RedNorte.Backend.events.CitaCanceladaEvent;
import cl.RedNorte.Backend.model.EstadoCita;
import cl.RedNorte.Backend.model.primary.CitaMedica;
import cl.RedNorte.Backend.repository.CitaMedicaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CitaMedicaService {

    private final CitaMedicaRepository citaRepository;
    private final ApplicationEventPublisher eventPublisher; // Inyectamos el publicador de Spring

    @Transactional
    public void cancelarCita(Long citaId) {
        CitaMedica cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        
        cita.setEstado(EstadoCita.CANCELADA);
        citaRepository.save(cita);

        CitaCanceladaEvent evento = new CitaCanceladaEvent(cita.getId(), cita.getEspecialidad().getId());
        eventPublisher.publishEvent(evento);
    }
}