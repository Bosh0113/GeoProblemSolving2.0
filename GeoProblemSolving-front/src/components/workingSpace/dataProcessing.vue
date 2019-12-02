<style scoped>
.picscreen {
  position: relative;
  padding-top: 10px;
}

.picbg {
  background-image: url("../../assets/images/condefbg.png");
  background-size: cover;
  position: absolute;
  height: 100px;
  width: 100%;
  background-color: #8b8b8b;
  top: 0;
  left: 0;
}

.fileUdx {
  float: left;
  height: 100%;
  width: 35%;
  margin-left: 10px;
}

.condef {
  float: left;
  height: 100%;
  width: 44%;
  margin-left: 10px;
}

/* content */
.condefContent {
  margin: 0 15%;
  height: auto;
}

.home_content >>> .ivu-breadcrumb {
  color: rgb(180, 197, 207);
  /* min-height: 500px; */
}
.home_content >>> .ivu-breadcrumb > span:last-child {
  color: rgb(180, 197, 207);
}

.tool-panel {
  display: flex;
  height: auto;
  flex-wrap: wrap;

  /* justify-content: center; */
}

/* 工具库中抽屉的工具样式*/
.singl_tool_style {
  margin: 10px;
  width: 100%;
  cursor: pointer;
  clear: both;
  display: flex;
  justify-content: center;
}

.singl_tool_style:hover {
  transition: all 1s;
  background-color: lightgray;
}

.resourceCard {
  clear: both;
  width: 100%;
}

#subDescription {
  width: 100%;
  display: inline-block;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
  text-align: center;
  font-size: 1rem;
  height: 20px;
  color: white;
}

.resourceMenu {
  width: 35%;
  position: absolute;
  top: 50px;
  left: 10px;
  right: 50px;
}

.resourceTable {
  width: 60%;
  position: absolute;
  top: 50px;
  left: 300px;
  right: 50px;
}

.stepName {
  text-align: center;
  font-size: 1.2rem;
  height: 20px;
  color: white;
}
.stepName p {
  margin: 0 auto;
  font-size: 1rem;
  height: 20px;
  color: white;
  word-break: break-word;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  max-width: 400px;
}

.onlineListBtn {
  position: absolute;
  top: 70%;
  right: 10%;
  /* width: 100px */
}

/* .pro-tab>>>.ivu-modal-footer {
    border-top:none;
    padding-bottom:12px;
    padding-right:18px ;
  }
  .pro-tab>>>.ivu-modal-body {
    padding-bottom: 0;
  } */

/* .pro-tab >>> .ivu-btn-icon-only {
  font-size: 40px;
} */
.subproject-back >>> .ivu-breadcrumb-item-link {
  color: white;
}

