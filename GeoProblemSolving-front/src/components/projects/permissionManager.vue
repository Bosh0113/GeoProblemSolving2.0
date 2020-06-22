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
        <div>{{projectInfo.title}}</div>
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
export default {
  data() {
    return {
      //权限
      permissionHead: [
        { title: "Operation", key: "operation", width: 200 },
        {
          title: "Project manager",
          key: "project_manager",
          align: "center",
          render: (h, params) => {
            let row_value = params.row.project_manager;
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
                      value: "Yes, partly"
                    },
                    attrs: {
                      title:
                        "A user could only manage what he/she created/provided."
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
              Object.prototype.toString.call(params.row.project_manager) ==
              "[object Null]"
            ) {
            }
          }
        },
        {
          title: "Subproject manager",
          key: "subproject_manager",
          align: "center",
          render: (h, params) => {
            let row_value = params.row.subproject_manager;
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
                      value: "Yes, partly"
                    },
                    attrs: {
                      title:
                        "A user could only manage what he/she created/provided."
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
              Object.prototype.toString.call(params.row.subproject_manager) ==
              "[object Null]"
            ) {
            }
          }
        },
        {
          title: "Member",
          key: "member",
          align: "center",
          render: (h, params) => {
            let row_value = params.row.member;
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
                      value: "Yes, partly"
                    },
                    attrs: {
                      title:
                        "A user could only manage what he/she created/provided."
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
              Object.prototype.toString.call(params.row.subproject_manager) ==
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
                      value: "All"
                    }
                  }),
                  h("Option", {
                    props: {
                      value: "At subproject level"
                    }
                  }),
                  h("Option", {
                    props: {
                      value: "At project level"
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
              Object.prototype.toString.call(params.row.subproject_manager) ==
              "[object Null]"
            ) {
            }
          }
        }
      ],
      permissionList: [],
      defaultPublic: {
        observe: {
          project_manager: null,
          subproject_manager: null,
          member: null,
          visitor: "All"
        },
        auto_join: {
          project_manager: null,
          subproject_manager: null,
          member: null,
          visitor: true
        },
        project_edit_info: {
          project_manager: true,
          subproject_manager: null,
          member: false,
          visitor: null
        },
        project_invite_member: {
          project_manager: true,
          subproject_manager: null,
          member: true,
          visitor: null
        },
        project_remove_member: {
          project_manager: true,
          subproject_manager: null,
          member: false,
          visitor: null
        },
        project_task_create: {
          project_manager: true,
          subproject_manager: null,
          member: true,
          visitor: null
        },
        project_task_manage: {
          project_manager: true,
          subproject_manager: null,
          member: "Yes, partly",
          visitor: null
        },
        project_resource_manage: {
          project_manager: true,
          subproject_manager: null,
          member: "Yes, partly",
          visitor: null
        },
        project_workspace_type_manage: {
          project_manager: true,
          subproject_manager: null,
          member: false,
          visitor: null
        },
        subprojects_manage: {
          project_manager: true,
          subproject_manager: null,
          member: "Yes, partly",
          visitor: null
        },
        subproject_edit_info: {
          project_manager: true,
          subproject_manager: true,
          member: false,
          visitor: null
        },
        subproject_invite_member: {
          project_manager: true,
          subproject_manager: true,
          member: true,
          visitor: null
        },
        subproject_remove_member: {
          project_manager: true,
          subproject_manager: true,
          member: false,
          visitor: null
        },
        subproject_task_create: {
          project_manager: true,
          subproject_manager: true,
          member: true,
          visitor: null
        },
        subproject_task_manage: {
          project_manager: true,
          subproject_manager: true,
          member: "Yes, partly",
          visitor: null
        },
        subproject_resource_manage: {
          project_manager: "Yes, partly",
          subproject_manager: true,
          member: "Yes, partly",
          visitor: null
        },
        subproject_workspace_type_manage: {
          project_manager: true,
          subproject_manager: true,
          member: false,
          visitor: null
        },
        activity_manage: {
          project_manager: true,
          subproject_manager: true,
          member: true,
          visitor: null
        },
        workspace_resource: {
          project_manager: "Yes",
          subproject_manager: true,
          member: "Yes, partly",
          visitor: null
        },
        workspace_tool: {
          project_manager: true,
          subproject_manager: true,
          member: true,
          visitor: null
        },
        workspace_edit: {
          project_manager: true,
          subproject_manager: true,
          member: true,
          visitor: null
        }
      },
      defaultPrivate: {
        observe: {
          project_manager: null,
          subproject_manager: null,
          member: null,
          visitor: "No"
        },
        auto_join: {
          project_manager: null,
          subproject_manager: null,
          member: null,
          visitor: null
        },
        project_edit_info: {
          project_manager: true,
          subproject_manager: null,
          member: false,
          visitor: null
        },
        project_invite_member: {
          project_manager: true,
          subproject_manager: null,
          member: false,
          visitor: null
        },
        project_remove_member: {
          project_manager: true,
          subproject_manager: null,
          member: false,
          visitor: null
        },
        project_task_create: {
          project_manager: true,
          subproject_manager: null,
          member: true,
          visitor: null
        },
        project_task_manage: {
          project_manager: true,
          subproject_manager: null,
          member: "Yes, partly",
          visitor: null
        },
        project_resource_manage: {
          project_manager: true,
          subproject_manager: null,
          member: "Yes, partly",
          visitor: null
        },
        project_workspace_type_manage: {
          project_manager: true,
          subproject_manager: null,
          member: false,
          visitor: null
        },
        subprojects_manage: {
          project_manager: true,
          subproject_manager: null,
          member: "Yes, partly",
          visitor: null
        },
        subproject_edit_info: {
          project_manager: false,
          subproject_manager: true,
          member: false,
          visitor: null
        },
        subproject_invite_member: {
          project_manager: false,
          subproject_manager: true,
          member: false,
          visitor: null
        },
        subproject_remove_member: {
          project_manager: false,
          subproject_manager: true,
          member: false,
          visitor: null
        },
        subproject_task_create: {
          project_manager: true,
          subproject_manager: true,
          member: true,
          visitor: null
        },
        subproject_task_manage: {
          project_manager: false,
          subproject_manager: true,
          member: "Yes, partly",
          visitor: null
        },
        subproject_resource_manage: {
          project_manager: "Yes, partly",
          subproject_manager: true,
          member: "Yes, partly",
          visitor: null
        },
        subproject_workspace_type_manage: {
          project_manager: true,
          subproject_manager: true,
          member: false,
          visitor: null
        },
        activity_manage: {
          project_manager: false,
          subproject_manager: true,
          member: false,
          visitor: null
        },
        workspace_resource: {
          project_manager: "Yes, partly",
          subproject_manager: true,
          member: "Yes, partly",
          visitor: null
        },
        workspace_tool: {
          project_manager: true,
          subproject_manager: true,
          member: true,
          visitor: null
        },
        workspace_edit: {
          project_manager: false,
          subproject_manager: true,
          member: false,
          visitor: null
        }
      },
      defaultDiscoverable: {
        observe: {
          project_manager: null,
          subproject_manager: null,
          member: null,
          visitor: "No"
        },
        auto_join: {
          project_manager: null,
          subproject_manager: null,
          member: null,
          visitor: null
        },
        project_edit_info: {
          project_manager: true,
          subproject_manager: null,
          member: false,
          visitor: null
        },
        project_invite_member: {
          project_manager: true,
          subproject_manager: null,
          member: false,
          visitor: null
        },
        project_remove_member: {
          project_manager: true,
          subproject_manager: null,
          member: false,
          visitor: null
        },
        project_task_create: {
          project_manager: true,
          subproject_manager: null,
          member: true,
          visitor: null
        },
        project_task_manage: {
          project_manager: true,
          subproject_manager: null,
          member: "Yes, partly",
          visitor: null
        },
        project_resource_manage: {
          project_manager: true,
          subproject_manager: null,
          member: "Yes, partly",
          visitor: null
        },
        project_workspace_type_manage: {
          project_manager: true,
          subproject_manager: null,
          member: false,
          visitor: null
        },
        subprojects_manage: {
          project_manager: true,
          subproject_manager: null,
          member: "Yes, partly",
          visitor: null
        },
        subproject_edit_info: {
          project_manager: false,
          subproject_manager: true,
          member: false,
          visitor: null
        },
        subproject_invite_member: {
          project_manager: false,
          subproject_manager: true,
          member: false,
          visitor: null
        },
        subproject_remove_member: {
          project_manager: false,
          subproject_manager: true,
          member: false,
          visitor: null
        },
        subproject_task_create: {
          project_manager: true,
          subproject_manager: true,
          member: true,
          visitor: null
        },
        subproject_task_manage: {
          project_manager: "Yes, partly",
          subproject_manager: true,
          member: "Yes, partly",
          visitor: null
        },
        subproject_resource_manage: {
          project_manager: "Yes, partly",
          subproject_manager: true,
          member: "Yes, partly",
          visitor: null
        },
        subproject_workspace_type_manage: {
          project_manager: false,
          subproject_manager: true,
          member: false,
          visitor: null
        },
        activity_manage: {
          project_manager: false,
          subproject_manager: true,
          member: false,
          visitor: null
        },
        workspace_resource: {
          project_manager: "Yes, partly",
          subproject_manager: true,
          member: "Yes, partly",
          visitor: null
        },
        workspace_tool: {
          project_manager: true,
          subproject_manager: true,
          member: true,
          visitor: null
        },
        workspace_edit: {
          project_manager: false,
          subproject_manager: true,
          member: false,
          visitor: null
        }
      },
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
        if (vm.projectInfo.managerId !== vm.$store.getters.userId) {
          vm.$Message.error("You have no property to access it");
          // next(`/project/${vm.$store.getters.currentProjectId}`);
          vm.$router.go(-1);
        } else {
          next();
        }
      }
    });
  },
  mounted() {
    this.getPermission();
  },
  methods: {
    getPermission() {
      let projectInfo = this.$store.getters.project;

      if (JSON.stringify(projectInfo) != "{}") {
        this.projectInfo = projectInfo;
        if (this.projectInfo.permissionManager != undefined) {
          this.permission = this.projectInfo.permissionManager;
        } else {
          if (this.projectInfo.privacy == "Public") {
            this.permission = JSON.parse(JSON.stringify(this.defaultPublic));
          } else if (this.projectInfo.privacy == "Discoverable") {
            this.permission = JSON.parse(
              JSON.stringify(this.defaultDiscoverable)
            );
          } else if (this.projectInfo.privacy == "Private") {
            this.permission = JSON.parse(JSON.stringify(this.defaultPrivate));
          }
        }
        this.getPermissionList();
      } else {
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
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else if (data != "None" && data != "Fail") {
              this.projectInfo = data[0];
              this.$store.commit("setProjectInfo", data[0]);

              if (this.projectInfo.permissionManager != undefined) {
                this.permission = this.projectInfo.permissionManager;
              } else {
                if (this.projectInfo.privacy == "Public") {
                  this.permission = JSON.parse(
                    JSON.stringify(this.defaultPublic)
                  );
                } else if (this.projectInfo.privacy == "Discoverable") {
                  this.permission = JSON.parse(
                    JSON.stringify(this.defaultDiscoverable)
                  );
                } else if (this.projectInfo.privacy == "Private") {
                  this.permission = JSON.parse(
                    JSON.stringify(this.defaultPrivate)
                  );
                }
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
      this.permissionList = [];
      if (this.permission.observe != undefined) {
        let row = {
          operation: "Observe the project",
          project_manager: this.permission.observe.project_manager,
          subproject_manager: this.permission.observe.subproject_manager,
          member: this.permission.observe.member,
          visitor: this.permission.observe.visitor
        };
        this.permissionList.push(row);
      }
      if (
        this.permission.auto_join != undefined &&
        this.projectInfo.privacy == "Public"
      ) {
        let row = {
          operation: "Join automatically",
          project_manager: this.permission.auto_join.project_manager,
          subproject_manager: this.permission.auto_join.subproject_manager,
          member: this.permission.auto_join.member,
          visitor: this.permission.auto_join.visitor
        };
        this.permissionList.push(row);
      }
      if (this.permission.project_edit_info != undefined) {
        let row = {
          operation: "Edit project information",
          project_manager: this.permission.project_edit_info.project_manager,
          subproject_manager: this.permission.project_edit_info
            .subproject_manager,
          member: this.permission.project_edit_info.member,
          visitor: this.permission.project_edit_info.visitor
        };
        this.permissionList.push(row);
      }
      if (this.permission.project_invite_member != undefined) {
        let row = {
          operation: "Invite to join project",
          project_manager: this.permission.project_invite_member
            .project_manager,
          subproject_manager: this.permission.project_invite_member
            .subproject_manager,
          member: this.permission.project_invite_member.member,
          visitor: this.permission.project_invite_member.visitor
        };
        this.permissionList.push(row);
      }
      if (this.permission.project_remove_member != undefined) {
        let row = {
          operation: "Remove the project member",
          project_manager: this.permission.project_remove_member
            .project_manager,
          subproject_manager: this.permission.project_remove_member
            .subproject_manager,
          member: this.permission.project_remove_member.member,
          visitor: this.permission.project_remove_member.visitor
        };
        this.permissionList.push(row);
      }
      if (this.permission.project_task_create != undefined) {
        let row = {
          operation: "Create project tasks",
          project_manager: this.permission.project_task_create.project_manager,
          subproject_manager: this.permission.project_task_create
            .subproject_manager,
          member: this.permission.project_task_create.member,
          visitor: this.permission.project_task_create.visitor
        };
        this.permissionList.push(row);
      }
      if (this.permission.project_task_manage != undefined) {
        let row = {
          operation: "Manage project tasks",
          project_manager: this.permission.project_task_manage.project_manager,
          subproject_manager: this.permission.project_task_manage
            .subproject_manager,
          member: this.permission.project_task_manage.member,
          visitor: this.permission.project_task_manage.visitor
        };
        this.permissionList.push(row);
      }
      if (this.permission.project_resource_manage != undefined) {
        let row = {
          operation: "Manage project resources",
          project_manager: this.permission.project_resource_manage
            .project_manager,
          subproject_manager: this.permission.project_resource_manage
            .subproject_manager,
          member: this.permission.project_resource_manage.member,
          visitor: this.permission.project_resource_manage.visitor
        };
        this.permissionList.push(row);
      }
      if (this.permission.project_workspace_type_manage != undefined) {
        let row = {
          operation: "Change the type of workspace in the project",
          project_manager: this.permission.project_workspace_type_manage
            .project_manager,
          subproject_manager: this.permission.project_workspace_type_manage
            .subproject_manager,
          member: this.permission.project_workspace_type_manage.member,
          visitor: this.permission.project_workspace_type_manage.visitor
        };
        this.permissionList.push(row);
      }
      if (this.permission.subprojects_manage != undefined) {
        let row = {
          operation: "Manage subprojects",
          project_manager: this.permission.subprojects_manage.project_manager,
          subproject_manager: this.permission.subprojects_manage
            .subproject_manager,
          member: this.permission.subprojects_manage.member,
          visitor: this.permission.subprojects_manage.visitor
        };
        this.permissionList.push(row);
      }
      if (this.permission.subproject_edit_info != undefined) {
        let row = {
          operation: "Edit subproject information",
          project_manager: this.permission.subproject_edit_info.project_manager,
          subproject_manager: this.permission.subproject_edit_info
            .subproject_manager,
          member: this.permission.subproject_edit_info.member,
          visitor: this.permission.subproject_edit_info.visitor
        };
        this.permissionList.push(row);
      }
      if (this.permission.subproject_invite_member != undefined) {
        let row = {
          operation: "Invite to join subproject",
          project_manager: this.permission.subproject_invite_member
            .project_manager,
          subproject_manager: this.permission.subproject_invite_member
            .subproject_manager,
          member: this.permission.subproject_invite_member.member,
          visitor: this.permission.subproject_invite_member.visitor
        };
        this.permissionList.push(row);
      }
      if (this.permission.subproject_remove_member != undefined) {
        let row = {
          operation: "Remove the subproject member",
          project_manager: this.permission.subproject_remove_member
            .project_manager,
          subproject_manager: this.permission.subproject_remove_member
            .subproject_manager,
          member: this.permission.subproject_remove_member.member,
          visitor: this.permission.subproject_remove_member.visitor
        };
        this.permissionList.push(row);
      }
      if (this.permission.subproject_task_create != undefined) {
        let row = {
          operation: "Create subproject tasks",
          project_manager: this.permission.subproject_task_create
            .project_manager,
          subproject_manager: this.permission.subproject_task_create
            .subproject_manager,
          member: this.permission.subproject_task_create.member,
          visitor: this.permission.subproject_task_create.visitor
        };
        this.permissionList.push(row);
      }
      if (this.permission.subproject_task_manage != undefined) {
        let row = {
          operation: "Manage subproject tasks",
          project_manager: this.permission.subproject_task_manage
            .project_manager,
          subproject_manager: this.permission.subproject_task_manage
            .subproject_manager,
          member: this.permission.subproject_task_manage.member,
          visitor: this.permission.subproject_task_manage.visitor
        };
        this.permissionList.push(row);
      }
      if (this.permission.subproject_resource_manage != undefined) {
        let row = {
          operation: "Manage subproject resources",
          project_manager: this.permission.subproject_resource_manage
            .project_manager,
          subproject_manager: this.permission.subproject_resource_manage
            .subproject_manager,
          member: this.permission.subproject_resource_manage.member,
          visitor: this.permission.subproject_resource_manage.visitor
        };
        this.permissionList.push(row);
      }
      if (this.permission.subproject_workspace_type_manage != undefined) {
        let row = {
          operation: "Change the type of workspace in the subproject",
          project_manager: this.permission.subproject_workspace_type_manage
            .project_manager,
          subproject_manager: this.permission.subproject_workspace_type_manage
            .subproject_manager,
          member: this.permission.subproject_workspace_type_manage.member,
          visitor: this.permission.subproject_workspace_type_manage.visitor
        };
        this.permissionList.push(row);
      }
      if (this.permission.activity_manage != undefined) {
        let row = {
          operation: "Manage activities",
          project_manager: this.permission.activity_manage.project_manager,
          subproject_manager: this.permission.activity_manage
            .subproject_manager,
          member: this.permission.activity_manage.member,
          visitor: this.permission.activity_manage.visitor
        };
        this.permissionList.push(row);
      }
      if (this.permission.workspace_resource != undefined) {
        let row = {
          operation: "Manage workspace resources",
          project_manager: this.permission.workspace_resource.project_manager,
          subproject_manager: this.permission.workspace_resource
            .subproject_manager,
          member: this.permission.workspace_resource.member,
          visitor: this.permission.workspace_resource.visitor
        };
        this.permissionList.push(row);
      }
      if (this.permission.workspace_tool != undefined) {
        let row = {
          operation: "Manage workspace tools",
          project_manager: this.permission.workspace_tool.project_manager,
          subproject_manager: this.permission.workspace_tool.subproject_manager,
          member: this.permission.workspace_tool.member,
          visitor: this.permission.workspace_tool.visitor
        };
        this.permissionList.push(row);
      }
      if (this.permission.workspace_edit != undefined) {
        let row = {
          operation: "Edit workspace information",
          project_manager: this.permission.workspace_edit.project_manager,
          subproject_manager: this.permission.workspace_edit.subproject_manager,
          member: this.permission.workspace_edit.member,
          visitor: this.permission.workspace_edit.visitor
        };
        this.permissionList.push(row);
      }
    },
    savePermission(value, key, index, operation) {
      if (operation == "Observe the project") {
        if (key == 1) {
          this.permission.observe.project_manager = value;
          this.permissionList[index].project_manager = value;
        } else if (key == 2) {
          this.permission.observe.subproject_manager = value;
          this.permissionList[index].subproject_manager = value;
        } else if (key == 3) {
          this.permission.observe.member = value;
          this.permissionList[index].member = value;
        } else if (key == 4) {
          this.permission.observe.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Join automatically") {
        if (key == 1) {
          this.permission.auto_join.project_manager = value;
          this.permissionList[index].project_manager = value;
        } else if (key == 2) {
          this.permission.auto_join.subproject_manager = value;
          this.permissionList[index].subproject_manager = value;
        } else if (key == 3) {
          this.permission.auto_join.member = value;
          this.permissionList[index].member = value;
        } else if (key == 4) {
          this.permission.auto_join.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Edit project information") {
        if (key == 1) {
          this.permission.project_edit_info.project_manager = value;
          this.permissionList[index].project_manager = value;
        } else if (key == 2) {
          this.permission.project_edit_info.subproject_manager = value;
          this.permissionList[index].subproject_manager = value;
        } else if (key == 3) {
          this.permission.project_edit_info.member = value;
          this.permissionList[index].member = value;
        } else if (key == 4) {
          this.permission.project_edit_info.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Invite to join project") {
        if (key == 1) {
          this.permission.project_invite_member.project_manager = value;
          this.permissionList[index].project_manager = value;
        } else if (key == 2) {
          this.permission.project_invite_member.subproject_manager = value;
          this.permissionList[index].subproject_manager = value;
        } else if (key == 3) {
          this.permission.project_invite_member.member = value;
          this.permissionList[index].member = value;
        } else if (key == 4) {
          this.permission.project_invite_member.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Remove the project member") {
        if (key == 1) {
          this.permission.project_remove_member.project_manager = value;
          this.permissionList[index].project_manager = value;
        } else if (key == 2) {
          this.permission.project_remove_member.subproject_manager = value;
          this.permissionList[index].subproject_manager = value;
        } else if (key == 3) {
          this.permission.project_remove_member.member = value;
          this.permissionList[index].member = value;
        } else if (key == 4) {
          this.permission.project_remove_member.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Create project tasks") {
        if (key == 1) {
          this.permission.project_task_create.project_manager = value;
          this.permissionList[index].project_manager = value;
        } else if (key == 2) {
          this.permission.project_task_create.subproject_manager = value;
          this.permissionList[index].subproject_manager = value;
        } else if (key == 3) {
          this.permission.project_task_create.member = value;
          this.permissionList[index].member = value;
        } else if (key == 4) {
          this.permission.project_task_create.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Manage project tasks") {
        if (key == 1) {
          this.permission.project_task_manage.project_manager = value;
          this.permissionList[index].project_manager = value;
        } else if (key == 2) {
          this.permission.project_task_manage.subproject_manager = value;
          this.permissionList[index].subproject_manager = value;
        } else if (key == 3) {
          this.permission.project_task_manage.member = value;
          this.permissionList[index].member = value;
        } else if (key == 4) {
          this.permission.project_task_manage.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Manage project resources") {
        if (key == 1) {
          this.permission.project_resource_manage.project_manager = value;
          this.permissionList[index].project_manager = value;
        } else if (key == 2) {
          this.permission.project_resource_manage.subproject_manager = value;
          this.permissionList[index].subproject_manager = value;
        } else if (key == 3) {
          this.permission.project_resource_manage.member = value;
          this.permissionList[index].member = value;
        } else if (key == 4) {
          this.permission.project_resource_manage.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Change the type of workspace in the project") {
        if (key == 1) {
          this.permission.project_workspace_type_manage.project_manager = value;
          this.permissionList[index].project_manager = value;
        } else if (key == 2) {
          this.permission.project_workspace_type_manage.subproject_manager = value;
          this.permissionList[index].subproject_manager = value;
        } else if (key == 3) {
          this.permission.project_workspace_type_manage.member = value;
          this.permissionList[index].member = value;
        } else if (key == 4) {
          this.permission.project_workspace_type_manage.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Manage subprojects") {
        if (key == 1) {
          this.permission.subprojects_manage.project_manager = value;
          this.permissionList[index].project_manager = value;
        } else if (key == 2) {
          this.permission.subprojects_manage.subproject_manager = value;
          this.permissionList[index].subproject_manager = value;
        } else if (key == 3) {
          this.permission.subprojects_manage.member = value;
          this.permissionList[index].member = value;
        } else if (key == 4) {
          this.permission.subprojects_manage.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Edit subproject information") {
        if (key == 1) {
          this.permission.subproject_edit_info.project_manager = value;
          this.permissionList[index].project_manager = value;
        } else if (key == 2) {
          this.permission.subproject_edit_info.subproject_manager = value;
          this.permissionList[index].subproject_manager = value;
        } else if (key == 3) {
          this.permission.subproject_edit_info.member = value;
          this.permissionList[index].member = value;
        } else if (key == 4) {
          this.permission.subproject_edit_info.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Invite to join subproject") {
        if (key == 1) {
          this.permission.subproject_invite_member.project_manager = value;
          this.permissionList[index].project_manager = value;
        } else if (key == 2) {
          this.permission.subproject_invite_member.subproject_manager = value;
          this.permissionList[index].subproject_manager = value;
        } else if (key == 3) {
          this.permission.subproject_invite_member.member = value;
          this.permissionList[index].member = value;
        } else if (key == 4) {
          this.permission.subproject_invite_member.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Remove the subproject member") {
        if (key == 1) {
          this.permission.subproject_remove_member.project_manager = value;
          this.permissionList[index].project_manager = value;
        } else if (key == 2) {
          this.permission.subproject_remove_member.subproject_manager = value;
          this.permissionList[index].subproject_manager = value;
        } else if (key == 3) {
          this.permission.subproject_remove_member.member = value;
          this.permissionList[index].member = value;
        } else if (key == 4) {
          this.permission.subproject_remove_member.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Create subproject tasks") {
        if (key == 1) {
          this.permission.subproject_task_create.project_manager = value;
          this.permissionList[index].project_manager = value;
        } else if (key == 2) {
          this.permission.subproject_task_create.subproject_manager = value;
          this.permissionList[index].subproject_manager = value;
        } else if (key == 3) {
          this.permission.subproject_task_create.member = value;
          this.permissionList[index].member = value;
        } else if (key == 4) {
          this.permission.subproject_task_create.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Manage subproject tasks") {
        if (key == 1) {
          this.permission.subproject_task_manage.project_manager = value;
          this.permissionList[index].project_manager = value;
        } else if (key == 2) {
          this.permission.subproject_task_manage.subproject_manager = value;
          this.permissionList[index].subproject_manager = value;
        } else if (key == 3) {
          this.permission.subproject_task_manage.member = value;
          this.permissionList[index].member = value;
        } else if (key == 4) {
          this.permission.subproject_task_manage.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Manage subproject resources") {
        if (key == 1) {
          this.permission.subproject_resource_manage.project_manager = value;
          this.permissionList[index].project_manager = value;
        } else if (key == 2) {
          this.permission.subproject_resource_manage.subproject_manager = value;
          this.permissionList[index].subproject_manager = value;
        } else if (key == 3) {
          this.permission.subproject_resource_manage.member = value;
          this.permissionList[index].member = value;
        } else if (key == 4) {
          this.permission.subproject_resource_manage.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Change the type of workspace in the subproject") {
        if (key == 1) {
          this.permission.subproject_workspace_type_manage.project_manager = value;
          this.permissionList[index].project_manager = value;
        } else if (key == 2) {
          this.permission.subproject_workspace_type_manage.subproject_manager = value;
          this.permissionList[index].subproject_manager = value;
        } else if (key == 3) {
          this.permission.subproject_workspace_type_manage.member = value;
          this.permissionList[index].member = value;
        } else if (key == 4) {
          this.permission.subproject_workspace_type_manage.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Manage activities") {
        if (key == 1) {
          this.permission.activity_manage.project_manager = value;
          this.permissionList[index].project_manager = value;
        } else if (key == 2) {
          this.permission.activity_manage.subproject_manager = value;
          this.permissionList[index].subproject_manager = value;
        } else if (key == 3) {
          this.permission.activity_manage.member = value;
          this.permissionList[index].member = value;
        } else if (key == 4) {
          this.permission.activity_manage.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Manage workspace resources") {
        if (key == 1) {
          this.permission.workspace_resource.project_manager = value;
          this.permissionList[index].project_manager = value;
        } else if (key == 2) {
          this.permission.workspace_resource.subproject_manager = value;
          this.permissionList[index].subproject_manager = value;
        } else if (key == 3) {
          this.permission.workspace_resource.member = value;
          this.permissionList[index].member = value;
        } else if (key == 4) {
          this.permission.workspace_resource.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Manage workspace tools") {
        if (key == 1) {
          this.permission.workspace_tool.project_manager = value;
          this.permissionList[index].project_manager = value;
        } else if (key == 2) {
          this.permission.workspace_tool.subproject_manager = value;
          this.permissionList[index].subproject_manager = value;
        } else if (key == 3) {
          this.permission.workspace_tool.member = value;
          this.permissionList[index].member = value;
        } else if (key == 4) {
          this.permission.workspace_tool.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
      if (operation == "Edit workspace information") {
        if (key == 1) {
          this.permission.workspace_edit.project_manager = value;
          this.permissionList[index].project_manager = value;
        } else if (key == 2) {
          this.permission.workspace_edit.subproject_manager = value;
          this.permissionList[index].subproject_manager = value;
        } else if (key == 3) {
          this.permission.workspace_edit.member = value;
          this.permissionList[index].member = value;
        } else if (key == 4) {
          this.permission.workspace_edit.visitor = value;
          this.permissionList[index].visitor = value;
        }
      }
    },
    setDefault() {
      if (this.projectInfo.privacy == "Public") {
        this.permission = JSON.parse(JSON.stringify(this.defaultPublic));
      } else if (this.projectInfo.privacy == "Discoverable") {
        this.permission = JSON.parse(JSON.stringify(this.defaultDiscoverable));
      } else if (this.projectInfo.privacy == "Private") {
        this.permission = JSON.parse(JSON.stringify(this.defaultPrivate));
      }
      this.getPermissionList();
    },
    save2Project() {
      let obj = new FormData();
      obj.append("projectId", this.projectId);
      obj.append("permissionManager", JSON.stringify(this.permission));
      this.axios
        .post("/GeoProblemSolving/project/update", obj)
        .then(res => {
          this.resetProjectTypeModel = false;
          if (res.data == "Offline") {
            parent.location.href = "/GeoProblemSolving/login";
          } else if (res.data != "Fail") {
            this.$store.commit("setProjectInfo", res.data);
            this.$Notice.info({
              desc: "Update permission successfully. "
            });
          } else {
            this.$Message.error("Failed to save permission.");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    }
  }
};
</script>