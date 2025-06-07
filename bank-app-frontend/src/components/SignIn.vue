<template>
    <div>
        <img class="SignUplogo" alt="Restaurant logo" src="../assets/img/applogo.png">
    </div>
    <div class="overlay">
        <div class="popup-container">
            <main>
                <div class="form-container">
                    <h1>Welcome Back</h1>
                    <p class="subtitle">Sign in to continue to your account</p>

                    <form id="loginForm" @submit.prevent="handleSignIn">
                        <div class="input-group">
                            <svg-icon class="input-icon" type="mdi" :path="acc" />
                            <input type="email" id="email" placeholder="Your Email Id / Account Number"
                                v-model="user.email" required>
                        </div>

                        <div class="input-group">
                            <svg-icon class="input-icon" type="mdi" :path="pass" />
                            <input type="password" id="password" placeholder="Your Password" v-model="user.password"
                                required>
                        </div>

                        <div class="remember-forgot">
                            <label class="remember">
                                <input type="checkbox" id="remember">
                                <span>Remember me</span>
                            </label>
                            <a href="#" class="forgot-link" data-form="forgotForm">Forgot Password?</a>
                        </div>

                        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>

                        <button type="submit" class="signin-btn">Sign In</button>

                        <p class="login-link">
                            Don't have an account? <a href="#" class="switch-form" data-form="registerForm"
                                @click="goToSignUp">CREATE AN ACCOUNT</a>
                        </p>
                    </form>
                </div>
            </main>
        </div>
    </div>
</template>

<script>
import axios from 'axios'
import SvgIcon from '@jamescoyle/vue-icon'
import { mdiAccount, mdiFormTextboxPassword } from '@mdi/js'

export default {
    name: 'SignIn',
    components: { SvgIcon },
    data() {
        return {
            acc: mdiAccount,
            pass: mdiFormTextboxPassword,
            isSignIn: true,
            user: {
                email: '',
                password: '',
            },
            errorMessage: "",
            duplicateUser: "" // Stores the error message
        };
    },
    mounted() {
        let isUserLoggedIn = localStorage.getItem("user-login-info");
        if (isUserLoggedIn) {
            try {
                this.$router.push("/User/Home");
                console.log("Sending back to login page");
            } catch (error) {
                console.error("Error parsing user data:", error);
            }
        }
    },
    methods: {
        goToSignUp() {
            this.$router.push("/CreateAccount");
        },
        async handleSignIn() {
            try {
                // Fetch user data from backend
                const loginresponse = await axios.post("http://192.168.1.4.nip.io:8088/api/login", {
                    email: this.user.email,
                    password: this.user.password
                });
                const user = loginresponse.data;
                console.log(user);

                if (user && user.password === this.user.password) {
                    this.errorMessage = "";
                    localStorage.setItem("user-login-info", JSON.stringify(user))
                    this.$router.push("/User/Home");
                } else {
                    console.log("Invalid email or password!")

                }
            } catch (error) {
                if (error.response && error.response.status === 401) {
                    // invalid credentials
                    console.warn("Invalid credentials");
                    this.errorMessage = "Invalid email or password!";
                } else {
                    console.error("Login error:", error);
                    this.errorMessage = "Something went wrong. Please try again.";
                }
            }
        }
    }
};
</script>

<style>
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    font-family: 'Poppins', sans-serif;
}

.SignUplogo {
    width: 200px;
}

.overlay {
    inset: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: .3s ease-out;
    margin: 20;
}

.popup-container {
    background: var(--white);
    border-radius: 16px;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
    width: 100%;
    max-width: 500px;
    animation: slideUp .3s ease-out;
    transition: .3s ease-out;
}

@keyframes slideUp {
    from {
        opacity: 0;
        transform: translateY(20px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.subtitle {
    color: var(--dark-text);
    margin-bottom: 2rem;
    font-size: .95rem;
}

.input-group {
    position: relative;
    margin-bottom: 1.2rem;
    transition: .2s ease-out;
}

.input-icon {
    position: absolute;
    left: 1rem;
    top: 30%;
    width: 20px;
    height: 20px;
    opacity: .5;
}

input {
    width: 100%;
    padding: .9rem 1rem .9rem 3rem;
    border: 1px solid var(--border);
    border-radius: 8px;
    background: var(--white);
    font-size: .95rem;
    transition: .5s;
}

input:focus {
    outline: none;
    border-color: var(--accent);
    box-shadow: 0 0 0 3px rgba(45, 50, 80, 0.1);
}

.remember-forgot {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1.2rem;
    font-size: .9rem;
}

.remember {
    display: flex;
    align-items: center;
    gap: .5rem;
    cursor: pointer;
}

.remember input[type="checkbox"] {
    width: auto;
    margin: 0;
}

.forgot-link {
    color: var(--accent);
    text-decoration: none;
    font-weight: var(--font-semi-bold);
}

.signin-btn {
    width: 100%;
    padding: .9rem;
    background: var(--primary);
    color: white;
    border: none;
    border-radius: 8px;
    font-size: 1rem;
    font-weight: var(--font-semi-bold);
    cursor: pointer;
    transition: .3s;
}

.signin-btn:hover {
    background: var(--accent);
}

.divider {
    text-align: center;
    margin: 1.5rem 0;
    position: relative;
    font-size: .9rem;
}

.divider::before,
.divider::after {
    content: '';
    position: absolute;
    top: 50%;
    width: 35%;
    height: 2px;
    background: var(--border);
}

.divider::before {
    left: 0;
}

.divider::after {
    right: 0;
}

.divider span {
    background: var(--white);
    padding: 0 1rem;
    color: var(--dark-text);
}

.login-link {
    text-align: center;
    margin-top: 1.5rem;
    color: var(--accent-blue);
    font-weight: var(--font-semi-bold);
    font-size: .9rem;
}

.login-link a {
    color: var(--accent);
    text-decoration: none;
    font-weight: var(--font-semi-bold);
}

.login-link a:hover {
    text-decoration: underline;
}

@media (max-width: 480px) {
    .popup-container {
        margin: 1rem;
        padding: 1.5rem;
    }
}

@media (max-width: 768px) {
    .popup-container {
        margin: 1rem;
        padding: 1.5rem;
    }

    .overlay {
        margin: 0;
    }
}

.message {
    padding: .8rem;
    border-radius: 6px;
    margin-bottom: 1rem;
    font-size: .9rem;
    text-align: center;
    animation: fadeIn .3s ease-out;
}

.error {
    padding: .8rem;
    border-radius: 6px;
    margin-bottom: 1rem;
    font-size: .9rem;
    text-align: center;
    animation: fadeIn .3s ease-out;
    background: #FEE2E2;
    color: var(--error);
    border: 1px solid #FCA5A5;
}

.message.success {
    background: #DCFCE7;
    color: #166534;
    border: 1px solid #86EFAC;
}

@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(-10px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.input-group.focused {
    transform: scale(1.02);
}

.form {
    display: none;
    opacity: 0;
    transform: translateX(20px);
    transition: .3s ease-out;
}

.form.active {
    display: block;
    opacity: 1;
    transform: translateX(0);
}

.switch-form {
    font-weight: 600;
}

.form-container {
    position: relative;
    padding: 5%;
}
</style>
