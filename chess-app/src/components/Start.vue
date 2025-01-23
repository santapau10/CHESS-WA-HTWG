<template>
  <div class="container">
    <div class="nav-container">
      <nav class="nav">
        <button class="btn" @click="$emit('rules')">Rules</button>
        <a class="btn" href="https://github.com/santapau10/CHESS-WA-HTWG.git" target="_blank">GitHub Repo</a>
      </nav>
    </div>
    <div class="main-content">
      <h1>CHESS</h1>
      <div class="button-wrapper">
        <button @click="$emit('start')" class="start-button">&#9655;</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ChessGameStart",
};
</script>

<style scoped>
.container {
  position: relative;
  width: 100vw;
  height: 100vh;
  background-image: url('@/assets/pictures/background.jpeg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  border-radius: 0;
  text-align: center;
  overflow: hidden;
}

.nav-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  display: flex;
  justify-content: flex-start;
  background-color: transparent;
  padding: 10px;
  z-index: 1000;
}

.nav {
  display: flex;
  gap: 10px;
  padding: 0;
  margin: 0;
}

.btn {
  display: flex;
  padding: 10px 20px;
  font-size: 25px;
  font-weight: bold;
  text-align: center;
  text-decoration: none;
  border-radius: 20px;
  background-color: #3A7CA5;
  color: white;
  border: 2px solid black;
  transition: background-color 0.3s ease, transform 0.2s ease, box-shadow 0.2s ease;
}

.btn:hover {
  background-color: #326488;
  transform: scale(1.05);
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
}

h1 {
  height: 38%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 6rem;
  font-weight: lighter;
  margin: 0;
  color: white;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
}

.main-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  height: 100%;
}

.button-wrapper {
  position: absolute;
  bottom: 20%;
  display: flex;
  width: 100%;
  justify-content: center;
}

.start-button {
  width: 100px;
  height: 100px;
  font-size: 40px;
  color: white;
  background-color: #404040;
  border: none;
  border-radius: 50%;
  cursor: pointer;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  padding-left: 12px;
  padding-bottom: 7px;
}

.start-button:hover {
  background-color: #218838;
  box-shadow: 0 0 20px rgba(0, 0, 0, 1);
}

@media (max-width: 800px) {
  h1 {
    font-size: 4rem;
  }
  .start-button {
    width: 70px;
    height: 70px;
    font-size: 30px;
  }
  .btn {
    font-size: 20px;
    padding: 8px 16px;
  }
}

@media (min-width: 1920px) {
  h1 {
    font-size: 8rem;
  }
  .start-button {
    width: 150px;
    height: 150px;
    font-size: 60px;
  }
  .btn {
    font-size: 30px;
    padding: 12px 24px;
  }
}

@media (min-width: 2560px) {
  h1 {
    font-size: 10rem;
  }
  .start-button {
    width: 180px;
    height: 180px;
    font-size: 70px;
  }
  .btn {
    font-size: 35px;
    padding: 15px 30px;
  }
}
</style>
