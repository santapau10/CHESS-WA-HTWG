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
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);
  text-align: center;
}

h2 {
  margin-bottom: 10px;
  color: #333;
  font-size: 24px;
}

.subtitle {
  margin-bottom: 20px;
  color: #555;
  font-size: 14px;
}

.input-group {
  margin-bottom: 15px;
  text-align: left;
}

label {
  display: block;
  margin-bottom: 5px;
  color: #555;
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
  background-color: #007bff;
  color: white;
  padding: 12px 15px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 16px;
  width: 100%;
}

.btn-primary:hover {
  background-color: #0056b3;
}

.or-divider {
  margin: 15px 0;
  font-size: 14px;
  color: #888;
}

.btn-secondary {
  background-color: #ffffff;
  color: #333;
  border: 1px solid #ccc;
  padding: 12px 15px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 16px;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.btn-secondary:hover {
  background-color: #f8f9fa;
}

.icon {
  height: 20px;
  width: 20px;
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
