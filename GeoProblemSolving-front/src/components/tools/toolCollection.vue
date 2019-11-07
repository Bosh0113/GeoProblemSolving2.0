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
.changeGreenColor:hover{
    background-color: #19be6b;
    color: white;
}

</style>
<template>
  <div :style="{height: contentHeight+'px'}">
    <Card dis-hover>
      <h1 slot="title">Tool management center</h1>
      <div>
        <Row>
          <Col span="16">
          <div span="2" style="height: inherit;width: 90px;position: absolute;" :style="{height: contentHeight-89+'px'}">
            <Menu
              active-name="publicTools"
              @on-select="changeMenuItem"
              style="height: inherit;width: fit-content;z-index:1"
            >
              <MenuItem name="publicTools" class="leftMenuItem">
                <Tooltip content="Public tools" placement="right">
                <Icon type="logo-dropbox" size="35"></Icon>
                </Tooltip>
              </MenuItem>
              <MenuItem name="personalTools" class="leftMenuItem">
                <Tooltip content="Personal tools" placement="right">
                <Icon type="ios-cube" size="35"></Icon>
                </Tooltip>
              </MenuItem>
            </Menu>
          </div>
            <Card dis-hover style="margin-left: 80px">
              <h1 slot="title" style="padding-top:5px" v-if="showMenuItem=='publicTools'">Public tools</h1>
              <h1 slot="title" style="padding-top:5px" v-if="showMenuItem=='personalTools'">Personal tools</h1>
              <div slot="extra">
                <Button class="changeGreenColor" shape="circle" icon="ios-add-circle-outline" @click="createTool()">Create tool</Button>
                <Button class="changeGreenColor" shape="circle" icon="ios-add-circle-outline" @click="createToolset()">Create toolset</Button>
              </div>
              <div v-if="showMenuItem=='publicTools'" style="height: inherit;min-height: fit-content;" :style="{height: contentHeight-178+'px'}">
                  <h3>Public tools</h3>
              </div>
              <div v-if="showMenuItem=='personalTools'" style="height: inherit;min-height: fit-content;" :style="{height: contentHeight-178+'px'}">
                  <h3>Personal tools</h3>
              </div>
            </Card>
          </Col>
          <Col span="8">
            <div style="padding: 0 5px;margin-left: 15px;">
              <Card dis-hover>
                <h1 slot="title" style="padding-top:5px">Tools in step</h1>
                <div :style="{height: contentHeight-178+'px'}">

                </div>
              </Card>
            </div>
          </Col>
        </Row>
      </div>
    </Card>
  </div>
</template>
<script>
export default {
  components: {},
  mounted() {
    this.resizeContent();
    this.getProjectList();
  },
  data() {
    return {
      contentHeight: "",
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
    resizeContent() {
      if (window.innerHeight > 675) {
        this.contentHeight = window.innerHeight - 120;
      } else {
        this.contentHeight = 555;
      }
      window.onresize = () => {
        return (() => {
          this.resizeContent();
        })();
      };
    },
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
