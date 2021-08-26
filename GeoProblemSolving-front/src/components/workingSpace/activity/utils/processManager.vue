<style scoped>
.link-protocol >>> .ivu-modal-body {
  padding: 0 16px 16px 16px;
}
.btnHoverGray:hover {
  background-color: #808695;
  color: white;
}
#steps {
  margin-top: 20px;
  padding: 15px;
  background-color: #f8f8f9;
  height: calc(100vh - 300px);
  width: calc(100vw - 400px);
}
.domain >>> .ti-input {
  display: inline-block;
  width: 655px;
  height: 32px;
  line-height: 1.5;
  padding: 4px 7px;
  font-size: 12px;
  border: 1px solid #dcdee2;
  border-radius: 4px;
  color: #515a6e;
  background-color: #fff;
  background-image: none;
  position: relative;
  cursor: text;
}
.domain >>> input {
  opacity: 0.5;
}
.res-protocol >>> .ti-input {
  display: inline-block;
  width: 267px;
  height: 32px;
  line-height: 1.5;
  padding: 4px 7px;
  font-size: 12px;
  border: 1px solid #dcdee2;
  border-radius: 4px;
  color: #515a6e;
  background-color: #fff;
  background-image: none;
  position: relative;
  cursor: text;
}
.res-protocol >>> input {
  opacity: 0.5;
}
</style>
<template>
  <Row>
    <Col span="24" style="margin-top: 20px">
      <div>
        <Row type="flex" justify="space-around">
          <div
            style="
              width: 80%;
              height: 25px;
              text-align: center;
              margin-left: 10%;
            "
          >
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
              <template v-if="childActivities.length > 1">
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
              <template v-else>
                <Button
                  type="default"
                  size="small"
                  icon="md-link"
                  title="Start to link"
                  style="float: right; margin-left: 10px; cursor: default"
                  id="linkBegin"
                  >Start to link</Button
                >
              </template>
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
    <Modal
      class="link-protocol"
      v-model="linkBuildModal"
      title="Set link protocol"
      width="800"
      :styles="{ top: '30px' }"
      @on-ok="buildLink"
      @on-cancel="cancelLink"
      ok-text="Link"
      cancel-text="Cancel"
    >
      <Divider orientation="left">Acitivity link</Divider>
      <div v-if="selectedActivities.length >= 2" style="margin: 0 20px">
        <div style="margin-bottom: 15px">
          <label>Type of activity relations: </label>
          <RadioGroup v-model="protocalType" @on-change="changeProtocalType">
            <Radio label="Sequence" style="margin-left: 20px"></Radio>
            <Radio label="Branch" style="margin-left: 20px"></Radio>
            <Radio label="Merger" style="margin-left: 20px"></Radio>
            <Radio label="Loop" style="margin-left: 20px"></Radio>
          </RadioGroup>
        </div>
        <!-- sequence -->
        <div v-if="protocalType == 'Sequence'">
          <template v-for="index in selectedActivities.length - 1">
            <div :key="index" style="margin-bottom: 5px">
              <span style="margin-right: 20px">
                <label>Start activity: </label>
                <Select
                  v-if="index == 1"
                  v-model="activityLinks[index - 1]"
                  style="width: 200px"
                  placeholder="Select activity"
                  @on-change="activitySelect(index - 1)"
                  clearable
                  @on-clear="activitySelectClear(index - 1)"
                >
                  <template v-for="item in otherNodes"
                    ><Option
                      :value="item.aid"
                      :key="item.id"
                      :class="item.aid"
                      :disabled="item.selected != undefined && item.selected"
                      >{{ item.name }}</Option
                    >
                  </template>
                </Select>
                <Select
                  v-else
                  v-model="activityLinks[index - 1]"
                  style="width: 200px"
                  placeholder="Select activity"
                  disabled
                >
                  <template v-for="item in otherNodes"
                    ><Option
                      :value="item.aid"
                      :key="item.id"
                      :class="item.aid"
                      >{{ item.name }}</Option
                    >
                  </template>
                </Select>
              </span>
              <Icon type="md-arrow-forward" style="margin-right: 20px" />
              <span>
                <label>End activity: </label>
                <Select
                  v-model="activityLinks[index]"
                  style="width: 200px"
                  placeholder="Select activity"
                  @on-change="activitySelect(index)"
                  clearable
                  @on-clear="activitySelectClear(index)"
                >
                  <template v-for="item in otherNodes">
                    <Option
                      :value="item.aid"
                      :key="item.id"
                      :class="item.aid"
                      :disabled="item.selected != undefined && item.selected"
                      >{{ item.name }}</Option
                    >
                  </template>
                </Select>
              </span>
              <Icon type="md-arrow-forward" style="margin: 0 20px" />
            </div>
          </template>
        </div>
        <!-- branch -->
        <div v-else-if="protocalType == 'Branch'" style="display: flex">
          <div style="width: 275px">
            <label>Start activity: </label>
            <Select
              v-model="activityLinks[0]"
              style="width: 200px"
              placeholder="Select activity"
              @on-change="activitySelect(0)"
              clearable
              @on-clear="activitySelectClear(0)"
            >
              <template v-for="item in otherNodes"
                ><Option
                  :value="item.aid"
                  :key="item.id"
                  :class="item.aid"
                  :disabled="item.selected != undefined && item.selected"
                  >{{ item.name }}</Option
                ></template
              >
            </Select>
          </div>
          <div style="width: 360px">
            <template v-for="index in selectedActivities.length - 1">
              <div :key="index" style="margin-bottom: 5px">
                <Icon type="md-arrow-forward" style="margin: 0 20px" />
                <label>End activity: </label>
                <Select
                  v-model="activityLinks[index]"
                  style="width: 200px"
                  placeholder="Select activity"
                  @on-change="activitySelect(index)"
                  clearable
                  @on-clear="activitySelectClear(index)"
                >
                  <template v-for="item in otherNodes">
                    <Option
                      :value="item.aid"
                      :key="item.id"
                      :class="item.aid"
                      :disabled="item.selected != undefined && item.selected"
                      >{{ item.name }}</Option
                    >
                  </template>
                </Select>
              </div>
            </template>
          </div>
        </div>
        <!-- merger -->
        <div v-else-if="protocalType == 'Merger'" style="display: flex">
          <div style="width: 335px">
            <template v-for="index in selectedActivities.length - 1">
              <div :key="index" style="margin-bottom: 5px">
                <label>Start activity: </label>
                <Select
                  v-model="activityLinks[index]"
                  style="width: 200px"
                  placeholder="Select activity"
                  @on-change="activitySelect(index)"
                  clearable
                  @on-clear="activitySelectClear(index)"
                >
                  <template v-for="item in otherNodes">
                    <Option
                      :value="item.aid"
                      :key="item.id"
                      :class="item.aid"
                      :disabled="item.selected != undefined && item.selected"
                      >{{ item.name }}</Option
                    >
                  </template>
                </Select>
                <Icon type="md-arrow-forward" style="margin: 0 20px" />
              </div>
            </template>
          </div>
          <div style="width: 360px">
            <label>End activity: </label>
            <Select
              v-model="activityLinks[0]"
              style="width: 200px"
              placeholder="Select activity"
              @on-change="activitySelect(0)"
              clearable
              @on-clear="activitySelectClear(0)"
            >
              <template v-for="item in otherNodes"
                ><Option
                  :value="item.aid"
                  :key="item.id"
                  :class="item.aid"
                  :disabled="item.selected != undefined && item.selected"
                  >{{ item.name }}</Option
                ></template
              >
            </Select>
          </div>
        </div>
        <!-- loop -->
        <div v-else-if="protocalType == 'Loop'">
          <template v-for="index in selectedActivities.length - 1">
            <div :key="index" style="margin-bottom: 5px">
              <span style="margin-right: 20px">
                <label>Start activity: </label>
                <Select
                  v-if="index == 1"
                  v-model="activityLinks[index - 1]"
                  style="width: 200px"
                  placeholder="Select activity"
                  @on-change="activitySelect(index - 1)"
                  clearable
                  @on-clear="activitySelectClear(index - 1)"
                >
                  <template v-for="item in otherNodes"
                    ><Option
                      :value="item.aid"
                      :key="item.id"
                      :class="item.aid"
                      :disabled="item.selected != undefined && item.selected"
                      >{{ item.name }}</Option
                    >
                  </template>
                </Select>
                <Select
                  v-else
                  v-model="activityLinks[index - 1]"
                  style="width: 200px"
                  placeholder="Select activity"
                  disabled
                >
                  <template v-for="item in otherNodes"
                    ><Option
                      :value="item.aid"
                      :key="item.id"
                      :class="item.aid"
                      >{{ item.name }}</Option
                    >
                  </template>
                </Select>
              </span>
              <Icon type="md-arrow-forward" style="margin-right: 20px" />
              <span>
                <label>End activity: </label>
                <Select
                  v-model="activityLinks[index]"
                  style="width: 200px"
                  placeholder="Select activity"
                  @on-change="activitySelect(index)"
                  clearable
                  @on-clear="activitySelectClear(index)"
                >
                  <template v-for="item in otherNodes">
                    <Option
                      :value="item.aid"
                      :key="item.id"
                      :class="item.aid"
                      :disabled="item.selected != undefined && item.selected"
                      >{{ item.name }}</Option
                    >
                  </template>
                </Select>
              </span>
              <Icon type="md-arrow-forward" style="margin: 0 20px" />
            </div>
          </template>
          <div style="margin-bottom: 5px">
            <span style="margin-right: 20px">
              <label>Start activity: </label>
              <Select
                v-model="activityLinks[selectedActivities.length - 1]"
                style="width: 200px"
                placeholder="Select activity"
                disabled
              >
                <template v-for="item in otherNodes"
                  ><Option :value="item.aid" :key="item.id" :class="item.aid">{{
                    item.name
                  }}</Option>
                </template>
              </Select>
            </span>
            <Icon type="md-arrow-forward" style="margin-right: 20px" />
            <span>
              <label>End activity: </label>
              <Select
                v-model="activityLinks[0]"
                style="width: 200px"
                placeholder="Select activity"
                disabled
              >
                <template v-for="item in otherNodes">
                  <Option :value="item.aid" :key="item.id" :class="item.aid">{{
                    item.name
                  }}</Option>
                </template>
              </Select>
            </span>
          </div>
        </div>
      </div>
      <Divider orientation="left">Person link</Divider>
      <div style="margin: 0 20px">
        <div style="display: flex; margin-bottom: 15px">
          <div style="margin-top: 5px; width: 150px">
            Type of person protocol:
          </div>
          <Select
            v-model="roleProtocol"
            style="width: 200px"
            placeholder="Select"
            @on-change="roleProtocolChange"
          >
            <Option value="None">None</Option>
            <Option value="All">All</Option>
            <Option value="Constraints">Constraints</Option>
          </Select>
        </div>
        <template v-if="roleProtocol === 'Constraints'">
          <div style="display: flex; margin-bottom: 15px">
            <div style="margin-top: 5px; width: 80px">Role:</div>
            <Select
              v-model="linkRoles"
              multiple
              placeholder="Which roles of participants could join the next activity?"
            >
              <Option value="manager">Manager</Option>
              <Divider style="margin: 5px 0"></Divider>
              <Option value="core" disabled>Core team</Option>
              <Option value="researcher">Researcher</Option>
              <Option value="expert">Expert</Option>
              <Option value="decision-maker">Decision-maker</Option>
              <Option value="core-member">Core-member</Option>
              <Divider style="margin: 5px 0"></Divider>
              <Option value="ordinary" disabled>Ordinary team</Option>
              <Option value="stakeholder">Stakeholder</Option>
              <Option value="consultant">Consultant</Option>
              <Option value="ordinary-member">Ordinary-member</Option>
            </Select>
          </div>
          <div style="display: flex">
            <div style="margin-top: 5px; width: 72px">Domains:</div>
            <!--            <vue-tags-input-->
            <!--              class="domain"-->
            <!--              v-model="domain_tag"-->
            <!--              :tags="linkDomains"-->
            <!--              @tags-changed="(newTags) => (linkDomains = newTags)"-->
            <!--            />-->
            <Select v-model="selectUserDomain" multiple :max-tag-count="4">
              <Option
                v-for="(item, index) in userDomain"
                :value="item"
                :key="index"
              ></Option>
            </Select>
          </div>
          <div style="display: flex">
            <div style="margin-top: 5px; width: 72px">Organizations:</div>
            <Select v-model="selectUserOrg" multiple :max-tag-count="4">
              <Option
                v-for="(item, index) in userOrganizations"
                :value="item"
                :key="index"
              ></Option>
            </Select>
          </div>
        </template>
      </div>
      <Divider orientation="left">Resource link</Divider>
      <div style="margin: 0 20px">
        <div style="display: flex; margin-bottom: 15px">
          <div style="width: 150px">Update automatically:</div>
          <i-switch v-model="autoUpdate" />
        </div>
        <div style="display: flex; margin-bottom: 15px">
          <div style="margin-top: 5px; width: 150px">
            Type of resource protocol:
          </div>
          <Select
            v-model="resProtocol"
            style="width: 200px"
            placeholder="Select"
            @on-change="resProtocolChange"
          >
            <Option value="None">None</Option>
            <Option value="All">All</Option>
            <Option value="Constraints">Constraints</Option>
          </Select>
        </div>
        <template v-if="resProtocol === 'Constraints'">
          <div style="display: flex; margin-bottom: 15px">
            <div style="margin-top: 5px; width: 72px">Types:</div>
            <Select
              v-model="resProtocolForm.types"
              multiple
              :max-tag-count="2"
              placeholder="Type of resources"
              style="width: 267px"
            >
              <Option value="data">Data</Option>
              <Option value="paper">Papers</Option>
              <Option value="document">Documents</Option>
              <Option value="model">Models</Option>
              <Option value="image">Images</Option>
              <Option value="video">Videos</Option>
              <Option value="variable">Variables</Option>
              <Option value="others">Others</Option>
            </Select>
            <div style="margin-top: 5px; margin-left: 50px; width: 72px">
              Formats:
            </div>
            <vue-tags-input
              class="res-protocol"
              v-model="formats_tag"
              :tags="resProtocolForm.formats"
              @tags-changed="(newTags) => (resProtocolForm.formats = newTags)"
            />
          </div>
          <div style="display: flex; margin-bottom: 15px">
            <div style="margin-top: 5px; width: 72px">Scales:</div>
            <vue-tags-input
              class="res-protocol"
              v-model="scales_tag"
              :tags="resProtocolForm.scales"
              @tags-changed="(newTags) => (resProtocolForm.scales = newTags)"
            />
            <div style="margin-top: 5px; margin-left: 50px; width: 72px">
              References:
            </div>
            <vue-tags-input
              class="res-protocol"
              v-model="references_tag"
              :tags="resProtocolForm.references"
              @tags-changed="
                (newTags) => (resProtocolForm.references = newTags)
              "
            />
          </div>
          <div style="display: flex; margin-bottom: 15px">
            <div style="margin-top: 5px; width: 72px">Units:</div>
            <vue-tags-input
              class="res-protocol"
              v-model="units_tag"
              :tags="resProtocolForm.units"
              @tags-changed="(newTags) => (resProtocolForm.units = newTags)"
            />
            <div style="margin-top: 5px; margin-left: 50px; width: 72px">
              Concepts:
            </div>
            <vue-tags-input
              class="res-protocol"
              v-model="concepts_tag"
              :tags="resProtocolForm.concepts"
              @tags-changed="(newTags) => (resProtocolForm.concepts = newTags)"
            />
          </div>
        </template>
      </div>
    </Modal>
    <login-modal
      :tempLoginModal="tempLoginModal"
      @changeLoginModal="changeLoginModal"
    ></login-modal>
  </Row>
