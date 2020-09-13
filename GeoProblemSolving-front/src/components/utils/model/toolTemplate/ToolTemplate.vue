
<style scoped>
.stepTitle {
  background-color: rgba(184, 184, 184, 0.596);
}
.addBtn {
  position: absolute;
  /* top: 20px; */
  right: 120px;
}
</style>
</style>
<template>
  <div>
    <Row>
      <Col span="20" offset="2">
        <Row>
          <div>
            <template-general :generalChange="generalPageChange" @generalInfo="getGeneralInfo"></template-general>
            <Modal v-model="generalChangeSave" width="360"></Modal>
          </div>
        </Row>
      </Col>
     
    </Row>
  </div>
</template>

<script>
import Avatar from "vue-avatar";
// import TemplateGeneral from "./TemplateGeneral";
// import ToolFromModel from "./ToolFromModel";
export default {
  components: {
    Avatar,
    // TemplateGeneral,
    // ToolFromModel,
  },
  data() {
    return {
      selectModelItem: {},
      inputUrl: "",
      toolInfoDetail: {},
      currentStep: 0,
      previousBtn: false,
      nextBtn: true,
      finishBtn: false,
      generalPageChange: true,
      generalChangeSave: false,
      toolInfo: {}
    };
  },

  watch: {
    currentStep(val) {
      if (val == 0) {
        this.previousBtn = false;
        this.nextBtn = true;
        this.generalPageChange = true;
      } else if (val == 1) {
        this.previousBtn = true;
        this.nextBtn = true;
        this.finishBtn = false;
        this.generalPageChange = false;
      } else if (val == 2) {
        this.nextBtn = false;
        this.finishBtn = true;
        this.generalPageChange = false;
      }
    }
  },
  methods: {
    getGeneralInfo(form) {
      this.toolInfo.toolName = form.name;
      this.toolInfo.description = form.description;
      this.toolInfo.provider = "testId";
      this.toolInfo.privacy = form.privacy;
    },
    getSelectModel(model) {
      this.selectModelItem = model;
      // this.toolInfo.toolUrl = //将选择的model的url
      console.log(this.selectModelItem);
    },
    // step上下翻
    next() {
      this.currentStep += 1;
    },

    previous() {
      this.currentStep -= 1;
    },

    //uploadToolInfo
    async finish() {
      let data = await this.axios.post(
        `/GeoProblemSolving/tool/create`,
        this.toolInfo
      );
      this.modelList = data.data.data;
    }
  }
};
</script>
