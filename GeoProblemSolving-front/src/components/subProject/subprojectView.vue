<style lang="stylus" scoped></style>
<template>
  <Row style="margin-left:60px">
    <Col span="9" offset="2">
      <div style="margin:20px">
        <Card style="width:100%;height:10%">
          <div style="text-align:center">
            <h3>
              Click it to check
              <a @click="gotoIntroduction()">Introduction</a>
            </h3>
          </div>
          <div style="text-align:center; width:40%; float:left">
            <img src="../../../src/assets/images/info.png" />
          </div>
          <div style="width:60%; float:left">
            <ul>
              <li>The description of this subproject</li>
              <li>The background of this subproject</li>
              <li>The limitation in this subproject</li>
              <li>The participant list of this subproject</li>
            </ul>
          </div>
          <div style="text-align:center">
            <h3>
              Click it to check
              <a @click="gotoIntroduction()">Introduction</a>
            </h3>
          </div>
        </Card>
      </div>
    </Col>
    <Col span="9" offset="2">
      <div style="margin:20px">
        <Card style="width:100%">
          <div style="text-align:center; width:40%; float:left">
            <img src="../../../src/assets/images/data.png" />
          </div>
          <div style="width:60%; float:left">
            <ul>
              <li>Collect different types of resources</li>
              <li>Support context understanding</li>
              <li>Support data processing</li>
              <li>Support modeling and analysis</li>
            </ul>
          </div>
          <div style="text-align:center">
            <h3>
              Click it to check
              <a @click="gotoResource()">Resources</a>
            </h3>
          </div>
        </Card>
      </div>
    </Col>
    <Col span="9" offset="2">
      <div style="margin:20px">
        <Card style="width:100%">
          <div style="text-align:center; width:40%; float:left">
            <img src="../../../src/assets/images/steps.jpg" />
          </div>
          <div style="width:60%; float:left">
            <ul>
              <li>Node-presented steps</li>
              <li>Create, select and delete steps</li>
              <li>Support the geo-problem analysis during the whole process</li>
            </ul>
          </div>
          <div style="text-align:center">
            <h3>
              Click it to check
              <a @click="gotoWorkingsteps()">Working steps</a>
            </h3>
          </div>
        </Card>
      </div>
    </Col>
    <Col span="9" offset="2">
      <div style="margin:20px">
        <Card style="width:100%">
          <div style="text-align:center; width:40%; float:left">
            <img src="../../../src/assets/images/tasks.png" />
          </div>
          <div style="width:60%; float:left">
            <ul>
              <li>Task schedule</li>
              <li>"To do" list: tasks that need to be done</li>
              <li>"Doing" list: tasks that are being done</li>
              <li>"Done" list: tasks that have been done</li>
            </ul>
          </div>
          <div style="text-align:center">
            <h3>
              Click it to check
              <a @click="gotoTaskassignment()">Task assignment</a>
            </h3>
          </div>
        </Card>
      </div>
    </Col>
    <Col span="14" offset="6">
      <span style="margin:25px 0; font-size:28px">Subproject</span>
      <span style="margin: 0px 10px; font-size: 14px;">
        is created for dealing with sub-problems in a complex geo-problem.
        Different subprojects are for distinct purposes, but all of these are significant for facilitating complex geo-problem solving.
      </span>
    </Col>
  </Row>
</template>
<script>
export default {
  data() {
    return {};
  },
  mounted(){
  },
  methods: {
    gotoIntroduction() {
      this.$router.replace({ name: "info" });
    },
    gotoResource() {
      this.$router.replace({ name: "resource" });
    },
    gotoWorkingsteps() {
      this.$router.replace({ name: "process" });
    },
    gotoTaskassignment() {
      this.$router.replace({ name: "task" });
    }
  }
};
</script>