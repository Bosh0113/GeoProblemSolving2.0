<!--resource Center页面-->
<style scoped>
.selector {
  width: 250px;
}
.resourceTitle {
  font-size: 18px;
  height: 20px;
  line-height: 20px;
}
.resourceBtnDiv {
  display: flex;
  align-items: center;
  height: 20px;
  padding: 5px;
}
.fileBtnHoverGreen:hover {
  background-color: #19be6b;
  color: white;
}
.fileBtnHoverGray:hover {
  background-color: #808695;
  color: white;
}
.leftMenuItem {
  margin: 0 0 10px 0;
}
</style>
<template>
  <div>
    <div span="2" style="height: inherit;width: 90px;position: absolute;z-index: 1;">
      <Menu
        active-name="projectResource"
        @on-select="changeMenuItem"
        style="height: inherit;width: fit-content;"
      >
        <MenuItem name="projectResource" class="leftMenuItem">
          <Icon type="ios-paper" title="Project resources" size="35"></Icon>
        </MenuItem>
        <MenuItem name="personalResource" class="leftMenuItem">
          <Icon type="ios-folder" title="Personal resources" size="35"></Icon>
        </MenuItem>
        <MenuItem name="publicResource" class="leftMenuItem">
          <Icon type="logo-dropbox" title="Public resources" size="35"></Icon>
        </MenuItem>
      </Menu>
    </div>
    <div style="margin-left: 90px;height: inherit;min-height: fit-content;">
      <div v-if="showMenuItem=='projectResource'" style="height: inherit;min-height: fit-content;">
        <Card shadow>
          <h1 slot="title">Project resources</h1>
          <div>
            <div>
              <span>Project:</span>
              <Select class="selector" v-model="selectedProjectId" @on-change="changeProject">
                <Option
                  v-for="project in projectList"
                  :key="project.projectId"
                  :value="project.projectId"
                >{{project.title}}</Option>
              </Select>
              <span style="margin-left:15px">-> Subproject:</span>
              <Select
                class="selector"
                v-model="selectedSubProejctId"
                :disabled="subProjectDisable"
                @on-change="changeSubProject"
              >
                <Option
                  v-for="subProject in subProjectList"
                  :key="subProject.subProjectId"
                  :value="subProject.subProjectId"
                >{{subProject.title}}</Option>
              </Select>
              <span style="margin-left:15px">-> Step:</span>
              <Select
                class="selector"
                v-model="selectedStepId"
                :disabled="stepDisable"
                @on-change="changeStep"
              >
                <Option
                  v-for="step in stepList"
                  :key="step.stepID"
                  :value="step.stepID"
                >{{step.name}}</Option>
              </Select>
            </div>
            <div style="margin-top:20px">
              <Card dis-hover>
                <div slot="title">
                  <div style="display:inline-block">
                    <span style="font-weight: bold;">view:</span>
                    <i-switch
                      v-model="folderType"
                      style="margin-right:5px"
                      @on-change="getResourceInfo"
                    >
                      <Icon type="ios-folder" slot="open" title="Folder"></Icon>
                      <Icon type="md-list" slot="close" title="List"></Icon>
                    </i-switch>
                  </div>
                  <div v-if="!folderType" style="display:inline-block;margin-left:20px">
                    <div style="display:inline-block">
                      <span style="font-weight: bold;">category:</span>
                      <RadioGroup v-model="fileType" @on-change="changeFileType">
                        <Radio label="all">All</Radio>
                        <Radio label="data">Data</Radio>
                        <Radio label="paper">Paper</Radio>
                        <Radio label="document">Document</Radio>
                        <Radio label="model">Model</Radio>
                        <Radio label="image">Image</Radio>
                        <Radio label="video">Video</Radio>
                        <Radio label="others">Others</Radio>
                      </RadioGroup>
                    </div>
                    <div style="display:inline-block;margin-left:50px">
                      <Input search placeholder="Enter something..." size="small" />
                    </div>
                  </div>
                </div>
                <div>
                  <div v-if="folderType" style="min-height:500px;">
                    <h1 v-if="scopeId==''">Folder</h1>
                    <folder-tree
                      v-else
                      :root-folder-id="scopeId"
                      :role="userRole"
                      :project="selectProjectInfo"
                      ref="folderTreeEl"
                      :key="freshFolder"
                    ></folder-tree>
                  </div>
                  <div v-else>
                    <h1 v-if="scopeId==''">List</h1>
                    <Card v-else>
                      <div slot="title" class="resourceTitle">
                        <strong>Resources</strong>
                      </div>
                      <div slot="extra" class="resourceBtnDiv">
                        <Tooltip content="Download all selected" placement="bottom">
                          <Button class="fileBtnHoverGray" @click="downloadFiles">
                            <Icon size="20" type="md-cloud-download"></Icon>
                          </Button>
                        </Tooltip>
                      </div>
                      <div style="height:500px">
                        <vue-scroll :ops="ops">
                          <Table
                            :data="showResourceList"
                            :columns="resourceColumn"
                            class="table"
                            @on-selection-change="setSelectedFiles"
                            no-data-text="No data"
                          >
                            <template slot-scope="{ row }" slot="name">
                              <strong>{{ row.name }}</strong>
                            </template>
                            <template slot-scope="{ row, index }" slot="action">
                              <Button
                                class="fileBtnHoverGreen"
                                size="small"
                                title="Download"
                                @click="downloadSingleFile(index)"
                                icon="md-download"
                                shape="circle"
                                type="text"
                              ></Button>
                            </template>
                          </Table>
                        </vue-scroll>
                      </div>
                    </Card>
                  </div>
                </div>
              </Card>
            </div>
          </div>
        </Card>
      </div>
      <div v-if="showMenuItem=='personalResource'" style="height: inherit;min-height: fit-content;">
        <Card shadow>
          <h1 slot="title">Personal resources</h1>
          <personal-resouce></personal-resouce>
        </Card>
      </div>
      <div v-if="showMenuItem=='publicResource'" style="height: inherit;min-height: fit-content;">
        <Card shadow>
          <h1 slot="title">Public resources</h1>
          <div>
            <public-resouce></public-resouce>
          </div>
        </Card>
      </div>
    </div>
  </div>
