<template>
  <div class="settings-container">
    <h1>Welcome to Chess Game</h1>
    <button @click="startGame">Start Game</button>
  </div>
</template>

<script>
export default {
  name: "start-component",
  props: {
    socket: {
      type: WebSocket,
      required: true
    }
  },
  methods: {
    startGame() {
      // When the button is clicked, send the "start" message to the WebSocket server
      console.log("Sending start message to WebSocket server");
      this.socket.send("start"); // Sends the "start" message to the server
    }
  }
};
</script>

<style scoped>
body {
  font-family: Arial, sans-serif;
  background-image: url('../assets/background.jpeg');
  background-size: cover;
  background-position: center;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  margin: 0;
}

.settings-container {
  text-align: center;
  background: rgba(255, 255, 255, 0.8);
  padding: 20px;
  border-radius: 10px;
}
</style>
