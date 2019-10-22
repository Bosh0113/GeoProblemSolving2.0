<style scoped>
.modelToolBtn {
  height: 60px;
  width: 60px;
  float: left;
  padding: 0;
  color: #00c0ff;
}
.modelToolsetBtn {
  height: 60px;
  width: 60px;
  float: left;
  padding: 0;
  color: #19be6b;
}
</style>
<template>
  <div>
    <div>
      <div style="width:3px;height:18px;float:left;background-color:rgb(124, 126, 126)"></div>
      <h4 style="float:left;margin-left:5px">Toolbox</h4>
      <div style="float:right;cursor:pointer" @click="addTools" title="Add other tools">
        <Icon type="md-add" />
      </div>
    </div>
    <div :style="{height:contentHeight-66+'px'}">
      <vue-scroll :ops="ops">
        <Tooltip
          placement="bottom"
          style="margin-left:2%; margin-top:3%"
          v-for="(item,index) in toolsetList"
          :key="index"
          content="Test toolset"
          theme="light"
        >
          <template v-if="item.toolsetImg == '' || item.toolsetImg == undefined">
            <Button
              class="modelToolsetBtn"
              to="//134.175.111.77/note"
              target="_blank"
            ><Icon type="ios-hammer-outline" size="60"/></Button>
          </template>
          <template v-else>
            <img :src="item.toolsetImg" class="modelToolsetBtn" />
          </template>
        </Tooltip>
        <Tooltip
          placement="bottom"
          v-for="(item,index) in toolList"
          :key="index"
          content="Test tool"
          theme="light"
          style="margin-left:2%;margin-top:3%"
        >
          <template v-if="item.toolImg == '' || item.toolsetImg == undefined">
            <Button
              class="modelToolBtn"
              to="//134.175.111.77/note"
              target="_blank"
            ><Icon type="ios-hammer" size="60" /></Button>
          </template>
          <template v-else>
            <img :src="item.toolsetImg" class="modelToolBtn" />
          </template>
        </Tooltip>
      </vue-scroll>
    </div>
  </div>
</template>
<script>
export default {
  props: ["stepInfo", "contentHeight", "userRole"],
  data() {
    return {
      ops: {
        bar: {
          background: "#808695"
        }
      },
      toolList: [
        {
          name: "111",
          model_stateId: "",
          model_oid: "",
          model_mdlId: "",
          toolsetRecords: [],
          tool_url: "",
          recomStep: "",
          categoryTag: [],
          toolImg: "",
          privacy: "pirvate"
        },
        {}
      ],
      toolsetList: [
        {
          name: "222",
          recomStep: "",
          toolsetImg: "",
          privacy: "private",
          categoryTag: []
        },
        {}
      ]
    };
  },
  methods: {
    addTools() {
      let routeUrl = this.$router.resolve({
        path: "/toolCollection",
        query: { id: 1 }
      });
      window.open(routeUrl.href, "_blank");
    },
    toolPanel(type) {
      // if (this.userRole != "Visitor") {
      // this.axios
      //   .get("/GeoProblemSolving/user/state")
      //   .then(res => {
      //     if (!res.data) {
      //       this.$store.commit("userLogout");
      //       this.$router.push({ name: "Login" });
      //     } else {
      //       var toolURL = "";
      //       let toolName = "";
      //       if (type == "chat") {
      //         toolURL =
      //           '<iframe src="' +
      //           "http://" +
      //           this.$store.state.IP_Port +
      //           '/GeoProblemSolving/chat" style="width: 100%;height:100%"></iframe>';
      //         toolName = "Chatroom";
      //       }
      //       let panel = jsPanel.create({
      //         theme: "success",
      //         headerTitle: toolName,
      //         footerToolbar: '<p style="height:10px"></p>',
      //         contentSize: "1200 600",
      //         content: toolURL,
      //         disableOnMaximized: true,
      //         dragit: {
      //           containment: 5
      //         },
      //         callback: function() {
      //           // this.content.style.padding = "20px";
      //         }
      //       });
      //       panel.resizeit("disable");
      //       $(".jsPanel-content").css("font-size", "0");
      //       this.panelList.push(panel);
      //       // 生成records, 同步
      //       let record = {
      //         who: this.$store.getters.userName,
      //         whoid: this.$store.getters.userId,
      //         type: "tools",
      //         toolType: type,
      //         content: "used a tool: " + type,
      //         moduleId: this.stepId,
      //         time: new Date().toLocaleString()
      //       };
      //       this.subprojectSocket.send(JSON.stringify(record));
      //     }
      //   })
      //   .catch(err => {
      //     console.log("Get user info fail.");
      //   });
    }
  }
};
</script>