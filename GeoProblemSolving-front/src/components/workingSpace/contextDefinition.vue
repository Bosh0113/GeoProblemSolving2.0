<style scoped>
.picscreen {
  position: relative;
  padding-top: 0.5%;
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
  width: 85px;
}
.condef {
  float: left;
  height: 100%;
  width: 93%;
  margin-left: 10px;
}

/* content */
.condefContent {
  clear: both;
  margin-top: 3%;
  margin-left: 15%;
  width: 800px;
}
.pro-tab {
  margin-top: 30px;
  /* height:auto; */
  min-height: 200px;
 
  
}

.pro-tab >>> .tabpane-syle {
  font-size: 20px;
  background-color: rgba(133, 115, 92, 0.1);
  /* min-height: 500px; */
}

.pro-tab >>> .ivu-tabs-bar {
  /*border-bottom: 1px solid #dddee1;*/
  margin-bottom: 0px;
  box-shadow: 0 0 3px 4px #e8eaec;
}

.pro-tab >>> .ivu-tabs-nav-wrap {
  text-align: center;
  margin-bottom: 0px;
  background-color: white;
}

.pro-tab >>> .ivu-tabs-nav-scroll {
  display: inline-block;
}
.pro-tab >>> .ivu-tabs-tab {
  font-size: 16px;
  height:auto
}
.pro-tab >>> .ivu-tabs-tabpane{
   height:auto;
    min-height: auto;
  
}

.tool-panel {
  display: flex;
  height: auto;
  flex-wrap: wrap;
  /* justify-content: center; */
  align-items: center;
}

