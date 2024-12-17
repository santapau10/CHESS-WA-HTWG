<template>
  <div class="settings-container">
    <h1>Welcome to Chess Game</h1>
    <button @click="$emit('start')">Start Game</button>
  </div>
</template>

<script>
export default {
  name: "settings-component",
};
</script>

<style scoped>
.settings-container {
  text-align: center;
  background: rgba(255, 255, 255, 0.8);
  padding: 20px;
  border-radius: 10px;

  /* Zentrierung des Containers */
  display: flex;
  flex-direction: column; /* Um die Elemente vertikal anzuordnen */
  justify-content: center; /* Vertikal zentrieren */
  align-items: center; /* Horizontal zentrieren */

  position: absolute; /* Absolut positionieren, um den Container zu zentrieren */
  top: 50%; /* 50% von oben */
  left: 50%; /* 50% von links */
  transform: translate(-50%, -50%); /* Verschiebt den Container, um ihn exakt zu zentrieren */
}

button {
  margin-top: 20px; /* Abstand zwischen Titel und Button */
  padding: 10px 20px;
  font-size: 18px;
  background-color: #404040;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #218838;
}
</style>
