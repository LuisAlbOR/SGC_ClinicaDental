import type { Medico } from "./Medico";
import type { Paciente } from "./Paciente";

export interface Cita { id_cita: number; fecha: string; hora: string; paciente: Paciente; medico: Medico}