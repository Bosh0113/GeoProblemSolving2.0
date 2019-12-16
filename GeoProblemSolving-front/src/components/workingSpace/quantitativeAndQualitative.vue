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
.homeContent >>> .ivu-breadcrumb {
  color: rgb(180, 197, 207);
}
.homeContent >>> .ivu-breadcrumb > span:last-child {
  color: rgb(180, 197, 207);
}
.imgBox {
  position: relative;
  overflow: hidden;
  padding-bottom: 100%; /*重要属性*/
  /* outline: 1px solid #dcdee2; */
}
.imgBox img {
  width: 100%;
  position: absolute;
}
.itemDesc {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 4;
  overflow: hidden;
}
.fileBtnHoverGreen:hover {
  background-color: #19be6b;
  color: white;
}
.subproject-back >>>.ivu-breadcrumb-item-link{
  color:white;
}
</style>
<template>
  <div style="background-color:#e8eaec;height:auto">
    <Row>
      <step-header :stepInfo="stepInfo" :subProjectInfo="subProjectInfo" :userRole="userRole" :key="infoRefresh"></step-header>
    </Row>
    <div style="margin-top:50px;padding:10px">
      <Row>
        <Col span="7" :style="{height:sidebarHeight+40+'px'}">
          <Card shadow>
              <div class="condefTitle">
                <div
                  style="width:3px;height:18px;float:left;background-color:rgb(124, 126, 126)"
                ></div>
                  <data-list :stepInfo="stepInfo" :contentHeight="contentHeight" :userRole="userRole" :key="infoRefresh"></data-list>
              </div>
          </Card>
        </Col>
        <Col span="17">
          <div style="margin-left:15px">
            <Card shadow>
              <div :style="{height:sidebarHeight+1+'px'}">
                <Tabs value="qualitative">
                  <TabPane label="Qualitative analysis" name="qualitative" icon="md-done-all">
                    <div :style="{height:sidebarHeight- 50 +'px'}">
                      <vue-scroll :ops="ops">
                        <Col span="8" v-for="(tool,index) in quantitativeToolSet" :key="index">
                          <Card dis-hover style="min-width:200px;">
                            <h3 slot="title">{{tool.title}}</h3>
                            <div
                              style="width:60%;display:inline-block;cursor: pointer;"
                              @click="enterTheme(tool.url)"
                            >
                              <div class="imgBox">
                                <img :src="tool.img" />
                              </div>
                            </div>
                            <Button
                              class="fileBtnHoverGreen"
                              shape="circle"
                              icon="md-exit"
                              style="margin: -150px 0 0 25px;"
                              size="large"
                              @click="enterTheme(tool.url)"
                            ></Button>
                            <div style="height:80px;">
                              <span class="itemDesc" :title="tool.description">{{tool.description}}</span>
                            </div>
                          </Card>
                        </Col>
                      </vue-scroll>
                    </div>
                  </TabPane>
                  <TabPane label="Quantitative analysis" name="quantitative" icon="ios-flask">
                    <div :style="{height:sidebarHeight- 50 +'px'}">
                      <vue-scroll :ops="ops">
                        <Col span="8" v-for="(tool,index) in qualitativeToolSet" :key="index">
                          <Card dis-hover style="min-width:200px;">
                            <h3 slot="title">{{tool.title}}</h3>
                            <div
                              style="width:60%;display:inline-block;cursor: pointer;"
                              @click="enterTheme(tool.url)"
                            >
                              <div class="imgBox">
                                <img :src="tool.img" />
                              </div>
                            </div>
                            <Button
                              class="fileBtnHoverGreen"
                              shape="circle"
                              icon="md-exit"
                              style="margin: -150px 0 0 25px;"
                              size="large"
                              @click="enterTheme(tool.url)"
                            ></Button>
                            <div style="height:80px;">
                              <span class="itemDesc" :title="tool.description">{{tool.description}}</span>
                            </div>
                          </Card>
                        </Col>
                      </vue-scroll>
                    </div>
                  </TabPane>
                </Tabs>
              </div>
            </Card>
          </div>
        </Col>
      </Row>
    </div>
  </div>
</template>
<script>
import dataList from "./utils/dataList"
import stepHeader from "./utils/stepHeader"
export default {
  components: {
    dataList,
    stepHeader
  },
  created(){
    this.getStepInfo();
    this.getSubprojectInfo();
    this.getProjectInfo();
    this.userRoleIdentity();
  },
  mounted() {
    this.initSize();
    this.infoRefresh++;
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
  data() {
    return {
      stepId: this.$route.params.id,
      stepInfo: {
        name: ""
      },
      userRole:"Visitor",
      sidebarHeight: 800,
      contentHeight: 800,
      subProjectInfo:{},
      projectInfo:{},
      infoRefresh:0,
      quantitativeToolSet: [
        {
          img: "http://www.saga-gis.org/_images/logo_saga_big.png",
          url: "http://geomodeling.njnu.edu.cn/Online_Saga/saga-tools",
          title: "Online-SAGA",
          description:
            "SAGA stands for System for Automated Geoscientific Analyses. SAGA GIS has been developed by a small group of developers primarily based in Germany. Most past and current SAGA developments come from the team around J. Böhner and O. Conrad, both are now working at the Institute of Geography, Section for Physical Geography, Klimacampus and University of Hamburg, Germany."
        }
      ],
      qualitativeToolSet: [
        {
          img: "https://jupyter.org/assets/main-logo.svg",
          url: "http://134.175.111.77/note",
          title: "Jupyter",
          description:
            "Project Jupyter exists to develop open-source software, open-standards, and services for interactive computing across dozens of programming languages."
        }
      ],
      ops: {
        bar: {
          background: "#808695"
        }
      }
    };
  },
  methods: {
    initSize() {
      if (window.innerHeight > 675) {
        this.sidebarHeight = window.innerHeight - 290;
        this.contentHeight = window.innerHeight - 254;
      } else {
        this.sidebarHeight = 675 - 290;
        this.contentHeight = 675 - 254;
      }
    },
    getStepInfo() {
      $.ajax({
          url:
          "/GeoProblemSolving/step/inquiry" +
            "?key=" +
            "stepId" +
            "&value=" +
            this.stepId,
          type: "GET",
          async: false,
          success: data => {
            if (data == "Offline") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else if (data != "Fail" && data != "None") {
              this.stepInfo = data[0];
            } else {
              this.$Message.warning("Get step info fail.");
            }
          },
          error: function(err) {
            console.log("Get step info fail.");
          }
        });
    },
    enterTheme(url) {
      window.open(url);
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
            this.stepInfo.subProjectId,
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
  }
};
</script>
