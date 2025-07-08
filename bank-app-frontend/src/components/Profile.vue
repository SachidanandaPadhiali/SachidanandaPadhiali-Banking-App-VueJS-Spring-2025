<template>
    <div class="container">
        <div class="main-body">
            <div v-if="isMobileScreen">
                <svg-icon v-if="!mobileMenuOpen" class="hamburger-icon" type="mdi" :path="menuToggle"
                    @click="toggleMobileMenu" />
                <!-- Mobile Navigation -->
                <div class="mobile-nav-wrapper" :class="{ expanded: mobileMenuOpen }">
                    <div class="mobile-nav-overlay" @click.self="toggleMobileMenu">
                        <div class="mobile-nav-content">
                            <span class="mobile-nav-close" @click="toggleMobileMenu">
                                <svg-icon type="mdi" :path="menuclose" />
                            </span>
                            <div class="logo-box">
                                <router-link to="/" class="logo">
                                    <img style="height: 50px; padding:0px; margin: 0px;"
                                        src="../assets/img/logoblack.png" alt="Logo" />
                                </router-link>
                            </div>
                            <div class="d-flex flex-column align-items-center text-center">
                                <div class="mt-3">
                                    <h4>{{ user.firstName }}</h4>
                                    <p class="text-secondary mb-1">{{ user.email }}</p>
                                    <p class="text-muted font-size-sm">{{ user.phoneNumber }}</p>
                                    <p class="text-secondary mb-1">{{ formattedDate }}</p>
                                </div>
                            </div>
                            <div class="list-group list-group-flush text-center mt-4">
                                <a href="#" @click="goToHome" class="btn mybtn">
                                    <svg-icon type="mdi" :path="home" />
                                    HomePage
                                </a>
                                <a href="#" @click="logout" class="btn mybtn">
                                    <svg-icon type="mdi" :path="logouticon" />
                                    Sign out
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <CollapsDetails title="Personal Details">
                    <div class="accdetails">
                        <div class="mycard-body">
                            <h4>{{ user.fullname }}</h4>
                            <p><strong>Email:</strong> {{ user.email }}</p>
                            <p><strong>Phone No.:</strong> {{ user.phoneNumber }}</p>
                            <hr>
                            <h5>Address Book</h5>
                            <hr>
                            <p>{{ user.addressLine1 }}, {{ user.addressLine2 }}, {{ user.city }}</p>
                            <p>{{ user.state }} - {{ user.pin }}</p>
                            <p>Mobile: {{ user.phoneNumber }}</p>
                        </div>
                    </div>
                </CollapsDetails>

                <div class="detailscard">
                    <div class="mycard-body">
                        <h5>Account Information</h5>
                        <hr>
                        <h3>{{ user.fullname }}</h3>
                        <div class="savingsnav">
                            <div class="savingsnav1">
                                Savings Account<br>
                                <p>Account Balance</p>
                                <span v-if="showBal" @click="showBalance()">₹{{ user.bal }}</span>
                                <span v-else @click="showBalance()">XX XXX <svg-icon class="myicons"
                                        style="margin-left: 20px;" type="mdi" :path="eye" />
                                </span>
                            </div>

                            <router-link to="/User/savings"><svg-icon class="myicons" type="mdi" style="margin: 20px;"
                                    :path="gotosavings" /></router-link>
                        </div>
                    </div>
                </div>

                <CollapsDetails title="Fixed Deposits" id="accounts">
                </CollapsDetails>
                <CollapsDetails title="Recurring Deposits" id="accounts">
                </CollapsDetails>

            </div>
            <div v-else class="row">
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
                                <p class="text-secondary mb-1">{{ formattedDate }}</p>
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
                            <div class="savingsnav">
                                <div class="savingsnav1">
                                    Savings Account<br>
                                    <p>Account Balance</p>
                                    <span v-if="showBal" @click="showBalance()">₹{{ user.bal }}</span>
                                    <span v-else @click="showBalance()">XX XXX <svg-icon class="myicons"
                                            style="margin-left: 20px;" type="mdi" :path="eye" />
                                    </span>
                                </div>

                                <router-link to="/User/savings"><svg-icon class="myicons" type="mdi"
                                        :path="gotosavings" /></router-link>
                            </div>
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
import { mdiMenu, mdiEyeCircle, mdiCloseCircle, mdiArrowRightDropCircle, mdiAccount, mdiArrowDownDropCircle, mdiBook, mdiLogout, mdiHome, mdiWallet } from '@mdi/js';
import axios from 'axios';
import UserAvatarDisplay from './UserAvatarDisplay.vue';
import CollapsDetails from './Collaps.vue';

