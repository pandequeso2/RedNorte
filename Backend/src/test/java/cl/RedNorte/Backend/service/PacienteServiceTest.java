package cl.RedNorte.Backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cl.RedNorte.Backend.model.primary.Paciente;
import cl.RedNorte.Backend.repository.primary.PacienteRepository;

class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteService pacienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void obtenerTodos_DebeRetornarListaDePacientes() {
        // Arrange
        Paciente p1 = new Paciente(); p1.setId(1L); p1.setRut("11111111-1");
        Paciente p2 = new Paciente(); p2.setId(2L); p2.setRut("22222222-2");
        when(pacienteRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        // Act
        List<Paciente> pacientes = pacienteService.obtenerTodos();

        // Assert
        assertEquals(2, pacientes.size());
        verify(pacienteRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_DebeRetornarPaciente_CuandoExiste() {
        // Arrange
        Long id = 1L;
        Paciente paciente = new Paciente();
        paciente.setId(id);
        when(pacienteRepository.findById(id)).thenReturn(Optional.of(paciente));

        // Act
        Paciente resultado = pacienteService.obtenerPorId(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
    }

    @Test
    void obtenerPorId_DebeLanzarExcepcion_CuandoNoExiste() {
        // Arrange
        Long id = 99L;
        when(pacienteRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            pacienteService.obtenerPorId(id);
        });
        assertTrue(exception.getMessage().contains("Paciente no encontrado"));
    }

    @Test
    void registrarPaciente_DebeGuardarYRetornarPaciente() {
        // Arrange
        Paciente paciente = new Paciente();
        paciente.setRut("12345678-9");
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

        // Act
        Paciente guardado = pacienteService.registrarPaciente(paciente);

        // Assert
        assertNotNull(guardado);
        assertEquals("12345678-9", guardado.getRut());
        verify(pacienteRepository, times(1)).save(paciente);
    }
}