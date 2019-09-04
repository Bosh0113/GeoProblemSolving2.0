<template>
  <div class="projectContent">
    <div class="projectInfo">
      <basic-info v-bind="basicInfo"></basic-info>
      <div class="constrainsContent">
        <div class="resourceContent">
          <span class="infoName">Resource:</span>
        </div>
        <div class="constrainsInfo">
          <constrains-info></constrains-info>
        </div>
      </div>

      <Content :style="{minHeight: '280px', background: '#fff'}">
        <Layout>
          <Sider hide-trigger :style="{background: '#fff'}">
            <Menu @on-select="showTab($event)" theme="light" active-name="Models" :open-names="['Models']" width="150px"
              :style="{'z-index':1}">
              <MenuItem name="Models">
              <Icon type="md-planet" />Models</MenuItem>
              <MenuItem name="Instances">
              <Icon type="ios-skip-forward" />Instances</MenuItem>
            </Menu>
          </Sider>
          <Content :style="{ minHeight: '280px', background: '#fff'}">
            <div v-show="currentTab=='Models'">
              <div class="comparisonContent" v-show="projectInfo!=={}">
                <div v-if="modelList.length>0">

                  <div class="cmpModelBox">
                    <Row>
                      <Col :xs="{ span: 21, offset: 1 }" :md="{ span: 11, offset: 1 }" :lg="{ span: 6 }">
                      <div @click="createCmpModel">
                        <Card style="height:150px;margin:10px -15px">
                          <div style="display:flex; justify-content: center;  height: 120px; align-items: center;">
                            <img style="width:70px" src="@/assets/images/comparison/add.png"
                              alt="add comparison subproject">
                          </div>
                        </Card>
                      </div>
                      </Col>

                      <Col :xs="{ span: 21, offset: 1 }" :md="{ span: 11, offset: 1 }" :lg="{ span: 6 }"
                        v-for="model of modelList" :key="model.oid">
                      <Card style="height:150px;margin:10px -15px">
                        <div>
                          <div class="cmpItemTitle">
                            <a href="#" @click.prevent="modelDetail(model)">{{model.modelName}}</a>
                          </div>
                          <p class="cmpItemDesc">{{model.description}}</p>
                          <div id="bottom-info">
                            <div class="info">
                              <Icon type="md-body" :size="15" />
                              <span style="margin-left:10px; color:#2b85e4">{{model.ownerName}}</span>
                            </div>
                            <div class="info">
                              <Icon type="md-clock" :size="15" />
                              <span style="margin-left:10px">{{getCreatedTime(model)}}</span>
                            </div>
                          </div>
                        </div>
                      </Card>
                      </Col>
                    </Row>
                  </div>
                </div>
                <blank-box v-else v-bind="blankInfo" v-on:linkClicked="onLinkClick"></blank-box>
              </div>
            </div>
            <div v-show="currentTab=='Instances'">
              ASDasdASD
            </div>
          </Content>

        </Layout>
      </Content>

    </div>
  </div>
</template>
<script>
import BlankBox from "@/components/comparison/BlankBox";
import BasicInfo from "@/components/comparison/ProjectBasicInfo";
import ConstrainsInfo from "@/components/comparison/ConstrainsInfo";
export default {
  name: "specific-project",
  components: {
    "blank-box": BlankBox,
    "basic-info": BasicInfo,
    "constrains-info": ConstrainsInfo
  },
  created: function() {
    //* 获取路由信息
    this.parentInfo = this.$route.params.parentProject;
    // console.log("parent: ",this.parentInfo);
    //* 获取项目信息
    this.getProjectInfo();
  },
  data() {
    return {
      // parentInfo:{},
      projectInfo: {},
      blankInfo: {
        welcomeTitle: "Welcome to Comparison!",
        welcomeInfo:
          "Comprehensive comparison of simulation capabilities from multiple perspectives improving our knowledge and understanding of models. To get started, you should",
        linkInfo: "create an comparison task."
      },
      modelList: [],
      currentTab: "Models"
    };
  },
  computed: {
    basicInfo() {
      let projectTitle = "";
      let subprojectTitle = "";
      if (this.parentInfo == null) {
        projectTitle = this.projectInfo.title || "";
      } else {
        projectTitle = this.parentInfo.title;
        subprojectTitle = this.projectInfo.title;
      }
      return {
        projectTitle: projectTitle,
        subprojectTitle: subprojectTitle,
        description: this.projectInfo.description || "",
        rules: this.projectInfo.evaluationRules || "",
        managerName: this.projectInfo.managerName || "",
        members: this.projectInfo.members || [],
        createTime: this.projectInfo.createTime || ""
      };
    },
    getCreatedTime(){
      return function(model) {
        return model.createTime ? model.createTime.split(" ")[0] : "";
      };
    }
  },
  methods: {
    getProjectInfo() {
      //* 请求后台数据
      let projectId = this.$route.params.id;
      this.$api.cmp_project
        .getProjectAllInfo(projectId)
        .then(res => {
          console.log(res);
          this.projectInfo = res.project;
          this.modelList = res.model;
        })
        .catch(err => {
          this.$Message.error(err);
        });

      // this.$api.cmp_project
      //   .getProject("projectId", projectId)
      //   .then(res => {
      //     console.log(res);
      //     this.projectInfo = res[0];
      //     //todo 请求其他数据：模型，输入数据，输出数据，子项目数据
      //   })
      //   .catch(err => {
      //     this.$Message.error(err);
      //   });
    },
    onLinkClick() {
      console.log("11");
      if (!this.$store.getters.userState) {
        this.$router.push({ name: "Login" });
      } else {
        //* 上传模型

        this.$router.push({
          path: `/create-cmp-model/${this.projectInfo.projectId}`
        });
      }
    },
    showTab($event) {
      this.currentTab = $event;
    },
    createCmpModel(){
      this.onLinkClick();
    },
    modelDetail(model){
      this.$router.push({
        path: `/cmp-model`,
        name: "cmp-model-detail",
        params: {
          id: model.oid,
          project: this.projectInfo
        }
      });
    },

    //todo 1.基本信息编辑功能
    //todo 2.模型信息编辑功能
    //todo 3.数据信息编辑功能
    //todo 4.资源
  }
};
</script>
<style scoped>
.projectContent {
  display: flex;
  flex-wrap: wrap;
  /* justify-content: center; */
  padding: auto;
  background-color: #fff;
}

