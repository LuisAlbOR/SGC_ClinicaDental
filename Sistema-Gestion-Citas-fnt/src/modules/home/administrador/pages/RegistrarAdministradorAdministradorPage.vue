<template>
  <!-- Modal de notificación -->
  <!-- Modal de notificación -->
  <NotificationModal :message="notificationMessage" :type="notificationType" :visible="showNotification"
    @update:visible="showNotification = false" />


  <div class="w-full max-w-md mx-auto mt-10">
    <h1 class="text-2xl font-bold text-center mb-6">Registro de Administrador</h1>
    <!-- Formulario -->
    <FormKit type="form" @submit="submitForm" class="space-y-4" :actions="false">

      <!-- Nombre -->
      <FormKit type="text" name="nombre" label="Nombre" v-model="form.nombre"
        input-class="mt-1 block w-full p-2 border border-gray-300 rounded-md text-black" validation="required"
        :validation-messages="{ required: 'El nombre es obligatorio.', alpha:'Solo se aceptan mayusculas y minusculas' }" />
      <br>
      <!-- Apellido -->
      <FormKit type="text" name="apellido" label="Apellido" v-model="form.apellido"
        input-class="mt-1 block w-full p-2 border border-gray-300 rounded-md text-black" validation="required"
        :validation-messages="{ required: 'El apellido es obligatorio.' }" />
      <br>
      <!-- Correo -->
      <FormKit type="email" name="correo" label="Correo Electrónico" v-model="form.correo"
        input-class="mt-1 block w-full p-2 border border-gray-300 rounded-md text-black" validation="required|email"
        :validation-messages="{ required: 'El correo es obligatorio.', email: 'Ingrese un correo válido.' }" />
      <br>
      <!-- Contraseña -->
      <FormKit type="password" name="password" label="Contraseña" v-model="form.password" @keyup.enter="submitForm"
        input-class="mt-1 block w-full p-2 border border-gray-300 rounded-md text-black" validation="required"
        :validation-messages="{ required: 'La contraseña es obligatoria.' }" />
      <br>
      <!-- Botón de envío -->
      <button type="submit"
        class="w-full py-3 px-6 bg-blue-500 text-white rounded-lg font-semibold hover:bg-blue-600 transition duration-300">
        Registrar administrador
      </button>

    </FormKit>
    <!-- Pantalla de carga -->
    <loading :active="isLoading" :can-cancel="true" :is-full-page="true" />
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import axios from 'axios';
import Loading from 'vue-loading-overlay';
import 'vue-loading-overlay/dist/css/index.css';
import NotificationModal from '@/modules/common/pages/NotificationModal.vue';


// Estado para controlar la visibilidad del modal
const showNotification = ref(false);
// Estado para el mensaje de notificación
const notificationMessage = ref('');
// Estado para el tipo de notificación (éxito o error)
const notificationType = ref('');

const isLoading = ref(false);

// Estado del formulario
const form = reactive({
  nombre: '',
  apellido: '',
  correo: '',
  password: ''
});

// Función para enviar los datos del formulario
async function submitForm() {
  isLoading.value = true;
  try {
    const response = await axios.post('http://localhost:8080/Administrador/RegistrarAdministrador', form);

    if (response.status >= 200 && response.status < 300) {
      notificationMessage.value = "Administrador registrado exitosamente"; // Mensaje de éxito
      notificationType.value = 'success';                           // Tipo éxito
    }
  } catch (error: any) {
    if (error.response) {
      // Si el servidor envía un mensaje en el body del response
      const mensajeError = error.response.data; // El mensaje viene desde el backend

      if (error.response.status === 409) {
        // Caso específico para conflicto de correos (código 409)
        notificationMessage.value = mensajeError; // Usar el mensaje de conflicto desde el backend
      } else {
        // Otros errores con respuesta del servidor
        notificationMessage.value = "Error al registrar: " + mensajeError;
      }

      notificationType.value = 'error'; // Tipo error
      console.log(notificationMessage.value, notificationType.value);

    } else if (error.request) {
      // Cuando no hay respuesta del servidor
      console.error('No se recibió respuesta del servidor:', error.request);
      notificationMessage.value = "No se recibió respuesta del servidor"; // Otro caso de error
      notificationType.value = 'error';
    } else {
      // Otros errores relacionados con la solicitud
      console.error('Error en la solicitud:', error.message);
      notificationMessage.value = `Error en la solicitud: ${error.message}`;
      notificationType.value = 'error';
    }
  } finally {
    showNotification.value = true; // Mostrar la notificación
    isLoading.value = false;
  }

}

</script>
