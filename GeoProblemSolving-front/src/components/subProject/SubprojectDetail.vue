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
  min-height: 100px;
  word-break: break-all;
  word-wrap: break-word;
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
      <!-- <Col span="4" style="height:40px;" class="operatePanel">
        <Button
          type="default"
          @click="conveneWork()"
          icon="md-mail"
          title="Inform others to work together"
        >Inform</Button>
      </Col>-->
      <Col :xs="14" :sm="15" :md="16" :lg="17" style="margin:30px 0 0 80px">
        <div>
          <vue-scroll :ops="scrollOps" :style="{height:sidebarHeight+150+'px'}">
            <Card class="Information">
              <div style="width:100%">
                <strong>Purposes / Goals:</strong>
                <template  v-if="subProjectInfo.managerId == $store.getters.userId">
                  <Icon
                    v-if="!edit1"
                    type="ios-create"
                    :size="18"
                    style="float:right;cursor:pointer"
                    title="Edit"
                    @click="editPurposes"
                  />
                  <Icon
                    v-else
                    type="md-checkbox-outline"
                    :size="18"
                    style="float:right;cursor:pointer"
                    title="Complete"
                    @click="editPurposes"
                  />
                </template>
              </div>
              <Divider style="margin:10px 0; background:lightblue" />
              <div v-if="!edit1" class="subProjectDesc" style="overflow-y:auto">{{purpose}}</div>
              <template v-else>
                <Input
                  v-model="purpose"
                  type="textarea"
                  :rows="5"
                  placeholder="Enter something..."
                />
              </template>
            </Card>
            <Card class="Information">
              <div style="width:100%">
                <strong>Background:</strong>
                <template  v-if="subProjectInfo.managerId == $store.getters.userId">
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
                  :rows="5"
                  placeholder="Enter something..."
                />
              </template>
            </Card>
            <Card class="Information">
              <div style="width:100%">
                <strong>Limitations / problems:</strong>
                <template v-if="subProjectInfo.managerId == $store.getters.userId">
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
              v-if="subProjectInfo.managerId == $store.getters.userId"
              type="md-add"
              :size="18"
              style="cursor:pointer"
              title="Invite other participants"
              @click="inviteMembersModalShow"
            />
            <Icon
              v-else-if="subProjectInfo.isMember"
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
                <div style="line-height:50px" type="default">
                  <span
                    style="cursor:pointer"
                    title="Out"
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
            title="Quit Sub-Project"
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
    <!-- </Row> -->
  </div>
