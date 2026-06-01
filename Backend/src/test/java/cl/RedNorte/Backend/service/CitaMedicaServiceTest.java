package cl.RedNorte.Backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import cl.RedNorte.Backend.events.CitaCanceladaEvent;
import cl.RedNorte.Backend.model.EstadoCita;
import cl.RedNorte.Backend.model.primary.CitaMedica;
import cl.RedNorte.Backend.model.primary.Especialidad;
import cl.RedNorte.Backend.repository.primary.CitaMedicaRepository;

class CitaMedicaServiceTest {

    @Mock
    private CitaMedicaRepository citaRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private CitaMedicaService citaMedicaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void cancelarCita_DebeCambiarEstadoYPublicarEvento() {
        // Arrange
        Long citaId = 1L;
        Especialidad especialidad = new Especialidad();
        especialidad.setId(10L);
        
        CitaMedica cita = new CitaMedica();
        cita.setId(citaId);
        cita.setEspecialidad(especialidad);
        cita.setEstado(EstadoCita.RESERVADA);

        when(citaRepository.findById(citaId)).thenReturn(Optional.of(cita));

        // Act
        citaMedicaService.cancelarCita(citaId);

        // Assert
        assertEquals(EstadoCita.CANCELADA, cita.getEstado());
        verify(citaRepository, times(1)).save(cita);
        verify(eventPublisher, times(1)).publishEvent(any(CitaCanceladaEvent.class));
    }

    @Test
    void cancelarCita_DebeLanzarExcepcion_CuandoCitaNoExiste() {
        // Arrange
        Long citaId = 99L;
        when(citaRepository.findById(citaId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            citaMedicaService.cancelarCita(citaId);
        });
        assertEquals("Cita no encontrada", exception.getMessage());
        verify(citaRepository, never()).save(any(CitaMedica.class));
        verify(eventPublisher, never()).publishEvent(any());
    }
}