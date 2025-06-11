import PreLoader from "./components/PreLoader.vue";
import HomePage from "./components/HomePage.vue";
import UserHome from "./components/UserHome.vue";
import Profile from "./components/Profile.vue";
import SignUp from "./components/SignUp.vue";
import SignIn from "./components/SignIn.vue";
import CreateAccount from './components/CreateAccount.vue';
import {createRouter, createWebHistory} from "vue-router";
import SavingsAcc from "./components/SavingsAcc.vue";

const routes = [
    {
        name: "PreLoader",
        component: PreLoader,
        path:"/",
    },
    {
        name: "HomePage",
        component: HomePage,
        path:"/HomePage",
    },
    {
        name: "UserHomePage",
        component: UserHome,
        path:"/User/Home",
        meta: { requiresAuth: true }
    },
    {
        name: "SignUp",
        component: SignUp,
        path:"/Sign-Up"
    },
    {
        name: "CreateAccount",
        component: CreateAccount,
        path:"/CreateAccount"
    },
    {
        name: "SignIn",
        component: SignIn,
        path:"/Sign-In"
    },
    {
        name: "UserProfile",
        component: Profile,
        path:"/User/Profile",
        meta: { requiresAuth: true }
    },
    {
        name: "SavingsAcc",
        component: SavingsAcc,
        path:"/User/savings",
        meta: { requiresAuth: true }
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes,
    scrollBehavior(to) {
    if (to.hash) {
      return {
        el: to.hash,
        behavior: 'smooth'
      };
    }
    return { top: 0 };
  }
});

// Navigation Guard to check authentication
router.beforeEach((to, from, next) => {
    const isAuthenticated = localStorage.getItem('user-login-info'); // Check if token exists

    if (to.meta.requiresAuth && !isAuthenticated) {
        next('/HomePage'); // Redirect to login
    } else {
        next(); // Allow access
    }
});

export default router;