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

  .home_content >>>.ivu-breadcrumb {
   color:rgb(180, 197, 207);
    /* min-height: 500px; */
  }
   .home_content>>>.ivu-breadcrumb>span:last-child {
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

  .resourceMenu{
    width:35%;
    position:absolute;
    top:50px;
    left:10px;
    right:50px
  }

  .resourceTable{    
    width:60%;
    position:absolute;
    top:50px;
    left:300px;
    right:50px
  }
  /* .pro-tab >>> ivu-btn ivu-btn-default ivu-btn-icon-only{
      width:100px;
      height:100px
  } */
  .pro-tab >>> .ivu-btn-icon-only{
      font-size: 40px
  }
  .modelToolBtn{
      margin-left:2%;
      margin-top:1%;
      float: left;
  }
  .pro-tab >>> .ivu-tabs{
      min-height: 300px
  }


</style>
<template>
  <div style="background-color:#e8eaec;height:auto">
    <Row>
      <div class="picscreen">
        <div class="picbg"></div>

        <div class="home_content">
          <Row>
            <!-- 需要修改样式 -->
            <Col span="6" style="height:40px;">
            <Breadcrumb>
              <!-- <BreadcrumbItem :to="toProjectPage">Project</BreadcrumbItem> -->
                <BreadcrumbItem :to="toSubProjectPage">Subproject</BreadcrumbItem>
                <BreadcrumbItem>Modeling for Geographic Process</BreadcrumbItem>
            </Breadcrumb>
            </Col>
            
            <Col span="12" style="text-align:center;font-size:1.5rem;height:20px;color:white;margin-top:1%">
            <strong>Modeling for Geographic Process</strong>
            </Col>
          </Row>
         
        </div>
      </div>
    </Row>

    <!-- tab  on-click事件 -->
    <Row>
      <div class="pro-tab" :style="{height:sidebarHeight+ 80+'px'}">
        <div>
          <template>
            <Row style="margin-top:40px">
              <div :style="{height:sidebarHeight+45+'px'}" style="margin:20px 1%">
                <div class="tools">
                  <Card style=" height:100%;">
                    <Tabs value="Description">
                      <TabPane name="Description" icon="ios-brush" label="Description">
                        <Form ref="modelProcessForm" :rules="dataProcessingValidate" :model="modelProcessForm" :label-width="120" label-position="left" style="margin-top:15px" >
                          <FormItem label="Model description" prop="description">
                            <Input v-model="modelProcessForm.description" placeholder="Please enter the description of the model"
                              type="textarea" :autosize="{minRows: 1,maxRows: 5}" clearable />
                          </FormItem>
                          <FormItem label="Main methods" prop="methods">
                            <Input v-model="modelProcessForm.methods" placeholder="Please enter the main method"
                              type="textarea" :autosize="{minRows: 1,maxRows: 5}" clearable />
                          </FormItem>
                          <FormItem label="Outcome description" prop="outcome">
                            <Input v-model="modelProcessForm.outcome" placeholder="Please enter the outcome data description"
                              type="textarea" :autosize="{minRows: 1,maxRows: 5}" clearable />
                          </FormItem>
                          <FormItem label="Others" prop="others">
                            <Input v-model="modelProcessForm.others" placeholder="Please enter something to add" type="textarea"
                              :autosize="{minRows: 1,maxRows: 5}" clearable />
                          </FormItem>
                          <FormItem>
                            <Button @click="submit('modelProcessForm')">Submit</Button>
                          </FormItem>
                        </Form>
                      </TabPane>
                      <TabPane name="Data" icon="ios-paper" label="Data">
                        <ul v-for="(item,index) in dataList" :key="index">
                          <li>{{item.name}}</li>
                        </ul>
                      </TabPane>
                    </Tabs>
                  </Card>
                </div>
                <div class="condef">
                  <Card style="height:auto;min-height:100%">
                    <!-- <div class="condefTitle">
                      <div style="width:3px;height:18px;float:left;background-color:rgb(124, 126, 126)"></div>
                      <h4 style="float:left;margin-left:5px">Resources</h4>
                    </div> -->
                     <div class="condefTitle">
                       <!-- <Menu active-name="single"  @on-select="filterResourceType">
                          <MenuItem name="single">
                            <Icon type="ios-stats" />
                            Single Modeling
                          </MenuItem>
                          <MenuItem name="integrated">
                            <Icon type="md-image" />
                            Integrated Modeling
                          </MenuItem>                         
                        </Menu> -->
                       <Tabs value="single">
                         <TabPane name="single" icon="ios-brush" label="Single Modeling">
                           <Tooltip placement="bottom-start" class="modelToolBtn">
                             <Button icon="md-bonfire"  @click.native="toolPanel('ConceptualModel')"></Button>
                             <div slot="content">
                               <p>Conceptual modeling tool</p>
                             </div>
                           </Tooltip>

                            <Tooltip placement="bottom-start" class="modelToolBtn">
                             <Button icon="ios-brush"   to="//134.175.111.77/note"  target="_blank"></Button>
                             <div slot="content">
                               <p>Jupyter Tool</p>
                             </div>
                           </Tooltip>

                         </TabPane>
                         <TabPane name="integrated" icon="ios-paper" label="Integrated Modeling">
                            <Tooltip placement="bottom-start" class="modelToolBtn">
                             <Button icon="md-bonfire"  @click.native="toolPanel('ConceptualModel')"></Button>
                             <div slot="content">
                               <p>Conceptual Modeling Tool</p>
                             </div>
                           </Tooltip>
                            <Tooltip placement="bottom-start" class="modelToolBtn">
                             <Button icon="md-git-commit"   @click.native="toolPanel('LogicalModel')"></Button>
                             <div slot="content">
                               <p>Logical Modeling Tool</p>
                             </div>
                           </Tooltip>
                           <Tooltip placement="bottom-start" class="modelToolBtn">
                             <Button icon="md-pulse"   @click.native="toolPanel('ComputationalModel')"></Button>
                             <div slot="content">
                               <p>Computational Modeling Tool</p>
                             </div>
                           </Tooltip>
                         </TabPane>
                       </Tabs>
                     </div>                  
                  </Card>
                </div>
              </div>
            </Row>
          </template>
        </div>
      </div>
    </Row>
  </div>
</template>

<script>
  import {
    VueFlowy,
    FlowChart
  } from "vue-flowy";
  import * as socketApi from "./../../api/socket";
  import Avatar from "vue-avatar";
  import echarts from "echarts";
  import folderTree from "../resources/folderTree";

  export default {
    components: {
      VueFlowy,
      Avatar,
      folderTree
    },
    data() {
      return {
        userInfo:"",
        contextDefinitionId: this.$route.params.id,//上一步的ID 和这一步的id？？
        fileList:[],
        dataList:[],
        sidebarHeight: 800,
        stepId: this.$route.params.id,

        // submit
        modelProcessForm: {
            // others:"",
            methods: "",
            description: "",
            others: "",
            outcome: "",
            },

        dataProcessingValidate: {
          description: [{
            required: true,
            message: "The description of data cannot be empty",
            trigger: "blur"
          }],
          others: [{
            required: false,
            message: "The income data should be described",
            trigger: "blur"
          }],
          outcome: [{
            required: true,
            message: "The outcome data should be described",
            trigger: "blur"
          }],
          methods: [{
            required: true,
            message: "The method of data processing is need",
            trigger: "blur"
          }]          
        },

        // web socket for module
        subprojectSocket: null,
        timer: null,
        jupyterLink:{
            path:'http://134.175.111.77/note'
        }
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

    methods: {
      initSize() {
        this.sidebarHeight = window.innerHeight - 290;
      },

      init() {
        this.initSize();
        this.getModelProcess();
      },

      getResources() {
        this.userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
        let list = [];
        $.ajax({
          url: "/GeoProblemSolving/folder/inquiry" +
            "?folderId=" + this.contextDefinitionId,
          type: "GET",
          async: false,
          success: data => {
            if (data !== "None") {
              this.$set(this, "fileList", data.files);
            } else {
              this.fileList = [];
            };
            //    this.$set(this, "stepResourceList", list);
          }

        });
        this.filterData(this.fileList);
      },

      filterData(data) {
        let filterdata = data.filter(item => {
          return item.type === "Data";
        })
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

            modelProcess.append("content.description", this.modelProcessForm.description);
            modelProcess.append("content.methods", this.modelProcessForm.methods);
            modelProcess.append("content.outcome", this.modelProcessForm.outcome);
            modelProcess.append("content.others", this.modelProcessForm.others);
            // modelProcess.append("others", this.modelProcessForm.others);
            modelProcess.append("stepId", this.stepId)

            this.axios
              .post(
                "/GeoProblemSolving/step/update",
                modelProcess
              )
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
            if(res.data[0].type === "modeling for geographic process"){
                this.modelProcessForm = res.data[0].content;
            }
            else{
                this.$Notice.info({
                    desc: "Get the description failed!"
                  });
            }
            
            
          });
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
                };

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

        closeModuleSocket() {
        if (this.subprojectSocket != null) {
            this.removeTimer();
            this.subprojectSocket.close();
        }
        },
        openModuleSocket() {
        if (this.subprojectSocket != null) {
            this.subprojectSocket = null;
        }
        let subProjectId = this.subProjectInfo.subProjectId;
        var subprojectSocketURL =
            "ws://localhost:8081/GeoProblemSolving/Module/" + subProjectId;
        // var subprojectSocketURL = "ws://" + this.$store.state.IP_Port + "/GeoProblemSolving/Module/" + subProjectId;
        this.subprojectSocket = new WebSocket(subprojectSocketURL);
        this.subprojectSocket.onopen = this.onOpen;
        this.subprojectSocket.onmessage = this.onMessage;
        this.subprojectSocket.onclose = this.onClose;
        this.subprojectSocket.onerror = this.onError;
        this.setTimer();
        },

        onClose(e) {
        this.removeTimer();
        console.log("ModuleSocket连接断开！");
        },
        onError(e) {
        this.removeTimer();
        console.log("ModuleSocket连接错误！");
        },
        setTimer() {
        var that = this;
        this.timer = setInterval(() => {
            var messageJson = {};
            messageJson["type"] = "ping";
            messageJson["message"] = "ping";
            if (
            that.subprojectSocket != null &&
            that.subprojectSocket != undefined
            ) {
            that.subprojectSocket.send(JSON.stringify(messageJson));
            }
        }, 20000);
        },
        removeTimer() {
        clearInterval(this.timer);
        },

    }

    
  };
</script>