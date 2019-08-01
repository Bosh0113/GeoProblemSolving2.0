<style scoped>
.sidebar {
  font-weight: bold;
  width: 15%;
}
.right {
  width: 90%;
  margin-left: 5%;
  height: 100%;
}
.createTaskBtn {
  font-size: 10px;
}
.createTaskBtn:hover {
  color: white;
  background: #47cb89;
}
.title {
  height: 40px;
  line-height: 40px;
  text-align: center;
  font-size: 20px;
  font-weight: bold;
  border-bottom: 1px solid lightgray;
}
.member-desc {
  height: 60px;
  margin: 0 20px 0 10px;
  display: flex;
}
.member-image {
  width: 60px;
  height: 60px;
  padding: 5px;
}
.memebr-work {
  width: 65%;
  height: 60px;
}
.userName {
  margin-top: 10px;
  height: 20px;
  display: flex;
  align-items: center;
}
.organization {
  height: 30px;
  display: flex;
  align-items: center;
}
.subProjectDesc {
  text-indent: 2em;
  padding: 10px;
  /* word-break: break-all; */
}
.taskFormItem {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}
.taskFormItem span {
  text-align: center;
}
#taskContainer {
  padding: 10px;
  background-color: white;
}
.taskList {
  min-height: 60px;
  background: #f7f7f7;
}
.taskName {
  display: inline-block;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  max-width: 120px;
}
.operatePanel button {
  margin-right: 2.5%;
}
.memberOrganization {
  height: 40px;
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 100%;
}
</style>
<template>
  <div>
    <Row>        
        <Col span="21" style="margin:10px 0 0 100px">          
          <div class="resourcePanel" style="padding-top: 20px">
            <folder-tree
              :rootFolderId="subProjectInfo.subProjectId"
              :role="userRole"
              ref="folderTreeEle"
            ></folder-tree>
          </div>
        </Col>
      </Row>
    <!-- </Row> -->
  </div>
