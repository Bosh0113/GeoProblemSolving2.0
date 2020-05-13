<style scoped>
#stepType {
  width: fit-content;
  margin: 0 auto;
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  max-width: 600px;
  display: inline-block;
}
.btnHoverBlue:hover {
  background-color: #2db7f5;
  color: white;
}
.pageBackground {
  background-color: white;
  border: 1px solid #dcdee2;
}
</style>
<template>
  <div>
    <div style="position:relative;padding:10px 10px 0 10px; margin-top:10px">
      <Breadcrumb style="display: inline-block" separator=">">
        <BreadcrumbItem><a @click="toProjectDetailPage">Project</a></BreadcrumbItem>
        <BreadcrumbItem>Workspace</BreadcrumbItem>
      </Breadcrumb>
      <h1 id="stepType">{{stepInfo.type}}</h1>
      <step-change style="float:right" :step-info="stepInfo" :solving-process="projectInfo.solvingProcess" scope="project"></step-change>
    </div>
    <Divider style="margin:10px 0 3px 0" />
    <div style="display:flex;background-color: #f8f8f9;padding:5px;">
      <div
        style="width:300px;margin:0 5px;"
        class="pageBackground"
        :style="{minHeight:contentHeight - 10 +'px'}"
      >
        <div style="padding:15px;">
          <h2 style="display:inline-block">Title:</h2>
          <Button
            style="float:right;"
            class="btnHoverBlue"
            icon="ios-create"
            size="small"
            @click="modifyStepShow"
          >Edit</Button>
          <p style="font-size: medium;">{{stepInfo.name}}</p>
          <Divider style="margin:10px 0" />
          <h2>Description:</h2>
          <span
            style="font-size: larger;white-space: pre-line;word-break: break-word;"
          >{{stepInfo.description}}</span>
          <Divider style="margin:10px 0" />
          <h2>Members:</h2>
          <online-participant
            :online-participants="onlineParticipants"
            :offline-participants="offlineParticipants"
          ></online-participant>
          <Divider style="margin:10px 0" />
        </div>
      </div>
      <div style="flex:1;" class="pageBackground">
        <router-view
          :stepInfo="stepInfo"
          :userRole="userRole"
          :received-chat-msgs="receivedChatMsgs"
          :projectInfo="projectInfo"
        ></router-view>
      </div>
    </div>
    <Modal v-model="modifyStep" title="Modify step name and description">
      <Form
        ref="stepForm"
        :label-width="120"
        label-position="left"
        :model="stepForm"
        :rules="stepRule"
      >
        <FormItem label="Step Name" prop="name">
          <Input v-model="stepForm.name" :autosize="{minRows: 1,maxRows: 3}" />
        </FormItem>
        <FormItem label="Step Description" prop="description">
          <Input v-model="stepForm.description" type="textarea" :rows="5" />
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="modifyStep = false;">Cancel</Button>
        <Button type="primary" @click="submitModifyStep('stepForm')">Modify</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import onlineParticipant from "./../workingSpace/functionSteps/utils/onlineParticipants";
