import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";

Vue.use(VueRouter)

const routes = [
    //登录，注册，404页面路由
    {
        path: '/login',   //登录
        name: 'login',
        component: () => import('../views/login')
    },
    {
        path: '/register',   //注册
        name: 'register',
        component: () => import('../views/register')
    },
    {
        path: '/404',   //404页面
        name: '404',
        component: () => import('../views/404')
    },
    {
        path: '/password',   //修改密码
        name: 'password',
        component: () => import('../views/password')
    },


    {
        path: '/forget',   //忘记密码
        name: 'forget',
        component: () => import('../views/forget')
    },

    //前台路由
    {
        path: '/',
        redirect: '/front/home'
    },
    {
        path: '/front',
        redirect: '/front/home'
    },
    {
        path: '/front',   //主页面
        name: 'main',
        component: () => import('../views/front/front'),//懒加载模式
        children:
            [{
                path: '/front/home',   //home
                name: 'home',
                component: () => import('../views/front/home') //懒加载模式
            },
            ]
    },
    //后台路由
    {
        path: '/back',
        redirect: '/back/association',
        component: () => import('../views/back/association')
    },
    {
        path: '/back',
        name: 'back',
        component: () => import('../views/back/back'),
        children: [
            {
                path: '/back/home',
                name: 'BackHome',
                component: () => import('../views/back/home')
            },
            {
                path: '/back/association',
                name: 'association',
                component: () => import('../views/back/association')
            },
            {
                path: '/back/activity',
                name: 'activity',
                component: () => import('../views/back/activity')
            },
            {
                path: '/back/addClub',
                name: 'addClub',
                component: () => import('../views/back/addClub')
            },
            {
                path: '/back/college',
                name: 'college',
                component: () => import('../views/back/college')
            },
            {
                path: '/back/major',
                name: 'major',
                component: () => import('../views/back/major')
            },
        ]
    },
]

const router = new VueRouter({
    routes,
    mode: "history",
    base: process.env.BASE_URL
})

router.beforeEach((to, from, next) => {
    localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称
    store.commit("setPath")
    // 未找到路由的情况
    if (!to.matched.length) {
        const storeMenus = localStorage.getItem("menus")
        if (storeMenus) {
            next("/front/home")
        } else {
            // 跳回home页面
            next("/404")
        }
    }
    // 其他的情况都放行
    next()
})
export default router
