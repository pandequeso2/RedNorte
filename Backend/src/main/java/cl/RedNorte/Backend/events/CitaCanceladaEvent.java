package cl.RedNorte.Backend.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CitaCanceladaEvent {
    private final Long citaId;
    private final Long especialidadId;
}