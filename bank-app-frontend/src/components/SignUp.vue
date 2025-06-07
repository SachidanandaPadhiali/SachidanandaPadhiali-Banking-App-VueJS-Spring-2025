<template>
    <div>
        <img class="SignUplogo" alt="Restaurant logo" src="../assets/img/applogo.png">
    </div>
    <div class="overlay">
        <div class="popup-container">
            <main>
                <div class="form-container">
                    <h2>Sign Up</h2>
                    <p class="subtitle">Sign up to get started</p>

                    <form id="registerFormElement" @submit.prevent="handleSignUp">
                        <div class="input-group">
                            <svg-icon class="input-icon" type="mdi" :path="per" />
                            <input type="text" v-model="user.firstName" id="name" placeholder="First Name" required>
                        </div>
                        <div class="input-group">
                            <svg-icon class="input-icon" type="mdi" :path="per" />
                            <input type="text" v-model="user.lastName" id="name" placeholder="Last Name" required>
                        </div>

                        <div class="input-group">
                            <svg-icon class="input-icon" type="mdi" :path="aadhar" />
                            <input type="text" v-model="user.lastName" id="name" placeholder="Last Name" required>
                        </div>

                        <div class="input-group">
                            <svg-icon class="input-icon" type="mdi" :path="email" />
                            <input type="email" v-model="user.email" id="registerEmail" placeholder="Your Email"
                                required>
                        </div>

                        <div class="input-group">
                            <svg-icon class="input-icon" type="mdi" :path="ph" />
                            <input type="string" v-model="user.phno" id="registerPhno" placeholder="Your Phone No."
                                required>
                        </div>

                        <div class="input-group">
                            <svg-icon class="input-icon" type="mdi" :path="pass" />
                            <input type="password" v-model="user.password" id="registerPassword"
                                placeholder="Create Password" required>
                        </div>

                        <div class="input-group">
                            <svg-icon class="input-icon" type="mdi" :path="checkpass" />
                            <input type="password" v-model="user.confirmPassword" id="confirmPassword"
                                placeholder="Confirm Password" required>
                        </div>

                        <p v-if="passwordMismatch" class="message error">Passwords do not match!</p>

                        <p v-if="user.password && !isPasswordValid(user.password)" class="message error">
                            Password must contain uppercase, lowercase, number, and special character.
                        </p>

                        <p v-if="user.password && !isPasswordLong(user.password)" class="message error">
                            Password Is not Long Enough.
                        </p>

                        <button type="submit" class="signup-btn" v-on:click="signUp">CREATE AN ACCOUNT</button>
                        <p v-if="duplicateUser" class="message error">{{ duplicateUser }}</p>

                        <p class="login-link">
                            Already have an account? <router-link to="/Sign-In" class="login-link">Log In</router-link>
                        </p>

                    </form>
                </div>
            </main>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
import SvgIcon from '@jamescoyle/vue-icon'
import { mdiAccount, mdiEmail, mdiPhone, mdiFormTextbox, mdiSmartCardOutline, mdiLockOutline, mdiLockCheckOutline } from '@mdi/js'

export default {
    name: 'SignUp',
    components: { SvgIcon },
    data() {
        return {
            per: mdiAccount,
            email: mdiEmail,
            ph: mdiPhone,
            add: mdiFormTextbox,
            aadhar: mdiSmartCardOutline,
            pass: mdiLockOutline,
            checkpass: mdiLockCheckOutline,
            isSignIn: true,
            user: {
                firstName: '',
                lastName: '',
                email: '',
                phoneNumber: '',
                
                password: '',
                confirmPassword: '',

                addressLine1: '',
                addressLine2: '',
                city: '',
                state: '',
                pin: '',

                gender: ''

            },
            errorMessage: "",
            duplicateUser: "" // Stores the error message
        };
    },
    computed: {
        passwordMismatch() {
            return this.user.password && this.user.confirmPassword && this.user.password !== this.user.confirmPassword;
        }
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
        } else {
            console.log("No user data found.");
        }
    },
    methods: {
        toggleForm() {
            this.isSignIn = !this.isSignIn;
        },
        isPasswordValid(password) {
            const hasUpperCase = /[A-Z]/.test(password);
            const hasLowerCase = /[a-z]/.test(password);
            const hasNumber = /[0-9]/.test(password);
            const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);

            return hasUpperCase && hasLowerCase && hasNumber && hasSpecialChar;
        },
        isPasswordLong(password) {
            const hasMinLength = password.length >= 8;

            return hasMinLength;
        },
        async handleSignUp() {
            console.log({
                username: this.user.username,
                email: this.user.email,
                phno: this.user.phno,
                password: this.user.password,
                confirmPassword: this.user.confirmPassword
            });

            try {
                // Check if user exists based on email
                const exists = await this.checkDuplicate();

                // if not create an entry for the user
                let response = await axios.post("http://192.168.1.4.nip.io:8088/api/auth/signup", {
                    firstName: this.user.firstName,
                    lastName: this.user.lastName,
                    gender: this.user.gender,
                    addressLine1: this.user.addressLine1,
                    addressLine2: this.user.addressLine2,
                    city: this.user.city,
                    state: this.user.state,
                    pin: this.user.pin,
                    email: this.user.email,
                    phoneNumber: this.user.phoneNumber,
                    password: this.user.password,
                    confirmPassword: this.user.confirmPassword
                }, {
                    headers: { "Content-Type": "application/json" }
                });

                if (response.status === 201 && !exists) {
                    alert(`Sign Up successful for ${response.data.username || this.username}`);
                    this.toggleForm();
                }
            } catch (error) {
                console.error("Error signing up:", error);
                alert("Signup failed! Please try again.");
            }
        },
        async checkDuplicate() {
            try {
                // Fetch user data from backend
                const response = await axios.get("http://192.168.1.4.nip.io:8088/api/auth/signup");
                const users = response.data;

                if (users.some(u => u.email === this.user.email)) {
                    this.duplicateUser = "User already exists! Please log in instead."
                    return true;
                }

                return false;
            } catch (error) {
                console.error("Error fetching users:", error);
                return false;
            }
        }
    }
};
</script>
<style>
.signup-btn {
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

.signup-btn:hover {
    background: var(--accent);
}
</style>