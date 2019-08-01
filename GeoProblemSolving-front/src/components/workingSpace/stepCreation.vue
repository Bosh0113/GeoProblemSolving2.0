<style scoped>
</style>
<template>
  <div>
    <Row>
      <div style="background-color:white">
        <Col offset="1" style="padding-left:20px;padding-top:10px">
          <Breadcrumb>
            <BreadcrumbItem :to="toProjectPage">Project</BreadcrumbItem>
            <BreadcrumbItem :to="toSubProjectPage">Subproject</BreadcrumbItem>
            <BreadcrumbItem>Working panel</BreadcrumbItem>
          </Breadcrumb>
        </Col>
        <div style="text-align:center;font-size:1.5rem;height:20px;">
          <strong>{{subProjectInfo.title}}</strong>
        </div>
        <div
          style="text-align:center;font-size:1rem;margin-top:40px"
        >Create the step of geo-problem solving process, and fill in the information and select the modules for dealing with problem.</div>
        <div style="margin-top:40px;margin-bottom:30px;height:380px;width:100%">
          <Col :lg="{ span: 10, offset: 2 }" :xxl="{ span: 9, offset: 3 }">
            <div
              style="height:380px;width:100%;border-right:1px solid;border-color:#00c0ff;padding-right:20px"
            >
              <Form
                ref="formValidate"
                :model="formValidate"
                :rules="ruleValidate"
                :label-width="120"
              >
                <FormItem label="Name" prop="name">
                  <Input v-model="formValidate.name" placeholder="Enter step name"></Input>
                </FormItem>
                <FormItem label="Step type" prop="type" style="margin-top:40px">
                  <Select v-model="formValidate.type" placeholder="Select step type">
                    <Option value="beijing">Resource collection</Option>
                    <Option value="shanghai">Data processing</Option>
                    <Option value="shenzhen">Modeling</Option>
                    <Option value="beijing">Model evaluation</Option>
                    <Option value="shanghai">Analysis with models or tools</Option>
                    <Option value="shenzhen">Simulation/Prediction</Option>
                    <Option value="beijing">Visualization & Representation</Option>
                    <Option value="shanghai">Qualitative analysis</Option>
                    <Option value="shenzhen">Decision-making and management</Option>
                  </Select>
                </FormItem>
                <FormItem label="Expected result" prop="result" style="margin-top:50px">
                  <Input
                    v-model="formValidate.result"
                    type="textarea"
                    :rows="10"
                    placeholder="Enter something..."
                  ></Input>
                </FormItem>
              </Form>
            </div>
          </Col>
          <Col  :lg="{ span: 10 }" :xxl="{ span: 9}" style="height:380px;padding-left:20px">
            <div style="font-size: 12px">Contained modules:</div>
            <div
              style="width:100%;height:345px;border:1px solid #dcdee2;border-radius: 4px; margin-top:10px"
            >
              <Card style="width:23%;height:80px;border:1px solid lightblue;margin:1%;float:left">
                <Avatar
                  shape="square"
                  icon="ios-hammer"
                  size="large"
                  style="margin-left: 23px;margin-top: -5px"
                />
                <div style="text-align:center;font-size: 8px">module 1</div>
              </Card>
            </div>
          </Col>
        </div>
        <div>
          <Col span="2" offset="4">
            <Button style="width:100%" type="primary" @click="handleSubmit('formValidate')">Submit</Button>
          </Col>
          <Col span="2" offset="12">
            <Button @click="handleReset('formValidate')" style="margin-left: 8px; width:100%">Reset</Button>
          </Col>
        </div>
      </div>
    </Row>
  </div>
