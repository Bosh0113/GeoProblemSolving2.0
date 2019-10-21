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
                <BreadcrumbItem style="color:white">Simulation and prediction</BreadcrumbItem>
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
        <Col span="7" :style="{height:sidebarHeight+40+'px'}">
          <Card shadow>
            <p slot="title">
              <Icon type="ios-paper" />Data
            </p>
            <div :style="{height:sidebarHeight-50+'px'}">
              <h3>Data list</h3>
            </div>
          </Card>
        </Col>
        <Col span="17">
          <div style="margin-left:15px">
            <Card shadow>
              <div :style="{height:sidebarHeight+1+'px'}">
                <Tabs value="thematic">
                  <TabPane label="Thematic Models" name="thematic" icon="md-sunny">
                    <Row :gutter="16">
                      <div :style="{height:sidebarHeight-55 +'px'}">
                        <vue-scroll :ops="ops">
                          <Col span="8" v-for="(model,index) in themeModels" :key="index">
                            <Card dis-hover style="min-width:200px;">
                              <h3 slot="title">{{model.title}}</h3>
                              <div
                                style="width:60%;display:inline-block;cursor: pointer;"
                                @click="enterTheme(model.url)"
                              >
                                <div class="imgBox">
                                  <img :src="model.img" />
                                </div>
                              </div>
                              <Button
                                class="fileBtnHoverGreen"
                                shape="circle"
                                icon="md-exit"
                                style="margin: -150px 0 0 25px;"
                                size="large"
                                @click="enterTheme(model.url)"
                              ></Button>
                              <div style="height:80px;">
                                <span
                                  class="itemDesc"
                                  :title="model.description"
                                >{{model.description}}</span>
                              </div>
                            </Card>
                          </Col>
                        </vue-scroll>
                      </div>
                    </Row>
                  </TabPane>
                  <TabPane label="Customize Model" name="customize" icon="md-construct">
                    <h3 style="display:inline-block">Deploy your model:</h3>
                    <a
                      href="http://106.14.78.235:8060/index"
                      style="margin-left:10px"
                      target="_blank"
                    >Model Service Container</a>
                  </TabPane>
                  <TabPane label="Model Integration" name="integration" icon="md-analytics">
                    <Row>
                      <Col span="8">
                        <Card>
                          <h3 slot="title">Integration Tool</h3>
                          <div
                            @click="enterTheme('/GeoProblemSolving/Collaborative/LogicalModel/index.html?groupID='+stepId)"
                          >
                            <div class="imgBox" style="cursor: pointer;">
                              <img
                                src="http://94.191.49.160:8080/GeoProblemSolving/resource/subProjectUpload/integration379564.png"
                              />
                            </div>
                          </div>
                        </Card>
                      </Col>
                    </Row>
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
      toSubProjectPage: "",
      stepInfo: {
        name: ""
      },
      sidebarHeight: 800,
      themeModels: [
        {
          img: "http://www.saga-gis.org/_images/logo_saga_big.png",
          url: "http://geomodeling.njnu.edu.cn/Online_Saga/saga-tools",
          title: "Online-SAGA",
          description:
            "SAGA stands for System for Automated Geoscientific Analyses. SAGA GIS has been developed by a small group of developers primarily based in Germany. Most past and current SAGA developments come from the team around J. Böhner and O. Conrad, both are now working at the Institute of Geography, Section for Physical Geography, Klimacampus and University of Hamburg, Germany."
        },
        {
          img:
            "http://geomodeling.njnu.edu.cn/FGM-theme/images/model/forest.jpg",
          url: "http://geomodeling.njnu.edu.cn/FGM-theme/",
          title: "Forest Growth Models",
          description:
            "These models can predict the growth, mortality of individual trees in forest stand."
        },
        {
          img:
            "http://geomodeling.njnu.edu.cn/hydro-model-integration/images/model/water3.jpg",
          url: "http://geomodeling.njnu.edu.cn/hydro-model-integration/main",
          title: "Hydrological Theme",
          description:
            "Water is an important carrier of material movement and transformation in nature. The water cycle is an important material guarantee for the existence of life and economic and social development. Hydrological model is an important means to explore and understand water cycle and hydrological process, and also an effective tool to solve practical problems such as hydrological forecasting, water resources planning and management, hydrological analysis and calculation."
        },
        {
          img:
            "http://geomodeling.njnu.edu.cn/TrafficNoiseTheme/img/model/traffic.jpg",
          url: "http://geomodeling.njnu.edu.cn/TrafficNoiseTheme/",
          title: "Traffic Noise",
          description:
            "Traffic noise pollution problem is increasingly emerging with the rapid development of urban traffic.Researchers have paid close attention to the health effects of traffic noise. "
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
