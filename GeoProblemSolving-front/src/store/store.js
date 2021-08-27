import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  // strict:true,
  state: {
    //data
    userInfo: {
      userState: false,
      name: 'Visitor',
      userId: '',
      avatar: '',
      state: '',
    },
    projectImg: '',
    // activityTree:[],
    tempOperationChange: [],
    activityTaskChange: [],
    IP_Port: window.location.host,
    //用于当前选择的页面内容
    // IP_Port:"172.21.213.185:8080",
    // IP_Port:"localhost:8080",
    // IP_Port:"172.21.212.72:8082",
    // IP_Port:"94.191.49.160:8080",
    DataServer: "221.226.60.2:8082", // 数据容器
    //外网版本用户服务器
    // UserServer: "http://94.191.49.160:8080/userServer",
    //测试用用户服务器
    UserServer: "http://172.21.212.103:8088/userServer",
  },
  getters: {
    userState: state => {
      return state.userInfo.userState;
    },
    userName: state => {
      return state.userInfo.name;
    },
    userProvince:state => {
      return state.userInfo.state;
    },
    userId: state => {
      return state.userInfo.userId;
    },
    avatar: state => {
      return state.userInfo.avatar;
    },
    userInfo: state => {
      return state.userInfo;
    },
    userEmail: state => {
      return state.userInfo.email;
    },
    // activityTree: state => {
    //     return state.activityTree;
    // },
    userServer: state => {
      return state.UserServer;
    },
    tempOperationChange: state => {
      return state.tempOperationChange;
    },
    activityTaskChange: state => {
      return state.activityTaskChange;
    }
  },
  mutations: {
    getUserInfo: state => {
      if (!state.userInfo.userState) {
        $.ajax({
          url: "/GeoProblemSolving/user/state",
          type: "POST",
          async: false,
          success: function (data) {
            if (data) {
              let userInfo = {};
              userInfo = data;
              userInfo.userState = true;
              state.userInfo = userInfo;
            }
          },
          error: function (err) {
            console.log("Get user info fail.");
          }
        });
      }
    },
    userLogin: (state, data) => {
      let userInfo = data;
      userInfo.userState = true;
      state.userInfo = userInfo;
      sessionStorage.setItem("userInfo", JSON.stringify(state.userInfo));
    },
    userLogout: (state) => {
      state.userInfo = {
        userState: false,
        name: 'Visitor',
        userId: '',
        avatar: '',
      };
      state.project = {};
      state.subProject = {};
      sessionStorage.removeItem("userInfo");
    },
    uploadAvatar: (state, avatar) => {
      state.userInfo.avatar = avatar;
    },
    setUserInfo: (state, userInfo) => {
      state.userInfo = userInfo;
    },
    updateProject: (state, projectId) => {
      //删除某项project
      let index = state.userInfo.createdProjects.indexOf(projectId);
      state.userInfo.createdProjects.splice(index, 1);
    },
    // setActivityTree: (state, activityTree) => {
    //     Vue.set(state, "activityTree", activityTree);
    // },
    clearTempOperations: (state) => {
      Vue.set(state, "tempOperationChange", []);
    },
    clearActivityTasks: (state) => {
      Vue.set(state, "activityTaskChange", []);
    },
    updateTempOperations: (state, data) => {
      state.tempOperationChange.push(data);
    },
    updateActivityTasks: (state, data) => {
      state.activityTaskChange.push(data);
    },
  }
})
