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
              <label style="margin-left:20px">Step name:</label>
              <Input
                v-model="formValidate1.stepTitle"
                placeholder="Enter something..."
                style="width: 250px;margin-left:10px"
              />
              <label style="margin-left:50px">Step type:</label>
              <Select
                v-model="formValidate1.stepType"
                style="width:250px;margin-left:10px"
                placeholder="please select step type..."
              >
                <Option v-for="item in typeList" :key="item.index" :value="item">{{ item }}</Option>
              </Select>
              <template v-if="this.processStructure.length > 0">
                <Button
                  type="default"
                  @click="removeStep()"
                  icon="md-remove"
                  class="removeBtn"
                  title="Remove this step"
                  style="float:right;margin-left:10px"
                >Remove</Button>
              </template>
              <Button
                type="default"
                @click="addNewStep()"
                icon="md-add"
                class="addBtn"
                title="Add a new step"
                style="float:right;margin-left:10px"
              >Add</Button>
            </div>
          </template>
          <div
            style="width:90%;margin-top:20px;padding:15px;background-color:#f8f8f9"
            :style="{height:contentHeight-120+'px'}"
            id="steps"
          ></div>
          <h3 style="margin-top:10px">Steps for solving geo-problem can be ctrated and deleted here.</h3>
        </Row>
      </div>
    </Col>
    <Modal
      v-model="delModal"
      title="Delete this process"
      @on-ok="delStep"
      ok-text="Ok"
      cancel-text="Cancel"
    >
      <p>Do you really want to delete this step?</p>
    </Modal>
  </Row>
