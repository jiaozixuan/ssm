const { defineConfig } = require('@vue/cli-service')
module.exports = {
  devServer:{
    port:8080,
    proxy: 'http://localhost:8088',
  }
}


