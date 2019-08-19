<style scoped>
/* 注意css在哪里生效,需要全局使用就全局定义，仅在某个局部定义就在某个div或者元素下定义，在其子元素生效 */
        .layout {
            background-color: #ffffff;
            --headerHeight: 60px;
            /*--footerHeight: 60px;*/
            overflow: hidden;
        }
      

        .content {
            background-image: url("../../assets/images/c5.jpg");
            background-size:cover;
            display: flex;
            justify-content: center;   
            /* background-attachment:fixed; */
            margin: 0 auto;
        }
        .loginDiv {
            width: 450px;
            border: 1px solid rgba(133, 115, 92, 0.4);
            position: absolute;
            left: 60%;
            top: 5%;
            background-color: rgba(255,255,255,1);
        }
        .loginTitle {
            text-align:center;
            vertical-align: middle;
            line-height: 50px;
            color: #332d23;
            font-size: 25px;
            font-weight: bold;
            padding-top: 40px;
            margin-left: 20%;
            margin-right: 20%;
            border-bottom: 1px solid rgba(133, 115, 92, 0.4);
        }
        .login-content {
            padding: 50px 10px 20px 20px;
            height: 350px;
            margin-left: 10%;
            margin-right: 10%;
        }
        .ivu-form .ivu-form-item-label {
            text-align: right;
            vertical-align: middle;
            float: left;
            font-size: 14px;
            color: #495060;
            line-height: 1.5;
            padding: 5px 12px 10px 0;
            -webkit-box-sizing: border-box;
            box-sizing: border-box;
        }
        .ivu-input{
            width: 90%;
            font-size: 13px;
        }

        #logo {
            position: absolute;
            width: 129px;
            height: 40px;
            z-index: 1;
            margin-top: 5px;
            margin-left: 2.5%;
        }
        .loginBtn {
            font-size: 15px;
        }
        .loginBtn:hover {
            color: rgb(54, 72, 109);
            background-color: white;
            font-size: 15px;

        }
        img {
            cursor: pointer;
        }
        .reUseDiv {
            display: flex;
            text-align: center;
            justify-content: center;
        }
        .reciverSpan {
            width: 100px;
            text-align: center;
        }
        .resetReuseDiv {
            display: flex;
            align-items: center;
            padding: 10px;
        }
        .resetReuseDiv p,
        .resetReuseDiv input {
            width: 100%;
        }

</style>
<template>
  <div class="layout">
    <div class="content" ref="homePage" v-if="UserState===false" v-bind:style="contentStyle">
      <div class="loginDiv" v-bind:style="loginStyle">
        <div class="loginTitle">Log in</div>
        <div class="login-content">
          <Form ref="loginForm" :model="loginForm" :rules="loginFormRule">
            <FormItem prop="user" label="E-mail" :label-width="100">
              <Input
                placeholder="Please input your username"
                style="width: 90%"
                v-model="loginForm.user"
                type="text"
              />
            </FormItem>
            <FormItem prop="password" label="Password" :label-width="100">
              <Input
                placeholder="Please input your password"
                style="width: 90%"
                v-model="loginForm.password"
                @keyup.enter.native="login('loginForm')"
                type="password"
              />
            </FormItem>
            <div class="reUseDiv">
              <Checkbox v-model="checked">Automatic login within one week</Checkbox>
            </div>
            <div class="reUseDiv">
              <p>
                Forget password?
                <a @click="resetModalSHow=true">Reset</a>
              </p>
            </div>
            <br>
            <FormItem>
              <div class="reUseDiv">
                <Button
                  type="default"
                  @click="login('loginForm')"
                  class="loginBtn"
                >Log in</Button>
                <Button
                  type="default"
                  @click="register"
                  class="loginBtn"
                  style="margin-left:10px"
                >Sign up</Button>
              </div>
            </FormItem>
          </Form>
        </div>
      </div>
    </div>
    <Modal
      v-model="resetModalSHow"
      @on-ok="sendResetEmail"
      @on-cancel
      ok-text="Confirm"
      cancel-text="Cancel"
      title="Reset password board"
    >
      <div class="resetReuseDiv">
        <span class="reciverSpan">reciever:</span>
        <Input v-model="loginForm.user"/>
      </div>
      <div class="resetReuseDiv">
        <Icon type="ios-information-circle-outline" :size="20" color="lightblue"/>
        <p>We will send you an email with a url you can visit it and reset your password, if you agree, you can click the Confirm button and you will get an email soon.</p>
      </div>
    </Modal>
  </div>
</template>

