// 导入用来创建路由和确定路由模式的两个方法
import {
    createRouter, createWebHistory
} from 'vue-router'
import store from '@/store'
import storage from '@/util/storage.js'


/**
 * 定义路由信息
 *
 */
const routes = [
    {
        name: 'login',alias: '/', path: '/login', component: () => import('@/components/login/login'),
    },
    {
        name: 'main',  path: '/main', component: () => import('@/components/main/main'),
        children:[
            {
                name:'user',
                path:'user',
                component:()=>import('@/components/system/user/user')
            }
        ]

    },

]

// 创建路由实例并传递 `routes` 配置
// 我们在这里使用 html5 的路由模式，url中不带有#，部署项目的时候需要注意。
const router = createRouter({
    history: createWebHistory(), routes,
})


// 全局的路由守卫
router.beforeEach((to, from) => {
    console.log(to)
    if (to.name === 'login') {
        return true;
    }
    if (store.getters.isLogin) {
        if (storage.getSessionObject("loginUser")) {
            routes.push({name: "login"})
        }
    } else {
        store.dispatch("assign")
    }

    return true
})

// 讲路由实例导出
export default router