export default {
    name: 'UserProfile',
    components: { SvgIcon, UserAvatarDisplay, CollapsDetails },
    data() {
        return {
            windowWidth: window.innerWidth,
            isMobileNavOpen: false,
            mobileMenuOpen: false,
            apiUrl: `${process.env.VUE_APP_API_URL}/api`,
            menuToggle: mdiMenu,
            menuclose: mdiCloseCircle,
            eye: mdiEyeCircle,
            gotosavings: mdiArrowRightDropCircle,
            cir: mdiArrowDownDropCircle,
            close: mdiCloseCircle,
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
            showBal: false,
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
                gender: '',
                lastLogIn: ''
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

                this.user.firstName = userData.firstName;
                this.user.fullname = userData.firstName + " " + userData.lastName;
                this.user.email = userData.email;
                this.user.phoneNumber = userData.phoneNumber;
                this.user.accountNum = userData.accNo;
                this.user.lastLogIn = userData.lastLogIn;
            } catch (error) {
                console.error("Error!! parsing user data:", error);
            }
        } else {
            console.log("No user data found.");
        }
        this.fetchAddresses();
        this.fetchAccInfo();
    },
    computed: {
        isMobileScreen() {
            return this.windowWidth <= 768; // Mobile screens typically have widths of 768px or less
        },
        formattedDate() {
            const logInTime = new Date(this.user.lastLogIn); // Create a Date object
            return logInTime.toLocaleString('en-US', {
                year: 'numeric',
                month: 'long',
                day: 'numeric',
                hour: '2-digit',
                minute: '2-digit',
            });
        }
    },
    created() {
        // Add event listener for window resize
        window.addEventListener('resize', this.handleResize);
    },
    beforeUnmount() {
        // Clean up the event listener when the component is destroyed
        window.removeEventListener('resize', this.handleResize);
    },
    methods: {
        handleResize() {
            this.windowWidth = window.innerWidth;
        },
        toggleMobileMenu() {
            this.mobileMenuOpen = !this.mobileMenuOpen;
        },
        toggleSubmenu(key) {
            // Toggle only the requested submenu; collapse others:
            this.submenuOpen[key] = !this.submenuOpen[key];
            Object.keys(this.submenuOpen).forEach(k => {
                if (k !== key) this.submenuOpen[k] = false;
            });
        },
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
                    `${this.apiUrl}/users/${this.user.accountNum}/avatar`,
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
        showBalance() {
            this.showBal = true;
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

                const loginresponse = await axios.get(`${this.apiUrl}/user/BalanceEnquiry`, {
                    params: { accountNumber: this.user.accountNum }
                });
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

.hamburger-icon {
    font-size: 30px;
    background: transparent;
    border: none;
    cursor: pointer;
    position: fixed;
    top: 20px;
    left: 20px;
    z-index: 1001;
}

.mobile-nav-close {
    display: inline-block;
    font-size: 24px;
    cursor: pointer;
    text-align: right;
    width: 100%;
}

/* Full screen overlay when the mobile menu is open */
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

.mobile-nav-content {
    background-color: #fff;
    width: 80%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    gap: 5vh;
    transform: translateX(100%);
    transition: transform 0.3s ease-in-out;
    /* Smooth sliding effect */
    padding: 20px;
    padding-bottom: 20%;
}

.mobile-nav-overlay .mobile-nav-content {
    transform: translateX(0);
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

.savingsnav1 {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    font-weight: var(--font-semi-bold);
    font-size: var(--normal-font-size);
    color: var(--dark-text);
    padding: 5px 20px;
}

.savingsnav1 p {
    color: var(--light-text);
    font-weight: var(--font-slim);
    font-size: var(--smaller-font-size);
    margin-bottom: 0;
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
    padding: 0.5em;
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

@media (max-width: 900px) {
    .mybtn {
        padding: 5px;
        min-width: 100%;
    }

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
        padding: 0.5em;
    }

    .accdetails {
        border: none;
        border-bottom: 3px solid var(--primary);
        border-radius: 5px;
    }
}
</style>