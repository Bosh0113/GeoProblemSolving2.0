<template>
  <div class="main">
    <el-button @click="getAllRecords">getAllRecords</el-button>
    <el-button @click="loadingClose">close</el-button>
    <el-row class="title">
      <el-col>
        <i class="fa fa-cogs"></i>
        {{modelItem.name}}
      </el-col>
    </el-row>

    <el-row class="des">
      <el-col>
        <p>{{modelItem.des}}</p>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="2" :offset="16" class="save-btn">
        <el-button plain type="primary" @click="invoke">
          <i class="el-icon-setting"></i>&nbsp;invoke
        </el-button>
      </el-col>
      <el-col :span="2" :offset="18" class="save-btn">
        <el-button plain type="primary" @click="saveRecords">
          <i class="el-icon-setting"></i>&nbsp;save
        </el-button>
      </el-col>
    </el-row>

    <el-tabs v-model="activeTab" tab-position="left">
      <el-tab-pane
        v-for="(state,index) in modelItem.stateList"
        :label="state.name"
        :name="state.name"
        :key="index"
      >
        <div class="state-desc">{{state.des}}</div>

        <div class="params-group">
          <el-row class="title">Input</el-row>
          <div class="items">
            <el-row
              v-for="(inEvent,inEventIndex) in inEventList(state)"
              :key="inEventIndex"
              class="item"
            >
              <el-row>
                <div class="itemContent">
                  <el-col :span="16">
                    <span class="event-name">
                      <span v-show="!inEvent.isOptional" style="color:red">*</span>
                      {{inEvent.name}}
                    </span>
                  </el-col>
                  <el-col :span="3" :offset="2">
                    <el-tooltip
                      :content="inEvent.dataTemplate.tooltip"
                      placement="top"
                      effect="light"
                    >
                      <component
                        :disabled="componentDisables"
                        :is="inEvent.dataTemplate.type"
                        :initDataTemplate="inEvent.dataTemplate"
                      ></component>
                    </el-tooltip>
                  </el-col>
                </div>
              </el-row>

              <el-row>
                <p class="event-desc" :title="inEvent.des">{{inEvent.des}}</p>
              </el-row>
            </el-row>
          </div>
        </div>

        <div class="params-group" v-if="outEventList(state).length">
          <el-row class="title">Output</el-row>
          <div class="items">
            <el-row
              v-for="(outEvent,outEventIndex) in outEventList(state)"
              :key="outEventIndex"
              class="item"
            >
              <el-row>
                <el-col :span="16">{{outEvent.name}}</el-col>
                <el-col :span="3" :offset="2">
                  <div>
                    <el-button
                      plain
                      type="warning"
                      icon="el-icon-download"
                      v-show="outEvent.dataTemplate.value!=''"
                      @click="download(outEvent.dataTemplate.value)"
                    ></el-button>
                  </div>
                </el-col>
              </el-row>

              <el-row>
                <p class="event-desc" :title="outEvent.des">{{outEvent.des}}</p>
              </el-row>
            </el-row>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import file from "./../dataTemplate/File";
import parameter_input from "./../dataTemplate/ParameterInput";
import parameter_select from "./../dataTemplate/ParameterSelect";
import parameter_slider from "./../dataTemplate/ParameterSlider";
import parameter_table from "./../dataTemplate/ParameterTable";
import parameter_range from "./../dataTemplate/ParameterRange";

