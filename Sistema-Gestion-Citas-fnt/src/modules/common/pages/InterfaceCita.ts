import type { Medico } from "../../home/administrador/pages/Medico";
import type { Paciente } from "../../home/administrador/pages/Paciente";

export interface Cita { id_cita: number; fecha: string; hora: string; paciente: Paciente; medico: Medico}