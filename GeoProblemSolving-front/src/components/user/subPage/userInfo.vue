<template>
  <div>
    <Row>
      <Col span="12" offset="6">
        <div style="text-align: center;font-family: 'Roboto Light'">
          <h2>Personal Information</h2>
          <span style="font-size: 14px;font-family: 'Roboto Light'">Basic information you use in Geo-problem solving platform</span>
        </div>
        <div>
          <Card>
            <div slot="title">
              <Icon type="ios-contact" size="20"/>
              About {{userInfo.name}}
            </div>
            <List>
              <ListItem>
                <span class="uTitle">Name</span>
                <div class="uAlign">
                  <span class="uContent">{{userInfo.name}}</span>
                </div>
              </ListItem>

              <ListItem>
                <span class="uTitle">Email</span>
                <div class="uAlign">
                  <span class="uContent">{{userInfo.email}}</span>
                </div>
              </ListItem>

<!--              <ListItem>-->
<!--                <span class="uTitle">Email</span>-->
<!--                <div class="uAlign">-->
<!--                  <span class="uContent">{{userInfo.email}}</span>-->
<!--                </div>-->
<!--              </ListItem>-->
              <ListItem>
                <span class="uTitle">Title</span>
                <div class="uAlign">
                  <span class="uContent">{{userInfo.title}}</span>
                </div>
              </ListItem>
              <ListItem>
                <span class="uTitle">Organizations</span>
                <div class="uAlign">
                  <span class="uContent">{{myOrganizations}}</span>
                </div>
              </ListItem>
              <ListItem>
                <span class="uTitle">Domain</span>
                <div class="uAlign">
                  <span class="uContent">{{myDomain}}</span>
                </div>
              </ListItem>
              <ListItem>
                <span class="uTitle">Phone</span>
                <div class="uAlign">
                  <span class="uContent">{{userInfo.phone}}</span>
                </div>
              </ListItem>
              <ListItem>
                <span class="uTitle">Country</span>
                <div class="uAlign">
                  <span class="uContent">{{userInfo.country}}</span>
                </div>
              </ListItem>
              <ListItem>
                <span class="uTitle">State/Province</span>
                <div class="uAlign">
                  <span class="uContent">{{userInfo.province}}</span>
                </div>
              </ListItem>
              <ListItem>
                <span class="uTitle">City</span>
                <div class="uAlign">
                  <span class="uContent">{{userInfo.city}}</span>
                </div>
              </ListItem>
            </List>
            <div style="margin-top: 30px;text-align: right">
              <Button @click="showEditUserInfoModal" type="info" style="margin-right: 20px">Edit Information</Button>
              <Button @click="showResetPwdModal" type="info">Change Password</Button>
            </div>
          </Card>
        </div>
        <!--        <div style="margin: 0 auto">-->
        <!--          <img-->
        <!--            v-bind:src="userInfo.avatar"-->
        <!--            v-if="userInfo.avatar!=''&&userInfo.avatar!=undefined&&userInfo.avatar!=null"-->
        <!--            :title="userInfo.name"-->
        <!--            style="width:40px;height:40px;vertical-align:middle;"-->
        <!--          />-->
        <!--          <avatar-->
        <!--            :username="userInfo.name"-->
        <!--            :size="40"-->
        <!--            style="margin-top:10px"-->
        <!--            :title="userInfo.name"-->
        <!--            v-else-->
        <!--          />-->
        <!--          <div>-->
        <!--            <span style="font-size: 18px">Welcome,</span>-->
        <!--            <span style="color: #0056b3; font-size: 18px">{{userInfo.name}}</span>-->
        <!--          </div>-->
        <!--        </div>-->

        <!--        <Card>-->
        <!--          <p slot="title">Personal Information</p>-->
        <!--          <div>Name: <span style="margin-left: 10px;font-size: 15px">{{userInfo.name}}</span></div>-->
        <!--          <div style="margin-top: 20px">Email: <span style="margin-left: 10px;font-size: 15px">{{userInfo.email}}</span></div>-->
        <!--          <div style="margin-top: 20px">Organizations: <span style="margin-left: 10px;font-size: 15px">{{myOrganizations}}</span></div>-->
        <!--          <hr/>-->
        <!--                  <Button @click="showEditUserInfoModal">Edit Information</Button>-->
        <!--                  <br/>-->
        <!--                  <Button @click="showResetPwdModal">Change Password</Button>-->
        <!--                </Card>-->
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

          <FormItem label="Province">
            <Input v-model="userInfoFormItems.province" placeholder="province"> </Input>
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
      <div slot="footer">
        <Button @click="editUserInfoModal = false">Cancel</Button>
        <Button
          type="primary"
          @click="updateUserInfo"
        >Save changes
        </Button>
      </div>
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
        @click="resetPwd()"
        :disabled="resetPwdFormItems.newPwd != resetPwdFormItems.validPwd || resetPwdFormItems.oldPwd == '' || resetPwdFormItems.newPwd == ''"
      >Submit
      </Button>
    </Modal>
  </div>
