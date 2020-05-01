<style scoped>
.Content {
  background-size: cover;
}
.header {
  height: 60px;
}
#logo {
  position: absolute;
  width: 129px;
  height: 40px;
  z-index: 1;
  margin-top: 5px;
  margin-left: 2.5%;
}
.InputStyle {
  width: 400px;
}
.registerForm {
  padding: 20px;
}
.register_title {
  background-color: rgb(34, 167, 240);
  text-align: center;
  vertical-align: middle;
  line-height: 60px;
  color: white;
  font-size: 30px;
  font-weight: bold;
}
.demo-upload-list {
  display: inline-block;
  width: 60px;
  height: 60px;
  text-align: center;
  line-height: 60px;
  border: 1px solid transparent;
  border-radius: 4px;
  overflow: hidden;
  background: #fff;
  position: relative;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.2);
  margin-right: 4px;
}
.demo-upload-list img {
  width: 100%;
  height: 100%;
}
.demo-upload-list-cover {
  display: none;
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.6);
}
.demo-upload-list:hover .demo-upload-list-cover {
  display: block;
}
.demo-upload-list-cover i {
  color: #fff;
  font-size: 20px;
  cursor: pointer;
  margin: 0 2px;
}
.uploadAvatar {
  position: relative;
  width: 58px;
  height: 58px;
  top: 0;
  left: 0;
  outline: none;
  background-color: transparent;
  opacity: 0;
}
.uploadBox {
  display: inline-block;
  width: 58px;
  height: 58px;
  line-height: 58px;
  overflow: hidden;
  border-width: 0.75px;
  border-style: dashed;
  border-color: lightslategray;
}
.formStyle {
  display: flex;
  justify-content: center;
}
</style>
<template>
  <div>
    <Layout>
      <!-- <Header class="header">
        <img src="@/assets/images/OGMS.png" id="logo" @click="goHome" style="cursor:pointer">
      </Header>-->
      <Content>
        <Row class="Content" id="register">
          <Col :xs="{ span: 12, offset: 6 }" :lg="{ span: 12, offset: 6 }">
            <div class="registerForm">
              <Card>
                <h2 slot="title" class="register_title">Sign up</h2>
                <!-- 实现注册的样式 -->
                <Form
                  ref="registerForm"
                  :model="registerForm"
                  :rules="registerFormValidate"
                  :label-width="150"
                  label-position="right"
                  inline
                >
                  <div class="formStyle">
                    <FormItem label="Email" prop="email">
                      <Input
                        v-model="registerForm.email"
                        placeholder="Plase enter your email"
                        :class="{InputStyle: inputstyle}"
                      ></Input>
                    </FormItem>
                  </div>
                  <div class="formStyle">
                    <FormItem label="Name" prop="userName">
                      <Input
                        v-model="registerForm.userName"
                        placeholder="Plase enter username"
                        :class="{InputStyle: inputstyle}"
                      ></Input>
                    </FormItem>
                  </div>
                  <!-- <div class="formStyle">
                  <FormItem label="Phone" prop="mobilePhone">
                    <Input v-model="registerForm.mobilePhone" placeholder="Plase enter your mobilePhone"
                      :class="{InputStyle: inputstyle}"></Input>
                  </FormItem>
                  </div>-->
                  <div class="formStyle">
                    <FormItem label="Title" prop="jobTitle">
                      <Select
                        v-model="registerForm.jobTitle"
                        placeholder="Plase enter your title"
                        :class="{InputStyle: inputstyle}"
                      >
                        <Option value="Professor">Professor</Option>
                        <Option value="Dr">Dr</Option>
                        <Option value="Miss">Miss</Option>
                        <Option value="Mr">Mr</Option>
                        <Option value="Mrs">Mrs</Option>
                        <Option value="Ms">Ms</Option>
                        <Option value="Mx">Mx</Option>
                      </Select>
                    </FormItem>
                  </div>
                  <div class="formStyle">
                    <FormItem label="Country / Region " prop="country">
                      <Input
                        v-model="registerForm.country"
                        placeholder="Plase enter your country"
                        :class="{InputStyle: inputstyle}"
                      ></Input>
                    </FormItem>
                  </div>
                  <div class="formStyle">
                    <FormItem label="Organization" prop="organization">
                      <Input
                        v-model="registerForm.organization"
                        placeholder="Plase enter your affiliation"
                        :class="{InputStyle: inputstyle}"
                      ></Input>
                    </FormItem>
                  </div>
                  <div class="formStyle">
                    <FormItem label="Password" prop="password">
                      <Input
                        v-model="registerForm.password"
                        placeholder="Plase enter password"
                        :class="{InputStyle: inputstyle}"
                        :type="pwdType"
                      >
                        <Button slot="append" @click="changeType()">
                          <Icon type="ios-eye" size="20" v-show="pwdType=='text'" />
                          <Icon type="ios-eye-off" size="20" v-show="pwdType=='password'" />
                        </Button>
                      </Input>
                    </FormItem>
                  </div>
                  <div class="formStyle">
                    <FormItem label="Confirm password" prop="confimPassword">
                      <Input
                        v-model="registerForm.confimPassword"
                        placeholder="Plase enter password again"
                        :class="{InputStyle: inputstyle}"
                        :type="pwdType"
                      ></Input>
                    </FormItem>
                  </div>
                  <!-- <div class="formStyle">
                  <FormItem label="City" prop="city">
                    <Input v-model="registerForm.city" placeholder="Plase enter your city"
                      :class="{InputStyle: inputstyle}"></Input>
                  </FormItem>
                </div>
                <div class="formStyle">
                  <FormItem label="Field" prop="field">
                    <Input v-model="registerForm.field" placeholder="Plase enter your research field"
                      :class="{InputStyle: inputstyle}"></Input>
                  </FormItem>
                </div>
                <div class="formStyle">
                  <FormItem label="Home page" prop="homePage">
                    <Input v-model="registerForm.homePage" placeholder="Plase enter your homepage url"
                      :class="{InputStyle: inputstyle}"></Input>
                  </FormItem>
                  </div>-->
                  <!-- <div class="formStyle">
                  <FormItem label="Gender" prop="gender">
                    <RadioGroup v-model="registerForm.gender" :class="{InputStyle: inputstyle}">
                      <Radio label="male">Male</Radio>
                      <Radio label="female">Female</Radio>
                    </RadioGroup>
                  </FormItem>
                  </div>-->
                  <!-- <div class="formStyle">
                  <FormItem label="Avatar" prop="avatar">
                    <div :class="{InputStyle: inputstyle}">
                      <div class="demo-upload-list" v-if="avatar!=''">
                        <template>
                          <img v-bind:src="avatar" />
                          <div class="demo-upload-list-cover">
                            <Icon type="ios-eye-outline" @click.native="handleView()"></Icon>
                            <Icon type="ios-trash-outline" @click.native="handleRemove()"></Icon>
                          </div>
                        </template>
                      </div>
                      <div class="uploadBox">
                        <Icon type="ios-camera" size="20" style="position:absolute;margin:18px;"></Icon>
                        <input @change="uploadPhoto($event)" type="file" class="uploadAvatar" id="avatarInput" />
                      </div>
                      <Modal title="View Image" v-model="visible">
                        <img :src="avatar" v-if="visible" style="width: 100%" />
                      </Modal>
                    </div>
                  </FormItem>
                  </div>-->
                  <!-- <div class="formStyle">
                  <FormItem label="Introduction" prop="introduction">
                    <Input v-model="registerForm.introduction" type="textarea" :autosize="{minRows: 2,maxRows: 5}"
                      placeholder="Plase introduce yourself" :class="{InputStyle: inputstyle}"></Input>
                  </FormItem>
                  </div>-->
                  <div class="formStyle">
                    <Button @click="handleReset('registerForm')" style="float:left">Reset</Button>
                    <Button
                      type="primary"
                      @click="handleSubmit('registerForm')"
                      style="margin-left: 50%"
                    >Create</Button>
                  </div>
                </Form>
                <!-- 注册样式结束 -->
              </Card>
            </div>
          </Col>
        </Row>
      </Content>
    </Layout>
    <!-- <app-header></app-header> -->
  </div>
