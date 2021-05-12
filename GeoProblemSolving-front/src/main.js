// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import Vuex from 'vuex';
import App from './App'
import router from './router'
import iView from 'iview'
import axios from 'axios'
import VueAxios from 'vue-axios'
// 滚轮样式
import vuescroll from 'vuescroll'
// import $ from 'jquery'
import 'iview/dist/styles/iview.css'
import store from './store/store.js'
//引入index.js文件夹
// import './mock/index'
import * as socketApi from './api/socket'
import * as operationApi from './api/operation'
import * as userRoleApi from "@/api/userRole.js";

//set globe CSS
// import './assets/css/style.css'


import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";

axios.defaults.withCredentials=true;
Vue.prototype.socketApi = socketApi
Vue.prototype.operationApi = operationApi
Vue.prototype.userRoleApi = userRoleApi
Vue.config.productionTip = false;
Vue.config.devtools = true;

Vue.use(Vuex)
Vue.use(VueAxios,axios)
Vue.use(iView)
Vue.use(ElementUI)
Vue.use(vuescroll)

axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
Vue.prototype.$axios = axios;

// 实例化对象
new Vue({
  el: '#app',
  store,
  router,
  components: { App },
  template: '<App/>',
  // components: { App },
  //   mounted () {
  //   document.dispatchEvent(new Event('render-event'))
  // }
})
