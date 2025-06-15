<template>
    <div class="container">
        <div class="main-body">
            <div class="row">
                <!-- Sidebar -->
                <div class="col-md-4">
                    <div class="mycard">
                        <div class="d-flex flex-column align-items-center text-center">
                            <UserAvatarDisplay />
                            <a class="updateProfile" @click="showPopup = true">Update Profile Image</a>

                            <div v-if="showPopup" class="modal-overlay" @click.self="showPopup = false">
                                <div class="modal-content">
                                    <h3>Select an Image</h3>
                                    <input type="file" accept="image/*" @change="onImageSelected" />
                                    <div v-if="previewUrl" class="image-preview">
                                        <img :src="previewUrl" alt="Preview" />
                                    </div>

                                    <button class="success-btn" :disabled="!selectedFile"
                                        :class="{ 'disabled-btn': !selectedFile }" @click="uploadAvatar">
                                        Upload
                                    </button>
                                    <button class="close-btn" @click="showPopup = false">Close</button>
                                </div>
                            </div>

                            <div class="mt-3">
                                <h4>{{ user.firstName }}</h4>
                                <p class="text-secondary mb-1">{{ user.email }}</p>
                                <p class="text-muted font-size-sm">{{ user.phoneNumber }}</p>
                            </div>
                        </div>
                        <div class="list-group list-group-flush text-center mt-4">
                            <a href="#" @click="goToHome" class="btn mybtn">
                                <svg-icon type="mdi" :path="home" />
                                HomePage
                            </a>
                            <a href="#" class="btn mybtn" :class="{ active: activeIndex === 0 }"
                                @click.prevent="showProfileDetails">
                                <svg-icon type="mdi" :path="acc" />
                                Personal Information
                            </a>
                            <a href="#" class="btn mybtn" :class="{ active: activeIndex === 1 }"
                                @click.prevent="showAddressBook">
                                <svg-icon type="mdi" :path="add" />
                                Address
                            </a>
                            <a href="#" class="btn mybtn" :class="{ active: activeIndex === 2 }"
                                @click.prevent="showAcc">
                                <svg-icon type="mdi" :path="passbook" />
                                Account Information
                            </a>
                            <a href="#" @click="logout" class="btn mybtn">
                                <svg-icon type="mdi" :path="logouticon" />
                                Sign out
                            </a>
                        </div>
                    </div>
                </div>

                <!-- Main Content -->
                <div class="col-md-8">
                    <div v-show="activeIndex === 0" id="profileDetails" class="detailscard">
                        <div class="mycard-body">
                            <h5>Profile Information</h5>
                            <hr>
                            <p><strong>Name:</strong> {{ user.fullname }}</p>
                            <p><strong>Email:</strong> {{ user.email }}</p>
                            <p><strong>Phone No.:</strong> {{ user.phoneNumber }}</p>
                        </div>
                    </div>

                    <div v-show="activeIndex === 1" id="addressBook" class="detailscard">
                        <div class="mycard-body">
                            <h5>Address Book</h5>
                            <hr>
                            <h3>{{ user.fullname }}</h3>
                            <p>{{ user.addressLine1 }}, {{ user.addressLine2 }}, {{ user.city }}</p>
                            <p>{{ user.state }} - {{ user.pin }}</p>
                            <p>Mobile: {{ user.phoneNumber }}</p>
                        </div>
                    </div>
                    <div v-show="activeIndex === 2" id="accounts" class="detailscard">
                        <div class="mycard-body">
                            <h5>Account Information</h5>
                            <hr>
                            <h3>{{ user.fullname }}</h3>
                            <router-link to="/User/savings" class="savingsnav">
                                <p>
                                    {{ 'Savings Account' }} <br>
                                    {{ 'Account Balance: ₹ ' + user.bal }}
                                </p>
                                <svg-icon class="myicons" type="mdi" :path="gotosavings" />
                            </router-link>
                        </div>
                    </div>
                    <CollapsDetails title="Fixed Deposits" class="accdetails" v-show="activeIndex === 2" id="accounts">
                        <p><strong>Account Number:</strong> 1234567890</p>
                        <p><strong>Balance:</strong> ₹0.00</p>
                        <p><strong>Status:</strong> Active</p>
                    </CollapsDetails>
                    <CollapsDetails title="Recurring Deposits" class="accdetails" v-show="activeIndex === 2"
                        id="accounts">
                        <ul>
                            <li>🟢 Credited ₹5000 on 01-Jun</li>
                            <li>🔴 Debited ₹1200 on 03-Jun</li>
                        </ul>
                    </CollapsDetails>

                </div>
            </div>
        </div>
    </div>
