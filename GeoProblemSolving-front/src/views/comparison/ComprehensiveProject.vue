<template>
  <div class="projectContent">
    <div class="projectInfo">
      <div class="basicInfo">
        <h3 class="cmpTitle">{{projectInfo.title}}</h3>
        <span class="infoName">Description:</span>
        <p>{{projectInfo.description}}</p>
        <div class="members">
          <span class="infoName">Members:</span>
          <span class="member">{{projectInfo.managerName}}</span>
          <span
            class="member"
            v-for="member of projectInfo.members"
          >{{member}}</span>
        </div>
        <div class="createTime">
          <span>{{getCreatedTime(projectInfo)}}</span><span>Created Time： </span>
        </div>
      </div>

      <div class="comparisonContent">
        <div class="box">
            <div class="blankslate">
                <svg height="40" class="blankslate-icon" viewBox="0 0 14 16" version="1.1" width="35"
                    aria-hidden="true">
                    <path fill-rule="evenodd"
                        d="M7 2.3c3.14 0 5.7 2.56 5.7 5.7s-2.56 5.7-5.7 5.7A5.71 5.71 0 0 1 1.3 8c0-3.14 2.56-5.7 5.7-5.7zM7 1C3.14 1 0 4.14 0 8s3.14 7 7 7 7-3.14 7-7-3.14-7-7-7zm1 3H6v5h2V4zm0 6H6v2h2v-2z">
                    </path>
                </svg>
                <h3>Welcome to Comparison!</h3>
                <p>
                    Comprehensive comparison of simulation capabilities from multiple perspectives improving our
                    knowledge and understanding of models. To get started, you should
                    <a href="#" class="">create an comparison item.</a>
                </p>
            </div>
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
  computed:{
    getCreatedTime(){
      return function(project){
        return project.createTime?project.createTime.split(' ')[0]:"";
      }
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

.constrain_tb_name {
  width: 45%;
  font-size: 14px;
}

.constrain_tb_value {
  width: 50%;
  font-size: 14px;
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
    box-shadow: inset 0 0 10px rgba(27, 31, 35, .05);
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

.comparisonContent{
    margin-bottom: 10px;
    margin-top: 10px;
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