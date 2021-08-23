/**
 * User roles and permissions
 */

var defaultPermission = {
    observe: {
        "manager": null,
        "coreteam": null,
        "widerteam": null,
        "visitor": "No"
    },
    auto_join: {
        "manager": null,
        "coreteam": null,
        "widerteam": null,
        "visitor": "No"
    },
    edit_info: {
        "manager": true,
        "coreteam": true,
        "widerteam": false,
        "visitor": null
    },
    invite_member: {
        "manager": true,
        "coreteam": true,
        "widerteam": true,
        "visitor": null
    },
    manage_member: {
        "manager": true,
        "coreteam": false,
        "widerteam": false,
        "visitor": null
    },
    create_task: {
        "manager": true,
        "coreteam": false,
        "widerteam": false,
        "visitor": null
    },
    manage_task: {
        "manager": true,
        "coreteam": false,
        "widerteam": false,
        "visitor": null
    },
    upload_resource: {
        "manager": true,
        "coreteam": true,
        "widerteam": true,
        "visitor": null
    },
    use_resource: {
        "manager": true,
        "coreteam": true,
        "widerteam": true,
        "visitor": null
    },
    manage_resource: {
        "manager": true,
        "coreteam": false,
        "widerteam": false,
        "visitor": null
    },
    manage_workspace_type: {
        "manager": true,
        "coreteam": false,
        "widerteam": false,
        "visitor": null
    },
    import_tool: {
        "manager": true,
        "coreteam": true,
        "widerteam": false,
        "visitor": null
    },
    use_tool: {
        "manager": true,
        "coreteam": true,
        "widerteam": true,
        "visitor": null
    },
    manage_tool: {
        "manager": true,
        "coreteam": false,
        "widerteam": false,
        "visitor": null
    },
    manage_child_activity: {
        "manager": true,
        "coreteam": false,
        "widerteam": false,
        "visitor": null
    }
}

// Identify user roles
function roleIdentify(members, userId) {
    var role = "visitor";
    for (var i = 0; i < members.length; i++) {
        if (members[i].userId == userId) {
            role = members[i].role;
            break;
        }
    }
    return role;
}

// Identify user role level
function roleLevelIdentify(role) {
    var roleLevel = "visitor";
    switch (role) {
        case "manager":
        case "creator": {
            roleLevel = "manager";
            break;
        };
        case "researcher":
        case "expert":
        case "decision-maker":
        case "core-member": {
            roleLevel = "coreteam";
            break;
        };
        case "stakeholder":
        case "consultant":
        case "ordinary-member": {
            roleLevel = "widerteam";
            break;
        };
        case "visitor": {
            roleLevel = "visitor";
            break;
        }
    }
    return roleLevel;
}

// Get default permission
function getDefault() {
    var permission = defaultPermission;
    return permission;
}

// Compare the level between roles
// 0: same level; 1: role1 has the higher level than role2; -1: role1 has the lower level than role2
function roleCompare(role1, role2) {
    let roleLevel1 = roleLevelIdentify(role1);
    let roleLevel2 = roleLevelIdentify(role2);
    if (roleLevel1 == "manager") {
        if (roleLevel2 == "manager")
            return 0;
        else
            return 1
    } else if (roleLevel1 == "coreteam") {
        if (roleLevel2 == "manager")
            return -1;
        else if (roleLevel2 == "coreteam")
            return 0
        else
            return 1
    } else if (roleLevel1 == "widerteam") {
        if (roleLevel2 == "visitor")
            return 1;
        else if (roleLevel2 == "widerteam")
            return 0
        else
            return -1
    } else if (roleLevel1 == "visitor") {
        if (roleLevel2 == "visitor")
            return 0;
        else
            return -1
    }
}

// Get participants' userId by their roles
function getMemberByRole(activity, role) {
    let members = [];
    for (let i = 0; i < activity.members.length; i++) {
        if (activity.members[i].role == role) {
            members.push(activity.members[i].userId)
        }
    }
    return members;
}

