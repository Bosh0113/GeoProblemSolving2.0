<template>
  <div style="display: flex;background-color:lightgrey">
    <Card style="margin: 5px; width: 250px;overflow-y: auto" dis-hover class="activityCard">
      <h3 slot="title">Activity list</h3>
      <div style="padding-right: 15px">
        <Tree :data="activityTree" :render="renderStyle" @on-toggle-expand="expand"></Tree>
      </div>
    </Card>
    <Card
      dis-hover
      style="width: calc(100vw - 260px); height: calc(100vh - 10px); background-color: white; margin: 5px;overflow-x: auto"
    >
      <div slot="title">{{slctActivity.name}}</div>
      <div slot="extra" v-show="slctActivity.level > 0">
        <Button
          icon="md-create"
          size="small"
          style="margin-top:-10px; margin-right: 10px"
          @click="activityEditModal = true"
          v-show="permissionIdentity(slctActivity.permission, roleIdentity(slctActivity), 'edit_info')"
        >Edit</Button>
        <Button
          icon="md-trash"
          size="small"
          style="margin-top:-10px"
          @click="activityDeleteModal = true"
          v-show="userInfo.userId === slctActivity.creator"
        >Delete</Button>
      </div>
    </Card>
    <Modal
      v-model="activityEditModal"
      title="Edit information of the activity"
      :mask-closable="false"
    >
      <Form
        ref="activityForm"
        :model="slctActivity"
        :rules="activityRuleValidate"
        :label-width="120"
      >
        <FormItem label="Name" prop="name">
          <Input
            type="text"
            v-model="slctActivity.name"
            placeholder="Fill in the name (less than 60 characters) ..."
          ></Input>
        </FormItem>
        <FormItem label="Description" prop="description">
          <Input
            v-model="slctActivity.description"
            placeholder="Fill in the description..."
            :rows="4"
            type="textarea"
          ></Input>
        </FormItem>
      </Form>
      <div slot="footer" style="display: inline-block">
        <Button type="primary" @click="editActivity()" style="float:right;">OK</Button>
        <Button @click="createActivityModel = false" style="float:right;margin-right: 15px;">Cancel</Button>
      </div>
    </Modal>
    <Modal
      v-model="activityDeleteModal"
      title="Delete the current activity"
      @on-ok="delActivity"
      ok-text="OK"
      cancel-text="Cancel"
    >
      <h3 style="color:red">Do you really want to delete this activity?</h3>
    </Modal>
    <Modal
      v-model="createActivityModel"
      title="Create a new activity"
      width="800"
      :mask-closable="false"
    >
      <Form
        ref="activityForm"
        :model="activityForm"
        :rules="activityRuleValidate"
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
      </Form>
      <div slot="footer" style="display: inline-block">
        <Button type="primary" @click="createActivity('activityForm')" style="float:right;">OK</Button>
        <Button @click="createActivityModel = false" style="float:right;margin-right: 15px;">Cancel</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
