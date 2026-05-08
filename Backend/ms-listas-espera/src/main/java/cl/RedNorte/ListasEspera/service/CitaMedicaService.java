package cl.RedNorte.ListasEspera.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import cl.RedNorte.ListasEspera.model.EstadoCita;
import cl.RedNorte.ListasEspera.model.CitaMedica;
import cl.RedNorte.ListasEspera.model.SolicitudEspera;
import cl.RedNorte.ListasEspera.repository.CitaMedicaRepository;
import cl.RedNorte.ListasEspera.repository.SolicitudEsperaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CitaMedicaService {

    private final CitaMedicaRepository citaRepository;
    private final SolicitudEsperaRepository solicitudRepository; // Añadimos esto para buscar candidatos

    @Transactional
    public void cancelarCita(Long citaId) {
        CitaMedica cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        
        // 1. Cancelamos la cita actual
        cita.setEstado(EstadoCita.CANCELADA);
        citaRepository.save(cita);

        // 2. Buscamos a alguien en la lista de espera (Lógica que rescatamos del viejo ReasignacionService)
        List<SolicitudEspera> candidatos = solicitudRepository
                .findByEspecialidadIdAndEstadoOrderByPrioridadDesc(cita.getEspecialidad().getId(), "PENDIENTE");

        if (!candidatos.isEmpty()) {
            SolicitudEspera beneficiado = candidatos.get(0);
            beneficiado.setEstado("ASIGNADO");
            solicitudRepository.save(beneficiado);

            // 3. COMUNICACIÓN ENTRE MICROSERVICIOS: Llamamos al ms-reasignacion (Puerto 8082) por HTTP
            try {
                RestTemplate restTemplate = new RestTemplate();
                String url = "http://localhost:8082/api/reasignaciones/procesar-cancelacion" +
                             "?citaCanceladaId=" + cita.getId() +
                             "&solicitudEsperaId=" + beneficiado.getId() +
                             "&pacienteId=" + beneficiado.getPacienteId();

                restTemplate.postForEntity(url, null, String.class);
                System.out.println("Microservicio de Reasignación notificado con éxito vía HTTP.");
                
            } catch (Exception e) {
                System.err.println("Error al comunicarse con ms-reasignacion: " + e.getMessage());
            }
        } else {
            System.out.println("Cita cancelada. No hay pacientes en lista de espera para esta especialidad.");
        }
    }
}