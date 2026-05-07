package cl.RedNorte.Reasignacion.factory;

import cl.RedNorte.Reasignacion.service.NotificacionStrategy;
import cl.RedNorte.Reasignacion.service.impl.EmailNotificacion;
import cl.RedNorte.Reasignacion.service.impl.SmsNotificacion;
import org.springframework.stereotype.Component;

@Component
public class NotificacionFactory {

    public NotificacionStrategy obtenerCanalDeEnvio(String tipoCanal) {
        if ("EMAIL".equalsIgnoreCase(tipoCanal)) {
            return new EmailNotificacion();
        } else if ("SMS".equalsIgnoreCase(tipoCanal)) {
            return new SmsNotificacion();
        }
        
        throw new IllegalArgumentException("El tipo de notificación no está soportado: " + tipoCanal);
    }
}