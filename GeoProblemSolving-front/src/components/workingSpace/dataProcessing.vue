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
                <BreadcrumbItem>Data Processing</BreadcrumbItem>
            </Breadcrumb>
            </Col>
            
            <Col span="12" style="text-align:center;font-size:1.5rem;height:20px;color:white;margin-top:1%">
            <strong>Data Processing</strong>
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
                        <Form ref="dataForm" :rules="dataProcessingValidate" :model="dataForm" :label-width="120" label-position="left" style="margin-top:15px" >
                          <FormItem label="Data description" prop="description">
                            <Input v-model="dataForm.description" placeholder="Please enter the description of the data"
                              type="textarea" :autosize="{minRows: 1,maxRows: 5}" clearable ></Input>
                          </FormItem>
                          <FormItem label="Income data" prop="income">
                            <Input v-model="dataForm.income" placeholder="Please enter the income data description"
                              type="textarea" :autosize="{minRows: 1,maxRows: 5}" clearable /></Input>
                          </FormItem>
                          <FormItem label="Outcome data" prop="outcome">
                            <Input v-model="dataForm.outcome" placeholder="Please enter the outcome data description"
                              type="textarea" :autosize="{minRows: 1,maxRows: 5}" clearable /></Input>
                          </FormItem>
                          <FormItem label="Main methods" prop="methods">
                            <Input v-model="dataForm.methods" placeholder="Please enter your goal" type="textarea"
                              :autosize="{minRows: 1,maxRows: 5}" clearable /></Input>
                          </FormItem>
                          <FormItem>
                            <Button @click="submit('dataForm')">Submit</Button>
                          </FormItem>
                        </Form>
                      </TabPane>
                      <TabPane name="Data" icon="ios-paper" label="Data">
                        <ul v-for="(item,index) in dataList" :key="index">
                          <li>{{item.name}}</li>
                        </ul>
                      </TabPane>
                      <TabPane name="Toolbox" icon="ios-construct" label="Toolbox">

                      </TabPane>
                    </Tabs>
                    
                  </Card>
                </div>
                <div class="condef">
                  <Card style="height:auto;min-height:100%">
                   
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
  import min from  "../../../static/js/jquery.min.js";
  import slimscroll from  "../../../static/js/jquery.slimscroll.min.js";
  import klorofil from  "../../../static/js/klorofil-common.js";
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
        dataForm: {
            // others:"",
            methods: "",
              description: "",
              income: "",
              outcome: "",
            },

        dataProcessingValidate: {
          description: [{
            required: true,
            message: "The description of data cannot be empty",
            trigger: "blur"
          }],
          income: [{
            required: true,
            message: "The income data should be described",
            trigger: "blur"
          }],
          outcome: [{
            required: false,
            message: "The outcome data should be described",
            trigger: "blur"
          }],
          methods: [{
            required: false,
            message: "The method of data processing is need",
            trigger: "blur"
          }]          
        },
      };
    },

    created() {
      this.init();
    },
    mounted() {
      window.addEventListener("resize", this.initSize);
      this.getDataProcessing();
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
            let dataProcessing = new URLSearchParams();
            // dataProcessing.append("subProjectId", this.subprojectId);
            dataProcessing.append("creator", creatorId);
            dataProcessing.append("type", "data processing");

            dataProcessing.append("content.description", this.dataForm.description);
            dataProcessing.append("content.income", this.dataForm.income);
            dataProcessing.append("content.outcome", this.dataForm.outcome);
            dataProcessing.append("content.methods", this.dataForm.methods);
            // dataProcessing.append("others", this.dataForm.others);
            dataProcessing.append("stepId", this.stepId)

            this.axios
              .post(
                "/GeoProblemSolving/step/update",
                dataProcessing
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

      getDataProcessing() {
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
            this.dataForm = res.data[0].content;
          });
      },

    }

    
  };
</script>