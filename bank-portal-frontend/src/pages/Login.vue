<template>
  <div class="flex flex-col items-center justify-center min-h-[80vh]">
    <h2 class="text-2xl mb-4">Login</h2>
    <input v-model="username" placeholder="Username" class="input" />
    <input v-model="password" type="password" placeholder="Password" class="input" />
    <button @click="login" class="btn">Login</button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      username: '',
      password: '',
    };
  },
  methods: {
    async login() {
      const res = await fetch('http://localhost:8088/api/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(this.$data),
      });
      if (res.ok) {
        const data = await res.json();
        this.$router.push({ path: '/', state: { user: data } });
      } else {
        alert('Invalid credentials');
      }
    },
  },
};
</script>

<style scoped>
.input {
  @apply p-2 mb-2 border rounded w-64;
}
.btn {
  @apply px-4 py-2 bg-blue-600 text-white rounded;
}
</style>

