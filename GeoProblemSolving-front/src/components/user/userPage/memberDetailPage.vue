<template>
  <div>
    <Row>
      <Col span="22" offset="1">
        <Row>
          <Col :lg="5" :md="8" :sm="10" :xs="12">
            <div class="detailSidebar" :style="{ height: detailSidebarHeight }">
              <div class="user-img">
                <img
                    :src= "avatarUrl(userDetail.avatar)"
                    class="u_img"
                    v-if="
                      userDetail.avatar != '' &&
                      userDetail.avatar != 'undefined' &&
                      userDetail.avatar != 'null'
                    "
                  />
                  <avatar
                    style="width: 200px"
                    class="avatarStyle"
                    :username="userDetail.name"
                    :size="200"
                    :rounded="true"
                    v-else
                  >
                  </avatar>
              </div>
              <div
                style="
                  margin-top: 5%;
                  border: 1px solid lightgray;
                  border-radius: 4px;
                  padding: 10px;
                "
              >
                <div class="single-info" :title="`Name: ` + userDetail.name">
                  <span class="profileInfo">
                    <Icon type="ios-contact-outline" :size="25" />
                    {{ userDetail.name }}
                  </span>
                </div>
                <div class="single-info" :title="`Email:  ` + userDetail.email">
                  <span class="profileInfo">
                    <Icon type="ios-mail-outline" :size="25" />
                    {{ userDetail.email }}
                  </span>
                </div>
                <div
                  class="single-info"
                  :title="`Phone number:  ` + userDetail.phone"
                  v-show="userDetail.phone != ''"
                >
                  <span class="profileInfo">
                    <Icon type="ios-call-outline" :size="25" />
                    {{ userDetail.phone }}
                  </span>
                </div>
                <div class="single-info" :title="`Title:  ` + userDetail.title">
                  <span class="profileInfo">
                    <Icon type="ios-school-outline" :size="25" />
                    {{ userDetail.title }}
                  </span>
                </div>
                <div
                  class="single-info"
                  :title="`Location:  ` + userDetail.country"
                  v-show="userDetail.country != ''"
                >
                  <span class="profileInfo">
                    <Icon type="ios-compass-outline" :size="25" />
                    <span>{{ userDetail.country }}</span>
                  </span>
                </div>
                <!-- <div
                  class="single-info"
                  :title="`Organization:  ` + userDetail.organizations"
                  v-show="userDetail.organizations != ''"
                  style="
                    overflow: hidden;
                    white-space: nowrap;
                    text-overflow: ellipsis;
                  "
                >
                  <span class="profileInfo">
                    <Icon type="ios-home-outline" :size="20" />
                    {{ userDetail.organizations }}
                  </span>
                </div> -->
                <!-- <div
                  class="single-info"
                  :title="`Area of interest:  ` + userDetail.direction"
                  v-show="userDetail.direction != ''"
                  style="
                    overflow: hidden;
                    white-space: nowrap;
                    text-overflow: ellipsis;
                  "
                >
                  <span class="profileInfo">
                    <Icon type="ios-contract" :size="20" />
                    {{ userDetail.direction }}
                  </span>
                </div> -->
                <div
                  class="single-info"
                  :title="`Home Page:  ` + userDetail.homepage"
                  v-show="userDetail.homepage != '' && userDetail.homepage != 'null'"
                  style="
                    overflow: hidden;
                    white-space: nowrap;
                    text-overflow: ellipsis;
                  "
                >
                  <span class="profileInfo">
                    <Icon type="md-link" :size="25" />
                    <a :href="'http://'+ userDetail.homepage">{{ userDetail.homepage }}</a>
                  </span>
                </div>
                <div
                  style="
                    word-break: break-word;
                    padding: 10px;
                    font-size: 12px;
                    border: 1px dotted lightgray;
                    white-space: pre-line;
                    height: 120px;
                  "
                  title="Introduction"
                  v-show="userDetail.introduction != ''"
                >
                  <vue-scroll :ops="ops">{{
                    userDetail.introduction
                  }}</vue-scroll>
                </div>
              </div>
            </div>
          </Col>
          <Col
            :lg="{ span: 18, offset: 1 }"
            :md="{ span: 15, offset: 1 }"
            :sm="{ span: 13, offset: 1 }"
            :xs="{ span: 11, offset: 1 }"
          >
            <div class="rightContent">
              <Tabs value="Overview" type="card" style="font-weight: bold">
                <TabPane label="Overview" name="Overview">
                  <Col
                    :lg="{ span: 22, offset: 1 }"
                    :md="{ span: 22, offset: 1 }"
                    :sm="{ span: 22, offset: 1 }"
                  >
                    <Card dis-hover style="margin-top: 10px;" class="customCard">
                      <p slot="title">History line</p>
                      <div class="timeLineStyle">
                        <vue-scroll :ops="ops">
                          <Timeline
                            style="
                              margin-top: 20px;
                              margin-left: 5%;
                              max-height: 300px;
                              overflow-y: auto;
                            "
                          >
                            <div
                              v-if="
                                memberEventList != undefined &&
                                memberEventList.length == 0
                              "
                            >
                              <div
                                style="display: flex; justify-content: center"
                              >
                                <Icon type="md-alert" size="40" color="gray" />
                              </div>
                              <br />
                              <div
                                style="display: flex; justify-content: center"
                              >
                                <h3 style="text-align: center; width: 80%">
                                  Sorry, there is no event here. After some
                                  projects are created or joined, the events of
                                  creation or joining will be listed here.
                                </h3>
                              </div>
                            </div>
                            <TimelineItem
                              v-for="(item, index) in memberEventList"
                              :key="index"
                              v-show="
                                memberEventList != undefined &&
                                memberEventList.length > 0
                              "
                            >
                              <strong>
                                <p class="time">{{ item.createTime }}</p>
                              </strong>
                              <p class="content">{{ item.description }}</p>
                            </TimelineItem>
                          </Timeline>
                        </vue-scroll>
                      </div>
                    </Card>
                  </Col>
                </TabPane>
                <TabPane label="Joined projects" name="Participatory">
                  <div
                    style="
                      height: 900px;
                      padding: 5px;
                      border: #dcdee2 solid 1px;
                    "
                  >
                    <vue-scroll :ops="ops">
                      <Card
                        :bordered="false"
                        v-if="
                          joinedProjectsList != undefined &&
                          joinedProjectsList.length == 0
                        "
                      >
                        <div style="display: flex; justify-content: center">
                          <Icon type="md-alert" size="40" color="gray" />
                        </div>
                        <br />
                        <div style="display: flex; justify-content: center">
                          <h2 style="text-align: center; width: 50%">
                            Sorry, this user didn't join any project.
                          </h2>
                        </div>
                      </Card>
                      <div v-for="(item,index) in joinedProjectsList" :key="index">
                        <Col
                          :lg="{ span: 11, offset: 1 }"
                          :md="{ span: 22, offset: 1 }"
                        >
                          <div>
                            <Card style="height: 320px; margin-top: 20px" class="projectCard" @click="enterProject(item)">
                              <p
                                slot="title"
                                style="height: 40x"
                                class="projectsTitle"
                              >
                                {{ item.name }}
                              </p>
                              <p
                                style="
                                  height: 200px;
                                  text-indent: 2em;
                                  overflow-y: auto;
                                  word-break: break-word;
                                  white-space: pre-line;
                                "
                              >
                                <vue-scroll :ops="ops">{{
                                  item.description
                                }}</vue-scroll>
                              </p>
                              <br />
                              <div style="height: 40px">
                                <span style="float: left">Created time:</span>
                                <span style="float: right">{{
                                  item.createdTime
                                }}</span>
                              </div>
                            </Card>
                          </div>
                        </Col>
                      </div>
                    </vue-scroll>
                  </div>
                </TabPane>
                <TabPane label="Created projects" name="Creation">
                  <div
                    style="
                      height: 900px;
                      padding: 5px;
                      border: #dcdee2 solid 1px;
                    "
                  >
                    <vue-scroll :ops="ops">
                      <Card
                        :bordered="false"
                        v-if="
                          createdProjectList != undefined &&
                          createdProjectList.length == 0
                        "
                      >
                        <div style="display: flex; justify-content: center">
                          <Icon type="md-alert" size="40" color="gray" />
                        </div>
                        <br />
                        <div style="display: flex; justify-content: center">
                          <h2 style="text-align: center; width: 50%">
                            Sorry, this user didn't create any project.
                          </h2>
                        </div>
                      </Card>
                      <div
                        v-for="(mProject,index) in createdProjectList"
                        :key="index"
                      >
                        <Col
                          :lg="{ span: 11, offset: 1 }"
                          :md="{ span: 22, offset: 1 }"
                        >
                          <div>
                            <Card style="height: 320px; margin-top: 20px" class="projectCard" @click="enterProject(mProject)">
                              <p slot="title" class="projectsTitle">
                                {{ mProject.name }}
                              </p>
                              <p
                                style="
                                  height: 200px;
                                  text-indent: 2em;
                                  overflow-y: auto;
                                  word-break: break-word;
                                  white-space: pre-line;
                                "
                              >
                                <vue-scroll :ops="ops">{{
                                  mProject.description
                                }}</vue-scroll>
                              </p>
                              <br />
                              <div>
                                <span style="float: left">Created Time:</span>
                                <span style="float: right">{{
                                  mProject.createdTime
                                }}</span>
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
          </Col>
        </Row>
        <div></div>
      </Col>
    </Row>
  </div>
