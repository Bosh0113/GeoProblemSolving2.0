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
.InputStyle {
  width: 300px;
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
                <!-- <Badge :count="unreadNoticeCount" id="noticeBadge">
                  <Icon type="ios-notifications-outline" size="25"></Icon>
                </Badge> -->
              </MenuItem>
              <MenuItem name="personal" style="width:100px">
                <Dropdown @on-click="changeSelect" placement="bottom-start">
                  <img
                    v-bind:src="avatar"
                    v-if="avatar!=''&&avatar!='undefined'"
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
    <Col span="16" offset="4">
      <div
        style="margin-top:100px;bcakground-color:lightblue;height:500px;display:flex;justify-content:center"
      >
        <Form
          label-position="left"
          :label-width="150"
          ref="formValidate"
          :model="formValidate"
          :rules="ruleValidate"
        >
          <FormItem label="Email" prop="email">
            <Input v-model="formValidate.email" :class="{InputStyle: inputstyle}"></Input>
          </FormItem>
          <FormItem label="new password" prop="newPassword">
            <Input
              v-model="formValidate.newPassword"
              placeholder="Plase enter password"
              :class="{InputStyle: inputstyle}"
              :type="pwdType"
            >
              <Button slot="append" @click="changeType()">
                <Icon type="ios-eye" size="20" v-show="pwdType=='text'"/>
                <Icon type="ios-eye-off" size="20" v-show="pwdType=='password'"/>
              </Button>
            </Input>
          </FormItem>
          <FormItem label="confirm password" prop="confirmPassword">
            <Input
              v-model="formValidate.confirmPassword"
              placeholder="Plase enter password again"
              :class="{InputStyle: inputstyle}"
              :type="pwdType"
            ></Input>
          </FormItem>
          <div style="display:flex;justify-content:center;margin-top:40px">
            <Button type="default" @click="resetPwd()" :disabled="formValidate.newPassword!=formValidate.confirmPassword||formValidate.newPassword==''||formValidate.confirmPassword==''">Confirm Change Password</Button>
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
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("Please enter your password again"));
      } else if (value !== this.formValidate.newPassword) {
        callback(new Error("The two passwords are inconsistent!"));
      } else {
        callback();
      }
    };
    return {
      inputstyle: true,
      pwdType: "password",
      newPassword: "",
      confirmPassword: "",
      formValidate: {
        email: "",
        newPassword: "",
        confirmPassword: "",
        // 密码类型,
      },
      ruleValidate: {
        email: [
          {
            required: true,
            message: "Mailbox cannot be empty",
            trigger: "blur"
          },
          {
            type: "email",
            message: "Incorrect email format",
            trigger: "blur"
          }
        ],
        newPassword: [
          {
            required: true,
            min: 6,
            message: "Password must more than 6 words",
            trigger: "blur"
          }
        ],
        confirmPassword: [
          {
            required: true,
            validator: validatePass,
            trigger: "blur"
          }
        ]
      }
    };
  },
  mounted() {
    this.formValidate.email = this.$route.params.email;
  },
  methods: {
    resetPwd() {
      this.axios
        .get("/GeoProblemSolving/user/newPassword?email="+this.formValidate.email+"&password="+this.formValidate.newPassword)
        .then(res => {
          if (res.data !== "Fail") {
            this.$Notice.success({
              title: "notification",
              desc: "Password update successfully,you can use it to login again!"
            });
            this.$router.push({ name: "Login" });
          }
        })
        .catch(err => {});
    },
    changeType() {
      this.pwdType = this.pwdType === "password" ? "text" : "password";
    },
    goHome() {
      this.$router.push({ name: "Home" });
    },
    turnContent(name) {
      if (name === "home") {
        this.$router.replace({ name: "Home" });
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
      } else if (name == "personalPage") {
        this.$router.push({ name: "PersonalPage" });
      }
    },
  }
};
</script>