/* 工具库中抽屉的工具样式*/
.singl_tool_style {
  margin: 10px;
  cursor: pointer;
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
  margin-top:2.5%;
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
                <!-- <BreadcrumbItem :to="toProjectPage">Project</BreadcrumbItem>
                            <BreadcrumbItem :to="toSubProjectPage">Subproject</BreadcrumbItem>
                <BreadcrumbItem>Working panel</BreadcrumbItem>-->
              </Breadcrumb>
            </Col>
            <Col span="12" style="text-align:center;font-size:1.5rem;height:20px;color:white">
              <strong>{{subProjectInfo.title}}</strong>
            </Col>
          </Row>
          <Row>
            <Col
              span="24"
              style="text-align:center;font-size:1rem;height:20px;color:white"
            >{{subProjectInfo.description}}</Col>
          </Row>
        </div>
      </div>
    </Row>

    <!-- tab  on-click事件 -->
    <Row>
      <div class="pro-tab" >
        <div >
          <Tabs>
            <TabPane label="Context Definition" name="Context" icon="md-home">
              <div class="workspaceContent">
                <template>
                  <Row style="margin-top:10px">
                    <!--contextdefinition -->
                    <div :style="{height:sidebarHeight+'px'}" style="margin:0 1% 50px 1%">
                      <div class="tools">
                        <Card style=" position:relative;height:100%;">
                          <h4 align="center">ToolBox</h4>
                          <!-- toolbox -->
                          <div class="tool-panel">
                            <!-- map -->
                            <div class="singl_tool_style">
                              <Icon type="md-globe" size="40" @click.native="toolPanel('map')" title="Map"
                                color="#808695" />
                            </div>
                            <!-- draw -->
                            <div class="singl_tool_style">
                              <Icon type="ios-brush" size="40" @click.native="toolPanel('draw')" title="DrawBoard"
                                color="#808695" />
                            </div>
                            <!-- mindMapping -->
                            <div class="singl_tool_style">
                              <Icon type="md-bulb" size="40" @click.native="toolPanel('mindMapping')" title="DrawBoard"
                                color="#808695" />
                            </div>
                            <!-- chat -->
                            <div class="singl_tool_style">
                              <Icon type="ios-chatboxes" size="40" @click.native="toolPanel('chat')" title="DrawBoard"
                                color="#808695" />
                            </div>
                          </div>
                        </Card>
                      </div>
                      <div class="condef">
                        <Card style="height:auto;min-height:100%">
                          <div class="condefTitle">
                            <div style="width:3px;height:18px;float:left;background-color:rgb(124, 126, 126)"></div>
                            <h4 style="float:left;margin-left:5px">Context Definition</h4>
                          </div>
                          <div class="condefContent" style="height:auto;">
                            <Form ref="contextForm" :rules="contextFormValidate" :model="contextForm"
                              :label-width="180">
                              <FormItem label="Problem Boundary" prop="boundary">
                                <Input v-model="contextForm.boundary"
                                  placeholder="Please enter the boundry of the problem" type="textarea"
                                  :autosize="{minRows: 1,maxRows: 5}" clearable></Input>
                              </FormItem>
                              <FormItem label="Spatiotemporal Scale" prop="scale">
                                <Input v-model="contextForm.scale" placeholder="Please enter the spatiotemporal scale"
                                  type="textarea" :autosize="{minRows: 1,maxRows: 5}" clearable />
                              </FormItem>
                              <FormItem label=" Main Methods" prop="methods">
                                <Input v-model="contextForm.methods" placeholder="Please enter the main methods"
                                  type="textarea" :autosize="{minRows: 1,maxRows: 5}" clearable />
                              </FormItem>
                              <FormItem label="Goals" prop="goals">
                                <Input v-model="contextForm.goals" placeholder="Please enter your goal" type="textarea"
                                  :autosize="{minRows: 1,maxRows: 5}" clearable />
                              </FormItem>
                              <FormItem label=" Difficulties" prop="difficulties">
                                <Input v-model="contextForm.difficulties" placeholder="Please enter the difficulties"
                                  type="textarea" :autosize="{minRows: 1,maxRows: 5}" clearable />
                              </FormItem>
                              <FormItem label="Others" prop="others">
                                <Input v-model="contextForm.others" placeholder="Supplement" type="textarea"
                                  :autosize="{minRows: 1,maxRows: 5}" clearable />
                              </FormItem>
                              <FormItem>
                                <Button @click="submit('contextForm')">Submit</Button>
                              </FormItem>
                            </Form>
                          </div>
                        </Card>
                      </div>
                    </div>
                  </Row>
                </template>
              </div>
            </TabPane>

            <TabPane label="Resources" name="Resources" icon="logo-buffer">
              <div class="resourceContent">
                <Row style="margin-top:10px">
                  <Card :style="{height:sidebarHeight+'px'}" style="margin:0 1% 50px 1%">
                    <div class="condefTitle">
                      <div style="width:3px;height:18px;float:left;background-color:rgb(124, 126, 126)"></div>
                      <h4 style="float:left;margin-left:5px">Resources</h4>
                    </div>
                    <div class="resourceCard" :style="{height:sidebarHeight+'px'}">
                      <div v-for="(item,index) in resourceType" :key="index">
                        <Col :xs="{ span: 20, offset: 1 }" :sm="{ span: 10, offset: 1 }" :md="{ span: 10, offset: 1 }"
                          :lg="{ span: 11 }">
                        <Card style="height:150px;margin-bottom:10px">
                          <Icon :type="item.typeIcon" style="float:left;" size="18"></Icon>
                          <h4> &ensp;{{item.typeName}}</h4>
                        </Card>
                        </Col>
                      </div>

                    </div>
                  </Card>
                </Row>
              </div>
            </TabPane>
          </Tabs>
        </div>
      </div>
    </Row>
  </div>
</template>

<script>
import { VueFlowy, FlowChart } from "vue-flowy";
import * as socketApi from "./../../api/socket";
import Avatar from "vue-avatar";
import echarts from "echarts";
export default {
  components: {
    VueFlowy,
    Avatar
  },
  data() {
    return {
      stepId: this.$route.params.id,
      // info of subproject --by mzy
      subProjectInfo: [],
      // 关于邀请的模态框
      sidebarHeight: 800,
      // web socket for contextDefinition
      subprojectSocket: null,
      // 资源继承
      selectResource: [],    

      // 问题解决流程结构
      processStructure: [],
      //现在的contextdefiniton
      currentContextDefinition: {},

      //form
      contextForm: {
        boundary: "",
        methods: "",
        scale: "",
        difficulties: "",
        goals: "",
        others: ""
      },

      contextFormValidate: {
        boundary: [
          {
            required: true,
            message: "The boundary of problem cannot be empty",
            trigger: "blur"
          }
        ],
        methods: [
          {
            required: true,
            message: "The main methods cannot be empty",
            trigger: "blur"
          }
        ],
        scale: [
          {
            required: false,
            message: "Please enter the spatiotemporal scale",
            trigger: "blur"
          }
        ],
        difficulties: [
          {
            required: false,
            message: "Please enter something to explain the difficulties",
            trigger: "blur"
          }
        ],
        goals: [
          {
            required: true,
            message: "The goals cannot be empty",
            trigger: "blur"
          }
        ],
        others: [
          {
            required: false,
            message: "Please enter something to add the context definition",
            trigger: "blur"
          }
        ]
      },
      resourceType: [{
          typeName: "Data",
          typeIcon: "ios-stats"
        },
        {
          typeName: "Images",
          typeIcon: "md-image"
        },
        {
          typeName: "Videos",
          typeIcon: "md-videocam"
        },
        {
          typeName: "Papers",
          typeIcon: "ios-paper"
        },
        {
          typeName: "Documents",
          typeIcon: "md-document"
        },
        {
          typeName: "Models",
          typeIcon: "md-construct"
        },
        {
          typeName: "Others",
          typeIcon: "md-cube"
        }
      ]

    };
  },

  created() {
    this.init();
  },
  mounted() {
    window.addEventListener("resize", this.reSize);
   // this.getContextDefiniton();
  },

  methods: {
    initSize() {
      this.sidebarHeight = window.innerHeight - 227;
    },

    init() {
      this.initSize();
      this.getContextDefinition();     
      
    },

    submit(contextform) {
      let subprojectId = this.$route.params.id;
      let creatorId = this.$store.getters.userId;
      this.$refs[contextform].validate(valid => {
        // 提交表单
        if (valid) {
          let contextDefinition = new URLSearchParams();
          contextDefinition.append("subProjectId", subprojectId);
          contextDefinition.append("creator", creatorId);
          contextDefinition.append("type", "context definition");

          contextDefinition.append("boundary", this.contextForm.boundary);
          contextDefinition.append("scale", this.contextForm.scale);
          contextDefinition.append("methods", this.contextForm.methods);
          contextDefinition.append(
            "difficulties",
            this.contextForm.difficulties
          );
          contextDefinition.append("goals", this.contextForm.goals);
          contextDefinition.append("others", this.contextForm.others);
          contextDefinition.append("stepId", this.stepId);

          this.axios
            .post(
              "/GeoProblemSolving/contextdefinition/update",
              contextDefinition
            )
            .then(res => {
              console.log(res.data);
              if (res.data == "Offline") {
                this.$store.commit("userLogout");
                this.$router.push({ name: "Login" });
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

    getContextDefinition() {
      this.axios
        .get("/GeoProblemSolving/contextdefinition/inquiry/"+
        "?key=stepId" +
        "&value=" 
         +this.stepId
        )
      .then(res => {
        console.log(res.data);
          //new id
          //ContextDefinition["stepId"] = res.data;
          this.contextForm= res.data[0];
          console.log(this.currentContextDefinition);         
        });
    }
  }
};
</script>
