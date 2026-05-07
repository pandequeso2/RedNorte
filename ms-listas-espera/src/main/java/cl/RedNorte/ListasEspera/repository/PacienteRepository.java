package cl.RedNorte.ListasEspera.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.RedNorte.ListasEspera.model.primary.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>{
    //Opcionales
    Optional<Paciente> findByRut(String rut);
    Optional<Paciente> findById(Long id);

}
