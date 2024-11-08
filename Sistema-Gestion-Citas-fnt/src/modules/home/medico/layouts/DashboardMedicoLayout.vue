<template>
  <div :class="isDarkMode ? 'dark bg-gray-900 text-white' : 'bg-white text-black'" class="flex min-h-screen">
    <!-- Sidebar -->
    <aside :class="sidebarVisible ? 'block' : 'hidden'" class="w-64 bg-gray-800 text-white flex flex-col">
      <div class="p-4 text-2xl font-semibold">Dashboard Administrador</div>
      <nav class="flex-1 px-4">
        <ul>


          <li class="my-2">
            <div @click="toggleCitaMenu"
              class="flex items-center justify-between py-2 px-3 rounded cursor-pointer hover:bg-gray-700">
              Cita
              <svg :class="{ 'transform rotate-180': citaMenuOpen }"
                class="w-5 h-5 transition-transform duration-300">
                <path fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                  d="M6 9l6 6 6-6" />
              </svg>
            </div>
            <ul v-show="citaMenuOpen" class="pl-6">
              <li class="my-1">
                <RouterLink :to="{ name: 'visualizarCitaMedico', query: { id: id } }"
                  class="block py-2 px-3 rounded hover:bg-gray-700">
                  Visualizar citas
                </RouterLink>
              </li>
            </ul>
            <ul v-show="citaMenuOpen" class="pl-6">
              <li class="my-1">
                <RouterLink :to="{ name: 'registrarCitaMedico' , query: {id: id}}"
                  class="block py-2 px-3 rounded hover:bg-gray-700">
                  Registrar cita
                </RouterLink>
              </li>
            </ul>
          </li>

        </ul>
      </nav>
      <footer class="p-4 bg-gray-900 text-center">
        <span>© 2024 Clínica Santa María</span>
      </footer>
    </aside>

    <!-- Main content -->
    <div class="flex-1 flex flex-col">
      <!-- Header -->
      <header :class="isDarkMode ? 'bg-gray-800 text-white' : 'bg-white text-gray-700'"
        class="shadow p-4 flex justify-between items-center">
        <div class="flex items-center">
          <button @click="toggleSidebar" class="focus:outline-none mr-4">
            <svg class="w-6 h-6" fill="none" :stroke="isDarkMode ? 'white' : 'currentColor'" stroke-width="2"
              stroke-linecap="round" stroke-linejoin="round">
              <path d="M4 6h16M4 12h16M4 18h16"></path>
            </svg>
          </button>
          <h1 class="text-2xl font-semibold">Dashboard</h1>
        </div>

        <div class="flex items-center space-x-4">

          <!-- Botón de cambio de tema -->
          <button @click="toggleTheme" @blur="saveTheme" class="focus:outline-none">
            <img :src="isDarkMode ? '/public/ThemeIcon.svg' : '/public/ThemeIcon.svg'" alt="Theme Icon"
              :class="isDarkMode ? 'w-6 h-6 transform rotate-180 bg-white rounded-full' : 'w-6 h-6'" />
          </button>


          <!-- Nombre del usuario -->
          <span :class="isDarkMode ? 'text-white' : 'text-gray-700'" class="font-medium">
            Hola, {{ userName }}
          </span>

          <!-- Botón de logout -->
          <button class="bg-red-500 text-white px-4 py-2 rounded" @click="Logout">Logout</button>
        </div>
      </header>

      <!-- Content area -->
      <main class="p-4 flex-1">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import router from '@/router';
import { useRoute } from 'vue-router';

const sidebarVisible = ref(true);

const toggleSidebar = () => {
  sidebarVisible.value = !sidebarVisible.value;
};

const Logout = () => {
  router.push('/');
};

// Estado del menú
const medicoMenuOpen = ref(false);
const administradorMenuOpen = ref(false);
const pacienteMenuOpen = ref(false);
const citaMenuOpen = ref(false);

// Método para alternar el estado del menú
const toggleMedicoMenu = () => {
  medicoMenuOpen.value = !medicoMenuOpen.value;
};

// Método para alternar el estado del menú
const toggleAdministradorMenu = () => {
  administradorMenuOpen.value = !administradorMenuOpen.value;
};

//Método para alternar el estado de menú
const togglePacienteMenu = () => {
  pacienteMenuOpen.value = !pacienteMenuOpen.value;
}

//Método para alternar el estado del menú
const toggleCitaMenu= () =>{
  citaMenuOpen.value = !citaMenuOpen.value;
}

const route = useRoute();
const userName = route.query.userName as string; // Esto extrae el userName de la query
const id = Number(route.query.id); // Convierte a número

const goToProfile = () => {
  console.log('Ir al perfil del usuario');
};

// Estado para el modo oscuro
const isDarkMode = ref(false);

// Método para alternar el tema
const toggleTheme = () => {
  isDarkMode.value = !isDarkMode.value;
  if (isDarkMode.value) {
    document.documentElement.classList.add('dark');
  } else {
    document.documentElement.classList.remove('dark');
  }
};

// Persistencia del modo seleccionado
onMounted(() => {
  const savedTheme = localStorage.getItem('theme');
  if (savedTheme === 'dark') {
    isDarkMode.value = true;
    document.documentElement.classList.add('dark');
  }
});

// Guardar el tema en localStorage
const saveTheme = () => {
  localStorage.setItem('theme', isDarkMode.value ? 'dark' : 'light');
};

</script>