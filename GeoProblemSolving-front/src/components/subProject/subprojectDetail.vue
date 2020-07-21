<style scoped>
.sidebar {
  font-weight: bold;
  width: 15%;
}
.right {
  width: 90%;
  margin-left: 5%;
  height: 100%;
}
.createTaskBtn {
  font-size: 10px;
}
.createTaskBtn:hover {
  color: white;
  background: #47cb89;
}
.title {
  height: 40px;
  line-height: 40px;
  text-align: center;
  font-size: 20px;
  font-weight: bold;
  border-bottom: 1px solid lightgray;
}
.member-desc {
  height: 50px;
  margin: 0 20px 0 10px;
  display: flex;
}
.member-image {
  width: 50px;
  height: 50px;
  padding: 5px;
}
.memebr-work {
  width: 70%;
  height: 50px;
}
.subProjectDesc {
  text-indent: 2em;
  padding: 10px 0;
  min-height: 250px;
  word-break: break-all;
  word-wrap: break-word;
  white-space: pre-line;
}
.operatePanel button {
  margin-right: 2.5%;
}
.memberOrganization {
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 100%;
}
.Information {
  margin-bottom: 10px;
}
</style>
<template>
  <div>
    <Row>
      <Col :xs="14" :sm="15" :md="16" :lg="17" style="margin:30px 0 0 80px">
        <div>
          <vue-scroll :ops="scrollOps" :style="{height:sidebarHeight+150+'px'}">
            <Card class="Information">
              <div style="width:100%">
                <strong>Subproject name:</strong>
                <template v-if="permissionIdentity('subproject_edit_info')">
                  <Icon
                    v-if="!edit0"
                    type="ios-create"
                    :size="18"
                    style="float:right;cursor:pointer"
                    title="Edit"
                    @click="editTitle"
                  />
                  <Icon
                    v-else
                    type="md-checkbox-outline"
                    :size="18"
                    style="float:right;cursor:pointer"
                    title="Complete"
                    @click="editTitle"
                  />
                </template>
              </div>
              <Divider style="margin:10px 0; background:lightblue" />
              <div v-if="!edit0" style="overflow-y:auto; height:30px">{{title}}</div>
              <template v-else>
                <Input v-model="title" type="text" :rows="1" placeholder="Enter something..." />
              </template>
            </Card>
            <Card class="Information">
              <div style="width:100%">
                <strong>Description:</strong>
                <template v-if="permissionIdentity('subproject_edit_info')">
                  <Icon
                    v-if="!edit1"
                    type="ios-create"
                    :size="18"
                    style="float:right;cursor:pointer"
                    title="Edit"
                    @click="editDescription"
                  />
                  <Icon
                    v-else
                    type="md-checkbox-outline"
                    :size="18"
                    style="float:right;cursor:pointer"
                    title="Complete"
                    @click="editDescription"
                  />
                </template>
              </div>
              <Divider style="margin:10px 0; background:lightblue" />
              <div v-if="!edit1" class="subProjectDesc" style="overflow-y:auto">{{description}}</div>
              <template v-else>
                <Input
                  v-model="description"
                  type="textarea"
                  :rows="12"
                  placeholder="Enter something..."
                />
              </template>
            </Card>
            <Card class="Information">
              <div style="width:100%">
                <strong>Background:</strong>
                <template v-if="permissionIdentity('subproject_edit_info')">
                  <Icon
                    v-if="!edit2"
                    type="ios-create"
                    :size="18"
                    style="float:right;cursor:pointer"
                    title="Edit"
                    @click="editBackground"
                  />
                  <Icon
                    v-else
                    type="md-checkbox-outline"
                    :size="18"
                    style="float:right;cursor:pointer"
                    title="Complete"
                    @click="editBackground"
                  />
                </template>
              </div>
              <Divider style="margin:10px 0; background:lightblue" />
              <div v-if="!edit2" class="subProjectDesc" style="overflow-y:auto">{{background}}</div>
              <template v-else>
                <Input
                  v-model="background"
                  type="textarea"
                  :rows="12"
                  placeholder="Enter something..."
                />
              </template>
            </Card>
            <Card class="Information">
              <div style="width:100%">
                <strong>Limitations / problems:</strong>
                <template v-if="permissionIdentity('subproject_edit_info')">
                  <Icon
                    v-if="!edit3"
                    type="ios-create"
                    :size="18"
                    style="float:right;cursor:pointer"
                    title="Edit"
                    @click="editLimitation"
                  />
                  <Icon
                    v-else
                    type="md-checkbox-outline"
                    :size="18"
                    style="float:right;cursor:pointer"
                    title="Complete"
                    @click="editLimitation"
                  />
                </template>
              </div>
              <Divider style="margin:10px 0; background:lightblue" />
              <div v-if="!edit3" class="subProjectDesc" style="overflow-y:auto">{{limitation}}</div>
              <template v-else>
                <Input
                  v-model="limitation"
                  type="textarea"
                  :rows="5"
                  placeholder="Enter something..."
                />
              </template>
            </Card>
          </vue-scroll>
        </div>
      </Col>
      <Col
        :xs="{span:8}"
        :sm="{span:7}"
        :md="{span:6}"
        :lg="{span:5}"
        v-bind="this.participants"
        style="margin:30px 0 0 20px;"
      >
        <Card>
          <div slot="title" style="font-size:16px">
            <strong>Participants</strong>
          </div>
          <div slot="extra">
            <Icon
              v-if="permissionIdentity('subproject_invite_member')"
              type="md-add"
              :size="18"
              style="cursor:pointer"
              title="Invite other participants"
              @click="inviteMembersModalShow"
            />
            <Icon
              v-if="userRole == 'Member' || userRole == 'PManager'"
              type="ios-log-out"
              :size="18"
              style="cursor:pointer"
              title="Quit this subproject"
              @click="quitModal=true"
            />
          </div>
          <div :style="{height:sidebarHeight+60+'px'}" style="overflow-y:auto;overflow-x:hidden">
            <vue-scroll :ops="scrollOps" :style="{height:sidebarHeight + 60 + 'px'}">
              <div class="member-desc" v-for="(member,index) in participants" :key="member.index">
                <template v-if="index==0">
                  <Badge text="♔" type="warning" class="userAvatar">
                    <div
                      class="member-image"
                      @click="gotoPersonalSpace(member.userId)"
                      style="cursor:pointer;display:flex;justify-content:center;align-ittems:center"
                    >
                      <img
                        v-if="member.avatar != '' && member.avatar!='undefined' && member.avatar!='null'"
                        :src="member.avatar"
                        style="width:100%;height:100%"
                      />
                      <avatar
                        :username="member.userName"
                        :size="50"
                        style="width:100%;height:100%"
                        :title="member.userName"
                        v-else
                      ></avatar>
                    </div>
                  </Badge>
                  <div class="memebr-work" style="display:flex;align-items:center">
                    <div style="height:100%;width:100%">
                      <div style="margin:5px 0 0 15px;">
                        <span :title="member.userName">{{member.userName}}</span>
                      </div>
                      <div style="margin:5px 0 0 15px;">
                        <span
                          class="memberOrganization"
                          :title="member.organization"
                        >{{member.organization}}</span>
                      </div>
                    </div>
                  </div>
                </template>
                <template v-else>
                  <div
                    class="member-image"
                    @click="gotoPersonalSpace(member.userId)"
                    style="cursor:pointer;display:flex;justify-content:center;align-items:center"
                  >
                    <img
                      v-if="member.avatar != '' && member.avatar!='undefined' && member.avatar!='null'"
                      :src="member.avatar"
                      style="width:100%;height:100%"
                    />
                    <avatar
                      :username="member.userName"
                      :size="50"
                      style="width:100%;height:100%"
                      :title="member.userName"
                      v-else
                    ></avatar>
                  </div>
                  <div class="memebr-work" style="display:flex;align-items:center">
                    <div style="height:100%;width:100%">
                      <div style="margin:5px 0 0 15px;">
                        <span :title="member.userName">{{member.userName}}</span>
                      </div>
                      <div style="margin:5px 0 0 15px;">
                        <span
                          class="memberOrganization"
                          :title="member.organization"
                        >{{member.organization}}</span>
                      </div>
                    </div>
                  </div>
                </template>
                <div
                  style="line-height:50px"
                  type="default"
                  v-if="permissionIdentity('subproject_remove_member')"
                >
                  <span
                    style="cursor:pointer"
                    title="Remove this member"
                    v-show="giveDeleteProperty(index)"
                    @click="removeMemberAlert=true"
                  >
                    <Icon type="md-log-out" :size="20" />
                  </span>
                </div>
                <Modal
                  v-model="removeMemberAlert"
                  title="Remove member alert"
                  ok-text="Assure"
                  cancel-text="Cancel"
                  @on-ok="removeMember(participants[index].userId,participants[index].userName)"
                >
                  <h5
                    style="text-align:center;color:red"
                  >Do you really want to remove this member from this subproject?</h5>
                </Modal>
              </div>
            </vue-scroll>
          </div>
          <Modal
            v-model="quitModal"
            width="400px"
            title="Quit the subproject"
            @on-ok="quitSubProject()"
            ok-text="Ok"
            cancel-text="Cancel"
          >
            <h4 style="color:red">Are you sure to quit this subproject?</h4>
          </Modal>
          <Modal
            v-model="inviteModal"
            width="400px"
            title="Invite new participants"
            @on-ok="inviteMembers"
            ok-text="Ok"
            cancel-text="Cancel"
          >
            <div>
              <p>Members:</p>
              <Tag
                v-for="participant in this.participants"
                :key="participant.index"
              >{{participant.userName}}</Tag>
              <p>Candidates:</p>
              <CheckboxGroup v-model="inviteList">
                <Checkbox
                  v-for="candidate in candidates"
                  :key="candidate.index"
                  :label="candidate.userId"
                >
                  <span>{{candidate.userName}}</span>
                </Checkbox>
              </CheckboxGroup>
            </div>
          </Modal>
        </Card>
      </Col>
    </Row>
  </div>
