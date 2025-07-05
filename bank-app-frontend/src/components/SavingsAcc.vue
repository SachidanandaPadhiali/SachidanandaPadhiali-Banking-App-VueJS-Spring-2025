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

            </CollapsDetails>
            <hr>

            <CollapsDetails title="Statement" id="accounts" v-model:expanded="isStatementExpanded">
                <hr>
                <ul>
                    <li v-for="(transaction, index) in displayedTransactions" :key="index" class="transaction">
                        <table class="transactionTable">
                            <tbody>
                                <tr>
                                    <td class="txDate">{{ formattedDate(transaction.transactionTime) }}</td>

                                    <!--                                    <td class="txFromTo" v-if="transaction.transactionType == 'DEBIT'">TO {{
                                        transaction.destAcc }}</td>
                                    <td class="txFromTo" v-else-if="transaction.transactionType == 'CREDIT'">FROM {{
                                        transaction.sourceAcc }} </td>
                                    <td class="txFromTo" v-else></td>-->

                                    <td class="td-right txAmt" colspan="2">{{ transaction.transactionAmt }}</td>
                                </tr>
                                <tr>
                                    <td class="stack-td" v-if="transaction.transactionType == 'DEBIT'">
                                        <svg-icon style="color:red; height: 20px; width:20px;" type="mdi"
                                            :path="debitOut" />{{ transaction.transactionType }}
                                    </td>
                                    <td class="stack-td" v-else-if="transaction.transactionType == 'CREDIT'">
                                        <svg-icon style="color:green; height: 20px; width:20px;" type="mdi"
                                            :path="creditIn" />{{ transaction.transactionType }}
                                    </td>
                                    <td class="stack-td" v-else-if="transaction.transactionType == 'DEPOSIT'">🟢{{
                                        transaction.transactionType }}</td>
                                    <td class="stack-td" v-else-if="transaction.transactionType == 'WITHDRAW'">🔴{{
                                        transaction.transactionType }}</td>

                                    <td class="stack-td">TXN ID : {{ transaction.transactionId }} </td>

                                    <td class="td-right"> {{ transaction.transactionStatus }} </td>
                                </tr>
                            </tbody>
                        </table>
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
import { mdiArrowBottomLeft, mdiArrowTopRight, mdiArrowLeftDropCircleOutline, mdiArrowDownDropCircleOutline, mdiArrowUpDropCircleOutline } from '@mdi/js';
import axios from 'axios';
import CollapsDetails from './Collaps.vue';

export default {
    name: 'SavingsAcc',
    components: { SvgIcon, CollapsDetails },
    data() {
        return {
            windowWidth: window.innerWidth,
            apiUrl: `${process.env.VUE_APP_API_URL}/api`,
            gotoprofile: mdiArrowLeftDropCircleOutline,
            less: mdiArrowUpDropCircleOutline,
            more: mdiArrowDownDropCircleOutline,
            creditIn: mdiArrowBottomLeft,
            debitOut: mdiArrowTopRight,
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
                ifsc: '',
                branch: ''
            },
            allTransactions: [],
            isStatementExpanded: false,
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

                this.user.id = userData.id;
                this.user.fullname = userData.firstName + " " + userData.lastName;
                this.user.accountNum = userData.accNo;
                this.user.ifsc = userData.ifsc;
                this.user.branch = userData.bankAddress

            } catch (error) {
                console.error("Error!! parsing user data:", error);
            }
        } else {
            console.log("No user data found.");
        }

        this.fetchBank();
        this.fetchAccInfo();
        this.fetchTransactions();
        this.initTransactions();
    },
    computed: {
        isMobileScreen() {
            return this.windowWidth <= 768; // Mobile screens typically have widths of 768px or less
        },
        showMoreEnabled() {
            return this.displayedTransactions.length < this.allTransactions.length && this.displayedTransactions.length < 30;
        },
        showLessEnabled() {
            return this.displayedTransactions.length == 30 || this.displayedTransactions.length >= this.allTransactions.length;
        }
    },
    watch: {
        isStatementExpanded(newVal) {
            if (newVal) {
                this.initTransactions();
            }
        }
    },
    methods: {
        async initTransactions() {
            await this.fetchTransactions();
            this.displayedTransactions = this.allTransactions.slice(0, this.recordsToShow); // now show first 10
        },

        showMore() {
            const nextRecords = this.allTransactions.slice(
                this.displayedTransactions.length,
                this.displayedTransactions.length + this.recordsToShow
            );
            this.displayedTransactions.push(...nextRecords);
        },
        showLess() {
            this.displayedTransactions = this.allTransactions.slice(0, 10);
        },
        async fetchBank() {

            try {
                await axios.get(`${this.apiUrl}/getIFSC`, {
                    params: { accountNumber: this.user.accountNum }
                });

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
        async fetchTransactions() {
            try {
                const response = await axios.get(`${this.apiUrl}/getTxs`, {
                    params: { accountNumber: this.user.accountNum }
                });
                this.allTransactions = response.data;

            } catch (error) {
                console.error('Error fetching Trsansactions:', error);
            }
        },
        formattedDate(trDate) {
            if (!trDate) return '';

            const logInTime = new Date(trDate);
            return logInTime.toLocaleString('en-US', {
                year: 'numeric',
                month: 'long',
                day: 'numeric',
                hour: '2-digit',
                minute: '2-digit',
                hour12: true, // Optional: 12-hour format
            });
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

.transactionTable {
    width: 100%;
    border-collapse: collapse;
    font-size: var(--normal-font-size);
}

.transactionTable td {
    padding: 0 0.5rem;
    border: none;
}

.txDate {
    font-weight: var(--font-semi-bold);
}

.txAmt {
    font-weight: var(--font-semi-bold);
    font-size: var(--h1-font-size);
}

.td-right {
    text-align: right;
}

.statement {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.transaction {
    list-style: none;
    padding: 5px;
    border-bottom: 1px dashed var(--border);
    /* subtle separator */
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
    padding: 5px 10px;
    color: var(--dark-text);
    text-decoration: none;
    font-weight: var(--font-normal);
    font-size: var(--normal-font-size);
    cursor: pointer;
    border-radius: 10px;
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

@media (max-width: 768px) {
    .table td {
        padding: 0.2rem 0.2rem;
        border: none;
    }

    .stack-td {
        display: block;
        width: 90%;
    }

    .transactionTable {
        width: 100%;
        border-collapse: collapse;
        font-size: var(--small-font-size);
    }

    .txAmt {
        font-size: var(--h2-font-size);
    }
}
</style>