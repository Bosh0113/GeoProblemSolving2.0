<template>
  <div>
    <Row>
      <Col span="16" style="border-right: 1px solid #d3d3d3;">
        <chat-panel
          :activityInfo="activityInfo"
          :participants="participants"
        ></chat-panel>
      </Col>
      <Col span="8">
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
import chatPanel from "../utils/chatroom/chatroom.vue";
// import chatPanel from "../utils/chatPanel.vue";
import resList from "../utils/resList.vue";
import toolBox from "../utils/toolBox.vue";
export default {
  props: ["activityInfo", "participants"],
  components: {
    chatPanel,
    resList,
    toolBox
  },
  data() {
    return {};
  },
  mounted() {},
  beforeDestroy() {},
  methods: {}
};
</script>
<style scoped>
.activityTab >>> .ivu-tabs-bar {
  margin-bottom: 8px;
}
</style>
