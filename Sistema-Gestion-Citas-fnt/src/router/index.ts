import { createRouter, createWebHistory } from "vue-router";
import NotFound from "@/modules/common/pages/NoFound.vue";
import path from "path";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      redirect: "/auth", // Redirige automáticamente a /auth al cargar la app
    },
    //Auth
    {
      path: "/auth",
      redirect: { name: "login" }, //'/login'
      component: () => import("@/modules/auth/layouts/AuthLayout.vue"),
      children: [
        {
          path: "login",
          name: "login",
          component: () => import("@/modules/auth/pages/LoginPage.vue"),
        },
        {
          path: "register",
          name: "register",
          component: () => import("@/modules/auth/pages/RegisterPage.vue"),
        },
        {
          path: "email",
          name: "email",
          component: () => import("@/modules/auth/pages/ForgotPassword.vue"),
        },
      ],
    },

    //Dashboard del paciente
    {
      path: "/paciente",
      name: 'paciente',
      redirect: {name: "overviewPaciente"},
      component: () => import("@/modules/home/paciente/layouts/DashboardPacienteLayout.vue"),
      children: [
        {
          path: "overviewPaciente",
          name: "overviewPaciente",
          component: () => import("@/modules/home/paciente/pages/OverviewPage.vue"),
        },
        {
          path: "visualizarCitaPaciente",
          name: "visualizarCitaPaciente",
          component: () => import('@/modules/home/paciente/pages/VisualizarCitaPacientePage.vue'),
        },
        {
          path: "registrarCitaPaciente",
          name: "registrarCitaPaciente",
          component: () => import('@/modules/home/paciente/pages/RegistrarCitaPacientePage.vue'),
        },

      ],
    },
    

    //Dashboard del medico
    {
      path: "/medico",
      name: 'medico',
      redirect: {name: "overviewMedico"},
      component: () => import("@/modules/home/medico/layouts/DashboardMedicoLayout.vue"),
      children: [
        {
          path: "overviewMedico",
          name: "overviewMedico",
          component: () => import("@/modules/home/medico/pages/OverviewPage.vue"),
        },
        {
          path: "visualizarCitaMedico",
          name: "visualizarCitaMedico",
          component: () => import('@/modules/home/medico/pages/VisualizarCitaMedicoPage.vue'),
        },
        {
          path: "registrarCitaMedico",
          name: "registrarCitaMedico",
          component: () => import('@/modules/home/medico/pages/RegistrarCitaMedicoPage.vue'),
        },
      ],
    },

    //Dashboard del administrador
    {
      path: "/administrador",
      name: 'administrador',
      redirect: {name: "overviewAdministrador"},
      component: () => import("@/modules/home/administrador/layouts/DashboardLayout.vue"),
      children: [
        {
          path: "overviewAdministrador",
          name: "overviewAdministrador",
          component: () => import("@/modules/home/administrador/pages/OverviewPage.vue"),
        },
        {
          path: "configuracionAdministrador",
          name: "configuracionAdministrador",
          component: () => import('@/modules/home/administrador/pages/ConfiguracionPage.vue'),
        },
        {
          path: "visualizarMedicosAdministrador",
          name: "visualizarMedicosAdministrador",
          component: () => import('@/modules/home/administrador/pages/VisualizarMedicosAdministradorPage.vue'),
        },
        {
          path: "registrarMedicoAdministrador",
          name: "registrarMedicoAdministrador",
          component: () => import('@/modules/home/administrador/pages/RegistrarMedicoAdministradorPage.vue'),
        },
        {
          path: "visualizarAdministradoresAdministrador",
          name: "visualizarAdministradoresAdministrador",
          component: () => import('@/modules/home/administrador/pages/VisualizarAdministradoresAdministradorPage.vue'),
        },
        {
          path: "registrarAdministradoAdministrador",
          name: "registrarAdministradorAdministrador",
          component: () => import('@/modules/home/administrador/pages/RegistrarAdministradorAdministradorPage.vue')
        },
        {
          path: "visualizarPacientesAdministrador",
          name: "visualizarPacientesAdminsitrador",
          component: () => import('@/modules/home/administrador/pages/VisualizarPacientesAdministradorPage.vue')
        },
        {
          path: "registrarPacienteAdministrador",
          name: "registrarPacienteAdministrador",
          component: () => import('@/modules/home/administrador/pages/RegistrarPacienteAdministradorPage.vue')
        },
        {
          path: "registrarCitaAdministrador",
          name: "registrarCitaAdministrador",
          component: () => import('@/modules/home/administrador/pages/RegistrarCitaAdministradorPage.vue')
        },
        {
          path: "visualizarCitaAdministrador",
          name: "visualizarCitaAdministrador",
          component: () => import('@/modules/home/administrador/pages/VisualizarCitaAdministradorPage.vue')
        }
      ],
    },

    // Not Found
    {
      path: "/:pathMatch(.*)*",
      // redirect: '/',
      component: NotFound,
    },
  ],
});

export default router;
