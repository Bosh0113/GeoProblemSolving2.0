<template>
  <div style="display: flex; background-color: #eee">
    <Button
      title="Activity tree"
      @click="unfoldTree"
      v-show="treeFold"
      class="foldTreeBtn"
    >
      <Icon
        type="md-arrow-dropright-circle"
        size="20"
        title="Unfold the activity tree"
      />
    </Button>

    <!--  Activity List部分  -->
    <Card dis-hover class="activityCard" id="ActivityTree">
      <h3 slot="title">Activity list</h3>
      <Icon
        slot="extra"
        type="ios-arrow-dropleft"
        size="20"
        title="Fold the activity tree"
        style="cursor: pointer"
        @click="foldTree"
      />
      <vue-scroll :ops="scrollOps" style="height: calc(100vh - 70px)">
        <div style="padding-right: 15px">
          <Tree :data="activityTree" :render="renderStyle"></Tree>
        </div>
      </vue-scroll>
    </Card>

    <!--    使用slot控制是否显示-->
    <Card dis-hover class="workspaceCard" id="ActivityContent">
      <h3 slot="title">
        <Breadcrumb style="display: inline-block" separator=">">
          <BreadcrumbItem v-for="name in cascader" :key="name"
            >{{ name }}
          </BreadcrumbItem>
        </Breadcrumb>
      </h3>
      <div slot="extra">
        <Button
          v-if="
            slctActivity.level > 0 ||
            (slctActivity.level == 0 && slctActivity.type != 'Activity_Default')
          "
          icon="md-create"
          size="small"
          style="margin-top: -10px; margin-right: 10px"
          title="Edit information"
          @click="preEditting()"
          v-show="
            permissionIdentity(
              slctActivity.permission,
              roleIdentity(slctActivity),
              'edit_info'
            )
          "
          >Edit</Button
        >
        <Button
          v-if="
            slctActivity.level > 0 && roleIdentity(slctActivity) == 'visitor'
          "
          icon="md-log-in"
          size="small"
          style="margin-top: -10px; margin-right: 10px"
          title="Apply to join the activity"
          @click="preApplication()"
          >Join</Button
        >
        <Button
          v-if="
            slctActivity.level > 0 && userInfo.userId === slctActivity.creator
          "
          icon="md-trash"
          size="small"
          title="Delete activity"
          style="margin-top: -10px"
          @click="activityDeleteModal = true"
          >Delete</Button
        >
      </div>
      <!-- <Spin size="large" fix v-if="spinShow"></Spin> -->
      <!--      默认contentType为0，根据contType编号进行模块显示-->
      <type-choose
        v-if="contentType == 0"
        :activityInfo="slctActivity"
        :userInfo="userInfo"
        @typeChanged="typeChanged"
        :key="slctActivity.aid"
      ></type-choose>
      <single-activity
        v-else-if="contentType == 1"
        :activityInfo="slctActivity"
        :key="slctActivity.aid"
      ></single-activity>
      <multi-activity
        v-else-if="contentType == 2"
        :activityInfo="slctActivity"
        :userInfo="userInfo"
        :childActivities="childActivities"
        :nameConfirm="nameConfirm"
        :key="slctActivity.aid"
      ></multi-activity>
      <activity-visitor
        v-else-if="contentType == 3"
        :activityInfo="slctActivity"
        :key="slctActivity.aid"
      ></activity-visitor>
    </Card>
    <!-- edit project -->
    <Modal
      v-model="activityEditModal"
      title="Edit information of the activity"
      :mask-closable="false"
    >
      <Form
        ref="editActivityForm"
        :model="editActivityForm"
        :rules="activityEditRule"
        :label-width="120"
      >
        <FormItem label="Name" prop="name" v-if="slctActivity.level != 0">
          <Input
            type="text"
            v-model="editActivityForm.name"
            placeholder="Fill in the name (less than 60 characters) ..."
          ></Input>
        </FormItem>
        <FormItem
          label="Description"
          prop="description"
          v-if="slctActivity.level != 0"
        >
          <Input
            v-model="editActivityForm.description"
            placeholder="Fill in the description..."
            :rows="4"
            type="textarea"
          ></Input>
        </FormItem>
        <FormItem
          label="Activity type:"
          prop="type"
          v-show="slctActivity.type != 'Activity_Default'"
        >
          <Select
            v-model="editActivityForm.type"
            placeholder="Select the type of this activity"
            readonly
          >
            <Option value="Activity_Default">Select type later</Option>
            <Option value="Activity_Unit">Single activity</Option>
            <Option value="Activity_Group">Multi activities</Option>
          </Select>
        </FormItem>
        <FormItem
          label="Purpose:"
          prop="purpose"
          v-show="editActivityForm.type == 'Activity_Unit'"
        >
          <Select
            v-model="editActivityForm.purpose"
            placeholder="Select the purpose of this activity"
            readonly
          >
            <Option v-for="item in purposes" :key="item.index" :value="item"
              >{{ item }}
            </Option>
          </Select>
        </FormItem>
      </Form>
      <div slot="footer" style="display: inline-block">
        <Button
          type="primary"
          @click="editActivity('editActivityForm')"
          style="float: right"
          >OK
        </Button>
        <Button
          @click="activityEditModal = false"
          style="float: right; margin-right: 15px"
          >Cancel
        </Button>
      </div>
    </Modal>
    <Modal
      v-model="applyJoinActivityModal"
      title="Apply to join the activity"
      width="800px"
      :mask-closable="false"
    >
      <div>
        <Form ref="applyJoinForm" :model="applyJoinForm" :label-width="120">
          <FormItem label="Reason" prop="reason">
            <Input v-model="applyJoinForm.reason" :rows="4" type="textarea" />
          </FormItem>
        </Form>
      </div>
      <div slot="footer" style="display: inline-block">
        <Button type="primary" @click="applyJoinActivity()" style="float: right"
          >OK</Button
        >
        <Button
          @click="applyJoinActivityModal = false"
          style="float: right; margin-right: 15px"
          >Cancel
        </Button>
      </div>
    </Modal>
    <Modal
      v-model="activityDeleteModal"
      title="Delete the current activity"
      @on-ok="delActivity"
      ok-text="Yes"
      cancel-text="Think again..."
    >
      <h3>Do you really want to delete this activity?</h3>
      <h3 style="color: red; margin-top: 10px">
        * The selected activity and its all child activities will be deleted!
      </h3>
    </Modal>
  </div>
