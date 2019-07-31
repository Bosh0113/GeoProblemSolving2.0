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
.chat-notice{
  color: darkgreen;
  position: absolute;
  left: 50%;
}
.offlineparticipantState{
  background:rgba(255,255,255,0.4);
}
.onlineparticipantState{
  background:rgba(18, 158, 71, 0.4);
}
.onlinecircle{
        width: 14px;      
        height: 14px;      
        background-color:rgb(93, 243, 79);    
        border:2px solid rgb(221, 245, 221);  
        border-radius: 50%;      
        -moz-border-radius: 50%;      
        -webkit-border-radius: 50%;
        position:absolute; 
        bottom:-2px; 
        right: -2px
       }
.offlinecircle{
        width: 14px;      
        height: 14px;      
        background-color:rgb(151, 153, 152);
        border:2px solid rgb(175, 175, 175);       
        border-radius: 50%;      
        -moz-border-radius: 50%;      
        -webkit-border-radius: 50%;
        position:absolute; 
        bottom:-2px; 
        right: -2px
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
          <!-- 选择聊天室 -->
          <Select v-model="selectItem" style="width:195px;margin-left:2.5px;margin-right:2.5px">
            <Option v-for="(item,index) in itemList" :value="item.value" :key="item.index" @click.native="changeChatroom(index)">{{ item.label }}</Option>
          </Select>

          <!-- 参与者 -->
          <div class="participants">
            <h4>Participants</h4>
            <div>
            <Card v-for="(participant,index) in onlineParticipants" :key="'online' +index" style="margin:2.5%" :padding="5"  >
              <div style="display:flex;align-items:center">
                <div class="memberImg" style="position:relative">
                    <img v-if="participant.avatar != '' && participant.avatar!='undefined'"  :src="participant.avatar" style="width:40px;height:40px" >                 
                    <avatar v-else :username="participant.userName" :size="40" :rounded="false"></avatar>
                    <div class="onlinecircle" ></div>
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
             <Card v-for="(participant,index) in offlineParticipants" :key=" 'offline' + index" style="margin:2.5%" :padding="5"  >
              <div style="display:flex;align-items:center">
                <div class="memberImg" style="position:relative">
                    <img v-if="participant.avatar != '' && participant.avatar!='undefined'"  :src="participant.avatar" style="width:40px;height:40px" >                 
                    <avatar v-else :username="participant.userName" :size="40" :rounded="false"></avatar>
                    <div class="offlinecircle" ></div>
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
               <template v-if="list.type === 'notice'">
                  <div class="chat-notice" :style="message_notice">{{list.message}}上线</div>
              </template>
              <template v-else-if="list.fromid === thisUserId ">
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
//import notification from '../utils/notification';


//notice


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
      message_notice:{
        color:"green",
        right: "0px",
        borderRight: "0px",
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
      notice_msglist:[],
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
      selectItem: "Module",
      onlineParticipants:[],
      onlineP:[],
      offlineParticipants:[],
      parMessage:[],
      windowStatus :'focus',//监听浏览器失焦事件
    };
  },

  mounted() {    
    
    window.addEventListener('blur', this.winBlur);//监听浏览器失焦事件    
    window.addEventListener('focus', this.winFocus);//监听浏览器失焦事件
    window.addEventListener("resize", this.initSize);   

    this.init();
    //init module
    this.participants = [];
    this.select_group = this.moduleName;
    this.select_groupId = this.moduleId;
    this.startWebSocket(this.moduleId);
    this.seletRoom = "module";
    this.supportNotify();
   
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
        this.startWebSocket(this.projectId, "project");
        this.seletRoom = "project";
      };
         
      
    },

    getParticipants(id, scope) {
      if (scope == "subproject") {
        //let that = this;
        $.ajax({
          url:
            "/GeoProblemSolving/subProject/inquiry" +
            "?key=subProjectId" +
            "&value=" +
            that.subProjectId,
          type: "GET",
          async: false,
          success: data => {
            if (data != "None" && data != "Fail" && data != "Offline") {
              let subProjectInfo = data[0];
              let membersList = subProjectInfo["members"];
              let manager = { userId: subProjectInfo["managerId"] };
              membersList.unshift(manager);//在List开头加入manager元素
              let participantsTemp = [];
              for (let i = 0; i < membersList.length; i++) {
                //查找数据库中userId和subProject里面的成员相同的
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
              this.$set(this, "participants", participantsTemp);
              //this.judgeonlineParticipant(this.participants);
            }
          },
          error: function(err) {
            console.log("Get manager name fail.");
          }
        });
      } else if (scope == "project") {
        let that = this;
        console.log(this.onlineParticipants);
        let queryObject = { key: "projectId", value: this.projectId };    
        $.ajax({
          url:
           "/GeoProblemSolving/project/inquiry" +
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
                let manager = { userId: projectInfo["managerId"] };

                membersList.unshift(manager);
                let participantsTemp = [];
                let count = membersList.length;
                for (let i = 0; i < membersList.length; i++) {
                  //  this.axios.get(
                  //     "/GeoProblemSolving/user/inquiry" +
                  //     "?key=" +
                  //     "userId" +
                  //     "&value=" +
                  //     membersList[i].userId
                  //     )
                  //     .then(res=>{
                  //       participantsTemp.push(res.data);
                  //       if(--count==0){
                  //         //this.$set(this, "participants", participantsTemp);//获取项目所有成员
                  //         this.participants = participantsTemp;
                  //         console.log(this.onlineParticipants);
                  //         this.judgeonlineParticipant(this.onlineParticipants);
                  //       }
                  //     });
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
                this.$set(this, "participants", participantsTemp);
                 console.log(this.participants);
              }
            }
          });
         
        }
    },  

    olParticipantChange() {
      let userIndex = -1;
      // 自己刚上线，olParticipants空
      if (this.participants.length == 0) {
        var that = this;
        for (let i = 0; i < this.olParticipants.length; i++) {
          //在库中查找到userId=this.olParticipants[i]的user；
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
                that.participants.push(res.data);//向数组的末尾添加res.data元素
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
                that.participants.push(res.data);//向participants的末尾添加res.data
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
          this.participants.splice(userIndex, 1);  //删除一个元素，返回userIndex数组      
          //console.log(this.participants);
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
       console.log(msg);
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
        this.socketApi.sendSock(this.send_msg, this.getSocketConnect);//连接后台onopen方法
      }
      this.message = "";
    },   
    
    startWebSocket(id, scope) {
      // this.getParticipants(id, scope);
      this.socketApi.initWebSocket("ChatServer/" + id,this.$store.state.IP_Port);
      this.send_msg = {
        type: "test",
        from: "Test",
        content: "TestChat"
      };
      this.socketApi.sendSock(this.send_msg, this.getSocketConnect);
    }, 
    
    getSocketConnect(data) {
      var chatMsg = data;//data传回onopen方法里的值
      if (data.type === "members") {
        let members = data.message
          .replace("[", "")
          .replace("]", "")
          .replace(/\s/g, "")
          .split(",");
          //this.$set(this,"onlineParticipants",members);
          this.onlineParticipants=members;
          console.log(this.onlineParticipants);
          this.judgeonlineParticipant(members);
        if (this.seletRoom == "project") {
          //this.judgeonlineParticipant(this.onlineParticipants);   
        }
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

    //传过来的是ID 根据ID获得用户所有的属性值
    getParMessage(member){
      this.parMessage=[];
      let count = member.length;
      for (let i = 0; i < member.length; i++) {
        // 异步-->转同步
       this.axios
          .get(
              "/GeoProblemSolving/user/inquiry" +
                "?key=" +
                "userId" +
                "&value=" +
                member[i]
            )
            .then(res => {
              if (res.data != "None" && res.data != "Fail") {
                this.parMessage.push(res.data);
                 if(--count==0){
                    this.$set(this,"onlineParticipants",this.parMessage);
                    this.$set(this,"olParticipants",this.parMessage);
                    console.log(this.onlineParticipants);
                  }
              } else if (res.data == "None") {
            };              
          }); 
      };      
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
    },

    //判断在线的用户列表
    judgeonlineParticipant(onlineparticipants){ 
      console.log(this.participants);
      console.log(this.onlineParticipants); 
      var that=this;     

     // let onlineparticipants=this.onlineParticipants; //online成员 ID
      let participants=this.participants; //项目所有成员

      let offline=[];//不在线成员过渡
      let online=[];//在线成员过渡
      var index=0;//不在线成员index

      for (var i= 0 ; i<participants.length ; i++) {
        for (var j = 0; j < onlineparticipants.length; j++) {
          if (participants[i].userId === onlineparticipants[j]) {
            online.push(participants[i]);            
            break;
          }          
          if (j == (onlineparticipants.length-1)) {
            index = i;
            offline.push(participants[index]);  //删除一个元素，返回userIndex数组      
            break;
          }
        }
      };
      this.offlineParticipants=[];
      this.$set(this,"offlineParticipants",offline);
      this.$set(this,"onlineParticipants",online);
      console.log(offline);
      console.log(online);
    },

    winBlur(){
      this.windowStatus = 'blur';
    },
    winFocus(){
      this.windowStatus = 'focus';
    },

    supportNotify() {
        //if (window.Notification && (window.Notification.permission === 'default' || window.Notification.permission === 'denied')) {
        if (window.Notification) {
            window.Notification.requestPermission();
        }
      },

    
    sendNotify(msg){
        if (this.windowStatus === 'blur') {
             var notify = new Notification(
              msg.fromid,
              {
                // tag:"tag",
                // icon:'http://******.png',//通知的缩略图,//icon 支持ico、png、jpg、jpeg格式
                body:msg.content //通知的具体内容
              }
            );
            console.log(notify);

            //通知显示
            notify.onshow = function () {
                //5s自行停止通知
                setTimeout(notify.close.bind(notify), 5000);
            };

            //单击通知，跳转到页面
            notify.onclick=function(){
              window.focus();
              notify.close();
            };

            //报错处理
            notify.onerror = EventListener;           

            //通知关闭
            notify.onclose = function(){
              console.log("HTML5桌面消息关闭！！！");
            };
      
        }        
    }, 
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
  },
  
};
</script>
