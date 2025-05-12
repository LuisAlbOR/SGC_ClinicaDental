// Paciente.ts
export interface Paciente {
    id_paciente: number;
    nombrePaciente: string;
    apellidoPaciente: string;
    fechaNacimiento: string;
    generoPaciente: string;
    correoPaciente: string;
    telefonoPaciente: string;
    direccionPaciente: string;
    passwordPaciente: string; // Si necesitas usar este campo, de lo contrario, puedes omitirlo
}
