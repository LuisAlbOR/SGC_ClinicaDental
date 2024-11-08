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
            if (citaRepository.findByFechaAndHoraAndPacienteAndMedico(
                    datosRegistroCita.fecha(), datosRegistroCita.hora(), pacienteOpt.get(), medicoOpt.get()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Cita duplicada: ya existe una cita con estos datos.");
            }
            // Obtén todas las citas para el mismo médico en la misma fecha
            List<Cita> citasEnFecha = citaRepository.findByMedicoAndFecha(medicoOpt.get(), datosRegistroCita.fecha());
            // Compara la diferencia horaria
            for (Cita cita : citasEnFecha) {
                long differenceInMillis = Math.abs(cita.getHora().getTime() - datosRegistroCita.hora().getTime());
                long differenceInMinutes = differenceInMillis / (60 * 1000);

                if (differenceInMinutes < 60) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya existe una cita en el mismo día con menos de una hora de diferencia.");
                }
            }

            Cita nuevaCita = new Cita();
            nuevaCita.setFecha(datosRegistroCita.fecha());
            nuevaCita.setHora(datosRegistroCita.hora());
            nuevaCita.setPaciente(pacienteOpt.get());
            nuevaCita.setMedico(medicoOpt.get());

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

    @GetMapping(path = "/ObtenerCitasPorPaciente/{id_paciente}")
    public ResponseEntity<List<Cita>> ObtenerCitasPorPaciente(@PathVariable Integer id_paciente) {

        try {
            List<Cita> citas = citaRepository.findByPacienteId(id_paciente);
            if (citas.isEmpty()) {
                return ResponseEntity.noContent().build(); // Si no hay citas, retorna 204 No Content
            }
            return ResponseEntity.ok(citas); // Retorna 200 OK con la lista de citas
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Error interno del servidor
        }
    }

    @GetMapping(path = "/ObtenerCitasPorMedico/{id_medico}")
    public ResponseEntity<List<Cita>> ObtenerCitasPorMedico(@PathVariable Integer id_medico) {
        try {
            List<Cita> citas = citaRepository.findByMedicoId(id_medico);
            if (citas.isEmpty()) {
                return ResponseEntity.noContent().build(); // Si no hay citas, retorna 204 No Content
            }
            return ResponseEntity.ok(citas); // Retorna 200 OK con la lista de citas
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Error interno del servidor
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