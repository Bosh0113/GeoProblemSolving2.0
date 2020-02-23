<style scoped>
.btnHoverGray:hover {
  background-color: #808695;
  color: white;
}
</style>
<template>
  <Row>
    <Col span="23" offset="1" style="margin-top:20px">
      <div :style="{height:contentHeight+'px'}">
        <Row type="flex" justify="space-around">
          <template v-if="$store.getters.userInfo.userId == scopeInfo.managerId">
            <div style="width: 80%; height: 25px">
              <span
                style="font-weight: bold; font-size:16px"
              >Procedure of iterative attempts for geo-problem solving</span>
              <Button
                v-if="workspaceBtn"
                type="primary"
                @click="gotoworkspace()"
                size="small"
                icon="md-globe"
                title="Enter the workspace"
                style="float:right;margin-left:10px;"
              >Workspace</Button>
              <Button
                v-else
                type="default"
                size="small"
                icon="md-globe"
                title="Enter the workspace"
                style="float:right;margin-left:10px;cursor:default"
              >Workspace</Button>
              <Button
                v-if="nodePositionBtn && procedureDrag"
                type="warning"
                @click="editPosition()"
                size="small"
                icon="md-git-commit"
                title="Adjust the postion of nodes"
                style="float:right;margin-left:10px;"
              >Move node</Button>
              <Button
                v-else-if="nodePositionBtn && !procedureDrag"
                type="warning"
                @click="editPosition()"
                size="small"
                icon="md-git-network"
                title="Move the postion of procedure"
                style="float:right;margin-left:10px;"
              >Move procedure</Button>
              <Button
                v-else
                type="default"
                size="small"
                icon="md-git-commit"
                title="Adjust the postion of nodes"
                style="float:right;margin-left:10px;cursor:default"
              >Move node</Button>
              <template v-show="userRole == 'Manager'">
                <Button
                  v-if="removeBtn"
                  type="error"
                  size="small"
                  @click="removeStep()"
                  icon="md-remove"
                  title="Remove the activity"
                  style="float:right;margin-left:10px"
                >Remove</Button>
                <Button
                  v-else
                  type="default"
                  size="small"
                  icon="md-remove"
                  title="Remove the activity"
                  style="float:right;margin-left:10px; cursor:default"
                >Remove</Button>
                <Button
                  v-if="activeBtn"
                  type="success"
                  size="small"
                  @click="activateStep()"
                  icon="md-bulb"
                  title="Active the activity"
                  style="float:right;margin-left:10px;"
                >Active</Button>
                <Button
                  v-else
                  type="default"
                  size="small"
                  icon="md-bulb"
                  title="Active the activity"
                  style="float:right;margin-left:10px; cursor:default"
                >Active</Button>
              </template>
              <Button
                v-if="addBtn"
                type="info"
                size="small"
                @click="addNewStep()"
                icon="md-add"
                v-show="userRole == 'Manager'"
                title="Add a new step"
                style="float:right;margin-left:10px"
              >Add</Button>
              <Button
                v-else
                type="default"
                size="small"
                icon="md-add"
                v-show="userRole == 'Manager'"
                title="Add a new step"
                style="float:right;margin-left:10px; cursor:default"
              >Add</Button>
            </div>
          </template>
          <div
            style="width:90%;margin-top:20px;padding:15px;background-color:#f8f8f9"
            :style="{height:contentHeight-120+'px'}"
            id="steps"
          ></div>
          <h3 style="margin-top:10px">Steps for solving geo-problem can be created and deleted here.</h3>
          <h3
            style="margin-top:10px"
          >Double click the node, and you can enter the corresponding workspace.</h3>
          <div
            style="width:100%;text-align:center;margin-top:10px"
            v-if="scopeType == 'subproject'"
          >
            <Button
              class="btnHoverGray"
              @click="resetSubProjectTypeModalShow()"
            >Reset workspace type</Button>
          </div>
          <div style="width:100%;text-align:center;margin-top:100px" v-if="scopeType == 'project'">
            <Button class="btnHoverGray" @click="resetProjectTypeModalShow()">Reset workspace type</Button>
          </div>
        </Row>
      </div>
    </Col>
    <Modal
      v-model="slctActivateModal"
      title="Select an active activity"
      @on-ok="selectAActivity('activate')"
      ok-text="OK"
      cancel-text="Cancel"
    >
      <div
        style="font-size:14px;"
      >There are {{activeStepInfo.length}} active activities. Please choose the needed activities and activate the next activity.</div>
      <CheckboxGroup v-model="aActivitiesName" style="margin-top:10px">
        <Checkbox v-for="item in activeStepInfo" :key="item.index" :label="item.name"></Checkbox>
      </CheckboxGroup>
    </Modal>
    <Modal
      v-model="gotoworkModal"
      title="Select an active activity"
      @on-ok="selectAActivity('workspace')"
      ok-text="OK"
      cancel-text="Cancel"
    >
      <div
        style="font-size:14px;"
      >There are {{activeStepInfo.length}} active activities. Please choose an activity and go to the worksapce.</div>
      <RadioGroup v-model="workspaceName">
        <Radio
          style="margin-top:10px"
          v-for="item in activeStepInfo"
          :key="item.index"
          :label="item.name"
        ></Radio>
      </RadioGroup>
    </Modal>
    <Modal
      v-model="delModal"
      title="Delete this process"
      @on-ok="delStepGraph"
      ok-text="OK"
      cancel-text="Cancel"
    >
      <p>Do you really want to delete this step?</p>
    </Modal>
    <Modal v-model="resetSubProjectTypeNotice" title="Reset workspace type">
      <h2>Please confirm that all nodes have been deleted.</h2>
      <div slot="footer">
        <Button type="primary" @click="resetSubProjectTypeNotice=false">OK</Button>
      </div>
    </Modal>
    <Modal v-model="resetSubProjectTypeModel" title="Reset workspace type">
      <h2>Are you sure to reset the workspace type?</h2>
      <div slot="footer">
        <Button type="primary" @click="resetSubProjectType()">OK</Button>
      </div>
    </Modal>
    <Modal v-model="resetProjectTypeNotice" title="Reset workspace type">
      <h2>Please confirm that all nodes have been deleted.</h2>
      <div slot="footer">
        <Button type="primary" @click="resetProjectTypeNotice=false">OK</Button>
      </div>
    </Modal>
    <Modal v-model="resetProjectTypeModel" title="Reset workspace type">
      <h2>Are you sure to reset the workspace type?</h2>
      <div slot="footer">
        <Button type="primary" @click="resetProjectType()">OK</Button>
      </div>
    </Modal>
    <Modal v-model="activityInfoModal" title="Information of the activity" footer-hide>
      <div>
        <label style="margin-left:20px">Activity name:</label>
        <Input v-model="showActivityInfo.name" style="width: 300px;margin-left:10px" readonly />
      </div>
      <div style="margin-top:20px">
        <label style="margin-left:20px">Activity type:</label>
        <Input v-model="showActivityInfo.type" style="width:300px;margin-left:17px" readonly />
      </div>
    </Modal>
    <Modal
      width="800px"
      v-model="createStepModal"
      title="Create a new step"
      :styles="{top: '20px'}"
    >
      <Carousel
        v-if="createStepModal"
        v-model="stepValue"
        :dots="setting.dots"
        :trigger="setting.trigger"
        :arrow="setting.arrow"
      >
        <CarouselItem>
          <div style="height:380px;width:640px;margin-left:50px">
            <Form
              ref="formValidate1"
              :model="formValidate1"
              :rules="ruleValidate1"
              :label-width="120"
            >
              <FormItem label="Name:" prop="stepTitle">
                <Input v-model="formValidate1.stepTitle" placeholder="Enter step name" />
              </FormItem>
              <FormItem label="Step type:" prop="stepType" style="margin-top:40px">
                <Select v-model="formValidate1.stepType" placeholder="Select step type">
                  <Option v-for="item in typeList" :key="item.index" :value="item">{{ item }}</Option>
                </Select>
              </FormItem>
              <FormItem label="Description:" prop="result" style="margin-top:50px">
                <Input
                  v-model="formValidate1.result"
                  type="textarea"
                  :rows="10"
                  placeholder="Enter something..."
                />
              </FormItem>
            </Form>
          </div>
        </CarouselItem>
        <CarouselItem>
          <div style="margin-left:75px">
            <div style="font-size:14px">Select needed data:</div>
            <Transfer
              :data="inheritResource"
              :target-keys="targetKeys"
              :list-style="listStyle"
              :render-format="resourceRender"
              :titles="['This process', 'The next process']"
              filter-placeholder="Enter key words..."
              filterable
              :filter-method="filterMethod"
              @on-change="handleChange"
            ></Transfer>
          </div>
        </CarouselItem>
      </Carousel>
      <div slot="footer">
        <Button @click="createStepModal=false">Cancel</Button>
        <Button type="primary" @click="createStep('formValidate1')">Submit</Button>
      </div>
    </Modal>
  </Row>
