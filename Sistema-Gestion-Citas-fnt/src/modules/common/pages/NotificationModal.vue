<template>
  <div v-if="visible" class="notification-overlay" @click="hideNotification">
    <div class="notification-window" @click.stop>
      <h3 :class="['notification-title', type]">{{ title }}</h3>
      <p>{{ message }}</p>
      <button ref="closeButton" @click="hideNotification">Cerrar</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, computed, onMounted, onBeforeUnmount } from 'vue';

const props = defineProps({
  message: String,
  type: String,
  visible: Boolean,
});

const emit = defineEmits(['update:visible']);

const closeButton = ref<HTMLButtonElement | null>(null);

const title = computed(() => {
  return props.type === 'success' ? 'Éxito' : 'Error';
});

const hideNotification = () => {
  emit('update:visible', false);
};

const handleKeyDown = (event: KeyboardEvent) => {
  if (event.key === 'Escape') {
    hideNotification();
  }
};

watch(
  () => props.visible,
  (newValue) => {
    if (newValue && closeButton.value) {
      closeButton.value.focus();
    }
  }
);

onMounted(() => {
  document.addEventListener('keydown', handleKeyDown);
});

onBeforeUnmount(() => {
  document.removeEventListener('keydown', handleKeyDown);
});
</script>

<style scoped>
.notification-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.notification-window {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.notification-title {
  font-size: 1.5em;
  margin-bottom: 10px;
}

.notification-title.success {
  color: green;
}

.notification-title.error {
  color: red;
}

button {
  margin-top: 10px;
  padding: 10px;
  border: none;
  border-radius: 5px;
  background-color: #007BFF;
  color: white;
  cursor: pointer;
  display: block;
  margin-left: auto;
  margin-right: auto;
}

button:hover {
  background-color: #0056b3;
}
</style>
