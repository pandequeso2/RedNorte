package cl.RedNorte.Backend.controller;

import cl.RedNorte.Backend.model.espera.SolicitudEspera;
import cl.RedNorte.Backend.service.ListaEsperaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listaEspera")
@RequiredArgsConstructor
@Tag(name = "Lista de Espera", description = "Gestión de registros y priorización de pacientes")
public class ListaEsperaController {

    private final ListaEsperaService listaEsperaService;

    @PostMapping("/registrar")
    @Operation(summary = "Registrar un nuevo paciente en la lista de espera")
    public ResponseEntity<SolicitudEspera> registrarSolicitud(@RequestBody SolicitudEspera solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(listaEsperaService.crearSolicitud(solicitud));
    }

    @GetMapping("/criticos")
    @Operation(summary = "Obtener pacientes con mayor tiempo de espera", description = "Resuelve el problema de pacientes estancados")
    public ResponseEntity<List<SolicitudEspera>> listarPacientesCriticos() {
        return ResponseEntity.ok(listaEsperaService.obtenerPacientesCriticos());
    }

    @GetMapping("/paciente/{id}")
    @Operation(summary = "Consultar estado para el Portal del Paciente")
    public ResponseEntity<List<SolicitudEspera>> verMiEstado(@PathVariable Long id) {
        return ResponseEntity.ok(listaEsperaService.consultarEstadoPaciente(id));
    }
}