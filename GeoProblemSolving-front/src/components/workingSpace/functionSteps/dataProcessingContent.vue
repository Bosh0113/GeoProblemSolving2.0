<style scoped>
</style>
<template>
  <div style="padding:15px">
    <p>Map</p>
    <Divider style="margin:10px 0" />
    <map-canvas :stepInfo="stepInfo" :userRole="userRole"></map-canvas>
    <Collapse simple v-model="unfold">
      <Panel name="tool">
        Toolbox
        <tool-container slot="content" :stepInfo="stepInfo" :userRole="userRole"></tool-container>
      </Panel>
      <Panel name="data">
        Data list
        <data-list slot="content" :stepInfo="stepInfo" :userRole="userRole"></data-list>
      </Panel>
    </Collapse>
    <message-panel></message-panel>
    <BackTop></BackTop>
  </div>
</template>
<script>
import mapCanvas from "./utils/mapCanvas";
import dataList from "./utils/dataList";
import toolContainer from "./utils/toolContainer";
import messagePanel from "./utils/messagePanel";
export default {
  props: ["stepInfo", "userRole"],
  components: {
    mapCanvas,
    dataList,
    toolContainer,
    messagePanel
  },
  mounted() {},
  data() {
    return {
      unfold: ["tool", "data"]
    };
  }
};
</script>