import stepChange from "./../workingSpace/functionSteps/utils/stepChange";
export default {
  components: {
    onlineParticipant,
    stepChange
  },
  created() {
    this.init();
  },
  mounted() {
    window.addEventListener("resize", this.initSize);
  },
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      if (!vm.$store.getters.userState) {
        next("/login");
      } else {
        if (
          !(
            vm.userRole == "Manager" ||
            vm.userRole == "Member" ||
            vm.userRole == "PManager" ||
            vm.getVisitorAccess()
          )
        ) {
          vm.$Message.error("You have no property to access it");
          // next(`/project/${vm.$store.getters.currentProjectId}`);
          vm.$router.go(-1);
        } else {
          next();
        }
      }
    });
  },
  data() {
    return {
      userInfo:JSON.parse(sessionStorage.getItem("userInfo")),
      contentHeight: 560,
      split: "400px",
      toProjectPage: "",
      stepInfo: {},
      projectInfo: {},
      participants: [],
      userRole: "Visitor",
      modifyStep: false,
      stepForm: {
        name: "",
        description: ""
      },
      stepRule: {
        name: [
          {
            required: true,
            message: "The title cannot be empty",
            trigger: "blur"
          }
        ],
        description: [
          {
            required: false,
            message: "Please enter description",
            trigger: "blur"
          }
        ]
      },
      // onlineParticipants.vue
      onlineUserIdList: [],
      onlineParticipants: [],
      offlineParticipants: [],
      // messagePanel.vue -- chat
      receivedChatMsgs: []
    };
  },
  watch: {
    $route() {
      this.init();
    }
  },
  methods: {
    init() {
      this.initSize();
      this.getStepInfo();
      this.getProjectInfo();
      this.getParticipants();
      this.userRoleIdentity();
      this.startWebSocket();
    },
    initSize() {
      if (window.innerHeight > 675) {
        this.contentHeight = window.innerHeight - 175;
      } else {
        this.contentHeight = 490;
      }
    },
    getVisitorAccess() {
      let visitorPermission = this.projectInfo.permissionManager.observe
        .visitor;
      if (
        this.projectInfo.permissionManager != undefined &&
        this.userRole == "Visitor" &&
        (visitorPermission == "All")
      ) {
        return true;
      }
    },
    toProjectDetailPage(){
      window.location.href=this.toProjectPage;
    },
    getStepInfo() {
      if (
        this.stepInfo.stepId != this.$route.params.stepId ||
        this.stepInfo.stepId == undefined ||
        this.stepInfo.stepId == ""
      ) {
        var that = this;
        $.ajax({
          url:
            "/GeoProblemSolving/step/inquiry/" +
            "?key=stepId" +
            "&value=" +
            that.$route.params.stepId,
          type: "GET",
          async: false,
          success: data => {
            if (data == "Offline") {
              that.$store.commit("userLogout");
              that.$router.push({ name: "Login" });
            } else if (data != "None" && data != "Fail") {
              that.stepInfo = data[0];
              that.toProjectPage =
                "/GeoProblemSolving/projectDetail/" + data[0].projectId;
            } else {
              that.$Notice.info({
                desc: "Get step description failed!"
              });
            }
          }
        });
      } else {
        this.$Notice.info({
          desc: "Get step information failed!"
        });
      }
    },
    getProjectInfo() {
      let projectInfo = this.$store.getters.project;
      if (
        JSON.stringify(projectInfo) != "{}" &&
        projectInfo.projectId == this.stepInfo.projectId
      ) {
        this.projectInfo = projectInfo;
      } else {
        var that = this;
        $.ajax({
          url:
            "/GeoProblemSolving/project/inquiry" +
            "?key=projectId" +
            "&value=" +
            this.stepInfo.projectId,
          type: "GET",
          async: false,
          success: data => {
            if (data == "Offline") {
              that.$store.commit("userLogout");
              that.$router.push({ name: "Login" });
            } else if (data != "None" && data != "Fail") {
              that.projectInfo = data[0];
              that.$store.commit("setProjectInfo", data[0]);
            } else {
              console.log(data);
            }
          }
        });
      }
    },
    getParticipants() {
      let members = this.projectInfo.members;
      if(members[0].userId!=this.projectInfo["managerId"]){
        let manager = [
          {
            userId: this.projectInfo["managerId"],
            userName: this.projectInfo["managerName"]
          }
        ];
        let membersList = manager.concat(members);
      }

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
    },
    userRoleIdentity() {
      this.userRole = "Visitor";
      if (this.userInfo.userState) {
        if (this.projectInfo.managerId === this.userInfo.userId) {
          this.userRole = "PManager";
        }
        else {
          for (let i = 0; i < this.projectInfo.members.length; i++) {
            if (
              this.projectInfo.members[i].userId ===
              this.userInfo.userId
            ) {
              this.userRole = "Member";
              break;
            }
          }
        }
      } else {
        this.userRole = "Visitor";
      }
    },
    modifyStepShow() {
      this.stepForm.name = this.stepInfo.name;
      this.stepForm.description = this.stepInfo.description;
      this.modifyStep = true;
    },
    submitModifyStep(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          let obj = new URLSearchParams();
          obj.append("name", this.stepForm.name);
          obj.append("description", this.stepForm.description);
          obj.append("stepId", this.stepInfo.stepId);

          this.axios
            .post("/GeoProblemSolving/step/update", obj)
            .then(res => {
              if (res.data == "Offline") {
                this.$store.commit("userLogout");
                this.$router.push({
                  name: "Login"
                });
              } else if (res.data != "Fail") {
                this.$Notice.info({
                  desc: "Update successfully!"
                });
                this.stepInfo.name = this.stepForm.name;
                this.stepInfo.description = this.stepForm.description;
              } else {
                this.$Message.error("Update step failed.");
              }
            })
            .catch(err => {
              console.log(err.data);
            });
          this.modifyStep = false;
        }
      });
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
    }
  }
};
</script>
