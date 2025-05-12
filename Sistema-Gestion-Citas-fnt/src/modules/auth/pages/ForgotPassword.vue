<template>
    <div class="xl:w-1/2 p-6 sm:p-12">
        <div class="w-full flex-1 flex flex-col items-center mt-20">
            <div
                class="mt-7 bg-white rounded-xl shadow-lg dark:bg-gray-800 dark:border-gray-700 border-2 border-indigo-300 w-full">
                <div class="p-4 sm:p-7">
                    <div class="text-center">
                        <h1 class="block text-2xl font-bold text-gray-800 dark:text-white">¿Olvidó la contraseña?</h1>
                        <p class="mt-2 text-sm text-gray-600 dark:text-gray-400">
                            ¿Recuerdas la contraseña?
                            <RouterLink :to="{ name: 'login' }"
                                class="text-blue-500 text-sm font-medium hover:underline hover:text-blue-700 transition duration-300 ease-in-out">
                                Ingresar
                            </RouterLink>
                        </p>
                    </div>

                    <div class="mt-5">
                        <!-- Formulario de recuperación de contraseña -->
                        <FormKit type="form" @submit.prevent="checkEmail" :actions="false">
                            <!-- Campo de correo electrónico -->
                            <FormKit type="email" name="email" label="Correo electrónico" v-model="email"
                                input-class="py-3 px-4 block w-full border-2 border-gray-200 rounded-md text-sm focus:border-blue-500 focus:ring-blue-500 shadow-sm"
                                validation="required|email" :input-error-class="'border-red-500'"
                                :validation-class="'text-red-500 text-sm mt-1'" validation-visibility="blur"
                                :validation-messages="{ required: 'El correo es obligatorio.', email: 'Ingrese un correo válido.' }"
                                placeholder="ejemplo@correo.com" />

                            <!-- Botón de envío -->
                            <button type="submit"
                                class="mt-4 py-3 px-4 inline-flex justify-center items-center gap-2 rounded-md border border-transparent font-semibold bg-blue-500 text-white hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-all text-sm dark:focus:ring-offset-gray-800">
                            Enviar código de verificación
                            </button>
                        </FormKit>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal de notificación -->
    <NotificationModal :message="notificationMessage" :type="notificationType" :visible="showNotification"
        @update:visible="showNotification = false" />

    <!-- Pantalla de carga -->
    <loading :active="isLoading" :can-cancel="true" :is-full-page="true" />
</template>

<script setup lang="ts">
import { ref } from 'vue';
import axios from 'axios';
import { FormKit } from '@formkit/vue';
import Loading from 'vue-loading-overlay';
import 'vue-loading-overlay/dist/css/index.css';

// Estado del formulario
const email = ref('');
const isLoading = ref(false);
const showNotification = ref(false);
const notificationMessage = ref('');
const notificationType = ref<'success' | 'error'>();

// URL de la API (reemplaza con la tuya)
const API_URL = 'https://api.ejemplo.com/verificar-correo';

// Función que realiza la petición a la API para verificar si el correo existe
const checkEmail = async () => {
    isLoading.value = true;

    try {
        // Llamada a la API usando axios
        const response = await axios.post(API_URL, { email: email.value });

        if (response.data.exists) {
            notificationMessage.value = 'El correo electrónico ya está registrado.';
            notificationType.value = 'error';
        } else {
            notificationMessage.value = 'El correo electrónico está disponible.';
            notificationType.value = 'success';
        }
    } catch (error) {
        notificationMessage.value = 'Error al verificar el correo. Intente nuevamente.';
        notificationType.value = 'error';
    } finally {
        isLoading.value = false;
        showNotification.value = true;
    }
};
</script>