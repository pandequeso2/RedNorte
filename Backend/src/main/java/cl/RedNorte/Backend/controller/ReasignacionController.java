package cl.RedNorte.Backend.controller;

import cl.RedNorte.Backend.service.ReasignacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reasignacion")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@Tag(name = "Reasignación", description = "Optimización de horas médicas por cancelaciones")
public class ReasignacionController {

    private final ReasignacionService reasignacionService;

    @PostMapping("/procesar-cancelacion/{citaId}")
    @Operation(summary = "Activar motor de reasignación", description = "Busca al paciente más prioritario para ocupar una cita cancelada")
    public ResponseEntity<String> ejecutarReasignacion(@PathVariable Long citaId) {
        reasignacionService.procesarCancelacion(citaId);
        return ResponseEntity.ok("Proceso de reasignación ejecutado con éxito.");
    }
}