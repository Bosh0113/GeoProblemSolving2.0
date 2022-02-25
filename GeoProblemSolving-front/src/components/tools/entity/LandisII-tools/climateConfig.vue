<template>
  <div>
    <div id="collab-tool-head"></div>
    <div id="collab-tool-sidebar"></div>
    <div id="collab-tool-content">
      <div id="edit-mask" title="The other participant is operating."></div>

      <vue-scroll :ops="ops" style="height: calc(100vh - 40px)">
        <!-- coding for your tools // begin-->

        <!-- ClimateTimeSeries -->
        <div style="margin: 30px 20px">
          <Label class="labelTile">ClimateTimeSeries:</Label>
          <Select v-model="climateTimeSeries" style="width: 200px">
            <Option value="Monthly_AverageAllYears">Monthly_AverageAllYears</Option>
            <Option value="Monthly_RandomYears">Monthly_RandomYears</Option>
            <Option value="Monthly_SequencedYears">Monthly_SequencedYears</Option>
            <Option value="Daily_AverageAllYears">Daily_AverageAllYears</Option>
            <Option value="Daily_RandomYears">Daily_RandomYears</Option>
            <Option value="Daily_SequencedYears">Daily_SequencedYears</Option>
          </Select>
        </div>

        <!-- ClimateFile -->
        <div style="margin: 30px 20px">
          <Button
            type="info"
            style="width: 200px"
            :loading="boolClimateFile"
            @click="selectClimateFile"
          >
            <span v-if="!boolClimateFile"
              >Select climate file</span
            >
            <span v-else>Importing data...</span>
          </Button>
          <Label class="labelTile" style="margin-left: 20px"
            >ClimateFile:
            {{ climateFile.name + climateFile.suffix }}</Label
          >
        </div>

        <!-- ClimateFileFormat -->
        <div style="margin: 30px 20px">
          <Label class="labelTile">ClimateFileFormat:</Label>
          <Select v-model="climateFileFormat" style="width: 200px">
            <Option value="Monthly_Temp-C_Precip-mmMonth">Monthly_Temp-C_Precip-mmMonth</Option>
            <Option value="Monthly_Temp-K_Precip-kgm2Sec">Monthly_Temp-K_Precip-kgm2Sec</Option>
            <Option value="Monthly_Temp-K_Precip-mmMonth">Monthly_Temp-K_Precip-mmMonth</Option>
            <Option value="Daily_Temp-C_Precip-mmDay">Daily_Temp-C_Precip-mmDay</Option>
            <Option value="Daily_Temp-K_Precip-kgm2Sec">Daily_Temp-K_Precip-kgm2Sec</Option>
            <Option value="Daily_Temp-K_Precip-mmDay">Daily_Temp-K_Precip-mmDay</Option>
          </Select>
        </div>

        <!-- SpinUpClimateTimeSeries -->
        <div style="margin: 30px 20px">
          <Label class="labelTile">SpinUpClimateTimeSeries:</Label>
          <Select v-model="spinUpClimateTimeSeries" style="width: 200px">
            <Option value="Monthly_AverageAllYears">Monthly_AverageAllYears</Option>
            <Option value="Monthly_RandomYears">Monthly_RandomYears</Option>
            <Option value="Monthly_SequencedYears">Monthly_SequencedYears</Option>
            <Option value="Daily_AverageAllYears">Daily_AverageAllYears</Option>
            <Option value="Daily_RandomYears">Daily_RandomYears</Option>
            <Option value="Daily_SequencedYears">Daily_SequencedYears</Option>
          </Select>
        </div>

        <!-- SpinUpClimateFile -->
        <div style="margin: 30px 20px">
          <Button
            type="info"
            style="width: 200px"
            :loading="boolSpinUpClimateFile"
            @click="selectSpinUpClimateFile"
          >
            <span v-if="!boolSpinUpClimateFile"
              >Select spin up climate file</span
            >
            <span v-else>Importing data...</span>
          </Button>
          <Label class="labelTile" style="margin-left: 20px"
            >spinUpClimateFile:
            {{ spinUpClimateFile.name + spinUpClimateFile.suffix }}</Label
          >
        </div>

        <!-- SpinUpClimateFileFormat -->
        <div style="margin: 30px 20px">
          <Label class="labelTile">SpinUpClimateFileFormat:</Label>
          <Select v-model="spinUpClimateFileFormat" style="width: 200px">
            <Option value="Monthly_Temp-C_Precip-mmMonth">Monthly_Temp-C_Precip-mmMonth</Option>
            <Option value="Monthly_Temp-K_Precip-kgm2Sec">Monthly_Temp-K_Precip-kgm2Sec</Option>
            <Option value="Monthly_Temp-K_Precip-mmMonth">Monthly_Temp-K_Precip-mmMonth</Option>
            <Option value="Daily_Temp-C_Precip-mmDay">Daily_Temp-C_Precip-mmDay</Option>
            <Option value="Daily_Temp-K_Precip-kgm2Sec">Daily_Temp-K_Precip-kgm2Sec</Option>
            <Option value="Daily_Temp-K_Precip-mmDay">Daily_Temp-K_Precip-mmDay</Option>
          </Select>
        </div>
        <Divider></Divider>
        <Button
          @click="submit()"
          type="success"
          style="margin: 0 20px 20px; width: 200px"
          >Submit</Button
        >
        <Button
          @click="resetAll()"
          type="warning"
          style="margin: 0 20px 20px; width: 200px"
          >Clear All</Button
        >
        <!-- // end -->
      </vue-scroll>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      ops: {
        bar: {
          background: "#808695",
        },
      },
      // basic info
      activityInfo: {},
      userInfo: {},
      resources: [],
      //
      boolClimateFile: false,
      boolSpinUpClimateFile: false,
      climateTimeSeries: "",
      climateFile: {},
      climateFileFormat: "",
      spinUpClimateTimeSeries: "",
      spinUpClimateFile: {},
      spinUpClimateFileFormat: "",
    };
  },
  created() {},
  mounted() {
    // 加载协同组件
    loadCollabComponent();
    this.getStepInfo();
  },
  methods: {
    getStepInfo() {
      if (componentStatus) {
        // 获取数据
        this.activityInfo = activityInfo;
        this.userInfo = userInfo;
        this.resources = resources;

        // 绑定函数
        buildSocketChannel(
          this.getSocketOperation,
          this.getSocketData,
          this.getSocketComputation
        );
        loadResChannel = this.loadResources;
      } else {
        let _this = this;
        setTimeout(function () {
          _this.getStepInfo();
        }, 1000);
      }
    },
    selectClimateFile() {
      this.boolClimateFile = !this.boolClimateFile;
      this.boolSpinUpClimateFile = false;
    },
    selectSpinUpClimateFile() {
      this.boolClimateFile = false;
      this.boolSpinUpClimateFile = !this.boolSpinUpClimateFile;
    },
    getSocketComputation(data) {},
    getClimateConfigFile(data) {},
    getSocketData(data) {},
    loadResources(resList) {
      for (let i = 0; i < resList.length; i++) {
        // your function
        if (this.boolClimateFile) {
          this.climateFile = resList[0];
          this.boolClimateFile = false;
        } else if (this.boolSpinUpClimateFile) {
          this.spinUpClimateFile = resList[0];
          this.boolSpinUpClimateFile = false;
        }
        //

        let send_content = {
          type: "resource",
          sender: this.userInfo.userId,
          behavior: "select",
          content: this.selectData,
        };
        sendCustomOperation(send_content);
      }
    },
    submit() {},
  },
};
</script>
<style scoped>
.labelTile {
  font-size: larger;
  font-weight: bold;
  margin: 5px 0;
}
</style>