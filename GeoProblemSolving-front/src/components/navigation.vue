<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
#logo {
  position: absolute;
  width: 129px;
  height: 40px;
  z-index: 1;
  margin-top: 5px;
  margin-left: 2.5%;
  cursor: pointer;
}
.header span {
  font-size: 15px;
}
.container {
  display: flex;
  flex-direction: column;
  height: 100%;
}
header {
  height: 60px;
  top: 0;
  z-index: 100;
  position: absolute;
  width: 100%;
  flex: 0 0 auto;
  position: absolute;
  min-width: 1200px;
}
.content {
  flex: 1 0 auto;
  margin-top: 60px;
}
footer {
  background-color: #515a6e;
  height: 60px;
  width: 100%;
  bottom: 0;
  flex: 0 0 auto;
}
.userState {
  position: absolute;
  margin-left: 85%;
  width: 15%;
  top: 0px;
  z-index: 1;
}
.navPart {
  width: 85%;
}
.menuItem span {
  font-size: 1.2em;
  font-weight: bold;
}
.menuItem:first-child {
  /* margin-left: 25%; */
}
.userImg {
  width: 40px;
  height: 40px;
  vertical-align: middle;
}
.footerTop {
  text-align: center;
  color: white;
  font-weight: bold;
  margin-top: 10px;
}
.footerBottom {
  text-align: center;
  color: white;
  font-size: 0.8em;
}
</style>
<template>
  <div class="container">
    <header :style="absoluteStyle">
      <img
        src="@/assets/images/OGMS.png"
        id="logo"
        class="pic"
        @click="goHome"
        style="cursor:pointer;margin-left:5%"
      >
      <div class="navPart">
        <Menu
          mode="horizontal"
          theme="dark"
          active-name="home"
          @on-select="turnContent"
          :style="`z-index:0;background:`+headerBgColor"
          width="auto"
        >
          <MenuItem name="home" class="menuItem" style="margin-left:35%">
            <span>Home</span>
          </MenuItem>
          <Submenu name="projects" class="menuItem" >
            <template slot="title">
              <span>Projects</span>
            </template>
              <MenuItem name="projects">GeoProblem Solving</MenuItem>
              <MenuItem name="geoModel-compare-projects">GeoModel Compare</MenuItem>
          </Submenu>
          <MenuItem name="resources" class="menuItem">
            <span>Resources</span>
          </MenuItem>
          <!-- <MenuItem name="community" class="menuItem">
            <span>Community</span>
          </MenuItem> -->
          <MenuItem name="help" class="menuItem">
            <span>Help</span>
          </MenuItem>
        </Menu>
        <div class="userState">
          <Menu
            mode="horizontal"
            theme="dark"
            @on-select="unlogin"
            :style="`z-index:0;background:`+headerBgColor"
            v-show="!userState"
            class="menuItem"
          >
            <MenuItem name="login">
              <span>Login</span>
            </MenuItem>
            <MenuItem name="register">
              <span>Sign up</span>
            </MenuItem>
          </Menu>
          <Menu
            mode="horizontal"
            theme="dark"
            @on-select="logged"
            :style="`z-index:0;background:`+headerBgColor"
            v-show="userState"
            class="menuItem"
          >
            <MenuItem name="notification">
              <Badge :count="unreadNoticeCount" id="noticeBadge">
                <Icon type="ios-notifications-outline" size="25"></Icon>
              </Badge>
            </MenuItem>
            <MenuItem name="personal" style="width:100px">
              <Dropdown @on-click="changeSelect" placement="bottom-start">
                <div @click="toPersonalPage">
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
                </div>
                <DropdownMenu slot="list">
                  <!-- <DropdownItem name="personalPage">User Space</DropdownItem> -->
                  <DropdownItem name="logout">Log out</DropdownItem>
                </DropdownMenu>
              </Dropdown>
            </MenuItem>
          </Menu>
        </div>
      </div>
    </header>
    <section class="content" :style="{minHeight:contentHeight}">
      <router-view @sendNotice="sendMessage" @readNotification="readNotification"></router-view>
    </section>
    <footer>
      <h2 class="footerTop">
        <i>Open Geographic Modeling and Simulation</i>
      </h2>
      <p class="footerBottom">Copyright © 2013-2019 OpenGMS. All rights reserved.</p>
    </footer>
  </div>
</template>
<script>
import Avatar from "vue-avatar";
export default {
  name: "HelloWorld",
  data() {
    return {
      //消息机制
      noticeSocket: null,
      unreadNoticeCount: 0,
      timer: null,
      //导航栏宽度
      headerWidth: "",
      contentHeight: window.innerHeight - 120 + "px"
    };
  },
  mounted() {
    if(window.innerHeight > 675){
      this.contentHeight = window.innerHeight - 120 + "px";
    }
    else{
      this.contentHeight = 675 - 120 + "px";
    }
    this.headerWidth = window.innerWidth + "px";
    if (this.$store.getters.userState) {
      this.setTimer();
      this.initWebSocket();
      this.getUnreadNoticeCount();
    }
    window.addEventListener("resize", this.reSize);
  },
  beforeDestroy: function() {
    window.removeEventListener("resize", this.reSize);
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
    },
    headerBgColor() {
      return this.$route.name === "Home" ? "none" : "";
    },
    absoluteStyle() {
      return this.$route.name === "Home"
        ? "position:absolute"
        : "position:fixed";
    }
  },
  methods: {
    reSize() {
      if (window.innerHeight > 675) {
        this.contentHeight = window.innerHeight - 120 + "px";
      } else {
        this.contentHeight = 675;
      }
    },
    turnContent(name) {
      if (name === "home") {
        this.$router.replace({ name: "Home" });
      } else if (name == "projects") {
        this.$router.replace({ name: "Projects" });
      } else if (name == "resources") {
        this.$router.replace({ name: "PublicResource" });
      } else if (name == "community") {
        this.$router.replace({ name: "Community" });
      } else if (name == "help") {
        this.$router.replace({ name: "Help" });
      } else if(name == "geoModel-compare-projects"){
        this.$router.replace({name:"cmp-projectlist"})
      }
    },
    goHome() {
      this.$router.push({ name: "Home" });
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
    toPersonalPage() {
      this.$router.push({ name: "PersonalPage" });
    },
    // 获取到通知的数量
    getUnreadNoticeCount() {
      this.unreadNoticeCount = 0;
      //get请求发送的是用户id
      this.axios
        .get(
          "/GeoProblemSolving/notice/inquiry" +
            "?key=recipientId" +
            "&value=" +
            this.$store.getters.userId
        )
        .then(res => {
          let noticeList = res.data;
          let unreadCount = 0;
          for (let i = 0; i < noticeList.length; i++) {
            if (noticeList[i].state === "unread") {
              unreadCount++;
              continue;
            }
          }
          this.$set(this, "unreadNoticeCount", unreadCount);
        })
        .catch(err => {
          console.log("失败的原因是" + err.data);
        });
    },
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
      // console.log("NoticeSocket连接成功！");
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
      // console.log("NoticeSocket连接断开！");
    },
    onError(e) {
      this.removeTimer();
      // console.log("NoticeSocket连接错误！");
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
            this.$router.replace({ name: "Home" });
          })
          .catch(err => {
            confirm("logout fail!");
          });
      }
    }
  }
};
</script>
