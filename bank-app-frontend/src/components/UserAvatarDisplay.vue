<template>
    <div class="avatar-container">
        <img v-if="avatarUrl" :src="avatarUrl" alt="Profile Avatar" class="avatar-image" @error="onImageError"
            width="100" />
        <div v-else>
            <img src="../assets/img/avatar-default.svg" alt="" width="100">
        </div>
    </div>
</template>

<script>
export default {
    name: "UserAvatarDisplay",
    props: {
        accountNum: {
            type: String,
            required: true,
            default: 'Unknown'
        }
    },
    data() {
        return {
            avatarUrl: null
        };
    },
    methods: {
        async fetchAvatar() {
            try {
                // This endpoint returns image bytes, so set responseType to 'blob'
                const response = await this.$axios.get(
                    `http://192.168.1.4.nip.io:8088/api/users/${this.accountNum}/avatar`,
                    { responseType: "blob" }
                );
                // Create a local object URL for the blob
                this.avatarUrl = URL.createObjectURL(response.data);
            } catch (err) {
                // 404 or no image → keep avatarUrl null
                this.avatarUrl = null;
            }
        },
        onImageError() {
            this.avatarUrl = null;
        }
    },
    watch: {
        userId: "fetchAvatar"
    },
    mounted() {
        this.fetchAvatar();
    },
    beforeUnmount() {
        if (this.avatarUrl) {
            URL.revokeObjectURL(this.avatarUrl);
        }
    }
};
</script>

<style scoped>
.avatar-container {
    width: 150px;
    height: 150px;
    border-radius: 50%;
    overflow: hidden;
    background-color: #f0f0f0;
    display: flex;
    align-items: center;
    justify-content: center;
}
</style>
