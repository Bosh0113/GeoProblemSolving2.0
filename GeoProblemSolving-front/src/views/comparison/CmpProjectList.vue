<template>
  <div class="listContent">
      <Divider orientation="left" dashed>
        <h1>
          Comparison Projects
        </h1>
      </Divider>
        <div>
          <Tabs value="All" type="card" @click="filterProjects">
            <TabPane label="All" name="All" icon="ios-list">
              <cmp-project-tab :projectShowList="projectShowList"></cmp-project-tab>
            </TabPane>
            <TabPane label="Terrestrial" name="Terrestrial" icon="md-globe">
            </TabPane>
            <TabPane label="Coastal" name="Coastal" icon="ios-boat">
            </TabPane>
            <TabPane label="Marine" name="Marine" icon="ios-water">
            </TabPane>
            <TabPane label="Climate" name="Climate" icon="ios-partly-sunny">
            </TabPane>
            <TabPane label="Ecological" name="Ecological" icon="ios-leaf">
            </TabPane>
            <TabPane label="Geological" name="Geological" icon="ios-analytics">
            </TabPane>
            <TabPane label="Human-Activity" name="Human" icon="ios-people">
            </TabPane>
            <TabPane label="GIS & RS" name="GISRS" icon="ios-globe">
            </TabPane>
            <TabPane label="General" name="General" icon="ios-grid">
            </TabPane>
            <Button
              @click="newProject"
              type="success"
              icon="md-add"
              slot="extra"
            >Create project</Button>
          </Tabs>
        </div>
  </div>

</template>

<script>
import CmpProjectTab from "@/components/comparison/CmpProjectTab"
export default {
  components:{
    CmpProjectTab
  },
  created: function(){
    this.$api.cmp_project.getAllProjects()
      .then(res=>{
        this.projectShowList = res;
      })
      .catch(err=>{
        this.$Message.error(err);
      })
  },
  data(){
    return {
      projectShowList:[
      ]
    }
  },
  methods:{
    filterProjects(){

    },
    newProject(){
      if (!this.$store.getters.userState) {
        this.$router.push({ name: "Login" });
      } else {
        this.$router.push({ name: "create-cmp-project"  });
      }
    }
  }

};
</script>

<style scoped>
h1{
  color: #17233d;
}

.listContent{
  margin-left: 5%;
  margin-right: 5%;
}

.btnCreate {
  font-size: 15px;
  height: 40px;
  margin: 10px;
}
.btnCreate:hover {
  background-color: #19be6b;
  color: white;
}

</style>