</template>
<script>
// import "driver.js/dist/driver.min.css";
import echarts from "echarts";
import VueTagsInput from "@johmun/vue-tags-input";
import loginModal from "../../../user/userState/loginModal.vue";
// import Driver from "driver.js";
export default {
  components: { VueTagsInput, loginModal },
  props: ["activityInfo", "childActivities", "userInfo"],
  data() {
    return {
      projectInfo: {},
      // driver
      // driver: new Driver(),
      // user
      // userInfo: JSON.parse(sessionStorage.getItem("userInfo")),
      userRole: "visitor",
      //userTag
      selectUserDomain: [],
      selectUserOrg: [],
      //button
      linkBtn: false,
      removeLinkBtn: false,
      nodePositionBtn: false,
      //link
      // relation
      protocalType: "Sequence",
      linkStep: 0,
      otherNodes: [],
      activityLinks: [],
      // linkRelations: [
      //   {
      //     start: "",
      //     end: "",
      //   },
      // ],
      // relationCount: 1,
      // relationIndex: 0,
      linkBuildModal: false,
      beginNode: {},
      endNode: {},
      linkBuildModal: false,
      //恢复登录的模态框
      tempLoginModal: false,

      roleProtocol: "None",
      linkRoles: [],
      domain_tag: "",
      linkDomains: [],
      // resource
      resProtocol: "None",
      autoUpdate: false,
      types_tag: "",
      formats_tag: "",
      scales_tag: "",
      references_tag: "",
      units_tag: "",
      concepts_tag: "",
      resProtocolForm: {
        types: [],
        formats: [],
        scales: [],
        references: [],
        units: [],
        concepts: [],
      },
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
        "Other purpose",
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
      userDomain: [],
      userOrganizations: [],
      resTypes: [],
      resSuffixes: [],
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
      this.userRole = this.userRoleApi.roleIdentify(
        this.activityInfo.members,
        this.userInfo.userId
      );
    },
    permissionIdentity(permission, operation) {
      return this.userRoleApi.permissionIdentity(
        JSON.parse(permission),
        this.userRole,
        operation
      );
    },
    changeLoginModal(status) {
      this.tempLoginModal = status;
    },
    updateStepchart() {
      // 重新渲染
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
        } else if (this.selectedActivities.length >= 2) {
          if (this.linkStep == 1) {
            this.removeLinkBtn = false;
            this.linkBtn = true;
          } else if (
            this.linkStep == 0 &&
            this.selectedActivities.length == 2
          ) {
            let activity1 = this.selectedActivities[0];
            let activity2 = this.selectedActivities[1];
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
              name: "Other purpose",
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
                name: "Other purpose",
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
        purpose = "Other purpose";
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
        this.$Notice.info({
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
            this.$store.commit("userLogout");
            this.tempLoginModal = true;
          } else if (res.data.code == 0) {
            this.updateStepchart();
            this.selectedActivities = [];
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
          desc: "Please select two nodes at least!",
        });
      } else if (this.linkStep == 1) {
        if (this.selectedActivities.length >= 2) {
          // operation
          this.changeProtocalType();
          this.linkBuildModal = true;
          // record and end
          this.linkStep = 0;
        } else {
          this.$Notice.info({
            desc: "Please select two nodes and restart to link activities!",
          });
        }
      }
    },
    changeProtocalType() {
      this.otherNodes = [];
      // this.otherNodes = Object.assign([], this.selectedActivities);
      this.otherNodes = JSON.parse(JSON.stringify(this.selectedActivities));

      this.activityLinks = [];
      for (let i = 0; i < this.selectedActivities.length; i++) {
        this.activityLinks.push(0);
      }
    },
    activitySelect(index) {
      let slctActivityId = this.activityLinks[index];
      for (let i = 0; i < this.otherNodes.length; i++) {
        if (this.otherNodes[i].aid === slctActivityId) {
          this.otherNodes[i]["selected"] = true;
        }
      }
    },
    activitySelectClear(index) {
      let slctActivityId = this.activityLinks[index];
      for (let i = 0; i < this.otherNodes.length; i++) {
        if (this.otherNodes[i].aid === slctActivityId) {
          this.otherNodes[i]["selected"] = false;
        }
      }
    },
    cancelLink() {
      // 重新渲染
      this.updateStepchart();
      // clear
      this.selectedActivities = [];
      this.beginNode = {};
      this.endNode = {};
      this.clearProtocolSetting();
      this.activityLinks = [];
    },
    processUpdate() {
      let relations = [];
      // identify the relations
      switch (this.protocalType) {
        case "Sequence": {
          for (let i = 0; i < this.activityLinks.length - 1; i++) {
            let relation = {
              last: this.activityLinks[i],
              next: this.activityLinks[i + 1],
            };
            relations.push(relation);
          }
          break;
        }
        case "Branch": {
          for (let i = 1; i < this.activityLinks.length; i++) {
            let relation = {
              last: this.activityLinks[0],
              next: this.activityLinks[i],
            };
            relations.push(relation);
          }
          break;
        }
        case "Merger": {
          for (let i = 1; i < this.activityLinks.length; i++) {
            let relation = {
              last: this.activityLinks[i],
              next: this.activityLinks[0],
            };
            relations.push(relation);
          }
          break;
        }
        case "Loop": {
          for (let i = 0; i < this.activityLinks.length - 1; i++) {
            let relation = {
              last: this.activityLinks[i],
              next: this.activityLinks[i + 1],
            };
            relations.push(relation);
          }
          let relation = {
            last: this.activityLinks[this.activityLinks.length - 1],
            next: this.activityLinks[0],
          };
          relations.push(relation);
          break;
        }
      }
      //  update
      for (let i = 0; i < relations.length; i++) {
        let lastnode, nextnode;

        for (let j = 0; j < this.selectedActivities.length; j++) {
          if (this.selectedActivities[j].aid == relations[i].last) {
            lastnode = {
              name: this.selectedActivities[j].name,
              id: this.selectedActivities[j].id,
            };
          }
          if (this.selectedActivities[j].aid == relations[i].next) {
            nextnode = {
              name: this.selectedActivities[j].name,
              id: this.selectedActivities[j].id,
            };
          }
        }

        for (let k = 0; k < this.processStructure.length; k++) {
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
      }
      this.updatePathway();
    },
    buildLink: function () {
      //** front link */
      this.processUpdate();

      //*** save protocol */

      //// normalize protocol
      let relation = {
        graphId: this.activityInfo.aid,
        type: this.protocalType,
        nodes: this.activityLinks,
      };

      let restriction = {
        resProtocol: this.resProtocol,
        autoUpdate: this.autoUpdate,
        types: this.resProtocolForm.types,
        formats: this.filterTags(this.resProtocolForm.formats),
        concepts: this.filterTags(this.resProtocolForm.concepts),
        scales: this.filterTags(this.resProtocolForm.scales),
        references: this.filterTags(this.resProtocolForm.references),
        units: this.filterTags(this.resProtocolForm.units),

        roleProtocol: this.roleProtocol,
        roles: this.linkRoles,
        domains: this.selectUserDomain,
        organizations: this.selectUserOrg,
      };

      let protocolForm = {
        relation: relation,
        restriction: restriction,
      };

      //// save protocol
      this.axios
        .post("/GeoProblemSolving/activityDriven", protocolForm)
        .then((res) => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.tempLoginModal = true;
          } else if (res.data.code == 0) {
            // 重新渲染
            this.updateStepchart();
            // link in the activity document
            let protocolId = res.data.data.pid;
            this.newLinkStore(this.beginNode.aid, this.endNode.aid, protocolId);
            // clear
            this.selectedActivities = [];
            this.beginNode = {};
            this.endNode = {};
          } else {
            this.$Message.error("Fail to link activities.");
            console.log(res.data.msg);
          }
        })
        .catch((err) => {
          throw err;
        });

      // if save protocol success, update the avtivity document
      // link in the activity document
      let protocolId = res.data.data.pid;
      this.newLinkStore(this.beginNode.aid, this.endNode.aid, protocolId);
    },
    clearProtocolSetting() {
      this.roleProtocol = "";
      this.linkRoles = [];
      this.domain_tag = "";
      this.linkDomains = [];

      this.resProtocol = "";
      this.autoUpdate = false;
      this.types_tag = "";
      this.formats_tag = "";
      this.scales_tag = "";
      this.references_tag = "";
      this.units_tag = "";
      this.concepts_tag = "";
      this.resProtocolForm = {
        types: [],
        formats: [],
        scales: [],
        references: [],
        units: [],
        concepts: [],
      };
    },
    filterTags(tags) {
      let newtags = [];
      for (let i = 0; i < tags.length; i++) {
        newtags.push(tags[i].text);
      }
      return newtags;
    },
    newLinkStore(begin, end, pid) {
      let updateurl = "";
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
            this.$store.commit("userLogout");
            this.tempLoginModal = true;
          } else if (res.data.code == 0) {
            this.operationApi.processRecord(
              this.activityInfo.aid,
              "",
              "link",
              this.userInfo.userId,
              begin,
              end,
              pid
            );

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
      this.selectedActivities = [];
      this.beginNode = {};
      this.endNode = {};
      this.removeLinkBtn = false;
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
            this.$store.commit("userLogout");
            this.tempLoginModal = true;
          } else if (res.data.code == 0) {
            this.operationApi.processRecord(
              this.activityInfo.aid,
              "",
              "break",
              this.userInfo.userId,
              begin,
              end,
              ""
            );
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
          
          window.location.href =
            "/GeoProblemSolving/activityInfo/" +
            this.projectInfo.aid +
            "?aid=" +
            this.childActivities[i].aid +
            "&level=" +
            this.childActivities[i].level;
        }
      }
    },
    roleProtocolChange(val) {
      let aidList = [];
      this.selectedActivities.forEach((activity) => {
        aidList.push(activity.aid);
      });
      if (val == "Constraints") {
        this.axios
          .get(
            "/GeoProblemSolving/activityDriven/user/tag/" + aidList.toString()
          )
          .then((res) => {
            let code = res.data.code;
            if (code != 0) {
              this.$Notice.info({
                desc: "ERROR!",
              });
            } else {
              let data = res.data.data;
              this.userDomain = data.domains;
              this.userOrganizations = data.organizations;
            }
          })
          .catch((e) => {
            this.$Notice.info({
              desc: "ERROR!",
            });
          });
      }
    },
    resProtocolChange(val) {
      let aidList = [];
      this.selectedActivities.forEach((activity) => {
        aidList.push(activity.aid);
      });
      if (val == "Constraints") {
        this.axios
          .get(
            "/GeoProblemSolving/activityDriven/res/tag/" + aidList.toString()
          )
          .then((res) => {
            let code = res.data.code;
            if (code != 0) {
              this.$Notice.info({
                desc: "ERROR!",
              });
            } else {
              let data = res.data.data;
              this.resTypes = data.types;
              this.resSuffixes = data.suffixes;
            }
          })
          .catch((e) => {
            this.$Notice.info({
              desc: "ERROR!",
            });
          });
      }
    },
  },
};
</script>
