<template>

  <!-- Modal de notificación -->
  <NotificationModal :message="notificationMessage" :type="notificationType" :visible="showNotification"
    @update:visible="showNotification = false" />

  <div class="w-full max-w-md mx-auto mt-10">
    <h1 class="text-3xl font-bold text-center mb-8">Registro de paciente</h1>
    <FormKit type="form" @submit="submitForm" class="space-y-4" :actions="false">
      <!-- Nombre -->
      <FormKit type="text" name="nombre" label="Nombre" v-model="form.nombre"
        input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 text-black"
        validation="required" :input-error-class="'border-red-500'" :validation-class="'text-red-500 text-sm mt-1'"
        validation-visibility="blur" :validation-messages="{ required: 'El nombre es obligatorio.' }" />
      <br>
      <!-- Apellido -->
      <FormKit type="text" name="apellido" label="Apellido" v-model="form.apellido"
        input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 text-black"
        validation="required" :input-error-class="'border-red-500'" :validation-class="'text-red-500 text-sm mt-1'"
        validation-visibility="blur" :validation-messages="{ required: 'El apellido es obligatorio.' }" />
      <br>
      <!-- Fecha de nacimiento -->
      <FormKit type="date" name="fechaNacimiento" label="Fecha de Nacimiento" v-model="form.fechaNacimiento"
        input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 text-black"
        validation="required" :input-error-class="'border-red-500'" :validation-class="'text-red-500 text-sm mt-1'"
        validation-visibility="blur" :validation-messages="{ required: 'La fecha de nacimiento es obligatoria.' }" />
      <br>
      <!-- Teléfono -->
      <FormKit type="tel" name="telefono" label="Teléfono" v-model.trim="form.telefono"
        input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 text-black"
        :input-error-class="'border-red-500'" :validation-class="'text-red-500 text-sm mt-1'"
        validation="requeried|matches:/^[0-9]{3}-[0-9]{3}-[0-9]{4}$/" validation-visibility="blur" :validation-messages="{
          required: 'El teléfono es obligatorio.',
          matches: 'El formato debe ser xxx-xxx-xxxx'
        }" placeholder="000-000-000" />
      <br>
      <!-- Dirección -->
      <FormKit type="text" name="direccion" label="Dirección" v-model="form.direccion"
        input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 text-black"
        validation="required" :input-error-class="'border-red-500'" :validation-class="'text-red-500 text-sm mt-1'"
        validation-visibility="blur" :validation-messages="{ required: 'La dirección es obligatoria.', matches:'El formato debe ser xxx-xxx-xxxx' }" />
      <br>
      <!-- Selección de género -->
      <FormKit type="select" name="genero" label="Género" v-model="form.genero"
        input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 text-black"
        validation="required" :input-error-class="'border-red-500'" :validation-class="'text-red-500 text-sm mt-1'"
        validation-visibility="blur" :validation-messages="{ required: 'El género es obligatorio.' }" :options="[
          { label: 'Masculino', value: 'masculino' },
          { label: 'Femenino', value: 'femenino' },
          { label: 'Otro', value: 'otro'}
        ]" placeholder="Seleccione una opción" />
      <br>
      <!-- Correo -->
      <FormKit type="email" name="correo" label="Correo Electrónico" v-model="form.correo"
        input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 text-black"
        validation="required|email" :input-error-class="'border-red-500'"
        :validation-class="'text-red-500 text-sm mt-1'" validation-visibility="blur"
        :validation-messages="{ required: 'El correo es obligatorio.', email: 'Ingrese un correo válido.' }" />
      <br>
      <!-- Contraseña -->
      <FormKit type="password" name="password" label="Contraseña" v-model="form.password"
        input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 text-black"
        validation="required" :input-error-class="'border-red-500'" :validation-class="'text-red-500 text-sm mt-1'"
        validation-visibility="blur" :validation-messages="{ required: 'La contraseña es obligatoria.' }" />
      <br>
      <!-- Botón de envío -->
      <button type="submit" :validation-messages="{required:'Por favor llene todos los campos'}"
        class="w-full py-3 px-6 bg-blue-500 text-white rounded-lg font-semibold hover:bg-blue-600 transition duration-300">
        Registrarse
      </button>
    </FormKit>
  </div>

  <br>
  <Loading :active="isLoading" :can-cancel="true" :is-full-page="true" />
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import axios from 'axios';
import NotificationModal from '@/modules/common/pages/NotificationModal.vue';
import Loading from 'vue-loading-overlay';
import 'vue-loading-overlay/dist/css/index.css'


const generos = [
  { value: 'masculino', label: 'Masculino' },
  { value: 'femenino', label: 'Femenino' },
  { value: 'otro', label: 'Otro' },
];

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

const isLoading = ref(false);
const notificationMessage = ref('');
const notificationType = ref('');
const showNotification = ref(false);

// Función para enviar los datos del formulario
async function submitForm() {
  isLoading.value = true;
  try {
    const response = await axios.post('http://localhost:8080/Paciente/RegistrarPaciente', form);

    if (response.status >= 200 && response.status < 300) {
      notificationMessage.value = 'Paciente registrado exitosamente'; // Mensaje de éxito
      notificationType.value = 'success';                           // Tipo éxito
    }
  } catch (error: any) {
    if (error.response) {
      notificationMessage.value = error.response.data || 'Error en el registro'; // Mensaje de error
      notificationType.value = 'error';                                        // Tipo error
    } else if (error.request) {
      notificationMessage.value = 'No se recibió respuesta del servidor'; // Otro caso de error
      notificationType.value = 'error';
    } else {
      notificationMessage.value = `Error en la solicitud: ${error.message}`;
      notificationType.value = 'error';
    }
  } finally {
    showNotification.value = true; // Mostrar la notificación
    isLoading.value = false;
  }
}
</script>
