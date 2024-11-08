<template>
    <div v-if="isVisible" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
        <div class="bg-white p-6 rounded-lg shadow-lg w-96">
            <h3 class="text-xl font-semibold mb-4 text-red-600">Alerta</h3>
            <p class="mb-6 text-center text-black">¿Estás seguro de la eliminación?</p>
            <div class="flex justify-end space-x-4">
                <button @click="cancel" class="px-4 py-2 bg-gray-500 rounded-md">Cancelar</button>
                <button @click="confirm" class="px-4 py-2 bg-red-600 text-white rounded-md">Aceptar</button>
            </div>
        </div>
    </div>
    <loading :active="isLoading" :can-cancel="true" :is-full-page="true" />

</template>

<script setup lang="ts">
import { ref, defineProps, defineEmits } from 'vue';
import type { Paciente } from './InterfacePaciente';
import axios from 'axios';
import loading from 'vue-loading-overlay';
import 'vue-loading-overlay/dist/css/index.css';

// Props para controlar la visibilidad del modal desde el componente padre
const props = defineProps<{ isVisible: boolean, paciente: Paciente }>();
const notificationMessage = ref('');
const notificationType = ref('');
const isLoading = ref(false);

// Emite eventos de confirmación o cancelación
const emit = defineEmits<{
    (e: 'confirm', payload: { message: string; type: string }): void;
    (e: 'cancel'): void;
}>();

// Métodos para confirmar o cancelar
const confirm = () => {
    console.log(props.paciente.id_paciente);
    deletePatient();
};

const cancel = () => {
    emit('cancel');
};

const closeEditWindow = () => {
    emit('confirm', {
        message: notificationMessage.value,  // Envía el mensaje de notificación
        type: notificationType.value          // Envía el tipo de notificación
    });
}

//Método de eliminación del paciente
const deletePatient = async () => {
    isLoading.value = true;
    const apiUrl = `http://localhost:8080/Paciente/EliminarPaciente/${props.paciente.id_paciente}`;

    try {
        await axios.delete(apiUrl);
        notificationMessage.value = 'Paciente eliminado con éxito';
        notificationType.value = 'success';
        // Aquí puedes agregar lógica adicional para actualizar la lista de pacientes después de eliminar.
    } catch (error: any) {
        notificationMessage.value = 'Error al eliminar el paciente, tiene una cita pendiente';
        notificationType.value = 'error';
    } finally {

        setTimeout(() => { isLoading.value = false; closeEditWindow(); }, 2000);
    }
};
</script>
