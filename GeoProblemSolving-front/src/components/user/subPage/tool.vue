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
                <label class="badge">{{ publicTools.length }}</label>
              </Checkbox>
              <Checkbox label="private">
                <label>Privacy</label>
                <label class="badge">{{ privateTools.length }}</label>
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
                @click="createToolModal = true"
              >Create tool
              </Button>
            </div>
            <!--            工具显示内容  -->
            <div
              :style="{height: contentHeight-230+'px'}"
              style="height: inherit;min-height: fit-content;"
            >
              <vue-scroll :ops="ops">
                <Row>
<!--                  每一排 4 个-->
                  <Col
                    span="6"
                    v-for="(tool,index) in userToolCount"
                    :key="index"
                  >
                    <div style="margin:0 5px 15px 5px">
                      <Card>
                        <p
                          slot="title"
                          class="ellipsis"
                          style="width:50%;display:inline-block;"
                          :title="tool.toolName"
                        >{{tool.toolName}}</p>
                        <div slot="extra">
                          <Icon
                            type="ios-settings"
                            title="Edit"
                            size="20"
                            class="icon"
                            @click="editTool(tool)"
                            style="margin-right: 3px"
                          />
                          <Icon
                            type="ios-share-alt"
                            title="Share"
                            size="20"
                            class="icon"
                            @click="shareTool(tool)"
                            style="margin-right: 3px"
                          />
                          <Icon
                            type="md-close"
                            size="20"
                            class="icon"
                            title="Delete"
                            @click="delTool(tool.tid, tool.privacy, index, tool.toolName)"
                          />
                        </div>
<!--                       内容    -->
                        <div @click="toToolPage(tool.toolUrl)" style="cursor: pointer;">
                          <div style="algin:left;">
                            <img
                              :src="tool.toolImg"
                              v-if="tool.toolImg!=''"
                              style="height:100%;max-height:50px;width: 50%"
                            />
                            <avatar
                              :username="tool.toolName"
                              :size="50"
                              style="margin-bottom:6px;"
                              v-else
                            ></avatar>
                          </div>

                          <div
                            style="display: inline-block;vertical-align: top;height: 100%"
                          >
                            <p class="toolDes">{{tool.description}}</p>
                          </div>
                        </div>
                      </Card>
                    </div>
                  </Col>
                </Row>
              </vue-scroll>
            </div>
          </Card>
        </Row>
      </Col>
    </Row>
    <!--    Modal 对话框部分 -->
    <Modal v-model="createToolModal" title="Create tool" width="800">
      <template-general
        @generalInfo="getGeneralInfo"
        :step="currentStep"
      ></template-general>
      <div slot="footer">
        <Button
          @click="previousStep"
          v-show="this.currentStep == 1"
          type="warning"
        >Previous
        </Button>
        <Button v-show="this.currentStep == 0" @click="nextStep" type="primary"
        >Next
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
            <Option value="selectProject"
            >Share "{{ this.sharedTool.toolName }}" to Project
            </Option
            >
