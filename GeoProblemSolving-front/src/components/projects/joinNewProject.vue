<style scoped>
.header{
  height:60px;
  background:#515a6e;
  width:100%;
  color:white;
  top:0;
  position:fixed;
}
.footer{
  background:#515a6e;
  height:60px;
  width:100%;
  bottom:0;
  position:fixed;
}
.content{
  height:auto;
}
.menuItem span{
  font-size: 1.2em;
  font-weight: bold;
}
#logo {
  position: absolute;
  width: 129px;
  height: 40px;
  z-index: 1;
  margin-top: 5px;
  margin-left: 2.5%;
  /* margin-left: 5%; */
}
.userState {
  position: absolute;
  margin-left: 80%;
  width: 20%;
  top: 0px;
  z-index: 1;
}
.navPart {
  width: 85%;
}
.footer{
  background-color:#515a6e;
  height:60px;
  width:100%;
  bottom:0;
}

</style>

<template>
<div>
  <div class="header">
    <img src="@/assets/images/OGMS.png" id="logo" class="pic" @click="goHome" style="cursor:pointer">
    <div class="navPart">
        <Menu
          mode="horizontal"
          theme="dark"
          active-name="home"
          @on-select="turnContent"
          style="z-index:0"
          width="auto"
        >
          <MenuItem name="home" class="menuItem" style="margin-left:30%">
            <span>Home</span>
          </MenuItem>
          <MenuItem name="project" class="menuItem">
            <span>Project</span>
          </MenuItem>
          <MenuItem name="Public Resource" class="menuItem">
            <span>Public Resource</span>
          </MenuItem>
          <MenuItem name="community" class="menuItem">
            <span>Community</span>
          </MenuItem>
          <MenuItem name="help" class="menuItem" >
            <span>Help</span>
          </MenuItem>
        </Menu>
        <div class="userState">
            <Menu mode="horizontal" theme="dark" @on-select="unlogin" style="z-index:0" v-show="!userState" class="menuItem">
              <MenuItem name="login">
                <span>Login</span>
              </MenuItem>
              <MenuItem name="register">
                 <span>Sign up</span>
              </MenuItem>
            </Menu>
            <Menu mode="horizontal" theme="dark" @on-select="logged" style="z-index:0"  v-show="userState" class="menuItem">
              <MenuItem name="notification">
                <Badge :count="unreadNoticeCount" id="noticeBadge">
                  <Icon type="ios-notifications-outline" size="25"></Icon>
                </Badge>
              </MenuItem>
              <MenuItem name="personal" style="width:100px">
                <Dropdown @on-click="changeSelect" placement="bottom-start">
                  <img
                    v-bind:src="avatar"
                    v-if="avatar!=''&&avatar!=undefined&&avatar!=null"
                    :title="userName"
                    style="width:40px;height:40px;vertical-align:middle;"
                  >
                  <avatar
                    :username="userName"
                    :size="40"
                    style="margin-top:10px"
                    :title="userName"
                    v-else
                  ></avatar>
                  <DropdownMenu slot="list" >
                      <DropdownItem name="personalPage">User Space</DropdownItem>
                      <DropdownItem name="logout">Log out</DropdownItem>
                  </DropdownMenu>
                </Dropdown>
              </MenuItem>
            </Menu>
        </div>
      </div>
  </div>
  <Row>
    <Col span="18" offset="3" :style="{height:contentHeight}">
    <div style="display:flex;justify-content:center;margin-top:100px;height:100%;padding:100px">
      <Form :label-width="120">
        <FormItem label="ProjectName">
          <Input v-model="projectName" disabled style="width:600px"></Input>
        </FormItem>
        <br>
          <FormItem label="ProjectId" style="display:none">
          <Input v-model="projectId" disabled style="width:600px"></Input>
        </FormItem>
        <FormItem label="Email" >
          <Input v-model="email" disabled style="width:600px"></Input>
        </FormItem>
        <br>
        <div v-show="registeredHintShow==true" style="margin-left:120px;display:flex;align-items:center;width:600px"><Icon type="ios-information-circle-outline" :size="30" color="yellowGreen"/><span style="font-size:10px;">{{this.registeredHint}}<br>Now you need to fill the password in the blank and then click the <strong style="font-weight:bold;color:blue">Join</strong> button later you will be a member of the project.</span></div>
        <div v-show="unregisteredHintShow==true" style="margin-left:120px;display:flex;align-items:center;width:600px"><Icon type="ios-information-circle-outline" :size="30" color="red"/><span style="font-size:10px;">{{this.unregisteredHint}}<br>If you input your own password here and press <strong style="font-weight:bold;color:blue">Sign up and Join</strong> button, your own account would be created based on the email presented above and you will join the project at the same time.</span></div>
        <FormItem label="Password" v-show="passwordInputShow" style="margin-top:20px">
          <Input v-model="password" type="password" style="width:600px"></Input>
        </FormItem>
        <br>
        <div style="display:flex;justify-content:center">
          <Button type="default" @click="joinByMail()">
            <span style="font-weight:bold;font-size:1.2em" v-show="unregisteredHintShow==true">Sign up and Join</span>
            <span style="font-weight:bold;font-size:1.2em" v-show="registeredHintShow==true">Join</span>
          </Button>
        </div>
      </Form>
    </div>
    </Col>
  </Row>
  <div class="footer">
      <h2 style="text-align:center;color:white;font-weight:bold;margin-top:15px"><i>Open Geographic Modeling and Simulation</i></h2>
      <h4 style="text-align:center;color:white">Copyright © 2013-2019 OpenGMS. All rights reserved.</h4>
    </div>
