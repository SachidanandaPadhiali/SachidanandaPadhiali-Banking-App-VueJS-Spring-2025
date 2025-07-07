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
                <table class="transactionTable">
                    <tbody>
                        <template v-for="(transaction, index) in displayedTransactions" :key="index">
                            <!-- ROW 1 -->
                            <tr class="transactionMeta">
                                <td class="txDate">{{ formattedDate(transaction.transactionTime) }}</td>

                                <td class="txFromTo" style="text-align: left;">
                                    <span v-if="transaction.transactionType === 'DEBIT'">TO {{ transaction.destAcc
                                    }}</span>
                                    <span v-else-if="transaction.transactionType === 'CREDIT'">FROM {{
                                        transaction.sourceAcc }}</span>
                                    <span v-else>&nbsp;</span>
                                </td>

                                <td class="td-right txAmt">{{ transaction.transactionAmt }}</td>
                            </tr>

                            <!-- ========== LINE 2 ========== -->
                            <tr class="transactionMeta">
                                <td colspan="3">
                                    <div class="meta-flex">
                                        <div class="tx-type">
                                            <div style="display: flex; flex-direction: row;">
                                                <p v-if="transaction.transactionType == 'DEBIT'"><svg-icon
                                                        style="color:red;" type="mdi" :path="debitOut" /></p>
                                                <p v-else-if="transaction.transactionType == 'CREDIT'"><svg-icon
                                                        style="color:green;" type="mdi" :path="creditIn" /></p>
                                                <p v-else-if="transaction.transactionType == 'DEPOSIT'">🟢</p>
                                                <p v-else-if="transaction.transactionType == 'WITHDRAW'">🔴</p>
                                                <span>&nbsp;{{ transaction.transactionType }}</span>
                                            </div>
                                            <div class="txn-id mobile-only">TXN ID: {{ transaction.transactionId }}
                                            </div>
                                        </div>
                                        <div class="txn-id desktop-only">TXN ID: {{ transaction.transactionId }}</div>
                                        <div class="tx-status">{{ transaction.transactionStatus }}</div>
                                    </div>
                                </td>
                            </tr>
                        </template>
                    </tbody>
                </table>
                    <hr />
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
            const formatter = new Intl.DateTimeFormat('en-GB', {
                dateStyle: 'medium',
                timeStyle: 'short'
            });
            return formatter.format(logInTime);
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

.container {
    max-width: 1200px;
    width: 100%;
    margin: 70px auto;
}

.table td {
    border: none;
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

.transactionTable {
    width: 100%;
    border-collapse: collapse;
    font-size: var(--normal-font-size);
}

.transactionRow {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    flex-wrap: nowrap;
    justify-content: space-between;
}

.txDate {
    font-weight: var(--font-semi-bold);
    width: 35%;
}

.txfromTo {
    width: 45%;
}

.txAmt {
    font-weight: var(--font-semi-bold);
    font-size: var(--h1-font-size);
}

.td-right {
    justify-content: flex-end;
    text-align: right;
}

.meta-flex {
    display: flex;
    justify-content: space-between;
    flex-wrap: wrap;
    align-items: center;
    flex-wrap: nowrap;
    gap: 0.5rem;
    padding-bottom: 5px;
    border-bottom: 1px solid var(--border);
}

.txn-id {
    font-size: 0.9rem;
    color: var(--text-secondary);
}

.tx-status {
    text-align: right;
    font-weight: bold;
}

.tx-type {
    flex-basis: 30%;
    display: flex;
    flex-direction: row;
}

.tx-type p {
    height: 20px;
    width: 20px;
    margin: 0px;
    padding: 0px;
}

.mobile-only {
    display: none;
}

.desktop-only {
    flex-basis: 50%;
    display: inline;
    text-align: left;
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


@media (max-width: 768px) {

    .transactionTable {
        width: 100%;
        border-collapse: collapse;
        font-size: var(--small-font-size);
    }

    .transactionTable td {
        padding: 0px;
    }

    .txAmt {
        font-size: var(--h2-font-size);
    }

    .meta-flex {
        flex-direction: row;
        align-items: flex-start;
        gap: 0;

    }

    .tx-type {
        flex-direction: column;
    }

    .mobile-only {
        display: block;
    }

    .desktop-only {
        display: none;
    }

    .tx-type,
    .txn-id {
        flex-basis: auto;
        width: 100%;
        overflow: visible;
        text-overflow: unset;
        text-align: left;
    }
}
</style>