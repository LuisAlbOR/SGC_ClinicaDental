<template>
    <div v-if="isVisible" class="fixed inset-0 bg-gray-800 bg-opacity-50 flex justify-center items-center z-50">
      <div class="bg-white rounded-lg shadow-lg w-11/12 md:w-1/3 p-6">
        <h2 class="text-xl font-semibold text-black mb-4 text-center">Detalles del Paciente</h2>
        
        <ul class="text-gray-700 space-y-2">
          <li><strong>ID:</strong> {{ paciente.id_paciente }}</li>
          <li><strong>Nombre:</strong> {{ paciente.nombrePaciente }}</li>
          <li><strong>Apellido:</strong> {{ paciente.apellidoPaciente }}</li>
          <li><strong>Fecha de Nacimiento:</strong> {{ paciente.fechaNacimiento }}</li>
          <li><strong>Género:</strong> {{ paciente.generoPaciente }}</li>
          <li><strong>Correo:</strong> {{ paciente.correoPaciente }}</li>
          <li><strong>Teléfono:</strong> {{ paciente.telefonoPaciente }}</li>
          <li><strong>Dirección:</strong> {{ paciente.direccionPaciente }}</li>
          <li><strong>Contraseña:</strong> {{ paciente.passwordPaciente }}</li>
        </ul>
  
        <div class="mt-6 flex justify-center">
          <button @click="closeModal" class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 focus:outline-none focus:ring-2 focus:ring-green-400">
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
    paciente: {
      id_paciente: number;
      nombrePaciente: string;
      apellidoPaciente: string;
      fechaNacimiento: string;
      generoPaciente: string;
      correoPaciente: string;
      telefonoPaciente: string;
      direccionPaciente: string;
      passwordPaciente: string;
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