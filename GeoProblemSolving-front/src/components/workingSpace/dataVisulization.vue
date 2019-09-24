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
                <BreadcrumbItem style="color:white">Data visualization and result representation</BreadcrumbItem>
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
                <Tabs value="chart">
                  <TabPane label="Chart & Table" name="chart" icon="md-pie">
                    <div :style="{height:sidebarHeight- 50 +'px'}">
                      <!-- <iframe :src="'/GeoProblemSolving/charts'" style="height:100%;width:100%;"></iframe> -->
                      <iframe :src="'/GeoProblemSolving/charts'" style="height:100%;width:100%;"></iframe>
                    </div>
                  </TabPane>
                  <TabPane label="Earth & Map" name="earth" icon="md-globe">
                    <div :style="{height:sidebarHeight- 50 +'px'}">
                      <iframe
                        :src="'/GeoProblemSolving/Collaborative/3DEarth/index.html'"
                        style="height:100%;width:100%;"
                      ></iframe>
                    </div>
                  </TabPane>
                  <TabPane label="3D Model" name="3dModel" icon="md-cube">
                    <div :style="{height:sidebarHeight- 50 +'px'}">
                      <iframe
                        :src="'/GeoProblemSolving/Collaborative/3DmodelViewer/index.html?groupID='+stepId"
                        style="height:100%;width:100%;"
                      ></iframe>
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
    this.getStepInfo();
    window.addEventListener("resize", this.initSize);
  },
  data() {
    return {
      stepId: this.$route.params.id,
      toSubProjectPage: "/project/" + this.$route.params.subid + "/subproject",
      stepInfo: {
        name: ""
      },
      sidebarHeight: 800
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
          } else {
            this.$Message.warning("Get step info fail.");
          }
        })
        .catch();
    }
  }
};
</script>
