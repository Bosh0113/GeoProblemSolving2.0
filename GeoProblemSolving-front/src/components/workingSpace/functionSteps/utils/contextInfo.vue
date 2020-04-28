<style scoped>
.subProjectDesc {
  padding: 10px 0;
  min-height: 50px;
  word-break: break-all;
  word-wrap: break-word;
  white-space: pre-line;
}
</style>
<template>
  <Card dis-hover>
    <vue-scroll :ops="scrollOps" style="height:600px">
      <div style="margin: 0 20px">
        <div style="margin-bottom:20px">
          <span style="font-size:12px;font-weight:bold">Problem boundary</span>
          <div v-show="stepInfo.activeStatus" v-if="permissionIdentity('workspace_edit')">
            <Icon
              v-if="!edit1"
              type="ios-create"
              :size="18"
              style="float:right;cursor:pointer"
              title="Edit"
              @click="editBoundary"
            />
            <Icon
              v-else
              type="md-checkbox-outline"
              :size="18"
              style="float:right;cursor:pointer"
              title="Complete"
              @click="editBoundary"
            />
          </div>
          <Divider style="margin:5px 0; background:lightblue" />
          <div v-if="!edit1" class="subProjectDesc" style="overflow-y:auto">{{contextForm.boundary}}</div>
          <template v-else>
            <Input
              v-model="contextForm.boundary"
              placeholder="Please enter the boundry of the geo-problem"
              type="textarea"
              :autosize="{minRows: 2,maxRows: 5}"
              clearable
            />
          </template>
        </div>
        <div style="margin-bottom:20px">
          <span style="font-size:12px;font-weight:bold">Spatiotemporal scale</span>
          <div v-show="stepInfo.activeStatus" v-if="permissionIdentity('workspace_edit')">
            <Icon
              v-if="!edit2"
              type="ios-create"
              :size="18"
              style="float:right;cursor:pointer"
              title="Edit"
              @click="editScale"
            />
            <Icon
              v-else
              type="md-checkbox-outline"
              :size="18"
              style="float:right;cursor:pointer"
              title="Complete"
              @click="editScale"
            />
          </div>
          <Divider style="margin:5px 0; background:lightblue" />
          <div v-if="!edit2" class="subProjectDesc" style="overflow-y:auto">{{contextForm.scale}}</div>
          <template v-else>
            <Input
              v-model="contextForm.scale"
              placeholder="Please enter the spatiotemporal scale"
              type="textarea"
              :autosize="{minRows: 2,maxRows: 5}"
              clearable
            />
          </template>
        </div>
        <div style="margin-bottom:20px">
          <span style="font-size:12px;font-weight:bold">Main methods</span>
          <div v-show="stepInfo.activeStatus" v-if="permissionIdentity('workspace_edit')">
            <Icon
              v-if="!edit3"
              type="ios-create"
              :size="18"
              style="float:right;cursor:pointer"
              title="Edit"
              @click="editMethods"
            />
            <Icon
              v-else
              type="md-checkbox-outline"
              :size="18"
              style="float:right;cursor:pointer"
              title="Complete"
              @click="editMethods"
            />
          </div>
          <Divider style="margin:5px 0; background:lightblue" />
          <div v-if="!edit3" class="subProjectDesc" style="overflow-y:auto">{{contextForm.methods}}</div>
          <template v-else>
            <Input
              v-model="contextForm.methods"
              placeholder="Please enter the main methods"
              type="textarea"
              :autosize="{minRows: 2,maxRows: 5}"
              clearable
            />
          </template>
        </div>
        <div style="margin-bottom:20px">
          <span style="font-size:12px;font-weight:bold">Goals and purposes</span>
          <div v-show="stepInfo.activeStatus" v-if="permissionIdentity('workspace_edit')">
            <Icon
              v-if="!edit4"
              type="ios-create"
              :size="18"
              style="float:right;cursor:pointer"
              title="Edit"
              @click="editPurposes"
            />
            <Icon
              v-else
              type="md-checkbox-outline"
              :size="18"
              style="float:right;cursor:pointer"
              title="Complete"
              @click="editPurposes"
            />
          </div>
          <Divider style="margin:5px 0; background:lightblue" />
          <div v-if="!edit4" class="subProjectDesc" style="overflow-y:auto">{{contextForm.purposes}}</div>
          <template v-else>
            <Input
              v-model="contextForm.purposes"
              placeholder="Please enter the purposes"
              type="textarea"
              :autosize="{minRows: 2,maxRows: 5}"
              clearable
            />
          </template>
        </div>
        <div style="margin-bottom:20px">
          <span style="font-size:12px;font-weight:bold">Difficulties or limitations</span>
          <div v-show="stepInfo.activeStatus" v-if="permissionIdentity('workspace_edit')">
            <Icon
              v-if="!edit5"
              type="ios-create"
              :size="18"
              style="float:right;cursor:pointer"
              title="Edit"
              @click="editLimitations"
            />
            <Icon
              v-else
              type="md-checkbox-outline"
              :size="18"
              style="float:right;cursor:pointer"
              title="Complete"
              @click="editLimitations"
            />
          </div>
          <Divider style="margin:5px 0; background:lightblue" />
          <div
            v-if="!edit5"
            class="subProjectDesc"
            style="overflow-y:auto"
          >{{contextForm.limitations}}</div>
          <template v-else>
            <Input
              v-model="contextForm.limitations"
              placeholder="Please enter limitations during the process of solving the geo-problem"
              type="textarea"
              :autosize="{minRows: 2,maxRows: 5}"
              clearable
            />
          </template>
        </div>
        <div style="margin-bottom:20px">
          <span style="font-size:12px;font-weight:bold">Supplementary information</span>
          <div v-show="stepInfo.activeStatus" v-if="permissionIdentity('workspace_edit')">
            <Icon
              v-if="!edit6"
              type="ios-create"
              :size="18"
              style="float:right;cursor:pointer"
              title="Edit"
              @click="editOthers"
            />
            <Icon
              v-else
              type="md-checkbox-outline"
              :size="18"
              style="float:right;cursor:pointer"
              title="Complete"
              @click="editOthers"
            />
          </div>
          <Divider style="margin:5px 0; background: lightblue" />
          <div v-if="!edit6" class="subProjectDesc" style="overflow-y:auto">{{contextForm.others}}</div>
          <template v-else>
            <Input
              v-model="contextForm.others"
              placeholder="Supplementary information"
              type="textarea"
              :autosize="{minRows: 2,maxRows: 5}"
              clearable
            />
          </template>
        </div>
      </div>
    </vue-scroll>
  </Card>
