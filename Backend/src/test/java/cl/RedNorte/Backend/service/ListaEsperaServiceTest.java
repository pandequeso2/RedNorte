package cl.RedNorte.Backend.service;

import cl.RedNorte.Backend.model.espera.SolicitudEspera;
import cl.RedNorte.Backend.model.espera.TipoAtencion;
import cl.RedNorte.Backend.repository.espera.SolicitudEsperaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListaEsperaServiceTest {

    @Mock
    private SolicitudEsperaRepository repository;

    @InjectMocks
    private ListaEsperaService service;

    private SolicitudEspera solicitudMock;

    @BeforeEach
    void setUp() {
        solicitudMock = new SolicitudEspera();
        solicitudMock.setId(1L);
        solicitudMock.setPacienteId(10L);
        solicitudMock.setEspecialidadId(2L);
        solicitudMock.setTipoAtencion(TipoAtencion.CONSULTA_MEDICA);
        solicitudMock.setPrioridad(1);
        solicitudMock.setEstado("PENDIENTE");
    }

    @Test
    void crearSolicitud_debeGuardarYRetornar() {
        when(repository.save(any(SolicitudEspera.class))).thenReturn(solicitudMock);
        SolicitudEspera resultado = service.crearSolicitud(solicitudMock);
        assertThat(resultado).isNotNull();
        assertThat(resultado.getEstado()).isEqualTo("PENDIENTE");
        verify(repository, times(1)).save(solicitudMock);
    }

    @Test
    void consultarEstadoPaciente_debeRetornarLista() {
        when(repository.findByPacienteId(10L)).thenReturn(List.of(solicitudMock));
        List<SolicitudEspera> resultado = service.consultarEstadoPaciente(10L);
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getPacienteId()).isEqualTo(10L);
    }

    @Test
    void obtenerPacientesCriticos_debeRetornarOrdenados() {
        when(repository.findAllByOrderByFechaIngresoAsc()).thenReturn(List.of(solicitudMock));
        List<SolicitudEspera> resultado = service.obtenerPacientesCriticos();
        assertThat(resultado).isNotEmpty();
    }
}