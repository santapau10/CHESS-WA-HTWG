const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})
module.exports = {
  devServer: {
    proxy: {
      "/": {
        target: "http://localhost:9000", // URL deines Backends
        changeOrigin: true,
      },
    },
  },
};
