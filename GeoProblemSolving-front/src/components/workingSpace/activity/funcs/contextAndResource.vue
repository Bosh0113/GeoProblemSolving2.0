<style scoped>
</style>
<template>
  <div>
    <Row>
      <Col span="14">
        <chat-panel></chat-panel>
      </Col>
      <Col span="10">
        <Tabs type="card">
          <TabPane label="Tools"><tool-box></tool-box></TabPane>
          <TabPane label="Resources"><res-list></res-list></TabPane>
        </Tabs>
      </Col>
    </Row>
  </div>
</template>
<script>
import chatPanel from "../utils/chatPanel.vue";
import resList from "../utils/resList.vue";
import toolBox from "../utils/toolBox.vue";
export default {
  props: ["activityInfo"],
  components: {
    chatPanel,
    resList,
    toolBox,
  },
  data() {
    return {};
  },
  mounted() {},
  beforeDestroy() {},
  watch: {},
  methods: {},
};
</script>
