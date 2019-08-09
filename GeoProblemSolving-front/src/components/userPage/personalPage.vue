<style scoped>
.detailSidebar {
  margin-right: 20px;
}
.rightContent {
  margin-top: 20px;
  flex: 1;
}
/* 用户头像 */
.user-img {
  margin-top: 20px;
  width: 100%;
  max-height: 100%;
  text-align: center;
}
/* 注册时上传头像的用户的头像样式 */
.u_img {
  max-width: 100%;
  padding: 10px;
}
/* 注册时未上传头像的用户头像显示样式 */
.avatarStyle {
  margin: 0 auto;
}
/* 用户头像结束 */
body {
  overflow-x: hidden;
}
/* 侧边用户信息的显示样式 */
.single-info {
  padding: 5px;
  height: 30px;
  font-size: 12px;
  line-height: 15px;
}
/* 表示空格间距的 */
.whitespace {
  height: 20px;
}
/* 关于提交用户更改头像信息的样式 */
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
/* 2-25 add 实现的效果是旋浮上去出现下划线且变红 */
.projectsTitle:hover {
  /* color: red; */
  cursor: pointer;
}

/* 新定义的样式 */
.authorBtn:hover {
  background-color: #57a3f3;
  color: white;
}
.deleteBtn:hover {
  background-color: #ed4014;
  color: white;
}
.table table {
  table-layout: auto;
  width: 100% !important;
}
.participatoryProjectCard:hover {
  cursor: pointer;
}
.manageProjectsCard:hover {
  cursor: pointer;
}
/* 时间轴样式 */
.timeLineStyle {
  margin-left: 5%;
  height: 180px;
}
.parent .ivu-tabs-nav-container {
  font-size: 15px !important;
  font-weight: bold;
}
.fileBtnHoverGreen:hover {
  background-color: #19be6b;
  color: white;
}
.fileBtnHoverRed:hover {
  background-color: #ed4014;
  color: white;
}
.fileBtnHoverBlue:hover {
  background-color: #2db7f5;
  color: white;
}
</style>
<template>
  <div>
    <Row>
      <Col span="22" offset="1">
        <Row>
          <Col :lg="5" :md="8" :sm="10" :xs="12">
            <div class="detailSidebar" :style="{height:detailSidebarHeight}">
              <div class="user-img">
                <img
                  v-bind:src="userDetail.avatar"
                  class="u_img"
                  v-if="userDetail.avatar!=''&&userDetail.avatar!='undefined'
                  &&userDetail.avatar!='null'"
                >
                <avatar
                  style="width:200px"
                  class="avatarStyle"
                  :username="userDetail.userName"
                  :size="200"
                  :rounded="false"
                  v-else
                ></avatar>
              </div>
              <div class="single-info" :title="`Name: `+ userDetail.userName">
                <Icon type="ios-contact-outline" :size="20"/>
                <span>{{userDetail.userName}}</span>
              </div>
              <div class="single-info" :title="`Email:  ` + userDetail.email">
                <Icon type="ios-mail-outline" :size="20"/>
                <span>{{userDetail.email}}</span>
              </div>
              <div class="single-info"  v-show="userDetail.mobilePhone!=''">
                <Icon type="ios-call-outline" :size="20"/>
                <span>{{userDetail.mobilePhone}}</span>
              </div>
              <div class="single-info" :title="`Job Title:  `+userDetail.jobTitle">
                <Icon type="ios-hammer-outline" :size="20"/>
                <span>{{userDetail.jobTitle}}</span>
              </div>
              <div
                class="single-info"
                :title="`Position:  `+ userDetail.city + userDetail.country"
                v-show="userDetail.city!=''&&userDetail.country!=''"
              >
                <Icon type="ios-compass-outline" :size="20"/>
                <span>{{userDetail.city}}&nbsp{{userDetail.country}}</span>
              </div>
              <div
                class="single-info"
                :title="`Organization:  `+ userDetail.organization"
                v-show="userDetail.organization!=''"
                style="overflow:hidden;white-space:nowrap;text-overflow:ellipsis"
              >
                <Icon type="ios-home-outline" :size="20"/><span> {{userDetail.organization}}</span>
              </div>
              <div class="single-info" :title="`Direction:  `+ userDetail.direction" v-show="userDetail.direction!=''" style="overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
                <Icon type="ios-contract" :size="20"/><span> {{userDetail.direction}}</span>
              </div>
              <div class="single-info" :title="`Home Page:  `+ userDetail.homePage" v-show="userDetail.homePage!=''" style="overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
                <Icon type="md-link" :size="20"/>
                <span> {{userDetail.homePage}}</span>
              </div>
              <br>
              <div
                style="padding:10px;font-size:12px;border:1px dotted lightgray"
                title="Introduction"
                v-show="userDetail.introduction!=''"
              >{{userDetail.introduction}}</div>
              <div class="whitespace"></div>
              <div style="display:flex;justify-content:center">
                <Button class="fileBtnHoverBlue" style="height:40px" @click="editModalShow()" title="Edit">
                  <Icon type="md-create" :size="20"/>
                </Button>
                <Drawer
                  title="Profile Edit Panel"
                  placement="left"
                  :closable="true"
                  v-model="editProfileModal"
                  width="600px"
                >
                <vue-scroll :ops="ops">
                  <Form
                    ref="personalInfoItem"
                    :model="personalInfoItem"
                    :rules="ruleValidate"
                    :label-width="80"
                  >
                    <FormItem label="Name" prop="userName">
                      <Input v-model="personalInfoItem.userName" :class="{InputStyle: inputstyle}"></Input>
                    </FormItem>
                    <FormItem label="Job title" prop="jobTitle">
                      <Input v-model="personalInfoItem.jobTitle" :class="{InputStyle: inputstyle}"></Input>
                    </FormItem>
                    <FormItem label="E-mail" prop="email">
                      <Input
                        v-model="personalInfoItem.email"
                        :class="{InputStyle: inputstyle}"
                        disabled
                      ></Input>
                    </FormItem>
                    <FormItem label="Phone" prop="mobilePhone">
                      <Input
                        v-model="personalInfoItem.mobilePhone"
                        :class="{InputStyle: inputstyle}"
                      ></Input>
                    </FormItem>
                    <FormItem label="Country" prop="country">
                      <Input v-model="personalInfoItem.country" :class="{InputStyle: inputstyle}"></Input>
                    </FormItem>
                    <FormItem label="City" prop="city">
                      <Input v-model="personalInfoItem.city" :class="{InputStyle: inputstyle}"></Input>
                    </FormItem>
                    <FormItem label="Affiliation" prop="organization">
                      <Input
                        v-model="personalInfoItem.organization"
                        :class="{InputStyle: inputstyle}"
                      ></Input>
                    </FormItem>
                    <FormItem label="Field" prop="direction">
                      <Input v-model="personalInfoItem.direction" :class="{InputStyle: inputstyle}"></Input>
                    </FormItem>
                    <FormItem label="Home page" prop="homePage">
                      <Input v-model="personalInfoItem.homePage" :class="{InputStyle: inputstyle}"></Input>
                    </FormItem>
                    <FormItem label="Introduction" prop="introduction">
                      <Input
                        style="word-wrap:break-word"
                        v-model="personalInfoItem.introduction"
                        type="textarea"
                        :autosize="{minRows: 2,maxRows: 5}"
                      ></Input>
                    </FormItem>
                    <FormItem label="Avatar" prop="avatar">
                      <div>
                        <div class="demo-upload-list" v-if="personalInfoItem.avatar!=''">
                          <template>
                            <img v-bind:src="personalInfoItem.avatar" class="avatarImage">
                            <div class="demo-upload-list-cover">
                              <Icon type="ios-eye-outline" @click.native="handleView()"></Icon>
                              <Icon type="ios-trash-outline" @click.native="handleRemove()"></Icon>
                            </div>
                          </template>
                        </div>
                        <div class="uploadBox">
                          <Icon type="ios-camera" size="20" style="position:absolute;margin:18px;"></Icon>
                          <input @change="uploadPhoto($event)" type="file" class="uploadAvatar">
                        </div>
                        <Modal title="View Image" v-model="visible">
                          <img :src="personalInfoItem.avatar" v-if="visible" style="width: 100%">
                        </Modal>
                      </div>
                    </FormItem>
                    <FormItem>
                      <Button type="success" @click="submitProfileEdit('personalInfoItem')">Submit</Button>
                      <Button @click="resetForm()" style="margin-left: 50%" type="primary">Reset</Button>
                    </FormItem>
                  </Form>
                  </vue-scroll>
                </Drawer>
              </div>
            </div>
          </Col>
          <Col
            :lg="{span:18,offset:1}"
            :md="{span:15,offset:1}"
            :sm="{span:13,offset:1}"
            :xs="{span:11,offset:1}"
          >
            <div class="rightContent">
              <div class="parent">
                <Tabs value="Overview" style="font-size:20px" type="card">
                  <TabPane label="Overview" name="Overview">
                    <Col :lg="{span:22,offset:1}" :md="{span:22,offset:1}" :sm="{span:22,offset:1}">
                      <Card dis-hover>
                        <p slot="title">History line</p>
                        <div class="timeLineStyle">
                          <vue-scroll :ops="ops">
                          <Timeline>
                            <div v-if="userEventList.length==0">
                              <div style="display:flex;justify-content:center">
                                <Icon type="md-alert" size="40" color="gray"/>
                              </div>
                              <br>
                              <div style="display:flex;justify-content:center">
                                <h3
                                  style="text-align:center;width:80%"
                                >Sorry, there are no events now.</h3>
                              </div>
                            </div>
                              <TimelineItem
                                v-for="(item,index) in userEventList"
                                :key="index"
                                v-show="userEventList.length>0"
                              >
                                <strong>
                                  <p class="time">{{item.createTime}}</p>
                                </strong>
                                <p class="content">{{item.description}}</p>
                              </TimelineItem>
                          </Timeline>
                          </vue-scroll>
                        </div>
                      </Card>
                      <br>
                      <div style="margin-bottom:40px">
                        <Card dis-hover>
                          <p slot="title">Resource list</p>
                          <div style="height:500px">
                            <vue-scroll :ops="ops">
                              <Table :data="userResourceList" :columns="resourceColumn" class="table">
                                <template slot-scope="{ row }" slot="name">
                                  <strong>{{ row.name }}</strong>
                                </template>
                                <template slot-scope="{ row, index }" slot="action">
                                  <Button
                                    class="fileBtnHoverGreen"
                                    size="small"
                                    title="Download"
                                    @click="download(index)"
                                    icon="md-download"
                                    shape="circle"
                                    type="text"
                                  >
                                  </Button>
                                  <!-- <Button type="warning" size="small" style="margin-right: 10px">
                                    <Icon
                                      type="md-share"
                                      @click="processResourceModalShow(index)"
                                      title="share"
                                    />
                                  </Button> -->
                                  <Button
                                    @click="fileEditModalShow(index)"
                                    shape="circle"
                                    icon="md-create"
                                    title="Edit"
                                    size="small"
                                    class="fileBtnHoverBlue"
                                    type="text"
                                  ></Button>
                                  <Button
                                    class="fileBtnHoverRed"
                                    size="small"
                                    shape="circle"
                                    type="text"
                                    icon="md-close"
                                    title="Remove"
                                    @click="deleteResourceModalShow(userResourceList[index].resourceId)"
                                  >
                                  </Button>
                                </template>
                              </Table>
                            </vue-scroll>
                          </div>
                        </Card>
                      </div>
                    </Col>
                  </TabPane>
                  <TabPane label="Joined projects" name="Participatory">
                    <div style="height:900px;padding:5px;border:#dcdee2 solid 1px;">
                      <vue-scroll :ops="ops">
                        <Card :bordered="false" v-if="joinedProjectsList.length == 0">
                          <div style="display:flex;justify-content:center">
                            <Icon type="md-alert" size="40" color="gray"/>
                          </div>
                          <br>
                          <div style="display:flex;justify-content:center">
                            <h3
                              style="text-align:center;width:80%"
                            >Sorry, you didn't participate in any projects.</h3>
                          </div>
                        </Card>
                        <div
                          v-for="(item,index) in joinedProjectsList"
                          :key="index"
                          v-show="joinedProjectsList!=[]"
                        >
                          <Col :lg="{span:10, offset:1}" :md="{span:22, offset:1}">
                            <div
                              class="participatoryProjectCard"
                              @click="goSingleProject(item.projectId)"
                            >
                              <Card style="height:320px;margin-top:20px;">
                                <p slot="title" style="height:40x" class="projectsTitle">{{item.title}}</p>
                                <Button class="fileBtnHoverRed" slot="extra" @click.stop="quitModalShow(item)">Quit</Button>
                                <p
                                  style="height:200px;text-indent:2em;word-break:break-word"
                                >
                                <vue-scroll :ops="ops">{{item.introduction}}</vue-scroll></p>
                                <br>
                                <div style="height:40px">
                                  <span style="float:left">CreateTime:</span>
                                  <span style="float:right">{{item.createTime}}</span>
                                </div>
                              </Card>
                            </div>
                          </Col>
                        </div>
                      </vue-scroll>
                    </div>
                  </TabPane>
                  <TabPane label="Managed projects" name="Management">
                    <div style="height:900px;padding:5px;border:#dcdee2 solid 1px;">
                      <vue-scroll :ops="ops">
                        <Card :bordered="false" v-if="userManagerProjectList.length == 0">
                          <div style="display:flex;justify-content:center">
                            <Icon type="md-alert" size="40" color="gray"/>
                          </div>
                          <br>
                          <div style="display:flex;justify-content:center">
                            <h3
                              style="text-align:center;width:80%"
                            >Sorry, you didn't created any projects.</h3>
                          </div>
                        </Card>
                        <div
                          v-for="(mProject,index) in userManagerProjectList"
                          v-show="userManagerProjectList!='None'"
                          :key="index"
                        >
                          <Col :lg="{span:10, offset:1}" :md="{span:22, offset:1}">
                            <div
                              class="manageProjectsCard"
                              @click="goSingleProject(mProject.projectId)"
                            >
                              <Card style="height:320px;margin-top:20px">
                                <p slot="title" class="projectsTitle">{{mProject.title}}</p>
                                <Button
                                  class="authorBtn"
                                  type="default"
                                  slot="extra"
                                  title="Privilege change"
                                  style="margin:-5px 5px 0 5px"
                                  @click.stop="authorizeModalShow(index)"
                                  icon="md-happy"
                                ></Button>
                                <Button
                                  class="deleteBtn"
                                  type="default"
                                  slot="extra"
                                  style="margin:-5px 5px 0 5px"
                                  @click.stop="deleteProjectModalShow(mProject.projectId)"
                                  icon="md-close"
                                  title="remove"
                                ></Button>
                                <!--  @click.stop="deleteProjectModalShow(mProject.projectId)" -->
                                <!-- 表头结束 -->
                                <p
                                  style="height:200px;text-indent:2em;word-break:break-word"
                                ><vue-scroll :ops="ops">{{mProject.introduction}}</vue-scroll></p>
                                <!-- <hr> -->
                                <br>
                                <div>
                                  <span style="float:left">CreateTime:</span>
                                  <span style="float:right">{{mProject.createTime}}</span>
                                </div>
                              </Card>
                            </div>
                          </Col>
                        </div>
                      </vue-scroll>
                    </div>
                  </TabPane>
                </Tabs>
              </div>
            </div>
          </Col>
        </Row>
        <div></div>
      </Col>
    </Row>
    <!-- 授权的modal -->
    <Modal
      v-model="authorizeProjectModal"
      @on-ok="authorize()"
      @on-cancel
      ok-text="Assure"
      cancel-text="Cancel"
    >
      <p style="slot">Hand the project to others</p>
      <div>
        <RadioGroup v-model="selectManagerId">
          <Radio v-for="member in projectMemberList" :key="member.index" :label="member.userId">
            <span>{{member.userName}}</span>
          </Radio>
        </RadioGroup>
        <!-- 用radio将用户表示出来 -->
        <!-- <tag v-for="(member,index) in projectMemberList" @click="choose(index)" :key="member.index">{{member.userName}}</tag> -->
      </div>
    </Modal>
    <Modal
      title="Quit Project"
      v-model="quitModal"
      @on-ok="quitProject()"
      @on-cancel
      ok-text="Ok"
      cancel-text="Cancel"
    >
      <p>Once you exit the project, you will not be able to participate in the collaborative process, confirm the exit?</p>
    </Modal>
    <Modal
      v-model="processResourceModal"
      title="share resource in other projects"
      @on-ok="processResource()"
      @on-cancel
      ok-text="Confirm"
      cancel-text="Cancel"
    >
      <!-- todo:这里需要过滤参与的项目以及管理的项目重新渲染 -->
      <div>
        <h3>Participatory Projects</h3>
        <!-- 多选框 -->
        <RadioGroup v-model="selectShareProject">
          <span
            @click="selectPID(item.projectId, item.title)"
            v-for="(item,index) in joinedProjectsNameList"
            v-bind:key="index"
          >
            <Radio :key="item.index" :label="item.title"></Radio>
          </span>
        </RadioGroup>
        <!-- <div v-for="(item,index)in joinedProjectsNameList" :key="item.index">{{item.title}}</div> -->
      </div>
      <div>
        <h3>Management Projects</h3>
        <RadioGroup v-model="selectShareProject">
          <span
            @click="selectPID(item.projectId, item.title)"
            v-for="(item,index) in userManagerProjectList"
            v-bind:key="index"
          >
            <Radio :key="item.index" :label="item.title"></Radio>
          </span>
        </RadioGroup>
      </div>
      <div style="margin-top:5px">
        <Icon type="ios-information-circle-outline" color="lightblue"/>
        <span>the resource will be shared in the project you choosed.</span>
      </div>
    </Modal>
    <!-- 删除资源的模态框 -->
    <!-- deleteResourceModal -->
    <Modal
      v-model="deleteResourceModal"
      @on-ok="deleteResource"
      @on-cancel
      ok-text="Assure"
      cancel-text="Cancel"
    >
      <h3>Do you really want to delete this resource?</h3>
    </Modal>
    <!-- 退出子项目的modal -->
    <Modal
      v-model="deleteProjectModal"
      @on-ok="deleteProject"
      @on-cancel
      ok-text="Assure"
      cancel-text="Cancel"
    >
      <h3>Do you really want to delete this project?</h3>
    </Modal>
    <Modal v-model="editFileModel" title="Edit file info" width="600">
      <Form
        ref="editFileValidate"
        :model="editFileValidate"
        :rules="editFileRuleValidate"
        :label-width="100"
        label-position="left"
      >
        <FormItem label="Privacy" prop="privacy">
          <RadioGroup v-model="editFileValidate.privacy" style="width:80%">
            <Radio label="private">Private</Radio>
            <Radio label="public">Public</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem label="Type" prop="type">
          <RadioGroup v-model="editFileValidate.type">
            <Radio label="data"></Radio>
            <Radio label="paper"></Radio>
            <Radio label="document"></Radio>
            <Radio label="model"></Radio>
            <Radio label="image"></Radio>
            <Radio label="video"></Radio>
            <Radio label="others"></Radio>
          </RadioGroup>
        </FormItem>
        <FormItem label="Name" prop="name">
          <Input type="text" :rows="4" v-model="editFileValidate.name" />
        </FormItem>
        <FormItem label="Description" prop="description">
          <Input type="textarea" :rows="4" v-model="editFileValidate.description" />
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="editFileModel=false">Cancel</Button>
        <Button type="success" @click="changeFileInfo('editFileValidate')">Submit</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