</template>
<script>
import Avatar from "vue-avatar";
import folderTree from "../resources/folderTree.vue";
export default {
  updated() {
    $(".userAvatar sup").css("margin", "10px 10px 0 0");
  },
  components: {
    Avatar,
    folderTree
  },
  props: ["subProjectInfo", "userRole", "projectInfo"],
  data() {
    return {
      scrollOps: {
        bar: {
          background: "lightgrey"
        }
      },
      //登陆者身份
      // 关于邀请的模态框
      inviteModal: false,
      quitModal: false,
      sidebarHeight: 100,
      participants: [],
      candidates: [],
      inviteList: [],
      inviteAble: true,
      // 删除成员的提醒
      removeMemberAlert: false,
      // 编辑子项目信息
      edit0: false,
      edit1: false,
      edit2: false,
      edit3: false,
      title: this.subProjectInfo.title,
      background: "",
      limitation: "",
      description: "",
      oldTitle: this.subProjectInfo.title,
      oldBackground: "",
      oldLimitation: "",
      oldDescription: ""
    };
  },
  created() {
    this.init();
  },
  mounted() {
    window.addEventListener("resize", this.initSize);
  },

  beforeRouteLeave(to, from, next) {
    var editStart = false;

    if (this.oldTitle != this.title) {
      editStart = true;
    }
    if (this.oldLimitation != this.limitation) {
      editStart = true;
    }
    if (this.oldBackground != this.background) {
      editStart = true;
    }
    if (this.oldDescription != this.description) {
      editStart = true;
    }

    if (!editStart || !(this.edit0 || this.edit1 || this.edit2 || this.edit3)) {
      next();
    } else {
      this.$Notice.info({
        desc: "The content that you edited has not been submitted!"
      });
    }
  },
  beforeDestroy: function() {
    window.removeEventListener("resize", this.initSize);
  },
  methods: {
    initSize() {
      //侧边栏的高度随着屏幕的高度自适应
      this.sidebarHeight = window.innerHeight - 380;
    },
    //初始化函数，作用是控制侧边栏的高度，设置右边通知栏弹出时候的距顶高度以及延迟的时间
    init() {
      this.initSize();
      this.getSubprojectDes();
      this.showMembers();
    },
    permissionIdentity(operation) {
      if (
        this.projectInfo.permissionManager != undefined &&
        operation === "subproject_edit_info"
      ) {
        if (
          this.userRole == "PManager" &&
          this.projectInfo.permissionManager.subproject_edit_info
            .project_manager
        ) {
          return true;
        } else if (
          this.userRole == "Manager" &&
          this.projectInfo.permissionManager.subproject_edit_info
            .subproject_manager
        ) {
          return true;
        } else if (
          this.userRole == "Member" &&
          this.projectInfo.permissionManager.subproject_edit_info.member
        ) {
          return true;
        }
      } else if (
        this.projectInfo.permissionManager != undefined &&
        operation === "subproject_invite_member"
      ) {
        if (
          this.userRole == "PManager" &&
          this.projectInfo.permissionManager.subproject_invite_member
            .project_manager
        ) {
          return true;
        } else if (
          this.userRole == "Manager" &&
          this.projectInfo.permissionManager.subproject_invite_member
            .subproject_manager
        ) {
          return true;
        } else if (
          this.userRole == "Member" &&
          this.projectInfo.permissionManager.subproject_invite_member.member
        ) {
          return true;
        }
      } else if (
        this.projectInfo.permissionManager != undefined &&
        operation === "subproject_remove_member"
      ) {
        if (
          this.userRole == "PManager" &&
          this.projectInfo.permissionManager.subproject_remove_member
            .project_manager
        ) {
          return true;
        } else if (
          this.userRole == "Manager" &&
          this.projectInfo.permissionManager.subproject_remove_member
            .subproject_manager
        ) {
          return true;
        } else if (
          this.userRole == "Member" &&
          this.projectInfo.permissionManager.subproject_remove_member.member
        ) {
          return true;
        }
      }
    },
    getSubprojectDes() {
      if (
        this.subProjectInfo.description != undefined &&
        this.subProjectInfo.description != null
      ) {
        this.description = this.subProjectInfo.description;
        this.oldDescription = this.subProjectInfo.description;
      } else if (
        this.subProjectInfo.purpose != undefined &&
        this.subProjectInfo.purpose != null
      ) {
        this.description = this.subProjectInfo.purpose;
        this.oldDescription = this.subProjectInfo.purpose;
      }

      if (
        this.subProjectInfo.background != undefined &&
        this.subProjectInfo.background != null
      ) {
        this.background = this.subProjectInfo.background;
        this.oldBackground = this.subProjectInfo.background;
      }

      if (
        this.subProjectInfo.limitation != undefined &&
        this.subProjectInfo.limitation != null
      ) {
        this.limitation = this.subProjectInfo.limitation;
        this.oldLimitation = this.subProjectInfo.limitation;
      }
    },
    showMembers() {
      let membersList = this.subProjectInfo.members;
      let manager = { userId: this.subProjectInfo.managerId };
      if (
        membersList.length == 0 ||
        membersList[0].userId != this.subProjectInfo.managerId
      ) {
        membersList.unshift(manager);
      }
      let participantsTemp = [];
      let index = membersList.length - 1;

      this.axios
        .get(
          "/PExploration/user/inquiry" +
            "?key=" +
            "userId" +
            "&value=" +
            membersList[0].userId
        )
        .then(res => {
          if (res.data != "Fail" && res.data != "None") {
            participantsTemp.push(res.data);
            this.$set(this, "participants", participantsTemp);

            for (let i = 1; i < membersList.length; i++) {
              this.axios
                .get(
                  "/PExploration/user/inquiry" +
                    "?key=" +
                    "userId" +
                    "&value=" +
                    membersList[i].userId
                )
                .then(res => {
                  if (res.data != "Fail" && res.data != "None") {
                    participantsTemp.push(res.data);
                    if (index-- == 1) {
                      this.$set(this, "participants", participantsTemp);
                    }
                  }
                })
                .catch(err => {});
            }
          }
        })
        .catch(err => {});
    },
    ok() {
      this.$Message.info("Clicked ok");
    },
    cancel() {},
    //加载并打开成员邀请Modal
    inviteMembersModalShow() {
      this.candidates = [];
      this.inviteList = [];

      let allMembers = this.projectInfo.members;
      let manager = {
        userName: this.projectInfo.managerName,
        userId: this.projectInfo.managerId
      };
      allMembers.unshift(manager);
      for (let i = 0; i < allMembers.length; i++) {
        let exist = false;
        for (let j = 0; j < this.participants.length; j++) {
          if (allMembers[i].userId === this.participants[j].userId) {
            exist = true;
          }
        }
        if (!exist) {
          this.candidates.push(allMembers[i]);
        }
      }
      this.inviteModal = true;
    },
    inviteMembers() {
      var that = this;
      for (let i = 0; i < this.inviteList.length; i++) {
        $.ajax({
          url:
            "/PExploration/subProject/join" +
            "?subProjectId=" +
            this.$route.params.id +
            "&userId=" +
            this.inviteList[i],
          type: "GET",
          async: false,
          success: data => {
            if (data == "Exist") {
              this.$Message.error("Exist!");
            } else if (data == "None") {
              this.$Message.error("None!");
            } else if (data == "Fail") {
              this.$Message.error("Fail!");
            } else {
              that.axios
                .get(
                  "/PExploration/user/inquiry" +
                    "?key=" +
                    "userId" +
                    "&value=" +
                    that.inviteList[i]
                )
                .then(res => {
                  if (res.data != "Fail" && res.data != "None") {
                    that.participants.push(res.data);
                    this.$set(
                      this.subProjectInfo,
                      "members",
                      this.participants
                    );
                    this.$store.commit(
                      "setSubProjectInfo",
                      this.subProjectInfo
                    );
                  }
                })
                .catch(err => {});
              //notice
              let replyNotice = {};
              replyNotice["recipientId"] = this.inviteList[i];
              replyNotice["type"] = "notice";
              replyNotice["content"] = {
                title: "Join subproject",
                description:
                  "You have been invited by " +
                  this.subProjectInfo.managerName +
                  " to join in the subproject: " +
                  this.subProjectInfo.title +
                  " of project: " +
                  this.projectInfo.title +
                  " , and now you are a member in this subproject."
              };
              this.axios
                .post("/PExploration/notice/save", replyNotice)
                .then(result => {
                  if (result.data == "Success") {
                    this.$emit("sendNotice", this.inviteList[i]); // 改apply.content.userId
                  } else {
                    this.$Message.error("reply fail.");
                  }
                })
                .catch(err => {
                  this.$Message.error("reply fail.");
                });
            }
          }
        });
      }
      this.init();
    },
    quitSubProject() {
      this.axios
        .get(
          "/PExploration/subProject/quit" +
            "?subProjectId=" +
            this.$route.params.id +
            "&userId=" +
            this.$store.getters.userId
        )
        .then(res => {
          if (res.data == "Success") {
            // 这里添加一个通知
            let quitNotice = {};
            quitNotice["recipientId"] = this.subProjectInfo.managerId;
            quitNotice["type"] = "notice";
            quitNotice["content"] = {
              userName: this.$store.getters.userId,
              title: "Member quit notice",
              description:
                "User " +
                this.$store.getters.userName +
                " quit from your project called " +
                this.subProjectInfo.title,
              scope: "subProject",
              approve: "unknow"
            };
            this.axios
              .post("/PExploration/notice/save", quitNotice)
              .then(res => {
                if (res.data == "Success") {
                  this.$emit("sendNotice", this.subProjectInfo.managerId);
                } else {
                }
              })
              .catch(err => {
                console.log(err.data);
              });
            let projectId = this.projectInfo.projectId;
            this.$router.push({
              name: "ProjectDetail",
              params: { id: projectId }
            });
          } else {
            this.$Message.error("Fail!");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },

    gotoPersonalSpace(id) {
      if (id == this.$store.getters.userId) {
        this.$router.push({ name: "PersonalPage" });
      } else {
        this.$router.push({ name: "MemberDetailPage", params: { id: id } });
      }
    },
    giveDeleteProperty(index) {
      if (
        this.subProjectInfo.managerId == this.$store.getters.userId &&
        index != 0
      ) {
        return true;
      } else if (
        this.projectInfo.managerId == this.subProjectInfo.members[index].userId
      ) {
        return false;
      } else {
        return false;
      }
    },
    removeMember(uid, uname) {
      // 获取到userId
      this.axios
        .get(
          "/PExploration/subProject/quit" +
            "?subProjectId=" +
            this.$route.params.id +
            "&userId=" +
            uid
        )
        .then(res => {
          if (res.data == "Success") {
            var members = this.participants;
            for (var i = 0; i < members.length; i++) {
              if (members[i].userId == uid) {
                this.participants.splice(i, 1);
                break;
              }
            }
            this.$set(this.subProjectInfo, "members", this.participants);
            this.$store.commit("setSubProjectInfo", this.subProjectInfo);
            this.$Message.info("Remove member successfully");
            //notice
            let projectName = this.projectInfo.title;
            let removeNotice = {};
            removeNotice["recipientId"] = uid;
            removeNotice["type"] = "notice";
            removeNotice["content"] = {
              title: "Remove notification",
              description:
                "You have been expeled from sub project called " +
                this.subProjectInfo.title +
                ", which belongs to project " +
                projectName +
                "."
            };
            this.axios
              .post("/PExploration/notice/save", removeNotice)
              .then(res => {
                if (res.data == "Success") {
                  this.$emit("sendNotice", uid);
                }
              })
              .catch(err => {
                console.log("申请失败的原因是：" + err.data);
              });
          } else {
            this.$Message.error("Fail!");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    editTitle() {
      if (this.edit0) {
        this.edit0 = false;
        if (this.oldTitle != this.title) {
          let obj = new URLSearchParams();
          obj.append("subProjectId", this.subProjectInfo.subProjectId);
          obj.append("title", this.title);
          this.axios
            .post("/PExploration/subProject/update", obj)
            .then(res => {
              if (res.data == "Offline") {
                this.$store.commit("userLogout");
                this.$router.push({ name: "Login" });
              } else if (res.data != "Fail") {
                this.$Notice.info({
                  desc: "Update successfully!"
                });
                this.oldTitle = this.title;
              } else {
                this.$Message.error("Update subproject failed.");
              }
            })
            .catch(err => {
              console.log(err.data);
            });
        }
      } else {
        this.edit0 = true;
      }
    },
    editDescription() {
      if (this.edit1) {
        this.edit1 = false;

        if (this.oldDescription != this.description) {
          let obj = new URLSearchParams();
          obj.append("subProjectId", this.subProjectInfo.subProjectId);
          obj.append("description", this.description);
          this.axios
            .post("/PExploration/subProject/update", obj)
            .then(res => {
              if (res.data == "Offline") {
                this.$store.commit("userLogout");
                this.$router.push({ name: "Login" });
              } else if (res.data != "Fail") {
                this.$Notice.info({
                  desc: "Update successfully!"
                });
                this.oldDescription = this.description;
              } else {
                this.$Message.error("Update subproject failed.");
              }
            })
            .catch(err => {
              console.log(err.data);
            });
        }
      } else {
        this.edit1 = true;
      }
    },
    editBackground() {
      if (this.edit2) {
        this.edit2 = false;

        if (this.oldBackground != this.background) {
          let obj = new URLSearchParams();
          obj.append("subProjectId", this.subProjectInfo.subProjectId);
          obj.append("background", this.background);
          this.axios
            .post("/PExploration/subProject/update", obj)
            .then(res => {
              if (res.data == "Offline") {
                this.$store.commit("userLogout");
                this.$router.push({ name: "Login" });
              } else if (res.data != "Fail") {
                this.$Notice.info({
                  desc: "Update successfully!"
                });
                this.oldBackground = this.background;
              } else {
                this.$Message.error("Update subproject failed.");
              }
            })
            .catch(err => {
              console.log(err.data);
            });
        }
      } else {
        this.edit2 = true;
      }
    },
    editLimitation() {
      if (this.edit3) {
        this.edit3 = false;

        if (this.oldLimitation != this.limitation) {
          let obj = new URLSearchParams();
          obj.append("subProjectId", this.subProjectInfo.subProjectId);
          obj.append("limitation", this.limitation);
          this.axios
            .post("/PExploration/subProject/update", obj)
            .then(res => {
              if (res.data == "Offline") {
                this.$store.commit("userLogout");
                this.$router.push({ name: "Login" });
              } else if (res.data != "Fail") {
                this.$Notice.info({
                  desc: "Update successfully!"
                });
                this.oldLimitation = this.limitation;
              } else {
                this.$Message.error("Update subproject failed.");
              }
            })
            .catch(err => {
              console.log(err.data);
            });
        }
      } else {
        this.edit3 = true;
      }
    }
  }
};
</script>
