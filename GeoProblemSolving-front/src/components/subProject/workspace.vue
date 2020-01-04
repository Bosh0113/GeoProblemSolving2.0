<style scoped>
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
          <Col span="22" offset="1">
            <Collapse simple v-model="unfold">
              <Panel name="tool">
                Toolbox
                <tool-container slot="content" :stepInfo="stepInfo" :userRole="userRole"></tool-container>
              </Panel>
              <Panel name="data">
                Data list
                <data-list slot="content" :stepInfo="stepInfo" :userRole="userRole"></data-list>
              </Panel>
            </Collapse>
          </Col>
        </Row>
      </Col>
    </Row>
    <div style="margin:5px 0 5px 80px;text-align:center">
      <Button class="btnHoverRed" @click="resetSubProjectTypeModalShow()">Reset sub-project's type</Button>
    </div>
    <Modal v-model="resetSubProjectTypeModel" title="Reset sub-project's type">
      <h2>Are you sure you want to reset the sub-project type?</h2>
      <div slot="footer">
        <Button type="primary" @click="resetSubProjectType()">Submit</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
import dataList from "./../workingSpace/noStep/dataList";
import toolContainer from "./../workingSpace/functionSteps/utils/toolContainer";
export default {
  props: ["subProjectInfo", "userRole"],
  components: {
    dataList,
    toolContainer
  },
  data() {
    return {
      stepInfo: {},
      unfold: ["tool", "data"],
      resetSubProjectTypeModel: false
    };
  },
  created() {
    this.getStepInfo();
  },
  methods: {
    getStepInfo() {
      this.axios
        .get(
          "/GeoProblemSolving/step/inquiry?key=stepId&value=" +
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
        .post("/GeoProblemSolving/subProject/update", obj)
        .then(res => {
          this.resetProjectTypeModel = false;
          if (res.data == "Offline") {
            parent.location.href = "/GeoProblemSolving/login";
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
