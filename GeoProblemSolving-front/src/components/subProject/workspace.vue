<style scoped>
#workCard >>> .ivu-card-body {
  padding: 5px;
}
.btnHoverRed:hover {
  background-color: #ed4014;
  color: white;
}
</style>

<template>
  <div>
    <Row>
      <Col span="23" offset="1">
        <Row>
          <Col span="22" offset="1" id="workCard">
            <data-list :stepInfo="stepInfo" :userRole="userRole" :projectInfo="projectInfo"></data-list>
          </Col>
        </Row>
      </Col>
    </Row>
    <div style="margin:5px 0 5px 80px;text-align:center">
      <Button
        class="btnHoverRed"
        @click="resetSubProjectTypeModalShow()"
        v-if="permissionIdentity('subproject_workspace_type_manage')"
      >Reset workspace type</Button>
    </div>
    <Modal v-model="resetSubProjectTypeModel" title="Reset workspace type">
      <h2>Are you sure to reset the workspace type?</h2>
      <div slot="footer">
        <Button type="primary" @click="resetSubProjectType()">OK</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
import dataList from "./../workingSpace/noStep/dataList";
import toolContainer from "./../workingSpace/functionSteps/utils/toolContainer";
export default {
  props: ["subProjectInfo", "userRole", "projectInfo"],
  components: {
    dataList,
    toolContainer
  },
  data() {
    return {
      stepInfo: {},
      resetSubProjectTypeModel: false
    };
  },
  created() {
    this.getStepInfo();
  },
  methods: {
    permissionIdentity(operation) {
      if (
        this.projectInfo.permissionManager != undefined &&
        operation === "subproject_workspace_type_manage"
      ) {
        if (
          this.userRole == "PManager" &&
          this.projectInfo.permissionManager.subproject_workspace_type_manage
            .project_manager
        ) {
          return true;
        } else if (
          this.userRole == "Manager" &&
          this.projectInfo.permissionManager.subproject_workspace_type_manage
            .subproject_manager
        ) {
          return true;
        } else if (
          this.userRole == "Member" &&
          this.projectInfo.permissionManager.subproject_workspace_type_manage.member
        ) {
          return true;
        }
      }
    },
    getStepInfo() {
      this.axios
        .get(
          "/PExploration/step/inquiry?key=stepId&value=" +
            this.subProjectInfo.stepId
        )
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data != "None" && res.data != "Fail") {
            this.$set(this, "stepInfo", res.data[0]);
          }
        })
        .catch(err => {});
    },
    resetSubProjectTypeModalShow() {
      this.resetSubProjectTypeModel = true;
    },
    resetSubProjectType() {
      let obj = new URLSearchParams();
      obj.append("subProjectId", this.subProjectInfo.subProjectId);
      obj.append("type", "");
      obj.append("stepId", "");
      this.axios
        .post("/PExploration/subProject/update", obj)
        .then(res => {
          this.resetProjectTypeModel = false;
          if (res.data == "Offline") {
            parent.location.href = "/PExploration/login";
          } else if (res.data != "Fail") {
            this.$store.commit("setSubProjectInfo", res.data);
            this.$emit("changeSubProjectInfo", res.data);
          } else {
            this.$Message.error("Set type failed.");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    }
  }
};
</script>
