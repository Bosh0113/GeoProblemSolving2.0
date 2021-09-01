<template>
  <div>
    <div id="title">
      <h1 style="text-align: center; margin-top: 10px">Tool</h1>
      <h3 style="text-align: center; margin-bottom: 10px">
        you can manage your tools here
      </h3>
    </div>
    <Row>
      <Col span="22" offset="1">
        <!--  Filter 包括 Created by me, Shared with me(项目中内容)，public, privacy-->
        <!--  下移  -->
        <!--  具体显示区，图标加 缩略图 + 名字的显示方案; 先把created tool 拉过来；添加 shared 接口-->
        <Row>
          <Card dis-hover style="position: relative" class="customCard">
            <p slot="title">
              <CheckboxGroup v-model="checkedType" style="margin-left: 1%">
                <Checkbox label="public">
                  <label>Public</label>
                  <label class="badge">{{ publicTools.length }}</label>
                </Checkbox>
                <Checkbox label="private">
                  <label>Privacy</label>
                  <label class="badge">{{ privateTools.length }}</label>
                </Checkbox>
              </CheckboxGroup>
            </p>
            <!--              添加一个模态显示判断 -->
            <Button
              slot="extra"
              type="primary"
              icon="md-add"
              @click="createToolModal = true"
              style="
                display: inline-block;
                position: absolute;
                right: 20px;
                top: -4px;
              "
              >Create tool
            </Button>

            <!--            工具显示内容  -->
            <div
              :style="{ height: contentHeight - 180 + 'px' }"
              style="height: inherit; min-height: fit-content"
            >
              <vue-scroll :ops="ops">
                <Card
                  :bordered="false"
                  v-if="userToolCount.length == 0"
                  dis-hover
                >
                  <div style="display: flex; justify-content: center">
                    <Icon type="md-alert" size="40" color="gray" />
                  </div>
                  <br />
                  <div style="display: flex; justify-content: center">
                    <h3 style="text-align: center; width: 80%">
                      Sorry, you don't have any tools.
                    </h3>
                  </div>
                </Card>

                <div v-if="useToolCSS">
                  <Row>
                    <!--        每一排 1 个-->
                    <Col
                      span="24"
                      v-for="(tool, index) in userToolCount"
                      :key="index"
                    >
                      <div style="margin: 0 5px 15px 5px">
                        <Card class="toolCard">
                          <div slot="title" class="toolCardHead">
                            <p
                              class="ellipsis"
                              style="
                                width: 50%;
                                display: inline-block;
                                font-size: 16px;
                              "
                              :title="tool.toolName"
                            >
                              {{ tool.toolName
                              }}<Icon
                                v-if="tool.scope == 'outer'"
                                type="ios-paper-plane"
                                title="External tool"
                                color="orange"
                                style="margin-left: 5px"
                              />
                            </p>
                          </div>
                          <div slot="extra">
                            <Icon
                              type="ios-settings"
                              title="Edit"
                              size="25"
                              class="icon"
                              @click="editTool(tool)"
                              style="margin-right: 3px"
                            />
                            <Icon
                              type="ios-share-alt"
                              title="Share"
                              size="25"
                              class="icon"
                              @click="shareTool(tool)"
                              style="margin-right: 3px"
                            />
                            <Icon
                              type="md-close"
                              size="25"
                              class="icon"
                              title="Delete"
                              @click="
                                delTool(
                                  tool.tid,
                                  tool.privacy,
                                  index,
                                  tool.toolName
                                )
                              "
                            />
                          </div>
                          <!--                       内容    -->
                          <div style="cursor: pointer; position: relative">
                            <div style="position: absolute; margin-top: 5px">
                              <img
                                :src="tool.toolImg"
                                v-if="
                                  tool.toolImg != '' && tool.toolImg != null
                                "
                                style="
                                  height: 70px;
                                  width: 70px;
                                  border-radius: 50%;
                                "
                              />
                              <avatar
                                :username="tool.toolName"
                                :size="70"
                                style="margin-bottom: 6px"
                                v-else
                              ></avatar>
                            </div>
                            <div
                              style="
                                margin-left: 10%;
                                width: 85%;
                                display: flex;
                                align-items: center;
                                height: 80px;
                              "
                            >
                              <p class="toolDes">{{ tool.description }}</p>
                            </div>
                          </div>
                        </Card>
                        <Card dis-hover class="toolCardFoot">
                          <div class="toolTags">
                            <div
                              style="position: absolute; top: -5px; left: -10px"
                            >
                              <span class="toolTypeSpan">
                                {{ tool.backendType }}
                              </span>
                              <span
                                v-for="(tag, index) in tool.tags"
                                :key="tag"
                                class="toolTagSpan"
                              >
                                {{ tool.tags[index] }}
                              </span>
                            </div>
                          </div>
                        </Card>
                      </div>
                    </Col>
                  </Row>
                </div>

                <!-- v-else -->
                <div v-else>
                  <Row>
                    <!--        每一排 1 个-->
                    <Col
                      span="24"
                      v-for="(tool, index) in userToolCount"
                      :key="index"
                    >
                      <div style="margin: 0 5px 15px 5px">
                        <Card class="toolCard">
                          <p
                            slot="title"
                            class="ellipsis"
                            style="width: 50%; display: inline-block"
                            :title="tool.toolName"
                          >
                            {{ tool.toolName }}
                          </p>
                          <div slot="extra">
                            <Icon
                              type="ios-settings"
                              title="Edit"
                              size="25"
                              class="icon"
                              @click="editTool(tool)"
                              style="margin-right: 3px"
                            />
                            <Icon
                              type="ios-share-alt"
                              title="Share"
                              size="25"
                              class="icon"
                              @click="shareTool(tool)"
                              style="margin-right: 3px"
                            />
                            <Icon
                              type="md-close"
                              size="25"
                              class="icon"
                              title="Delete"
                              @click="
                                delTool(
                                  tool.tid,
                                  tool.privacy,
                                  index,
                                  tool.toolName
                                )
                              "
                            />
                          </div>
                          <!--                       内容    -->
                          <div
                            @click="toToolPage(tool)"
                            style="cursor: pointer; position: relative"
                          >
                            <div style="position: absolute; margin-top: 5px">
                              <img
                                :src="tool.toolImg"
                                v-if="
                                  tool.toolImg != '' && tool.toolImg != null
                                "
                                style="
                                  height: 70px;
                                  width: 70px;
                                  border-radius: 50%;
                                "
                              />
                              <avatar
                                :username="tool.toolName"
                                :size="70"
                                style="margin-bottom: 6px"
                                v-else
                              ></avatar>
                            </div>
                            <div
                              style="
                                margin-left: 80px;
                                width: 90%;
                                display: flex;
                                align-items: center;
                                height: 80px;
                              "
                            >
                              <p class="toolDes">{{ tool.description }}</p>
                            </div>
                          </div>
                        </Card>
                        <Card dis-hover class="toolCardFoot">
                          <div class="toolTags">
                            <div
                              style="position: absolute; top: -5px; left: -10px"
                            >
                              <span class="toolTypeSpan">
                                {{ tool.backendType }}
                              </span>
                              <span
                                v-for="(tag, index) in tool.tags"
                                :key="index"
                                class="toolTagSpan"
                              >
                                {{ tool.tags[index] }}
                              </span>
                            </div>
                          </div>
                        </Card>
                      </div>
                    </Col>
                  </Row>
                </div>
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
          :loading="loading"
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
            </Option>
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
            </Option>
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
        </Button>
        <Button type="primary" @click="nextStep" v-show="this.currentStep == 0"
          >Next
        </Button>
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
import { del, post } from "../../../axios";
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
      useToolCSS: true,
      personalTools: [],
      publicTools: [],
      privateTools: [],
      userProject: [],
      ops: {
        bar: {
          background: "#808695",
        },
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
      loading: false,
    };
  },
  created() {
    this.getUserProjects();
  },
  mounted() {
    this.getPersonalTools();
    this.resizeContent();
    this.reSize();
    window.addEventListener("resize", this.reSize);
  },
  beforeDestroy: function () {
    window.removeEventListener("resize", this.reSize);
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
    reSize() {
      // if (window.innerHeight > 675) {
      //   this.contentHeight = window.innerHeight - 120 + "px";
      // } else {
      //   this.contentHeight = 675 - 120 + "px";
      // }
      if (window.innerWidth < 950) {
        this.useToolCSS = false;
      } else {
        this.useToolCSS = true;
      }
    },

    //？？？？？why getUserProjects
    getUserProjects: function () {
      let projectIds = "";
      let userInfo = this.$store.getters.userInfo;
      if (userInfo.createdProjects != null) {
        for (let i = 0; i < userInfo.createdProjects.length; i++) {
          if (userInfo.joinedProjects != null) {
            projectIds += userInfo.createdProjects[i] + ",";
          } else if (userInfo.joinedProjects == null) {
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
      console.log(this.editToolInfo);
    },
    getPersonalTools: function () {
      // "/GeoProblemSolving/tool/findByProvider/" + this.$store.getters.userId
      this.$axios
        .get("/GeoProblemSolving/tool/provider/" + this.$store.getters.userId)
        .then((res) => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data.code == 0) {
            let tempTools = res.data.data;
            for (let i = 0; i < tempTools.length; i++) {
              if (tempTools[i].privacy == "Public") {
                this.publicTools.push(tempTools[i]);
              } else {
                this.privateTools.push(tempTools[i]);
              }
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
      // console.log(createToolForm);
      this.loading = true;
      setTimeout(() => {
        this.loading = false;
      }, 2000);
      createToolForm["creator"] = this.$store.getters.userId;
      // let data = await post("/GeoProblemSolving/tool", createToolForm);
      this.axios
        .post("/GeoProblemSolving/tool", createToolForm)
        .then((res) => {
          // ？？？ 判断返回值，进行下一步操作
          if (res.data.code == 0) {
            this.createToolModal = false;
            this.editToolInfo = {};
            this.toolInfo = {};
            let toolData = res.data.data;
            console.log(res);
            console.log(toolData);
            if (toolData != null) {
              if (toolData.privacy == "Public") {
                this.publicTools.push(toolData);
              } else if (toolData.privacy == "Private") {
                this.privateTools.push(toolData);
              }
              this.currentStep = 0;
              this.$Notice.success({ title: "Create successfully" });
            } else {
              this.$Notice.error({
                title: "Crate failed",
                desc: "No Corresponding service",
              });
            }
          }
        })
        .catch((err) => {
          this.$Notice.error({ title: "Create failed" });
        });
      // //Notice 与 Message 的区别
      // this.toolInfo = {};
      // console.log("data", data)
      // if (data != null){
      //   if (data.privacy == "Public") {
      //     this.privateTools.push(data);
      //   } else if (data.privacy == "Private") {
      //     this.publicTools.push(data);
      //   }
      //   this.currentStep = 0;
      //   this.$Notice.success({title: "Create successfully"});
      // }else {
      // }
    },
    delTool: function (toolId, toolType, index, toolName) {
      this.confirmDelModal = true;
      this.delToolInfo.delToolId = toolId;
      this.delToolInfo.delToolIndex = index;
      this.delToolInfo.delToolType = toolType;
      this.delToolInfo.delToolName = toolName;
    },
    confirmDelTool: function () {
      // "/GeoProblemSolving/tool/delete?tid=" + this.delToolInfo.delToolId
      this.$axios
        .delete("/GeoProblemSolving/tool/" + this.delToolInfo.delToolId)
        .then((res) => {
          if (res.data.code == 0) {
            if (this.checkedType.length == 2) {
              if (this.delToolInfo.delToolType == "Public") {
                // console.log("publicDel", this.delToolInfo.delToolIndex);
                this.publicTools.splice(this.delToolInfo.delToolIndex, 1);
              } else if (this.delToolInfo.delToolType == "Private") {
                console.log("privateDel", this.delToolInfo.delToolIndex);
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
            this.$Notice.success({ title: "Delete Success." });
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
      // "/GeoProblemSolving/tool/update/" + this.editToolInfo.tid,
      //   this.editToolInfo
      this.$axios
        .put("/GeoProblemSolving/tool", this.editToolInfo)
        .then((res) => {
          this.$Notice.success({ title: "Update successfully." });
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
            this.$Notice.success({ desc: "Sharing successfully." });
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
.customCard {
  opacity: 0.95;
}

.toolCard {
  background-color: white;
  border-radius: 3px;
  border: 1px solid #dadce0;
  border-bottom: transparent;
  box-shadow: 0 0.6px 1.8px 0 rgb(0 0 0 / 11%);
  height: 160px;
}

/* .toolCardHead{
    height: 45px;
    width: 100%;
    border-bottom-style: none;
    background-image: linear-gradient(to right, rgb(253, 249, 244) 0%, rgb(216, 244, 249) 100%);
  } */

.toolCardFoot {
  height: 40px;
  background-color: rgba(232, 232, 232, 0.5);
  border-radius: 3px;
  border: 1px solid #dadce0;
  border-top: transparent;
}

.toolTags {
  position: relative;
}

.toolTypeSpan {
  background-color: #2db7f5;
  color: white;
  font-size: 15px;
  font-weight: 700;
  margin-left: 10px;
  line-height: 1;
  padding: 3px 7px;
  white-space: nowrap;
  vertical-align: baseline;
  border-radius: 10px;
}

.toolTagSpan {
  background-color: #95a0ba;
  color: white;
  font-size: 15px;
  font-weight: 700;
  margin-left: 10px;
  line-height: 1;
  padding: 3px 7px;
  white-space: nowrap;
  vertical-align: baseline;
  border-radius: 10px;
}

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
  /* height: 100px; */
  /* line-height: 20px; */
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 5;
  -webkit-box-orient: vertical;
}
</style>
