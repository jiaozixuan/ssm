const { defineConfig } = require('@vue/cli-service')
module.exports = {
  lintOnSave:false,
  devServer:{
    port:8080,
    proxy: 'http://localhost:8088',
  },
}


