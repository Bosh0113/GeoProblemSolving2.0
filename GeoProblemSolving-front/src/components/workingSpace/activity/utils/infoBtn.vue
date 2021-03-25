<template>
  <div>
    <Button icon="ios-paper" @click="infoModal = true">Introduction</Button>
    <Modal
      class="info"
      v-model="infoModal"
      width="900px"
      title="Activity information"
      ><activity-show :activityInfo="activityInfo"></activity-show
    ></Modal>
  </div>
</template>
<script>
import activityShow from "../activityShow.vue";
export default {
  props: ["activityInfo"],
  components: {
    activityShow,
  },
  data() {
    return {
      infoModal: false,
    };
  },
  methods: {},
};
</script>
<style scoped>
.info >>> .ivu-modal-body {
  height: 540px;
  padding: 0 10px;
}
</style>