<template>
    <div class="w-full max-w-md mx-auto mt-10">

        <h2 class="text-2xl font-bold mb-4">Registrar Cita Médica</h2>
        <NotificationModal :message="notificationMessage" :type="notificationType" :visible="showNotification"
            @update:visible="showNotification = false" />

        <FormKit type="form" @submit="registerAppointment" class="space-y-4" :actions="false">

            <!--Fecha-->
            <FormKit type="date" name="fecha" label="Fecha" @blur="handleDateInput"
                input-class="mt-1 block w-full p-2 border border-gray-300 rounded-md text-black" validation="required"
                :validation-messages="{ required: 'La fecha es obligatoria.' }" />


            <br>

            <!-- Hora -->
            <FormKit type="time" name="hora" label="Hora de la Cita" v-model="appointmentData.hora"
                input-class="mt-1 block w-full p-2 border border-gray-300 rounded-md text-black" validation="required"
                :validation-messages="{ required: 'La hora es obligatoria.' }" />
            <br>
            <!-- Selección de Paciente -->
            <FormKit type="select" name="paciente" label="Paciente" v-model="appointmentData.id_paciente"
                input-class="mt-1 block w-full p-2 border border-gray-300 rounded-md text-black" validation="required"
                :validation-messages="{ required: 'Seleccione un paciente.' }">
                <option disabled value="0">Seleccione un paciente</option>
                <option v-for="paciente in pacientes" :key="paciente.id_paciente" :value="paciente.id_paciente">
                    {{ paciente.nombrePaciente }}
                </option>
            </FormKit>
            <br>

            <div class="space-y-4">

            </div>

            <br>

            <!-- Botón de Envío -->
            <button type="submit"
                class="w-full py-3 px-6 bg-blue-500 text-white rounded-lg font-semibold hover:bg-blue-600 transition duration-300">
                Registrar Cita Médica
            </button>

        </FormKit>
        <loading :active="isLoading" :can-cancel="true" :is-full-page="true" />

    </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import axios from 'axios';
import type { AppointmentData } from '../../administrador/pages/AppointmentData';
import type { Medico } from '../../administrador/pages/Medico';
import type { Paciente } from '../../administrador/pages/Paciente';
import especialidades from '../../administrador/pages/Especialidades';
import NotificationModal from '@/modules/common/pages/NotificationModal.vue';
import loading from 'vue-loading-overlay';
import { useRoute } from 'vue-router';


const route = useRoute();
const id = Number(route.query.id);
const notificationMessage = ref('');
const notificationType = ref<'success' | 'error'>('success');
const medicosOriginales = ref<Medico[]>([]);
const pacientes = ref<Paciente[]>([]);
const especialidadSeleccionada = ref<string>(''); // Especialidad seleccionada
const medicos = ref<Medico[]>([]);  // Lista de médicos filtrados
const showNotification = ref(false);
const isLoading = ref(false);


const appointmentData = ref<AppointmentData>({
    fecha: '',
    hora: '',
    id_paciente: 0,
    id_medico: id,
});


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
        appointmentData.value.fecha = ''; // Limpiar el valor si no es válido
    }

    appointmentData.value.fecha = inputDate;

};

const formatHora = (hora: string): string => {
    // Verifica si la hora ya tiene segundos
    if (hora.length === 5) {
        return `${hora}:00`;  // Agrega los segundos
    }
    return hora;  // Si la hora ya tiene segundos, la retorna sin cambios
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

const fetchMedicos = async () => {
    try {
        const response = await axios.get('http://localhost:8080/Medico/ListadoMedico');
        medicosOriginales.value = response.data; // Asegúrate de que el formato del response sea compatible
    } catch (error) {
        console.error('Error al obtener los médicos:', error);
    }
};

const fetchPacientes = async () => {
    try {
        const response = await axios.get('http://localhost:8080/Paciente/ListadoPaciente');
        pacientes.value = response.data; // Asegúrate de que el formato del response sea compatible
    } catch (error) {
        console.error('Error al obtener los pacientes:', error);
    }
};

// Función para registrar la cita
const registerAppointment = async () => {
    isLoading.value = true;
    const citaFormateada = {
        ...appointmentData.value,
        hora: formatHora(appointmentData.value.hora),  // Asegura el formato correcto de la hora
    };

    console.log(citaFormateada)
    try {
        const response = await axios.post('http://localhost:8080/Cita/RegistrarCita', citaFormateada);

        if (response.status === 200) {
            notificationMessage.value = "Cita registrada exitosamente.";
            notificationType.value = 'success';
            // Reinicia los datos del formulario después del registro exitoso
            appointmentData.value = { fecha: '', hora: '', id_paciente: 0, id_medico: 0 };
        }
    } catch (error: any) {
        if (error.response) {
            // El servidor respondió con un código de estado fuera del rango 2xx
            if (error.response.data && error.response.data.message) { notificationMessage.value = error.response.data; }
            else {
                notificationMessage.value = `Error: ${error.response.data} ${error.response.statusText}`;
            }
        } else if (error.request) { // La petición fue hecha pero no hubo respuesta 
            notificationMessage.value = "No se recibió respuesta del servidor. Por favor, intente nuevamente más tarde.";
        }else { // Algo sucedió al configurar la petición que lanzó un error
             notificationMessage.value = "Error al configurar la petición. Por favor, intente nuevamente."; }
             notificationType.value = 'error';
    } finally {
        showNotification.value = true;
        isLoading.value = false;
    }
};


// Llama a la función al montar el componente
onMounted(() => {
    fetchPacientes();
    fetchMedicos();
});
</script>