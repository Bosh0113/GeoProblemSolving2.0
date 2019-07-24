const state = {
  projectInfo:{}
}

const mutations = {
  //* xSet是一个通用set方法
  xSet:function(state,obj){
    function _set(obj){
      for(var key in obj){
        if(typeof key === 'object'){
          _set(obj[key])
        }else{
          state[key] = obj[key]
        }
      }
    }

    _set(obj);
  },
}

const actions = {
  //
}

export default {
  namespaced: true,//* 启用命名空间
  state,
  mutations,
  actions
}
