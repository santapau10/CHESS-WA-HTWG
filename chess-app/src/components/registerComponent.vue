<template>
  <div class="register-container">
    <h2>Create an Account</h2>
    <form @submit.prevent="registerWithEmail">
      <div class="input-group">
        <label for="email">Email</label>
        <input id="email" v-model="email" type="email" placeholder="Enter your email" required />
      </div>
      <div class="input-group">
        <label for="password">Password</label>
        <input
            id="password"
            v-model="password"
            type="password"
            placeholder="Enter your password"
            required
        />
      </div>
      <button class="btn-primary" type="submit">Register</button>
    </form>
    <p class="switch-text">
      Already have an account? <a @click="switchToLogin">Login here</a>
    </p>
  </div>
</template>

<script>
import { createUserWithEmailAndPassword } from "firebase/auth";
import { auth } from "../firebase";

export default {
  data() {
    return {
      email: "",
      password: "",
    };
  },
  methods: {
    async registerWithEmail() {
      try {
        const userCredential = await createUserWithEmailAndPassword(auth, this.email, this.password);
        const user = userCredential.user;
        this.$emit("register-success", user);
      } catch (error) {
        alert("Registration failed: " + error.message);
      }
    },
    switchToLogin() {
      this.$emit("switch-to-login");
    },
  },
};
</script>
<style scoped>
.register-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 20px;
  background-image: url('@/assets/pictures/knight.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  color: white; /* Text sichtbar auf Hintergrundbild */
}

h2 {
  margin-bottom: 10px;
  color: white;
  font-size: 30px;
}

.input-group {
  margin-bottom: 15px;
  text-align: left;
  width: 87%;
  max-width: 400px;
}

label {
  display: block;
  margin-bottom: 5px;
  color: white;
  font-weight: bold;
}

input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 14px;
}

.btn-primary {
  background-color: darkslategrey;
  color: white;
  padding: 12px 15px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 20px;
  width: 100%;
  max-width: 400px;
}

.btn-primary:hover {
  background-color: seagreen;
}

.switch-text {
  margin-top: 15px;
  color: white;
}

.switch-text a {
  color: #007bff;
  cursor: pointer;
  text-decoration: none;
}

.switch-text a:hover {
  text-decoration: underline;
}
</style>
