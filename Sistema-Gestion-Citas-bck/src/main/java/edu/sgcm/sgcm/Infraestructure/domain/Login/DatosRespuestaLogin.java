package edu.sgcm.sgcm.Infraestructure.domain.Login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DatosRespuestaLogin{

    private Integer id;
    private String nombre;
    private String apellido;
    private String ocupacion;
}
