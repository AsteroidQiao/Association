const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
    transpileDependencies: true,
    lintOnSave: false,
    // devServer: {
    //     port: 8093,
    // },
    pluginOptions: {
        redisDeploy: {
            registry: undefined,
            pluginVersion: '0.0.1'
        }
    }
})
