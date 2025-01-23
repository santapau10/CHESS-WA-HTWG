const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})
module.exports = {
  devServer: {
    port: 8080,
    proxy: {
      "/": {
        target: process.env.VUE_APP_BACKEND_URL, // URL deines Backends
        changeOrigin: true,
      },
    },
  },
};
// vue.config.js
module.exports = {
  pwa: {
    name: 'DeinAppName',   // Name deiner App
    themeColor: '#4DBA87',  // Thema Farbe der App
    msTileColor: '#000000', // Farbe des Kacheltiles für Windows
    appleMobileWebAppCapable: 'yes', // Aktiviert das "Add to Home Screen"-Feature auf iOS
    appleMobileWebAppStatusBarStyle: 'black',
    workboxOptions: {
      runtimeCaching: [
        {
          urlPattern: /\.(?:png|jpg|jpeg|svg|gif)$/,
          handler: 'CacheFirst',
          options: {
            cacheName: 'image-cache',
            expiration: {
              maxEntries: 50,
            },
          },
        },
        {
          urlPattern: /\/api\//,
          handler: 'NetworkFirst',
          options: {
            cacheName: 'api-cache',
            expiration: {
              maxEntries: 10,
            },
          },
        },
      ],
    },
  },
};

