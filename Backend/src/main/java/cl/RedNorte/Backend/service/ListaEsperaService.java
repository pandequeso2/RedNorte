package cl.RedNorte.Backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.RedNorte.Backend.model.espera.SolicitudEspera;
import cl.RedNorte.Backend.repository.espera.SolicitudEsperaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListaEsperaService {

    // FIX: faltaba "final" — sin él Lombok no genera el constructor y queda null
    private final SolicitudEsperaRepository solEspera;

    public SolicitudEspera crearSolicitud(SolicitudEspera solicitud) {
        return solEspera.save(solicitud);
    }

    public List<SolicitudEspera> obtenerPacientesCriticos() {
        return solEspera.findAllByOrderByFechaIngresoAsc();
    }

    public List<SolicitudEspera> consultarEstadoPaciente(Long pacienteId) {
        return solEspera.findByPacienteId(pacienteId);
    }
}