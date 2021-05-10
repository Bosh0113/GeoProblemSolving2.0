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
      <activity-show :activityInfo="activityInfo" :nameConfirm="nameConfirm"></activity-show>
    </div>
    <div v-show="activeMenu=='Resources'">
      <folder-tree
        style="margin-top: 10px"
        :activityInfo="activityInfo"
        @toolPanel="listenToolPanel"
      ></folder-tree>
    </div>
    <div v-show="activeMenu=='Tasks'">
      <task-manager :activityInfo="activityInfo" :childActivities="childActivities" ></task-manager>
    </div>
    <div v-show="activeMenu=='Activities'">
      <process-manager :activityInfo="activityInfo" :childActivities="childActivities" ></process-manager>
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

export default {
  props: ["activityInfo", "userInfo","childActivities","nameConfirm"],
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
  mounted() {},
  methods: {
    listenToolPanel(data) {
      this.panel = data;
    },
    changeMenuItem(name) {
      this.activeMenu = name;
    },
  },
};
</script>
<style scoped>
</style>