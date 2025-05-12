<template>

    <div class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
        <div class="bg-gray-100 rounded-lg shadow-lg w-2/3 p-6 text-black">
            <h2 class="text-2xl font-semibold mb-4 text-center text-black">Editar cita</h2>

            <!-- Formulario de edición -->
            <FormKit type="form" @submit="saveCitaData" class="space-y-6" :actions="false">
                <!-- Primera Parte -->


                <!--Fecha-->
                <FormKit type="date" name="fecha" label="Fecha" @blur="handleDateInput" v-model="editedCita.fecha"
                    input-class="mt-1 block w-full p-2 border border-gray-300 rounded-md text-black"
                    validation="required" :validation-messages="{ required: 'La fecha es obligatoria.' }" />


                <br>

                <!-- Hora -->
                <FormKit type="time" name="hora" label="Hora de la Cita" v-model="editedCita.hora"
                    input-class="mt-1 block w-full p-2 border border-gray-300 rounded-md text-black"
                    validation="required" :validation-messages="{ required: 'La hora es obligatoria.' }" />
                <br>
                <br>

                <div class="space-y-4">
                    <!-- Filtro de Especialidad -->
                    <div class="flex space-x-4">
                        <!-- Select de Especialidad con opción predeterminada -->
                        <FormKit type="select" name="especialidad" label="Especialidad"
                            v-model="especialidadSeleccionada"
                            input-class="mt-1 block w-full p-2 border border-gray-300 rounded-md text-black"
                            validation="required" :input-error-class="'border-red-500'"
                            :validation-messages="{ required: 'La especialidad es obligatoria.' }">
                            <option disabled value="">Seleccione una especialidad</option>
                            <option v-for="especialidad in especialidades" :value="especialidad">
                                {{ especialidad }}
                            </option>
                        </FormKit>


                        <!-- Botón Filtrar -->
                        <button type="button" @click="filterMedicos"
                            class="w-20 py-2 px-4 bg-blue-500 text-white rounded-lg font-semibold hover:bg-blue-600 transition duration-300">
                            Filtrar
                        </button>

                    </div>

                    <!-- Select de Médico filtrado por especialidad -->
                    <FormKit type="select" name="medico" label="Médico"     @change="updateMedico"
                        input-class="mt-1 block w-full p-2 border border-gray-300 rounded-md text-black"
                        validation="required" :validation-messages="{ required: 'Seleccione un médico.' }">
                        <option disabled value="0">Seleccione un médico</option>
                        <option v-for="medico in medicos" :key="medico.id_medico" :value="medico.id_medico">
                            {{ medico.nombreMedico }} {{ medico.apellidoMedico }}
                        </option>
                    </FormKit>
                </div>

            </FormKit>
            <br>
            <!-- Botones de acción -->
            <div class="flex justify-end space-x-4">
                <button type="button" @click="closeEditWindow"
                    class="px-4 py-2 bg-gray-300 text-gray-700 rounded hover:bg-gray-400 transition">Cancelar</button>
                <button type="submit" @click="saveCitaData"
                    class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition">Guardar</button>
            </div>


        </div>
    </div>

    <loading :active="isLoading" :can-cancel="true" :is-full-page="true" />
</template>



<script setup lang="ts">
import { onMounted, reactive, ref, watch } from 'vue';
import axios from 'axios';
import loading from 'vue-loading-overlay';
import 'vue-loading-overlay/dist/css/index.css';
import type { Cita } from './InterfaceCita';
import type { Medico } from '@/modules/home/administrador/pages/Medico';
import type { AppointmentData } from '@/modules/home/administrador/pages/AppointmentData';
import especialidades from '@/modules/home/administrador/pages/Especialidades';

const medicosOriginales = ref<Medico[]>([]);
const medicos = ref<Medico[]>([]);  // Lista de médicos filtrados
const especialidadSeleccionada = ref<string>(''); // Especialidad seleccionada

// Propiedades y eventos
const props = defineProps<{ cita: Cita }>();
const emit = defineEmits(['close']);
const isLoading = ref(false);
const notificationMessage = ref('');
const notificationType = ref('');

