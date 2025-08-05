<template>
    <PageHeader />
    <section class="hero">
        <div class="hero-left">
            <h1>Banking That’s Built Around You</h1>
            <p>Secure accounts, competitive rates, and 24/7 support—designed for your financial peace of mind.</p>
            <p>From everyday checking to long-term investments, we offer solutions designed for every stage of life.</p>
            <button class="btn">Read more...</button>
        </div>
        <div class="hero-right">
            <img src="../assets/img/1738565448060_blob.png" alt="Banking Illustration">
        </div>
        <div class="decor-circles">
            <div class="circle"></div>
            <div class="circle"></div>
            <div class="circle"></div>
        </div>
    </section>
    <ScrollToTop />
</template>

<script>
import PageHeader from './PageHeader.vue'
import ScrollToTop from './ScrollToTop.vue'
export default {
    name: 'HomePage',
    components: {
        PageHeader,
        ScrollToTop
    }
}
</script>

<style>
nav a {
    color: white;
    margin: 0 12px;
    text-decoration: none;
    font-weight: 500;
}

nav a:hover {
    text-decoration: underline;
}

.hero {
    background: linear-gradient(to bottom right, var(--primary), var(--accent));
    color: white;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 60px 40px;
    clip-path: polygon(0 0, 100% 0, 100% 90%, 0 100%);
    position: relative;
}

.hero-left {
    max-width: 50%;
}

.hero-left h1 {
    font-size: 2.5rem;
    margin-bottom: 20px;
}

.hero-left p {
    color: #e5e7eb;
    margin-bottom: 30px;
}

.hero-left button {
    border: none;
    border-radius: 4px;
}

.hero-right img {
    max-width: 400px;
}

.decor-circles {
    position: absolute;
    top: 20px;
    left: 20px;
    display: flex;
    gap: 10px;
}

.circle {
    width: 12px;
    height: 12px;
    border-radius: 50%;
    background-color: white;
    opacity: 0.5;
}
</style>