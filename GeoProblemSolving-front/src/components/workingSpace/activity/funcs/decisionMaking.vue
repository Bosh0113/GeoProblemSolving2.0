<template>
  <div>
    <Row>
      <Col span="14" style="border-right: 1px solid #d3d3d3;">
        <chat-panel :activityInfo="activityInfo" :participants="participants"></chat-panel>
      </Col>
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
import chatPanel from "../utils/chatPanel.vue";
import resList from "../utils/resList.vue";
import toolBox from "../utils/toolBox.vue";
import actionContainer from "../utils/taskflow/actionContainer.vue";
export default {
  props: ["activityInfo", "participants"],
  components: {
    chatPanel,
    resList,
    toolBox,
    actionContainer
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

