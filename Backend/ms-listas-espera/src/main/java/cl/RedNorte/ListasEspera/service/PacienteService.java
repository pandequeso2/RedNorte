package cl.RedNorte.ListasEspera.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.RedNorte.ListasEspera.model.Paciente;
import cl.RedNorte.ListasEspera.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    // Listar todos los pacientes
    public List<Paciente> obtenerTodos() {
        return pacienteRepository.findAll();
    }

    // Buscar paciente por ID
    public Paciente obtenerPorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + id));
    }

    // Buscar paciente por RUT (Aprovechando el método Optional que creaste en el Repository)
    public Paciente obtenerPorRut(String rut) {
        return pacienteRepository.findByRut(rut)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con RUT: " + rut));
    }

    // Registrar un nuevo paciente
    public Paciente registrarPaciente(Paciente paciente) {
        // Aquí a futuro podrías agregar validaciones, como verificar si el email ya existe
        return pacienteRepository.save(paciente);
    }
    
    // Opcional: Actualizar datos de contacto del paciente
    public Paciente actualizarContacto(Long id, String nuevoEmail, String nuevoFono) {
        Paciente paciente = obtenerPorId(id);
        paciente.setEmail(nuevoEmail);
        paciente.setFono(nuevoFono);
        return pacienteRepository.save(paciente);
    }
}