<style scoped>
#msgPanel {
}
</style>
<template>
  <div>
    <div id="msgPanelBtn" @click="openPanel=!openPanel">
      <Icon type="ios-paper" size="48" />
    </div>
    <template v-if="openPanel">
      <Tabs value="records" id="msgPanel">
        <TabPane label="Records" name="records">
          <div></div>
        </TabPane>
        <TabPane label="Chats" name="chats">
          <div></div>
        </TabPane>
      </Tabs>
    </template>
  </div>
</template>
<script>
export default {
  data() {
    return {
      openPanel: false
    };
  },
  props: [],
  mounted() {},
  methods: {}
};
</script>