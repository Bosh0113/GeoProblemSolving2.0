<template>
  <div style="display: flex;background-color:lightgrey">
    <Card dis-hover class="activityCard">
      <h3 slot="title">Activity list</h3>
      <div style="padding-right: 15px">
        <Tree :data="activityTree" :render="renderStyle"></Tree>
      </div>
    </Card>
    <Card dis-hover class="workspaceCard">
      <h3 slot="title">{{slctActivity.name}}</h3>
      <div slot="extra" v-show="slctActivity.level > 0">
        <!-- <Button
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
        >Delete</Button>-->
        <Button
          icon="md-create"
          size="small"
          style="margin-top:-10px; margin-right: 10px"
          @click="activityEditModal = true"
        >Edit</Button>
        <Button
          icon="md-trash"
          size="small"
          style="margin-top:-10px"
          @click="activityDeleteModal = true"
        >Delete</Button>
      </div>
      <!-- <Spin size="large" fix v-if="spinShow"></Spin> -->
      <type-choose
        v-if="contentType==0"
        :activityInfo="slctActivity"
        :userInfo="userInfo"
        @typeChanged="typeChanged"
      ></type-choose>
      <!-- <work-space v-else-if="contentType==1" :activityInfo="slctActivity" :userInfo="userInfo"></work-space> -->
      <activity-manager
        v-else-if="contentType==2"
        :activityInfo="slctActivity"
        :userInfo="userInfo"
      ></activity-manager>
      <!-- <activity-show v-else-if="contentType==4" :activityInfo="slctActivity" :userInfo="userInfo"></activity-show> -->
    </Card>
    <Modal
      v-model="activityEditModal"
      title="Edit information of the activity"
      :mask-closable="false"
    >
      <Form
        ref="activityForm"
        :model="slctActivity"
        :rules="activityEditRule"
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
        <FormItem label="Purpose:" prop="purpose">
          <Input
            type="text" readonly
            v-model="slctActivity.purpose"
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
        <FormItem label="Purpose:" prop="purpose">
            <Select v-model="activityForm.purpose" placeholder="Select the purpose of this activity" readonly>
              <Option v-for="item in purposes" :key="item.index" :value="item">{{item}}</Option>
            </Select>
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
import typeChoose from "./activity/typeChoose.vue";
import workSpace from "./activity/workSpace.vue";
import activityManager from "./activity/activityManager.vue";
import activityShow from "./activity/activityShow.vue";
export default {
  components: {
    typeChoose,
    workSpace,
    activityManager,
    activityShow,
  },
  data() {
    return {
      projectInfo: {},
      userInfo: JSON.parse(sessionStorage.getItem("userInfo")),
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
      activityCreateRule: {
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
        purpose: [
          {
            required: true,
            message: "The purpose should not be empty",
            trigger: "blur",
          },
        ],
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
          }
        ],
      },
      activityEditModal: false,
      activityDeleteModal: false,
      contentType: -1,
      purposes: [
        "Context definition & resource collection",
        "Data processing",
        "Data visualization",
        "Geographic model construction",
        "Model effectiveness evaluation",
        "Geographical simulation",
        "Quantitative and qualitative analysis",
        "Decision-making for management",
        "Others"
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
      this.setContent(this.slctActivity);
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
      if (this.projectInfo.type == "Activity_Group") {
        this.axios
          .get(
            "/GeoProblemSolving/project/" + this.projectInfo.aid + "/children"
          )
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
              children.push({ aid: "add" }); // create activity node
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
      } else {
        let root = Object.assign({}, this.projectInfo);
        this.activityTree.push(root);
      }
    },
    typeChanged(type) {
      if (type == "Activity_Group") {
        this.slctActivity.children = [{ aid: "add" }];
      }
      this.slctActivity.type = type;
      this.setContent(activity);
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
              // if (
              //   this.permissionIdentity(
              //     activity.permission,
              //     this.roleIdentity(activity),
              //     "manage_child_activity"
              //   )
              // ) {
              children.push({
                aid: "add",
              });
              // }

              this.expandNode["expand"] = true;
              this.expandNode["children"] = children;
            } else {
              console.log(res.data.msg);
            }
          })
          .catch((err) => {
            console.log(err.data);
          });
      }
    },
    switchActivity(root, node, activity) {
      // if (
      //   this.roleIdentity(activity) != "visitor" ||
      //   activity.permission.observe == "Yes"
      // ) {
      //content
      if (activity.level > 0) {
        this.parentNode = root[node.parent].node;
      } else {
        this.parentNode = {};
      }
      this.slctActivity = activity;
      this.setContent(activity);
      // expand
      this.expandActivityTree(activity);
      this.expandNode = activity;
      // } else {
      //   this.contentType = 4;
      // }
    },
    setContent(activity) {
      if (activity.type == "Activity_Default") {
        this.contentType = 0;
      } else if (activity.type == "Activity_Unit") {
        this.contentType = 1;
      } else if (activity.type == "Activity_Group") {
        this.contentType = 2;
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
            this.setContent(this.slctActivity);
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
              this.setContent(this.slctActivity);

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
.activityCard {
  margin: 5px;
  width: 250px;
  overflow-y: auto;
}
.activityCard >>> .ivu-card-body {
  padding: 5px 10px;
}
.workspaceCard {
  width: calc(100vw - 260px);
  height: calc(100vh - 10px);
  background-color: white;
  margin: 5px;
  overflow-x: auto;
}
.workspaceCard >>> .ivu-card-body {
  padding: 0px 10px;
}
</style>