</template>

<script>
  import Avatar from "vue-avatar";
  import md5 from "js-md5";
  import VueTagsInput from "@johmun/vue-tags-input";
  // import image2Base64 from 'image-to-base64/browser'

  export default {
    name: "userInfo",
    components: {
      Avatar,
      VueTagsInput
    },
    data() {
      const validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('Please enter your password.'));
        } else if (value !== this.resetPwdFormItems.newPwd) {
          callback(new Error('The two input passwords do not match!'))
        } else {
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
          //用于 localUser
          userId: "",
          name: "",
          title: "",
          organizations: [],
          domain: [],
          phone: "",
          country: "",
          province: "",
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
      orgTags: function (tags) {
        let orgs = [];
        console.log(tags)
        for (let i = 0; i < tags.length; i++) {
          orgs.push(tags[i].text);
        }
        this.userInfoFormItems.organizations = orgs;
      },
      domainTags: function (tags) {
        let domainTags = [];
        for (let i = 0; i < tags.length; i++) {
          domainTags.push(tags[i].text);
        }
        this.userInfoFormItems.domain = domainTags;
      },
      initInfo: function () {
        this.userInfo = this.$store.getters.userInfo;
        this.userInfoFormItems.userId = this.userInfo.userId;
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
        let avatarBase64 = "";
        //在上传图片的时候使用 imageToBase64.js 转换为base64 即可
        // "avatar": avatarBase64,
        let updateInfo = {
          "userId": this.$store.getters.userId,
          "name": this.userInfoFormItems.name,
          "title": this.userInfoFormItems.title,
          "organizations": this.userInfoFormItems.organizations,
          "domain": this.userInfoFormItems.domain,
          "phone":  this.userInfoFormItems.phone,
          "city": this.userInfoFormItems.city,
          "province": this.userInfoFormItems.province,
          "homepage": this.userInfoFormItems.homepage,
          "introduction": this.userInfoFormItems.introduction
        };
        // image2Base64("https://whatever-image/").then(res=>{res})
        this.$axios
          .put("/GeoProblemSolving/user", updateInfo)
          .then(res => {
            if (res.data.code == 0){
              this.userInfo = this.userInfoFormItems;
              this.editUserInfoModal = false;
              //更新成功是否需要将新的内容返回给前端？
              this.$Notice.success({title: "Update Success."})
            }else {
              this.$Notice.error({title: "Update Failed."})
            }
          })
          .catch(err => {

          })
      },
      showResetPwdModal: function () {
        this.resetPasswordModal = true;
      },
      resetPwd: function () {
        let encodeNewPwd = md5(this.resetPwdFormItems.newPwd);
        let encodeOldPwd = md5(this.resetPwdFormItems.oldPwd);
        this.axios
          .get("/GeoProblemSolving/user/resetPwd/" + this.userInfo.email + "/" + encodeOldPwd + "/" + encodeNewPwd)
          .then(res => {
            if (res.data.code == 0) {
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
            orgTemp += this.userInfo.organizations[i] + "  ";
          }
        }
        return orgTemp;
      },
      myDomain: function () {
        let domainTemp = "";
        if (this.userInfo.domain != null) {
          for (let i = 0; i < this.userInfo.domain.length; i++) {
            domainTemp += this.userInfo.domain[i] + "  ";
          }
        }
        return domainTemp;
      },
      orgsTags: function () {
        let tempOrgs = [];
        for (let i = 0; i < this.organizationTags.length; i++) {
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

  .uTitle {
    padding-right: 20px;
    font-family: 'Roboto Light';
    color: #999999
  }

  .uContent {
    color: #333333;
  }

  .uAlign {
    position: absolute;
    left: 100px;
    text-align: left;
    width: 200px
  }
</style>
