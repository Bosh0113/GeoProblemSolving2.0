<style scoped>
.InputStyle {
  width: 300px;
}
</style>
<template>
<div>
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
          <FormItem label="New password" prop="newPassword">
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
          <FormItem label="Confirm password" prop="confirmPassword">
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
</div>

</template>
<script>
import md5 from "js-md5";
export default {
  components: {
    md5
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
        .get("/PExploration/user/newPassword?email="+this.formValidate.email+"&password="+md5(this.formValidate.newPassword))
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
  }
};
</script>
