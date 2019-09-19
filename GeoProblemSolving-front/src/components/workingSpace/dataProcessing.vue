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

    .stepName{   
    text-align:center;
    font-size:1.2rem;
    height:20px;
    color:white;
  }
  .stepName p{  
    font-size:1rem;
    height:20px;
    color:white;
  }

   .onlineListBtn{
    position:absolute;
    top:70%;
    right:15%;
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

   .pro-tab >>> .ivu-btn-icon-only{
      font-size: 40px
  }
  .modelToolBtn{
      margin-left:2%;
      margin-top:3%;
      /* clear: both; */
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
            <div  class="breadCrumb">
              <Breadcrumb>
                <!-- <BreadcrumbItem :to="toProjectPage">Project</BreadcrumbItem> -->
                <BreadcrumbItem :to="toSubProjectPage">Subproject</BreadcrumbItem>
                <BreadcrumbItem>Data Processing</BreadcrumbItem>
              </Breadcrumb>
            </div>

            <div class="stepName">
              <strong>{{stepContent.name}}</strong>
              <p>{{stepContent.description}}</p>              
            </div>

            <div class="onlineListBtn">
              <Button @click="drawerValue = true" type="default" ghost>Participants</Button>
              <Button @click="modifyStep = true" style="margin-left:20px" type="info" ghost>Modify Step</Button>
            </div>

          </Row>
          <Drawer title="Participants" :closable="false" v-model="drawerValue">
            <online-participant :sub-project-id="subprojectId" :room-id="stepId"></online-participant>
          </Drawer>

          
            <Modal v-model="modifyStep">
              <p slot="header" style="text-align:center">
                <Icon type="ios-information-circle"></Icon>
                <span>Modify Step Name and Step Description</span>
              </p>
              <Form :label-width="120" label-position="left" :model="stepForm">
                <FormItem label="Step Name" prop="name">
                  <Input v-model="stepForm.name" type="textarea" :autosize="{minRows: 1,maxRows: 3}" clearable />
                </FormItem>
                <FormItem label="Step Description" prop="description">
                  <Input v-model="stepForm.description" type="textarea" :autosize="{minRows: 1,maxRows: 3}" clearable />
                </FormItem>
              </Form>
              <div slot="footer">
                <Button @click="cancelModifyStep">Cancel</Button>
                <Button type="primary" @click="submitModifyStep">Modify</Button>
              </div>
            </Modal>
         
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
                <div class="fileList">
                  <Card style=" height:100%;">
                    <div class="condefTitle">
                      <div style="width:3px;height:18px;float:left;background-color:rgb(124, 126, 126)"></div>
                      <h4 style="float:left;margin-left:5px">Data</h4>
                    </div>
                    <ul v-for="(item,index) in dataList" :key="index">
                      <li>{{item.name}}</li>
                    </ul>


                  </Card>
                </div>
                <div class="fileUdx">
                  <Card style="height:auto;min-height:100%">
                    <div class="condefTitle">
                      <div style="width:3px;height:18px;float:left;background-color:rgb(124, 126, 126)"></div>
                      <h4 style="float:left;margin-left:5px">Data Origin</h4>
                       
                    </div>
                    <div  style="clear:both">
                      <vue-markdown>this is the default slot</vue-markdown>
                      <Tabs>
                        <TabPane label="Data Origin" name="origin" icon="md-home"></TabPane>
                        <TabPane label="UDX Schema" name="udx" icon="md-home">
                          <vue-markdown>this is the default slot</vue-markdown>
                          <!-- <pre class="brush: html"><button></button></pre> -->
                        </TabPane>
                      </Tabs>
                    </div>
                  </Card>
                </div>
                <div class="condef">
                  <Card style="height:auto;min-height:100%">
                    <div class="condefTitle">
                      <div style="width:3px;height:18px;float:left;background-color:rgb(124, 126, 126)"></div>
                      <h4 style="float:left;margin-left:5px">Toolbox</h4>                     
                    </div>
                    <div  style="clear:both">
                     <Tooltip placement="bottom-start" class="modelToolBtn">
                        <Button icon="ios-brush" to="//134.175.111.77/note" target="_blank"></Button>
                        <div slot="content">
                          <p>Test Tool</p>
                        </div>
                      </Tooltip>
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
  import onlineParticipant from "./onlineParticipants";
  import VueMarkdown from 'vue-markdown';
  import markDown from "./../../mock/markdown.js";
  export default {
    components: {
      VueFlowy,
      Avatar,
      folderTree,
      onlineParticipant,
      VueMarkdown,
      markDown
    },
    data() {
      return {
        userInfo:"",
        contextDefinitionId: this.$route.params.id,//上一步的ID 和这一步的id？？
        fileList:[],
        dataList:[],
        sidebarHeight: 800,
        stepId: this.$route.params.id,
        subprojectId:this.$route.params.subid,

        stepContent:[],
         // online drawer
        drawerValue : false,
        modifyStep:false,
        stepForm: {          
          name:"",
          description:""
        },
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
        console.log(this.stepId);
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

      getDataProcessing() {
        this.axios
          .get(
            "/GeoProblemSolving/step/inquiry/" +
            "?key=stepId" +
            "&value=" +
            this.stepId
          )
          .then(res => {
            if (res.data[0].type === "dataProcessing") {
              // this.dataForm = res.data[0].content;
              this.stepContent = res.data[0];
              this.stepForm = res.data[0];
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
    

    }

    
  };
</script>