</template>
<script>
export default {
  props: ["stepInfo", "userRole", "projectInfo"],
  data() {
    return {
      scrollOps: {
        bar: {
          background: "lightgrey"
        }
      },
      contextForm: {
        boundary: "",
        scale: "",
        limitations: "",
        purposes: "",
        methods: "",
        others: ""
      },
      originalContext: {
        boundary: "",
        scale: "",
        limitations: "",
        purposes: "",
        methods: "",
        others: ""
      },
      edit1: false,
      edit2: false,
      edit3: false,
      edit4: false,
      edit5: false,
      edit6: false
    };
  },
  mounted() {
    this.getContext();
  },
  beforeRouteLeave(to, from, next) {
    if (
      this.edit1 ||
      this.edit2 ||
      this.edit3 ||
      this.edit4 ||
      this.edit5 ||
      this.edit6
    ) {
      this.updateContext();
      this.$Notice.info({
        desc: "Autosave."
      });
    } else {
      next();
    }
  },
  methods: {
    permissionIdentity(operation) {
      if (
        this.projectInfo.permissionManager != undefined &&
        operation === "workspace_edit"
      ) {
        if (
          this.userRole == "PManager" &&
          this.projectInfo.permissionManager.workspace_edit.project_manager
        ) {
          return true;
        } else if (
          this.userRole == "Manager" &&
          this.projectInfo.permissionManager.workspace_edit.subproject_manager
        ) {
          return true;
        } else if (
          this.userRole == "Member" &&
          this.projectInfo.permissionManager.workspace_edit.member
        ) {
          return true;
        }
      }
    },
    getContext() {
      this.axios
        .get(
          "/GeoProblemSolving/step/inquiry/" +
            "?key=stepId" +
            "&value=" +
            this.stepInfo.stepId
        )
        .then(res => {
          if (res.data != "Fail") {
            if (JSON.stringify(res.data[0].content) != "{}") {
              this.contextForm = res.data[0].content;
              this.originalContext = JSON.parse(
                JSON.stringify(res.data[0].content)
              );
            }
          } else {
            this.$Notice.info({
              desc: "Get the context description failed!"
            });
          }
        })
        .catch(err => {});
    },
    updateContext() {
      let contextDefinition = new URLSearchParams();
      contextDefinition.append("content.boundary", this.contextForm.boundary);
      contextDefinition.append("content.scale", this.contextForm.scale);
      contextDefinition.append("content.methods", this.contextForm.methods);
      contextDefinition.append(
        "content.limitations",
        this.contextForm.limitations
      );
      contextDefinition.append("content.purposes", this.contextForm.purposes);
      contextDefinition.append("content.others", this.contextForm.others);
      contextDefinition.append("stepId", this.stepInfo.stepId);

      this.axios
        .post("/GeoProblemSolving/step/update", contextDefinition)
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
          } else {
            this.$Message.error("Update subproject failed.");
          }
        });
    },
    editBoundary() {
      if (this.edit1) {
        this.edit1 = false;
        if (this.contextForm.boundary != this.originalContext.boundary) {
          this.updateContext();
          this.originalContext.boundary = this.contextForm.boundary;
        }
      } else {
        this.edit1 = true;
      }
    },
    editScale() {
      if (this.edit2) {
        this.edit2 = false;
        if (this.contextForm.scale != this.originalContext.scale) {
          this.updateContext();
          this.originalContext.scale = this.contextForm.scale;
        }
      } else {
        this.edit2 = true;
      }
    },
    editMethods() {
      if (this.edit3) {
        this.edit3 = false;
        if (this.contextForm.methods != this.originalContext.methods) {
          this.updateContext();
          this.originalContext.methods = this.contextForm.methods;
        }
      } else {
        this.edit3 = true;
      }
    },
    editPurposes() {
      if (this.edit4) {
        this.edit4 = false;
        if (this.contextForm.purposes != this.originalContext.purposes) {
          this.updateContext();
          this.originalContext.purposes = this.contextForm.purposes;
        }
      } else {
        this.edit4 = true;
      }
    },
    editLimitations() {
      if (this.edit5) {
        this.edit5 = false;
        if (this.contextForm.limitations != this.originalContext.limitations) {
          this.updateContext();
          this.originalContext.limitations = this.contextForm.limitations;
        }
      } else {
        this.edit5 = true;
      }
    },
    editOthers() {
      if (this.edit6) {
        this.edit6 = false;
        if (this.contextForm.others != this.originalContext.others) {
          this.updateContext();
          this.originalContext.others = this.contextForm.others;
        }
      } else {
        this.edit6 = true;
      }
    }
  }
};
</script>