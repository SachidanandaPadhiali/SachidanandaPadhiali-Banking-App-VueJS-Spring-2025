<!-- src/components/ScrollToTop.vue -->
<template>
    <div class="scroll-to-top" :class="{ visible: scrollPercentage > 5 }" @click="scrollToTop">
        <div class="scroll-top-inner">
            <div class="scroll-bar">
                <div class="bar-inner" :style="{ width: scrollPercentage + '%' }"></div>
            </div>
            <span class="scroll-bar-text">Top</span>
        </div>
    </div>
</template>

<script>
export default {
    name: 'ScrollToTop',
    data() {
        return {
            scrollPercentage: 0
        }
    },
    mounted() {
        window.addEventListener('scroll', this.handleScrollbar);
        // Initialize in case the page is already scrolled
        this.handleScrollbar();
    },
    beforeUnmount() {
        // If you're on Vue 2, use beforeDestroy instead
        window.removeEventListener('scroll', this.handleScrollbar);
    },
    methods: {
        handleScrollbar() {
            // Calculate total document height
            const doc = document.documentElement;
            const body = document.body;
            const totalHeight = Math.max(
                body.scrollHeight, doc.scrollHeight,
                body.offsetHeight, doc.offsetHeight,
                body.clientHeight, doc.clientHeight
            );

            // How far user has scrolled from top
            const scrollTop = window.scrollY || window.pageYOffset || doc.scrollTop;

            // Viewport height
            const viewportHeight = window.innerHeight;

            // Compute percentage
            let percentage = ((scrollTop + viewportHeight) / totalHeight) * 100;
            if (percentage > 100) percentage = 100;
            this.scrollPercentage = percentage;
        },
        scrollToTop() {
            window.scrollTo({
                top: 0,
                behavior: 'smooth'
            });
        }
    }
}
</script>

<style scoped>
/*== Scroll To Top CSS ======*/
.scroll-to-top {
    position: fixed;
    right: -10px;
    bottom: 80px;
    transform: rotate(90deg);
    z-index: 99999999999;
    cursor: pointer;
}

.scroll-to-top .visible {
    visibility: visible !important;
    opacity: 1 !important;
}

.scroll-to-top .scroll-top-inner {
    opacity: 0;
    visibility: hidden;
    display: flex;
    align-items: center;
    transition: all cubic-bezier(0.4, 0, 0.2, 1) 0.4s;
}

.scroll-to-top.visible .scroll-top-inner {
    opacity: 1;
    visibility: visible;
}

.scroll-to-top .scroll-bar {
    width: 50px;
    height: 4px;
    margin-right: 10px;
    position: relative;
}

.scroll-to-top .scroll-bar:before {
    content: "";
    position: absolute;
    left: 0;
    top: 0;
    height: 100%;
    width: 100%;
    background-color: var(--thm-base);
    opacity: 1;
}

.scroll-to-top .scroll-bar .bar-inner {
    position: absolute;
    top: 0;
    right: 0;
    height: 100%;
    background-color: var(--thm-primary);
    width: 0%;
}

.scroll-to-top .scroll-bar-text {
    color: var(--thm-black);
    font-size: 15px;
    font-weight: 700;
    cursor: pointer;
    font-family: var(--thm-font);
    transition: all 500ms ease;
}

.scroll-to-top .scroll-bar-text:hover {
    transform: scale(1);
}
</style>
