package cl.RedNorte.Backend.controller;

import cl.RedNorte.Backend.model.Notificacion;
import cl.RedNorte.Backend.service.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
@Tag(name = "Notificaciones", description = "Comunicación directa con los pacientes")
public class NotificacionController {

    private final NotificacionService notificacionService;

    @GetMapping("/paciente/{pacienteId}/pendientes")
    @Operation(summary = "Obtener mensajes no leídos para el portal")
    public ResponseEntity<List<Notificacion>> listarPendientes(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(notificacionService.obtenerMensajesPaciente(pacienteId));
    }
}
