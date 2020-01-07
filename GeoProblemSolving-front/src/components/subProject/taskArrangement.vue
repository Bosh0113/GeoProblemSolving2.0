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
</style>
<template>
  <div>
    <Row>
      <Col span="4" offset="20" style="margin-top:8px">
      <span id="todoPanel" style="cursor:pointer;color:#57a3f3" @click="switch2Manager">Task manager</span>
      <Divider type="vertical" />
      <span id="ganttPanel" style="cursor:pointer" @click="switch2Gantt">Gantt chart</span>
      </Col>
      <Col id="taskPage" span="23" offset="1">
      <div id="taskContainer" :style="{height:contentHeight+'px'}">
        <template v-if="!chartSwitch">
          <Row type="flex" justify="space-around">
            <Col span="7">
            <Card :padding="0" :border="false" dis-hover>
              <h3 slot="title">Todo</h3>
              <Button slot="extra" type="default" class="createTaskBtn" style="margin-top:-8px"
                @click="createTaskModalShow()">Add</Button>
              <vue-scroll :ops="ops" :style="{height:contentHeight-80+'px'}">
                <draggable :disabled="taskItemDraggable()" class="taskList" element="ul" :options="{group:'task'}"
                  v-model="taskTodo" :style="{height:contentHeight-80+'px'}" @start="setMoveCount()"
                  @update="updateMoveTask(taskTodo,'todo')" @add="addMoveTask(taskTodo,'todo')"
                  @remove="removeMoveTask(taskTodo,'todo')">
                  <Card v-for="(item,index) in taskTodo" :key="index" :padding="3" style="margin:5px">
                    <div>
                      <span style="float:left;padding:0 2.5px">
                        <Icon type="ios-list" color="gray" :size="20" />
                      </span>
                      <span style="padding:5px">
                        <strong style="color:#57a3f3" class="taskName" :title="item.taskName">{{item.taskName}}</strong>
                      </span>
                      <div style="float:right">
                        <Rate :disabled="!(isManager()||isOwner(item))" v-model="item.importance" :count="1" clearable
                          title="Importance" @on-change="changeImportance(item)" />
                        <span title="Edit" v-if="isManager()||isOwner(item)">
                          <Icon type="ios-create" color="gray" :size="20" style="cursor:pointer"
                            @click="editOneTask(index, taskTodo)" />
                        </span>
                        <span v-if="isManager()||isOwner(item)"
                          style="margin-left:5px;margin-right:3px;cursor: pointer;color:gray;" title="Delete"
                          @click="taskRemoveAssure(index,taskTodo)">
                          <Icon type="ios-trash" :size="20" color="gray" />
                        </span>
                      </div>
                      <p style="word-break:break-word;padding:5px;cursor:pointer" @click="showTask(index, taskTodo)">
                        {{item.description}}</p>
                      <div style="display:flex;justify-content:flex-end">
                        <Tag color="default" style="cursor:default" title="Creator">{{item.creatorName}}</Tag>
                      </div>
                    </div>
                  </Card>
                  <Spin size="large" fix v-if="todoLoading"></Spin>
                </draggable>
              </vue-scroll>
            </Card>
            </Col>
            <Col span="7">
            <Card :padding="0" :border="false" dis-hover>
              <h3 slot="title">Doing</h3>
              <vue-scroll :ops="ops" :style="{height:contentHeight-80+'px'}">
                <draggable :disabled="taskItemDraggable()" class="taskList" element="ul" :options="{group:'task'}"
                  v-model="taskDoing" :style="{height:contentHeight-80+'px'}" @start="setMoveCount()"
                  @update="updateMoveTask(taskDoing,'doing')" @add="addMoveTask(taskDoing,'doing')"
                  @remove="removeMoveTask(taskDoing,'doing')">
                  <Card v-for="(item,index)  in taskDoing" :key="index" :padding="3" style="margin:5px">
                    <div>
                      <span style="float:left;padding:0 2.5px">
                        <Icon type="ios-information-circle-outline" color="gray" :size="20" />
                      </span>
                      <span style="padding:5px">
                        <strong style="color:#57a3f3" class="taskName" :title="item.taskName">{{item.taskName}}</strong>
                      </span>
                      <div style="float:right" v-show="userRole != 'Visitor'">
                        <Rate :disabled="!(isManager()||isOwner(item))" v-model="item.importance" :count="1" clearable
                          title="Importance" @on-change="changeImportance(item)" />
                        <span title="Edit" v-if="isManager()||isOwner(item)">
                          <Icon type="ios-create" color="gray" :size="20" style="cursor:pointer"
                            @click="editOneTask(index,taskDoing)" />
                        </span>
                        <span v-if="isManager()||isOwner(item)"
                          style="margin-left:5px;margin-right:3px;cursor: pointer;color:gray;" title="Delete"
                          @click="taskRemoveAssure(index,taskDoing)">
                          <Icon type="ios-trash" :size="20" color="gray" />
                        </span>
                      </div>
                    </div>
                    <p style="word-break:break-word;padding:5px;cursor:pointer" @click="showTask(index,taskDoing)">
                      {{item.description}}</p>
                    <div style="display:flex;justify-content:flex-end">
                      <Tag color="default" style="cursor:default" title="Executor">{{item.managerName}}</Tag>
                    </div>
                  </Card>
                  <Spin size="large" fix v-if="doingLoading"></Spin>
                </draggable>
              </vue-scroll>
            </Card>
            </Col>
            <Col span="7">
            <Card :padding="0" :border="false" dis-hover>
              <h3 slot="title">Done</h3>
              <vue-scroll :ops="ops" :style="{height:contentHeight-80+'px'}">
                <draggable :disabled="taskItemDraggable()" class="taskList" element="ul" :options="{group:'task'}"
                  v-model="taskDone" :style="{height:contentHeight-80+'px'}" @start="setMoveCount()"
                  @update="updateMoveTask(taskDone,'done')" @add="addMoveTask(taskDone,'done')"
                  @remove="removeMoveTask(taskDone,'done')">
                  <Card v-for="(item,index) in taskDone" :key="index" :padding="3" style="margin:5px">
                    <div>
                      <span style="float:left;padding:0 2.5px">
                        <Icon type="md-checkmark-circle-outline" />
                      </span>
                      <span style="padding:5px">
                        <strong style="color:#57a3f3" class="taskName" :title="item.taskName">{{item.taskName}}</strong>
                      </span>
                      <div style="float:right" v-show="userRole != 'Visitor'">
                        <Rate :disabled="!(isManager()||isOwner(item))" v-model="item.importance" :count="1" clearable
                          title="Importance" @on-change="changeImportance(item)" />
                        <span title="Edit" v-if="isManager()||isOwner(item)">
                          <Icon type="ios-create" color="gray" :size="20" style="cursor:pointer"
                            @click="editOneTask(index,taskDone)" />
                        </span>
                        <span v-if="isManager()||isOwner(item)"
                          style="margin-left:5px;margin-right:3px;cursor: pointer;color:gray;" title="Delete"
                          @click="taskRemoveAssure(index,taskDone)">
                          <Icon type="ios-trash" :size="20" color="gray" />
                        </span>
                      </div>
                      <p style="word-break:break-word;padding:5px;cursor:pointer" @click="showTask(index,taskDone)">
                        {{item.description}}</p>
                      <div style="display:flex;justify-content:flex-end">
                        <Tag color="default" style="cursor:default" title="Executor">{{item.managerName}}</Tag>
                      </div>
                    </div>
                  </Card>
                  <Spin size="large" fix v-if="doneLoading"></Spin>
                </draggable>
              </vue-scroll>
            </Card>
            </Col>
          </Row>
        </template>
        <div v-show="chartSwitch">
          <vue-scroll :ops="scrollOps" :style="{height:contentHeight - 15 +'px'}">
            <gantt-elastic :tasks="ganttTasks" :options="ganttOptions"></gantt-elastic>
          </vue-scroll>
        </div>
      </div>
      </Col>
    </Row>
    <Modal v-model="taskDeleteModal" title="Delete Task" @on-ok="taskRemove()" ok-text="Assure" cancel-text="Cancel">
      <p>Do yout want to delete this task?</p>
    </Modal>
    <Modal v-model="createTaskModal" title="Create Task" width="800px" :closable="false">
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100" style="margin-left: 30px">
        <FormItem label="Name" prop="taskName">
          <Input v-model="formValidate.taskName" placeholder="Fill in the name of task..." style="width: 560px" />
        </FormItem>
        <FormItem label="Description" prop="description">
          <Input v-model="formValidate.description" type="textarea" placeholder="Fill in the description of task..."
            style="width: 560px" :autosize="{minRows: 6}" />
        </FormItem>
        <FormItem label="Start time" prop="startTime">
          <DatePicker v-model="formValidate.startTime" type="datetime" format="yyyy-MM-dd HH:mm:ss"
            placeholder="Select start time..." style="width: 560px"></DatePicker>
        </FormItem>
        <FormItem label="End time" prop="endTime">
          <DatePicker v-model="formValidate.endTime" type="datetime" format="yyyy-MM-dd HH:mm:ss"
            placeholder="Select end time..." style="width: 560px"></DatePicker>
        </FormItem>
        <FormItem label prop="importance">
          <Checkbox v-model="formValidate.importanceCheck">Important Task</Checkbox>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="text" @click="createTaskModal=false">Cancel</Button>
        <Button type="primary" @click="createTask('formValidate')">Create</Button>
      </div>
    </Modal>
    <Modal v-model="editTaskModal" title="Edit Task" @on-ok="updateTask('formValidate')" ok-text="Ok"
      cancel-text="Cancel" width="800px" :closable="false">
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="100" style="margin-left: 30px">
        <FormItem label="Name" prop="taskName">
          <Input v-model="formValidate.taskName" placeholder="Fill in the name of task..." style="width: 560px" />
        </FormItem>
        <FormItem label="Description" prop="description">
          <Input v-model="formValidate.description" type="textarea" placeholder="Fill in the description of task..."
            style="width:560px" :autosize="{minRows: 6}" />
        </FormItem>
        <FormItem label="Start time" prop="startTime">
          <DatePicker v-model="formValidate.startTime" type="datetime" format="yyyy-MM-dd HH:mm:ss"
            placeholder="Select start time..." style="width: 560px"></DatePicker>
        </FormItem>
        <FormItem label="End time" prop="endTime">
          <DatePicker v-model="formValidate.endTime" type="datetime" format="yyyy-MM-dd HH:mm:ss"
            placeholder="Select end time..." style="width: 560px"></DatePicker>
        </FormItem>
        <FormItem label prop="importance">
          <Checkbox v-model="formValidate.importanceCheck">Important Task</Checkbox>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="text" @click="editTaskModal=false">Cancel</Button>
        <Button type="primary" @click="updateTask('formValidate')">Update</Button>
      </div>
    </Modal>
    <Modal v-model="taskDetailModal" title="Task Detail" width="800px">
      <div class="taskFormItem">
        <span style="width:15%">Task name</span>
        <Input style="width: 600px" :placeholder="this.taskPlaceHolder.name" v-model="taskInfo.taskName" readonly />
      </div>
      <div class="taskFormItem">
        <span style="width:15%">Description</span>
        <Input style="width: 600px" :placeholder="this.taskPlaceHolder.description" type="textarea" :rows="4"
          v-model="taskInfo.description" :autosize="{minRows: 6}" readonly />
      </div>
      <div class="taskFormItem">
        <span style="width:15%">Start time</span>
        <DatePicker type="datetime" format="yyyy-MM-dd HH:mm:ss" :placeholder="this.taskPlaceHolder.startTime"
          style="width: 600px" v-model="taskInfo.startTime" readonly></DatePicker>
      </div>
      <div class="taskFormItem" style="margin-bottom:10px">
        <span style="width:15%">End time</span>
        <DatePicker type="datetime" format="yyyy-MM-dd HH:mm:ss" :placeholder="this.taskPlaceHolder.endTime"
          style="width: 600px" v-model="taskInfo.endTime" readonly></DatePicker>
      </div>
      <div slot="footer"></div>
    </Modal>
  </div>
