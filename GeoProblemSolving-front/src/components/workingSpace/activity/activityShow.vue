<!--workspace introduction-->
<template>
  <div style="padding: 5px 10px">
    <div
      style="
        height: calc(100vh - 110px);
        width: 75%;
        float: left;
        padding: 20px;
      "
    >
      <vue-scroll
        :ops="scrollOps"
        style="margin-top: 10px; height: calc(100vh - 165px)"
      >
        <div>
          <div>
            <h2>{{ activityInfo.name }}</h2>
          </div>
          <div style="heigth: 50%; width: 50%; float: left">
            <div style="margin: 10px 0">
              <Label>Purpose:</Label>
              <span style="margin-left: 10px">{{ activityInfo.purpose }}</span>
            </div>
            <div style="margin: 10px 0">
              <Label>Total participants:</Label>
              <span style="margin-left: 10px">{{ participants.length }}</span>
            </div>
            <div style="margin: 10px 0">
              <Label>Total child activities:</Label>
              <span
                v-if="activityInfo.children != undefined"
                style="margin-left: 10px"
                >{{ activityInfo.children.length }}</span
              >
              <span
                v-else
                style="margin-left: 10px; cursor: pointer; color: #2d8cf0"
                >0</span
              >
            </div>
          </div>
          <div style="heigth: 50%; width: 50%; float: left">
            <div style="margin: 10px 0">
              <Label>Creator:</Label>
              <span
                style="margin-left: 10px; cursor: pointer; color: #2d8cf0"
                @click="gotoPersonalSpace(creatorInfo.userId)"
                >{{ creatorInfo.name }}
              </span>
            </div>
            <div
              style="margin: 10px 0"
              v-if="
                permissionIdentity(
                  activityInfo.permission,
                  userRole,
                  'manage_resource'
                )
              "
            >
              <Label>Permission:</Label>
              <span
                style="margin-left: 10px; cursor: pointer; color: #2d8cf0"
                @click="modifyPermission"
              >
                <Icon type="ios-create" />
              </span>
            </div>
            <div style="margin: 10px 0">
              <Label>Last active time:</Label>
              <span style="margin-left: 10px">{{
                activityInfo.activeTime.split(" ")[0]
              }}</span>
            </div>
          </div>
          <div style="margin: 10px 0">
            <Label style="float: left">Description: </Label>
            <div
              style="
                float: left;
                min-height: 40px;
                max-height: 120px;
                overflow-y: auto;
                word-break: break-word;
                margin-left: 10px;
              "
            >
              {{ activityInfo.description }}
            </div>
          </div>
        </div>
        <Divider style="margin: 10px 0"></Divider>
        <div>
          <h4 style="margin: 8px 0">Child activities</h4>
          <div
            v-for="item in activityInfo.children"
            :key="item.aid"
            class="childrenCard"
          >
            <Card
              style="
                height: 120px;
                width: 220px;
                float: left;
                margin: 0 10px 10px 0;
                cursor: pointer;
              "
              @click.native="enterChildActivity(item)"
            >
              <p slot="title">{{ item.name }}</p>
              <div
                slot="extra"
                v-if="roleIdentity(item) == 'visitor'"
                style="margin-top: -10px; margin-right: -5px"
              >
                <Tooltip
                  trigger="hover"
                  content="Apply to join this activity"
                  placement="bottom"
                  ><Icon
                    type="md-log-in"
                    class="changeInviteColor"
                    size="small"
                    style="cursor: pointer; color: #2d8cf0"
                    @click="preApplication(item)"
                  />
                </Tooltip>
              </div>
              <div
                slot="extra"
                style="margin-top: -10px; margin-right: -5px"
                v-else
              >
                <Icon type="ios-person" :size="20"> </Icon>
              </div>
              <div style="margin-top: 5px" title="item.description">
                {{ item.description }}
              </div>
            </Card>
          </div>
          <div>
            <Card
              style="
                height: 120px;
                width: 220px;
                float: left;
                margin: 0 10px 10px 0;
                cursor: pointer;
              "
            >
              <div
                style="text-align: center; margin-top: 20px; color: #2d8cf0"
                title="Create activity"
                @click="preCreation()"
              >
                <Icon type="ios-add" size="80" />
              </div>
            </Card>
          </div>
        </div>
      </vue-scroll>
    </div>
    <div
      style="
        width: 25%;
        float: left;
        padding: 0 20px;
        margin: 20px 0;
        border-left: 1px solid lightgray;
      "
    >
      <div>
        <span style="font-size: 1.17em; font-weight: bold">Participants</span>
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
          v-if="userRole != 'visitor'"
          type="md-log-out"
          size="16"
          title="Leave the activity"
          @click="quitActivityModal = true"
          style="float: right; margin: 5px 20px 0 0; cursor: pointer"
        />
      </div>
      <vue-scroll
        :ops="scrollOps"
        style="margin-top: 10px; height: calc(100vh - 190px)"
      >
        <Card
          style="margin: 5px 0"
          :padding="5"
          v-for="member in participants"
          :key="member.name"
        >
          <div style="display: flex; align-items: center">
            <div
              v-if="delUserBtn"
              style="cursor: pointer; margin-right: 10px"
              @click="selectMember(member)"
            >
              <Icon type="md-remove-circle" size="20" color="#ed4014" />
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
                  <span>{{ member.role }}</span>
                </div>
              </div>
            </div>
          </div>
        </Card>
      </vue-scroll>
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
      v-model="quitActivityModal"
      width="400px"
      title="Leave this activity"
      @on-ok="leaveActivity()"
      ok-text="Ok"
      cancel-text="Cancel"
    >
      <h4 style="color: red">Are you sure to quit this subproject?</h4>
    </Modal>
    <Modal
      v-model="applyJoinActivityModal"
      title="Apply to join the activity"
      width="800px"
      :mask-closable="false"
    >
      <div>
        <Form ref="applyJoinForm" :model="applyJoinForm" :label-width="120">
          <FormItem label="Reason" prop="reason">
            <Input v-model="applyJoinForm.reason" :rows="4" type="textarea" />
          </FormItem>
        </Form>
      </div>
      <div slot="footer" style="display: inline-block">
        <Button type="primary" @click="applyJoinActivity()" style="float: right"
          >OK</Button
        >
        <Button
          @click="applyJoinActivityModal = false"
          style="float: right; margin-right: 15px"
          >Cancel
        </Button>
      </div>
    </Modal>
    <Modal
      v-model="createActivityModel"
      title="Create a new child activity"
      width="800"
      :mask-closable="false"
    >
      <Form
        ref="activityForm"
        :model="activityForm"
        :rules="activityCreateRule"
        :label-width="120"
      >
        <FormItem label="Name" prop="name">
          <Input
            type="text"
            v-model="activityForm.name"
            placeholder="Fill in the name (less than 60 characters) ..."
          />
        </FormItem>
        <FormItem label="Description" prop="description">
          <Input
            v-model="activityForm.description"
            placeholder="Fill in the description..."
            :rows="4"
            type="textarea"
          />
        </FormItem>
        <FormItem label="Activity type:" prop="type">
          <Select
            v-model="activityForm.type"
            placeholder="Select the type of this activity"
            readonly
          >
            <Option value="Activity_Default">Select type later</Option>
            <Option value="Activity_Unit">Single activity</Option>
            <Option value="Activity_Group">Multi activities</Option>
          </Select>
        </FormItem>
        <FormItem
          label="Purpose:"
          prop="purpose"
          v-show="activityForm.type == 'Activity_Unit'"
        >
          <Select
            v-model="activityForm.purpose"
            placeholder="Select the purpose of this activity"
            readonly
          >
            <Option v-for="item in purposes" :key="item.index" :value="item">{{
              item
            }}</Option>
          </Select>
        </FormItem>
      </Form>
      <div slot="footer" style="display: inline-block">
        <Button
          type="primary"
          @click="createActivity('activityForm')"
          style="float: right"
          >OK</Button
        >
        <Button
          @click="createActivityModel = false"
          style="float: right; margin-right: 15px"
          >Cancel</Button
        >
      </div>
    </Modal>
  </div>
