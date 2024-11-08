<template>

  <div class="flex flex-col">
    <div class=" overflow-x-auto">
      <div class="min-w-full inline-block align-middle">
        <div class="relative  text-gray-500 focus-within:text-gray-900 mb-4">
          <div class="absolute inset-y-0 left-1 flex items-center pl-3 pointer-events-none ">

          </div>
        </div>
        <div class="overflow-hidden ">
          <table class=" min-w-full rounded-xl">
            <thead>
              <tr class="bg-gray-300">
                <th scope="col"
                  class="p-5 text-left text-sm leading-6 font-semibold text-gray-900 capitalize rounded-t-xl"> Nombre
                </th>
                <th scope="col" class="p-5 text-left text-sm leading-6 font-semibold text-gray-900 capitalize"> Apellido
                </th>
                <th scope="col" class="p-5 text-left text-sm leading-6 font-semibold text-gray-900 capitalize"> Correo
                </th>
                <th scope="col" class="p-5 text-left text-sm leading-6 font-semibold text-gray-900 capitalize">
                  Contraseña
                </th>
                <th scope="col"
                  class="p-5 text-left text-sm leading-6 font-semibold text-gray-900 capitalize rounded-t-xl">Acciones
                </th>
              </tr>
            </thead>
            <tbody v-if="ok" class="divide-y divide-gray-300">
              <tr v-for="(administrador, index) in administradores" :key="index">
                <td class="p-5">{{ administrador.nombreAdministrador }}</td>
                <td class="p-5">{{ administrador.apellidoAdministrador }}</td>
                <td class="p-5">{{ administrador.correoAdministrador }}</td>
                <td class="p-5">{{ administrador.passwordAdministrador }}</td>
                <td class="p-5 flex items-center space-x-3">
                  <img src="/public/EditIcon.svg" alt="Edit Icon" @click="openEditWindow(administrador)"
                    class="w-6 h-6 cursor-pointer filter drop-shadow-lg transition-transform duration-200 ease-in-out transform hover:scale-125 bg-blue-200 hover:bg-blue-400 rounded-md" />
                  <img src="/public/DeleteIcon.svg" alt="Delete Icon" @click="openDeleteConfirmation(administrador)"
                    class="w-6 h-6 cursor-pointer filter drop-shadow-lg transition-transform duration-200 ease-in-out transform hover:scale-125 bg-red-200 hover:bg-red-400 rounded-md" />
                  <img src="/public/ViewIcon.svg" alt="View Icon" @click="openViewDetails(administrador)"
                    class="w-6 h-6 cursor-pointer filter drop-shadow-lg transition-transform duration-200 ease-in-out transform hover:scale-125 bg-green-200 hover:bg-green-400 rounded-md" />
                </td>
              </tr>
            </tbody>

            <tbody v-else>
              <tr>
                <td colspan="5" class="text-center p-5">
                  <h1 class="text-xl font-semibold text-gray-500">No se encontraron registros</h1>
                  <p class="text-gray-400 mt-2">Intenta ajustar tus filtros o buscar de nuevo.</p>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
  <NotificationModal :message="notificationMessage" :type="notificationType" :visible="showNotification"
    @update:visible="showNotification = false" />
  <Loading :active="isLoading" :can-cancel="true" :is-full-page="true" />
  <ViewAdministradorPage :is-visible="isViewModalVisible" :administrador="selectedAdministrador"
    @close="handleCloseViewModal" />
  <NotificationDeleteAdministradorModal :administrador="selectedAdministrador" :is-visible="isModalDeleteVisible"
    @cancel="handleCancel" @confirm="handleConfirm" />
  <EditAdministradorPage v-if="isEditModalVisible" :administrador="selectedAdministrador" @close="handleCloseEditModal"/>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import type { Administrador } from './Administrador';
import NotificationModal from '@/modules/common/pages/NotificationModal.vue';
import 'vue-loading-overlay/dist/css/index.css'
import Loading from 'vue-loading-overlay'
import axios from 'axios';
import ViewAdministradorPage from '@/modules/common/pages/ViewAdministradorPage.vue';
import NotificationDeleteAdministradorModal from '@/modules/common/pages/NotificationDeleteAdministradorModal.vue';
import type { NotificationData } from './NotificationData';
import EditAdministradorPage from '@/modules/common/pages/EditAdministradorPage.vue';

//Variables
const apiUrl = "http://localhost:8080/Administrador/ListarAdministrador"; // Reemplaza con tu URI
const administradores = ref<Array<Administrador>>([]);
const isLoading = ref(false);
const notificationMessage = ref('');
const notificationType = ref('');
const showNotification = ref(false);
const ok = ref(false);
const isEditModalVisible = ref(false);
const isModalDeleteVisible = ref(false);
const isViewModalVisible = ref(false);
const selectedAdministrador = ref({
  id_administrador: 0,
  nombreAdministrador: '',
  apellidoAdministrador: '',
  correoAdministrador: '',
  passwordAdministrador: '',
});

