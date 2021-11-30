<template>
  <div class="card-devices">
    <div class="drawflow-node-title">
      <h3>{{ name }}</h3>
    </div>
    <div class="drawflow-node-body">
      <div
        v-if="!unfold"
        @click="operationShow"
        style="
          cursor: pointer;
          text-align: center;
          background-color: #f9fdf1;
          padding: 10px 0;
        "
      >
        See more <Icon type="md-more" size="20" />
      </div>
      <div v-else-if="unfold" style="cursor: default">
        <Timeline style="padding: 10px; height: 350px">
          <vue-scroll :ops="scrollOps">
            <TimelineItem v-for="record in operationRecords" :key="record.id">
              <p class="time">{{ record.time }}</p>
              <div class="content" v-if="record.type === 'resource'">
                Resource operation:
                <span class="behavior">{{ record.behavior }}</span>
                <div @click="checkRes(record.resource)" style="cursor: pointer">
                  <img
                    :src="dataUrl"
                    height="42px"
                    width="42px"
                    :title="record.resource.name"
                    v-if="record.resource.type == 'data'"
                  />
                  <img
                    :src="modelUrl"
                    height="42px"
                    width="42px"
                    :title="record.resource.name"
                    v-else-if="record.resource.type == 'model'"
                  />
                  <img
                    :src="documentUrl"
                    height="42px"
                    width="42px"
                    :title="record.resource.name"
                    v-else-if="record.resource.type == 'document'"
                  />
                  <img
                    :src="paperUrl"
                    height="42px"
                    width="42px"
                    :title="record.resource.name"
                    v-else-if="record.resource.type == 'paper'"
                  />
                  <img
                    :src="imageUrl"
                    height="42px"
                    width="42px"
                    :title="record.resource.name"
                    v-else-if="record.resource.type == 'iamge'"
                  />
                  <img
                    :src="videoUrl"
                    height="42px"
                    width="42px"
                    :title="record.resource.name"
                    v-else-if="record.resource.type == 'video'"
                  />
                  <img
                    :src="otherUrl"
                    height="42px"
                    width="42px"
                    :title="record.resource.name"
                    v-else-if="record.resource.type == 'others'"
                  />
                </div>
                <div class="participant">
                  <span style="margin-right: 5px">People: </span>
                  <avatar
                    class="person"
                    :username="record.operator.name"
                    :size="16"
                    :rounded="true"
                    :title="record.operator.name"
                  />
                </div>
              </div>
              <div class="content" v-else-if="record.type === 'tool'">
                Tool operation:
                <span class="behavior">{{ record.behavior }}</span>
                <div @click="checkTool(record.tool)" style="cursor: pointer">
                  <img
                    :src="toolUrl"
                    height="42px"
                    width="42px"
                    :title="record.tool.name"
                    class="tool"
                  />
                </div>
                <div class="participant">
                  <span style="margin-right: 5px">People: </span>
                  <avatar
                    class="person"
                    :username="record.operator.name"
                    :size="16"
                    :rounded="true"
                    :title="record.operator.name"
                  />
                </div>
              </div>
              <div class="content" v-else-if="record.type === 'communication'">
                <span class="behavior">Communication</span>
                <div>
                  <img
                    :src="chatUrl"
                    height="42px"
                    width="42px"
                    title="Communication records"
                    class="resources"
                  />
                </div>
                <div class="participant">
                  <span style="margin-right: 5px">People: </span>
                  <avatar
                    v-for="person in record.participants"
                    :key="person.id"
                    class="person"
                    :username="person.name"
                    :size="16"
                    :rounded="true"
                    :title="person.name"
                  />
                </div>
              </div>
              <div class="content" v-else-if="record.type === 'geo-analysis'">
                <span class="behavior">Geo-analysis by using tools</span>
                <div @click="checkGeoAnalysis(record.tool,record.outputs)" style="cursor: pointer">
                  <img
                    :src="toolUrl"
                    height="36px"
                    width="36px"
                    :title="record.tool.name"
                    class="resources"
                  />
                  <Divider
                    type="vertical"
                    style="height: 42px; margin-top: -30px"
                  />
                  <img
                    v-for="result in record.outputs"
                    :key="result.uid"
                    :src="dataUrl"
                    height="36px"
                    width="36px"
                    style="margin-right: 5px"
                    title="Result"
                    class="resources"
                  />
                </div>
                <div class="participant">
                  <span style="margin-right: 5px">People: </span>
                  <avatar
                    v-for="person in record.participants"
                    :key="person.id"
                    class="person"
                    :username="person.name"
                    :size="16"
                    :rounded="true"
                    :title="person.name"
                  />
                </div>
              </div>
              <Divider />
            </TimelineItem>
            <!-- <TimelineItem>
              <p class="time">2021-1-1 20:00:00</p>
              <div class="content">
                Resource <span class="behavior">upload</span>
                <div>
                  <img
                    :src="dataUrl"
                    height="36px"
                    width="36px"
                    title="Data"
                    class="resources"
                  />
                </div>
                <div class="participant">
                  <span style="margin-right: 5px">People: </span>
                  <avatar
                    class="person"
                    username="1XXX"
                    :size="16"
                    :rounded="true"
                    title="xxx"
                  />
                </div>
              </div>
              <Divider />
            </TimelineItem>
            <TimelineItem>
              <p class="time">2021-1-1</p>
              <div class="content">
                <span class="behavior">Tool add</span>
                <div>
                  <img
                    :src="toolUrl"
                    height="36px"
                    width="36px"
                    title="Data"
                    class="resources"
                  />
                </div>
                <div class="participant">
                  <span style="margin-right: 5px">People: </span>
                  <avatar
                    class="person"
                    username="mzy"
                    :size="16"
                    :rounded="true"
                    title="xxx"
                  />
                </div>
              </div>
              <Divider />
            </TimelineItem>
            <TimelineItem>
              <p class="time">2021-1-1</p>
              <div class="content">
                <span class="behavior">communication</span>
                <div>
                  <img
                    :src="otherUrl"
                    height="36px"
                    width="36px"
                    title="Data"
                    class="resources"
                  />
                </div>
                <div class="participant">
                  <span style="margin-right: 5px">People: </span>
                  <avatar
                    class="person"
                    username="1XXX"
                    :size="16"
                    :rounded="true"
                    title="xxx"
                  />
                  <avatar
                    class="person"
                    username="1XXX"
                    :size="16"
                    :rounded="true"
                    title="xxx"
                  />
                </div>
              </div>
              <Divider />
            </TimelineItem>
            <TimelineItem>
              <p class="time">2021-1-1</p>
              <div class="content">
                <span class="behavior"
                  >xxxx xxx xxxxxxxx xxxxxxx xxxxxxx xxx xxx xxxxx xxxxx</span
                >
                <div>
                  <img
                    :src="toolUrl"
                    height="36px"
                    width="36px"
                    title="Tool"
                    class="resources"
                  />
                  <Divider
                    type="vertical"
                    style="height: 36px; margin-top: -30px"
                  />
                  <img
                    :src="dataUrl"
                    height="36px"
                    width="36px"
                    title="Result"
                    class="resources"
                  />
                </div>
                <div class="participant">
                  <span style="margin-right: 5px">People: </span>
                  <avatar
                    class="person"
                    username="1XXX"
                    :size="16"
                    :rounded="true"
                    title="xxx"
                  />
                </div>
              </div>
              <Divider />
            </TimelineItem> -->
          </vue-scroll>
        </Timeline>
        <div
          style="
            cursor: pointer;
            text-align: center;
            background-color: #f9fdf1;
            padding: 10px 0;
          "
          @click="unfold = false"
        >
          Hide <Icon type="ios-arrow-up" size="20" style="margin-top: -2px" />
        </div>
      </div>
    </div>
    <Modal
      v-model="checkDataModal"
      title="Data information"
      width="600"
      footer-hide
    >
      <div style>
        <div class="dataInfo">
          <Label class="dataLabel">Name:</Label>
          <span class="dataText">{{
            selectData.name + selectData.suffix
          }}</span>
        </div>
        <div class="dataInfo">
          <Label class="dataLabel">Type:</Label>
          <span class="dataContent">{{ selectData.type }}</span>
          <Label class="dataLabel">Provider:</Label>
          <span class="dataContent">{{ selectData.uploaderName }}</span>
        </div>
        <div class="dataInfo">
          <Label class="dataLabel">File size:</Label>
          <span class="dataContent">{{
            selectData.fileSize | filterSizeType
          }}</span>
          <Label class="dataLabel">Created time:</Label>
          <span class="dataContent">{{
            dateFormat(selectData.uploadTime)
          }}</span>
        </div>
        <div class="dataInfo">
          <Label class="dataLabel">Description:</Label>
          <span class="dataText">{{ selectData.description }}</span>
        </div>
      </div>
      <br />
      <div>
        <a
          :href="resProxy + '/data/' + resId"
          :download="selectData.name"
          target="_self"
        >
          <Button
            type="info"
            size="small"
            title="Download"
            icon="md-download"
            style="margin: 10px 20px 0 0; cursor: pointer; width: 60px"
          ></Button>
        </a>
        <Button
          type="success"
          size="small"
          title="Preview"
          icon="md-eye"
          style="margin: 10px 20px 0 0; cursor: pointer; width: 60px"
          @click="preview(selectData)"
        ></Button>
      </div>
    </Modal>
    <Modal
      v-model="checkToolModal"
      title="Tool information"
      width="600"
      footer-hide
    >
      <div style>
        <div class="dataInfo">
          <Label class="dataLabel">Name:</Label>
          <span class="dataText">{{
            selectTool.toolName
          }}</span>
        </div>
        <div class="dataInfo">
          <Label class="dataLabel">Type:</Label>
          <span class="dataContent">{{ selectTool.backendType }}</span>
          <Label class="dataLabel">Created time:</Label>
          <span class="dataContent">{{
            shortTimeFormat(selectTool.createdTime)
          }}</span>
        </div>
        <div class="dataInfo">
          <Label class="dataLabel">Toolset:</Label>
          <span class="dataContent">{{
            selectTool.toolSet
          }}</span>
          <Label class="dataLabel">Tool tags:</Label>
          <span class="dataContent">{{
            showTags
          }}</span>
        </div>
        <div class="dataInfo">
          <Label class="dataLabel">Description:</Label>
          <span class="dataText">{{ selectTool.description }}</span>
        </div>
      </div>
      <br />
      <div>
        <Button
          type="success"
          size="small"
          title="Preview"
          icon="md-eye"
          style="margin: 10px 20px 0 0; cursor: pointer; width: 60px"
          @click="openTool(selectTool)"
        ></Button>
      </div>
    </Modal>
  </div>
