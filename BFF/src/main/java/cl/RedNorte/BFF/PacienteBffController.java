package cl.RedNorte.BFF.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cl.RedNorte.BFF.service.PacienteBffService;
import java.util.List;

@RestController
@RequestMapping("/api/bff/pacientes")
public class PacienteBffController {

    private final PacienteBffService bffService;

    public PacienteBffController(PacienteBffService bffService) {
        this.bffService = bffService;
    }

    @GetMapping
    public List<Object> obtenerPacientesParaVista() {
        return bffService.obtenerPacientes();
    }
}