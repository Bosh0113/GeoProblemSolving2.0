<style scoped>
.chatPanel {
  display: flex;
  overflow-y:hidden
}
/* member部分样式 */
.memberPanel {
  width: 200px;
  background-color: #515a6e;
}
.panelHeader {
  height: 40px;
  margin-top: 10px;
}
.participants {
  height: auto;
  margin-top: 10px;
}
.participants h4,
.panelHeader h4 {
  padding: 10px;
  font-size: 20px;
  line-height: 20px;
  text-align: center;
  color: white;
}
.f_list {
  display: flex;
  padding-left: 10%;
  padding-bottom: 15px;
  color: white;
}
.name {
  height: 28px;
  margin-top: 2px;
  line-height: 28px;
  padding-left: 5%;
  padding-right: 5%;
  margin-bottom: 2px;
  width: 100%;
  font-size: 18px;
  font-family: Helvetica;
}
/* member杨式布局结束 */
/* 关于右侧的布局 */
.searchPanel {
  width: 250px;
  background-color: white;
}
.date_pick {
  padding: 10px;
  width: 60%;
  margin-left: 2.5%;
  margin-right: 2.5%;
}
.date_pick_btn {
  width: 20%;
  margin-left: 5%;
  margin-right: 5%;
  height: 32px;
  margin-top: 10px;
}
.search_msg {
  padding: 10px;
  /* background-color: lightblue; */
  width: 90%;
  margin-left: 2.5%;
}
.searchHeader {
  height: 40px;
}
.searchHeader p {
  height: 20px;
  padding: 10px;
  line-height: 15px;
  font-size: 15px;
  text-align: center;
}
.searchmessageList {
  overflow-y: auto;
}
.message_record_board {
  margin-left: 5%;
  margin-right: 5%;
}
.single_record {
  padding: 5px;
}
/* 右侧布局结束 */
.contentPanel {
  display: flex;
  flex-direction: column;
  flex: auto;
}
.contentHeader {
  /* background-color: lightgray; */
  border-bottom: 1px solid lightgray;
  height: 60px;
  display: flex;
}
/* 信息主题列表显示窗口 */
.contentBody {
  flex: 1;
  padding: 25px;
  overflow-y: auto;
}
.chat-bubble-r {
  position: relative;
  margin: 12px;
  padding: 5px 8px;
  word-break: break-all;
  background: #fff;
  border: 1px solid lightgray;
  border-radius: 5px;
  min-width: 20%;
  float: right;
  /* 这两句让文字从右面开始显示 */
  /* direction: rtl;
  unicode-bidi: bidi-override; */
  /* float:right; */
}

