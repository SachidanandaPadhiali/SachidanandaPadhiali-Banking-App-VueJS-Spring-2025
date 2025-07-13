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
            <nav class="navbar">
                <ul class="menu-list">
                    <li class="nav-link">
                        <router-link to="/User/Home">Home</router-link>
                    </li>

                    <li class="nav-link has-submenu">
                        <span>Deposits</span>
                        <ul class="submenu">
                            <li><router-link to="/fdDeposit">Fixed Deposits</router-link></li>
                            <li><router-link to="/rdDeposit">Recurring Deposits</router-link></li>
                            <li><router-link to="/interestRates">Interest rates</router-link></li>
                        </ul>
                    </li>

                    <li class="nav-link has-submenu">
                        <span>Cards</span>
                        <ul class="submenu">
                            <li><router-link to="/creditCard">Credit Cards</router-link></li>
                            <li><router-link to="/debitCard">Debit Cards</router-link></li>
                        </ul>
                    </li>

                    <li class="nav-link">
                        <router-link to="/apply">Borrow</router-link>
                    </li>

                    <li class="nav-link">
                        <router-link to="/contact">Contact</router-link>
                    </li>
                </ul>
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

/* Desktop nav list */
.navbar .menu-list {
    display: flex;
    justify-content: center;
    gap: 2rem;
    list-style: none;
    margin: 0;
    padding: 0;
    color: var(--primary);
    font-weight: var(--font-medium);
}

.navbar .nav-link {
    position: relative;
}

.navbar .nav-link>a,
.navbar .nav-link>span {
    color: var(--primary);
    padding: 0.5rem;
    cursor: pointer;
    font-weight: 500;
}

.navbar .nav-link:hover>a,
.navbar .nav-link:hover>span {
    color: var(--accent);
    text-decoration: none;
}

/* Submenu (hidden by default) */
.navbar .has-submenu .submenu {
    margin: 0;
    padding: 0;
    list-style-type: none;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
}

.navbar .has-submenu .submenu {
    display: none;
    position: absolute;
    top: 100%;
    min-width: 10vw;
    border-radius: 20px;
    background-color: var(--transparent);
    color: var(--primary);
    box-shadow: 2px 2px 5px 1px rgba(0, 0, 0, 0.05), -2px 0px 5px 1px rgba(0, 0, 0, 0.05);
    z-index: 99;
}
/*====== MENNU ITEMS HOVER ====== */
.navbar .has-submenu:hover .submenu {
    display: block;
}

.navbar .submenu li {
    padding: 0;
}

.navbar .submenu li a {
    display: block;
    text-decoration: none;
}

.navbar .submenu li a:hover {
    color: var(--primary);
}

/*====== DROPDOWN STYLING ======*/
.navbar .menu-list>li>ul>li {
    position: relative;
    width: 100%;
    padding-left: 20px;
}

.navbar .menu-list>li>ul>li>a {
    position: relative;
    display: flex;
    font-size: 17px;
    color: var(--primary);
    font-weight: var(--font-medium);
    padding: 10px 0px;
    border-bottom: 1px solid var(--border);
    z-index: 1;
}

.navbar .menu-list>li>ul>li:last-child>a {
    border-bottom: none;
}

.navbar .menu-list>li>ul>li:hover>a {
    background-color: var(--transparent-accent);
    color: var(--primary);
}

.navbar .menu-list>li>ul>li.dropdown>a:before {
    position: absolute;
    top: 0px;
    right: 25px;
    display: block;
    font-size: 20px;
    line-height: 50px;
    font-weight: 400;
    z-index: 5;
    transition: all 500ms ease;
}

/*Logo*/
.navlogo a {
    display: flex;
    align-items: center;
    text-decoration: none;
    gap: 1px;
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
