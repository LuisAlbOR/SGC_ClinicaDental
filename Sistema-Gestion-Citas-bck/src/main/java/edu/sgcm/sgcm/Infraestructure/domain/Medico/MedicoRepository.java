package edu.sgcm.sgcm.Infraestructure.domain.Medico;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository <Medico, Integer>{
    Optional<Medico> findByCorreoMedico(String correo);
    List<Medico> findByEspecialidadMedico(String especialidadMedico);

}
