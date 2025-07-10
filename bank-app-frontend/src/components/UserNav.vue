<template>
    <header class="navContainer">

        <!-- Mobile Menu Toggle Button -->
        <button id="mobile-menu-button" :class="{ active: mobileMenuOpen }" @click="toggleMobileMenu">
            <span id="line1" class="line"></span>
            <span id="line2" class="line"></span>
            <span id="line3" class="line"></span>
        </button>
        <!-- Logo -->
        <div class="navlogo">
            <img src="../assets/img/applogo.png" alt="App Logo" />
        </div>

        <!-- Desktop Navigation -->
        <nav class="navbar">
            <router-link :to="{ path: '/User/Home' }" class="nav-link">Home</router-link>
            <a href="#about" class="nav-link">Deposits</a>
            <a href="#services" class="nav-link">Cards</a>
            <a href="#portfolio" class="nav-link">Borrow</a>
            <a href="#contact" class="nav-link">Contact</a>
        </nav>

        <!-- Profile Button (shared between desktop and mobile) -->
        <router-link :to="{ path: '/User/Profile' }" class="probutton button--animated">
            <span class="button__text">My Profile</span>
            <span class="button__icon">
                <svg-icon type="mdi" :path="accdetail" class="icon" />
            </span>
        </router-link>
    </header>

    <!-- Mobile Navigation Menu -->
    <div class="user-nav-wrapper" :class="{ expanded: mobileMenuOpen }">
        <div class="user-nav-overlay" @click.self="toggleMobileMenu">
            <div class="user-nav-content">
                <div class="logo-box">
                    <router-link :to="{ path: '/User/Home' }" class="logo">
                        <img style="height: 50px; padding:0px; margin: 0px;" src="../assets/img/logoblack.png"
                            alt="Logo" />
                    </router-link>
                </div>
                <ul id="menu">
                    <li>
                        <h5>Welcome {{ firstName }}</h5>
                    </li>
                    <li><a href="#home" @click="closeMenu">Home</a></li>
                    <li><a href="#about" @click="closeMenu">Deposits</a></li>
                    <li><a href="#services" @click="closeMenu">Cards</a></li>
                    <li><a href="#portfolio" @click="closeMenu">Borrow</a></li>
                    <li><a href="#contact" @click="closeMenu">Contact</a></li>
                </ul>
                <router-link :to="{ path: '/' }" style="color: var(--primary);">Sign Out<svg-icon type="mdi"
                        :path="logouticon" /></router-link>
            </div>
        </div>
    </div>
</template>

<script>
import SvgIcon from '@jamescoyle/vue-icon';
import { mdiAccountCircleOutline, mdiLogout } from '@mdi/js';

export default {
    name: 'UserNav',
    components: { SvgIcon },
    data() {
        return { mobileMenuOpen: false, accdetail: mdiAccountCircleOutline, logouticon: mdiLogout, firstName: '' };
    },
    mounted() {
        const storedData = localStorage.getItem("user-login-info");
        if (storedData) {
            try {
                const userData = JSON.parse(storedData);
                this.firstName = userData.firstName;
            } catch (error) {
                console.error("Error!! parsing user data:", error);
            }
        } else {
            console.log("No user data found.");
        }
    },
    methods: {
        toggleMobileMenu() { this.mobileMenuOpen = !this.mobileMenuOpen; },
        closeMenu() { this.mobileMenuOpen = false; },
        logout() {
            localStorage.removeItem("user-login-info");
            this.$router.push("/");
        }
    }
}
</script>

<style>
.navContainer {
    height: 60px;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    padding: 10px;
    border-bottom: 1px solid rgba(6, 182, 212, 0.3);
    opacity: 0.85;
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    z-index: 999;
}

/* Navigation Bar Styles */
.navbar {
    z-index: 50;
    transition: all 0.3s ease;
    display: flex;
    align-items: center;
    gap: 20px;
}

.navbar a {
    color: var(--primary);
    text-decoration: none;
    font-size: var(--h3-font-size);
    font-weight: var(--font-semi-bold);
}

/*Logo*/
.navlogo a {
    display: flex;
    align-items: center;
    text-decoration: none;
    gap: 1px;
    /* Adds space between image and text */
}

.navlogo img {
    height: 40px;
}

@media (max-width: 600px) {
    .navlogo img {
        max-height: 70%;
    }
}

.probutton {
    padding: 5px 25px;
    font-size: 16px;
    font-weight: 600;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.3s ease;
}

.button--animated {
    background-color: var(--primary);
    color: var(--white-text);
    overflow: hidden;
    position: relative;
}

.button--animated .button__text {
    display: inline-block;
    transition: transform 0.3s ease;
}

.button--animated .button__icon {
    position: absolute;
    right: -40px;
    transition: right 0.3s ease;
}

.button--animated:hover {
    background: none;
}

.button--animated:hover .button__text {
    transform: translateX(-25px);
}

.button--animated:hover .button__icon {
    right: 20px;
}

/* Mobile Navigation */
@media (min-width: 768px) {

    .navbar {
        display: flex;
        gap: 1.5rem;
    }
}

.user-nav-wrapper {
    position: fixed;
    inset: 0;
    z-index: 999;
    visibility: hidden;
    width: 100vw;
    height: 100vh;
    visibility: hidden;
    transform: translateX(100%);
    transition: transform 0.5s ease, visibility 0.5s ease;
}

.user-nav-wrapper.expanded {
    visibility: visible;
    transform: translateX(0);
}

.user-nav-overlay {
    position: absolute;
    top: 0;
    right: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.7);
}

.user-nav-content {
    background-color: #fff;
    width: 85%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    transform: translateX(20%);
    transition: transform 1s ease-in-out;
    padding: 20px;
    padding-bottom: 20%;
}

/* Slide out before overlay fades out */
.user-nav-wrapper:not(.expanded) .user-nav-content {
    transform: translateX(100%);
    transition-delay: 0s;
}

@media (max-width: 900px) {
    .navbar {
        display: none;
    }
    .navContainer {
        gap: 5%;
        justify-content: space-between;
    }

    #navbar {
        height: auto;
    }

    .nav-link::after {
        display: none;
    }

    .probutton {
        padding: 3px 10px;
        border-radius: 20px;
    }
}
</style>
