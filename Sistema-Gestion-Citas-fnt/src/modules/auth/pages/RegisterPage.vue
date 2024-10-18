<template>
  <div class="lg:w-1/2 xl:w-2/4 p-6 sm:p-12">
    <div class="w-full flex-1 flex flex-col items-center mt-2">

      <!-- Modal de notificación -->
      <NotificationModal :message="notificationMessage" :type="notificationType" :visible="showNotification"
        @update:visible="showNotification = false" />

        <div class="w-full lg:w-2/3 flex flex-col justify-center">
    <h1 class="text-3xl font-bold text-center mb-8">Registro de paciente</h1>
    <FormKit type="form" @submit.capture="submitForm" class="space-y-4" :actions="false">
      <!-- Nombre -->
      <FormKit
        type="text"
        name="nombre"
        label="Nombre"
        v-model="form.nombre"
        input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500"
        validation="required"
        :input-error-class="'border-red-500'"
        :validation-class="'text-red-500 text-sm mt-1'"
        validation-visibility="blur"
        :validation-messages="{ required: 'El nombre es obligatorio.' }"
      />
      <br>
      <!-- Apellido -->
      <FormKit
        type="text"
        name="apellido"
        label="Apellido"
        v-model="form.apellido"
        input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500"
        validation="required"
        :input-error-class="'border-red-500'"
        :validation-class="'text-red-500 text-sm mt-1'"
        validation-visibility="blur"
        :validation-messages="{ required: 'El apellido es obligatorio.' }"
      />
      <br>
      <!-- Fecha de nacimiento -->
      <FormKit
        type="date"
        name="fechaNacimiento"
        label="Fecha de Nacimiento"
        v-model="form.fechaNacimiento"
        input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500"
        validation="required"
        :input-error-class="'border-red-500'"
        :validation-class="'text-red-500 text-sm mt-1'"
        validation-visibility="blur"
        :validation-messages="{ required: 'La fecha de nacimiento es obligatoria.' }"
      />
      <br>
      <!-- Teléfono -->
      <FormKit
        type="tel"
        name="telefono"
        label="Teléfono"
        v-model.trim="form.telefono"
        input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500"
        :input-error-class="'border-red-500'"
        :validation-class="'text-red-500 text-sm mt-1'"
        validation-visibility="blur"
        :validation-messages="{
          required: 'El teléfono es obligatorio.',
        }"
        placeholder="000-000-000"
      />
      <br>
      <!-- Dirección -->
      <FormKit
        type="text"
        name="direccion"
        label="Dirección"
        v-model="form.direccion"
        input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500"
        validation="required"
        :input-error-class="'border-red-500'"
        :validation-class="'text-red-500 text-sm mt-1'"
        validation-visibility="blur"
        :validation-messages="{ required: 'La dirección es obligatoria.' }"
      />
      <br>
      <!-- Selección de género -->
      <FormKit
        type="select"
        name="genero"
        label="Género"
        v-model="form.genero"
        input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500"
        validation="required"
        :input-error-class="'border-red-500'"
        :validation-class="'text-red-500 text-sm mt-1'"
        validation-visibility="blur"
        :validation-messages="{ required: 'El género es obligatorio.' }"
        :options="[
          { label: 'Masculino', value: 'masculino' },
          { label: 'Femenino', value: 'femenino' }
        ]"
        placeholder="Seleccione una opción"
      />
      <br>
      <!-- Correo -->
      <FormKit
        type="email"
        name="correo"
        label="Correo Electrónico"
        v-model="form.correo"
        input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500"
        validation="required|email"
        :input-error-class="'border-red-500'"
        :validation-class="'text-red-500 text-sm mt-1'"
        validation-visibility="blur"
        :validation-messages="{ required: 'El correo es obligatorio.', email: 'Ingrese un correo válido.' }"
      />
      <br>
      <!-- Contraseña -->
      <FormKit
        type="password"
        name="password"
        label="Contraseña"
        v-model="form.password"
        @keyup.enter="submitForm"
        input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500"
        validation="required"
        :input-error-class="'border-red-500'"
        :validation-class="'text-red-500 text-sm mt-1'"
        validation-visibility="blur"
        :validation-messages="{ required: 'La contraseña es obligatoria.' }"
      />
      <br>
      <!-- Botón de envío -->
      <FormKit
        type="submit"
        label="Registrarse"
        input-class="w-full py-3 px-6 bg-blue-500 text-white rounded-lg font-semibold hover:bg-blue-600 transition duration-300"
      />
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
      notificationType.value = 'success';                           // Tipo éxito
    }
  } catch (error: any) {
    if (error.response) {
      console.error('Correo usado actualmente:', error.response.data);
      notificationMessage.value = "Correo ya está en uso";           // Mensaje de error
      notificationType.value = 'error';                             // Tipo error
    } else if (error.request) {
      console.error('No se recibió respuesta del servidor:', error.request);
      notificationMessage.value = "No se recibió respuesta del servidor"; // Otro caso de error
      notificationType.value = 'error';
    } else {
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