</template>
<script>
import dayjs from "dayjs";
import draggable from "vuedraggable";
import GanttElastic from "gantt-elastic";
export default {
  components: {
    draggable,
    dayjs,
    ganttElastic: GanttElastic
  },
  props: ["subProjectInfo", "userRole"], //子项目管理者Manager，项目管理者PManager,成员Member
  data() {
    return {
      todoLoading: true,
      doingLoading: true,
      doneLoading: true,
      ops: {
        bar: {
          background: "#808695"
        }
      },
      scrollOps: {
        bar: {
          background: "#808080",
          keepShow: true,
          size: "8px"
        },
        rail: {
          background: "#d7d7d7",
          opacity: 0.8,
          size: "10px"
        }
      },
      // 后台获取的subproject下的task列表
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
      chartSwitch: false, // 切换至甘特图
      // gantt 图
      ganttTasks: [
        {
          id: 1,
          name: "",
          user: "",
          start: new Date(),
          end: new Date(),
          progress: 0,
          type: "task"
        }
      ],
      ganttOptions: {
        maxRows: 100,
        maxHeight: this.contentHeight - 60,
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
              label: "Name",
              value: "name",
              width: 200,
              expander: true
            },
            {
              id: 3,
              label: "Assigned to",
              value: "user",
              width: 100
            },
            {
              id: 4,
              label: "Start",
              value: task => dayjs(task.start).format("YYYY-MM-DD"),
              width: 90
            },
            {
              id: 5,
              label: "End",
              value: task => dayjs(task.end).format("YYYY-MM-DD"),
              width: 90
            }
          ]
        }
      }
    };
  },
  created() {
    this.initSize();
  },
  mounted() {
    this.inquiryTask();
    window.addEventListener("resize", this.initSize);
  },
  beforeRouteLeave(to, from, next) {
    next();
  },
  beforeDestroy: function () {
    window.removeEventListener("resize", this.initSize);
  },
  methods: {
    initSize() {
      this.contentHeight = window.innerHeight - 210;
    },
    cancel() { },
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
                this.addNewTask(res.data);
                this.createTaskModal = false;
              }
            })
            .catch(err => { });
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
            this.$Message.info("Changed the importance of one task.");
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
                this.editTaskModal = false;
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
          this.todoLoading = false;
          if (res.data != "None" && res.data != "Fail") {
            this.$set(this, "taskTodo", res.data);
          } else {
            this.$Message.error("Fail!");
          }
        })
        .catch(err => {
          this.todoLoading = false;
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
          this.doingLoading = false;
          if (res.data != "None" && res.data != "Fail") {
            this.$set(this, "taskDoing", res.data);
          } else {
            this.$Message.error("Fail!");
          }
        })
        .catch(err => {
          this.doingLoading = false;
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
          this.doneLoading = false;
          if (res.data != "None" && res.data != "Fail") {
            this.$set(this, "taskDone", res.data);
          } else {
            this.$Message.error("Fail!");
          }
        })
        .catch(err => {
          this.doneLoading = false;
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
          } else {
            this.$Message.error("Fail!");
          }
        })
        .catch(err => {
          this.$Message.error("Fail!");
        });
    },
    isManager() {
      if (this.userRole == 'Manager' || this.userRole == 'PManager') {
        return true;
      }
      else {
        return false;
      }
    },
    isOwner(task) {
      if (task.creatorId == this.$store.getters.userId) {
        return true;
      }
      else {
        return false;
      }
    },
    taskItemDraggable() {
      // if(this.userRole=='Manager'||this.userRole=='PManager'||this.userRole=='Member'){
      return false;
      // }
      // else{
      //   return true;
      // }
    },
    initGantt() {
      this.ganttTasks = [];
      for (let i = 0; i < this.taskDoing.length; i++) {
        let gantttask = {
          id: i + 1,
          name: this.taskDoing[i].taskName,
          user: this.taskDoing[i].managerName,
          start: new Date(this.taskDoing[i].startTime),
          end: new Date(this.taskDoing[i].endTime),
          type: "task",
          progress: 100,
          style: {
            base: {
              fill: "#1EBC61"
            }
          }
        };
        this.ganttTasks.push(gantttask);
      }
      for (let i = 0; i < this.taskTodo.length; i++) {
        let gantttask = {
          id: this.taskDoing.length + i + 1,
          name: this.taskTodo[i].taskName,
          user: this.taskTodo[i].creatorName,
          start: new Date(this.taskTodo[i].startTime),
          end: new Date(this.taskTodo[i].endTime),
          type: "task",
          progress: 100,
          style: {
            base: {
              fill: "#F90"
            }
          }
        };
        this.ganttTasks.push(gantttask);
      }
      for (let i = 0; i < this.taskDone.length; i++) {
        let gantttask = {
          id: this.taskDoing.length + this.taskTodo.length + i + 1,
          name: this.taskDone[i].taskName,
          user: this.taskDone[i].creatorName,
          start: new Date(this.taskDone[i].startTime),
          end: new Date(this.taskDone[i].endTime),
          type: "task",
          progress: 100,
          style: {
            base: {
              fill: "#57A3F3"
            }
          }
        };
        this.ganttTasks.push(gantttask);
      }
    },
    switch2Manager() {
      this.chartSwitch = false;
      $("#todoPanel").css("color", "#57a3f3");
      $("#ganttPanel").css("color", "black");
    },
    switch2Gantt() {
      $("#todoPanel").css("color", "black");
      $("#ganttPanel").css("color", "#57a3f3");
      this.chartSwitch = true;

      this.initGantt();
    }
  }
};
</script>
