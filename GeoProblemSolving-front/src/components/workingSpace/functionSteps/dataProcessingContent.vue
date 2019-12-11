<style scoped>
</style>
<template>
  <div style="padding:15px">
    <Collapse simple v-model="unfold">
      <Panel name="data">
        Data list
        <data-list slot="content" :stepInfo="stepInfo" :userRole="userRole"></data-list>
      </Panel>
      <Panel name="tool">
        Toolbox
        <tool-container slot="content" :stepInfo="stepInfo" :userRole="userRole"></tool-container>
      </Panel>
    </Collapse>
    <p style="margin: 10px 0">Map</p>
    <Divider style="margin:10px 0" />
    <map-canvas :stepInfo="stepInfo" :userRole="userRole"></map-canvas>
    <message-panel :stepInfo="stepInfo"></message-panel>
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
