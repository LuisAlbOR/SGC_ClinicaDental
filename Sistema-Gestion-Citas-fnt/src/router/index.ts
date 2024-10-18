import { createRouter, createWebHistory } from "vue-router";
import NotFound from "@/modules/common/pages/NoFound.vue";

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
      component: () => import("@/modules/home/paciente/layouts/DashboardLayout.vue"),
      children: [
        {
          path: "overviewPaciente",
          name: "overviewPaciente",
          component: () => import("@/modules/home/paciente/pages/OverviewPage.vue"),
        },
        {
          path: "citaPaciente",
          name: "citaPaciente",
          component: () => import('@/modules/home/paciente/pages/CitaPage.vue'),
        },
        {
          path: "configuracionPaciente",
          name: "configuracionPaciente",
          component: () => import('@/modules/home/paciente/pages/ConfiguracionPage.vue'),
        },
      ],
    },
    

    //Dashboard del medico
    {
      path: "/medico",
      name: 'medico',
      redirect: {name: "overviewMedico"},
      component: () => import("@/modules/home/medico/layouts/DashboardLayout.vue"),
      children: [
        {
          path: "overviewMedico",
          name: "overviewMedico",
          component: () => import("@/modules/home/medico/pages/OverviewPage.vue"),
        },
        {
          path: "citaMedico",
          name: "citaMedico",
          component: () => import('@/modules/home/medico/pages/CitaPage.vue'),
        },
        {
          path: "configuracionMedico",
          name: "configuracionMedico",
          component: () => import('@/modules/home/medico/pages/ConfiguracionPage.vue'),
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
          path: "citaAdministrador",
          name: "citaAdministrador",
          component: () => import('@/modules/home/administrador/pages/CitaPage.vue'),
        },
        {
          path: "configuracionAdministrador",
          name: "configuracionAdministrador",
          component: () => import('@/modules/home/administrador/pages/ConfiguracionPage.vue'),
        },
        {
          path: "visualizarMedicosAdministrador",
          name: "visualizarMedicosAdministrador",
          component: () => import('@/modules/home/administrador/pages/VisualizarMedicosPage.vue'),
        },
        {
          path: "registrarMedicoAdministrador",
          name: "registrarMedicoAdministrador",
          component: () => import('@/modules/home/administrador/pages/RegistrarMedicoPage.vue'),
        },
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
