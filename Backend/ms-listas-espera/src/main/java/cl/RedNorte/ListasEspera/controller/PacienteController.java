package cl.RedNorte.ListasEspera.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.RedNorte.ListasEspera.model.Paciente;
import cl.RedNorte.ListasEspera.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Pacientes", description = "Gestión de información personal y de contacto de los pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    @GetMapping
    @Operation(summary = "Obtener lista completa de pacientes")
    public ResponseEntity<List<Paciente>> listarTodos() {
        return ResponseEntity.ok(pacienteService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar paciente por ID")
    public ResponseEntity<Paciente> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.obtenerPorId(id));
    }

    @GetMapping("/rut/{rut}")
    @Operation(summary = "Buscar paciente por RUT")
    public ResponseEntity<Paciente> obtenerPorRut(@PathVariable String rut) {
        return ResponseEntity.ok(pacienteService.obtenerPorRut(rut));
    }

    @PostMapping("/registrar")
    @Operation(summary = "Registrar un nuevo paciente en el sistema")
    public ResponseEntity<Paciente> registrarPaciente(@RequestBody Paciente paciente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.registrarPaciente(paciente));
    }

    @PutMapping("/{id}/contacto")
    @Operation(summary = "Actualizar correo y teléfono de un paciente")
    public ResponseEntity<Paciente> actualizarContacto(
            @PathVariable Long id,
            @RequestBody ActualizarContactoRequest request) {
        return ResponseEntity.ok(pacienteService.actualizarContacto(id, request.getEmail(), request.getFono()));
    }
}