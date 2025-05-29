<template>
  <div :class="darkMode ? 'dark bg-gray-900 text-white' : 'bg-white text-black'">
    <Navbar :toggleDarkMode="toggleDarkMode" :darkMode="darkMode" />
    <router-view />
    <Footer />
  </div>
</template>

<script>
import Navbar from './components/Navbar.vue';
import Footer from './components/Footer.vue';

export default {
  components: { Navbar, Footer },
  data() {
    return {
      darkMode: false,
    };
  },
  methods: {
    toggleDarkMode() {
      this.darkMode = !this.darkMode;
    },
  },
};
</script>

<style>
body.dark {
  background-color: #1f2937;
  color: white;
}
</style>
