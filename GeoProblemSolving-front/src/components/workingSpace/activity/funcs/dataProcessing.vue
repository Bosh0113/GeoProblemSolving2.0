<template>
  <div>
    <Collapse simple v-model="unfold">
      <Panel name="tool">
        Toolbox
        <tool-box slot="content" :activityInfo="activityInfo"></tool-box>
      </Panel>
      <Panel name="data">
        Resource list
        <res-list slot="content" :activityInfo="activityInfo"></res-list>
      </Panel>
    </Collapse>
  </div>
</template>
<script>
import resList from "../utils/resList.vue";
import toolBox from "../utils/toolBox.vue";
import actions from "../utils/taskflow/actionContainer.vue";
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
