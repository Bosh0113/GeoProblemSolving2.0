<style scoped>
.modelToolBtn {
  height: 60px;
  width: 60px;
  float: left;
  padding: 0;
  color: #00c0ff;
}
.modelToolsetBtn {
  height: 60px;
  width: 60px;
  float: left;
  padding: 0;
  color: #19be6b;
}
.ellipsis {
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  vertical-align: top;
}
</style>
<template>
  <div>
    <Card dis-hover>
      <div slot="title">
        <h4>Toolsets and tools</h4>
      </div>
      <div slot="extra" v-if="permissionIdentity('workspace_tool')">
        <manage-tools
          :step-info="stepInfo"
          @updateStepTools="stepToolListChanged"
          :key="toolModal"
          title="Manage toolsets and tools"
          style="margin-top:-10px"
        ></manage-tools>
      </div>
      <div>
        <div v-if="toolsetList.length<1" style="text-align: center;">
          <h2 style="color:#808695">No toolsets</h2>
          <small style="color:#dcdee2">*Click the button on the top right to add toolsets.</small>
        </div>
        <div
          v-for="toolset in toolsetList"
          :key="toolset.index"
          style="margin-top:15px;width:130px;display: inline-block;"
          v-else
        >
          <Card style="background-color: #3f51b530;cursor: pointer;margin: 0 5px 10px 5px">
            <div style="text-align:center" @click="showTools(toolset)">
              <Tooltip placement="bottom" max-width="600">
                <img
                  :src="toolset.toolsetImg"
                  v-if="toolset.toolsetImg!=''"
                  style="height:100%;max-height:50px;"
                />
                <avatar :username="toolset.toolsetName" :size="50" style="margin-bottom:6px" v-else></avatar>
                <div slot="content">
                  <span>{{toolset.description}}</span>
                  <br v-if="toolset.categoryTag.length>0" />
                  <p>
                    <i>{{toolset.categoryTag.join(',')}}</i>
                  </p>
                </div>
              </Tooltip>
              <h4
                :title="toolset.toolsetName"
                style="width:75px;"
                class="ellipsis"
              >{{toolset.toolsetName}}</h4>
            </div>
          </Card>
        </div>
        <Divider style="margin:10px 0" />
        <div v-if="toolList.length<1" style="text-align: center;">
          <h2 style="color:#808695">No tools</h2>
          <small style="color:#dcdee2">*Click the button on the top right to add tools.</small>
        </div>
        <div
          v-for="tool in toolList"
          :key="tool.index"
          style="margin-top:15px;width:130px;display: inline-block;"
          v-else
        >
          <Card style="background-color: ghostwhite;margin: 0 5px 10px 5px">
            <div style="text-align:center;cursor: pointer;" @click="useTool(tool)">
              <Tooltip placement="bottom" max-width="600">
                <img
                  :src="tool.toolImg"
                  v-if="tool.toolImg!=''"
                  style="height:100%;max-height:50px;"
                />
                <avatar :username="tool.toolName" :size="50" style="margin-bottom:6px" v-else></avatar>
                <div slot="content">
                  <span>{{tool.description}}</span>
                  <br v-if="tool.categoryTag.length>0" />
                  <span>
                    <i>{{tool.categoryTag.join(',')}}</i>
                  </span>
                </div>
              </Tooltip>
              <h4
                :title="tool.toolName"
                style="display:block;width:90px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;"
              >{{tool.toolName}}</h4>
            </div>
          </Card>
        </div>
      </div>
    </Card>
    <Modal
      v-model="showToolsetToolsModal"
      width="432"
      title="Tools in the toolset"
      :closable="false"
    >
      <div style="height:350px" v-if="toolsetToolList.length>0">
        <vue-scroll :ops="ops">
          <Row>
            <Col span="8" v-for="tool in toolsetToolList" :key="tool.index" style="margin-top:15px">
              <Card style="background-color: ghostwhite;margin: 0 5px 10px 5px">
                <div style="text-align:center;cursor: pointer;" @click="useTool(tool)">
                  <Tooltip placement="bottom" max-width="600">
                    <img
                      :src="tool.toolImg"
                      v-if="tool.toolImg!=''"
                      style="height:100%;max-height:50px;"
                    />
                    <avatar :username="tool.toolName" :size="50" style="margin-bottom:6px" v-else></avatar>
                    <div slot="content">
                      <span>{{tool.description}}</span>
                      <br v-if="tool.categoryTag.length>0" />
                      <span>
                        <i>{{tool.categoryTag.join(',')}}</i>
                      </span>
                    </div>
                  </Tooltip>
                  <h4
                    :title="tool.toolName"
                    style="display:block;width:90px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;"
                  >{{tool.toolName}}</h4>
                </div>
              </Card>
            </Col>
          </Row>
        </vue-scroll>
      </div>
      <div style="height:200px" v-else>
        <div style="text-align: center;height:200px;position: relative;">
          <div
            style="position: absolute;top: 0;bottom: 0;left: 0;right: 0;margin: auto;height: fit-content;"
          >
            <h1>No tools in this toolset.</h1>
          </div>
        </div>
      </div>
      <div slot="footer"></div>
    </Modal>
    <Modal
      v-model="jupyterModal"
      title="Open a jupyter notebook environment"
      @on-ok="checkJupyterUser"
      ok-text="Yes"
      cancel-text="Cancel"
    >
      <h3>Note: Jupyter notebooks will be accessed by all members in this project.</h3>
    </Modal>
    <Modal
      v-model="openToolModal"
      title="Open tool"
    >
      <h3>How would you like to open the tool?</h3>
      <div slot="footer" style="text-align:center">
        <Button @click="openToolInWindow" type="primary" style="margin:0 15px">This window</Button>
        <Button @click="openToolNewWindow" style="margin:0 15px">New window</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
