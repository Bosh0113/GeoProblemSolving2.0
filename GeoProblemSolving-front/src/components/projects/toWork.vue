<template>
  <div>
    <type-choose
      :projectInfo="projectInfo"
      :userRole="userRole"
      v-if="projectInfo.type==''"
      @changeProjectInfo="changeProjectInfo"
    ></type-choose>
    <solving-step
      :scopeInfo="projectInfo"
      :userRole="userRole"
      v-else-if="projectInfo.type=='type1'"
      @changeProjectInfo="changeProjectInfo"
    ></solving-step>
    <workspace
      :projectInfo="projectInfo"
      :userRole="userRole"
      v-else-if="projectInfo.type=='type2'"
      @changeProjectInfo="changeProjectInfo"
    ></workspace>
  </div>
</template>
<script>
import typeChoose from "./typeChoose.vue";
import workspace from "./workspace.vue";
import solvingStep from "./../subProject/solvingStep";
export default {
  components: {
    typeChoose,
    workspace,
    solvingStep
  },
  created() {
    this.getUserInfo();
    this.getProjectInfo();
  },
  mounted() {
    $("#app").css("min-width", "0");
  },
  data() {
    return {
      projectId: this.$route.params.projectId,
      userInfo: JSON.parse(sessionStorage.getItem("userInfo")),
      projectInfo: {},
      userRole: "Visitor"
    };
  },
  methods: {
    getUserInfo() {
      if (this.userInfo == {} || this.userInfo == undefined) {
        $.ajax({
          url: "/GeoProblemSolving/user/state",
          type: "POST",
          async: false,
          success: data => {
            if (data) {
              var userInfo = data;
              userInfo.userState = true;
              this.userInfo = userInfo;
            }
          },
          error: function(err) {
            console.log("Get user info fail.");
          }
        });
      }
    },
    getProjectInfo() {
      $.ajax({
        url:
          "/GeoProblemSolving/project/inquiry" +
          "?key=projectId" +
          "&value=" +
          this.projectId,
        type: "GET",
        async: false,
        success: data => {
          if (data == "Offline") {
            // parent.location.href = "/GeoProblemSolving/login";
            var pageUrl = parent.location.href;
            this.axios
              .get("/GeoProblemSolving/user/login?pageUrl="+pageUrl)
              .then(res=>{
                parent.location.href = res.data;
              })
          }
          if (data != "None" && data != "Fail") {
            this.projectInfo = data[0];
            this.identifyUserRole();
          } else {
            console.log(data);
          }
        }
      });
    },
    identifyUserRole() {
      //Manager|Member|Visitor
      var thisUserId = this.userInfo.userId;
      var projectInfo = this.projectInfo;
      var members = projectInfo.members;
      var managerId = projectInfo.managerId;
      if (managerId == thisUserId) {
        this.userRole = "Manager";
      } else {
        for (var i = 0; i < members.length; i++) {
          var userId = members[i].userId;
          if (userId == thisUserId) {
            this.userRole = "Member";
          }
        }
      }
    },
    changeProjectInfo(newProjectInfo) {
      this.projectInfo = newProjectInfo;
      parent.vm.projectInfo = newProjectInfo;
    }
  }
};
</script>
