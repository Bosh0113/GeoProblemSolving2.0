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
<!--            右侧三个按钮-->
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
<!--            link按钮      -->
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
<!--            link的三种状态 -->
            <Button
              v-if="linkStep == 0"
              type="default"
              size="small"
              @click="linkActivities()"
              icon="md-link"
              title="Start to link"
              style="float: right; margin-left: 10px"
              id="linkBegin"
              >Start to link</Button
            >
            <Button
              v-else-if="linkStep == 1"
              type="info"
              size="small"
              @click="linkActivities()"
              icon="md-link"
              title="Confirm the beginning activities"
              style="float: right; margin-left: 10px"
              id="Linking"
              >Linking</Button
            >
            <Button
              v-else-if="linkStep == 2"
              type="success"
              size="small"
              @click="linkActivities()"
              icon="md-link"
              title="Confirm the end activities"
              style="float: right; margin-left: 10px"
              id="linkEnd"
              >Complete linking</Button
            >
            <!-- <template v-if="permissionIdentity(activityInfo.permission, 'activity_manage')">
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
                <Button
                  v-if="removeLinkBtn"
                  type="error"
                  size="small"
                  @click="removeLink()"
                  icon="md-remove"
                  title="Remove links"
                  style="float:right;margin-left:10px"
                >Unlink</Button>
                <Button
                  v-else
                  type="default"
                  size="small"
                  icon="md-remove"
                  title="Please click and select one node (activity)"
                  style="float:right;margin-left:10px; cursor:default"
                >Unlink</Button>
                <Button
                  v-if="linkStep == 0"
                  type="default"
                  size="small"
                  @click="linkActivities()"
                  icon="md-link"
                  title="Start to link"
                  style="float: right; margin-left: 10px"
                  id="linkBegin"
                  >Start to link</Button
                >
                <Button
                  v-else-if="linkStep == 1"
                  type="info"
                  size="small"
                  @click="linkActivities()"
                  icon="md-link"
                  title="Confirm the beginning activities"
                  style="float: right; margin-left: 10px"
                  id="Linking"
                  >Linking</Button
                >
                <Button
                  v-else-if="linkStep == 2"
                  type="success"
                  size="small"
                  @click="linkActivities()"
                  icon="md-link"
                  title="Confirm the end activities"
                  style="float: right; margin-left: 10px"
                  id="linkEnd"
                  >Complete linking</Button
                >
              </template> -->
          </div>
          <div id="steps"></div>
        </Row>
      </div>
    </Col>
    <Modal
      v-model="delModal"
      title="Delete this process"
      @on-ok="delLinkGraph"
      ok-text="OK"
      cancel-text="Cancel"
    >
      <p>Do you really want to delete this step?</p>
    </Modal>
    <Modal v-model="resetSubProjectTypeNotice" title="Reset workspace type">
      <h2>Please confirm that all nodes have been deleted.</h2>
      <div slot="footer">
        <Button type="primary" @click="resetSubProjectTypeNotice = false"
          >OK</Button
        >
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
        <Button type="primary" @click="resetProjectTypeNotice = false"
          >OK</Button
        >
      </div>
    </Modal>
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
        <Button
          type="primary"
          @click="gotoActivity(showActivityInfo.purpose, showActivityInfo.aid)"
          >Go to this workspace</Button
        >
      </div>
    </Modal>
    <Modal
      width="800px"
      v-model="addActivityModal"
      title="Select one child activity as the step"
      :styles="{ top: '20px' }"
    >
      <div style="width: 640px; margin-left: 50px">
        <Form
          ref="stepInfo"
          :model="stepInfo"
          :rules="stepInfoRule"
          :label-width="120"
        >
          <FormItem label="Child activity:" style="margin-top: 40px">
            <Select
              v-model="stepInfo.aid"
              placeholder="Select a child activity"
              @on-change="activitySelected"
            >
              <Option
                v-for="activity in childActivities"
                :key="activity.aid"
                :value="activity.aid"
                :title="activity.description"
                >{{ activity.name }}</Option
              >
            </Select>
          </FormItem>
          <FormItem label="Name:" prop="name">
            <Input
              v-model="stepInfo.name"
              placeholder="The name of the selected activity"
              readonly
            />
          </FormItem>
          <FormItem
            label="Description:"
            prop="description"
            style="margin-top: 50px"
          >
            <Input
              v-model="stepInfo.description"
              type="textarea"
              :rows="4"
              placeholder="The description of the selected activity"
              readonly
            />
          </FormItem>
          <FormItem label="Purpose:" prop="purpose">
            <Input
              v-model="stepInfo.purpose"
              placeholder="The purpose of the selected activity"
              readonly
            />
          </FormItem>
        </Form>
      </div>
      <div slot="footer">
        <Button @click="addActivityModal = false">Cancel</Button>
        <Button type="primary" @click="createStep('stepInfo')">Submit</Button>
      </div>
    </Modal>
  </Row>
