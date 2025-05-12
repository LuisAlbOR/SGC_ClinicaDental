<template>

    <div class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
        <div class="bg-gray-100 rounded-lg shadow-lg w-2/3 p-6 text-black">
            <h2 class="text-2xl font-semibold mb-4 text-center text-black">Editar Paciente</h2>

            <!-- Formulario de edición -->
            <FormKit type="form" @submit="savePatientData" class="space-y-6" :actions="false">
                <!-- Primera Parte -->

                <div class=" grid grid-cols-1 md:grid-cols-2 gap-6">
                    <!-- Nombre -->
                    <FormKit type="text" name="nombre" label="Nombre" v-model="editedPatient.nombrePaciente"
                        input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 text-black"
                        validation="required" :input-error-class="'border-red-500'"
                        :validation-class="'text-red-500 text-sm mt-1'" validation-visibility="blur"
                        :validation-messages="{ required: 'El nombre es obligatorio.' }"
                        placeholder="Ingrese el nombre" />

                    <!-- Apellido -->
                    <FormKit type="text" name="apellido" label="Apellido" v-model="editedPatient.apellidoPaciente"
                        input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 text-black"
                        validation="required" :input-error-class="'border-red-500'"
                        :validation-class="'text-red-500 text-sm mt-1'" validation-visibility="blur"
                        :validation-messages="{ required: 'El apellido es obligatorio.' }"
                        placeholder="Ingrese el apellido" />

                    <!-- Fecha de nacimiento -->
                    <FormKit type="date" name="fechaNacimiento" label="Fecha de Nacimiento"
                        v-model="editedPatient.fechaNacimiento"
                        input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 text-black"
                        validation="required" :input-error-class="'border-red-500'"
                        :validation-class="'text-red-500 text-sm mt-1'" validation-visibility="blur"
                        :validation-messages="{ required: 'La fecha de nacimiento es obligatoria.' }" />

                    <!-- Correo -->
                    <FormKit type="email" name="correo" label="Correo Electrónico"
                        v-model="editedPatient.correoPaciente"
                        input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 text-black"
                        validation="required|email" :input-error-class="'border-red-500'"
                        :validation-class="'text-red-500 text-sm mt-1'" validation-visibility="blur"
                        :validation-messages="{ required: 'El correo es obligatorio.', email: 'Ingrese un correo válido.' }"
                        placeholder="Ingrese el correo electrónico" />
                </div>
                <br>
                <!-- Segunda Parte -->
                <div class=" grid grid-cols-1 md:grid-cols-2 gap-6">
                    <!-- Teléfono -->
                    <FormKit type="tel" name="telefono" label="Teléfono" v-model.trim="editedPatient.telefonoPaciente"
                        input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 text-black"
                        :input-error-class="'border-red-500'" :validation-class="'text-red-500 text-sm mt-1'"
                        validation="required|matches:/^[0-9]{3}-[0-9]{3}-[0-9]{4}$/" :validation-messages="{
                            required: 'El teléfono es obligatorio.',
                            matches: 'El formato debe ser xxx-xxx-xxxx'
                        }" placeholder="000-000-000" />

                    <!-- Dirección -->
                    <FormKit type="text" name="direccion" label="Dirección" v-model="editedPatient.direccionPaciente"
                        input-class="mt-1 block w-full p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 text-black"
                        validation="required" :input-error-class="'border-red-500'"
                        :validation-class="'text-red-500 text-sm mt-1'" validation-visibility="blur"
                        :validation-messages="{ required: 'La dirección es obligatoria.' }"
                        placeholder="Ingrese la dirección" />

                </div>
            </FormKit>
            <br>
            <!-- Botones de acción -->
            <div class="flex justify-end space-x-4">
                <button type="button" @click="closeEditWindow"
                    class="px-4 py-2 bg-gray-300 text-gray-700 rounded hover:bg-gray-400 transition">Cancelar</button>
                <button type="submit" @click="savePatientData"
                    class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition">Guardar</button>
            </div>


        </div>
    </div>

    <loading :active="isLoading" :can-cancel="true" :is-full-page="true" />
</template>



<script setup lang="ts">
import { reactive, ref, watch } from 'vue';
import type { Paciente } from '@/modules/common/pages/InterfacePaciente'
import axios from 'axios';
import loading from 'vue-loading-overlay';
import 'vue-loading-overlay/dist/css/index.css';

// Propiedades y eventos
const props = defineProps<{ paciente: Paciente }>();
const emit = defineEmits(['close']);
const isLoading = ref(false);
const notificationMessage = ref('');
const notificationType = ref('');

//console.log('Desde el modal: ' + props.paciente.nombrePaciente);
// Estado del paciente editado
// Importación tipo-only para evitar el error
// Ahora puedes usar la interfaz en tu código
const editedPatient = reactive<Paciente>({
    id_paciente: 0, // Inicializa con un valor por defecto, puedes cambiarlo después
    nombrePaciente: '',
    apellidoPaciente: '',
    fechaNacimiento: '',
    generoPaciente: '',
    correoPaciente: '',
    telefonoPaciente: '',
    direccionPaciente: '',
    passwordPaciente: '',
});

// Función para convertir editedPatient a formato API
const convertToApiFormat = () => {
    return {
        nombre: editedPatient.nombrePaciente,
        apellido: editedPatient.apellidoPaciente,
        fechaNacimiento: editedPatient.fechaNacimiento, // Puedes formatear esta fecha según sea necesario
        correo: editedPatient.correoPaciente,
        telefono: editedPatient.telefonoPaciente,
        direccion: editedPatient.direccionPaciente,
    };
};

// Al momento de asignar valores desde el response
// Asegúrate de incluir la propiedad id_paciente también
Object.assign(editedPatient, props.paciente); // response.data debe tener id_paciente

// Inicializa el paciente editado al recibir props
watch(() => props.paciente, (newValue) => {
    if (newValue) {
        Object.assign(editedPatient, newValue); // Asigna los valores del nuevo paciente
    }
});

// Función para cerrar el modal
const closeEditWindow = () => {
    emit('close', {
        message: notificationMessage.value,  // Envía el mensaje de notificación
        type: notificationType.value          // Envía el tipo de notificación
    });
};

async function savePatientData() {
    isLoading.value = true; // Variable para mostrar un indicador de carga
    try {
        const apiData = convertToApiFormat(); // Convierte editedPatient a formato API
        const response = await axios.put(`http://localhost:8080/Paciente/ActualizarPaciente/${editedPatient.id_paciente}`, apiData);

        if (response.status >= 200 && response.status < 300) {
            notificationMessage.value = "Paciente actualizado exitosamente"; // Mensaje de éxito
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