<script>
export default {
  components: {},
  computed: {
    UserState() {
      return this.$store.getters.userState;
    },
  },
  created(){
    if(this.$store.getters.userState){
      this.$router.replace({ name: "PersonalPage" });
    }
  },
  data() {
    return {
      loginForm: {
        user: "",
        password: ""
      },
      loginFormRule: {
        user: [
          {
            required: true,
            message: "User name can not be empty",
            trigger: "blur"
          }
        ],
        password: [
          {
            required: true,
            message: "Password can not be empty",
            trigger: "blur"
          },
          {
            type: "string",
            min: 6,
            message: "The length of password can not be less than 6 characters",
            trigger: "blur"
          }
        ]
      },
      loginStyle: {
        marginTop: ""
      },
      contentStyle: {
        height: ""
      },
      checked: false,
      changePwdEmailStyle:
        "This email is used for help you reset your password, you can click this URL ",
      urlAddress: 'http://'+this.$store.state.IP_Port+"/GeoProblemSolving/resetPassword/",
      resetModalSHow: false
    };
  },
  mounted() {
    // this.$Notice.config({
    //   top: 100,
    //   duration: 0`
    // });
    this.contentStyle.height = window.innerHeight - 60 + "px";
    this.loginStyle.marginTop = window.innerHeight / 5 + "px";
    this.getlocalStorage();
  },
  methods: {
    login(form) {
      this.$refs[form].validate(valid => {
        if (valid) {
          if (this.checked == true) {
            localStorage.setItem("user", this.loginForm.user);
            var password = this.loginForm.password;
            password = this.encrypto(password);
            localStorage.setItem("password", password);
            localStorage.setItem("statusRecord", this.checked);
          } else if (this.checked == false) {
            localStorage.setItem("user", "");
            localStorage.setItem("password", "");
            localStorage.setItem("statusRecord", false);
          }
          var email = this.loginForm.user;
          var passwordFro = this.loginForm.password;
          var passwordAES = this.encrypto(passwordFro);
          var passwordAESURI = window.encodeURIComponent(passwordAES);
          this.axios
            .get(
              "/GeoProblemSolving/user/login" +
                "?email=" +
                email +
                "&password=" +
                passwordAESURI
            )
            .then(res => {
              if (res.data === "Email") {
                this.$Message.error("Email does not exist.");
              } else if (res.data === "Password" || res.data === "Fail") {
                this.$Message.error("Invalid account or password.");
              } else {
                this.$store.commit("userLogin", res.data);
                //这个地方到底应该怎么跳转？？
                this.$router.go(-1);
                // this.$router.push({ name: "Home" });
              }
            });
        } else {
          this.$Message.error(
            "If you don't have an account, you can regisetr one."
          );
        }
      });
    },
    goBack() {
      this.$router.push({ name: "Home" });
    },
    register() {
      this.$router.push({ name: "Register" });
    },
    goHome() {
      this.$router.push({ name: "Home" });
    },
    getlocalStorage() {
      this.loginForm.user = localStorage.getItem("user");
      this.loginForm.password = this.decrypto(localStorage.getItem("password"));
      // 将字符串格式的true转换为boolean模式的true
      if (localStorage.getItem("statusRecord")) {
        this.checked = eval(localStorage.getItem("statusRecord"));
      }
    },
    encrypto(context) {
      var CryptoJS = require("crypto-js");
      var key = CryptoJS.enc.Utf8.parse("NjnuOgmsNjnuOgms");
      var iv = CryptoJS.enc.Utf8.parse("NjnuOgmsNjnuOgms");
      var encrypted = "";
      if (typeof context == "string") {
      } else if (typeof context == "object") {
        context = JSON.stringify(context);
      }
      var srcs = CryptoJS.enc.Utf8.parse(context);
      encrypted = CryptoJS.AES.encrypt(srcs, key, {
        iv: iv,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
      });
      return encrypted.toString();
    },
    decrypto(context) {
      var CryptoJS = require("crypto-js");
      var key = CryptoJS.enc.Utf8.parse("NjnuOgmsNjnuOgms");
      var iv = CryptoJS.enc.Utf8.parse("NjnuOgmsNjnuOgms");
      var decrypt = CryptoJS.AES.decrypt(context, key, {
        iv: iv,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
      });
      var decryptedStr = decrypt.toString(CryptoJS.enc.Utf8);
      return decryptedStr.toString();
    },
    goRegister() {
      this.$router.push({ name: "Register" });
    },
    sendResetEmail() {
      var emailFormBody = {};
      emailFormBody["recipient"] = this.loginForm.user;
      emailFormBody["mailTitle"] = "Reset password notification";
      emailFormBody["mailContent"] =
        this.changePwdEmailStyle +
        this.urlAddress +
        this.loginForm.user +
        " to change your password, thanks.";
      this.axios
        .post("/GeoProblemSolving/email/send", emailFormBody)
        .then(res => {
          if (res.data == "Success") {
            this.$Notice.success({
              title: "Email send success",
              desc: "The email has been sent successfully."
            });
          } else {
            this.$Notice.error({
              title: "Email send fail",
              desc: "Maybe you input a wrong email , please check it out."
            });
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    register(){
      this.$router.push({ name: "Register" });
    }
  }
};
</script>

