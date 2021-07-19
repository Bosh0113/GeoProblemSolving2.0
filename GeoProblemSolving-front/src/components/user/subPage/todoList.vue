<template>
  <div>
    <div id="header" style="margin-top: 2%;">
      <h1 style="text-align:center;">Todo List</h1>
      <h3 style="margin:1%;text-align:center;">You can manage your todo list here</h3>
    </div>
    <!--  Fiter  -->
    <Row>
      <Col span="22" offset="1" >
        <Card dis-hover>
          <p slot="title" style="height: 30px;">
            <CheckboxGroup v-model="selectedTaskType" style="margin-left: 3.5%;margin-top: 5px;">
              <Checkbox label="Doing">
                <span>Doing</span>
                <span class="badge">{{doingList.length}}</span>
              </Checkbox>
              <Checkbox label="Done">
                <span>Done</span>
                <span class="badge">{{doneList.length}}</span>
              </Checkbox>
              <!--            <Checkbox label="Assigned">-->
              <!--              <span>Assigned to you</span>-->
              <!--              <span class="badge">10</span>-->
              <!--            </Checkbox>-->
              <Checkbox label="Importance" :disabled="true">
                 <span>Importance</span>
                 <span class="badge">{{importanceNum}}</span>
              </Checkbox>
            </CheckboxGroup>
          </p>
          <div slot="extra">
            <!--         新建任务   -->
              <div style="position: relative;">
                <DatePicker
                  type="date"
                  style="position: absolute;right: 5px;  top: 2px; z-index: 3"
                  class="customDatePicker custom"
                  title="select endTime"
                  @on-change="handleChange"
                >
                </DatePicker>
                <Input
                  v-model="newTask.content"
                  prefix="ios-radio-button-off"
                  placeholder="Add Todo"
                  size="large"
                  @on-enter="addTask()"
                  style="display: inline-block;"
                  class="customIcon"
                />
                <!-- 拓展input框长度，借助slot="extra"的增长实现，通过透明的icon图标，增加 extra长度-->
                <Icon type="ios-trash-outline" color="transparent"
                      size="25"
                /><Icon type="ios-trash-outline" color="transparent"
                      size="25"
                /><Icon type="ios-trash-outline" color="transparent"
                      size="25"
                /><Icon type="ios-trash-outline" color="transparent"
                      size="25"
                /><Icon type="ios-trash-outline" color="transparent"
                      size="25"
                />
            </div>
          </div>


          <!--        未完成      -->
          <div>
            <Card
              dis-hover
              v-if="selectedTaskType.includes('Doing')"
            >
              <p slot="title">Doing</p>
              <div :style="{height: contentHeight-contentHeightCompute+'px'}">
                <vue-scroll :ops="ops">
                  <div
                    v-for="(item, doingIndex) in doingList"
                    :key="doingIndex"
                  >
                    <Card
                      class="customIcon"
                    >
                      <Icon type="ios-radio-button-off"
                            size="20"
                            style="cursor:pointer;"
                            title="Change State"
                            @click="changeState(item, doingIndex)"
                      />
                      <Icon type="ios-close" size="31"
                            style="float: right;color: #ff7800;cursor: pointer; line-height: .8"
                            title="Delete"
                            @click="delTask(item.ptId, doingIndex, 'doing')"
                      />
                      <Icon v-if="item.importance == 0"
                            size="20" type="ios-star-outline"
                            style="float: right;cursor: pointer;margin-right: 20px"
                            @click="changeImportance(item, doingIndex, 'doing')"
                      />
                      <Icon v-else size="20"
                            type="ios-star"
                            style="float: right;cursor:pointer;margin-right: 20px"
                            @click="changeImportance(item, doingIndex, 'doing')"
                      />
                      <label
                        style="margin-left: 10px;"
                      >{{item.content}}</label>
                    </Card>
                  </div>
                </vue-scroll>
              </div>

            </Card>
          </div>
          <!--        已完成      -->
          <div>
            <Card
              dis-hover
              v-if="selectedTaskType.includes('Done')"
            >
              <p slot="title">Done</p>
              <!--            显示内容                -->
              <div :style="{height: contentHeight-contentHeightCompute+'px'}">
                <vue-scroll :ops="ops">
                  <div
                    v-for="(item, doneIndex) in doneList"
                    :key="doneIndex"
                  >
                    <Card class="customIcon">
                      <Icon type="ios-checkmark-circle-outline" size="20" style="line-height: .8; cursor: pointer"
                            @click="changeState(item, doneIndex)"/>
                      <Icon type="ios-close" size="31"
                            style="float: right;color: #ff7800;cursor: pointer; line-height: .8"
                            @click="delTask(item.ptId, doneIndex, 'done')"
                      />
                      <Icon v-if="item.importance == 1"
                            size="20"
                            type="ios-star"
                            style="float: right;cursor:pointer;margin-right: 20px"
                            @click="changeImportance(item, doneIndex, 'done')"
                      />
                      <Icon v-else
                            size="20"
                            type="ios-star-outline"
                            style="float: right;cursor: pointer;margin-right: 20px"
                            @click="changeImportance(item, doneIndex, 'done')"
                      />
                      <label style="margin-left: 10px;">{{item.content}}</label>
                    </Card>

                  </div>
                </vue-scroll>
              </div>

            </Card>
          </div>

        </Card>
      </Col>
    </Row>
  </div>