.breadCrumb {
  margin-left: 1%;
}
</style>
<template>
  <div style="background-color:#e8eaec;height:auto;margin-right: calc(100% - 100vw)">
    <div class="picscreen">
      <div class="picbg"></div>

      <div class="home_content">
        <Row>
          <!-- 需要修改样式 -->
          <div class="breadCrumb">
            <Breadcrumb>
              <!-- <BreadcrumbItem :to="toProjectPage">Project</BreadcrumbItem> -->
              <BreadcrumbItem :to="toSubProjectPage" class="subproject-back">Subproject</BreadcrumbItem>
              <BreadcrumbItem style="color: white">Data processing</BreadcrumbItem>
            </Breadcrumb>
          </div>
          <div class="stepName">
            <strong>{{stepContent.name}}</strong>
            <p :title="stepContent.description">{{stepContent.description}}</p>
          </div>
          <div class="onlineListBtn">
            <Button @click="drawerValue = true" type="default" ghost>Participants</Button>
            <template v-if="userRole == 'Manager'">
              <Button @click="modifyStep = true" style="margin-left:20px" ghost>Modify Step</Button>
            </template>
            <step-change></step-change>
          </div>
        </Row>
        <Drawer title="Participants" :closable="false" v-model="drawerValue">
          <online-participant :sub-project-id="subProjectInfo.subProjectId" :room-id="stepId"></online-participant>
          <div class="toChatroom" style="position:absolute; left:25%;bottom:5%">
            <Button @click.native="toolPanel('chat')" type="success">Go to chatroom</Button>
          </div>
        </Drawer>

        <Modal v-model="modifyStep">
          <p slot="header" style="text-align:center">
            <Icon type="ios-information-circle"></Icon>
            <span>Modify step name and description</span>
          </p>
          <Form :label-width="120" label-position="left" :model="stepForm">
            <FormItem label="Step Name" prop="name">
              <Input
                v-model="stepForm.name"
                type="textarea"
                :autosize="{minRows: 1,maxRows: 3}"
                clearable
              />
            </FormItem>
            <FormItem label="Step Description" prop="description">
              <Input
                v-model="stepForm.description"
                type="textarea"
                :autosize="{minRows: 1,maxRows: 3}"
                clearable
              />
            </FormItem>
          </Form>
          <div slot="footer">
            <Button @click="cancelModifyStep">Cancel</Button>
            <Button type="primary" @click="submitModifyStep">Modify</Button>
          </div>
        </Modal>
      </div>
    </div>

    <!-- tab  on-click事件 -->

    <div class="pro-tab" :style="{height:contentHeight+14+'px'}" style="margin-top:60px">
      <template style="margin:20px 1%">
        <Card
          :style="{height:contentHeight+'px'}"
          style="margin:0 5px 0 10px; width:300px; float:left"
        >
          <data-list :stepInfo="stepContent" :contentHeight="contentHeight" :userRole="userRole"></data-list>
        </Card>
        <Card
          :style="{height:contentHeight+'px', width:mapWidth+'px'}"
          style="margin:0 5px;float:left"
        >
          <map-canvas
            :stepInfo="stepContent"
            :contentHeight="contentHeight"
            :mapWidth="mapWidth"
            :userRole="userRole"
          ></map-canvas>
        </Card>
        <Card
          :style="{height:contentHeight+'px'}"
          style="margin:0 10px 0 5px; width:400px;float:left;margin-right: calc(100% - 100vw)"
        >
          <tool-container
            :stepInfo="stepContent"
            :contentHeight="contentHeight"
            :userRole="userRole"
          ></tool-container>
        </Card>
      </template>
    </div>
  </div>
</template>

<script>
import * as socketApi from "./../../api/socket";
import onlineParticipant from "./utils/onlineParticipants";
import mapCanvas from "./utils/mapCanvas";
import dataList from "./utils/dataList";
import toolContainer from "./utils/toolContainer";
import stepChange from "./utils/stepChange"

