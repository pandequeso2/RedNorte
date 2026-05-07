package cl.RedNorte.Reasignacion.service;

public interface NotificacionStrategy {
    void enviar(String mensaje, String destinatario);
}