<template>
    <div class="main-header-style_top">
        <div class="inner-content">
            <div class="main-header-style_top-left">
                <div class="header-menu-style1">
                    <ul>
                        <li><a href="#">Contact Us</a></li>
                        <li><a href="#">Our Locations</a></li>
                        <li><a href="#">FAQ </a></li>
                        <li><a href="#">Media Center</a></li>
                    </ul>
                </div>
            </div>
            <div class="main-header-style_top-right">
                <div class="header-contact-info-style1">
                    <h5>24/7 Banking Support
                        <a href="tel:+12345000975">+91 234 567 8910</a>
                    </h5>
                </div>

                <div class="header-social-link-style1">
                    <ul class="clearfix">
                        <li><a href="#"><svg-icon class="myicons" type="mdi" :path="wsup" /></a></li>
                        <li><a href="#"><svg-icon class="myicons" type="mdi" :path="twitter" /></a></li>
                        <li><a href="#"><svg-icon class="myicons" type="mdi" :path="ig" /></a></li>
                        <li><a href="#"><svg-icon class="myicons" type="mdi" :path="utube" /></a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>

    <header class="app-header">
        <div class="header-left">
            <router-link to="/" class="logo">
                <img src="../assets/img/applogo.png" alt="Logo" />
            </router-link>
        </div>

        <div class="header-center">
            <!-- Desktop Menu -->
            <nav class="desktop-nav">
                <ul class="menu-list">
                    <li class="nav-item">
                        <router-link to="/">Home</router-link>
                    </li>

                    <li class="nav-item has-submenu">
                        <span>About</span>
                        <ul class="submenu">
                            <li><router-link to="/about-bank">About Bank</router-link></li>
                            <li><router-link to="/testimonials">Testimonials</router-link></li>
                            <li><router-link to="/faqs">FAQ’s</router-link></li>
                        </ul>
                    </li>

                    <li class="nav-item has-submenu">
                        <span>Services</span>
                        <ul class="submenu">
                            <li><router-link to="/accounts">Accounts</router-link></li>
                        </ul>
                    </li>

                    <li class="nav-item">
                        <router-link to="/apply">Apply Now</router-link>
                    </li>

                    <li class="nav-item">
                        <router-link to="/contact">Contact</router-link>
                    </li>
                </ul>
            </nav>

            <!-- Mobile “Menu” Button -->
            <svg-icon class="mobile-menu-btn" type="mdi" :path="hamburger" @click="toggleMobileMenu" />
        </div>

        <div class="header-right">
            <router-link to="/Sign-In" class="btn">Login</router-link>
            <router-link to="/CreateAccount" class="btn">Create Account</router-link>
        </div>

        <!-- Mobile Menu Drawer -->
        <div class="mobile-nav-wrapper" :class="{ expanded: mobileMenuOpen }">
            <div class="mobile-nav-overlay mobile-drawer" v-if="mobileMenuOpen" @click.self="toggleMobileMenu">
                <div class="mobile-drawer-content">
                    <span class="mobile-nav-close" @click="toggleMobileMenu">
                        <svg-icon type="mdi" :path="menuclose" />
                    </span>
                    <div class="logo-box">
                        <router-link to="/" class="logo">
                            <img src="../assets/img/logowhite.png" alt="Logo" />
                        </router-link>
                    </div>
                    <ul class="mobile-menu-list">
                        <li class="mobile-nav-item">
                            <router-link to="/" @click="toggleMobileMenu">Home</router-link>
                        </li>

                        <li class="mobile-nav-item mobile-has-submenu">
                            <div class="mobile-submenu-header" @click="toggleSubmenu('about')">
                                <span>About</span>
                                <SvgIcon type="mdi" :path="submenuOpen.about ? uparrow : downarrow" :size="24"
                                    class="myicos" />
                            </div>
                            <ul v-if="submenuOpen.about" class="mobile-submenu">
                                <li>
                                    <router-link to="/about-bank" @click="toggleMobileMenu">About Bank</router-link>
                                </li>
                                <li>
                                    <router-link to="/testimonials" @click="toggleMobileMenu">Testimonials</router-link>
                                </li>
                                <li>
                                    <router-link to="/faqs" @click="toggleMobileMenu">FAQ’s</router-link>
                                </li>
                            </ul>
                        </li>

                        <li class="mobile-nav-item mobile-has-submenu">
                            <div class="mobile-submenu-header" @click="toggleSubmenu('services')">
                                <span>Services</span>
                                <SvgIcon type="mdi" :path="submenuOpen.services ? uparrow : downarrow" :size="24"
                                    class="myicos" />
                            </div>
                            <ul v-if="submenuOpen.services" class="mobile-submenu">
                                <li>
                                    <router-link to="/accounts" @click="toggleMobileMenu">Accounts</router-link>
                                </li>
                            </ul>
                        </li>

                        <li class="mobile-nav-item">
                            <router-link to="/apply" @click="toggleMobileMenu">Apply Now</router-link>
                        </li>

                        <li class="mobile-nav-item">
                            <router-link to="/contact" @click="toggleMobileMenu">Contact</router-link>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </header>
