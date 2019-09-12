<style scoped>
.title {
  height: 40px;
  line-height: 40px;
  text-align: center;
  font-size: 20px;
  font-weight: bold;
  border-bottom: 1px solid lightgray;
}
</style>
<template>
  <div>
    <Row>
      <Menu
        active-name="info"
        style="width:60px;position:absolute"
        :style="{height:contentHeight}"
        @on-select="changeContent"
      >
        <MenuItem name="home" style="padding-left: 16px;" title="Project page" :to="toProjectPage">
          <Icon type="md-arrow-round-back" size="30" />
        </MenuItem>
        <MenuItem name="info" style="padding-left: 16px;" title="Subproject introduction">
          <Icon type="md-globe" size="30" />
        </MenuItem>
        <MenuItem name="resource" style="padding-left: 16px;" title="Subproject resources">
          <Icon type="ios-cloud-outline" size="30" />
        </MenuItem>
        <MenuItem name="process" style="padding-left: 16px;" title="Working steps">
          <Icon type="ios-git-network" size="30" />
        </MenuItem>
        <MenuItem name="task" style="padding-left: 16px;" title="Task arrangement">
          <Icon type="ios-calendar-outline" size="30" />
        </MenuItem>
      </Menu>
      <div style="font-size:1.5rem;height:60px;padding-top:10px;border:1px solid lightgrey">
        <Col offset="4" span="16" style="text-align:center;">
          <strong>{{subProjectInfo.title}}</strong>
        </Col>
        <Button
          type="info"
          to="../workspace"
          icon="md-globe"
          style="float:right;margin:3px 30px 0 0"
          title="Go to work"
        >Working space</Button>
      </div>
      <router-view :subProjectInfo="subProjectInfo" :userRole="userRole" :projectInfo="projectInfo"></router-view>
    </Row>
  </div>
</template>
<script>
export default {
  data() {
    return {
      // information of project
      projectInfo: {},
      // info of subproject
      subProjectInfo: [],
      contentHeight: "",
      toProjectPage: "",
      // 用户角色
      userRole: "Visitor"
    };
  },
  created() {
    this.init();
  },
  mounted() {
    this.toProjectPage = "/project/" + sessionStorage.getItem("projectId");
    window.addEventListener("resize", this.initSize);
  },
  // add by mzy for navigation guards
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      if (!vm.$store.getters.userState) {
        next("/login");
      } else {
        // var userId = vm.$store.getters.userId;
        // var members = vm.subProjectInfo.members;
        // var isMember = false;
        // for (var i = 0; i < members.length; i++) {
        //   if (members[i].userId == userId) {
        //     isMember = true;
        //     break;
        //   }
        // }
        if (
          !(
            vm.userRole == "Manager" ||
            vm.userRole == "Member" ||
            vm.userRole == "PManager"
          )
        ) {
          vm.$Message.error("You have no property to access it");
          // next(`/project/${vm.$store.getters.currentProjectId}`);
          vm.$router.go(-1);
        } else {
          next();
        }
      }
    });
  },
  beforeRouteLeave(to, from, next) {
    next();
  },
  beforeDestroy: function() {
    window.removeEventListener("resize", this.initSize);
  },
  methods: {
    initSize() {
      this.contentHeight = window.innerHeight - 120 + "px";
    },
    //初始化函数，作用是控制侧边栏的高度，设置右边通知栏弹出时候的距顶高度以及延迟的时间
    init() {
      this.initSize();
      this.getSubprojectInfo();
    },
    getProjectInfo() {
      let projectInfo = this.$store.getters.project;
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
              this.projectInfo = data[0];
              this.$store.commit("setProjectInfo", data[0]);
            } else {
              console.log(data);
            }
          }
        });
      }
    },
    getSubprojectInfo() {
      let subProjectId = this.$route.params.id;
      let subProjectInfo = this.$store.getters.subProject;
      if (
        JSON.stringify(subProjectInfo) != "{}" &&
        subProjectInfo.subProjectId == subProjectId
      ) {
        this.$set(this, "subProjectInfo", subProjectInfo);
        this.userRoleIdentity();

        sessionStorage.setItem("subProjectId", subProjectInfo.subProjectId);
        sessionStorage.setItem("subProjectName", subProjectInfo.title);
      } else {
        let that = this;
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
              that.$store.commit("userLogout");
              that.$router.push({ name: "Login" });
            } else if (data != "None" && data != "Fail") {
              subProjectInfo = data[0];
              that.$set(that, "subProjectInfo", subProjectInfo);
              that.userRoleIdentity();

              sessionStorage.setItem(
                "subProjectId",
                subProjectInfo.subProjectId
              );
              sessionStorage.setItem("subProjectName", subProjectInfo.title);
              that.$store.commit("setSubProjectInfo", subProjectInfo);
            }
          },
          error: function(err) {
            console.log("Get manager name fail.");
          }
        });
      }
    },
    userRoleIdentity() {
      this.userRole = "Visitor";
      if (this.$store.getters.userState) {
        // 是否是子项目管理员
        if (this.subProjectInfo.managerId === this.$store.getters.userId) {
          this.userRole = "Manager";
        }
        // 是否是子项目成员
        else {
          for (let i = 0; i < this.subProjectInfo.members.length; i++) {
            if (
              this.subProjectInfo.members[i].userId ===
              this.$store.getters.userId
            ) {
              this.userRole = "Member";
              break;
            }
          }
        }
        // 是否是项目管理员
        this.getProjectInfo();
        if (this.userRole != "Manager") {
          if (this.projectInfo.managerId === this.$store.getters.userId) {
            this.userRole = "PManager";
          }
        }
      } else {
        this.userRole = "Visitor";
      }
    },
    ok() {
      this.$Message.info("Clicked ok");
    },
    cancel() {},
    changeContent(name) {
      if (name == "process") {
        this.$router.replace({ name: "process" });
      } else if (name == "info") {
        this.$router.replace({ name: "info" });
      } else if (name == "resource") {
        this.$router.replace({ name: "resource" });
      } else if (name == "task") {
        this.$router.replace({ name: "task" });
      }
    }
  }
};
</script>
