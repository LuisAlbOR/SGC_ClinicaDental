package edu.sgcm.sgcm.Infraestructure.Controller;

import edu.sgcm.sgcm.Infraestructure.domain.Medico.DatosActualizarMedico;
import edu.sgcm.sgcm.Infraestructure.domain.Medico.DatosRegistroMedico;
import edu.sgcm.sgcm.Infraestructure.domain.Medico.Medico;
import edu.sgcm.sgcm.Infraestructure.domain.Medico.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/Medico")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE} )
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    @PostMapping(path = "/RegistroMedico")
    public ResponseEntity RegistrarMedico(@RequestBody DatosRegistroMedico datosRegistroMedico){
        try {
            medicoRepository.save(new Medico(datosRegistroMedico));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
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