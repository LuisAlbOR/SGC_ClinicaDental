package edu.sgcm.sgcm.Infraestructure.Controller;

import edu.sgcm.sgcm.Infraestructure.domain.Cita.Cita;
import edu.sgcm.sgcm.Infraestructure.domain.Cita.CitaRepository;
import edu.sgcm.sgcm.Infraestructure.domain.Cita.DatosActualizarCita;
import edu.sgcm.sgcm.Infraestructure.domain.Cita.DatosRegistroCita;
import edu.sgcm.sgcm.Infraestructure.domain.Medico.Medico;
import edu.sgcm.sgcm.Infraestructure.domain.Medico.MedicoRepository;
import edu.sgcm.sgcm.Infraestructure.domain.Paciente.Paciente;
import edu.sgcm.sgcm.Infraestructure.domain.Paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/Cita")
@CrossOrigin(origins = "*", methods = {RequestMethod.PUT,RequestMethod.GET,RequestMethod.DELETE,RequestMethod.POST})
public class CitaController {

    @Autowired
    private CitaRepository citaRepository;
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private MedicoRepository medicoRepository;;

    @PostMapping("/RegistrarCita")
    public ResponseEntity<?> registrarCita(@RequestBody DatosRegistroCita datosRegistroCita) {
        Optional<Paciente> pacienteOpt = pacienteRepository.findById(datosRegistroCita.id_paciente());
        Optional<Medico> medicoOpt = medicoRepository.findById(datosRegistroCita.id_medico());

        if (pacienteOpt.isPresent() && medicoOpt.isPresent()) {
            Cita nuevaCita = new Cita();
            nuevaCita.setFecha(datosRegistroCita.fecha());
            nuevaCita.setHora(datosRegistroCita.hora());
            nuevaCita.setId_paciente(pacienteOpt.get());
            nuevaCita.setId_medico(medicoOpt.get());

            citaRepository.save(nuevaCita);
            return ResponseEntity.ok("Cita registrada con éxito");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Paciente o Médico no encontrado");
        }
    }


    @GetMapping(path = "/ListadoCita")
    public List<Cita> ListarCita(){
        try {
            return citaRepository.findAll();
        }catch(Exception e){
            return (List<Cita>) ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/ListadoCita/{id}")
    public ResponseEntity<Cita> ListadoMedico(@PathVariable Integer id){
        try {
            return ResponseEntity.ok().body(citaRepository.findById(id).get());
        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(path = "/ActualizarCita/{id}")
    public ResponseEntity ActualizarCita(@RequestBody DatosActualizarCita datosACtualizarCita, @PathVariable Integer id){
        try {
            Cita cita = citaRepository.findById(id).get();
            cita.ActualizarCita(datosACtualizarCita);
            citaRepository.save(cita);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/EliminarCita/{id}")
    public ResponseEntity EliminarCita(@PathVariable Integer id){
        try {
            Cita cita = citaRepository.findById(id).get();
            citaRepository.delete(cita);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }
}