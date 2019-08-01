<template>
<div>
    <Row>
      <Col span="22" offset="1">
        <Row>
          <Col :lg="5" :md="8" :sm="10" :xs="12">
            <div class="detailSidebar" :style="{height:detailSidebarHeight}">
              <div class="user-img">
                <img v-bind:src="userDetail.avatar" class="u_img"
                  v-if="userDetail.avatar!=''&&userDetail.avatar!='undefined'&&userDetail.avatar!='null'">
                <avatar
                  style="width:200px"
                  class="avatarStyle"
                  :username="userDetail.userName"
                  :size="200"
                  :rounded="false"
                  v-else>
                </avatar>
              </div>
              <div class="single-info" :title="`Name: `+ userDetail.userName">
                <Icon type="ios-contact-outline" :size="20"/>
                <span>{{userDetail.userName}}</span>
              </div>
                <div class="single-info" :title="`Email:  ` + userDetail.email">
                  <!-- <span>email:</span> -->
                  <Icon type="ios-mail-outline" :size="20"/>
                  <span>{{userDetail.email}}</span>
                </div>
                <div class="single-info" v-show="userDetail.mobilePhone!=''" :title="`Phone:  `+ userDetail.mobilePhone ">
                  <Icon type="ios-call-outline" :size="20"/>
                  <span>{{userDetail.mobilePhone}}</span>
                </div>
                <div class="single-info" :title="`Job Title:  `+userDetail.jobTitle">
                  <Icon type="ios-hammer-outline" :size="20"/>
                  <span>{{userDetail.jobTitle}}</span>
                </div>
                <div class="single-info" v-show="userDetail.city!=''&&userDetail.country!=''" :title="`Position:  `+ userDetail.city + ' ' + userDetail.country">
                  <Icon type="ios-compass-outline" :size="20"/>
                  <span>{{userDetail.country}}&nbsp{{userDetail.city}}</span>
                </div>
                <div class="single-info" v-show="userDetail.organization!=''" :title="`Organization:  `+ userDetail.organization" style="overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
                  <Icon type="ios-home-outline" :size="20"/>
                  <span> {{userDetail.organization}}</span>
                </div>
                <div class="single-info" v-show="userDetail.direction!=''" :title="`Direction:  `+ userDetail.direction" style="overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
                  <Icon type="ios-contract" :size="20"/>
                  <span> {{userDetail.direction}}</span>
                </div>
                <div class="single-info" v-show="userDetail.homePage!=''" :title="`Home Page:  `+ userDetail.homePage" style="overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
                  <Icon type="md-link" :size="20"/>
                  <span> {{userDetail.homePage}}</span>
                <br>
                <div style="padding:20px 20px 0 20px;font-size:12px;text-indent:2em;border:1px dotted lightgray" v-show="userDetail.introduction!=''">
                  {{this.userDetail.introduction}}
                </div>
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
              <Tabs value="Overview" type="card" style="font-weight:bold">
                <TabPane label="Overview" name="Overview">
                  <Col :lg="{span:22,offset:1}" :md="{span:22,offset:1}" :sm="{span:22,offset:1}">
                    <Card>
                      <p slot="title">History Line</p>

                      <Timeline
                        style="margin-top:20px;margin-left:5%;max-height:300px;overflow-y:auto"
                      >
                      <div v-if="memberEventList.length==0">
                            <div style="display:flex;justify-content:center">
                              <Icon type="md-alert" size="40" color="gray"/>
                            </div>
                            <br>
                            <div style="display:flex;justify-content:center">
                              <h3
                                style="text-align:center;width:80%"
                              >Sorry, there is no event here. After some projects are created or joined, the events of creation or joining will be listed here.</h3>
                            </div>
                          </div>
                        <TimelineItem v-for="(item,index) in memberEventList" :key="index" v-show="memberEventList.length>0">
                          <strong>
                            <p class="time">{{item.createTime}}</p>
                          </strong>
                          <p class="content">{{item.description}}</p>
                        </TimelineItem>
                      </Timeline>
                    </Card>
                  </Col>
                </TabPane>
                <TabPane label="Joined Project" name="Participatory">
                  <Card :bordered="false" v-if="joinedProjectsList.length == 0">
                      <div style="display:flex;justify-content:center">
                        <Icon type="md-alert" size="40" color="gray"/>
                      </div>
                      <br>
                      <div style="display:flex;justify-content:center" >
                        <h2 style="text-align:center;width:50%">Sorry,this user doesn't participate in any projects.</h2>
                      </div>
                  </Card>
                  <div
                    v-for="(item,index) in joinedProjectsList"
                    :key="index"
                    v-show="joinedProjectsList!=[]"
                  >
                    <Col :lg="{span:11, offset:1}" :md="{span:22, offset:1}">
                      <div>
                        <Card style="height:320px;margin-top:20px;">
                        <p
                          slot="title"
                          style="height:40x"
                          class="projectsTitle"
                        >{{item.title}}</p>
                        <p
                          style="height:200px;text-indent:2em;overflow-y:auto;word-break:break-word"
                        >{{item.introduction}}</p>
                        <br>
                        <div style="height:40px">
                          <span style="float:left">CreateTime:</span>
                          <span style="float:right">{{item.createTime}}</span>
                        </div>
                      </Card>
                      </div>
                    </Col>
                  </div>
                </TabPane>
                <TabPane label="Managed Project" name="Management">
                  <Card :bordered="false" v-if="userManagerProjectList.length == 0">
                      <div style="display:flex;justify-content:center">
                        <Icon type="md-alert" size="40" color="gray"/>
                      </div>
                      <br>
                      <div style="display:flex;justify-content:center" >
                        <h2 style="text-align:center;width:50%">Sorry,this user doesn't manage any projects.</h2>
                      </div>
                  </Card>
                  <div
                    v-for="(mProject,index) in userManagerProjectList"
                    v-show="userManagerProjectList!='None'"
                    :key="index"
                  >
                    <Col :lg="{span:11, offset:1}" :md="{span:22, offset:1}">
                    <div>
                      <Card style="height:320px;margin-top:20px">
                        <p
                          slot="title"
                          class="projectsTitle"
                        >{{mProject.title}}</p>
                        <p
                          style="height:200px;text-indent:2em;overflow-y:auto;word-break:break-word"
                        >{{mProject.introduction}}</p>
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
                </TabPane>
              </Tabs>
            </div>
          </Col>
        </Row>
        <div></div>
      </Col>
    </Row>
  </div>