</template>
<script>
import * as userRoleJS from "./../../../api/userRole.js";
import { get, del, post, put } from "../../../axios";
import * as socketApi from "./../../../api/socket.js";
import Avatar from "vue-avatar";
export default {
  components: {
    Avatar,
  },
  props: ["activityInfo"],
  data() {
    return {
      scrollOps: {
        bar: {
          background: "lightgrey",
        },
      },
      projectInfo: parent.vm.projectInfo,
      userInfo: JSON.parse(sessionStorage.getItem("userInfo")),
      userRole: "visitor",
      // Members
      creatorInfo: {},
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
      // apply
      applyJoinActivityModal: false,
      createActivityModel: false,
      appliedActivity: {},
      applyJoinForm: {
        reason: "",
      },
      // Activity
      activityForm: {
        name: "",
        description: "",
        parent: "",
        creator: "",
        level: -1,
        permission: JSON.stringify(userRoleJS.getDefault()),
        type: "Activity_Default",
        purpose: "Multi-purpose",
      },
      activityCreateRule: {
        name: [
          {
            required: true,
            message: "The name should not be empty and more than 60 characters",
            trigger: "blur",
            type: "string",
            max: 60,
          },
        ],
        description: [
          {
            required: true,
            message: "The description should not be empty",
            trigger: "blur",
          },
        ],
        purpose: [
          {
            required: true,
            message: "The purpose should not be empty",
            trigger: "blur",
          },
        ],
      },
      purposes: [
        "Multi-purpose",
        "Context definition & resource collection",
        "Data processing",
        "Data visualization",
        "Geo-analysis model construction",
        "Model effectiveness evaluation",
        "Geographical simulation",
        "Data analysis",
        "Decision making",
      ],
      listStyle: { width: "280px", height: "360px" },
    };
  },
  created() {
    this.userRole = this.roleIdentity(this.activityInfo);
    this.getParticipants();
  },
  mounted() {
    Array.prototype.contains = function (obj) {
      var i = this.length;
      while (i--) {
        if (this[i].userId != undefined && this[i].userId === obj.userId) {
          return true;
        }
      }
      return false;
    };
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
    async getParentPermission() {
      let url = "";
      if (this.appliedActivity.level == 0 || this.appliedActivity.level == 1) {
        return this.projectInfo.members.contains(this.userInfo);
      } else if (this.appliedActivity.level == 2) {
        url = "/GeoProblemSolving/subproject/" + this.appliedActivity.parent;
        let parent = await get(url);
        return parent.members.contains(this.userInfo);
      } else if (this.appliedActivity.level > 2) {
        url = "/GeoProblemSolving/activity/" + this.appliedActivity.parent;
        let parent = await get(url);
        return parent.members.contains(this.userInfo);
      }
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
    getAllTasks() {},
    getAllResource() {},
    modifyPermission() {
      parent.location.href =
        "/GeoProblemSolving/permission/" +
        this.activityInfo.level + "/" +
        this.activityInfo.aid;
    },
    preCreation() {
      if (
        !this.permissionIdentity(
          this.activityInfo.permission,
          this.userRole,
          "manage_child_activity"
        )
      ) {
        this.$Notice.info({ desc: "Please join the activity firstly." });
        return;
      }
      this.activityForm.parent = this.activityInfo.aid;
      this.activityForm.creator = this.userInfo.userId;
      this.activityForm.level = this.activityInfo.level + 1;

      this.createActivityModel = true;
    },
    createActivity(name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          let url = "";
          if (this.activityForm.level == 1) {
            // subproject
            url = "/GeoProblemSolving/subproject";
          } else if (this.activityForm.level > 1) {
            // activity
            url = "/GeoProblemSolving/activity";
          }
          this.axios
            .post(url, this.activityForm)
            .then((res) => {
              if (res.data.code == 0) {
                parent.location.href =
                  "/GeoProblemSolving/projectInfo/" +
                  this.projectInfo.aid +
                  "?content=workspace&aid=" +
                  res.data.data.aid +
                  "&level=" +
                  res.data.data.level;
              } else {
                console.log(res.data.msg);
              }
            })
            .catch((err) => {
              throw err;
            });
          this.createActivityModel = false;
        }
      });
    },
    enterChildActivity(activity) {
      if (this.roleIdentity(activity) == "visitor") return;

      parent.location.href =
        "/GeoProblemSolving/projectInfo/" +
        this.projectInfo.aid +
        "?content=workspace&aid=" +
        activity.aid +
        "&level=" +
        activity.level;
    },
    preApplication(activity) {
      this.appliedActivity = activity;
      if (
        this.permissionIdentity(activity.permission, "visitor", "auto_join")
      ) {
        this.joinActivity();
      } else {
        this.applyJoinActivityModal = true;
      }
    },
    joinActivity() {
      let url = "";
      if (this.appliedActivity.level == 1) {
        url =
          "/GeoProblemSolving/subproject/" +
          this.appliedActivity.aid +
          "/user?userId=" +
          this.userInfo.userId;
      } else if (this.appliedActivity.levell > 1) {
        url =
          "/GeoProblemSolving/activity/" +
          this.appliedActivity.aid +
          "/user?userId=" +
          this.userInfo.userId;
      } else {
        return;
      }

      this.axios
        .post(url)
        .then((res) => {
          if (res.data.code == 0) {
            parent.location.href =
              "/GeoProblemSolving/projectInfo/" +
              this.projectInfo.aid +
              "?content=workspace&aid=" +
              this.appliedActivity.aid +
              "&level=" +
              this.appliedActivity.level;
          } else if (res.data.code == -3) {
            this.$Notice.info( {desc:
              "You has already been a member of the activity."}
            );
          } else {
            console.log(res.data.msg);
          }
        })
        .catch((err) => {
          throw err;
        });
    },
    applyJoinActivity() {
      let notice = {
        recipientId: userRoleJS.getMemberByRole(this.applyJoinForm, "manager"),
        type: "apply",
        content: {
          title:
            "Application of joining the activity: " + this.appliedActivity.name,
          activityId: this.appliedActivity.aid,
          activityName: this.appliedActivity.name,
          activityLevel: this.appliedActivity.level,
          userEmail: this.userInfo.email,
          userName: this.userInfo.name,
          userId: this.userInfo.userId,
          description: this.applyJoinForm.reason,
        },
      };
      this.sendNotice(activity, notice);
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
          disabled: this.participants.contains(candidates[i])
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
      this.slctDletMember = member;
      this.removeMemberModal = true;
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
            let notice = {
              recipientId: userRoleJS.getMemberByRole(this.activityInfo, "manager"),
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
    sendNotice(activity, notice) {
      if (
        Object.prototype.toString.call(activity.recipientId) == "[object Array]"
      ) {
        for (let i = 0; i < activity.recipientId.length; i++) {
          this.axios
            .post("/GeoProblemSolving/notice/save", notice)
            .then((result) => {
              if (result.data == "Success") {
                parent.vm.$emit("sendNotice", notice.recipientId[i]);
              } else {
                this.$Message.error("Notice fail.");
              }
            })
            .catch((err) => {
              throw err;
            });
        }
      } else {
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
      }
    },
    gotoPersonalSpace(id) {
      if (id == this.$store.getters.userId) {
        parent.location.href = "/GeoProblemSolving/personalPage";
      } else {
        parent.location.href = "/GeoProblemSolving/memberPage/" + id;
      }
    },
  },
};
</script>
<style scoped>
.childrenCard >>> .ivu-card-head {
  padding: 5px 10px;
}
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
