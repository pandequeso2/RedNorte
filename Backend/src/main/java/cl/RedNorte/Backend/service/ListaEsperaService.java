package cl.RedNorte.Backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.RedNorte.Backend.model.espera.SolicitudEspera;
import cl.RedNorte.Backend.repository.espera.SolicitudEsperaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListaEsperaService {
    private  SolicitudEsperaRepository solEspera;

    public SolicitudEspera crearSolicitud(SolicitudEspera solicitud) {
        return solEspera.save(solicitud);
    }

    // Obtener pacientes con mayor tiempo de espera (Resuelve Problema 1) [cite: 15]
    public List<SolicitudEspera> obtenerPacientesCriticos() {
        return solEspera.findAllByOrderByFechaIngresoAsc();
    }

    // Consultar estado para el Portal del Paciente [cite: 24]
    public List<SolicitudEspera> consultarEstadoPaciente(Long pacienteId) {
        return solEspera.findByPacienteId(pacienteId);
    }
}
