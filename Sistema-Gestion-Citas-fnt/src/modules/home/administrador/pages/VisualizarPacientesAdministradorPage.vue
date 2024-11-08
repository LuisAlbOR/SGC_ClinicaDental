<template>

  <NotificationModal :message="notificationMessage" :type="notificationType" :visible="showNotification"
    @update:visible="showNotification = false" />

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
                <th scope="col" class="p-5 text-left text-sm leading-6 font-semibold text-gray-900 capitalize"> Télefono
                </th>
                <th scope="col" class="p-5 text-left text-sm leading-6 font-semibold text-gray-900 capitalize"> Correo
                </th>
                <th scope="col"
                  class="p-5 text-left text-sm leading-6 font-semibold text-gray-900 capitalize rounded-t-xl">Acciones
                </th>
              </tr>
            </thead>
            <tbody v-if="ok" class="divide-y divide-gray-300">
              <tr v-for="(paciente, index) in pacientes" :key="index">
                <td class="p-5">{{ paciente.nombrePaciente }}</td>
                <td class="p-5">{{ paciente.apellidoPaciente }}</td>
                <td class="p-5">{{ paciente.telefonoPaciente }}</td>
                <td class="p-5">{{ paciente.correoPaciente }}</td>
                <td class="p-5 flex items-center space-x-3">
                  <img src="/public/EditIcon.svg" alt="Edit Icon" @click="openEditWindow(paciente)"
                    class="w-6 h-6 cursor-pointer filter drop-shadow-lg transition-transform duration-200 ease-in-out transform hover:scale-125 bg-blue-200 hover:bg-blue-400 rounded-md" />
                  <img src="/public/DeleteIcon.svg" alt="Delete Icon" @click="openDeleteConfirmation(paciente)"
                    class="w-6 h-6 cursor-pointer filter drop-shadow-lg transition-transform duration-200 ease-in-out transform hover:scale-125 bg-red-200 hover:bg-red-400 rounded-md" />
                  <img src="/public/ViewIcon.svg" alt="View Icon" @click="openViewDetails(paciente)"
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
  <NotificationDeletePacienteModal :isVisible="isModalDeleteVisible" @confirm="handleConfirm" @cancel="handleCancel"
    :paciente="selectedPaciente" />
  <!-- Modal de edición -->
  <EditPacientePage v-if="isEditModalVisible" :paciente="selectedPaciente" @close="handleCloseEditModal" />
  <ViewPacientePage :is-visible="isViewModalVisible" :paciente="selectedPaciente" @close="handleCloseViewModal"/>
  <Loading :active="isLoading" :can-cancel="true" :is-full-page="true" />
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import axios from "axios";
import Loading from 'vue-loading-overlay'
import 'vue-loading-overlay/dist/css/index.css';
import NotificationModal from "@/modules/common/pages/NotificationModal.vue";
import EditPacientePage from "@/modules/common/pages/EditPacientePage.vue";
import NotificationDeletePacienteModal from "@/modules/common/pages/NotificationDeletePacienteModal.vue";
import ViewPacientePage from "@/modules/common/pages/ViewPacientePage.vue";
import  type {NotificationData} from "./NotificationData";

//Variables
const apiUrl = "http://localhost:8080/Paciente/ListadoPaciente"; // Reemplaza con tu URI
const pacientes = ref<Array<{ nombrePaciente: string; apellidoPaciente: string; telefonoPaciente: string; correoPaciente: string }>>([]);
const isLoading = ref(false);
const notificationMessage = ref('');
const notificationType = ref('');
const showNotification = ref(false);
const ok = ref(false);
const isEditModalVisible = ref(false);
const isModalDeleteVisible = ref(false);
const isViewModalVisible = ref (false);
const selectedPaciente = ref({
  apellidoPaciente: '',
  correoPaciente: '',
  direccionPaciente: '',
  fechaNacimiento: '',
  generoPaciente: '',
  id_paciente: 0,
  nombrePaciente: '',
  passwordPaciente: '',
  telefonoPaciente: ''
});




// Función para manejar el cierre del modal
const handleCloseEditModal = (data: NotificationData) => {
  isEditModalVisible.value = false; // Cambia el estado de la visibilidad del modal

  // Recibe los datos del evento emitido
  if (data && data.message && data.type) {
    notificationMessage.value = data.message; // Asigna el mensaje de notificación
    notificationType.value = data.type;       // Asigna el tipo de notificación
    showNotification.value = true;            // Muestra la notificación
    fetchPacientes();
  } else {
    showNotification.value = false;           // Oculta la notificación si falta mensaje o tipo
  }

  // Aquí puedes realizar otras acciones si es necesario, como limpiar los datos
};

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
    fetchPacientes();
  } else {
    showNotification.value = false;           // Oculta la notificación si falta mensaje o tipo
  }
}
//Métodos
const openEditWindow = async (paciente: any) => {

  selectedPacienteMethod(paciente);
  setTimeout(() => {
    isLoading.value = false;
    isEditModalVisible.value = true; // Asegúrate de que esta variable controla la visibilidad del modal
  }, 1000);
}

async function selectedPacienteMethod(paciente: any) {
  isLoading.value = true;
  try {
    // Realiza la consulta a la API para obtener los detalles del paciente
    const response = await axios.get(`http://localhost:8080/Paciente/ListadoPaciente/${paciente.id_paciente}`);

    // Aquí puedes manejar los datos obtenidos
    //console.log("Datos del response:", response.data);

    // Asigna los datos obtenidos a selectedPaciente
    Object.assign(selectedPaciente.value, response.data);
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

const openDeleteConfirmation = (paciente: any) => {
  // Lógica para abrir el modal de confirmación de eliminación
  selectedPacienteMethod(paciente);
  setTimeout(() => {
    isLoading.value = false;
    isModalDeleteVisible.value = true; // Asegúrate de que esta variable controla la visibilidad del modal
  }, 1000);
};

const openViewDetails = (paciente: any) => {
  // Lógica para mostrar los detalles del paciente
  selectedPacienteMethod(paciente);
  setTimeout(()=>{
    isLoading.value= false;
    isViewModalVisible.value = true;
  }, 1000);  
};

const handleCloseViewModal = () =>{
   isViewModalVisible.value = false;
}
// Método independiente para obtener los pacientes
async function fetchPacientes() {
  try {
    const response = await axios.get(apiUrl);
    pacientes.value = response.data;

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

// Llama a fetchPacientes en el ciclo onMounted
onMounted(() => {
  fetchPacientes();
});


</script>
