import {login, logout} from '@/api/user.js'
import storage from '@/util/storage.js'

const user = {
    state: {
        username: '', nickname: '', token: ''
    }, getters: {
        isLogin(state){
            return state.nickname !== '' && state.token !=='';
        }
    }, mutations: {
        SAVE_USERNAME(state, username) {
            state.username = username
        }, SAVE_NICKNAME(state, nickname) {
            state.nickname = nickname
        }, SAVE_TOKEN(state, token) {
            state.token = token
        },
    }, actions: {
        LOGIN({commit}, user) {
            return new Promise(function (resolve) {
                login(user).then(res => {
                    // vuex的使用是为了保障数据的响应式
                    commit('SAVE_USERNAME', res.data.user.userName);
                    commit('SAVE_NICKNAME', res.data.user.nickName);
                    commit('SAVE_TOKEN', res.data.token);
                    storage.saveSessionObject("loginUser", res.data);
                    resolve(res)

                })
            })
        },
        LOGOUT({commit}) {
            return new Promise(function (resolve) {
                logout().then(res => {
                    // vuex的使用是为了保障数据的响应式
                    commit('SAVE_USERNAME', '');
                    commit('SAVE_NICKNAME', '');
                    commit('SAVE_TOKEN',  '');
                    storage.remove("loginUser");
                    resolve(res)

                })
            })
        },
        assign({commit}){
            let sessionObject = storage.getSessionObject("loginUser");
            if(sessionObject){
                commit('SAVE_USERNAME', sessionObject.user.userName);
                commit('SAVE_NICKNAME', sessionObject.user.nickName);
                commit('SAVE_TOKEN',  sessionObject.token);
            }
        }
    }
}

export default user