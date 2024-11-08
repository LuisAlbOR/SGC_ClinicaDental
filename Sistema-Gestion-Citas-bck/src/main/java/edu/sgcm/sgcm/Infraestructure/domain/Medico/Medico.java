package edu.sgcm.sgcm.Infraestructure.domain.Medico;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity(name = "Medico")
@Table(name = "medico")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "Id")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_medico;
    private String nombreMedico;
    private String apellidoMedico;
    private String especialidadMedico;
    private String telefonoMedico;
    @Column(unique = true)
    private String correoMedico;
    private String passwordMedico;

    public Medico(DatosRegistroMedico datosRegistroMedico){
        this.nombreMedico = datosRegistroMedico.nombre();
        this.apellidoMedico = datosRegistroMedico.apellido();
        this.especialidadMedico = datosRegistroMedico.especialidad();
        this.telefonoMedico = datosRegistroMedico.telefono();
        this.correoMedico = datosRegistroMedico.correo();
        this.passwordMedico = datosRegistroMedico.password();
    }

    public void ActualizarMedico(DatosActualizarMedico datosActualizarMedico){
        this.nombreMedico = datosActualizarMedico.nombre();
        this.apellidoMedico = datosActualizarMedico.apellido();
        this.telefonoMedico = datosActualizarMedico.telefono();
        this.correoMedico = datosActualizarMedico.correo();
    }
}
