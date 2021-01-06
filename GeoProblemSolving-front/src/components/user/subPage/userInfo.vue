<template>
  <div>
    <Row>
      <Col span="10" offset="7">
        <Card>
          <img
            v-bind:src="userInfo.avatar"
            v-if="userInfo.avatar!=''&&userInfo.avatar!=undefined&&userInfo.avatar!=null"
            :title="userInfo.name"
            style="width:40px;height:40px;vertical-align:middle;"
          />
          <avatar
            :username="userInfo.name"
            :size="40"
            style="margin-top:10px"
            :title="userInfo.name"
            v-else
          />
          <div>Welcome, {{userInfo.name}}</div>
        </Card>

        <Card>
          <Card>
            <p slot="title">Personal Information</p>
            <div>Name: {{userInfo.name}}</div>
            <div>Email: {{userInfo.email}}</div>
            <div>Organizations: {{myOrganizations}}</div>
            <hr/>
            <Button @click="editUserInfoModal = true">Edit Information</Button>
            <br/>
            <Button @click="resetPasswordModal = true">Change Password</Button>
          </Card>
        </Card>
      </Col>
      <!-- 两个卡片 -->
    </Row>


    <Modal
      v-model="editUserInfoModal"
      width="600"
    >
      <!--   头像显示, 点击上传 -->
      <div style="margin-top: 25px">
        <Form
          :model="userInfoFormItems"
          :rules="userFormRuleValidate"
          :label-width="100"
          label-position="left"
        >

          <FormItem label="Name">
            <Input v-model="userInfoFormItems.name"> </Input>
          </FormItem>

          <FormItem label="Title">
            <Select v-model="userInfoFormItems.title">
              <Option value="Professor">Professor</Option>
              <Option value="Dr">Dr</Option>
              <Option value="Miss">Miss</Option>
              <Option value="Mr">Mr</Option>
              <Option value="Mrs">Mrs</Option>
              <Option value="Ms">Ms</Option>
              <Option value="Mx">Mx</Option>
            </Select>
          </FormItem>

          <FormItem label="Organizations">
            <vue-tags-input
              class="organization"
              v-model="organizations"
              :tags="userInfoFormItems.organizations"
              @tags-changed="(newTags) => (userInfoFormItems.organizations = newTags)"
            >
            </vue-tags-input>
            <!--        vue-tags-input -->
          </FormItem>

          <FormItem label="Subject Areas">
            <vue-tags-input
              class="organization"
              v-model="domains"
              :tags="userInfoFormItems.domain"
              @tags-changed="(newTags) => (userInfoFormItems.domain = newTags)"
            >
            </vue-tags-input>
          </FormItem>

          <FormItem label="Phone">
            <Input v-model="userInfoFormItems.phone" placeholder="Phone"></Input>
          </FormItem>

          <FormItem label="Country">
            <Input v-model="userInfoFormItems.country" placeholder="Country"> </Input>
          </FormItem>

          <FormItem label="City">
            <Input v-model="userInfoFormItems.city" placeholder="City"> </Input>
          </FormItem>

          <FormItem label="Homepage">
            <Input v-model="userInfoFormItems.homePage" placeholder="Homepage"> </Input>
          </FormItem>

          <FormItem label="Introduction">
            <Input
              v-model="userInfoFormItems.introduction"
              type="textarea"
              :autosize="{minRows: 2, maxRows: 5}"
              placeholder="Description"></Input>
          </FormItem>
        </Form>
      </div>
      <Button
        slot="footer"
        type="primary"
      >Submit
      </Button>
    </Modal>


    <Modal v-model="resetPasswordModal">
      <Form :model="resetPwdFormItems" :rules="resetPwdRuleValidate">
        <FormItem label="Old Password">
          <Input v-model="resetPwdFormItems.oldPwd" type="password" placeholder="Old password"> </Input>
        </FormItem>
        <FormItem label="New Password">
          <Input v-model="resetPwdFormItems.newPwd" type="password" placeholder="New password"> </Input>
        </FormItem>
        <FormItem label="Confirm Password">
          <Input v-model="resetPwdFormItems.validPwd" type="password" placeholder="New password confirm"></Input>
        </FormItem>
      </Form>
      <Button
        slot="footer"
        type="primary"
        @click="resetPwd()">Submit</Button>
    </Modal>
  </div>
</template>

