package cl.RedNorte.Reasignacion.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.RedNorte.Reasignacion.model.ReasignacionCita;
import cl.RedNorte.Reasignacion.repository.ReasignacionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReasignacionService {

    // ¡Fíjate que ahora SOLO inyectamos el repositorio que le pertenece a este microservicio!
    private final ReasignacionRepository reasignacionRepository;

    @Transactional
    public void registrarReasignacion(Long citaCanceladaId, Long solicitudEsperaId, Long pacienteId) {
        
        System.out.println("Registrando nueva reasignación para la cita cancelada ID: " + citaCanceladaId);

        ReasignacionCita nuevaAsignacion = new ReasignacionCita();
        nuevaAsignacion.setCitaCanceladaId(citaCanceladaId);
        nuevaAsignacion.setSolicitudEsperaId(solicitudEsperaId);
        nuevaAsignacion.setPacienteId(pacienteId);
        // Aquí podríamos usar tu Factory Method de notificaciones que hicimos antes
        nuevaAsignacion.setNotificacionExitosa(true); 

        reasignacionRepository.save(nuevaAsignacion);

        System.out.println("Éxito: Paciente " + pacienteId + " reasignado en el microservicio.");
    }
}