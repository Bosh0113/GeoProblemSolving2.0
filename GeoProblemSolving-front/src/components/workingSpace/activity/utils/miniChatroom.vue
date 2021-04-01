<style scoped>
.content {
  margin-left: 10px;
  word-break: break-word;
}
#msgPanelBtn {
  z-index: 100;
  position: fixed;
  right: 30px;
  bottom: 20px;
  cursor: pointer;
  background-color: #fff;
}
#msgPanel {
  z-index: 99;
  width: 340px;
  height: 450px;
  position: fixed;
  right: 55px;
  bottom: 35px;
  background: #5093f8;
  padding: 5px;
}
#msgPanel > .ivu-tabs-card > .ivu-tabs-content > .ivu-tabs-tabpane {
  background: #fff;
  padding: 10px;
}
#msgPanel > .ivu-tabs.ivu-tabs-card > .ivu-tabs-bar .ivu-tabs-tab {
  border-color: transparent;
}
#msgPanel > .ivu-tabs-card > .ivu-tabs-bar .ivu-tabs-tab-active {
  border-color: #fff;
}
.contentPanel {
  display: flex;
  flex-direction: column;
  flex: auto;
}
.contentBody {
  flex: 1;
  padding: 5px;
  overflow-y: auto;
}
.chat-notice {
  color: lightgrey;
  /* position: absolute; */
  margin: auto;
}
.user_detail {
  height: 60px;
}
.u_name {
  height: 20px;
  margin-top: 5px;
  /* margin-left: 10px; */
  text-align: center;
  font-size: 10px;
  line-height: 10px;
}
.chat-bubble-r {
  position: relative;
  margin: 12px;
  padding: 5px 8px;
  word-break: break-word;
  background: #fff;
  border: 1px solid lightgray;
  border-radius: 5px;
  min-width: 20%;
  float: right;
}
.chat-bubble-l {
  position: relative;
  margin: 12px;
  padding: 5px 8px;
  word-break: break-word;
  background: #fff;
  border: 1px solid lightgray;
  border-radius: 5px;
  min-width: 20%;
  float: left;
}
.chat-bubble-left:before {
  content: "";
  position: absolute;
  width: 0;
  height: 0;
  left: -20px;
  top: 5px;
  border: 10px solid;
  border-color: transparent #989898 transparent transparent;
}
.chat-bubble-left:after {
  content: "";
  position: absolute;
  width: 0;
  height: 0;
  left: -16px;
  top: 7px;
  border: 8px solid;
  border-color: transparent #989898 transparent transparent;
}
.chat-bubble-right:before {
  content: "";
  position: absolute;
  width: 0;
  height: 0;
  right: -20px;
  top: 5px;
  border: 10px solid;
  border-color: transparent transparent transparent lightgray;
}
.chat-bubble-right:after {
  content: "";
  position: absolute;
  width: 0;
  height: 0;
  right: -16px;
  top: 7px;
  border: 8px solid;
  border-color: transparent transparent transparent lightgray;
}
.contentFooter {
  display: flex;
  height: 30px;
}
.user_img {
  background-color: lightblue;
  margin-top: 2px;
  margin-left: 5px;
}
.input_panel {
  width: 85%;
}
.message_input {
  height: 40px;
}
.send_panel {
  display: flex;
}
.send_message_btn {
  height: 32px;
}
</style>
<template>
  <div>
    <Button
      id="msgPanelBtn"
      @click="openPanel = !openPanel"
      title="Mini chatbox"
      shape="circle"
      size="large"
    >
      <Icon type="ios-chatboxes" size="30" color="#2d8cf0" />
    </Button>
    <div id="msgPanel" v-show="openPanel">
      <Tabs v-model="msgType" type="card">
        <TabPane :label="label1" name="chats" style="margin-top: -16px">
          <div>
            <div class="contentPanel">
              <div class="contentBody" id="contentBody">
                <vue-scroll :ops="scrollOps" style="height: 345px">
                  <div
                    style="display: flex"
                    v-for="(item, index) in allChatMsgs"
                    :key="index"
                  >
                    <template v-if="item.type == 'notice'">
                      <div class="chat-notice">{{ item.content }}</div>
                    </template>
                    <template v-else-if="item.fromid === $store.getters.userId">
                      <div style="width: 95%">
                        <div class="chat-bubble-r chat-bubble-right">
                          {{ item.content }}
                        </div>
                      </div>
                      <div class="user_detail">
                        <div class="u_img">
                          <avatar
                            class="user_img"
                            :username="item.from"
                            :size="25"
                          ></avatar>
                        </div>
                        <div class="u_name">{{ item.from }}</div>
                      </div>
                    </template>
                    <template v-else>
                      <div
                        class="user_detail"
                        style="margin-left: 2.5%; margin-top: 5px"
                      >
                        <div class="u_img">
                          <avatar
                            class="user_img"
                            :username="item.from"
                            :size="25"
                          ></avatar>
                        </div>
                        <div class="u_name">
                          <span style="font-size: 3px">{{ item.from }}</span>
                        </div>
                      </div>
                      <div style="width: 95%">
                        <div class="chat-bubble-l chat-bubble-left">
                          {{ item.content }}
                        </div>
                      </div>
                    </template>
                  </div>
                </vue-scroll>
              </div>
              <div class="contentFooter">
                <div class="input_panel">
                  <Input
                    placeholder="Enter message..."
                    class="message_input"
                    v-model="typingMsg"
                    @keyup.enter.native="send(typingMsg)"
                  />
                </div>
                <div class="send_panel">
                  <Button class="send_message_btn" @click="send(typingMsg)"
                    >Send</Button
                  >
                </div>
              </div>
            </div>
          </div>
        </TabPane>
        <Button
          @click="openChatroom()"
          size="small"
          slot="extra"
          title="Message records"
          v-show="msgType == 'chats'"
        >
          <Icon type="md-time" />
        </Button>
      </Tabs>
    </div>
  </div>
