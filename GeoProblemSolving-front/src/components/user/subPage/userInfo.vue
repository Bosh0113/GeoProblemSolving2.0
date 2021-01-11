<template>
  <div>
    <Row>
      <Col span="8" offset="8">
        <div>
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
          <div>
            <span style="font-size: 18px">Welcome,</span>
            <span style="color: #0056b3; font-size: 18px">{{userInfo.name}}</span>
          </div>
        </div>

        <Card>
          <p slot="title">Personal Information</p>
          <div>Name: {{userInfo.name}}</div>
          <div>Email: {{userInfo.email}}</div>
          <div>Organizations: {{myOrganizations}}</div>
          <hr/>
          <Button @click="showEditUserInfoModal">Edit Information</Button>
          <br/>
          <Button @click="showResetPwdModal">Change Password</Button>
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

<!--          @tags-changed="(newTags) => (userInfoFormItems.organizations = newTags)"-->
          <FormItem label="Organizations">
            <vue-tags-input
              class="organization"
              v-model="organizations"
              :tags="userInfoFormItems.organizations"
              @tags-changed="orgTags"
            >
            </vue-tags-input>
            <!--        vue-tags-input -->
          </FormItem>

          <FormItem label="Subject Areas">
            <vue-tags-input
              class="organization"
              v-model="domains"
              :tags="userInfoFormItems.domain"
              @tags-changed="domainTags"
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
            <Input v-model="userInfoFormItems.homepage" placeholder="Homepage"> </Input>
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
        @click="updateUserInfo"
      >Submit
      </Button>
    </Modal>


    <Modal v-model="resetPasswordModal">
      <Form ref="formValidate" :model="resetPwdFormItems" :rules="resetPwdRuleValidate">
        <FormItem label="Old Password" prop="oldPwd">
          <Input v-model="resetPwdFormItems.oldPwd" type="password" placeholder="Old password"> </Input>
        </FormItem>
        <FormItem label="New Password" prop="newPwd">
          <Input v-model="resetPwdFormItems.newPwd" type="password" placeholder="New password"> </Input>
        </FormItem>
        <FormItem label="Confirm Password" prop="validPwd">
          <Input v-model="resetPwdFormItems.validPwd" type="password" placeholder="New password confirm"></Input>
        </FormItem>
      </Form>
      <Button
        slot="footer"
        type="primary"
        @click="resetPwd()">Submit
      </Button>
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
      const validatePass = (rule, value, callback) => {
        if (value === ''){
          callback(new Error('Please enter your password.'));
        }else if (value !== this.resetPwdFormItems.newPwd){
          callback(new Error('The two input passwords do not match!'))
        }else {
          callback();
        }
      }
      return {
        editUserInfoModal: false,
        resetPasswordModal: false,
        //显示的内容
        userInfo: {},
        //tags
        organizations: "",
        domains: "",
        //编辑的内容
        userInfoFormItems: {
          // avatar: "",
          name: "",
          title: "",
          organizations: [],
          domain: [],
          phone: "",
          country: "",
          city: "",
          homepage: "",
          introduction: "",
        },
        userFormRuleValidate: {
          name: [
            {
              required: true,
              message: "The name cannot be empty",
              trigger: "blur"
            }
          ],
          homepage: [],
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
        //重设密码表单
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
            {required: true, validator: validatePass, trigger: "blur"}
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
      orgTags: function(tags){
        let orgs = [];
        console.log(tags)
        for (let i = 0; i < tags.length; i++){
          orgs.push(tags[i].text);
        }
        this.userInfoFormItems.organizations = orgs;
      },
      domainTags: function(tags){
        let domainTags = [];
        for (let i = 0; i < tags.length; i++){
          domainTags.push(tags[i].text);
        }
        this.userInfoFormItems.domain = domainTags;
      },
      initInfo: function () {
        this.userInfo = this.$store.getters.userInfo;
        if (this.userInfo.organizations == null) {
          this.userInfo.organizations = [];
        }
        if (this.userInfo.domain == null) {
          this.userInfo.domain = [];
        }
      },
      showEditUserInfoModal: function () {
        this.editUserInfoModal = true;
        this.userInfoFormItems = this.userInfo;
      },
      updateUserInfo: function () {
        let formData = new URLSearchParams();
        formData.append("userId", this.$store.getters.userId);
        formData.append("name", this.userInfoFormItems.name);
        formData.append("title", this.userInfoFormItems.title);
        formData.append("organizations", this.userInfoFormItems.organizations);
        formData.append("domain", this.userInfoFormItems.domain);
        formData.append("phone", this.userInfoFormItems.phone);
        formData.append("country", this.userInfoFormItems.country);
        formData.append("city", this.userInfoFormItems.city);
        formData.append("introduction", this.userInfoFormItems.introduction);
        formData.append("homepage", this.userInfoFormItems.homepage);
        this.$axios
          .post("/GeoProblemSolving/user/update", formData)
          .then(res=>{
            this.userInfo = this.userInfoFormItems;
            this.editUserInfoModal =false;
            this.$Notice.success({title: "Update Success."})
          })
          .catch(err=>{

          })
      },
      showResetPwdModal: function () {
        this.resetPasswordModal = true;
      },
      resetPwd: function () {
        let md5_newPwd = md5(this.resetPwdFormItems.newPwd);
        let md5_oldPwd = md5(this.resetPwdFormItems.oldPwd);
        let paramUrl = "email=" + this.userInfo.email + "&oldPwd=" + md5_oldPwd + "&newPwd=" + md5_newPwd;
        this.axios
          .post("http://106.14.78.235/AuthServer/user/newPassword?" + paramUrl)
          .then(res => {
            if (res.data == 1) {
              this.resetPasswordModal = false;
              this.$Notice.success({
                title: "Reset password success."
              })
              this.resetPwdFormItems.newPwd = "";
              this.resetPwdFormItems.oldPwd = "";
              this.resetPwdFormItems.validPwd = "";
            } else {
              this.$Message.info("Old password error")
              this.resetPwdFormItems.newPwd = "";
              this.resetPwdFormItems.oldPwd = "";
              this.resetPwdFormItems.validPwd = "";
            }
          })
          .catch(err => {
            this.$Message.error("Modify password fail.")
            this.resetPwdFormItems.newPwd = "";
            this.resetPwdFormItems.oldPwd = "";
            this.resetPwdFormItems.validPwd = "";
          })

      },

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
      },
      orgsTags: function () {
        let tempOrgs = [];
        for (let i =0; i<this.organizationTags.length;i++){
          tempOrgs.push(this.organizationTags[i].text);
        }
        return this.userInfoFormItems.organizations;
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
