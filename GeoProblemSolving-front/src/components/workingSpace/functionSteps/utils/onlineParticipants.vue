<style scoped>
.chatPanel {
  display: flex;
  overflow-y: hidden;
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
  color: #515a6e;
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
  right: -2px;
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
  right: -2px;
}
</style>
<template>
  <div>
    <div class="memberPanel">
      <!-- 参与者 -->
      <div class="participants">
        <div>
          <Card
            v-for="(participant,index) in onlineParticipants"
            :key="'online' +index"
            style="margin:2.5%"
            :padding="5"
          >
            <div style="display:flex;align-items:center">
              <div class="memberImg" style="position:relative">
                <img
                  v-if="participant.avatar != '' && participant.avatar!='undefined'"
                  :src="participant.avatar"
                  style="width:40px;height:40px"
                />
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
          <Card
            v-for="(participant,index) in offlineParticipants"
            :key=" 'offline' + index"
            style="margin:2.5%"
            :padding="5"
          >
            <div style="display:flex;align-items:center">
              <div class="memberImg" style="position:relative">
                <img
                  v-if="participant.avatar != '' && participant.avatar!='undefined'"
                  :src="participant.avatar"
                  style="width:40px;height:40px"
                />
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
  </div>
</template>


<script>
import Avatar from "vue-avatar";

export default {
  components: {
    Avatar
  },
  props: ["onlineParticipants", "offlineParticipants"],

  data() {
    return {
    };
  },
  mounted() {
  },
  beforeDestroy() {
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
  updated: function() {},
  methods: {
  }
};
</script>