</template>
<script>
import Avatar from "vue-avatar";
import folderTree from "../resources/folderTree";
export default {
  updated() {
    $(".userAvatar sup").css("margin", "15px 15px 0 0");
  },
  components: {
    Avatar,
    folderTree
  },
  props: ["subProjectInfo"],
  data() {
    return {
      subProjectFileUploadForm: {
        privacy: "private",
        type: "data",
        description: ""
      },
      subProjectFileUploadFormRuleValidate: {
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
        description: [
          {
            required: true,
            message: "file description cannot be empty",
            trigger: "blur"
          }
        ]
      },
      // information of project
      projectInfo: {},
      //登陆者身份
      // 关于邀请的模态框
      inviteModal: false,
      quitModal: false,
      sidebarHeight: 800,
      descHeight: 250,
      taskContainerHeight: 800,
      participants: [],
      candidates: [],
      inviteList: [],
      inviteAble: true,
      oldTabPaneState: "home",
      // 后台获取的step下的task列表
      taskList: [],
      selectTaskIndex: 0,
      taskDeleteModal: false,
      // 创建任务的模态框
      createTaskModal: false,
      // 编辑任务的模态框
      editTaskModal: false,
      taskDetailModal: false,
      // task的placeHolder默认值
      taskPlaceHolder: {
        description: "Please input the task description.",
        name: "Please input the task name",
        startTime: "Choose the start time of task",
        endTime: "Choose the end time of task"
      },
      //task相关
      taskInfo: {},
      taskTodo: [],
      taskDoing: [],
      taskDone: [],
      MoveCount: 0,
      // 动态记录相关
      // 消息
      subprojectSocket: null,
      timer: null,
      socketMsg: {
        type: "",
        time: "",
        who: "",
        whoid: "",
        content: ""
      },
      formValidate: {
        taskName: "",
        description: "",
        startTime: "",
        endTime: "",
        importanceCheck: false
      },
      ruleValidate: {
        taskName: [
          { required: true, message: "Please enter name...", trigger: "blur" }
        ],
        description: [
          { required: true, message: "Please select type...", trigger: "blur" }
        ],
        startTime: [{ required: true, type: "date", trigger: "blur" }],
        endTime: [{ required: true, type: "date", trigger: "blur" }]
      },
      contentHeight: "",
      // tab栏当前选中的tab,初始化默认为home
      currentTab: "home",
      toProjectPage: "",
      toSubProjectPage: "",
      // 上传文件
      uploadShow: false,
      privacy: "",
      file: [],
      fileDescription: "",
      fileType: "",
      progressModalShow: false,
      uploadProgress: 0,
      // 子项目资源列表
      subProjectResourceList: [],
      projectTableColName: [
        {
          title: "Name",
          key: "name",
          tooltip: true,
          sortable: true
        },
        {
          title: "Type",
          key: "type",
          width: 90,
          sortable: true
        },
        {
          title: "Size",
          key: "fileSize",
          width: 100,
          sortable: true
        },
        {
          title: "Description",
          key: "description",
          tooltip: true,
          sortable: true
        },
        {
          title: "Upload time",
          key: "uploadTime",
          width: 150,
          sortable: true
        },
        {
          title: "Action",
          slot: "action",
          width: 120,
          align: "center"
        }
      ],
      panel: null,
      // 删除成员的提醒
      removeMemberAlert: false,
      // 用户角色
      userRole: ""
    };
  },
  created() {
    this.init();
  },
  mounted() {
    this.contentHeight = window.innerHeight + "px";
    this.toProjectPage = "/project/" + sessionStorage.getItem("projectId");
    this.inquiryTask();
    this.getProjectInfo();
    window.addEventListener("resize", this.initSize);
  },
  
  beforeRouteLeave(to, from, next) {
    // this.$refs.folderTreeEle.closePanel();
    next();
  },
  beforeDestroy: function() {
    window.removeEventListener("resize", this.initSize);
  },
  methods: {
    initSize() {
      //侧边栏的高度随着屏幕的高度自适应
      this.sidebarHeight = window.innerHeight - 250;
      this.descHeight = (window.innerHeight - 250) / 3;
      this.taskContainerHeight = this.sidebarHeight + 10;
    },
    //初始化函数，作用是控制侧边栏的高度，设置右边通知栏弹出时候的距顶高度以及延迟的时间
    init() {
      this.initSize();
      var that = this;
      let subProjectId = this.$route.params.id;
      let subProjectInfo = this.$store.getters.subProject;
      if (
        JSON.stringify(subProjectInfo) != "{}" &&
        subProjectInfo.subProjectId == subProjectId
      ) {
        var userId = this.$store.getters.userId;
        var members = subProjectInfo.members;
        subProjectInfo.isMember = false;
        for (var i = 0; i < members.length; i++) {
          if (members[i].userId == userId) {
            subProjectInfo.isMember = true;
            break;
          }
        }
        this.$set(this, "subProjectInfo", subProjectInfo);
        this.inviteAble = false;
        this.showMembers();
        sessionStorage.setItem("subProjectId", subProjectInfo.subProjectId);
        sessionStorage.setItem("subProjectName", subProjectInfo.title);
      } else {
        $.ajax({
          url:
            "/GeoProblemSolving/subProject/inquiry" +
            "?key=subProjectId" +
            "&value=" +
            subProjectId,
          type: "GET",
          async: false,
          success: data => {
            if (data == "Offline") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else if (data != "None" && data != "Fail") {
              subProjectInfo = data[0];
              this.$set(this, "subProjectInfo", subProjectInfo);
              sessionStorage.setItem(
                "subProjectId",
                subProjectInfo.subProjectId
              );
              sessionStorage.setItem("subProjectName", subProjectInfo.title);

              // this.managerIdentity(subProjectInfo.managerId);
              this.memberIdentity(subProjectInfo.members);
              this.$store.commit("setSubProjectInfo", subProjectInfo);
              this.inviteAble = false;
              this.showMembers();
            }
          },
          error: function(err) {
            console.log("Get manager name fail.");
          }
        });
      }
      // 判断用户权限
      if (
        !this.subProjectInfo.isMember &&
        this.subProjectInfo.managerId != this.$store.getters.userId
      ) {
        this.userRole = "Visitor";
      }
    },
    // managerIdentity(managerId) {
    //   if (managerId === this.$store.getters.userId) {
    //     this.subProjectInfo.isManager = true;
    //   }
    // },
    memberIdentity(members) {
      for (let i = 0; i < members.length; i++) {
        if (members[i].userId === this.$store.getters.userId) {
          this.subProjectInfo.isMember = true;
          break;
        }
      }
    },
    showMembers() {
      let membersList = this.subProjectInfo.members;
      let manager = { userId: this.subProjectInfo.managerId };
      if (
        membersList.length == 0 ||
        membersList[0].userId != this.subProjectInfo.managerId
      ) {
        membersList.unshift(manager);
      }
      let participantsTemp = [];
      let index = membersList.length - 1;

      this.axios
        .get(
          "/GeoProblemSolving/user/inquiry" +
            "?key=" +
            "userId" +
            "&value=" +
            membersList[0].userId
        )
        .then(res => {
          if (res.data != "Fail" && res.data != "None") {
            participantsTemp.push(res.data);
            this.$set(this, "participants", participantsTemp);

            for (let i = 1; i < membersList.length; i++) {
              this.axios
                .get(
                  "/GeoProblemSolving/user/inquiry" +
                    "?key=" +
                    "userId" +
                    "&value=" +
                    membersList[i].userId
                )
                .then(res => {
                  if (res.data != "Fail" && res.data != "None") {
                    participantsTemp.push(res.data);
                    if (index-- == 1) {
                      this.$set(this, "participants", participantsTemp);
                    }
                  }
                })
                .catch(err => {});
            }
          }
        })
        .catch(err => {});
    },
    closeStepSocket() {
      if (this.subprojectSocket != null) {
        this.removeTimer();
        this.subprojectSocket.close();
      }
    },
    openStepSocket() {
      if (this.subprojectSocket != null) {
        this.subprojectSocket = null;
      }

      let roomId = this.subProjectInfo.subProjectId + "task";
      var subprojectSocketURL =
        "ws://localhost:8081/GeoProblemSolving/Module/" + roomId;
      // var subprojectSocketURL = "ws://"+this.$store.state.IP_Port+"/GeoProblemSolving/Module/" + roomId;
      this.subprojectSocket = new WebSocket(subprojectSocketURL);
      this.subprojectSocket.onopen = this.onOpen;
      this.subprojectSocket.onmessage = this.onMessage;
      this.subprojectSocket.onclose = this.onClose;
      this.subprojectSocket.onerror = this.onError;
      this.setTimer();
    },
    onOpen() {
      console.log("StepSocket连接成功！");
    },
    // 更新人员，更新数据，更新records
    onMessage(e) {
      let messageJson = JSON.parse(e.data);
      let record = {
        type: "",
        time: "",
        who: "",
        content: ""
      };

      // 任务记录
      if (messageJson.type == "tasks") {
        this.inquiryTask();
      }
    },
    onClose(e) {
      this.removeTimer();
      console.log("StepSocket连接断开！");
    },
    onError(e) {
      this.removeTimer();
      console.log("StepSocket连接错误！");
    },
    setTimer() {
      var that = this;
      this.timer = setInterval(() => {
        var messageJson = {};
        messageJson["type"] = "ping";
        messageJson["message"] = "ping";
        if (
          that.subprojectSocket != null &&
          that.subprojectSocket != undefined
        ) {
          that.subprojectSocket.send(JSON.stringify(messageJson));
        }
      }, 20000);
    },
    removeTimer() {
      clearInterval(this.timer);
    },
    sendMessage(message) {
      if (this.subprojectSocket != null) {
        this.subprojectSocket.send(JSON.stringify(message));
      }
    },
    // 召集参与者
    conveneWork() {
      for (let i = 0; i < this.participants.length; i++) {
        if (this.participants[i].userId != this.$store.getters.userId) {
          let notice = {};
          notice["recipientId"] = this.participants[i].userId;
          notice["type"] = "work";
          notice["content"] = {
            subProjectId: this.subProjectInfo.subProjectId,
            title: "Work Notice",
            description:
              "The manager of" +
              " the sub-project " +
              this.subProjectInfo.title +
              " of project " +
              this.projectInfo.title +
              " informs you to start working!"
          };
          this.axios
            .post("/GeoProblemSolving/notice/save", notice)
            .then(res => {
              if (res.data == "Success") {
                this.$Notice.success({
                  desc: "Inform Successfully"
                });
              } else {
                this.$Notice.info({
                  desc: "Inform failed"
                });
              }
            })
            .catch(err => {
              console.log(err.data);
            });
        }
      }
    },
    ok() {
      this.$Message.info("Clicked ok");
    },
    cancel() {},
    //加载并打开成员邀请Modal
    inviteMembersModalShow() {
      this.candidates = [];
      this.inviteList = [];

      let allMembers = this.projectInfo.members;
      let manager = {
        userName: this.projectInfo.managerName,
        userId: this.projectInfo.managerId
      };
      allMembers.unshift(manager);
      for (let i = 0; i < allMembers.length; i++) {
        let exist = false;
        for (let j = 0; j < this.participants.length; j++) {
          if (allMembers[i].userId === this.participants[j].userId) {
            exist = true;
          }
        }
        if (!exist) {
          this.candidates.push(allMembers[i]);
        }
      }
      this.inviteModal = true;
    },
    getProjectInfo() {
      let that = this;
      let projectInfo = that.$store.getters.project;
      if (
        JSON.stringify(projectInfo) != "{}" &&
        projectInfo.projectId.substring(0, 36) ==
          this.subProjectInfo.projectId.substring(0, 36)
      ) {
        this.projectInfo = projectInfo;
      } else {
        $.ajax({
          url:
            "/GeoProblemSolving/project/inquiry" +
            "?key=projectId" +
            "&value=" +
            this.subProjectInfo.projectId,
          type: "GET",
          async: false,
          success: data => {
            if (data != "None" && data != "Fail") {
              that.projectInfo = data[0];
              that.$store.commit("setProjectInfo", data[0]);
            } else {
              console.log(data);
            }
          }
        });
      }
    },
    inviteMembers() {
      var that = this;
      for (let i = 0; i < this.inviteList.length; i++) {
        $.ajax({
          url:
            "/GeoProblemSolving/subProject/join" +
            "?subProjectId=" +
            this.$route.params.id +
            "&userId=" +
            this.inviteList[i],
          type: "GET",
          async: false,
          success: data => {
            if (data == "Exist") {
              this.$Message.error("Exist!");
            } else if (data == "None") {
              this.$Message.error("None!");
            } else if (data == "Fail") {
              this.$Message.error("Fail!");
            } else {
              that.axios
                .get(
                  "/GeoProblemSolving/user/inquiry" +
                    "?key=" +
                    "userId" +
                    "&value=" +
                    that.inviteList[i]
                )
                .then(res => {
                  if (res.data != "Fail" && res.data != "None") {
                    that.participants.push(res.data);
                    this.$set(
                      this.subProjectInfo,
                      "members",
                      this.participants
                    );
                    this.$store.commit(
                      "setSubProjectInfo",
                      this.subProjectInfo
                    );
                  }
                })
                .catch(err => {});
              //notice
              let replyNotice = {};
              replyNotice["recipientId"] = this.inviteList[i];
              replyNotice["type"] = "notice";
              replyNotice["content"] = {
                title: "Join subproject",
                description:
                  "You have been invited by " +
                  this.subProjectInfo.managerName +
                  " to join in the subproject: " +
                  this.subProjectInfo.title +
                  " of project: " +
                  this.projectInfo.title +
                  " , and now you are a member in this subproject."
              };
              this.axios
                .post("/GeoProblemSolving/notice/save", replyNotice)
                .then(result => {
                  if (result.data == "Success") {
                    this.$emit("sendNotice", this.inviteList[i]); // 改apply.content.userId
                  } else {
                    this.$Message.error("reply fail.");
                  }
                })
                .catch(err => {
                  this.$Message.error("reply fail.");
                });
            }
          }
        });
      }
      this.init();
    },
    quitSubProject() {
      this.axios
        .get(
          "/GeoProblemSolving/subProject/quit" +
            "?subProjectId=" +
            this.$route.params.id +
            "&userId=" +
            this.$store.getters.userId
        )
        .then(res => {
          if (res.data == "Success") {
            // 这里添加一个通知
            let quitNotice = {};
            quitNotice["recipientId"] = this.subProjectInfo.managerId;
            quitNotice["type"] = "notice";
            quitNotice["content"] = {
              userName: this.$store.getters.userId,
              title: "Member quit notice",
              description:
                "User " +
                this.$store.getters.userName +
                " quit from your project called " +
                sessionStorage.getItem("subProjectName"),
              scope: "subProject",
              approve: "unknow"
            };
            this.axios
              .post("/GeoProblemSolving/notice/save", quitNotice)
              .then(res => {
                if (res.data == "Success") {
                  this.$emit("sendNotice", this.subProjectInfo.managerId);
                } else {
                }
              })
              .catch(err => {
                console.log(err.data);
              });
            let projectId = sessionStorage.getItem("projectId");
            this.$router.push({
              name: "ProjectDetail",
              params: { id: projectId }
            });
          } else {
            this.$Message.error("Fail!");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    //创建任务
    createTaskModalShow() {
      let taskDefult = {
        taskName: "",
        description: "",
        startTime: "",
        endTime: "",
        state: "todo"
      };
      this.$set(this, "taskInfo", taskDefult);
      this.$set(this, "formValidate", taskDefult);
      this.createTaskModal = true;
    },
    createTask(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          let taskForm = {};
          taskForm["taskName"] = this.formValidate.taskName;
          taskForm["description"] = this.formValidate.description;
          taskForm["startTime"] = new Date(this.formValidate.startTime);
          taskForm["endTime"] = new Date(this.formValidate.endTime);
          taskForm["creatorId"] = this.$store.getters.userId;
          taskForm["creatorName"] = this.$store.getters.userName;
          taskForm["managerName"] = this.$store.getters.userName;
          taskForm["importance"] = this.formValidate.importanceCheck ? 1 : 0;
          taskForm["subProjectId"] = this.subProjectInfo.subProjectId;
          taskForm["state"] = "todo";
          taskForm["order"] = this.taskTodo.length;
          this.axios
            .post("/GeoProblemSolving/task/save", taskForm)
            .then(res => {
              if (res.data == "Offline") {
                this.$store.commit("userLogout");
                this.$router.push({ name: "Login" });
              } else if (res.data != "Fail") {
                // 任务更新socket
                this.socketMsg.whoid = this.$store.getters.userId;
                this.socketMsg.who = this.$store.getters.userName;
                this.socketMsg.type = "tasks";
                this.socketMsg.content = "created a new task.";
                this.socketMsg.time = new Date().toLocaleString();
                this.addNewTask(res.data);
                this.createTaskModal = false;
                this.sendMessage(this.socketMsg);
              }
            })
            .catch(err => {});
        } else {
          this.$Message.error("Please enter the necessary information!");
        }
      });
    },
    addNewTask(newTaskObject) {
      this.taskTodo.push(newTaskObject);
    },
    changeImportance(task) {
      let taskForm = new URLSearchParams();
      taskForm.append("taskId", task.taskId);
      taskForm.append("importance", task.importance);
      this.axios
        .post("/GeoProblemSolving/task/update", taskForm)
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data != "None" && res.data != "Fail") {
            this.socketMsg.whoid = this.$store.getters.userId;
            this.socketMsg.who = this.$store.getters.userName;
            this.socketMsg.type = "tasks";
            this.socketMsg.content = "Changed the importance of one task.";
            this.socketMsg.time = new Date().toLocaleString();
            this.sendMessage(this.socketMsg);
          } else {
            this.$Message.error("Fail!");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    //打开task编辑器
    editOneTask(index, taskList) {
      this.axios
        .get(
          "/GeoProblemSolving/task/inquiry?" +
            "key=taskId" +
            "&value=" +
            taskList[index]["taskId"]
        )
        .then(res => {
          if (res.data != "Fail") {
            let taskInfoRes = res.data[0];
            taskInfoRes.startTime = new Date(taskInfoRes.startTime);
            taskInfoRes.endTime = new Date(taskInfoRes.endTime);
            taskInfoRes.importanceCheck = taskInfoRes.importance ? true : false;
            this.$set(this, "formValidate", taskInfoRes);
            this.editTaskModal = true;
          } else {
            this.$Message.error("Fail!");
          }
        })
        .catch(err => {
          this.$Message.error("Fail!");
        });
    },
    showTask(index, taskList) {
      this.axios
        .get(
          "/GeoProblemSolving/task/inquiry?" +
            "key=taskId" +
            "&value=" +
            taskList[index]["taskId"]
        )
        .then(res => {
          if (res.data != "Fail") {
            let taskInfoRes = res.data[0];
            taskInfoRes.startTime = new Date(taskInfoRes.startTime);
            taskInfoRes.endTime = new Date(taskInfoRes.endTime);
            this.$set(this, "taskInfo", taskInfoRes);
            this.taskDetailModal = true;
          } else {
            this.$Message.error("Fail!");
          }
        })
        .catch(err => {
          this.$Message.error("Fail!");
        });
    },
    updateTaskList(taskObject) {
      taskObject.importanceCheck = taskObject.importance ? 1 : 0;
      switch (taskObject.state) {
        case "todo": {
          let taskList = this.taskTodo;
          for (var i = 0; i < taskList.length; i++) {
            if (taskList[i].taskId == taskObject.taskId) {
              this.$set(this.taskTodo, i, taskObject);
              break;
            }
          }
          break;
        }
        case "doing": {
          let taskList = this.taskDoing;
          for (var i = 0; i < taskList.length; i++) {
            if (taskList[i].taskId == taskObject.taskId) {
              this.$set(this.taskDoing, i, taskObject);
              break;
            }
          }
          break;
        }
        case "done": {
          let taskList = this.taskDone;
          for (var i = 0; i < taskList.length; i++) {
            if (taskList[i].taskId == taskObject.taskId) {
              this.$set(this.taskDone, i, taskObject);
              break;
            }
          }
          break;
        }
      }
    },
    //更新某个task
    updateTask(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          let taskForm = new URLSearchParams();
          taskForm.append("taskId", this.formValidate.taskId);
          taskForm.append("taskName", this.formValidate.taskName);
          taskForm.append("description", this.formValidate.description);
          taskForm.append("startTime", new Date(this.formValidate.startTime));
          taskForm.append("endTime", new Date(this.formValidate.endTime));
          let importance = this.formValidate.importanceCheck ? 1 : 0;
          taskForm.append("importance", importance);
          this.axios
            .post("/GeoProblemSolving/task/update", taskForm)
            .then(res => {
              if (res.data == "Offline") {
                this.$store.commit("userLogout");
                this.$router.push({ name: "Login" });
              } else if (res.data != "None" && res.data != "Fail") {
                this.updateTaskList(res.data); // 只更新单个任务
                this.socketMsg.whoid = this.$store.getters.userId;
                this.socketMsg.who = this.$store.getters.userName;
                this.socketMsg.type = "tasks";
                this.socketMsg.content = "edited a new task.";
                this.socketMsg.time = new Date().toLocaleString();
                this.editTaskModal = false;
                this.sendMessage(this.socketMsg);
              } else {
                this.$Message.error("Fail!");
              }
            })
            .catch(err => {
              console.log(err.data);
            });
        } else {
          this.$Message.error("Please enter the necessary information!");
        }
      });
    },
    //查询task
    inquiryTask() {
      this.inquiryTodoTask();
      this.inquiryDoingTask();
      this.inquiryDoneTask();
    },
    inquiryTodoTask() {
      this.axios
        .get(
          "/GeoProblemSolving/task/inquiryTodo?" +
            "subProjectId=" +
            this.subProjectInfo.subProjectId
        )
        .then(res => {
          if (res.data != "None" && res.data != "Fail") {
            this.$set(this, "taskTodo", res.data);
          } else {
            this.$Message.error("Fail!");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    inquiryDoingTask() {
      this.axios
        .get(
          "/GeoProblemSolving/task/inquiryDoing?" +
            "subProjectId=" +
            this.subProjectInfo.subProjectId
        )
        .then(res => {
          if (res.data != "None" && res.data != "Fail") {
            this.$set(this, "taskDoing", res.data);
          } else {
            this.$Message.error("Fail!");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    inquiryDoneTask() {
      this.axios
        .get(
          "/GeoProblemSolving/task/inquiryDone?" +
            "subProjectId=" +
            this.subProjectInfo.subProjectId
        )
        .then(res => {
          if (res.data != "None" && res.data != "Fail") {
            this.$set(this, "taskDone", res.data);
          } else {
            this.$Message.error("Fail!");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    setMoveCount() {
      this.MoveCount = 2;
    },
    addMoveTask(taskList, type) {
      this.MoveCount--;
      this.taskOrderUpdate(taskList, type);
    },
    removeMoveTask(taskList, type) {
      this.MoveCount--;
      this.taskOrderUpdate(taskList, type);
    },
    updateMoveTask(taskList, type) {
      this.MoveCount -= 2;
      this.taskOrderUpdate(taskList, type);
    },
    taskOrderUpdate(taskList, type) {
      if (this.userRole != "Visitor") {
        let thisUserName = this.$store.getters.userName;
        let stateChangeIndex = 0;
        let count = taskList.length;
        for (let i = 0; i < taskList.length; i++) {
          let thisTask = taskList[i];
          if (thisTask.order != i || thisTask.state != type) {
            if (thisTask.state != type) {
              stateChangeIndex = i;
              let taskUpdateObj = new URLSearchParams();
              taskUpdateObj.append("taskId", taskList[i]["taskId"]);
              taskUpdateObj.append("order", i);
              taskUpdateObj.append("state", type);
              taskUpdateObj.append("managerName", thisUserName);
              this.axios
                .post("/GeoProblemSolving/task/update", taskUpdateObj)
                .then(res => {
                  count--;
                  if (res.data == "Offline") {
                    this.$store.commit("userLogout");
                    this.$router.push({ name: "Login" });
                  } else if (res.data != "Fail") {
                    //更新数组
                    taskList[stateChangeIndex].managerName = thisUserName;
                    if (this.MoveCount == 0 && count == 1) {
                      this.endMove();
                    }
                  }
                })
                .catch(err => {
                  console.log(err.data);
                });
            } else {
              let taskUpdateObj = new URLSearchParams();
              taskUpdateObj.append("taskId", taskList[i]["taskId"]);
              taskUpdateObj.append("order", i);
              taskUpdateObj.append("state", type);
              this.axios
                .post("/GeoProblemSolving/task/update", taskUpdateObj)
                .then(res => {
                  count--;
                  if (res.data == "Offline") {
                    this.$store.commit("userLogout");
                    this.$router.push({ name: "Login" });
                  } else if (res.data != "Fail") {
                    if (this.MoveCount == 0 && count == 1) {
                      this.endMove();
                    }
                  }
                })
                .catch(err => {
                  console.log(err.data);
                });
            }
          }
        }
      }
    },
    endMove() {
      // 任务更新socket
      this.socketMsg.whoid = this.$store.getters.userId;
      this.socketMsg.who = this.$store.getters.userName;
      this.socketMsg.type = "tasks";
      this.socketMsg.content = "changed the task schedule.";
      this.socketMsg.time = new Date().toLocaleString();
      this.sendMessage(this.socketMsg);
    },
    taskRemoveAssure(index, taskList) {
      this.taskDeleteModal = true;
      this.selectTaskIndex = index;
      this.taskList = taskList;
    },
    taskRemove() {
      this.axios
        .get(
          "/GeoProblemSolving/task/delete" +
            "?taskId=" +
            this.taskList[this.selectTaskIndex]["taskId"]
        )
        .then(res => {
          if (res.data == "Success") {
            this.taskList.splice(this.selectTaskIndex, 1);
            // 任务更新socket
            this.socketMsg.whoid = this.$store.getters.userId;
            this.socketMsg.who = this.$store.getters.userName;
            this.socketMsg.type = "tasks";
            this.socketMsg.content = "removed a task.";
            this.socketMsg.time = new Date().toLocaleString();
            this.sendMessage(this.socketMsg);
          } else {
            this.$Message.error("Fail!");
          }
        })
        .catch(err => {
          this.$Message.error("Fail!");
        });
    },
    gotoPersonalSpace(id) {
      if (id == this.$store.getters.userId) {
        this.$router.push({ name: "PersonalPage" });
      } else {
        this.$router.push({ name: "MemberDetailPage", params: { id: id } });
      }
    },
    giveDeleteProperty(index) {
      if (
        this.subProjectInfo.managerId == this.$store.getters.userId &&
        index != 0
      ) {
        return true;
      } else {
        return false;
      }
    },
    removeMember(uid, uname) {
      // 获取到userId
      this.axios
        .get(
          "/GeoProblemSolving/subProject/quit" +
            "?subProjectId=" +
            this.$route.params.id +
            "&userId=" +
            uid
        )
        .then(res => {
          if (res.data == "Success") {
            var members = this.participants;
            for (var i = 0; i < members.length; i++) {
              if (members[i].userId == uid) {
                this.participants.splice(i, 1);
                break;
              }
            }
            this.$set(this.subProjectInfo, "members", this.participants);
            this.$store.commit("setSubProjectInfo", this.subProjectInfo);
            this.$Message.info("Remove member successfully");
            //notice
            let projectName = sessionStorage.getItem("projectName");
            let removeNotice = {};
            removeNotice["recipientId"] = uid;
            removeNotice["type"] = "notice";
            removeNotice["content"] = {
              title: "Remove notification",
              description:
                "You have been expeled from sub project called " +
                this.subProjectInfo.title +
                ", which belongs to project " +
                projectName +
                "."
            };
            this.axios
              .post("/GeoProblemSolving/notice/save", removeNotice)
              .then(res => {
                if (res.data == "Success") {
                  this.$emit("sendNotice", uid);
                }
              })
              .catch(err => {
                console.log("申请失败的原因是：" + err.data);
              });
          } else {
            this.$Message.error("Fail!");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    currentTabChanged(name) {
      if (this.oldTabPaneState !== name) {
        this.closeStepSocket();

        if (name == "task") {
          this.openStepSocket();
          this.$refs.folderTreeEle.closePanel();
        }
      }
    },
    gotoWorkingPanel() {
      this.$router.push(`./stepCreation`);
    }
  }
};
</script>
