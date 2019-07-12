<style scoped>
.top,
.bottom {
  text-align: center;
}
img {
  width: 100%;
  height: auto;
}
.projectTitle {
  height: 30px;
  line-height: 30px;
  font-size: 20px;
  max-width: 200px;
  padding-left: 5px;
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.btnCreate:hover,
.joinProjectBtn:hover {
  background-color: #19be6b;
  color: white;
}
</style>
<template>
  <div>
    <div class="TabContent">
      <div v-if="projectList.length > 0">
        <div
          class="projectCard"
          v-for="(item,index) in projectList"
          :key="item.index"
          style="width:95%;margin-right:5%"
        >
          <Col :xs="{ span: 21, offset: 1 }" :md="{ span: 11, offset: 1 }" :lg="{ span: 5 }">
            <div @click="projectInfoModalShow(item)" style="cursor:pointer">
              <Card style="height:auto;margin:20px -15px">
                <span slot="title" class="projectTitle" :title="item.title">{{item.title}}</span>
                <div class="operate" slot="extra" style="display:flex;align-items:center">
                  <Icon
                    type="md-eye"
                    :size="20"
                    title="Public project"
                    v-show="!(item.isManager||item.isMember) && (item.privacy=='Public')"
                  />
                  <Icon
                    type="md-lock"
                    :size="20"
                    title="Discoverable project"
                    v-show="!(item.isManager||item.isMember) && (item.privacy=='Discoverable')"
                  />
                  <!-- <Button
                    class="joinProjectBtn"
                    type="default"
                    title="Join in project"
                    v-show="!(item.isManager||item.isMember) && UserState  && (item.privacy=='Discoverable')"
                    @click.stop="joinApplyModalShow(item)"
                  >
                    <Icon type="md-add" :size="20" />
                  </Button>-->
                  <br />
                  <Icon
                    type="md-person"
                    :size="20"
                    title="Managed or joined project"
                    v-show="item.isMember||item.isManager"
                    :id="item.projectId"
                  />
                </div>
                <div style="display:flex;align-items:center;height:20px">
                  <strong style="text-align: center">Description</strong>
                  <p
                    style="padding: 0 10px;word-break:break-word;overflow: hidden;
                            white-space: nowrap;
                            text-overflow: ellipsis;
                            max-width: 400px;"
                    :title="item.description"
                  >{{item.description}}</p>
                </div>
                <div style="height:200px;display:flex;justify-content:center;margin-top:10px">
                  <img :src="item.picture" v-if="item.picture!=''&&item.picture!='undefined'" />
                  <avatar
                    :username="item.title"
                    :size="200"
                    :title="item.title"
                    :rounded="false"
                    v-else
                  ></avatar>
                </div>
                <div
                  style="height:15px;margin-top:10px;align-items:center;display:flex;justify-content:flex-start"
                >
                  <Icon type="md-body" :size="15" />Manager
                  <span style="height:20px;margin-left:5%">
                    <strong>{{item.managerName}}</strong>
                  </span>
                </div>
                <div
                  style="height:15px;align-items:center;display:flex;justify-content:flex-start;margin-top:10px"
                >
                  <Icon type="md-clock" :size="15" />Time
                  <span style="height:20px;margin-left:5%">
                    <strong>{{item.createTime.split(' ')[0]}}</strong>
                  </span>
                </div>
                <div
                  style="height:15px;align-items:center;display:flex;justify-content:flex-start;margin-top:10px"
                >
                  <Icon type="md-pricetags" :size="15" />Tags
                  <strong
                    style="
                          margin-left:5%;
                          display: inline-block;
                          overflow: hidden;
                          white-space: nowrap;
                          text-overflow: ellipsis;"
                  >{{item.tag}}</strong>
                </div>
              </Card>
            </div>
          </Col>
        </div>
      </div>
      <div v-else>
        <Col span="22" offset="1">
          <Card :bordered="false">
            <div style="display:flex;justify-content:center">
              <Icon type="md-alert" size="40" color="gray" />
            </div>
            <br />
            <div style="display:flex;justify-content:center">
              <h2 style="text-align:center;width:50%">No more projects in this category.</h2>
            </div>
            <br />
            <div style="display:flex;justify-content:center">
              <h4 style="text-align:center;width:50%;color:lightblue">
                You can click the button right top to add a new project.
                <br />Enriching your description of the project and attracting more people to join in.
              </h4>
            </div>
          </Card>
        </Col>
      </div>
    </div>
    <Modal v-model="projectInfoModal" title="Project information" width="800px">
      <Table stripe border :columns="columns" :data="projectInfoShow" :show-header="false"></Table>
      <div slot="footer">
        <Alert
          show-icon
          style="float: left;width: fit-content;display: inline-block;"
          v-show="!UserState"
        >If you want to participate in the project, please login.</Alert>
        <Button v-show="!UserState" type="success" @click="login">Log in</Button>
        <Button
          v-show="!selectedProjectInfo.isMember&&!selectedProjectInfo.isManager && UserState && selectedProjectInfo.privacy=='Public'"
          type="success"
          title="Enter project directly"
          @click="enterProject(selectedProjectInfo)"
        >Enter</Button>
        <Button
          type="success"
          @click="joinApplyModalShow(selectedProjectInfo)"
          v-show="!selectedProjectInfo.isMember&&!selectedProjectInfo.isManager&&UserState"
          title="Apply to join the project"
        >Apply</Button>
      </div>
    </Modal>
    <Modal v-model="applyJoinModal" title="Apply to join the project">
      <Form ref="applyValidate" :model="applyValidate" :rules="applyRuleValidate" :label-width="80">
        <FormItem label="Reason" prop="reason">
          <Input
            v-model="applyValidate.reason"
            type="textarea"
            :rows="4"
            placeholder="Enter The Reason For Application ..."
          />
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="applyJoinModal=false">Cancel</Button>
        <Button type="success" @click="joinApply('applyValidate')">Apply</Button>
      </div>
    </Modal>
    <!-- <Modal
      v-model="publicApplyAssureModal"
      title="Join Assure Notification"
      @on-ok="joinPublicProjects"
      @on-cancel
      ok-text="ok"
      cancel-text="cancel"
    >
      <p>Do you really want to join in this project?</p>
    </Modal> -->
  </div>
</template>
<script>
import Avatar from "vue-avatar";
export default {
  props: ["projectList", "projectType"],
  computed: {
    UserState() {
      return this.$store.getters.userState;
    }
  },
  components: {
    Avatar
  },
  data() {
    return {
      selectedProjectInfo: {},
      projectInfoModal: false,
      columns: [
        {
          title: "Key",
          key: "key",
          width: "150px"
        },
        {
          title: "Value",
          key: "value"
        }
      ],
      projectInfoShow: [],
      //加入项目
      applyProjectInfo: {},
      applyValidate: {
        reason: ""
      },
      applyRuleValidate: {
        reason: [
          {
            required: true,
            message: "Please enter the reason for application",
            trigger: "blur"
          },
          {
            type: "string",
            min: 5,
            message: "The reason no less than 10 characters",
            trigger: "blur"
          }
        ]
      },
      applyJoinModal: false,
      // 记录已经申请的情况
      haveApplied: false,
      // 申请加入项目的模态框
      applyJoinModal: false,
      // // 申请加入公共项目的模态框
      // publicApplyAssureModal: false
    };
  },
  methods: {
    //进入项目详情页面的函数
    projectInfoModalShow(projectInfo) {
      if (projectInfo.isMember || projectInfo.isManager) {
        this.$router.push({ path: `project/${projectInfo.projectId}` });
      } else {
        this.selectedProjectInfo = Object.assign({}, projectInfo);
        var category = "";
        if (
          projectInfo.category != "Human" &&
          projectInfo.category != "GISRS"
        ) {
          category = projectInfo.category;
        } else if (projectInfo.category == "Human") {
          category = "Human-Activity";
        } else if (projectInfo.category == "GISRS") {
          category = "GIS & RS";
        }
        var membersName = "";
        var members = projectInfo.members;
        for (var i = 0; i < members.length; i++) {
          if (i == 0) {
            membersName = members[i].userName;
          } else {
            membersName = membersName + "," + members[i].userName;
          }
        }
        this.projectInfoShow = [
          {
            key: "Category",
            value: category
          },
          {
            key: "Title",
            value: projectInfo.title
          },
          {
            key: "Description",
            value: projectInfo.description
          },
          {
            key: "Introduction",
            value: projectInfo.introduction
          },
          {
            key: "Tag",
            value: projectInfo.tag
          },
          {
            key: "Manager",
            value: projectInfo.managerName
          },
          {
            key: "Members",
            value: membersName
          },
          {
            key: "Created Time",
            value: projectInfo.createTime
          }
        ];
        this.projectInfoModal = true;
      }
    },
    goSingleProject() {
      var id = this.selectedProjectInfo.projectId;
      let isManager, isMember;
      for (let i = 0; i < this.projectList.length; i++) {
        if (this.projectList[i]["projectId"] === id) {
          let projectInfo = this.projectList[i];
          isManager = projectInfo["isManager"];
          isMember = projectInfo["isMember"];
          this.$store.commit("setProjectInfo", projectInfo);
          break;
        }
      }
      if (this.$store.getters.userState) {
        if (isManager || isMember) {
          this.$router.push({ path: `project/${id}` });
        } else {
          this.$Notice.error({
            title: "No access",
            desc:
              "You need to click + button at the north right corner to apply join the project"
          });
        }
      } else {
        this.$router.push({ path: "/login" });
      }
    },
    joinApplyModalShow(applyProjectInfo) {
      if (this.UserState) {
        this.$set(this, "applyProjectInfo", applyProjectInfo);
        this.applyValidate.reason = "";
        if (this.applyProjectInfo.privacy == "Public") {
          // 加个模态框
          // this.publicApplyAssureModal = true;
          this.applyJoinModal = true;
        } else if (this.applyProjectInfo.privacy == "Discoverable") {
          this.applyJoinModal = true;
        }
      } else {
        this.$router.push({ path: "/login" });
      }
    },
    joinApply(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          this.applyJoinModal = false;
          var data = this.applyProjectInfo;
          if (
            this.haveApplied == true &&
            data.projectId == sessionStorage.getItem("applyId")
          ) {
            this.$Notice.warning({
              title: "repeat apply warning",
              desc: "You have apply success, no need to click again!"
            });
          } else {
            if (this.$store.getters.userState) {
              let userDetail = this.$store.getters.userInfo;
              let joinForm = {};
              sessionStorage.setItem("applyId", data.projectId);
              joinForm["recipientId"] = data.managerId;
              joinForm["type"] = "apply";
              joinForm["content"] = {
                userEmail: userDetail.email,
                userName: this.$store.getters.userName,
                userId: this.$store.getters.userId,
                title: "Group application",
                description:
                  "User " +
                  this.$store.getters.userName +
                  " apply to join your project: " +
                  data.title +
                  " ." +
                  " The reason for application is: " +
                  this.applyValidate.reason,
                projectId: data.projectId,
                projectTitle: data.title,
                scope: "project",
                approve: "unknow"
              };
              this.axios
                .post("/GeoProblemSolving/notice/save", joinForm)
                .then(res => {
                  if (res.data == "Success") {
                    this.$Notice.open({
                      title: "Apply Successfully",
                      desc:
                        "The project's manager will process your apply in time,you can get a notification later to tell you the result."
                    });
                    this.$emit("sendNotice", data.managerId);
                    this.haveApplied = true;
                  } else {
                    this.$Notice.open({
                      desc: "Apply failed"
                    });
                  }
                })
                .catch(err => {
                  console.log("申请失败的原因是：" + err.data);
                });

              let emailObject = {
                recipient: joinForm.recipientId,
                mailTitle: "Group application",
                mailContent:
                  joinForm.content.description +
                  "<br>" +
                  "You can click this url and enter the site to process this application: " +
                  "http://" +
                  this.$store.state.IP_Port +
                  "/GeoProblemSolving/home"
              };
              this.axios
                .post("/GeoProblemSolving/project/applyByMail", emailObject)
                .then(res => {
                  if (res.data == "Success") {
                    console.log("Email Success.");
                  } else {
                    console.log("Email fail.");
                  }
                })
                .catch(err => {
                  console.log("Email fail.");
                });
            } else {
              this.$Message.error("you must have an account before you apply");
              this.$router.push({ name: "Login" });
            }
          }
        } else {
          this.$Message.error("Fail!");
        }
      });
    },
    login() {
      this.$router.push({ name: "Login" });
    },
    enterProject(projectInfo) {
      this.$router.push({ path: `project/${projectInfo.projectId}` });
    },
    joinPublicProjects() {
      this.axios
        .get(
          "/GeoProblemSolving/project/join?projectId=" +
            this.applyProjectInfo.projectId +
            "&userId=" +
            this.$store.getters.userId
        )
        .then(res => {
          if (res.data == "Success") {
            this.$Notice.open({
              title: "Notification title",
              desc: "Success,now you are a member in this project.",
              duration: 0
            });
            for (var i = 0; i < this.projectList.length; i++) {
              var thisProjectId = this.projectList[i].projectId;
              if (thisProjectId == this.applyProjectInfo.projectId) {
                this.projectList[i].isMember = true;
                break;
              }
            }
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    }
  }
};
</script>