</template>
<script>
import * as userRoleJS from "@/api/userRole.js";
import typeChoose from "./activity/typeChoose.vue";
import singleActivity from "./activity/singleActivity.vue";
import multiActivity from "./activity/multiActivity.vue";
import activityVisitor from "./activity/activityVisitor.vue";

export default {
  components: {
    typeChoose,
    singleActivity,
    multiActivity,
    activityVisitor,
  },
  data() {
    return {
      scrollOps: {
        bar: {
          background: "lightgrey",
        },
      },
      treeFold: false,
      cascader: [],
      nameConfirm: [],
      activityTree: [],
      projectInfo: parent.vm.projectInfo,
      userInfo: JSON.parse(sessionStorage.getItem("userInfo")),
      slctActivity: {},
      childActivities: [],
      parentActivity: {},
      editActivityForm: {
        name: "",
        description: "",
        parent: "",
        creator: "",
        level: -1,
        permission: JSON.stringify(userRoleJS.getDefault()),
        type: "Activity_Default",
        purpose: "Multi-purpose",
      },
      activityEditRule: {
        name: [
          {
            required: true,
            message: "The name should not be empty and more than 60 characters",
            trigger: "blur",
            type: "string",
            max: 60,
          },
        ],
        description: [
          {
            required: true,
            message: "The description should not be empty",
            trigger: "blur",
          },
        ],
      },
      applyJoinForm: {
        reason: "",
      },
      activityEditModal: false,
      applyJoinActivityModal: false,
      activityDeleteModal: false,
      contentType: -1,
      purposes: [
        "Multi-purpose",
        "Context definition & resource collection",
        "Data processing",
        "Data analysis",
        "Data visualization",
        "Geo-analysis model construction",
        "Model effectiveness evaluation",
        "Geographical simulation",
        "Decision making",
      ],
      // spinShow: false,
    };
  },
  beforeRouteEnter: (to, from, next) => {
    next((vm) => {
      if (!vm.$store.getters.userState || vm.$store.getters.userId == "") {
        vm.$router.push({ name: "Login" });
      }
    });
    next();
  },
  mounted() {
    this.locateActivity();

    Array.prototype.contains = function (obj) {
      var i = this.length;
      while (i--) {
        if (
          (this[i].userId != undefined && this[i].userId === obj.userId) ||
          (this[i].tid != undefined && this[i].tid === obj.tid) ||
          (this[i].tsId != undefined && this[i].tsId === obj.tsId) ||
          (this[i].id != undefined && this[i].id === obj.id) ||
          (this[i].aid != undefined && this[i].aid === obj.aid)
        ) {
          return true;
        }
      }
      return false;
    };
  },
  methods: {
    roleIdentity(activity) {
      return userRoleJS.roleIdentify(activity.members, this.userInfo.userId);
    },
    permissionIdentity(permission, role, operation) {
      if(permission == undefined) permission = JSON.stringify(userRoleJS.getDefault());
      if (operation == "observe") {
        if (JSON.parse(permission).observe.visitor == "Yes") return true;
        else if (JSON.parse(permission).observe.visitor == "No") return false;
        else {
          return this.getParentPermission();
        }
      } else if (operation == "auto_join") {
        if (JSON.parse(permission).auto_join.visitor == "Yes") return true;
        else if (JSON.parse(permission).auto_join.visitor == "No") return false;
        else {
          return this.getParentPermission();
        }
      } else {
        return userRoleJS.permissionIdentity(
          JSON.parse(permission),
          role,
          operation
        );
      }
    },
    async getParentPermission() {
      let url = "";
      if (this.slctActivity.level == 0 || this.slctActivity.level == 1) {
        return this.projectInfo.members.contains(this.userInfo);
      } else if (this.slctActivity.level == 2) {
        return this.parentActivity.members.contains(this.userInfo);
      }
    },
    renderStyle(h, { root, node, data }) {
      let props = {};
      let style = {};
      let on = {};
      let name = data.name;

      if (this.slctActivity.aid !== data.aid) {
        style = {
          width: "100%",
        };
        on = {
          click: () => {
            parent.location.href =
              "/GeoProblemSolving/projectInfo/" +
              this.projectInfo.aid +
              "?content=workspace&aid=" +
              data.aid +
              "&level=" +
              data.level;
          },
        };
      } else {
        style = {
          width: "100%",
          backgroundColor: "lightblue",
          cursor: "default",
        };
      }

      return h(
        "span",
        {
          style: {
            display: "inline-block",
            width: "100%",
          },
        },
        [
          h("span", [
            h(
              "Button",
              {
                props: props,
                style: style,
                on: on,
                attrs: { title: name },
              },
              [
                h(
                  "div",
                  {
                    style: {
                      overflow: "hidden",
                      maxWidth: "120px",
                      textOverflow: "ellipsis",
                      margin: "0 auto",
                    },
                  },
                  name
                ),
              ]
            ),
          ]),
        ]
      );
    },
    foldTree() {
      this.treeFold = true;
      document.getElementById("ActivityTree").style.width = 0;
      document.getElementById("ActivityContent").style.width =
        window.innerWidth - 10 + "px";
    },
    unfoldTree() {
      this.treeFold = false;
      document.getElementById("ActivityTree").style.width = 250 + "px";
      document.getElementById("ActivityContent").style.width =
        window.innerWidth - 260 + "px";
    },
    locateActivity() {
      let content = this.getURLParameter("content");
      let aid = this.getURLParameter("aid");
      let level = this.getURLParameter("level");
      this.operationApi.getActivityDoc(aid);

      let url = "";
      if (aid == undefined || level == undefined) {
        url =
          "/GeoProblemSolving/projectInfo/" +
          this.projectInfo.aid +
          "?content=workspace";
        this.getProjectInfo();
      } else {
        url =
          "/GeoProblemSolving/projectInfo/" +
          this.projectInfo.aid +
          "?content=workspace&aid=" +
          aid +
          "&level=" +
          level;
        if (level > 1) {
          this.getActivityBranch(aid);
        } else if (level == 1) {
          this.getSubprojectBranch(aid);
        } else {
          this.getProjectInfo();
        }
      }
      if (content == "workspace") {
        parent.vm.workspaceUrl = url;
      }
    },
    getURLParameter(name) {
      var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
      var r = parent.location.search.substr(1).match(reg);

      if (r != null) {
        return unescape(r[2]);
      }
      return null;
    },
    getProjectInfo() {
      this.cascader = [this.projectInfo.name];
      this.activityTree = [];
      if (this.projectInfo.type == "Activity_Group") {
        this.axios
          .get(
            "/GeoProblemSolving/project/" + this.projectInfo.aid + "/children"
          )
          .then((res) => {
            if (res.data.code == 0) {
              let children = res.data.data;
              this.childActivities = children;
              this.nameConfirm = [];

              // 处理掉异常
              for (let i = 0; i < children.length; i++) {
                children[i].children = [];
                this.nameConfirm.push(children[i].name);
              }

              var root = Object.assign({}, this.projectInfo);
              this.nameConfirm.push(root.name);
              root["expand"] = true;
              root["children"] = children;

              // update activity tree
              this.activityTree.push(root);
              this.slctActivity = root;
              this.parentActivity = null;
              this.setContent(this.slctActivity);
            } else {
              console.log(res.data.msg);
            }
          })
          .catch((err) => {
            throw err;
          });
      } else {
        let root = Object.assign({}, this.projectInfo);
        this.activityTree.push(root);
        this.slctActivity = root;
        this.nameConfirm.push(root.name);
        this.parentActivity = null;
        this.setContent(this.slctActivity);
      }
    },
    getSubprojectBranch(aid) {
      // init
      this.cascader = [];
      this.activityTree = [];
      // url
      let url = "/GeoProblemSolving/subproject/" + aid + "/lineage";

      this.axios
        .get(url)
        .then((res) => {
          if (res.data.code == 0) {
            let branch = res.data.data;
            this.childActivities = branch.children;

            this.buildActivityTree(
              branch.ancestors,
              branch.brothers,
              branch.children
            );
          } else {
            console.log(res.data.msg);
          }
        })
        .catch((err) => {
          console.log(err.data);
        });
    },
    getActivityBranch(aid) {
      // init
      this.cascader = [];
      this.activityTree = [];
      // url
      let url = "/GeoProblemSolving/activity/" + aid + "/lineage";

      this.axios
        .get(url)
        .then((res) => {
          if (res.data.code == 0) {
            let branch = res.data.data;
            this.childActivities = branch.children;

            this.buildActivityTree(
              branch.ancestors,
              branch.brothers,
              branch.children
            );
          } else {
            console.log(res.data.msg);
          }
        })
        .catch((err) => {
          console.log(err.data);
        });
    },
    buildActivityTree(ancestors, brothers, children) {
      this.activityTree = [];
      this.nameConfirm = [];
      // child activities normalization
      if (children.length > 0) {
        for (var i = 0; i < children.length; i++) {
          children[i].children = [];
          this.nameConfirm.push(children[i].name);
        }
        ancestors[0].children = children;
        ancestors[0]["expand"] = true;
      }

      // brother activities normalization
      for (var i = 0; i < brothers.length; i++) {
        if (brothers[i].aid !== ancestors[0].aid) {
          brothers[i].children = [];
          this.nameConfirm.push(brothers[i].name);
        }
      }
      ancestors[1].children = brothers;

      // ancestor activities normalization
      for (let i = 1; i < ancestors.length; i++) {
        let parentActivity = ancestors[i];
        this.nameConfirm.push(parentActivity.name);
        let current = ancestors[i - 1];

        let childrenNum = parentActivity.children.length;
        parentActivity["expand"] = true;

        for (let j = 0; j < childrenNum; j++) {
          if (
            Object.prototype.toString.call(parentActivity.children[j]) ==
            "[object String]"
          ) {
            if (parentActivity.children[j] == current.aid) {
              parentActivity.children = [current];
              break;
            }
          } else if (
            Object.prototype.toString.call(parentActivity.children[j]) ==
            "[object Object]"
          ) {
            if (parentActivity.children[j].aid == current.aid) {
              parentActivity.children[j] = current;
              break;
            }
          }
        }
      }

      // update activity tree
      var root = ancestors[ancestors.length - 1];
      this.activityTree.push(root);
      this.slctActivity = ancestors[0];
      this.parentActivity = ancestors[1];
      this.setContent(this.slctActivity);

      // Cascader names
      for (let i = ancestors.length - 1; i >= 0; i--) {
        this.cascader.push(ancestors[i].name);
      }
    },
    typeChanged(type) {
      this.slctActivity.type = type;
      this.setContent(this.slctActivity);
    },
    setContent(activity) {
      if (
        this.roleIdentity(activity) == "visitor" &&
        !this.permissionIdentity(
          this.slctActivity.permission,
          "visitor",
          "observe"
        )
      ) {
        this.contentType = 3;
      } else {
        if (activity.type == "Activity_Default") {
          this.contentType = 0;
        } else if (activity.type == "Activity_Unit") {
          this.contentType = 1;
        } else if (activity.type == "Activity_Group") {
          this.contentType = 2;
        }
      }
    },
    preEditting() {
      let children = [];
      let activity = Object.assign({}, this.slctActivity);
      if (activity.children != undefined) {
        for (let i = 0; i < activity.children.length; i++) {
          children.push(activity.children[i].aid);
        }
        activity.children = children;
      }
      this.editActivityForm = activity;
      this.activityEditModal = true;
    },
    editActivity(name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          if (this.editActivityForm.type != this.slctActivity.type) {
            if (this.slctActivity.type == "Activity_Unit") {
              this.editActivityForm.purpose = "Multi-purpose";
            } else if (this.slctActivity.type == "Activity_Group") {
              if (
                this.slctActivity.children != undefined &&
                this.slctActivity.children.length > 0
              ) {
                this.$Notice.info({
                  desc:
                    "Please make sure no child activity existing, before changing the type of current activity.",
                });
                this.activityEditModal = false;
                return;
              } else {
                this.editActivityForm.pathway = [];
              }
            }
          }
          // url
          let url = "";
          if (this.slctActivity.level == 1) {
            url = "/GeoProblemSolving/subproject/" + this.slctActivity.aid;
          } else if (this.slctActivity.level > 1) {
            url = "/GeoProblemSolving/activity/" + this.slctActivity.aid;
          } else {
            url = "/GeoProblemSolving/project/" + this.slctActivity.aid;
          }
          this.axios
            .put(url, this.editActivityForm)
            .then((res) => {
              if (res.data.code == 0) {
                this.$Notice.info({ title: "Result", desc: "Success!" });
                // update activity tree
                this.slctActivity.name = this.editActivityForm.name;
                this.slctActivity.description = this.editActivityForm.description;
                this.slctActivity.type = this.editActivityForm.type;
                this.slctActivity.purpose = this.editActivityForm.purpose;
                // cascader
                let index = this.cascader.length - 1;
                this.$set(this.cascader, index, this.editActivityForm.name);
                // change content
                this.setContent(this.slctActivity);
              } else {
                this.$Notice.info({ title: "Result", desc: res.data.msg });
              }
            })
            .catch((err) => {
              throw err;
            });

          this.activityEditModal = false;
        }
      });
    },
    preApplication() {
      if (
        this.permissionIdentity(
          this.slctActivity.permission,
          "visitor",
          "auto_join"
        )
      ) {
        this.joinActivity();
      } else {
        this.applyJoinActivityModal = true;
      }
    },
    joinActivity() {
      let url = "";
      if (this.slctActivity.level == 1) {
        url =
          "/GeoProblemSolving/subproject/" +
          this.slctActivity.aid +
          "/user?userId=" +
          this.userInfo.userId;
      } else if (this.slctActivity.levell > 1) {
        url =
          "/GeoProblemSolving/activity/" +
          this.slctActivity.aid +
          "/user?userId=" +
          this.userInfo.userId;
      } else {
        return;
      }

      this.axios
        .post(url)
        .then((res) => {
          if (res.data.code == 0) {
            parent.location.href =
              "/GeoProblemSolving/projectInfo/" +
              this.projectInfo.aid +
              "?content=workspace&aid=" +
              this.slctActivity.aid +
              "&level=" +
              this.slctActivity.level;
          } else if (res.data.code == -3) {
            this.$Notice.info({
              desc: "You has already been a member of the activity.",
            });
          } else {
            console.log(res.data.msg);
          }
        })
        .catch((err) => {
          throw err;
        });
    },
    applyJoinActivity() {
      let managers = userRoleJS.getMemberByRole(this.slctActivity, "manager");
      for (var i = 0; i < managers.length; i++) {
        let notice = {
          recipientId: managers[i],
          type: "apply",
          content: {
            title:
              "Application of joining the activity: " + this.slctActivity.name,
            activityId: this.slctActivity.aid,
            activityName: this.slctActivity.name,
            activityLevel: this.slctActivity.level,
            userEmail: this.userInfo.email,
            userName: this.userInfo.name,
            userId: this.userInfo.userId,
            description: this.applyJoinForm.reason,
            approve: "unknow",
          },
        };
        this.sendNotice(this.slctActivity, notice);
      }
    },
    sendNotice(activity, notice) {
      this.axios
        .post("/GeoProblemSolving/notice/save", notice)
        .then((result) => {
          if (result.data == "Success") {
            parent.vm.$emit("sendNotice", notice.recipientId);
            this.$Notice.info({desc: "Send application successfully."})
            this.applyJoinActivityModal = false;
          } else {
            this.$Message.error("Notice fail.");
          }
        })
        .catch((err) => {
          throw err;
        });
    },
    delActivity() {
      if (this.userInfo.userId !== this.slctActivity.creator) return;
      let url = "";
      let aid = this.slctActivity.aid;
      if (this.slctActivity.level == 1) {
        url = "/GeoProblemSolving/subproject?aid=" + aid;
      } else if (this.slctActivity.level > 1) {
        url = "/GeoProblemSolving/activity?aid=" + aid;
      }
      this.axios
        .delete(url)
        .then((res) => {
          if (res.data.code == 0) {
            
            this.operationApi.activityRecord("remove", this.userInfo.userId, this.slctActivity);

            parent.location.href =
              "/GeoProblemSolving/projectInfo/" +
              this.projectInfo.aid +
              "?content=workspace&aid=" +
              this.slctActivity.parent +
              "&level=" +
              (this.slctActivity.level - 1).toString();
          } else {
            console.log(res.data.msg);
          }
        })
        .catch((err) => {
          throw err;
        });
    },
  },
};
</script>
<style scoped>
.foldTreeBtn {
  padding: 5px 8px 6px;
  height: calc(100vh - 10px);
  margin-right: 5px;
  border: 0px;
}

.activityCard {
  margin-right: 5px;
  width: 250px;
  height: calc(100vh - 10px);
  overflow-y: auto;
  border: 0;
}

.activityCard >>> .ivu-card-body {
  padding: 5px 10px;
}

.workspaceCard {
  width: calc(100vw - 260px);
  height: calc(100vh - 10px);
  background-color: white;
  border: 0;
  overflow-x: auto;
}

.workspaceCard >>> .ivu-card-body {
  padding: 0px 10px;
}
</style>
