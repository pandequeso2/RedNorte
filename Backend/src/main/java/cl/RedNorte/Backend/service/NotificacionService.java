package cl.RedNorte.Backend.service;

import cl.RedNorte.Backend.model.Notificacion;
import cl.RedNorte.Backend.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public void enviarNotificacion(Long pacienteId, String mensaje) {
        Notificacion noti = new Notificacion();
        noti.setPacienteId(pacienteId);
        noti.setMensaje(mensaje);
        notificacionRepository.save(noti);
    }

    public List<Notificacion> obtenerMensajesPaciente(Long pacienteId) {
        return notificacionRepository.findByPacienteIdAndLeidaFalseOrderByFechaEnvioDesc(pacienteId);
    }
}