</template>
<script>
import * as socketApi from "@/api/socket.js";
import * as operationApi from "@/api/operation.js";
import Avatar from "vue-avatar";
export default {
  components: {
    Avatar,
  },
  props: ["activityInfo"],
  data() {
    return {
      //basic
      scrollOps: {
        bar: {
          background: "lightgrey",
        },
      },
      msgType: "chats",
      unreadRecords: 0,
      label1: (h) => {
        return h("div", [
          h("span", "Chats"),
          h("Badge", {
            props: {
              count: this.receivedChats.length,
            },
          }),
        ]);
      },
      unreadChats: 0,
      openPanel: false,
      // chats
      typingMsg: "",
      allChatMsgs: [],
      receivedChats: [],
    };
  },
  watch: {
    receivedChatMsgs(val) {
      for (let i = 0; i < this.receivedChatMsgs.length; i++) {
        this.receivedChats.push(this.receivedChatMsgs[i]);
      }
    },
  },
  mounted() {
    this.readHistoricalRecords();
  },
  updated: function () {
    // this.$refs["vs"].scrollTo(
    //   {
    //     y: "100%"
    //   },
    //   0,
    //   "easeInQuad"
    // );
    if (this.msgType == "chats") {
      for (let i = 0; i < this.receivedChats.length; i++) {
        this.allChatMsgs.push(this.receivedChats.shift());
      }
    }
  },
  methods: {
    openChatroom() {
        // 聊天记录
    },
    // websocket
    send(msg) {
      this.message = msg;
      let myDate = new Date().Format("yyyy-MM-dd HH:mm:ss");
      let current_time = myDate.toLocaleString(); //获取日期与时间

      // 消息不为空
      if (this.message !== "") {
        this.send_msg = {
          type: "message",
          from: this.$store.getters.userName,
          srcUserId: this.$store.getters.userId,
          content: this.message,
          time: current_time,
          targetUserId: this.$store.getters.userId,
        };
        if (this.socketApi.getSocketInfo().linked) {
          this.msglist.push(this.send_msg);
          this.msgRecords.push(this.send_msg);
          this.socketApi.sendSock(this.send_msg, this.getSocketConnect); //连接后台onopen方法
        } else {
          let chatMsg = {
            type: "notice",
            content: "You are disconnecting with others.",
          };
          this.msglist.push(chatMsg);
        }
      }
      this.message = "";
    },
    startWebSocket() {
      this.socketApi.initWebSocket(
        "ChatServer/" + this.activityInfo.aid,
        this.$store.state.IP_Port
      );
      let send_msg = {
        type: "test",
        from: "Test",
        content: "TestChat",
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
          msg: members,
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
          msg: chatMsg,
        };
        this.$emit("participantsChange", participantMsg);
      } else if (chatMsg.type == undefined && chatMsg.length > 0) {
        for (let i = 0; i < chatMsg.length; i++) {
          if (chatMsg[i].content != "") {
            this.receivedChatMsgs.push(chatMsg[i]);
          }
        }
      }
    },
    //chat
    send(msg) {
      //修改时间格式使其统一
      Date.prototype.Format = function (fmt) {
        var o = {
          "M+": this.getMonth() + 1, //月份
          "d+": this.getDate(), //日
          "H+": this.getHours(), //小时
          "m+": this.getMinutes(), //分
          "s+": this.getSeconds(), //秒
          "q+": Math.floor((this.getMonth() + 3) / 3), //季度
          "S+": this.getMilliseconds(), //毫毛
        };
        if (/(y+)/.test(fmt))
          fmt = fmt.replace(
            RegExp.$1,
            (this.getFullYear() + "").substr(4 - RegExp.$1.length)
          );
        for (var k in o)
          if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(
              RegExp.$1,
              RegExp.$1.length == 1
                ? o[k]
                : ("00" + o[k]).substr(("" + o[k]).length)
            );
        return fmt;
      };
      this.typingMsg = msg;
      let myDate = new Date().Format("yyyy-MM-dd HH:mm:ss");
      let current_time = myDate.toLocaleString(); //获取日期与时间
      // 消息不为空
      if (this.typingMsg !== "") {
        if (this.socketApi.getSocketInfo().linked) {
          let send_msg = {
            type: "message",
            from: this.$store.getters.userName,
            fromid: this.$store.getters.userId,
            content: this.typingMsg,
            time: current_time,
          };
          this.allChatMsgs.push(send_msg);
          this.socketApi.sendSock(send_msg, this.getSocketConnect);
        } else {
          let chatMsg = {
            type: "notice",
            content: "You are disconnected with others.",
          };
          this.allChatMsgs.push(chatMsg);
        }
      }
      this.typingMsg = "";
    },
  },
};
</script>