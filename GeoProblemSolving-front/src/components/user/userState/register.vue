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
.organization >>> .ti-input {
  display: inline-block;
  width: 400px;
  height: 32px;
  line-height: 1.5;
  padding: 4px 7px;
  font-size: 12px;
  border: 1px solid #dcdee2;
  border-radius: 4px;
  color: #515a6e;
  background-color: #fff;
  background-image: none;
  position: relative;
  cursor: text;
}
.organization >>> input{
  opacity: 0.5;
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
                <h2 slot="title" class="register_title">Register</h2>
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
                        :class="{ InputStyle: inputstyle }"
                      ></Input>
                    </FormItem>
                  </div>
                  <div class="formStyle">
                    <FormItem label="Name" prop="name">
                      <Input
                        v-model="registerForm.name"
                        placeholder="Plase enter user name"
                        :class="{ InputStyle: inputstyle }"
                      ></Input>
                    </FormItem>
                  </div>
                  <div class="formStyle">
                    <FormItem label="Title" prop="title">
                      <Select
                        v-model="registerForm.title"
                        placeholder="Plase enter your title"
                        :class="{ InputStyle: inputstyle }"
                      >
                        <Option value="Prof">Professor</Option>
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
                        placeholder="Please enter your country"
                        :class="{ InputStyle: inputstyle }"
                      ></Input>
                    </FormItem>
                  </div>
                  <div class="formStyle">
                    <FormItem label="Organizations" prop="organizations">
                      <!-- <Input
                        v-model="registerForm.organizations"
                        placeholder="Plase enter your affiliation"
                        :class="{ InputStyle: inputstyle }"
                      ></Input> -->
                      <vue-tags-input
                        class="organization"
                        v-model="organization"
                        :tags="registerForm.organizations"
                        @tags-changed="(newTags) => (registerForm.organizations = newTags)"
                      />
                    </FormItem>
                  </div>
                  <div class="formStyle">
                    <FormItem label="Password" prop="password">
                      <Input
                        v-model="registerForm.password"
                        placeholder="Plase enter password"
                        :class="{ InputStyle: inputstyle }"
                        :type="pwdType"
                      >
                        <Button slot="append" @click="changeType()">
                          <Icon
                            type="ios-eye"
                            size="20"
                            v-show="pwdType == 'text'"
                          />
                          <Icon
                            type="ios-eye-off"
                            size="20"
                            v-show="pwdType == 'password'"
                          />
                        </Button>
                      </Input>
                    </FormItem>
                  </div>
                  <div class="formStyle">
                    <FormItem label="Confirm password" prop="confimPassword">
                      <Input
                        v-model="registerForm.confimPassword"
                        placeholder="Plase enter password again"
                        :class="{ InputStyle: inputstyle }"
                        :type="pwdType"
                      ></Input>
                    </FormItem>
                  </div>
                  <div class="formStyle">
                    <Button
                      @click="handleReset('registerForm')"
                      style="float: left"
                      >Reset</Button
                    >
                    <Button
                      type="primary"
                      @click="handleSubmit('registerForm')"
                      style="margin-left: 50%"
                      >Create</Button
                    >
                  </div>
                </Form>
                <!-- 注册样式结束 -->
              </Card>
            </div>
          </Col>
        </Row>
      </Content>
    </Layout>
  </div>
</template>
<script>
import md5 from "js-md5";
import VueTagsInput from "@johmun/vue-tags-input";
export default {
  components: { md5, VueTagsInput },
  computed: {
    avatar() {
      return this.$store.getters.avatar;
    },
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
      organization: "",
      registerForm: {
        email: "",
        name: "",
        title: "",
        country: "",
        organizations: [],
        password: "",
        confimPassword: "",
      },
      registerFormValidate: {
        name: [
          {
            required: true,
            message: "The name cannot be empty",
            trigger: "blur",
          },
        ],
        email: [
          {
            required: true,
            message: "Email address cannot be empty",
            trigger: "blur",
          },
          {
            type: "email",
            message: "Incorrect email format",
            trigger: "blur",
          },
        ],
        password: [
          {
            required: true,
            min: 6,
            message: "Password must more than 6 words",
            trigger: "blur",
          },
        ],
        confimPassword: [
          {
            required: true,
            validator: validatePass,
            trigger: "blur",
          },
        ],
        title: [
          {
            required: true,
            message: "Title cannot be empty",
            trigger: "blur",
          },
        ],
        // country: [
        //   {
        //     required: false,
        //     message: "Please enter your country",
        //     trigger: "blur",
        //   },
        // ],
        // organizations: [
        //   {
        //     required: false,
        //     message: "Please enter your organizations",
        //     trigger: "blur",
        //   },
        // ],
      },
      visible: false,
      // 隐藏密码图标样式
      pwdType: "password", // 密码类型
    };
  },
  methods: {
    handleSubmit(name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          let organizations = [];
          for(let i = 0; i < this.registerForm.organizations.length; i++){
            organizations.push(this.registerForm.organizations[i].text);
          }
          var userData = {
            name: this.registerForm.name,
            email: this.registerForm.email,
            title: this.registerForm.title,
            password: md5(this.registerForm.password),
            country: this.registerForm.country,
            organizations: organizations,
          };

          this.axios
            .post("/GeoProblemSolving/user", userData)
            .then((res) => {
              if (res.data.code == -3) {
                this.$Message.error("This email has been registered!");
                this.registerForm.email = "";
              } else if (res.data.code == 0) {
                this.$Message.success("Register successfully!");
                this.$router.replace({ name: "Login" });
              }
            })
            .catch((err) => {
              throw err;
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
      reader.onload = (e) => {
        //正巧后台也是使用的base64 hah
        // 读取到的图片base64 数据编码 将此编码字符串传给后台即可,
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
    },
  },
};
</script>

