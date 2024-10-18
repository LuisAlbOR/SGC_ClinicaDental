package edu.sgcm.sgcm.Infraestructure.Controller;

import edu.sgcm.sgcm.Infraestructure.domain.Administrador.Administrador;
import edu.sgcm.sgcm.Infraestructure.domain.Paciente.DatosActualizarPaciente;
import edu.sgcm.sgcm.Infraestructure.domain.Paciente.DatosRegistroPaciente;
import edu.sgcm.sgcm.Infraestructure.domain.Paciente.Paciente;
import edu.sgcm.sgcm.Infraestructure.domain.Paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping(path = "/RegistrarPaciente")
    public ResponseEntity RegistrarPaciente(@RequestBody DatosRegistroPaciente datosRegistroPaciente){
        try {
            pacienteRepository.save(new Paciente(datosRegistroPaciente));
            return ResponseEntity.ok().build();
        }catch (Exception e){
            throw new RuntimeException(e);
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
