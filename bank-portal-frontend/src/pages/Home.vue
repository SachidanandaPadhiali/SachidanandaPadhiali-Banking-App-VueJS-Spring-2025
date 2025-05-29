<template>
  <div class="flex flex-col items-center justify-center min-h-[80vh]">
    <h1 class="text-3xl mb-4">Welcome</h1>
    <p class="text-xl">Account Balance: ${{ accountBalance }}</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      accountBalance: 0,
    };
  },
  created() {
    // Example static data; replace with user data fetch logic
    this.accountBalance = 4200;
  },
};
</script>