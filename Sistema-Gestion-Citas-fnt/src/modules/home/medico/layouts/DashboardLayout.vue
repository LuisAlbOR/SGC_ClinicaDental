<template>
    <div class="flex min-h-screen">
      <!-- Sidebar -->
      <aside :class="{'hidden': !sidebarVisible, 'block': sidebarVisible}" class="w-64 bg-gray-800 text-white flex flex-col">
        <div class="p-4 text-2xl font-semibold">Dashboard Medico</div>
        <nav class="flex-1 px-4">
          <ul>
            <li class="my-2">
              <RouterLink :to="{name: 'overviewMedico'}" class="block py-2 px-3 rounded hover:bg-gray-700">
                Overview
              </RouterLink>
            </li>
            <li class="my-2">
              <RouterLink :to="{name: 'citaMedico'}" class="block py-2 px-3 rounded hover:bg-gray-700">
                Reports
              </RouterLink>
            </li>
            <li class="my-2">
              <RouterLink :to="{name: 'configuracionMedico'}" class="block py-2 px-3 rounded hover:bg-gray-700">
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

  const Logout = () =>{
    router.push('/')
  }
  </script>  