import * as userRoleJS from "./../../api/userRole.js";
export default {
  data() {
    return {
      userRole: JSON.parse(sessionStorage.getItem("userInfo")),
      projectInfo: {},
      userInfo: {},
      activityTree: [
        {
          aid: "111",
          name: "activity 1",
          level: 0,
          children: [
            {
              aid: "121",
              name: "activity 1-1",
              level: 1,
              children: [
                {
                  aid: "122",
                  name: "leaf 1-1-1",
                  level: 2,
                },
                {
                  aid: "123",
                  name: "leaf 1-1-2",
                  level: 2,
                },
                {
                  aid: "add",
                },
              ],
            },
            {
              aid: "130",
              name: "activity 1-2",
              level: 1,
              children: [
                {
                  aid: "131",
                  name: "leaf 1-2-1",
                  level: 2,
                },
                {
                  aid: "132",
                  name: "leaf 1-2-1",
                  level: 2,
                },
                {
                  aid: "add",
                },
              ],
            },
            {
              aid: "add",
            },
          ],
          expand: true,
        },
      ],
      slctActivity: {},
      expandNode: {}, // 使用引用传递，记录expand的位置，对activity tree 进行修改
      parentNode: {}, // 记录所创建/选择activity的父节点位置，对activity tree 进行修改
      // create activity
      createActivityModel: false,
      activityForm: {
        name: "",
        description: "",
        parent: "",
        creator: "",
        level: -1,
        permission: JSON.stringify(userRoleJS.getDefault()),
        type: "Activity_Default",
      },
      activityRuleValidate: {
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
      activityEditModal: false,
      activityDeleteModal: false,
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
    this.getProjectInfo();
  },
  methods: {
    renderStyle(h, { root, node, data }) {
      let props = {};
      let style = {};
      let on = {};
      let name = "";

      if (data.aid == "add") {
        props = Object.assign({}, this.buttonProps, {
          icon: "ios-add",
        });
        style = {
          width: "100%",
          backgroundColor: "#e4e7ed",
        };
        on = {
          click: () => {
            this.preCreation(root, node);
          },
        };
        name = "Create activity";
      } else {
        name = data.name;
        if (this.slctActivity.aid !== data.aid) {
          style = {
            width: "100%",
          };
          on = {
            click: () => {
              this.switchActivity(root, node, data);
            },
          };
        } else {
          style = {
            width: "100%",
            backgroundColor: "lightblue",
            cursor: "default",
          };
        }
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
    getProjectInfo() {
      this.projectInfo = parent.vm.projectInfo;
      this.initActivityTree();
      this.slctActivity = this.projectInfo;
      this.userRole = this.roleIdentity(this.projectInfo);
    },
    roleIdentity(activity) {
      return userRoleJS.roleIdentify(activity.members, this.userInfo.userId);
    },
    permissionIdentity(permission, role, operation) {
      return userRoleJS.permissionIdentity(
        JSON.parse(permission),
        role,
        operation
      );
    },
    initActivityTree() {
      this.activityTree = [];
      this.axios
        .get("/GeoProblemSolving/project/" + this.projectInfo.aid + "/children")
        .then((res) => {
          if (res.data.code == 0) {
            let children = res.data.data;
            // if (
            //   this.permissionIdentity(
            //     this.projectInfo.permission,
            //     this.roleIdentity(this.projectInfo),
            //     "manage_child_activity"
            //   )
            // ) {
            children.push({
              aid: "add",
            }); // create activity node
            // }

            let root = Object.assign({}, this.projectInfo);
            root["expand"] = true;
            root["children"] = children;

            this.activityTree.push(root);
          } else {
            console.log(res.data.msg);
          }
        })
        .catch((err) => {
          console.log(err.data);
        });
    },
    expandActivityTree(activity) {
      let url = "";
      if (activity.level == 1) {
        url =
          "/GeoProblemSolving/subproject/" + this.projectInfo.aid + "/children";
      } else if (activity.level > 1) {
        url =
          "/GeoProblemSolving/activity/" + this.projectInfo.aid + "/children";
      }

      this.axios
        .get(url)
        .then((res) => {
          if (res.data.code == 0) {
            // children
            let children = res.data.data;
            if (
              this.permissionIdentity(
                activity.permission,
                this.roleIdentity(activity),
                "manage_child_activity"
              )
            ) {
              children.push({
                aid: "add",
              });
            }

            this.expandNode["expand"] = true;
            this.expandNode["children"] = children;
          } else {
            console.log(res.data.msg);
          }
        })
        .catch((err) => {
          console.log(err.data);
        });
    },
    expand(activity) {
      if (activity.expand) {
        if (
          this.roleIdentity(activity) != "visitor" ||
          activity.permission.observe == "Yes"
        ) {
          this.expandActivityTree(activity);
          this.expandNode = activity;
        } else {
          activity.expand = false;
        }
      }
    },
    switchActivity(root, node, activity) {
      if (activity.level > 0) {
        this.parentNode = root[node.parent].node;
      } else {
        this.parentNode = {};
      }
      this.slctActivity = activity;
      this.setContent(activity);
    },
    setContent(activity) {
      if (activity.level == 0) {
        if (activity.children == undefined || activity.children.lenght == 0) {
          // leaf
        } else if (activity.children.lenght > 0) {
          // not leaf
        }
      } else if (activity.level > 0) {
        if (activity.children == undefined || activity.children.lenght == 0) {
          // leaf
        } else if (activity.children.lenght > 0) {
          // not leaf
        }
      }
    },
    preCreation(root, node) {
      this.parentNode = root[node.parent].node;

      this.activityForm.parent = this.parentNode.aid;
      this.activityForm.creator = this.userInfo.userId;
      this.activityForm.level = this.parentNode.level + 1;

      this.createActivityModel = true;
    },
    createActivity() {
      let url = "";
      if (this.activityForm.level == 1) {
        // subproject
        url = "/GeoProblemSolving/subproject";
      } else if (this.activityForm.level > 1) {
        // activity
        url = "/GeoProblemSolving/activity";
      }

      this.axios
        .post(url, this.activityForm)
        .then((res) => {
          if (res.data.code == 0) {
            // change activity tree
            for (let i = 0; i < this.parentNode.children.length; i++) {
              if (this.parentNode.children[i].aid == "add") {
                this.parentNode.children[i] = res.data.data;
              }
            }
            this.parentNode.children.push({ aid: "add" });
            this.slctActivity = res.data.data;
            // change content
            //...
          } else {
            console.log(res.data.msg);
          }
        })
        .catch((err) => {
          console.log(err);
        });
      this.createActivityModel = false;
    },
    preEditting(activity) {
      let children = [];
      if (activity.children != undefined) {
        for (let i = 0; i < activity.children.length; i++) {
          children.push(activity.children[i].aid);
        }
        activity.children = children;
      }
      return activity;
    },
    editActivity() {
      let url = "";
      let aid = this.slctActivity.aid;
      let data = this.preEditting(this.slctActivity);
      if (this.slctActivity.level == 1) {
        url = "/GeoProblemSolving/subproject?aid=" + aid;
      } else if (this.slctActivity.level > 1) {
        url = "/GeoProblemSolving/activity?aid=" + aid;
      }
      this.axios
        .put(url, data)
        .then((res) => {
          if (res.data.code == 0) {
            this.$Notice.info({ title: "Result", desc: "Success!" });
          } else {
            this.$Notice.info({ title: "Result", desc: res.data.msg });
          }
        })
        .catch((err) => {
          console.log(err);
        });
      this.activityEditModal = false;
    },
    delActivity() {
      if (this.userInfo.userId === this.slctActivity.creator) {
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
      }
    },
  },
};
</script>
<style scoped>
.activityCard >>> .ivu-card-body {
  padding: 5px 10px;
}
</style>