</template>

<script>
  export default {
    name: "todoList",
    data() {
      return {
        selectedTaskType: ["Doing", "Done", "Importance"],
        doneList: [],
        doingList: [],
        importanceNum: 0,
        newTask: {
          userId: "",
          content: "",
          endTime: "",
          importance: "0",
          state: "doing"
        },
        contentHeight: "",
        ops: {
          bar: {
            background: "#808695"
          }
        },
      }
    },
    mounted() {
      this.initTodoList();
      this.resizeContent();
    },
    methods: {
      resizeContent() {
        if (window.innerHeight > 800) {
          this.contentHeight = window.innerHeight - 120;
        } else {
          this.contentHeight = 800;
        }
        window.onresize = () => {
          return (() => {
            this.resizeContent();
          })();
        };
      },
      initTodoList: function () {
        this.axios
          .get("/GeoProblemSolving/user/todo/" + this.$store.getters.userId)
          .then(res => {
            let doingListTemp = [];
            let doneListTemp = [];
            if (res.data.code == 0) {
              let todoList = res.data.data;
              for (let index = 0; index < todoList.length; index++) {
                if (todoList[index].state == "doing" && todoList[index].importance == 1) {
                  doingListTemp.unshift(todoList[index]);
                  this.importanceNum++;
                } else if (todoList[index].state == "doing" && todoList[index].importance == 0) {
                  doingListTemp.push(todoList[index]);
                } else if (todoList[index].state == "done" && todoList[index].importance == 1) {
                  doneListTemp.unshift(todoList[index]);
                  this.importanceNum++;
                } else if (todoList[index].state == "done" && todoList[index].importance == 0) {
                  doneListTemp.push(todoList[index]);
                }
              }
              this.$set(this, "doneList", doneListTemp);
              this.$set(this, "doingList", doingListTemp);
            } else if (res.code == -2) {
              this.$Message.error("Todo Task Loading Fail.")
            }
          })
          .catch(err => {
            this.$Message.error("Todo Task Loading Fail.")
          })
      },
      addTask: function () {
        this.newTask.userId = this.$store.getters.userId;
        if (this.newTask.content !== "") {
          this.axios
            .post("/GeoProblemSolving/user/todo", this.newTask)
            .then(res => {
              console.log(res);
              if (res.data.code == 0) {
                this.doingList.push(res.data.data);
                this.newTask.content = "";
                this.newTask.endTime = "";
              }
            })
            .catch(err => {
              this.$Message.error("Failed to create todo task")
            })
        }
      },
      // 选择日期
      handleChange: function (date) {
        this.newTask.endTime = date;
      },
      changeState: function (item, index) {
        let changeType = 0;
        if (item.state == "doing") {
          item.state = "done";
          changeType = 1;
        } else if (item.state == "done") {
          item.state = "doing";
          changeType = 2;
        }
        this.axios
          .put("/GeoProblemSolving/user/todo", item)
          .then(res => {
            if (res.data.code == 0) {
              //
              if (changeType == 1) {
                this.doneList.push(item);
                this.doingList.splice(index, 1);
              } else if (changeType == 2) {
                this.doingList.push(item);
                this.doneList.splice(index, 1);
              }
            }
          })
          .catch(err => {
            this.$Message.error("Change task state Fail.")
          })

      },
      changeImportance: function (item, index, state) {
        if (item.importance == 0) {
          item.importance = 1;
        } else if (item.importance == 1) {
          item.importance = 0;
          this.importanceNum--;
        }
        this.axios
          .put("/GeoProblemSolving/user/todo", item)
          .then(res => {
            if (res.data.code == 0) {
              if (state == "doing" && item.importance == 1) {
                this.doingList.unshift(this.doingList.splice(index, 1)[0]);
                this.importanceNum++;
              } else if (state == "done" && item.importance == 1) {
                this.doneList.unshift(this.doneList.splice(index, 1)[0]);
                this.importanceNum++;
              }
            }
          })
          .catch()
      },
      delTask: function (ptId, index, state) {
        this.axios
          .delete("/GeoProblemSolving/user/todo/" + ptId)
          .then(res => {
            if (res.data.code == 0) {
              if (state == "doing") {
                this.doingList.splice(index, 1);
                this.$Notice.success({title: "Delete Success"})
              } else if (state == "done") {
                this.$Notice.success({title: "Delete Success"})
                this.doneList.splice(index, 1);
              }
            } else {
              this.$Message.error("Delete Fail.")
            }
          })
          .catch(err => {
            this.$Message.error("Delete Fail.")
          })
      },
      //使用计算属性还是方法更好？？？
      timeCompare: function (time) {
        let nowTime = new Date();
        let formatTime = Date.parse(time);
        let secondsNum = nowTime - formatTime - 86400000;
        if (secondsNum > 0) {
          return true;
        } else {
          return false;
        }
      },
      test: function () {
        console.log("123131")
      }
    },
    //用一个计算属性动态捕捉doing/done List 的变化
    computed: {
      contentHeightCompute: function () {
        let tempHeight = "";
        if (this.selectedTaskType.length == 2){
          tempHeight = 230;
        }else if (this.selectedTaskType.length == 3){
          tempHeight = 600;
        }
        return tempHeight;
      }
    },
  }
</script>

<style scoped>
  .badge {
    display: inline-block;
    min-width: 10px;
    padding: 3px 7px;
    font-size: 12px;
    font-weight: bold;
    line-height: 1;
    color: #ffffff;
    text-align: center;
    white-space: nowrap;
    vertical-align: baseline;
    background-color: #999999;
    border-radius: 10px;
  }

  .customCardHead >>> .ivu-card-head {
    border-bottom-color: white;
    padding: 4px 6px;
  }

  .customDatePicker >>> .ivu-input {
    display: inline-block;
    width: 100%;
    height: 32px;
    line-height: 1.5;
    padding: 4px 7px;
    font-size: 12px;
    border: 1px solid white;
    text-align: center;
    border-radius: 0px;
    color: #515a6e;
    background-color: #fff;
    background-image: none;
    position: relative;
    cursor: text;
    transition: border .2s ease-in-out, background .2s ease-in-out, box-shadow .2s ease-in-out;
  }

  .customDatePicker:hover >>> .ivu-input {
    cursor: pointer;
  }

  .customIcon >>> .ivu-icon {
    font-weight: 700;
  }

  .custom >>> .ivu-input-suffix i {
    font-size: 22px;
    line-height: 30px;
    font-weight: 700;
  }
</style>