</template>
<script>
import Avatar from "vue-avatar";
export default {
  components: {
    Avatar,
  },
  props: ["taskId", "name","activityInfo"],
  data() {
    return {
      scrollOps: {
        bar: {
          background: "lightgrey",
        },
      },
      unfold: false,
      operationRecords: [],
      operations: [],
      temOperation: {},
      // 资源avatar 图片路径
      dataUrl: require("@/assets/images/data.png"),
      modelUrl: require("@/assets/images/model.png"),
      paperUrl: require("@/assets/images/paper.png"),
      documentUrl: require("@/assets/images/document.png"),
      imageUrl: require("@/assets/images/image.png"),
      videoUrl: require("@/assets/images/video.png"),
      otherUrl: require("@/assets/images/otherfile.png"),
      // 工具avatar 图片路径
      toolUrl: require("@/assets/images/toolbox.png"),
      chatUrl: require("@/assets/images/chat.png"),
      checkDataModal: false,
      checkToolModal: false,
      selectData: {},
      selectTool: {},
      resProxy: "https://geomodeling.njnu.edu.cn/dataTransferServer",
      panel: null,
    };
  },
  mounted() {},
  computed: {
    resId: function () {
      let address = this.selectData.address;
      if (address != "" && typeof(address) == "string"){
        if (address.length == 34){
          return address;
        }else {
          let uidArr = address.split("data/");
          if (uidArr instanceof Array && uidArr.length > 1){
            return uidArr[1];
          }
        }
      }
    },
    showTags: function () {
        let tagsTemp = "";
        if (this.selectTool.tags != null && this.selectTool.tags.length > 0 && this.selectTool.tags[0].text == undefined) {
          for (let i = 0; i < this.selectTool.tags.length; i++) {
            tagsTemp += this.selectTool.tags[i] + "  ";
          }
        }else if(this.selectTool.tags != null && this.selectTool.tags.length > 0 && this.selectTool.tags[0].text != undefined){
          for (let i = 0; i < this.selectTool.tags.length; i++) {
            tagsTemp += this.selectTool.tags[i].text + "  ";
          }
        }
        return tagsTemp;
      },
  },
  methods: {
    operationShow() {
      this.unfold = true;
      this.operationRecords = [];
      let taskInfo = this.operationApi.getTaskInfo(this.taskId);
      let operations = taskInfo.operations;

      for (var i = 0; i < operations.length; i++) {
        let operation = this.operationApi.getOperationInfo(operations[i]);
        if (operation != undefined) {
          if (operation.type === "resource") {
            // resource
            let resInfo = this.operationApi.getResInfo(operation.resRef);
            operation.resource = resInfo;
            let operatorInfo = this.operationApi.getMemberInfo(
              operation.operator
            );
            operation.operator = operatorInfo;
          } else if (operation.type === "tool") {
            // tool
            let toolInfo = this.operationApi.getToolInfo(operation.toolRef);
            operation.tool = toolInfo;
            let operatorInfo = this.operationApi.getMemberInfo(
              operation.operator
            );
            operation.operator = operatorInfo;
          } else if (operation.type === "communication") {
            // communication
            let toolInfo = this.operationApi.getToolInfo(operation.toolRef);
            operation.tool = toolInfo;
            let resInfo = this.operationApi.getResInfo(operation.resRef);
            operation.resource = resInfo;
            // participants
            let participants = [];
            for (var j = 0; j < operation.personRef.length; j++) {
              let personInfo = this.operationApi.getMemberInfo(
                operation.personRef[j]
              );
              participants.push(personInfo);
            }
            operation.participants = participants;
          } else if (operation.type === "geo-analysis") {
            // geo-analysis
            let toolInfo = this.operationApi.getToolInfo(operation.toolRef);
            operation.tool = toolInfo;
            // participants
            let participants = [];
            for (var j = 0; j < operation.personRef.length; j++) {
              let operatorInfo = this.operationApi.getMemberInfo(
                operation.personRef[j]
              );
              participants.push(operatorInfo);
            }
            operation.participants = participants;
            // results
            let results = [];
            for (var j = 0; j < operation.resesRef.outputs.length; j++) {
              let resInfo = this.operationApi.getResInfo(
                operation.resesRef.outputs[j]
              );
              results.push(resInfo);
            }
            operation.outputs = results;
          }
          this.operationRecords.push(operation);
        }
      }
    },
    checkRes(item) {
      this.axios
          .get("/GeoProblemSolving/rip/file/" + this.activityInfo.aid + "/uid/" + item.id)
          .then((res) => {
            if (res.data == "Offline") {
              this.$store.commit("userLogout");
              // this.$router.push({ name: "Login" });
              this.tempLoginModal = true;
            } else if (res.data.code == 0) {
              let list = res.data.data;
              this.$set(this, "selectData", list[0]);
            }
          })
          .catch((err) => {
            throw err;
          });
      this.checkDataModal = true;
    },
    checkTool(item) {
      console.log(item);
      this.axios
          .get("/GeoProblemSolving/tool/" + item.id )
          .then((res) => {
            if (res.data == "Offline") {
              this.$store.commit("userLogout");
              // this.$router.push({ name: "Login" });
              this.tempLoginModal = true;
            } else if (res.data.code == 0) {
              let list = res.data.data;
              this.$set(this, "selectTool", list);
              console.log(this.selectTool);
              // this.openTool(toolInfo);
            }
          })
          .catch((err) => {
            throw err;
          });
        this.checkToolModal = true;
    },
    checkGeoAnalysis(toolInfo,resInfo){
      console.log(toolInfo);
      console.log(resInfo);
    },
    // openTool(toolInfo) {
    //   // this.openToolModal = false;
    //   this.openToolByPanel(toolInfo);
    //   if (toolInfo.scope == "outer") {
    //     this.openToolNewpage(toolInfo);
    //   } else if (toolInfo.scope == "inner") {
    //     this.openToolByPanel(toolInfo);
    //   } else {
    //     this.openToolByPanel(toolInfo);
    //   }
    // },
    // openToolByPanel(toolInfo) {
    //   //判断tool的类型，三种 modelItem（模型容器提供计算能力）, dataMethod(数据容器提供计算能力), webTool(自行开发，如mapTool)
    //   let routerUrl = toolInfo.toolUrl;
    //   if (toolInfo.backendType == "webTool") {
    //     routerUrl = toolInfo.toolUrl;
    //   } else if (toolInfo.backendType == "modelItem") {
    //     routerUrl = "/GeoProblemSolving/computeModel";
    //   } else if (toolInfo.backendType == "dataMethod") {
    //     routerUrl = "/GeoProblemSolving/dataMethod";
    //   }

    //   var toolContent = `<iframe src="${routerUrl}" id="${toolInfo.tid}" style="width: 100%; height:100%;" frameborder="0"></iframe>`;

    //   this.panel = jsPanel.create({
    //     theme: "success",
    //     footerToolbar: `<p></p>`,
    //     contentSize: "800 400",
    //     id: toolInfo.toolName.replace(/ /g, '-'),
    //     headerTitle: toolInfo.toolName,
    //     content: toolContent,
    //     container: "div#drawflow",
    //     dragit: {
    //       containment: 5,
    //     },
    //     closeOnEscape: true,
    //     disableOnMaximized: true,
    //   });
    //   $(".jsPanel-content").css("font-size", "0");
    //   // this.panelList.push(panel);//
    //   // this.$emit("toolPanel", panel);//

    //   // // 设置iframe 父子页面消息传输处理
    //   // window.addEventListener("message", this.toolMsgHandle, false);
    //   // let activity = this.activityInfo;
    //   // let userInfo = this.userInfo;
    //   // let taskList = this.operationApi.getTaskList();
    //   // let iFrame = document.getElementById(toolInfo.tid);
    //   // //iframe加载完毕后再发送消息，否则子页面接收不到message
    //   // iFrame.onload = function () {
    //   //   //iframe加载完立即发送一条消息
    //   //   iFrame.contentWindow.postMessage(
    //   //     {
    //   //       user: userInfo,
    //   //       activity: activity,
    //   //       tasks: taskList,
    //   //       tid: toolInfo.tid,
    //   //       type: "activity",
    //   //     },
    //   //     "*"
    //   //   );
    //   // };
    // },
    dateFormat(date) {
      let time = new Date(date);
      return time.Format("yyyy-MM-dd HH:mm:ss");
    },
    shortTimeFormat(date) {
      let time = new Date(date);
      return time.Format("yyyy-MM-dd");
    },
    preview(data){
      this.checkDataModal = false;
      var res = data;
      let name = data.suffix;
      if (/(doc|docx|xls|xlsx|ppt|pptx|xml|json|md|gif|jpg|png|jpeg|pdf)$/.test(name.toLowerCase())) {
        let url = "";
        if(res.address.indexOf("http://221.226.60.2:8082") != -1){
          url = this.resProxy + res.address.split("http://221.226.60.2:8082")[1];
        } else if(res.address.indexOf("/PExploration/resource") != -1){
          url = "https://geomodeling.njnu.edu.cn" + res.address;
        } else {
          url = this.resProxy + res.address;
        }
        let finalUrl = "https://view.xdocin.com/xdoc?_xdoc=" + url;
        var toolURL =
          "<iframe src=" +
          finalUrl +
          ' style="width: 100%;height:100%" frameborder="0"></iframe>';
        this.panel = jsPanel.create({
          headerControls: {
            smallify: "remove"
          },
          theme: "primary",
          footerToolbar: '<p style="height:5px"></p>',
          headerTitle: "Preview",
          contentSize: "800 600",
          content: toolURL,
          disableOnMaximized: true,
          dragit: {
            containment: 5
          },
          closeOnEscape: true,
        });
        $(".jsPanel-content").css("font-size", "0");
        
      } else if (/(mp4)$/.test(name.toLowerCase())) {
        if (this.panel != null) {
          this.panel.close();
        }
        var url = res.address;
        var toolURL =
          "<video src=" +
          url +
          ' style="width: 100%;height:100%" controls></video>';
        this.panel = jsPanel.create({
          headerControls: {
            smallify: "remove"
          },
          theme: "primary",
          footerToolbar: '<p style="height:10px"></p>',
          headerTitle: "Preview",
          contentSize: "800 600",
          content: toolURL,
          disableOnMaximized: true,
          dragit: {
            containment: 5
          },
          closeOnEscape: true
        });
        $(".jsPanel-content").css("font-size", "0");
      }  else {
        this.$Notice.error({
          title: "Open failed",
          desc: "Not supported file format."
        });
        return false;
      }
    },
  },
  filters: {
    filterSizeType(value) {
      if (value === 0) return "0 B";
      let k = 1024;
      let sizes = ["B", "KB", "MB", "GB"];
      let i = Math.floor(Math.log(value) / Math.log(k));
      return (value / Math.pow(k, i)).toPrecision(3) + " " + sizes[i];
    },
  },
};
</script>
<style scoped>
.time {
  font-size: 14px;
  font-weight: bold;
}
.content {
  width: 200px;
  word-break: break-word;
}
.tool {
  cursor: pointer;
}
.resources {
  cursor: pointer;
}
.participant {
  display: flex;
  position: relative;
}
.participant .person {
  margin-right: 5px;
  cursor: pointer;
}
.ivu-divider-horizontal {
  margin: 10px 0 0 0;
}
.drawflow-node-title {
  height: 50px;
  line-height: 50px;
  background: #f7f7f7;
  border-bottom: 1px solid #e9e9e9;
  border-radius: 4px 4px 0px 0px;
  padding-left: 15px;
}
.drawflow-node-body {
  font-size: 14px;
  color: #555555;
}
.dataInfo {
  margin: 5px 0;
}
.dataLabel {
  width: 90px;
  display: inline-block;
  font-size: 13px;
  font-weight: bold;
  vertical-align: top;
  color: dodgerblue;
}
.dataContent {
  width: 180px;
  display: inline-block;
  padding-left: 10px;
}
.dataText {
  padding-left: 10px;
  display: inline-block;
  word-break: break-word;
  width: 470px;
}
</style>