//console.log('Desde el modal: ' + props.paciente.nombrePaciente);
// Estado del paciente editado
// Importación tipo-only para evitar el error
// Ahora puedes usar la interfaz en tu código
const editedCita = reactive<Cita>({
    id_cita: 0,
    fecha: '',
    hora: '',
    paciente: {
        id_paciente: 0,
        nombrePaciente: '',
        apellidoPaciente: '',
        fechaNacimiento: '',
        generoPaciente: '',
        correoPaciente: '',
        telefonoPaciente: '',
        direccionPaciente: '',
        passwordPaciente: '',
    },
    medico: {
        id_medico: 0,
        nombreMedico: '',
        apellidoMedico: '',
        especialidadMedico: '',
        telefonoMedico: '',
        correoMedico: '',
    },
});

const updateMedico = (event: Event) => {
    const selectedId = Number((event.target as HTMLSelectElement).value);

    // Buscar el objeto médico en la lista de médicos usando el id seleccionado
    const selectedMedico = medicos.value.find(
        (medico) => medico.id_medico === selectedId
    );

    // Asignar el objeto médico completo a editedCita.medico
    if (selectedMedico) {
        editedCita.medico = selectedMedico;
    }
};

// Función para convertir editedPatient a formato API
const convertToApiFormat = () => {
    return {
        fecha: editedCita.fecha,
        hora: editedCita.hora,
        id_medico: editedCita.medico,
    };
};

// Al momento de asignar valores desde el response
// Asegúrate de incluir la propiedad id_paciente también

// Inicializa el paciente editado al recibir props
watch(() => props.cita, (newValue) => {
    if (newValue) {
        Object.assign(editedCita, newValue);

        // Buscar y asignar el objeto médico completo
        const selectedMedico = medicos.value.find(
            (medico) => medico.id_medico === newValue.medico.id_medico
        );

        if (selectedMedico) {
            editedCita.medico = selectedMedico;
        }
    }
});


// Función para cerrar el modal
const closeEditWindow = () => {
    emit('close', {
        message: notificationMessage.value,  // Envía el mensaje de notificación
        type: notificationType.value          // Envía el tipo de notificación
    });
};

async function saveCitaData() {
    isLoading.value = true; // Variable para mostrar un indicador de carga
    const citaFormateada = {
        ...convertToApiFormat() ,
        hora: formatHora(editedCita.hora),  // Asegura el formato correcto de la hora
    };
    console.log(citaFormateada)
    try {
        const apiData = convertToApiFormat(); // Convierte editedPatient a formato API
        const response = await axios.put(`http://localhost:8080/Cita/ActualizarCita/${editedCita.id_cita}`, citaFormateada);

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

const formatHora = (hora: string): string => {
    // Verifica si la hora ya tiene segundos
    if (hora.length === 5) {
        return `${hora}:00`;  // Agrega los segundos
    }
    return hora;  // Si la hora ya tiene segundos, la retorna sin cambios
};
const fetchMedicos = async () => {
    try {
        const response = await axios.get('http://localhost:8080/Medico/ListadoMedico');
        medicosOriginales.value = response.data; // Asegúrate de que el formato del response sea compatible
    } catch (error) {
        console.error('Error al obtener los médicos:', error);
    }
};

const filterMedicos = async () => {
    if (especialidadSeleccionada.value) {
        try {
            // Codificar el valor de la especialidad para manejar espacios en blanco
            const encodedEspecialidad = encodeURIComponent(especialidadSeleccionada.value);
            const response = await axios.get<Medico[]>(`http://localhost:8080/Medico/ListadoMedico/Especialidad/${encodedEspecialidad}`);
            medicos.value = response.data;
            // console.log(medicos.value);
        } catch (error) {
            console.error("Error al obtener médicos por especialidad:", error);
        }
    } else {
        medicos.value = medicosOriginales.value;
    }
};
// Método para validar la fecha
const handleDateInput = (event: Event) => {
    const inputDate = (event.target as HTMLInputElement).value;
    const selectedDate = new Date(inputDate);
    const currentDate = new Date();

    currentDate.setDate(currentDate.getDate()); // Suma un día a la fecha actual

    // Comprobar si la fecha seleccionada es menor o igual a la fecha actual
    if (selectedDate <= currentDate) {
        // Aquí puedes hacer alguna acción, como mostrar un mensaje de error, etc.
        alert('La fecha seleccionada debe ser al menos un día después de la fecha actual.');
        editedCita.fecha = ''; // Limpiar el valor si no es válido
    }

    editedCita.fecha = inputDate;

};

onMounted(() => {
    fetchMedicos();
    Object.assign(editedCita, props.cita); // response.data debe tener id_paciente

});
</script>
