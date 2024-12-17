<template>
  <div class="offline-overlay">
    <div class="offline-message">
      <p>Failed to Connect to Server. Please check your connection.</p>
      <button @click="goToSettings">Go to Settings</button>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    // Du könntest `isOffline` als Prop übergeben, falls nötig
    isOffline: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    goToSettings() {
      // Dies könnte einen Event-Emit oder einen Aufruf an die Parent-Komponente beinhalten, um den Status zurückzusetzen
      this.$emit("go-to-settings"); // Event zur Parent-Komponente
    }
  }
};
</script>

<style scoped>
.offline-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  color: white;
}

.offline-message {
  text-align: center;
  font-size: 1.2rem;
}
</style>