import manageTools from "./../../tools/toolToStepModal";
import Avatar from "vue-avatar";
export default {
  props: ["stepInfo", "userRole", "projectInfo"],
  components: {
    manageTools,
    Avatar
  },
  watch: {
    stepInfo(val) {
      this.checkScope();
      this.getAllTools();
    }
  },
  data() {
    return {
      ops: {
        bar: {
          background: "#808695"
        }
      },
      userInfo: this.$store.getters.userInfo,
      toolList: [],
      toolsetList: [],
      toolModal: 0,
      toolsetToolList: [],
      showToolsetToolsModal: false,
      panelList: [],
      scopeType: "subproject",
      jupyterModal: false,
      selectedTool:{},
      openToolModal:false
    };
  },
  mounted() {
    this.checkScope();
    this.getAllTools();

    Date.prototype.Format = function(fmt) {
      var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S+": this.getMilliseconds() //毫毛
      };
      if (/(y+)/.test(fmt))
        fmt = fmt.replace(
          RegExp.$1,
          (this.getFullYear() + "").substr(4 - RegExp.$1.length)
        );
      for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
          fmt = fmt.replace(
            RegExp.$1,
            RegExp.$1.length == 1
              ? o[k]
              : ("00" + o[k]).substr(("" + o[k]).length)
          );
      return fmt;
    };
  },
  methods: {
    permissionIdentity(operation) {
      if (
        this.projectInfo.permissionManager != undefined &&
        operation === "workspace_tool"
      ) {
        if (
          this.userRole == "PManager" &&
          this.projectInfo.permissionManager.workspace_tool.project_manager
        ) {
          return true;
        } else if (
          this.userRole == "Manager" &&
          this.projectInfo.permissionManager.workspace_tool.subproject_manager
        ) {
          return true;
        } else if (
          this.userRole == "Member" &&
          this.projectInfo.permissionManager.workspace_tool.member
        ) {
          return true;
        }
      }
    },
    checkScope() {
      if (
        this.stepInfo.subProjectId == null ||
        this.stepInfo.subProjectId == "" ||
        this.stepInfo.subProjectId == undefined
      ) {
        this.scopeType = "project";
      } else {
        this.scopeType = "subproject";
      }
    },
    getAllTools() {
      if (
        this.stepInfo.toolList != null &&
        this.stepInfo.toolList != undefined
      ) {
        this.getToolInfos(this.stepInfo.toolList);
      }
      if (
        this.stepInfo.toolsetList != null &&
        this.stepInfo.toolsetList != undefined
      ) {
        this.getToolsetInfos(this.stepInfo.toolsetList);
      }
    },
    getToolInfos(toolIds) {
      var toolsCount = toolIds.length;
      var flagCount = toolsCount;
      var ToolInfos = [];
      for (var i = 0; i < toolsCount; i++) {
        this.axios
          .get(
            "/GeoProblemSolving/tool/inquiry" +
              "?key=" +
              "tId" +
              "&value=" +
              toolIds[i]
          )
          .then(res => {
            if (res.data == "Offline") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else if (res.data === "Fail") {
              this.$Notice.error({ desc: "Loading tools fail." });
            } else if (res.data === "None") {
              // this.$Notice.error({ desc: "There is no existing tool" });
            } else {
              ToolInfos.push(res.data[0]);
              if (--flagCount < 1) {
                var sortTools = [];
                for (var j = 0; j < toolsCount; j++) {
                  for (var k = 0; k < toolsCount; k++) {
                    if (toolIds[j] == ToolInfos[k].tId) {
                      sortTools.push(ToolInfos[k]);
                      break;
                    }
                  }
                }
                this.$set(this, "toolList", sortTools);
              }
            }
          })
          .catch(err => {
            console.log(err);
          });
      }
    },
    getToolsetInfos(toolsetIds) {
      var toolsetsCount = toolsetIds.length;
      var flagCount = toolsetsCount;
      var toolsetInfos = [];
      for (var i = 0; i < toolsetsCount; i++) {
        this.axios
          .get(
            "/GeoProblemSolving/toolset/inquiry" +
              "?key=" +
              "tsId" +
              "&value=" +
              toolsetIds[i]
          )
          .then(res => {
            if (res.data == "Offline") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else if (res.data === "Fail") {
              this.$Notice.error({ desc: "Loading toolsets fail." });
            } else if (res.data === "None") {
              // this.$Notice.error({ desc: "There is no existing toolset" });
            } else {
              toolsetInfos.push(res.data[0]);
              if (--flagCount < 1) {
                var sortToolsets = [];
                for (var j = 0; j < toolsetsCount; j++) {
                  for (var k = 0; k < toolsetsCount; k++) {
                    if (toolsetIds[j] == toolsetInfos[k].tsId) {
                      sortToolsets.push(toolsetInfos[k]);
                      break;
                    }
                  }
                }
                this.$set(this, "toolsetList", sortToolsets);
              }
            }
          })
          .catch(err => {
            console.log(err);
          });
      }
    },
    stepToolListChanged(tools, toolsets) {
      this.stepInfo.toolList = tools;
      this.stepInfo.toolsetList = toolsets;
      this.toolModal++;
      this.getAllTools(tools, toolsets);
    },
    showTools(toolsetInfo) {
      if (this.userRole != "Visitor" && this.userRole != "Token") {
        this.toolsetToolList = toolsetInfo.toolList;
        this.showToolsetToolsModal = true;
      } else {
        this.$Notice.info({
          desc: "Please login before using toolsets and join this project."
        });
      }
    },
    useTool(toolInfo) {
      if (this.userRole != "Visitor" && this.userRole != "Token") {
        this.selectedTool = toolInfo;
        this.openToolModal = true;
      } else {
        this.$Notice.info({
          desc: "Please login before using toolsets and join this project."
        });
      }
    },
    openToolInWindow(){
      var toolInfo = this.selectedTool;
      this.openToolModal = false;
      // 记录信息
      let toolRecords = {
        type: "tools",
        time: new Date().Format("yyyy-MM-dd HH:mm:ss"),
        who: this.userInfo.userName,
        content: "used a tool",
        toolType: toolInfo.toolName
      };
      this.$emit("toolBehavior", toolRecords);

      if (toolInfo.toolName == "Jupyter notebook") {
        this.jupyterModal = true;
        return;
      }

      var toolURL =
        '<iframe src="' +
        toolInfo.toolUrl +
        "?userName=" +
        this.userInfo.userName +
        "&userID=" +
        this.userInfo.userId +
        "&groupID=" +
        this.stepInfo.stepId +
        '" style="width: 100%;height:100%;" frameborder="0"></iframe>';

      var demoPanelTimer = null;
      if (this.scopeType == "project") {
        parent.vm.showToolPanel(toolURL, toolInfo.toolName);
      } else {
        var panel = parent.jsPanel.create({
          theme: "success",
          headerTitle: toolInfo.toolName,
          footerToolbar: '<p style="height:10px"></p>',
          contentSize: "800 400",
          content: toolURL,
          disableOnMaximized: true,
          dragit: {
            containment: 5
          },
          closeOnEscape: true,
          onclosed: function(panel, status, closedByUser) {
            window.clearTimeout(demoPanelTimer);
          }
        });
        $(".jsPanel-content").css("font-size", "0");
        this.panelList.push(panel);
        this.$emit("toolPanel", panel);
      }
    },
    openToolNewWindow(){
      var toolInfo = this.selectedTool;
      this.openToolModal = false;
      // 记录信息
      let toolRecords = {
        type: "tools",
        time: new Date().Format("yyyy-MM-dd HH:mm:ss"),
        who: this.userInfo.userName,
        content: "used a tool",
        toolType: toolInfo.toolName
      };
      this.$emit("toolBehavior", toolRecords);

      if (toolInfo.toolName == "Jupyter notebook") {
        this.jupyterModal = true;
        return;
      }

      var toolURL =
        toolInfo.toolUrl +
        "?userName=" +
        this.userInfo.userName +
        "&userID=" +
        this.userInfo.userId +
        "&groupID=" +
        this.stepInfo.stepId;
        window.open(toolURL);
    },
    checkJupyterUser() {
      this.axios
        .get(
          "/GeoProblemSolving/jupyter/inquiry?projectId=" +
            this.projectInfo.projectId
        )
        .then(res => {
          if (res.data == "None") {
            this.prepareJupyter();
          } else {
            this.jupyterLogin(res.data[0].jupyterUserId);
          }
        })
        .catch(err => {});
    },
    jupyterLogin(jupyterUserId) {
      let jupyterUrl = "";
      if (this.$store.state.IP_Port == "localhost:8080") {
        jupyterUrl =
          "http://172.21.212.83";
      }
      else if(this.$store.state.IP_Port == "118.190.246.198:80" || this.$store.state.IP_Port == "www.geofuturelab.com"){
        jupyterUrl =
          "http://118.190.246.198:8000";
      }

      let loginInfo = {
        JupyterUser: jupyterUserId,
        userId: this.userInfo.userId,
        projectId: this.projectInfo.projectId
      };
      let info = JSON.stringify(loginInfo);
      
      let data = new FormData();
      data.append("login-info", info);
      this.axios
        .post(
          jupyterUrl + "/hub/login?next=/hub/user/" + jupyterUserId,
          data
        )
        .then(res => {
          let url = jupyterUrl + "/hub/user/" + jupyterUserId;
          window.open(url);
        })
        .catch(err => {
          let url = jupyterUrl + "/hub/user/" + jupyterUserId;
          window.open(url);
        });
    },
    prepareJupyter() {
      let jupyterUrl = "";
      if (this.$store.state.IP_Port == "localhost:8080") {
        jupyterUrl =
          "http://172.21.212.83";
      }
      else if(this.$store.state.IP_Port == "118.190.246.198:80" || this.$store.state.IP_Port == "www.geofuturelab.com"){
        jupyterUrl =
          "http://118.190.246.198:8000";
      }

      let name_jupyterhub = this.projectInfo.projectId;
      name_jupyterhub = name_jupyterhub.replace(/[-]/g, "");

      this.axios
        .post(
          jupyterUrl + "/hub/api/users/" + name_jupyterhub,
          { name: name_jupyterhub },
          {
            headers: {
              Authorization: "token 50e3fa2f34c74d36b09e967733a621b0"
            }
          }
        )
        .then(res => {
          this.createJupyterUser(name_jupyterhub);
        })
        .catch(err => {
          console.log(err);
        });
    },
    createJupyterUser(name_jupyterhub) {
      let data = {
        projectId: this.projectInfo.projectId,
        jupyterUserId: name_jupyterhub
      };
      this.axios
        .post("/GeoProblemSolving/jupyter/create", data)
        .then(res => {
          if (res.data == "Success") {
            this.$Notice.info({
              desc: "Create Jupyter notebook successfully. It can be used now."
            });
          }
        })
        .catch(err => {});
    }
  }
};
</script>