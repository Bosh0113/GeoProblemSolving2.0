<style scoped>
.picscreen {
  position: relative;
  padding-top: 10px;
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

.fileList {
  float: left;
  height: 100%;
  width: 19%;
}

.fileUdx {
  float: left;
  height: 100%;
  width: 35%;
  margin-left: 10px;
}

.condef {
  float: left;
  height: 100%;
  width: 44%;
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
  word-break:break-word;
  overflow: hidden;
  white-space: nowrap; 
  text-overflow: ellipsis; 
  max-width: 400px;
}

.onlineListBtn {
  position: absolute;
  top: 70%;
  right: 15%;
  /* width: 100px */
}

/* .pro-tab>>>.ivu-modal-footer {
    border-top:none;
    padding-bottom:12px;
    padding-right:18px ;
  }
  .pro-tab>>>.ivu-modal-body {
    padding-bottom: 0;
  } */

.pro-tab >>> .ivu-btn-icon-only {
  font-size: 40px;
}
.modelToolBtn {
  margin-left: 2%;
  margin-top: 3%;
  /* clear: both; */
}
.subproject-back >>>.ivu-breadcrumb-item-link{
  color:white;
}

.breadCrumb{
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
                <BreadcrumbItem :to="toSubProjectPage"  class="subproject-back">Subproject</BreadcrumbItem>
                <BreadcrumbItem style="color: white">Data processing</BreadcrumbItem>
              </Breadcrumb>
            </div>

            <div class="stepName">
              <strong>{{stepContent.name}}</strong>
              <p :title="stepContent.description">{{stepContent.description}}</p>
            </div>

            <div class="onlineListBtn">
              <Button @click="drawerValue = true" type="default" ghost>Participants</Button>
               <template  v-if="userRole == 'Manager'">
                  <Button                
                    @click="modifyStep = true"
                    style="margin-left:20px"
                    type="info"
                    ghost
                  >Modify Step</Button>
               </template>
            </div>
          </Row>
          <Drawer title="Participants" :closable="false" v-model="drawerValue">
            <online-participant :sub-project-id="subprojectId" :room-id="stepId"></online-participant>
            <div class="toChatroom" style="position:absolute; left:25%;bottom:5%">
              <Button  @click.native="toolPanel('chat')" type="success">Go to Chatroom</Button>
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
    
      <div class="pro-tab" :style="{height:sidebarHeight+ 59+'px'}" style="margin-top:60px">
       
                <template style="margin:20px 1%">
                  <Row>
                    <Col span="7" >
                  <!-- <div class="fileList"> -->
                    <Card :style="{height:sidebarHeight+45+'px'}">
                      <div class="condefTitle">
                        <div
                          style="width:3px;height:18px;float:left;background-color:rgb(124, 126, 126)"
                        ></div>
                        <h4 style="float:left;margin-left:5px">Data</h4>
                      </div>
                      <ul v-for="(item,index) in dataList" :key="index">
                        <li>{{item.name}}</li>
                      </ul>
                    </Card>
                  <!-- </div> -->
                  </Col>
                  <Col span="9">
                  <!-- <div class="fileUdx"> -->
                    <Card :style="{height:sidebarHeight+45+'px'}">
                      <!-- <div class="condefTitle">
                        <div
                          style="width:3px;height:18px;float:left;background-color:rgb(124, 126, 126)"
                        ></div>
                        <h4 style="float:left;margin-left:5px">Data Origin</h4>
                      </div> -->
                      <div style="clear:both">
                        <Tabs>
                          <TabPane label="Data Origin" name="origin" icon="md-home">
                            <div style="width:100%;position:absolute;right:5%">
                              <template  v-if="userRole == 'Manager'">
                                <Icon
                                  v-if="!edit1"
                                  type="ios-create"
                                  :size="25"
                                  style="float:right;cursor:pointer"
                                  title="Edit"
                                  @click="editDataOrginInput"
                                />
                                <Icon
                                  v-else
                                  type="md-checkbox-outline"
                                  :size="25"
                                  style="float:right;cursor:pointer"
                                  title="Complete"
                                  @click="editDataOrginInput"
                                />
                              </template>                           
                            </div>
                            
              
                            <div v-if="!edit1" class="subProjectDesc" style="word-break: break-all; word-wrap: break-word;width:85%">{{dataOrginInput}}</div>
                            <template v-else >
                              <Input
                                v-model="dataOrginInput"
                                type="textarea"
                                :rows="5"
                                :autosize="{minRows: 5,maxRows: 18}"
                                placeholder="Enter something..."
                                style="width:85%"
                              
                              />
                            </template>
                          </TabPane>
                          <TabPane label="UDX Schema" name="udx" icon="md-home">
                            <!-- <mark-down> </mark-down> -->
                            <pre class="brush: html"></pre>
                            <template>
                              <!-- <mark-down /> -->
                            </template>
                          </TabPane>
                        </Tabs>
                      </div>
                    </Card>
                  <!-- </div> -->
                  </Col>
                  <Col span="8">
                  <!-- <div class="condef"> -->
                    <Card :style="{height:sidebarHeight+45+'px'}">
                      <div class="condefTitle">
                        <div
                          style="width:3px;height:18px;float:left;background-color:rgb(124, 126, 126)"
                        ></div>
                        <h4 style="float:left;margin-left:5px">Toolbox</h4>
                      </div>
                      <div style="clear:both">
                        <Tooltip placement="bottom-start" class="modelToolBtn">
                          <Button icon="ios-brush" to="//134.175.111.77/note" target="_blank"></Button>
                          <div slot="content">
                            <p>Test Tool</p>
                          </div>
                        </Tooltip>
                      </div>
                    </Card>
                  <!-- </div> -->
                  </Col>
                  </Row>
                </template>
      </div>
   
  </div>
</template>

<script>
import { VueFlowy, FlowChart } from "vue-flowy";
import * as socketApi from "./../../api/socket";
import Avatar from "vue-avatar";
import echarts from "echarts";
import folderTree from "../resources/folderTree";
import onlineParticipant from "./onlineParticipants";

export default {
  components: {
    VueFlowy,
    Avatar,
    folderTree,
    onlineParticipant,
   
  },
  // props: ["subProjectInfo","userRole","projectInfo"],
  data() {
    return {
      userInfo: "",
      fileList: [],
      dataList: [],
      sidebarHeight: 800,
      stepId: this.$route.params.id,
      toSubProjectPage: "/project/" + this.$route.params.subid + "/subproject",
      stepContent: [],
      // online drawer
      subprojectId:sessionStorage.getItem("subProjectId"),
      drawerValue: false,
      modifyStep: false,
      stepForm: {
        name: "",
        description: ""
      },
      dataOrginInput:"",
      // 编辑data描述信息
      edit1: false,
      description: "",
       // 用户角色
      userRole: "Visitor"
    };
  },

  created() {
    this.init();
  },
  mounted() {
    window.addEventListener("resize", this.initSize);
    // this.getDataProcessing();
    // this.init();
  },

  methods: {
    initSize() {
      this.sidebarHeight = window.innerHeight - 290;
    },

    init() {
      this.initSize();
      this.getDataProcessing();
    },

    getResources() {
      this.userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
      let list = [];
      $.ajax({
        url: "/GeoProblemSolving/folder/inquiry" + "?folderId=" + this.stepId,
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
      this.filterData(this.fileList);
    },

    filterData(data) {
      let filterdata = data.filter(item => {
        return item.type === "Data";
      });
      this.$set(this, "dataList", filterdata);
    },

    userRoleIdentity() {
      this.userRole = "Visitor";
      // console.log(this.stepContent.creator);
      // console.log(this.$store.getters.userId);
      let creatorId = sessionStorage.getItem("subProjectManagerId");
      console.log(creatorId);
      if (this.$store.getters.userState) {
        // 是否是子项目管理员
        if (creatorId === this.$store.getters.userId) {
          this.userRole = "Manager";
          console.log(this.userRole);
        }
        else{
           this.userRole = "Visitor";
        }
      }
    },

    getDataProcessing() {
      this.axios
        .get(
          "/GeoProblemSolving/step/inquiry/" +
            "?key=stepId" +
            "&value=" +
            this.stepId
        )
        .then(res => {
          if (res.data != "Fail") {
            // this.dataForm = res.data[0].content;
            this.stepContent = res.data[0];
            this.stepForm = res.data[0];
            this.dataOrginInput = res.data[0].content.dataOrginInput;//绑定input
            this.userRoleIdentity();
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
        .get("/GeoProblemSolving/user/state")
        .then(res => {
          if (!res.data) {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else {
            var toolURL = "";
            let toolName = "";
            if (type == "chat") {
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
            let record = {
              who: this.$store.getters.userName,
              whoid: this.$store.getters.userId,
              type: "tools",
              toolType: type,
              content: "used a tool: " + type,
              moduleId: this.stepId,
              time: new Date().toLocaleString()
            };
            this.subprojectSocket.send(JSON.stringify(record));
          }
        })
        .catch(err => {
          console.log("Get user info fail.");
        });
  
    },

     editDataOrginInput() {
      if (this.edit1) {
        this.edit1 = false;

        let obj = new URLSearchParams();
        obj.append("stepId", this.stepId);
        obj.append("content.dataOrginInput", this.dataOrginInput);
        this.axios
          .post("/GeoProblemSolving/step/update", obj)
          .then(res => {
            if (res.data == "Offline") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else if (res.data != "Fail") {
              this.$Notice.info({
                desc: "Update successfully!"
              });
            } else {
              this.$Message.error("Update step failed.");
            }
          })
          .catch(err => {
            console.log(err.data);
          });
      } else {
        this.edit1 = true;
      }
    },


  }
};
</script>

  


