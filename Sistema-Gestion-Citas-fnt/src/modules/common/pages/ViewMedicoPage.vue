<template>
  <div v-if="isVisible" class="fixed inset-0 bg-gray-800 bg-opacity-50 flex justify-center items-center z-50">
    <div class="bg-white rounded-lg shadow-lg w-11/12 md:w-1/3 p-6">
      <h2 class="text-xl font-semibold text-black mb-4 text-center">Detalles del Médico</h2>

      <ul class="text-gray-700 space-y-2">
        <li><strong>ID:</strong> {{ medico.id_medico }}</li>
        <li><strong>Nombre:</strong> {{ medico.nombreMedico }}</li>
        <li><strong>Apellido:</strong> {{ medico.apellidoMedico }}</li>
        <li><strong>Especialidad:</strong> {{ medico.especialidadMedico }}</li>
        <li><strong>Télefono:</strong> {{ medico.telefonoMedico }}</li>
        <li><strong>Correo:</strong> {{ medico.correoMedico }}</li>
        <li><strong>Contraseña:</strong> {{ medico.passwordMedico }}</li>
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
import { ref, defineProps, defineEmits } from 'vue';

// Props: Datos del paciente pasados al modal
const props = defineProps<{
  medico: {
    id_medico: number;
    nombreMedico: string;
    apellidoMedico: string;
    telefonoMedico: string;
    correoMedico: string;
    especialidadMedico: string;
    passwordMedico: string;
  };
  isVisible: boolean;
}>();

// Emisor de eventos para cerrar el modal
const emit = defineEmits<{ (e: 'close'): void }>();

// Método para cerrar el modal
function closeModal() {
  emit('close');
}
</script>