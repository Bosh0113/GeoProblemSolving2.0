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
    <Button shape="circle" icon="md-cog" @click="stepToolModalShow"></Button>
    <Modal
      v-model="stepToolModal"
      title="Manage toolset and tools"
      width="800"
      :mask-closable="false"
      :styles="{top: '15px'}"
    >
      <Row>
        <Col span="16">
          <div span="2" style="height: inherit;width: 90px;position: absolute;">
            <Menu
              :active-name="showMenuItem"
              @on-select="changeMenuItem"
              style="height: inherit;width: fit-content;z-index:1"
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
            <h2 slot="title" style="padding-top:5px;color: #2d8cf099">{{listTitle()}}</h2>
            <div slot="extra" style="width:210px;">
              <Select v-model="typeSelected" @on-change="typeChanged" style="width:160px">
                <Option v-for="item in typeOptions" :key="item.index" :value="item">{{ item }}</Option>
              </Select>
              <i-switch v-model="isPublic">
                <Icon type="logo-dropbox" slot="open" title="Public"></Icon>
                <Icon type="ios-cube" slot="close" title="Personal"></Icon>
              </i-switch>
            </div>
            <div v-if="isPublic&&showMenuItem=='allToolsets'" style="height: 400px;">
              <vue-scroll :ops="ops" style="height:400px;">
                <Row>
                  <draggable element="ul" :options="{group:'toolset'}" v-model="publicToolsetsShow">
                    <Col span="8" v-for="toolset in publicToolsetsShow" :key="toolset.index">
                      <Card style="background-color: ghostwhite;margin: 0 5px 10px 5px">
                        <div style="text-align:center">
                          <Tooltip placement="bottom" max-width="600">
                            <img
                              :src="toolset.toolsetImg"
                              v-if="toolset.toolsetImg!=''"
                              style="height:100%;max-height:50px;"
                            />
                            <avatar
                              :username="toolset.toolsetName"
                              :size="50"
                              style="margin-bottom:6px"
                              v-else
                            ></avatar>
                            <div slot="content">
                              <span>{{toolset.description}}</span>
                              <br v-if="toolset.categoryTag.length>0" />
                              <p>
                                <i>{{toolset.categoryTag.join('|')}}</i>
                              </p>
                            </div>
                          </Tooltip>
                          <h4
                            :title="toolset.toolsetName"
                            style="display:block;width:90px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;"
                          >{{toolset.toolsetName}}</h4>
                        </div>
                      </Card>
                    </Col>
                  </draggable>
                </Row>
              </vue-scroll>
            </div>
            <div v-if="!isPublic&&showMenuItem=='allToolsets'" style="height: 400px;">
              <vue-scroll :ops="ops" style="height:400px;">
                <Row>
                  <draggable
                    element="ul"
                    :options="{group:'toolset'}"
                    v-model="personalToolsetsShow"
                  >
                    <Col span="8" v-for="toolset in personalToolsetsShow" :key="toolset.index">
                      <Card style="background-color: #faebd794;margin: 0 5px 10px 5px">
                        <div style="text-align:center">
                          <Tooltip placement="bottom" max-width="600">
                            <img
                              :src="toolset.toolsetImg"
                              v-if="toolset.toolsetImg!=''"
                              style="height:100%;max-height:50px;"
                            />
                            <avatar
                              :username="toolset.toolsetName"
                              :size="50"
                              style="margin-bottom:6px"
                              v-else
                            ></avatar>
                            <div slot="content">
                              <span>{{toolset.description}}</span>
                              <br v-if="toolset.categoryTag.length>0" />
                              <p>
                                <i>{{toolset.categoryTag.join('|')}}</i>
                              </p>
                            </div>
                          </Tooltip>
                          <h4
                            :title="toolset.toolsetName"
                            style="width:90px;"
                            class="ellipsis"
                          >{{toolset.toolsetName}}</h4>
                        </div>
                      </Card>
                    </Col>
                  </draggable>
                </Row>
              </vue-scroll>
            </div>
            <div v-if="isPublic&&showMenuItem=='allTools'" style="height: 400px;">
              <vue-scroll :ops="ops" style="height:400px;">
                <Row>
                  <draggable element="ul" :options="{group:'tool'}" v-model="publicToolsShow">
                    <Col span="8" v-for="tool in publicToolsShow" :key="tool.index">
                      <Card
                        style="background-color: ghostwhite;margin: 0 5px 10px 5px;cursor: pointer;"
                      >
                        <div style="text-align:center" @click="showTool(tool)">
                          <Tooltip placement="bottom" max-width="600">
                            <img
                              :src="tool.toolImg"
                              v-if="tool.toolImg!=''"
                              style="height:100%;max-height:50px;"
                            />
                            <avatar
                              :username="tool.toolName"
                              :size="50"
                              style="margin-bottom:6px"
                              v-else
                            ></avatar>
                            <div slot="content">
                              <span>{{tool.description}}</span>
                              <br v-if="tool.categoryTag.length>0" />
                              <span>
                                <i>{{tool.categoryTag.join('|')}}</i>
                              </span>
                            </div>
                          </Tooltip>
                          <h4
                            :title="tool.toolName"
                            style="display:block;width:90px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;"
                          >{{tool.toolName}}</h4>
                        </div>
                      </Card>
                    </Col>
                  </draggable>
                </Row>
              </vue-scroll>
            </div>
            <div v-if="!isPublic&&showMenuItem=='allTools'" style="height: 400px;">
              <vue-scroll :ops="ops" style="height:400px;">
                <Row>
                  <draggable element="ul" :options="{group:'tool'}" v-model="personalToolsShow">
                    <Col span="8" v-for="tool in personalToolsShow" :key="tool.index">
                      <Card
                        style="background-color: #faebd794;margin: 0 5px 10px 5px;cursor: pointer;"
                      >
                        <div style="text-align:center" @click="showTool(tool)">
                          <Tooltip placement="bottom" max-width="600">
                            <img
                              :src="tool.toolImg"
                              v-if="tool.toolImg!=''"
                              style="height:100%;max-height:50px;"
                            />
                            <avatar
                              :username="tool.toolName"
                              :size="50"
                              style="margin-bottom:6px"
                              v-else
                            ></avatar>
                            <div slot="content">
                              <span>{{tool.description}}</span>
                              <br v-if="tool.categoryTag.length>0" />
                              <span>
                                <i>{{tool.categoryTag.join('|')}}</i>
                              </span>
                            </div>
                          </Tooltip>
                          <h4
                            :title="tool.toolName"
                            style="width:90px;"
                            class="ellipsis"
                          >{{tool.toolName}}</h4>
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
          <div style="padding: 0 5px;margin-left: 15px;">
            <Card dis-hover>
              <h2
                slot="title"
                style="padding-top:5px"
                v-if="showMenuItem=='allToolsets'"
              >Toolsets in step</h2>
              <h2 slot="title" style="padding-top:5px" v-if="showMenuItem=='allTools'">Tools in step</h2>
              <div style="height: 400px;" v-if="showMenuItem=='allToolsets'">
                <vue-scroll :ops="ops" style="height:400px;">
                  <draggable
                    element="ul"
                    :group="{name:'toolset', put:true, pull:false}"
                    v-model="stepToolsetsShow"
                    @add="addToolsettoStep"
                    style="min-height:400px"
                  >
                    <Card
                      v-for="(toolset,index) in stepToolsetsShow"
                      :key="toolset.index"
                      class="stepItems"
                      style="margin:0 0 5px 0"
                    >
                      <div>
                        <Button
                          class="ellipsis"
                          type="text"
                          style="width: 140px;padding:0"
                          @click="showInfo(toolset,toolset.toolsetName)"
                        >{{toolset.toolsetName}}</Button>
                        <Button
                          shape="circle"
                          icon="md-remove"
                          class="changeRedColor"
                          size="small"
                          style="float:right"
                          @click="removeToolset(index)"
                        ></Button>
                      </div>
                    </Card>
                  </draggable>
                </vue-scroll>
              </div>
              <div style="height: 400px;" v-if="showMenuItem=='allTools'">
                <vue-scroll :ops="ops" style="height:400px;">
                  <draggable
                    element="ul"
                    :group="{name:'tool', put:true, pull:false}"
                    v-model="stepToolsShow"
                    @add="addTooltoStep"
                    style="min-height:400px"
                  >
                    <Card
                      v-for="(tool,index) in stepToolsShow"
                      :key="tool.index"
                      class="stepItems"
                      style="margin:0 0 5px 0"
                    >
                      <div>
                        <Button
                          class="ellipsis"
                          type="text"
                          style="width: 140px;padding:0"
                          @click="showInfo(tool,tool.toolName)"
                        >{{tool.toolName}}</Button>
                        <Button
                          shape="circle"
                          icon="md-remove"
                          class="changeRedColor"
                          size="small"
                          style="float:right"
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
        <Button @click="stepToolModal=false">Cancel</Button>
        <Button type="success" @click="confirmSetting()">Save</Button>
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
              <td>{{itemInfo.name}}</td>
            </tr>
            <tr>
              <td>Description</td>
              <td>{{itemInfo.description}}</td>
            </tr>
            <tr>
              <td>Tags</td>
              <td>{{itemInfo.tags.join(',')}}</td>
            </tr>
            <tr>
              <td>Step</td>
              <td>{{itemInfo.recomStep.join(',')}}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <div slot="footer">
        <Button type="info" @click="infoModal=false">OK</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
