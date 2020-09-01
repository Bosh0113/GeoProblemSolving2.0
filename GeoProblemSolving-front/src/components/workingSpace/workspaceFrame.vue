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
    ></Card>
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
      slctActivity: "",
      expandNode: {}, // 使用引用传递，记录expand的位置，对activity tree 进行修改
    };
  },
  beforeRouteEnter: (to, from, next) => {
    next((vm) => {
      if (!vm.$store.getters.userState || vm.$store.getters.userId == "") {
        vm.$router.push({ name: "Login" });
      }
    });
  },
  mounted() {
    this.getProjectInfo();
    this.getUserInfo();
  },
  methods: {
    renderStyle(h, { root, node, data }) {
      if (data.aid == "add") {
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
                  props: Object.assign({}, this.buttonProps, {
                    icon: "ios-add",
                  }),
                  style: {
                    width: "100%",
                    backgroundColor: "#e4e7ed",
                  },
                  on: {
                    click: () => {
                      this.preCreation();
                    },
                  },
                },
                "Create activity"
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
      } else {
        if (this.slctActivity !== data.aid) {
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
                    style: {
                      width: "100%",
                    },
                    on: {
                      click: () => {
                        this.switchActivity(data);
                      },
                    },
                  },
                  data.name
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
        } else {
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
                    style: {
                      width: "100%",
                      backgroundColor: "lightblue",
                      cursor: "default",
                    },
                  },
                  data.name
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
        }
      }
    },
    getProjectInfo() {
      // this.projectInfo = parent.vm.projectInfo;
      // this.initActivityList();
      // this.slctActivity = this.projectInfo.aid;
      // this.userRole = this.roleIdentity(this.projectInfo);
    },
    roleIdentity(activity) {
      let role = userRoleJS.roleIdentify(
        activity.members,
        this.userInfo.userId
      );
      return role;
    },
    initActivityTree() {
      this.activityTree = [];
      this.axios
        .get("/GeoProblemSolving/project/" + this.projectInfo.aid + "/children")
        .then((res) => {
          if (res.data.code == 0) {
            let children = res.data.data;
            children.push({ aid: "add" }); // create activity node

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
            children.push({ aid: "add" });

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
          roleIdentity(activity) != "visitor" ||
          activity.permission.observe == "Yes"
        ) {
          this.expandActivityTree(activity);
          this.expandNode = activity;
        } else {
          activity.expand = false;
        }
      }
    },
    switchActivity(data) {
      this.slctActivity = data.aid;
      this.setContent(data);
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
    preCreation(activity) {
      if (activity.level == 1) {
        // subproject
      } else if (activity.level > 1) {
        // activity
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