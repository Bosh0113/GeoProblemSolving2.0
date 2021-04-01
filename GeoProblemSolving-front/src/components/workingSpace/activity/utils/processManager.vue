<style scoped>
.btnHoverGray:hover {
  background-color: #808695;
  color: white;
}
#steps {
  margin-top: 20px;
  padding: 15px;
  background-color: #f8f8f9;
  height: calc(100vh - 200px);
  width: calc(100vw - 400px);
}
</style>
<template>
  <Row>
    <Col span="24" style="margin-top: 20px">
      <div>
        <Row type="flex" justify="space-around">
          <div style="width: 80%; height: 25px; text-align: center">
            <span style="font-weight: bold; font-size: 16px"
              >Procedure of the current activity</span
            >
            <!-- 右侧三个按钮-->
            <template
              v-if="
                permissionIdentity(
                  activityInfo.permission,
                  'manage_child_activity'
                )
              "
            >
              <Button
                v-if="nodePositionBtn && procedureDrag"
                type="warning"
                @click="editPosition()"
                size="small"
                icon="md-git-commit"
                title="Adjust the postion of nodes"
                style="float: right; margin-left: 10px"
                >Move node</Button
              >
              <Button
                v-else-if="nodePositionBtn && !procedureDrag"
                type="warning"
                @click="editPosition()"
                size="small"
                icon="md-git-network"
                title="Move the postion of procedure"
                style="float: right; margin-left: 10px"
                >Move procedure</Button
              >
              <Button
                v-else
                type="default"
                size="small"
                icon="md-git-commit"
                title="Adjust the postion of nodes"
                style="float: right; margin-left: 10px; cursor: default"
                >Move node</Button
              >
              <!--link按钮-->
              <Button
                v-if="removeLinkBtn"
                type="error"
                size="small"
                @click="removeLink()"
                icon="md-remove"
                title="Remove links"
                style="float: right; margin-left: 10px"
                >Unlink</Button
              >
              <Button
                v-else
                type="default"
                size="small"
                icon="md-remove"
                title="Please click and select one node (activity)"
                style="float: right; margin-left: 10px; cursor: default"
                >Unlink</Button
              >
              <!--link-->
              <Button
                v-show="linkStep == 0"
                type="info"
                size="small"
                @click="linkActivities()"
                icon="md-link"
                title="Start to link"
                style="float: right; margin-left: 10px"
                id="linkBegin"
                >Start to link</Button
              >
              <Button
                v-if="linkBtn"
                v-show="linkStep == 1"
                type="success"
                size="small"
                @click="linkActivities()"
                icon="md-link"
                title="Complete linking"
                style="float: right; margin-left: 10px"
                id="linkEnd"
                >Complete linking</Button
              >
              <Button
                v-else
                v-show="linkStep == 1"
                type="default"
                size="small"
                icon="md-link"
                title="Complete linking"
                style="float: right; margin-left: 10px; cursor: default"
                id="linkEnd"
                >Complete linking</Button
              >
            </template>
          </div>
          <div id="steps"></div>
        </Row>
      </div>
    </Col>
    <Modal v-model="activityInfoModal" title="Information of the activity">
      <div>
        <label style="margin-left: 20px">Activity name:</label>
        <Input
          v-model="showActivityInfo.name"
          style="width: 300px; margin-left: 10px"
          readonly
        />
      </div>
      <div style="margin-top: 20px">
        <label style="margin-left: 20px">Activity purpose:</label>
        <Input
          v-model="showActivityInfo.purpose"
          style="width: 300px; margin-left: 17px"
          readonly
        />
      </div>
      <div slot="footer">
        <Button type="primary" @click="gotoActivity(showActivityInfo.aid)"
          >Go to this workspace</Button
        >
      </div>
    </Modal>
  </Row>
