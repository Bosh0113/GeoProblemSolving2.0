<style scoped>
.picscreen {
  position: relative;
  padding-top: 10px;
}

.picbg {
  background-image: url("../../../assets/images/condefbg.png");
  background-size: cover;
  position: absolute;
  height: 100px;
  width: 100%;
  background-color: #8b8b8b;
  top: 0;
  left: 0;
}

.home_content >>> .ivu-breadcrumb {
  color: rgb(180, 197, 207);
  /* min-height: 500px; */
}
.home_content >>> .ivu-breadcrumb > span:last-child {
  color: rgb(180, 197, 207);
}

.stepName {
  text-align: center;
  font-size: 1.2rem;
  height: 20px;
  color: white;
}
.stepName p {
  margin: 0 auto;
  font-size: 1rem;
  height: 20px;
  color: white;
  word-break: break-word;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  max-width: 400px;
}

.onlineListBtn {
  position: absolute;
  top: 70%;
  right: 10%;
  /* width: 100px */
}
.subproject-back >>> .ivu-breadcrumb-item-link {
  color: white;
}

.breadCrumb {
  margin-left: 1%;
}
</style>
<template>
    <div class="picscreen">
      <div class="picbg"></div>

      <div class="home_content">
        <Row>
          <!-- 需要修改样式 -->
          <div class="breadCrumb">
            <Breadcrumb>
              <!-- <BreadcrumbItem :to="toProjectPage">Project</BreadcrumbItem> -->
              <BreadcrumbItem :to="toSubProjectPage" class="subproject-back">Subproject</BreadcrumbItem>
              <BreadcrumbItem style="color: white">{{category}}</BreadcrumbItem>
            </Breadcrumb>
          </div>
          <div class="stepName">
            <strong>{{stepInfo.name}}</strong>
            <p :title="stepInfo.description">{{stepInfo.description}}</p>
          </div>
          <div class="onlineListBtn">
            <Button @click="drawerValue = true" type="default" ghost>Participants</Button>
            <template v-if="userRole == 'Manager'">
              <Button @click="modifyStepShow" style="margin-left:20px" ghost>Modify Step</Button>
            </template>
            <step-change></step-change>
          </div>
        </Row>
      </div>
      <Drawer title="Participants" :closable="false" v-model="drawerValue">
        <online-participant :sub-project-id="subProjectInfo.subProjectId" :room-id="stepId"></online-participant>
        <div class="toChatroom" style="position:absolute; left:25%;bottom:5%">
          <Button @click.native="toolPanel('chat')" type="success">Go to chatroom</Button>
        </div>
      </Drawer>
      <Modal v-model="modifyStep" title="Modify step name and description">
        <Form :label-width="120" label-position="left" :model="stepForm">
          <FormItem label="Step Name" prop="name">
            <Input
              v-model="stepForm.name"
              :autosize="{minRows: 1,maxRows: 3}"
            />
          </FormItem>
          <FormItem label="Step Description" prop="description">
            <Input
              v-model="stepForm.description"
              type="textarea"
              :rows="4"
            />
          </FormItem>
        </Form>
        <div slot="footer">
          <Button @click="cancelModifyStep">Cancel</Button>
          <Button type="primary" @click="submitModifyStep">Modify</Button>
        </div>
      </Modal>
    </div>
</template>

<script>
import onlineParticipant from "./onlineParticipants";
import stepChange from "./stepChange"
export default {
  components: {
    onlineParticipant,
    stepChange
  },
  props: ["stepInfo", "subProjectInfo", "userRole"],
  data() {
    return {
      stepId: this.$route.params.id,
      category:"Context definition & resource collection",
      toSubProjectPage: "",
      // online drawer
      drawerValue: false,
      modifyStep: false,
      stepForm: {
        name: "",
        description: ""
      },
    };
  },
  mounted(){
    this.checkCategory();
    this.toSubProjectPage =  "/project/" + this.subProjectInfo.subProjectId + "/subproject";
  },
  methods: {
    checkCategory(){
      if(this.stepInfo!=null){
        this.category = this.stepInfo.type;
      }
    },
    modifyStepShow(){
      this.stepForm.name = this.stepInfo.name;
      this.stepForm.description = this.stepInfo.description;
      this.modifyStep = true;
    },
    submitModifyStep() {
      let obj = new URLSearchParams();
      obj.append("name", this.stepForm.name);
      obj.append("description", this.stepForm.description);
      obj.append("stepId", this.stepId);

      this.axios
        .post("/GeoProblemSolving/step/update", obj)
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({
              name: "Login"
            });
          } else if (res.data != "Fail") {
            this.$Notice.info({
              desc: "Update successfully!"
            });
            this.stepInfo.name = this.stepForm.name;
            this.stepInfo.description = this.stepForm.description;
          } else {
            this.$Message.error("Update step failed.");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
      this.modifyStep = false;
    },
    cancelModifyStep() {
      this.modifyStep = false;
    },
    toolPanel(type) {
      // if (this.userRole != "Visitor") {
      this.axios
        .post("/GeoProblemSolving/user/state")
        .then(res => {
          if (!res.data) {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else {
            var toolURL = "";
            let toolName = "";if (type == "chat") {
              toolURL =
                '<iframe src="' +
                "http://" +
                this.$store.state.IP_Port +
                '/GeoProblemSolving/chat" style="width: 100%;height:100%"></iframe>';
              toolName = "Chatroom";
            }

            let panel = jsPanel.create({
              theme: "success",
              headerTitle: toolName,
              footerToolbar: '<p style="height:10px"></p>',
              contentSize: "1200 600",
              content: toolURL,
              disableOnMaximized: true,
              dragit: {
                containment: 5
              },
              callback: function() {
                // this.content.style.padding = "20px";
              }
            });
            panel.resizeit("disable");
            $(".jsPanel-content").css("font-size", "0");
            this.panelList.push(panel);
          }
        })
        .catch(err => {
          console.log("Get user info fail.");
        });
    }
  }
}
</script>
