package cl.RedNorte.Backend.dto; // O el paquete que prefieras
import lombok.Data;
import cl.RedNorte.Backend.model.espera.TipoAtencion;

@Data
public class RegistrarSolicitudRequest {
    private Long pacienteId;
    private Long especialidadId;
    private TipoAtencion tipoAtencion;
    private String observaciones;
}