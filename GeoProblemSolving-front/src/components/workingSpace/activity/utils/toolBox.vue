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
.toolCard >>> .ivu-card-head {
  padding: 11px 16px;
}
</style>
<template>
  <div>
    <Card dis-hover class="toolCard">
      <div slot="title">
        <h4>Tools</h4>
      </div>
      <div
        slot="extra"
        v-if="
          permissionIdentity(activityInfo.permission, userRole, 'manage_tool')
        "
      >
        <manage-tools
          :activity-info="activityInfo"
          :tool-list="toolList"
          @updateStepTools="stepToolListChanged"
          title="Manage toolsets and tools"
          style="margin-top: -10px"
        ></manage-tools>
      </div>
      <vue-scroll :ops="ops" style="max-height: calc(100vh - 200px)">
        <div
          v-if="toolList != undefined && toolList.length < 1"
          style="text-align: center"
        >
          <h2 style="color: #808695">No tools</h2>
          <small style="color: #dcdee2"
            >*Click the button on the top right to add tools.</small
          >
        </div>
        <div
          v-for="tool in toolList"
          :key="tool.index"
          style="width: 100px; display: inline-block"
          v-else
        >
          <Card
            style="margin: 5px; height: 50px; width: 50px"
            v-show="toolsetLevel > 0"
            title="Back to last level"
            ><Icon
              type="md-arrow-back"
              size="30"
              style="text-align: center; margin-top: 10px"
              color="#5089b8"
              @click="back2LastLevel()"
          /></Card>
          <Card
            style="padding-top: 5px; background-color: #d3f1b1; margin: 5px"
            v-if="tool.isToolset"
          >
            <div
              style="text-align: center; cursor: pointer"
              @click="expandTool(tool)"
            >
              <Tooltip placement="bottom" max-width="600">
                <img
                  :src="tool.toolImg"
                  v-if="tool.toolImg != ''"
                  style="height: 100%; max-height: 50px"
                />
                <avatar
                  :username="tool.toolName"
                  :size="50"
                  style="margin-bottom: 6px"
                  v-else
                ></avatar>
                <div
                  slot="content"
                  v-if="
                    tool.description !== '' ||
                    (tool.tags != undefined && tool.tags.length > 0)
                  "
                >
                  <span>{{ tool.description }}</span>
                  <template
                    v-if="tool.tags != undefined && tool.tags.length > 0"
                  >
                    <br />
                    <span>
                      <i>{{ tool.tags.join(" | ") }}</i>
                    </span>
                  </template>
                </div>
              </Tooltip>
              <h4
                :title="tool.toolName"
                style="
                  display: block;
                  overflow: hidden;
                  white-space: nowrap;
                  text-overflow: ellipsis;
                "
              >
                {{ tool.toolName }}
              </h4>
            </div>
          </Card>
          <Card
            style="padding-top: 5px; background-color: ghostwhite; margin: 5px"
          >
            <div
              style="text-align: center; cursor: pointer"
              @click="useTool(tool)"
            >
              <Tooltip placement="bottom" max-width="600">
                <img
                  :src="tool.toolImg"
                  v-if="tool.toolImg != ''"
                  style="height: 100%; max-height: 50px"
                />
                <avatar
                  :username="tool.toolName"
                  :size="50"
                  style="margin-bottom: 6px"
                  v-else
                ></avatar>
                <div
                  slot="content"
                  v-if="
                    tool.description !== '' ||
                    (tool.tags != undefined && tool.tags.length > 0)
                  "
                >
                  <span>{{ tool.description }}</span>
                  <template
                    v-if="tool.tags != undefined && tool.tags.length > 0"
                  >
                    <br />
                    <span>
                      <i>{{ tool.tags.join(" | ") }}</i>
                    </span>
                  </template>
                </div>
              </Tooltip>
              <h4
                :title="tool.toolName"
                style="
                  display: block;
                  overflow: hidden;
                  white-space: nowrap;
                  text-overflow: ellipsis;
                "
              >
                {{ tool.toolName }}
              </h4>
            </div>
          </Card>
        </div>
      </vue-scroll>
    </Card>
    <Modal
      v-model="jupyterModal"
      title="Open a jupyter notebook environment"
      @on-ok="checkJupyterUser"
      ok-text="Yes"
      cancel-text="Cancel"
    >
      <h3>
        Note: Jupyter notebooks will be accessed by all members in this project.
      </h3>
    </Modal>
    <!-- <Modal v-model="openToolModal" title="Open tool">
      <h2>How would you like to open this tool?</h2>
      <small style="color: #ff9900"
        >*Some tools are not supported to be used in page.</small
      >
      <div slot="footer" style="text-align: center">
        <Button @click="openTool" type="primary" style="margin: 0 15px"
          >This page</Button
        >
        <Button @click="openToolNewpage" style="margin: 0 15px"
          >New page</Button
        >
      </div>
    </Modal> -->
  </div>