</template>
<script>
import { VueFlowy, FlowChart } from "vue-flowy";
import * as socketApi from "./../../api/socket";
// import Avatar from "vue-avatar";
import echarts from "echarts";
export default {
  updated() {
    $(".userAvatar sup").css("margin", "15px 15px 0 0");
    $(".ivu-steps-title").css("cursor", "pointer");
    $(".ivu-steps-title").css("overflow", "hidden");
    $(".ivu-steps-title").css("white-space", "nowrap");
    $(".ivu-steps-title").css("text-overflow", "ellipsis");
    $(".ivu-steps-title").css("max-width", "120px");
  },
  components: {
    VueFlowy
    // Avatar
  },
  data() {
    return {
      // info of subproject
      subProjectInfo: [],
      sidebarHeight: 800,
      toProjectPage: "",
      toSubProjectPage: "",
      // 用户角色
      userRole: "",
      formValidate: {
        name: "",
        result: "",
        type: ""
      },
      ruleValidate: {
        name: [
          {
            required: true,
            type: "string",
            message: "Please enter the step name",
            trigger: "blur",
            max: 60
          }
        ],
        result: [
          {
            required: true,
            type: "string",
            message: "Please enter a expected result",
            trigger: "blur"
          }
        ],
        type: [
          {
            required: true,
            message: "Please select the stpe type",
            trigger: "change"
          }
        ]
      }
    };
  },
  created() {
    this.init();
  },
  mounted() {
    window.addEventListener("resize", this.reSize);
  },
  // add by mzy for navigation guards
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      if (!vm.$store.getters.userState) {
        next("/login");
        // vm.$router.push({ name: "Login" });
      } else if (vm.userRole == "Visitor") {
        next();
      } else {
        var isManager = false;
        var isMember = false;
        var subProjectInfo = vm.subProjectInfo;
        if (subProjectInfo.managerId == vm.$store.getters.userId) {
          isManager = true;
        } else {
          var members = subProjectInfo.members;
          for (var i = 0; i < members.length; i++) {
            if (members[i].userId == vm.$store.getters.userId) {
              isMember = true;
              break;
            }
          }
        }
        if (!(isManager || isMember)) {
          vm.$Message.error("You have no property to access it");
          // next(`/project/${vm.$store.getters.currentProjectId}`);
          vm.$router.go(-1);
        }
      }
    });
  },
  methods: {
    //初始化函数，作用是控制侧边栏的高度，设置右边通知栏弹出时候的距顶高度以及延迟的时间
    init() {
      var that = this;
      let subProjectId = this.$route.params.id;
      let subProjectInfo = this.$store.getters.subProject;
      if (
        JSON.stringify(subProjectInfo) != "{}" &&
        subProjectInfo.subProjectId == subProjectId
      ) {
        this.$set(this, "subProjectInfo", subProjectInfo);
        this.toProjectPage = "/project/" + subProjectInfo.projectId;
        this.toSubProjectPage =
          "/project/" + subProjectInfo.subProjectId + "/subproject";
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

              this.managerIdentity(subProjectInfo.managerId);
              this.memberIdentity(subProjectInfo.members);
              this.$store.commit("setSubProjectInfo", subProjectInfo);
              this.toProjectPage = "/project/" + subProjectInfo.projectId;
              this.toSubProjectPage =
                "/project/" + subProjectInfo.subProjectId + "/subproject";
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
    managerIdentity(managerId) {
      if (managerId === this.$store.getters.userId) {
        this.subProjectInfo.isManager = true;
      }
    },
    memberIdentity(members) {
      for (let i = 0; i < members.length; i++) {
        if (members[i].userId === this.$store.getters.userId) {
          this.subProjectInfo.isMember = true;
          break;
        }
      }
    },
    backtoSubproject() {
      this.$router.push(`./subproject`);
    },
    ok() {
      this.$Message.info("Clicked ok");
    },
    cancel() {},
    createModuleSuccess(title) {
      this.$Notice.success({
        title: "Create Notification",
        desc:
          "The process module" +
          `<span style="color:lightblue"><strong>` +
          "&nbsp" +
          title +
          "&nbsp" +
          `</strong></span>` +
          "has been created successfully"
      });
    },
    deleteModuleSuccess() {
      this.$Notice.info({
        title: "Delete Notification",
        desc: "The module has been deleted successfully"
      });
    }
  }
};
</script>