</template>
<script>
import echarts from "echarts";
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
      // 添加/编辑step
      formValidate1: {
        stepTitle: "",
        stepType: ""
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
        "Decision-making & management"
      ],
      userRole: "",
      // 步骤逻辑图
      stepChart: null,
      //现在点击的step
      currentStep: {},
      // 选择的步骤
      selectedStep: [],
      delModal: false
    };
  },
  created() {
    this.init();
  },
  mounted() {
    window.addEventListener("resize", this.initSize);
    this.getProcessSteps();
    this.showSteps();
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
    closeStepSocket() {
      if (this.subprojectSocket != null) {
        this.removeTimer();
        this.subprojectSocket.close();
      }
    },
    openStepSocket() {
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
      console.log("StepSocket连接成功！");
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
      console.log("StepSocket连接断开！");
    },
    onError(e) {
      this.removeTimer();
      console.log("StepSocket连接错误！");
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
    //---------------------------------------------------------进入具体的step页面---------------------------------------------------------
    enterStep (stepId) {},
    //---------------------------------------------------------进入具体的step页面---------------------------------------------------------
    getStepInfo(stepId) {
      this.axios
        .get(
          "/GeoProblemSolving/module/inquiry" +
            "?key=stepId" +
            "&value=" +
            stepId
        )
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data != "None" && res.data != "Fail") {
            this.currentStep = res.data[0];
          } else if (res.data == "None") {
            this.currentStep = [];
          }
        })
        .catch(err => {});
    },
    getProcessSteps() {
      this.processStructure = [];
      if (
        this.subProjectInfo.solvingProcess == undefined ||
        this.subProjectInfo.solvingProcess.length == 0
      ) {
        // 为了兼容
        this.axios
          .get(
            "/GeoProblemSolving/module/inquiry" +
              "?key=subProjectId" +
              "&value=" +
              this.subProjectInfo.subProjectId
          )
          .then(res => {
            if (res.data == "Offline") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else if (res.data != "None" && res.data != "Fail") {
              let stepList = res.data;

              let nodeCategory = this.getStepCategroy(stepList[0].type);
              
              this.processStructure.push({
                id: 0,
                stepID: stepList[0].stepId,
                name: stepList[0].title,
                category: nodeCategory,
                last: [],
                next: [{ name: stepList[1].title, id: 1 }],
                x: 0,
                y: 200,
                level: 0,
                end: false,
                activeStatus: stepList[0].activeStatus
              });

              for (let i = 1; i < stepList.length - 1; i++) {

                nodeCategory = this.getStepCategroy(stepList[i].type);
                this.processStructure.push({
                  id: i,
                  stepID: stepList[i].stepId,
                  name: stepList[i].title,
                  category: nodeCategory,
                  last: [{ name: stepList[i - 1].title, id: i - 1 }],
                  next: [{ name: stepList[i + 1].title, id: i + 1 }],
                  x: i * (800 / stepList.length),
                  y: 200,
                  level: i,
                  end: false,
                  activeStatus: stepList[i].activeStatus
                });
              }

              nodeCategory = this.getStepCategroy(
                stepList[stepList.length - 1].type
              );

              this.processStructure.push({
                id: stepList.length - 1,
                stepID: stepList[stepList.length - 1].stepId,
                name: stepList[stepList.length - 1].title,
                category: nodeCategory,
                last: [
                  {
                    name: stepList[stepList.length - 1].title,
                    id: stepList.length - 1
                  }
                ],
                next: [],
                x: (stepList.length - 1) * (800 / stepList.length),
                y: 200,
                level: stepList.length - 1,
                end: true,
                activeStatus: stepList[stepList.length - 1].activeStatus
              });

              for (let i = 0; i < this.processStructure.length; i++) {
                if (this.processStructure[i].activeStatus) {
                  this.currentStep = this.processStructure[i];
                  this.currentStep = stepList[i];
                  break;
                } else if (
                  i == this.processStructure.length - 1 &&
                  this.processStructure[i].activeStatus == undefined
                ) {
                  this.processStructure[i].activeStatus = true;
                  this.currentStep = this.processStructure[i];
                  this.currentStep = stepList[i];
                }
              }
            } else if (res.data == "None" || res.data == "Fail") {
              this.currentStep = [];
            }
          })
          .catch(err => {});
      } else if (this.subProjectInfo.solvingProcess.length > 0) {
        this.processStructure = JSON.parse(this.subProjectInfo.solvingProcess);

        for (let i = 0; i < this.processStructure.length; i++) {
          if (this.processStructure[i].activeStatus) {
            this.currentStep = this.processStructure[i];
            // 请求step数据
            this.getStepInfo(this.processStructure[i].stepID);
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
              name: "Resource collection",
              icon: "circle"
            },
            {
              name: "Data processing",
              icon: "circle"
            },
            {
              name: "Modeling",
              icon: "circle"
            },
            {
              name: "Model evaluation",
              icon: "circle"
            },
            {
              name: "Analysis with models or tools",
              icon: "circle"
            },
            {
              name: "Simulation/Prediction",
              icon: "circle"
            },
            {
              name: "Visualization & Representation",
              icon: "circle"
            },
            {
              name: "Qualitative analysis",
              icon: "circle"
            },
            {
              name: "Decision-making & management",
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
                name: "Resource collection"
              },
              {
                name: "Data processing"
              },
              {
                name: "Modeling"
              },
              {
                name: "Model evaluation"
              },
              {
                name: "Analysis with models or tools"
              },
              {
                name: "Simulation/Prediction"
              },
              {
                name: "Visualization & Representation"
              },
              {
                name: "Qualitative analysis"
              },
              {
                name: "Decision-making & management"
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
        this.selectedStep = [];
        for (let i = 0; i < this.processStructure.length; i++) {
          //get data
          if (this.processStructure[i].stepID == this.currentStep.stepId) {
            option.series[0].data.push({
              name: this.processStructure[i].name,
              index: this.processStructure[i].id,
              stepId: this.processStructure[i].stepID,
              x: this.processStructure[i].x,
              y: this.processStructure[i].y,
              category: this.processStructure[i].category,
              symbolSize: 45
            });
            this.selectedStep.push({
              stepId: this.processStructure[i].stepID,
              index: this.processStructure[i].id,
              name: this.processStructure[i].name
            });
          } else {
            option.series[0].data.push({
              name: this.processStructure[i].name,
              index: this.processStructure[i].id,
              stepId: this.processStructure[i].stepID,
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
          _this.formValidate1.stepTitle = params.data.name;
          _this.formValidate1.stepType = _this.getStepType(
            params.data.category
          );

          // record the selected step nodes
          _this.selectedStep.push({
            stepId: params.data.stepId,
            index: params.data.index,
            name: params.data.name
          });
        } else if (option.series[0].data[params.data.index].symbolSize == 45) {
          option.series[0].data[params.data.index].symbolSize = 30;

          // remove these not selected step nodes
          for (let i = 0; i < _this.selectedStep.length; i++) {
            if (_this.selectedStep[i].stepId == params.data.stepId) {
              _this.selectedStep.splice(i, 1);
              break;
            }
          }
        }
        _this.stepChart.setOption(option);
      });
      // 双击切换当前步骤
      this.stepChart.on("dblclick", function(params) {
        _this.currentStep = _this.processStructure[params.data.index];
        _this.enterStep(params.data.stepId);

        _this.selectedStep = [];
        option.series[0].data = [];
        for (let i = 0; i < _this.processStructure.length; i++) {
          //get data
          if (_this.processStructure[i].stepID == params.data.stepId) {
            option.series[0].data.push({
              name: _this.processStructure[i].name,
              index: _this.processStructure[i].id,
              stepId: _this.processStructure[i].stepID,
              x: _this.processStructure[i].x,
              y: _this.processStructure[i].y,
              category: _this.processStructure[i].category,
              symbolSize: 45
            });
            _this.selectedStep.push({
              stepId: _this.processStructure[i].stepID,
              index: _this.processStructure[i].id,
              name: _this.processStructure[i].name
            });
          } else {
            option.series[0].data.push({
              name: _this.processStructure[i].name,
              index: _this.processStructure[i].id,
              stepId: _this.processStructure[i].stepID,
              x: _this.processStructure[i].x,
              y: _this.processStructure[i].y,
              category: _this.processStructure[i].category,
              symbolSize: 30
            });
          }
        }
        _this.stepChart.setOption(option);
      });
    },
    getStepType(category) {
      let type;
      if (category == 0) {
        type = "Resource collection";
      } else if (category == 1) {
        type = "Data processing";
      } else if (category == 2) {
        type = "Modeling";
      } else if (category == 3) {
        type = "Model evaluation";
      } else if (category == 4) {
        type = "Analysis with models or tools";
      } else if (category == 5) {
        type = "Simulation/Prediction";
      } else if (category == 6) {
        type = "Visualization & Representation";
      } else if (category == 7) {
        type = "Qualitative analysis";
      } else if (category == 8) {
        type = "Decision-making & management";
      }
      return type;
    },
    getStepCategroy(type) {
      let category;
      if (type == "Resource collection") {
        category = 0;
      } else if (type == "Data processing") {
        category = 1;
      } else if (type == "Modeling") {
        category = 2;
      } else if (type == "Model evaluation") {
        category = 3;
      } else if (type == "Analysis with models or tools") {
        category = 4;
      } else if (type == "Simulation/Prediction") {
        category = 5;
      } else if (type == "Visualization & Representation") {
        category = 6;
      } else if (type == "Qualitative analysis") {
        category = 7;
      } else if (type == "Decision-making & management") {
        category = 8;
      }
      return category;
    },
    // ---------------------------------------------------需要改---------------------------------------------------
    addNewStep() {
      // 重复命名检测
      for (let i = 0; i < this.processStructure.length; i++) {
        if (this.formValidate1.stepTitle == this.processStructure[i].name) {
          this.$Notice.info({
            desc: "The name of new step should not be different!"
          });
          return;
        }
      }
      if (
        this.formValidate1.stepTitle != "" &&
        this.formValidate1.stepType != ""
      ) {
        if (this.selectedStep.length > 0) {
          //  计算新增节点的属性信息
          let lastNode = [];
          let nodeLevel = 0;
          let nodeY = 0;
          let nodeCategory = 0;
          for (let i = 0; i < this.selectedStep.length; i++) {
            lastNode.push({
              name: this.selectedStep[i].name,
              id: this.selectedStep[i].index
            });

            if (
              this.processStructure[this.selectedStep[i].index].level >=
              nodeLevel
            ) {
              nodeLevel =
                this.processStructure[this.selectedStep[i].index].level + 1;
            }

            // modify original step node
            this.processStructure[this.selectedStep[i].index].next.push({
              name: this.formValidate1.stepTitle,
              id: this.processStructure.length
            });
            this.processStructure[this.selectedStep[i].index].end = false;

            // calculate y
            if (this.processStructure[i].last == []) {
              nodeY = 200;
            } else {
              let sumY = 0;
              for (let j = 0; j < this.selectedStep.length; j++) {
                sumY += this.processStructure[this.selectedStep[j].index].y;
              }
              nodeY = sumY / this.selectedStep.length;
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
          nodeCategory = this.getStepCategroy(this.formValidate1.stepType)
          
          // create step node
          let newStepNode = {
            id: this.processStructure.length,
            stepID: "",
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

          // 重新渲染
          this.stepChart.dispose();
          this.stepChart = null;
          this.createNewStep();
          // this.showSteps();

          // //选择资源
          // this.chooseResource();
        } else {
          this.$Notice.info({
            desc: "There is no step node being selected!"
          });
        }
      } else if (this.formValidate1.stepTitle == "") {
        this.$Notice.info({
          desc: "The name of new step should not be empty!"
        });
      } else if (this.formValidate1.stepType == "") {
        this.$Notice.info({
          desc: "The type of new step should not be empty!"
        });
      }
    },
    removeStep() {
      if (this.selectedStep.length > 0) {
        // this.stepsModal = false;
        this.delModal = true;
      } else {
        this.$Notice.info({
          desc: "There is no step selected! "
        });
      }
    },
    delStep() {
      if (
        this.$store.getters.userInfo.userId == this.subProjectInfo.managerId
      ) {
        for (let i = 0; i < this.selectedStep.length; i++) {
          let currentIndex = this.selectedStep[i].index;
          if (
            this.processStructure[currentIndex].end &&
            this.processStructure[currentIndex].name != this.currentStep.name
          ) {
            // 删除module
            this.axios
              .get(
                "/GeoProblemSolving/module/delete" +
                  "?moduleId=" +
                  this.processStructure[currentIndex].stepID
              )
              .then(res => {
                if (res.data === "Success") {
                }
              })
              .catch(err => {
                console.log(err.data);
              });

            // 删除step节点
            if (currentIndex > 0) {
              // 处理被删除节点的前驱节点
              for (
                let j = 0;
                j < this.processStructure[currentIndex].last.length;
                j++
              ) {
                let lastIndex = this.processStructure[currentIndex].last[j].id;
                if (this.processStructure[lastIndex].next.length === 1) {
                  this.processStructure[lastIndex].next = [];
                } else if (this.processStructure[lastIndex].next.length > 1) {
                  for (
                    let s = 0;
                    s < this.processStructure[lastIndex].next.length;
                    s++
                  ) {
                    if (
                      this.processStructure[lastIndex].next[s].name ===
                      this.selectedStep[i].name
                    ) {
                      this.processStructure[lastIndex].next.splice(s, 1);
                    }
                  }
                }
                this.processStructure[lastIndex].end = true;
              }

              // 删除节点
              this.processStructure.splice(currentIndex, 1);

              // 处理后继节点, 统一步骤id与step在数组里的位置index
              for (
                let j = currentIndex;
                j < this.processStructure.length;
                j++
              ) {
                if (this.processStructure[j].id !== j) {
                  this.processStructure[j].id = j;
                }
              }
            } else if (currentIndex === 0) {
              // 删除节点
              this.processStructure.splice(currentIndex, 1);
            }
            // this.updateSteps();

            // 重新渲染
            this.stepChart.dispose();
            this.stepChart = null;
            this.showSteps();

            // collaborative
            let socketMsg = {
              type: "step",
              operate: "update",
              content: JSON.stringify(this.processStructure)
            };
            // this.subprojectSocket.send(socketMsg);
          } else {
            this.$Notice.info({
              desc:
                "The selected step " +
                this.selectedStep[i].name +
                "can not be removed. Because it has the next step, or it is a active step."
            });
          }
        }
      }
    },
    updateSteps() {
      let obj = new URLSearchParams();
      obj.append("subProjectId", this.subProjectInfo.subProjectId);
      obj.append("solvingProcess", JSON.stringify(this.processStructure));
      this.axios
        .post("/GeoProblemSolving/subProject/update", obj)
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data != "Fail") {
            this.$Notice.info({
              desc: "Update successfully!"
            });
          } else {
            this.$Message.error("Update sub-project failed.");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    createNewStep() {
      this.$router.push({ path: "../stepCreation" });
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
