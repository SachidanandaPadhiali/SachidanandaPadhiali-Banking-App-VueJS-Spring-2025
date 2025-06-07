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
                                <h4>{{ username }}</h4>
                                <p class="text-secondary mb-1">{{ email }}</p>
                                <p class="text-muted font-size-sm">{{ phno }}</p>
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
                                Profile Information
                            </a>
                            <a href="#" class="btn mybtn" :class="{ active: activeIndex === 1 }"
                                @click.prevent="showAddressBook">
                                <svg-icon type="mdi" :path="add" />
                                Address
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
                            <p><strong>Name:</strong> {{ fullname }}</p>
                            <p><strong>Email:</strong> {{ email }}</p>
                            <p><strong>Phone No.:</strong> {{ phno }}</p>
                        </div>
                    </div>

                    <div v-show="activeIndex === 1" id="addressBook" class="card">
                        <div class="card-body">
                            <h5>Address Book</h5>
                            <div id="addressList" class="address-list-container">
                                <h2>Your Saved Addresses</h2>

                                <div v-if="addresses.length === 0" class="no-address">
                                    No addresses saved yet.
                                </div>

                                <div v-else class="address-cards">
                                    <div v-for="(address, index) in addresses" :key="index" class="address-card">
                                        <h3>{{ address.name }}</h3>
                                        <p>{{ address.addressLine1 }}, {{ address.city }}</p>
                                        <p>{{ address.state }}, {{ address.country }} - {{ address.pin }}</p>
                                        <p>Mobile: {{ address.mobile }}</p>

                                        <div class="action-buttons">
                                            <button @click="editAddress(index)">Edit</button>
                                            <button @click="confirmAndDeleteAddress(address.addId)">Delete</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import SvgIcon from '@jamescoyle/vue-icon';
import { mdiAccount, mdiArrowDownDropCircle, mdiBook, mdiLogout, mdiHome } from '@mdi/js';
import axios from 'axios';
import UserAvatarDisplay from './UserAvatarDisplay.vue';

export default {
    name: 'UserProfile',
    components: { SvgIcon, UserAvatarDisplay },
    data() {
        return {
            cir: mdiArrowDownDropCircle,
            acc: mdiAccount,
            add: mdiBook,
            logouticon: mdiLogout,
            home: mdiHome,
            userId: '',
            fullname: "",
            username: "",
            email: "",
            activeIndex: 0,
            addresses: [],
            showPopup: false,
            previewUrl: null,
            selectedFile: null,
            uploadMessage: ""
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
                this.userId = userData.id;
                this.username = userData.username.split(" ")[0];
                this.fullname = userData.username;
                this.email = userData.email;
                this.phno = userData.phno;
                console.log("Welcome, " + userData.username);
            } catch (error) {
                console.error("Error!! parsing user data:", error);
            }
        } else {
            console.log("No user data found.");
        }
        this.fetchAddresses();
    },
    beforeUnmount() {
        window.removeEventListener("resize", this.checkMobile);
        document.removeEventListener("click", this.handleClickOutside);
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
                    `http://192.168.1.4.nip.io:8088/api/users/${this.userId}/avatar`,
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
        async fetchAddresses() {
            try {
                const response = await axios.get(`http://192.168.1.4.nip.io:8088/api/address/user/${this.userId}`);
                this.addresses = response.data || [];
                console.log(response.data);
            } catch (error) {
                console.error('Error fetching addresses:', error);
            }
        },
        async editAddress(addressIndex) {
            try {
                const editThisAddress = this.addresses[addressIndex];
                this.$refs.popupRef.showFilledPopup(editThisAddress);
                //await axios.post(`http://192.168.1.4.nip.io:8088/api/address/update/${addressId}`);
                // Remove from local list
            } catch (error) {
                console.error("Error deleting address:", error);
                alert("Failed to delete address.");
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

/*Logo*/
.logo {
    width: 20%;
    display: flex;
    align-items: center;
    height: 100%;
}

.logo a {
    display: flex;
    align-items: center;
    text-decoration: none;
    height: 100%;
    gap: 8px;
    /* Adds space between image and text */
}

.logo img {
    max-height: 200%;
}

@media (max-width: 600px) {
    .logo {
        width: 50%;
    }

    .logo img {
        max-height: 40px;
    }

    .detailscard {
        margin-top: 10px;
    }

}

@media (max-width: 500px) {
    .logo {
        width: 50%;
    }

    .logo img {
        max-height: 40px;
    }

    .detailscard {
        margin-top: 10px;
    }


}
.file-upload-wrapper {
  display: inline-block;
  position: relative;
}

.hidden-file-input {
  display: none;
}

.custom-file-label {
  background-color: var(--primary, #007bff);
  color: #fff;
  padding: 10px 30px;
  border-radius: 5px;
  font-weight: 500;
  font-family: "Open Sans", sans-serif;
  text-transform: uppercase;
  cursor: pointer;
  transition: background-color 0.3s ease;
  display: inline-block;
  text-align: center;
}

.custom-file-label:hover {
  background-color: var(--primary-dark, #0056b3);
}

/* FLEX for list group */
.container {
    max-width: 1200px;
    width: 90%;
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


/*======= Addresses =======*/
.address-list-container {
    padding: 20px;
}

.address-cards {
    display: flex;
    flex-wrap: wrap;
    gap: 1rem;
}

.address-card {
    background: var(--card-bg);
    padding: 16px;
    border-radius: 12px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
    width: 100%;
    max-width: 300px;
}

.action-buttons {
    margin-top: 12px;
}

.action-buttons button {
    margin-right: 8px;
    padding: 6px 12px;
    border: none;
    background-color: var(--bg-btn);
    color: var(--white-text);
    border-radius: 6px;
    cursor: pointer;
    font-weight: 600;
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
    cursor: pointer;
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