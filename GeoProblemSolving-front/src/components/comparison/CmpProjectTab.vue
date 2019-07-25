<template>
  <Row>
    <Col :xs="{ span: 21, offset: 1 }" :md="{ span: 11, offset: 1 }" :lg="{ span: 6 }" v-for="project of projectShowList" :key="project.projectId">
      <Card style="height:180px;margin:10px -15px">
        <div>
          <div class="cmpTitle">
            <a href="#" @click.prevent="projectDetail(project)">{{project.title}}</a>
          </div>

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
      if(projectInfo.projectType=="COMPREHENSIVE"){
        this.$router.push({path:`/cmp-project/comprehensive/${projectInfo.projectId}`})
      }else{
        this.$router.push({path:`/cmp-project/specific/${projectInfo.projectId}`})
      }

      // console.log("跳转项目详情");
    }
  }
};
</script>

<style scoped>

.cmpTitle{
    height: 3em;
    overflow: hidden;
    /* text-overflow: ellipsis; */
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
}
.cmpTitle a {
  color: #0366d6;
  text-decoration: none;
  font-weight: 600;
  font-size: 18px;
  line-height: 1em;
}

.cmpDesc{
  font-size: 14px;
  height: 80px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
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

