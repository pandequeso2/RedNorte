package cl.RedNorte.ListasEspera.service;

import java.util.List;
import org.springframework.stereotype.Service;
import cl.RedNorte.ListasEspera.model.SolicitudEspera;
import cl.RedNorte.ListasEspera.repository.SolicitudEsperaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListaEsperaService {

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