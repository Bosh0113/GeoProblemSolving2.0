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
            style=" height: 725px; padding: 5px; border: #dcdee2 solid 1px;"
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
                  :key="index"
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
                          @click.stop="authorizeModalShow(index)"
                          icon="md-happy"
                        ></Button>
                        <Button
                          class="deleteBtn"
                          type="default"
                          slot="extra"
                          style="margin:0 0 0 5px"
                          @click.stop="deleteProjectModalShow(mProject.projectId)"
                          icon="md-close"
                          title="remove"
                        ></Button>

                        <!--  @click.stop="deleteProjectModalShow(mProject.projectId)" -->
                        <!-- 表头结束 -->
                        <!--              200px  -->
                        <p
                          style="height:200px;text-indent:2em;word-break:break-word;white-space: pre-line;"
                        >
                          <span style="font-weight: bold">Description</span>
                          <vue-scroll :ops="ops">{{mProject.description}}</vue-scroll>
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
                  v-for="(item,index) in joinedProjectsList"
                  :key="index"
                  v-show="joinedProjectsList!=[]"
                >
                  <Col span="10" offset="1">
                    <div @click="goSingleProject(item)" class="projectItem">
                      <Card style="height:320px;margin-top:20px;">
                        <p
                          slot="title"
                          style="height: 40px"
                          class="projectsTitle"
                        >{{item.title}}</p>
                        <Button
                          class="fileBtnHoverRed"
                          slot="extra"
                          @click.stop="quitModalShow(item)"
                        >Quit
                        </Button>
                        <p
                          style="height:200px;text-indent:2em;word-break:break-word;white-space: pre-line;"
                        >
                          <vue-scroll :ops="ops">{{item.introduction}}</vue-scroll>
                        </p>
                        <br/>
                        <div style="height:40px">
                          <span style="float:left">CreateTime:</span>
                          <span style="float:right">{{item.createTime}}</span>
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
        <h3>Do you really want to delete this project?</h3>
      </Modal>
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
        deleteProjectId: "",
        userEventList: [],
        selectedProjectType: ['joinedProject', 'createdProject'],
        deleteProjectId: "",
        member: {},
        ops: {
          bar: {
            background: "#808695"
          }
        },
      }
    },
    mounted() {
      this.getUserProject();
      this.readPersonalEvent();
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
          this.$axios.get("/GeoProblemSolving/project/getProjects?aids=" + createdProjectIds)
            .then(res => {
              this.$set(this, "joinedProjectsList", res.data.data)
            })
            .catch(err => {
              this.$Message.error("Loading project failed.")
            })
        }
      },

      deleteProject() {
        if (this.deleteProjectId != "") {
          this.axios
            .delete(
              "/GeoProblemSolving/project?" +
              "aid=" + this.deleteProjectId
            )
            .then(res => {
              if (res.data == "Offline") {
                this.$store.commit("userLogout");
                this.$router.push({name: "Login"});
              } else if (res.data == "Success") {
                var newManageProjects = [];
                var oldManageProjects = this.createdProjectList;
                for (var i = 0; i < oldManageProjects.length; i++) {
                  if (oldManageProjects[i].projectId != this.deleteProjectId) {
                    newManageProjects.push(oldManageProjects[i]);
                  }
                }
                this.$set(this, "createdProjectList", newManageProjects);
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

      deleteProjectModalShow(pid) {
        this.deleteProjectId = pid;
        this.deleteProjectModal = true;
      },
      authorizeModalShow(index) {
        this.authorizeProjectModal = true;
        this.projectMemberList = this.createdProjectList[index].members;
        this.currentProject = this.createdProjectList[index];
      },

      //获取用户参与的项目列表
      getParticipatoryList(projectType, projectIds) {
        let projectTemp = [];
        if (projectIds != null) {
          this.axios
            .post("/GeoProblemSolving/user/getMProject", projectIds)
            .then(res => {
              if (res.data.data != "Fail" && res.data.data != "None") {
                projectTemp = res.data.data;
                if (projectType == "joinedProject") {
                  this.$set(this, "joinedProjectsList", projectTemp);
                } else if (projectType == "createdProject") {
                  this.$set(this, "createdProjectList", projectTemp);
                }

              } else {
                this.$Message.error("Load Fail.")
              }
            })
            .catch(e => {
              this.$Message.error("Load Fail.")
            })
        }
        // if (projectIds != null) {
        //   var count = projectIds.length;
        //   let participatoryProjectListTemp = [];
        //   for (let i = 0; i < projectIds.length; i++) {
        //     this.axios
        //       .get(
        //         "/GeoProblemSolving/user//" +
        //         projectIds[i].projectId
        //       )
        //       .then(res => {
        //         if (res.data != "None") {
        //           participatoryProjectListTemp.push(res.data[0]);
        //         }
        //         if (--count == 0) {
        //           var participatoryProjectList = [];
        //           for (var j = 0; j < projectIds.length; j++) {
        //             for (
        //               var k = 0;
        //               k < participatoryProjectListTemp.length;
        //               k++
        //             ) {
        //               if (
        //                 projectIds[j].projectId ==
        //                 participatoryProjectListTemp[k].projectId
        //               ) {
        //                 participatoryProjectList.push(
        //                   participatoryProjectListTemp[k]
        //                 );
        //                 break;
        //               }
        //             }
        //           }
        //           this.$set(this, "joinedProjectsList", participatoryProjectList);
        //         }
        //       })
        //       .catch(err => {
        //         console.log(err.data);
        //       });
        //   }
        // }
      },


      //废弃
      //获取用户可管理支配的全部项目列表
      getManagerProjectList() {
        let createdProjectTemp = [];
        this.axios
          .post("/GeoProblemSolving/user/getMProject", this.$store.getters.userInfo.createdProjects)
          .then(res => {
            if (res.data.data != "Fail" && res.data.data != "None") {
              createdProjectTemp = res.data.data;
              this.$set(this, "createdProjectList", createdProjectTemp);
            } else {
              this.$Message.error("Load Fail.")
            }
          })
          .catch(e => {
            this.$Message.error("Load Fail.")
          })
      },

      //读取用户对项目的操作
      readPersonalEvent() {
        this.axios.get(
          "/GeoProblemSolving/history/inquiry?" +
          "eventType=project" +
          "&key=userId" +
          "&value=" +
          this.$store.getters.userId
        )
          .then(res => {
            if (res.data == "offline") {
              this.$store.commit("userLogout");
            } else if (res.data != "None" && res.data != "Fail") {
              this.userEventList = res.data.reverse();
            }
          })
      },
    }
  }
</script>

