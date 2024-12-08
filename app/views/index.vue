<template>
  <div class="app-container">
    <nav class="nav-container">
      <router-link to="/" class="btn btn-secondary" :class="{ disabled: isHomePage }">Homepage</router-link>
      <router-link to="/rules" class="nav-link">Rules</router-link>
      <a href="https://github.com/santapau10/CHESS-WA-HTWG.git"
         class="nav-link"
         target="_blank">GitHub Repo</a>
    </nav>
    <main>
      <slot></slot>
    </main>
  </div>
</template>

<script>
export default {
  name: 'MainLayout',
  computed: {
    isHomePage() {
      return this.$route.path === '/';
    }
  }
}
</script>