</template>
<script>
// import "driver.js/dist/driver.min.css";
import echarts from "echarts";
// import Driver from "driver.js";
import * as userRoleJS from "./../../../../api/userRole.js";
export default {
  props: ["activityInfo", "childActivities"],
  data() {
    return {
      projectInfo: parent.vm.projectInfo,
      // driver
      // driver: new Driver(),
      // user
      userInfo: JSON.parse(sessionStorage.getItem("userInfo")),
      userRole: "visitor",
      //button
      linkBtn: false,
      removeLinkBtn: false,
      nodePositionBtn: false,
      //link
      linkStep: 0,
      beginNode: {},
      endNode: {},
      // 添加/编辑step
      stepInfo: {
        aid: "",
        name: "",
        description: "",
        purpose: "",
      },
      stepInfoRule: {
        name: [
          { required: true, message: "Please enter name...", trigger: "blur" },
        ],
      },
      processStructure: [],
      workspaceName: "",
      typeList: [
        "Context definition & resource collection",
        "Data processing",
        "Data analysis",
        "Data visualization",
        "Geo-analysis model construction",
        "Model effectiveness evaluation",
        "Geographical simulation",
        "Decision making",
        "Multi-purpose",
      ],
      // 步骤逻辑图
      stepChart: null,
      // 选择的活动
      selectedActivities: [],
      // 双击展示活动信息
      activityInfoModal: false,
      showActivityInfo: {},
      // 工具
      personalTools: [],
      publicTools: [],
      personalToolsets: [],
      publicToolsets: [],
      selectActivityTools: [],
      selectActivityToolsets: [],
      // activity 结构信息
      procedureDrag: true,
      nodeData: [],
    };
  },
  created() {
    this.init();
    this.getProcessSteps();
  },
  mounted() {
    this.showSteps();
    this.btnEnable();
    this.roleIdentity();
  },
  beforeRouteLeave(next) {
    next();
  },
  beforeDestroy: function () {},
  methods: {
    init() {
      window.addEventListener("resize", this.updateStepchart);
    },
    roleIdentity() {
      this.userRole = userRoleJS.roleIdentify(
        this.activityInfo.members,
        this.userInfo.userId
      );
    },
    permissionIdentity(permission, operation) {
      return userRoleJS.permissionIdentity(
        JSON.parse(permission),
        this.userRole,
        operation
      );
    },
    updateStepchart() {
      // 重新渲染
      this.selectedActivities = [];
      this.stepChart.dispose();
      this.stepChart = null;
      this.showSteps();
      this.btnEnable();
    },
    btnEnable() {
      if (
        this.processStructure.length <= 0 ||
        this.processStructure.length == undefined
      ) {
        this.removeLinkBtn = false;
        this.nodePositionBtn = false;
      } else {
        this.nodePositionBtn = true;
        if (this.selectedActivities.length == 0) {
          this.removeLinkBtn = false;
        } else if (this.selectedActivities.length == 1) {
          this.removeLinkBtn = false;
        } else if (this.selectedActivities.length == 2) {
          let activity1 = this.selectedActivities[0];
          let activity2 = this.selectedActivities[1];
          if (this.linkStep == 1) {
            this.removeLinkBtn = false;
            if (activity1.next.contains(activity2)) {
              this.linkBtn = false;
            } else {
              this.linkBtn = true;
            }
          } else if (this.linkStep == 0) {
            this.linkBtn = false;
            if (activity1.next.contains(activity2)) {
              this.removeLinkBtn = true;
            } else {
              this.removeLinkBtn = false;
            }
          }
        }
      }
    },
    getProcessSteps() {
      if (this.activityInfo.pathway == undefined) {
        this.activityInfo.pathway = [];
        for (var i = 0; i < this.childActivities.length; i++) {
          // catagory of node
          var nodeCategory = this.getStepCategroy(
            this.childActivities[i].purpose
          );
          // create step node
          let rows = Math.round(Math.sqrt(this.childActivities.length / 2)) * 2;
          var newStepNode = {
            id: i,
            aid: this.childActivities[i].aid,
            name: this.childActivities[i].name,
            category: nodeCategory,
            last: [],
            next: [],
            x: (i % rows) * 100,
            y: Math.floor(i / rows) * 100,
            status: true,
          };
          this.activityInfo.pathway.push(newStepNode);
        }
      } else if (
        this.activityInfo.pathway.length < this.childActivities.length
      ) {
        for (
          var i = this.activityInfo.pathway.length;
          i < this.childActivities.length;
          i++
        ) {
          // catagory of node
          var nodeCategory = this.getStepCategroy(
            this.childActivities[i].purpose
          );
          // create step node
          var newStepNode = {
            id: i,
            aid: this.childActivities[i].aid,
            name: this.childActivities[i].name,
            category: nodeCategory,
            last: [],
            next: [],
            x: (i % 5) * 100,
            y: Math.floor(i / 5) * 100,
            status: true,
          };
          this.activityInfo.pathway.push(newStepNode);
          this.updatePathway();
        }
      } else if (
        this.activityInfo.pathway.length > this.childActivities.length
      ) {
        for (var i = this.activityInfo.pathway.length - 1; i >= 0; i--) {
          let exist = false;
          for (var j = 0; j < this.childActivities.length; j++) {
            if (
              this.activityInfo.pathway[i].aid == this.childActivities[j].aid
            ) {
              exist = true;
            }
          }
          if (!exist) {
            this.removePathwayNode(this.activityInfo.pathway[i].aid);
          }
        }
      }
      this.processStructure = this.activityInfo.pathway;
      this.updatePathway();
    },
    showSteps() {
      this.selectedActivities = [];
      let option = {
        animationDurationUpdate: 500,
        animationEasingUpdate: "quinticInOut",
        legend: {
          show: true,
          data: [
            {
              name: "Context definition & resource collection",
              icon: "circle",
            },
            {
              name: "Data processing",
              icon: "circle",
            },
            {
              name: "Data visualization",
              icon: "circle",
            },
            {
              name: "Geo-analysis model construction",
              icon: "circle",
            },
            {
              name: "Model effectiveness evaluation",
              icon: "circle",
            },
            {
              name: "Geographical simulation",
              icon: "circle",
            },
            {
              name: "Data analysis",
              icon: "circle",
            },
            {
              name: "Decision making",
              icon: "circle",
            },
            {
              name: "Multi-purpose",
              icon: "circle",
            },
          ],
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
                show: true,
              },
            },
            edgeSymbol: ["circle", "arrow"],
            edgeSymbolSize: [4, 10],
            focusNodeAdjacency: true,
            data: [],
            categories: [
              {
                name: "Context definition & resource collection",
              },
              {
                name: "Data processing",
              },
              {
                name: "Data visualization",
              },
              {
                name: "Geo-analysis model construction",
              },
              {
                name: "Model effectiveness evaluation",
              },
              {
                name: "Geographical simulation",
              },
              {
                name: "Data analysis",
              },
              {
                name: "Decision making",
              },
              {
                name: "Multi-purpose",
              },
            ],
            links: [],
            lineStyle: {
              normal: {
                opacity: 1,
                width: 5,
                curveness: 0.1,
              },
            },
          },
        ],
      };
      this.nodeData = [];
      if (this.processStructure.length > 0) {
        for (var i = 0; i < this.processStructure.length; i++) {
          //get data
          let datum = {
            name: this.processStructure[i].name,
            index: this.processStructure[i].id,
            aid: this.processStructure[i].aid,
            x: this.processStructure[i].x,
            y: this.processStructure[i].y,
            category: this.processStructure[i].category,
            next: this.processStructure[i].next,
            last: this.processStructure[i].last,
            symbolSize: 45,
          };
          this.nodeData.push(datum);

          //get links
          for (var j = 0; j < this.processStructure[i].next.length; j++) {
            option.series[0].links.push({
              source: this.processStructure[i].name,
              target: this.processStructure[i].next[j].name,
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
      this.stepChart.on("click", function (params) {
        if (_this.procedureDrag) {
          if (option.series[0].data[params.data.index].symbolSize == 45) {
            option.series[0].data[params.data.index].symbolSize = 60;

            // record the selected step nodes
            _this.selectedActivities.push({
              aid: params.data.aid,
              id: params.data.index,
              name: params.data.name,
              category: params.data.category,
              next: params.data.next,
              last: params.data.last,
            });

            if (_this.selectedActivities.length > 2) {
              option.series[0].data[
                _this.selectedActivities[0].id
              ].symbolSize = 45;
              _this.selectedActivities.splice(0, 1);
            }
          } else if (
            option.series[0].data[params.data.index].symbolSize == 60
          ) {
            option.series[0].data[params.data.index].symbolSize = 45;

            // remove these not selected step nodes
            for (var i = 0; i < _this.selectedActivities.length; i++) {
              if (_this.selectedActivities[i].aid == params.data.aid) {
                _this.selectedActivities.splice(i, 1);
                break;
              }
            }
          }
          _this.stepChart.setOption(option);
          _this.btnEnable();
        }
      });
      // 双击切换当前步骤
      this.stepChart.on("dblclick", function (params) {
        if (_this.procedureDrag) {
          _this.activityInfoModal = true;
          let purpose = _this.getStepType(params.data.category);
          let activity = {
            aid: params.data.aid,
            name: params.data.name,
            purpose: purpose,
          };
          _this.showActivityInfo = activity;
        }
      });
    },
    getStepType(category) {
      let purpose;
      if (category == 0) {
        purpose = "Context definition & resource collection";
      } else if (category == 1) {
        purpose = "Data processing";
      } else if (category == 2) {
        purpose = "Data visualization";
      } else if (category == 3) {
        purpose = "Geo-analysis model construction";
      } else if (category == 4) {
        purpose = "Model effectiveness evaluation";
      } else if (category == 5) {
        purpose = "Geographical simulation";
      } else if (category == 6) {
        purpose = "Data analysis";
      } else if (category == 7) {
        purpose = "Decision making";
      } else {
        purpose = "Multi-purpose";
      }
      return purpose;
    },
    getStepCategroy(purpose) {
      let category;
      if (purpose == "Context definition & resource collection") {
        category = 0;
      } else if (purpose == "Data processing") {
        category = 1;
      } else if (purpose == "Data visualization") {
        category = 2;
      } else if (purpose == "Geo-analysis model construction") {
        category = 3;
      } else if (purpose == "Model effectiveness evaluation") {
        category = 4;
      } else if (purpose == "Geographical simulation") {
        category = 5;
      } else if (purpose == "Data analysis") {
        category = 6;
      } else if (purpose == "Decision making") {
        category = 7;
      } else {
        category = 8;
      }
      return category;
    },
    // move
    editPosition() {
      this.procedureDrag = !this.procedureDrag;
      // swith off the node dragging fuction
      if (this.procedureDrag) {
        this.updatePathway();
      }

      this.stepChart.setOption({
        animationDurationUpdate: this.procedureDrag ? 500 : 0,
        series: [
          {
            id: "procedure",
            roam: this.procedureDrag,
          },
        ],
      });

      // node的拖拽功能
      let _this = this;
      try {
        this.stepChart.setOption({
          // https://www.echartsjs.com/zh/tutorial.html#小例子：自己实现拖拽
          graphic: echarts.util.map(
            _this.nodeData,
            function (dataItem, dataIndex) {
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
                  r: 20,
                },
                position: nodePosition,
                invisible: true,
                draggable: !_this.procedureDrag,
                z: 100,
                ondrag: echarts.util.curry(function () {
                  let position = _this.stepChart.convertFromPixel(
                    { seriesIndex: 0 },
                    this.position
                  );
                  _this.nodeData[dataIndex].x = position[0];
                  _this.nodeData[dataIndex].y = position[1];
                  _this.processStructure[dataIndex].x = position[0];
                  _this.processStructure[dataIndex].y = position[1];
                  _this.stepChart.setOption({
                    series: [
                      {
                        id: "procedure",
                        data: _this.nodeData,
                      },
                    ],
                  });
                }, dataIndex),
              };
            }
          ),
        });
      } catch (ex) {
        this.$Nothis.info({
          desc: "ERROR!",
        });
        tice;
      }
    },
    updatePathway() {
      let updateurl = "";
      if (this.activityInfo.level == 0) {
        updateurl = "/GeoProblemSolving/project/" + this.activityInfo.aid;
      } else if (this.activityInfo.level == 1) {
        updateurl = "/GeoProblemSolving/subproject/" + this.activityInfo.aid;
      } else if (this.activityInfo.level > 1) {
        updateurl = "/GeoProblemSolving/activity/" + this.activityInfo.aid;
      } else {
        return;
      }
      let data = {
        aid: this.activityInfo.aid,
        pathway: this.processStructure,
      };

      this.axios
        .put(updateurl, data)
        .then((res) => {
          if (res.data == "Offline") {
            if (this.activityInfo.level == 1) {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else {
              parent.location.href = "/GeoProblemSolving/login";
            }
          } else if (res.data.code == 0) {
            this.updateStepchart();
            // this.$Notice.info({
            //   desc: "Reshape pathway successfully!",
            // });
          } else {
            this.$Message.error("Fail to reshape pathway.");
            console.log(res.data.msg);
          }
        })
        .catch((err) => {
          throw err;
        });
    },
    // remove
    removePathwayNode(aid) {
      if (this.activityInfo.pathway != undefined) {
        let nodeId = -1;
        for (var i = 0; i < this.activityInfo.pathway.length; i++) {
          if (this.activityInfo.pathway[i].aid == aid) {
            nodeId = this.activityInfo.pathway[i].id;
            // remove mode
            this.activityInfo.pathway.splice(i, 1);
          }
        }
        // normalize
        if (nodeId != -1) {
          for (var i = 0; i < this.activityInfo.pathway.length; i++) {
            let node = this.activityInfo.pathway[i];
            // node
            if (node.id > nodeId) {
              node.id = node.id - 1;
            }
            // last
            for (var j = 0; j < node.last.length; j++) {
              if (node.last[j].id == nodeId) {
                node.last.splice(j, 1);
              } else if (node.last[j].id > nodeId) {
                node.last[j].id = node.last[j].id - 1;
              }
            }
            // next
            for (var j = 0; j < node.next.length; j++) {
              if (node.next[j].id == nodeId) {
                node.next.splice(j, 1);
              } else if (node.next[j].id > nodeId) {
                node.next[j].id = node.next[j].id - 1;
              }
            }
          }
        }
      }
    },
    // link
    linkActivities() {
      if (this.linkStep == 0) {
        this.showSteps();
        this.selectedActivities = [];
        this.linkStep = 1;
        this.$Notice.info({
          desc: "Please select two nodes!",
        });
      } else if (this.linkStep == 1) {
        if (this.selectedActivities.length == 2) {
          // operation
          this.beginNode = this.selectedActivities[0];
          this.endNode = this.selectedActivities[1];
          this.buildLink();
          // record and end
          this.linkStep = 0;
        } else {
          this.$Notice.info({
            desc: "Please select two nodes and restart to link activities!",
          });
          // init
          this.linkStep = 0;
          this.showSteps();
          this.selectedActivities = [];
        }
      }
    },
    buildLink() {
      // The next-activities have been selected
      // 前后继承关系
      let lastnode = {
        name: this.beginNode.name,
        id: this.beginNode.id,
      };
      let nextnode = {
        name: this.endNode.name,
        id: this.endNode.id,
      };
      for (var k = 0; k < this.processStructure.length; k++) {
        // 后继节点
        if (this.processStructure[k].aid == this.endNode.aid) {
          if (!this.processStructure[k].last.contains(lastnode)) {
            this.processStructure[k].last.push(lastnode);
          }
        }
        // 前驱节点
        if (this.processStructure[k].aid == this.beginNode.aid) {
          if (!this.processStructure[k].next.contains(nextnode)) {
            this.processStructure[k].next.push(nextnode);
          }
        }
      }
      // 重新渲染
      this.updateStepchart();
      // 更新pathway
      this.newLinkStore(this.beginNode.aid, this.endNode.aid);
      this.beginNode = {};
      this.endNode = {};
    },
    newLinkStore(begin, end) {
      let updateurl = "";
      let pid = "test"; // 待完善...
      if (this.activityInfo.level == 0) {
        updateurl =
          "/GeoProblemSolving/project/link/" +
          begin +
          "/" +
          end +
          "?pid=" +
          pid;
      } else if (this.activityInfo.level == 1) {
        updateurl =
          "/GeoProblemSolving/subproject/link/" +
          begin +
          "/" +
          end +
          "?pid=" +
          pid;
      } else if (this.activityInfo.level > 1) {
        updateurl =
          "/GeoProblemSolving/activity/link/" +
          begin +
          "/" +
          end +
          "?pid=" +
          pid;
      } else {
        return;
      }
      let data = {
        aid: this.activityInfo.aid,
        pathway: this.processStructure,
      };

      this.axios
        .post(updateurl, data)
        .then((res) => {
          if (res.data == "Offline") {
            if (this.activityInfo.level == 1) {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else {
              parent.location.href = "/GeoProblemSolving/login";
            }
          } else if (res.data.code == 0) {
            this.$Notice.info({
              desc: "Link activities successfully!",
            });
          } else {
            this.$Message.error("Fail to link activities.");
            console.log(res.data.msg);
          }
        })
        .catch((err) => {
          throw err;
        });
    },
    // seperate
    removeLink() {
      this.beginNode = this.selectedActivities[0];
      this.endNode = this.selectedActivities[1];

      for (let i = 0; i < this.processStructure.length; i++) {
        // 前驱节点
        if (this.processStructure[i].aid == this.beginNode.aid) {
          for (let j = 0; j < this.processStructure[i].next.length; j++) {
            if (this.processStructure[i].next[j].name == this.endNode.name) {
              this.processStructure[i].next.splice(j, 1);
              break;
            }
          }
        }
        // 后继节点
        if (this.processStructure[i].aid == this.endNode.aid) {
          for (let j = 0; j < this.processStructure[i].last.length; j++) {
            if (this.processStructure[i].last[j].name == this.beginNode.name) {
              this.processStructure[i].last.splice(j, 1);
              break;
            }
          }
        }
      }

      // 重新渲染
      this.updateStepchart();
      // 更新pathway
      this.delLinkStore(this.beginNode.aid, this.endNode.aid);
      this.beginNode = {};
      this.endNode = {};
    },
    delLinkStore(begin, end) {
      let updateurl = "";
      if (this.activityInfo.level == 0) {
        updateurl = "/GeoProblemSolving/project/separate/" + begin + "/" + end;
      } else if (this.activityInfo.level == 1) {
        updateurl =
          "/GeoProblemSolving/subproject/separate/" + begin + "/" + end;
      } else if (this.activityInfo.level > 1) {
        updateurl = "/GeoProblemSolving/activity/separate/" + begin + "/" + end;
      } else {
        return;
      }
      let data = {
        aid: this.activityInfo.aid,
        pathway: this.processStructure,
      };

      this.axios
        .post(updateurl, data)
        .then((res) => {
          if (res.data == "Offline") {
            if (this.activityInfo.level == 1) {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else {
              parent.location.href = "/GeoProblemSolving/login";
            }
          } else if (res.data.code == 0) {
            this.$Notice.info({
              desc: "Seperate activities successfully!",
            });
          } else {
            this.$Message.error("Fail to seperate activities.");
            console.log(res.data.msg);
          }
        })
        .catch((err) => {
          throw err;
        });
    },
    gotoActivity(aid) {
      for (let i = 0; i < this.childActivities.length; i++) {
        if (this.childActivities[i].aid == aid) {
          parent.location.href =
            "/GeoProblemSolving/projectInfo/" +
            this.projectInfo.aid +
            "?content=workspace&aid=" +
            this.childActivities[i].aid +
            "&level=" +
            this.childActivities[i].level;
        }
      }
    },
  },
};
</script>
