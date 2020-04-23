<style scoped>
.btnAuthority {
  margin-top: 20px;
  display: flex;
  justify-content: space-around;
}
</style>
<template>
  <Row>
    <Col span="6" offset="2">
      <h1 style="margin-top: 20px;">Authority management</h1>
      <div style="margin: 20px 0">
        <h2>Project name</h2>
        <Divider style="margin: 5px 0" />
        <div>{{projectInfo.title}}</div>
      </div>
      <div style="margin: 20px 0">
        <h2>Project description</h2>
        <Divider style="margin: 5px 0" />
        <div>{{projectInfo.description}}</div>
      </div>
      <div class="btnAuthority">
        <Button @click="setDefault">Default</Button>
        <Button @click="save2Project">Save</Button>
      </div>
    </Col>
    <Col span="14" style="margin: 30px 15px">
      <Table
        :columns="authorityHead"
        :data="authorityList"
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
      //权限------------------------------??双向绑定
      authorityHead: [
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
                    this.saveAuthority(e, 1, params.row.operation);
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
                    size: "small"
                  },
                  attrs: {
                    title: row_value
                  },
                  on: {
                    "on-change": e => {
                      this.saveAuthority(e, 1, params.row.operation);
                    }
                  }
                },
                [
                  h("Option", {
                    props: {
                      value: "Yes",
                      key: "Yes"
                    }
                  }),
                  h("Option", {
                    props: {
                      value: "Yes, partly",
                      key: "Yes, partly"
                    }
                  }),
                  h("Option", {
                    props: {
                      value: "No",
                      key: "No"
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
                    this.saveAuthority(e, 2, params.row.operation);
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
                    size: "small"
                  },
                  attrs: {
                    title: row_value
                  },
                  on: {
                    "on-change": e => {
                      this.saveAuthority(e, 2, params.row.operation);
                    }
                  }
                },
                [
                  h("Option", {
                    props: {
                      value: "Yes",
                      key: "Yes"
                    }
                  }),
                  h("Option", {
                    props: {
                      value: "Yes, partly",
                      key: "Yes, partly"
                    }
                  }),
                  h("Option", {
                    props: {
                      value: "No",
                      key: "No"
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
                    this.saveAuthority(e, 3, params.row.operation);
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
                    size: "small"
                  },
                  attrs: {
                    title: row_value
                  },
                  on: {
                    "on-change": e => {
                      this.saveAuthority(e, 3, params.row.operation);
                    }
                  }
                },
                [
                  h("Option", {
                    props: {
                      value: "Yes",
                      key: "Yes"
                    }
                  }),
                  h("Option", {
                    props: {
                      value: "Yes, partly",
                      key: "Yes, partly"
                    }
                  }),
                  h("Option", {
                    props: {
                      value: "No",
                      key: "No"
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
          title: "Visitor",
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
                    this.saveAuthority(e, 4, params.row.operation);
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
                    size: "small"
                  },
                  attrs: {
                    title: row_value
                  },
                  on: {
                    "on-change": e => {
                      this.saveAuthority(e, 4, params.row.operation);
                    }
                  }
                },
                [
                  h("Option", {
                    props: {
                      value: "Yes",
                      key: "Yes"
                    }
                  }),
                  h("Option", {
                    props: {
                      value: "Yes, partly",
                      key: "Yes, partly"
                    }
                  }),
                  h("Option", {
                    props: {
                      value: "No",
                      key: "No"
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
      authorityList: [],
      defaultAuthority: {
        project_edit_info: {
          project_manager: true,
          subproject_manager: null,
          member: false,
          visitor: false
        },
        project_invite_member: {
          project_manager: true,
          subproject_manager: null,
          member: false,
          visitor: false
        },
        project_remove_member: {
          project_manager: true,
          subproject_manager: null,
          member: false,
          visitor: false
        },
        project_task_assignment: {
          project_manager: true,
          subproject_manager: null,
          member: true,
          visitor: false
        },
        project_resource_manage: {
          project_manager: true,
          subproject_manager: null,
          member: "Yes, partly",
          visitor: false
        },
        project_workspace_type_manage: {
          project_manager: true,
          subproject_manager: null,
          member: false,
          visitor: false
        },
        subprojects_manage: {
          project_manager: true,
          subproject_manager: null,
          member: "Yes, partly",
          visitor: false
        },
        subproject_edit_info: {
          project_manager: false,
          subproject_manager: true,
          member: false,
          visitor: false
        },
        subproject_invite_member: {
          project_manager: false,
          subproject_manager: true,
          member: false,
          visitor: false
        },
        subproject_remove_member: {
          project_manager: false,
          subproject_manager: true,
          member: false,
          visitor: false
        },
        subproject_task_assignment: {
          project_manager: false,
          subproject_manager: true,
          member: true,
          visitor: false
        },
        subproject_resource_manage: {
          project_manager: "Yes, partly",
          subproject_manager: true,
          member: "Yes, partly",
          visitor: false
        },
        subproject_workspace_type_manage: {
          project_manager: true,
          subproject_manager: true,
          member: false,
          visitor: false
        },
        activity_manage: {
          project_manager: false,
          subproject_manager: true,
          member: false,
          visitor: false
        },
        workspace_resource: {
          project_manager: true,
          subproject_manager: true,
          member: true,
          visitor: false
        },
        workspace_tool: {
          project_manager: true,
          subproject_manager: true,
          member: true,
          visitor: false
        },
        workspace_edit: {
          project_manager: false,
          subproject_manager: true,
          member: false,
          visitor: false
        }
      },
      authority: {},
      // project 信息
      projectId: this.$route.params.id,
      projectInfo: {}
    };
  },
  mounted() {
    this.getAuthority();
  },
  methods: {
    getAuthority() {
      let projectInfo = this.$store.getters.project;

      if (JSON.stringify(projectInfo) != "{}") {
        this.projectInfo = projectInfo;
        if (this.projectInfo.authorityManager != undefined) {
          this.authority = this.projectInfo.authorityManager;
        } else {
          this.authority = JSON.parse(JSON.stringify(this.defaultAuthority));
        }
        this.getAuthorityList();
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

              if (this.projectInfo.authorityManager != undefined) {
                this.authority = this.projectInfo.authorityManager;
              } else {
                this.authority = JSON.parse(
                  JSON.stringify(this.defaultAuthority)
                );
              }
              this.getAuthorityList();
            } else {
              console.log(data);
            }
          }
        });
      }
    },
    getAuthorityList() {
      this.authorityList = [];
      if (this.authority.project_edit_info != undefined) {
        let row = {
          operation: "Edit project information",
          project_manager: this.authority.project_edit_info.project_manager,
          subproject_manager: this.authority.project_edit_info
            .subproject_manager,
          member: this.authority.project_edit_info.member,
          visitor: this.authority.project_edit_info.visitor
        };
        this.authorityList.push(row);
      }
      if (this.authority.project_invite_member != undefined) {
        let row = {
          operation: "Invite to join project",
          project_manager: this.authority.project_invite_member.project_manager,
          subproject_manager: this.authority.project_invite_member
            .subproject_manager,
          member: this.authority.project_invite_member.member,
          visitor: this.authority.project_invite_member.visitor
        };
        this.authorityList.push(row);
      }
      if (this.authority.project_remove_member != undefined) {
        let row = {
          operation: "Remove the project member",
          project_manager: this.authority.project_remove_member.project_manager,
          subproject_manager: this.authority.project_remove_member
            .subproject_manager,
          member: this.authority.project_remove_member.member,
          visitor: this.authority.project_remove_member.visitor
        };
        this.authorityList.push(row);
      }
      if (this.authority.project_task_assignment != undefined) {
        let row = {
          operation: "Manage project tasks",
          project_manager: this.authority.project_task_assignment
            .project_manager,
          subproject_manager: this.authority.project_task_assignment
            .subproject_manager,
          member: this.authority.project_task_assignment.member,
          visitor: this.authority.project_task_assignment.visitor
        };
        this.authorityList.push(row);
      }
      if (this.authority.project_resource_manage != undefined) {
        let row = {
          operation: "Manage project resources",
          project_manager: this.authority.project_resource_manage
            .project_manager,
          subproject_manager: this.authority.project_resource_manage
            .subproject_manager,
          member: this.authority.project_resource_manage.member,
          visitor: this.authority.project_resource_manage.visitor
        };
        this.authorityList.push(row);
      }
      if (this.authority.project_workspace_type_manage != undefined) {
        let row = {
          operation: "Change the type of workspace in the project",
          project_manager: this.authority.project_workspace_type_manage
            .project_manager,
          subproject_manager: this.authority.project_workspace_type_manage
            .subproject_manager,
          member: this.authority.project_workspace_type_manage.member,
          visitor: this.authority.project_workspace_type_manage.visitor
        };
        this.authorityList.push(row);
      }
      if (this.authority.subprojects_manage != undefined) {
        let row = {
          operation: "Manage subprojects",
          project_manager: this.authority.subprojects_manage.project_manager,
          subproject_manager: this.authority.subprojects_manage
            .subproject_manager,
          member: this.authority.subprojects_manage.member,
          visitor: this.authority.subprojects_manage.visitor
        };
        this.authorityList.push(row);
      }
      if (this.authority.subproject_edit_info != undefined) {
        let row = {
          operation: "Edit subproject information",
          project_manager: this.authority.subproject_edit_info.project_manager,
          subproject_manager: this.authority.subproject_edit_info
            .subproject_manager,
          member: this.authority.subproject_edit_info.member,
          visitor: this.authority.subproject_edit_info.visitor
        };
        this.authorityList.push(row);
      }
      if (this.authority.subproject_invite_member != undefined) {
        let row = {
          operation: "Invite to join subproject",
          project_manager: this.authority.subproject_invite_member
            .project_manager,
          subproject_manager: this.authority.subproject_invite_member
            .subproject_manager,
          member: this.authority.subproject_invite_member.member,
          visitor: this.authority.subproject_invite_member.visitor
        };
        this.authorityList.push(row);
      }
      if (this.authority.subproject_remove_member != undefined) {
        let row = {
          operation: "Remove the subproject member",
          project_manager: this.authority.subproject_remove_member
            .project_manager,
          subproject_manager: this.authority.subproject_remove_member
            .subproject_manager,
          member: this.authority.subproject_remove_member.member,
          visitor: this.authority.subproject_remove_member.visitor
        };
        this.authorityList.push(row);
      }
      if (this.authority.subproject_task_assignment != undefined) {
        let row = {
          operation: "Manage subproject tasks",
          project_manager: this.authority.subproject_task_assignment
            .project_manager,
          subproject_manager: this.authority.subproject_task_assignment
            .subproject_manager,
          member: this.authority.subproject_task_assignment.member,
          visitor: this.authority.subproject_task_assignment.visitor
        };
        this.authorityList.push(row);
      }
      if (this.authority.subproject_resource_manage != undefined) {
        let row = {
          operation: "Manage subproject resources",
          project_manager: this.authority.subproject_resource_manage
            .project_manager,
          subproject_manager: this.authority.subproject_resource_manage
            .subproject_manager,
          member: this.authority.subproject_resource_manage.member,
          visitor: this.authority.subproject_resource_manage.visitor
        };
        this.authorityList.push(row);
      }
      if (this.authority.subproject_workspace_type_manage != undefined) {
        let row = {
          operation: "Change the type of workspace in the subproject",
          project_manager: this.authority.subproject_workspace_type_manage
            .project_manager,
          subproject_manager: this.authority.subproject_workspace_type_manage
            .subproject_manager,
          member: this.authority.subproject_workspace_type_manage.member,
          visitor: this.authority.subproject_workspace_type_manage.visitor
        };
        this.authorityList.push(row);
      }
      if (this.authority.activity_manage != undefined) {
        let row = {
          operation: "Manage activities",
          project_manager: this.authority.activity_manage.project_manager,
          subproject_manager: this.authority.activity_manage.subproject_manager,
          member: this.authority.activity_manage.member,
          visitor: this.authority.activity_manage.visitor
        };
        this.authorityList.push(row);
      }
      if (this.authority.workspace_resource != undefined) {
        let row = {
          operation: "Manage workspace resources",
          project_manager: this.authority.workspace_resource.project_manager,
          subproject_manager: this.authority.workspace_resource
            .subproject_manager,
          member: this.authority.workspace_resource.member,
          visitor: this.authority.workspace_resource.visitor
        };
        this.authorityList.push(row);
      }
      if (this.authority.workspace_tool != undefined) {
        let row = {
          operation: "Manage workspace tools",
          project_manager: this.authority.workspace_tool.project_manager,
          subproject_manager: this.authority.workspace_tool.subproject_manager,
          member: this.authority.workspace_tool.member,
          visitor: this.authority.workspace_tool.visitor
        };
        this.authorityList.push(row);
      }
      if (this.authority.workspace_edit != undefined) {
        let row = {
          operation: "Edit workspace information",
          project_manager: this.authority.workspace_edit.project_manager,
          subproject_manager: this.authority.workspace_edit.subproject_manager,
          member: this.authority.workspace_edit.member,
          visitor: this.authority.workspace_edit.visitor
        };
        this.authorityList.push(row);
      }
    },
    saveAuthority(value, key, operation) {
      if (operation == "Edit project information") {
        if (key == 1) {
          this.authority.project_edit_info.project_manager = value;
        } else if (key == 2) {
          this.authority.project_edit_info.subproject_manager = value;
        } else if (key == 3) {
          this.authority.project_edit_info.member = value;
        } else if (key == 4) {
          this.authority.project_edit_info.visitor = value;
        }
      }
      if (operation == "Invite to join project") {
        if (key == 1) {
          this.authority.project_invite_member.project_manager = value;
        } else if (key == 2) {
          this.authority.project_invite_member.subproject_manager = value;
        } else if (key == 3) {
          this.authority.project_invite_member.member = value;
        } else if (key == 4) {
          this.authority.project_invite_member.visitor = value;
        }
      }
      if (operation == "Remove the project member") {
        if (key == 1) {
          this.authority.project_remove_member.project_manager = value;
        } else if (key == 2) {
          this.authority.project_remove_member.subproject_manager = value;
        } else if (key == 3) {
          this.authority.project_remove_member.member = value;
        } else if (key == 4) {
          this.authority.project_remove_member.visitor = value;
        }
      }
      if (operation == "Manage project tasks") {
        if (key == 1) {
          this.authority.project_task_assignment.project_manager = value;
        } else if (key == 2) {
          this.authority.project_task_assignment.subproject_manager = value;
        } else if (key == 3) {
          this.authority.project_task_assignment.member = value;
        } else if (key == 4) {
          this.authority.project_task_assignment.visitor = value;
        }
      }
      if (operation == "Manage project resources") {
        if (key == 1) {
          this.authority.project_resource_manage.project_manager = value;
        } else if (key == 2) {
          this.authority.project_resource_manage.subproject_manager = value;
        } else if (key == 3) {
          this.authority.project_resource_manage.member = value;
        } else if (key == 4) {
          this.authority.project_resource_manage.visitor = value;
        }
      }
      if (operation == "Change the type of workspace in the project") {
        if (key == 1) {
          this.authority.project_workspace_type_manage.project_manager = value;
        } else if (key == 2) {
          this.authority.project_workspace_type_manage.subproject_manager = value;
        } else if (key == 3) {
          this.authority.project_workspace_type_manage.member = value;
        } else if (key == 4) {
          this.authority.project_workspace_type_manage.visitor = value;
        }
      }
      if (operation == "Manage subprojects") {
        if (key == 1) {
          this.authority.subprojects_manage.project_manager = value;
        } else if (key == 2) {
          this.authority.subprojects_manage.subproject_manager = value;
        } else if (key == 3) {
          this.authority.subprojects_manage.member = value;
        } else if (key == 4) {
          this.authority.subprojects_manage.visitor = value;
        }
      }
      if (operation == "Edit subproject information") {
        if (key == 1) {
          this.authority.subproject_edit_info.project_manager = value;
        } else if (key == 2) {
          this.authority.subproject_edit_info.subproject_manager = value;
        } else if (key == 3) {
          this.authority.subproject_edit_info.member = value;
        } else if (key == 4) {
          this.authority.subproject_edit_info.visitor = value;
        }
      }
      if (operation == "Invite to join subproject") {
        if (key == 1) {
          this.authority.subproject_invite_member.project_manager = value;
        } else if (key == 2) {
          this.authority.subproject_invite_member.subproject_manager = value;
        } else if (key == 3) {
          this.authority.subproject_invite_member.member = value;
        } else if (key == 4) {
          this.authority.subproject_invite_member.visitor = value;
        }
      }
      if (operation == "Remove the subproject member") {
        if (key == 1) {
          this.authority.subproject_remove_member.project_manager = value;
        } else if (key == 2) {
          this.authority.subproject_remove_member.subproject_manager = value;
        } else if (key == 3) {
          this.authority.subproject_remove_member.member = value;
        } else if (key == 4) {
          this.authority.subproject_remove_member.visitor = value;
        }
      }
      if (operation == "Manage subproject tasks") {
        if (key == 1) {
          this.authority.subproject_task_assignment.project_manager = value;
        } else if (key == 2) {
          this.authority.subproject_task_assignment.subproject_manager = value;
        } else if (key == 3) {
          this.authority.subproject_task_assignment.member = value;
        } else if (key == 4) {
          this.authority.subproject_task_assignment.visitor = value;
        }
      }
      if (operation == "Manage subproject resources") {
        if (key == 1) {
          this.authority.subproject_resource_manage.project_manager = value;
        } else if (key == 2) {
          this.authority.subproject_resource_manage.subproject_manager = value;
        } else if (key == 3) {
          this.authority.subproject_resource_manage.member = value;
        } else if (key == 4) {
          this.authority.subproject_resource_manage.visitor = value;
        }
      }
      if (operation == "Change the type of workspace in the subproject") {
        if (key == 1) {
          this.authority.subproject_workspace_type_manage.project_manager = value;
        } else if (key == 2) {
          this.authority.subproject_workspace_type_manage.subproject_manager = value;
        } else if (key == 3) {
          this.authority.subproject_workspace_type_manage.member = value;
        } else if (key == 4) {
          this.authority.subproject_workspace_type_manage.visitor = value;
        }
      }
      if (operation == "Manage activities") {
        if (key == 1) {
          this.authority.activity_manage.project_manager = value;
        } else if (key == 2) {
          this.authority.activity_manage.subproject_manager = value;
        } else if (key == 3) {
          this.authority.activity_manage.member = value;
        } else if (key == 4) {
          this.authority.activity_manage.visitor = value;
        }
      }
      if (operation == "Manage workspace resources") {
        if (key == 1) {
          this.authority.workspace_resource.project_manager = value;
        } else if (key == 2) {
          this.authority.workspace_resource.subproject_manager = value;
        } else if (key == 3) {
          this.authority.workspace_resource.member = value;
        } else if (key == 4) {
          this.authority.workspace_resource.visitor = value;
        }
      }
      if (operation == "Manage workspace tools") {
        if (key == 1) {
          this.authority.workspace_tool.project_manager = value;
        } else if (key == 2) {
          this.authority.workspace_tool.subproject_manager = value;
        } else if (key == 3) {
          this.authority.workspace_tool.member = value;
        } else if (key == 4) {
          this.authority.workspace_tool.visitor = value;
        }
      }
      if (operation == "Edit workspace information") {
        if (key == 1) {
          this.authority.workspace_edit.project_manager = value;
        } else if (key == 2) {
          this.authority.workspace_edit.subproject_manager = value;
        } else if (key == 3) {
          this.authority.workspace_edit.member = value;
        } else if (key == 4) {
          this.authority.workspace_edit.visitor = value;
        }
      }
    },
    setDefault() {
      this.authority = JSON.parse(JSON.stringify(this.defaultAuthority));
      this.getAuthorityList();
    },
    save2Project() {}
  }
};
</script>