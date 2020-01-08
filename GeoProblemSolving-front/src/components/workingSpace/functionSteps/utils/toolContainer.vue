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
      <div slot="extra" v-show="userRole!='Visitor'">
        <manage-tools
          :step-info="stepInfo"
          @updateStepTools="stepToolListChanged"
          :key="toolModal"
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
  </div>
</template>
<script>
import manageTools from "./../../../tools/toolToStepModal";
import Avatar from "vue-avatar";
export default {
  props: ["stepInfo", "userRole"],
  components: {
    manageTools,
    Avatar
  },
  watch: {
    stepInfo(val) {
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
      panelList: []
    };
  },
  mounted() {
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
      this.toolsetToolList = toolsetInfo.toolList;
      this.showToolsetToolsModal = true;
    },
    useTool(toolInfo) {
      let toolURL =
        '<iframe src="' +
        toolInfo.toolUrl +
        "?userName=" +
        this.userInfo.userName +
        "&userID=" +
        this.userInfo.userId +
        "&groupID=" +
        this.stepInfo.stepId +
        '" style="width: 100%;height:100%;"></iframe>';
      var demoPanelTimer = null;
      var panel = jsPanel.create({
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
      // panel.resizeit("disable");
      $(".jsPanel-content").css("font-size", "0");
      this.panelList.push(panel);
      this.$emit("toolPanel", panel);

      // 记录信息
      let toolRecords = {
        type: "tools",
        time: new Date().Format("yyyy-MM-dd HH:mm:ss"),
        who: this.userInfo.userName,
        content: "used a tool",
        toolType: toolInfo.toolName
      };
      this.$emit("toolBehavior", toolRecords);
    }
  }
};
</script>