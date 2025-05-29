<template>
  <div class="flex flex-col items-center justify-center min-h-[80vh]">
    <h2 class="text-2xl mb-4">Create Account</h2>
    <input v-model="username" placeholder="Username" class="input" />
    <input v-model="password" type="password" placeholder="Password" class="input" />
    <input v-model="initialBalance" type="number" placeholder="Initial Balance" class="input" />
    <button @click="signup" class="btn">Sign Up</button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      username: '',
      password: '',
      initialBalance: 0
    };
  },
  methods: {
    async signup() {
      const res = await fetch('http://localhost:8088/api/user', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(this.$data)
      });
      if (res.ok) {
        this.$router.push('/login');
      } else {
        alert('Signup failed');
      }
    }
  }
};
</script>

<style scoped>
.input {
  @apply p-2 mb-2 border rounded w-64;
}
.btn {
  @apply px-4 py-2 bg-purple-600 text-white rounded;
}
</style>

