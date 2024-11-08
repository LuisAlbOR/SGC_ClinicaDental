package edu.sgcm.sgcm.Infraestructure.Controller;

import edu.sgcm.sgcm.Infraestructure.domain.Administrador.AdministradorRepository;
import edu.sgcm.sgcm.Infraestructure.domain.Medico.DatosActualizarMedico;
import edu.sgcm.sgcm.Infraestructure.domain.Medico.DatosRegistroMedico;
import edu.sgcm.sgcm.Infraestructure.domain.Medico.Medico;
import edu.sgcm.sgcm.Infraestructure.domain.Medico.MedicoRepository;
import edu.sgcm.sgcm.Infraestructure.domain.Paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/Medico")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE} )
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private AdministradorRepository administradorRepository;

    @PostMapping(path = "/RegistroMedico")
    public ResponseEntity<String> RegistrarMedico(@RequestBody DatosRegistroMedico datosRegistroMedico) {
        // Verificar si el correo ya está registrado en Paciente o Administrador
        boolean existeComoPaciente = pacienteRepository.findByCorreoPaciente(datosRegistroMedico.correo()).isPresent();
        boolean existeComoAdministrador = administradorRepository.findByCorreoAdministrador(datosRegistroMedico.correo()).isPresent();

        if (existeComoPaciente || existeComoAdministrador) {
            // Si el correo ya está registrado como Paciente o Administrador, devolver un error
            String mensaje = "No se puede registrar el correo porque ya está registrado como "
                    + (existeComoPaciente ? "paciente" : "administrador") + ".";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(mensaje);
        }

        // Si no está registrado, proceder con la creación del médico
        try {
            medicoRepository.save(new Medico(datosRegistroMedico));
            return ResponseEntity.ok("Médico registrado exitosamente.");
        } catch (Exception e) {
            // Manejar cualquier error inesperado durante la creación del médico
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Correo usado actualmente");
        }
    }

    @GetMapping(path = "/ListadoMedico")
    public List<Medico> ListarMedico(){
        try {
            return medicoRepository.findAll();
        }catch (Exception e){
            return (List<Medico>) ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/ListadoMedico/Especialidad/{especialidadNombre}")
    public ResponseEntity<List<Medico>> listarMedicosPorEspecialidad(@PathVariable String especialidadNombre) {
        try {
            List<Medico> medicos = medicoRepository.findByEspecialidadMedico(especialidadNombre);
            if (medicos.isEmpty()) {
                return ResponseEntity.noContent().build(); // Devuelve 204 si no hay médicos para esa especialidad
            }
            return ResponseEntity.ok().body(medicos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(path = "/ListadoMedico/{id}")
    public ResponseEntity<Medico> ListadoAdministrado(@PathVariable Integer id){
        try {
            return ResponseEntity.ok().body(medicoRepository.findById(id).get());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(path = "/ActualizarMedico/{id}")
    public ResponseEntity ActualizarMedico(@RequestBody DatosActualizarMedico datosActualizarMedico, @PathVariable Integer id){
        try {
            Medico medico = medicoRepository.findById(id).get();
            medico.ActualizarMedico(datosActualizarMedico);

            medicoRepository.save(medico);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/EliminarMedico/{id}")
    public ResponseEntity EliminarMedico(@PathVariable Integer id){
        try {
            Medico medico = medicoRepository.findById(id).get();
            medicoRepository.delete(medico);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}