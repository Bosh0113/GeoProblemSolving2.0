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
  name: "component_name",
  components: {
    "blank-box": BlankBox,
    "basic-info": BasicInfo,
    "constrains-info": ConstrainsInfo
  },
  created: function() {
    //*获取 item 信息
    this.getItemInfo();
  },
  data() {
    return {
      itemInfo: {},
      projectInfo:{},
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
        cmpItemTitle: this.itemInfo.title || "",
        description: this.itemInfo.description || "",
        rules: this.itemInfo.evaluationRules || "",
        managerName: this.itemInfo.managerName || "",
        members: this.itemInfo.members || [],
        createTime: this.itemInfo.createTime || ""
      };
    }
  },
  methods: {
    getItemInfo() {
      let cmpItemId = this.$route.params.id;
      this.$api.cmp_item
        .getCmpItem("itemId", cmpItemId)
        .then(res => {
          console.log(res);
          this.itemInfo = res[0];
          this.getProjectInfo(this.itemInfo.projectId);
        })
        .catch(err => {
          this.$Message.error(err);
        });
    },
    getProjectInfo(projectId) {
      //* 请求后台数据
      this.$api.cmp_project
        .getProject("projectId", projectId)
        .then(res => {
          console.log(res);
          this.projectInfo = res[0];
        })
        .catch(err => {
          this.$Message.error(err);
        });
    },
    onLinkClick() {
      console.log("11");
    }
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

.infoName {
  color: #0366d6;
  font-size: 16px;
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