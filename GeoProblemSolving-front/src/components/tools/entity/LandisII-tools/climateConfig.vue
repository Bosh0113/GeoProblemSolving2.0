<template>
  <div>
    <div id="collab-tool-head"></div>
    <div id="collab-tool-sidebar"></div>
    <div id="collab-tool-content">
      <div id="edit-mask" title="The other participant is operating."></div>

      <vue-scroll :ops="ops" style="height: calc(100vh - 40px)">
      <!-- coding for your tools // begin-->      
      
      <!-- // end -->
      </vue-scroll>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      ops: {
        bar: {
          background: "#808695",
        },
      },      
      // basic info
      activityInfo: {},
      userInfo: {},
      resources: [],
      //
    };
  },
  created() {},
  mounted() {
    // 加载协同组件
    loadCollabComponent();
    this.getStepInfo();
  },
  methods: {
    getStepInfo() {
      if (componentStatus) {
        // 获取数据
        this.activityInfo = activityInfo;
        this.userInfo = userInfo;
        this.resources = resources;

        // 绑定函数
        buildSocketChannel(
          this.getSocketOperation,
          this.getSocketData,
          this.getSocketComputation
        );
        loadResChannel = this.loadResources;
      } else {
        let _this = this;
        setTimeout(function () {
          _this.getStepInfo();
        }, 1000);
      }
    },
    loadResources(resList) {
      for (let i = 0; i < resList.length; i++) {
        
        // your function

        //

        let send_content = {
          type: "resource",
          sender: this.userInfo.userId,
          behavior: "select",
          content: this.selectData,
        };
        sendCustomOperation(send_content);
      }
    },
    submit() {},
    
  },
};
</script>
<style scoped>
</style>