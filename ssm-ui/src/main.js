import {createApp} from 'vue'
import App from './App.vue'
import router from '@/router'
import store from '@/store';
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import '@/assets/style/common.css'
import keys from '@/directive'

let app = createApp(App)
app.use(router).use(store).use(ElementPlus);

for (let key in keys) {
    app.directive(key,keys[key]);
}


app.mount('#app')