// Permission: from Json to Array
function permissionJson2Array(currentPermission) {

    var permission = {};
    if (currentPermission == undefined || currentPermission == {})
        permission = defaultPermission;
    else
        permission = currentPermission;

    var permissionArray = [];
    if (permission.observe != undefined) {
        var row = {
            operation: "Learn more about the activity",
            manager: permission.observe.manager,
            coreteam: permission.observe.coreteam,
            widerteam: permission.observe.widerteam,
            visitor: permission.observe.visitor
        };
        permissionArray.push(row);
    }
    if (permission.auto_join != undefined) {
        var row = {
            operation: "Join the activity automatically",
            manager: permission.auto_join.manager,
            coreteam: permission.auto_join.coreteam,
            widerteam: permission.auto_join.widerteam,
            visitor: permission.auto_join.visitor
        };
        permissionArray.push(row);
    }
    if (permission.edit_info != undefined) {
        var row = {
            operation: "Edit the information of the activity",
            manager: permission.edit_info.manager,
            coreteam: permission.edit_info.coreteam,
            widerteam: permission.edit_info.widerteam,
            visitor: permission.edit_info.visitor
        };
        permissionArray.push(row);
    }
    if (permission.invite_member != undefined) {
        var row = {
            operation: "Invite other person to join the activity",
            manager: permission.invite_member.manager,
            coreteam: permission.invite_member.coreteam,
            widerteam: permission.invite_member.widerteam,
            visitor: permission.invite_member.visitor
        };
        permissionArray.push(row);
    }
    if (permission.manage_member != undefined) {
        var row = {
            operation: "Manage members",
            manager: permission.manage_member.manager,
            coreteam: permission.manage_member.coreteam,
            widerteam: permission.manage_member.widerteam,
            visitor: permission.manage_member.visitor
        };
        permissionArray.push(row);
    }
    if (permission.create_task != undefined) {
        var row = {
            operation: "Create tasks in activities",
            manager: permission.create_task.manager,
            coreteam: permission.create_task.coreteam,
            widerteam: permission.create_task.widerteam,
            visitor: permission.create_task.visitor
        };
        permissionArray.push(row);
    }
    if (permission.manage_task != undefined) {
        var row = {
            operation: "Manage tasks in activities",
            manager: permission.manage_task.manager,
            coreteam: permission.manage_task.coreteam,
            widerteam: permission.manage_task.widerteam,
            visitor: permission.manage_task.visitor
        };
        permissionArray.push(row);
    }
    if (permission.upload_resource != undefined) {
        var row = {
            operation: "Upload resources",
            manager: permission.upload_resource.manager,
            coreteam: permission.upload_resource.coreteam,
            widerteam: permission.upload_resource.widerteam,
            visitor: permission.upload_resource.visitor
        };
        permissionArray.push(row);
    }
    if (permission.use_resource != undefined) {
        var row = {
            operation: "Use resources",
            manager: permission.use_resource.manager,
            coreteam: permission.use_resource.coreteam,
            widerteam: permission.use_resource.widerteam,
            visitor: permission.use_resource.visitor
        };
        permissionArray.push(row);
    }
    if (permission.manage_resource != undefined) {
        var row = {
            operation: "Manage resources",
            manager: permission.manage_resource.manager,
            coreteam: permission.manage_resource.coreteam,
            widerteam: permission.manage_resource.widerteam,
            visitor: permission.manage_resource.visitor
        };
        permissionArray.push(row);
    }
    if (permission.manage_workspace_type != undefined) {
        var row = {
            operation: "Switch the type of workspace",
            manager: permission.manage_workspace_type.manager,
            coreteam: permission.manage_workspace_type.coreteam,
            widerteam: permission.manage_workspace_type.widerteam,
            visitor: permission.manage_workspace_type.visitor
        };
        permissionArray.push(row);
    }
    if (permission.import_tool != undefined) {
        var row = {
            operation: "Import tools or toolsets",
            manager: permission.import_tool.manager,
            coreteam: permission.import_tool.coreteam,
            widerteam: permission.import_tool.widerteam,
            visitor: permission.import_tool.visitor
        };
        permissionArray.push(row);
    }
    if (permission.use_tool != undefined) {
        var row = {
            operation: "Use tools or toolsets",
            manager: permission.use_tool.manager,
            coreteam: permission.use_tool.coreteam,
            widerteam: permission.use_tool.widerteam,
            visitor: permission.use_tool.visitor
        };
        permissionArray.push(row);
    }
    if (permission.manage_tool != undefined) {
        var row = {
            operation: "Manage tools or toolsets",
            manager: permission.manage_tool.manager,
            coreteam: permission.manage_tool.coreteam,
            widerteam: permission.manage_tool.widerteam,
            visitor: permission.manage_tool.visitor
        };
        permissionArray.push(row);
    }
    if (permission.manage_child_activity != undefined) {
        var row = {
            operation: "Manage child activity",
            manager: permission.manage_child_activity.manager,
            coreteam: permission.manage_child_activity.coreteam,
            widerteam: permission.manage_child_activity.widerteam,
            visitor: permission.manage_child_activity.visitor
        };
        permissionArray.push(row);
    }
    return permissionArray;
}

/**
 * 判断权限
 * @param {*} currentPermission
 * @param {*} role
 * @param {*} operation
 */
