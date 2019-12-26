<template>
  <div class="main">
    <el-row class="title">
      <el-col>
        <i class="fa fa-cogs"></i>
        {{modelInstance.name}}
      </el-col>
    </el-row>

    <el-row class="des">
      <el-col offset="22">
        <div class="modelState">
          <i class="el-icon-s-flag"></i>
          {{modelInstance.statusEnum}}
        </div>
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
              <div class="itemContent">
                <el-col :span="16">{{inEvent.des}}</el-col>

                <el-col :span="3" :offset="2">
                  <el-tooltip
                    :content="inEvent.dataTemplate.tooltip"
                    placement="top"
                    effect="light"
                  >
                    <component
                      :disabled="true"
                      :is="inEvent.dataTemplate.type"
                      :initDataTemplate="inEvent.dataTemplate"
                    ></component>
                  </el-tooltip>
                </el-col>
              </div>
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
              <div class="itemContent">
                <el-col :span="16">{{outEvent.des}}</el-col>

                <el-col :span="3" :offset="2">
                  <el-tooltip
                    :content="outEvent.dataTemplate.tooltip"
                    placement="top"
                    effect="light"
                  >
                    <component
                      :disabled="true"
                      :is="outEvent.dataTemplate.type"
                      :initDataTemplate="outEvent.dataTemplate"
                    ></component>
                  </el-tooltip>
                </el-col>
              </div>
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
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";

export default {
  name: "invoke",
  components: {
    file,
    parameter_input,
    parameter_select,
    parameter_slider,
    parameter_table,
    parameter_range
  },
  props: {
    pid: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      modelInstance: {
        modelitem: {
          stateList: []
        }
      }
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
    },
    modelItem() {
      return this.modelInstance.modelItem;
    }
  },
  methods: {
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
    async getData() {
      // let modelInstance = await get("/mc/modelinstance/{id}", null, {
      //   id: this.pid
      // });
      // this.modelInstance = modelInstance;

      this.axios
        .get("/GeoProblemSolving/modelitem/getModelInstance/" + this.pid)
        .then(res => {
          if (res.data != "None") {
            this.modelInstance = res.data;
          }
        })
        .catch(err => {});
      this.modelInstance = modelInstance;
    }
  },
  created() {
    this.getData();
  }
};
</script>

<style>
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

.invoke-btn {
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
