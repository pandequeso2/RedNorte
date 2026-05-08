package cl.RedNorte.Reasignacion.service.impl;

import cl.RedNorte.Reasignacion.service.NotificacionStrategy;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificacion implements NotificacionStrategy {
    @Override
    public void enviar(String mensaje, String destinatario) {
        // Aquí simulamos el envío de un correo
        System.out.println("Enviando EMAIL a [" + destinatario + "]: " + mensaje);
    }
}