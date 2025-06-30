<template>
    <form id="registerFormElement" @submit.prevent="submitForm">
        <div class="input-group">
            <select v-model="localForm.country" required>
                <option disabled value="">Select a country</option>
                <option v-for="country in countries" :key="country">{{ country }}</option>
            </select>
            <span class="custom-arrow"></span>
        </div>

        <div class="input-group">
            <select v-model="localForm.state" required>
                <option disabled value="">Select a state</option>
                <option v-for="state in statesByCountry[localForm.country]" :key="state">
                    {{ state }}
                </option>
            </select>
            <span class="custom-arrow"></span>
        </div>

        <div class="input-group">
            <svg-icon class="input-icon" type="mdi" :path="add" />
            <input type="text" v-model="localForm.addressLine1" id="name" placeholder="Address Line 1" required>
        </div>

        <div class="input-group">
            <svg-icon class="input-icon" type="mdi" :path="add" />
            <input type="text" v-model="localForm.addressLine2" id="name" placeholder="Address Line 2" required>
        </div>

        <div class="input-group">
            <svg-icon class="input-icon" type="mdi" :path="add" />
            <input type="text" v-model="localForm.city" id="name" placeholder="Your City" required>
        </div>

        <div class="input-group">
            <input type="text" v-model="localForm.pin" id="name" placeholder="Pin Code" required>
        </div>
        <p v-if="localForm.pin && !isPincodeValid(localForm.pin)" class="message error">
            Pincode should ONLY contain 6 digits.
        </p>

        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>

    </form>
</template>

<script>
import SvgIcon from '@jamescoyle/vue-icon'
import { mdiFormTextbox } from '@mdi/js'

export default {
    name: "SignupStep1",
    components: { SvgIcon },
    data() {
        return {
            add: mdiFormTextbox,
            errorMessage: '',
            countries: ['India', 'other'],
            statesByCountry: {
                India: ['Andhra Pradesh', 'Arunachal Pradesh', 'Assam', 'Bihar', 'Chhattisgarh', 'Goa', 'Gujarat', 'Haryana', 'Himachal Pradesh', 'Jharkhand', 'Karnataka', 'Kerala', 'Madhya Pradesh', 'Maharashtra', 'Manipur', 'Meghalaya', 'Mizoram', 'Nagaland', 'Odisha', 'Punjab', 'Rajasthan', 'Sikkim', 'Tamil Nadu', 'Telangana', 'Tripura', 'Uttar Pradesh', 'Uttarakhand', 'West Bengal', 'Andaman and Nicobar Islands', 'Chandigarh', 'Dadra and Nagar Haveli and Daman and Diu', 'Delhi', 'Jammu and Kashmir', 'Ladakh', 'Lakshadweep', 'Puducherry'],
                other: ['other']
            }
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
                return this.modelValue || { country: '', state: '', addressLine1: '', addressLine2: '', city: '', pin: '' }
            },
            set(value) {
                this.$emit('update:modelValue', value)
            }
        }
    },
    methods: {
        isPincodeValid(pin) {
            const hasUpperCase = /[A-Z]/.test(pin);
            const hasLowerCase = /[a-z]/.test(pin);
            const hasNumber = /[0-9]/.test(pin);
            const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(pin);
            const hasLength = pin.length == 6;

            return !hasUpperCase && !hasLowerCase && hasNumber && !hasSpecialChar && hasLength;
        },
        validateStep() {
            const requiredFields = ['country', 'state', 'addressLine1', 'addressLine2', 'city', 'pin'];
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
