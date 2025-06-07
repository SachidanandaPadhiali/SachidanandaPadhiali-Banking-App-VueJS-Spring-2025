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
    
    <div class="parent">
        <div class="child">
            <img src="../assets/img/round-dotted-shape-1.png" alt="Rotating Image" class="image">
        </div>
    </div>

    <section class="section-cards">
        <div class="card">
            <img src="https://via.placeholder.com/100x100.png?text=Banking" alt="Banking Services">
            <div class="card-content">
                <h3>BANKING SERVICES</h3>
                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
                <button>DETAILS...</button>
            </div>
        </div>
        <div class="card">
            <img src="https://via.placeholder.com/100x100.png?text=Encryption" alt="Encryption">
            <div class="card-content">
                <h3>ENCRYPTION</h3>
                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
                <button>DETAILS...</button>
            </div>
        </div>
        <div class="card">
            <img src="https://via.placeholder.com/100x100.png?text=AI" alt="Cognitive Computing">
            <div class="card-content">
                <h3>COGNITIVE COMPUTING</h3>
                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
                <button>DETAILS...</button>
            </div>
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

.section-cards {
    background-color: #c7d3e3;
    padding: 40px;
    display: flex;
    justify-content: space-around;
    flex-wrap: wrap;
}

.card {
    background-color: white;
    width: 280px;
    margin: 20px;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.card img {
    width: 100%;
    height: 150px;
    object-fit: contain;
    background-color: #f97316;
    padding: 20px;
}

.card-content {
    padding: 16px;
}

.card-content h3 {
    color: #0f2c57;
    font-size: 1.1rem;
}

.card-content p {
    font-size: 0.9rem;
    color: #555;
    margin: 10px 0;
}

.card-content button {
    background-color: #0f2c57;
    color: white;
    padding: 8px 16px;
    border: none;
    border-radius: 4px;
    font-weight: bold;
    cursor: pointer;
}

.image {
    width: 120px;
    height: 120px;
    animation: spin 4s linear infinite;
}

@keyframes spin {
    100% {
        transform: rotate(360deg);
    }
}

.parent {
    position: relative;
    width: 300px;
    height: 200px;
    background-color: lightgray;
}

.child {
    position: absolute;
    top: 0;
    right: 0;
    width: 50px;
    height: 50px;
    background-color: blue;
}
</style>