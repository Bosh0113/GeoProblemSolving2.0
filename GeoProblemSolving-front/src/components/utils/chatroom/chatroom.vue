<style scoped>
.chatPanel {
  display: flex;
  min-width: 1200px;
}

/* member部分样式 */
.memberPanel {
  width: 300px;
  background-color: #ffffff;
  box-shadow: 6px 0px 3px -5px #b9b9b9;
}

.participants {
  height: auto;
  margin-top: 10px;
}

.participants h4 {
  padding: 10px;
  font-size: 20px;
  line-height: 20px;
  text-align: center;
  color: rgb(0, 0, 0);
}

.extendedPanel {
  width: 500px;
  height: 100%;
}

/* 右侧布 */
.contentPanel {
  display: flex;
  flex-direction: column;
  flex: auto;
}

/* 信息主题列表显示窗口 */
.contentBody {
  flex: 1;
  /* padding: 25px; */
  overflow-y: auto;
  background-color: #afafa53a;
}

/* 发送信息部分 */
.contentFooter {
  height: 240px;
  width: 100%;
  padding: 4px 5px 4px 5px;
  background-color: lightgray;
}

.message_bar {
  display: flex;
  height: 52px;
}

.u_avater {
  padding: 10px;
  width: 2.5%;
  margin-right: 5%;
}

.send_emoji {
  /* text-align: center; */
  width: 35px;
  float: left;
  margin: 0 10px;
}
.send_emoji >>> #btn-emoji-default {
  margin: 0;
}
.send_emoji >>> #btn-emoji-default:hover {
  -webkit-transform: translateY(-4px);
  -ms-transform: translateY(-4px);
  transform: translateY(-4px);
  -webkit-transition: all 0.2s ease-out;
  transition: all 0.2s ease-out;
}
.send_tool {
  /* text-align: center; */
  width: 35px;
  float: left;
  margin: 0 10px;
}
.send_send {
  float: right;
  margin-right: 2px;
}
.send_icon:hover {
  cursor: pointer;
  -webkit-transform: translateY(-4px);
  -ms-transform: translateY(-4px);
  transform: translateY(-4px);
  -webkit-transition: all 0.2s ease-out;
  transition: all 0.2s ease-out;
}

/* 发送信息部分结束 */

