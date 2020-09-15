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
      <MenuItem name="Resources">
        <Icon type="ios-albums" />Resources
      </MenuItem>
      <MenuItem name="Tasks">
        <Icon type="ios-construct" />Tasks
      </MenuItem>
      <MenuItem name="Activities">
        <Icon type="ios-analytics" />Activities
      </MenuItem>
      <!-- <MenuItem name="Discussion">
        <Icon type="ios-people" />Discussion
      </MenuItem> -->
    </Menu>
    <div v-show="activeMenu=='Introduction'">
      <activity-show :activityInfo="activityInfo"></activity-show>
    </div>
    <div v-show="activeMenu=='Resources'">
      <folder-tree
        style="margin-top: 10px"
        :activityInfo="activityInfo"
        @toolPanel="listenToolPanel"
      ></folder-tree>
    </div>
    <div v-show="activeMenu=='Tasks'">
      <task-manager :activityInfo="activityInfo"></task-manager>
    </div>
    <div v-show="activeMenu=='Activities'">
      <process-manager :activityInfo="activityInfo"></process-manager>
    </div>
    <div v-show="activeMenu=='Discussion'"></div>
  </div>
</template>
<script>
import activityShow from "./activityShow.vue";
import folderTree from "../../resources/folderTree";
import taskManager from "./utils/taskManger.vue";
import processManager from "./utils/processManager.vue";

export default {
  props: ["activityInfo", "userInfo"],
  components: {
    activityShow,
    folderTree,
    taskManager,
    processManager,
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