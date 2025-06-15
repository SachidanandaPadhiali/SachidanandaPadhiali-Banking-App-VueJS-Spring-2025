<template>
    <header class="savingsheader">
        <router-link to="/User/Profile" class="header-content">
            <svg-icon style="color:white; height: 30px; width:30px;" type="mdi" :path="gotoprofile" />
            <h2 class="savingstext">Savings Account</h2>
        </router-link>
    </header>
    <div id="SavingsAccount" class="container">
        <div class="main-body">
            <div class="row">
                <div id="profileDetails" class="savnavdetails">
                    <p>Savings Account</p>
                    <p>{{ user.accountNum }}</p>
                    <p>Available Balance: {{ user.bal }}</p>
                </div>
            </div>
            <hr>

            <CollapsDetails title="Show Account Details" id="accounts">
                <hr>
                <div class="feature-comparison">
                    <div class="comparison-table">
                        <table class="table table-hover mb-0">
                            <tbody>
                                <tr>
                                    <td><strong>Account Holder</strong></td>
                                    <td>{{ user.fullname }}</td>
                                </tr>
                                <tr>
                                    <td><strong>Branch IFSC</strong></td>
                                    <td>{{ user.ifsc }}</td>
                                </tr>
                                <tr>
                                    <td><strong>Branch</strong></td>
                                    <td>{{ user.branch }}</td>
                                </tr>
                                <tr>
                                    <td><strong>Status</strong></td>
                                    <td>{{ user.accStatus }}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

            </CollapsDetails>
            <hr>

            <CollapsDetails title="Statement" id="accounts">
                <hr>
                <ul>
                    <li v-for="(transaction, index) in displayedTransactions" :key="index">
                        {{ transaction }}
                    </li>
                </ul>
                <hr>
                <div class="statement">
                    <a class="moreless" v-if="showMoreEnabled" @click="showMore">Show More Transactions<svg-icon
                            style="color:var(--dark-text); height: 20px; width:20px;" type="mdi" :path="more" /></a>
                    <a class="moreless" v-if="showLessEnabled" @click="showLess">Show Less Transactions<svg-icon
                            style="color:var(--dark-text); height: 20px; width:20px;" type="mdi" :path="less" /></a>
                    <button class='rsbtn'>Request Statement</button>
                </div>
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
            showPopup: false,
            previewUrl: null,
            selectedFile: null,
            uploadMessage: "",
            user: {
                id: '',
                accountNum: '',
                fullname: '',
                accStatus: 'ACTIVE',
                bal: '',
                firstName: '',
                lastName: '',
                email: '',
                phoneNumber: '',
                nominee: '',
                ifsc:'',
                branch:''
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
            recordsToDedut: 20,
            maxRecordsToShow: 30
        }
    },
    mounted() {
        const storedData = localStorage.getItem("user-login-info");

        if (storedData) {
            try {
                const userData = JSON.parse(storedData);
                console.log(`user : `, userData.id);
                this.user.id = userData.id;
                this.user.firstName = userData.firstName;
                this.user.fullname = userData.firstName + " " + userData.lastName;
                this.user.email = userData.email;
                this.user.phoneNumber = userData.phoneNumber;
                this.user.accountNum = userData.accNo;
                this.user.ifsc = userData.ifsc,
                this.user.branch = userData.bankAddress
                console.log("Welcome, " + this.user);
            } catch (error) {
                console.error("Error!! parsing user data:", error);
            }
        } else {
            console.log("No user data found.");
        }
        this.showMore();
        this.fetchBank();
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
                this.displayedTransactions.splice(this.recordsToShow, this.recordsToDedut);
            }
        },
        async fetchBank() {
            console.log('User ID ::: ',this.user);
            try {
                const bankDetails = await axios.get("http://192.168.1.4.nip.io:8088/api/user/getIFSC", {
                    params: { accountNumber: this.user.id }
                });
                console.log('Bank Details ::: ',bankDetails.data);
            } catch (error) {
                console.error('Error fetching addresses:', error);
            }
        },
        async fetchAccInfo() {
            try {
                // Fetch user data from backend
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
    margin: 5px 0;
    justify-content: center;
    font-size: var(--normal-font-size);
    color: var(--white-text);
}

.savnavdetails {
    padding-left: 5%;
}

.savnavdetails p,
.savnavdetails h3 {
    padding: 0;
    margin: 0;
    text-align: left;
}

.main-body hr {
    margin-top: 2%;
}


.table {
    width: 100%;
    border-collapse: collapse;
    font-size: 0.875rem;
}

.table td {
    padding: 0.5rem 0.5rem;
    border: none;
}

.table tr {
    transition: all .3s ease;
}

.table tr:last-child td {
    border-bottom: none;
}

.statement {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.moreless {
    margin: 1rem 0;
    font-size: var(--normal-font-size);
    border: none;
    color: var(--brown);
    background: transparent;
    font-weight: bold;
    cursor: pointer;
    border-bottom: 2px solid transparent;
    text-decoration: none;
    display: block;
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

.rsbtn {
    position: relative;
    z-index: 0;
    overflow: hidden;
    width: fit-content;
    padding: 5px;
    color: var(--dark-text);
    text-decoration: none;
    font-weight: var(--font-normal);
    font-size: var(--normal-font-size);
    cursor: pointer;
    border-radius: 50px;
    transition: all 0.4s ease;
}

.rsbtn:hover {
    background-color: var(--primary);
    letter-spacing: 2px;
    color: var(--white-text);
}


/*====== FLEX for list group ======*/
.container {
    max-width: 1200px;
    width: 100%;
    margin: 70px auto;
}
</style>