.inputText >>> textarea.ivu-input {
  border: none;
  height: auto;
  width: 100%;
  resize: none;
  font-size: 22px;
}
.sent_to_tip {
  line-height: 40px;
  font-size: 18px;
  color: rgb(92, 93, 94);
  margin-right: 10px;
  font-weight: 600;
}
</style>
<template>
  <Row>
    <div class="chatPanel" :style="{height:panelHeight+'px',width:panelWidth+'px'}">
      <div class="memberPanel">
        <div class="participants">
          <h4>Participants</h4>
          <chat-manager :item="manager" @sendSingle="sendSingle"></chat-manager>
          <div v-for="(participant,index) in member" :key="'online' +index">
            <chat-member :item="participant" @sendSingle="sendSingle"></chat-member>
          </div>
        </div>
      </div>

      <div class="contentPanel" :style="{width:panelWidth-800+'px'}" ref="contentBody">
        <div class="contentBody" ref="contentBody">
          <div v-for="(list,index) in msglist" :key="index">
            <template v-if="list.srcUserId === $store.getters.userId ">
              <bubble-right :message="list"></bubble-right>
            </template>
            <template v-else>
              <bubble-left :message="list"></bubble-left>
            </template>
          </div>
        </div>
        <div class="contentFooter">
          <Row>
            <Col :span="24">
              <Row>
                <Col :span="12">
                  <div class="send_emoji">
                    <twemoji-picker
                      :emojiData="emojiDataAll"
                      :emojiGroups="emojiGroups"
                      :pickerHeight="300"
                      :pickerWidth="500"
                      @emojiUnicodeAdded="addEmoj"
                    ></twemoji-picker>
                  </div>
                  <div class="send_tool">
                    <Icon type="md-time" @click.native="showRecords()" size="35" class="send_icon" />
                  </div>
                  <div class="send_tool">
                    <Upload multiple :before-upload="beforeUploadPic" action>
                      <Icon type="md-image" size="35" class="send_icon" />
                    </Upload>
                  </div>
                  <div class="send_tool">
                    <Icon
                      type="md-hammer"
                      size="35"
                      class="send_icon"
                      @click.native="toolModalShow = true"
                    />
                  </div>

                  <div class="send_tool">
                    <Icon
                      type="md-water"
                      size="35"
                      class="send_icon"
                      @click.native="showConcepts()"
                    />
                  </div>

                  <div class="send_tool">
                    <Icon type="md-send" @click.native="send" size="35" class="send_icon" />
                  </div>
                </Col>
                <Col :span="12">
                  <div class="send_send">
                    <el-select v-model="sendToMemberId" style="width:150px">
                      <el-option
                        v-for="item in selectRoom"
                        :value="item.userId"
                        :label="item.userName"
                        :key="item.userId"
                      >{{ item.userName}}</el-option>
                    </el-select>
                  </div>
                  <div class="send_send">
                    <div class="sent_to_tip">Send to</div>
                  </div>
                </Col>
              </Row>
              <Row>
                <Col :span="24">
                  <Input
                    :rows="5"
                    type="textarea"
                    class="inputText"
                    v-model="message"
                    @keydown.native="handleKeyCode($event)"
                  ></Input>
                  <!-- <div id="a" contentEditable="true" >{{message}}</div> -->
                </Col>
              </Row>
            </Col>
          </Row>
        </div>
      </div>

      <div class="extendedPanel" v-show="extendedPanelShow">
        <Tabs type="card">
          <TabPane label="Records">
            <records
              :showTab="showTabs"
              :groupId="groupId"
              :userId="userId"
              :panelHeight="panelHeight"
            ></records>
          </TabPane>
        </Tabs>
        <!-- 日期选择器 -->
      </div>

      <div class="extendedPanel" v-show="extendedConceptsShow">
        <Tabs type="card">
          <TabPane label="Concepts">
            <concepts :msgConcepts="msgConcepts"></concepts>
          </TabPane>
          <TabPane label="ConceptMap">
            <conceptMap :msgConceptMap="msgConceptMap" :panelHeight="panelHeight"></conceptMap>
          </TabPane>
        </Tabs>
        <!-- 日期选择器 -->
      </div>

      <Modal v-model="toolModalShow" width="800" footer-hide>
        <tool-modal :userId="userId" @selectedTools="sendTools" footer-hide></tool-modal>
      </Modal>
    </div>
    <!-- </Col> -->
  </Row>
</template>
<script>
import * as socketApi from "./../../../api/socket.js";
import toolModal from "@/components/common/tools/toolModal";
import chatMember from "@/components/common/card/chat/chatMember";
import chatManager from "@/components/common/card/chat/chatManager";
import Avatar from "vue-avatar";
import { Upload } from "element-ui";
import { TwemojiPicker } from "@kevinfaguiar/vue-twemoji-picker";
import EmojiAllData from "@kevinfaguiar/vue-twemoji-picker/emoji-data/en/emoji-all-groups.json";
import EmojiDataAnimalsNature from "@kevinfaguiar/vue-twemoji-picker/emoji-data/en/emoji-group-animals-nature.json";
import EmojiDataFoodDrink from "@kevinfaguiar/vue-twemoji-picker/emoji-data/en/emoji-group-food-drink.json";
import EmojiGroups from "@kevinfaguiar/vue-twemoji-picker/emoji-data/emoji-groups.json";

import bubbleRight from "./components/bubbleRight";
import bubbleLeft from "./components/bubbleLeft";
import records from "./components/records";
import concepts from "./components/concepts";
import conceptMap from "./components/conceptMap";