</template>

<script>
import SvgIcon from '@jamescoyle/vue-icon'
import { mdiAccount, mdiMenu, mdiCloseCircle, mdiEmail, mdiPhone, mdiWhatsapp, mdiArrowDownDropCircle, mdiArrowUpDropCircle } from '@mdi/js'

export default {
    name: "PageHeader",
    components: { SvgIcon },
    data() {
        return {
            downarrow: mdiArrowDownDropCircle,
            uparrow: mdiArrowUpDropCircle,
            acc: mdiAccount,
            email: mdiEmail,
            phone: mdiPhone,
            hamburger: mdiMenu,
            menuclose: mdiCloseCircle,
            wsup: mdiWhatsapp,
            mobileMenuOpen: false,
            submenuOpen: {
                about: false,
                services: false
            }
        };
    },
    methods: {
        toggleMobileMenu() {
            this.mobileMenuOpen = !this.mobileMenuOpen;
            // When closing the drawer, also close all submenus:
            if (!this.mobileMenuOpen) {
                this.submenuOpen.about = false;
                this.submenuOpen.services = false;
            }
        },
        toggleSubmenu(key) {
            // Toggle only the requested submenu; collapse others:
            this.submenuOpen[key] = !this.submenuOpen[key];
            Object.keys(this.submenuOpen).forEach(k => {
                if (k !== key) this.submenuOpen[k] = false;
            });
        }
    }
};
</script>

<style scoped>
/* Desktop Top Bar */
.main-header-style_top {
    background-color: var(--bg-grey);
    padding: 10px 0;
    font-size: small;
    font-family: var(--primary);
}

.myicons {
    color: var(--primary);
    width: 30px;
    height: 30px;
}

.myicons:hover {
    color: var(--hover);
}

.inner-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: wrap;
    margin-left: 1vw;
}

.main-header-style_top-left .header-menu-style1 ul {
    list-style: none;
    display: flex;
    gap: 15px;
    margin: 0;
    padding: 0;
}

.main-header-style_top-left .header-menu-style1 ul li a {
    text-decoration: none;
    color: var(--dark-text);
    font-size: var(--normal-font-size);
}

.main-menu-style1-middle {
    position: relative;
    display: flex;
    align-items: center;
}

.main-menu-style1-middle .main-menu-box {
    position: static;
    display: block;
}

.main-header-style_top-right {
    display: flex;
    align-items: center;
    margin-right: 5vw;
}

.header-social-link-style1 ul {
    list-style: none;
    display: flex;
    margin: 0;
    padding: 0;
}

.header-social-link-style1 ul li a {
    color: var(--dark-text);
    font-size: 1.2rem;
    text-decoration: none;
}

.header-menu-style1 {
    position: relative;
    display: block;
}

.header-menu-style1 ul {
    overflow: hidden;
}

.header-menu-style1 ul li {
    position: relative;
    display: block;
    float: left;
    line-height: 22px;
    margin-right: 30px;
}

.header-menu-style1 ul li:last-child {
    margin-right: 0;
}

.header-menu-style1 ul li a {
    color: var(--thm-black);
    font-size: 13px;
    font-weight: 400;
    text-transform: uppercase;
    transition: all 200ms linear;
    transition-delay: 0.1s;
}

.header-menu-style1 ul li a:hover {
    color: var(--hover);
}

.header-contact-info-style1 {
    position: relative;
    display: block;
}