</template>

<script>
import SvgIcon from '@jamescoyle/vue-icon';
import { mdiArrowRightDropCircle, mdiAccount, mdiArrowDownDropCircle, mdiBook, mdiLogout, mdiHome, mdiWallet } from '@mdi/js';
import axios from 'axios';
import UserAvatarDisplay from './UserAvatarDisplay.vue';
import CollapsDetails from './Collaps.vue';

export default {
    name: 'UserProfile',
    components: { SvgIcon, UserAvatarDisplay, CollapsDetails },
    data() {
        return {
            gotosavings: mdiArrowRightDropCircle,
            cir: mdiArrowDownDropCircle,
            acc: mdiAccount,
            add: mdiBook,
            logouticon: mdiLogout,
            home: mdiHome,
            passbook: mdiWallet,
            activeIndex: 0,
            addresses: [],
            showPopup: false,
            previewUrl: null,
            selectedFile: null,
            uploadMessage: "",
            user: {
                accountNum: '',
                bal: '',
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
                gender: ''
            }
        }
    },
    mounted() {
        this.showProfileDetails(); // Set default view
        const storedData = localStorage.getItem("user-login-info");
        window.addEventListener("resize", this.checkMobile);
        document.addEventListener("click", this.handleClickOutside);

        if (storedData) {
            try {
                const userData = JSON.parse(storedData);
                console.log(`user : `, userData.id);
                this.user.firstName = userData.firstName;
                this.user.fullname = userData.firstName + " " + userData.lastName;
                this.user.email = userData.email;
                this.user.phoneNumber = userData.phoneNumber;
                this.user.accountNum = userData.accNo;
                console.log("Welcome, " + this.user);
            } catch (error) {
                console.error("Error!! parsing user data:", error);
            }
        } else {
            console.log("No user data found.");
        }
        this.fetchAddresses();
        this.fetchAccInfo();
    },
    methods: {
        onImageSelected(event) {
            const file = event.target.files[0];
            if (file && file.type.startsWith("image/")) {
                this.selectedFile = file;
                this.previewUrl = URL.createObjectURL(file);
            } else {
                this.selectedFile = null;
                this.uploadMessage = "Please select a valid image file.";
            }
        },
        async uploadAvatar() {
            if (!this.selectedFile) return;
            const formData = new FormData();
            formData.append("file", this.selectedFile);

            try {
                await axios.post(
                    `http://192.168.1.4.nip.io:8088/api/users/${this.user.accountNum}/avatar`,
                    formData,
                    {
                        headers: {
                            "Content-Type": "multipart/form-data"
                        }
                    }
                );
                this.uploadMessage = "Upload successful!";
                // Optionally: emit an event or refresh the displayed avatar
                this.$emit("avatar-updated");
            } catch (err) {
                this.uploadMessage = "Upload failed: " + err.response?.data || err.message;
            }
        },
        showProfileDetails() {
            this.activeIndex = 0;
        },
        showAddressBook() {
            this.activeIndex = 1;
        },
        showAcc() {
            this.activeIndex = 2;
        },
        fetchAddresses() {
            try {
                const storedData = localStorage.getItem("user-login-info");
                const userData = JSON.parse(storedData);

                this.user.addressLine1 = userData.addressLine1;
                this.user.addressLine2 = userData.addressLine2;
                this.user.city = userData.city;
                this.user.state = userData.state;
                this.user.country = userData.country;
                this.user.pin = userData.pin;

            } catch (error) {
                console.error('Error fetching addresses:', error);
            }
        },
        async fetchAccInfo() {
            try {
                // Fetch user data from backend
                console.log(this.user.accountNum);
                const loginresponse = await axios.get("http://192.168.1.4.nip.io:8088/api/user/BalanceEnquiry", {
                    params: { accountNumber: this.user.accountNum }
                });
                console.log(loginresponse.data.accountInfo.accBalance);
                this.user.bal = loginresponse.data.accountInfo.accBalance;
            } catch (error) {
                if (error.response && error.response.status === 401) {
                    // invalid credentials
                    console.warn("Invalid credentials");
                } else {
                    console.error("Error fetching account details:", error);
                }
            }
        },
        refreshAvatar() {
            // Force the UserAvatarDisplay to reload after upload
            this.$refs.avatarDisplay.fetchAvatar();
        },
        logout() {
            // Clear user session
            localStorage.removeItem("user-login-info");

            // Redirect to signin page
            this.$router.push("/Sign-In");
        },
        goToHome() {
            // Redirect to signin page
            this.$router.push("/User/Home");
        }
    }
};
</script>

