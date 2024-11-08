package edu.sgcm.sgcm.Infraestructure.domain.Cita;

import edu.sgcm.sgcm.Infraestructure.domain.Medico.Medico;
import edu.sgcm.sgcm.Infraestructure.domain.Paciente.Paciente;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Time;

@Entity(name = "Cita")
@Table(name = "cita")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "Id")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_cita;
    private Date fecha;
    private Time hora;
    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;
    @ManyToOne
    @JoinColumn(name = "id_medico", nullable = false)
    private Medico medico;


    public void ActualizarCita(DatosActualizarCita datosActualizarCita){
        this.fecha = datosActualizarCita.fecha();
        this.hora = datosActualizarCita.hora();
        this.medico = datosActualizarCita.id_medico();
    }
}