export default {
  components: {
    onlineParticipant,
    mapCanvas,
    dataList,
    toolContainer,
    stepChange
  },
  data() {
    return {
      subProjectInfo: {},
      projectInfo: {},
      contentHeight: 800,
      mapWidth: 0,
      stepId: this.$route.params.id,
      toSubProjectPage: "",
      stepContent: {},
      // online drawer
      drawerValue: false,
      modifyStep: false,
      stepForm: {
        name: "",
        description: ""
      },
      description: "",
      // 用户角色
      userRole: "Visitor"
    };
  },
  created() {
    this.init();
  },
  mounted() {
    window.addEventListener("resize", this.initSize);
  },
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      if (!vm.$store.getters.userState) {
        next("/login");
      } else {
        if (
          !(
            vm.userRole == "Manager" ||
            vm.userRole == "Member" ||
            vm.userRole == "PManager"
          )
        ) {
          vm.$Message.error("You have no property to access it");
          // next(`/project/${vm.$store.getters.currentProjectId}`);
          vm.$router.go(-1);
        } else {
          next();
        }
      }
    });
  },
  methods: {
    init() {
      this.initSize();
      this.getDataProcessing();
      this.getSubprojectInfo();
      this.getProjectInfo();
      this.userRoleIdentity();
    },
    initSize() {
      if (window.innerHeight > 675) {
        this.contentHeight = window.innerHeight - 245;
      } else {
        this.contentHeight = 675 - 245;
      }

      this.mapWidth = window.innerWidth - 700 - 40;
    },
    getSubprojectInfo() {
      let subProjectInfo = this.$store.getters.subProject;
      if (
        JSON.stringify(subProjectInfo) != "{}" &&
        subProjectInfo.subProjectId == sessionStorage.getItem("subProjectId")
      ) {
        this.$set(this, "subProjectInfo", subProjectInfo);
      } else {
        $.ajax({
          url:
            "/GeoProblemSolving/subProject/inquiry" +
            "?key=subProjectId" +
            "&value=" +
            this.stepContent.subProjectId,
          type: "GET",
          async: false,
          success: data => {
            if (data == "Offline") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else if (data != "None" && data != "Fail") {
              subProjectInfo = data[0];
              this.$set(this, "subProjectInfo", subProjectInfo);

              this.$store.commit("setSubProjectInfo", subProjectInfo);
            } else {
              console.log(data);
            }
          },
          error: function(err) {
            console.log("Get manager name fail.");
          }
        });
      }
    },
    getProjectInfo() {
      let projectInfo = this.$store.getters.project;
      if (
        JSON.stringify(projectInfo) != "{}" &&
        projectInfo.projectId == this.subProjectInfo.projectId
      ) {
        this.projectInfo = projectInfo;
      } else {
        $.ajax({
          url:
            "/GeoProblemSolving/project/inquiry" +
            "?key=projectId" +
            "&value=" +
            this.subProjectInfo.projectId,
          type: "GET",
          async: false,
          success: data => {
            if (data != "None" && data != "Fail") {
              this.projectInfo = data[0];
              this.$store.commit("setProjectInfo", data[0]);
            } else {
              console.log(data);
            }
          }
        });
      }
    },
    userRoleIdentity() {
      this.userRole = "Visitor";
      if (this.$store.getters.userState) {
        // 是否是子项目管理员
        if (this.subProjectInfo.managerId === this.$store.getters.userId) {
          this.userRole = "Manager";
        }
        // 是否是子项目成员
        else {
          for (let i = 0; i < this.subProjectInfo.members.length; i++) {
            if (
              this.subProjectInfo.members[i].userId ===
              this.$store.getters.userId
            ) {
              this.userRole = "Member";
              break;
            }
          }
        }
        // 是否是项目管理员
        if (this.userRole != "Manager") {
          if (this.projectInfo.managerId === this.$store.getters.userId) {
            this.userRole = "PManager";
          }
        }
      } else {
        this.userRole = "Visitor";
      }
    },
    getDataProcessing() {
      if (
        this.stepContent.stepId == "" ||
        this.stepContent.stepId == undefined
      ) {
        $.ajax({
          url:
            "/GeoProblemSolving/step/inquiry/" +
            "?key=stepId" +
            "&value=" +
            this.stepId,
          type: "GET",
          async: false,
          success: data => {
            if (data != "Fail") {
              this.stepContent = data[0];
              this.stepForm = data[0];
              this.toSubProjectPage =
                "/project/" + data[0].subProjectId + "/subproject";
            } else {
              this.$Notice.info({
                desc: "Get the description failed!"
              });
            }
          }
        });
      } else {
        this.$Notice.info({
          desc: "Get step information failed!"
        });
      }
    },
    submitModifyStep() {
      let obj = new URLSearchParams();
      obj.append("name", this.stepForm.name);
      obj.append("description", this.stepForm.description);
      obj.append("stepId", this.stepId);

      this.axios
        .post("/GeoProblemSolving/step/update", obj)
        .then(res => {
          console.log(res.data);
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({
              name: "Login"
            });
          } else if (res.data != "Fail") {
            this.$Notice.info({
              desc: "Update successfully!"
            });
            this.stepContent.name = this.stepForm.name;
            this.stepContent.description = this.stepForm.description;
          } else {
            this.$Message.error("Update step failed.");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
      this.modifyStep = false;
    },
    cancelModifyStep() {
      this.modifyStep = false;
    }
  }
};
</script>

  


