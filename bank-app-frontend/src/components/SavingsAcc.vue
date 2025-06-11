<template>
    <header class="savingsheader">
        <router-link to="/User/Profile" class="header-content">
            <svg-icon style="color:white; height: 50px; width:50px;" type="mdi" :path="gotoprofile" />
            <p class="savingstext">Savings Account</p>
        </router-link>
    </header>
    <div id="SavingsAccount" class="container">
        <div class="main-body">
            <div class="row">
                <div id="profileDetails" class="savnavdetails">
                    <p>Savings Account</p>
                    <p>{{ user.accountNum }}</p>
                </div>
            </div>
            <hr>

            <CollapsDetails title="Show Account Details" class="savaccdetails" id="accounts">
                <hr>
                <p><strong>Account Number:</strong> 1234567890</p>
                <p><strong>Balance:</strong> ₹0.00</p>
                <p><strong>Status:</strong> Active</p>
            </CollapsDetails>

            <CollapsDetails title="Statement" class="savaccdetails" id="accounts">
                <hr>
                <ul>
                    <li v-for="(transaction, index) in displayedTransactions" :key="index">
                        {{ transaction }}
                    </li>
                </ul>
                <hr>
                <a class="moreless" v-if="showMoreEnabled" @click="showMore">Show More Transactions<svg-icon
                        style="color:var(--dark-text); height: 20px; width:20px;" type="mdi" :path="more" /></a>
                <a class="moreless" v-if="showLessEnabled" @click="showLess">Show Less Transactions<svg-icon
                        style="color:var(--dark-text); height: 20px; width:20px;" type="mdi" :path="less" /></a>
            </CollapsDetails>

        </div>
    </div>

</template>

<script>
import SvgIcon from '@jamescoyle/vue-icon';
import { mdiArrowLeftDropCircleOutline, mdiArrowDownDropCircleOutline, mdiArrowUpDropCircleOutline } from '@mdi/js';
import axios from 'axios';
import CollapsDetails from './Collaps.vue';

export default {
    name: 'SavingsAcc',
    components: { SvgIcon, CollapsDetails },
    data() {
        return {
            gotoprofile: mdiArrowLeftDropCircleOutline,
            less: mdiArrowUpDropCircleOutline,
            more: mdiArrowDownDropCircleOutline,
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
            },
            allTransactions: [
                "Transaction 1", "Transaction 2", "Transaction 3", "Transaction 4", "Transaction 5",
                "Transaction 6", "Transaction 7", "Transaction 8", "Transaction 9", "Transaction 10",
                "Transaction 11", "Transaction 12", "Transaction 13", "Transaction 14", "Transaction 15",
                "Transaction 16", "Transaction 17", "Transaction 18", "Transaction 19", "Transaction 20",
                "Transaction 21", "Transaction 22", "Transaction 23", "Transaction 24", "Transaction 25",
                "Transaction 26", "Transaction 27", "Transaction 28", "Transaction 29", "Transaction 30",
                "Transaction 11", "Transaction 12", "Transaction 13", "Transaction 14", "Transaction 15",
                "Transaction 16", "Transaction 17", "Transaction 18", "Transaction 19", "Transaction 20",
                "Transaction 21", "Transaction 22", "Transaction 23", "Transaction 24", "Transaction 25",
                "Transaction 26", "Transaction 27", "Transaction 28", "Transaction 29", "Transaction 30"
            ],
            displayedTransactions: [],
            recordsToShow: 10,
            recordsCount: 10,
            maxRecordsToShow: 30
        }
    },
    mounted() {
        const storedData = localStorage.getItem("user-login-info");

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
        this.showMore();
        this.fetchAddresses();
        this.fetchAccInfo();
    },
    computed: {
        showMoreEnabled() {
            return this.displayedTransactions.length < 30;
        },
        showLessEnabled() {
            return this.displayedTransactions.length == 30;
        }
    },
    methods: {
        showMore() {
            const nextRecords = this.allTransactions.slice(
                this.displayedTransactions.length,
                this.displayedTransactions.length + this.recordsToShow
            );
            this.displayedTransactions.push(...nextRecords);
        },
        showLess() {
            if (this.displayedTransactions.length >= this.maxRecordsToShow) {
                this.displayedTransactions.splice(-this.recordsToShow, this.recordsToShow);
            }
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
.savingsheader {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    display: flex;
    align-items: center;
    background-color: var(--primary);
    /* Adjust to your preferred header color */
    margin-bottom: 10px;
    z-index: 1000;
}

.header-content {
    text-decoration: none;
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 10px;
}

.savingstext {
    justify-content: center;
    font-size: var(--normal-font-size);
    color: var(--white-text);
}

.savnavdetails {
    padding-left: 5%;
}

.savnavdetails p {
    padding: 0;
    margin: 0;
    text-align: left;
}

.main-body hr {
    margin-top: 2%;
}

.moreless {
    width: 110px;
    margin-top: 1rem;
    font-family: inherit;
    font-size: 1rem;
    text-align: left;
    padding: 0;
    text-transform: capitalize;
    border: none;
    color: var(--brown);
    background: transparent;
    font-weight: bold;
    cursor: pointer;
    border-bottom: 2px solid transparent;
    position: relative;
    padding-right: 1.5rem;
    text-decoration: none;
}

.moreless::before {
    content: "";
    position: absolute;
    width: 18px;
    height: 12px;
    background: var(--brown);
    right: 0;
    top: 48%;
    transform: translateY(-50%);
    clip-path: polygon(0 0, 50% 100%, 100% 0);
}


.moreless:hover {
    text-decoration: none;
}


/*====== FLEX for list group ======*/
.container {
    max-width: 1200px;
    width: 100%;
    margin: 70px auto;
}

.savaccdetails {
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}
</style>