.chat-bubble-l {
  position: relative;
  margin: 12px;
  padding: 5px 8px;
  word-break: break-all;
  background: #fff;
  border: 1px solid lightgray;
  border-radius: 5px;
  min-width: 20%;
  float: left;
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
/* 信息主体结束 */
/* 发送信息部分 */
.contentFooter {
  height: 60px;
  padding: 4px 20px 4px 10px;
  background-color: lightgray;
}
.message_bar {
  display: flex;
  height: 52px;
}
.send_panel {
  display: flex;
}
.u_avater {
  padding: 10px;
  width: 2.5%;
  margin-right: 5%;
}
.user_img {
  background-color: lightblue;
  margin-top: 2px;
  margin-left: 5px;
}
.input_panel {
  width: 85%;
  margin-right: 4%;
}
.message_input {
  height: 40px;
  margin-top: 10px;
  /* margin-bottom: 10px; */
  margin-right: 15px;
  margin-left: 15px;
}
.send_panel {
  display: flex;
}
.send_message_btn {
  height: 32px;
  margin-top: 10px;
  /* margin-bottom: 10px; */
}
/* 发送信息部分结束 */
.chatObject {
  height: 60px;
  padding: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 90%;
}
.s_name {
  font-size: 20px;
  text-align: center;
}
.chatOperate {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 10%;
}
.recordsButton {
  height: 40px;
  line-height: 15px;
  font-size: 15px;
  padding: 5px 5px 5px 5px;
  /* float:right; */
}
.memberImg{
  width: 40px;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.memberName {
  height: 20px;
  padding: 0px 10px;
  width: 100%;
  /* display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap; */
}
.memberDetail {
  height: 100%;
  width: 100%;
  overflow: hidden;
}
.memberOrganization {
  height: 20px;
  padding: 0px 10px;
  width: 100%;
}
.memberName span{
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.memberOrganization span {
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width:50%;
}
</style>
<template>
  <Row>
    <Col span="24">
      <div class="chatPanel" :style="{height:panelHeight+'px',width:panelWidth+'px'}">
        <div class="memberPanel">
          <div class="panelHeader">
            <h4>Groups</h4>
          </div>
          <Select v-model="selectItem" style="width:195px;margin-left:2.5px;margin-right:2.5px">
            <Option v-for="(item,index) in itemList" :value="item.value" :key="item.index" @click.native="changeChatroom(index)">{{ item.label }}</Option>
          </Select>
          <div class="participants">
            <h4>Participants</h4>
            <Card v-for="(participant,index) in participants" :key="index" style="margin:2.5%" :padding="5">
              <div style="display:flex;align-items:center">
                <div class="memberImg">
                  <img
                  v-if="participant.avatar != '' && participant.avatar!='undefined'"
                  :src="participant.avatar"
                  style="width:40px;height:40px"
                >
                <avatar v-else :username="participant.userName" :size="40" :rounded="false"></avatar>
                </div>
                <div class="memberDetail">
                  <div class="memberName">
                    <span>{{participant.userName}}</span>
                  </div>
                  <div class="memberOrganization">
                    <span>{{participant.organization}}</span>
                  </div>
                </div>
              </div>
            </Card>

            <!-- <div v-for="(participant,index) in participants" :key="index" class="f_list">
              <div>
                <img
                  v-if="participant.avatar != '' && participant.avatar!='undefined'"
                  :src="participant.avatar"
                  style="width:35px;height:35px"
                >
                <avatar v-else :username="participant.userName" :size="35" :rounded="false"></avatar>
              </div>
              <div class="name">{{participant.userName}}</div>
            </div> -->
          </div>
        </div>
        <div class="contentPanel">
          <div class="contentHeader">
            <div class="chatObject">
              <div class="s_name" v-show="this.select_group!==''">{{this.select_group}}</div>
            </div>
            <div class="chatOperate">
              <Button type="default" class="recordsButton" @click="showRecords">Records</Button>
            </div>
          </div>
          <div class="contentBody" :style="message_panelObj" id="contentBody">
            <div style="display:flex" v-for="(list,index) in msglist" :key="index">
              <template v-if="list.fromid === thisUserId">
                <div style="width:95%">
                  <div class="chat-bubble-r chat-bubble-right">{{list.content}}</div>
                </div>
                <div class="user_detail">
                  <div class="u_img">
                    <avatar class="user_img" :username="list.from" :size="35"></avatar>
                  </div>
                  <div class="u_name">{{list.from}}</div>
                </div>
              </template>
              <template v-else>
                <div class="user_detail" style="margin-left:2.5%;margin-top:5px;">
                  <div class="u_img">
                    <avatar class="user_img" :username="list.from" :size="35"></avatar>
                  </div>
                  <div class="u_name">
                    <span style="font-size:3px;">{{list.from}}</span>
                  </div>
                </div>
                <div style="width:95%">
                  <div class="chat-bubble-l chat-bubble-left">{{list.content}}</div>
                </div>
              </template>
            </div>
          </div>
          <div class="contentFooter">
            <div class="message_bar">
              <div class="u_avater">
                <avatar class="user_img" username="m e" :size="40" style="margin-top: -4px;"></avatar>
              </div>
              <div class="input_panel">
                <Input
                  placeholder="Enter message..."
                  icon="ios-link"
                  class="message_input"
                  v-model="message"
                  @keyup.enter.native="send(message)"
                />
              </div>
              <div class="send_panel">
                <Button class="send_message_btn" @click="send(message)">Send</Button>
              </div>
            </div>
          </div>
        </div>
        <div class="searchPanel" v-show="searchPanelShow" style="border:1px solid lightgray">
          <div class="searchHeader">
            <p>Message Records</p>
          </div>
          <div style="display:flex">
            <DatePicker
              type="date"
              placeholder="Select date"
              class="date_pick"
              v-model="query_date"
            ></DatePicker>
            <Button class="date_pick_btn" type="success" @click="find(query_date)">ok</Button>
          </div>
          <div class="search_msg">
            <Input search placeholder="Enter something..."/>
          </div>
          <div
            class="searchmessageList"
            :style="{height:messageListPanelHeight}"
            id="searchmessageList"
          >
            <div class="message_record_board">
              <div style="display:flex" v-for="(list,index) in msgRecords" :key="index">
                <div class="single_record">
                  <span style="color:#2d8cf0; margin-right:2%">{{list.from}}:</span>
                  <span style="color:lightgray; margin-right:2%">{{list.time}}</span>
                  <div style="color:gray">{{list.content}}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </Col>
  </Row>
</template>
<script>
import * as socketApi from "./../../api/socket.js";
import Avatar from "vue-avatar";
export default {
  components: {
    Avatar
  },
  data() {
    return {
      chatPanelHeight: window.innerHeight + "px",
      searchPanelShow: false,
      message_panelObj: {
        // grid:1,
        right: "300px",
        borderRight: "0px",
        searchPanelShow: false
      },
      // 原有的变量字段
      projectId: "",
      subProjectId: "",
      moduleId: "",
      participants: [],
      olParticipants: [],
      groups: [],
      select_group: "",
      select_groupId: "",
      message: "",
      my_msglist: [],
      other_msglist: [],
      msglist: [],
      msgRecords: [],
      send_msg: [],
      query_date: "",
      thisUserName: this.$store.getters.userName,
      thisUserId: this.$store.getters.userId,
      panelHeight: "",
      panelWidth:"",
      messageListPanelHeight: "",
      seletRoom: "module",
      // 下拉框的测试数据
      itemList: [
        {
          value:"Module",
          label:"Module"
        },
        {
          value:"Subproject",
          label:"Subproject"
        },
        {
          value:"Project",
          label:"Project"
        },
      ],
      selectItem: "Module"
    };
  },
  methods: {
    init() {
      this.initSize();
      this.moduleId = sessionStorage.getItem("moduleId");
      this.subProjectId = sessionStorage.getItem("subProjectId");
      this.projectId = sessionStorage.getItem("projectId");
      this.moduleName = sessionStorage.getItem("moduleName");
      this.subProjectName = sessionStorage.getItem("subProjectName");
      this.projectName = sessionStorage.getItem("projectName");
      //groups
      this.groups = [
        {
          name: this.moduleName,
          id: this.moduleId,
          scope: "Module",
          title: "Chat in the module"
        },
        {
          name: this.subProjectName,
          id: this.subProjectId,
          scope: "Subproject",
          title: "Chat in the subproject"
        },
        {
          name: this.projectName,
          id: this.projectId,
          scope: "Project",
          title: "Chat in the project"
        }
      ];
    },
    changeChatroom(index) {
      this.socketApi.close();
      if (index == 0) {
        if(this.moduleId == null || this.moduleId == undefined || this.moduleId == ""){
          this.$Notice.open({
            desc: "No information about this module",
          });
          return;
        }
        this.participants = [];
        this.msglist = [];
        this.msgRecords = [];
        this.select_group = this.moduleName;
        this.select_groupId = this.moduleId;
        this.startWebSocket(this.moduleId);
        this.seletRoom = "module";
      } else if (index == 1) {
        if(this.subProjectId == null || this.subProjectId == undefined || this.subProjectId == ""){
          this.$Notice.open({
            desc: "No information about this subproject",
          });
          return;
        }
        this.participants = [];
        this.msglist = [];
        this.msgRecords = [];
        this.getParticipants(this.subProjectId, "subproject");
        this.select_group = this.subProjectName;
        this.select_groupId = this.subProjectId;
        this.startWebSocket(this.subProjectId);
        this.seletRoom = "subproject";
      } else if (index == 2) {
        if(this.projectId == null || this.projectId == undefined || this.projectId == ""){
          this.$Notice.open({
            desc: "No information about this project",
          });
          return;
        }
        this.participants = [];
        this.msglist = [];
        this.msgRecords = [];
        this.getParticipants(this.projectId, "project");
        this.select_group = this.projectName;
        this.select_groupId = this.projectId;
        this.startWebSocket(this.projectId);
        this.seletRoom = "project";
      }
    },
    getParticipants(id, scope) {
      if (scope == "subproject") {
        let that = this;
        $.ajax({
          url:
            "/GeoProblemSolving/subProject/inquiry" +
            "?key=subProjectId" +
            "&value=" +
            that.subProjectId,
          type: "GET",
          success: data => {
            if (data != "None" && data != "Fail" && data != "Offline") {
              let subProjectInfo = data[0];
              let membersList = subProjectInfo["members"];
              let manager = { userId: subProjectInfo["managerId"] };
              membersList.unshift(manager);
              let participantsTemp = [];
              for (let i = 0; i < membersList.length; i++) {
                $.ajax({
                  url:
                    "/GeoProblemSolving/user/inquiry" +
                    "?key=" +
                    "userId" +
                    "&value=" +
                    membersList[i].userId,
                  type: "GET",
                  async: false,
                  success: function(data) {
                    participantsTemp.push(data);
                  }
                });
              }
              that.$set(that, "participants", participantsTemp);
            }
          },
          error: function(err) {
            console.log("Get manager name fail.");
          }
        });
      } else if (scope == "project") {
        let that = this;
        let queryObject = { key: "projectId", value: this.projectId };
        try {
          $.ajax({
            url:
              "/GeoProblemSolving/project/inquiry" +
              "?key=" +
              queryObject["key"] +
              "&value=" +
              queryObject["value"],
            type: "GET",
            success: function(data) {
              if (data != "None" && data != "Fail") {
                let projectInfo = data[0];
                let membersList = projectInfo["members"];
                let manager = { userId: projectInfo["managerId"] };

                membersList.unshift(manager);
                let participantsTemp = [];
                for (let i = 0; i < membersList.length; i++) {
                  $.ajax({
                    url:
                      "/GeoProblemSolving/user/inquiry" +
                      "?key=" +
                      "userId" +
                      "&value=" +
                      membersList[i].userId,
                    type: "GET",
                    async: false,
                    success: function(data) {
                      participantsTemp.push(data);
                    },
                    error: function(err) {
                      console.log("Get manager name failed.");
                    }
                  });
                }
                that.$set(that, "participants", participantsTemp);
              }
            },
            error: function(err) {
              console.log("Get manager name fail.");
            }
          });
        } catch (err) {
          console.log(err);
        }
      }
    },
    olParticipantChange() {
      let userIndex = -1;

      // 自己刚上线，olParticipants空
      if (this.participants.length == 0) {
        var that = this;
        for (let i = 0; i < this.olParticipants.length; i++) {
          this.axios
            .get(
              "/GeoProblemSolving/user/inquiry" +
                "?key=" +
                "userId" +
                "&value=" +
                this.olParticipants[i]
            )
            .then(res => {
              if (res.data != "None" && res.data != "Fail") {
                that.participants.push(res.data);
              } else if (res.data == "None") {
              }
            });
        }
      } else {
        // members大于olParticipants，有人上线；小于olParticipants，离线
        if (this.olParticipants.length > this.participants.length) {
          for (var i = 0; i < this.olParticipants.length; i++) {
            for (var j = 0; j < this.participants.length; j++) {
              if (this.olParticipants[i] == this.participants[j].userId) {
                break;
              }
            }
            if (j == this.participants.length) {
              userIndex = i;
              break;
            }
          }

          // 人员渲染
          var that = this;
          this.axios
            .get(
              "/GeoProblemSolving/user/inquiry" +
                "?key=" +
                "userId" +
                "&value=" +
                this.olParticipants[userIndex]
            )
            .then(res => {
              if (res.data != "None" && res.data != "Fail") {
                that.participants.push(res.data);
                if (userIndex != -1) {
                }
              } else if (res.data == "None") {
              }
            });
        } else if (this.olParticipants.length < this.participants.length) {
          for (var i = 0; i < this.participants.length; i++) {
            for (var j = 0; j < this.olParticipants.length; j++) {
              if (this.participants[i].userId == this.olParticipants[j]) {
                break;
              }
            }
            if (j == this.olParticipants.length) {
              userIndex = i;
              break;
            }
          }
          this.participants.splice(userIndex, 1);
        }
      }
    },
    showRecords() {
      this.searchPanelShow = !this.searchPanelShow;
      this.message_panelObj["right"] = 0;

      if (this.searchPanelShow) {
        this.msgRecords = [];
        let that = this;
        this.axios
          .get(
            "/GeoProblemSolving/message/inquiry" +
              "?type=message" +
              "&key=roomId" +
              "&value=" +
              this.select_groupId
          )
          .then(res => {
            if (res.data != "None" && res.data != "Fail") {
              for (let i = 0; i < res.data.length; i++) {
                let message = JSON.parse(res.data[i].content);
                that.msgRecords.push(message);
              }
            }
          });
      }
    },
    send(msg) {
      this.message = msg;
      let myDate = new Date();
      let current_time = myDate.toLocaleString(); //获取日期与时间

      // 消息不为空
      if (this.message !== "") {
        this.send_msg = {
          type: "message",
          from: this.$store.getters.userName,
          fromid: this.$store.getters.userId,
          content: this.message,
          time: current_time
        };

        this.my_msglist.push(this.send_msg);
        this.msglist.push(this.send_msg);
        this.msgRecords.push(this.send_msg);
        this.socketApi.sendSock(this.send_msg, this.getSocketConnect);
      }
      this.message = "";
    },
    getSocketConnect(data) {
      var chatMsg = data;
      if (data.type === "members") {
        let members = data.message
          .replace("[", "")
          .replace("]", "")
          .replace(/\s/g, "")
          .split(",");
        this.olParticipants = members;
        if (this.seletRoom == "module") {
          this.olParticipantChange();
        }
      } else if (data.type === "message") {
        //判断消息的发出者
        if (chatMsg.content != "") {
          this.other_msglist.push(chatMsg);
          this.msglist.push(chatMsg);
          this.msgRecords.push(chatMsg);
        }
      } else if (chatMsg.type == undefined && chatMsg.length > 0) {
        for (let i = 0; i < chatMsg.length; i++) {
          if (chatMsg[i].content != "") {
            this.other_msglist.push(chatMsg[i]);
            this.msglist.push(chatMsg[i]);
            this.msgRecords.push(chatMsg[i]);
          }
        }
      }
    },
    startWebSocket(id) {
      this.socketApi.initWebSocket("ChatServer/" + id,this.$store.state.IP_Port);

      this.send_msg = {
        type: "test",
        from: "Test",
        content: "TestChat"
      };
      this.socketApi.sendSock(this.send_msg, this.getSocketConnect);
    },
    find(date) {
      let q_date = date.toLocaleDateString();
      console.log(q_date);
    },
    initSize() {
      if(window.innerHeight > 675) {
        this.panelHeight = window.innerHeight;
      }
      else{
        this.panelHeight = 675 ;
      }
      if(window.innerWidth > 1200){
        this.panelWidth = window.innerWidth;
      }
      else{
        this.panelWidth = 1200;
      }
      this.messageListPanelHeight = this.panelHeight - 144 + "px";
    }
  },
  mounted() {
    window.addEventListener("resize", this.initSize);
    this.init();
    //init module
    this.participants = [];
    this.select_group = this.moduleName;
    this.select_groupId = this.moduleId;
    this.startWebSocket(this.moduleId);
    this.seletRoom = "module";
  },
  beforeDestroy() {
    this.socketApi.close();
    window.removeEventListener("resize", this.initSize);
  },
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      if (!vm.$store.getters.userState || vm.$store.getters.userId == "") {
        vm.$router.push({ name: "Login" });
      } else {
      }
    });
  },
  updated: function() {
    this.$nextTick(function() {
      var div = document.getElementById("contentBody");
      var div2 = document.getElementById("searchmessageList");
      div.scrollTop = div.scrollHeight - 60;
      div2.scrollTop = div.scrollHeight;
    });
  }
};
</script>
