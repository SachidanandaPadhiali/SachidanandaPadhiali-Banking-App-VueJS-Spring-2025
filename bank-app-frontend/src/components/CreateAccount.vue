<template>
    <div>
        <img class="SignUplogo" alt="Restaurant logo" src="../assets/img/applogo.png">
    </div>
    <div class="overlay">
        <div class="popup-container">
            <main>
                <div class="form-container">
                    <div class="signup-step">
                        <component :is="currentComponent" v-model="formData" ref="currentStepRef" />
                        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
                    </div>

                    <div class="step-controls" :class="{ 'one-button': step === 1 }">
                        <button class="signup-btn" @click="prevStep" v-if="step > 1">Back</button>
                        <button class="signup-btn" @click="nextStep" v-if="step < steps.length">Next</button>
                        <button type="submit" class="signup-btn" v-if="step === steps.length" @click="submitForm">
                            CREATE AN ACCOUNT
                        </button>
                    </div>
                </div>
            </main>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

import SignupStep1 from './SignupStep1.vue'
import SignupStep2 from './SignupStep2.vue'
import SignupStep3 from './SignupStep3.vue'

export default {
    name: 'CreateAccount',
    components: { SignupStep1, SignupStep2, SignupStep3 },
    computed: {
        currentComponent() {
            return this.steps[this.step - 1]
        }
    },
    data() {
        return {
            step: 1,
            errorMessage: '',
            isfilled: true,
            formData: {
                firstName: '',
                lastName: '',
                email: '',
                phoneNumber: '',
                addressLine1: '',
                addressLine2: '',
                city: '',
                state: '',
                country: '',
                pin: '',
                gender: '',
                aadhar: '',
                password: '',
                confirmPassword: ''
            },
            steps: ['SignupStep1', 'SignupStep2', 'SignupStep3']
        }
    },
    methods: {
        async nextStep() {
            const valid = await this.$refs.currentStepRef?.validateStep?.();
            if (!valid) return;

            if (this.step < this.steps.length) {
                this.step++
            }

        },
        prevStep() {
            if (this.step > 1) {
                this.step--
            }
        },
        async submitForm() {
            try {
                const requiredFields = ['password', 'confirmPassword'];
                for (const field of requiredFields) {
                    if (!this.formData[field]) {
                        this.errorMessage = `${field.replace(/([A-Z])/g, ' $1')} is required`;
                        console.log(this.errorMessage);
                        this.isfilled = false;
                    }
                }
                if (this.isfilled) {
                    let response = await axios.post("http://192.168.1.4.nip.io:8088/api/user", {
                        firstName: this.formData.firstName,
                        lastName: this.formData.lastName,
                        gender: this.formData.gender,
                        addressLine1: this.formData.addressLine1,
                        addressLine2: this.formData.addressLine2,
                        city: this.formData.city,
                        state: this.formData.state,
                        pin: this.formData.pin,
                        email: this.formData.email,
                        phoneNumber: this.formData.phoneNumber,
                        password: this.formData.password,
                        confirmPassword: this.formData.confirmPassword
                    }, {
                        headers: { "Content-Type": "application/json" }
                    });
                    console.log(response.data.responseCode);

                    if (response.data.responseCode === '001') {
                        alert(response.data.responseMessage);
                        this.$router.push("/Sign-In");
                    }
                    else if (response.data.responseCode === '002') {
                        alert(`Sign Up successful for ${response.data.username || this.formData.firstName}`);
                        this.$router.push("/Sign-In");
                    }
                }
            } catch (error) {
                console.error("Error signing up:", error);
                alert("Signup failed! Please try again.");
            }
        }
    }
}
</script>

<style scoped>
.signup-container {
    max-width: 600px;
    margin: auto;
    padding: 2rem;
    border: 1px solid #ccc;
    border-radius: 8px;
}

.step-controls {
    display: flex;
    justify-content: space-between;
    margin-top: 1.5rem;
    flex-direction: row;
}

.step-controls.one-button {
  justify-content: flex-end; /* override when only one */
}

button {
    padding: 10px 20px;
    font-weight: bold;
}
</style>
