<template>
  <div :class="isDarkMode ? 'dark bg-gray-900 text-white' : 'bg-white text-black'" class="flex min-h-screen">
    <!-- Sidebar -->
    <aside :class="sidebarVisible ? 'block' : 'hidden'" class="w-64 bg-gray-800 text-white flex flex-col">
      <div class="p-4 text-2xl font-semibold">Dashboard Administrador</div>
      <nav class="flex-1 px-4">
        <ul>
          <li class="my-2">
            <RouterLink :to="{ name: 'overviewMedico' }" class="block py-2 px-3 rounded hover:bg-gray-700">
              Overview
            </RouterLink>
          </li>
          <li class="my-2">
            <RouterLink :to="{ name: 'citaMedico' }" class="block py-2 px-3 rounded hover:bg-gray-700">
              Reports
            </RouterLink>
          </li>
          <li class="my-2">
            <RouterLink :to="{ name: 'configuracionMedico' }" class="block py-2 px-3 rounded hover:bg-gray-700">
              Settings
            </RouterLink>
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
          <!-- Barra de búsqueda -->
          <input type="text" placeholder="Buscar..."
            :class="isDarkMode ? 'bg-gray-700 text-white border-gray-600' : 'bg-white border-gray-300 text-gray-700'"
            class="border rounded px-4 py-2 focus:outline-none focus:ring focus:border-blue-300" />

          <!-- Botón de cambio de tema -->
          <button @click="toggleTheme" @blur="saveTheme" class="focus:outline-none">
            <svg v-if="isDarkMode" class="w-6 h-6" fill="none" stroke="white" stroke-width="2" stroke-linecap="round"
              stroke-linejoin="round">
              <path
                d="M12 3v1m0 16v1m8.485-8.485h1M4.515 12.515h1m13.93-5.657l-.707-.707M6.343 18.243l-.707-.707m12.728 0l-.707-.707M6.343 5.657l-.707-.707M12 5.293a7 7 0 100 14 7 7 0 000-14z">
              </path>
            </svg>
            <svg v-else class="w-6 h-6" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
              stroke-linejoin="round">
              <path
                d="M12 3v1m0 16v1m8.485-8.485h1M4.515 12.515h1m13.93-5.657l-.707-.707M6.343 18.243l-.707-.707m12.728 0l-.707-.707M6.343 5.657l-.707-.707">
              </path>
            </svg>
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

const sidebarVisible = ref(true);

const toggleSidebar = () => {
  sidebarVisible.value = !sidebarVisible.value;
};

const Logout = () => {
  router.push('/');
};

// Estado del menú
const medicoMenuOpen = ref(false);

// Método para alternar el estado del menú
const toggleMedicoMenu = () => {
  medicoMenuOpen.value = !medicoMenuOpen.value;
};

const userName = ref('Juan Pérez');

// Cambio de idioma
const changeLanguage = (event) => {
  const selectedLanguage = event.target.value;
  console.log('Idioma cambiado a:', selectedLanguage);
};

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