<template>
    <form id="registerFormElement" @submit.prevent="submitForm">
        <div class="input-group">
            <select v-model="localForm.bankAddress" required>
                <option disabled value="">Choose your Bank Branch</option>
                <option v-for="(bankaddress, index) in bankAddresses" :key="index" :value="bankaddress"
                    :title="bankaddress" style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis;">
                    {{ bankaddress }}
                </option>
            </select>
            <span class="custom-arrow"></span>
        </div>

        <div class="input-group">
            <svg-icon class="input-icon" type="mdi" :path="pass" />
            <input type="password" v-model="localForm.password" id="registerPassword" placeholder="Create Password">
        </div>

        <div class="input-group">
            <svg-icon class="input-icon" type="mdi" :path="checkpass" />
            <input type="password" v-model="localForm.confirmPassword" id="confirmPassword"
                placeholder="Confirm Password">
        </div>

        <p v-if="passwordMismatch" class="message error">Passwords do not match!</p>

        <p v-if="localForm.password && !isPasswordValid(localForm.password)" class="message error">
            Password must contain uppercase, lowercase, number, and special character.
        </p>

        <p v-if="localForm.password && !isPasswordLong(localForm.password)" class="message error">
            Password Is not Long Enough.
        </p>
        <p v-if="duplicateUser" class="message error">{{ duplicateUser }}</p>

        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>

    </form>
</template>

<script>
import axios from 'axios'
import SvgIcon from '@jamescoyle/vue-icon'
import { mdiLockOutline, mdiLockCheckOutline } from '@mdi/js'

export default {
    name: "SignupStep2",
    components: { SvgIcon },
    data() {
        return {
            apiUrl: `${process.env.VUE_APP_API_URL}/api`,
            pass: mdiLockOutline, checkpass: mdiLockCheckOutline,
            errorMessage: '', duplicateUser: '',
            bankAddresses: []
        }
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
                // Safely return a default object to avoid undefined error
                return this.modelValue || { bankaddress: '', password: '', confirmPassword: '' };
            },
            set(newValue) {
                this.$emit('update:modelValue', newValue);
            }
        },
        passwordMismatch() {
            return this.localForm.password && this.localForm.confirmPassword && this.localForm.password !== this.localForm.confirmPassword;
        }
    },
    mounted() {
        this.fetchBankAddresses();
    },
    methods: {
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
        async fetchBankAddresses() {
            try {
                const response = await axios.get(`${this.apiUrl}/bank/addresses`);
                this.bankAddresses = response.data;
            } catch (error) {
                console.error('Error fetching addresses:', error);
            }
        },
        validateStep() {
            const requiredFields = ['password', 'confirmPassword'];
            for (const field of requiredFields) {
                if (!this.modelValue[field]) {
                    this.errorMessage = `${field.replace(/([A-Z])/g, ' $1')} is required`;
                    return false;
                }
            }
            this.errorMessage = '';
            return true;
        }
    }
}
</script>
