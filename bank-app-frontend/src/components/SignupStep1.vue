<template>
    <form id="registerFormElement" @submit.prevent="submitForm">
        <div class="input-group">
            <svg-icon class="input-icon" type="mdi" :path="per" />
            <input type="text" v-model="localForm.firstName" id="name" placeholder="First Name" required>
        </div>
        <div class="input-group">
            <svg-icon class="input-icon" type="mdi" :path="per" />
            <input type="text" v-model="localForm.lastName" id="name" placeholder="Last Name" required>
        </div>

        <div class="input-group">
            <svg-icon class="input-icon" type="mdi" :path="email" />
            <input type="email" v-model="localForm.email" id="registerEmail" placeholder="Your Email" required>
        </div>

        <div class="input-group">
            <svg-icon class="input-icon" type="mdi" :path="ph" />
            <input type="string" v-model="localForm.phoneNumber" id="registerPhno" placeholder="Your Phone No."
                required>
        </div>

        <div class="input-group">
            <select v-model="localForm.gender" id="gender" required>
                <option disabled value="">Select Gender</option>
                <option value="Male">Male</option>
                <option value="Female">Female</option>
                <option value="Other">Other</option>
            </select>
            <span class="custom-arrow"></span>
        </div>

        <p class="login-link">
            Already have an account? <router-link to="/Sign-In" class="login-link">Log In</router-link>
        </p>

        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>

    </form>
</template>

<script>
import SvgIcon from '@jamescoyle/vue-icon'
import { mdiAccount, mdiEmail, mdiPhone } from '@mdi/js';
import axios from 'axios'

export default {
    name: "SignupStep1",
    components: { SvgIcon },
    data() {
        return { per: mdiAccount, email: mdiEmail, ph: mdiPhone, errorMessage: '' }
    },
    props: {
        modelValue: {
            type: Object,
            required: true
        }
    },
    computed: {
        localForm: {
            get() {
                return this.modelValue || { firstName: '', lastName: '', email: '', phoneNumber: '', gender: '' }
            },
            set(value) {
                this.$emit('update:modelValue', value)
            }
        }
    },
    methods: {
        async validateStep() {
            const requiredFields = ['firstName', 'lastName', 'email', 'phoneNumber', 'gender'];
            for (const field of requiredFields) {
                if (!this.modelValue[field]) {
                    this.errorMessage = `${field.replace(/([A-Z])/g, ' $1')} is required`;
                    return false;
                }
            }
            try {
                let response = await axios.get("http://192.168.1.4.nip.io:8088/api/checkuser", {
                    email: this.localForm.email,
                }, {
                    headers: { "Content-Type": "application/json" }
                });
                console.log(response.data.responseCode);

                if (response.data.responseCode === '001') {
                    return false;
                }
            } catch (error) {
                console.error("Error signing up:", error);
            }
            this.errorMessage = "";
            return true;
        }
    }
}
</script>
<style>
.input-group select {
    border: 1px solid #ccc;
    border-radius: 5px;
    width: 100%;
    font-family: inherit;
    font-size: 16px;
    background-color: white;
    color: #333;
    padding: 12px 20px 12px 20px;
    display: flex;
    align-items: center;
    color: var(--light-text);
    cursor: pointer;
    appearance: none;
}

.input-group select:hover {
    border-color: var(--accent);
}

/* 4. Position the custom arrow */
.input-group .custom-arrow {
    position: absolute;
    top: 50%;
    right: 21px;
    pointer-events: none;
    /* so clicks go through to the select */
    transform: translateY(-50%);
    border-left: 6px solid transparent;
    border-right: 6px solid transparent;
    border-top: 6px solid var(--light-text);
    /* arrow color */
}

/* 5. (Optional) Change arrow color on hover/focus */
.input-group select:hover+.custom-arrow,
.input-group select:focus+.custom-arrow {
    border-top-color: var(--primary);
}
</style>