</template>
<script>
import echarts from "echarts";
export default {
  props: ["userRole", "scopeInfo"],
  data() {
    return {
      scrollOps: {
        bar: {
          background: "lightgrey"
        }
      },
      formValidate0: {
        stepTitle: "",
        stepType: ""
      },
      //button
      addBtn: false,
      activeBtn: false,
      removeBtn: false,
      workspaceBtn: false,
      nodePositionBtn: false,
      // 添加/编辑step
      formValidate1: {
        stepTitle: "",
        stepType: "",
        result: ""
      },
      ruleValidate1: {
        stepTitle: [
          { required: true, message: "Please enter name...", trigger: "blur" }
        ],
        stepType: [
          { required: true, message: "Please select type...", trigger: "blur" }
        ]
      },
      contentHeight: "",
      processStructure: [],
      activeStepInfo: [],
      slctActiveStepInfo: [],
      aActivitiesName: [],
      workspaceName: "",
      typeList: [
        "Context definition & resource collection",
        "Data processing",
        "Data visualization",
        "Geographic model construction",
        "Model effectiveness evaluation",
        "Geographical simulation",
        "Quantitative and qualitative analysis",
        "Decision-making for management"
      ],
      // 步骤逻辑图
      stepChart: null,
      // 选择的步骤
      selectedStep: [],
      delModal: false,
      // 创建步骤
      createStepModal: false,
      slctActivateModal: false,
      gotoworkModal: false,
      // 双击展示活动信息
      activityInfoModal: false,
      showActivityInfo: {},
      stepValue: 0,
      setting: {
        dots: "outside",
        trigger: "click",
        arrow: "always"
      },
      //资源继承
      inheritResource: [],
      targetKeys: [],
      selectResource: [],
      listStyle: { width: "280px", height: "375px" },
      // 工具
      personalTools: [],
      publicTools: [],
      personalToolsets: [],
      publicToolsets: [],
      selectStepTools: [],
      selectStepToolsets: [],
      // step 结构信息
      procedureDrag: true,
      nodeData: [],
      scopeType: "",
      scopeId: "",
      resetSubProjectTypeNotice: false,
      resetSubProjectTypeModel: false,
      resetProjectTypeNotice: false,
      resetProjectTypeModel: false
    };
  },
  created() {
    this.init();
    this.getProcessSteps();

    Array.prototype.contains = function(obj) {
      var i = this.length;
      while (i--) {
        if (
          (this[i].tId != undefined && this[i].tId === obj.tId) ||
          (this[i].tsId != undefined && this[i].tsId === obj.tsId) ||
          (this[i].id != undefined && this[i].id === obj.id) ||
          (this[i].stepId != undefined && this[i].stepId === obj.stepId)
        ) {
          return true;
        }
      }
      return false;
    };
  },
  mounted() {
    this.showSteps();
    this.getAllTools();
    this.getAllToolsets();
    this.btnFunction();
  },
  beforeRouteLeave(to, from, next) {
    next();
  },
  beforeDestroy: function() {
    window.removeEventListener("resize", this.initSize);
  },
  methods: {
    initSize() {
      this.contentHeight = window.innerHeight - 200;
    },
    //初始化函数，作用是控制侧边栏的高度，设置右边通知栏弹出时候的距顶高度以及延迟的时间
    init() {
      this.initSize();
      if (this.scopeInfo.subProjectId == undefined) {
        this.scopeType = "project";
        this.scopeId = this.scopeInfo.projectId;
      } else {
        this.scopeType = "subproject";
        this.scopeId = this.scopeInfo.subProjectId;
      }
      window.addEventListener("resize", this.initSize);
      window.addEventListener("resize", this.updateStepchart);
    },
    updateStepchart() {
      this.btnFunction();
      // 重新渲染
      this.selectedStep = [];
      this.stepChart.dispose();
      this.stepChart = null;
      this.showSteps();
    },
    btnFunction() {
      if (
        this.processStructure.length <= 0 ||
        this.processStructure.length == undefined
      ) {
        this.addBtn = true;
        this.activeBtn = false;
        this.removeBtn = false;
        this.workspaceBtn = false;
        this.nodePositionBtn = false;
      } else {
        this.workspaceBtn = true;
        this.nodePositionBtn = true;
        if (this.selectedStep.length == 0) {
          this.addBtn = false;
          this.activeBtn = false;
          this.removeBtn = false;
        } else {
          this.addBtn = true;
          this.removeBtn = true;
          if (this.activeStepInfo.length == 0) {
            this.activeBtn = true;
          } else {
            let count = 0;
            for (var i = 0; i < this.selectedStep.length; i++) {
              for (var j = 0; j < this.activeStepInfo.length; j++) {
                if (
                  this.selectedStep[i].stepId == this.activeStepInfo[j].stepID
                ) {
                  count++;
                }
              }
            }
            if (
              count <= this.activeStepInfo.length &&
              this.selectedStep.length - count == 1
            ) {
              this.activeBtn = true;
            } else {
              this.activeBtn = false;
            }
          }
        }
      }
    },
    // 进入具体的step页面
    enterStep(type, stepId) {
      if (this.scopeInfo.subProjectId != undefined) {
        if (type == 0) {
          this.$router.push({
            name: "contextDefinition",
            params: { stepId: stepId }
          });
        } else if (type == 1) {
          this.$router.push({
            name: "dataProcessing",
            params: { stepId: stepId }
          });
        } else if (type == 2) {
          this.$router.push({
            name: "modelProcess",
            params: { stepId: stepId }
          });
        } else if (type == 3) {
          this.$router.push({
            name: "modelEvaluation",
            params: { stepId: stepId }
          });
        } else if (type == 4) {
          this.$router.push({
            name: "analysis",
            params: { stepId: stepId }
          });
        } else if (type == 5) {
          this.$router.push({
            name: "simulation",
            params: { stepId: stepId }
          });
        } else if (type == 6) {
          this.$router.push({
            name: "visualization",
            params: { stepId: stepId }
          });
        } else if (type == 7) {
          this.$router.push({
            name: "decisionMaking",
            params: { stepId: stepId }
          });
        }
      } else {
        switch (type) {
          case 0: {
            parent.location.href =
              "/GeoProblemSolving/workspaceP/" + stepId + "/contextDefinition";
            break;
          }
          case 1: {
            parent.location.href =
              "/GeoProblemSolving/workspaceP/" + stepId + "/dataProcessing";
            break;
          }
          case 2: {
            parent.location.href =
              "/GeoProblemSolving/workspaceP/" + stepId + "/modelProcess";
            break;
          }
          case 3: {
            parent.location.href =
              "/GeoProblemSolving/workspaceP/" + stepId + "/modelEvaluation";
            break;
          }
          case 4: {
            parent.location.href =
              "/GeoProblemSolving/workspaceP/" + stepId + "/analysis";
            break;
          }
          case 5: {
            parent.location.href =
              "/GeoProblemSolving/workspaceP/" + stepId + "/simulation";
            break;
          }
          case 6: {
            parent.location.href =
              "/GeoProblemSolving/workspaceP/" + stepId + "/visualization";
            break;
          }
          case 7: {
            parent.location.href =
              "/GeoProblemSolving/workspaceP/" + stepId + "/decisionMaking";
            break;
          }
        }
      }
    },
    getProcessSteps() {
      this.processStructure = [];
      if (
        this.scopeInfo.solvingProcess != undefined &&
        this.scopeInfo.solvingProcess != ""
      ) {
        try {
          this.processStructure = JSON.parse(this.scopeInfo.solvingProcess);
        } catch (err) {
          return;
        }
        // get active activities information
        this.activeStepInfo = [];
        for (var i = 0; i < this.processStructure.length; i++) {
          if (this.processStructure[i].activeStatus) {
            this.activeStepInfo.push(this.processStructure[i]);
          }
        }
      }
    },
    showSteps() {
      this.selectedStep = [];
      let option = {
        animationDurationUpdate: 500,
        animationEasingUpdate: "quinticInOut",
        legend: {
          show: true,
          data: [
            {
              name: "Context definition & resource collection",
              icon: "circle"
            },
            {
              name: "Data processing",
              icon: "circle"
            },
            {
              name: "Data visualization",
              icon: "circle"
            },
            {
              name: "Geographic model construction",
              icon: "circle"
            },
            {
              name: "Model effectiveness evaluation",
              icon: "circle"
            },
            {
              name: "Geographical simulation",
              icon: "circle"
            },
            {
              name: "Quantitative and qualitative analysis",
              icon: "circle"
            },
            {
              name: "Decision-making for management",
              icon: "circle"
            }
          ]
        },
        series: [
          {
            id: "procedure",
            type: "graph",
            layout: "none",
            legendHoverLink: true,
            roam: this.procedureDrag,
            label: {
              normal: {
                show: true
              }
            },
            edgeSymbol: ["circle", "arrow"],
            edgeSymbolSize: [4, 10],
            focusNodeAdjacency: true,
            data: [],
            categories: [
              {
                name: "Context definition & resource collection"
              },
              {
                name: "Data processing"
              },
              {
                name: "Data visualization"
              },
              {
                name: "Geographic model construction"
              },
              {
                name: "Model effectiveness evaluation"
              },
              {
                name: "Geographical simulation"
              },
              {
                name: "Quantitative and qualitative analysis"
              },
              {
                name: "Decision-making for management"
              }
            ],
            links: [],
            lineStyle: {
              normal: {
                opacity: 1,
                width: 5,
                curveness: 0.1
              }
            }
          }
        ]
      };
      if (this.processStructure.length > 0) {
        this.nodeData = [];
        for (var i = 0; i < this.processStructure.length; i++) {
          //get data
          let datum = {
            name: this.processStructure[i].name,
            index: this.processStructure[i].id,
            stepId: this.processStructure[i].stepID,
            x: this.processStructure[i].x,
            y: this.processStructure[i].y,
            category: this.processStructure[i].category,
            symbolSize: 45
          };
          if (this.processStructure[i].activeStatus) {
            datum.symbolSize = 60;
            this.nodeData.push(datum);
            this.selectedStep.push({
              stepId: this.processStructure[i].stepID,
              id: this.processStructure[i].id,
              name: this.processStructure[i].name
            });
          } else {
            this.nodeData.push(datum);
          }

          //get links
          for (var j = 0; j < this.processStructure[i].next.length; j++) {
            option.series[0].links.push({
              source: this.processStructure[i].name,
              target: this.processStructure[i].next[j].name
            });
          }
        }
      }
      option.series[0].data = this.nodeData;
      if (this.stepChart == null) {
        this.stepChart = echarts.init(document.getElementById("steps"));
      } else {
        this.stepChart.off("click");
        this.stepChart.off("dblclick");
      }
      this.stepChart.setOption(option);

      let _this = this;
      // 单击选择步骤
      this.stepChart.on("click", function(params) {
        if (_this.procedureDrag) {
          if (option.series[0].data[params.data.index].symbolSize == 45) {
            option.series[0].data[params.data.index].symbolSize = 60;
            _this.formValidate0.stepTitle = params.data.name;
            _this.formValidate0.stepType = _this.getStepType(
              params.data.category
            );

            // record the selected step nodes
            _this.selectedStep.push({
              stepId: params.data.stepId,
              id: params.data.index,
              name: params.data.name
            });
          } else if (
            option.series[0].data[params.data.index].symbolSize == 60
          ) {
            option.series[0].data[params.data.index].symbolSize = 45;

            // remove these not selected step nodes
            for (var i = 0; i < _this.selectedStep.length; i++) {
              if (_this.selectedStep[i].stepId == params.data.stepId) {
                _this.selectedStep.splice(i, 1);
                break;
              }
            }
          }
          _this.stepChart.setOption(option);
          _this.btnFunction();
        }
      });
      // 双击切换当前步骤
      this.stepChart.on("dblclick", function(params) {
        if (_this.procedureDrag) {
          // _this.enterStep(params.data.category, params.data.stepId);
          _this.activityInfoModal = true;
          let stepType = _this.getStepType(params.data.category);
          let activity = {
            stepID: params.data.stepId,
            name: params.data.name,
            type: stepType
          };
          _this.showActivityInfo = activity;
        }
      });
    },
    editPosition() {
      this.procedureDrag = !this.procedureDrag;

      this.stepChart.setOption({
        animationDurationUpdate: this.procedureDrag ? 500 : 0,
        series: [
          {
            id: "procedure",
            roam: this.procedureDrag
          }
        ]
      });

      // node的拖拽功能
      let _this = this;
      try {
        this.stepChart.setOption({
          // https://www.echartsjs.com/zh/tutorial.html#小例子：自己实现拖拽
          graphic: echarts.util.map(_this.nodeData, function(
            dataItem,
            dataIndex
          ) {
            let x = dataItem.x;
            let y = dataItem.y;
            let item = [x, y];
            let nodePosition = _this.stepChart.convertToPixel(
              { seriesIndex: 0 },
              item
            );

            return {
              type: "circle",
              shape: {
                r: 20
              },
              position: nodePosition,
              invisible: true,
              draggable: !_this.procedureDrag,
              z: 100,
              ondrag: echarts.util.curry(function() {
                let position = _this.stepChart.convertFromPixel(
                  { seriesIndex: 0 },
                  this.position
                );
                _this.nodeData[dataIndex].x = position[0];
                _this.nodeData[dataIndex].y = position[1];
                _this.stepChart.setOption({
                  series: [
                    {
                      id: "procedure",
                      data: _this.nodeData
                    }
                  ]
                });
              }, dataIndex)
            };
          })
        });
      } catch (ex) {
        this.$Nothis.info({
          desc: "ERROR!"
        });
        tice;
      }
    },
    onPointDragging(dataIndex) {},
    getStepType(category) {
      let type;
      if (category == 0) {
        type = "Context definition & resource collection";
      } else if (category == 1) {
        type = "Data processing";
      } else if (category == 2) {
        type = "Data visualization";
      } else if (category == 3) {
        type = "Geographic model construction";
      } else if (category == 4) {
        type = "Model effectiveness evaluation";
      } else if (category == 5) {
        type = "Geographical simulation";
      } else if (category == 6) {
        type = "Quantitative and qualitative analysis";
      } else if (category == 7) {
        type = "Decision-making for management";
      }
      return type;
    },
    getStepCategroy(type) {
      let category;
      if (type == "Context definition & resource collection") {
        category = 0;
      } else if (type == "Data processing") {
        category = 1;
      } else if (type == "Data visualization") {
        category = 2;
      } else if (type == "Geographic model construction") {
        category = 3;
      } else if (type == "Model effectiveness evaluation") {
        category = 4;
      } else if (type == "Geographical simulation") {
        category = 5;
      } else if (type == "Quantitative and qualitative analysis") {
        category = 6;
      } else if (type == "Decision-making for management") {
        category = 7;
      }
      return category;
    },
    activateStep(activities) {
      // 多个激活的活动
      this.slctActiveStepInfo = [];
      if (this.activeStepInfo.length > 1) {
        if (activities == undefined || activities.length == 0) {
          this.slctActivateModal = true;
          return;
        } else if (activities.length > 0) {
          for (var i = 0; i < activities.length; i++) {
            for (var j = 0; j < this.activeStepInfo.length; j++) {
              if (activities[i] == this.activeStepInfo[j].name) {
                this.slctActiveStepInfo.push(this.activeStepInfo[j]);
              }
            }
          }
        }
      } else if (this.activeStepInfo.length == 1) {
        this.slctActiveStepInfo.push(this.activeStepInfo[0]);
      }

      // 新激活的活动
      var nextnode = {};
      for (var i = 0; i < this.selectedStep.length; i++) {
        for (var j = 0; j < this.processStructure.length; j++) {
          if (this.processStructure[j].stepID == this.selectedStep[i].stepId) {
            if (!this.processStructure[j].activeStatus) {
              this.processStructure[j].activeStatus = true;

              // 前后继承关系
              for (var k = 0; k < this.slctActiveStepInfo.length; k++) {
                let lastnode = {
                  name: this.slctActiveStepInfo[k].name,
                  id: this.slctActiveStepInfo[k].id
                };
                if (!this.processStructure[j].last.contains[lastnode]) {
                  this.processStructure[j].last.push(lastnode);
                }
              }

              nextnode = {
                name: this.selectedStep[i].name,
                id: this.selectedStep[i].id
              };
            }
            break;
          }
        }
      }

      // 使处于非激活状态的活动
      for (var i = 0; i < this.slctActiveStepInfo.length; i++) {
        for (var j = 0; j < this.processStructure.length; j++) {
          if (
            this.slctActiveStepInfo[i].stepID == this.processStructure[j].stepID
          ) {
            if (!this.processStructure[j].next.contains[nextnode]) {
              this.processStructure[j].next.push(nextnode);
            }

            this.processStructure[j].activeStatus = false;
            break;
          }
        }
      }
      // update scopeInfo/activeStepInfo/selectedStep/graph
      this.slctActiveStepInfo = [];
      this.scopeInfo.solvingProcess = JSON.stringify(this.processStructure);
      this.getProcessSteps();
      // 重新渲染
      this.updateStepchart();
      // 存储Step
      this.updateSteps();
    },
    addNewStep() {
      // 选择父节点检测
      if (
        this.processStructure.length != 0 &&
        (this.selectedStep.length == undefined || this.selectedStep.length == 0)
      ) {
        this.$Notice.info({
          desc: "Please select at least one node in advance!"
        });
        return;
      }

      // 获取可继承的资源
      this.getInheritResource();
      // 创建步骤模态框
      this.createStepModal = true;
      this.stepValue = 0;
    },
    getInheritResource() {
      this.inheritResource = this.getMockData();
    },
    getMockData() {
      let mockData = [];
      let selectedRes = [];
      // 继承的子项目资源，项目资源，需要手动拉入子项目
      $.ajax({
        url:
          "/GeoProblemSolving/folder/findByFileType?" +
          "scopeId=" +
          this.scopeId +
          "&type=all",
        type: "GET",
        async: false,
        success: function(data) {
          if (data !== "Fail") {
            selectedRes = data;
            for (var i = 0; i < selectedRes.length; i++) {
              mockData.push({
                key: i,
                name: selectedRes.name,
                type: selectedRes.type,
                resourceId: selectedRes.resourceId
              });
            }
          } else {
            selectedRes = [];
          }
        },
        error: function(err) {
          selectedRes = [];
          console.log("err!");
        }
      });
      // 前驱步骤的资源
      for (var i = 0; i < this.selectedStep.length; i++) {
        let selectedStepId = this.selectedStep[i].stepId;

        let getResUrl = "";
        if (
          this.formValidate1.stepType ==
          "Context definition & resource collection"
        ) {
          getResUrl =
            "/GeoProblemSolving/folder/findByFileType?" +
            "scopeId=" +
            selectedStepId +
            "&type=all";
        } else {
          getResUrl =
            "/GeoProblemSolving/folder/findByFileType?" +
            "scopeId=" +
            selectedStepId +
            "&type=data";
        }
        $.ajax({
          url: getResUrl,
          type: "GET",
          async: false,
          success: function(data) {
            if (data !== "Fail") {
              selectedRes = data;
              for (var i = 0; i < selectedRes.length; i++) {
                mockData.push({
                  key: i,
                  name: selectedRes.name,
                  type: selectedRes.type,
                  resourceId: selectedRes.resourceId
                });
              }
            } else {
              selectedRes = [];
            }
          },
          error: function(err) {
            selectedRes = [];
            console.log("err!");
          }
        });
      }
      return mockData;
    },
    getTargetKeys() {
      let mockData = [];
      if (this.inheritResource.length > 0) {
        for (var i = 0; i < this.targetKeys.length; i++) {
          mockData.push({
            key: this.targetKeys[i],
            name: this.inheritResource[this.targetKeys[i]].name,
            type: this.inheritResource[this.targetKeys[i]].type,
            resourceId: this.inheritResource[this.targetKeys[i]].resourceId
          });
        }
      }
      return mockData;
    },
    handleChange(newTargetKeys) {
      this.targetKeys = newTargetKeys;
    },
    filterMethod(data, query) {
      if (data.length > 0) {
        return data.type.indexOf(query) > -1;
      } else {
        return false;
      }
    },
    resourceRender(item) {
      return item.type + " - " + item.name;
    },
    createStep(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          this.createStepModal = false;
          // 重复命名检测
          for (var i = 0; i < this.processStructure.length; i++) {
            if (this.formValidate1.stepTitle == this.processStructure[i].name) {
              this.$Notice.info({
                desc: "The name of new step should not be different!"
              });
              return;
            }
          }
          this.createStepContent();
        } else {
        }
      });
    },
    createStepContent() {
      // 新步骤的基本信息、资源（数据）、拓展工具
      this.selectResource = [];
      this.selectResource = this.getTargetKeys();

      this.filterShowListByType(this.formValidate1.stepType);

      let Step = {};
      Step["name"] = this.formValidate1.stepTitle;
      Step["type"] = this.formValidate1.stepType;
      Step["description"] = this.formValidate1.result;
      if (this.scopeType == "project") {
        Step["projectId"] = this.scopeId;
        Step["subProjectId"] = "";
      } else {
        Step["projectId"] = "";
        Step["subProjectId"] = this.scopeId;
      }
      Step["creator"] = this.$store.getters.userId;
      Step["toolList"] = this.selectStepTools;
      Step["toolsetList"] = this.selectStepToolsets;
      Step["content"] = {};

      this.axios
        .post("/GeoProblemSolving/step/create", Step)
        .then(res => {
          if (res.data == "Offline") {
            if (this.scopeType == "subproject") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else {
              parent.location.href = "/GeoProblemSolving/login";
            }
          } else if (res.data === "Fail") {
            this1.$Message.info("Fail");
          } else {
            this.createStepGraph(res.data);

            // 更新新Step的资源----------------------------------------------------------------------需要改，留坑
            this.copyResource(res.data);
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    createStepGraph(id) {
      if (this.processStructure.length == 0) {
        // 新步骤的类别
        let nodeCategory = 0;
        nodeCategory = this.getStepCategroy(this.formValidate1.stepType);
        // create step node
        let newStepNode = {
          id: 0,
          stepID: id,
          name: this.formValidate1.stepTitle,
          category: nodeCategory,
          last: [],
          next: [],
          x: 600,
          y: 200,
          level: 0,
          end: true,
          activeStatus: true
        };
        this.processStructure.push(newStepNode);

        // 更新scopeInfo
        this.scopeInfo.solvingProcess = JSON.stringify(this.processStructure);
        this.getProcessSteps();
        // 重新渲染
        this.updateStepchart();
        // 存储Step
        this.updateSteps();
      } else if (this.selectedStep.length > 0) {
        //  计算新增节点的属性信息
        let lastNode = [];
        let nodeLevel = 0;
        let nodeY = 0;
        let nodeCategory = 0;
        for (var i = 0; i < this.selectedStep.length; i++) {
          lastNode.push({
            name: this.selectedStep[i].name,
            id: this.selectedStep[i].id
          });
          if (
            this.processStructure[this.selectedStep[i].id].level >= nodeLevel
          ) {
            nodeLevel =
              this.processStructure[this.selectedStep[i].id].level + 1;
          }
          // modify original step node
          this.processStructure[this.selectedStep[i].id].next.push({
            name: this.formValidate1.stepTitle,
            id: this.processStructure.length
          });
          this.processStructure[this.selectedStep[i].id].end = false;
          // calculate y
          if (this.processStructure[i].last == []) {
            nodeY = 200;
          } else {
            let sumY = 0;
            for (var j = 0; j < this.selectedStep.length; j++) {
              sumY += this.processStructure[this.selectedStep[j].id].y;
            }
            nodeY = sumY / this.selectedStep.length;
          }
          //inactivate selected step
          this.processStructure[this.selectedStep[i].id].activeStatus = false;
        }
        let isOverlap = false;
        // 统计每层的节点数
        let levelNum = [];
        for (var i = 0; i < this.processStructure.length; i++) {
          if (this.processStructure[i].level == nodeLevel) {
            levelNum.push(this.processStructure[i].id);
          }
        }
        // 节点重复检测
        for (var i = 0; i < levelNum.length; i++) {
          if (Math.abs(this.processStructure[levelNum[i]].y - nodeY) < 30) {
            isOverlap = true;
            break;
          }
        }
        // 新步骤的类别
        nodeCategory = this.getStepCategroy(this.formValidate1.stepType);
        // create step node
        let newStepNode = {
          id: this.processStructure.length,
          stepID: id,
          name: this.formValidate1.stepTitle,
          category: nodeCategory,
          last: lastNode,
          next: [],
          x: 0,
          y: nodeY,
          level: nodeLevel,
          end: true,
          activeStatus: true
        };
        this.processStructure.push(newStepNode);
        levelNum.push(newStepNode.id);
        // 如果重叠，修改y坐标
        if (isOverlap) {
          for (var i = 0; i < levelNum.length; i++) {
            this.processStructure[levelNum[i]].y =
              (400 / (levelNum.length + 1)) * (i + 1);
          }
          isOverlap = false;
        }
        // calculate x
        let maxLevel = 0;
        for (var i = 0; i < this.processStructure.length; i++) {
          if (this.processStructure[i].level > maxLevel) {
            maxLevel = this.processStructure[i].level;
          }
        }
        for (var i = 0; i < this.processStructure.length; i++) {
          this.processStructure[i].x =
            (800 / (maxLevel + 1)) * (this.processStructure[i].level + 1);
        }
        // 更新scopeInfo
        this.scopeInfo.solvingProcess = JSON.stringify(this.processStructure);
        this.getProcessSteps();
        // 重新渲染
        this.updateStepchart();
        // 存储Step
        this.updateSteps();
      } else {
        this.$Notice.info({
          desc: "Select at least one step as source(s), please!"
        });
      }
    },
    getPublicTools() {
      this.axios
        .get(
          "/GeoProblemSolving/tool/inquiry" +
            "?key=" +
            "privacy" +
            "&value=" +
            "Public"
        )
        .then(res => {
          if (res.data == "Offline") {
            if (this.scopeType == "subproject") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else {
              parent.location.href = "/GeoProblemSolving/login";
            }
          } else if (res.data === "Fail") {
            // this.$Notice.error({ desc: "Loading tools fail." });
          } else if (res.data === "None") {
            // this.$Notice.error({ desc: "There is no existing tool" });
          } else {
            this.$set(this, "publicTools", res.data);
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    getPersonalTools() {
      this.axios
        .get(
          "/GeoProblemSolving/tool/inquiryAll" +
            "?provider=" +
            this.$store.getters.userInfo.userId
        )
        .then(res => {
          if (res.data == "Offline") {
            if (this.scopeType == "subproject") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else {
              parent.location.href = "/GeoProblemSolving/login";
            }
          } else if (res.data === "Fail") {
            // this.$Notice.error({ desc: "Loading tool fail." });
          } else if (res.data === "None") {
            // this.$Notice.error({ desc: "There is no existing tool" });
          } else {
            this.$set(this, "personalTools", res.data);
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    getAllTools() {
      this.getPublicTools();
      this.getPersonalTools();
    },
    getPublicToolsets() {
      this.axios
        .get(
          "/GeoProblemSolving/toolset/inquiry" +
            "?key=" +
            "privacy" +
            "&value=" +
            "Public"
        )
        .then(res => {
          if (res.data == "Offline") {
            if (this.scopeType == "subproject") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else {
              parent.location.href = "/GeoProblemSolving/login";
            }
          } else if (res.data === "Fail") {
            // this.$Notice.error({ desc: "Loading toolsets fail." });
          } else if (res.data === "None") {
            // this.$Notice.error({ desc: "There is no existing toolset" });
          } else {
            this.$set(this, "publicToolsets", res.data);
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    getPersonalToolsets() {
      this.axios
        .get(
          "/GeoProblemSolving/toolset/inquiryAll" +
            "?provider=" +
            this.$store.getters.userInfo.userId
        )
        .then(res => {
          if (res.data == "Offline") {
            if (this.scopeType == "subproject") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else {
              parent.location.href = "/GeoProblemSolving/login";
            }
          } else if (res.data === "Fail") {
            // this.$Notice.error({ desc: "Loading toolsets fail." });
          } else if (res.data === "None") {
            // this.$Notice.error({ desc: "There is no existing toolset" });
          } else {
            this.$set(this, "personalToolsets", res.data);
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    getAllToolsets() {
      this.getPublicToolsets();
      this.getPersonalToolsets();
    },
    filterShowListByType(stepType) {
      var toolList = this.filterDuplicateTools();
      this.selectStepTools = this.getFilterToolResult(toolList, stepType);

      var toolsetList = this.filterDuplicateToolsets();
      this.selectStepToolsets = this.getFilterToolsetResult(
        toolsetList,
        stepType
      );
    },
    filterDuplicateTools() {
      let tools = this.publicTools;
      for (var i = 0; i < this.personalTools.length; i++) {
        if (this.publicTools.contains(this.personalTools[i])) {
          continue;
        } else {
          this.publicTools.push(this.personalTools[i]);
        }
      }
      return tools;
    },
    filterDuplicateToolsets() {
      let toolsets = this.publicToolsets;
      for (var i = 0; i < this.personalToolsets.length; i++) {
        if (this.publicToolsets.contains(this.personalToolsets[i])) {
          continue;
        } else {
          this.publicToolsets.push(this.personalToolsets[i]);
        }
      }
      return toolsets;
    },
    getFilterToolResult(foreList, stepType) {
      let resultList = [];
      for (var i = 0; i < foreList.length; i++) {
        var stepTypes = foreList[i].recomStep;
        for (var j = 0; j < stepTypes.length; j++) {
          if (stepTypes[j] == stepType || stepTypes[j] == "General step") {
            resultList.push(foreList[i].tId);
            break;
          }
        }
      }
      return resultList;
    },
    getFilterToolsetResult(foreList, stepType) {
      let resultList = [];
      for (var i = 0; i < foreList.length; i++) {
        var stepTypes = foreList[i].recomStep;
        for (var j = 0; j < stepTypes.length; j++) {
          if (stepTypes[j] == stepType || stepTypes[j] == "General step") {
            resultList.push(foreList[i].tsId);
            break;
          }
        }
      }
      return resultList;
    },
    // 数据继承-----------------------------------------
    copyResource(stepId) {},
    removeStep() {
      if (this.selectedStep.length == 1) {
        this.delModal = true;
      } else if (this.selectedStep.length > 1) {
        this.$Notice.info({
          desc: "Steps should be deleted one by one! "
        });
      } else {
        this.$Notice.info({
          desc: "There is no step selected! "
        });
      }
    },
    delStepContent(stepId) {
      // 删除step
      this.axios
        .get("/GeoProblemSolving/step/delete" + "?stepId=" + stepId)
        .then(res => {
          if (res.data === "Success") {
            this.$Notice.info({
              desc: "Remove successfully! "
            });
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    delStepGraph() {
      if (this.$store.getters.userInfo.userId == this.scopeInfo.managerId) {
        let currentIndex = this.selectedStep[0].id;

        if (this.processStructure[currentIndex].end) {
          let selectedStepId = this.processStructure[currentIndex].stepID;
          // 删除step节点
          if (currentIndex > 0) {
            // 处理被删除节点的前驱节点
            for (
              var i = 0;
              i < this.processStructure[currentIndex].last.length;
              i++
            ) {
              let lastIndex = this.processStructure[currentIndex].last[i].id;
              if (this.processStructure[lastIndex].next.length === 1) {
                this.processStructure[lastIndex].next = [];
                this.processStructure[lastIndex].end = true;
              } else if (this.processStructure[lastIndex].next.length > 1) {
                for (
                  let j = 0;
                  j < this.processStructure[lastIndex].next.length;
                  j++
                ) {
                  if (
                    this.processStructure[lastIndex].next[j].name ===
                    this.selectedStep[0].name
                  ) {
                    this.processStructure[lastIndex].next.splice(j, 1);
                  }
                }
              }
            }
            // 删除节点
            this.processStructure.splice(currentIndex, 1);

            for (var i = currentIndex; i < this.processStructure.length; i++) {
              var originalID = this.processStructure[i].id;
              // 当前节点id
              if (originalID !== i) {
                this.processStructure[i].id = i;
              }

              var originalName = this.processStructure[i].name;
              // 前驱节点的 next id
              for (var j = 0; j < this.processStructure[i].last.length; j++) {
                var lastIndex = this.processStructure[i].last[j].id;
                var lastnode = this.processStructure[lastIndex];
                for (var k = 0; k < lastnode.next.length; k++) {
                  if (lastnode.next[k].name === originalName) {
                    this.processStructure[lastIndex].next[k].id = i;
                  }
                }
              }

              // 后继节点的 last id
              for (var j = 0; j < this.processStructure[i].next.length; j++) {
                var nextIndex = this.processStructure[i].next[j].id - 1;
                var nextnode = this.processStructure[nextIndex];
                for (var k = 0; k < nextnode.last.length; k++) {
                  if (nextnode.last[k].name === originalName) {
                    this.processStructure[nextIndex].last[k].id = i;
                  }
                }
              }
            }
          } else if (currentIndex === 0) {
            // 删除节点
            this.processStructure.splice(currentIndex, 1);
          }

          // 更新scopeInfo
          this.scopeInfo.solvingProcess = JSON.stringify(this.processStructure);
          this.getProcessSteps();
          // 重新渲染
          this.updateStepchart();
          this.updateSteps();
          //删除数据库
          this.delStepContent(selectedStepId);
        } else {
          this.$Notice.info({
            desc:
              "The selected step " +
              this.selectedStep[0].name +
              " can not be removed. Because it has the next activities."
          });
        }
      }
    },
    updateSteps() {
      let obj = new URLSearchParams();
      obj.append("solvingProcess", JSON.stringify(this.processStructure));
      var updateurl = "";
      if (this.scopeType == "project") {
        obj.append("projectId", this.scopeId);
        updateurl = "/GeoProblemSolving/project/update";
      } else if (this.scopeType == "subproject") {
        obj.append("subProjectId", this.scopeId);
        updateurl = "/GeoProblemSolving/subProject/update";
      }
      this.axios
        .post(updateurl, obj)
        .then(res => {
          if (res.data == "Offline") {
            if (this.scopeType == "subproject") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else {
              parent.location.href = "/GeoProblemSolving/login";
            }
          } else if (res.data != "Fail") {
            if (this.scopeType == "project") {
              this.$store.commit("setProjectInfo", res.data);
              parent.vm.projectInfo = res.data;
              this.$Notice.info({
                desc: "Project update successfully!"
              });
            } else if (this.scopeType == "subproject") {
              this.$store.commit("setSubProjectInfo", res.data);
              this.$Notice.info({
                desc: "Subproject update successfully!"
              });
            }
          } else {
            this.$Message.error("Update subproject failed.");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    resetSubProjectTypeModalShow() {
      if (this.processStructure.length > 0) {
        this.resetSubProjectTypeNotice = true;
      } else {
        this.resetSubProjectTypeModel = true;
      }
    },
    resetProjectTypeModalShow() {
      if (this.processStructure.length > 0) {
        this.resetProjectTypeNotice = true;
      } else {
        this.resetProjectTypeModel = true;
      }
    },
    resetSubProjectType() {
      let obj = new URLSearchParams();
      obj.append("subProjectId", this.scopeId);
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
    },
    resetProjectType() {
      let obj = new URLSearchParams();
      obj.append("projectId", this.scopeId);
      obj.append("type", "");
      obj.append("stepId", "");
      this.axios
        .post("/GeoProblemSolving/project/update", obj)
        .then(res => {
          this.resetProjectTypeModel = false;
          if (res.data == "Offline") {
            parent.location.href = "/GeoProblemSolving/login";
          } else if (res.data != "Fail") {
            this.$store.commit("setProjectInfo", res.data);
            this.$emit("changeProjectInfo", res.data);
          } else {
            this.$Message.error("Set type failed.");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    gotoworkspace() {
      if (this.activeStepInfo.length == 1) {
        this.enterStep(
          this.activeStepInfo[0].category,
          this.activeStepInfo[0].stepID
        );
      } else if (this.activeStepInfo.length > 1) {
        this.slctActiveStepInfo = [];
        this.gotoworkModal = true;
        return;
      } else {
        this.enterStep(
          this.processStructure[0].category,
          this.processStructure[0].stepID
        );
      }
    },
    selectAActivity(value) {
      if (value == "workspace") {
        for (var i = 0; i < this.activeStepInfo.length; i++) {
          if (this.workspaceName == this.activeStepInfo[i].name) {
            this.enterStep(
              this.activeStepInfo[i].category,
              this.activeStepInfo[i].stepID
            );
            this.workspaceName = "";
          }
        }
      } else if (value == "activate") {
        this.activateStep(this.aActivitiesName);
        this.aActivitiesName = [];
      }
    }
  }
};
</script>
