<template>
  <div style="display: flex; background-color: lightgrey">
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
    <Card dis-hover class="workspaceCard" id="ActivityContent">
      <h3 slot="title">
        <Breadcrumb style="display: inline-block" separator=">">
          <BreadcrumbItem v-for="name in cascader" :key="name">{{
            name
          }}</BreadcrumbItem>
        </Breadcrumb>
      </h3>
      <div slot="extra" v-show="slctActivity.level > 0">
        <!-- <Button
          icon="md-create"
          size="small"
          style="margin-top:-10px; margin-right: 10px"
          title="Edit information"
          @click="activityEditModal = true"
          v-show="permissionIdentity(slctActivity.permission, roleIdentity(slctActivity), 'edit_info')"
        >Edit</Button>
        <Button
          icon="md-trash"
          size="small"
          title="Delete activity"
          style="margin-top:-10px"
          @click="activityDeleteModal = true"
          v-show="userInfo.userId === slctActivity.creator"
        >Delete</Button>-->
        <Button
          icon="md-create"
          size="small"
          style="margin-top: -10px; margin-right: 10px"
          @click="preEditting()"
          >Edit</Button
        >
        <Button
          icon="md-trash"
          size="small"
          style="margin-top: -10px"
          @click="activityDeleteModal = true"
          >Delete</Button
        >
      </div>
      <!-- <Spin size="large" fix v-if="spinShow"></Spin> -->
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
        :key="slctActivity.aid"
      ></multi-activity>
      <activity-show
        v-else-if="contentType == 3"
        :activityInfo="slctActivity"
        :key="slctActivity.aid"
      ></activity-show>
    </Card>
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
        <FormItem label="Name" prop="name">
          <Input
            type="text"
            v-model="editActivityForm.name"
            placeholder="Fill in the name (less than 60 characters) ..."
          ></Input>
        </FormItem>
        <FormItem label="Description" prop="description">
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
            <Option v-for="item in purposes" :key="item.index" :value="item">{{
              item
            }}</Option>
          </Select>
        </FormItem>
      </Form>
      <div slot="footer" style="display: inline-block">
        <Button
          type="primary"
          @click="editActivity('editActivityForm')"
          style="float: right"
          >OK</Button
        >
        <Button
          @click="activityEditModal = false"
          style="float: right; margin-right: 15px"
          >Cancel</Button
        >
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
    <!-- <Modal
      v-model="createActivityModel"
      title="Create a new activity"
      width="800"
      :mask-closable="false"
    >
      <Form
        ref="activityForm"
        :model="activityForm"
        :rules="activityCreateRule"
        :label-width="120"
      >
        <FormItem label="Name" prop="name">
          <Input
            type="text"
            v-model="activityForm.name"
            placeholder="Fill in the name (less than 60 characters) ..."
          ></Input>
        </FormItem>
        <FormItem label="Description" prop="description">
          <Input
            v-model="activityForm.description"
            placeholder="Fill in the description..."
            :rows="4"
            type="textarea"
          ></Input>
        </FormItem>
        <FormItem label="Activity type:" prop="type">
          <Select
            v-model="activityForm.type"
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
          v-show="activityForm.type == 'Activity_Unit'"
        >
          <Select
            v-model="activityForm.purpose"
            placeholder="Select the purpose of this activity"
            readonly
          >
            <Option v-for="item in purposes" :key="item.index" :value="item">{{
              item
            }}</Option>
          </Select>
        </FormItem>
      </Form>
      <div slot="footer" style="display: inline-block">
        <Button
          type="primary"
          @click="createActivity('activityForm')"
          style="float: right"
          >OK</Button
        >
        <Button
          @click="createActivityModel = false"
          style="float: right; margin-right: 15px"
          >Cancel</Button
        >
      </div>
    </Modal> -->
  </div>
