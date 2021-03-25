<template>
  <div>
    <Collapse simple v-model="unfold">
      <Panel name="tool">
        Individual simulation 
         <Tabs slot="content" type="card" class="activityTab">
          <TabPane label="Tools">
            <tool-box :activityInfo="activityInfo"></tool-box>
          </TabPane>
          <TabPane label="Resources">
            <res-list :activityInfo="activityInfo"></res-list>
          </TabPane>
        </Tabs>
      </Panel>
      <Panel name="data">
        Integrated simulation
        
      </Panel>
    </Collapse>
  </div>
</template>
<script>
import resList from "../utils/resList.vue";
import toolBox from "../utils/toolBox.vue";
import actions from "../utils/actionContainer.vue";
export default {
  props: ["activityInfo"],
  components: {
    resList,
    toolBox,
  },
  data() {
    return {
      unfold: ["tool", "data"],
    };
  },
  mounted() {},
  methods: {
    changeMenuItem(name) {
      this.activeMenu = name;
    },
  },
};
</script>
<style scoped>
.tab >>> .ivu-tabs-bar {
  margin-bottom: 0px;
}
.activityTab >>> .ivu-tabs-bar {
  margin-bottom: 8px;
}
</style>
