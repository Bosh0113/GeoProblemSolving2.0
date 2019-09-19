<style scoped>
  .chatPanel {
    display: flex;
    overflow-y: hidden
  }

  /* member部分样式 */
  .memberPanel {
    width: 200px;
    /* background-color: rgba(0,0,0,0.1); */
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
    color:  #515a6e;;
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

  .message_record_page {
    height: 20px;
    padding: 10px;
    line-height: 15px;
    text-align: center;
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

  .memberImg {
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

  .memberName span {
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
    max-width: 50%;
  }

  .chat-notice {
    color: darkgreen;
    position: absolute;
    left: 50%;
  }

  .offlineparticipantState {
    background: rgba(255, 255, 255, 0.4);
  }

  .onlineparticipantState {
    background: rgba(18, 158, 71, 0.4);
  }

  .onlinecircle {
    width: 14px;
    height: 14px;
    background-color: rgb(93, 243, 79);
    border: 2px solid rgb(221, 245, 221);
    border-radius: 50%;
    -moz-border-radius: 50%;
    -webkit-border-radius: 50%;
    position: absolute;
    bottom: -2px;
    right: -2px
  }

  .offlinecircle {
    width: 14px;
    height: 14px;
    background-color: rgb(151, 153, 152);
    border: 2px solid rgb(175, 175, 175);
    border-radius: 50%;
    -moz-border-radius: 50%;
    -webkit-border-radius: 50%;
    position: absolute;
    bottom: -2px;
    right: -2px
  }

</style>
<template>
  <div>   
    <!-- <div class="chatPanel" :style="{height:panelHeight+'px',width:panelWidth+'px'}"> -->
      <div class="memberPanel">
        <!-- 参与者 -->
        <div class="participants">
          <div>
            <Card v-for="(participant,index) in onlineParticipants" :key="'online' +index" style="margin:2.5%"
              :padding="5">
              <div style="display:flex;align-items:center">
                <div class="memberImg" style="position:relative">
                  <img v-if="participant.avatar != '' && participant.avatar!='undefined'" :src="participant.avatar"
                    style="width:40px;height:40px">
                  <avatar v-else :username="participant.userName" :size="40" :rounded="false"></avatar>
                  <div class="onlinecircle"></div>
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
          </div>
          <div>
            <Card v-for="(participant,index) in offlineParticipants" :key=" 'offline' + index" style="margin:2.5%"
              :padding="5">
              <div style="display:flex;align-items:center">
                <div class="memberImg" style="position:relative">
                  <img v-if="participant.avatar != '' && participant.avatar!='undefined'" :src="participant.avatar"
                    style="width:40px;height:40px">
                  <avatar v-else :username="participant.userName" :size="40" :rounded="false"></avatar>
                  <div class="offlinecircle"></div>
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
          </div>
        </div>
      </div>
    <!-- </div>    -->
  </div>
</template>


<script>
  import * as socketApi from "./../../api/socket.js";
  import Avatar from "vue-avatar";

  export default {
    components: {
      Avatar
    },
     props:["subProjectId","roomId"],

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
        message_notice: {
          color: "green",
          right: "0px",
          borderRight: "0px",
        },
        // 原有的变量字段
        projectId: "",
        // subProjectId: "",
        moduleId: "",
        participants: [],
        groups: [],
        message: "",
        my_msglist: [],
        other_msglist: [],
        notice_msglist: [],
        msglist: [],



        send_msg: [],
        query_date: "",
        thisUserName: this.$store.getters.userName,
        thisUserId: this.$store.getters.userId,
        panelHeight: "",
        panelWidth: "",
        messageListPanelHeight: "",
       
        onlineParticipants: [],
        onlineP: [],
        offlineParticipants: [],
        parMessage: [],
      };
    },

    mounted() {
      window.addEventListener("resize", this.initSize);

      this.initSize();
      this.participants = [];
     
      this.getParticipants(this.subProjectId);
      this.startWebSocket(this.roomId);
      
      console.log(this.subProjectId);

    },

    methods: {
      getParticipants(id) {
        let queryObject = {
          key: "subProjectId",
          value: this.subProjectId
        };
        $.ajax({
          url: "/GeoProblemSolving/subProject/inquiry" +
            "?key=" +
            queryObject["key"] +
            "&value=" +
            queryObject["value"],
          type: "GET",
          async: false,
          success: data => {
            if (data != "None" && data != "Fail") {
              let projectInfo = data[0];
              let membersList = projectInfo["members"];
              let manager = {
                userId: projectInfo["managerId"],
                userName: projectInfo["managerName"]
              };

              membersList.unshift(manager);
              let participantsTemp = [];
              let count = membersList.length;
              for (let i = 0; i < membersList.length; i++) {
                $.ajax({
                  url: "/GeoProblemSolving/user/inquiry" +
                    "?key=" +
                    "userId" +
                    "&value=" +
                    membersList[i].userId,
                  type: "GET",
                  async: false,
                  success: function (data) {
                    participantsTemp.push(data);
                  }
                });
              }
              this.$set(this, "participants", participantsTemp);
              console.log(this.participants);
            }
          }
        });

      },

     
      startWebSocket(id) {
        // this.getParticipants(id, scope);
        this.socketApi.initWebSocket("ChatServer/" + id, this.$store.state.IP_Port);
        this.send_msg = {
          type: "test",
          from: "Test",
          content: "TestChat"
        };
        this.socketApi.sendSock(this.send_msg, this.getSocketConnect);
      },

      getSocketConnect(data) {
        var chatMsg = data; //data传回onopen方法里的值
        if (data.type === "members") {
          let members = data.message
            .replace("[", "")
            .replace("]", "")
            .replace(/\s/g, "")
            .split(",");
        
          this.onlineParticipants = members;
          console.log(this.onlineParticipants);
          this.judgeonlineParticipant(members);
       
        } else if (data.type === "message") {
          //判断消息的发出者
          if (chatMsg.content != "") {
            console.log(chatMsg);
            this.other_msglist.push(chatMsg);
            this.msglist.push(chatMsg);
            this.msgRecords.push(chatMsg);
            this.sendNotify(chatMsg);
          }
        } else if (data.type === "notice") {
          //上线下线提示
          if (chatMsg.content != "") {
            this.msglist.push(chatMsg);
            this.notice_msglist.push(chatMsg);
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

      initSize() {
        if (window.innerHeight > 675) {
          this.panelHeight = window.innerHeight;
        } else {
          this.panelHeight = 675;
        }
        if (window.innerWidth > 1200) {
          this.panelWidth = window.innerWidth;
        } else {
          this.panelWidth = 1200;
        }
        this.messageListPanelHeight = this.panelHeight - 144 - 40 + "px";
      },

      //判断在线的用户列表
      judgeonlineParticipant(onlineparticipants) {
        let participants = this.participants; //项目所有成员

        let offline = []; //不在线成员过渡
        let online = []; //在线成员过渡
        var index = 0; //不在线成员index

        for (var i = 0; i < participants.length; i++) {
          for (var j = 0; j < onlineparticipants.length; j++) {
            if (participants[i].userId === onlineparticipants[j]) {
              online.push(participants[i]);
              break;
            }
            if (j == (onlineparticipants.length - 1)) {
              index = i;
              offline.push(participants[index]); //删除一个元素，返回userIndex数组      
              break;
            }
          }
        };
        this.offlineParticipants = [];
        this.$set(this, "offlineParticipants", offline);
        this.$set(this, "onlineParticipants", online);
        console.log(this.onlineparticipants);
        console.log(this.offlineparticipants);
      }, 

      getSubProjectId(id){
         this.axios
          .get(
            "/GeoProblemSolving/step/inquiry/" +
            "?key=stepId" +
            "&value=" +
            id
          )
          .then(res => {            
            this.subProjectId = res.data[0].subProjectId;
            console.log(this.subProjectId);
          });
      }
    },

    beforeDestroy() {
      this.socketApi.close();
      window.removeEventListener("resize", this.initSize);
    },
    beforeRouteEnter: (to, from, next) => {
      next(vm => {
        if (!vm.$store.getters.userState || vm.$store.getters.userId == "") {
          vm.$router.push({
            name: "Login"
          });
        } else {}
      });
    },
    updated: function () {
      this.$nextTick(function () {
        var div = document.getElementById("contentBody");
        var div2 = document.getElementById("searchmessageList");
        div.scrollTop = div.scrollHeight - 60;
        div2.scrollTop = div.scrollHeight;
      });
    },

  };

</script>
