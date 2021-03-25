<template>
  <div>
    <Row>
      <Col span="14" style="border-right: 1px solid #d3d3d3;"><visual-canvas></visual-canvas></Col>
      <Col span="10">
        <Tabs type="card" class="activityTab">
          <TabPane label="Tools">
            <tool-box :activityInfo="activityInfo"></tool-box>
          </TabPane>
          <TabPane label="Resources">
            <res-list :activityInfo="activityInfo"></res-list>
          </TabPane>
        </Tabs>
      </Col>
    </Row>
  </div>
</template>
<script>
import visualCanvas from "../utils/mapCanvas.vue";
import resList from "../utils/resList.vue";
import toolBox from "../utils/toolBox.vue";
import actions from "../utils/actionContainer.vue";
export default {
  props: ["activityInfo"],
  components: {
    visualCanvas,
    resList,
    toolBox,
  },
  data() {
    return {};
  },
  mounted() {},
  beforeDestroy() {},
  methods: {},
};
</script>
<style scoped>
.activityTab >>> .ivu-tabs-bar {
  margin-bottom: 8px;
}
</style>

