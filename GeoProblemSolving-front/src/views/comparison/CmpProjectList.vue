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
  data(){
    return {
      projectShowList:[
        {
          title:"陆地碳循环模型对比",
          description:"模型对比评价是地球系统碳循环模型和陆地碳循环模型研究的一个热点，据统计，在EMDI对比项目中，有12个陆地碳循环模型参与模拟和对比，在LBA-DMIP中，有21个陆地碳循环模型参与模拟和对比。但是，陆地碳循环模型的对比仍然面临着诸多挑战。希望通过此项目进行模型的公开化‘可重用的对比。"
        },{
          title:"陆地碳循环模型对比",
          description:"模型对比评价是地球系统碳循环模型和陆地碳循环模型研究的一个热点，据统计，在EMDI对比项目中，有12个陆地碳循环模型参与模拟和对比，在LBA-DMIP中，有21个陆地碳循环模型参与模拟和对比。但是，陆地碳循环模型的对比仍然面临着诸多挑战。希望通过此项目进行模型的公开化‘可重用的对比。"
        },
        {
          title:"陆地碳循环模型对比",
          description:"模型对比评价是地球系统碳循环模型和陆地碳循环模型研究的一个热点，据统计，在EMDI对比项目中，有12个陆地碳循环模型参与模拟和对比，在LBA-DMIP中，有21个陆地碳循环模型参与模拟和对比。但是，陆地碳循环模型的对比仍然面临着诸多挑战。希望通过此项目进行模型的公开化‘可重用的对比。"
        },
        {
          title:"陆地碳循环模型对比",
          description:"模型对比评价是地球系统碳循环模型和陆地碳循环模型研究的一个热点，据统计，在EMDI对比项目中，有12个陆地碳循环模型参与模拟和对比，在LBA-DMIP中，有21个陆地碳循环模型参与模拟和对比。但是，陆地碳循环模型的对比仍然面临着诸多挑战。希望通过此项目进行模型的公开化‘可重用的对比。"
        },
        {
          title:"陆地碳循环模型对比",
          description:"模型对比评价是地球系统碳循环模型和陆地碳循环模型研究的一个热点，据统计，在EMDI对比项目中，有12个陆地碳循环模型参与模拟和对比，在LBA-DMIP中，有21个陆地碳循环模型参与模拟和对比。但是，陆地碳循环模型的对比仍然面临着诸多挑战。希望通过此项目进行模型的公开化‘可重用的对比。"
        },
        {
          title:"陆地碳循环模型对比",
          description:"模型对比评价是地球系统碳循环模型和陆地碳循环模型研究的一个热点，据统计，在EMDI对比项目中，有12个陆地碳循环模型参与模拟和对比，在LBA-DMIP中，有21个陆地碳循环模型参与模拟和对比。但是，陆地碳循环模型的对比仍然面临着诸多挑战。希望通过此项目进行模型的公开化‘可重用的对比。"
        }
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