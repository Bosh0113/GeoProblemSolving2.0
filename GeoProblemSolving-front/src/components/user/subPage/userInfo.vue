<template>
  <div>
    <div id="userPic" style="margin-top: 20px;text-align: center;">
      <!-- 用户上传照片作为头像 图片显示失败 -->
     <img
        :src="avatarUrl"
        v-if="userInfo.avatar != null && userInfo.avatar != '' && userInfo.avatar != undefined"
        :title="userInfo.name"
        style="vertical-align:middle;width: 80px;height: 80px;"
      />

      <!-- v-else -->
      <avatar
        :username="userInfo.name"
        :size="80"
        style="margin-top:10px;margin-left: 47%;"
        :title="userInfo.name"
        v-else
      />
      <p style="margin-bottom: 20px;"></p>
      <h1 style="display: inline-block;">Welcome,</h1>
      <h1 style="color: #2d8cf0;display: inline-block;" v-show="userInfo.title!=''">{{userInfo.title}}. </h1>
      <h1 style="color: #2d8cf0;display: inline-block;">{{userInfo.name}}</h1>
      <p style="margin-bottom: 20px;"></p>
    </div>
    <div v-if="useUserInfoCSS">
      <Row>
        <Col span="10" offset="2">
          <!-- <div id="title">
            <h1 style="text-align: center;margin-top: 10px;">Personal Information</h1>
            <h3 style="text-align: center;margin-bottom: 10px;">Basic information you use in Geo-problem solving platform</h3>
          </div> -->
          <div>
            <Card style="height:450px;margin-bottom: 10px;" class="userInfoCard">
              <h2>Personal Information</h2>
              <List style="margin-top: 3%;">
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
                  <span class="uTitle">Phone</span>
                  <div class="uAlign">
                    <span class="uContent">{{userInfo.phone}}</span>
                  </div>
                </ListItem>
               <!-- <ListItem>
                  <span class="uTitle">Title</span>
                  <div class="uAlign">
                    <span class="uContent">{{userInfo.title}}</span>
                  </div>
                </ListItem> -->
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
                  <span class="uTitle">Country</span>
                  <div class="uAlign">
                    <span class="uContent">{{userInfo.country}}</span>
                  </div>
                </ListItem>
               <ListItem >
                  <span class="uTitle">State/Province</span>
                  <div class="uAlign">
                    <span class="uContent">{{userInfo.state}}</span>
                  </div>
                </ListItem>
                <ListItem >
                  <span class="uTitle">City</span>
                  <div class="uAlign">
                    <span class="uContent">{{userInfo.city}}</span>
                  </div>
                </ListItem>
              </List>

            </Card>
          </div>
        </Col>

        <!-- 两个卡片-->
        <Col span="10">
          <!-- <div id="title">
            <h1 style="text-align: center;margin-top: 10px;">Personal Information</h1>
            <h3 style="text-align: center;margin-bottom: 10px;">Basic information you use in Geo-problem solving platform</h3>
          </div> -->
          <div >
            <Card style="margin-left: 5%;height:450px;" class="userInfoCard">
              <div >
                <h2>&nbsp;</h2>
                <!-- <h1>setting</h1> -->
                <list style="margin-top: 3%;">
                  <ListItem >
                    <span class="uTitle">Homepage</span>
                    <div class="uAlign">
                      <a :href="'http://'+userInfo.homepage">
                        <span class="uContent">{{userInfo.homepage}}</span>
                      </a>
                    </div>
                  </ListItem>
                  <ListItem >
                    <span class="uTitle">Introduction</span>
                    <div class="uAlignIntro">
                      <span class="uContentIntro" >{{userInfo.introduction}}</span>
                    </div>
                  </ListItem>
                  <ListItem >
                    <div></div>
                  </ListItem>
                </list>
                <div>
                  <Button @click="showEditUserInfoModal" type="info" style="width: 100%;">Edit Information</Button> <br>
                  <Button @click="showResetPwdModal" type="info" style="margin-top: 10px;width: 100%;background-color: #2bbbad;">Change Password</Button>
                </div>
              </div>
            </Card>
          </div>
        </Col>
      </Row>

    </div>

    <!-- v-else -->
    <div v-else>
      <Row>
        <Col span="22" offset="1">
          <!-- <div id="title">
            <h1 style="text-align: center;margin-top: 10px;">Personal Information</h1>
            <h3 style="text-align: center;margin-bottom: 10px;">Basic information you use in Geo-problem solving platform</h3>
          </div> -->
          <div>
            <Card class="userInfoCard" style="margin-bottom: 10px;">
              <h2 style="text-align: left;">Personal Information</h2>
              <List>
                <ListItem>
                  <span class="uTitle">Name</span>
                  <div class="uAlign">
                    <span class="uContentElse">{{userInfo.name}}</span>
                  </div>
                </ListItem>

                <ListItem>
                  <span class="uTitle">Email</span>
                  <div class="uAlign">
                    <span class="uContentElse">{{userInfo.email}}</span>
                  </div>
                </ListItem>
                <ListItem>
                  <span class="uTitle">Phone</span>
                  <div class="uAlign">
                    <span class="uContentElse">{{userInfo.phone}}</span>
                  </div>
                </ListItem>
               <!-- <ListItem>
                  <span class="uTitle">Title</span>
                  <div class="uAlign">
                    <span class="uContentElse">{{userInfo.title}}</span>
                  </div>
                </ListItem> -->
                <ListItem>
                  <span class="uTitle">Organizations</span>
                  <div class="uAlign">
                    <span class="uContentElse">{{myOrganizations}}</span>
                  </div>
                </ListItem>
                <ListItem>
                  <span class="uTitle">Domain</span>
                  <div class="uAlign">
                    <span class="uContentElse">{{myDomain}}</span>
                  </div>
                </ListItem>

                <ListItem>
                  <span class="uTitle">Country</span>
                  <div class="uAlign">
                    <span class="uContentElse">{{userInfo.country}}</span>
                  </div>
                </ListItem>
               <ListItem >
                  <span class="uTitle">State/Province</span>
                  <div class="uAlign">
                    <span class="uContentElse">{{userInfo.state}}</span>
                  </div>
                </ListItem>
                <ListItem >
                  <span class="uTitle">City</span>
                  <div class="uAlign">
                    <span class="uContentElse">{{userInfo.city}}</span>
                  </div>
                </ListItem>
                <ListItem >
                  <span class="uTitle">Homepage</span>
                  <div class="uAlign">
                    <a :href="'http://'+userInfo.homepage">
                      <span class="uContentElse">{{userInfo.homepage}}</span>
                    </a>
                  </div>
                </ListItem>
                <ListItem style="height: 200px;">
                  <span class="uTitle">Introduction</span>
                  <div class="uAlignIntro">
                    <span class="uContentIntroElse">{{userInfo.introduction}}</span>
                  </div>
                </ListItem>
                <ListItem >
                  <div></div>
                </ListItem>
              </List>
              <div>
                <Button @click="showEditUserInfoModal" type="info" style="width: 40%;margin-left: 5%;">Edit Information</Button>
                <Button @click="showResetPwdModal" type="info" style="width: 40%;background-color: #2bbbad;margin-left: 2%;">Change Password</Button>
              </div>
            </Card>
          </div>
        </Col>
      </Row>
    </div>

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

          <FormItem label="State/province">
            <Input v-model="userInfoFormItems.state" placeholder="state/province"> </Input>
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
              <!-- 显示用户原始头像信息 -->
              <div class="demo-upload-list" v-if="userInfoFormItems.avatar!=''&& userInfoFormItems.avatar != null  && userInfoFormItems.avatar != undefined && !reUpload ">
                <template>
                  <img v-bind:src="avatarUrl" class="avatarImage"/>
                  <div class="demo-upload-list-cover">
                    <Icon type="ios-eye-outline" @click.native="handleView()"></Icon>
                    <Icon type="ios-trash-outline" @click.native="handleRemove()"></Icon>
                  </div>
                </template>
              </div>
              <!-- 显示用户新上传的头像信息 -->
              <div class="demo-upload-list" v-if="userInfoFormItems.avatar!=''&& userInfoFormItems.avatar != null  && userInfoFormItems.avatar != undefined && reUpload ">
                <template>
                  <img v-bind:src="userInfoFormItems.avatar" class="avatarImage"/>
                  <div class="demo-upload-list-cover">
                    <Icon type="ios-eye-outline" @click.native="handleView()"></Icon>
                    <Icon type="ios-trash-outline" @click.native="handleRemove()"></Icon>
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

              <Modal title="View Image" v-model="visible" v-if="!reUpload">
                <img :src="avatarUrl" v-if="visible" style="width: 100%" />
              </Modal>
              <Modal title="View Image" v-model="visible" v-if="reUpload">
                <img :src="userInfoFormItems.avatar" v-if="visible" style="width: 100%" />
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
        contentHeight:"",
        editUserInfoModal: false,
        resetPasswordModal: false,
        avatar: null,
        visible: false,
        useUserInfoCSS: true,
        reUpload: false,
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
          state: "",
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
      this.reSize();
      window.addEventListener("resize", this.reSize);
    },
    beforeDestroy: function () {
      window.removeEventListener("resize", this.reSize);
    },
    methods: {
      resizeContent() {
        if (window.innerHeight > 675) {
          this.contentHeight = window.innerHeight - 120;
        } else {
          this.contentHeight = 555;
        }
        window.onresize = () => {
          return (() => {
            this.resizeContent();
          })();
        };
      },
      reSize() {
        // if (window.innerHeight > 675) {
        //   this.contentHeight = window.innerHeight - 120 + "px";
        // } else {
        //   this.contentHeight = 675 - 120 + "px";
        // }
        if (window.innerWidth < 1200) {
          this.useUserInfoCSS = false;
        } else {
          this.useUserInfoCSS = true;
        }
      },
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
            this.reUpload = true;
            this.userInfoFormItems.avatar = imgcode;
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
        // this.userInfoFormItems.organizations = orgs;
        this.restoreFormat(orgs,"org");
      },
      orgTagsUpload: function (tags) {
        let orgs = [];
        for (let i = 0; i < tags.length; i++) {
          orgs.push(tags[i].text);
        }
        return orgs;
      },
      domainTags: function (tags) {
        let domainTags = [];
        for (let i = 0; i < tags.length; i++) {
          domainTags.push(tags[i].text);
        }
        // this.userInfoFormItems.domain = domainTags;
        this.restoreFormat(domainTags,"dom");
      },
      domainTagsUpload: function (tags) {
        let domainTags = [];
        for (let i = 0; i < tags.length; i++) {
          domainTags.push(tags[i].text);
        }
        return domainTags;
      },
      initInfo: function () {
        this.userInfo = this.$store.getters.userInfo;
        this.userInfo.state = this.$store.getters.userProvince;
        if (this.userInfo.organizations == null) {
          this.userInfo.organizations = [];
        }
        if (this.userInfo.domain == null) {
          this.userInfo.domain = [];
        }
        // this.avatar = this.userInfo.avatar;
      },
      showEditUserInfoModal: function () {
        this.userInfoFormItems = this.userInfo;
        if(this.userInfoFormItems.organizations.length > 0 && this.userInfoFormItems.organizations[0].text == undefined){
          this.restoreFormat(this.userInfoFormItems.organizations,"org");
        }
        if(this.userInfoFormItems.domain.length > 0 && this.userInfoFormItems.domain[0].text == undefined){
          this.restoreFormat(this.userInfoFormItems.domain,"dom");
        }
        this.editUserInfoModal = true;
      },
      restoreFormat:function(arr,str){
        let restoreFormatArr = [];
        for (let i = 0; i < arr.length; i++) {
          restoreFormatArr.push({ text: arr[i] });    //必须将它还原成[{text:'data1'},{text:'data2}]这种格式
        }
        if(str == "org"){
          this.userInfoFormItems.organizations = restoreFormatArr;
        } else if(str == "dom"){
          this.userInfoFormItems.domain = restoreFormatArr;
        }
      },
      updateUserInfo: function () {
        //在上传图片的时候使用 imageToBase64.js 转换为base64 即可
        // "avatar": avatarBase64,
        let updateInfo =  {};
        if (this.reUpload == true || this.userInfoFormItems.avatar == ""){
          updateInfo = {
            "userId": this.$store.getters.userId,
            "name": this.userInfoFormItems.name,
            "title": this.userInfoFormItems.title,
            "organizations": this.orgTagsUpload(this.userInfoFormItems.organizations),
            "domain": this.domainTagsUpload(this.userInfoFormItems.domain),
            "phone": this.userInfoFormItems.phone,
            "country": this.userInfoFormItems.country,
            "city": this.userInfoFormItems.city,
            "state": this.userInfoFormItems.state,
            "homepage": this.userInfoFormItems.homepage,
            "introduction": this.userInfoFormItems.introduction,
            "avatar": this.userInfoFormItems.avatar
          };
        } else if (this.reUpload == false){
          updateInfo = {
            "userId": this.$store.getters.userId,
            "name": this.userInfoFormItems.name,
            "title": this.userInfoFormItems.title,
            "organizations": this.orgTagsUpload(this.userInfoFormItems.organizations),
            "domain": this.domainTagsUpload(this.userInfoFormItems.domain),
            "phone": this.userInfoFormItems.phone,
            "country": this.userInfoFormItems.country,
            "city": this.userInfoFormItems.city,
            "state": this.userInfoFormItems.state,
            "homepage": this.userInfoFormItems.homepage,
            "introduction": this.userInfoFormItems.introduction
          };
        }
        // let avatarBase64 = this.userInfoFormItems.avatar;
        // if (avatarBase64.indexOf("base64") != -1){
        //   updateInfo["avatar"] = avatarBase64;
        // }
        // console.log(this.userInfoFormItems.avatar);

        this.$axios
          .put("/GeoProblemSolving/user", updateInfo)
          .then(res => {
            if (res.data.code == 0) {
              // this.userInfo = this.userInfoFormItems;
              this.editUserInfoModal = false;
              if (this.userInfoFormItems.avatar == ""){
                this.avatar = "";
                this.$emit("changeAvatar", this.avatar);
              } else if (this.avatar != res.data.data){
                this.avatar = res.data.data;
                this.$emit("changeAvatar", this.avatar);
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
        if (this.userInfo.organizations != null  && this.userInfo.organizations.length > 0 && this.userInfo.organizations[0].text == undefined) {
          for (let i = 0; i < this.userInfo.organizations.length; i++) {
            orgTemp += this.userInfo.organizations[i] + "  ";
          }
        } else if(this.userInfo.organizations != null  && this.userInfo.organizations.length > 0 && this.userInfo.organizations[0].text != undefined){
          for (let i = 0; i < this.userInfo.organizations.length; i++) {
            orgTemp += this.userInfo.organizations[i].text + "  ";
          }
        } else {
          orgTemp = "";
        }
        return orgTemp;
      },
      myDomain: function () {
        let domainTemp = "";
        if (this.userInfo.domain != null && this.userInfo.domain.length > 0 && this.userInfo.domain[0].text == undefined) {
          for (let i = 0; i < this.userInfo.domain.length; i++) {
            domainTemp += this.userInfo.domain[i] + "  ";
          }
        }else if(this.userInfo.domain != null && this.userInfo.domain.length > 0 && this.userInfo.domain[0].text != undefined){
          for (let i = 0; i < this.userInfo.domain.length; i++) {
            domainTemp += this.userInfo.domain[i].text + "  ";
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
        let temp = this.userInfo.avatar;
        if(this.avatar != undefined && this.avatar != null && this.avatar != '' && this.avatar.indexOf("/userServer/") != -1){
          return this.avatar;
        } else if (this.avatar != undefined && this.avatar != null && this.avatar != '' && this.avatar.indexOf("base64") != -1){
          return this.avatar;
        } else if (this.avatar != undefined && this.avatar != null && this.avatar != '' && this.avatar.indexOf("/avatar/") != -1) {
          this.avatar = this.$store.getters.userServer + this.avatar;
        } else if (temp != undefined & temp != null && temp != '' && temp.indexOf("/avatar/") != -1){
          this.avatar  = this.$store.getters.userServer + temp;
        } else if (temp != undefined & temp != null && temp != '' && temp.indexOf("base64") != -1){
          this.avatar = temp;
        } else {
          this.avatar = '';
        }
        return this.avatar;
      },
      avatarUrlForm:function(){
        return this.$store.getters.userServer + this.userInfoFormItems.avatar;
      }
    }
  }
</script>

<style scoped>
  .userInfoCard{
    background-color: white;
    border-radius: 3px;
    border:1px solid #dadce0;
    box-shadow:  0 3.2px 7.2px 0 rgb(0 0 0 / 13%), 0 0.6px 1.8px 0 rgb(0 0 0 / 11%);
    opacity: 0.95;
  }

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
    color: #999999;
    margin-left: 10%;
  }

  .uContent {
    color: #333333;
    margin-left: 20%;
  }

  .uContentIntro{
    color: #333333;
    margin-left: 10%;
  }

  .uContentElse {
    color: #333333;
    margin-left: 40%;
  }

  .uContentIntroElse{
    color: #333333;
    margin-left: 10%;
  }

  .uAlign {
    position: absolute;
    left: 100px;
    text-align: left;
    width: 400px
  }

  .uAlignIntro {
    display:flex;
    align-items: center;
    height: 200px;
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
