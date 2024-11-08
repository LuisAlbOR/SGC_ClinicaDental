<template>

    <div class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
        <div class="bg-gray-100 rounded-lg shadow-lg w-2/3 p-6 text-black">
            <h2 class="text-2xl font-semibold mb-4 text-center text-black">Editar médico</h2>

            <!-- Formulario de edición -->
            <FormKit type="form" @submit="saveMedicoData" class="space-y-6" :actions="false">
                <!-- Primera Parte -->

                <div class=" grid grid-cols-1 md:grid-cols-2 gap-6">
                    <!-- Nombre -->
                    <FormKit type="text" name="nombre" label="Nombre" v-model="editedMedico.nombreMedico"
                        input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 text-black"
                        validation="required" :input-error-class="'border-red-500'"
                        :validation-class="'text-red-500 text-sm mt-1'" validation-visibility="blur"
                        :validation-messages="{ required: 'El nombre es obligatorio.' }"
                        placeholder="Ingrese el nombre" />

                    <!-- Apellido -->
                    <FormKit type="text" name="apellido" label="Apellido" v-model="editedMedico.apellidoMedico"
                        input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 text-black"
                        validation="required" :input-error-class="'border-red-500'"
                        :validation-class="'text-red-500 text-sm mt-1'" validation-visibility="blur"
                        :validation-messages="{ required: 'El apellido es obligatorio.' }"
                        placeholder="Ingrese el apellido" />

                    <!-- Correo -->
                    <FormKit type="email" name="correo" label="Correo Electrónico" v-model="editedMedico.correoMedico"
                        input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 text-black"
                        validation="required|email" :input-error-class="'border-red-500'"
                        :validation-class="'text-red-500 text-sm mt-1'" validation-visibility="blur"
                        :validation-messages="{ required: 'El correo es obligatorio.', email: 'Ingrese un correo válido.' }"
                        placeholder="Ingrese el correo electrónico" />

                    <!-- Teléfono -->
                    <FormKit type="tel" name="telefono" label="Teléfono" v-model.trim="editedMedico.telefonoMedico"
                        input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 text-black"
                        :input-error-class="'border-red-500'" :validation-class="'text-red-500 text-sm mt-1'"
                        validation="required|matches:/^[0-9]{3}-[0-9]{3}-[0-9]{4}$/" :validation-messages="{
                            required: 'El teléfono es obligatorio.',
                            matches: 'El formato debe ser xxx-xxx-xxxx'
                        }" placeholder="000-000-000" />
                </div>
                <br>
            </FormKit>
            <br>
            <!-- Botones de acción -->
            <div class="flex justify-end space-x-4">
                <button type="button" @click="closeEditWindow"
                    class="px-4 py-2 bg-gray-300 text-gray-700 rounded hover:bg-gray-400 transition">Cancelar</button>
                <button type="submit" @click="saveMedicoData"
                    class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition">Guardar</button>
            </div>


        </div>
    </div>

    <loading :active="isLoading" :can-cancel="true" :is-full-page="true" />
</template>



<script setup lang="ts">
import { reactive, ref, watch } from 'vue';
import type { Medico } from '@/modules/common/pages/InterfaceMedico'
import axios from 'axios';
import loading from 'vue-loading-overlay';
import 'vue-loading-overlay/dist/css/index.css';

// Propiedades y eventos
const props = defineProps<{ medico: Medico }>();
const emit = defineEmits(['close']);
const isLoading = ref(false);
const notificationMessage = ref('');
const notificationType = ref('');

//console.log('Desde el modal: ' + props.paciente.nombrePaciente);
// Estado del paciente editado
// Importación tipo-only para evitar el error
// Ahora puedes usar la interfaz en tu código
const editedMedico = reactive<Medico>({
    id_medico: 0, // Inicializa con un valor por defecto, puedes cambiarlo después
    nombreMedico: '',
    apellidoMedico: '',
    telefonoMedico: '',
    correoMedico: '',
    passwordMedico: '',
    especialidadMedico: '',
});

// Función para convertir editedPatient a formato API
const convertToApiFormat = () => {
    return {
        nombre: editedMedico.nombreMedico,
        apellido: editedMedico.apellidoMedico,
        correo: editedMedico.correoMedico,
        telefono: editedMedico.telefonoMedico,
    };
};

// Al momento de asignar valores desde el response
// Asegúrate de incluir la propiedad id_medico también
Object.assign(editedMedico, props.medico); // response.data debe tener id_medico

// Inicializa el medico editado al recibir props
watch(() => props.medico, (newValue) => {
    if (newValue) {
        Object.assign(editedMedico, newValue); // Asigna los valores del nuevo paciente
    }
});

// Función para cerrar el modal
const closeEditWindow = () => {
    emit('close', {
        message: notificationMessage.value,  // Envía el mensaje de notificación
        type: notificationType.value          // Envía el tipo de notificación
    });
};

async function saveMedicoData() {
    isLoading.value = true; // Variable para mostrar un indicador de carga
    try {
        const apiData = convertToApiFormat(); // Convierte editedPatient a formato API
        const response = await axios.put(`http://localhost:8080/Medico/ActualizarMedico/${editedMedico.id_medico}`, apiData);

        if (response.status >= 200 && response.status < 300) {
            notificationMessage.value = "Medico actualizado exitosamente"; // Mensaje de éxito
            notificationType.value = 'success'; // Tipo éxito
            //console.log('exitoso')
        }
    } catch (error: any) {
        if (error.response) {
            const mensajeError = error.response.data; // Mensaje del backend

            if (error.response.status === 409) {
                notificationMessage.value = mensajeError; // Mensaje específico para conflictos
            } else {
                notificationMessage.value = "Error al actualizar: " + mensajeError; // Otros errores
            }

            notificationType.value = 'error'; // Tipo error
            console.log(notificationMessage.value, notificationType.value);
        } else if (error.request) {
            console.error('No se recibió respuesta del servidor:', error.request);
            notificationMessage.value = "No se recibió respuesta del servidor"; // Error al no recibir respuesta
            notificationType.value = 'error';
        } else {
            console.error('Error en la solicitud:', error.message);
            notificationMessage.value = `Error en la solicitud: ${error.message}`; // Otros errores de solicitud
            notificationType.value = 'error';
        }
    } finally {

        setTimeout(() => { isLoading.value = false; closeEditWindow(); }, 2000);
    }

}

</script>
