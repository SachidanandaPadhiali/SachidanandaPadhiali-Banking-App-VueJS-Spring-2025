<template>
    <div>
        <div v-if="loading" class="loader-container">
            <div class="loader-grid">
                <div v-for="(delay, idx) in delays" :key="idx" class="loader-square" :style="{
                    '--delay': delay + 'ms',
                    background: colors[idx],
                    top: positions[idx].top,
                    left: positions[idx].left
                }"></div>
            </div>
        </div>

        <div v-else class="logo-container">
            <img src="../assets/img/logoonly.png" alt="My Bank Logo" />
        </div>
    </div>
</template>

<script>
export default {
    name: "PreLoader",
    data() {
        return {
            loading: true,
            delays: [0, 300, 600, 900, 1200, 1500],
            colors: [
                "var(--primary)",
                "var(--black)",
                "var(--black)",
                "var(--primary)",
                "var(--accent)",
                "var(--primary)"
            ],
            positions: [
                { top: "0px", left: "90px" },
                { top: "40px", left: "50px" },
                { top: "40px", left: "130px" },
                { top: "80px", left: "10px" },
                { top: "80px", left: "90px" },
                { top: "80px", left: "170px" }
            ]
        };
    },
    mounted() {
        const TOTAL_DURATION = 2100
        setTimeout(() => { this.loading = false; }, TOTAL_DURATION);
        setTimeout(() => {
            this.showText = true;
            this.$router.push('/HomePage'); // Navigate to Home after loading
        }, 2500);
    }
};
</script>

<style>
.loader-container {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background: var(--bg-white);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 9999;
}

.loader-grid {
    position: relative;
    width: 240px;
    height: 180px;
}

.loader-square {
    width: 50px;
    height: 50px;
    border-radius: 4px;
    position: absolute;
    opacity: 0;
    transform-origin: center center;
    transform: rotate(45deg) scale(0.5);
    animation: appear 300ms ease-in-out forwards;
    animation-delay: var(--delay);
}


@keyframes appear {
    to {
        opacity: 1;
        transform: rotate(45deg) scale(1);
    }
}

.logo-container {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    margin-top: 10px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    z-index: 9000;
    opacity: 0;
    animation: fadeInLogo 0ms ease-in-out forwards 1ms;
}

@keyframes fadeInLogo {
    to {
        opacity: 1;
    }
}

.logo-container img {
    max-width: 250px;
    width: 100%;
}
</style>
