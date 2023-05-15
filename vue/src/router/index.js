import Vue from 'vue'
import VueRouter from 'vue-router'
// import store from "@/store";

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
        name: 'front',
        component: () => import('../views/front/front'),//懒加载模式
        children:
            [
                {
                    path: '/front/association',   //全部社团
                    name: 'frontAssociation',
                    component: () => import('../views/front/association') //懒加载模式
                },
                {
                    path: '/front/myAssociation',   //全部社团
                    name: 'myAssociation',
                    component: () => import('../views/front/myAssociation') //懒加载模式
                },
                {
                    path: '/front/club/:id',   //社团详情
                    name: 'club',
                    component: () => import('../views/front/club') //懒加载模式
                },
                {
                    path: '/front/home',   //主页面
                    name: 'home',
                    component: () => import('../views/front/home') //懒加载模式
                },
                {
                    path: '/front/activity',   //全部活动
                    name: 'activity',
                    component: () => import('../views/front/activity') //懒加载模式
                },
                {
                    path: '/front/myActivity',   //我的活动
                    name: 'myActivity',
                    component: () => import('../views/front/myActivity') //懒加载模式
                },
                {
                    path: '/front/activityDetail/:id',   //活动详情
                    name: 'frontActivityDetail',
                    component: () => import('../views/front/activityDetail') //懒加载模式
                },
                {
                    path: '/front/enroll/:id',   //活动人员录取详情
                    name: 'enroll',
                    component: () => import('../views/front/enroll')
                },
                {
                    path: '/front/manage/:id',   //部落人员录取详情
                    name: 'manage',
                    component: () => import('../views/front/manage')
                },
                {
                    path: '/front/notification',   //通知详情
                    name: 'notification',
                    component: () => import('../views/front/notification') //懒加载模式
                },
                {
                    path: '/front/addActivity',   //发布活动
                    name: 'addActivity',
                    component: () => import('../views/front/addActivity') //懒加载模式
                },
                {
                    path: '/front/center',   //person
                    name: 'center',
                    component: () => import('../views/front/center'),//懒加载模式
                    children:
                        [
                            {
                                path: '/front/center/person',   //person
                                name: 'person',
                                component: () => import('../views/person') //懒加载模式
                            },
                            {
                                path: '/front/center/password',   //password
                                name: 'password',
                                component: () => import('../views/password') //懒加载模式
                            },
                        ]
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
                path: '/back/carousel',
                name: 'backCarousel',
                component: () => import('../views/back/carousel')   //轮播图
            },
            {
                path: '/back/page',
                name: 'backPage',
                component: () => import('../views/back/page')   //页面设置
            },
            {
                path: '/back/notification',
                name: 'backNotification',
                component: () => import('../views/back/notification')   //页面设置
            },
            {
                path: '/back/association',//社团中心
                name: 'backAssociation',
                component: () => import('../views/back/association')
            },
            {
                path: '/back/club/:id',//社团详情
                name: 'backClub',
                component: () => import('../views/back/club')
            },
            {
                path: '/back/addClub',//添加社团
                name: 'backAddClub',
                component: () => import('../views/back/addClub')
            },
            {
                path: '/back/activity',//活动中心
                name: 'backActivity',
                component: () => import('../views/back/activity')
            },
            {
                path: '/back/enroll/:id',   //人员录取详情
                name: 'backEnroll',
                component: () => import('../views/back/enroll')
            },
            {
                path: '/back/activityDetail/:id',//活动详情
                name: 'backActivityDetail',
                component: () => import('../views/back/activityDetail')
            },
            {
                path: '/back/addActivity',//添加活动
                name: 'backAddActivity',
                component: () => import('../views/back/addActivity')
            },
            {
                path: '/back/college',//学院中心
                name: 'backCollege',
                component: () => import('../views/back/college')
            },
            {
                path: '/back/major',//专业中心
                name: 'backMajor',
                component: () => import('../views/back/major')
            },
            {
                path: '/back/person',   //个人中心
                name: 'backPerson',
                component: () => import('../views/back/person')
            },
            {
                path: '/back/password',   //修改密码
                name: 'backPassword',
                component: () => import('../views/password')
            },
            {
                path: '/back/role',   //修改权限
                name: 'backRole',
                component: () => import('../views/back/role')
            },
            {
                path: '/back/credit',   //学分管理
                name: 'backCredit',
                component: () => import('../views/back/credit')
            },
            {
                path: '/back/manage/:id',   //社团成员管理
                name: 'backMange',
                component: () => import('../views/back/manage')
            },
        ]
    },
    {
        path: '*',//匹配未定义的路由
        redirect: '/404'//重定向
    }
]
import {Notification} from "element-ui";

const router = new VueRouter({
    routes,
    mode: "history",
    base: process.env.BASE_URL
})

router.beforeEach((to, from, next) => {
    localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称

    //前台主界面和login、register、forget等可以不登录，正常访问
    if (to.name === "home" || to.name === "login" || to.name === "register" || to.name === "forget" || to.name === "404") {
        next()
    } else {
        //访问需要登录界面， 校验用户
        let user = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : null
        // console.log(to.name)
        console.log(user)
        //如果user为空，跳到login
        if (user === null) {
            Notification({
                message: "无权限，请登录",
                type: "warning"
            })
            next('/login')
            console.log("can not to login")
        }
        //user存在且是学生，不能访问后台
        else if (user.urole === '学生' && to.name.includes('back')) {
            {
                Notification({
                    message: "无权限访问后台，请登录",
                    type: "error"
                })
                next('/login')
                console.log("can not to login")
            }
        }
        //其余情况放行
        else {
            // console.log(user.urole)
            // console.log('success')
            next()
        }

    }
})
export default router
