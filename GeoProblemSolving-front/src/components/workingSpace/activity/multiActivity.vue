<template>
  <div>
    <Menu
      mode="horizontal"
      :active-name="activeMenu"
      style="height: 45px; line-height: 45px;"
      @on-select="changeMenuItem"
    >
      <MenuItem name="Introduction">
        <Icon type="ios-paper" />Introduction
      </MenuItem>
      <MenuItem name="Tasks">
        <Icon type="ios-construct" />Task
      </MenuItem>
      <MenuItem name="Activities">
        <Icon type="logo-steam" />Pathway
      </MenuItem>
      <MenuItem name="Resources">
        <Icon type="ios-albums" />Resource
      </MenuItem>
    </Menu>
    <div v-show="activeMenu=='Introduction'">
      <activity-show
        :activityInfo="activityInfo"
        :nameConfirm="nameConfirm"
        :userInfo="userInfo"
        :projectInfo="projectInfo"
        v-on:enterActivity="enterActivity"
        v-on:enterRootActivity="enterRootActivity"
      ></activity-show>
    </div>
    <div v-show="activeMenu=='Resources'">
      <folder-tree
        style="margin-top: 20px"
        :activityInfo="activityInfo"
        @toolPanel="listenToolPanel"
      ></folder-tree>
    </div>
    <div v-show="activeMenu=='Tasks'" @click="initTaskSocket">
      <task-manager :activityInfo="activityInfo" :projectInfo="projectInfo" :childActivities="childActivities" :userInfo="userInfo" ></task-manager>
    </div>
    <div v-if="activeMenu=='Activities'">
      <process-manager :activityInfo="activityInfo" :childActivities="childActivities" :projectInfo="projectInfo" :userInfo="userInfo" ></process-manager>
    </div>
    <mini-chatroom
      :activityInfo="activityInfo"
    ></mini-chatroom>
  </div>
</template>
<script>
import activityShow from "./activityShow.vue";
import folderTree from "../../resources/folderTree";
import taskManager from "./utils/taskManger.vue";
import processManager from "./utils/processManager.vue";
import miniChatroom from "./utils/miniChatroom";
import * as socketApi from "../../../api/socket";

export default {
  props: [
      "activityInfo",
      "userInfo",
      "projectInfo",
      "childActivities",
      "nameConfirm"
    ],
  components: {
    activityShow,
    folderTree,
    taskManager,
    processManager,
    miniChatroom
  },
  data() {
    return {
      activeMenu: "Introduction",
      userRole: "visitor",
    };
  },
  created(){
  },
  mounted() {},
  methods: {
    enterActivity(activity){
      this.$emit('enterActivity', activity);
    },
    enterRootActivity(){
      this.$emit('enterRootActivity');
    },
    listenToolPanel(data) {
      this.panel = data;
    },
    changeMenuItem(name) {
      this.activeMenu = name;
    },
    initTaskSocket(){
      this.socketId = "OperationServer/task/vueTask";
      socketApi.initWebSocket(this.socketId);
      socketApi.sendSock(this.socketId, {"type":"test"}, this.socketOnMessage);
      console.log("in in in")
    },
    socketOnMessage(messageJson){
      let behavior = messageJson.behavior;
      let content = messageJson.content;
      if (messageJson.type == "task"){
        if(behavior == "create"){
          let task = content.newTask;
          this.taskTodo.push(task);
        } else if (behavior == "importance") {
          let taskState = content.state;
          let taskId = content.taskId;
          let importance = content.importance;
          switch (taskState) {
            case "todo":
              this.taskTodo.forEach(item => {
                if (item.taskId == taskId) {
                  item.importance = importance;
                }
              })
              break;
            case "doing":
              this.taskDoing.forEach(item => {
                if (item.taskId == taskId) {
                  item.importance = importance;
                }
              })
              break;
            case "done":
              this.taskDone.forEach(item => {
                if (item.taskId == taskId) {
                  item.importance = importance;
                }
              })
              break;
          }
        } else if (behavior == "updateTask") {
          let taskState = content.state;
          let putTask = content.editedTask;
          let taskId = putTask.taskId;
          switch (taskState) {
            case "todo":
              this.taskTodo.forEach(item => {
                if (item.taskId == taskId) {
                  Object.assign(item, putTask);
                }
              })
              break;
            case "doing":
              this.taskDoing.forEach(item => {
                if (item.taskId == taskId) {
                  Object.assign(item, putTask);
                }
              })
              break;
            case "done":
              this.taskDone.forEach(item => {
                if (item.taskId == taskId) {
                  Object.assign(item, putTask);
                }
              })
              break;
          }
        } else if (behavior == "order") {
          let taskState = content.state;
          let taskList = content.taskList;
          switch (taskState) {
            case "todo":
              this.$set(this, "taskTodo", taskList);
              break;
            case "doing":
              this.$set(this, "taskDoing", taskList);
              break;
            case "done":
              this.$set(this, "taskDone", taskList);
              break;
          }
        }else if (behavior == "remove"){
          let taskState = content.state;
          let index = content.removeIndex;
          switch (taskState) {
            case "todo":
              this.taskTodo.splice(index, 1);
              break;
            case "doing":
              this.taskDoing.splice(index, 1);
              break;
            case "done":
              this.taskDone.splice(index, 1);
              break;
          }
        }
      }
    },
  },
};
</script>
<style scoped>
</style>
