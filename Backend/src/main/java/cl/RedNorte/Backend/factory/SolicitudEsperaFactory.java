package cl.RedNorte.Backend.factory;

import cl.RedNorte.Backend.model.espera.SolicitudEspera;
import cl.RedNorte.Backend.model.espera.TipoAtencion;
import org.springframework.stereotype.Component;

/**
 * Factory Method Pattern — centraliza la creación de SolicitudEspera
 * asignando prioridades y valores por defecto según el tipo de atención.
 */
@Component
public class SolicitudEsperaFactory {

    public SolicitudEspera crear(Long pacienteId, Long especialidadId,
                                  TipoAtencion tipo, String observaciones) {
        SolicitudEspera solicitud = new SolicitudEspera();
        solicitud.setPacienteId(pacienteId);
        solicitud.setEspecialidadId(especialidadId);
        solicitud.setTipoAtencion(tipo);
        solicitud.setObservaciones(observaciones);
        solicitud.setPrioridad(calcularPrioridad(tipo));
        solicitud.setEstado("PENDIENTE");
        return solicitud;
    }

    private Integer calcularPrioridad(TipoAtencion tipo) {
        return switch (tipo) {
            case URGENCIA_EXTREMA -> 5;
            case INTERVENCION_QUIRURGICA      -> 4;
            case PROCEDIMIENTO    -> 3;
            case DIAGNOSTICO      -> 2;
            case CONSULTA_MEDICA        -> 1;
        };
    }
}