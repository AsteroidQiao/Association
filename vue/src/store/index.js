import Vue from 'vue'
import Vuex from 'vuex'
import router, {resetRouter} from "@/router";

Vue.use(Vuex)

const store = new Vuex.Store({
    state: {
    },
    mutations: {
        logout() {
            // 清空缓存
            localStorage.removeItem("user")
            router.push("/login")
            // 重置路由
            resetRouter()
        },
    },
    getters: {}
})

export default store