<script>
  import Avatar from "vue-avatar";
  import md5 from "js-md5";
  import VueTagsInput from "@johmun/vue-tags-input";

  export default {
    name: "userInfo",
    components: {
      Avatar,
      VueTagsInput
    },
    data() {
      let validateConfirmPwd = (rule, value, callback) => {
        if (value === "") {
          callback(new Error("Please enter your password again."))
        } else if (value !== this.resetPwdFormItems.newPwd) {
          callback(new Error("The two passwords are inconsistent."))
        } else {
          callback();
        }
      };
      return {
        editUserInfoModal: false,
        resetPasswordModal: false,
        userInfo: {},
        organizations: "",
        domains: "",
        userInfoFormItems: {
          avatar: "",
          name: "",
          homePage: "",
          title: "",
          organizations: [],
          introduction: "",
          domain: [],
          phone: "",
          country: "",
          city: "",
        },
        userFormRuleValidate: {
          name: [
            {
              required: true,
              message: "The name cannot be empty",
              trigger: "blur"
            }
          ],
          homePage: [],
          title: [
            {
              required: true,
              message: "Job Title cannot be empty",
              trigger: "blur"
            }
          ],
          phone: [
            {
              required: false,
              message: "Please enter your phone number",
              trigger: "blur"
            }
          ],
          organizations: [
            {
              required: true,
              message: "Please enter your affiliation",
              trigger: "blur"
            }
          ],
          introduction: [
            {
              required: false,
              message: "Please enter a personal introduction",
              trigger: "blur"
            }
          ],
          domain: [
            {
              required: false,
              message: "Please enter your research field",
              trigger: "blur"
            }
          ],
          country: [
            {
              required: true,
              message: "Please enter your country",
              trigger: "blur"
            }
          ],
          city: [
            {
              required: false,
              message: "Please enter your city",
              trigger: "blur"
            }
          ],
        },
        resetPwdFormItems: {
          oldPwd: "",
          newPwd: "",
          validPwd: ""
        },
        resetPwdRuleValidate: {
          oldPwd: [
            {required: true, message: 'The old password cannot be empty', trigger: 'blur'}
          ],
          newPwd: [
            {required: true, message: 'The new password cannot be empty', trigger: 'blur'}
          ],
          validPwd: [
            {required: true, validator: validateConfirmPwd, trigger: "blur"}
          ]
        }
      }
    },
    created() {
      this.initInfo();
    },
    mounted() {
    },
    methods: {
      initInfo: function () {
        this.userInfo = this.$store.getters.userInfo;
        this.userInfoFormItems.avatar = this.userInfo.avatar;
        this.userInfoFormItems.name = this.userInfo.name;
        this.userInfoFormItems.homePage = this.userInfo.homepage;
        this.userInfoFormItems.title = this.userInfo.title;
        this.userInfoFormItems.organizations = this.userInfo.organizations;
        this.userInfoFormItems.introduction = this.userInfo.introduction;
        this.userInfoFormItems.domain = this.userInfo.domain;
        this.userInfoFormItems.phone = this.userInfo.phone;
        this.userInfoFormItems.country = this.userInfo.country;
        this.userInfoFormItems.city = this.userInfo.city;
        if (this.userInfo.organizations == null) {
          this.userInfoFormItems.organizations = [];
        }
        if (this.userInfo.domain == null) {
          this.userInfoFormItems.domain = [];
        }
      },
      resetPwd: function () {
        let md5_newPwd = md5(this.resetPwdFormItems.newPwd);
        let md5_oldPwd = md5(this.resetPwdFormItems.oldPwd);
        let paramUrl = "email=" + this.userInfo.email + "&oldPwd=" + md5_oldPwd + "&newPwd=" + md5_newPwd;
        this.axios
          .post("http://106.14.78.235/AuthServer/user/newPassword?" + paramUrl)
          .then(res => {
            if (res.data == 1) {
              this.$router.push({name: "Login"})
            } else {
              this.$Message.info("Old password error")
            }
          })
          .catch(err => {
            this.$Message.error("Modify password fail.")
          })

      },
      updateUserInfo: function () {
        let organizations = [];
        let domains = [];
        for (let i = 0; i < this.userInfoFormItems.organizations.length; i++) {
          organizations.push(this.userInfoFormItems.organizations[i].text);
        }
        for (let i = 0; i < this.userInfoFormItems.domain.length; i++) {
          domains.push(this.userInfoFormItems.domain[i].text);
        }
        console.log("organizations: " + organizations);
        console.log("domains: " + domains);

      }
    },
    computed: {
      myOrganizations: function () {
        let orgTemp = "";
        if (this.userInfo.organizations != null) {
          for (let i = 0; i < this.userInfo.organizations.length; i++) {
            orgTemp += this.userInfo.organizations[i] + "; ";
          }
        }
        return orgTemp;
      }
    }
  }
</script>

<style scoped>
  .organization >>> .ti-input {
    display: inline-block;
    width: 469px;
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

  .organization >>> input {
    opacity: 0.5;
  }

  .formStyle {
    display: flex;
    justify-content: center;
  }
</style>
