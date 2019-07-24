<style scoped>
.addBtn,
.removeBtn {
  font-size: 10px;
}
.addBtn:hover {
  color: white;
  background: #47cb89;
}
.removeBtn:hover {
  color: white;
  background: #f16643;
}
</style>
<template>
    <Row>
      <Col span="23" offset="1" style="margin-top:20px">
        <div :style="{height:contentHeight+'px'}">
          <Row type="flex" justify="space-around">
            <template v-if="$store.getters.userInfo.userId == this.subProjectInfo.managerId">
              <div style="width: 80%; height: 25px">
                <label style="margin-left:20px">New step name:</label>
                <Input
                  v-model="formValidate1.moduleTitle"
                  placeholder="Enter something..."
                  style="width: 200px;margin-left:10px"
                />
                <label style="margin-left:40px">New step type:</label>
                <Select
                  v-model="formValidate1.moduleType"
                  style="width:200px;margin-left:10px"
                  placeholder="please select module type..."
                >
                  <Option v-for="item in typeList" :key="item.index" :value="item">{{ item }}</Option>
                </Select>
                <Button
                  type="default"
                  @click="toWorkingSpace()"
                  class="enterBtn"
                  title="Add a new module"
                  style="float:right;margin-left:20px"
                >Working space</Button>
                <template v-if="this.processStructure.length > 0">
                  <Button
                    type="default"
                    @click="removeStep()"
                    icon="md-remove"
                    class="removeBtn"
                    title="Remove this module"
                    style="float:right;margin-left:10px"
                  >Remove</Button>
                </template>
                <Button
                  type="default"
                  @click="addNewStep()"
                  icon="md-add"
                  class="addBtn"
                  title="Add a new module"
                  style="float:right;margin-left:10px"
                >Add</Button>
              </div>
            </template>
            <div
              style="width:90%;margin-top:20px;background-color:#efeeee;"
              :style="{height:contentHeight-120+'px'}"
              id="steps"
            ></div>
            <h3
              style="margin-top:10px"
            >Steps for solving geo-problem can be ctrated and deleted here.</h3>
          </Row>
        </div>
      </Col>
    </Row>
