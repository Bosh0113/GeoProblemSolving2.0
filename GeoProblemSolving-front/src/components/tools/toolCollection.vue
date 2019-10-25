<style scoped>
.selector {
  width: 250px;
}
.fileBtnHoverGreen:hover {
  background-color: #19be6b;
  color: white;
}
.fileBtnHoverGray:hover {
  background-color: #808695;
  color: white;
}
.leftMenuItem {
  margin: 0 0 10px 0;
}
</style>
<template>
  <div>
    <div span="2" style="height: inherit;width: 90px;position: absolute;">
      <Menu
        active-name="publicTools"
        @on-select="changeMenuItem"
        style="height: inherit;width: fit-content;"
      >
        <MenuItem name="publicTools" class="leftMenuItem">
          <Icon type="logo-dropbox" title="Public tools" size="35"></Icon>
        </MenuItem>
        <MenuItem name="personalTools" class="leftMenuItem">
          <Icon type="ios-cube" title="Personal tools" size="35"></Icon>
        </MenuItem>
      </Menu>
    </div>
    <div style="margin-left: 90px">
      <Card shadow>
        <h1 slot="title" style="padding-top:5px">Tool Collection for Solving Geo-problem</h1>
        <div slot="extra" style="cursor:pointer" @click="createTool()">
          <Icon type="ios-add-circle-outline" />Create your tool
        </div>
        <div slot="extra" style="cursor:pointer" @click="createToolset()">
          <Icon type="ios-add-circle-outline" />Create your toolset
        </div>
        <!-- <div v-if="showMenuItem=='publicTools'" style="height: inherit;min-height: fit-content;"></div>
        <div v-if="showMenuItem=='personalTools'" style="height: inherit;min-height: fit-content;"></div>-->
      </Card>
    </div>
  </div>
</template>
<script>
export default {
  components: {},
  mounted() {
    this.getProjectList();
  },
  data() {
    return {
      userInfo: this.$store.getters.userInfo,
      showMenuItem: "publicTools",
      // 待用
      ops: {
        bar: {
          background: "#808695"
        }
      },
      projectList: [],
      subProjectList: [],
      subProjectDisable: true,
      scopeId: "",
      toolsetList: [],
      toolList: []
    };
  },
  methods: {
    getProjectList() {},
    getSubProjectList(projectId) {},
    getStepList(subProjectId) {},
    getToolsets() {
      this.axios
        .get(
          "/GeoProblemSolving/toolset/inquiryAll" +
            "?provider=" +
            this.$store.getters.userId
        )
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data === "Fail") {
            this.$Notice.error({ desc: "Loading tool fail." });
          } else if (res.data === "None") {
            this.$Notice.error({ desc: "There is no existing toolset" });
          } else {
            this.$set(this, "toolsetList", res.data);
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    getTools() {
      this.axios
        .get(
          "/GeoProblemSolving/tool/inquiryAll" +
            "?provider=" +
            this.$store.getters.userId
        )
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data === "Fail") {
            this.$Notice.error({ desc: "Loading tool fail." });
          } else if (res.data === "None") {
            this.$Notice.error({ desc: "There is no existing tool" });
          } else {
            this.$set(this, "toolList", res.data);
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    changeProject(projectId) {
      this.scopeId = projectId;
      this.subProjectDisable = true;
      this.getSubProjectList(projectId);
    },
    changeSubProject(subProjectId) {},
    changeStep(stepId) {},
    changeFileType(value) {},
    changeMenuItem(name) {
      this.showMenuItem = name;
    },
    createTool() {
      this.$router.push({ path: "createNewTool" });
    },
    createToolset() {
      this.$router.push({ path: "createToolset" });
    }
  }
};
</script>