export default {
  data() {
    return {
      id: this.$route.query.id,
      modelItem: {},
      modelInstance: {},
      timer: {},
      fullscreenLoading: "",
      componentDisables: false,
      inputFile: false,
      userId: "testUserId1234",
      stepId: "testStepId1234",
      recordList: []
    };
  },

  computed: {
    activeTab: {
      get() {
        return this.modelItem.stateList
          ? this.modelItem.stateList[0].name
          : "defaultState";
      },
      set() {}
    }
  },

  methods: {
    test() {
      this.save();
    },

    download(value) {
      let url = `/GeoProblemSolving/dataItem/download/${value}`;
      window.open(url);
    },

    async init() {
      let data = await this.axios.get(
        "/GeoProblemSolving/modelItem/getModelItem/" + this.id
      );
      let modelItem = data.data.data;

      //利用defaultValue对UI进行初始化
      modelItem.stateList.forEach(state => {
        state.eventList.forEach((event, index, eventList) => {
          let dataTemplate = event.dataTemplate;
          if (
            dataTemplate.value == "" ||
            dataTemplate.value == null ||
            dataTemplate.value == undefined
          ) {
            if (
              dataTemplate.defaultValue != "" &&
              dataTemplate.defaultValue != null &&
              dataTemplate.defaultValue != undefined
            ) {
              if (dataTemplate.type === "parameter_slider") {
                //TODO 无奈之举，因为后台是存储的字符串
                eventList[index].dataTemplate.value = new Number(
                  dataTemplate.defaultValue
                ).valueOf();
              } else {
                eventList[index].dataTemplate.value = dataTemplate.defaultValue;
              }
            }
          }
        });
      });
      this.modelItem = modelItem;
    },

    inEventList(state) {
      return state.eventList.filter(value => {
        return value.ioFlagEnum === "INPUT";
      });
    },

    outEventList(state) {
      return state.eventList.filter(value => {
        return value.ioFlagEnum === "OUTPUT";
      });
    },

    testInputValue() {
      let modelItem = this.modelItem;
      try {
        modelItem.stateList.forEach(state => {
          state.eventList.forEach((event, index, eventList) => {
            let dataTemplate = event.dataTemplate;
            let ioFlag = event.ioFlagEnum;
            if (dataTemplate.type == "file" && ioFlag == "INPUT") {
              if (
                dataTemplate.value == "" ||
                dataTemplate.value == null ||
                dataTemplate.value == undefined
              ) {
                this.inputFile = false;
                throw new Error("breakForEach"); //终止foreach循环
              } else {
                this.inputFile = true;
              }
            }
          });
        });
      } catch (e) {
        if (e.message != "breakForEach") throw e;
      }
    },

    async invoke() {
      this.testInputValue();
      if (this.inputFile == false) {
        this.$notify.error({
          title: "Error",
          message: "There is no input file !"
        });
      } else {
        this.loading();
        let data = await this.axios.post(
          "/GeoProblemSolving/modelItem/addModelInstance",
          {
            name: "`${this.modelItem.name}` + `${this.$store.state.user.name}`",
            modelItem: this.modelItem
          }
        );
        let modelInstance = data.data.data;
        let modelInstanceId = data.data.data.id;

        await this.axios.post(
          `/GeoProblemSolving/modelItem/modelInstance/${modelInstanceId}/invoke`
        );

        this.timer = setInterval(async () => {
          console.log("轮询");
          if (modelInstance.statusEnum == "FINISH") {
            this.loadingClose();
            console.log("完成");
            clearInterval(this.timer);
            this.modelItem = modelInstance.modelItem;
            this.modelInstance = modelInstance;
            console.log(this.modelInstanceId);
            this.saveRecords(modelInstance.id); //save model instance records
            console.log(this.modelInstance);
            return;
          } else {
            let data = await this.axios.get(
              `/GeoProblemSolving/modelItem/getModelInstance/${modelInstanceId}`
            );
            modelInstance = data.data.data;
            this.modelInstance = modelInstance;
            // console.log(this.modelInstance);
          }
        }, 2000);

        this.componentDisables = true;
      }
    },

    loading() {
      this.fullscreenLoading = this.$loading({
        lock: true,
        text: "Calculating",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
    },

    loadingClose() {
      this.fullscreenLoading.close();
    },

    async saveRecords(modelInstanceId) {
      let data = await this.axios.post(
        "/GeoProblemSolving/modelItem/saveRecord",
        {
          modelInstanceId: modelInstanceId,
          userId: this.userId,
          stepId: this.stepId
        }
      );
    },

    async getAllRecords() {
      let stepId = this.stepId;
      //根据用户ID查找所欲的modelInstance
      let data = await this.axios.get(
        `/GeoProblemSolving/modelItem/getAllRecords/${stepId}`
      );
      if (data.status == "200") {
        console.log("返回记录成功");
        // this.recordList = data.data.data;
        let recordList = data.data.data;
        console.log(recordList);
        for (let i = 0; i < recordList.length ; i++) {
          let modelInstanceId = recordList[i].modelInstanceId;
          console.log(modelInstanceId);
          let item = await this.axios.get(
            `/GeoProblemSolving/modelItem/getModelInstance/${modelInstanceId}`
          );
          console.log(item);
          this.recordList.push(item);
        }
      } else {
        console.log("返回记录失败");
      }
      console.log(this.recordList);
    }
  },
  created() {
    this.init();
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  components: {
    file,
    parameter_input,
    parameter_select,
    parameter_slider,
    parameter_table,
    parameter_range
  }
};
</script>

<style scoped>
.main {
  position: relative;
}
.title {
  position: relative;
  font-size: 18px;
  padding: 0px 0px 30px 0px;
}
.title i {
  font-size: 30px !important;
}

.el-button {
  padding: 12px;
}
.params-group {
  /* position: relative; */
  padding-bottom: 30px;
}
.params-group > .title {
  font-style: italic;
  font-size: 16px;
  padding-bottom: 10px;
  border-bottom: solid 2px #05889c;
  color: #05889c;
  font-weight: 600;
}

.params-group > .items > .item {
  padding: 15px 0px 5px 0px;
  border-bottom: solid 0.5px rgba(153, 153, 153, 0.671);
  line-height: 2;
  color: #696969;
  font-size: 16px;
}

.params-group > .items > .item > .itemContent {
  padding: 0px 0px 0px 50px;
}

.save-btn {
  margin-top: 20px;
}
.state-desc {
  margin: 0px 0px 15px 0px;
  padding: 4px 0px;
  line-height: 2;
  background-color: #f3f3f3;
  font-size: 16px;
  font-style: italic;
}
.el-tabs__item {
  font-size: 16px;
}
.el-tabs__item:hover {
  color: #00bbd8;
  background-color: #b5dce244;
}
.el-tabs__item.is-active {
  color: #00bbd8;
}
.el-tabs__active-bar {
  background-color: #00bbd8;
}
.item:hover {
  background-color: #c4f0f734;
}
.modelState {
  color: rgb(112, 30, 30);
  font-size: 18px;
  font-family: "微软雅黑";
  margin: 1% 0;
  font-weight: 600;
}
</style>