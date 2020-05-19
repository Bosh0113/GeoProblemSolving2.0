<style scoped>
</style>
<template>
  <div style="padding:15px">
    <Collapse simple v-model="unfold">
      <Panel name="data">
        Resource list
        <data-list
          slot="content"
          :stepInfo="stepInfo"
          :userRole="userRole"
          :projectInfo="projectInfo"
          @dataBehavior="listenDatalist"
        ></data-list>
      </Panel>
      <Panel name="tool" v-show="stepInfo.activeStatus">
        Toolbox
        <tool-container
          slot="content"
          :stepInfo="stepInfo"
          :userRole="userRole"
          :projectInfo="projectInfo"
          @toolBehavior="listenToolbox"
          @toolPanel="listenToolPanel"
        ></tool-container>
      </Panel>
    </Collapse>
    <p style="margin: 10px 0">Map</p>
    <Divider style="margin:10px 0" />
    <map-canvas :stepInfo="stepInfo" :userRole="userRole"></map-canvas>
    <message-panel
      :stepInfo="stepInfo"
      :received-chat-msgs="receivedChatMsgs"
      :operation-records="operationRecords"
      :getSocketConnect="getSocketConnect"
    ></message-panel>
    <BackTop></BackTop>
  </div>
</template>
<script>
import mapCanvas from "./utils/mapCanvas";
import dataList from "./utils/dataList";
import toolContainer from "./utils/toolContainer";
import messagePanel from "./utils/messagePanel";
export default {
  components: {
    mapCanvas,
    dataList,
    toolContainer,
    messagePanel
  },
  props: ["stepInfo", "userRole", "projectInfo"],
  data() {
    return {
      unfold: ["tool", "data"],
      stepSocket: null,
      operationRecords: [],
      panelList: [],
      // messagePanel.vue -- chat
      receivedChatMsgs: []
    };
  },
  mounted() {
    this.openStepSocket();
    this.startWebSocket();
  },
  beforeDestroy() {
    this.closeStepSocket();
    this.closePanel();
  },
  watch: {
    stepInfo(data) {
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
    listenToolPanel(data) {
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
      console.log("StepSocket连接成功！");
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
    },

    // websocket
    startWebSocket() {
      this.socketApi.initWebSocket(
        "ChatServer/" + this.stepInfo.stepId,
        this.$store.state.IP_Port
      );
      let send_msg = {
        type: "test",
        from: "Test",
        content: "TestChat"
      };
      this.socketApi.sendSock(send_msg, this.getSocketConnect);
    },
    getSocketConnect(data) {
      this.receivedChatMsgs = [];
      var chatMsg = data; //data传回onopen方法里的值
      if (data.type === "members") {
        let members = data.content
          .replace("[", "")
          .replace("]", "")
          .replace(/\s/g, "")
          .split(",");
        let participantMsg = {
          content: "members",
          msg: members
        };
        this.$emit("participantsChange", participantMsg);
      } else if (data.type === "message") {
        //判断消息的发出者
        if (chatMsg.content != "") {
          this.receivedChatMsgs.push(chatMsg);
        }
      } else if (data.type === "notice") {
        //上线下线提示
        if (chatMsg.behavior != "" && chatMsg.userId != "") {
          this.receivedChatMsgs.push(chatMsg);
        }
        let participantMsg = {
          content: "notice",
          msg: chatMsg
        };
        this.$emit("participantsChange", participantMsg);
      } else if (chatMsg.type == undefined && chatMsg.length > 0) {
        for (let i = 0; i < chatMsg.length; i++) {
          if (chatMsg[i].content != "") {
            this.receivedChatMsgs.push(chatMsg[i]);
          }
        }
      }
    }
  }
};
</script>
