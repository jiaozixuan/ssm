import {login} from '@/api/user.js'
import storage from '@/util/storage.js'

const user = {
    state: {
        username:'',
        nickname:'',
        token:''
    },
    getters:{

    },
    mutations: {
        SAVE_USERNAME(state,username){
            state.username = username
        },
        SAVE_NICKNAME(state,nickname){
            state.nickname = nickname
        },
        SAVE_TOKEN(state,token){
            state.token = token
        },
    },
    actions: {
        LOGIN({commit},user){
            return new Promise(function (resolve){
                login(user).then(res => {
                        // vuex的使用是为了保障数据的响应式
                        commit('SAVE_USERNAME', res.data.user.userName);
                        commit('SAVE_NICKNAME', res.data.user.nickName);
                        commit('SAVE_TOKEN', res.data.token);
                        storage.saveSessionObject("loginUser",res.data);
                        resolve(res)

                })
            })


        }
    }
}

export default user