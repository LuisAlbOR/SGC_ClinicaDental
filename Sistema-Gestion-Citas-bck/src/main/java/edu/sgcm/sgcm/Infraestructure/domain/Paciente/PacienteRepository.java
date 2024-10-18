package edu.sgcm.sgcm.Infraestructure.domain.Paciente;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
    Optional<Paciente> findByCorreoPaciente(String correo);
}
