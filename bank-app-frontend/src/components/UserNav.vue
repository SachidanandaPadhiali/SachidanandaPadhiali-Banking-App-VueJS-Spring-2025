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
            <a href="#home" class="nav-link">Home</a>
            <a href="#about" class="nav-link">About</a>
            <a href="#services" class="nav-link">Services</a>
            <a href="#portfolio" class="nav-link">Portfolio</a>
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
                <ul>
                    <li><a href="#home" @click="closeMenu">Home</a></li>
                    <li><a href="#about" @click="closeMenu">About</a></li>
                    <li><a href="#services" @click="closeMenu">Services</a></li>
                    <li><a href="#portfolio" @click="closeMenu">Portfolio</a></li>
                    <li><a href="#contact" @click="closeMenu">Contact</a></li>
                    <li>
                        <router-link :to="{ path: '/User/Profile' }" class="mobile-profile-link" @click="closeMenu">
                            My Profile
                        </router-link>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</template>

<script>
import SvgIcon from '@jamescoyle/vue-icon';
import { mdiAccountCircleOutline } from '@mdi/js';

export default {
    name: 'UserNav',
    components: { SvgIcon },
    data() {
        return { mobileMenuOpen: false, accdetail: mdiAccountCircleOutline };
    },
    methods: {
        toggleMobileMenu() { this.mobileMenuOpen = !this.mobileMenuOpen; },
        closeMenu() { this.mobileMenuOpen = false; }
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
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
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
    .navlogo {
        width: 70%;
    }

    .navlogo img {
        max-height: 70%;
    }
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

.button--animated:hover .button__text {
    transform: translateX(-25px);
}

.button--animated:hover .button__icon {
    right: 20px;
}

/* Mobile Navigation */
@media (min-width: 768px) {

    #mobile-menu-button,
    #mobile-menu {
        display: none;
    }

    .navbar {
        display: flex;
        gap: 1.5rem;
    }
}

#mobile-menu-button {
    color: black;
    background: transparent;
    border: none;
    cursor: pointer;
}

/* Hamburger Lines */
.line {
    display: block;
    width: 25px;
    height: 3px;
    margin: 5px auto;
    background-color: black;
    transition: transform 0.3s ease, opacity 0.2s ease, background-color 0.3s ease;
    transform-origin: center;
}

/* Hamburger Animation - Active State */
#mobile-menu-button.active #line1 {
    transform: rotate(45deg) translate(5px, 5px);
}

#mobile-menu-button.active #line2 {
    opacity: 0;
}

#mobile-menu-button.active #line3 {
    transform: rotate(-45deg) translate(6px, -6px);
}

/* Full screen wrapper */
.user-nav-wrapper {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    visibility: hidden;
    z-index: 999;
    transition: visibility 0s linear 0.7s; /* Delay hiding after transition */
    pointer-events: none;
    opacity: 0;
}

/* Wrapper handles visibility and base transitions */
.user-nav-wrapper {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    opacity: 0;
    visibility: hidden;
    pointer-events: none;
    transition: opacity 0.3s ease, visibility 0.3s ease;
    z-index: 999;
}

/* When menu is open */
.user-nav-wrapper.expanded {
    opacity: 1;
    visibility: visible;
    pointer-events: auto;
}

/* Overlay (fades in/out) */
.user-nav-overlay {
    position: absolute;
    inset: 0;
    background-color: rgba(0, 0, 0, 0.7);
    opacity: 0;
    transition: opacity 0.3s ease;
}

/* Overlay fades in with wrapper */
.user-nav-wrapper.expanded .user-nav-overlay {
    opacity: 1;
}

/* Menu content (slides in from right, then out on close) */
.user-nav-content {
    position: absolute;
    top: 0;
    right: 0;
    width: 80%;
    height: 100%;
    background-color: #fff;
    transform: translateX(100%);
    transition: transform 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    transition-delay: 0.2s;
    display: flex;
    flex-direction: column;
    gap: 5vh;
    padding: 20px;
    padding-bottom: 20%;
}

/* Slide in when expanded */
.user-nav-wrapper.expanded .user-nav-content {
    transform: translateX(0);
    transition-delay: 0.2s; /* Slides in after overlay fades in */
}

/* Slide out before overlay fades out */
.user-nav-wrapper:not(.expanded) .user-nav-content {
    transform: translateX(100%);
    transition-delay: 0s;
}

@media (max-width: 767px) {
    .navbar {
        display: none;
    }

    #mobile-menu-button {
        display: block;
    }

    #mobile-menu {
        display: block;
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

</style>