.projectInfo {
  /* border-bottom: 1px solid #d1d5da; */
  border-top: 1px solid #d1d5da;
  padding-left: 20px;
  padding-right: 20px;
  width: 1200px;
  margin-left: auto;
  margin-right: auto;
  /* margin-top: 10px; */
  background-color: #fff;
}

.basicInfo {
  border-bottom: 1px solid #d1d5da;
}

.cmpTitle {
  color: #0366d6;
  text-decoration: none;
  font-weight: 600;
  margin-top: 10px;
  margin-bottom: 10px;
  font-size: 20px;
}

.infoName {
  color: #0366d6;
  font-size: 16px;
}

.basicInfo p {
  margin-left: 20px;
  margin-top: 5px;
  margin-bottom: 5px;
  font-size: 16px;
}

.members {
  display: flex;
  width: 100%;
  flex-wrap: wrap;
  margin-top: 5px;
}

.member {
  font-size: 12px;
  border: 1px solid #d1d5da;
  border-radius: 8px;
  padding: 3px;
  margin-right: 5px;
  margin-left: 5px;
  margin-bottom: 5px;
}

.constrainsContent {
  display: flex;
  margin-top: 10px;
  margin-bottom: 10px;
  border-bottom: 1px solid #d1d5da;
}

.resourceContent {
  flex: 3 0 auto;
  /* flex: auto; */
  border-right: 1px solid #d1d5da;
  margin-top: 5px;
  margin-bottom: 5px;
  margin-right: 10px;
}

.constrainsInfo {
  flex: 1 0 auto;
}

.box {
  background-color: #fff;
  border: 1px solid #d1d5da;
  border-radius: 3px;
}

.blankslate {
  padding: 80px 40px;
  background-color: #fafbfc;
  border-radius: 3px;
  box-shadow: inset 0 0 10px rgba(27, 31, 35, 0.05);
  text-align: center;
}

.blankslate-icon {
  color: #a3aab1;
  margin-bottom: 8px;
  margin-left: 4px;
  margin-right: 4px;
  vertical-align: text-bottom;
  display: inline-block;
  fill: currentColor;
}

.blankslate h3 {
  font-size: 20px;
  margin: 16px 0;
  font-weight: 600;
}

.blankslate a {
  color: #0366d6;
  text-decoration: none;
}

.blankslate p {
  font-size: 16px;
  margin: 0px 50px;
}

.comparisonContent {
  margin-bottom: 10px;
}

.createTime {
  display: flex;
  flex-direction: row-reverse;
}

.createTime span {
  color: #586069;
  font-size: 14px;
  line-height: 20px;
}


.cmpItemTitle {
  /* height: 3em; */
  overflow: hidden;
  /* text-overflow: ellipsis; */
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
}
.cmpItemTitle a {
  color: #2d8cf0;
  text-decoration: none;
  font-weight: 600;
  font-size: 16px;
  line-height: 1em;
}

.cmpItemDesc {
  font-size: 14px;
  height: 80px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
}

#bottom-info {
  display: flex;
  margin-top: 5px;
  justify-content: space-between;
}

.info {
  display: flex;
  align-items: center;
  margin-right: 10px;
}
</style>