<template>
  <div class="lg:w-1/2 xl:w-2/4 p-6 sm:p-12">
    <div class="flex justify-center items-center">
      <img src="../../../../public/Clinica_Logo.png" class="w-4/12 rounded-full" />
    </div>
    <div class="mt-12 flex flex-col items-center">
      <div class="w-full flex-1 mt-8">
        <div class="mx-auto max-w-xs">
          <!-- FormKit Form -->
          <FormKit type="form" @submit="handleKeyupEnter" class="space-y-5" :actions="false">
            <FormKit type="email" name="email" label="Email" :value="email" v-model="email"
              input-class="w-full px-8 py-4 rounded-lg font-medium bg-gray-100 border border-gray-200 placeholder-gray-500 text-sm focus:outline-none focus:border-gray-400 focus:bg-white mt-2" />
            <br>
              <FormKit type="password" name="password" label="Password" :value="password" v-model="password"
              @keyup.enter="handleKeyupEnter"
              input-class="w-full px-8 py-4 rounded-lg font-medium bg-gray-100 border border-gray-200 placeholder-gray-500 text-sm focus:outline-none focus:border-gray-400 focus:bg-white mt-2" />
            <!-- Botón submit dentro del formulario -->
            <FormKit 
              type="submit" 
              label="Entrar" 
              input-class="mt-5 tracking-wide font-semibold bg-green-400 text-white w-5/12 py-4 rounded-lg hover:bg-green-700 transition-all duration-300 ease-in-out flex justify-center items-center focus:shadow-outline focus:outline-none"
            />  
          </FormKit>
        </div>
        <br>
        <br>
        <div class="flex justify-center w-full mt-5">
          <p class="text-sm">¿Eres un paciente no registrado?</p>
          <RouterLink :to="{ name: 'register' }"
            class="text-blue-500 text-sm font-medium hover:underline hover:text-blue-700 transition duration-300 ease-in-out">
            Regístrate aquí
          </RouterLink>
        </div>
        <div class="flex justify-center w-full mt-5">
          <p class="text-sm">¿Olvidaste tu contraseña?</p>
          <RouterLink :to="{ name: 'email' }"
            class="text-blue-500 text-sm font-medium hover:underline hover:text-blue-700 transition duration-300 ease-in-out">
            Recuperar contraseña
          </RouterLink>
        </div>
      </div>
    </div>
  </div>
  <NotificationModal :message="notificationMessage" :type="notificationType" :visible="showNotification"
    @update:visible="showNotification = false" />
  <loading :active="isLoading" :can-cancel="true" :is-full-page="true" />
</template>

<script lang="ts" setup>
import { reactive, ref } from 'vue';
import axios from 'axios';
import NotificationModal from '@/modules/common/pages/NotificationModal.vue';
import Loading from 'vue-loading-overlay';
import 'vue-loading-overlay/dist/css/index.css';
import router from '@/router';

// Estado para controlar la visibilidad del modal
const showNotification = ref(false);
// Estado para el mensaje de notificación
const notificationMessage = ref('');
// Estado para el tipo de notificación (éxito o error)
const notificationType = ref('success');
const isLoading = ref(false);
const email = ref('');
const password = ref('');
const data = reactive({
  id: 0,
  nombre: '',
  apellido: '',
  ocupacion: '',
});

const login = async () => {
  isLoading.value = true;
  try {
    const response = await axios.post('http://localhost:8080/Login/Verificar', {
      correo: email.value,
      password: password.value,
    });

    if (response.status === 200 && response.status < 300) {
      notificationMessage.value = 'Logín exitoso';
      notificationType.value = 'success';
      data.id = response.data.id;
      data.nombre = response.data.nombre;
      data.apellido = response.data.apellido;
      data.ocupacion = response.data.ocupacion;

      if (data.ocupacion === 'paciente') {
        router.push('/paciente');
      } else if (data.ocupacion === 'medico') {
        router.push('/medico');
      } else {
        router.push('/administrador');
      }
    } else {
      notificationMessage.value = 'Error en el login: ' + response.statusText;
      notificationType.value = 'error';
    }
  } catch (error: any) {
    if (error.response) {
      notificationMessage.value = 'Correo inválido: ' + error.response.data;
      notificationType.value = 'error';
    } else if (error.request) {
      notificationMessage.value = 'No se recibió respuesta del servidor';
      notificationType.value = 'error';
    } else {
      notificationMessage.value = 'Error en la solicitud: ' + error.message;
      notificationType.value = 'error';
    }
  } finally {
    showNotification.value = true;
    isLoading.value = false;
  }
};

const handleKeyupEnter = () => {
  if (!showNotification.value) {
    login();
  }
};
</script>

<style scoped>
/* Aquí puedes agregar estilos específicos para este componente */
</style>
