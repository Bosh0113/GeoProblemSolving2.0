<style scoped>
.picscreen {
  position: relative;
  padding-top: 10px;
  /* transform: translateY(60px); */
}

.picbg {
  background-image: url("../../assets/images/condefbg.png");
  background-size: cover;
  position: absolute;
  height: 100px;
  width: 100%;
  background-color: #8b8b8b;
  top: 0;
  left: 0;
}

.tools {
  float: left;
  height: 100%;
  width: 27%;
}

.condef {
  float: left;
  height: 100%;
  width: 72%;
  margin-left: 10px;
}

/* content */
.condefContent {
  margin: 0 15%;
  height: auto;
}

.home_content >>> .ivu-breadcrumb {
  color: rgb(180, 197, 207);
  /* min-height: 500px; */
}
.home_content >>> .ivu-breadcrumb > span:last-child {
  color: rgb(180, 197, 207);
}

.tool-panel {
  display: flex;
  height: auto;
  flex-wrap: wrap;

  /* justify-content: center; */
}

/* 工具库中抽屉的工具样式*/
.singl_tool_style {
  margin: 10px;
  width: 100%;
  cursor: pointer;
  clear: both;
  display: flex;
  justify-content: center;
}

.singl_tool_style:hover {
  transition: all 1s;
  background-color: lightgray;
}

.resourceCard {
  clear: both;
  width: 100%;
}

#subDescription {
  width: 100%;
  display: inline-block;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
  text-align: center;
  font-size: 1rem;
  height: 20px;
  color: white;
}

.resourceMenu {
  width: 35%;
  position: absolute;
  top: 50px;
  left: 10px;
  right: 50px;
}

.resourceTable {
  width: 60%;
  position: absolute;
  top: 50px;
  left: 300px;
  right: 50px;
}
/* .pro-tab >>> ivu-btn ivu-btn-default ivu-btn-icon-only{
      width:100px;
      height:100px
  } */
.pro-tab >>> .ivu-btn-icon-only {
  font-size: 40px;
}
.modelToolBtn {
  margin-left: 2%;
  margin-top: 1%;
  float: left;
}
.pro-tab >>> .ivu-tabs {
  min-height: 300px;
}

.stepName {
  text-align: center;
  font-size: 1.2rem;
  height: 20px;
  color: white;
}
.stepName p {
  margin: 0 auto;
  font-size: 1rem;
  height: 20px;
  color: white;
  word-break: break-word;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  max-width: 400px;
}

.onlineListBtn {
  position: absolute;
  top: 70%;
  right: 10%;
  /* width: 100px */
}

.pro-tab >>> .ivu-modal-footer {
  border-top: none;
  padding-bottom: 12px;
  padding-right: 18px;
}
.pro-tab >>> .ivu-modal-body {
  padding-bottom: 0;
}
.subproject-back >>> .ivu-breadcrumb-item-link {
  color: white;
}

