<template>
  <Row>
    <Col span="24">
      <div style="text-align: center; margin-top: 20px">
        <div
          style="
            max-width: 80%;
            text-align: left;
            display: inline-block;
            margin-buttom: 2%;
          "
        >
          <span style="margin: 25px 0; font-size: 20px" title="Activity name"
            >{{activityInfo.name}}</span
          >
          <span style="margin: 0px 10px; font-size: 14px;overflow: hidden;width: 90%;display: inline-block;text-overflow: ellipsis;" title="Activity description">{{
            activityInfo.description
          }}</span>
        </div>
      </div>
      <div style="text-align: center; margin-top: 10px; color: grey">
        Activity contains workspaces to support collaborative geo-problem
        solving. Here provides two activity types for different solutions of
        geo-problems.
      </div>
      <Row
        type="flex"
        justify="space-around"
        class="code-row-bg"
        style="margin-top: 20px"
      >
        <Col span="10">
          <Card style="min-height: 350px">
            <div style="text-align: center; margin-top:10px">
              <img
                src="../../../assets/images/startWork.png"
                style="height: 150px"
              />
            </div>
            <div style="margin: 10px 0; text-align: center">
              <h3>Single-activity type</h3>
            </div>
            <div style="padding: 0 5%; min-height: 85px; margin-left: 10%;">
              <ul>
                <li>A individual workspaces</li>
                <li>Start working directly</li>
                <li>Accessible participatory tools and resources</li>
              </ul>
            </div>
            <div style="text-align: center; margin-top: 2%">
              <h3 v-if="operationPermissionIdentity(this.activityInfo.permission, this.userRole, 'manage_workspace_type')">
                Select this type ->
                <a @click="selectTypeModalShow('Activity_Unit')">Go</a>
              </h3>
            </div>
          </Card>
        </Col>
        <Col span="10">
          <Card style="min-height: 350px">
            <div style="text-align: center; margin-top:10px;">
              <img
                src="../../../assets/images/designWorkflow.png"
                style="height: 150px"
              />
            </div>
            <div style="margin: 10px 0; text-align: center">
              <h3>Multi-acitivites type</h3>
            </div>
            <div style="padding: 0 5%; min-height: 85px; margin-left: 10%;">
              <ul>
                <li>Several activities for different purposes</li>
                <li>Hierarchical activity management</li>
                <li>Task coordination and participant management</li>
              </ul>
            </div>
            <div style="text-align:center;margin-top:2%">
              <h3 v-if="operationPermissionIdentity(this.activityInfo.permission, this.userRole, 'manage_workspace_type')">
                Select this type ->
                <a @click="selectTypeModalShow('Activity_Group')">Go</a>
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
      cancel-text="Think again..."
    >
      <h2>Do you want to select this type?</h2>
      <div v-if="selectType == 'Activity_Unit'" style="margin-top: 20px">
        <Label>Select purpose: </Label>
        <Select
          style="margin-top: 10px"
          v-model="purpose"
          placeholder="Select the purpose of this activity"
          readonly
        >
          <Option v-for="item in purposes" :key="item.index" :value="item">{{
            item
          }}</Option>
        </Select>
      </div>
    </Modal>
  </Row>
</template>
<script>
export default {
  props: [
    "activityInfo",
    "userInfo"
  ],
  data() {
    return {
      selectType: "Activity_Default",
      selectTypeModal: false,
      userRole: "visitor",
      purposes: [
        "Context definition & resource collection",
        "Data processing",
        "Data visualization",
        "Geo-analysis model construction",
        "Model effectiveness evaluation",
        "Geographical simulation",
        "Data analysis",
        "Decision making",
        "Others",
      ],
      purpose: "",
    };
  },
  created() {
    this.currentRoleIdentity();
  },
  methods: {
    currentRoleIdentity() {
      this.userRole = this.userRoleApi.roleIdentify(
        this.activityInfo.members,
        this.userInfo.userId
      );
    },
    operationPermissionIdentity(permission, role, operation) {
      return this.userRoleApi.permissionIdentity(
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
      if (this.selectType == "Activity_Group") {
        data.children = [];
      } else if (this.selectType == "Activity_Unit") {
        data.purpose = this.purpose;
      }
      if (this.activityInfo.level == 1) {
        url = "/GeoProblemSolving/subproject/" + aid;
      } else if (this.activityInfo.level > 1) {
        url = "/GeoProblemSolving/activity/" + aid;
      } else {
        url = "/GeoProblemSolving/project/" + aid;
      }
      this.axios
        .put(url, data)
        .then((res) => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data.code == 0) {
            if(res.data.data.level == 0){
              this.activityInfo = res.data.data;
            }
            this.$emit("typeChanged", this.selectType);
          } else {
            this.$Notice.info({ title: "Result", desc: res.data.msg });
          }
        })
        .catch((err) => {
          throw err;
        });
      this.activityEditModal = false;
    },
  },
};
</script>
