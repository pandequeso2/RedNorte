package cl.RedNorte.Backend.factory;

import cl.RedNorte.Backend.model.espera.SolicitudEspera;
import cl.RedNorte.Backend.model.espera.TipoAtencion;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


class SolicitudEsperaFactoryTest {

    private final SolicitudEsperaFactory factory = new SolicitudEsperaFactory();

    @Test
    void urgenciaExtrema_debeTenerPrioridadMaxima() {
        SolicitudEspera s = factory.crear(1L, 1L, TipoAtencion.URGENCIA_EXTREMA, null);
        assertThat(s.getPrioridad()).isEqualTo(5);
    }

    @Test
    void consulta_debeTenerPrioridadMinima() {
        SolicitudEspera s = factory.crear(1L, 1L, TipoAtencion.CONSULTA_MEDICA, null);
        assertThat(s.getPrioridad()).isEqualTo(1);
    }

    @Test
    void solicitudCreada_debeTenerEstadoPendiente() {
        SolicitudEspera s = factory.crear(1L, 1L, TipoAtencion.DIAGNOSTICO, "test");
        assertThat(s.getEstado()).isEqualTo("PENDIENTE");
    }
}