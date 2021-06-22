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
        },
        projectImg: '',
        activityTree:[],
        tempOperations: [],
        activityTasks: [],
        taskDependencies: [],
        IP_Port: window.location.host,
        //用于当前选择的页面内容
        // IP_Port:"172.21.213.185:8080",
        // IP_Port:"localhost:8080",
        // IP_Port:"172.21.212.72:8082",
        // IP_Port:"94.191.49.160:8080",
        DataServer:"221.226.60.2:8082", // 数据容器
        UserServer: "http://172.21.212.103:8088/userServer",
    },
    getters: {
        userState: state => {
            return state.userInfo.userState;
        },
        userName: state => {
            return state.userInfo.name;
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
        // activityTree: state => {
        //     return state.activityTree;
        // },
        // tempOperations: state => {
        //     return state.tempOperations;
        // },
        // activityTasks: state => {
        //     return state.activityTasks;
        // }
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
                            var userInfo = data;
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
        setActivityTree: (state, activityTree) => {
            Vue.set(state, "activityTree", activityTree);
        },
        setTempOperations: (state, operations) => {
            Vue.set(state, "tempOperations", operations);
        },
        setActivityTasks: (state, tasks) => {
            Vue.set(state, "activityTasks", tasks);
        },
        setTaskDependencies: (state, links) => {
            Vue.set(state, "taskDependencies", links);
        },
        updateTempOperations: (state, data) => {
            if(data.behavior === "add"){
                state.tempOperations.push(data.operation);
            } else if(data.behavior === "remove"){
                for(var i = 0; i < state.tempOperations.length; i++){
                    if(state.tempOperations[i].id === data.operation.id){
                        state.tempOperations.splice(i, 1);
                    }
                }
            } else if(data.behavior === "update"){
                for(var i = 0; i < state.tempOperations.length; i++){
                    for(var i = 0; i < state.tempOperations.length; i++){
                        if(state.tempOperations[i].id === data.operation.id){
                            state.tempOperations[i] = data.operation;
                        }
                    }
                }
            }
        },
        updateActivityTasks: (state, data) => {
            if(data.behavior === "add"){
                state.activityTasks.push(data.task);
            } else if(data.behavior === "remove"){
                for(var i = 0; i < state.activityTasks.length; i++){
                    if(state.activityTasks[i].taskId === data.task.taskId){
                        state.activityTasks.splice(i, 1);
                    }
                }
            } else if(data.behavior === "update"){
                for(var i = 0; i < state.activityTasks.length; i++){
                    if(state.activityTasks[i].taskId === data.task.taskId){
                        state.activityTasks[i] = data.task;
                    }
                }
            }
        },
        updateTaskDependencies: (state, data) => {
            if(data.behavior === "add"){
                state.taskDependencies.push(data.link);
            } else if(data.behavior === "remove"){
                
            } else if(data.behavior === "update"){

            }
        }
    }
})