</template>
<script>
import Avatar from "vue-avatar";
import folderTree from "../resource/FolderTree.vue";
export default {
  updated() {
    $(".userAvatar sup").css("margin", "10px 10px 0 0");
  },
  components: {
    Avatar,
    folderTree
  },
  props: ["subProjectInfo"],
  data() {
    return {
      scrollOps: {
        bar: {
          background: "lightgrey"
        }
      },
      // information of project
      projectInfo: {},
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
      edit1: false,
      edit2: false,
      edit3: false,
      background: "",
      limitation: "",
      purpose: "",
      // 用户角色
      userRole: ""
    };
  },
  created() {
    this.init();
  },
  mounted() {
    this.toProjectPage = "/project/" + sessionStorage.getItem("projectId");
    this.getProjectInfo();
    window.addEventListener("resize", this.initSize);
  },

  beforeRouteLeave(to, from, next) {
    next();
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
      var that = this;
      let subProjectId = this.$route.params.id;
      let subProjectInfo = this.$store.getters.subProject;
      if (
        JSON.stringify(subProjectInfo) != "{}" &&
        subProjectInfo.subProjectId == subProjectId
      ) {
        var userId = this.$store.getters.userId;
        var members = subProjectInfo.members;
        subProjectInfo.isMember = false;
        for (var i = 0; i < members.length; i++) {
          if (members[i].userId == userId) {
            subProjectInfo.isMember = true;
            break;
          }
        }
        this.$set(this, "subProjectInfo", subProjectInfo);
        this.getSubprojectDes();

        this.inviteAble = false;
        this.showMembers();
        sessionStorage.setItem("subProjectId", subProjectInfo.subProjectId);
        sessionStorage.setItem("subProjectName", subProjectInfo.title);
      } else {
        $.ajax({
          url:
            "/GeoProblemSolving/subProject/inquiry" +
            "?key=subProjectId" +
            "&value=" +
            subProjectId,
          type: "GET",
          async: false,
          success: data => {
            if (data == "Offline") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else if (data != "None" && data != "Fail") {
              subProjectInfo = data[0];
              this.$set(this, "subProjectInfo", subProjectInfo);
              sessionStorage.setItem(
                "subProjectId",
                subProjectInfo.subProjectId
              );
              sessionStorage.setItem("subProjectName", subProjectInfo.title);
              this.getSubprojectDes();

              this.managerIdentity(subProjectInfo.managerId);
              this.memberIdentity(subProjectInfo.members);
              this.$store.commit("setSubProjectInfo", subProjectInfo);

              this.inviteAble = false;
              this.showMembers();
            }
          },
          error: function(err) {
            console.log("Get manager name fail.");
          }
        });
      }
      // 判断用户权限
      if (
        !this.subProjectInfo.isMember &&
        this.subProjectInfo.managerId != this.$store.getters.userId
      ) {
        this.userRole = "Visitor";
      }
    },
    managerIdentity(managerId) {
      if (managerId === this.$store.getters.userId) {
        this.subProjectInfo.isManager = true;
      }
    },
    memberIdentity(members) {
      for (let i = 0; i < members.length; i++) {
        if (members[i].userId === this.$store.getters.userId) {
          this.subProjectInfo.isMember = true;
          break;
        }
      }
    },
    getSubprojectDes() {
      if (
        this.subProjectInfo.purpose != undefined &&
        this.subProjectInfo.purpose != null
      ) {
        this.purpose = this.subProjectInfo.purpose;
      } else if (
        this.subProjectInfo.description != undefined &&
        this.subProjectInfo.description != null
      ) {
        this.purpose = this.subProjectInfo.description;
      }

      if (
        this.subProjectInfo.limitation != undefined &&
        this.subProjectInfo.limitation != null
      ) {
        this.limitation = this.subProjectInfo.limitation;
      }
      if (
        this.subProjectInfo.background != undefined &&
        this.subProjectInfo.background != null
      ) {
        this.background = this.subProjectInfo.background;
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
          "/GeoProblemSolving/user/inquiry" +
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
                  "/GeoProblemSolving/user/inquiry" +
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
    // 召集参与者
    conveneWork() {
      for (let i = 0; i < this.participants.length; i++) {
        if (this.participants[i].userId != this.$store.getters.userId) {
          let notice = {};
          notice["recipientId"] = this.participants[i].userId;
          notice["type"] = "work";
          notice["content"] = {
            subProjectId: this.subProjectInfo.subProjectId,
            title: "Work Notice",
            description:
              "The manager of" +
              " the sub-project " +
              this.subProjectInfo.title +
              " of project " +
              this.projectInfo.title +
              " informs you to start working!"
          };
          this.axios
            .post("/GeoProblemSolving/notice/save", notice)
            .then(res => {
              if (res.data == "Success") {
                this.$Notice.success({
                  desc: "Inform Successfully"
                });
              } else {
                this.$Notice.info({
                  desc: "Inform failed"
                });
              }
            })
            .catch(err => {
              console.log(err.data);
            });
        }
      }
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
    getProjectInfo() {
      let that = this;
      let projectInfo = that.$store.getters.project;
      if (
        JSON.stringify(projectInfo) != "{}" &&
        projectInfo.projectId.substring(0, 36) ==
          this.subProjectInfo.projectId.substring(0, 36)
      ) {
        this.projectInfo = projectInfo;
      } else {
        $.ajax({
          url:
            "/GeoProblemSolving/project/inquiry" +
            "?key=projectId" +
            "&value=" +
            this.subProjectInfo.projectId,
          type: "GET",
          async: false,
          success: data => {
            if (data != "None" && data != "Fail") {
              that.projectInfo = data[0];
              that.$store.commit("setProjectInfo", data[0]);
            } else {
              console.log(data);
            }
          }
        });
      }
    },
    inviteMembers() {
      var that = this;
      for (let i = 0; i < this.inviteList.length; i++) {
        $.ajax({
          url:
            "/GeoProblemSolving/subProject/join" +
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
                  "/GeoProblemSolving/user/inquiry" +
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
                .post("/GeoProblemSolving/notice/save", replyNotice)
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
          "/GeoProblemSolving/subProject/quit" +
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
                sessionStorage.getItem("subProjectName"),
              scope: "subProject",
              approve: "unknow"
            };
            this.axios
              .post("/GeoProblemSolving/notice/save", quitNotice)
              .then(res => {
                if (res.data == "Success") {
                  this.$emit("sendNotice", this.subProjectInfo.managerId);
                } else {
                }
              })
              .catch(err => {
                console.log(err.data);
              });
            let projectId = sessionStorage.getItem("projectId");
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
      } else {
        return false;
      }
    },
    removeMember(uid, uname) {
      // 获取到userId
      this.axios
        .get(
          "/GeoProblemSolving/subProject/quit" +
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
            let projectName = sessionStorage.getItem("projectName");
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
              .post("/GeoProblemSolving/notice/save", removeNotice)
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
    editPurposes() {
      if (this.edit1) {
        this.edit1 = false;

        let obj = new URLSearchParams();
        obj.append("subProjectId", this.subProjectInfo.subProjectId);
        obj.append("purpose", this.purpose);
        this.axios
          .post("/GeoProblemSolving/subProject/update", obj)
          .then(res => {
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
          })
          .catch(err => {
            console.log(err.data);
          });
      } else {
        this.edit1 = true;
      }
    },
    editBackground() {
      if (this.edit2) {
        this.edit2 = false;

        let obj = new URLSearchParams();
        obj.append("subProjectId", this.subProjectInfo.subProjectId);
        obj.append("background", this.background);
        this.axios
          .post("/GeoProblemSolving/subProject/update", obj)
          .then(res => {
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
          })
          .catch(err => {
            console.log(err.data);
          });
      } else {
        this.edit2 = true;
      }
    },
    editLimitation() {
      if (this.edit3) {
        this.edit3 = false;

        let obj = new URLSearchParams();
        obj.append("subProjectId", this.subProjectInfo.subProjectId);
        obj.append("limitation", this.limitation);
        this.axios
          .post("/GeoProblemSolving/subProject/update", obj)
          .then(res => {
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
          })
          .catch(err => {
            console.log(err.data);
          });
      } else {
        this.edit3 = true;
      }
    }
  }
};
</script>