import Avatar from "vue-avatar";
export default {
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      if (!vm.$store.getters.userState) {
        next("/login");
      } else {
        next();
      }
    });
  },
  mounted() {
    // 获取用户信息
    this.getUserProfile();
    //获取用户资源
    this.getUserResource();
    // 获取用户管理的项目信息
    this.getManagerProjectList();
    // 获取用户参与的项目信息（根据参与的项目id、array获取项目详情）
    this.getParticipatoryList(this.joinedProjectsNameList);
    // 获取用户的历史事件记录
    this.readPersonalEvent();
    // 初始化样式的高度
    this.initStyle();
  },
  components: {
    Avatar
  },
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("Please enter your password again"));
      } else if (value !== this.formValidate.password) {
        callback(new Error("The two passwords are inconsistent!"));
      } else {
        callback();
      }
    };
    return {
      userDetail: {},
      resourceColumn: [
        {
          type: "selection",
          width: 60,
          align: "center"
        },
        {
          title: "Name",
          key: "name",
          tooltip: true,
          sortable: true
        },
        {
          title: "Type",
          key: "type",
          sortable: true,
          width: 90
        },
        {
          title: "Description",
          key: "description",
          tooltip: true,
          sortable: true
        },
        {
          title: "Time",
          key: "uploadTime",
          width: 160,
          sortable: true
        },
        {
          title: "Action",
          slot: "action",
          width: 150,
          align: "center"
        }
      ],
      userManagerProjectList: [],
      editProfileModal: false,
      logOutProfileModal: false,
      editProjectModal: false,
      // 项目授权的模态框
      authorizeProjectModal: false,
      //编辑项目专用的字段结束
      item: "",
      projectMemberList: [],
      selectManagerId: this.$store.getters.userId,
      //当前选中项目
      currentProject: {},
      //当前用户信息的表单
      personalInfoItem: {
        userName: "",
        email: "",
        jobTitle: "",
        mobilePhone: "",
        gender: "",
        country: "",
        city: "",
        organization: "",
        introduction: "",
        direction: "",
        homePage: "",
        avatar: ""
      },
      ruleValidate: {
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
        gender: [
          {
            required: true,
            message: "Please select gender",
            trigger: "change"
          }
        ],
        mobilePhone: [
          {
            required: false,
            message: "Please enter your phone number",
            trigger: "blur"
          }
        ],
        country: [
          {
            required: false,
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
        organization: [
          {
            required: false,
            message: "Please enter your affiliation",
            trigger: "blur"
          }
        ],
        introduction: [
          {
            required: false,
            message: "Please enter a personal introduction",
            trigger: "blur"
          },
          {
            type: "string",
            min: 20,
            message: "Introduction no less than 20 characters",
            trigger: "blur"
          }
        ],
        field: [
          {
            required: false,
            message: "Please enter your research field",
            trigger: "blur"
          }
        ],
        homePage: [
          {
            required: false,
            message: "Please enter your home page url",
            trigger: "blur"
          }
        ]
      },
      //用来验证个人信息维护表单填写规范的依据
      //输入框的样式
      inputstyle: true,
      //控制填写表单时用户头像显示的样式
      visible: false,
      //抽屉开启状态控制
      // drawerClose:false,
      //加入的项目的名字id数组
      joinedProjectsNameList: [],
      //加入的项目详情数组列表
      joinedProjectsList: [],
      // 关于样式的变量定义
      detailSidebarHeight: "",
      // 用户event列表
      userEventList: [],
      userResourceList: [],
      // rightContentWidth:"",
      // 退出项目的modal
      quitModal: false,
      // 处理资源的模态框激活
      processResourceModal: false,
      // 选中资源的索引
      selectResourceIndex: 0,
      // 选中的将要分享资源的项目名
      selectShareProject: "",
      selectShareProjectId: "",
      selectShareProjectName: "",
      // 删除资源的的模态框
      deleteResourceModal: false,
      // 要删除的资源的id
      deleteResourceId: "",
      // 要删除管理的项目的模态框
      deleteProjectModal: false,
      // 要删除的项目的Id
      deleteProjectId: "",
      ops: {
        bar: {
          background: "#808695"
        }
      },
      editFileModel:false,
      editFileValidate: {
        name:"",
        description:"",
        type:"",
        privacy:""
      },
      editFileRuleValidate: {
        privacy: [
          {
            required: true,
            message: "file privacy cannot be empty",
            trigger: "blur"
          }
        ],
        type: [
          {
            required: true,
            message: "file type cannot be empty",
            trigger: "blur"
          }
        ],
        name: [
          {
            required: true,
            message: "file description cannot be empty",
            trigger: "blur"
          }
        ],
        description: [
          {
            required: true,
            message: "file description cannot be empty",
            trigger: "blur"
          }
        ]
      },
    };
  },
  methods: {
    // 初始化侧边栏样式
    initStyle() {
      this.detailSidebarHeight = window.innerHeight - 60 + "px";
    },
    //获取用户的详细信息
    getUserProfile() {
      this.userDetail = Object.assign({}, this.$store.getters.userInfo);
      delete this.userDetail.password;
      delete this.userDetail.joinedProjects;
      delete this.userDetail.manageProjects;
      this.joinedProjectsNameList = this.$store.getters.userInfo.joinedProjects;
    },
    //获取用户参与的项目列表
    getParticipatoryList(projectIds) {
      if (projectIds != null) {
        var count = projectIds.length;
        let participatoryProjectListTemp = [];
        for (let i = 0; i < projectIds.length; i++) {
          this.axios
            .get(
              "/GeoProblemSolving/project/inquiry" +
                "?key=projectId" +
                "&value=" +
                projectIds[i].projectId
            )
            .then(res => {
              if (res.data != "None") {
                participatoryProjectListTemp.push(res.data[0]);
              }
              if (--count == 0) {
                this.$set(
                  this,
                  "joinedProjectsList",
                  participatoryProjectListTemp
                );
              }
            })
            .catch(err => {
              console.log(err.data);
            });
        }
      }
    },
    //获取用户可管理支配的全部项目列表
    getManagerProjectList() {
      this.axios
        .get(
          "/GeoProblemSolving/project/inquiry" +
            "?key=managerId" +
            "&value=" +
            this.userDetail.userId
        )
        .then(res => {
          if (res.data != "None" && res.data != "Fail") {
            this.userManagerProjectList = res.data;
          } else {
            this.userManagerProjectList = [];
          }
        })
        .catch(err => {});
    },
    //注销的模态框按钮
    logOutModalShow() {
      this.logOutProfileModal = true;
    },
    //注销账户的函数
    logOutAccount() {
      this.axios
        .get(
          "/GeoProblemSolving/user/remove?" +
            "userId=" +
            this.$store.getters.userId
        )
        .then(res => {
            window.location.href="/GeoProblemSolving/home";
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    quitModalShow(project) {
      this.quitModal = true;
      this.currentProject = project;
    },
    //退出项目的函数
    quitProject() {
      let quitProjectId = this.currentProject.projectId;
      this.axios
        .get(
          "/GeoProblemSolving/project/quit" +
            "?projectId=" +
            quitProjectId +
            "&userId=" +
            this.$store.getters.userId
        )
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data != "Fail" && res.data != "None") {
            this.$Message.success("Quit Success");
            this.removeQuitProject(quitProjectId);
            let notice = {};
            let recipientId = this.currentProject.managerId;
            notice["recipientId"] = recipientId;
            notice["type"] = "notice";
            notice["content"] = {
              title: "Manager change",
              description:
                "The member called " +
                this.$store.getters.userName +
                " had quited from your project " +
                this.currentProject.title +
                "!"
            };
            this.axios
              .post("/GeoProblemSolving/notice/save", notice)
              .then(res => {
                if (res.data == "Success") {
                  this.$emit("sendNotice", recipientId);
                }
              })
              .catch(err => {
                console.log("申请失败的原因是：" + err.data);
              });
          } else {
            this.$Message.error("Quit Fail.");
            console.log("Quit fail: " + res.data);
          }
        })
        .catch(err => {});
    },
    removeQuitProject(projectId) {
      let oldjoinedProjectsList = this.joinedProjectsList;
      let newjoinedProjectsList = [];
      for (var i = 0; i < oldjoinedProjectsList.length; i++) {
        if (oldjoinedProjectsList[i].projectId != projectId) {
          newjoinedProjectsList.push(oldjoinedProjectsList[i]);
        }
      }
      this.$set(this, "joinedProjectsList", newjoinedProjectsList);
    },
    authorizeModalShow(index) {
      this.authorizeProjectModal = true;
      this.projectMemberList = this.userManagerProjectList[index].members;
      this.currentProject = this.userManagerProjectList[index];
    },
    authorize() {
      this.axios
        .get(
          "/GeoProblemSolving/project/manager?" +
            "projectId=" +
            this.currentProject.projectId +
            "&newManagerId=" +
            this.selectManagerId
        )
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data != "Fail") {
            let projectInfo = res.data;
            projectInfo.projectId = this.currentProject.projectId;
            this.projectManageToJoin(projectInfo);
            let notice = {};
            let recipientId = this.selectManagerId;
            notice["recipientId"] = recipientId;
            notice["type"] = "notice";
            notice["content"] = {
              title: "Manager change",
              description:
                "You have been the manager of project " +
                this.currentProject.title +
                " !"
            };
            this.axios
              .post("/GeoProblemSolving/notice/save", notice)
              .then(res => {
                if (res.data == "Success") {
                  this.$emit("sendNotice", recipientId);
                }
              })
              .catch(err => {
                console.log("申请失败的原因是：" + err.data);
              });
          }
        })
        .catch(err => {});
    },
    projectManageToJoin(project) {
      this.joinedProjectsList.push(project);
      var oldManageProjects = this.userManagerProjectList;
      var newManageProjects = [];
      for (let i = 0; i < oldManageProjects.length; i++) {
        if (oldManageProjects[i].projectId != project.projectId) {
          newManageProjects.push(oldManageProjects[i]);
        }
      }
      this.$set(this, "userManagerProjectList", newManageProjects);
    },
    deleteProjectModalShow(pid) {
      this.deleteProjectId = pid;
      this.deleteProjectModal = true;
    },
    deleteProject() {
      if (this.deleteProjectId != "") {
        this.axios
          .get(
            "/GeoProblemSolving/project/delete?" +
              "projectId=" +
              this.deleteProjectId
          )
          .then(res => {
            if (res.data == "Offline") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else if (res.data == "Success") {
              var newManageProjects = [];
              var oldManageProjects = this.userManagerProjectList;
              for (var i = 0; i < oldManageProjects.length; i++) {
                if (oldManageProjects[i].projectId != this.deleteProjectId) {
                  newManageProjects.push(oldManageProjects[i]);
                }
              }
              this.$set(this, "userManagerProjectList", newManageProjects);
            } else {
              this.$Notice.error({
                title: "Error",
                desc: "Delete project fail."
              });
            }
          })
          .catch(err => {
            console.log(err.data);
          });
      }
    },
    editModalShow() {
      this.personalInfoItem = Object.assign({}, this.userDetail);
      this.editProfileModal = true;
    },
    //处理修改用户头像信息相关的函数
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
      } else {
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = e => {
          // 读取到的图片base64 数据编码 将此编码字符串传给后台即可
          imgcode = e.target.result;
          this.personalInfoItem.avatar = imgcode;
          this.userDetail.avatar = "";
          this.$store.commit("uploadAvatar", imgcode);
        };
      }
    },
    handleView() {
      this.visible = true;
    },
    handleRemove() {
      this.personalInfoItem.avatar = "";
      this.userDetail.avatar = "";
      this.$store.commit("uploadAvatar", "");
    },
    submitProfileEdit(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          var data = this.personalInfoItem;
          var changedProfile = new URLSearchParams();
          changedProfile.append("userId", this.$store.getters.userId);
          //筛选出需要修改的信息
          for (var item in data) {
            if (data[item] != "") {
              changedProfile.append(item, data[item]);
            }
          }
          this.axios
            .post("/GeoProblemSolving/user/update", changedProfile)
            .then(res => {
              if (res.data !== "Fail") {
                // this.drawerClose = true;
                this.$Notice.success({
                  title: "notification",
                  desc: "Profile update successfully"
                });
                let userInfo = res.data;
                userInfo.userState = true;
                this.$store.commit("setUserInfo", userInfo);
                this.$set(this, "userDetail", userInfo);
                sessionStorage.setItem("userInfo", JSON.stringify(userInfo));
              }
            })
            .catch(err => {});
        }
      });
    },
    resetForm() {
      this.personalInfoItem = Object.assign({}, this.userDetail);
    },
    //点击跳转到指定项目的函数
    goSingleProject(id) {
      this.$router.push({ name: "ProjectDetail", params: { id: id } });
    },
    readPersonalEvent() {
      this.axios
        .get(
          "/GeoProblemSolving/history/inquiry?" +
            "eventType=project" +
            "&key=userId" +
            "&value=" +
            this.$store.getters.userId
        )
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data != "None" && res.data != "Fail") {
            this.userEventList = res.data;
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    getUserResource() {
      this.axios
        .get(
          "/GeoProblemSolving/resource/inquiry" +
            "?key=uploaderId" +
            "&value=" +
            this.$store.getters.userId
        )
        .then(res => {
          if (res.data != "None" && res.data != "Fail") {
            this.userResourceList = res.data;
          } else if (res.data == "None") {
            this.userResourceList = [];
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    download(index) {
      window.open(this.userResourceList[index].pathURL);
    },
    deleteResourceModalShow(id) {
      this.deleteResourceModal = true;
      this.deleteResourceId = id;
    },
    deleteResource() {
      if (this.deleteResourceId != "") {
        this.axios
          .get(
            "/GeoProblemSolving/resource/delete?" +
              "resourceId=" +
              this.deleteResourceId
          )
          .then(res => {
            if (res.data == "Success") {
              this.$Notice.success({
                title: "Process result",
                desc: "Delete successfully"
              });
              this.getUserResource();
            } else if (res.data == "Fail") {
              this.$Notice.error({
                title: "Process result",
                desc: "Delete fail"
              });
              // this.$Message.info("Failure");
            }
          })
          .catch(err => {
            console.log(err.data);
          });
      }
    },
    // 处理个人的资源可以选择copy到参与的项目，也可以选择copy到管理的项目
    processResource() {
      let resourceInfo = {};
      this.joinedProjectsNameList = this.$store.getters.userInfo[
        "joinedProjects"
      ];
      resourceInfo = this.userResourceList[this.selectResourceIndex];
      let shareForm = new FormData();
      shareForm.append("name", resourceInfo.name);
      shareForm.append("description", resourceInfo.description);
      shareForm.append("belong", this.selectShareProject);
      shareForm.append("type", resourceInfo.type);
      shareForm.append("fileSize", resourceInfo.fileSize);
      shareForm.append("pathURL", resourceInfo.pathURL);
      shareForm.append("uploaderId", resourceInfo.uploaderId);
      // 还有一个获取到选中的项目的id
      let scopeObject = {
        projectId: this.selectShareProjectId,
        subProjectId: "",
        moduleId: ""
      };
      shareForm.append("scope", JSON.stringify(scopeObject));
      if (scopeObject.projectId != "") {
        this.axios
          .post("/GeoProblemSolving/resource/share", shareForm)
          .then(res => {
            if (res.data != "Fail") {
              this.$Notice.open({
                title: "Upload notification title",
                desc:
                  "File shared to " + this.selectShareProject + " successfully."
              });
              // 保存记录
              this.addUploadEvent(this.selectShareProjectId);
            }
          })
          .catch(err => {});
      }
      // uploaderId
    },
    addUploadEvent(scopeId) {
      let form = {};
      let description =
        this.$store.getters.userName +
        " share a " +
        this.fileType +
        " file to " +
        " project called " +
        this.selectShareProjectName;
      form["description"] = description;
      form["scopeId"] = scopeId;
      form["eventType"] = "project";
      form["userId"] = this.$store.getters.userId;
      this.axios
        .post("/GeoProblemSolving/history/save", form)
        .then(res => {
          console.log(res.data);
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    processResourceModalShow(index) {
      this.processResourceModal = true;
      this.selectResourceIndex = index;
    },
    selectPID(id, name) {
      this.selectShareProjectId = id;
      this.selectShareProjectName = name;
    },
    fileEditModalShow(index){
      this.selectResourceIndex = index;
      var oldFileInfo = this.userResourceList[index]; 
      this.editFileValidate={
        name:oldFileInfo.name,
        description:oldFileInfo.description,
        type:oldFileInfo.type,
        privacy:oldFileInfo.privacy
      };
      this.editFileModel = true;
    },
    changeFileInfo(name){
      this.$refs[name].validate(valid => {
        if (valid) {
          var editFormData=new FormData();
          editFormData.append("resourceId",this.userResourceList[this.selectResourceIndex].resourceId);
          editFormData.append("name",this.editFileValidate.name);
          editFormData.append("type",this.editFileValidate.type);
          editFormData.append("description",this.editFileValidate.description);
          editFormData.append("privacy",this.editFileValidate.privacy);
          this.axios({
            url:"/GeoProblemSolving/resource/update",
            method: "post",
            data:editFormData
          })
            .then(res=>{
              this.editFileModel = false;
              if(res.data!="Fail"&&res.data!="None"){
                var newResourceInfo = res.data;
                this.userResourceList.splice(this.selectResourceIndex,1,newResourceInfo);
              }else{
                this.$Message.error("Edit fail: "+res.data+".");
              }
            })
            .catch(err=>{
              this.$Message.error("Edit error.");
            });
        }
      });
    }
  }
};
</script>