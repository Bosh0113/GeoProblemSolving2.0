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
      <div class="comparisonContent">
        <blank-box v-bind="blankInfo" v-on:linkClicked="onLinkClick"></blank-box>
      </div>
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
    "constrains-info":ConstrainsInfo,
  },
  created: function() {
    //* 获取项目信息
    this.getProjectInfo();
  },
  data() {
    return {
      projectInfo: {},
      blankInfo: {
        welcomeTitle: "Welcome to Comparison!",
        welcomeInfo:
          "Comprehensive comparison of simulation capabilities from multiple perspectives improving our knowledge and understanding of models. To get started, you should",
        linkInfo: "create an comparison task."
      }
    };
  },
  computed: {
    basicInfo() {
      return {
        projectTitle: this.projectInfo.title || "",
        description: this.projectInfo.description || "",
        rules: this.projectInfo.evaluationRules|| "",
        managerName: this.projectInfo.managerName|| "",
        members: this.projectInfo.members || [],
        createTime: this.projectInfo.createTime || ""
      };
    }
  },
  methods: {
    getProjectInfo() {
      this.projectInfo = this.$store.state.comparison.projectInfo;
      if (JSON.stringify(this.projectInfo) === "{}") {
        //* 请求后台数据
        let projectId = this.$route.params.id;
        this.$api.cmp_project
          .getProject("projectId", projectId)
          .then(res => {
            console.log(res);
            this.projectInfo = res[0];
            //todo 请求其他数据：模型，输入数据，输出数据，子项目数据
          })
          .catch(err => {
            this.$Message.error(err);
          });
      }
    },
    onLinkClick() {
      console.log("11");
      if (!this.$store.getters.userState) {
        this.$router.push({ name: "Login" });
      } else {
        this.$router.push({ name: "create-cmp-solution"  });
      }
    }
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


</style>