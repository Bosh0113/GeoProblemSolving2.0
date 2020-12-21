<style scoped>
</style>

<template>
  <div>
    <Row>
      <Col span="18" offset="3" :style="{ height: contentHeight }">
        <div
          style="
            display: flex;
            justify-content: center;
            margin-top: 100px;
            height: 100%;
            padding: 100px;
          "
        >
          <Form :label-width="120">
            <FormItem label="Project name">
              <Input
                v-model="projectInfo.name"
                disabled
                style="width: 600px"
              ></Input>
            </FormItem>
            <br/>
            <FormItem label="ProjectId" style="display: none">
              <Input v-model="projectId" disabled style="width: 600px"></Input>
            </FormItem>
            <FormItem label="Email">
              <Input v-model="email" disabled style="width: 600px"></Input>
            </FormItem>
            <br/>
            <div
              v-if="registeredHintShow"
              style="
                margin-left: 120px;
                display: flex;
                align-items: center;
                width: 600px;
              "
            >
              <Icon
                type="ios-information-circle-outline"
                :size="30"
                color="yellowGreen"
              />
              <span style="font-size: 10px">
                {{ this.registeredHint }}
                <br/>Now you need to click the
                <strong style="font-weight: bold; color: blue">Join</strong>
                button for becoming a member of the project.
              </span>
            </div>
            <div
              v-else
              style="
                margin-left: 120px;
                display: flex;
                align-items: center;
                width: 600px;
              "
            >
              <Icon
                type="ios-information-circle-outline"
                :size="30"
                color="red"
              />
              <span style="font-size: 10px">
                {{ this.unregisteredHint }}
                <br/>If you input your password here and click the
                <strong style="font-weight: bold; color: blue"
                >Sign up and join</strong
                >
                button, your account would be created and you will join this
                project.
              </span>
            </div>
            <FormItem
              label="Password"
              style="margin-top: 20px"
            >
              <Input
                v-model="password"
                type="password"
                style="width: 600px"
              ></Input>
            </FormItem>
            <br/>
            <div style="display: flex; justify-content: center">
              <Button
                type="default"
                @click="joinProject(userInfo.userId)"
                v-if="registeredHintShow"
              >
                <span style="font-weight: bold; font-size: 1.2em">Join</span>
              </Button>
              <Button type="default" @click="registerAndJoin()" v-else>
                <span style="font-weight: bold; font-size: 1.2em"
                >Sign up and join</span
                >
              </Button>
            </div>
          </Form>
        </div>
      </Col>
    </Row>
  </div>
</template>
<script>
  import md5 from "js-md5";
  import {get, del, post, put} from "../../axios";

  export default {
    components: {md5},
    data() {
      return {
        headerWidth: "",
        contentHeight: "",
        userInfo: {},
        email: "",
        password: "",
        projectId: "",
        passwordInputShow: false,
        registeredHintShow: false,
        registeredHint:
          "This email has been registered, you have been a user in our platform.",
        unregisteredHint: "This email has not been registered in our platform.",
        unregisteredOperate:
          "If you fill in your password and Submit it, your account would be created and you will join this project.",
        // navaigation页面的变量
        //消息机制
        noticeSocket: null,
        unreadNoticeCount: 0,
        timer: null,
        // 项目信息
        projectInfo: [],
      };
    },
    mounted() {
      // navigation页面的
      this.getProjectInfo();
      this.headerWidth = window.innerWidth + "px";
      this.contentHeight = window.innerHeight - 120 + "px";
      this.projectId = this.$route.params.id;
      this.email = this.$route.params.email;
      this.judgeMailRegister();
    },
    computed: {
      userState() {
        return this.$store.getters.userState;
      },
    },
    methods: {
      getProjectInfo() {
        this.axios
          .get("/GeoProblemSolving/project/" + this.$route.params.id)
          .then((res) => {
            if (res.data.code == 0) {
              this.projectInfo = res.data.data;
            }
          })
          .catch((err) => {
            throw err;
          });
      },
      judgeMailRegister() {
        this.axios
          .get("/GeoProblemSolving/user?key=email&value=" + this.email)
          .then((res) => {
            if (res.data.code == 0) {
              this.registeredHintShow = true;
              this.passwordInputShow = false;
              this.userInfo = res.data.data;
              this.$Message.info(
                "You have used this email to register an account."
              );
            } else if (res.data.code == -1) {
              this.registeredHintShow = false;
              this.passwordInputShow = true;
            } else {
              console.log(res.data.data);
            }
          })
          .catch((err) => {
            throw err;
          });
      },
      async joinProject(userId) {

        if (this.password == "") {
          this.$Message.error("Please fill in your password.");
          return;
        }

        await post("/GeoProblemSolving/project/" +
          this.projectId +
          "/user?userId=" +
          userId);

        if (
          this.$store.getters.userState &&
          this.$store.getters.userId == userId
        ) {
          window.location.href("/GeoProblemSolving/projectInfo/" + this.$route.params.id);
        } else if (!this.$store.getters.userState) {
          this.autoLogin()
        } else if (this.$store.getters.userId != userId) {
          await get("/GeoProblemSolving/user/logout");
          this.autoLogin()
        }
      },
      async registerAndJoin() {
        if (this.password == "") {
          this.$Message.error("Please fill in your password.");
          return;
        }
        // register in UserServer
        let userInfo = {
          email: this.email,
          name: this.email,
          password: this.password,
        };
        let user = await post(
          "http://106.14.78.235/AuthServer/user/add",
          userInfo
        );

        // add userInfo in database
        this.axios
          .post("/GeoProblemSolving/user", user)
          .then((res) => {
            if (res.data.code == 0 || res.data.code == -3) {
              // join project
              this.joinProject(user.userId);
            } else {
              console.log(res.data.msg);
            }
          })
          .catch((err) => {
            throw err;
          });
      },
      autoLogin() {
        this.axios
          .get(
            "/GeoProblemSolving/user/login" +
            "?email=" +
            this.email +
            "&password=" +
            this.passsword
          )
          .then((res) => {
            if (res.data === "Email") {
              this.$Message.error("Email does not exist.");
            } else if (res.data === "Password" || res.data === "Fail") {
              this.$Message.error("Invalid account or password.");
            } else {
              this.$store.commit("userLogin", res.data);
              window.location.href("/GeoProblemSolving/projectInfo/" + this.$route.params.id);
            }
          });
      },
    },
  };
</script>
