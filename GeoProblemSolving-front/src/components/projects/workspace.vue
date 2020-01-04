<style scoped>
.btnHoverRed:hover {
  background-color: #ed4014;
  color: white;
}
.shareLabel {
  display: block;
  width: 50px;
  float: left;
  margin-top: 8px;
  text-align: right;
  font-weight: bold;
}
</style>

<template>
  <div>
    <Card dis-hover>
      <h1 slot="title">Workspace</h1>
      <div slot="extra">
        <Button icon="ios-share-alt" @click="shareModal=true" v-show="userRole=='Manager'">Share</Button>
      </div>
      <div style="display: flex;">
        <div style="width:300px;height:735px">
          <vue-scroll :ops="ops">
            <tool-container :stepInfo="stepInfo" :userRole="userRole"></tool-container>
          </vue-scroll>
        </div>
        <div style="margin-left:10px" :style="{width:resourceWidth+'px'}">
          <data-list :stepInfo="stepInfo" :userRole="userRole"></data-list>
        </div>
      </div>
      <div style="margin:5px 0 5px 80px;text-align:center">
        <Button
          class="btnHoverRed"
          @click="resetProjectTypeModalShow()"
          v-show="userRole=='Manager'"
        >Reset project type</Button>
      </div>
    </Card>
    <Modal v-model="resetProjectTypeModel" title="Reset project type">
      <h2>Are you sure you want to reset the project type?</h2>
      <small style="color:red">* All operation and data in this page can't be restore any more.</small>
      <div slot="footer">
        <Button type="primary" @click="resetProjectType()">Submit</Button>
      </div>
    </Modal>
    <Modal v-model="shareModal" title="Share workspace" footer-hide>
      <div style="margin-bottom:20px">
        <span class="shareLabel">Token:</span>
        <Input v-model="sharedToken" readonly style="width: 300px;margin-left:10px" />
        <Button
          type="primary"
          @click="generateToken()"
          style="margin-right:30px;float:right"
        >Generate</Button>
      </div>
      <div style="margin-bottom:20px">
        <span class="shareLabel">Url:</span>
        <Input v-model="sharedUrl" readonly style="width:300px;margin-left:10px" />
        <Button type="primary" @click="copySharedUrl()" style="margin-right:30px;float:right">Copy</Button>
      </div>
      <div style="margin-bottom:20px">
        <span class="shareLabel">Email:</span>
        <Input v-model="sharingEmail" placeholder="Plase enter the email address" style="width: 300px;margin-left:10px" />
        <Button type="primary" @click="shareWorkspace()" style="margin-right:30px;float:right">Share</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
