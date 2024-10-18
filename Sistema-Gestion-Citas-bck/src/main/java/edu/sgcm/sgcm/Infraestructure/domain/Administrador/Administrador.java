package edu.sgcm.sgcm.Infraestructure.domain.Administrador;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Administrador")
@Table(name = "administrador")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@EqualsAndHashCode(of = "Id")
public class Administrador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_administrador;
    private String nombreAdministrador;
    private String apellidoAdministrador;
    @Column(unique = true)
    private String correoAdministrador;
    private String passwordAdministrador;

    public Administrador(DatosRegistroAdministrador datosRegistroAdministrador){
        this.id_administrador = datosRegistroAdministrador.id();
        this.nombreAdministrador = datosRegistroAdministrador.nombre();
        this.apellidoAdministrador = datosRegistroAdministrador.apellido();
        this.correoAdministrador = datosRegistroAdministrador.correo();
        this.passwordAdministrador = datosRegistroAdministrador.password();
    }

    public void ActualizaDatos(DatosActualizarAdministrador datosRegistroAdministrador){
        this.nombreAdministrador = datosRegistroAdministrador.nombre();
        this.apellidoAdministrador = datosRegistroAdministrador.apellido();
        this.correoAdministrador = datosRegistroAdministrador.correo();
    }

}
