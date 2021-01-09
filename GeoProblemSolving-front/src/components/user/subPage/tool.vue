<template>
  <div>
    <Row>
      <Col span="20">
        <!--  Filter 包括 Created by me, Shared with me(项目中内容)，public, privacy-->
        <Row>
          <Card>
            <CheckboxGroup v-model="checkedType">
              <Checkbox label="public">
                <label>Public</label>
                <label class="badge">{{publicToolNum}}</label>
              </Checkbox>
              <Checkbox label="private">
                <label>Privacy</label>
                <label class="badge">{{privateToolNum}}</label>
              </Checkbox>
            </CheckboxGroup>
          </Card>
        </Row>
        <!--  具体显示区，图标加 缩略图 + 名字的显示方案; 先把created tool 拉过来；添加 shared 接口-->
        <Row>
          <Card>
            <p slot="title">Tools</p>
            <div slot="extra">
              <!--              添加一个模态显示判断 -->
              <Button
                shape="circle"
                icon="ios-add-circle-outline"
                @click="createToolModal=true"
              >Create tool
              </Button>
            </div>
            <!--            工具显示内容  -->

            <div
              style="width: 210px; display: inline-block; margin-left: 20px"
              v-for="(tool,index) in personalTools"
            >
<!--              显示 public-->
                <Card
                  v-if="tool.privacy =='Public' && checkedType.includes('public')"
                >
                  <p slot="title">{{tool.toolName}}</p>
                  <div slot="extra">
                    <Icon type="ios-settings" size="20" class="icon" @click="editTool(tool)"/>
                    <Icon type="ios-share-alt" size="20" class="icon" @click="shareTool(tool)"/>
                    <Icon type="md-close" size="20" class="icon" @click="delTool(tool.tid, tool.toolName, index)"/>
                  </div>
                  <img
                    :src="tool.toolImg"
                    v-if="tool.toolImg!=''"
                    style="height:100%;max-height:50px;"
                  />
                  <avatar
                    :username="tool.toolName"
                    :size="50"
                    style="margin-bottom:6px"
                    v-else
                  ></avatar>
                  {{tool.description}}
                </Card>

              <Card
                v-if="tool.privacy =='Private' && checkedType.includes('private')"
              >
                <p slot="title">{{tool.toolName}}</p>
                <div slot="extra">
                  <Icon type="ios-settings" size="20" class="icon" @click="editTool(tool)"/>
                  <Icon type="ios-share-alt" size="20" class="icon" @click="shareTool(tool)"/>
                  <Icon type="md-close" size="20" class="icon" @click="delTool(tool.tid, tool.toolName, index)"/>
                </div>
                <img
                  :src="tool.toolImg"
                  v-if="tool.toolImg!=''"
                  style="height:100%;max-height:50px;"
                />
                <avatar
                  :username="tool.toolName"
                  :size="50"
                  style="margin-bottom:6px"
                  v-else
                ></avatar>
                {{tool.description}}
              </Card>
              </div>
