<style scoped>
/* import { try } from 'q'; */
.selector {
  width: 250px;
}
.leftMenuItem {
  margin: 0 0 10px 0;
}
.changeGreenColor:hover {
  background-color: #19be6b;
  color: white;
}
.changeRedColor:hover {
  background-color: #ed4014;
  color: white;
}
.ellipsis {
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  vertical-align: top;
}
.stepItems >>> .ivu-card-body {
  padding: 5px;
}
.api table {
  font-size: 12px;
  border-collapse: collapse;
  border-spacing: 0;
  empty-cells: show;
  border: 1px solid #e9e9e9;
  width: 100%;
}
.api table td,
.api table th {
  border: 1px solid #e9e9e9;
  padding: 8px 16px;
  text-align: left;
}
</style>
<template>
  <div>
    <Button
      shape="circle"
      icon="md-cog"
      @click="stepToolModalShow"
      size="small"
    ></Button>
    <Modal
      v-model="stepToolModal"
      title="Manage toolset and tools"
      width="800"
      :mask-closable="false"
      :styles="{ top: '15px' }"
    >
      <Row>
        <Col span="16">
          <div
            span="2"
            style="height: inherit; width: 90px; position: absolute"
          >
            <Menu
              :active-name="showMenuItem"
              @on-select="changeMenuItem"
              style="height: inherit; width: fit-content; z-index: 1"
            >
              <MenuItem name="allToolsets" class="leftMenuItem">
                <Tooltip content="All toolsets" placement="right">
                  <Icon type="ios-briefcase" size="25"></Icon>
                </Tooltip>
              </MenuItem>
              <MenuItem name="allTools" class="leftMenuItem">
                <Tooltip content="All tools" placement="right">
                  <Icon type="ios-hammer" size="25"></Icon>
                </Tooltip>
              </MenuItem>
              <MenuItem name="diyTools" class="leftMenuItem">
                <Tooltip content="Share your tools" placement="right">
                  <Icon type="md-add" size="25"></Icon>
                </Tooltip>
              </MenuItem>
            </Menu>
          </div>
          <Card dis-hover style="margin-left: 80px">
            <h3 slot="title" style="padding-top: 5px; color: #2d8cf099">
              {{ listTitle() }}
            </h3>
            <div slot="extra" style="width: 210px; margin-top: -5px">
              <Select
                v-model="typeSelected"
                @on-change="typeChanged"
                style="width: 160px"
              >
                <Option v-for="item in typeOptions" :key="item" :value="item">{{
                  item
                }}</Option>
              </Select>
              <i-switch v-model="isPublic">
                <Icon type="logo-dropbox" slot="open" title="Public"></Icon>
                <Icon type="ios-cube" slot="close" title="Personal"></Icon>
              </i-switch>
            </div>
            <div
              v-if="isPublic && showMenuItem == 'allToolsets'"
              style="height: 400px"
            >
              <vue-scroll :ops="ops" style="height: 400px">
                <Row>
                  <draggable
                    element="ul"
                    :options="{ group: 'toolset' }"
                    v-model="publicToolsetsShow"
                  >
                    <Col
                      span="8"
                      v-for="toolset in publicToolsetsShow"
                      :key="toolset.toolName"
                    >
                      <Card
                        style="
                          background-color: ghostwhite;
                          margin: 0 5px 10px 5px;
                          height: 110px;
                        "
                      >
                        <div style="text-align: center; overflow: hidden">
                          <Tooltip placement="bottom" max-width="600">
                            <img
                              :src="toolset.toolImg"
                              v-if="
                                toolset.toolImg != undefined &&
                                toolset.toolImg != ''
                              "
                              style="height: 100%; max-height: 50px"
                            />
                            <avatar
                              :username="toolset.toolName"
                              :size="50"
                              style="margin-bottom: 6px"
                              v-else
                            ></avatar>
                            <div slot="content">
                              <span>{{ toolset.description }}</span>
                              <template
                                v-if="
                                  toolset.tags != undefined &&
                                  toolset.tags.length > 0
                                "
                              >
                                <br />
                                <p>
                                  <i>{{ toolset.tags.join("|") }}</i>
                                </p>
                              </template>
                            </div>
                          </Tooltip>
                          <h4
                            :title="toolset.toolName"
                            style="
                              display: block;
                              width: 90px;
                              overflow: hidden;
                              white-space: nowrap;
                              text-overflow: ellipsis;
                            "
                          >
                            {{ toolset.toolName }}
                          </h4>
                        </div>
                      </Card>
                    </Col>
                  </draggable>
                </Row>
              </vue-scroll>
            </div>
            <div
              v-if="!isPublic && showMenuItem == 'allToolsets'"
              style="height: 400px"
            >
              <vue-scroll :ops="ops" style="height: 400px">
                <Row>
                  <draggable
                    element="ul"
                    :options="{ group: 'toolset' }"
                    v-model="personalToolsetsShow"
                  >
                    <Col
                      span="8"
                      v-for="toolset in personalToolsetsShow"
                      :key="toolset.toolName"
                    >
                      <Card
                        style="
                          background-color: #faebd794;
                          margin: 0 5px 10px 5px;
                          height: 110px;
                        "
                      >
                        <div style="text-align: center; overflow: hidden">
                          <Tooltip placement="bottom" max-width="600">
                            <img
                              :src="toolset.toolImg"
                              v-if="
                                toolset.toolImg != undefined &&
                                toolset.toolImg != ''
                              "
                              style="height: 100%; max-height: 50px"
                            />
                            <avatar
                              :username="toolset.toolName"
                              :size="50"
                              style="margin-bottom: 6px"
                              v-else
                            ></avatar>
                            <div slot="content">
                              <span>{{ toolset.description }}</span>
                              <template
                                v-if="
                                  toolset.tags != undefined &&
                                  toolset.tags.length > 0
                                "
                              >
                                <br />
                                <p>
                                  <i>{{ toolset.tags.join("|") }}</i>
                                </p>
                              </template>
                            </div>
                          </Tooltip>
                          <h4
                            :title="toolset.toolName"
                            style="width: 90px"
                            class="ellipsis"
                          >
                            {{ toolset.toolName }}
                          </h4>
                        </div>
                      </Card>
                    </Col>
                  </draggable>
                </Row>
              </vue-scroll>
            </div>
            <div
              v-if="isPublic && showMenuItem == 'allTools'"
              style="height: 400px"
            >
              <vue-scroll :ops="ops" style="height: 400px">
                <Row>
                  <draggable
                    element="ul"
                    :options="{ group: 'tool' }"
                    v-model="publicToolsShow"
                  >
                    <Col
                      span="8"
                      v-for="tool in publicToolsShow"
                      :key="tool.toolName"
                    >
                      <Card
                        style="
                          background-color: ghostwhite;
                          margin: 0 5px 10px 5px;
                          cursor: pointer;
                          height: 110px;
                        "
                      >
                        <div
                          style="text-align: center; overflow: hidden"
                          @click="showTool(tool)"
                        >
                          <Tooltip placement="bottom" max-width="600">
                            <img
                              :src="tool.toolImg"
                              v-if="
                                tool.toolImg != undefined && tool.toolImg != ''
                              "
                            />
                            <avatar
                              :username="tool.toolName"
                              :size="50"
                              style="margin-bottom: 6px"
                              v-else
                            ></avatar>
                            <div slot="content">
                              <span>{{ tool.description }}</span>
                              <template
                                v-if="
                                  tool.tags != undefined && tool.tags.length > 0
                                "
                              >
                                <br />
                                <span>
                                  <i>{{ tool.tags.join("|") }}</i>
                                </span></template
                              >
                            </div>
                          </Tooltip>
                          <h4
                            :title="tool.toolName"
                            style="
                              display: block;
                              width: 90px;
                              overflow: hidden;
                              white-space: nowrap;
                              text-overflow: ellipsis;
                            "
                          >
                            {{ tool.toolName }}
                          </h4>
                        </div>
                      </Card>
                    </Col>
                  </draggable>
                </Row>
              </vue-scroll>
            </div>
            <div
              v-if="!isPublic && showMenuItem == 'allTools'"
              style="height: 400px"
            >
              <vue-scroll :ops="ops" style="height: 400px">
                <Row>
                  <draggable
                    element="ul"
                    :options="{ group: 'tool' }"
                    v-model="personalToolsShow"
                  >
                    <Col
                      span="8"
                      v-for="tool in personalToolsShow"
                      :key="tool.toolName"
                    >
                      <Card
                        style="
                          background-color: #faebd794;
                          margin: 0 5px 10px 5px;
                          cursor: pointer;
                          height: 110px;
                        "
                      >
                        <div
                          style="text-align: center; overflow: hidden"
                          @click="showTool(tool)"
                        >
                          <Tooltip placement="bottom" max-width="600">
                            <img
                              :src="tool.toolImg"
                              v-if="
                                tool.toolImg != undefined && tool.toolImg != ''
                              "
                              style="height: 100%; max-height: 50px"
                            />
                            <avatar
                              :username="tool.toolName"
                              :size="50"
                              style="margin-bottom: 6px"
                              v-else
                            ></avatar>
                            <div slot="content">
                              <span>{{ tool.description }}</span>
                              <template
                                v-if="
                                  tool.tags != undefined && tool.tags.length > 0
                                "
                              >
                                <br />
                                <span>
                                  <i>{{ tool.tags.join("|") }}</i>
                                </span>
                              </template>
                            </div>
                          </Tooltip>
                          <h4
                            :title="tool.toolName"
                            style="width: 90px"
                            class="ellipsis"
                          >
                            {{ tool.toolName }}
                          </h4>
                        </div>
                      </Card>
                    </Col>
                  </draggable>
                </Row>
              </vue-scroll>
            </div>
          </Card>
        </Col>
        <Col span="8">
          <div style="padding: 0 5px; margin-left: 15px">
            <Card dis-hover>
              <h3
                slot="title"
                style="padding-top: 5px"
                v-if="showMenuItem == 'allToolsets'"
              >
                Toolsets in this activity
              </h3>
              <h3
                slot="title"
                style="padding-top: 5px"
                v-if="showMenuItem == 'allTools'"
              >
                Tools in this activity
              </h3>
              <div style="height: 400px" v-if="showMenuItem == 'allToolsets'">
                <vue-scroll :ops="ops" style="height: 400px">
                  <draggable
                    element="ul"
                    :group="{ name: 'toolset', put: true, pull: false }"
                    v-model="stepToolsetsShow"
                    @add="addToolsettoStep"
                    style="min-height: 400px"
                  >
                    <Card
                      v-for="(toolset, index) in stepToolsetsShow"
                      :key="toolset.toolName"
                      class="stepItems"
                      style="margin: 0 0 5px 0"
                    >
                      <div>
                        <Button
                          class="ellipsis"
                          type="text"
                          style="width: 140px; padding: 0"
                          @click="showInfo(toolset)"
                          >{{ toolset.toolName }}</Button
                        >
                        <Button
                          shape="circle"
                          icon="md-remove"
                          class="changeRedColor"
                          size="small"
                          style="float: right"
                          @click="removeToolset(index)"
                        ></Button>
                      </div>
                    </Card>
                  </draggable>
                </vue-scroll>
              </div>
              <div style="height: 400px" v-if="showMenuItem == 'allTools'">
                <vue-scroll :ops="ops" style="height: 400px">
                  <draggable
                    element="ul"
                    :group="{ name: 'tool', put: true, pull: false }"
                    v-model="stepToolsShow"
                    @add="addTooltoStep"
                    style="min-height: 400px"
                  >
                    <Card
                      v-for="(tool, index) in stepToolsShow"
                      :key="tool.toolName"
                      class="stepItems"
                      style="margin: 0 0 5px 0"
                    >
                      <div>
                        <Button
                          class="ellipsis"
                          type="text"
                          style="width: 140px; padding: 0"
                          @click="showInfo(tool)"
                          >{{ tool.toolName }}</Button
                        >
                        <Button
                          shape="circle"
                          icon="md-remove"
                          class="changeRedColor"
                          size="small"
                          style="float: right"
                          @click="removeTool(index)"
                        ></Button>
                      </div>
                    </Card>
                  </draggable>
                </vue-scroll>
              </div>
            </Card>
          </div>
        </Col>
      </Row>
      <div slot="footer">
        <Button @click="stepToolModal = false">Cancel</Button>
        <Button type="success" @click="confirmSetting">Save</Button>
      </div>
    </Modal>
    <Modal v-model="infoModal" title="Info of the toolset or tool" width="400">
      <div class="api">
        <table>
          <thead>
            <tr>
              <th>Title</th>
              <th>Info</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>Name</td>
              <td>{{ itemInfo.name }}</td>
            </tr>
            <tr>
              <td>Description</td>
              <td>{{ itemInfo.description }}</td>
            </tr>
            <tr v-if="itemInfo.tags != undefined">
              <td>Tags</td>
              <td>{{ itemInfo.tags.join(",") }}</td>
            </tr>
            <tr v-if="itemInfo.recommendation != undefined">
              <td>Step</td>
              <td>{{ itemInfo.recommendation.join(",") }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <div slot="footer">
        <Button type="info" @click="infoModal = false">OK</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
// import JPanel from 'JSPanel'
import Avatar from "vue-avatar";
import draggable from "vuedraggable";
import { get, del, post, put } from "../../axios";
export default {
  props: ["activityInfo", "toolList"],
  components: {
    draggable,
    Avatar,
  },
  data() {
    return {
      ops: {
        bar: {
          background: "#808695",
        },
      },
      toolIdList: [],
      stepToolsShow: [],
      stepToolsetsShow: [],
      userInfo: this.$store.getters.userInfo,
      showMenuItem: "allToolsets",
      publicToolsets: [],
      publicToolsetsShow: [],
      personalToolsets: [],
      personalToolsetsShow: [],
      publicTools: [],
      publicToolsShow: [],
      personalTools: [],
      personalToolsShow: [],
      stepToolModal: false,
      isPublic: true,
      typeSelected: "All",
      typeOptions: [
        "All",
        "General",
        "Context definition & resource collection",
        "Data processing",
        "Data visualization",
        "Geo-analysis model construction",
        "Model effectiveness evaluation",
        "Geographical simulation",
        "Data analysis",
        "Decision making",
      ],
      infoModal: false,
      itemInfo: {
        name: "",
        description: "",
        tags: [],
        recommendation: [],
      },
      panel: null,
    };
  },
  watch: {
    activityInfo: {
      handler(val) {
        this.init();
      },
      deep: true,
    },
    // stepToolModal(val) {
    //   if (!val) {
    //     this.panel.close();
    //   }
    // },
  },
  created() {},
  mounted() {
    Array.prototype.contains = function (obj) {
      var i = this.length;
      while (i--) {
        if (
          (this[i].tid != undefined && this[i].tid === obj) ||
          (this[i] != undefined && this[i] === obj)
        ) {
          return true;
        }
      }
      return false;
    };
  },
  methods: {
    stepToolModalShow() {
      this.toolIdList = this.operationApi.getToollist();
      this.getAllListInfo();
      this.typeSelected = "All";
      this.showMenuItem = "allToolsets";
      this.isPublic = true;
      this.stepToolModal = true;
    },
    async getAllListInfo() {
      await this.getPublicTools();
      await this.getPersonalTools();

      this.filterDuplicateTools();
      this.filterShowListByType();
    },
    async getPublicTools() {
      let data = await get(
        "/GeoProblemSolving/tool/inquiry?key=privacy&value=Public"
      );
      this.$set(this, "publicTools", data);
    },
    async getPersonalTools() {
      let data = await get(
        `/GeoProblemSolving/tool/findByProvider/${this.userInfo.userId}`
      );
      this.$set(this, "personalTools", data);
    },
    filterDuplicateTools() {
      this.stepToolsetsShow = [];
      this.stepToolsShow = [];

      // public tools and toolsets
      let tools = [],
        toolsets = [];
      for (var i = this.publicTools.length - 1; i >= 0; i--) {
        if (
          this.publicTools[i].isToolset != undefined &&
          this.publicTools[i].isToolset
        ) {
          if (!this.toolIdList.contains(this.publicTools[i].tid)) {
            toolsets.push(this.publicTools[i]);
          } else {
            this.stepToolsetsShow.push(this.publicTools[i]);
          }
        } else {
          if (!this.toolIdList.contains(this.publicTools[i].tid)) {
            tools.push(this.publicTools[i]);
          } else {
            this.stepToolsShow.push(this.publicTools[i]);
          }
        }
      }
      this.publicTools = tools;
      this.publicToolsets = toolsets;

      // personal tools and toolsets
      tools = [];
      toolsets = [];
      for (var i = this.personalTools.length - 1; i >= 0; i--) {
        if (
          this.personalTools[i].isToolset != undefined &&
          this.personalTools[i].isToolset
        ) {
          if (!this.toolIdList.contains(this.personalTools[i].tid)) {
            toolsets.push(this.personalTools[i]);
          } else {
            this.stepToolsetsShow.push(this.personalTools[i]);
          }
        } else {
          if (!this.toolIdList.contains(this.personalTools[i].tid)) {
            tools.push(this.personalTools[i]);
          } else {
            this.stepToolsShow.push(this.personalTools[i]);
          }
        }
      }
      this.personalTools = tools;
      this.personalToolsets = toolsets;
    },
    filterShowListByType() {
      this.publicToolsetsShow = this.getFilterResult(this.publicToolsets);
      this.personalToolsetsShow = this.getFilterResult(this.personalToolsets);
      this.publicToolsShow = this.getFilterResult(this.publicTools);
      this.personalToolsShow = this.getFilterResult(this.personalTools);
    },
    getFilterResult(foreList) {
      let selectedType = this.typeSelected;
      let resultList = foreList.filter(function (item) {
        switch (selectedType) {
          case "All": {
            return item;
          }
          case "General":
          case "Context definition & resource collection":
          case "Data processing":
          case "Data visualization":
          case "Geo-analysis model construction":
          case "Model effectiveness evaluation":
          case "Geographical simulation":
          case "Data analysis":
          case "Decision making": {
            if (item.recommendation != undefined) {
              let stepTypes = item.recommendation;
              for (var i = 0; i < stepTypes.length; i++) {
                if (stepTypes[i] == selectedType) {
                  return item;
                }
              }
            }
            break;
          }
        }
      });
      return resultList;
    },
    showInfo(item) {
      this.itemInfo.name = item.name;
      this.itemInfo.description = item.description;
      this.itemInfo.tags = item.tags;
      this.itemInfo.recommendation = item.recommendation;
      this.infoModal = true;
    },
    listTitle() {
      if (this.isPublic && this.showMenuItem == "allToolsets") {
        return "Public toolsets";
      } else if (this.isPublic && this.showMenuItem == "allTools") {
        return "Public tools";
      } else if (!this.isPublic && this.showMenuItem == "allToolsets") {
        return "Personal toolsets";
      } else if (!this.isPublic && this.showMenuItem == "allTools") {
        return "Personal tools";
      }
    },
    typeChanged(type) {
      this.typeSelected = type;
      this.filterShowListByType();
    },
    addToolsettoStep(evt) {
      var addedToolsetId = this.stepToolsetsShow[evt.newDraggableIndex].tsId;
      for (var i = 0; i < this.publicToolsets.length; i++) {
        if (this.publicToolsets[i].tid == addedToolsetId) {
          this.publicToolsets.splice(i, 1);
          break;
        }
      }
      for (var i = 0; i < this.personalToolsets.length; i++) {
        if (this.personalToolsets[i].tid == addedToolsetId) {
          this.personalToolsets.splice(i, 1);
          break;
        }
      }
      this.filterShowListByType();
    },
    removeToolset(index) {
      var removeToolsetInfo = this.stepToolsetsShow[index];
      this.stepToolsetsShow.splice(index, 1);
      if (removeToolsetInfo.privacy == "Public") {
        this.publicToolsets.push(removeToolsetInfo);
      }
      if (removeToolsetInfo.provider == this.userInfo.userId) {
        this.personalToolsets.push(removeToolsetInfo);
      }
      this.filterShowListByType();
    },
    addTooltoStep(evt) {
      var addedToolId = this.stepToolsShow[evt.newDraggableIndex].tid;
      for (var i = 0; i < this.publicTools.length; i++) {
        if (this.publicTools[i].tid == addedToolId) {
          this.publicTools.splice(i, 1);
          break;
        }
      }
      for (var i = 0; i < this.personalTools.length; i++) {
        if (this.personalTools[i].tid == addedToolId) {
          this.personalTools.splice(i, 1);
          break;
        }
      }
      this.filterShowListByType();
    },
    removeTool(index) {
      var removeToolInfo = this.stepToolsShow[index];
      this.stepToolsShow.splice(index, 1);
      if (removeToolInfo.privacy == "Public") {
        this.publicTools.push(removeToolInfo);
      }
      if (removeToolInfo.provider == this.userInfo.userId) {
        this.personalTools.push(removeToolInfo);
      }
      this.filterShowListByType();
    },
    confirmSetting() {
      let newStepToolsets = [];
      let newStepTools = [];
      this.stepToolsetsShow.forEach((toolset) => {
        newStepToolsets.push(toolset.tid);
      });
      this.stepToolsShow.forEach((tool) => {
        newStepTools.push(tool.tid);
      });

      // 更新协同文档
      // add
      for (var i = 0; i < this.stepToolsetsShow.length; i++) {
        if (!this.toolIdList.contains(this.stepToolsetsShow[i].tid)) {

          this.operationApi.toolOperationRecord(
            this.activityInfo.aid,
            "",
            "",
            "add",
            this.userInfo.userId,
            this.stepToolsetsShow[i]
          );
        }
      }
      for (var i = 0; i < this.stepToolsShow.length; i++) {
        if (!this.toolIdList.contains(this.stepToolsShow[i].tid)) {

          this.operationApi.toolOperationRecord(
            this.activityInfo.aid,
            "",
            "",
            "add",
            this.userInfo.userId,
            this.stepToolsShow[i]
          );
        }
      }
      // remove
      for (var i = 0; i < this.toolIdList.length; i++) {
        if (!this.stepToolsetsShow.contains(this.toolIdList[i]) && !this.stepToolsShow.contains(this.toolIdList[i])) {

          this.operationApi.toolOperationRecord(
            this.activityInfo.aid,
            "",
            "remove",
            this.userInfo.userId,
            { tid: this.toolIdList[i] }
          );
        }
      }

      //此处要更新父组件的列表
      this.$emit("updateStepTools", newStepTools, newStepToolsets);
      this.stepToolModal = false;
    },
    changeMenuItem(name) {
      if (name == "diyTools") {
        this.stepToolModal = false;
        parent.location.href = "/GeoProblemSolving/newPersonalPage/tool";
      }
      this.showMenuItem = name;
    },
    showTool(toolInfo) {},
  },
};
</script>