<style>
@import url("https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap");

@import url("https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css");

#accounts {
    white-space: pre-line;
}

/* ............/navbar/............ */
.navprofile {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: #fff;
    padding: 15px 30px;
    color: var(--primary);
    opacity: 0.85;
    height: 50px;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    z-index: 999;
}

.savingsnav {
    display: flex;
    justify-content: space-between;
    cursor: pointer;
    font-weight: var(--font-semi-bold);
    font-size: var(--normal-font-size);
    color: var(--dark-text);
}

.savingsnav:hover {
    text-decoration: none;
}

/*====== FLEX for list group ======*/
.container {
    max-width: 1200px;
    width: 100%;
    margin: 10px auto;
}

.mycard {
    border: 1px solid var(--primary);
    border-radius: 20px;
    border-left: 5px solid var(--accent);
    padding: 1em 1em;
}

.detailscard {
    border: 2px solid var(--primary);
    border-radius: 20px;
    padding: 2em;
}

@media (max-width: 768px) {
    .mycard {
        margin-bottom: var(--mb-2);
    }

}

@media (max-width: 500px) {
    .mycard {
        margin-bottom: var(--mb-2);
    }

    .detailscard {
        padding: 0.2em;
    }
}

.accdetails {
    border: 2px solid var(--primary);
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    border-radius: 20px;
    padding: 0.3em 1em;
}

/*======= LEFT SECTION BUTTONS =======*/
.list-group {
    list-style: none;
    padding: 0;
    display: flex;
    flex-direction: column;
    gap: 10px;
    align-items: center;
}

.mybtn {
    display: inline-block;
    padding: 0.5em 1.5em;
    text-decoration: none;
    cursor: pointer;
    min-width: 250px;
}

.mybtn:hover {
    transition: background 0.3s ease-in-out;
    text-decoration: none;
}

.mybtn.active {
    text-transform: uppercase;
    color: var(--white);
    background: var(--accent);
    border-left: 5px solid var(--primary);
}

.mybtn.active:hover {
    transition: background 0.3s ease-in-out;
}

.list-group-item {
    padding: 10px;
    cursor: pointer;
    background-color: var(--card-bg);
    transition: background 0.3s;
}

.list-group-item.active {
    background: var(--accent);
    font-weight: bold;
}

/*======= CHANGE PROFILE IMAGE ======== */

.updateProfile {
    display: inline-block;
    padding: 0.5em 1.5em;
    text-decoration: none;
    cursor: pointer;
    min-width: 250px;
}

.updateProfile:hover {
    color: var(--error);
}

.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.6);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 999;
}

.modal-content {
    background: var(--card-bg);
    padding: 2rem;
    border-radius: 6px;
    width: 400px;
    text-align: center;
    position: relative;
    background: var(--bg);
    border-radius: 24px;
    box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
    border: 1px solid var(--primary);
}

.image-preview {
    margin-top: 15px;
}

.image-preview img {
    width: 100%;
    height: auto;
    border-radius: 5px;
}

.success-btn {
    padding: 10px 20px;
    background-color: var(--success);
    /* green for success */
    color: var(--white-text);
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-weight: bold;
    transition: background-color 0.3s ease;
    margin-top: 15px;
    border: none;
    padding: 5px;
    border-radius: 50px;
}

.success-btn.disabled-btn {
    background-color: var(--bg-grey);
    cursor: not-allowed;
    color: var(--dark-text);
}

.close-btn {
    margin-top: 15px;
    background: var(--error);
    color: var(--white-text);
    border: none;
    padding: 5px;
    border-radius: 50px;
    cursor: pointer;
}
</style>