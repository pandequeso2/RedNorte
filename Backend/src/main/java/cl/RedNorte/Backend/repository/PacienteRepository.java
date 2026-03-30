package cl.RedNorte.Backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.RedNorte.Backend.model.primary.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>{
    //Opcionales
    Optional<Paciente> findByRut(String rut);
    Optional<Paciente> findById(Long id);

}