function permissionIdentity(currentPermission, role, operation) {

    var permission = {};
    if (currentPermission == undefined || currentPermission == {}) {
        permission = defaultPermission;
    }
    else {
        permission = currentPermission;
    }

    var roleLevel = roleLevelIdentify(role);

    if (operation === "edit_info") {
        if (roleLevel == "manager") {
            return permission.edit_info.manager;
        } else if (roleLevel == "coreteam") {
            return permission.edit_info.coreteam;
        } else if (roleLevel == "widerteam") {
            return permission.edit_info.widerteam;
        } else if (roleLevel == "visitor") {
            return permission.edit_info.visitor;
        }
    }
    else if (operation === "invite_member") {
        if (roleLevel == "manager") {
            return permission.invite_member.manager;
        } else if (roleLevel == "coreteam") {
            return permission.invite_member.coreteam;
        } else if (roleLevel == "widerteam") {
            return permission.invite_member.widerteam;
        } else if (roleLevel == "visitor") {
            return permission.invite_member.visitor;
        }
    }
    else if (operation === "manage_member") {
        if (roleLevel == "manager") {
            return permission.manage_member.manager;
        } else if (roleLevel == "coreteam") {
            return permission.manage_member.coreteam;
        } else if (roleLevel == "widerteam") {
            return permission.manage_member.widerteam;
        } else if (roleLevel == "visitor") {
            return permission.manage_member.visitor;
        }
    }
    else if (operation === "create_task") {
        if (roleLevel == "manager") {
            return permission.create_task.manager;
        } else if (roleLevel == "coreteam") {
            return permission.create_task.coreteam;
        } else if (roleLevel == "widerteam") {
            return permission.create_task.widerteam;
        } else if (roleLevel == "visitor") {
            return permission.create_task.visitor;
        }
    }
    else if (operation === "manage_task") {
        if (roleLevel == "manager") {
            return permission.manage_task.manager;
        } else if (roleLevel == "coreteam") {
            return permission.manage_task.coreteam;
        } else if (roleLevel == "widerteam") {
            return permission.manage_task.widerteam;
        } else if (roleLevel == "visitor") {
            return permission.manage_task.visitor;
        }
    }
    else if (operation === "upload_resource") {
        if (roleLevel == "manager") {
            return permission.upload_resource.manager;
        } else if (roleLevel == "coreteam") {
            return permission.upload_resource.coreteam;
        } else if (roleLevel == "widerteam") {
            return permission.upload_resource.widerteam;
        } else if (roleLevel == "visitor") {
            return permission.upload_resource.visitor;
        }
    }
    else if (operation === "use_resource") {
        if (roleLevel == "manager") {
            return permission.use_resource.manager;
        } else if (roleLevel == "coreteam") {
            return permission.use_resource.coreteam;
        } else if (roleLevel == "widerteam") {
            return permission.use_resource.widerteam;
        } else if (roleLevel == "visitor") {
            return permission.use_resource.visitor;
        }
    }
    else if (operation === "manage_resource") {
        if (roleLevel == "manager") {
            return permission.manage_resource.manager;
        } else if (roleLevel == "coreteam") {
            return permission.manage_resource.coreteam;
        } else if (roleLevel == "widerteam") {
            return permission.manage_resource.widerteam;
        } else if (roleLevel == "visitor") {
            return permission.manage_resource.visitor;
        }
    }
    else if (operation === "manage_workspace_type") {
        if (roleLevel == "manager") {
            return permission.manage_workspace_type.manager;
        } else if (roleLevel == "coreteam") {
            return permission.manage_workspace_type.coreteam;
        } else if (roleLevel == "widerteam") {
            return permission.manage_workspace_type.widerteam;
        } else if (roleLevel == "visitor") {
            return permission.manage_workspace_type.visitor;
        }
    }
    else if (operation === "import_tool") {
        if (roleLevel == "manager") {
            return permission.import_tool.manager;
        } else if (roleLevel == "coreteam") {
            return permission.import_tool.coreteam;
        } else if (roleLevel == "widerteam") {
            return permission.import_tool.widerteam;
        } else if (roleLevel == "visitor") {
            return permission.import_tool.visitor;
        }
    }
    else if (operation === "use_tool") {
        if (roleLevel == "manager") {
            return permission.edit_info.manager;
        } else if (roleLevel == "coreteam") {
            return permission.edit_info.coreteam;
        } else if (roleLevel == "widerteam") {
            return permission.edit_info.widerteam;
        } else if (roleLevel == "visitor") {
            return permission.edit_info.visitor;
        }
    }
    else if (operation === "manage_tool") {
        if (roleLevel == "manager") {
            return permission.manage_tool.manager;
        } else if (roleLevel == "coreteam") {
            return permission.manage_tool.coreteam;
        } else if (roleLevel == "widerteam") {
            return permission.manage_tool.widerteam;
        } else if (roleLevel == "visitor") {
            return permission.manage_tool.visitor;
        }
    }
    else if (operation === "manage_child_activity") {
        if (roleLevel == "manager") {
            return permission.manage_child_activity.manager;
        } else if (roleLevel == "coreteam") {
            return permission.manage_child_activity.coreteam;
        } else if (roleLevel == "widerteam") {
            return permission.manage_child_activity.widerteam;
        } else if (roleLevel == "visitor") {
            return permission.manage_child_activity.visitor;
        }
    }
}


export { roleIdentify, getDefault, getMemberByRole, roleCompare, permissionJson2Array, permissionIdentity }
