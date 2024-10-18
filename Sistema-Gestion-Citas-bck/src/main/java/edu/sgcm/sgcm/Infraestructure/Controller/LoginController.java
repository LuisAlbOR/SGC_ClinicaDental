package edu.sgcm.sgcm.Infraestructure.Controller;

import edu.sgcm.sgcm.Infraestructure.domain.Administrador.Administrador;
import edu.sgcm.sgcm.Infraestructure.domain.Administrador.AdministradorRepository;
import edu.sgcm.sgcm.Infraestructure.domain.Login.DatosRespuestaLogin;
import edu.sgcm.sgcm.Infraestructure.domain.Login.DatosVerificacionLogin;
import edu.sgcm.sgcm.Infraestructure.domain.Medico.Medico;
import edu.sgcm.sgcm.Infraestructure.domain.Medico.MedicoRepository;
import edu.sgcm.sgcm.Infraestructure.domain.Paciente.Paciente;
import edu.sgcm.sgcm.Infraestructure.domain.Paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/Login")
@CrossOrigin(origins = "*",methods = {RequestMethod.GET, RequestMethod.POST})
public class LoginController {

    @Autowired
    private AdministradorRepository administradorRepository;
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private MedicoRepository medicoRepository;

    @PostMapping(path = "/Verificar")
    public ResponseEntity<?> Login(@RequestBody DatosVerificacionLogin datosVerificacionLogin){
        //Primero verificamos si las credenciales son de un admin
        Optional<Administrador> adminOpt = administradorRepository.findByCorreoAdministrador(datosVerificacionLogin.correo());

        if(adminOpt.isPresent()){
            Administrador admin = adminOpt.get();
            if(admin.getPasswordAdministrador().equals(datosVerificacionLogin.password())){
                return ResponseEntity.ok(new DatosRespuestaLogin(admin.getId_administrador(),admin.getNombreAdministrador(),admin.getApellidoAdministrador(),"Administrador"));
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña invalida para el administrador");
            }
        }

        //Si no es admin, verificamos si es un médico
        Optional<Medico> medicoOpt = medicoRepository.findByCorreoMedico(datosVerificacionLogin.correo());

        if(medicoOpt.isPresent()){
            Medico medico = medicoOpt.get();
            if(medico.getPasswordMedico().equals(datosVerificacionLogin.password())){
                return ResponseEntity.ok(new DatosRespuestaLogin(medico.getId_medico(), medico.getNombreMedico(), medico.getApellidoMedico(), "Medico"));
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña invalida para el medico");
            }
        }

        //Y si no es medico o admin, verificamos si es un paciente
        Optional<Paciente> pacienteOpt = pacienteRepository.findByCorreoPaciente(datosVerificacionLogin.correo());

        if(pacienteOpt.isPresent()){
            Paciente paciente = pacienteOpt.get();
            if(paciente.getPasswordPaciente().equals(datosVerificacionLogin.password())){
                return ResponseEntity.ok(new DatosRespuestaLogin(paciente.getId_paciente(), paciente.getNombrePaciente(), paciente.getApellidoPaciente(), "Paciente"));
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña invalida para el paciente");
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Correo invalido1");
    }
}

