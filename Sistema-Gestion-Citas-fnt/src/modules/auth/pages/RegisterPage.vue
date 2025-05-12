<template>
  <div class="lg:w-1/2 xl:w-2/4 p-6 sm:p-12">
    <div class="w-full flex-1 flex flex-col items-center mt-2">

      <!-- Modal de notificación -->
      <NotificationModal :message="notificationMessage" :type="notificationType" :visible="showNotification"
        @update:visible="showNotification = false" />

      <div class="w-full lg:w-2/3 flex flex-col justify-center">
        <h1 class="text-3xl font-bold text-center mb-8">Registro de paciente</h1>
        <FormKit type="form" @submit="submitForm" class="space-y-4" :actions="false">
          <!-- Nombre -->
          <FormKit type="text" name="nombre" label="Nombre" v-model="form.nombre"
            input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500"
            validation="required" :input-error-class="'border-red-500'"
            :validation-class="'text-red-500 text-sm mt-1'" validation-visibility="blur"
            :validation-messages="{ required: 'El nombre es obligatorio.', alpha: 'Solo se aceptan letras mayusculas y minusculas' }" />
          <br>
          <!-- Apellido -->
          <FormKit type="text" name="apellido" label="Apellido" v-model="form.apellido"
            input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500"
            validation="required" :input-error-class="'border-red-500'"
            :validation-class="'text-red-500 text-sm mt-1'"
            :validation-messages="{ required: 'El apellido es obligatorio.', alpha: 'Solo se aceptan letras mayusculas y minusculas' }" />
          <br>
          <!-- Fecha de nacimiento -->
          <FormKit type="date" name="fechaNacimiento" label="Fecha de Nacimiento" v-model="form.fechaNacimiento"
            input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500"
            validation="required" :input-error-class="'border-red-500'" :validation-class="'text-red-500 text-sm mt-1'"
            :validation-messages="{ required: 'La fecha de nacimiento es obligatoria.' }" />
          <br>
          <!-- Teléfono -->
          <FormKit type="tel" name="telefono" label="Teléfono" v-model.trim="form.telefono"
            input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500"
            :input-error-class="'border-red-500'" :validation-class="'text-red-500 text-sm mt-1'"
            validation="requeried|matches:/^[0-9]{3}-[0-9]{3}-[0-9]{4}$/" :validation-messages="{
              required: 'El teléfono es obligatorio.',
              matches: 'El formato debe ser xxx-xxx-xxxx'
            }" placeholder="000-000-000" />
          <br>
          <!-- Dirección -->
          <FormKit type="text" name="direccion" label="Dirección" v-model="form.direccion"
            input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500"
            validation="required" :input-error-class="'border-red-500'" :validation-class="'text-red-500 text-sm mt-1'"
            :validation-messages="{ required: 'La dirección es obligatoria.' }" />
          <br>
          <!-- Selección de género -->
          <FormKit type="select" name="genero" label="Género" v-model="form.genero"
            input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500"
            validation="required" :input-error-class="'border-red-500'" :validation-class="'text-red-500 text-sm mt-1'"
            :validation-messages="{ required: 'El género es obligatorio.' }" :options="[
              { label: 'Masculino', value: 'masculino' },
              { label: 'Femenino', value: 'femenino' },
              { label: 'Otro', value: 'otro' }
            ]" placeholder="Seleccione una opción" />
          <br>
          <!-- Correo -->
          <FormKit type="email" name="correo" label="Correo Electrónico" v-model="form.correo"
            input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500"
            validation="required|email" :input-error-class="'border-red-500'"
            :validation-class="'text-red-500 text-sm mt-1'"
            :validation-messages="{ required: 'El correo es obligatorio.', email: 'Ingrese un correo válido.' }" />
          <br>
          <!-- Contraseña -->
          <FormKit type="password" name="password" label="Contraseña" v-model="form.password"
            input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500"
            validation="required" :input-error-class="'border-red-500'" :validation-class="'text-red-500 text-sm mt-1'"
            :validation-messages="{ required: 'La contraseña es obligatoria.' }" />
          <br>
          <!-- Botón de envío -->
          <button type="submit"
            class="w-full py-3 px-6 bg-blue-500 text-white rounded-lg font-semibold hover:bg-blue-600 transition duration-300">
            Registrarse
          </button>
        </FormKit>
      </div>

      <br>
      <div
        class="w-4/12 py-3 px-6 bg-blue-500 text-white rounded-lg font-semibold hover:bg-blue-600 transition duration-300 text-center">
        <button @click="goToLogin"
          class="text-white font-medium hover:underline hover:text-blue-700 transition duration-300 ease-in-out">
          Cancelar
        </button>
      </div>
    </div>
  </div>
  <loading :active="isLoading" :can-cancel="true" :is-full-page="true" />
</template>


<script lang="ts" setup>
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import Loading from 'vue-loading-overlay';
import 'vue-loading-overlay/dist/css/index.css';
//import { useSnackbar } from 'notistack';
//import { POSITION, useToast } from 'vue-toastification';
import NotificationModal from '@/modules/common/pages/NotificationModal.vue';

// Estado para controlar la visibilidad del modal
const showNotification = ref(false);
// Estado para el mensaje de notificación
const notificationMessage = ref('');
// Estado para el tipo de notificación (éxito o error)
const notificationType = ref('');

const isLoading = ref(false);

const router = useRouter();

const form = reactive({
  nombre: '',
  apellido: '',
  fechaNacimiento: '',
  genero: '',
  correo: '',
  telefono: '',
  direccion: '',
  password: ''
});

//const $toasted = useToast();
//const { enqueueSnackbar } = useSnackbar();

// Función para enviar los datos del formulario
async function submitForm() {
  isLoading.value = true;
  try {
    const response = await axios.post('http://localhost:8080/Paciente/RegistrarPaciente', form);

    if (response.status >= 200 && response.status < 300) {
      notificationMessage.value = "Paciente registrado exitosamente"; // Mensaje de éxito
      notificationType.value = 'success';            
      setTimeout(() =>{
        goToLogin();
      },3000);
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

function goToLogin() {
  router.push({ name: 'login' });
}

</script>