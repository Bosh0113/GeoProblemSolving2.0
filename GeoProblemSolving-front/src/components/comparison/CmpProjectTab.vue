<template>
  <Row>
    <Col :xs="{ span: 21, offset: 1 }" :md="{ span: 11, offset: 1 }" :lg="{ span: 6 }" v-for="project of projectShowList" :key="project.projectId">
      <Card style="height:auto;margin:10px -15px">
        <a
          href="#"
          class="cmpTitle"
        @click.prevent="projectDetail(project)">{{project.title}}</a>
        <p class="cmpDesc">{{project.description}}</p>
        <div id="bottom-info">
          <div class="info">
            <Icon type="md-body" :size="15" />Manager
            <span style="margin-left:10px; color:#2b85e4">{{project.managerName}}</span>
          </div>
          <div class="info">
            <Icon type="md-clock" :size="15" />Time
            <span style="margin-left:10px">{{getCreatedTime(project)}}</span>
          </div>
        </div>
      </Card>
    </Col>
  </Row>
</template>

<script>
export default {
  name:'cmp-project-tab',
  props:{
    projectShowList:{
      type:Array
    },
    projectType:{
      type:String
    }
  },
  computed:{
    getCreatedTime(){
      return function(project){
        return project.createTime.split(' ')[0];
      }
    }
  },
  methods:{
    projectDetail:function(projectInfo){
      // todo 做权限拦截
      this.$store.commit("comparison/xSet",{projectInfo:projectInfo});
      // console.log(this.$store.state.comparison.projectInfo);
      this.$router.push({path:`/cmp-project/${projectInfo.projectId}`})
      // console.log("跳转项目详情");
    }
  }
};
</script>

<style scoped>
.cmpTitle {
  color: #0366d6;
  text-decoration: none;
  font-weight: 600;
  font-size: 18px;
}

.cmpDesc{
  font-size: 14px;
}

#bottom-info{
  display: flex;
  margin-top: 5px;
  justify-content: space-between;
}

.info{
  display: flex;
  align-items: center;
  margin-right: 10px;
}

</style>

