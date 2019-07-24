/**
 * axio封装
 * 请求拦截、响应拦截、错误统一处理
 *
 * 参考自：vue中Axios的封装和API接口的管理
 * 地址：https://juejin.im/post/5b55c118f265da0f6f1aa354#heading-10
 */

import axios from 'axios';

/**
 * @description 请求失败后的错误统一处理
 * @param {Number} status 请求失败的状态码
 * @param {*} other
 */
const errorHandle = (status, other) => {
  switch (status) {
    case 401:
      break;
    case 403:
      break;
    case 404:
      // this.$Message.error("Resource Not Found!")
      break;
    default:
      console.log(other);
  }
}

//* 创建axio实例
var instance = axios.create({timeout:1000*30});
//* 设置post请求头
instance.defaults.headers.post['Content-Type']= 'application/x-www-form-urlencoded';

/**
 * 请求拦截器
 * 每次请求前，如果存在token则在请求头中携带token
 */
instance.interceptors.request.use(
  config=>{
        // 登录流程控制中，根据本地是否存在token判断用户的登录情况
        // 但是即使token存在，也有可能token是过期的，所以在每次的请求头中携带token
        // 后台根据携带的token判断用户的登录情况，并返回给我们对应的状态码
        // 而后我们可以在响应拦截器中，根据状态码进行一些统一的操作。
        // const token = store.state.token;
        // token && (config.headers.Authorization = token);
        return config;
  },
  error=>Promise.error(error)
)

//响应拦截器
instance.interceptors.response.use(
  //请求成功
  res=>{
    if(res.status===200){
      let resData = res.data;
      if(resData.code===0){
        return Promise.resolve(resData.data);
      }else{
        return Promise.reject(resData.msg);
      }
    }else{
      return Promise.reject("Network response was not ok.")
    }
  },
  //请求失败
  error=>{
    const {response} = error;
    if(response){
      //请求已发出，但不在 2XX 的范围
      // errorHandle(response.status,response.data.msg);
      return Promise.reject("Network response was not ok.")
    }else{
            // 处理断网的情况
            // eg:请求超时或断网时，更新state的network状态
            // network状态在app.vue中控制着一个全局的断网提示组件的显示隐藏
            // 关于断网组件中的刷新重新获取数据，会在断网组件中说明
          //   if (!window.navigator.onLine) {
          //     store.commit('changeNetwork', false);
          //  } else {
               return Promise.reject(error.message);
          //  }
    }
  }
)

export default instance;