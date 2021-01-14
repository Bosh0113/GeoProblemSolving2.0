<style scoped>
  .historyLine >>> .ivu-card-head {
    border-bottom-color: white;
    padding: 4px 6px;
  }

  .historyLine >>> .ivu-card-body {
    padding: 5px;
    height: 100%;
  }

  .timeLineStyle {
    height: 735px;
    margin-left: 5%;
  }

  .authorBtn:hover {
    background-color: #57a3f3;
    color: white;
  }

  .badge {
    display: inline-block;
    min-width: 10px;
    padding: 3px 7px;
    font-size: 12px;
    font-weight: bold;
    line-height: 1;
    color: #fff;
    text-align: center;
    white-space: nowrap;
    vertical-align: baseline;
    background-color: #999;
    border-radius: 10px;
  }
</style>
<template>
  <div>
    <Row>
      <Col span="20">
        <!--    Filter and Storage  -->
        <Row>
          <Card>
            <CheckboxGroup v-model="selectedProjectType" style="margin-left: 3.5%">
              <Checkbox label="joinedProject" checked="checked">
                <span>Joined by me</span>
                <span class="badge">{{joinedProjectsList.length}}</span>
              </Checkbox>
              <Checkbox label="createdProject">
                <span>Created by me</span>
                <span class="badge">{{createdProjectList.length}}</span>
              </Checkbox>
            </CheckboxGroup>
          </Card>
        </Row>

        <!--    project content     -->
        <Row>
          <div
            :style="{height: contentHeight-140+'px'}"
            style=" padding: 5px; border: #dcdee2 solid 1px;"
          >
            <vue-scroll :ops="ops">
              <Card :bordered="false"
                    v-if="createdProjectList.length == 0 && selectedProjectType.includes('createdProject')">
                <div style="display:flex;justify-content:center">
                  <Icon type="md-alert" size="40" color="gray"/>
                </div>
                <br/>
                <div style="display:flex;justify-content:center">
                  <h3
                    style="text-align:center;width:80%"
                  >Sorry, you didn't create any projects.</h3>
                </div>
              </Card>

              <Card :bordered="false"
                    v-if="joinedProjectsList.length == 0 && selectedProjectType.includes('joinedProject') && selectedProjectType.length == 1">
                <div style="display:flex;justify-content:center">
                  <Icon type="md-alert" size="40" color="gray"/>
                </div>
                <br/>
                <div style="display:flex;justify-content:center">
                  <h3
                    style="text-align:center;width:80%"
                  >Sorry, you didn't participate in any projects.</h3>
                </div>
              </Card>

              <Card :bordered="false" v-if="selectedProjectType.length == 0">
                <div style="display:flex;justify-content:center">
                  <Icon type="md-alert" size="40" color="gray"/>
                </div>
                <br/>
                <div style="display:flex;justify-content:center">
                  <h3
                    style="text-align:center;width:80%"
                  >Sorry, There are not any projects.</h3>
                </div>
              </Card>


              <!--               项目内容显示        -->
              <div v-show="userProjectCount.length != 0">
                <div
                  v-for="(mProject,index) in createdProjectList"
                  v-show="createdProjectList!='None'"
                  :key="'create'+index"
                >
                  <Col span="7" style="margin-left: 3.5%">
                    <div class="projectItem" @click="goSingleProject(mProject)">
                      <Card style="height:320px;margin-top:20px;">
                        <p slot="title" class="projectsTitle">{{mProject.name}}</p>
                        <Button
                          class="authorBtn"
                          type="default"
                          slot="extra"
                          title="Privilege change"
                          @click.stop="authorizeModalShow(mProject.aid)"
                          icon="md-happy"
                        ></Button>
                        <Button
                          class="deleteBtn"
                          type="default"
                          slot="extra"
                          style="margin:0 0 0 5px"
                          @click.stop="deleteProjectModalShow(mProject.aid, mProject.name)"
                          icon="md-close"
                          title="remove"
                        ></Button>

                        <!--  @click.stop="deleteProjectModalShow(mProject.projectId)" -->
                        <!-- 表头结束 -->
                        <!--              200px  -->
                        <p
                          style="height:200px;"
                        >
                          <span style="font-weight: bold;">Description</span>
                          <vue-scroll :ops="ops" style="text-indent:2em;word-break:break-word;white-space: pre-line;">{{mProject.description}}</vue-scroll>
                        </p>
                        <br/>
                        <div>
                          <span style="float:left">CreateTime:</span>
                          <span style="float:right">{{mProject.createdTime}}</span>
                        </div>
                      </Card>
                    </div>
                  </Col>
                </div>


                <div
                  v-for="(item,Index) in joinedProjectsList"
                  :key="'join'+ Index"
                  v-show="joinedProjectsList!=[]"
                >
                  <Col span="7" style="margin-left: 3.5%">
                    <div @click="goSingleProject(item)" class="projectItem">
                      <Card style="height:320px;margin-top:20px;">
                        <p
                          slot="title"
                          class="projectsTitle"
                        >{{item.name}}</p>
