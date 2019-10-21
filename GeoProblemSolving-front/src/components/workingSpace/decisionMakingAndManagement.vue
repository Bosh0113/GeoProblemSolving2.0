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
      <div class="picscreen">
        <div class="picbg"></div>
        <div class="homeContent">
          <Row>
            <!-- 需要修改样式 -->
            <Col span="6" style="height:40px;">
              <Breadcrumb>
                <!-- <BreadcrumbItem :to="toProjectPage">Project</BreadcrumbItem> -->
                <BreadcrumbItem :to="toSubProjectPage"  class="subproject-back">Subproject</BreadcrumbItem>
                <BreadcrumbItem style="color:white">Decision-making and Management</BreadcrumbItem>
              </Breadcrumb>
            </Col>
            <Col
              span="12"
              style="text-align:center;font-size:1.5rem;height:20px;color:white;margin-top:1%"
            >
              <strong>{{stepInfo.name}}</strong>
              <p style="font-size:0.8rem;margin-top:5px;">{{stepInfo.description}}</p>
            </Col>
          </Row>
        </div>
      </div>
    </Row>
    <div style="margin-top:50px;padding:15px">
      <Row>
        <Col span="24">
          <div style="margin-left:15px">
            <Card shadow>
              <div :style="{height:sidebarHeight+1+'px'}">
                <Tabs value="decision">
                  <TabPane label="Decision-making" name="decision" icon="md-paper">
                    <div :style="{height:sidebarHeight- 50 +'px'}">
                      <vue-scroll :ops="ops">
                        <Col span="8" v-for="(tool,index) in decisionToolSet" :key="index">
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
                  <TabPane label="Management" name="management" icon="md-checkmark-circle-outline">
                    <div :style="{height:sidebarHeight- 50 +'px'}">
                      <vue-scroll :ops="ops">
                        <Col span="8" v-for="(tool,index) in managementToolSet" :key="index">
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
export default {
  mounted() {
    this.initSize();
    this.userRoleIdentity();
    this.getStepInfo();
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
      toSubProjectPage: "",
      stepInfo: {
        name: ""
      },
      sidebarHeight: 800,
      decisionToolSet: [
        {
          img: "http://www.saga-gis.org/_images/logo_saga_big.png",
          url: "http://geomodeling.njnu.edu.cn/Online_Saga/saga-tools",
          title: "Online-SAGA",
          description:
            "SAGA stands for System for Automated Geoscientific Analyses. SAGA GIS has been developed by a small group of developers primarily based in Germany. Most past and current SAGA developments come from the team around J. Böhner and O. Conrad, both are now working at the Institute of Geography, Section for Physical Geography, Klimacampus and University of Hamburg, Germany."
        }
      ],
      managementToolSet: [
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
      } else {
        this.sidebarHeight = 385;
      }
    },
    getStepInfo() {
      this.axios
        .get(
          "/GeoProblemSolving/step/inquiry" +
            "?key=" +
            "stepId" +
            "&value=" +
            this.stepId
        )
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data != "Fail" && res.data != "None") {
            this.stepInfo = res.data[0];
            this.toSubProjectPage = "/project/" + res.data[0].subProjectId + "/subproject";
          } else {
            this.$Message.warning("Get step info fail.");
          }
        })
        .catch();
    },
    enterTheme(url) {
      window.open(url);
    },
    userRoleIdentity() {
      this.userRole = "Visitor";
      // console.log(this.stepContent.creator);
      // console.log(this.$store.getters.userId);
      let creatorId = sessionStorage.getItem("subProjectManagerId");
      console.log(creatorId);
      if (this.$store.getters.userState) {
        // 是否是子项目管理员
        if (creatorId === this.$store.getters.userId) {
          this.userRole = "Manager";
          console.log(this.userRole);
        } else {
          this.userRole = "Visitor";
        }
      }
    },
  }
};
</script>