.breadCrumb {
  margin-left: 1%;
}
</style>
<template>
  <div style="background-color:#e8eaec;height:auto">
    <div class="picscreen">
      <div class="picbg"></div>

      <div class="home_content">
        <Row>
          <!-- 需要修改样式 -->
          <div class="breadCrumb">
            <Breadcrumb>
              <!-- <BreadcrumbItem :to="toProjectPage">Project</BreadcrumbItem> -->
              <BreadcrumbItem :to="toSubProjectPage" class="subproject-back">Subproject</BreadcrumbItem>
              <BreadcrumbItem style="color:white">Modeling for geographic process</BreadcrumbItem>
            </Breadcrumb>
          </div>

          <div class="stepName">
            <strong>{{stepContent.name}}</strong>
            <p :title="stepContent.description">{{stepContent.description}}</p>
          </div>

          <div class="onlineListBtn">
            <Button @click="drawerValue = true" type="default" ghost>Participants</Button>
            <template v-if="userRole == 'Manager'">
              <Button
                @click="modifyStep = true"
                style="margin-left:20px"
                ghost
              >Modify Step</Button>
            </template>
          </div>
        </Row>
        <Drawer title="Participants" :closable="false" v-model="drawerValue">
          <online-participant :sub-project-id="subprojectId" :room-id="stepId"></online-participant>
          <div class="toChatroom" style="position:absolute; left:25%;bottom:5%">
            <Button @click.native="toolPanel('chat')" type="success">Go to Chatroom</Button>
          </div>
        </Drawer>

        <Modal v-model="modifyStep">
          <p slot="header" style="text-align:center">
            <Icon type="ios-information-circle"></Icon>
            <span>Modify step name and description</span>
          </p>
          <Form :label-width="120" label-position="left" :model="stepForm">
            <FormItem label="Step Name" prop="name">
              <Input
                v-model="stepForm.name"
                type="textarea"
                :autosize="{minRows: 1,maxRows: 3}"
                clearable
              />
            </FormItem>
            <FormItem label="Step Description" prop="description">
              <Input
                v-model="stepForm.description"
                type="textarea"
                :autosize="{minRows: 1,maxRows: 3}"
                clearable
              />
            </FormItem>
          </Form>
          <div slot="footer">
            <Button @click="cancelModifyStep">Cancel</Button>
            <Button type="primary" @click="submitModifyStep">Modify</Button>
          </div>
        </Modal>
      </div>
    </div>

    <!-- tab  on-click事件 -->

    <div class="pro-tab" :style="{height:sidebarHeight+ 79+'px'}">
      <div>
        <template>
          <Row style="margin-top:40px">
            <div :style="{height:sidebarHeight+45+'px'}" style="margin:20px 1%">
              <Col span="6">
                <Card :style="{height:sidebarHeight+45+'px'}">
                  <div class="condefTitle">
                    <div
                      style="width:3px;height:18px;float:left;background-color:rgb(124, 126, 126)"
                    ></div>
                      <data-list :stepInfo="stepContent" :contentHeight="contentHeight" :userRole="userRole" :key="dataListIndex"></data-list>
                  </div>
                </Card>
              </Col>
              <Col span="18">
                <Card :style="{height:sidebarHeight+45+'px'}" style="margin-left:15px">
                  <div class="condefTitle">
                    <Tabs value="single">
                      <TabPane name="single" icon="ios-brush" label="Single Modeling">
                        <Tooltip placement="bottom-start" class="modelToolBtn">
                          <Button icon="md-bonfire" @click.native="toolPanel('ConceptualModel')"></Button>
                          <div slot="content">
                            <p>Conceptual modeling tool</p>
                          </div>
                        </Tooltip>

                        <Tooltip placement="bottom-start" class="modelToolBtn">
                          <Button icon="ios-brush" to="//134.175.111.77/note" target="_blank"></Button>
                          <div slot="content">
                            <p>Jupyter Tool</p>
                          </div>
                        </Tooltip>
                      </TabPane>
                      <TabPane name="integrated" icon="ios-paper" label="Integrated Modeling">
                        <Tooltip placement="bottom-start" class="modelToolBtn">
                          <Button icon="md-bonfire" @click.native="toolPanel('ConceptualModel')"></Button>
                          <div slot="content">
                            <p>Conceptual Modeling Tool</p>
                          </div>
                        </Tooltip>
                        <Tooltip placement="bottom-start" class="modelToolBtn">
                          <Button icon="md-git-commit" @click.native="toolPanel('LogicalModel')"></Button>
                          <div slot="content">
                            <p>Logical Modeling Tool</p>
                          </div>
                        </Tooltip>
                        <Tooltip placement="bottom-start" class="modelToolBtn">
                          <Button icon="md-pulse" @click.native="toolPanel('ComputationalModel')"></Button>
                          <div slot="content">
                            <p>Computational Modeling Tool</p>
                          </div>
                        </Tooltip>
                      </TabPane>
                    </Tabs>
                  </div>
                </Card>
              </Col>
            </div>
          </Row>
        </template>
      </div>
    </div>
  </div>
</template>

<script>
import { VueFlowy, FlowChart } from "vue-flowy";
import * as socketApi from "./../../api/socket";
import Avatar from "vue-avatar";
import echarts from "echarts";
import folderTree from "../resources/folderTree";
import onlineParticipant from "./utils/onlineParticipants";
import dataList from "./utils/dataList";

