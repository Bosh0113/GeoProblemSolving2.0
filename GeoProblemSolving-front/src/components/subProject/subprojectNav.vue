<style scoped>
.title {
  height: 40px;
  line-height: 40px;
  text-align: center;
  font-size: 20px;
  font-weight: bold;
  border-bottom: 1px solid lightgray;
}

@media screen and (min-width: 1300px) {
  .project_title {
    font-size: 1.5rem;
    color: rgba(214, 109, 0, 0.82);
    max-width: 600px;
    display: inline-block;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

@media screen and (max-width: 1299px) {
  .project_title {
    font-size: 1.5rem;
    color: rgba(214, 109, 0, 0.82);
    max-width: 350px;
    display: inline-block;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

.left_bar_bg {
  background: lightslategrey;
}
</style>
<template>
  <div>
    <Row>
      <Menu
        :active-name="menuActive"
        style="width:60px;position:absolute;z-index:1"
        :style="{height:contentHeight}"
        @on-select="changeContent"
        class="left_bar_bg"
        theme="dark"
      >
        <MenuItem name="back" style="padding-left: 16px;" title="Project page">
          <Icon type="md-arrow-round-back" color="white" size="30" />
        </MenuItem>
        <!-- <MenuItem name="overview" style="padding-left: 16px;" title="Subproject Home">
        <Icon type="md-home" size="30" />
        </MenuItem>-->
        <MenuItem name="info" style="padding-left: 16px;" title="Subproject introduction">
          <Icon type="ios-information-circle" color="white" size="30" />
        </MenuItem>
        <MenuItem name="task" style="padding-left: 16px;" title="Task arrangement">
          <Icon type="md-calendar" size="30" color="white" />
        </MenuItem>

        <MenuItem name="process" style="padding-left: 16px;" title="Working steps">
          <!-- <Icon type="md-git-branch" size="30" /> -->
          <Icon type="md-analytics" size="30" color="white" />
        </MenuItem>

        <MenuItem name="resource" style="padding-left: 16px;" title="Subproject resources">
          <Icon type="ios-folder" size="30" color="white" />
        </MenuItem>
      </Menu>
      <div
        style="height:60px;display:flex;align-items:center;border:1px solid lightgrey;margin-left:60px;justify-content:center;"
      >
        <div style="margin-left:20px; position:absolute;left:80px;">
          <strong style="font-size:1.5rem">Subproject</strong>
          <Divider type="vertical" />
          <strong style="font-size:1rem;">{{panelTitle}}</strong>
        </div>
        <div>
          <span class="project_title">{{subProjectInfo.title}}</span>
        </div>
      </div>
      <router-view
        :subProjectInfo="subProjectInfo"
        :userRole="userRole"
        :projectInfo="projectInfo"
        :scopeInfo="scopeInfo"
        @changeSubProjectInfo="changeSubProjectInfo"
      ></router-view>
    </Row>
  </div>
</template>
<script>
export default {
  data() {
    return {
      menuActive: "info",
      panelTitle: "Overview",
      // information of project
      projectInfo: {},
      // info of subproject
      subProjectInfo: {},
      scopeInfo: {},
      contentHeight: "",
      // 用户角色
      userRole: "Visitor"
    };
  },
  watch: {
    $route() {
      this.initPenal();
    }
  },
  created() {
    this.init();
  },
  mounted() {
    this.initPenal();
    window.addEventListener("resize", this.initSize);
  },
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      if (!vm.$store.getters.userState) {
        next("/login");
      } else {
        if (
          !(
            vm.userRole == "Manager" ||
            vm.userRole == "Member" ||
            vm.userRole == "PManager" ||
            vm.getVisitorAccess()
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
    initSize() {
      this.contentHeight = window.innerHeight - 120 + "px";
    },
    initPenal() {
      var type = this.$route.name;
      if (type == "" || type == "overview") {
        this.menuActive = "overview";
        this.panelTitle = "Overview";
      } else if (type == "process") {
        this.menuActive = type;
        this.panelTitle = "Workspace";
      } else if (type == "info") {
        this.menuActive = type;
        this.panelTitle = "Introduction";
      } else if (type == "resource") {
        this.menuActive = type;
        this.panelTitle = "Resources";
      } else if (type == "task") {
        this.menuActive = type;
        this.panelTitle = "Task assignment";
      }
    },
    getVisitorAccess() {
      let visitorPermission = this.projectInfo.permissionManager.observe
        .visitor;
      if (
        this.projectInfo.permissionManager != undefined &&
        this.userRole == "Visitor" &&
        (visitorPermission == "All" ||
          visitorPermission == "At subproject level")
      ) {
        return true;
      }
    },
    getProjectInfo() {
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
    },
    getSubprojectInfo() {
      let subProjectId = this.$route.params.id;
      let subProjectInfo = this.$store.getters.subProject;
      if (
        JSON.stringify(subProjectInfo) != "{}" &&
        subProjectInfo.subProjectId == subProjectId
      ) {
        this.$set(this, "subProjectInfo", subProjectInfo);
        this.$set(this, "scopeInfo", subProjectInfo);
        this.userRoleIdentity();
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
              that.$set(that, "scopeInfo", subProjectInfo);
              that.userRoleIdentity();
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
    changeContent(name) {
      if (name == "back") {
        window.location.href =
          "/GeoProblemSolving/projectDetail/" + this.subProjectInfo.projectId;
      } else if (name == "overview") {
        this.$router.replace({ name: "overview" });
      } else if (name == "process") {
        this.$router.replace({ name: "process" });
      } else if (name == "info") {
        this.$router.replace({ name: "info" });
      } else if (name == "resource") {
        this.$router.replace({ name: "resource" });
      } else if (name == "task") {
        this.$router.replace({ name: "task" });
      }
    },
    changeSubProjectInfo(newInfo) {
      this.subProjectInfo = newInfo;
    }
  }
};
</script>
