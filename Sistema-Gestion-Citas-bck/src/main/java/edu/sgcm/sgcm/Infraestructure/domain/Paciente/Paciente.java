package edu.sgcm.sgcm.Infraestructure.domain.Paciente;

import edu.sgcm.sgcm.Infraestructure.domain.Administrador.DatosActualizarAdministrador;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity(name = "Paciente")
@Table(name = "paciente")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@EqualsAndHashCode(of = "Id")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_paciente;
    private String nombrePaciente;
    private String apellidoPaciente;
    private Date   fechaNacimiento;
    private String generoPaciente;
    @Column(unique = true)
    private String correoPaciente;
    private String telefonoPaciente;
    private String direccionPaciente;
    private String passwordPaciente;

    public Paciente(DatosRegistroPaciente datosRegistroPaciente){
        this.id_paciente = datosRegistroPaciente.id();
        this.nombrePaciente = datosRegistroPaciente.nombre();
        this.apellidoPaciente = datosRegistroPaciente.apellido();
        this.fechaNacimiento = datosRegistroPaciente.fechaNacimiento();
        this.generoPaciente = datosRegistroPaciente.genero();
        this.correoPaciente = datosRegistroPaciente.correo();
        this.telefonoPaciente = datosRegistroPaciente.telefono();
        this.direccionPaciente = datosRegistroPaciente.direccion();
        this.passwordPaciente = datosRegistroPaciente.password();
    }

    public void ActualizarDatos(DatosActualizarPaciente  datosActualizarPaciente){
        this.nombrePaciente = datosActualizarPaciente.nombre();
        this.apellidoPaciente = datosActualizarPaciente.apellido();
        this.fechaNacimiento = datosActualizarPaciente.fechaNacimiento();
        this.correoPaciente = datosActualizarPaciente.correo();
        this.telefonoPaciente = datosActualizarPaciente.telefono();
        this.direccionPaciente = datosActualizarPaciente.direccion();
    }
}