.header-contact-info-style1 h5 {
    margin: 0;
    color: var(--info);
    font-size: 15px;
    line-height: 28px;
    font-weight: var(--font-semi-bold);
}

.header-contact-info-style1 h5 a {
    position: relative;
    display: inline-block;
    text-decoration: none;
    color: var(--info);
    font-size: 18px;
    font-weight: var(--font-semi-bold);
}

.header-social-link-style1 {
    position: relative;
    display: block;
}

.header-social-link-style1 ul {
    position: relative;
    display: block;
}

.header-social-link-style1 ul li {
    position: relative;
    display: block;
    float: left;
    padding: 0 0 0 1vw;
}

.header-social-link-style1 ul li a {
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 32px;
    height: 32px;
    background: var(--bg);
    border-radius: 50%;
    color: var(--dark-text);
    font-size: 14px;
    transition: all 200ms linear;
    transition-delay: 0.1s;
}

.header-social-link-style1 ul li a:hover {
    color: var(--hover);
    background-color: var(--bg-grey)
}

/*========= Container flex setup ==========*/
.app-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    background: var(--bg);
    padding: 0 1rem;
    font-family: var(--font-family);
    border-bottom: 1px solid #e1e1e1;
}

/* LEFT: Logo */
.header-left .logo img {
    height: 40px;
    margin: 10px;
}

/* RIGHT: Buttons */
.header-right {
    display: flex;
    gap: 0.75rem;
}

/* CENTER: Desktop Menu */
.header-center {
    flex: 1;
    text-align: center;
    position: relative;
}

/* Hide mobile button on desktop */
.mobile-menu-btn {
    display: none;
    color: var(--primary);
    cursor: pointer;
}

/* Desktop nav list */
.desktop-nav .menu-list {
    display: flex;
    justify-content: center;
    gap: 2rem;
    list-style: none;
    margin: 0;
    padding: 0;
    color: var(--primary);
}

.desktop-nav .nav-item {
    position: relative;
    font-size: var(--h3-font-size);
    color: var(--primary);
}

.desktop-nav .nav-item>a,
.desktop-nav .nav-item>span {
    color: var(--primary);
    text-decoration: none;
    padding: 0.5rem;
    cursor: pointer;
}

.desktop-nav .nav-item:hover>a,
.desktop-nav .nav-item:hover>span {
    color: var(--accent);
    font-weight: var(--font-semi-bold);
}

/* Submenu (hidden by default) */
.desktop-nav .has-submenu .submenu {
    margin: 0;
    padding: 0;
    list-style-type: none;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
}

.desktop-nav .has-submenu .submenu {

    display: none;
    position: absolute;
    top: 100%;
    min-width: 10vw;
    padding: 20px 0 0 0;
    border-radius: 10px;
    background-color: var(--transparent);
    box-shadow: 2px 2px 5px 1px rgba(0, 0, 0, 0.05), -2px 0px 5px 1px rgba(0, 0, 0, 0.05);
    z-index: 99;
}

/*====== MENNU ITEMS HOVER ====== */
.desktop-nav .has-submenu:hover .submenu {
    display: block;
}

.desktop-nav .submenu li {
    padding: 0;
}

.desktop-nav .submenu li a {
    display: block;
    text-decoration: none;
}

.desktop-nav .menu-list>li:before {
    content: "[";
    position: absolute;
    top: 0;
    left: 0;
    bottom: 0;
    opacity: 0;
    transform: perspective(400px) rotate(0deg) translateX(-10px) scale(1.0);
    transform-origin: left;
    transition: background-color 0.5s ease;
    transition: all 0.5s ease;
    color: var(--accent);
}

.desktop-nav .menu-list>li:hover:before,
.desktop-nav .menu-list>li.current:before {
    opacity: 1;
    transform: perspective(400px) rotate(0deg) translateX(0px) scale(1.0);
    color: var(--accent);

}

.desktop-nav .menu-list>li:after {
    content: "]";
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    opacity: 0;
    transform: perspective(400px) rotate(0deg) translateX(10px) scale(1.0);
    transform-origin: right;
    transition: background-color .5s ease;
    transition: all 0.5s ease;
    color: var(--accent);
}

.desktop-nav .menu-list>li:hover:after,
.desktop-nav .menu-list>li.current:after {
    opacity: 1;
    transform: perspective(400px) rotate(0deg) translateX(0px) scale(1.0);
    color: var(--accent);
}

