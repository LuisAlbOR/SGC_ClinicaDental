<template>
  <div class="w-full max-w-md mx-auto mt-10">
    <h1 class="text-2xl font-bold text-center mb-6">Registro de Administrador</h1>

    <FormKit type="form" @submit.prevent="submitForm" class="space-y-4" :actions="false">

      <!-- Nombre -->
      <FormKit type="text" name="nombre" label="Nombre" v-model="form.nombre"
        input-class="mt-1 block w-full p-2 border border-gray-300 rounded-md" validation="required"
        :validation-messages="{ required: 'El nombre es obligatorio.' }" />
      <br>
      <!-- Apellido -->
      <FormKit type="text" name="apellido" label="Apellido" v-model="form.apellido"
        input-class="mt-1 block w-full p-2 border border-gray-300 rounded-md" validation="required"
        :validation-messages="{ required: 'El apellido es obligatorio.' }" />
      <br>
      <!-- Correo -->
      <FormKit type="email" name="correo" label="Correo Electrónico" v-model="form.correo"
        input-class="mt-1 block w-full p-2 border border-gray-300 rounded-md" validation="required|email"
        :validation-messages="{ required: 'El correo es obligatorio.', email: 'Ingrese un correo válido.' }" />
      <br>
      <!-- Contraseña -->
      <FormKit type="password" name="password" label="Contraseña" v-model="form.password"
        input-class="mt-1 block w-full p-2 border border-gray-300 rounded-md" validation="required|min:6"
        :validation-messages="{ required: 'La contraseña es obligatoria.', min: 'La contraseña debe tener al menos 6 caracteres.' }" />
      <br>
      <!-- Botón de envío -->
      <FormKit type="submit" label="Registrarse"
        input-class="w-full py-2 px-4 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition" />

    </FormKit>

    <!-- Modal de notificación -->
    <NotificationModal :message="notificationMessage" :type="notificationType" :visible="showNotification"
      @update:visible="showNotification = false" />

    <!-- Pantalla de carga -->
    <loading :active="isLoading" :can-cancel="true" :is-full-page="true" />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import axios from 'axios';
import Loading from 'vue-loading-overlay';
import 'vue-loading-overlay/dist/css/index.css';
import NotificationModal from '@/modules/common/pages/NotificationModal.vue';

// Estado del formulario
const form = ref({
  nombre: '',
  apellido: '',
  correo: '',
  password: ''
});

// Estado para la pantalla de carga y notificaciones
const isLoading = ref(false);
const showNotification = ref(false);
const notificationMessage = ref('');
const notificationType = ref('');

// Función para enviar el formulario
const submitForm = async () => {
  try {
    isLoading.value = true;

    // Enviar datos a la API
    const response = await axios.post('/api/admin/register', form.value);

    // Manejar respuesta
    notificationMessage.value = 'Administrador registrado exitosamente.';
    notificationType.value = 'success';
    showNotification.value = true;

    // Reiniciar formulario
    form.value = {
      nombre: '',
      apellido: '',
      correo: '',
      password: ''
    };
  } catch (error) {
    notificationMessage.value = 'Error al registrar el administrador.';
    notificationType.value = 'error';
    showNotification.value = true;
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
/* Aquí puedes agregar estilos personalizados si los necesitas */
</style>

  