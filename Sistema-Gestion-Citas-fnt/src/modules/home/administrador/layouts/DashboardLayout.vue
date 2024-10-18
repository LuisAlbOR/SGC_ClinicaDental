<template>
  <div class="flex min-h-screen">
    <!-- Sidebar -->
    <aside :class="{ 'hidden': !sidebarVisible, 'block': sidebarVisible }"
      class="w-64 bg-gray-800 text-white flex flex-col">
      <div class="p-4 text-2xl font-semibold">Dashboard Administrador</div>
      <nav class="flex-1 px-4">
        <ul>
          <li class="my-2">
            <RouterLink :to="{ name: 'overviewAdministrador' }" class="block py-2 px-3 rounded hover:bg-gray-700">
              Overview
            </RouterLink>
          </li>
          <li class="my-2">
            <RouterLink :to="{ name: 'citaAdministrador' }" class="block py-2 px-3 rounded hover:bg-gray-700">
              Reports
            </RouterLink>
          </li>
          <li class="my-2">
            <RouterLink :to="{ name: 'configuracionAdministrador' }" class="block py-2 px-3 rounded hover:bg-gray-700">
              Settings
            </RouterLink>
          </li>
          <li class="my-2">
            <div @click="toggleMedicoMenu"
              class="flex items-center justify-between py-2 px-3 rounded cursor-pointer hover:bg-gray-700">
              Medico
              <svg :class="{ 'transform rotate-180': medicoMenuOpen }"
                class="w-5 h-5 transition-transform duration-200">
                <path fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                  d="M6 9l6 6 6-6" />
              </svg>
            </div>
            <ul v-show="medicoMenuOpen" class="pl-6">
              <li class="my-1">
                <RouterLink :to="{ name: 'visualizarMedicosAdministrador' }"
                  class="block py-2 px-3 rounded hover:bg-gray-700">
                  Visualizar medicos
                </RouterLink>
              </li>
            </ul>
            <ul v-show="medicoMenuOpen" class="pl-6">
              <li class="my-1">
                <RouterLink :to="{ name: 'registrarMedicoAdministrador' }"
                  class="block py-2 px-3 rounded hover:bg-gray-700">
                  Registrar medico
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
      <header class="bg-white shadow p-4 flex justify-between items-center">
        <div class="flex items-center">
          <button @click="toggleSidebar" class="text-gray-600 focus:outline-none mr-4">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
              stroke-linejoin="round">
              <path d="M4 6h16M4 12h16M4 18h16"></path>
            </svg>
          </button>
          <h1 class="text-2xl font-semibold"></h1>
        </div>
        <button class="bg-red-500 text-white px-4 py-2 rounded" @click="Logout">Logout</button>
      </header>

      <!-- Content area -->
      <main class="p-4 flex-1">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import router from '@/router';
import { LoadingPlugin } from 'vue-loading-overlay';

const sidebarVisible = ref(true);

const toggleSidebar = () => {
  sidebarVisible.value = !sidebarVisible.value;
};

const Logout = () => {
  router.push('/')
}

// Estado del menú
const medicoMenuOpen = ref(false);

// Método para alternar el estado del menú
const toggleMedicoMenu = () => {
  medicoMenuOpen.value = !medicoMenuOpen.value;
};
</script>