.desktop-nav .submenu li a:hover {
    color: var(--primary);
}

/*====== DROPDOWN STYLING ======*/
.desktop-nav .menu-list>li>ul>li,
.desktop-nav .menu-list>li>ul>li>ul>li {
    position: relative;
    width: 100%;
    padding-left: 20px;
}

.desktop-nav .menu-list>li>ul>li>a,
.desktop-nav .menu-list>li>ul>li>ul>li>a {
    position: relative;
    display: flex;
    font-size: 17px;
    color: var(--primary);
    font-weight: var(--font-medium);
    padding: 10px 0px;
    border-bottom: 1px solid var(--border);
    z-index: 1;
}

.desktop-nav .menu-list>li>ul>li:last-child>a,
.desktop-nav .menu-list>li>ul>li>ul>li:last-child>a {
    border-bottom: none;
}

.desktop-nav .menu-list>li>ul>li:hover>a,
.desktop-nav .menu-list>li>ul>li>ul>li:hover>a {
    background-color: var(--transparent-accent);
    color: var(--white-text);
}

.desktop-nav .menu-list>li>ul>li.dropdown>a:before {
    font-family: 'icomoon' !important;
    content: "\e968";
    position: absolute;
    top: 0px;
    right: 25px;
    display: block;
    color: #a9a6a5;
    font-size: 20px;
    line-height: 50px;
    font-weight: 400;
    z-index: 5;
    transition: all 500ms ease;
}

.desktop-nav .menu-list>li>ul>li.dropdown:hover>a::before {
    color: var(--thm-base);
}

/*====== MOBILE MENU DRAWER ======*/
.mobile-nav-wrapper {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    visibility: hidden;
    transform: translateX(-100%);
    transition: transform 0.5s ease, visibility 0.5s ease;
    z-index: 999;
}

.mobile-nav-wrapper.expanded {
    visibility: visible;
    transform: translateX(0);
}

.mobile-nav-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.7);
}

.mobile-drawer {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background: rgba(0, 0, 0, 0.4);
    z-index: 1000;
}

.mobile-drawer-content {
    background: var(--bg-dark);
    width: 50%;
    max-width: 300px;
    height: 100%;
    padding: 1rem;
    box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
    overflow-y: auto;
}

.mobile-nav-close {
    display: inline-block;
    font-size: 24px;
    cursor: pointer;
    color: var(--white-text);
    text-align: right;
    width: 100%;
}

/*====== Mobile menu list ======*/

.mobile-menu-list {
    list-style: none;
    padding: 0;
    margin: 1rem 0 0 0;
}

.mobile-nav-item {
    margin-bottom: 0.5rem;
    border-bottom: 1px solid #e1e1e1;
}

.logo-box img {
    width: 120px;
    margin-bottom: 20px;
}

.mobile-nav-item a,
.mobile-nav-item span {
    display: flex;
    padding: 0.75rem 0;
    font-size: 1rem;
    color: var(--white-text);
    text-decoration: none;
    cursor: pointer;
}

/* Mobile submenu arrow */
.mobile-submenu-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.mobile-submenu-header svg {
    color: white;
}

.mobile-submenu {
    list-style: none;
    padding-left: 1rem;
    margin-top: 0.25rem;
}

.mobile-submenu li a {
    padding: 0.5rem 0;
    font-size: 0.95rem;
    color: var(--white-text);
}

/*====== RESPONSIVE BEHAVIOR ======*/
@media (max-width: 768px) {

    /* Hide desktop nav on small screens */
    .desktop-nav {
        display: none;
    }

    /* Show mobile “Menu” button */
    .mobile-menu-btn {
        display: inline-block;
    }
}

@media(max-width: 500px) {
    .myicons {
        width: 20px;
        height: 20px;
    }

    .main-header-style_top {
        align-items: center;
    }

    .main-header-style_top-left .header-menu-style1 ul {
        gap: 1px;
    }

    .main-header-style_top-left .header-menu-style1 ul li a {
        font-size: var(--small-font-size);
    }

    .inner-content {
        justify-content: center;
        margin: 0;
    }

    .header-social-link-style1 {
        padding: 1vh 0 0 0;
        margin-right: 0;
    }

    .header-social-link-style1 ul li {
        margin: 1vw;
    }
}
</style>
