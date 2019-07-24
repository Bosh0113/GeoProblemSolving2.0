<template>
  <div class="projectContent">
    <div class="projectInfo">
      <div class="basicInfo">
        <h3 class="cmpTitle">{{projectInfo.title}}</h3>
        <span class="infoName">Description:</span>
        <p>{{projectInfo.description}}</p>
        <span class="infoName">Evaluation Rules:</span>
        <p>{{projectInfo.evaluationRules}}</p>
        <span class="infoName">Members:</span>
        <div class="members">
          <span class="member">{{projectInfo.managerName}}</span>
          <span
            class="member"
            v-for="member of projectInfo.members"
          >{{member}}</span>
        </div>
      </div>
      <div class="constrainsContent">
        <div class="resourceContent">
          <span class="infoName">Resource:</span>
        </div>
        <div class="constrainsInfo">
          <Tabs
            size="default"
            :animated="false"
          >
            <TabPane label="Data Info">
              数据信息
            </TabPane>
            <TabPane label="Other Info">
              其他信息
            </TabPane>
          </Tabs>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "component_name",
  created: function() {
    //* 获取项目信息
    this.getProjectInfo();
  },
  data() {
    return {
      animated: false,
      projectInfo: {}
    };
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
          })
          .catch(err => {
            this.$Message.error(err);
          });
      }
      //todo 1. 获取子项目信息
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
  border-bottom: 1px solid #d1d5da;
  border-top: 1px solid #d1d5da;
  padding-left: 20px;
  padding-right: 20px;
  width: 1400px;
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
  margin-top: 10px;
  margin-left: 20px;
}

.member {
  font-size: 14px;
  border: 1px solid #d1d5da;
  border-radius: 8px;
  padding: 3px;
  margin-right: 5px;
  margin-bottom: 5px;
}

.constrainsContent {
  display: flex;
  margin-top: 10px;
  margin-bottom: 10px;
  border-bottom: 1px solid #d1d5da;
}

.resourceContent {
  flex: 2 0 auto;
  /* flex: auto; */
  border-right: 1px solid #d1d5da;
  margin-top: 5px;
  margin-bottom: 5px;
  margin-right: 10px;
}

.constrainsInfo {
  flex: 1 0 auto;
}
</style>