</template>
<script>
import "driver.js/dist/driver.min.css";
import echarts from "echarts";
import Driver from "driver.js";
import * as userRoleJS from "./../../../../api/userRole.js";
export default {
  props: ["activityInfo", "childActivities"],
  data() {
    return {
      projectInfo: parent.vm.projectInfo,
      // driver
      driver: new Driver(),
      // user
      userInfo: JSON.parse(sessionStorage.getItem("userInfo")),
      userRole: "visitor",
      //button
      removeLinkBtn: false,
      nodePositionBtn: false,
      //link
      linkStep: 0,
      beginNodes: [],
      endNodes: [],
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
      // 模态框
      addActivityModal: false,
      delModal: false,
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
      resetSubProjectTypeNotice: false,
      resetSubProjectTypeModel: false,
      resetProjectTypeNotice: false,
      resetProjectTypeModel: false,
    };
  },
  created() {
    this.init();
    this.getProcessSteps();

    Array.prototype.contains = function (obj) {
      var i = this.length;
      while (i--) {
        if (
          (this[i].tid != undefined && this[i].tid === obj.tid) ||
          (this[i].tsId != undefined && this[i].tsId === obj.tsId) ||
          (this[i].id != undefined && this[i].id === obj.id) ||
          (this[i].aid != undefined && this[i].aid === obj.aid)
        ) {
          return true;
        }
      }
      return false;
    };
  },
  mounted() {
    this.showSteps();
    this.btnEnable();
    this.roleIdentity();
  },
  beforeRouteLeave(to, from, next) {
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
          this.removeLinkBtn = true;
        } else if (this.selectedActivities.length > 1) {
          this.removeLinkBtn = false;
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
          var newStepNode = {
            id: i,
            aid: this.childActivities[i].aid,
            name: this.childActivities[i].name,
            category: nodeCategory,
            last: [],
            next: [],
            x: (i % 5) * 100,
            y: Math.floor(i / 5) * 100,
            end: true,
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
            end: true,
            status: true,
          };
          this.activityInfo.pathway.push(newStepNode);
        }
      }
      this.processStructure = this.activityInfo.pathway;
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
            });
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
    editPosition() {
      this.procedureDrag = !this.procedureDrag;

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
          graphic: echarts.util.map(_this.nodeData, function (
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
          }),
        });
      } catch (ex) {
        this.$Nothis.info({
          desc: "ERROR!",
        });
        tice;
      }
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
    linkInstruct() {
      this.driver.defineSteps([
        {
          element: "#linkBegin",
          popover: {
            title: "Before we start",
            description:
              "If you are familiar with the operation, you can close this instruction.",
            position: "left",
          },
        },
        {
          element: "#steps",
          popover: {
            title: "Select nodes",
            description: "Select nodes as the beginning activities.",
            position: "top",
          },
        },
        {
          element: "#Linking",
          popover: {
            title: "Click the Link button",
            description:
              "Click the button to confirm the beginning activities.",
            position: "bottom",
          },
        },
        {
          element: "#steps",
          popover: {
            title: "Select nodes",
            description: "Select nodes as the end activities.",
            position: "top-right",
          },
        },
        {
          element: "#linkEnd",
          popover: {
            title: "Click the Link button",
            description: "Confirm the end activities and build links.",
            position: "bottom",
          },
        },
      ]);
      this.driver.start();
    },
    linkActivities(activities) {
      if (this.linkStep == 0) {
        let driverShow = sessionStorage.getItem("driver");
        if (driverShow == undefined) {
          this.linkInstruct();
          sessionStorage.setItem("driver", false);
        }
        this.showSteps();
        this.selectedActivities = [];
        this.linkStep = 1;
      } else if (this.linkStep == 1) {
        if (this.selectedActivities.length > 0) {
          // operation
          this.beginNodes = this.selectedActivities;
          // record
          this.showSteps();
          this.selectedActivities = [];
          this.linkStep = 2;
        } else {
          this.$Notice.info({
            desc: "Please select at least one node!",
          });
        }
      } else if (this.linkStep == 2) {
        if (this.selectedActivities.length > 0) {
          // operation
          this.endNodes = this.selectedActivities;
          this.buildLink();
          // record and end
          this.showSteps();
          this.linkStep = 0;
        } else {
          this.$Notice.info({
            desc: "Please select at least one node!",
          });
        }
      }
    },
    buildLink() {
      // The next-activities have been selected
      for (var i = 0; i < this.beginNodes.length; i++) {
        for (var j = 0; j < this.endNodes.length; j++) {
          // 前后继承关系
          let lastnode = {
            name: this.beginNodes[i].name,
            id: this.beginNodes[i].id,
          };
          let nextnode = {
            name: this.endNodes[j].name,
            id: this.endNodes[j].id,
          };
          for (var k = 0; k < this.processStructure.length; k++) {
            if (this.processStructure[k].aid == this.endNodes[j].aid) {
              if (!this.processStructure[k].last.contains(lastnode)) {
                this.processStructure[k].last.push(lastnode);
              }
            }
            if (this.processStructure[k].aid == this.beginNodes[i].aid) {
              if (!this.processStructure[k].next.contains(nextnode)) {
                this.processStructure[k].next.push(nextnode);
              }
            }
          }
        }
      }
      this.beginNodes = [];
      this.endNodes = [];
      // 重新渲染
      this.updateStepchart();
      // 更新Step
      this.updateSteps();
    },
    addNewActivtiy() {
      // 防止Link与add相互干扰
      this.selectedPreActivities = [];
      // add
      // 选择父节点检测
      if (
        this.processStructure.length != 0 &&
        (this.selectedActivities.length == undefined ||
          this.selectedActivities.length == 0)
      ) {
        this.$Notice.info({
          desc: "Please select at least one node in advance!",
        });
        return;
      }

      // 创建步骤模态框
      this.addActivityModal = true;
    },
    activitySelected(aid) {
      for (let i = 0; i < this.childActivities.length; i++) {
        if (this.childActivities[i].aid == aid) {
          let activity = this.childActivities[i];
          this.stepInfo = {
            aid: activity.aid,
            name: activity.name,
            description: activity.description,
            purpose: activity.purpose,
          };
        }
      }
    },
    createStep(name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          let id = this.stepInfo.aid;
          if (this.processStructure.length == 0) {
            // 新步骤的类别
            let nodeCategory = 0;
            nodeCategory = this.getStepCategroy(this.stepInfo.purpose);
            // create step node
            let newStepNode = {
              id: 0,
              aid: id,
              name: this.stepInfo.name,
              category: nodeCategory,
              last: [],
              next: [],
              x: 600,
              y: 200,
              level: 0,
              end: true,
              activeStatus: true,
            };
            this.processStructure.push(newStepNode);
            // 重新渲染
            this.updateStepchart();
            // 存储Step
            this.updateSteps();
          } else if (this.selectedActivities.length > 0) {
            //  计算新增节点的属性信息
            let lastNode = [];
            let nodeLevel = 0;
            let nodeY = 0;
            let nodeCategory = 0;
            // var activityChangeList = [];
            for (var i = 0; i < this.selectedActivities.length; i++) {
              lastNode.push({
                name: this.selectedActivities[i].name,
                id: this.selectedActivities[i].id,
              });
              if (
                this.processStructure[this.selectedActivities[i].id].level >=
                nodeLevel
              ) {
                nodeLevel =
                  this.processStructure[this.selectedActivities[i].id].level +
                  1;
              }
              // modify original step node
              this.processStructure[this.selectedActivities[i].id].next.push({
                name: this.stepInfo.name,
                id: this.processStructure.length,
              });
              this.processStructure[this.selectedActivities[i].id].end = false;
              // calculate y
              if (this.processStructure[i].last == []) {
                nodeY = 200;
              } else {
                let sumY = 0;
                for (var j = 0; j < this.selectedActivities.length; j++) {
                  sumY += this.processStructure[this.selectedActivities[j].id]
                    .y;
                }
                nodeY = sumY / this.selectedActivities.length;
              }
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
            nodeCategory = this.getStepCategroy(this.stepInfo.purpose);
            // create step node
            let newStepNode = {
              id: this.processStructure.length,
              aid: id,
              name: this.stepInfo.name,
              category: nodeCategory,
              last: lastNode,
              next: [],
              x: 0,
              y: nodeY,
              level: nodeLevel,
              end: true,
              activeStatus: true,
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
            // 重新渲染
            this.updateStepchart();
            // 更新Step
            this.updateSteps();
          } else {
            this.$Notice.info({
              desc: "Select at least one step as source(s), please!",
            });
          }
        }
      });
    },
    // getPublicTools() {
    //   this.axios
    //     .get(
    //       "/GeoProblemSolving/tool/inquiry" +
    //         "?key=" +
    //         "privacy" +
    //         "&value=" +
    //         "Public"
    //     )
    //     .then((res) => {
    //       if (res.data == "Offline") {
    //         if (this.activityInfo.level == 1) {
    //           this.$store.commit("userLogout");
    //           this.$router.push({ name: "Login" });
    //         } else {
    //           parent.location.href = "/GeoProblemSolving/login";
    //         }
    //       } else if (res.data === "Fail") {
    //         // this.$Notice.error({ desc: "Loading tools fail." });
    //       } else if (res.data === "None") {
    //         // this.$Notice.error({ desc: "There is no existing tool" });
    //       } else {
    //         this.$set(this, "publicTools", res.data);
    //       }
    //     })
    //     .catch((err) => {
    //       throw err;
    //     });
    // },
    // getPersonalTools() {
    //   this.axios
    //     .get(
    //       `/GeoProblemSolving/tool/findByProvider/${this.$store.getters.userInfo.userId}`
    //     )
    //     .then((res) => {
    //       if (res.data == "Offline") {
    //         if (this.activityInfo.level == 1) {
    //           this.$store.commit("userLogout");
    //           this.$router.push({ name: "Login" });
    //         } else {
    //           parent.location.href = "/GeoProblemSolving/login";
    //         }
    //       } else if (res.data === "Fail") {
    //         // this.$Notice.error({ desc: "Loading tool fail." });
    //       } else if (res.data === "None") {
    //         // this.$Notice.error({ desc: "There is no existing tool" });
    //       } else {
    //         this.$set(this, "personalTools", res.data);
    //       }
    //     })
    //     .catch((err) => {
    //       throw err;
    //     });
    // },
    // getPublicToolsets() {
    //   this.axios
    //     .get(
    //       "/GeoProblemSolving/toolset/inquiry" +
    //         "?key=" +
    //         "privacy" +
    //         "&value=" +
    //         "Public"
    //     )
    //     .then((res) => {
    //       if (res.data == "Offline") {
    //         if (this.activityInfo.level == 1) {
    //           this.$store.commit("userLogout");
    //           this.$router.push({ name: "Login" });
    //         } else {
    //           parent.location.href = "/GeoProblemSolving/login";
    //         }
    //       } else if (res.data === "Fail") {
    //         // this.$Notice.error({ desc: "Loading toolsets fail." });
    //       } else if (res.data === "None") {
    //         // this.$Notice.error({ desc: "There is no existing toolset" });
    //       } else {
    //         this.$set(this, "publicToolsets", res.data);
    //       }
    //     })
    //     .catch((err) => {
    //       throw err;
    //     });
    // },
    // getPersonalToolsets() {
    //   this.axios
    //     .get(
    //       "/GeoProblemSolving/toolset/inquiryAll" +
    //         "?provider=" +
    //         this.$store.getters.userInfo.userId
    //     )
    //     .then((res) => {
    //       if (res.data == "Offline") {
    //         if (this.activityInfo.level == 1) {
    //           this.$store.commit("userLogout");
    //           this.$router.push({ name: "Login" });
    //         } else {
    //           parent.location.href = "/GeoProblemSolving/login";
    //         }
    //       } else if (res.data === "Fail") {
    //         // this.$Notice.error({ desc: "Loading toolsets fail." });
    //       } else if (res.data === "None") {
    //         // this.$Notice.error({ desc: "There is no existing toolset" });
    //       } else {
    //         this.$set(this, "personalToolsets", res.data);
    //       }
    //     })
    //     .catch((err) => {
    //       throw err;
    //     });
    // },
    filterShowListByType(purpose) {
      var toolList = this.filterDuplicateTools();
      this.selectActivityTools = this.getFilterToolResult(toolList, purpose);

      var toolsetList = this.filterDuplicateToolsets();
      this.selectActivityToolsets = this.getFilterToolsetResult(
        toolsetList,
        purpose
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
    getFilterToolResult(foreList, purpose) {
      let resultList = [];
      for (var i = 0; i < foreList.length; i++) {
        var stepTypes = foreList[i].recomStep;
        for (var j = 0; j < stepTypes.length; j++) {
          if (stepTypes[j] == stepType || stepTypes[j] == "General step") {
            resultList.push(foreList[i].tid);
            break;
          }
        }
      }
      return resultList;
    },
    getFilterToolsetResult(foreList, purpose) {
      let resultList = [];
      for (var i = 0; i < foreList.length; i++) {
        var stepTypes = foreList[i].recomStep;
        for (var j = 0; j < stepTypes.length; j++) {
          if (stepTypes[j] == purpose || stepTypes[j] == "General step") {
            resultList.push(foreList[i].tsId);
            break;
          }
        }
      }
      return resultList;
    },
    removeLink() {
      // // 防止Link与remove相互干扰
      // this.selectedPreActivities = [];
      // // remove
      // if (this.selectedActivities.length == 1) {
      //   this.delModal = true;
      // } else if (this.selectedActivities.length > 1) {
      //   this.$Notice.info({
      //     desc: "Steps should be deleted one by one! ",
      //   });
      // } else {
      //   this.$Notice.info({
      //     desc: "There is no step selected! ",
      //   });
      // }
    },
    delLinkContent(aid) {
      // // 删除step
      // this.axios
      //   .get("/GeoProblemSolving/step/delete" + "?aid=" + aid)
      //   .then((res) => {
      //     if (res.data === "Success") {
      //       this.$Notice.info({
      //         desc: "Remove successfully! ",
      //       });
      //     }
      //   })
      //   .catch((err) => {
      //     console.log(err.data);
      //   });
    },
    delLinkGraph() {
      // if (this.$store.getters.userInfo.userId == this.activityInfo.managerId) {
      //   let currentIndex = this.selectedActivities[0].id;
      //   if (this.processStructure[currentIndex].end) {
      //     let selectedStepId = this.processStructure[currentIndex].aid;
      //     // 删除step节点
      //     if (currentIndex > 0) {
      //       // 处理被删除节点的前驱节点
      //       for (
      //         var i = 0;
      //         i < this.processStructure[currentIndex].last.length;
      //         i++
      //       ) {
      //         let lastIndex = this.processStructure[currentIndex].last[i].id;
      //         if (this.processStructure[lastIndex].next.length === 1) {
      //           this.processStructure[lastIndex].next = [];
      //           this.processStructure[lastIndex].end = true;
      //         } else if (this.processStructure[lastIndex].next.length > 1) {
      //           for (
      //             let j = 0;
      //             j < this.processStructure[lastIndex].next.length;
      //             j++
      //           ) {
      //             if (
      //               this.processStructure[lastIndex].next[j].name ===
      //               this.selectedActivities[0].name
      //             ) {
      //               this.processStructure[lastIndex].next.splice(j, 1);
      //             }
      //           }
      //         }
      //       }
      //       // 删除节点
      //       this.processStructure.splice(currentIndex, 1);
      //       for (var i = currentIndex; i < this.processStructure.length; i++) {
      //         var originalID = this.processStructure[i].id;
      //         // 当前节点id
      //         if (originalID !== i) {
      //           this.processStructure[i].id = i;
      //         }
      //         var originalName = this.processStructure[i].name;
      //         // 前驱节点的 next id
      //         for (var j = 0; j < this.processStructure[i].last.length; j++) {
      //           var lastIndex = this.processStructure[i].last[j].id;
      //           var lastnode = this.processStructure[lastIndex];
      //           for (var k = 0; k < lastnode.next.length; k++) {
      //             if (lastnode.next[k].name === originalName) {
      //               this.processStructure[lastIndex].next[k].id = i;
      //             }
      //           }
      //         }
      //         // 后继节点的 last id
      //         for (var j = 0; j < this.processStructure[i].next.length; j++) {
      //           var nextIndex = this.processStructure[i].next[j].id - 1;
      //           var nextnode = this.processStructure[nextIndex];
      //           for (var k = 0; k < nextnode.last.length; k++) {
      //             if (nextnode.last[k].name === originalName) {
      //               this.processStructure[nextIndex].last[k].id = i;
      //             }
      //           }
      //         }
      //       }
      //     } else if (currentIndex === 0) {
      //       // 删除节点
      //       this.processStructure.splice(currentIndex, 1);
      //     }
      //     // 重新渲染
      //     this.updateStepchart();
      //     this.updateSteps();
      //     //删除数据库
      //     this.delLinkContent(selectedStepId);
      //   } else {
      //     this.$Notice.info({
      //       desc:
      //         "The selected step " +
      //         this.selectedActivities[0].name +
      //         " can not be removed. Because it has the next activities.",
      //     });
      //   }
      // }
    },
    updateSteps() {
      // let obj = new URLSearchParams();
      // var updateurl = "";
      // if (this.activityInfo.level == 0) {
      //   obj.append("aid", this.activityInfo.aid);
      //   updateurl = "/GeoProblemSolving/project/update";
      // } else if (this.activityInfo.level == 1) {
      //   obj.append("aid", this.activityInfo.aid);
      //   updateurl = "/GeoProblemSolving/subProject/update";
      // }
      this.axios;
      // .post(updateurl, obj)
      // .then((res) => {
      //   if (res.data == "Offline") {
      //     if (this.activityInfo.level == 1) {
      //       this.$store.commit("userLogout");
      //       this.$router.push({ name: "Login" });
      //     } else {
      //       parent.location.href = "/GeoProblemSolving/login";
      //     }
      //   } else if (res.data != "Fail") {
      //     if (this.activityInfo.level == 0) {
      //       //   parent.vm.projectInfo = res.data;
      //       this.$Notice.info({
      //         desc: "Project update successfully!",
      //       });
      //     } else if (this.activityInfo.level == 1) {
      //       this.$Notice.info({
      //         desc: "Subproject update successfully!",
      //       });
      //     }
      //   } else {
      //     this.$Message.error("Update subproject failed.");
      //   }
      // })
      // .catch((err) => {
      //   console.log(err.data);
      // });
    },
    resetSubProjectType() {
      // let obj = new URLSearchParams();
      // obj.append("aid", this.activityInfo.aid);
      // obj.append("purpose", "");
      // this.axios
      //   .post("/GeoProblemSolving/subProject/update", obj)
      //   .then((res) => {
      //     this.resetProjectTypeModel = false;
      //     if (res.data == "Offline") {
      //       parent.location.href = "/GeoProblemSolving/login";
      //     } else if (res.data != "Fail") {
      //       this.$emit("changeSubProjectInfo", res.data);
      //     } else {
      //       this.$Message.error("Set purpose failed.");
      //     }
      //   })
      //   .catch((err) => {
      //     console.log(err.data);
      //   });
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
