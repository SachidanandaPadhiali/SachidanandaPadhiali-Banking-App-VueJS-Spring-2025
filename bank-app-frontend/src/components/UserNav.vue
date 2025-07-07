<template>
    <div class="navContainer">
        <img class="navlogo" src="../assets/img/applogo.png">
        <div class="navbar">
            <a href="#home" class="nav-link">Home</a>
            <a href="#about" class="nav-link">About</a>
            <a href="#services" class="nav-link">Services</a>
            <a href="#portfolio" class="nav-link">Portfolio</a>
            <a href="#contact" class="nav-link">Contact</a>
        </div>
        <router-link :to="{ path: '/User/Profile' }" class="probutton button--animated">
            <span class="button__text">My Profile</span>
            <span class="button__icon"><svg-icon type="mdi" :path="accdetail" class="icon" /></span>
        </router-link>
    </div>
</template>

<script>
import SvgIcon from '@jamescoyle/vue-icon';
import { mdiAccount } from '@mdi/js';

export default {
    name: 'UserNav',

    data() {
        return {
            mobileMenuOpen: false,
            accdetail: mdiAccount
        };
    },

    components: { SvgIcon },

    mounted() {
        const mobileMenuBtn = document.getElementById('mobile-menu-button');
        if (mobileMenuBtn) {
            mobileMenuBtn.addEventListener('click', this.toggleMobileMenu);
        }
    },

    methods: {
        toggleMobileMenu() {
            const mobileMenu = document.getElementById('mobile-menu');
            const mobileMenuButton = document.getElementById('mobile-menu-button');

            if (!mobileMenu || !mobileMenuButton) return;

            mobileMenuButton.classList.toggle('active');

            if (mobileMenu.classList.contains('open')) {
                mobileMenu.style.height = '0';
                mobileMenu.classList.remove('open');
            } else {
                mobileMenu.classList.add('open');
                mobileMenu.style.height = `${mobileMenu.scrollHeight}px`;
            }

            this.mobileMenuOpen = mobileMenu.classList.contains('open');
        }
    }
}
</script>

<style>
.navContainer {
    top: 0;
    left: 0;
    width: 100%;
    max-width: 1280px;
    height: 60px;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    padding: 10px;
    border-bottom: 1px solid rgba(6, 182, 212, 0.3);
}

/* Custom Scrollbar */
::-webkit-scrollbar {
    width: 8px;
}

::-webkit-scrollbar-track {
    background: var(--dark-bg);
}

::-webkit-scrollbar-thumb {
    background: linear-gradient(to bottom, var(--primary), var(--accent));
    border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
    background: linear-gradient(to bottom, var(--primary), var(--accent));
}

/* Navigation Bar Styles */
.navbar {
    position: relative;
    top: 0;
    z-index: 50;
    transition: all 0.3s ease;
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    width: calc(70%);
}

.navbar a {
    color: var(--primary);
}

/* Navbar Logo */
.navlogo {
    padding-top: 5px;
    height: 40px;
    width: auto;
}

/* Navigation Links */
.nav-link {
    position: relative;
}

.nav-link::after {
    content: "";
    position: absolute;
    bottom: 0;
    left: 50%;
    width: 0;
    height: 2px;
    background: linear-gradient(to right, var(--primary), var(--accent));
    transition: width 0.3s ease;
    transform: translateX(-50%);
}

.nav-link:hover::after,
.nav-link.active::after {
    width: 80%;
}

.nav-link:hover {
    color: var(--accent);
    text-shadow: 0 0 8px rgba(6, 182, 212, 0.3);
}


.probutton {
    padding: 5px 24px;
    font-size: 16px;
    font-weight: 600;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.3s ease;
}

.button--animated {
    background-color: var(--primary-color);
    color: var(--primary);
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

.button--animated:hover .button__text {
    transform: translateX(-25px);
}

.button--animated:hover .button__icon {
    right: 20px;
}

/* Mobile Navigation */
#mobile-menu {
    max-height: 0;
    overflow: hidden;
    transition: max-height 0.5s ease;
    transition: height 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

#mobile-menu.open {
    max-height: 300px;
    /* Adjust based on your menu height */
}

#mobile-menu-button {
    color: black;
    background: transparent;
    border: none;
    cursor: pointer;
}

/* For the hamburger animation */
#mobile-menu-button.active #line1 {
    transform: translate(-50%, -50%) rotate(45deg);
    transform-origin: center;
}

#mobile-menu-button.active #line2 {
    opacity: 0;
}

#mobile-menu-button.active #line3 {
    transform: translate(-50%, -50%) rotate(-45deg);
    transform-origin: center;
}

#line1,
#line2,
#line3 {
    transition: transform 0.3s ease, opacity 0.2s ease, background-color 0.3s ease;
    transform-origin: center;
}

/* Gradient Text */
.gradient-text {
    background: linear-gradient(to right,
            var(--primary),
            var(--hover),
            var(--accent));
    -webkit-background-clip: text;
    background-clip: text;
    color: transparent;
}

/* Glow Effects */
.glow-effect {
    position: relative;
}

.glow-effect:before {
    content: "";
    position: absolute;
    inset: -5px;
    background: linear-gradient(to right, var(--primary), var(--accent));
    border-radius: inherit;
    opacity: 0;
    transition: opacity 0.3s ease;
    z-index: -1;
    filter: blur(8px);
}

.glow-effect:hover:before {
    opacity: 1;
}

/* Animations */
@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(20px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.animate-fadeIn {
    animation: fadeIn 0.8s ease forwards;
}

@keyframes pulse {
    0% {
        opacity: 0;
    }

    50% {
        opacity: 1;
    }

    100% {
        opacity: 0;
    }
}

.animate-pulse {
    animation: pulse 2s infinite;
}

/* Responsive Layout */
@media (min-width: 640px) {
    .sm\:grid-cols-2 {
        grid-template-columns: repeat(2, 1fr);
    }
}

@media (min-width: 768px) {
    .md\:grid-cols-2 {
        grid-template-columns: repeat(2, 1fr);
    }

    .md\:grid-cols-3 {
        grid-template-columns: repeat(3, 1fr);
    }

    /* Show desktop nav, hide mobile nav */
    .mobile-only {
        display: none;
    }
}

@media (max-width: 767px) {
    .desktop-only {
        display: none;
    }

    section {
        padding: 4rem 0;
    }

    .container {
        padding: 0 1rem;
    }

    #navbar {
        height: auto;
    }

    .nav-link::after {
        display: none;
    }
}

/* Dark Overlay for Modals */
.overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.8);
    backdrop-filter: blur(5px);
    z-index: 1000;
    display: flex;
    align-items: center;
    justify-content: center;
    opacity: 0;
    pointer-events: none;
    transition: opacity 0.3s ease;
}

.overlay.active {
    opacity: 1;
    pointer-events: auto;
}
</style>