</template>
<script>
import Avatar from "vue-avatar";
export default {
  components: {
    Avatar,
  },
  data() {
    return {
      userDetail: {},
      detailSidebarHeight: "",
      // 用户event列表
      memberEventList: [],
      userResourceList: [],
      //加入的项目详情数组列表
      joinedProjectsList: [],
      createdProjectList: [],
      ops: {
        bar: {
          background: "#808695",
        },
      },
    };
  },
  mounted() {
    this.getUserProfile();
    this.readPersonalEvent();
    this.detailSidebarHeight = window.innerHeight - 60 + "px";
  },
  computed:{
  },
  methods: {
    avatarUrl(url) {
      let avatarUrl = this.$store.state.UserServer + url;
      return avatarUrl;
    },
		enterProject(project){
			console.log(project);
		},
    getMemberDetail() {
      this.$axios
        .get(url, {
          params: {
            id: paramId,
          },
        })
        .then(function (response) {})
        .catch(function (error) {});
    },
    getUserProfile() {
      this.$axios
        .get(
          "/GeoProblemSolving/user" +
            "?key=userId" +
            "&value=" +
            this.$route.params.id
        )
        .then((res) => {
          if (res.data.code == 0) {
            this.userDetail = res.data.data;
            let joinedProjectsArray = this.userDetail.joinedProjects;
            if (
              joinedProjectsArray != null &&
              joinedProjectsArray.length > 0
            ) {
              this.getJoinedProjectList(joinedProjectsArray);
            }
            let createdProjectsArray = this.userDetail.createdProjects;
            if (
              createdProjectsArray != null &&
              createdProjectsArray.length > 0
            ) {
              this.getCreatedProjectList(createdProjectsArray);
            }
          }
        })
        .catch((err) => {
          console.log(err.data);
        });
    },
    readPersonalEvent() {
      this.$axios
        .get(
          "/GeoProblemSolving/history/inquiry?" +
            "eventType=project" +
            "&key=userId" +
            "&value=" +
            this.$route.params.id
        )
        .then((res) => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data != "None" && res.data != "Fail") {
            this.memberEventList = res.data;
          }
        })
        .catch((err) => {
          throw err;
        });
    },
    getCreatedProjectList(projectIds) {
      this.$axios
        .get("/GeoProblemSolving/project/getProjects" + "?aids=" + projectIds)
        .then((res) => {
          if (res.data.code == 0) {
            this.$set(this, "createdProjectList", res.data.data);
          } else {
            console.log(res.data.msg);
          }
        })
        .catch((err) => {
          throw err;
        });
    },
    getJoinedProjectList(projectIds) {
      this.$axios
        .get("/GeoProblemSolving/project/getProjects" + "?aids=" + projectIds)
        .then((res) => {
          if (res.data.code == 0) {
            this.$set(this, "joinedProjectsList", res.data.data);
          } else {
            console.log(res.data.msg);
          }
        })
        .catch((err) => {
          throw err;
        });
    },
  },
};
</script>
<style scoped>
.projectCard{
  background-color: white;
  border-radius: 3px;
  border:1px solid #dadce0;
  box-shadow:  0 3.2px 7.2px 0 rgb(0 0 0 / 13%), 0 0.6px 1.8px 0 rgb(0 0 0 / 11%);
}

.detailSidebar {
  margin-right: 20px;
  margin-top: 40px;
}
.rightContent {
  margin-top: 20px;
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
  width:80%;
  height: 30%;
  text-align: center;
  margin-left: 20px;
  /* background-color: #d3d3d333; */
}
.u_img {
  width: 200px;
  height: 200px;
  max-width: 100%;
  border-radius: 100%;
}
.avatarStyle {
  margin: 0 auto;
}
.single-info {
  padding: 5px;
  height: 25px;
  margin-bottom: 10px;
  font-size: 15px;
  font-weight: 700;
  color: slategrey;
  line-height: 15px;
}

.profileInfo {
  overflow: hidden;
  word-break: break-word;
  white-space: nowrap;
  display: block;
  text-overflow: ellipsis;
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
.participatoryProjectCard:hover {
  cursor: pointer;
}
</style>
