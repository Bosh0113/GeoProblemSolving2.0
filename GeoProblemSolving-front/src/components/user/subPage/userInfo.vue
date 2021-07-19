<template>
  <div>
    <Row>
      <Col span="12" offset="6">
        <div style="text-align: center;font-family: 'Roboto Light'">
          <h2>Personal Information</h2>
          <span style="font-size: 14px;font-family: 'Roboto Light';">Basic information you use in Geo-problem solving platform</span>
        </div>
        <div>
          <Card>
            <div slot="title">
              <img
                :src="avatarUrl"
                v-if="avatarUrl != null && avatarUrl != '' && avatarUrl != undefined"
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
              <span> 
                <Icon type="ios-contact" size="25"/>
                About {{userInfo.name}}
              </span>
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
      </Col>
      <!-- 两个卡片 -->
    </Row>


    <Modal
      v-model="editUserInfoModal"
      width="600"
    >

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

          <FormItem label="Domain">
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

          <FormItem label="Avatar">
            <div>
              <div class="demo-upload-list" v-if="avatarUrl!=''&& avatarUrl != null  && avatarUrl != undefined">
                <template>
                  <img v-bind:src="avatarUrl" class="avatarImage" />
                  <div class="demo-upload-list-cover">
                    <Icon type="ios-eye-outline" @click.native="handleView()"></Icon>
                  </div>
                </template>
              </div>

              <div class="uploadBox">
                <Icon
                  type="ios-camera"
                  size="20"
                  style="position:absolute;margin:18px;"
                ></Icon>
                <input
                  id="choosePicture"
                  @change="uploadPhoto($event)"
                  type="file"
                  class="uploadAvatar"
                />
              </div>

              <Modal title="View Image" v-model="visible">
                <img :src="avatarUrl" v-if="visible" style="width: 100%" />
              </Modal>
            </div>
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
        avatar: null,
        visible: false,
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
          avatar: ""
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
      selectImg: function(){
        document.getElementById("choosePicture").click();
      },
      uploadPhoto(e) {
        // 利用fileReader对象获取file
        let file = e.target.files[0];
        let filesize = file.size;
        let filename = file.name;
        let imgcode = "";
        // 2,621,440   2M
        if (filesize > 1024*1024) {
          // 图片大于2MB
          this.$Message.error("size > 1MB");
        } else {
          var reader = new FileReader();
          reader.readAsDataURL(file);
          reader.onload = e => {
            // 读取到的图片base64 数据编码 将此编码字符串传给后台即可
            imgcode = e.target.result;
            this.avatar = imgcode;
            $("#choosePicture").val("");
          };
        }
      },
      handleView() {
        this.visible = true;
      },
      handleRemove() {
        this.userInfoFormItems.avatar = "";
      },
      orgTags: function (tags) {
        let orgs = [];
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

        //在上传图片的时候使用 imageToBase64.js 转换为base64 即可
        // "avatar": avatarBase64,
        let updateInfo = {
          "userId": this.$store.getters.userId,
          "name": this.userInfoFormItems.name,
          "title": this.userInfoFormItems.title,
          "organizations": this.userInfoFormItems.organizations,
          "domain": this.userInfoFormItems.domain,
          "phone": this.userInfoFormItems.phone,
          "city": this.userInfoFormItems.city,
          "province": this.userInfoFormItems.province,
          "homepage": this.userInfoFormItems.homepage,
          "introduction": this.userInfoFormItems.introduction
        };
        let avatarBase64 = this.avatar;
        if (avatarBase64.indexOf("base64") != -1){
          updateInfo["avatar"] = avatarBase64;
        }

        this.$axios
          .put("/GeoProblemSolving/user", updateInfo)
          .then(res => {
            if (res.data.code == 0) {
              // this.userInfo = this.userInfoFormItems;
              this.editUserInfoModal = false;

              if (this.avatar != res.data.data){
                this.avatar = res.data.data;
                this.$emit("changeAvatar", this.avatar)
              }
              //更新成功是否需要将新的内容返回给前端？
              this.$Notice.success({title: "Update Success."})
            } else {
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
      },
      avatarUrl: function () {
        if (this.avatar != undefined && this.avatar != null && this.avatar != '' && this.avatar.indexOf("base64") != -1){
          return this.avatar;
        }
        let temp = this.userInfo.avatar;
        if (this.avatar != undefined && this.avatar != null && this.avatar != '' && this.avatar.indexOf("/avatar/") != -1) {
          this.avatar = this.$store.getters.userServer + this.avatar;
        } else if (temp != undefined & temp != null && temp != ''){
          this.avatar  = this.$store.getters.userServer + temp;
        }else {
          this.avatar = '';
        }
        return this.avatar;
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
    width: 500px
  }

  .selectImg:hover {
    background-color: rgba(5, 192, 255, 0.55);
    cursor: pointer;
    box-shadow: 1px 2px 5px rgb(0 0 0 / 30%) !important;
    border-radius: 100%;
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
  .avatarImage {
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
</style>
