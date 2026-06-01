package cl.RedNorte.Backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.RedNorte.Backend.service.CitaMedicaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@Tag(name = "Citas Médicas", description = "Gestión de citas y cancelaciones")
public class CitaMedicaController {

    private final CitaMedicaService citaMedicaService;

    @PostMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar una cita", description = "Cancela una cita y emite un evento para reasignación automática")
    public ResponseEntity<String> cancelarCita(@PathVariable Long id) {
        
        // Llamamos al servicio que cancela y publica el evento
        citaMedicaService.cancelarCita(id);
        
        return ResponseEntity.ok("Cita ID " + id + " cancelada exitosamente. El sistema está buscando un reemplazo en segundo plano.");
    }
}