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
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
  background: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  text-align: center;
}

h2 {
  margin-bottom: 20px;
  color: #333;
}

.input-group {
  margin-bottom: 15px;
  text-align: left;
}

label {
  display: block;
  margin-bottom: 5px;
  color: #555;
}

input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
}

.btn-primary {
  background-color: #007bff;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

.btn-primary:hover {
  background-color: #0056b3;
}

.switch-text {
  margin-top: 15px;
  color: #555;
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
