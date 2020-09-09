<template>
  <Row>
    <Col span="24">
      <div style="text-align: center; margin-top: 20px">
        <div style="max-width:80%;text-align: left;display: inline-block;margin-buttom:2%">
          <span style="margin:25px 0; font-size:28px">Activity</span>
          <span
            style="margin: 0px 10px;font-size: 14px;"
          >contains workspaces to support collaborative geo-problem solving. Here provides two activity types for different solutions of geo-problems.</span>
        </div>
      </div>
      <Row type="flex" justify="space-around" class="code-row-bg" style="margin-top:25px">
        <Col span="9">
          <Card style="min-height: 400px;">
            <div style="text-align:center;">
              <img src="../../../assets/images/startWork.png" style="height:150px" />
            </div>
            <div style="margin:10px 0;text-align:center;">
              <h3>Single-activity type</h3>
            </div>
            <div style="padding: 0 5%;min-height: 85px;">
              <ul>
                <li>A individual workspaces</li>
                <li>Start working directly</li>
                <li>Accessible participatory tools and resources</li>
              </ul>
            </div>
            <!-- <div style="text-align:center;margin-top:2%" v-if="permissionIdentity(activityInfo.permission, userRole, 'manage_workspace_type')"> -->
            <div style="text-align:center;margin-top:2%">
              <h3>
                Set this content as
                <a @click="selectTypeModalShow('Activity_Unit')">Do it right now</a>
              </h3>
            </div>
          </Card>
        </Col>
        <Col span="9">
          <Card style="min-height: 400px;">
            <div style="text-align:center;">
              <img src="../../../assets/images/designWorkflow.png" style="height:150px" />
            </div>
            <div style="margin:10px 0;text-align:center;">
              <h3>Multi-acitivites type</h3>
            </div>
            <div style="padding: 0 5%;min-height: 85px;">
              <ul>
                <li>Several activities for different purposes</li>
                <li>Hierarchical activity management</li>
                <li>Task coordination and participant management</li>
              </ul>
            </div>
            <!-- <div style="text-align:center;margin-top:2%" v-if="permissionIdentity(activityInfo.permission, userRole, 'manage_workspace_type')">                 -->
            <div style="text-align:center;margin-top:2%">
              <h3>
                Set this content as
                <a
                  @click="selectTypeModalShow('Activity_Group')"
                >Design workflow graph</a>
              </h3>
            </div>
          </Card>
        </Col>
      </Row>
    </Col>
    <Modal
      v-model="selectTypeModal"
      title="Set the workspace type"
      @on-ok="setType()"
      ok-text="Yes"
      cancel-text="Think again"
    >
      <h2>Do you want to select this type?</h2>
    </Modal>
  </Row>
</template>
<script>
import * as userRoleJS from "./../../../api/userRole.js";
export default {
  props: ["activityInfo", "userInfo"],
  data() {
    return {
      selectType: "Activity_Default",
      selectTypeModal: false,
      userRole: "visitor",
    };
  },
  mounted() {
    this.roleIdentity();
  },
  methods: {
    roleIdentity() {
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
    selectTypeModalShow(type) {
      this.selectType = type;
      this.selectTypeModal = true;
    },
    preEditting(activity) {
      let children = [];
      if (activity.children != undefined) {
        for (let i = 0; i < activity.children.length; i++) {
          children.push(activity.children[i].aid);
        }
        activity.children = children;
      }
      return activity;
    },
    setType() {
      let url = "";
      let aid = this.activityInfo.aid;
      let data = this.preEditting(this.activityInfo);
      data.type = this.selectType;
      if(this.selectType == "Activity_Group"){
          data.children = [];
      }
      if (this.activityInfo.level == 1) {
        url = "/GeoProblemSolving/subproject?aid=" + aid;
      } else if (this.activityInfo.level > 1) {
        url = "/GeoProblemSolving/activity?aid=" + aid;
      }
      this.axios
        .put(url, data)
        .then((res) => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data.code == 0) {
            this.$emit("typeChanged", this.selectType);
          } else {
            this.$Notice.info({ title: "Result", desc: res.data.msg });
          }
        })
        .catch((err) => {
          console.log(err);
        });
      this.activityEditModal = false;
    },
  },
};
</script>