const openEditWindow = (administrador: Administrador) => {
  selectedAdministradorMethod(administrador);
  setTimeout(() => {
    isLoading.value = false;
    isEditModalVisible.value = true; // Asegúrate de que esta variable controla la visibilidad del modal
  }, 1000);
}

const openDeleteConfirmation = (administrador: Administrador) => {
  // Lógica para abrir el modal de confirmación de eliminación
  selectedAdministradorMethod(administrador);
  setTimeout(() => {
    isLoading.value = false;
    isModalDeleteVisible.value = true; // Asegúrate de que esta variable controla la visibilidad del modal
  }, 1000);
}
const openViewDetails = (administrador: Administrador) => {
  // Lógica para mostrar los detalles del paciente
  selectedAdministradorMethod(administrador);
  setTimeout(() => {
    isLoading.value = false;
    isViewModalVisible.value = true;
  }, 1000);
}
const handleCloseViewModal = () => {
  isViewModalVisible.value = false;
}
const handleCancel = () => {
  isModalDeleteVisible.value = false;
}

const handleConfirm = (data: NotificationData) => {
  isModalDeleteVisible.value = false;

  // Recibe los datos del evento emitido
  if (data && data.message && data.type) {
    notificationMessage.value = data.message; // Asigna el mensaje de notificación
    notificationType.value = data.type;       // Asigna el tipo de notificación
    showNotification.value = true;            // Muestra la notificación
    fetchAdministradores();
  } else {
    showNotification.value = false;           // Oculta la notificación si falta mensaje o tipo
  }
}
// Función para manejar el cierre del modal
const handleCloseEditModal = (data: NotificationData) => {
  isEditModalVisible.value = false; // Cambia el estado de la visibilidad del modal

  // Recibe los datos del evento emitido
  if (data && data.message && data.type) {
    notificationMessage.value = data.message; // Asigna el mensaje de notificación
    notificationType.value = data.type;       // Asigna el tipo de notificación
    showNotification.value = true;            // Muestra la notificación
    fetchAdministradores();
  } else {
    showNotification.value = false;           // Oculta la notificación si falta mensaje o tipo
  }

  // Aquí puedes realizar otras acciones si es necesario, como limpiar los datos
};

async function selectedAdministradorMethod(administrador: Administrador) {
  isLoading.value = true;
  try {
    // Realiza la consulta a la API para obtener los detalles del paciente
    const response = await axios.get(`http://localhost:8080/Administrador/ListadoAdministrador/${administrador.id_administrador}`);

    // Aquí puedes manejar los datos obtenidos
    //console.log("Datos del response:", response.data);

    // Asigna los datos obtenidos a selectedPaciente
    Object.assign(selectedAdministrador.value, response.data);
    //console.log('selectedPaciente: ' + selectedPaciente)
    // Muestra el modal pasando el paciente seleccionado
    // Asigna los datos obtenidos a cada atributo de selectedPaciente
    //console.log("selectedPaciente " + selectedPaciente.value.apellidoPaciente)
  } catch (error) {
    // Maneja el error mostrando un mensaje en el modal de notificación
    if (axios.isAxiosError(error) && error.response) {
      // Error específico de Axios con respuesta
      notificationMessage.value = `Error al obtener los datos del paciente: ${error.response.data.message || error.message}`;
    } else {
      // Error genérico
      notificationMessage.value = `Error al obtener los datos del paciente: ${error instanceof Error ? error.message : 'Error desconocido'}`;
    }

    notificationType.value = 'error'; // o 'success' dependiendo del tipo de notificación
    showNotification.value = true; // Muestra el modal de notificación
  }
}

async function fetchAdministradores() {
  try {
    const response = await axios.get(apiUrl);
    administradores.value = response.data;

    // Validación para comprobar si la respuesta está vacía
    if (response.data && response.data.length > 0) {
      ok.value = true; // Indica que la solicitud fue exitosa y hay datos
    } else {
      ok.value = false; // Indica que no hay registros
    }
  } catch (error: any) { // Cambiamos a 'any' para el manejo de errores
    ok.value = false; // Indica que hubo un error
    if (error.response) {
      notificationMessage.value = error.response.data || 'Error en el registro'; // Mensaje de error
      notificationType.value = 'error'; // Tipo error
      showNotification.value = true; // Mostrar la notificación
    } else if (error.request) {
      notificationMessage.value = 'No se recibió respuesta del servidor'; // Otro caso de error
      notificationType.value = 'error';
      showNotification.value = true; // Mostrar la notificación
    } else {
      notificationMessage.value = `Error en la solicitud: ${error.message}`; // Mensaje de error genérico
      notificationType.value = 'error';
      showNotification.value = true; // Mostrar la notificación
    }
  }
}

onMounted(() => {
  fetchAdministradores();
});
</script>