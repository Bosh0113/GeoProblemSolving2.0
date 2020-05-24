<style scoped>
</style>
<template>
  <span class="stepChange">
    <Button @click="getProcessSteps()" style="margin-left:20px;" size="small">Switch step</Button>
    <Modal
      width="800"
      v-model="stepChanged"
      title="Switch to the last/next step"
      :mask-closable="false"
      footer-hide
    >
      <div style="width:780px;height:300px" id="steps"></div>
      <span>Note: double click on the node and switch the step.</span>
    </Modal>
  </span>
</template>
<script>
import echarts from "echarts";
export default {
  data() {
    return {
      stepChanged: false,
      partProcesses: []
    };
  },
  props: ["stepInfo", "solvingProcess", "scope"],
  created() {},
  mounted() {
    Array.prototype.contains = function(obj) {
      var i = this.length;
      while (i--) {
        if (this[i].id != undefined && this[i].id === obj.id) {
          return true;
        }
      }
      return false;
    };
  },
  methods: {
    getProcessSteps() {
      this.partProcesses = [];
      if (this.solvingProcess != undefined && this.solvingProcess.length > 0) {
        let processStructure = JSON.parse(this.solvingProcess);
        for (let i = 0; i < processStructure.length; i++) {
          //get data
          if (processStructure[i].stepID == this.stepInfo.stepId) {
            // last
            let lastSteps = processStructure[i].last;
            for (let j = 0; j < lastSteps.length; j++) {
              let lastnode = processStructure[lastSteps[j].id];
              if (!this.partProcesses.contains(lastnode)) {
                this.partProcesses.push(lastnode);
              }
            }
            // current
            this.partProcesses.push(processStructure[i]);
            // next
            let nextSteps = processStructure[i].next;
            for (let j = 0; j < nextSteps.length; j++) {
              let nextnode = processStructure[nextSteps[j].id];
              if (!this.partProcesses.contains(nextnode)) {
                this.partProcesses.push(nextnode);
              }
            }
          }
        }

        this.showSteps();
        this.stepChanged = true;
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
                width: 3,
                curveness: 0.1
              }
            }
          }
        ]
      };
      if (this.partProcesses.length > 0) {
        this.selectedStep = [];
        for (let i = 0; i < this.partProcesses.length; i++) {
          //get data
          if (this.partProcesses[i].stepID == this.stepInfo.stepId) {
            option.series[0].data.push({
              name: this.partProcesses[i].name,
              index: this.partProcesses[i].id,
              stepId: this.partProcesses[i].stepID,
              x: this.partProcesses[i].x,
              y: this.partProcesses[i].y,
              category: this.partProcesses[i].category,
              symbolSize: 60
            });
            this.selectedStep.push({
              stepId: this.partProcesses[i].stepID,
              index: this.partProcesses[i].id,
              name: this.partProcesses[i].name
            });
          } else {
            option.series[0].data.push({
              name: this.partProcesses[i].name,
              index: this.partProcesses[i].id,
              stepId: this.partProcesses[i].stepID,
              x: this.partProcesses[i].x,
              y: this.partProcesses[i].y,
              category: this.partProcesses[i].category,
              symbolSize: 45
            });
          }

          //get links
          for (let j = 0; j < this.partProcesses[i].next.length; j++) {
            option.series[0].links.push({
              source: this.partProcesses[i].name,
              target: this.partProcesses[i].next[j].name
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
      this.stepChart.on("click", function(params) {});
      // 双击切换当前步骤
      this.stepChart.on("dblclick", function(params) {
        _this.stepChanged = false;
        _this.enterStep(params.data.category, params.data.stepId);
      });
    },
    enterStep(type, stepId) {
      if (this.scope == "project") {
        switch (type) {
          case 0: {
            this.$router.push({
              name: "contextDefinitionP",
              params: { stepId: stepId }
            });
            break;
          }
          case 1: {
            this.$router.push({
              name: "dataProcessingP",
              params: { stepId: stepId }
            });
            break;
          }
          case 2: {
            this.$router.push({
              name: "modelProcessP",
              params: { stepId: stepId }
            });
            break;
          }
          case 3: {
            this.$router.push({
              name: "modelEvaluationP",
              params: { stepId: stepId }
            });
            break;
          }
          case 4: {
            this.$router.push({
              name: "analysisP",
              params: { stepId: stepId }
            });
            break;
          }
          case 5: {
            this.$router.push({
              name: "simulationP",
              params: { stepId: stepId }
            });
            break;
          }
          case 6: {
            this.$router.push({
              name: "visualizationP",
              params: { stepId: stepId }
            });
            break;
          }
          case 7: {
            this.$router.push({
              name: "decisionMakingP",
              params: { stepId: stepId }
            });
            break;
          }
        }
      } else {
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
            name: "visualization",
            params: { stepId: stepId }
          });
        } else if (type == 3) {
          this.$router.push({
            name: "modelBuild",
            params: { stepId: stepId }
          });
        } else if (type == 4) {
          this.$router.push({
            name: "modelEvaluation",
            params: { stepId: stepId }
          });
        } else if (type == 5) {
          this.$router.push({
            name: "simulation",
            params: { stepId: stepId }
          });
        } else if (type == 6) {
          this.$router.push({
            name: "analysis",
            params: { stepId: stepId }
          });
        } else if (type == 7) {
          this.$router.push({
            name: "decisionMaking",
            params: { stepId: stepId }
          });
        }
      }
    }
  }
};
</script>