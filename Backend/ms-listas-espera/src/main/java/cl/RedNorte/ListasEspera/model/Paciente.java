package cl.RedNorte.ListasEspera.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name="Pacientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String rut;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String email;
    @Column
    private LocalDate fechaNac;
    @Column(nullable = true)
    private String fono;
    @Column(nullable = false)
    private char genero;
    


}