import dataList from "./../workingSpace/noStep/dataList";
import toolContainer from "./../workingSpace/noStep/toolContainer";
import onlineParticipant from "./../workingSpace/functionSteps/utils/onlineParticipants";
export default {
  props: ["projectInfo", "userRole"],
  components: {
    dataList,
    toolContainer,
    onlineParticipant
  },
  created() {
    this.getStepInfo();
    window.addEventListener("resize", this.initSize);
  },
  data() {
    return {
      ops: {
        bar: {
          background: "#808695"
        }
      },
      stepInfo: {},
      unfold: ["tool", "data"],
      participants: [],
      // onlineParticipants.vue
      onlineUserIdList: [],
      onlineParticipants: [],
      offlineParticipants: [],
      // messagePanel.vue -- chat
      receivedChatMsgs: [],
      resetProjectTypeModel: false,
      //for share
      shareModal: false,
      sharedUrl: "",
      sharedToken: "",
      sharingEmail: "",
      resourceWidth: window.innerWidth - 350
    };
  },
  methods: {
    initSize() {
      if (window.innerWidth > 1100) {
        this.resourceWidth = window.innerWidth - 350;
      }
    },
    getStepInfo() {
      this.axios
        .get(
          "/GeoProblemSolving/step/inquiry?key=stepId&value=" +
            this.projectInfo.stepId
        )
        .then(res => {
          if (res.data == "Offline") {
            parent.location.href = "/GeoProblemSolving/login";
          } else if (res.data != "None" && res.data != "Fail") {
            this.$set(this, "stepInfo", res.data[0]);
            this.getParticipants();
          }
        })
        .catch(err => {});
    },
    resetProjectTypeModalShow() {
      this.resetProjectTypeModel = true;
    },
    resetProjectType() {
      let obj = new URLSearchParams();
      obj.append("projectId", this.stepInfo.projectId);
      obj.append("type", "");
      obj.append("stepId", "");
      this.axios
        .post("/GeoProblemSolving/project/update", obj)
        .then(res => {
          this.resetProjectTypeModel = false;
          if (res.data == "Offline") {
            parent.location.href = "/GeoProblemSolving/login";
          } else if (res.data != "Fail") {
            this.$store.commit("setProjectInfo", res.data);
            this.$emit("changeProjectInfo", res.data);
          } else {
            this.$Message.error("Set type failed.");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    getParticipants() {
      let members = this.projectInfo.members;
      let manager = [
        {
          userId: this.projectInfo["managerId"],
          userName: this.projectInfo["managerName"]
        }
      ];
      let membersList = manager.concat(members);

      let participantsTemp = [];
      let count = membersList.length;
      for (var i = 0; i < membersList.length; i++) {
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
      this.startWebSocket();
    },
    // websocket
    startWebSocket() {
      this.socketApi.initWebSocket(
        "ChatServer/" + this.stepInfo.stepId,
        this.$store.state.IP_Port
      );
      this.send_msg = {
        type: "test",
        from: "Test",
        content: "TestChat"
      };
      this.socketApi.sendSock(this.send_msg, this.getSocketConnect);
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
        this.onlineUserIdList = members;
        this.judgeonlineParticipant();
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
      } else if (chatMsg.type == undefined && chatMsg.length > 0) {
        for (let i = 0; i < chatMsg.length; i++) {
          if (chatMsg[i].content != "") {
            this.receivedChatMsgs.push(chatMsg[i]);
          }
        }
      }
    },
    judgeonlineParticipant(msg) {
      if (msg == undefined) {
        // initial
        this.onlineParticipants = [];
        this.offlineParticipants = [];
        for (let i = 0; i < this.participants.length; i++) {
          if (this.onlineUserIdList.includes(this.participants[i].userId)) {
            this.onlineParticipants.push(this.participants[i]);
          } else {
            this.offlineParticipants.push(this.participants[i]);
          }
        }
      } else {
        if (msg.behavior == "off") {
          // offline
          for (let i = 0; i < this.onlineParticipants.length; i++) {
            if (msg.userId == this.onlineParticipants[i].userId) {
              let offperson = this.onlineParticipants.splice(i, 1);
              this.offlineParticipants.push(offperson);
            }
          }
        } else if (msg.behavior == "on") {
          // online
          for (let i = 0; i < this.offlineParticipants.length; i++) {
            if (msg.userId == this.offlineParticipants[i].userId) {
              let onperson = this.offlineParticipants.splice(i, 1);
              this.onlineParticipants.push(onperson);
            }
          }
        }
      }
    },
    generateToken() {
      this.axios
        .get("/GeoProblemSolving/token/getShareToken?duration=0")
        .then(res => {
          this.sharedToken = res.data;
          this.sharedUrl = top.location.href + "&token=" + this.sharedToken;
        })
        .catch(err => {});
    },
    copySharedUrl() {
      let url = this.sharedUrl;
      var input = document.createElement("input");
      input.style.opacity = 0;
      input.style.position = "absolute";
      input.style.left = "-100000px";
      document.body.appendChild(input);

      input.value = url;
      input.select(); // 选择对象
      input.setSelectionRange(0, url.length);
      document.execCommand("Copy"); // 执行浏览器复制命令

      document.body.removeChild(input);
      this.$Notice.info({ desc: "Copy successfully." });
    }
  },
  shareWorkspace() {
    if (this.sharingEmail == "") {
      this.$Notice.info({
        desc: "Please fill in the email address."
      });
      return;
    }

    var url = this.sharedUrl + "&" + this.sharedToken;

    //send url via email
    var emailFormBody = {};
    emailFormBody["recipient"] = this.sharingEmail;
    emailFormBody["mailTitle"] = "Participatory workspace sharing";
    emailFormBody["mailContent"] =
      "Open the address:( " + url + " ), and check the latest work progress.";
    axios
      .post("/GeoProblemSolving/email/invite", emailFormBody)
      .then(res => {
        if (res.data == "Success") {
          this.$Notice.success({
            desc: "The share has been sent successfully."
          });
          this.inviteModal = false;
        } else {
          this.$Notice.error({
            desc: "Failed to send the share."
          });
        }
      })
      .catch(err => {
        console.log(err.data);
      });
  }
};
</script>