</template>
<script>
import * as userRoleJS from "./../../api/userRole.js";
import typeChoose from "./activity/typeChoose.vue";
import singleActivity from "./activity/singleActivity.vue";
import multiActivity from "./activity/multiActivity.vue";
import activityShow from "./activity/activityShow.vue";
export default {
  components: {
    typeChoose,
    singleActivity,
    multiActivity,
    activityShow,
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
      activityTree: [],
      projectInfo: parent.vm.projectInfo,
      userInfo: JSON.parse(sessionStorage.getItem("userInfo")),
      slctActivity: {},
      expandNode: {}, // 使用引用传递，记录expand的位置，对activity tree 进行修改
      parentNode: {}, // 记录所创建/选择activity的父节点位置，对activity tree 进行修改
      // activityForm: {
      //   name: "",
      //   description: "",
      //   parent: "",
      //   creator: "",
      //   level: -1,
      //   permission: JSON.stringify(userRoleJS.getDefault()),
      //   type: "Activity_Default",
      //   purpose: "Others",
      // },
      editActivityForm: {
        name: "",
        description: "",
        parent: "",
        creator: "",
        level: -1,
        permission: JSON.stringify(userRoleJS.getDefault()),
        type: "Activity_Default",
        purpose: "Others",
      },
      // activityCreateRule: {
      //   name: [
      //     {
      //       required: true,
      //       message: "The name should not be empty and more than 60 characters",
      //       trigger: "blur",
      //       type: "string",
      //       max: 60,
      //     },
      //   ],
      //   description: [
      //     {
      //       required: true,
      //       message: "The description should not be empty",
      //       trigger: "blur",
      //     },
      //   ],
      //   purpose: [
      //     {
      //       required: true,
      //       message: "The purpose should not be empty",
      //       trigger: "blur",
      //     },
      //   ],
      // },
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
      // createActivityModel: false,
      activityEditModal: false,
      activityDeleteModal: false,
      contentType: -1,
      purposes: [
        "Others",
        "Context definition & resource collection",
        "Data processing",
        "Data visualization",
        "Geographic model construction",
        "Model effectiveness evaluation",
        "Geographical simulation",
        "Quantitative and qualitative analyses",
        "Decision-making and management",
      ],
      // spinShow: false,
    };
  },
  beforeRouteEnter: (to, from, next) => {
    // next((vm) => {
    //   if (!vm.$store.getters.userState || vm.$store.getters.userId == "") {
    //     vm.$router.push({ name: "Login" });
    //   }
    // });
    next();
  },
  mounted() {
    this.locateActivity();
  },
  methods: {
    renderStyle(h, { root, node, data }) {
      let props = {};
      let style = {};
      let on = {};
      let name = data.name + " (Level:" +data.level+")";

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
              },
              name
            ),
          ]),
          h("span", {
            style: {
              display: "inline-block",
              float: "right",
              marginRight: "32px",
            },
          }),
        ]
      );
    },
    foldTree() {
      this.treeFold = true;
      document.getElementById("ActivityTree").style.width = 0;
      document.getElementById("ActivityTree").style.margin = 0;
      document.getElementById("ActivityContent").style.width =
        window.innerWidth - 10 + "px";
    },
    unfoldTree() {
      this.treeFold = false;
      document.getElementById("ActivityTree").style.width = 250 + "px";
      document.getElementById("ActivityTree").style.margin = 5 + "px";
      document.getElementById("ActivityContent").style.width =
        window.innerWidth - 260 + "px";
    },
    locateActivity() {
      let aid = this.getURLParameter("aid");
      let level = this.getURLParameter("level");

      if (aid == undefined || level == undefined) {
        this.getProjectInfo();
      } else {
        if (level > 1) {
          this.getActivityBranch(aid);
        } else if (level == 1) {
          this.getSubprojectBranch(aid);
        } else {
          this.getProjectInfo();
          parent.history.replaceState(
            null,
            null,
            "/GeoProblemSolving/projectInfo/" +
              this.projectInfo.aid +
              "?content=workspace"
          );
        }
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

              // 处理掉异常
              for (let i = 0; i < children.length; i++) {
                if (children[i].children != undefined) {
                  children[i].children = [];
                }
              }

              let root = Object.assign({}, this.projectInfo);
              root["expand"] = true;
              root["children"] = children;

              // update activity tree
              this.activityTree = [root];
              this.slctActivity = root;
              this.setContent(this.slctActivity, "init");
            } else {
              console.log(res.data.msg);
            }
          })
          .catch((err) => {
            console.log(err.data);
          });
      } else {
        let root = Object.assign({}, this.projectInfo);
        this.activityTree.push(root);
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
      // child activities normalization
      if (children.length > 0) {
        for (var i = 0; i < children.length; i++) {
          children[i].children = [];
        }
        ancestors[0].children = children;
        ancestors[0]["expand"] = true;
      }

      // brother activities normalization
      for (var i = 0; i < brothers.length; i++) {
        if (brothers[i].aid !== ancestors[0].aid) {
          brothers[i].children = [];
        }
      }
      ancestors[1].children = brothers;

      // ancestor activities normalization
      for (let i = 1; i < ancestors.length; i++) {
        let parentActivity = ancestors[i];
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
      let root = ancestors[ancestors.length - 1];
      this.activityTree = [root];
      this.slctActivity = ancestors[0];
      this.setContent(this.slctActivity, "init");

      // Cascader names
      for (let i = ancestors.length - 1; i >= 0; i--) {
        this.cascader.push(ancestors[i].name);
      }
    },
    typeChanged(type) {
      this.slctActivity.type = type;
      this.setContent(this.slctActivity, "type");
    },
    expandActivityTree(activity) {
      if (activity.type == "Activity_Group") {
        let url = "";
        if (activity.level == 1) {
          url = "/GeoProblemSolving/subproject/" + activity.aid + "/children";
        } else if (activity.level > 1) {
          url = "/GeoProblemSolving/activity/" + activity.aid + "/children";
        }

        this.axios
          .get(url)
          .then((res) => {
            if (res.data.code == 0) {
              // children
              let children = res.data.data;

              // 处理掉异常
              for (let i = 0; i < children.length; i++) {
                if (children[i].children != undefined) {
                  children[i].children = [];
                }
              }
              // expand activity tree
              this.expandNode["expand"] = true;
              this.expandNode["children"] = children;
              this.setContent(activity, "switch");
            } else {
              console.log(res.data.msg);
            }
          })
          .catch((err) => {
            console.log(err.data);
          });
      }
    },
    // switchActivity(root, node, activity) {
    //   // if (
    //   //   this.roleIdentity(activity) != "visitor" ||
    //   //   activity.permission.observe == "Yes"
    //   // ) {
    //   //content
    //   if (activity.level > 0) {
    //     this.parentNode = root[node.parent].node;
    //   } else {
    //     this.parentNode = {};
    //   }
    //   this.slctActivity = activity;
    //   this.getCascader(root, node);
    //   // expand
    //   if (
    //     activity.type == "Activity_Group" &&
    //     (activity.children == undefined || activity.children.length == 0)
    //   ) {
    //     this.expandActivityTree(activity);
    //     this.expandNode = activity;
    //   }
    //   // } else {
    //   //   this.contentType = 3;
    //   // }
    // },
    setContent(activity, operation) {
      if (activity.type == "Activity_Default") {
        this.contentType = 0;
      } else if (activity.type == "Activity_Unit") {
        this.contentType = 1;
      } else if (activity.type == "Activity_Group") {
        this.contentType = 2;
      }
      // if (operation !== "init") {

      //   parent.location.href =
      //     "/GeoProblemSolving/projectInfo/" +
      //     this.projectInfo.aid +
      //     "?content=workspace&aid=" +
      //     activity.aid +
      //     "&index=" +
      //     index;
      // }
    },
    // preCreation(root, node) {
    //   this.parentNode = root[node.parent].node;

    //   this.activityForm.parent = this.parentNode.aid;
    //   this.activityForm.creator = this.userInfo.userId;
    //   this.activityForm.level = this.parentNode.level + 1;

    //   this.createActivityModel = true;
    // },
    createActivity(name) {
      // this.$refs[name].validate((valid) => {
      //   if (valid) {
      //     let url = "";
      //     if (this.activityForm.level == 1) {
      //       // subproject
      //       url = "/GeoProblemSolving/subproject";
      //     } else if (this.activityForm.level > 1) {
      //       // activity
      //       url = "/GeoProblemSolving/activity";
      //     }
      //     this.axios
      //       .post(url, this.activityForm)
      //       .then((res) => {
      //         if (res.data.code == 0) {
      //           // change activity tree
      //           for (let i = 0; i < this.parentNode.children.length; i++) {
      //             if (this.parentNode.children[i].aid == "add") {
      //               this.parentNode.children[i] = res.data.data;
      //             }
      //           }
      //           // this.parentNode.children.push({ aid: "add" });
      //           this.slctActivity = res.data.data;
      //           // change content
      //           this.setContent(this.slctActivity, "create");
      //           this.activityForm = {
      //             name: "",
      //             description: "",
      //             parent: "",
      //             creator: "",
      //             level: -1,
      //             permission: JSON.stringify(userRoleJS.getDefault()),
      //             type: "Activity_Default",
      //             purpose: "Others",
      //           };
      //         } else {
      //           console.log(res.data.msg);
      //         }
      //       })
      //       .catch((err) => {
      //         console.log(err);
      //       });
      //     this.createActivityModel = false;
      //   }
      // });
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
              this.editActivityForm.purpose = "Others";
            } else if (this.slctActivity.type == "Activity_Group") {
              if (
                this.slctActivity.children != undefined &&
                this.slctActivity.children.length > 1
              ) {
                this.$Notice.info({
                  title: "Fail",
                  desc:
                    "Please make sure no child activity, before change the type of current activity.",
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
                this.setContent(this.slctActivity, "edit");
              } else {
                this.$Notice.info({ title: "Result", desc: res.data.msg });
              }
            })
            .catch((err) => {
              console.log(err);
            });

          this.activityEditModal = false;
        }
      });
    },
    delActivity() {
      // if (this.userInfo.userId === this.slctActivity.creator) {
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
            if (this.parentNode != {}) {
              this.slctActivity = this.parentNode;
              this.setContent(this.slctActivity, "delete");

              // delete activity from activity tree
              let children = this.parentNode.children;
              children.splice(
                children.findIndex((item) => item.aid === aid),
                1
              );
            }
          } else {
            console.log(res.data.msg);
          }
        })
        .catch((err) => {
          console.log(err);
        });
      // }
    },
  },
};
</script>
<style scoped>
.foldTreeBtn {
  padding: 5px 8px 6px;
  height: calc(100vh - 10px);
  margin: 5px;
  border: 0px;
}
.activityCard {
  margin: 5px;
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
  margin: 5px;
  border: 0;
  overflow-x: auto;
}
.workspaceCard >>> .ivu-card-body {
  padding: 0px 10px;
}
</style>