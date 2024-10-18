package edu.sgcm.sgcm.Infraestructure.Controller;

import edu.sgcm.sgcm.Infraestructure.domain.Administrador.Administrador;
import edu.sgcm.sgcm.Infraestructure.domain.Administrador.AdministradorRepository;
import edu.sgcm.sgcm.Infraestructure.domain.Administrador.DatosActualizarAdministrador;
import edu.sgcm.sgcm.Infraestructure.domain.Administrador.DatosRegistroAdministrador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Administrador")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class AdministradorController {

    @Autowired
    private AdministradorRepository administradorRepository;

    @PostMapping(path = "/RegistrarAdministrador")
    public ResponseEntity RegistrarAdministrador(@RequestBody DatosRegistroAdministrador datosRegistroAdministrador){
        try {
            administradorRepository.save(new Administrador(datosRegistroAdministrador));
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
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