</template>
<script>
import folderTree from "./folderTree.vue";
import personalResouce from "./personalResourceEl.vue";
import publicResouce from "./publicResourceList.vue";
export default {
  components: {
    folderTree,
    personalResouce,
    publicResouce
  },
  data() {
    return {
      userInfo: this.$store.getters.userInfo,
      showMenuItem: "projectResource",
      // 选择项目
      projectList: [],
      selectedProjectId: "",
      selectProjectInfo: {},
      // 选择子项目
      subProjectList: [],
      subProjectDisable: true,
      selectedSubProejctId: "",
      selectSubprojectInfo: {},
      // 选择step
      stepList: [],
      stepDisable: true,
      selectedStepId: "",
      fileType: "all",
      folderType: true,
      freshFolder: 0,
      switchDisable: false,
      scopeId: "",
      scopeResourceList: [],
      showResourceList: [],
      resourceColumn: [
        {
          type: "selection",
          width: 60,
          align: "center"
        },
        {
          title: "Name",
          key: "name",
          tooltip: true,
          sortable: true
        },
        {
          title: "Description",
          key: "description",
          tooltip: true,
          sortable: true
        },
        {
          title: "Type",
          key: "type",
          sortable: true,
          width: 90
        },
        {
          title: "Uploader",
          key: "uploaderName",
          sortable: true,
          width: 120
        },
        {
          title: "Time",
          key: "uploadTime",
          width: 160,
          sortable: true
        },
        {
          title: "Action",
          slot: "action",
          width: 150,
          align: "center"
        }
      ],
      ops: {
        bar: {
          background: "#808695"
        }
      },
      filesToPackage: [],
      userRole: "Visitor"
    };
  },
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      if (!vm.$store.getters.userState) {
        next("/login");
      } else {
        next();
      }
    });
  },
  mounted() {
    this.getProjectList();
  },
  methods: {
    getProjectList() {
      var manageProjects = this.userInfo.manageProjects;
      var joinedProjects = this.userInfo.joinedProjects;
      var allProjects = manageProjects.concat(joinedProjects);
      this.$set(this, "projectList", allProjects);
      if (allProjects.length > 0) {
        this.selectedProjectId = allProjects[0].projectId;
        this.getSubProjectList(this.projectList[0].projectId);
        this.scopeId = this.selectedProjectId;
      }
      this.getResourceInfo();
    },
    getSubProjectList(projectId) {
      var subProjectOptions = [];
      var defaultOption = [
        { name: "—show subproject resources—", stepID: "all" }
      ];
      this.selectedStepId = defaultOption[0].stepID;
      this.stepDisable = true;
      this.axios
        .get(
          "/GeoProblemSolving/subProject/inquiry" +
            "?key=" +
            "projectId" +
            "&value=" +
            projectId
        )
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data != "Fail") {
            if (res.data != "None") {
              var subProjectInfos = res.data;
              subProjectOptions = defaultOption.concat(subProjectInfos);
            } else {
              subProjectOptions = defaultOption;
            }
            this.$set(this, "subProjectList", subProjectOptions);
            this.selectedSubProejctId = subProjectOptions[0].subProjectId;
            this.subProjectDisable = false;
          } else {
            this.$Message.warning("Get subprojects info fail.");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    getStepList(subProjectId) {
      var thisSubProject = {};
      for (var i = 0; i < this.subProjectList.length; i++) {
        if (this.subProjectList[i].subProjectId == subProjectId) {
          thisSubProject = this.subProjectList[i];
          break;
        }
      }
      var defaultOption = [
        { name: "—show subproject resources—", stepID: "all" }
      ];
      var stepOptions = [];
      if (thisSubProject.solvingProcess != undefined) {
        var stepInfos = JSON.parse(thisSubProject.solvingProcess);
        stepOptions = defaultOption.concat(stepInfos);
      } else {
        stepOptions = defaultOption;
      }
      this.$set(this, "stepList", stepOptions);
      this.selectedStepId = stepOptions[0].stepID;
      this.stepDisable = false;
    },
    getProjectInfo(projectId) {
      var that = this;
      $.ajax({
        url:
          "/GeoProblemSolving/project/inquiry" +
          "?key=projectId" +
          "&value=" +
          projectId,
        type: "GET",
        async: false,
        success: data => {
          if (data == "Offline") {
            that.$store.commit("userLogout");
            that.$router.push({ name: "Login" });
          } else if (data != "None" && data != "Fail") {
            that.selectProjectInfo = data[0];
            this.userRoleIdentity("project");
          } else {
            console.log(data);
          }
        }
      });
    },
    getSubprojectInfo(subprojectId) {
      for (let i = 0; i < this.subProjectList.length; i++) {
        if (this.subProjectList[i].subProjectId == subprojectId) {
          this.$set(this, "selectSubprojectInfo", this.subProjectList[i]);
          break;
        }
      }
      this.userRoleIdentity("subproject");
    },
    changeProject(projectId) {
      this.scopeId = projectId;
      this.getProjectInfo(projectId);
      this.subProjectDisable = true;
      this.getSubProjectList(projectId);
      this.getResourceInfo();
    },
    changeSubProject(subProjectId) {
      if (subProjectId == "all") {
        this.scopeId = this.selectedProjectId;
        var defaultOption = [
          { title: "—show subproject resources—", stepID: "all" }
        ];
        this.selectedStepId = defaultOption[0].stepID;
        this.stepDisable = true;
      } else {
        this.scopeId = subProjectId;
        this.getSubProjectList(subProjectId);
        this.getSubprojectInfo(subProjectId);
        this.getStepList(subProjectId);
      }
      this.getResourceInfo();
    },
    userRoleIdentity(level) {
      this.userRole = "Visitor";
      if (this.$store.getters.userState) {
        if (level == "subproject") {
          // 是否是子项目管理员
          if (selectSubprojectInfo.managerId === this.$store.getters.userId) {
            this.userRole = "Manager";
          }
          // 是否是子项目成员
          else {
            for (let i = 0; i < selectSubprojectInfo.members.length; i++) {
              if (
                selectSubprojectInfo.members[i].userId ===
                this.$store.getters.userId
              ) {
                this.userRole = "Member";
                break;
              }
            }
          }
        }
        // 是否是项目管理员
        if (this.userRole != "Manager") {
          if (selectProjectInfo.managerId === this.$store.getters.userId) {
            this.userRole = "PManager";
          }
        }
      } else {
        this.userRole = "Visitor";
      }
    },
    changeStep(stepId) {
      if (stepId == "all") {
        this.scopeId = this.selectedSubProejctId;
      } else {
        this.scopeId = stepId;
      }
      this.getResourceInfo();
    },
    changeFileType(value) {
      if (value != "all") {
        this.showResourceList = this.scopeResourceList.filter(e => {
          return e.type == value;
        });
      } else {
        this.showResourceList = this.scopeResourceList;
      }
    },
    getResourceInfo() {
      if (this.folderType) {
        this.freshFolder++; //用于组件重渲染，无特别意义
      } else {
        this.getResourceShowList();
      }
    },
    getResourceShowList() {
      this.axios
        .get(
          "/GeoProblemSolving/folder/findByFileType" +
            "?scopeId=" +
            this.scopeId +
            "&type=" +
            "all"
        )
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data != "Fail") {
            this.scopeResourceList = res.data;
            this.changeFileType(this.fileType);
          } else {
            this.$Message.warning("Get resource info fail.");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    setSelectedFiles(selectedFileList) {
      var selectedFileUrls = [];
      for (var i = 0; i < selectedFileList.length; i++) {
        selectedFileUrls.push(selectedFileList[i].pathURL);
      }
      this.$set(this, "filesToPackage", selectedFileUrls);
    },
    downloadFiles() {
      if (this.filesToPackage.length > 0) {
        this.$Spin.show();
        var filesUrlStr = this.filesToPackage.toString();
        this.axios({
          method: "post",
          url: "/GeoProblemSolving/resource/packageZIP?fileURLs=" + filesUrlStr,
          responseType: "blob"
        })
          .then(res => {
            this.$Spin.hide();
            if (res.status == 200) {
              const blobUrl = window.URL.createObjectURL(res.data);
              if (blobUrl != "") {
                this.download(blobUrl);
              }
            }
          })
          .catch(err => {
            confirm("errror");
          });
      } else {
        this.$Message.warning("No file be selected.");
      }
    },
    download(blobUrl) {
      const a = document.createElement("a");
      a.style.display = "none";
      a.download = "package.zip";
      a.href = blobUrl;
      a.click();
      a.remove();
    },
    downloadSingleFile(index) {
      window.open(this.showResourceList[index].pathURL);
    },
    changeMenuItem(name) {
      this.showMenuItem = name;
    }
  }
};
</script>