</div>

</template>
<script>
import Avatar from "vue-avatar";
export default {
  data() {
    return {
      headerWidth: "",
      contentHeight:"",
      email:"",
      password:"",
      projectId:"",
      passwordInputShow:false,
      registeredHintShow:false,
      unregisteredHintShow:false,
      registeredHint:"This email has been registered,you have been a member in our platform.",
      // unregisteredHint:"Sorry,you are not a user in our platform,this email will be used to create a new account for you,you only need to set a password for log in,and if you want to enrich your personal information,you can go to userSpace to enrich them.",
      unregisteredHint:"It seems that this email is not a registed user in our platform.",
      unregisteredOperate:"If you input your own password here and press Submit button,your own account would be created based on the email presented above and you will join the project at the same time.",
      // navaigation页面的变量
       //消息机制
      noticeSocket: null,
      unreadNoticeCount: 0,
      timer: null,
      // 项目信息
      projectInfo:[],
      // 项目名
      projectName:"",
    };
  },
  // created(){

  // },
  mounted(){
    // navigation页面的
    this.getProjectName();
    if (this.$store.getters.userState) {
      this.setTimer();
      // this.initWebSocket();
      // this.getUnreadNoticeCount();
    }
    //
    this.headerWidth = window.innerWidth + "px";
    this.contentHeight = window.innerHeight - 120 + 'px';
    this.projectId = this.$route.params.id;
    this.email = this.$route.params.email;
    this.judgeMailRegiste();

  },
  updated() {
    $(".userState sup").css("margin-top", "20px");
  },
  components: {
    Avatar
  },
  computed: {
    userState() {
      return this.$store.getters.userState;
    },
    userName() {
      return this.$store.getters.userName;
    },
    avatar() {
      return this.$store.getters.avatar;
    }
  },
  methods:{
      getProjectName(){
        this.axios.get("/GeoProblemSolving/project/inquiry?key=projectId&&value=" + this.$route.params.id)
        .then(res=> {
          if(res.data!="None"){
            this.projectInfo = res.data;
            this.projectName = this.projectInfo[0].title;
          }
        })
        .catch(err=>{

        })
      },
      judgeMailRegiste(){
        this.axios
        .get(
          "/GeoProblemSolving/user/isRegistered?" +
            "email=" +
            this.email
        )
        .then(res => {
          if (res.data === true) {
            this.registeredHintShow = true;
            // this.$Notice.info({
            //   desc: "This email has been registerd",
            //   top:200,
            // });
            this.$Message.info("This email has been registerd");
            this.passwordInputShow = true;
          } else if (res.data === false) {
            this.unregisteredHintShow = true;
            this.$Message.info("you have use your email to register an account");
            this.passwordInputShow = true;
          }
        })
        .catch(err => {
          this.$Message.error("Judge fail");
        });
    },
    joinByMail(){
      this.axios
        .get(
          "/GeoProblemSolving/project/joinByMail" +
            "?projectId="+
            this.projectId+
            "&email=" +
            this.email+
            "&password="+
            this.password
        )
        .then(res => {
          let gotoProjectId = this.projectId;
          if (res.data === "Success") {
            this.$Message.info("Join successfuly");
            window.location.href="/GeoProblemSolving/projectDetail/"+gotoProjectId;
          } else if (res.data === "Fail") {
            this.$Message.info("Fail to join in this project");
          }else if(res.data === "Exist"){
            this.$Message.info("you have been in this group,no need to apply again");
            window.location.href="/GeoProblemSolving/projectDetail/"+gotoProjectId;
          }else if(res.data === "None"){
            this.$Message.info("this group doesn't exist");
          }else if(res.data === "Password"){
            this.$Message.info("password might input error, try it again");
          }
        })
        .catch(err => {
          this.$Message.error("Join fail");
        });
    },
    goHome() {
      window.location.href="/GeoProblemSolving/home";
    },
    turnContent(name) {
      if (name === "home") {
        window.location.href="/GeoProblemSolving/home";
      } else if (name == "project") {
        this.$router.replace({ name: "Project" });
      } else if (name == "Public Resource") {
        this.$router.replace({ name: "resourceList" });
      } else if (name == "community") {
        this.$router.replace({ name: "Community" });
      } else if (name == "help") {
        this.$router.replace({ name: "Help" });
      }
    },
    unlogin(name) {
      if (name === "login") {
        this.$router.push({ name: "Login" });
      } else if (name == "register") {
        this.$router.push({ name: "Register" });
      }
    },
    logged(name) {
      if (name === "notification") {
        this.$router.push({ name: "Notifications" });
      } else if (name === "personal") {
      }
    },
    // navigation的函数
    initWebSocket() {
      if (this.noticeSocket != null) {
        this.noticeSocket = null;
      }
      var noticeSocketURL = "ws://localhost:8081/GeoProblemSolving/NoticeSocket";
      // var noticeSocketURL = "ws://"+this.$store.state.IP_Port+"/GeoProblemSolving/NoticeSocket";
      this.noticeSocket = new WebSocket(noticeSocketURL);
      this.noticeSocket.onopen = this.onOpen;
      this.noticeSocket.onmessage = this.onMessage;
      this.noticeSocket.onclose = this.onClose;
      this.noticeSocket.onerror = this.onError;
    },
    onOpen() {
      console.log("NoticeSocket连接成功！");
    },
    onMessage(e) {
      if (e.data == "Notice") {
        let newCount = this.unreadNoticeCount + 1;
        this.$set(this, "unreadNoticeCount", newCount);
        this.$Message.info("You have a new notice!");
      } else {
        console.log(e.data);
      }
    },
    onClose(e) {
      this.removeTimer();
      console.log("NoticeSocket连接断开！");
    },
    onError(e) {
      this.removeTimer();
      console.log("NoticeSocket连接错误！");
    },
    sendMessage(recipientId) {
      this.noticeSocket.send(recipientId);
    },
    setTimer() {
      var that = this;
      this.timer = setInterval(() => {
        if (that.noticeSocket != null && that.noticeSocket != undefined) {
          that.noticeSocket.send("ping");
        }
      }, 20000);
    },
    removeTimer() {
      clearInterval(this.timer);
    },
    readNotification() {
      let newCount = this.unreadNoticeCount;
      if (newCount > 0) {
        this.unreadNoticeCount = newCount - 1;
      }
    },
    changeSelect(name) {
      if (name == "logout") {
        this.axios
          .get("/GeoProblemSolving/user/logout")
          .then(res => {
            this.$store.commit("userLogout");
            this.noticeSocket.close();
            window.location.href="/GeoProblemSolving/home";
          })
          .catch(err => {
            confirm("logout fail!");
          });
      } else if (name == "personalPage") {
        this.$router.push({ name: "PersonalPage" });
      }
    },
  }
};
</script>