<!--                        @click.stop="quitModalShow(item)"-->
                        <Button
                          class="fileBtnHoverRed"
                          slot="extra"
                          @click.stop="quitModalShow(item)"
                        >Quit
                        </Button>
                        <p
                          style="height:200px;"
                        >
                          <span style="font-weight: bold">Description</span>
                          <vue-scroll :ops="ops" style="text-indent:2em;word-break:break-word;white-space: pre-line;">{{item.description}}</vue-scroll>
                        </p>
                        <br/>
                        <div style="height:40px">
                          <span style="float:left">CreateTime:</span>
                          <span style="float:right">{{item.createdTime}}</span>
                        </div>
                      </Card>
                    </div>
                  </Col>
                </div>
              </div>
            </vue-scroll>
          </div>
        </Row>
      </Col>

      <!--    Event History-->
<!--      <Col span="4">-->
<!--        <Card dis-hover class="historyLine">-->
<!--          <p slot="title">Event line</p>-->
<!--          <div class="timeLineStyle">-->
<!--            <vue-scroll :ops="ops">-->
<!--              <Timeline>-->
<!--                <div v-if="userEventList.length==0">-->
<!--                  <div style="display:flex;justify-content:center">-->
<!--                    <Icon type="md-alert" size="40" color="gray"/>-->
<!--                  </div>-->
<!--                  <br/>-->
<!--                  <div style="display:flex;justify-content:center">-->
<!--                    <h3-->
<!--                      style="text-align:center;width:80%"-->
<!--                    >Sorry, there are no events now.</h3>-->
<!--                  </div>-->
<!--                </div>-->
<!--                <TimelineItem-->
<!--                  v-for="(item,index) in userEventList"-->
<!--                  :key="index"-->
<!--                  v-show="userEventList.length>0"-->
<!--                >-->
<!--                  <strong>-->
<!--                    <p class="time">{{item.createTime}}</p>-->
<!--                  </strong>-->
<!--                  <p class="content">{{item.description}}</p>-->
<!--                </TimelineItem>-->
<!--              </Timeline>-->
<!--            </vue-scroll>-->
<!--          </div>-->
<!--        </Card>-->
<!--      </Col>-->

      <!--      模态框     -->
      <Modal
        v-model="deleteProjectModal"
        @on-ok="deleteProject"
        @on-cancel
        ok-text="Assure"
        cancel-text="Cancel"
      >
        <p style="font-family: 'Roboto Light';">Do you really want to delete: <strong>{{delProjectName}}</strong>?</p>
      </Modal>

      <Modal
        title="Quit Project"
        v-model="quitModal"
        @on-ok="quitProject()"
        @on-cancel
        ok-text="Ok"
        cancel-text="Cancel"
      >
        <p>Once you exit the project: <strong>{{currentProject.name}}</strong> ,you will not be able to participate in the collaborative process, confirm the exit?</p>
      </Modal>

<!--      <Modal-->
<!--        v-model="authorizeProjectModal"-->
<!--        @on-ok="authorize()"-->
<!--        ok-text="Assure"-->
<!--        cancel-text="Cancel"-->
<!--      >-->
<!--        <p style="slot">Hand the project to others</p>-->
<!--        <div>-->
<!--          <RadioGroup v-model="selectManagerId">-->
<!--            <Radio v-for="member in projectMemberList" :key="member.index" :label="member.userId">-->
<!--              <span>{{member.userName}}</span>-->
<!--            </Radio>-->
<!--          </RadioGroup>-->
<!--          &lt;!&ndash; 用radio将用户表示出来 &ndash;&gt;-->
<!--          &lt;!&ndash; <tag v-for="(member,index) in projectMemberList" @click="choose(index)" :key="member.index">{{member.userName}}</tag> &ndash;&gt;-->
<!--        </div>-->
<!--      </Modal>-->
    </Row>
  </div>
