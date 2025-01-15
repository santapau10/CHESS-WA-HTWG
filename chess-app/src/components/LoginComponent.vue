<template>
  <div class="login-container">
    <h2>Welcome Back</h2>
    <p class="subtitle">Please sign in to continue</p>
    <form @submit.prevent="loginWithEmail">
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
      <button class="btn-primary" type="submit">Login</button>
    </form>
    <p class="or-divider">or</p>
    <button class="btn-secondary" @click="loginWithGoogle">
      <img src="/google-icon.svg" alt="Google" class="icon" />
      Login with Google
    </button>
    <p class="switch-text">
      Don't have an account? <a @click="switchToRegister">Register here</a>
    </p>
  </div>
</template>

<script>
import { signInWithEmailAndPassword, signInWithPopup, GoogleAuthProvider } from "firebase/auth";
import { auth } from "../firebase";

export default {
  data() {
    return {
      email: "",
      password: "",
    };
  },
  methods: {
    async loginWithEmail() {
      try {
        const userCredential = await signInWithEmailAndPassword(auth, this.email, this.password);
        const user = userCredential.user;
        this.$emit("login-success", user);
      } catch (error) {
        alert("Login failed: " + error.message);
      }
    },
    async loginWithGoogle() {
      const provider = new GoogleAuthProvider();
      try {
        const result = await signInWithPopup(auth, provider);
        const user = result.user;
        this.$emit("login-success", user);
      } catch (error) {
        alert("Google Login failed: " + error.message);
      }
    },
    switchToRegister() {
      this.$emit("switch-to-register");
    },
  },
};
</script>

<style scoped>
.login-container {
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

.subtitle {
  margin-bottom: 20px;
  color: white;
  font-size: 14px;
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

.or-divider {
  margin: 15px 0;
  font-size: 14px;
  color: white;
}

.btn-secondary {
  background-color: darkslategrey;
  color: white;
  padding: 12px 15px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 16px;
  width: 100%;
  max-width: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.btn-secondary:hover {
  background-color: seagreen;
}

.icon {
  height: 20px;
  width: 20px;
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
