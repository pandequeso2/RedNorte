package cl.RedNorte.Reasignacion.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.RedNorte.Reasignacion.service.ReasignacionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reasignaciones")
@RequiredArgsConstructor
public class ReasignacionController {

    private final ReasignacionService reasignacionService;

    // Fíjate que ahora recibimos los datos listos por parámetros, no los buscamos en la base de datos de citas
    @PostMapping("/procesar-cancelacion")
    public ResponseEntity<String> procesarCancelacion(
            @RequestParam Long citaCanceladaId,
            @RequestParam Long solicitudEsperaId,
            @RequestParam Long pacienteId) {

        reasignacionService.registrarReasignacion(citaCanceladaId, solicitudEsperaId, pacienteId);
        
        return ResponseEntity.ok("Reasignación procesada exitosamente para el paciente " + pacienteId);
    }
}