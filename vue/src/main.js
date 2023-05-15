import Vue from 'vue'
import './plugins/axios'
import App from './App.vue'
import router from './router'
import './plugins/element.js'
import store from './store'
Vue.config.productionTip = false

import VueRouter from 'vue-router'
Vue.use(VueRouter)
// 解决报错 此方法只针对VueRouter3.0以上
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