<!--            <Option value="selectUser"-->
<!--            >Share "{{ this.sharedTool.toolName }}" to User-->
<!--            </Option-->
<!--            >-->
          </Select>
        </FormItem>
        <FormItem v-if="formItem.select == 'selectProject'">
          <Select
            v-model="formItem.sharedProjectId"
            placeholder="Select Project"
          >
            <Option
              v-for="(item, index) in userProject"
              :value="item.aid"
              :key="index"
            >{{ item.name }}
            </Option
            >
          </Select>
        </FormItem>
        <FormItem v-if="formItem.select == 'selectUser'">
          <Input
            v-model="formItem.sharedUserEmail"
            placeholder="Enter email address"
          ></Input>
        </FormItem>
      </Form>

      <div slot="footer">
        <Button type="warning" @click="shareToolModal = false">Cancel</Button>
        <Button type="success" @click="sharingTool">Share</Button>
      </div>
    </Modal>

    <Modal
      v-model="editToolModal"
      title="Edit tool"
      width="800"
      :mask-closable="false"
    >
      <template-edit
        @generalInfo="getEditInfo"
        :step="currentStep"
        :selectTool="editToolInfo"
      ></template-edit>
      <div slot="footer">
        <Button
          type="warning"
          @click="previousStep"
          v-show="this.currentStep == 1"
        >Previous
        </Button
        >
        <Button type="primary" @click="nextStep" v-show="this.currentStep == 0"
        >Next
        </Button
        >
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
      <h2>{{ this.delToolInfo.delToolName }}</h2>
      <div slot="footer">
        <Button type="warning" @click="confirmDelModal = false">Cancel</Button>
        <Button type="success" @click="confirmDelTool">Ok</Button>
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
      TemplateEdit,
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
        ops: {
          bar: {
            background: "#808695"
          }
        },
        formItem: {
          select: "selectProject",
          sharedUserEmail: "",
          sharedProjectId: "",
        },
        toolInfo: {
          toolName: "",
          modelInfo: {
            stateId: "",
            oid: "",
            mdlId: "",
          },
          description: "",
          toolUrl: "",
          recomStep: [],
          categoryTag: [],
          toolImg: "",
          privacy: "Private",
          detail: "",
        },
        editToolInfo: {},
        toolInfoRule: {},
        // create tool step
        currentStep: 0,
        delToolInfo: {
          delToolId: "",
          delToolIndex: undefined,
          delToolType: "",
          delToolName: "",
        },
        sharedTool: {},
        contentHeight: "",
      };
    },
    created() {
      this.getUserProjects();
    },
    mounted() {
      this.getPersonalTools();
      this.resizeContent();
    },
    methods: {
      resizeContent() {
        if (window.innerHeight > 675) {
          this.contentHeight = window.innerHeight - 120;
        } else {
          this.contentHeight = 555;
        }
        window.onresize = () => {
          return (() => {
            this.resizeContent();
          })();
        };
      },
      getUserProjects: function () {
        let projectIds = "";
        let userInfo = this.$store.getters.userInfo;
        if (userInfo.createdProjects != null) {
          for (let i = 0; i < userInfo.createdProjects.length; i++) {
            if (userInfo.joinedProjects != null){
              projectIds += userInfo.createdProjects[i] + ",";
            }else if (userInfo.joinedProjects == null){
              if (i != userInfo.createdProjects.length - 1) {
                projectIds += userInfo.createdProjects[i] + ",";
              } else {
                projectIds += userInfo.createdProjects[i];
              }
            }
          }
        }
        if (userInfo.joinedProjects != null) {
          for (let j = 0; j < userInfo.joinedProjects.length; j++) {
            if (j != userInfo.joinedProjects.length - 1) {
              projectIds += userInfo.joinedProjects[j] + ",";
            } else {
              projectIds += userInfo.joinedProjects[j];
            }
          }
        }
        this.$axios
          .get("/GeoProblemSolving/project/getProjects?aids=" + projectIds)
          .then((res) => {
            this.$set(this, "userProject", res.data.data);
          })
          .catch((err) => {
            this.$Message.error("Loading project failed.");
          });
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
      },
      getPersonalTools: function () {
        this.$axios
          .get(
            "/GeoProblemSolving/tool/findByProvider/" + this.$store.getters.userId
          )
          .then((res) => {
            let tempTools = res.data.data;
            console.log(tempTools)
            for (let i = 0; i < tempTools.length; i++) {
              if (tempTools[i].privacy == "Public") {
                this.publicTools.push(tempTools[i]);
              } else {
                this.privateTools.push(tempTools[i]);
              }
            }
          })
          .catch((err) => {
            this.$Message.error("Loading Fail.");
          });
      },
      nextStep: function () {
        if (this.currentStep == 0) {
          this.currentStep = 1;
        }
      },
      previousStep: function () {
        if (this.currentStep == 1) {
          this.currentStep = 0;
        }
      },
      createTool: async function () {
        let createToolForm = this.toolInfo;
        createToolForm["provider"] = this.$store.getters.userId;
        let data = await post("/GeoProblemSolving/tool/create", createToolForm);
        // 判断返回值，进行下一步操作
        this.createToolModal = false;
        this.editToolInfo = {};
        //Notice 与 Message 的区别
        this.toolInfo = {};
        if (data.privacy == "Public") {
          this.privateTools.push(data);
        } else if (data.privacy == "Private") {
          this.publicTools.push(data);
        }
        this.currentStep = 0;
        this.$Notice.success({title: "Create successfully"});
      },
      delTool: function (toolId, toolType, index, toolName) {
        this.confirmDelModal = true;
        this.delToolInfo.delToolId = toolId;
        this.delToolInfo.delToolIndex = index;
        this.delToolInfo.delToolType = toolType;
        this.delToolInfo.delToolName = toolName;
      },
      confirmDelTool: function () {
        this.$axios
          .delete(
            "/GeoProblemSolving/tool/delete?tid=" + this.delToolInfo.delToolId
          )
          .then((res) => {
            if (res.data.code == 0) {
              if (this.checkedType.length == 2) {
                if (this.delToolInfo.delToolType == "Public") {
                  this.publicTools.splice(this.delToolInfo.delToolIndex, 1);
                } else if (this.delToolInfo.delToolType == "Private") {
                  let num =
                    this.delToolInfo.delToolIndex - this.publicTools.length;
                  this.privateTools.splice(num, 1);
                }
              } else if (
                this.checkedType.length == 1 &&
                this.checkedType == "public"
              ) {
                this.publicTools.splice(this.delToolInfo.delToolIndex, 1);
              } else if (
                this.checkedType.length == 1 &&
                this.checkedType == "private"
              ) {
                this.privateTools.splice(this.delToolInfo.delToolIndex, 1);
              }
              this.$Notice.success({title: "Delete Success."});
              this.confirmDelModal = false;
            } else {
              this.$Message.error("Delete Fail! Try again.");
            }
          })
          .catch((err) => {
            this.$Message.error("Delete Fail.");
          });
      },
      commitEdit: function () {
        this.$axios
          .post(
            "/GeoProblemSolving/tool/update/" + this.editToolInfo.tid,
            this.editToolInfo
          )
          .then((res) => {
            this.$Notice.success({title: "Update successfully."});
            this.currentStep = 0;
            this.editToolInfo = {};
            this.editToolModal = false;
          })
          .catch((err) => {
            this.$Message.error("Update Fail.Try again!");
          });
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
        } else {
          updateProject.toolList = [this.sharedTool.tid];
        }
        this.$axios
          .put(
            "/GeoProblemSolving/project/" + this.formItem.sharedProjectId,
            updateProject
          )
          .then((res) => {
            if (res.data.code == 0) {
              this.$Notice.success({desc: "Sharing successfully."});
              this.shareToolModal = false;

              this.userProject[index].toolList = updateProject.toolList;
            } else {
              this.$Message.error("Sharing Failed.");
            }
          })
          .catch((err) => {
            this.$Message.error("Update Fail.Try again!");
          });
      },
      toToolPage: function (toolUrl) {
        window.open(toolUrl);
      }
    },
    computed: {
      userToolCount: function () {
        if (this.checkedType.length == 0) {
          this.personalTools = [];
          return this.personalTools;
        } else if (this.checkedType.length == 2) {
          this.personalTools = [];
          this.personalTools = this.publicTools.concat(this.privateTools);
          return this.personalTools;
        } else if (
          this.checkedType.length == 1 &&
          this.checkedType[0] == "public"
        ) {
          this.personalTools = [];
          this.personalTools = this.publicTools;
          return this.personalTools;
        } else if (
          this.checkedType.length == 1 &&
          this.checkedType[0] == "private"
        ) {
          this.personalTools = [];
          this.personalTools = this.privateTools;
          return this.personalTools;
        }
      },
    },
  };
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
  .ellipsis {
    display: inline-block;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    vertical-align: top;
  }
  .toolDes {
    height: 100px;
    line-height: 20px;
    overflow : hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 5;
    -webkit-box-orient: vertical;
  }
</style>
