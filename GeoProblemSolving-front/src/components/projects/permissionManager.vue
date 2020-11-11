<style scoped>
.btnPermission {
  margin-top: 50px;
  display: flex;
  justify-content: space-around;
}
</style>
<template>
  <Row>
    <Col span="6" offset="2">
      <h1 style="margin-top: 20px;">Permission management</h1>
      <div style="margin: 40px 0 20px 0">
        <h2>Project name</h2>
        <Divider style="margin: 5px 0" />
        <div>{{projectInfo.name}}</div>
      </div>
      <div style="margin: 20px 0">
        <h2>Privacy</h2>
        <Divider style="margin: 5px 0" />
        <div>{{projectInfo.privacy}}</div>
      </div>
      <div style="margin: 20px 0">
        <h2>Project description</h2>
        <Divider style="margin: 5px 0" />
        <div>{{projectInfo.description}}</div>
      </div>
      <div class="btnPermission">
        <Button @click="setDefault">Default</Button>
        <Button @click="save2Project">Save</Button>
      </div>
    </Col>
    <Col span="14" style="margin: 30px 15px">
      <Table
        :columns="permissionHead"
        :data="permissionList"
        border
        size="small"
        no-data-text="No data"
      ></Table>
    </Col>
  </Row>
</template>
<script>
import * as userRoleJS from "./../../api/userRole.js";
export default {
  data() {
    return {
      //权限
      permissionHead: [
        { title: "Operation", key: "operation", width: 200 },
        {
          title: "Managers",
          key: "manager",
          align: "center",
          render: (h, params) => {
            let row_value = params.row.manager;
            if (
              Object.prototype.toString.call(row_value) == "[object Boolean]"
            ) {
              return h("Checkbox", {
                props: {
                  value: row_value
                },
                on: {
                  "on-change": e => {
                    this.savePermission(
                      e,
                      1,
                      params.index,
                      params.row.operation
                    );
                  }
                }
              });
            } else if (
              Object.prototype.toString.call(row_value) == "[object String]"
            ) {
              return h(
                "Select",
                {
                  props: {
                    value: row_value,
                    size: "small",
                    placeholder: row_value
                  },
                  on: {
                    "on-change": e => {
                      this.savePermission(
                        e,
                        1,
                        params.index,
                        params.row.operation
                      );
                    }
                  }
                },
                [
                  h("Option", {
                    props: {
                      value: "Yes"
                    }
                  }),
                  h("Option", {
                    props: {
                      value: "Same to the parent"
                    },
                    attrs: {
                      title:
                        "Same to the parent activity"
                    }
                  }),
                  h("Option", {
                    props: {
                      value: "No"
                    }
                  })
                ]
              );
            } else if (
              Object.prototype.toString.call(params.row.manager) ==
              "[object Null]"
            ) {
            }
          }
        },
        {
          title: "Core team",
          key: "coreteam",
          align: "center",
          render: (h, params) => {
            let row_value = params.row.coreteam;
            if (
              Object.prototype.toString.call(row_value) == "[object Boolean]"
            ) {
              return h("Checkbox", {
                props: {
                  value: row_value
                },
                on: {
                  "on-change": e => {
                    this.savePermission(
                      e,
                      2,
                      params.index,
                      params.row.operation
                    );
                  }
                }
              });
            } else if (
              Object.prototype.toString.call(row_value) == "[object String]"
            ) {
              return h(
                "Select",
                {
                  props: {
                    value: row_value,
                    size: "small",
                    placeholder: row_value
                  },
                  on: {
                    "on-change": e => {
                      this.savePermission(
                        e,
                        2,
                        params.index,
                        params.row.operation
                      );
                    }
                  }
                },
                [
                  h("Option", {
                    props: {
                      value: "Yes"
                    }
                  }),
                  h("Option", {
                    props: {
                      value: "Same to the parent"
                    },
                    attrs: {
                      title:
                        "Same to the parent activity"
                    }
                  }),
                  h("Option", {
                    props: {
                      value: "No"
                    }
                  })
                ]
              );
            } else if (
              Object.prototype.toString.call(params.row.coreteam) ==
              "[object Null]"
            ) {
            }
          }
        },
        {
          title: "Wider team",
          key: "widerteam",
          align: "center",
          render: (h, params) => {
            let row_value = params.row.widerteam;
            if (
              Object.prototype.toString.call(row_value) == "[object Boolean]"
            ) {
              return h("Checkbox", {
                props: {
                  value: row_value
                },
                on: {
                  "on-change": e => {
                    this.savePermission(
                      e,
                      3,
                      params.index,
                      params.row.operation
                    );
                  }
                }
              });
            } else if (
              Object.prototype.toString.call(row_value) == "[object String]"
            ) {
              return h(
                "Select",
                {
                  props: {
                    value: row_value,
                    size: "small",
                    placeholder: row_value
                  },
                  on: {
                    "on-change": e => {
                      this.savePermission(
                        e,
                        3,
                        params.index,
                        params.row.operation
                      );
                    }
                  }
                },
                [
                  h("Option", {
                    props: {
                      value: "Yes"
                    }
                  }),
                  h("Option", {
                    props: {
                      value: "Same to the parent"
                    },
                    attrs: {
                      title:
                        "Same to the parent activity"
                    }
                  }),
                  h("Option", {
                    props: {
                      value: "No"
                    }
                  })
                ]
              );
            } else if (
              Object.prototype.toString.call(params.row.coreteam) ==
              "[object Null]"
            ) {
            }
          }
        },
        {
          title: "Visitor (Logged in)",
          key: "visitor",
          align: "center",
          render: (h, params) => {
            let row_value = params.row.visitor;
            if (
              Object.prototype.toString.call(row_value) == "[object Boolean]"
            ) {
              return h("Checkbox", {
                props: {
                  value: row_value
                },
                on: {
                  "on-change": e => {
                    this.savePermission(
                      e,
                      4,
                      params.index,
                      params.row.operation
                    );
                  }
                }
              });
            } else if (
              Object.prototype.toString.call(row_value) == "[object String]"
            ) {
              return h(
                "Select",
                {
                  props: {
                    value: row_value,
                    size: "small",
                    placeholder: row_value
                  },
                  on: {
                    "on-change": e => {
                      this.savePermission(
                        e,
                        4,
                        params.index,
                        params.row.operation
                      );
                    }
                  }
                },
                [
                  h("Option", {
                    props: {
                      value: "Yes"
                    }
                  }),
                 h("Option", {
                    props: {
                      value: "Same to the parent"
                    },
                    attrs: {
                      title:
                        "Same to the parent activity"
                    }
                  }),
                  h("Option", {
                    props: {
                      value: "No"
                    }
                  })
                ]
              );
            } else if (
              Object.prototype.toString.call(params.row.coreteam) ==
              "[object Null]"
            ) {
            }
          }
        }
      ],
      permissionList: [],
      permission: {},
      // project 信息
      projectId: this.$route.params.id,
      projectInfo: {}
    };
  },
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      if (!vm.$store.getters.userState) {
        next("/login");
      } else {
        // if (vm.projectInfo.managerId !== vm.$store.getters.userId) {
        //   vm.$Message.error("You have no property to access it");
        //   // next(`/project/${vm.$store.getters.currentProjectId}`);
        //   vm.$router.go(-1);
        // } else {
        //   next();
        // }
      }
    });
  },
  mounted() {
    this.getPermission();
  },
  methods: {
    getPermission() {
      if (JSON.stringify(projectInfo) != "{}") {
        this.projectInfo = projectInfo;
        if (this.projectInfo.permission != undefined) {
          this.permission = JSON.parse(this.projectInfo.permission);
        } else {
          this.permission = userRoleJS.getDefault();
        }
        this.getPermissionList();
      } else {
        $.ajax({
          url:
            "/GeoProblemSolving/project/" + this.projectId,
          type: "GET",
          async: false,
          success: data => {
            if (data == "Offline") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else if (data != "None" && data != "Fail") {
              this.projectInfo = data;

              if (this.projectInfo.permission != undefined) {
                this.permission = JSON.parse(this.projectInfo.permission);
              } else {
                this.permission = userRoleJS.getDefault();
              }
              this.getPermissionList();
            } else {
              console.log(data);
            }
          }
        });
      }
    },
    getPermissionList() {      
      this.permissionList = userRoleJS.permissionJson2Array(this.permission);
    },
    savePermission(value, key, index, operation) {
      if (operation == "Learn more about the activity") {
        if (key == 1) {
          this.permission.observe.manager = value;
          this.permissionList[index].manager = value;
        } else if (key == 2) {
          this.permission.observe.coreteam = value;
          this.permissionList[index].coreteam = value;
        } else if (key == 3) {
          this.permission.observe.widerteam = value;
          this.permissionList[index].widerteam = value;
        } else if (key == 4) {
          this.permission.observe.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Join the activity automatically") {
        if (key == 1) {
          this.permission.auto_join.manager = value;
          this.permissionList[index].manager = value;
        } else if (key == 2) {
          this.permission.auto_join.coreteam = value;
          this.permissionList[index].coreteam = value;
        } else if (key == 3) {
          this.permission.auto_join.widerteam = value;
          this.permissionList[index].widerteam = value;
        } else if (key == 4) {
          this.permission.auto_join.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Edit the information of the activity") {
        if (key == 1) {
          this.permission.project_edit_info.manager = value;
          this.permissionList[index].manager = value;
        } else if (key == 2) {
          this.permission.project_edit_info.coreteam = value;
          this.permissionList[index].coreteam = value;
        } else if (key == 3) {
          this.permission.project_edit_info.widerteam = value;
          this.permissionList[index].widerteam = value;
        } else if (key == 4) {
          this.permission.project_edit_info.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Invite other person to join the activity") {
        if (key == 1) {
          this.permission.project_invite_member.manager = value;
          this.permissionList[index].manager = value;
        } else if (key == 2) {
          this.permission.project_invite_member.coreteam = value;
          this.permissionList[index].coreteam = value;
        } else if (key == 3) {
          this.permission.project_invite_member.widerteam = value;
          this.permissionList[index].widerteam = value;
        } else if (key == 4) {
          this.permission.project_invite_member.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Manage members") {
        if (key == 1) {
          this.permission.project_remove_member.manager = value;
          this.permissionList[index].manager = value;
        } else if (key == 2) {
          this.permission.project_remove_member.coreteam = value;
          this.permissionList[index].coreteam = value;
        } else if (key == 3) {
          this.permission.project_remove_member.widerteam = value;
          this.permissionList[index].widerteam = value;
        } else if (key == 4) {
          this.permission.project_remove_member.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Create activity tasks") {
        if (key == 1) {
          this.permission.project_task_create.manager = value;
          this.permissionList[index].manager = value;
        } else if (key == 2) {
          this.permission.project_task_create.coreteam = value;
          this.permissionList[index].coreteam = value;
        } else if (key == 3) {
          this.permission.project_task_create.widerteam = value;
          this.permissionList[index].widerteam = value;
        } else if (key == 4) {
          this.permission.project_task_create.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Manage activity tasks") {
        if (key == 1) {
          this.permission.project_task_manage.manager = value;
          this.permissionList[index].manager = value;
        } else if (key == 2) {
          this.permission.project_task_manage.coreteam = value;
          this.permissionList[index].coreteam = value;
        } else if (key == 3) {
          this.permission.project_task_manage.widerteam = value;
          this.permissionList[index].widerteam = value;
        } else if (key == 4) {
          this.permission.project_task_manage.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Upload resources") {
        if (key == 1) {
          this.permission.project_resource_manage.manager = value;
          this.permissionList[index].manager = value;
        } else if (key == 2) {
          this.permission.project_resource_manage.coreteam = value;
          this.permissionList[index].coreteam = value;
        } else if (key == 3) {
          this.permission.project_resource_manage.widerteam = value;
          this.permissionList[index].widerteam = value;
        } else if (key == 4) {
          this.permission.project_resource_manage.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Use resources") {
        if (key == 1) {
          this.permission.project_workspace_type_manage.manager = value;
          this.permissionList[index].manager = value;
        } else if (key == 2) {
          this.permission.project_workspace_type_manage.coreteam = value;
          this.permissionList[index].coreteam = value;
        } else if (key == 3) {
          this.permission.project_workspace_type_manage.widerteam = value;
          this.permissionList[index].widerteam = value;
        } else if (key == 4) {
          this.permission.project_workspace_type_manage.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Manage resources") {
        if (key == 1) {
          this.permission.subprojects_manage.manager = value;
          this.permissionList[index].manager = value;
        } else if (key == 2) {
          this.permission.subprojects_manage.coreteam = value;
          this.permissionList[index].coreteam = value;
        } else if (key == 3) {
          this.permission.subprojects_manage.widerteam = value;
          this.permissionList[index].widerteam = value;
        } else if (key == 4) {
          this.permission.subprojects_manage.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Switch the type of workspace") {
        if (key == 1) {
          this.permission.subproject_edit_info.manager = value;
          this.permissionList[index].manager = value;
        } else if (key == 2) {
          this.permission.subproject_edit_info.coreteam = value;
          this.permissionList[index].coreteam = value;
        } else if (key == 3) {
          this.permission.subproject_edit_info.widerteam = value;
          this.permissionList[index].widerteam = value;
        } else if (key == 4) {
          this.permission.subproject_edit_info.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Import tools or toolsets") {
        if (key == 1) {
          this.permission.subproject_invite_member.manager = value;
          this.permissionList[index].manager = value;
        } else if (key == 2) {
          this.permission.subproject_invite_member.coreteam = value;
          this.permissionList[index].coreteam = value;
        } else if (key == 3) {
          this.permission.subproject_invite_member.widerteam = value;
          this.permissionList[index].widerteam = value;
        } else if (key == 4) {
          this.permission.subproject_invite_member.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Use tools or toolsets") {
        if (key == 1) {
          this.permission.subproject_remove_member.manager = value;
          this.permissionList[index].manager = value;
        } else if (key == 2) {
          this.permission.subproject_remove_member.coreteam = value;
          this.permissionList[index].coreteam = value;
        } else if (key == 3) {
          this.permission.subproject_remove_member.widerteam = value;
          this.permissionList[index].widerteam = value;
        } else if (key == 4) {
          this.permission.subproject_remove_member.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Manage tools or toolsets") {
        if (key == 1) {
          this.permission.subproject_task_create.manager = value;
          this.permissionList[index].manager = value;
        } else if (key == 2) {
          this.permission.subproject_task_create.coreteam = value;
          this.permissionList[index].coreteam = value;
        } else if (key == 3) {
          this.permission.subproject_task_create.widerteam = value;
          this.permissionList[index].widerteam = value;
        } else if (key == 4) {
          this.permission.subproject_task_create.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Manage child activity") {
        if (key == 1) {
          this.permission.subproject_task_manage.manager = value;
          this.permissionList[index].manager = value;
        } else if (key == 2) {
          this.permission.subproject_task_manage.coreteam = value;
          this.permissionList[index].coreteam = value;
        } else if (key == 3) {
          this.permission.subproject_task_manage.widerteam = value;
          this.permissionList[index].widerteam = value;
        } else if (key == 4) {
          this.permission.subproject_task_manage.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }      
    },
    setDefault() {
      this.permission = userRoleJS.getDefault();
      this.getPermissionList();
    },
    save2Project() {
      this.projectInfo.permission = JSON.stringify(this.permission);
      this.axios
        .put("/GeoProblemSolving/project/"+ this.projectInfo.aid, this.projectInfo)
        .then(res => {
          this.resetProjectTypeModel = false;
          if (res.data == "Offline") {
            parent.location.href = "/GeoProblemSolving/login";
          } else if (res.data.code == 0) {
            this.$Notice.info({
              desc: "Update permission successfully. "
            });
          } else {
            this.$Message.error(res.data.msg);
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    }
  }
};
</script>