</template>
<script>
export default {
  props: ["subProjectInfo"],
  data() {
    return {
      scrollOps: {
        bar: {
          background: "lightgrey"
        }
      },

      // 消息
      subprojectSocket: null,
      timer: null,
      socketMsg: {
        type: "",
        time: "",
        who: "",
        whoid: "",
        content: ""
      },
      // 添加/编辑module
      formValidate1: {
        moduleTitle: "",
        moduleType: ""
      },
      ruleValidate1: {
        moduleTitle: [
          { required: true, message: "Please enter name...", trigger: "blur" }
        ],
        moduleType: [
          { required: true, message: "Please select type...", trigger: "blur" }
        ]
      },
      contentHeight: "",
      processStructure: [],
      currentStep: {},
      typeList: [
        "Resource collection",
        "Data processing",
        "Modeling",
        "Model evaluation",
        "Analysis with models or tools",
        "Simulation/Prediction",
        "Visualization & Representation",
        "Qualitative analysis",
        "Decision-making and management"
      ],
      userRole: ""
    };
  },
  created() {
    this.init();
  },
  mounted() {
    window.addEventListener("resize", this.initSize);
  },
  beforeRouteLeave(to, from, next) {
    this.removeTimer();
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
      if (
        !this.subProjectInfo.isMember &&
        this.subProjectInfo.managerId != this.$store.getters.userId
      ) {
        this.userRole = "Visitor";
      }
    },
    closeModuleSocket() {
      if (this.subprojectSocket != null) {
        this.removeTimer();
        this.subprojectSocket.close();
      }
    },
    openModuleSocket() {
      if (this.subprojectSocket != null) {
        this.subprojectSocket = null;
      }

      let roomId = this.subProjectInfo.subProjectId + "task";
      var subprojectSocketURL =
        "ws://localhost:8081/GeoProblemSolving/Module/" + roomId;
      // var subprojectSocketURL = "ws://"+this.$store.state.IP_Port+"/GeoProblemSolving/Module/" + roomId;
      this.subprojectSocket = new WebSocket(subprojectSocketURL);
      this.subprojectSocket.onopen = this.onOpen;
      this.subprojectSocket.onmessage = this.onMessage;
      this.subprojectSocket.onclose = this.onClose;
      this.subprojectSocket.onerror = this.onError;
      this.setTimer();
    },
    onOpen() {
      console.log("ModuleSocket连接成功！");
    },
    // 更新人员，更新数据，更新records
    onMessage(e) {
      let messageJson = JSON.parse(e.data);
      let record = {
        type: "",
        time: "",
        who: "",
        content: ""
      };

      // 任务记录
      if (messageJson.type == "tasks") {
        this.inquiryTask();
      }
    },
    onClose(e) {
      this.removeTimer();
      console.log("ModuleSocket连接断开！");
    },
    onError(e) {
      this.removeTimer();
      console.log("ModuleSocket连接错误！");
    },
    setTimer() {
      var that = this;
      this.timer = setInterval(() => {
        var messageJson = {};
        messageJson["type"] = "ping";
        messageJson["message"] = "ping";
        if (
          that.subprojectSocket != null &&
          that.subprojectSocket != undefined
        ) {
          that.subprojectSocket.send(JSON.stringify(messageJson));
        }
      }, 20000);
    },
    removeTimer() {
      clearInterval(this.timer);
    },
    sendMessage(message) {
      if (this.subprojectSocket != null) {
        this.subprojectSocket.send(JSON.stringify(message));
      }
    },
    ok() {
      this.$Message.info("Clicked ok");
    },
    cancel() {},
    showSteps() {
      this.selectedModule = [];
      let option = {
        animationDurationUpdate: 500,
        animationEasingUpdate: "quinticInOut",
        legend: {
          show: true,
          data: [
            {
              name: "Preparation",
              icon: "circle"
            },
            {
              name: "Analysis",
              icon: "circle"
            },
            {
              name: "Modeling",
              icon: "circle"
            },
            {
              name: "Simulation",
              icon: "circle"
            },
            {
              name: "Verification",
              icon: "circle"
            },
            {
              name: "Comparison",
              icon: "circle"
            }
          ]
        },
        series: [
          {
            type: "graph",
            layout: "none",
            legendHoverLink: true,
            roam: true,
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
                name: "Preparation"
              },
              {
                name: "Analysis"
              },
              {
                name: "Simulation"
              },
              {
                name: "Modeling"
              },
              {
                name: "Verification"
              },
              {
                name: "Comparison"
              }
            ],
            links: [],
            lineStyle: {
              normal: {
                opacity: 1,
                width: 3,
                curveness: 0
              }
            }
          }
        ]
      };
      if (this.processStructure.length > 0) {
        this.selectedModule = [];
        for (let i = 0; i < this.processStructure.length; i++) {
          //get data
          if (this.processStructure[i].stepID == this.currentModule.moduleId) {
            option.series[0].data.push({
              name: this.processStructure[i].name,
              index: this.processStructure[i].id,
              moduleId: this.processStructure[i].stepID,
              x: this.processStructure[i].x,
              y: this.processStructure[i].y,
              category: this.processStructure[i].category,
              symbolSize: 45
            });
            this.selectedModule.push({
              moduleId: this.processStructure[i].stepID,
              index: this.processStructure[i].id,
              name: this.processStructure[i].name
            });
          } else {
            option.series[0].data.push({
              name: this.processStructure[i].name,
              index: this.processStructure[i].id,
              moduleId: this.processStructure[i].stepID,
              x: this.processStructure[i].x,
              y: this.processStructure[i].y,
              category: this.processStructure[i].category,
              symbolSize: 30
            });
          }

          //get links
          for (let j = 0; j < this.processStructure[i].next.length; j++) {
            option.series[0].links.push({
              source: this.processStructure[i].name,
              target: this.processStructure[i].next[j].name
            });
          }
        }
      }

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
        if (option.series[0].data[params.data.index].symbolSize == 30) {
          option.series[0].data[params.data.index].symbolSize = 45;

          // record the selected step nodes
          _this.selectedModule.push({
            moduleId: params.data.moduleId,
            index: params.data.index,
            name: params.data.name
          });
        } else if (option.series[0].data[params.data.index].symbolSize == 45) {
          option.series[0].data[params.data.index].symbolSize = 30;

          // remove these not selected step nodes
          for (let i = 0; i < _this.selectedModule.length; i++) {
            if (_this.selectedModule[i].moduleId == params.data.moduleId) {
              _this.selectedModule.splice(i, 1);
              break;
            }
          }
        }
        _this.stepChart.setOption(option);
      });
      // 双击切换当前步骤
      this.stepChart.on("dblclick", function(params) {
        _this.currentStep = _this.processStructure[params.data.index];
        _this.getModuleInfo(params.data.moduleId);

        _this.selectedModule = [];
        option.series[0].data = [];
        for (let i = 0; i < _this.processStructure.length; i++) {
          //get data
          if (_this.processStructure[i].stepID == params.data.moduleId) {
            option.series[0].data.push({
              name: _this.processStructure[i].name,
              index: _this.processStructure[i].id,
              moduleId: _this.processStructure[i].stepID,
              x: _this.processStructure[i].x,
              y: _this.processStructure[i].y,
              category: _this.processStructure[i].category,
              symbolSize: 45
            });
            _this.selectedModule.push({
              moduleId: _this.processStructure[i].stepID,
              index: _this.processStructure[i].id,
              name: _this.processStructure[i].name
            });
          } else {
            option.series[0].data.push({
              name: _this.processStructure[i].name,
              index: _this.processStructure[i].id,
              moduleId: _this.processStructure[i].stepID,
              x: _this.processStructure[i].x,
              y: _this.processStructure[i].y,
              category: _this.processStructure[i].category,
              symbolSize: 30
            });
          }
        }
        _this.stepChart.setOption(option);
      });
      this.stepsModal = true;
    },
    addNewStep() {
      // 重复命名检测
      for (let i = 0; i < this.processStructure.length; i++) {
        if (this.formValidate1.moduleTitle == this.processStructure[i].name) {
          this.$Notice.info({
            desc: "The name of new step should not be different!"
          });
          return;
        }
      }
      if (
        this.formValidate1.moduleTitle != "" &&
        this.formValidate1.moduleType != ""
      ) {
        if (this.selectedModule.length > 0) {
          //  计算新增节点的属性信息
          let lastNode = [];
          let nodeLevel = 0;
          let nodeY = 0;
          let nodeCategory = 0;
          for (let i = 0; i < this.selectedModule.length; i++) {
            lastNode.push({
              name: this.selectedModule[i].name,
              id: this.selectedModule[i].index
            });

            if (
              this.processStructure[this.selectedModule[i].index].level >=
              nodeLevel
            ) {
              nodeLevel =
                this.processStructure[this.selectedModule[i].index].level + 1;
            }

            // modify original step node
            this.processStructure[this.selectedModule[i].index].next.push({
              name: this.formValidate1.moduleTitle,
              id: this.processStructure.length
            });
            this.processStructure[this.selectedModule[i].index].end = false;

            // calculate y
            if (this.processStructure[i].last == []) {
              nodeY = 200;
            } else {
              let sumY = 0;
              for (let j = 0; j < this.selectedModule.length; j++) {
                sumY += this.processStructure[this.selectedModule[j].index].y;
              }
              nodeY = sumY / this.selectedModule.length;
            }
          }

          let isOverlap = false;
          // 统计每层的节点数 并 非激活现有的step
          let levelNum = [];
          for (let i = 0; i < this.processStructure.length; i++) {
            if (this.processStructure[i].level == nodeLevel) {
              levelNum.push(this.processStructure[i].id);
            }
            this.processStructure[i].activeStatus = false;
          }
          // 节点重复检测
          for (let i = 0; i < levelNum.length; i++) {
            if (Math.abs(this.processStructure[levelNum[i]].y - nodeY) < 30) {
              isOverlap = true;
              break;
            }
          }

          // 新步骤的类别
          if (this.formValidate1.moduleType == "Preparation") {
            nodeCategory = 0;
          } else if (this.formValidate1.moduleType == "Analysis") {
            nodeCategory = 1;
          } else if (this.formValidate1.moduleType == "Modeling") {
            nodeCategory = 2;
          } else if (this.formValidate1.moduleType == "Simulation") {
            nodeCategory = 3;
          } else if (this.formValidate1.moduleType == "Validation") {
            nodeCategory = 4;
          } else if (this.formValidate1.moduleType == "Comparison") {
            nodeCategory = 5;
          }

          // create step node
          let newStepNode = {
            id: this.processStructure.length,
            stepID: "",
            name: this.formValidate1.moduleTitle,
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
            for (let i = 0; i < levelNum.length; i++) {
              this.processStructure[levelNum[i]].y =
                (400 / (levelNum.length + 1)) * (i + 1);
            }
            isOverlap = false;
          }

          // calculate x
          let maxLevel = 0;
          for (let i = 0; i < this.processStructure.length; i++) {
            if (this.processStructure[i].level > maxLevel) {
              maxLevel = this.processStructure[i].level;
            }
          }
          for (let i = 0; i < this.processStructure.length; i++) {
            this.processStructure[i].x =
              (800 / (maxLevel + 1)) * (this.processStructure[i].level + 1);
          }

          this.stepChart.dispose();
          this.stepChart = null;
          //关闭当前模态框
          this.stepsModal = false;
          //选择资源
          this.chooseResource();
        } else {
          this.$Notice.info({
            desc: "There is no step node being selected!"
          });
        }
      } else if (this.formValidate1.moduleTitle == "") {
        this.$Notice.info({
          desc: "The name of new step should not be empty!"
        });
      } else if (this.formValidate1.moduleType == "") {
        this.$Notice.info({
          desc: "The type of new step should not be empty!"
        });
      }
    },
    removeStep() {
      if (this.selectedModule.length > 0) {
        this.stepsModal = false;
        this.delModal = true;
      } else {
        this.$Notice.info({
          desc: "There is no step selected! "
        });
      }
    },
    toWorkingSpace() {}
    // gotoPersonalSpace(id) {
    //   if (id == this.$store.getters.userId) {
    //     this.$router.push({ name: "PersonalPage" });
    //   } else {
    //     this.$router.push({ name: "MemberDetailPage", params: { id: id } });
    //   }
    // }
  }
};
</script>