export default {
  components: {
    VueFlowy,
    Avatar,
    dataList,
    folderTree,
    onlineParticipant
  },
  data() {
    return {
      userInfo: "",
      contextDefinitionId: this.$route.params.id, //上一步的ID 和这一步的id？？
      fileList: [],
      dataList: [],
      contentHeight: 800,
      sidebarHeight: 800,
      stepId: this.$route.params.id,
      toSubProjectPage: "",
      dataListIndex:0,
      // submit
      modelProcessForm: {
        // others:"",
        methods: "",
        description: "",
        others: "",
        outcome: ""
      },

      dataProcessingValidate: {
        description: [
          {
            required: true,
            message: "The description of data cannot be empty",
            trigger: "blur"
          }
        ],
        others: [
          {
            required: false,
            message: "The income data should be described",
            trigger: "blur"
          }
        ],
        outcome: [
          {
            required: true,
            message: "The outcome data should be described",
            trigger: "blur"
          }
        ],
        methods: [
          {
            required: true,
            message: "The method of data processing is need",
            trigger: "blur"
          }
        ]
      },
      timer: null,
      jupyterLink: {
        path: "http://134.175.111.77/note"
      },

      subprojectId: sessionStorage.getItem("subProjectId"),
      stepContent: [],
      drawerValue: false,

      modifyStep: false,
      stepForm: {
        name: "",
        description: ""
      },
      // 用户角色
      userRole: "Visitor"
    };
  },

  created() {
    this.init();
  },
  mounted() {
    window.addEventListener("resize", this.initSize);
    //   this.getModelProcess();
    // this.init();
  },
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      if (!vm.$store.getters.userState) {
        next("/login");
      } else {
        if (
          !(
            vm.userRole == "Manager" ||
            vm.userRole == "Member" ||
            vm.userRole == "PManager"
          )
        ) {
          vm.$Message.error("You have no property to access it");
          // next(`/project/${vm.$store.getters.currentProjectId}`);
          vm.$router.go(-1);
        } else {
          next();
        }
      }
    });
  },
  methods: {
    initSize() {
      if (window.innerHeight > 675) {
        this.sidebarHeight = window.innerHeight - 290;
        this.contentHeight = window.innerHeight - 298;
      } else {
        this.sidebarHeight = 675 - 290;
        this.contentHeight = 675 - 298;
      }
    },

    init() {
      this.initSize();
      this.userRoleIdentity();
      this.getModelProcess();
    },

    getResources() {
      this.userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
      let list = [];
      $.ajax({
        url:
          "/GeoProblemSolving/folder/inquiry" +
          "?folderId=" +
          this.contextDefinitionId,
        type: "GET",
        async: false,
        success: data => {
          if (data !== "None") {
            this.$set(this, "fileList", data.files);
          } else {
            this.fileList = [];
          }
          //    this.$set(this, "stepResourceList", list);
        }
      });
      this.filterData();
    },

    filterData() {
      let filterdata = this.fileList.filter(item => {
        return item.type === "Data";
      });
      this.$set(this, "dataList", filterdata);
    },

    submit(dataform) {
      // this.subprojectId = this.$route.params.id;
      let creatorId = this.$store.getters.userId;
      this.$refs[dataform].validate(valid => {
        // 提交表单
        if (valid) {
          let modelProcess = new URLSearchParams();
          // modelProcess.append("subProjectId", this.subprojectId);
          modelProcess.append("creator", creatorId);
          modelProcess.append("type", "modeling for geographic process");

          modelProcess.append(
            "content.description",
            this.modelProcessForm.description
          );
          modelProcess.append("content.methods", this.modelProcessForm.methods);
          modelProcess.append("content.outcome", this.modelProcessForm.outcome);
          modelProcess.append("content.others", this.modelProcessForm.others);
          // modelProcess.append("others", this.modelProcessForm.others);
          modelProcess.append("stepId", this.stepId);

          this.axios
            .post("/GeoProblemSolving/step/update", modelProcess)
            .then(res => {
              if (res.data == "Offline") {
                this.$store.commit("userLogout");
                this.$router.push({
                  name: "Login"
                });
              } else if (res.data != "Fail") {
                this.$Notice.info({
                  desc: "Update successfully!"
                });
              } else {
                this.$Message.error("Update subproject failed.");
              }
            });
        }
      });
    },

    userRoleIdentity() {
      this.userRole = "Visitor";
      let creatorId = JSON.parse(sessionStorage.getItem("subProjectInfo")).managerId;
      if (this.$store.getters.userState) {
        // 是否是子项目管理员
        if (creatorId === this.$store.getters.userId) {
          this.userRole = "Manager";
        } else {
          this.userRole = "Visitor";
        }
      }
    },

    getModelProcess() {
      this.axios
        .get(
          "/GeoProblemSolving/step/inquiry/" +
            "?key=stepId" +
            "&value=" +
            this.stepId
        )
        .then(res => {
          //new id
          //ContextDefinition["stepId"] = res.data;
          if (res.data != "Fail") {
            this.modelProcessForm = res.data[0].content;
            this.stepContent = res.data[0];
            this.dataListIndex++;
            this.stepForm = res.data[0];
            this.toSubProjectPage = "/project/" + res.data[0].subProjectId + "/subproject";
          } else {
            this.$Notice.info({
              desc: "Get the description failed!"
            });
          }
        });
    },

    submitModifyStep() {
      let obj = new URLSearchParams();
      obj.append("name", this.stepForm.name);
      obj.append("description", this.stepForm.description);
      obj.append("stepId", this.stepId);

      this.axios
        .post("/GeoProblemSolving/step/update", obj)
        .then(res => {
          console.log(res.data);
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({
              name: "Login"
            });
          } else if (res.data != "Fail") {
            this.$Notice.info({
              desc: "Update successfully!"
            });
            this.stepContent.name = this.stepForm.name;
            this.stepContent.description = this.stepForm.description;
          } else {
            this.$Message.error("Update step failed.");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
      this.modifyStep = false;
    },

    cancelModifyStep() {
      this.modifyStep = false;
    },

    toolPanel(type) {
      // if (this.userRole != "Visitor") {
      this.axios
        .post("/GeoProblemSolving/user/state")
        .then(res => {
          if (!res.data) {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else {
            var toolURL = "";
            let toolName = "";
            if (type == "LogicalModel") {
              toolURL =
                '<iframe src="/GeoProblemSolving/Collaborative/LogicalModel/index.html' +
                "?groupID=" +
                this.stepId +
                '" style="width: 100%;height:100%"></iframe>';
              toolName = "Logical modeling";
            } else if (type == "ConceptualModel") {
              toolURL =
                '<iframe src="/GeoProblemSolving/Collaborative/ConceptualModel/index.html' +
                "?groupID=" +
                this.stepId +
                '" style="width: 100%;height:100%"></iframe>';
              toolName = "Conceptual modeling";
            } else if (type == "ComputationalModel") {
              toolURL =
                '<iframe src="/GeoProblemSolving/Collaborative/ComputationalModel/index.html' +
                "?groupID=" +
                this.stepId +
                '" style="width: 100%;height:100%"></iframe>';
              toolName = "Computational modeling";
            } else if (type == "chat") {
              toolURL =
                '<iframe src="' +
                "http://" +
                this.$store.state.IP_Port +
                '/GeoProblemSolving/chat" style="width: 100%;height:100%"></iframe>';
              toolName = "Chatroom";
            }

            let panel = jsPanel.create({
              theme: "success",
              headerTitle: toolName,
              footerToolbar: '<p style="height:10px"></p>',
              contentSize: "1200 600",
              content: toolURL,
              disableOnMaximized: true,
              dragit: {
                containment: 5
              },
              callback: function() {
                // this.content.style.padding = "20px";
              }
            });
            panel.resizeit("disable");
            $(".jsPanel-content").css("font-size", "0");
            this.panelList.push(panel);
            // 生成records, 同步
            // let record = {
            //   who: this.$store.getters.userName,
            //   whoid: this.$store.getters.userId,
            //   type: "tools",
            //   toolType: type,
            //   content: "used a tool: " + type,
            //   stepId: this.stepId,
            //   time: new Date().toLocaleString()
            // };
            // this.subprojectSocket.send(JSON.stringify(record));
          }
        })
        .catch(err => {
          console.log("Get user info fail.");
        });
      // }
      // else {
      //   this.$Notice.info({
      //   desc: "Please join this project first!"
      // });
      // }
    },
    closePanel() {
      for (let i = 0; i < this.panelList.length; i++) {
        this.panelList[i].close();
      }
    },
  }
};
</script>