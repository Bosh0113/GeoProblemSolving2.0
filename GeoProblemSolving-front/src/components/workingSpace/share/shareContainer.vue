<style scoped>
</style>
<template>
  <iframe
    :src="pageUrl"
    style="width:100%"
    :style="{height:contentHeight+'px'}"
    frameborder="0"
  ></iframe>
</template>
<script>
export default {
  data() {
    return {
      pageUrl: "",
      contentHeight: window.innerHeight - 125,
      userInfo: this.$store.getters.userInfo
    };
  },
  mounted() {
    window.addEventListener("resize", this.initSize);
    this.getSharedUrl();
  },
  methods: {
    initSize() {
      this.contentHeight = window.innerHeight - 125;
    },
    getSharedUrl() {
      let groupId = "7728e1d9-a5b0-44fd-8522-7dde99a07d92",
        resourceId = "49fd2d6f-ecbd-4218-b749-8ca21e8cf47f",
        userInfo = this.userInfo;
      if (
        userInfo.userId != undefined &&
        userInfo.userId != "" &&
        userInfo.userName != undefined &&
        userInfo.userName != "" 
      ) {
        this.pageUrl =
          "/GeoProblemSolving/Collaborative/Mindmap/mindmap.html?userName=" +
          userInfo.userName +
          "&userID=" +
          userInfo.userId +
          "&groupID=" +
          groupId +
          "&resourceID=" +
          resourceId;
      } else {
        this.pageUrl =
          "/GeoProblemSolving/Collaborative/Mindmap/share/mindmap.html?userName=&userID=&groupID=" +
          groupId +
          "&resourceID=" +
          resourceId;
      }
    }
  }
};
</script>