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
          <div style="heigth: 50%; width: 60%; float: left">
            <div style="margin: 10px 0">
              <Label>Purpose:</Label>
              <span style="margin-left: 10px">{{ activityInfo.purpose }}</span>
            </div>
            <div style="margin: 10px 0">
              <Label>Creator:</Label>
              <span
                style="margin-left: 10px; cursor: pointer; color: #2d8cf0"
                @click="gotoPersonalSpace(creatorInfo.userId)"
                >{{ creatorInfo.name }}</span
              >
            </div>
            <div style="margin: 10px 0">
              <Label>Created time:</Label>
              <span style="margin-left: 10px">{{
                activityInfo.createdTime
              }}</span>
            </div>
          </div>
          <div style="heigth: 50%; width: 40%; float: left">
            <div style="margin: 10px 0">
              <Label>Total child activities:</Label>
              <span
                v-if="activityInfo.children != undefined"
                style="margin-left: 10px; cursor: pointer; color: #2d8cf0"
                >{{ activityInfo.children.length}}</span
              >
              <span
                v-else
                style="margin-left: 10px; cursor: pointer; color: #2d8cf0"
                >0</span
              >
            </div>
            <div style="margin: 10px 0">
              <Label>Total participants:</Label>
              <span
                style="margin-left: 10px; cursor: pointer; color: #2d8cf0"
                >{{ activityInfo.members.length }}</span
              >
            </div>
            <div style="margin: 10px 0">
              <Label>Permission:</Label>
              <span
                style="margin-left: 10px; cursor: pointer; color: #2d8cf0"
                @click="modifyPermission"
              >
                <Icon type="ios-create" />
              </span>
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
            >
              <p slot="title">{{ item.name }}</p>
              <div
                slot="extra"
                v-if="userRole == 'visitor'"
                style="margin-top: -10px; margin-right: -5px"
              >
                <Poptip
                  trigger="hover"
                  content="Apply to join this activity"
                  placement="bottom"
                  ><Icon
                    type="ios-log-in"
                    class="changeInviteColor"
                    size="small"
                    style="cursor: pointer"
                    @click="preApplication(item)"
                  />
                </Poptip>
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
                style="text-align: center; margin-top: 20px"
                title="Create activity"
                @click="preCreation(item)"
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
          v-if="activityInfo.level > 0"
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
      </div>
      <vue-scroll
        :ops="scrollOps"
        style="margin-top: 10px; height: calc(100vh - 190px)"
      >
        <Card
          v-for="(member, index) in participants"
          :key="index"
          style="margin: 5px 0"
          :padding="5"
        >
          <div
            style="display: flex; align-items: center; cursor: pointer"
            @click="gotoPersonalSpace(member.userId)"
          >
            <div class="memberImg" style="position: relative">
              <img
                v-if="member.avatar != '' && member.avatar != 'undefined'"
                :src="member.avatar"
                style="width: 40px; height: 40px"
              />
              <avatar
                v-else
                :username="member.name"
                :size="40"
                :rounded="false"
              ></avatar>
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
        :target-keys="participants"
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
      v-model="applyJoinActivityModal"
      title="Apply to join the activity"
      width="800px"
      :mask-closable="false"
    >
      <div>
        <Form
          ref="applyJoinForm"
          :model="applyJoinForm"
          :rules="applyJoinRule"
          :label-width="120"
        >
          <FormItem label="Reason" prop="reason">
            <i-input
              v-model="applyJoinForm.reason"
              :rows="4"
              type="textarea"
            ></i-input>
          </FormItem>
        </Form>
      </div>
      <div slot="footer" style="display: inline-block">
        <Button
          type="primary"
          @click="applyJoinActivity('applyJoinForm')"
          style="float: right"
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
          ></Input>
        </FormItem>
        <FormItem label="Description" prop="description">
          <Input
            v-model="activityForm.description"
            placeholder="Fill in the description..."
            :rows="4"
            type="textarea"
          ></Input>
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
export default {
  props: ["activityInfo"],
  data() {
    return {
      scrollOps: {
        bar: {
          background: "lightgrey",
        },
      },
      userInfo: JSON.parse(sessionStorage.getItem("userInfo")),
      userRole: "visitor",
      creatorInfo: { name: "XXX", email: "XXX@XX.com" },
      participants: [
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
      ],
      inviteModal: false,
      applyJoinActivityModal: false,
      createActivityModel: false,
      appliedActivity: {},
      applyJoinForm: {
        reason: "",
      },
      applyJoinRule: {
        reason: [
          {
            required: true,
            message: "The description can't be empty",
            trigger: "blur",
          },
        ],
      },
      activityForm: {
        name: "",
        description: "",
        parent: "",
        creator: "",
        level: -1,
        permission: JSON.stringify(userRoleJS.getDefault()),
        type: "Activity_Default",
        purpose: "Others",
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
        "Others",
        "Context definition & resource collection",
        "Data processing",
        "Data visualization",
        "Geographic model construction",
        "Model effectiveness evaluation",
        "Geographical simulation",
        "Quantitative and qualitative analyses",
        "Decision-making and management",
      ],
      potentialMembers: [],
      listStyle: { width: "280px", height: "360px" },
    };
  },
  mounted() {
    // this.getParticipants();
    this.roleIdentity();
  },
  methods: {
    roleIdentity(activity) {
      this.userRole = userRoleJS.roleIdentify(
        this.activityInfo.members,
        this.userInfo.userId
      );
    },
    permissionIdentity(permission, role, operation) {
      return userRoleJS.permissionIdentity(
        JSON.parse(permission),
        role,
        operation
      );
    },
    getParticipants() {
      let url = "";
      let activity = this.activityInfo;
      if (activity.level == 1) {
        url = "/GeoProblemSolving/subproject/" + activity.aid + "/user";
      } else if (activity.level > 1) {
        url = "/GeoProblemSolving/activity/" + activity.aid + "/user";
      } else {
        return;
      }

      this.axios
        .get(url)
        .then((res) => {
          if (res.data.code == 0) {
            this.creatorInfo = res.data.data.creator;
            this.participants = res.data.data.members;
            console.log(this.participants);
          } else {
            console.log(res.data.msg);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getAllTasks() {},
    getAllResource() {},
    modifyPermission() {
      window.location.href =
        "/GeoProblemSolving/permission/" + this.projectInfo.aid;
    },
    preCreation(parent) {
      this.activityForm.parent = parent.aid;
      this.activityForm.creator = this.userInfo.userId;
      this.activityForm.level = parent.level + 1;

      this.createActivityModel = true;
    },
    preApplication(activity) {
      this.appliedActivity = activity;
      this.applyJoinActivityModal = true;
    },
    applyJoinActivity() {},
    preInvitation() {
      // let url = "";
      // let activity = this.activityInfo;
      // if (activity.level == 1) {
      //   url = "/GeoProblemSolving/project/" + activity.parent + "/user";
      // } else if (activity.level == 2) {
      //   url = "/GeoProblemSolving/subproject/" + activity.parent + "/user";
      // } else if (activity.level > 2) {
      //   url = "/GeoProblemSolving/activity/" + activity.parent + "/user";
      // } else {
      //   return;
      // }
      // this.axios
      //   .get(url)
      //   .then((res) => {
      //     if (res.data.code == 0) {
      //       let candidates = res.data.data.members;
      //       this.potentialMembers = [];
      //       for (let i = 0; i < candidates.length; i++) {
      //         let exist = false;
      //         for (let j = 0; j < this.participants.length; j++) {
      //           if (candidates[i].userId === this.participants[j].userId) {
      //             exist = true;
      //             break;
      //           }
      //         }
      //         if (!exist) {
      //           this.potentialMembers.push({
      //             key: this.potentialMembers.length,
      //             userId: candidates[i].userId,
      //             name: candidates[i].name,
      //             role: candidates[i].role,
      //           });
      //         }
      //       }
      //     } else {
      //       console.log(res.data.msg);
      //     }
      //   })
      //   .catch((err) => {
      //     console.log(err);
      //   });
      // this.inviteModal = true;
    },
    getTargetKeys() {
      // let mockData = [];
      // if (this.potentialMembers.length > 0) {
      //   for (var i = 0; i < this.participants.length; i++) {
      //     mockData.push({
      //       key: this.participants[i],
      //       name: this.potentialMembers[this.participants[i]].name,
      //       role: this.potentialMembers[this.participants[i]].role,
      //       userId: this.potentialMembers[this.participants[i]].userId,
      //     });
      //   }
      // }
      // return mockData;
    },
    memberRender(item) {
      // return item.type + " - " + item.name;
      // return `<span title="${item.name} - ${item.role}">${item.name} - ${item.role}</span>`;
    },
    filterMethod(data, query) {
      // return data.type.indexOf(query) > -1;
    },
    handleChange(newTargetKeys) {
      // this.participants = newTargetKeys;
    },
    inviteMembers() {
      // let selectResource = this.getTargetKeys();
    },
    gotoPersonalSpace(id) {
      if (id == this.$store.getters.userId) {
        this.$router.push({ name: "PersonalPage" });
      } else {
        this.$router.push({ name: "MemberDetailPage", params: { id: id } });
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
  max-width: 50%;
}
</style>