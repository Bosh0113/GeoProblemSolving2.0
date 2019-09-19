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
    width: 6%;
  }

  .condef {
    float: left;
    height: 100%;
    width: 93%;
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

  .pro-tab {
    margin-top: 50px;
    /* height:auto; */
    min-height: 200px;
  }

  .pro-tab>>>.tabpane-syle {
    font-size: 20px;
    background-color: rgba(133, 115, 92, 0.1);
    /* min-height: 500px; */
  }

  .pro-tab>>>.ivu-tabs-bar {
    /*border-bottom: 1px solid #dddee1;*/
    margin-bottom: 0px;
    box-shadow: 0 0 3px 4px #e8eaec;
  }

  .pro-tab>>>.ivu-tabs-nav-wrap {
    text-align: center;
    margin-bottom: 0px;
    background-color: white;
  }

  .pro-tab>>>.ivu-tabs-nav-scroll {
    display: inline-block;
  }

  .pro-tab>>>.ivu-tabs-tab {
    font-size: 16px;
    height: auto;
  }

  .pro-tab>>>.ivu-card-body {
    padding: 12px;
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
    width:20%;
    position:absolute;
    top:50px;
    left:10px;
    right:50px
  }

  .resourceTable{    
    width:70%;
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

   .stepName input{  
    outline-style: none ;
    border: 0px;
    background-color: transparent;
    color:white;
    /* float:center; */

  }

  .onlineListBtn{
    position:absolute;
    top:70%;
    right:15%;
    /* width: 100px */
  }

   /* .home_content >>> .ivu-modal-footer {
    border:none;
    padding-bottom:12px;
    padding-right:18px ;
  }
  .home_content >>> .ivu-modal-body {
    padding-bottom: 0;
  } */

</style>
<template>
  <div style="background-color:#e8eaec;height:auto">
    <Row>
      <div class="picscreen">
        <div class="picbg"></div>

        <div class="home_content">
          <Row>
            <!-- 需要修改样式 -->
            <div class="breadCrumb">
              <Breadcrumb>
                <BreadcrumbItem :to="toSubProjectPage">Subproject</BreadcrumbItem>
                <BreadcrumbItem>Context Definition & Resources Collection</BreadcrumbItem>
              </Breadcrumb>
            </div>

            <div class="stepName">
              <strong>{{stepContent.name}}</strong>
              <p>{{stepContent.description}}</p>
            </div>

            <div class="onlineListBtn">
              <Button  @click="drawerValue = true" type="default" ghost>Participants</Button>
              <Button  @click="modifyStep = true" style="margin-left:20px" type="info" ghost>Modify Step</Button>
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
      <div class="pro-tab" :style="{height:sidebarHeight+ 70+'px'}">
        <div>
          <Tabs >
            <TabPane label="Context definition" name="Context" icon="md-home">
              <template>
                <Row style="margin-top:10px">
                  <!--contextdefinition -->
                  <div :style="{height:sidebarHeight+10+'px'}" style="margin:0 1%">
                    <div class="tools">
                      <Card style=" height:100%;">
                        <h4 align="center">Toolbox</h4>
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
                          <h4 style="float:left;margin-left:5px">Context</h4>
                        </div>
                        <vue-scroll :ops="scrollOps" :style="{height:sidebarHeight-50+'px'}">
                          <div class="condefContent">
                            <Form ref="contextForm" :rules="contextFormValidate" :model="contextForm"
                              :label-width="180">
                              <FormItem label="Problem boundary" prop="boundary">
                                <Input v-model="contextForm.boundary"
                                  placeholder="Please enter the boundry of the problem" type="textarea"
                                  :autosize="{minRows: 1,maxRows: 5}" clearable />
                              </FormItem>
                              <FormItem label="Spatiotemporal scale" prop="scale">
                                <Input v-model="contextForm.scale" placeholder="Please enter the spatiotemporal scale"
                                  type="textarea" :autosize="{minRows: 1,maxRows: 5}" clearable />
                              </FormItem>
                              <FormItem label=" Main methods" prop="methods">
                                <Input v-model="contextForm.methods" placeholder="Please enter the main methods"
                                  type="textarea" :autosize="{minRows: 1,maxRows: 5}" clearable />
                              </FormItem>
                              <FormItem label="Goals or Purposes" prop="goals">
                                <Input v-model="contextForm.goals" placeholder="Please enter your goal" type="textarea"
                                  :autosize="{minRows: 1,maxRows: 5}" clearable />
                              </FormItem>
                              <FormItem label=" Difficulties or Limitations" prop="difficulties">
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
                        </vue-scroll>
                      </Card>
                    </div>
                  </div>
                </Row>
              </template>
            </TabPane>
            <TabPane label="Resources collection" name="Resources" icon="logo-buffer">
              <div class="resourceContent">
                <Row style="margin-top:10px">
                  <Card :style="{height:sidebarHeight+10+'px'}" style="margin:0 1% 50px 1%">
                    <div class="condefTitle">
                      <div style="width:3px;height:18px;float:left;background-color:rgb(124, 126, 126)"></div>
                      <h4 style="float:left;margin-left:5px">Resources</h4>
                    </div>
                    <div slot="extra" style="display:flex;align-items:center;height:40px" class="popCenter">
                      <!-- 上传 -->
                      <Button id="upload" type="default" @click="uploadFileModalShow()" class="uploadBtn"
                        title="Upload resource">
                        <Icon type="md-cloud-upload" size="20" />
                      </Button>
                      <!-- 显示所有的resource -->
                      <Button class="moreBtn" type="default" style="margin-left: 10px" @click="toResourceList()"
                        title="more">
                        <Icon type="md-more" />
                      </Button>
                    </div>
                    <!-- <vue-scroll :ops="scrollOps" :style="{height:sidebarHeight-50+'px'}"> -->
                    <div class="resourceMenu">
                      <!-- <div v-for="(item,index) in resourceType" :key="index">                         -->
                        <Menu active-name="data"  @on-select="filterResourceType">
                          <MenuItem name="data">
                            <Icon type="ios-stats" />
                            Data
                          </MenuItem>
                          <MenuItem name="image">
                            <Icon type="md-image" />
                            Images
                          </MenuItem>
                          <MenuItem name="video">
                            <Icon type="md-videocam" />
                            Videos
                          </MenuItem>
                          <MenuItem name="paper">
                            <Icon type="ios-paper" />
                            Papers
                          </MenuItem>
                          <MenuItem name="document">
                            <Icon type="md-document" />
                            Documents
                          </MenuItem>
                          <MenuItem name="model">
                            <Icon type="md-construct" />
                            Models
                          </MenuItem>
                           <MenuItem name="others">
                            <Icon type="md-cube" />
                            Others
                          </MenuItem>
                        
                        </Menu>  
                      <!-- </div> -->
                    </div>
                    <div class="resourceTable" >
                       <!-- <div style="height:250px;overflow-y:scroll;margin-top:10px" > -->
                            <Table  :columns="resourceTableColName"  :data="filterFileType"
                              v-show="filterFileType!=[]&&filterFileType!='None'">
                              <template slot-scope="{ row }" slot="name">
                                <strong>{{ row.name }}</strong>
                              </template>

                              <template slot-scope="{ row, index }" slot="action">
                                <a :href="filterFileType[index].pathURL"
                                  :download="filterFileType[index].name" title="Download">
                                  <Icon type="md-download" :size="20" />
                                </a>
                                <span @click="show(index)" style="margin-left:10px" title="Preview">
                                  <Icon type="md-eye" :size="20" color="#2d8cf0" style="cursor:pointer" />
                                </span>
                              </template>
                            </Table>
                          <!-- </div>  -->
                    </div>
                    <!-- </vue-scroll> -->
                  </Card>
                </Row>
              </div>

              <!-- 上传文件按钮的模态框 -->
              <Modal v-model="uploadFileModal" title="Upload file" width="800px" :mask-closable="false"
                @on-ok="filesUpload('fileUploadForm')" ok-text="Submit" cancel-text="Cancel">
                <div>
                  <Form ref="fileUploadForm" :model="fileUploadForm" :rules="fileUploadFormRuleValidate"
                    :label-width="100">
                    <FormItem label="Privacy" prop="privacy">
                      <RadioGroup v-model="fileUploadForm.privacy" style="width:80%">
                        <Radio label="private">Private</Radio>
                        <Radio label="public">Public</Radio>
                      </RadioGroup>
                    </FormItem>
                    <FormItem label="Type" prop="type">
                      <RadioGroup v-model="fileUploadForm.type">
                        <Radio label="data">Data</Radio>
                        <Radio label="image">Images</Radio>
                        <Radio label="video">Videos</Radio>
                        <Radio label="paper">Papers</Radio>
                        <Radio label="document">Documents</Radio>
                        <Radio label="model">Models</Radio>
                        <Radio label="others">Others</Radio>
                      </RadioGroup>
                    </FormItem>
                    <FormItem label="Description" prop="description">
                      <Input type="textarea" :rows="4" v-model="fileUploadForm.description"
                        placeholder="Enter file description" />
                    </FormItem>
                  </Form>
                </div>
                <Upload :max-size="1024*1024" multiple type="drag" :before-upload="gatherFile" action="-">
                  <div style="padding: 20px 0">
                    <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
                    <p>
                      Click or drag files here to upload(The file size must control in
                      <span style="color:red">1GB</span>)
                    </p>
                  </div>
                </Upload>

                <div style="padding:0 10px 0 10px">
                  <ul v-for="(list,index) in file" :key="index">
                    <li style="display:flex">
                      File name:
                      <span style="font-size:10px;margin: 0 5px 0 5px" :title="list.name">{{ list.name }}</span>
                      (Size:
                      <span v-if="list.size<(1024*1024)"
                        style="font-size:10px;margin-right:10px">{{(list.size/1024).toFixed(2)}}KB)</span>
                      <span v-else
                        style="font-size:10px;margin-right:10px">{{(list.size/1024/1024).toFixed(2)}}MB)</span>
                      <Icon type="ios-close" size="20" @click="delFileList(index)"
                        style="display:flex;justify-content:flex-end;cursor:pointer" title="Cancel"></Icon>
                    </li>
                  </ul>
                </div>
              </Modal>
            </TabPane>
          </Tabs>
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
  export default {
    components: {
      VueFlowy,
      Avatar,
      folderTree,
      onlineParticipant
    },
    data() {
      return {
        scrollOps: {
          bar: {
            background: "lightgrey"
          }
        },
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
          scale: "",
          difficulties: "",
          goals: "",
          methods: "",
          others: ""
        },

        contextFormValidate: {
          boundary: [{
            required: true,
            message: "The boundary of problem cannot be empty",
            trigger: "blur"
          }],
          methods: [{
            required: true,
            message: "The main methods cannot be empty",
            trigger: "blur"
          }],
          scale: [{
            required: false,
            message: "Please enter the spatiotemporal scale",
            trigger: "blur"
          }],
          difficulties: [{
            required: false,
            message: "Please enter something to explain the difficulties",
            trigger: "blur"
          }],
          goals: [{
            required: true,
            message: "The goals cannot be empty",
            trigger: "blur"
          }],
          others: [{
            required: false,
            message: "Please enter something to add the context definition",
            trigger: "blur"
          }]
        },
        resourceType: [{
            typeName: "data",
            typeIcon: "ios-stats"
          },
          {
            typeName: "image",
            typeIcon: "md-image"
          },
          {
            typeName: "video",
            typeIcon: "md-videocam"
          },
          {
            typeName: "paper",
            typeIcon: "ios-paper"
          },
          {
            typeName: "document",
            typeIcon: "md-document"
          },
          {
            typeName: "model",
            typeIcon: "md-construct"
          },
          {
            typeName: "others",
            typeIcon: "md-cube"
          }
        ],
         subprojectId:this.$route.params.subid,

        //////// resource
        // 点击上传文件按钮时弹出的模态框
        uploadFileModal: false,
        // 上传的文件数组（支持多文件）
        file: [],
        // 获取的项目下已上传的资源的列表
        stepResourceList: [],
        // 项目下资源表格的表头信息
        resourceTableColName: [{
            title: "Name",
            key: "name",
            tooltip: true,
            sortable: true
          },
          {
            title: "Type",
            key: "type",
            width: 100,
            sortable: true
          },
          {
            title: "Size",
            key: "fileSize",
            width: 100,
            sortable: true
          },
          {
            title: "Description",
            key: "description",
            tooltip: true,
            sortable: true
          },
          {
            title: "Upload time",
            key: "uploadTime",
            width: 160,
            sortable: true
          },
          {
            title: "Action",
            slot: "action",
            width: 120,
            align: "center"
          }
        ],
        // 点击项目编辑按钮弹出的模态框
        editProjectModal: false,
        // 点击删除项目按钮弹出的模态框
        removeProjectModal: false,
        // 子项目成员数组（用于子项目权限转移时使用）
        subProjectMembers: [],
        // 邮箱输入尾缀格式自动补全的提示变量
        prompt: [],
        // 显示进度条的模态框
        progressModalShow: false,
        // 文件上传的进度
        uploadProgress: 0,
        // 本页面使用的jspanel预览工具的控制变量
        panel: null,
        // 上传文件个数限制定时器
        fileCountTimer: null,

        fileUploadForm: {
          type: "data",
          privacy: "private",
          description: ""
        },
        fileUploadFormRuleValidate: {
          type: [{
            required: true,
            message: "The type cannot be empty",
            trigger: "blur"
          }],
          privacy: [{
            required: true,
            message: "The privacy cannot be empty",
            trigger: "blur"
          }],
          description: [{
            required: true,
            type: "string",
            message: "Please inpput file description",
            trigger: "blur"
          }]
        },
        filterFileType: [],
        typeName: "data",
          // 当前选中通知条目的详情
        currentNoticeDetail: [],
        panelList: [],
        panel: null,
          // web socket for module
        subprojectSocket: null,
        timer: null,

        // online drawer
        drawerValue : false,
        stepContent:[],
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
    },

    methods: {
      initSize() {
        this.sidebarHeight = window.innerHeight - 290;
      },

      init() {
        this.initSize();
        this.getContextDefinition();
        this.getAllResource();
      },

      submit(contextform) {
        // this.subprojectId = this.$route.params.id;
        let creatorId = this.$store.getters.userId;
        this.$refs[contextform].validate(valid => {
          // 提交表单
          if (valid) {
            let contextDefinition = new URLSearchParams();
            contextDefinition.append("creator", creatorId);
            contextDefinition.append("type", "contextDefinition");

            contextDefinition.append("content.boundary", this.contextForm.boundary);
            contextDefinition.append("content.scale", this.contextForm.scale);
            contextDefinition.append("content.methods", this.contextForm.methods);
            contextDefinition.append("content.difficulties",this.contextForm.difficulties);
            contextDefinition.append("content.goals", this.contextForm.goals);
            contextDefinition.append("content.others", this.contextForm.others);
            contextDefinition.append("stepId", this.stepId);

            this.axios
              .post(
                "/GeoProblemSolving/step/update",
                contextDefinition
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

      getContextDefinition() {
        this.axios
          .get(
            "/GeoProblemSolving/step/inquiry/" +
            "?key=stepId" +
            "&value=" +
            this.stepId
          )
          .then(res => {
            if (res.data[0].type === "contextDefinition") {
              this.contextForm = res.data[0].content;
              this.stepContent = res.data[0];
              this.stepForm = res.data[0];

            } else {
              this.$Notice.info({
                desc: "Get the description failed!"
              });
            }
          });
      },

      //上传文件的模态框打开
      uploadFileModalShow() {
        this.uploadFileModal = true;
      },

      filesUpload(form) {
        this.uploadProgress = 0;
        this.$refs[form].validate(valid => {
          if (valid) {
            let that = this;
            if (that.file.length != 0) {
              var formData = new FormData();
              var total = 0;
              for (var i = 0; i < that.file.length; i++) {
                if (that.file[i].fileSize < Math.pow(1024, 2)) {
                  formData.append("file", that.file[i]); // 文件对象
                } else {}
                total += that.file[i].fileSize;
              }
              if (total < Math.pow(1024, 2)) {
                let userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
                formData.append("description", this.fileUploadForm.description);
                formData.append("type", this.fileUploadForm.type);
                formData.append("uploaderId", userInfo.userId);
                formData.append("folderId", this.stepId);
                formData.append("privacy", this.fileUploadForm.privacy);
                this.progressModalShow = true;
                this.axios({
                    url: "/GeoProblemSolving/folder/uploadToFolder",
                    method: "post",
                    onUploadProgress: progressEvent => {
                      this.uploadProgress = ((progressEvent.loaded / progressEvent.total) * 100) | 0;
                    },
                    data: formData
                    })
                    .then(res => {
                      if (res.data != "Fail") {
                        var uploadedList = res.data.uploaded;
                        var failedList = res.data.failed;
                        var sizeOverList = res.data.sizeOver;
                        
                        if (sizeOverList.length > 0) {
                          this.$Notice.warning({
                            title: "Files too large.",
                            render: h => {
                              return h("span", sizeOverList.join(";"));
                            }
                          });
                        }
                        if (failedList.length > 0) {
                          this.$Notice.error({
                            title: "Upload fail.",
                            render: h => {
                              return h("span", failedList.join(";"));
                            }
                          });
                        }
                      } else {
                        this.$Message.warning("Upload fail.");
                      }

                      this.addUploadEvent(this.stepId);
                      this.getAllResource();
                      this.progressModalShow = false;
                      this.uploadProgress = 0;
                    })
                  .catch(err => {
                     this.progressModalShow = false;
                     this.$Message.warning("Upload fail.");
                     this.uploadProgress = 0;
                  });
              } 
            } else {
              this.$Message.warning("Upload file is null.");
            }
          } 
        });
       
        
      },

      // 预览
      show(index) {
        let name = this.filterFileType[index].name;
        if (/\.(doc|docx|xls|xlsx|csv|ppt|pptx|zip)$/.test(name.toLowerCase())) {
          if (this.panel != null) {
            this.panel.close();
          }
          var url =
            "http://172.21.212.72:8012/previewFile?url=" + 'http://' + this.$store.state.IP_Port +
            this.filterFileType[index].pathURL;
          var toolURL =
            '<iframe src=' + url + ' style="width: 100%;height:100%"></iframe>';
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
        } else if (/\.(mp4)$/.test(name.toLowerCase())) {
          if (this.panel != null) {
            this.panel.close();
          }
          var url =
            'http://' + this.$store.state.IP_Port + this.filterFileType[index].pathURL;
          var toolURL =
            '<video src=' + url + ' style="width: 100%;height:100%" controls></video>';
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
        } else if (/\.(pdf|xml|json|md|gif|jpg|png)$/.test(name.toLowerCase())) {
          if (this.panel != null) {
            this.panel.close();
          }
          var url =
            'http://' + this.$store.state.IP_Port + this.filterFileType[index].pathURL;
          var toolURL =
            '<iframe src=' + url + ' style="width: 100%;height:100%" controls></iframe>';
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
        } else {
          this.$Notice.error({
            title: "Open failed",
            desc: "Not supported file format."
          });
          return false;
        }
      },

      gatherFile(file) {
        let that = this;
        if (that.file.length >= 5) {
          if (this.fileCountTimer != null) {
            clearTimeout(this.fileCountTimer);
          }
          this.fileCountTimer = setTimeout(() => {
            this.$Message.info("you can upload 5 files one time!");
          }, 500);
        } else {
          that.file.push(file);
          that.file.map(element => {
            element["fileSize"] = Math.round((element.size / 1024) * 100) / 100;
          });
        }
        return false;
      },

      delFileList(index) {
        let that = this;
        that.file.splice(index, 1);
      },

      addUploadEvent(scopeId) {
        let form = {};
        // let description =
        //   this.$store.getters.userName +
        //   " uploaded a " +
        //   this.fileUploadForm.type +
        //   " file in " +
        //   " project called " +
        //   this.currentProjectDetail.title;
        form["description"] = scopeId;
        form["folderId"] = scopeId;
        form["eventType"] = "step";
        form["userId"] = this.$store.getters.userId;
        this.axios
          .post("/GeoProblemSolving/history/save", form)
          .then(res => {
            console.log(res.data);
          })
          .catch(err => {
            console.log(err.data);
          });
      },

      //获取全部资源的方法
      getAllResource() {
        var id=this.stepId;
        var list=[];
        var that= this;
        $.ajax({
          url: "/GeoProblemSolving/folder/inquiry" +
            "?folderId=" + id,
            type: "GET",
            async: false,
            success:data =>{
              if (data !== "None") {
               list.push(data.files);
              } else {
                this.stepResourceList = [];
              };
               this.$set(this, "stepResourceList", list);
            }
            
        });
        this.$set(this, "stepResourceList",this.stepResourceList[0]);

        this.filterResourceType(this.typeName);       

      },

      filterResourceType(name){  
        this.typeName = name;
        let filterFile = [];
        let resourceType = this.resourceType;
        let stepResourceList = this.stepResourceList;

        for(var i = 0; i<stepResourceList.length;i++)
        {
          if(stepResourceList[i].type == name)
          {
            filterFile.push(stepResourceList[i]);
          }
        }
        this.$set(this,"filterFileType",filterFile)
        console.log(this.filterFileType);
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

      // 工具栏
      toolPanel(type) {
        // if (this.userRole != "Visitor") {
        this.axios
          .get("/GeoProblemSolving/user/state")
          .then(res => {
            if (!res.data) {
              this.$store.commit("userLogout");
              this.$router.push({
                name: "Login"
              });
            } else {
              var toolURL = "";
              let toolName = "";
              if (type == "map") {
                toolURL =
                  '<iframe src="' +
                  "http://" +
                  this.$store.state.IP_Port +
                  '/GeoProblemSolving/map" style="width: 100%;height:100%"></iframe>';
                toolName = "Map";
              } else if (type == "draw") {
                toolURL =
                  '<iframe src="' +
                  "http://" +
                  this.$store.state.IP_Port +
                  '/GeoProblemSolving/draw" style="width: 100%;height:100%"></iframe>';
                toolName = "Drawing";
              } else if (type == "chart") {
                toolURL =
                  '<iframe src="' +
                  "http://" +
                  this.$store.state.IP_Port +
                  '/GeoProblemSolving/charts" style="width: 100%;height:100%"></iframe>';
                toolName = "Chart";
              } else if (type == "chat") {
                toolURL =
                  '<iframe src="' +
                  "http://" +
                  this.$store.state.IP_Port +
                  '/GeoProblemSolving/chat" style="width: 100%;height:100%"></iframe>';
                toolName = "Chatroom";
              } else if (type == "graphEditor") {
                toolURL =
                  '<iframe src="/GeoProblemSolving/Collaborative/GraphEditor/index.html' +
                  "?groupID=" +
                  this.stepId +
                  '" style="width: 100%;height:100%"></iframe>';
                toolName = "Sketchpad";
              } else if (type == "3DmodelViewer") {
                toolURL =
                  '<iframe src="/GeoProblemSolving/Collaborative/3DmodelViewer/index.html' +
                  "?groupID=" +
                  this.stepId +
                  '" style="width: 100%;height:100%"></iframe>';
                toolName = "3D model viewer";
              } else if (type == "LogicalModel") {
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
              } else if (type == "tableEditor") {
                toolURL =
                  '<iframe src="/GeoProblemSolving/Collaborative/jexcelTool/index.html' +
                  "?groupID=" +
                  this.stepId +
                  '" style="width: 100%;height:100%"></iframe>';
                toolName = "Table editor";
              } else if (type == "nc-map") {
                toolURL =
                  '<iframe src="' +
                  "http://" +
                  this.$store.state.IP_Port +
                  '/GeoProblemSolving/nc/map" style="width: 100%;height:100%"></iframe>';
                toolName = "Map";
              } else if (type == "nc-draw") {
                toolURL =
                  '<iframe src="' +
                  "http://" +
                  this.$store.state.IP_Port +
                  '/GeoProblemSolving/nc/draw" style="width: 100%;height:100%"></iframe>';
                toolName = "Drawing";
              } else if (type == "nc-chart") {
                toolURL =
                  '<iframe src="' +
                  "http://" +
                  this.$store.state.IP_Port +
                  '/GeoProblemSolving/nc/charts" style="width: 100%;height:100%"></iframe>';
                toolName = "Chart";
              } else if (type == "cn-tableEditor") {
                toolURL =
                  '<iframe src="/GeoProblemSolving/Collaborative/jexcelTool/excelToolSingle.html' +
                  '" style="width: 100%;height:100%"></iframe>';
                toolName = "Table editor";
              } else if (type == "nc-3DmodelViewer") {
                toolURL =
                  '<iframe src="/GeoProblemSolving/Collaborative/3DmodelViewer/indexSingle.html' +
                  '" style="width: 100%;height:100%"></iframe>';
                toolName = "3D model viewer";
              } else if (type == "nc-video") {
                toolURL =
                  '<iframe src="' +
                  "http://" +
                  this.$store.state.IP_Port +
                  '/GeoProblemSolving/video" style="width: 100%;height:100%"></iframe>';
                toolName = "Video player";
              } else if (type == "nc-pdf") {
                toolURL =
                  '<iframe src="' +
                  "http://" +
                  this.$store.state.IP_Port +
                  '/GeoProblemSolving/pdfview" style="width: 100%;height:100%"></iframe>';
                toolName = "Pdf viewer";
              } else if (type == "Doc Edit") {
                toolURL =
                  '<iframe src="' +
                  "http://" +
                  this.$store.state.IP_Port +
                  '/GeoProblemSolving/tinymce" style="width: 100%;height:100%"></iframe>';
                toolName = "Text editor";
              } else if (type == "Video Tool") {
                var userId = this.$store.getters.userId;
                var moduleId = this.stepId;
                var reg = /(\S)*:80/;
                var IP_Port = this.$store.state.IP_Port;
                var videoIP_Port = IP_Port.match(reg)[1];
                toolURL =
                  '<iframe src="https://' +
                  videoIP_Port +
                  ":8083/GeoProblemSolving/Collaborative/vedioChat/WebRtcTest.html" +
                  "?roomId=" +
                  moduleId +
                  "&userId=" +
                  userId +
                  '" style="width: 100%;height:100%"></iframe>';
                toolName = "Video Tool";
              } else if (type == "Web-SWMM") {
                toolURL =
                  '<iframe src="http://geomodeling.njnu.edu.cn/hydro-model-integration/webswmm" style="width: 100%; height:100%"></iframe>';
                toolName = "Web-SWMM";
              } else if (type == "TrafficNoise") {
                toolURL =
                  '<iframe src="http://geomodeling.njnu.edu.cn/TrafficNoiseTheme/trafficNoise.html" style="width: 100%; height:100%"></iframe>';
                toolName = "Traffic Noise";
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
                callback: function () {
                  // this.content.style.padding = "20px";
                }
              });
              // panel.resizeit("disable");
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