<!--            </div>-->


          </Card>
        </Row>
      </Col>
      <!--    History Line-->
      <Col span="4">
        <Card dis-hover>
          <p slot="title">Event Line</p>
        </Card>
      </Col>
    </Row>
    <!--    Modal 对话框部分 -->
    <!--  create tool modal -->
    <Modal
      v-model="createToolModal"
      title="Create Tool"
      width="800"
    >
      <template-general @generalInfo="getGeneralInfo" :step="currentStep"></template-general>
      <div slot="footer">
        <Button
          @click="previousStep"
          v-show="this.currentStep == 1"
          type="warning"
        >Previous
        </Button>
        <Button
          v-show="this.currentStep == 0"
          @click="nextStep"
          type="primary">Next
        </Button>
        <Button
          type="success"
          @click="createTool"
          v-show="this.currentStep == 1"
        >Create
        </Button>
      </div>
    </Modal>

    <Modal v-model="shareToolModal" width="800" title="Share tool">
      <Form>
        <FormItem>
          <Select v-model="formItem.select">
            <Option value="selectProject">Share "{{this.sharedTool.toolName}}" to Project</Option>
            <Option value="selectUser">Share "{{this.sharedTool.toolName}}" to User</Option>
          </Select>
        </FormItem>
        <FormItem v-if="formItem.select == 'selectProject'">
          <Select v-model="formItem.sharedProjectId" placeholder="Select Project">
            <Option v-for="(item,index) in userProject" :value="item.aid" :key="index">{{ item.name }}</Option>
          </Select>
        </FormItem>
        <FormItem v-if="formItem.select == 'selectUser'">
          <Input v-model="formItem.sharedUserEmail" placeholder="Enter email address"></Input>
        </FormItem>
      </Form>

      <div slot="footer">
        <Button type="warning" @click="shareToolModal = false">Cancel</Button>
        <Button type="success" @click="sharingTool">Share</Button>
      </div>
    </Modal>


    <Modal v-model="editToolModal" title="Edit tool" width="800" :mask-closable="false">
      <template-edit
        @generalInfo="getEditInfo"
        :step="currentStep"
        :selectTool="editToolInfo"
      ></template-edit>
      <div slot="footer">
        <Button type="warning" @click="previousStep" v-show="this.currentStep == 1">Previous</Button>
        <Button type="primary" @click="nextStep" v-show="this.currentStep == 0">Next</Button>
        <Button
          type="success"
          @click="commitEdit"
          class="create"
          v-show="this.currentStep == 1"
        >Commit
        </Button>
      </div>
    </Modal>

    <!--    确认删除 Modal-->
    <Modal v-model="confirmDelModal" width="300">
      <label style="font-size: 20px">Are you sure to delete </label>
      <h2>{{this.delToolName}}</h2>
      <div slot="footer">
        <Button
          type="warning"
          @click="confirmDelModal = false"
        >Cancel
        </Button>
        <Button
          type="success"
          @click="confirmDelTool"
        >Ok
        </Button>
      </div>
    </Modal>
  </div>
</template>