</template>
<script>
import manageTools from "@/components/tools/toolToStepModal";
import Avatar from "vue-avatar";
import { get, del, post, put } from "@/axios";
export default {
  props: ["activityInfo"],
  components: {
    manageTools,
    Avatar,
  },
  watch: {
    activityInfo(val) {
      // console.log(val);
      this.getAllTools();
    },
  },
  data() {
    return {
      ops: {
        bar: {
          background: "#808695",
        },
      },
      userInfo: this.$store.getters.userInfo,
      userRole: "visitor",
      toolIdList: [],
      toolList: [],
      toolsetLevel: 0,
      lastLevelList: [],
      panelList: [],
      jupyterModal: false,
      selectedTool: {},
      operationStore: false,
      // openToolModal: false,
    };
  },
  created() {
    this.roleIdentity();
  },
  mounted() {
    this.getAllTools();
    Date.prototype.Format = function (fmt) {
      var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S+": this.getMilliseconds(), //毫毛
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
    roleIdentity() {
      this.userRole = this.userRoleApi.roleIdentify(
        this.activityInfo.members,
        this.userInfo.userId
      );
    },
    permissionIdentity(permission, role, operation) {
      return this.userRoleApi.permissionIdentity(
        JSON.parse(permission),
        role,
        operation
      );
    },
    getAllTools() {
      this.toolIdList = this.operationApi.getToollist();
      if (this.toolIdList != undefined && this.toolIdList.length !== 0) {
        this.getToolInfos();
      }
    },
    async getToolInfos() {
      let data = await post("/GeoProblemSolving/tool/all", this.toolIdList);
      this.$set(this, "toolList", data);
    },
    stepToolListChanged(tools, toolsets) {
      this.toolIdList = tools;
      this.toolIdList.push.apply(this.toolIdList, toolsets);
      this.getToolInfos();
    },
    // showTools(toolsetInfo) {
    //   this.toolsetToolList = toolsetInfo.toolList;
    //   this.showToolsetToolsModal = true;
    // },
    useTool(toolInfo) {
      this.selectedTool = toolInfo;
      this.openTool();
      // this.openToolModal = true;
    },
    back2LastLevel() {
      this.toolList = Object.assign([], this.lastLevelList);
      this.toolsetLevel--;
    },
    expandTool(toolset) {
      this.lastLevelList = Object.assign([], this.toolList);
      this.toolIdList = toolset.toolList;
      this.getToolInfos();
      this.toolsetLevel++;
    },
    openTool() {
      var toolInfo = this.selectedTool;
      // this.openToolModal = false;

      // record
      let toolRecords = {
        type: "tools",
        time: new Date().Format("yyyy-MM-dd HH:mm:ss"),
        who: this.userInfo.userName,
        content: "used a tool",
        toolType: toolInfo.toolName,
      };
      this.$emit("toolBehavior", toolRecords);

      if (toolInfo.scope == "outer") {
        this.openToolNewpage(toolInfo);
      } else if (toolInfo.scope == "inner") {
        this.openToolByPanel(toolInfo);
      } else {
        this.openToolByPanel(toolInfo);
      }
    },
    openToolByPanel(toolInfo) {
      // var toolURL = window.location.origin + `${toolInfo.toolUrl}`;
      // var toolURL = toolInfo.toolUrl;
      // var toolContent = `<iframe src="${toolURL}?userName=${this.userInfo.name}&userID=${this.userInfo.userId}&groupID=${this.activityInfo.aid}" style="width: 100%; height:100%;" frameborder="0"></iframe>`;
      var toolContent = `<iframe src="${toolInfo.toolUrl}" id="${toolInfo.tid}" style="width: 100%; height:100%;" frameborder="0"></iframe>`;

      var panel = jsPanel.create({
        theme: "success",
        footerToolbar: `<p></p>`,
        contentSize: "800 400",
        id: toolInfo.toolName,
        headerTitle: toolInfo.toolName,
        content: toolContent,
        container: "div#drawflow",
        data: { user: this.userInfo, aid: this.activityInfo.aid, taskId: "" },
        dragit: {
          containment: 5,
        },
        closeOnEscape: true,
        disableOnMaximized: true,
        onbeforeclose: function () {
            if (this.operationStore) {
              return true;
            } else {
              return confirm("This operation has not been bind to the task. Close panel immediately?");
            }
          }
      });
      $(".jsPanel-content").css("font-size", "0");
      this.panelList.push(panel);
      this.$emit("toolPanel", panel);

      // 设置iframe 父子页面消息传输处理
      window.addEventListener("message", this.toolMsgHandle, false);
      let activityId = this.activityInfo.aid;
      let userInfo = this.userInfo;
      let iFrame = document.getElementById(toolInfo.tid);
      //iframe加载完毕后再发送消息，否则子页面接收不到message
      iFrame.onload = function () {
          //iframe加载完立即发送一条消息
          iFrame.contentWindow.postMessage({"user": userInfo, "aid": activityId, type:"activity"}, "*");
      }

    },
    toolMsgHandle(event){
      console.log(event);
      this.operationStore = true;
    },
    openToolNewpage(toolInfo) {
      // this.openToolModal = false;

      if (toolInfo.toolName == "Jupyter notebook") {
        this.jupyterModal = true;
        return;
      }

      //要发送的参数
      let params = {
        userName: this.userInfo.name,
        userId: this.userInfo.userId,
        groupID: this.activityInfo.aid,
      };
      var toolURL = window.location.origin + `${toolInfo.toolUrl}`;
      window["pageParams"] = params;

      // open
      window.open(toolURL);
    },
    checkJupyterUser() {
      this.axios
        .get(
          "/GeoProblemSolving/jupyter/inquiry?projectId=" +
            this.projectInfo.projectId
        )
        .then((res) => {
          if (res.data == "None") {
            this.prepareJupyter();
          } else {
            this.jupyterLogin(res.data[0].jupyterUserId);
          }
        })
        .catch((err) => {});
    },
    jupyterLogin(jupyterUserId) {
      let jupyterUrl = "";
      if (this.$store.state.IP_Port == "localhost:8080") {
        jupyterUrl = "http://172.21.212.83";
      } else if (
        this.$store.state.IP_Port == "118.190.246.198:80" ||
        this.$store.state.IP_Port == "www.geofuturelab.com"
      ) {
        jupyterUrl = "http://118.190.246.198:8000";
      }

      let loginInfo = {
        JupyterUser: jupyterUserId,
        userId: this.userInfo.userId,
        projectId: this.projectInfo.projectId,
      };
      let info = JSON.stringify(loginInfo);

      let data = new FormData();
      data.append("login-info", info);
      this.axios
        .post(jupyterUrl + "/hub/login?next=/hub/user/" + jupyterUserId, data)
        .then((res) => {
          let url = jupyterUrl + "/hub/user/" + jupyterUserId;
          window.open(url);
        })
        .catch((err) => {
          let url = jupyterUrl + "/hub/user/" + jupyterUserId;
          window.open(url);
        });
    },
    prepareJupyter() {
      let jupyterUrl = "";
      if (this.$store.state.IP_Port == "localhost:8080") {
        jupyterUrl = "http://172.21.212.83";
      } else if (
        this.$store.state.IP_Port == "118.190.246.198:80" ||
        this.$store.state.IP_Port == "www.geofuturelab.com"
      ) {
        jupyterUrl = "http://118.190.246.198:8000";
      }

      let name_jupyterhub = this.projectInfo.projectId;
      name_jupyterhub = name_jupyterhub.replace(/[-]/g, "");

      this.axios
        .post(
          jupyterUrl + "/hub/api/users/" + name_jupyterhub,
          { name: name_jupyterhub },
          {
            headers: {
              Authorization: "token 50e3fa2f34c74d36b09e967733a621b0",
            },
          }
        )
        .then((res) => {
          this.createJupyterUser(name_jupyterhub);
        })
        .catch((err) => {
          throw err;
        });
    },
    createJupyterUser(name_jupyterhub) {
      let data = {
        projectId: this.projectInfo.projectId,
        jupyterUserId: name_jupyterhub,
      };
      this.axios
        .post("/GeoProblemSolving/jupyter/create", data)
        .then((res) => {
          if (res.data == "Success") {
            this.$Notice.info({
              desc: "Create Jupyter notebook successfully. It can be used now.",
            });
          }
        })
        .catch((err) => {});
    },
  },
};
</script>