// import JPanel from 'JSPanel'
import Avatar from "vue-avatar";
import draggable from "vuedraggable";
export default {
  props: ["stepInfo"],
  components: {
    draggable,
    Avatar
  },
  watch: {
    stepInfo(val) {
      this.init();
    },
    stepToolModal(val) {
      if (!val) {
        this.panel.close();
      }
    }
  },
  created() {
    this.init();
  },
  mounted() {
    this.resizeContent();
  },
  data() {
    return {
      stepId: "",
      stepToolsetIds: [],
      stepToolIds: [],
      stepToolsShow: [],
      stepToolsetsShow: [],
      contentHeight: "",
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
      ops: {
        bar: {
          background: "#808695"
        }
      },
      typeSelected: "All",
      typeOptions: [
        "All",
        "General step",
        "Context definition & resource collection",
        "Data processing",
        "Data analysis",
        "Data visualization",
        "Geo-analysis model construction",
        "Model effectiveness evaluation",
        "Geographical simulation",
        "Decision making",
        "Others"
      ],
      infoModal: false,
      itemInfo: {
        name: "",
        description: "",
        tags: [],
        recomStep: []
      },
      panel: null
    };
  },
  methods: {
    resizeContent() {
      if (window.innerHeight > 675) {
        this.contentHeight = window.innerHeight - 120;
      } else {
        this.contentHeight = 555;
      }
      window.onresize = () => {
        return (() => {
          this.resizeContent();
        })();
      };
    },
    init() {
      this.stepId = this.stepInfo.stepId;
      this.stepToolsetIds = this.stepInfo.toolsetList;
      this.stepToolIds = this.stepInfo.toolList;
    },
    getAllListInfo() {
      this.getPublicToolsets();
      this.getPersonalToolsets();
      this.getPublicTools();
      this.getPersonalTools();
      this.getStepToolsets();
      this.getStepTools();
    },
    stepToolModalShow() {
      this.getAllListInfo();
      this.typeSelected = "All";
      this.showMenuItem = "allToolsets";
      this.isPublic = true;
      this.stepToolModal = true;
    },
    showInfo(item, name) {
      this.itemInfo.name = name;
      this.itemInfo.description = item.description;
      this.itemInfo.tags = item.categoryTag;
      this.itemInfo.recomStep = item.recomStep;
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
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data === "Fail") {
            this.$Notice.error({ desc: "Loading toolsets fail." });
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
            this.userInfo.userId
        )
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data === "Fail") {
            this.$Notice.error({ desc: "Loading toolsets fail." });
          } else if (res.data === "None") {
            // this.$Notice.error({ desc: "There is no existing toolset" });
          } else {
            this.$set(this, "personalToolsets", res.data);
            this.filterDuplicateToolsets();
            this.filterShowListByType();
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    getStepToolsets() {
      if (this.stepToolsetIds == null || this.stepToolsetIds == undefined) {
        this.stepToolsetIds = [];
      }

      var stepToolsetIds = this.stepToolsetIds;
      var toolsetsCount = this.stepToolsetIds.length;
      var flagCount = toolsetsCount;
      var stepToolsetInfos = [];
      for (var i = 0; i < toolsetsCount; i++) {
        this.axios
          .get(
            "/GeoProblemSolving/toolset/inquiry" +
              "?key=" +
              "tsId" +
              "&value=" +
              stepToolsetIds[i]
          )
          .then(res => {
            if (res.data == "Offline") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else if (res.data === "Fail") {
              this.$Notice.error({ desc: "Loading toolsets fail." });
            } else if (res.data === "None") {
              // this.$Notice.error({ desc: "There is no existing toolset" });
            } else {
              stepToolsetInfos.push(res.data[0]);
              if (--flagCount < 1) {
                var sortToolsets = [];
                for (var j = 0; j < toolsetsCount; j++) {
                  for (var k = 0; k < toolsetsCount; k++) {
                    if (stepToolsetIds[j] == stepToolsetInfos[k].tsId) {
                      sortToolsets.push(stepToolsetInfos[k]);
                      break;
                    }
                  }
                }
                this.$set(this, "stepToolsetsShow", sortToolsets);
                this.filterDuplicateToolsets();
                this.filterShowListByType();
              }
            }
          })
          .catch(err => {
            console.log(err);
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
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data === "Fail") {
            this.$Notice.error({ desc: "Loading tools fail." });
          } else if (res.data === "None") {
            // this.$Notice.error({ desc: "There is no existing tool" });
          } else {
            this.$set(this, "publicTools", res.data);
            this.filterDuplicateTools();
            this.filterShowListByType();
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
            this.userInfo.userId
        )
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data === "Fail") {
            this.$Notice.error({ desc: "Loading tool fail." });
          } else if (res.data === "None") {
            // this.$Notice.error({ desc: "There is no existing tool" });
          } else {
            this.$set(this, "personalTools", res.data);
            this.filterDuplicateTools();
            this.filterShowListByType();
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    getStepTools() {
      if (this.stepToolIds == null || this.stepToolIds == undefined) {
        this.stepToolIds = [];
      }

      var stepToolIds = this.stepToolIds;
      var toolsCount = this.stepToolIds.length;
      var flagCount = toolsCount;
      var stepToolInfos = [];
      for (var i = 0; i < toolsCount; i++) {
        this.axios
          .get(
            "/GeoProblemSolving/tool/inquiry" +
              "?key=" +
              "tId" +
              "&value=" +
              stepToolIds[i]
          )
          .then(res => {
            if (res.data == "Offline") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else if (res.data === "Fail") {
              this.$Notice.error({ desc: "Loading tools fail." });
            } else if (res.data === "None") {
              // this.$Notice.error({ desc: "There is no existing tool" });
            } else {
              stepToolInfos.push(res.data[0]);
              if (--flagCount < 1) {
                var sortTools = [];
                for (var j = 0; j < toolsCount; j++) {
                  for (var k = 0; k < toolsCount; k++) {
                    if (stepToolIds[j] == stepToolInfos[k].tId) {
                      sortTools.push(stepToolInfos[k]);
                      break;
                    }
                  }
                }
                this.$set(this, "stepToolsShow", sortTools);
                this.filterDuplicateTools();
                this.filterShowListByType();
              }
            }
          })
          .catch(err => {
            console.log(err);
          });
      }
    },
    changeMenuItem(name) {
      if(name=="diyTools"){
        this.stepToolModal = false;
        top.location.href="/GeoProblemSolving/toolsCenter";
      }
      this.showMenuItem = name;
    },
    typeChanged(type) {
      this.typeSelected = type;
      this.filterShowListByType();
    },
    filterDuplicateToolsets() {
      var tempPublicToolsets = [];
      for (var i = 0; i < this.publicToolsets.length; i++) {
        var exist = false;
        for (var j = 0; j < this.stepToolsetsShow.length; j++) {
          if (this.publicToolsets[i].tsId == this.stepToolsetsShow[j].tsId) {
            exist = true;
            break;
          }
        }
        if (!exist) {
          tempPublicToolsets.push(this.publicToolsets[i]);
        }
      }
      this.publicToolsets = tempPublicToolsets;
      var tempPersonalToolsets = [];
      for (var i = 0; i < this.personalToolsets.length; i++) {
        var exist = false;
        for (var j = 0; j < this.stepToolsetsShow.length; j++) {
          if (this.personalToolsets[i].tsId == this.stepToolsetsShow[j].tsId) {
            exist = true;
            continue;
          }
        }
        if (!exist) {
          tempPersonalToolsets.push(this.personalToolsets[i]);
        }
      }
      this.personalToolsets = tempPersonalToolsets;
    },
    filterDuplicateTools() {
      var tempPublicTools = [];
      for (var i = 0; i < this.publicTools.length; i++) {
        var exist = false;
        for (var j = 0; j < this.stepToolsShow.length; j++) {
          if (this.publicTools[i].tId == this.stepToolsShow[j].tId) {
            exist = true;
            break;
          }
        }
        if (!exist) {
          tempPublicTools.push(this.publicTools[i]);
        }
      }
      this.publicTools = tempPublicTools;
      var tempPersonalTools = [];
      for (var i = 0; i < this.personalTools.length; i++) {
        var exist = false;
        for (var j = 0; j < this.stepToolsShow.length; j++) {
          if (this.personalTools[i].tId == this.stepToolsShow[j].tId) {
            exist = true;
            continue;
          }
        }
        if (!exist) {
          tempPersonalTools.push(this.personalTools[i]);
        }
      }
      this.personalTools = tempPersonalTools;
    },
    filterShowListByType() {
      this.publicToolsetsShow = this.getFilterResult(this.publicToolsets);
      this.personalToolsetsShow = this.getFilterResult(this.personalToolsets);
      this.publicToolsShow = this.getFilterResult(this.publicTools);
      this.personalToolsShow = this.getFilterResult(this.personalTools);
    },
    getFilterResult(foreList) {
      var selectedType = this.typeSelected;
      var resultList = foreList.filter(function(item) {
        switch (selectedType) {
          case "All": {
            return item;
            break;
          }
          case "General step":
          case "Context definition & resource collection":
          case "Data processing":
          case "Data analysis":
          case "Data visualization":
          case "Geo-analysis model construction":
          case "Model effectiveness evaluation":
          case "Geographical simulation":
          case "Decision making": {
            var stepTypes = item.recomStep;
            for (var i = 0; i < stepTypes.length; i++) {
              if (stepTypes[i] == selectedType) {
                return item;
                break;
              }
            }
            break;
          }
          case "Others": {
            if (item.recomStep.length < 1) {
              return item;
            }
            break;
          }
        }
      });
      return resultList;
    },
    addToolsettoStep(evt) {
      var addedToolsetId = this.stepToolsetsShow[evt.newDraggableIndex].tsId;
      for (var i = 0; i < this.publicToolsets.length; i++) {
        if (this.publicToolsets[i].tsId == addedToolsetId) {
          this.publicToolsets.splice(i, 1);
          break;
        }
      }
      for (var i = 0; i < this.personalToolsets.length; i++) {
        if (this.personalToolsets[i].tsId == addedToolsetId) {
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
      var addedToolId = this.stepToolsShow[evt.newDraggableIndex].tId;
      for (var i = 0; i < this.publicTools.length; i++) {
        if (this.publicTools[i].tId == addedToolId) {
          this.publicTools.splice(i, 1);
          break;
        }
      }
      for (var i = 0; i < this.personalTools.length; i++) {
        if (this.personalTools[i].tId == addedToolId) {
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
      var newStepToolsets = [];
      var newStepTools = [];
      this.stepToolsetsShow.forEach(toolset => {
        newStepToolsets.push(toolset.tsId);
      });
      this.stepToolsShow.forEach(tool => {
        newStepTools.push(tool.tId);
      });
      let obj = new URLSearchParams();
      obj.append("stepId", this.stepInfo.stepId);
      obj.append("toolsetList", newStepToolsets);
      obj.append("toolList", newStepTools);
      this.axios
        .post("/GeoProblemSolving/step/update", obj)
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data === "Fail") {
            this.$Notice.error({ desc: "Loading tool fail." });
          } else if (res.data === "None") {
            // this.$Notice.error({ desc: "There is no existing tool" });
          } else {
            //此处要更新父组件的列表
            this.$emit("updateStepTools", newStepTools, newStepToolsets);
            this.stepToolModal = false;
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    showTool(toolInfo) {
      // this.axios
      //   .post("/GeoProblemSolving/user/state")
      //   .then(res => {
      //     if (!res.data) {
      //       this.$store.commit("userLogout");
      //       this.$router.push({ name: "Login" });
      //     } else {
      //       let toolURL =
      //         '<iframe src="' +
      //         toolInfo.toolUrl +
      //         "?userName=" +
      //         this.userInfo.userName +
      //         "&userID=" +
      //         this.userInfo.userId +
      //         "&groupID=" +
      //         "e2b02056-82c9-4dda-8916-17fb66a21634" +
      //         '" style="width: 100%;height:100%;"></iframe>';
      //       var demoPanelTimer = null;

      //       if (this.panel != null) {
      //         this.panel.close();
      //       }
      //       this.panel = jsPanel.create({
      //         theme: "success",
      //         headerTitle: toolInfo.toolName,
      //         footerToolbar: '<p style="height:10px"></p>',
      //         contentSize: "1200 600",
      //         content: toolURL,
      //         disableOnMaximized: true,
      //         dragit: {
      //           containment: 5
      //         },
      //         id: "demoPanel",
      //         onclosed: function(panel, status, closedByUser) {
      //           window.clearTimeout(demoPanelTimer);
      //         },
      //         callback: function() {
      //           var that = this;
      //           demoPanelTimer = window.setInterval(function() {
      //             that.style.zIndex = "9999";
      //           }, 1);
      //         }
      //       });
      //       // panel.resizeit("disable");
      //       $(".jsPanel-content").css("font-size", "0");
      //     }
      //   })
      //   .catch(err => {
      //     console.log("Get user info fail.");
      //   });
    }
  }
};
</script>
