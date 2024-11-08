<template>
  <div v-if="isVisible" class="fixed inset-0 bg-gray-800 bg-opacity-50 flex justify-center items-center z-50">
    <div class="bg-white rounded-lg shadow-lg w-11/12 md:w-1/3 p-6">
      <h2 class="text-xl font-semibold text-black mb-4 text-center">Detalles de la cita</h2>

      <ul class="text-gray-700 space-y-2">
        <li><strong>ID:</strong> {{ props.cita.id_cita }}</li>
        <li><strong>Fecha:</strong> {{ props.cita.fecha }}</li>
        <li><strong>Hora:</strong> {{ props.cita.hora }}</li>
        <li><strong>Paciente:</strong> {{ props.cita.paciente.nombrePaciente + ' ' + props.cita.paciente.apellidoPaciente  }}</li>
        <li><strong>Correo del paciente:</strong> {{ props.cita.paciente.correoPaciente }}</li>
        <li><strong>Télefono del paciente:</strong> {{ props.cita.paciente.telefonoPaciente }}</li>
        <li><strong>Género del paciente:</strong> {{ props.cita.paciente.generoPaciente }}</li>
        <li><strong>Fecha de nacimiento del paciente:</strong> {{ props.cita.paciente.fechaNacimiento }}</li>
        <li><strong>Dirección del paciente:</strong> {{ props.cita.paciente.direccionPaciente }}</li>
        <li><strong>Médico:</strong> {{ props.cita.medico.nombreMedico + ' ' + props.cita.medico.apellidoMedico }}</li>
        <li><strong>Especialidad del médico:</strong> {{ props.cita.medico.especialidadMedico }}</li>
      </ul>

      <div class="mt-6 flex justify-center">
        <button @click="closeModal"
          class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 focus:outline-none focus:ring-2 focus:ring-green-400">
          Aceptar
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import Loading from 'vue-loading-overlay'
import 'vue-loading-overlay/dist/css/index.css';
import type { Cita } from '@/modules/home/administrador/pages/Cita';
import { ref, defineProps, defineEmits } from 'vue';

// Props: Datos del paciente pasados al modal
const props = defineProps<{
  cita: Cita;
  isVisible: boolean;
}>();

// Emisor de eventos para cerrar el modal
const emit = defineEmits<{ (e: 'close'): void }>();

// Método para cerrar el modal
function closeModal() {
  emit('close');
}
</script>