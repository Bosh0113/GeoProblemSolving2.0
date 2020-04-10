<style scoped>
</style>
<template>
  <div style="padding:15px">
    <Collapse simple v-model="unfold">
      <Panel name="context">
        Problem context
        <context-info slot="content" :stepInfo="stepInfo" :userRole="userRole"></context-info>
      </Panel>
      <Panel name="data">
        Resource list
        <data-list slot="content" :stepInfo="stepInfo" :userRole="userRole" @dataBehavior="listenDatalist"></data-list>
      </Panel>
      <Panel name="tool" v-show="stepInfo.activeStatus">
        Toolbox
        <tool-container slot="content" :stepInfo="stepInfo" :userRole="userRole" @toolBehavior="listenToolbox" @toolPanel="listenToolPanel"></tool-container>
      </Panel>
    </Collapse>
    <message-panel :stepInfo="stepInfo" :received-chat-msgs="receivedChatMsgs" :operation-records="operationRecords"></message-panel>
    <BackTop></BackTop>
  </div>
</template>
<script>
import mapCanvas from "./utils/mapCanvas";
import dataList from "./utils/dataList";
import toolContainer from "./utils/toolContainer";
import messagePanel from "./utils/messagePanel";
import contextInfo from "./utils/contextInfo";
export default {
  components: {
    mapCanvas,
    dataList,
    toolContainer,
    messagePanel,
    contextInfo
  },
  props: ["stepInfo", "userRole", "receivedChatMsgs"],
  data() {
    return {
      unfold: ["context","tool", "data"],
      stepSocket: null,
      operationRecords: [],
      panelList:[],
    };
  },
  mounted() {
    this.openStepSocket();
  },
  beforeDestroy(){
    this.closeStepSocket();
    this.closePanel();
  },
  watch:{
    stepInfo(data){
      this.closePanel();
    }
  },
  methods: {
    listenDatalist(data) {
      this.operationRecords = JSON.stringify(data);
    },
    listenToolbox(data) {
      this.operationRecords = JSON.stringify(data);
    },
    listenToolPanel(data){
      this.panelList.push(data);
    },
    closePanel() {
      for (let i = 0; i < this.panelList.length; i++) {
        this.panelList[i].close();
      }
    },
    closeStepSocket() {
      if (this.stepSocket != null) {
        this.removeTimer();
        this.stepSocket.close();
      }
    },
    openStepSocket() {
      if (this.stepSocket != null) {
        this.stepSocket = null;
      }
      var stepSocketURL =
        "ws://" +
        this.$store.state.IP_Port +
        "/GeoProblemSolving/Step/" +
        this.stepInfo.stepId;
      if (this.$store.state.IP_Port == "localhost:8080") {
        stepSocketURL =
          "ws://localhost:8081/GeoProblemSolving/Step/" + this.stepInfo.stepId;
      }
      this.stepSocket = new WebSocket(stepSocketURL);
      this.stepSocket.onopen = this.onOpen;
      this.stepSocket.onmessage = this.onMessage;
      this.stepSocket.onclose = this.onClose;
      this.stepSocket.onerror = this.onError;
      this.setTimer();
    },
    onOpen() {
      console.log("NoticeSocket连接成功！");
    },
    onMessage(e) {
      if (e.data == "Notice") {
        // let newCount = this.unreadNoticeCount + 1;
        // this.$set(this, "unreadNoticeCount", newCount);
        // this.$Message.info("You have a new notice!");
      } else {
        // console.log(e.data);
      }
    },
    onClose(e) {
      this.removeTimer();
      console.log("StepSocket连接断开！");
    },
    onError(e) {
      this.removeTimer();
      console.log("StepSocket连接错误！");
    },
    sendMessage(recipientId) {
      this.stepSocket.send(recipientId);
    },
    setTimer() {
      var that = this;
      this.timer = setInterval(() => {
        var messageJson = {};
        messageJson["type"] = "ping";
        messageJson["message"] = "ping";
        if (that.stepSocket != null && that.stepSocket != undefined) {
          that.stepSocket.send(JSON.stringify(messageJson));
        }
      }, 20000);
    },
    removeTimer() {
      clearInterval(this.timer);
    }
  }
};
</script>