</template>

<script>
  export default {
    name: "project",
    components: {},
    data() {
      return {
        joinedProjectsList: [],
        createdProjectList: [],
        // 用于显示的project
        userProjectList: [],
        deleteProjectModal: false,
        quitModal: false,
        projectMemberList: [],
        deleteProjectId: "",
        delProjectName: "",
        currentProject: {},
        userEventList: [],
        selectedProjectType: ['joinedProject', 'createdProject'],
        deleteProjectId: "",
        member: {},
        contentHeight: "",
        ops: {
          bar: {
            background: "#808695"
          }
        },
      }
    },
    mounted() {
      this.getUserProject();
      // this.readPersonalEvent();
      this.resizeContent();
    },
    computed: {
      userProjectCount: function () {
        if (this.selectedProjectType.length == 0) {
          this.userProjectList = [];
          return this.userProjectList;
        } else if (this.selectedProjectType.length == 2) {
          this.userProjectList = [];
          this.userProjectList = this.joinedProjectsList.concat(this.createdProjectList);
          return this.userProjectList;
        } else if (this.selectedProjectType.length == 1 && this.selectedProjectType[0] == "joinedProject") {
          this.userProjectList = [];
          this.userProjectList = this.joinedProjectsList;
          return this.userProjectList;
        } else if (this.selectedProjectType.length == 1 && this.selectedProjectType[0] == "createdProject") {
          this.userProjectList = [];
          this.userProjectList = this.createdProjectList;
          return this.userProjectList;
        }
      },
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
      getUserProject: function () {
        let userInfo = this.$store.getters.userInfo;

        if (userInfo.createdProjects != null) {
          let createdProjectIds = "";
          for (let i = 0; i < userInfo.createdProjects.length; i++) {
            if (i != userInfo.createdProjects.length - 1) {
              createdProjectIds += userInfo.createdProjects[i] + ","
            } else {
              createdProjectIds += userInfo.createdProjects[i]
            }
          }
          // project/getProjects?aids=  用户获取项目
          this.$axios.get("/GeoProblemSolving/project/getProjects?aids=" + createdProjectIds)
            .then(res => {
              this.$set(this, "createdProjectList", res.data.data)
              console.log(res.data.data)
            })
            .catch(err => {
              this.$Message.error("Loading project failed.")
            })
        }

        if (userInfo.joinedProjects != null) {
          let joinedProjectIds = "";
          for (let i = 0; i < userInfo.joinedProjects.length; i++) {
            if (i != userInfo.joinedProjects.length - 1) {
              joinedProjectIds += userInfo.joinedProjects[i] + ","
            } else {
              joinedProjectIds += userInfo.joinedProjects[i]
            }
          }
          //projectIds 获取所有项目详情
          this.$axios.get("/GeoProblemSolving/project/getProjects?aids=" + joinedProjectIds)
            .then(res => {
              this.$set(this, "joinedProjectsList", res.data.data)
              console.log(res.data.data)
            })
            .catch(err => {
              this.$Message.error("Loading project failed.")
            })
        }
      },
      deleteProjectModalShow(pid, pName) {
        this.deleteProjectId = pid;
        this.delProjectName = pName;
        this.deleteProjectModal = true;
      },
      deleteProject() {
        if (this.deleteProjectId != "") {
          this.axios
            .delete(
              "/GeoProblemSolving/project?" +
              "aid=" + this.deleteProjectId
            )
            .then(res => {
              if (res.data.data == "Offline") {
                this.$store.commit("userLogout");
                this.$router.push({name: "Login"});
              } else if (res.data.data == "Success") {
                // var newManageProjects = [];
                // var oldManageProjects = this.createdProjectList;
                if (this.createdProjectList != null){
                  for (let i=0; i < this.createdProjectList.length; i++){
                    if (this.deleteProjectId == this.createdProjectList[i].aid){
                      this.createdProjectList.splice(i, 1);
                      break;
                    }
                  }
                }
                this.$Notice.success({title: "Delete Success."})
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
      //点击跳转到指定项目的函数
      goSingleProject(projectInfo) {
        // sessionStorage.setItem("projectInfo", JSON.stringify(projectInfo));
        // sessionStorage.setItem("projectInfo", this.encrypto(projectInfo));
        window.location.href =
          "/GeoProblemSolving/projectInfo/" + projectInfo.aid;
      },
      authorizeModalShow: function(aid) {
        let url = window.location.href;
        let permissionUrl = url.split("newPersonalPage")[0] + "permission/0/"+aid
        window.open(permissionUrl,"_self")
      },


      //读取用户对项目的操作
      // readPersonalEvent() {
      //   this.axios.get(
      //     "/GeoProblemSolving/history/inquiry?" +
      //     "eventType=project" +
      //     "&key=userId" +
      //     "&value=" +
      //     this.$store.getters.userId
      //   )
      //     .then(res => {
      //       if (res.data == "offline") {
      //         this.$store.commit("userLogout");
      //       } else if (res.data != "None" && res.data != "Fail") {
      //         this.userEventList = res.data.reverse();
      //       }
      //     })
      // },
      authorize() {
        this.axios
          .get(
            "/GeoProblemSolving/project/" + this.currentProject.projectId + "/user" +
            "?userId=" + this.selectManagerId +
            "&role=manager"
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
      quitModalShow(project) {
        this.quitModal = true;
        this.currentProject = project;
        console.log(project)
        this.delProjectName = project.name;
      },
      quitProject: function() {
        let quitProjectId = this.currentProject.aid;
        let quitUserId = this.$store.getters.userId;
        let members = this.currentProject.members;
        let creatorId = "";
        for (let i = 0; i < members.length; i++){
          if (members[i].role == "manager"){
            creatorId = members[i].userId;
          }
        }
        let formData  = new URLSearchParams();
        formData.append("userId", quitUserId);
        formData.append("projectId", quitProjectId);

        this.axios
          .put("/GeoProblemSolving/user/quitProject", formData)
          .then(res=>{
            if (res.data.code == 0){
              this.$Notice.success({title: "Quit Success"})
              this.removeQuitProject(quitProjectId);
              let notice = {};
              let recipientId = this.currentProject.members[0].userId;
              notice["recipientId"] = creatorId;
              notice["type"] = "notice";
              notice["content"] = {
                title: "Manager change",
                description:
                  "The member called " +
                  this.$store.getters.userInfo.name +
                  " had quited from your project " +
                  this.currentProject.name +
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
            }else if (res.data.code == -2){
              this.$Message.error("Quit Failed.")
            }
          })
          .catch(err=>{
            this.$Message.error("Quit Fail.");
          })
        // this.axios
        //   .get(
        //     "/GeoProblemSolving/project/" + quitProjectId + "/user" +
        //     "?userId=" +
        //     this.$store.getters.userId
        //   )
        //   .then(res => {
        //     if (res.data == "Offline") {
        //       this.$store.commit("userLogout");
        //       this.$router.push({ name: "Login" });
        //     } else if (res.data != "Fail" && res.data != "None") {
        //
        //       this.$Message.success("Quit Success");
        //       this.removeQuitProject(quitProjectId);
        //       let notice = {};
        //       let recipientId = this.currentProject.members[0].userId;
        //       notice["recipientId"] = recipientId;
        //       notice["type"] = "notice";
        //       notice["content"] = {
        //         title: "Manager change",
        //         description:
        //           "The member called " +
        //           this.$store.getters.userInfo.name +
        //           " had quited from your project " +
        //           this.currentProject.name +
        //           "!"
        //       };
        //       this.axios
        //         .post("/GeoProblemSolving/notice/save", notice)
        //         .then(res => {
        //           if (res.data == "Success") {
        //             this.$emit("sendNotice", recipientId);
        //           }
        //         })
        //         .catch(err => {
        //           console.log("申请失败的原因是：" + err.data);
        //         });
        //     } else {
        //       this.$Message.error("Quit Fail.");
        //       console.log("Quit fail: " + res.data);
        //     }
        //   })
        //   .catch(err => {});
      },
      removeQuitProject(projectId) {
        for (var i = 0; i < this.joinedProjectsList.length; i++) {
          if (this.joinedProjectsList[i].aid == projectId) {
            this.joinedProjectsList.splice(i, 1);
          }
        }
      },
    }
  }
</script>

