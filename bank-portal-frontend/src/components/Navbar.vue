<template>
  <nav class="p-4 flex justify-between items-center bg-gradient-to-r from-purple-600 to-blue-600 text-white">
    <div class="text-xl font-bold">BankPortal</div>
    <div class="flex gap-4 items-center">
      <router-link to="/login" class="hover:underline">Login</router-link>
      <router-link to="/signup" class="hover:underline">Sign Up</router-link>
      <button @click="toggleDarkMode" class="text-white">
        <span v-if="darkMode">☀️</span>
        <span v-else>🌙</span>
      </button>
    </div>
  </nav>
</template>

<script>
export default {
  props: ['toggleDarkMode', 'darkMode']
};
</script>