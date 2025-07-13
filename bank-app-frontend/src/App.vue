<template>
  <router-view />
</template>

<script>

export default {
  name: 'App',
  mounted() {
    document.body.style.cursor = 'default';
    document.body.setAttribute('contenteditable', 'false');
    document.body.style.userSelect = 'none';
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

:root {
  --primary: #134B70;
  --accent: #27A2A7;
  --black: #111111;
  --hover: #60A5FA;
  --dark-text: #111827;
  --light-text: #6B7280;
  --white-text: #FFFFFF;
  --bg: #F9FAFB;
  --bg-grey: #D2D1D2;
  --bg-dark: #123456;
  --bg-btn: #61aacc;
  --card-bg: #FFFFFF;
  --border: #D1D5DB;
  --success: #10B981;
  --warning: #F59E0B;
  --error: #EF4444;
  --info: #3B82F6;
  --transparent-accent: rgba(33, 169, 173, 0.25);
  --transparent: rgba(255, 255, 255, 0.5);

  /*===== Font and typography =====*/
  --text: 'Poppins', sans-serif;
  --font-family: "Poppins", sans-serif;
  --biggest-font-size: 2.5rem;
  --h1-font-size: 1.5rem;
  --h2-font-size: 1.25rem;
  --h3-font-size: 1.125rem;
  --normal-font-size: 1rem;
  --medium-font-size: 0.9rem;
  --small-font-size: 0.8rem;
  --smaller-font-size: 0.7rem;

  /*===== Font weight =====*/
  --font-slim: 200;
  --font-medium: 400;
  --font-semi-bold: 550;
  --font-bold: 700;

  /*===== Margins =====*/
  --mb-0: 0.1rem;
  --mb-1: 0.5rem;
  --mb-2: 1rem;
  --mb-3: 1.5rem;
  --mb-4: 2rem;
  --mb-5: 2.5rem;
  --mb-6: 3rem;

  /* Effects */
  --glow: 0 0 10px var(--primary-glow);
  --glass: saturate(180%) blur(10px);
  --gradient: linear-gradient(135deg, var(--surface), rgba(40, 40, 60, 0.95));

  /* Timing */
  --transition-fast: 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  --transition-medium: 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

/* Custom Scrollbar */
::-webkit-scrollbar {
  width: 5px;
}

::-webkit-scrollbar-track {
  background: var(--dark-bg);
}

::-webkit-scrollbar-thumb {
  background: linear-gradient(to bottom, var(--primary), var(--accent));
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: linear-gradient(to bottom, var(--primary-dark), var(--accent));
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

#menu {
  list-style-type: none;
}

#menu a {
  border-bottom: 1px solid #e1e1e1;
  padding: var(--mb-2) 0;
  font-size: var(--normal-font-size);
  text-align: left;
  color: var(--primary);
}

#menu li {
  border-bottom: 1px solid #e1e1e1;
  padding: var(--mb-2) 0;
  text-align: left;
  color: var(--primary);
}

#menu li a {
  font-size: var(--normal-font-size);
  color: var(--primary);
}

.btn {
  position: relative;
  z-index: 0;
  overflow: hidden;
  width: fit-content;
  background-color: var(--bg-btn);
  padding: var(--mb-2);
  height: 40px;
  color: var(--white-text);
  text-decoration: none;
  font-family: "Open Sans", sans-serif;
  font-weight: 600;
  font-size: var(--small-font-size);
  box-shadow: 0 15px 40px -10px rgba(0, 0, 0, 0.3);
  transition: all 0.4s ease;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
  border-radius: 5px;
}

.btn span {
  position: relative;
  z-index: 2;
  text-align: center;
}

.btn:before,
.btn:after {
  content: "";
  position: absolute;
  width: 5px;
  height: 100%;
  top: 0;
  background-color: var(--primary);
  z-index: -1;
  transition: all 0.4s ease;
}

.btn:before {
  left: 0;
}

.btn:after {
  right: 0;
}

.btn:hover {
  letter-spacing: 2px;
  color: white;
}

.btn:hover:before,
.btn:hover:after {
  width: 51%;
}

/*Global Mobile Menu*/
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

/* Mobile Navigation */
@media (min-width: 768px) {
  #mobile-menu-button {
    display: none;
  }
}

@media (max-width: 900px) {
  #mobile-menu-button {
    display: block;
  }
}
</style>