export default {
  components: {
    Avatar,
    toolModal,
    "twemoji-picker": TwemojiPicker,
    "el-upload": Upload,
    bubbleRight,
    bubbleLeft,
    chatMember,
    chatManager,
    records,
    concepts,
    conceptMap
  },

  computed: {
    emojiDataAll() {
      return EmojiAllData;
    },
    emojiGroups() {
      return EmojiGroups;
    }
  },

  data() {
    return {
      extendedPanelShow: false,
      extendedConceptsShow: false,
      message: "",
      msglist: [],
      send_msg: [],
      panelHeight: 0,
      panelWidth: 0,

      windowStatus: "focus", //监听浏览器失焦事件

      groupId: this.$route.query.groupID,
      //memberpanel
      manager: {},
      member: [],
      //工具对话框
      toolModalShow: false,
      //语义概念库
      toolConceptsShow: false,
      //user infomation
      userId: this.$store.getters.userId,
      userName: this.$store.getters.userName,
      selectRoom: [{ userName: this.userName, userId: this.userId }],
      sendToMemberId: this.userId,
      onlineIds: [],
      msgRecords: [],
      msglist: [],
      //聊天记录
      showTabs: true,
      msgConcepts: [],
      msgConceptMap: []
    };
  },

  mounted() {
    window.addEventListener("blur", this.winBlur, true); //监听浏览器失焦事件
    window.addEventListener("focus", this.winFocus, true); //监听浏览器失焦事件
    window.addEventListener("resize", this.initSize, true);

    this.init();
    this.supportNotify();
  },
  methods: {
    async init() {
      this.initSize();
      this.startWebSocket(this.groupId);
    },

    initSize() {
      $("#app").css("min-width", "0");
      $("#app").css("min-height", "0");
      this.panelHeight = window.innerHeight;
      this.panelWidth = window.innerWidth;
    },

    send() {
      // 消息不为空
      if (this.message !== "") {
        this.send_msg = {
          type: "message",
          srcUserName: this.userName,
          srcUserId: this.userId,
          targetUserId: this.sendToMemberId,
          content: this.message
        };
        if (this.socketApi.getSocketInfo().linked) {
          this.msglist.push(this.send_msg);
          this.msgRecords.push(this.send_msg);
          this.socketApi.sendSock(this.send_msg, this.getSocketConnect); //连接后台onopen方法
        } else {
          let chatMsg = {
            type: "notice",
            content: "You are disconnected with others."
          };
          this.msglist.push(chatMsg);
        }
      }
      this.message = "";
    },

    sendPic(messageUrl) {
      this.send_msg = {
        type: "message_pic",
        srcUserName: this.userName,
        srcUserId: this.userId,
        targetUserId: this.sendToMemberId,
        content: messageUrl
      };
      if (this.socketApi.getSocketInfo().linked) {
        this.msglist.push(this.send_msg);
        this.msgRecords.push(this.send_msg);
        this.socketApi.sendSock(this.send_msg, this.getSocketConnect); //连接后台onopen方法
      } else {
        let chatMsg = {
          type: "notice",
          content: "You are disconnected with others."
        };
        this.msglist.push(chatMsg);
      }
    },

    sendTools(tools) {
      tools.forEach(item => {
        this.send_msg = {
          type: "message_tool",
          srcUserName: this.userName,
          srcUserId: this.userId,
          targetUserId: this.sendToMemberId,
          content: item
        };
        if (this.socketApi.getSocketInfo().linked) {
          this.msglist.push(this.send_msg);
          this.msgRecords.push(this.send_msg);
          this.socketApi.sendSock(this.send_msg, this.getSocketConnect); //连接后台onopen方法
        } else {
          let chatMsg = {
            type: "notice",
            content: "You are disconnected with others."
          };
          this.msglist.push(chatMsg);
        }
        this.toolModalShow = false;
      });
    },

    sendSingle(val) {
      this.sendToMemberId = val.userId;
    },

    async beforeUploadPic(file) {
      let formData = new FormData();
      formData.append("messPic", file);
      let data = await this.axios.post(
        "/GeoProblemSolving/chat/picture",
        formData
      );
      let messageUrl = `http://${window.location.host}${data.data.data}`;
      this.sendPic(messageUrl);
    },

    startWebSocket(id) {
      this.socketApi.initWebSocket(
        "ChatServer/" + id,
        this.$store.state.IP_Port
      );
      this.send_msg = {
        type: "test",
        srcUserName: "Test",
        srcUserId: "test",
        targetUserName: "test",
        targetUserId: "test",
        content: "Test"
      };
      this.socketApi.sendSock(this.send_msg, this.getSocketConnect);
    },

    getSocketConnect(data) {
      let chatMsg = data; //data传回onopen方法里的值
      if (chatMsg.type === "members") {
        this.judgeonlineParticipant(chatMsg);
      } else if (
        chatMsg.type === "message" ||
        chatMsg.type === "message_pic" ||
        chatMsg.type === "message_tool"
      ) {
        if (chatMsg.content != "") {
          this.msglist.push(chatMsg);
          this.msgRecords.push(chatMsg);
          this.sendNotify(chatMsg);
        }
      } else if (chatMsg.type === "notice") {
        // if (chatMsg.behavior != "") {
        //   this.msglist.push(chatMsg);
        // }
        // this.judgeonlineParticipant(chatMsg);
      } else if (chatMsg.type === "concepts") {
        this.msgConcepts = chatMsg.frequency;
        this.msgConceptMap = chatMsg.conceptMap;
        this.extendedConceptsShow = true;
      } else if (chatMsg.type == undefined && chatMsg.length > 0) {
        for (let i = 0; i < chatMsg.length; i++) {
          if (chatMsg[i].content != "") {
            this.msglist.push(chatMsg[i]);
            this.msgRecords.push(chatMsg[i]);
          }
        }
      }
    },

    //判断在线的用户列表
    judgeonlineParticipant(msg) {
      let online = [];
      if (msg.type == "members") {
        online = msg.content;
        this.onlineIds = msg.content;
      } else {
        if (msg.behavior == "off") {
          online = this.onlineIds.filter(item => {
            return item.userId == msg.userId;
          });
        }
      }
      this.getParticipants(online);
    },

    async getParticipants(online) {
      let manager = [];
      let member = [];
      let membersList = online;
      let participantsTemp = [];
      for (let i = 0; i < membersList.length; i++) {
        let userId = membersList[i];
        let { data } = await this.axios.get(
          `/GeoProblemSolving/user/inquiry?key=userId&value=${userId}`
        );
        participantsTemp.push(data);
      }

      manager = participantsTemp.filter(par => {
        return par.userId == this.$store.getters.userId;
      });

      member = participantsTemp.filter(par => {
        return par.userId != this.$store.getters.userId;
      });
      this.$set(this, "manager", manager[0]);
      this.$set(this, "member", member);
      this.getSelectRoom(manager[0], member);
    },

    getSelectRoom(manager, member) {
      let item = Object.assign({}, manager);
      this.$set(this, "sendToMemberId", item.userId); //init sendroom
      this.selectRoom = [];
      item.userName = "All";
      this.selectRoom.push(item, ...member);
    },

    showRecords() {
      this.extendedPanelShow = !this.extendedPanelShow;
      this.showTabs = true;
    },
    showConcepts() {
      this.extendedConceptsShow = !this.extendedConceptsShow;
    },

    //notice
    winBlur() {
      this.windowStatus = "blur";
    },
    winFocus() {
      this.windowStatus = "focus";
    },

    supportNotify() {
      //检查浏览器是否支持
      if (!("Notification" in window)) {
        window.alert("This browser does not support desktop notification");
      } else {
        var Notification =
          window.Notification ||
          window.mozNotification ||
          window.webkitNotification;
        if (Notification && Notification.permission === "granted") {
          // 检查用户是否同意接受通知
          Notification.requestPermission();
        } else {
          // 否则我们需要向用户获取权限
          Notification.requestPermission();
        }
      }
    },

    sendNotify(msg) {
      if (this.windowStatus === "blur") {
        var notify = new Notification("You have got a new message", {
          tag: msg.fromid + msg.time,
          icon: require("@/assets/images/OGMS2.png"), //通知的缩略图,
          body: "from   " + msg.from, //通知的具体内容
          renotify: true
        });

        //通知显示
        notify.onshow = function() {
          //5s自行停止通知
          setTimeout(notify.close.bind(notify), 5000);
        };

        //单击通知，跳转到页面
        notify.onclick = function() {
          window.focus();
          notify.close();
        };

        //报错处理
        notify.onerror = function() {
          console.log("error");
        };

        //通知关闭
        notify.onclose = function() {
          console.log("HTML5桌面消息关闭！！！");
        };
      }
    },

    addEmoj(emoj) {
      this.message = this.message + emoj;
      console.log(this.message);
    },

    handleKeyCode(event) {
      if (event.keyCode == 13 && event.ctrlKey) {
        this.message += "\n"; //换行
      } else if (event.keyCode == 13) {
        this.send(); //提交的执行函数
        event.preventDefault(); //禁止回车的默认换行
      }
    }
  },

  beforeDestroy() {
    this.socketApi.close();
    window.removeEventListener("resize", this.initSize);
    window.removeEventListener("blur", this.winBlur, true); //监听浏览器失焦事件
    window.removeEventListener("focus", this.winFocus, true); //监听浏览器失焦事件
    window.removeEventListener("resize", this.initSize, true);
  },

  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      if (!vm.$store.getters.userState || vm.$store.getters.userId == "") {
        vm.$router.push({
          name: "Login"
        });
      } else {
      }
    });
  },

  updated() {
    this.$nextTick(() => {
      //滑块跨到最底端
      var div = this.$el.querySelector(".contentBody");
      div.scrollTop = div.scrollHeight;
    });
  }
};
</script>