</template>
<script>
import md5 from "js-md5";
export default {
  components: { md5 },
  computed: {
    avatar() {
      return this.$store.getters.avatar;
    }
  },
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("Please enter your password again"));
      } else if (value !== this.registerForm.password) {
        callback(new Error("The two passwords are inconsistent!"));
      } else {
        callback();
      }
    };
    return {
      //data预设
      country: "",
      //input的样式设定
      inputstyle: true,
      //表单验证
      registerForm: {
        userName: "",
        email: "",
        password: "",
        confimPassword: "",
        jobTitle: "",
        mobilePhone: "",
        gender: "",
        country: "",
        city: "",
        organization: "",
        introduction: "",
        field: "",
        homePage: "",
        avatar: ""
      },
      registerFormValidate: {
        userName: [
          {
            required: true,
            message: "The name cannot be empty",
            trigger: "blur"
          }
        ],
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
        password: [
          {
            required: true,
            min: 6,
            message: "Password must more than 6 words",
            trigger: "blur"
          }
        ],
        confimPassword: [
          {
            required: true,
            validator: validatePass,
            trigger: "blur"
          }
        ],
        jobTitle: [
          {
            required: true,
            message: "Job Title cannot be empty",
            trigger: "blur"
          }
        ],
        // gender: [
        //   {
        //     required: false,
        //     message: "Please select gender",
        //     trigger: "change"
        //   }
        // ],
        // mobilePhone: [
        //   {
        //     required: false,
        //     message: "Please enter your phone number",
        //     trigger: "blur"
        //   }
        // ],
        country: [
          {
            required: true,
            message: "Please enter your country",
            trigger: "blur"
          }
        ],
        // city: [
        //   {
        //     required: false,
        //     message: "Please enter your city",
        //     trigger: "blur"
        //   }
        // ],
        organization: [
          {
            required: true,
            message: "Please enter your affiliation",
            trigger: "blur"
          }
        ]
        // introduction: [
        //   {
        //     required: false,
        //     message: "Please enter a personal introduction",
        //     trigger: "blur"
        //   },
        //   {
        //     type: "string",
        //     min: 20,
        //     message: "Introduction no less than 20 characters",
        //     trigger: "blur"
        //   }
        // ],
        // field: [
        //   {
        //     required: false,
        //     message: "Please enter your research field",
        //     trigger: "blur"
        //   }
        // ],
        // homePage: [
        //   {
        //     required: false,
        //     message: "Please enter your home page url",
        //     trigger: "blur"
        //   }
        // ]
      },
      visible: false,
      // 隐藏密码图标样式
      pwdType: "password" // 密码类型
    };
  },
  methods: {
    handleSubmit(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          var userJson = {};
          userJson["userName"] = this.registerForm.userName;
          userJson["email"] = this.registerForm.email;
          userJson["password"] = md5(this.registerForm.password);
          userJson["mobilePhone"] = this.registerForm.mobilePhone;
          userJson["gender"] = this.registerForm.gender;
          userJson["jobTitle"] = this.registerForm.jobTitle;
          userJson["country"] = this.registerForm.country;
          userJson["city"] = this.registerForm.city;
          userJson["organization"] = this.registerForm.organization;
          userJson["introduction"] = this.registerForm.introduction;
          userJson["direction"] = this.registerForm.field;
          userJson["homePage"] = this.registerForm.homePage;
          userJson["avatar"] = this.registerForm.avatar;

          // var passwordFro = this.registerForm.password;
          // var passwordAES = this.encrypto(passwordFro);
          var passwordAES = md5(this.registerForm.password);
          var email = this.registerForm.email;
          this.axios
            .post("/GeoProblemSolving/user/register", userJson)
            .then(res => {
              if (res.data == "Email") {
                this.$Message.success("Email has been used!");
                // } else if (res.data == "MobilePhone") {
                //   this.$Message.success("Phone number has been used!");
              } else {
                this.$Message.success("Register successfully!");

                this.axios
                  .get(
                    "/GeoProblemSolving/user/login" +
                      "?email=" +
                      email +
                      "&password=" +
                      passwordAES
                  )
                  .then(res => {
                    if (res.data === "Fail") {
                      this.$Message.error("Invalid account or password.");
                    } else {
                      this.$store.commit("userLogin", res.data);

                      let registerEmailBody = {};
                      registerEmailBody["recipient"] = this.registerForm.email;
                      registerEmailBody["mailTitle"] = "Register result";
                      registerEmailBody["mailContent"] =
                        "Welcome to join in GeoProblem-Solving platform, you can solve complex problems in this platform with others collaboratively.";
                      this.axios
                        .post(
                          "/GeoProblemSolving/email/send",
                          registerEmailBody
                        )
                        .then(res => {
                          // console.log(res.data);
                        })
                        .catch(err => {
                          // console.log(err.data);
                        });
                      this.$router.go(-1);
                    }
                  })
                  .catch(err => {
                    this.$Message.error("Login fail!");
                  });
              }
            })
            .catch(err => {
              this.$Message.error("Register fail!");
            });
        } else {
          this.$Message.error("Form fill error!");
        }
      });
    },
    handleReset(name) {
      this.$refs[name].resetFields();
    },
    uploadPhoto(e) {
      // 利用fileReader对象获取file
      var file = e.target.files[0];
      var filesize = file.size;
      var filename = file.name;
      var imgcode = "";
      // 2,621,440   2M
      if (filesize > 2101440) {
        // 图片大于2MB
        this.$Message.error("size > 2MB");
      }
      var reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = e => {
        // 读取到的图片base64 数据编码 将此编码字符串传给后台即可
        imgcode = e.target.result;

        this.$store.commit("uploadAvatar", imgcode);
        $("#avatarInput").val("");
      };
    },
    handleView() {
      this.visible = true;
    },
    handleRemove() {
      this.$store.commit("uploadAvatar", "");
    },
    //点击图标片跳转到主页
    goHome() {
      window.location.href = "/GeoProblemSolving/home";
    },
    //输入密码时
    changeType() {
      this.pwdType = this.pwdType === "password" ? "text" : "password";
      // this.eyeIconType = 'ios-eye-on';
    },
    encrypto(context) {
      // var CryptoJS = require("crypto-js");
      // var key = CryptoJS.enc.Utf8.parse("NjnuOgmsNjnuOgms");
      // var iv = CryptoJS.enc.Utf8.parse("NjnuOgmsNjnuOgms");
      // var encrypted = "";
      // if (typeof context == "string") {
      // } else if (typeof context == "object") {
      //   context = JSON.stringify(context);
      // }
      // var srcs = CryptoJS.enc.Utf8.parse(context);
      // encrypted = CryptoJS.AES.encrypt(srcs, key, {
      //   iv: iv,
      //   mode: CryptoJS.mode.CBC,
      //   padding: CryptoJS.pad.Pkcs7
      // });
      // return encrypted.toString();
    }
  }
};
</script>