</template>
<style scoped>
.detailSidebar {
  margin-right: 20px;
}
.rightContent {
  margin-top:20px;
  flex: 1;
}
img {
  padding: 10px;
  max-width: 100%;
  max-height: 100%;
}
h1 {
  font-weight: normal;
}
body {
  overflow-x: hidden;
}
.sidebar {
  margin-top: 20px;
}
.username {
  text-align: center;
  height: 30px;
  margin-bottom: 20px;
  line-height: 30px;
  font-size: 15px;
}
.user-img {
  margin-top: 20px;
  width: 100%;
  max-height: 100%;
  text-align: center;
}
.u_img {
  max-width: 100%;
  padding: 10px;
}
.avatarStyle {
  margin: 0 auto;
}
.single-info {
  padding: 5px;
  height: 30px;
  font-size: 12px;
  line-height: 15px;
}
.userDescription {
  height: auto;
  line-height: 10px;
  font-size: 10px;
  /* max-width: 200px; */
  /* display: inline-block; */
  overflow: hidden;
  word-wrap: break-word;
  word-break: break-all;
}
.user-project {
  margin-top: 20px;
}
.project-card {
  margin-top: 5%;

  height: 200px;
  background-color: lightgray;
}
.user-contribution {
  margin-top: 20px;
}
.user-history {
  margin-top: 20px;
  border: 1px solid black;
}
.user-data {
  margin-top: 20px;
  border: 1px solid black;
  max-height: 300px;
  overflow-y: scroll;
}

/* 表示空格间距的 */
.whitespace {
  height: 20px;
}
.editStyle {
  display: flex;
  align-items: center;
  margin-top: 5px;
  /* justify-content: center; */
}
.editStyle span {
  width: 20%;
  text-align: left;
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
.editBtn:hover {
  background-color: #19be6b;
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
.participatoryProjectCard:hover,{
  cursor:pointer;
}
</style>
<script>
import Avatar from "vue-avatar";
export default {
  components: {
    Avatar
  },
  data() {
    return {
      userDetail: {
        userName: "",
        email: "",
        password: "",
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
      detailSidebarHeight: "",
      // 用户event列表
      memberEventList: [],
      userResourceList: [],
      joinedProjectsNameArray: [],
      //加入的项目详情数组列表
      joinedProjectsList: [],
      userManagerProjectList: [],
    };
  },
  methods: {
    getMemberDetail() {
      this.axios
        .get(url, {
          params: {
            id: paramId
          }
        })
        .then(function(response) {})
        .catch(function(error) {});
    },
    getUserProfile() {
      this.axios
        .get(
          "/GeoProblemSolving/user/inquiry" +
            "?key=userId" +
            "&value=" +
            this.$route.params.id
        )
        .then(res => {
          if(res.data != "None" && res.data != "Fail") {
            this.userDetail = res.data;
            this.joinedProjectsNameArray = this.userDetail.joinedProjects;
            if(this.joinedProjectsNameArray.length > 0) {
              this.getParticipatoryList(this.joinedProjectsNameArray);
            }
          }

        })
        .catch(err => {
          console.log(err.data);
        });
    },
    readPersonalEvent() {
      this.axios
        .get(
          "/GeoProblemSolving/history/inquiry?" +
            "eventType=project" +
            "&key=userId" +
            "&value=" +
            this.$route.params.id
        )
        .then(res => {
          if(res.data == "Offline"){
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          }
          else if(res.data !="None"&&res.data !="Fail"){
            this.memberEventList = res.data;
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    getManagerProjectList() {
      this.axios
        .get(
          "/GeoProblemSolving/project/inquiry" +
            "?key=managerId" +
            "&value=" +
            this.$route.params.id
        )
        .then(res => {
          if(res.data!="None" && res.data!=""){
            //判断是否为空
            this.userManagerProjectList = res.data;
          }
          // 打印用户所管理的项目

        })
        .catch(err => {});
    },
    getParticipatoryList(projectIds) {
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
            participatoryProjectListTemp.push(res.data[0]);
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
    },
  },
  mounted() {
    this.getUserProfile();
    this.getManagerProjectList();
    this.readPersonalEvent();
    this.detailSidebarHeight = window.innerHeight - 60 + "px";
  },
};
</script>

