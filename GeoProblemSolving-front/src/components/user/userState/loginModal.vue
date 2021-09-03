<template>
  <div>
    <Modal
      v-model="tempLoginModal"
      title="You are offline, please log in again!"
      cancel-text="Cancel"
      width="500px"
      :closable="false"
      :mask-closable="false"
    >
      <h2 style="text-align: center; margin-bottom: 30px">Log in</h2>
      <Form
        ref="loginForm"
        :model="loginForm"
        :rules="loginFormRule"
        :label-width="100"
        style="margin-left: 30px"
      >
        <FormItem prop="user" label="Email" :label-width="100">
          <Input
            placeholder="Please input your email"
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
        <div style="text-align: center">
          <Checkbox v-model="checked">Keep me logged in</Checkbox>
        </div>
        <p style="text-align: center">
          more actions?
          <a @click="go2LoginPage">Login Page</a>
        </p>
      </Form>
      <div slot="footer">
        <Button type="text" @click="changeLoginModal()">Cancel</Button>
        <Button type="primary" @click="login('loginForm')">Log in</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import md5 from "js-md5";
export default {
  props: ["tempLoginModal"],
  data() {
    return {
      loginForm: {
        user: "",
        password: "",
      },
      loginFormRule: {
        user: [
          {
            required: true,
            message: "User name can not be empty",
            trigger: "blur",
          },
        ],
        password: [
          {
            required: true,
            message: "Password can not be empty",
            trigger: "blur",
          },
          {
            type: "string",
            min: 3,
            message: "The length of password can not be less than 6 characters",
            trigger: "blur",
          },
        ],
      },
      checked: false,
    };
  },
  mounted() {
    this.getlocalStorage();
  },
  methods: {
    login(form) {
      this.$refs[form].validate((valid) => {
        if (valid) {
          if (this.checked == true) {
            localStorage.setItem("user", this.loginForm.user);
            var password = this.loginForm.password;
            localStorage.setItem("password", password);
            localStorage.setItem("statusRecord", this.checked);
          } else if (this.checked == false) {
            localStorage.setItem("user", "");
            localStorage.setItem("password", "");
            localStorage.setItem("statusRecord", false);
          }

          let email = this.loginForm.user;
          //前段md5 加密，后端再进行 shaHex256加密
          let passwordAESURI = md5(this.loginForm.password);
          this.axios
            .get(
              "/GeoProblemSolving/user/login/" + email + "/" + passwordAESURI
            )
            .then((res) => {
              if (res.data.code === 0) {
                this.$store.commit("userLogin", res.data.data);
                this.$Notice.info({ title: "Login", desc: "Success!" });
                this.changeLoginModal();
              } else {
                this.$Message.error("Invalid account or password.");
                this.loginForm.password = "";
              }
            });
        } else {
          this.$Message.error(
            "If you don't have an account, you can register one."
          );
        }
      });
    },

    getlocalStorage() {
      this.loginForm.user = localStorage.getItem("user");
      this.loginForm.password = localStorage.getItem("password");
      // 将字符串格式的true转换为boolean模式的true
      if (localStorage.getItem("statusRecord")) {
        this.checked = eval(localStorage.getItem("statusRecord"));
      }
    },
    changeLoginModal() {
      this.$emit("changeLoginModal", false);
    },
    go2LoginPage() {
      this.$router.push({ name: "Login" });
    },
  },
};
</script>

<style>
</style>
