<style scoped>
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

.taskFormItem {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}
.taskFormItem span {
  text-align: center;
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
</style>
<template>
  <div>
    <Row>
      <Col id="taskPage" span="21" offset="2" style="margin-top:20px">
        <div :style="{height:contentHeight+'px'}">            
          <gantt-elastic :tasks="ganttTasks" :options="ganttOptions">
          </gantt-elastic>
        </div>
      </Col>
    </Row>
  </div>
</template>
<script>
import dayjs from "dayjs";
import GanttElastic from "gantt-elastic";
export default {
  components: {
    ganttElastic: GanttElastic,
  },
  props: ["subProjectInfo"],
  data() {
    return {
      scrollOps: {
        bar: {
          background: "lightgrey"
        }
      },
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
      contentHeight: "",
      userRole: "",
      // gantt 图
      ganttTasks: null,
      ganttOptions: null
    };
  },
  created() {
    this.init();
      this.initGantt();
  },
  mounted() {
    // this.getProjectInfo();
    window.addEventListener("resize", this.initSize);
  },
  beforeRouteLeave(to, from, next) {
    this.removeTimer();
    next();
  },
  beforeDestroy: function() {
    window.removeEventListener("resize", this.initSize);
  },
  methods: {
    initSize() {
      this.contentHeight = window.innerHeight - 200;
    },
    //初始化函数，作用是控制侧边栏的高度，设置右边通知栏弹出时候的距顶高度以及延迟的时间
    init() {
      this.initSize();
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
    // memberIdentity(members) {
    //   for (let i = 0; i < members.length; i++) {
    //     if (members[i].userId === this.$store.getters.userId) {
    //       this.subProjectInfo.isMember = true;
    //       break;
    //     }
    //   }
    // },
    initGantt() {
      this.ganttTasks = [
        {
          id: 1,
          label: "Make some noise",
          user:
            '<a href="https://www.google.com/search?q=John+Doe" target="_blank" style="color:#0077c0;">John Doe</a>',
          start: this.getDate(-24 * 5),
          duration: 15 * 24 * 60 * 60 * 1000,
          progress: 85,
          type: "project",
          style: {
            base: {
              fill: "#1EBC61"
            }
          }
          //collapsed: true,
        },
        {
          id: 2,
          label: "With great power comes great responsibility",
          user:
            '<a href="https://www.google.com/search?q=Peter+Parker" target="_blank" style="color:#0077c0;">Peter Parker</a>',
          parentId: 1,
          start: this.getDate(-24 * 4),
          duration: 4 * 24 * 60 * 60 * 1000,
          progress: 50,
          type: "milestone",
          collapsed: true,
          style: {
            base: {
              fill: "#1EBC61",
              stroke: "#0EAC51"
            }
            /*'tree-row-bar': {
              fill: '#1EBC61',
              stroke: '#0EAC51'
            },
            'tree-row-bar-polygon': {
              stroke: '#0EAC51'
            }*/
          }
        },
        {
          id: 3,
          label: "Courage is being scared to death, but saddling up anyway.",
          user:
            '<a href="https://www.google.com/search?q=John+Wayne" target="_blank" style="color:#0077c0;">John Wayne</a>',
          parentId: 2,
          start: this.getDate(-24 * 3),
          duration: 2 * 24 * 60 * 60 * 1000,
          progress: 100,
          type: "task"
        },
        {
          id: 4,
          label: "Put that toy AWAY!",
          user:
            '<a href="https://www.google.com/search?q=Clark+Kent" target="_blank" style="color:#0077c0;">Clark Kent</a>',
          start: this.getDate(-24 * 2),
          duration: 2 * 24 * 60 * 60 * 1000,
          progress: 50,
          type: "task",
          dependentOn: [3]
        }
      ];

      this.ganttOptions = {
        maxRows: 100,
        maxHeight: 300,
        // title: {
        //   label: "Your project title as html (link or whatever...)",
        //   html: false
        // },
        row: {
          height: 24
        },
        calendar: {
          hour: {
            display: false
          }
        },
        chart: {
          progress: {
            bar: false
          },
          expander: {
            display: true
          }
        },
        taskList: {
          expander: {
            straight: false
          },
          columns: [
            {
              id: 1,
              label: "ID",
              value: "id",
              width: 40
            },
            {
              id: 2,
              label: "Description",
              value: "label",
              width: 200,
              expander: true,
              html: true,
              events: {
                click({ data, column }) {
                  alert("description clicked!\n" + data.label);
                }
              }
            },
            {
              id: 3,
              label: "Assigned to",
              value: "user",
              width: 130,
              html: true
            },
            {
              id: 3,
              label: "Start",
              value: task => dayjs(task.start).format("YYYY-MM-DD"),
              width: 78
            },
            {
              id: 4,
              label: "Type",
              value: "type",
              width: 68
            },
            {
              id: 5,
              label: "%",
              value: "progress",
              width: 35,
              style: {
                "task-list-header-label": {
                  "text-align": "center",
                  width: "100%"
                },
                "task-list-item-value-container": {
                  "text-align": "center",
                  width: "100%"
                }
              }
            }
          ]
        }
      };
    },
    getDate(hours) {
      const currentDate = new Date();
      const currentYear = currentDate.getFullYear();
      const currentMonth = currentDate.getMonth();
      const currentDay = currentDate.getDate();
      const timeStamp = new Date(
        currentYear,
        currentMonth,
        currentDay,
        0,
        0,
        0
      ).getTime();
      return new Date(timeStamp + hours * 60 * 60 * 1000).getTime();
    },
    closeModuleSocket() {
      if (this.subprojectSocket != null) {
        this.removeTimer();
        this.subprojectSocket.close();
      }
    },
    openModuleSocket() {
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
      console.log("ModuleSocket连接成功！");
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
      console.log("ModuleSocket连接断开！");
    },
    onError(e) {
      this.removeTimer();
      console.log("ModuleSocket连接错误！");
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
    ok() {
      this.$Message.info("Clicked ok");
    },
    cancel() {}

    // gotoPersonalSpace(id) {
    //   if (id == this.$store.getters.userId) {
    //     this.$router.push({ name: "PersonalPage" });
    //   } else {
    //     this.$router.push({ name: "MemberDetailPage", params: { id: id } });
    //   }
    // }
  }
};
</script>
