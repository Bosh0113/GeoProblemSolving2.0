<template>
  <div>
    <!--  Fiter  -->
    <Row>
      <Col span="22" offset="1">
        <Card>
          <CheckboxGroup v-model="selectedTaskType">
            <Checkbox label="Doing">
              <sapn>Doing</sapn>
              <span class="badge">1</span>
            </Checkbox>
            <Checkbox label="Done">
              <span>Done</span>
              <span class="badge">5</span>
            </Checkbox>
            <Checkbox label="Importance">
              <span>Importance</span>
              <span class="badge">2</span>
            </Checkbox>
            <Checkbox label="Assigned">
              <span>Assigned to you</span>
              <span class="badge">10</span>
            </Checkbox>
          </CheckboxGroup>
        </Card>
      </Col>
    </Row>
    <Row>
      <Col span="22" offset="1">
        <Card >
          <!--         新建任务   -->
          <div >
            <DatePicker
              type="date"
              style="position: absolute;top: 18px; right: 50px; z-index: 3"
              class="customDatePicker custom"
              title="select endTime"
              @on-change ="handleChange"
            >
            </DatePicker>
            <Input
              v-model="newTask.discription"
              prefix="ios-radio-button-off"
              placeholder="Add Todo"
              size="large"
              @on-enter="addTask()"
              style="display: inline-block;"
              class="customIcon"
            />
          </div>

          <!--      未完成            -->
          <div>
            <Card>
              <p slot="title">Doing</p>
              <div>
                <Card class="customIcon">
                  <Icon type="ios-radio-button-off" size="20" />
                  <Icon type="ios-close"  size="31" style="float: right;color: #ff7800;cursor: pointer; line-height: .8" />
                  <Icon v-if="test" size="20" type="ios-star-outline" style="float: right;cursor: pointer;margin-right: 20px" />
                  <Icon v-else size="20" type="ios-star" style="float: right;cursor:pointer;margin-right: 20px" />
                  This is doing task space.
                </Card>
              </div>
            </Card>
          </div>
          <!--        已完成      -->
          <div>
            <Card>
              <p slot="title">Done</p>
              <!--            显示内容                -->
              <div>
                <Card class="customIcon">
                  <Icon type="ios-checkmark-circle-outline" size="20" style="line-height: .8"/>
                  <Icon type="ios-close"  size="31" style="float: right;color: #ff7800;cursor: pointer; line-height: .8" />
                  <Icon v-if="test" size="20" type="ios-star-outline" style="float: right;cursor: pointer;margin-right: 20px" />
                  <Icon v-else size="20" type="ios-star" style="float: right;cursor:pointer;margin-right: 20px" />
                  <span>This is done task space.</span>
                </Card>

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
        selectedTaskType: [],
        doneList: [],
        doingList: [],
        newTask: {
          userId: "",
          discription: "",
          endTime: "",
        }
      }
    },
    mounted() {
    },
    methods: {
      initTodoList: function () {
        this.axios
          .get()
          .then()
          .catch(err => {
            this.$Message.error("Todo Task Loading Fail.")
          })
      },
      addTask: function(){
        this.newTask.userId = this.$store.getters.userId;
        if (this.newTask.discription != ""){
          this.newTask.discription = "";
          this.newTask.endTime = "";
        }
        console.log(this.newTask)

      },
      handleChange: function(date){
        this.newTask.endTime = date;
      },
      test: function () {
        console.log("123131")
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
  .customDatePicker >>> .ivu-input{
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
    transition: border .2s ease-in-out,background .2s ease-in-out,box-shadow .2s ease-in-out;
  }
  .customDatePicker:hover >>> .ivu-input{
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
