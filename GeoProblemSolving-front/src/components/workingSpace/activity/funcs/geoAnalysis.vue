<template>
  <div>
    <Row>
      <Col span="16" style="border-right: 1px solid #d3d3d3;">
      <vue-scroll :ops="ops" style="height: calc(100vh - 120px)">
        <Collapse simple v-model="unfold">
          <Panel name="Quantitative">
            Quantitative analysis
            <tool-box slot="content" :activityInfo="activityInfo"></tool-box>
          </Panel>
          <Panel name="Qualitative">
            Qualitative analysis
            <tool-box slot="content" :activityInfo="activityInfo"></tool-box>
          </Panel>
        </Collapse>
      </vue-scroll>
      </Col>
      <Col span="8">
        <res-list :activityInfo="activityInfo"></res-list>
      </Col>
    </Row>
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
      ops: {
        bar: {
          background: "#808695",
        },
      },
      unfold: ["Quantitative", "Qualitative"],
    };
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

