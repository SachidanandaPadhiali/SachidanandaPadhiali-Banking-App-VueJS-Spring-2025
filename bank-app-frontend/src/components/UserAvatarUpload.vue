<template>
  <div>
    <label for="avatarInput">Upload Profile Image:</label>
    <input
      type="file"
      id="avatarInput"
      @change="onFileSelected"
      accept="image/*"
    />
    <button
      :disabled="!selectedFile"
      @click="uploadAvatar"
    >
      Upload
    </button>
    <p v-if="uploadMessage">{{ uploadMessage }}</p>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "UserAvatarUpload",
  props: {
    userId: { type: Number, required: true }
  },
  data() {
    return {
      selectedFile: null,
      uploadMessage: ""
    };
  },
  methods: {
    onFileSelected(event) {
      const file = event.target.files[0];
      if (file && file.type.startsWith("image/")) {
        this.selectedFile = file;
      } else {
        this.selectedFile = null;
        this.uploadMessage = "Please select a valid image file.";
      }
    },
    async uploadAvatar() {
      if (!this.selectedFile) return;
      const formData = new FormData();
      formData.append("file", this.selectedFile);

      try {
        await axios.post(
          `http://192.168.1.4.nip.io:8088/api/users/${this.userId}/avatar`,
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data"
            }
          }
        );
        this.uploadMessage = "Upload successful!";
        // Optionally: emit an event or refresh the displayed avatar
        this.$emit("avatar-updated");
      } catch (err) {
        this.uploadMessage = "Upload failed: " + err.response?.data || err.message;
      }
    }
  }
};
</script>

<style scoped>
/* optional styling */
</style>
