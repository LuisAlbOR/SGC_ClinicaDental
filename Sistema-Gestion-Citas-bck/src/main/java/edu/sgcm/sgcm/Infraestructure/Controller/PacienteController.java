package edu.sgcm.sgcm.Infraestructure.Controller;

import edu.sgcm.sgcm.Infraestructure.domain.Administrador.Administrador;
import edu.sgcm.sgcm.Infraestructure.domain.Administrador.AdministradorRepository;
import edu.sgcm.sgcm.Infraestructure.domain.Medico.MedicoRepository;
import edu.sgcm.sgcm.Infraestructure.domain.Paciente.DatosActualizarPaciente;
import edu.sgcm.sgcm.Infraestructure.domain.Paciente.DatosRegistroPaciente;
import edu.sgcm.sgcm.Infraestructure.domain.Paciente.Paciente;
import edu.sgcm.sgcm.Infraestructure.domain.Paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Paciente")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class PacienteController
{
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private AdministradorRepository administradorRepository;
    @Autowired
    private MedicoRepository medicoRepository;

    @PostMapping(path = "/RegistrarPaciente")
    public ResponseEntity<String> RegistrarPaciente(@RequestBody DatosRegistroPaciente datosRegistroPaciente) {
        // Verificar si el correo ya está registrado en Medico o Administrador
        boolean existeComoMedico = medicoRepository.findByCorreoMedico(datosRegistroPaciente.correo()).isPresent();
        boolean existeComoAdministrador = administradorRepository.findByCorreoAdministrador(datosRegistroPaciente.correo()).isPresent();

        if (existeComoMedico || existeComoAdministrador) {
            // Si el correo ya está registrado como Médico o Administrador, devolver un error
            String mensaje = "No se puede registrar el correo porque ya está registrado " + ".";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(mensaje);
        }

        // Si no está registrado, proceder con la creación del paciente
        try {
            pacienteRepository.save(new Paciente(datosRegistroPaciente));
            return ResponseEntity.ok("Paciente registrado exitosamente.");
        } catch (Exception e) {
            // Manejar cualquier error inesperado durante la creación del paciente
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Correo usado actualmente");
        }
    }

    @GetMapping(path = "/ListadoPaciente")
    public List<Paciente> ListadoPaciente(){
        try {
            return pacienteRepository.findAll();
        }catch (Exception e){
            return (List<Paciente>) ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/ListadoPaciente/{id}")
    public ResponseEntity<Paciente> ListadoPaciente(@PathVariable Integer id){
        try {
            return ResponseEntity.ok().body(pacienteRepository.findById(id).get());
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(path = "/ActualizarPaciente/{id}")
    public ResponseEntity ActualizarPaciente (@RequestBody DatosActualizarPaciente datosActualizarPaciente, @PathVariable Integer id){
        try {
            Paciente paciente = pacienteRepository.findById(id).get();
            paciente.ActualizarDatos(datosActualizarPaciente);

            pacienteRepository.save(paciente);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/EliminarPaciente/{id}")
    public ResponseEntity EliminarPaciente(@PathVariable Integer id){
        try{
            Paciente paciente = pacienteRepository.findById(id).get();
            pacienteRepository.delete(paciente);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }
}
