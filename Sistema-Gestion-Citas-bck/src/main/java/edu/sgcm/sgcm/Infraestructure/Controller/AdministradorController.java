package edu.sgcm.sgcm.Infraestructure.Controller;

import edu.sgcm.sgcm.Infraestructure.domain.Administrador.Administrador;
import edu.sgcm.sgcm.Infraestructure.domain.Administrador.AdministradorRepository;
import edu.sgcm.sgcm.Infraestructure.domain.Administrador.DatosActualizarAdministrador;
import edu.sgcm.sgcm.Infraestructure.domain.Administrador.DatosRegistroAdministrador;
import edu.sgcm.sgcm.Infraestructure.domain.Medico.MedicoRepository;
import edu.sgcm.sgcm.Infraestructure.domain.Paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Administrador")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class AdministradorController {

    @Autowired
    private AdministradorRepository administradorRepository;
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private MedicoRepository medicoRepository;


    @PostMapping(path = "/RegistrarAdministrador")
    public ResponseEntity<String> RegistrarAdministrador(@RequestBody DatosRegistroAdministrador datosRegistroAdministrador)    {
        // Verificar si el correo ya está registrado en Paciente o Medico
        boolean existeComoPaciente = pacienteRepository.findByCorreoPaciente(datosRegistroAdministrador.correo()).isPresent();
        boolean existeComoMedico = medicoRepository.findByCorreoMedico(datosRegistroAdministrador.correo()).isPresent();

        if (existeComoPaciente || existeComoMedico) {
            // Si el correo ya está registrado como Paciente o Médico, devolver un error
            String mensaje = "No se puede registrar el correo porque ya está registrado como "
                    + (existeComoPaciente ? "paciente" : "médico") + ".";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(mensaje);
        }

        // Si no está registrado, proceder con la creación del administrador
        try {
            administradorRepository.save(new Administrador(datosRegistroAdministrador));
            return ResponseEntity.ok("Administrador registrado exitosamente.");
        } catch (Exception e) {
            // Manejar cualquier error inesperado durante la creación del administrador
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Correo usado actualmente");
        }
    }

    @GetMapping(path = "/ListarAdministrador")
    public List<Administrador> ListarAdministrador(){
        try {
            return administradorRepository.findAll();
        }catch (Exception e){
            return (List<Administrador>) ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/ListadoAdministrador/{id}")
    public ResponseEntity<Administrador> ListadoAdministrador(@PathVariable Integer id){
        try {
            return ResponseEntity.ok().body(administradorRepository.findById(id).get());
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(path = "/ActualizarAdministrador/{id}")
    public ResponseEntity ActualizarAdministrador(@RequestBody DatosActualizarAdministrador datosActualizarAdministrador, @PathVariable Integer id){
        try {
            Administrador administrador = administradorRepository.findById(id).get();
            administrador.ActualizaDatos(datosActualizarAdministrador);

            administradorRepository.save(administrador);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping(path = "/EliminarAdministrador/{id}")
    public ResponseEntity EliminarAdministrador(@PathVariable Integer id){
       try {
            Administrador administrador = administradorRepository.findById(id).get();
            administradorRepository.delete(administrador);
            return ResponseEntity.ok().build();
       }catch (Exception e){
           return ResponseEntity.notFound().build();
       }
    }
}