<script>
  import TemplateGeneral from "../../tools/TemplateGeneral";
  import {del, post} from "../../../axios";
  import Avatar from "vue-avatar";
  import TemplateEdit from "../../tools/TemplateEdit";
  import todoList from "./todoList";

  export default {
    name: "tool",
    components: {
      Avatar,
      TemplateGeneral,
      TemplateEdit
    },
    data() {
      return {
        checkedType: ["public", "private"],
        shareToolModal: false,
        createToolModal: false,
        editToolModal: false,
        confirmDelModal: false,
        personalTools: [],
        publicTools: [],
        privateTools: [],
        userProject: [],
        formItem: {
          select: "",
          sharedUserEmail: "",
          sharedProjectId: "",
        },
        toolInfo: {
          toolName: "",
          modelInfo: {
            stateId: "",
            oid: "",
            mdlId: ""
          },
          description: "",
          toolUrl: "",
          recomStep: [],
          categoryTag: [],
          toolImg: "",
          privacy: "Private",
          detail: ""
        },
        editToolInfo: {},
        toolInfoRule: {},
        // create tool step
        currentStep: 0,
        delToolId: "",
        delToolIndex: undefined,
        delToolName: "",
        sharedTool: {}
      }
    },
    created() {
      this.getUserProjects();
    },
    mounted() {
      this.getPersonalTools();
    },
    methods: {
      getUserProjects: function () {
        let projectIds = "";
        let userInfo = this.$store.getters.userInfo;
        if (userInfo.createdProjects != null) {
          for (let i = 0; i < userInfo.createdProjects.length; i++) {
            if (i != userInfo.createdProjects.length - 1) {
              projectIds += userInfo.createdProjects[i] + ","
            } else {
              projectIds += userInfo.createdProjects[i]
            }
          }
        }
        if (userInfo.joinedProjects != null) {
          for (let i = 0; i < userInfo.joinedProjects.length; i++) {
            if (i != userInfo.joinedProjects.length - 1) {
              projectIds += userInfo.joinedProjects[i] + ","
            } else {
              projectIds += userInfo.joinedProjects[i]
            }
          }
        }
        this.$axios.get("/GeoProblemSolving/project/getProjects?aids=" + projectIds)
          .then(res => {
            this.$set(this, "userProject", res.data.data)
          })
          .catch(err => {
            this.$Message.error("Loading project failed.")
          })
      },
      getGeneralInfo: function (form) {
        this.toolInfo = form;
      },
      //与TemplateGeneral 双向绑定，子组件向夫组件传值使用方法
      getEditInfo: function (form) {
        this.editToolInfo = form;
      },
      editTool: function (item) {
        this.editToolModal = true;
        this.editToolInfo = item;
        console.log("this.editToolInfo: ", this.editToolInfo)
      },
      getPersonalTools: function () {
        this.$axios.get("/GeoProblemSolving/tool/findByProvider/" + this.$store.getters.userId)
          .then(res => {
            this.$set(this, "personalTools", res.data.data);
          })
          .catch(err => {
            this.$Message.error("Loading Fail.")
          })
      },

      nextStep: function () {
        if (this.currentStep != 1) {
          this.currentStep += 1;
        }
      },
      previousStep: function () {
        if (this.currentStep != 0) {
          this.currentStep -= 1;
        }
      },
      createTool: async function () {
        let createToolForm = this.toolInfo;
        createToolForm["provider"] = this.$store.getters.userId;
        let data = await post("/GeoProblemSolving/tool/create", createToolForm);
        // 判断返回值，进行下一步操作
        this.createToolModal = false;
        //Notice 与 Message 的区别
        this.personalTools.push(data);
        this.currentStep = 0;
        this.$Notice.info({desc: "Create successfully"});
      },
      delTool: function (toolId, toolName, index) {
        this.confirmDelModal = true;
        this.delToolId = toolId;
        this.delToolIndex = index;
        this.delToolName = toolName;
      },
      confirmDelTool: function () {
        this.$axios.delete("/GeoProblemSolving/tool/delete?tid=" + this.delToolId)
          .then(res => {
            if (res.data.code == 0) {
              this.personalTools.splice(this.delToolIndex, 1);
            } else {
              this.$Message.error("Delete Fail! Try again.")
            }
          })
          .catch(err => {
            this.$Message.error("Delete Fail.")
          })

      },
      commitEdit: function () {
        this.$axios
          .post("/GeoProblemSolving/tool/update/" + this.editToolInfo.tid, this.editToolInfo)
          .then(res => {
            this.$Notice.info({desc: "Update successfully."});
            this.editToolModal = false;
          })
          .catch(err => {
            this.$Message.error("Update Fail.Try again!")
          })
      },
      shareTool: function (tool) {
        this.shareToolModal = true;
        this.sharedTool = tool;
      },
      sharingTool: function () {
        let updateProject = {};
        let index = null;
        for (let i = 0; i < this.userProject.length; i++) {
          if (this.userProject[i].aid == this.formItem.sharedProjectId) {
            updateProject.toolList = this.userProject[i].toolList;
            index = i;
          }
        }
        if (updateProject.toolList != null) {
          updateProject.toolList.push(this.sharedTool.tid);
        } else{
          updateProject.toolList = [this.sharedTool.tid];
        }
        this.$axios
          .put("/GeoProblemSolving/project/" + this.formItem.sharedProjectId, updateProject)
          .then(res => {
            if (res.data.code == 0){
              this.$Notice.info({desc: "Update successfully."});
              this.shareToolModal = false;

              this.userProject[index].toolList = updateProject.toolList;
            }else {
              this.$Message.error("Sharing Failed.")
            }

          })
          .catch(err => {
            this.$Message.error("Update Fail.Try again!")
          })
      }
    },
    computed: {
      publicToolNum: function () {
        let tempTools = this.personalTools;
        let num = 0;
        for (let i=0; i<tempTools.length; i++){
          if (tempTools[i].privacy == "Public"){
            num ++;
          }
        }
        return num;
      },
      privateToolNum: function () {
        let tempTools = this.personalTools;
        let num = 0;
        for (let i=0; i<tempTools.length; i++){
          if (tempTools[i].privacy == "Private"){
            num ++;
          }
        }
        return num;
      }
    },
  }
</script>

<style scoped>
  .badge {
    display: inline-block;
    min-width: 10px;
    padding: 3px 7px;
    font-size: 12px;
    font-weight: bold;
    line-height: 1;
    color: #ffffff;
    text-align: center;
    white-space: nowrap;
    vertical-align: baseline;
    background-color: #999999;
    border-radius: 10px;
  }

  .icon {
    cursor: pointer;
  }

  .icon:hover {
    color: #2d8cf0;
  }
</style>
