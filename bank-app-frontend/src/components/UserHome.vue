<template>
    <h1>HOME PAGE</h1>
    <router-link :to="{ path: '/User/Profile' }"><svg-icon type="mdi" :path="accdetail" /> My
                        Profile</router-link>
</template>

<script>
    export default{
        name: 'UserHomePage'
    }
</script>

<style>
</style>