<template>
    <div class="collapsible-container">
        <div @click="toggle" class="collapsible-header">
            <span>{{ title }}</span>
            <svg :class="{ 'rotated': isOpen }" class="chevron-icon" fill="none" stroke="var(--primary)"
                viewBox="0 0 20 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
            </svg>
        </div>

        <transition name="fade">
            <div v-if="isOpen" class="collapsible-content">
                <slot />
            </div>
        </transition>
    </div>
</template>

<script>
export default {
    name: 'CollapsDetails',
    props: {
        title: {
            type: String,
            required: true
        }
    },
    data() {
        return {
            isOpen: false
        };
    },
    methods: {
        toggle() {
            this.isOpen = !this.isOpen;
        }
    }
};
</script>

<style>
.collapsible-container {
    margin: var(--mb-2) auto;
    background-color: #fff;
}

.collapsible-header {
    display: flex;
    justify-content: space-between;
    cursor: pointer;
    margin: var(--mb-2) auto;
    font-weight: var(--font-semi-bold);
    font-size: var(--normal-font-size);
    color: var(--dark-text);
}

.chevron-icon {
    width: 20px;
    height: 20px;
    transition: transform 0.3s ease;
}

.rotated {
    transform: rotate(180deg);
}

.collapsible-content {
    margin-top: var(--mb-2);
    font-size: var(--normal-font-size);
    color: var(--light-text);
    text-align: left;
}

.fade-enter-active,
.fade-leave-active {
    transition: all 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
    opacity: 0;
    max-height: 0;
    overflow: hidden;
}

.fade-enter-to,
.fade-leave-from {
    opacity: 1;
    max-height: 300px;
}

@media (max-width: 500px) {
    .collapsible-container {
        margin: var(--mb-1) 0;
    }

.collapsible-content {
    margin: var(--mb-0);
    padding: 0;
}

}
</style>
