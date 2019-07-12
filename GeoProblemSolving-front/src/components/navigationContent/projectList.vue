<style scoped>
.top,
.bottom {
  text-align: center;
}
img {
  width: 100%;
  height: auto;
}
.whitespace {
  height: 20px;
}
.projectTitle {
  height: 30px;
  line-height: 30px;
  font-size: 20px;
  max-width: 200px;
  padding-left: 5px;
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.btnCreate {
  font-size: 15px;
  height: 40px;
  margin: 10px;
}
.btnCreate:hover {
  background-color: #19be6b;
  color: white;
}
/* Loading动画的特效 */
.demo-spin-icon-load {
  animation: ani-demo-spin 1s linear infinite;
}
</style>
<template>
    <Row>
      <Col span="22" offset="1">
        <Spin fix v-show="getFinish">
          <Icon type="ios-loading" size="100" class="demo-spin-icon-load" color="yellowgreen"></Icon>
          <div>Loading</div>
        </Spin>
          <div style="text-align:right;">
            <Button
              @click="newProject"
              type="success"
              class="btnCreate"
              icon="md-add"
            >Create a new project</Button>
          </div>
        <div>
          <Tabs value="All" type="card" @on-click="filterProjects">
            <TabPane label="All" name="All" icon="ios-list">
              <project-list :projectList = projectShowList projectType="All" @sendNotice="sendMessage"></project-list>
            </TabPane>
            <TabPane label="Terrestrial" name="Terrestrial" icon="md-globe">
              <project-list :projectList = projectShowList projectType="Terrestrial" @sendNotice="sendMessage"></project-list>
            </TabPane>
            <TabPane label="Coastal" name="Coastal" icon="ios-boat">
              <project-list :projectList = projectShowList projectType="Coastal" @sendNotice="sendMessage"></project-list>
            </TabPane>
            <TabPane label="Marine" name="Marine" icon="ios-water">
              <project-list :projectList = projectShowList projectType="Marine" @sendNotice="sendMessage"></project-list>
            </TabPane>
            <TabPane label="Climate" name="Climate" icon="ios-partly-sunny">
              <project-list :projectList = projectShowList projectType="Climate" @sendNotice="sendMessage"></project-list>
            </TabPane>
            <TabPane label="Ecological" name="Ecological" icon="ios-leaf">
              <project-list :projectList = projectShowList projectType="Ecological" @sendNotice="sendMessage"></project-list>
            </TabPane>
            <TabPane label="Geological" name="Geological" icon="ios-analytics">
              <project-list :projectList = projectShowList projectType="Geological" @sendNotice="sendMessage"></project-list>
            </TabPane>
            <TabPane label="Human-Activity" name="Human" icon="ios-people">
              <project-list :projectList = projectShowList projectType="Human" @sendNotice="sendMessage"></project-list>
            </TabPane>
            <TabPane label="GIS & RS" name="GISRS" icon="ios-globe">
              <project-list :projectList = projectShowList projectType="GISRS" @sendNotice="sendMessage"></project-list>
            </TabPane>
            <TabPane label="General" name="General" icon="ios-grid">
              <project-list :projectList = projectShowList projectType="General" @sendNotice="sendMessage"></project-list>
            </TabPane>
          </Tabs>
        </div>
      </Col>
    </Row>
</template>
<script>
import projectList from "./TabContent.vue";
export default {
  mounted() {
    this.getAllProjects();
  },
  components: {
    projectList
  },
  data() {
    return {
      getFinish: false,
      currentProjectList: [],
      projectShowList: []
    };
  },
  methods: {
    getAllProjects() {
      this.getFinish = true;
      this.axios
        .get("/GeoProblemSolving/project/inquiryAll")
        .then(res => {
          //结束等待
          this.getFinish = false;
          if (res.data === "None") {
            this.currentProjectList = [];
          } else {
            this.judgeMember(res.data);
          }
          this.projectShowList = Object.assign([], this.currentProjectList);
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    filterProjects(type) {
      var allProjectList = Object.assign([], this.currentProjectList);
      if (type != "All") {
        this.projectShowList = allProjectList.filter(function(project) {
          if (project.category == type) {
            return project;
          }
        });
      } else {
        this.projectShowList = allProjectList;
      }
    },
    // 判断是不是成员
    judgeMember(list) {
      //这样的话拿到了用户的id与name
      let projectList = list;
      if (projectList.length != 0) {
        for (var i = 0; i < projectList.length; i++) {
          let managerId = projectList[i].managerId;
          let members = projectList[i].members;
          if (managerId == this.$store.getters.userId) {
            projectList[i]["isManager"] = true;
          } else {
            projectList[i]["isManager"] = false;
          }
          if (members.length != 0) {
            for (var j = 0; j < members.length; j++) {
              if (members[j].userId == this.$store.getters.userId) {
                projectList[i]["isMember"] = true;
                break;
              } else {
                projectList[i]["isMember"] = false;
              }
            }
          } else {
            projectList[i]["isMember"] = false;
          }
        }
        this.$set(this, "currentProjectList", projectList);
      }
    },
    newProject() {
      if (!this.$store.getters.userState) {
        this.$router.push({ name: "Login" });
      } else {
        this.$router.push({ name: "NewProject" });
      }
    },
    sendMessage(recipientId){
      this.$emit("sendNotice", recipientId);
    }
  }
};
</script>

