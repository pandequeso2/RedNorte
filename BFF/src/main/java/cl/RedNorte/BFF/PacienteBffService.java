package cl.RedNorte.BFF.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.ArrayList;

@Service
public class PacienteBffService {

    private final RestTemplate restTemplate;

    public PacienteBffService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Si el ms-listas-espera (8081) se cae, el Circuit Breaker ejecuta el fallback
    @CircuitBreaker(name = "listaEsperaService", fallbackMethod = "fallbackObtenerPacientes")
    public List<Object> obtenerPacientes() {
        String url = "http://localhost:8081/api/pacientes";
        return restTemplate.getForObject(url, List.class);
    }

    // Método de contingencia
    public List<Object> fallbackObtenerPacientes(Exception e) {
        System.out.println("Microservicio 8081 caído. Devolviendo lista vacía por defecto.");
        return new ArrayList<>(); // Evita que el frontend explote
    }
}