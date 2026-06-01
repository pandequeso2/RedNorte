package cl.RedNorte.Reasignacion.service.impl;

import cl.RedNorte.Reasignacion.service.NotificacionStrategy;
import org.springframework.stereotype.Service;

@Service
public class SmsNotificacion implements NotificacionStrategy {
    @Override
    public void enviar(String mensaje, String destinatario) {
        // Aquí simulamos el envío de un SMS
        System.out.println("Enviando SMS al número [" + destinatario + "]: " + mensaje);
    }
}