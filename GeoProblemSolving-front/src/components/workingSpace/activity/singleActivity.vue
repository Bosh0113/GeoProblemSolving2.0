<template>
  <div>
    <div style="display: flex; background-color: #f8f8f9">
      <div
        style="
          width: 250px;
          margin-right: 5px;
          border-right: 1px solid #dcdee2;
          background-color: white;
        "
      >
        <vue-scroll :ops="scrollOps" style="height: calc(100vh - 70px)">
          <div style="padding: 5px 10px">
            <h3 style="display: inline-block">Name:</h3>
            <p style="font-size: medium; padding-top: 5px">
              {{ activityInfo.name }}
            </p>
            <Divider style="margin: 10px 0" />
            <h3 style="display: inline-block">Purpose:</h3>
            <p style="font-size: medium; padding-top: 5px">
              {{ activityInfo.purpose }}
            </p>
            <Divider style="margin: 10px 0" />
            <h3>Description:</h3>
            <p
              style="
                font-size: small;
                white-space: normal;
                word-break: break-word;
                padding-top: 5px;
              "
            >
              {{ activityInfo.description }}
            </p>
            <Divider style="margin: 10px 0" />
            <!--项目成员显示-->
            <h3 style="display: contents">Members:</h3>
            <Icon
              v-if="
                activityInfo.level > 0 &&
                permissionIdentity(
                  activityInfo.permission,
                  userRole,
                  'manage_member'
                )
              "
              type="md-trash"
              size="16"
              title="Remove users"
              @click="delUserBtn = !delUserBtn"
              style="
                float: right;
                margin: 5px 20px 0 0;
                cursor: pointer;
                color: #ed4014;
              "
            />
            <Icon
              v-if="
                permissionIdentity(
                  activityInfo.permission,
                  userRole,
                  'manage_member'
                )
              "
              type="md-people"
              size="16"
              title="Change user role"
              @click="userRoleBtn = !userRoleBtn"
              style="
                float: right;
                margin: 5px 10px 0 0;
                cursor: pointer;
                color: #8bc34a;
              "
            />
            <Icon
              v-if="
                activityInfo.level > 0 &&
                permissionIdentity(
                  activityInfo.permission,
                  userRole,
                  'invite_member'
                )
              "
              type="md-person-add"
              size="16"
              title="Invite users"
              @click="preInvitation()"
              style="
                float: right;
                margin: 5px 20px 0 0;
                cursor: pointer;
                color: #2d8cf0;
              "
            />
            <Icon
              v-if="
                activityInfo.level > 0 &&
                userRole != 'visitor' &&
                activityInfo.creator != userInfo.userId
              "
              type="md-log-out"
              size="16"
              title="Leave the activity"
              @click="quitActivityModal = true"
              style="float: right; margin: 5px 20px 0 0; cursor: pointer"
            />
            <Card
              style="margin: 5px 0"
              :padding="5"
              v-for="member in participants"
              :key="member.name"
            >
              <div style="display: flex; align-items: center">
                <div
                  v-if="delUserBtn && member.userId != userInfo.userId"
                  style="cursor: pointer; margin-right: 10px"
                  @click="selectMember(member, 'delete')"
                >
                  <Icon type="md-remove-circle" size="20" color="#ed4014" />
                </div>
                <div
                  v-if="
                    userRoleBtn &&
                    member.userId != userInfo.userId &&
                    roleCompare(userRole, member.role) != -1
                  "
                  title="Set user role"
                  style="cursor: pointer; margin-right: 10px"
                  @click="selectMember(member, 'role')"
                >
                  <Icon type="md-people" size="20" color="#8bc34a" />
                </div>
                <div
                  v-if="
                    userRoleBtn &&
                    (member.userId == userInfo.userId ||
                      roleCompare(userRole, member.role) == -1)
                  "
                  :title="
                    member.role.charAt(0).toUpperCase() + member.role.slice(1)
                  "
                  style="cursor: default; margin-right: 10px"
                >
                  <Icon type="md-people" size="20" color="grey" />
                </div>
                <div
                  @click="gotoPersonalSpace(member.userId)"
                  style="display: flex; align-items: center; cursor: pointer"
                >
                  <div class="memberImg" style="position: relative">
                    <img
                      v-if="member.avatar != '' && member.avatar != undefined"
                      :src="member.avatar"
                      style="width: 40px; height: 40px"
                    />
                    <avatar
                      v-else
                      :username="member.name"
                      :size="40"
                      :rounded="true"
                    />
                    <div class="onlinecircle"></div>
                  </div>
                  <div class="memberDetail">
                    <div class="memberName">
                      <span>{{ member.name }}</span>
                    </div>
                    <div class="memberRole">
                      <span>{{ member.role.charAt(0).toUpperCase() + member.role.slice(1) }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </Card>
            <Divider style="margin: 10px 0" />
          </div>
        </vue-scroll>
      </div>
      <div
        style="flex: 1; border-left: 1px solid #dcdee2; background-color: white"
      >
        <Menu
          mode="horizontal"
          :active-name="activeMenu"
          style="height: 45px; line-height: 45px; z-index: 1"
          @on-select="changeMenuItem"
        >
          <MenuItem name="Workspace">
            <Icon type="ios-paper" />Workspace
          </MenuItem>
          <MenuItem name="Task">
            <Icon type="ios-git-network" />Task management
          </MenuItem>
        </Menu>

        <div v-show="activeMenu == 'Workspace'">
          <vue-scroll
            :ops="scrollOps"
            style="height: calc(100vh - 120px); margin-top: 5px"
          >
            <universal-space
              v-if="activityInfo.purpose == 'Others'"
              :activityInfo="activityInfo"
              :participants="participants"
            ></universal-space>
            <context-res
              v-else-if="
                activityInfo.purpose ==
                'Context definition & resource collection'
              "
              :activityInfo="activityInfo"
              :participants="participants"
            ></context-res>
            <data-processing
              v-else-if="activityInfo.purpose == 'Data processing'"
              :activityInfo="activityInfo"
              :participants="participants"
            ></data-processing>
            <data-visual
              v-else-if="activityInfo.purpose == 'Data visualization'"
              :activityInfo="activityInfo"
              :participants="participants"
            ></data-visual>
            <model-build
              v-else-if="
                activityInfo.purpose == 'Geo-analysis model construction'
              "
              :activityInfo="activityInfo"
              :participants="participants"
            ></model-build>
            <model-evaluation
              v-else-if="
                activityInfo.purpose == 'Model effectiveness evaluation'
              "
              :activityInfo="activityInfo"
              :participants="participants"
            ></model-evaluation>
            <geo-simulation
              v-else-if="activityInfo.purpose == 'Geographical simulation'"
              :activityInfo="activityInfo"
              :participants="participants"
            ></geo-simulation>
            <geo-analysis
              v-else-if="activityInfo.purpose == 'Data analysis'"
              :activityInfo="activityInfo"
              :participants="participants"
            ></geo-analysis>
            <decision-making
              v-else-if="activityInfo.purpose == 'Decision making'"
              :activityInfo="activityInfo"
              :participants="participants"
            ></decision-making>
          </vue-scroll>
        </div>
        <div v-show="activeMenu == 'Task'">
          <task-manager :activityInfo="activityInfo"></task-manager>
        </div>
      </div>
    </div>
    <Modal
      v-model="inviteModal"
      width="660px"
      title="Invite new members"
      @on-ok="inviteMembers"
      ok-text="Ok"
      cancel-text="Cancel"
    >
      <Transfer
        :data="potentialMembers"
        :target-keys="invitingMembers"
        :list-style="listStyle"
        :render-format="memberRender"
        :titles="['Members of the parent activity', 'Members of this activity']"
        filter-placeholder="Enter key words..."
        filterable
        :filter-method="filterMethod"
        @on-change="handleChange"
      ></Transfer>
    </Modal>
    <Modal
      v-model="removeMemberModal"
      width="400px"
      title="Remove this member"
      @on-ok="removeUser()"
      ok-text="Ok"
      cancel-text="Cancel"
    >
      <h4 style="color: red">Are you sure to remove this member?</h4>
    </Modal>
    <Modal
      v-model="memberRoleModal"
      width="400px"
      title="Set the role of this member"
      @on-ok="updateActivity()"
      ok-text="Ok"
      cancel-text="Cancel"
    >
      <h4>Select user role</h4>
      <Select v-model="roleSelected" text="Role" style="margin: 10px 0">
        <Option value="manager" v-show="roleCompare(userRole, 'manager') != -1"
          >Manager</Option
        >
        <Divider style="margin: 5px 0"></Divider>
        <Option value="core" disabled>Core team</Option>
        <Option
          value="researcher"
          v-show="roleCompare(userRole, 'researcher') != -1"
          >Researcher</Option
        >
        <Option value="expert" v-show="roleCompare(userRole, 'expert') != -1"
          >Expert</Option
        >
        <Option
          value="decision-maker"
          v-show="roleCompare(userRole, 'decision-maker') != -1"
          >Decision-maker</Option
        >
        <Option
          value="core-member"
          v-show="roleCompare(userRole, 'core-member') != -1"
          >Core-member</Option
        >
        <Divider style="margin: 5px 0"></Divider>
        <Option value="ordinary" disabled>Ordinary team</Option>
        <Option
          value="stakeholder"
          v-show="roleCompare(userRole, 'stakeholder') != -1"
          >Stakeholder</Option
        >
        <Option
          value="consultant"
          v-show="roleCompare(userRole, 'consultant') != -1"
          >Consultant</Option
        >
        <Option
          value="ordinary-member"
          v-show="roleCompare(userRole, 'ordinary-member') != -1"
          >Ordinary-member</Option
        >
      </Select>
    </Modal>
    <Modal
      v-model="quitActivityModal"
      width="400px"
      title="Leave this activity"
      @on-ok="leaveActivity()"
      ok-text="Ok"
      cancel-text="Cancel"
    >
      <h4 style="color: red">Are you sure to quit this subproject?</h4>
    </Modal>
  </div>
</template>
<script>
import Avatar from "vue-avatar";
import * as userRoleJS from "./../../../api/userRole.js";
import { get, del, post, put } from "../../../axios";
import taskManager from "./utils/taskManger.vue";
import universalSpace from "./funcs/universalSpace.vue";
import contextRes from "./funcs/contextAndResource.vue";
import dataProcessing from "./funcs/dataProcessing.vue";
import dataVisual from "./funcs/dataVisualization.vue";
import modelBuild from "./funcs/modelBuild.vue";
import modelEvaluation from "./funcs/modelEvaluation.vue";
import geoSimulation from "./funcs/geoSimulation.vue";
import geoAnalysis from "./funcs/geoAnalysis.vue";
import decisionMaking from "./funcs/decisionMaking.vue";
export default {
  props: ["activityInfo"],
  components: {
    Avatar,
    taskManager,
    universalSpace,
    contextRes,
    dataProcessing,
    dataVisual,
    modelBuild,
    modelEvaluation,
    geoSimulation,
    geoAnalysis,
    decisionMaking,
  },
  data() {
    return {
      scrollOps: {
        bar: {
          background: "lightgrey",
        },
      },
      projectInfo: parent.vm.projectInfo,
      activeMenu: "Workspace",
      userInfo: JSON.parse(sessionStorage.getItem("userInfo")),
      userRole: "visitor",
      participants: [],
      // add
      potentialMembers: [],
      invitingMembers: [],
      inviteModal: false,
      // remove
      delUserBtn: false,
      slctDletMember: {},
      removeMemberModal: false,
      // leave
      quitActivityModal: false,
      listStyle: { width: "280px", height: "360px" },
      // update
      slctRoleMember: {},
      memberRoleModal: false,
      userRoleBtn: false,
      roleSelected: "",
    };
  },
  created() {
    this.userRole = this.roleIdentity(this.activityInfo);
    this.getParticipants();
  },
  methods: {
    roleIdentity(activity) {
      return userRoleJS.roleIdentify(activity.members, this.userInfo.userId);
    },
    permissionIdentity(permission, role, operation) {
      if (operation == "auto_join") {
        if (JSON.parse(permission).auto_join.visitor == "Yes") return true;
        else if (JSON.parse(permission).auto_join.visitor == "No") return false;
        else {
          return this.getParentPermission();
        }
      } else {
        return userRoleJS.permissionIdentity(
          JSON.parse(permission),
          role,
          operation
        );
      }
    },
    roleCompare(role1, role2) {
      return userRoleJS.roleCompare(role1, role2);
    },
    async preInvitation() {
      let url = "";
      let activity = this.activityInfo;
      if (activity.level == 1) {
        url = "/GeoProblemSolving/project/" + activity.parent + "/user";
      } else if (activity.level == 2) {
        url = "/GeoProblemSolving/subproject/" + activity.parent + "/user";
      } else if (activity.level > 2) {
        url = "/GeoProblemSolving/activity/" + activity.parent + "/user";
      } else {
        return;
      }
      let data = await get(url);

      this.potentialMembers = [];
      this.invitingMembers = [];
      let candidates = data.members;
      for (let i = 0; i < candidates.length; i++) {
        this.potentialMembers.push({
          key: this.potentialMembers.length,
          userId: candidates[i].userId,
          name: candidates[i].name,
          role: candidates[i].role,
          disabled: this.participants.contains(candidates[i]),
        });
      }
      this.invitingMembers = this.getTargetKeys();
      this.inviteModal = true;
    },
    getTargetKeys() {
      return this.potentialMembers
        .filter((item) => {
          return item.disabled;
        })
        .map((item) => item.key);
    },
    memberRender(item) {
      return `<span title="${item.name} - ${item.role}">${item.name} - ${item.role}</span>`;
    },
    filterMethod(data, query) {
      return data.name.indexOf(query) > -1;
    },
    handleChange(newTargetKeys) {
      this.invitingMembers = newTargetKeys;
    },
    inviteMembers() {
      let activity = this.activityInfo;

      // add members
      for (var i = 0; i < this.invitingMembers.length; i++) {
        let index = this.invitingMembers[i];
        if (this.participants.contains(this.potentialMembers[index])) continue;

        let user = this.potentialMembers[index];
        let url = "";
        if (activity.level == 1) {
          url =
            "/GeoProblemSolving/subproject/" +
            activity.aid +
            "/user?userId=" +
            user.userId;
        } else if (activity.level > 1) {
          url =
            "/GeoProblemSolving/activity/" +
            activity.aid +
            "/user?userId=" +
            user.userId;
        } else {
          return;
        }

        this.axios
          .post(url)
          .then((res) => {
            if (res.data.code == 0) {
              this.participants.push(user);
              //notice
              let notice = {
                recipientId: user.userId,
                type: "notice",
                content: {
                  title: "Join activity",
                  description:
                    "You have been invited by " +
                    this.userInfo.name +
                    " to join the activity: " +
                    activity.name +
                    " in project: " +
                    this.projectInfo.name +
                    " , and now you are a member in this activity!",
                  approve: "unknow",
                },
              };
              this.sendNotice(activity, notice);
            } else {
              console.log(res.data.msg);
            }
          })
          .catch((err) => {
            throw err;
          });
      }
    },
    selectMember(member) {
      if (operation == "delete") {
        this.slctDletMember = member;
        this.removeMemberModal = true;
      } else if (operation == "role") {
        this.slctRoleMember = member;
        this.memberRoleModal = true;
        this.roleSelected = member.role;
      }
    },
    removeUser() {
      let url = "";
      let member = this.slctDletMember;
      let activity = this.activityInfo;
      if (activity.level == 1) {
        url =
          "/GeoProblemSolving/subproject/" +
          activity.aid +
          "/user?userId=" +
          member.userId;
      } else if (activity.level > 1) {
        url =
          "/GeoProblemSolving/activity/" +
          activity.aid +
          "/user?userId=" +
          member.userId;
      } else {
        return;
      }

      this.axios
        .delete(url)
        .then((res) => {
          if (res.data.code == 0) {
            let index = this.participants.indexOf(member);
            this.participants.splice(index, 1);
            //notice
            let notice = {
              recipientId: member.userId,
              type: "notice",
              content: {
                title: "Leave activity",
                description:
                  "You have been remove from the activity: " +
                  activity.name +
                  " in project: " +
                  this.projectInfo.name +
                  ".",
                approve: "unknow",
              },
            };
            this.sendNotice(activity, notice);
          } else {
            console.log(res.data.msg);
          }
        })
        .catch((err) => {
          throw err;
        });
    },
    leaveActivity() {
      let member = this.userInfo;
      let activity = this.activityInfo;
      // get url
      let url = "";
      if (activity.level == 1) {
        url =
          "/GeoProblemSolving/subproject/" +
          activity.aid +
          "/user?userId=" +
          member.userId;
      } else if (activity.level > 1) {
        url =
          "/GeoProblemSolving/activity/" +
          activity.aid +
          "/user?userId=" +
          member.userId;
      } else {
        return;
      }

      this.axios
        .delete(url)
        .then((res) => {
          if (res.data.code == 0) {
            parent.location.href =
              "/GeoProblemSolving/projectInfo/" +
              this.projectInfo.aid +
              "?content=workspace&aid=" +
              activity.parent +
              "&level=" +
              (activity.level - 1).toString();

            //notice
            let managers = userRoleJS.getMemberByRole(activity, "manager");
            for (var i = 0; i < managers.length; i++) {
              let notice = {
                recipientId: managers[i],
                type: "notice",
                content: {
                  title: "Leave activity",
                  description:
                    member.name +
                    " left the activity: " +
                    activity.name +
                    " in project: " +
                    this.projectInfo.name +
                    ".",
                  approve: "unknow",
                },
              };
              this.sendNotice(activity, notice);
            }
          } else {
            console.log(res.data.msg);
          }
        })
        .catch((err) => {
          throw err;
        });
    },
    sendNotice(activity, notice) {
      this.axios
        .post("/GeoProblemSolving/notice/save", notice)
        .then((result) => {
          if (result.data == "Success") {
            parent.vm.$emit("sendNotice", notice.recipientId);
          } else {
            this.$Message.error("Notice fail.");
          }
        })
        .catch((err) => {
          throw err;
        });
    },
    gotoPersonalSpace(id) {
      if (id == this.$store.getters.userId) {
        this.$router.push({ name: "PersonalPage" });
      } else {
        this.$router.push({ name: "MemberDetailPage", params: { id: id } });
      }
    },
    changeMenuItem(name) {
      this.activeMenu = name;
    },
    getParticipants() {
      let url = "";
      let activity = this.activityInfo;
      if (activity.level == 0) {
        url = "/GeoProblemSolving/project/" + activity.aid + "/user";
      } else if (activity.level == 1) {
        url = "/GeoProblemSolving/subproject/" + activity.aid + "/user";
      } else if (activity.level > 1) {
        url = "/GeoProblemSolving/activity/" + activity.aid + "/user";
      }
      //callback setTimeBack
      this.axios
        .get(url)
        .then((res) => {
          if (res.data.code == 0) {
            this.creatorInfo = res.data.data.creator;
            this.participants = res.data.data.members;
          } else {
            console.log(res.data.msg);
          }
        })
        .catch((err) => {
          throw err;
        });
    },
  },
};
</script>
<style scoped>
.memberImg {
  width: 40px;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.memberName {
  height: 20px;
  padding: 0px 10px;
  width: 100%;
}
.memberDetail {
  height: 100%;
  width: 100%;
  overflow: hidden;
}
.memberRole {
  height: 20px;
  padding: 0px 10px;
  width: 100%;
}
.memberName span {
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.memberRole span {
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 100%;
}
</style>
