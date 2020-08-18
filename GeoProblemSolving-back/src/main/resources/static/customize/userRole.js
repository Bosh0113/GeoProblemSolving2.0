/**
 * User roles and permissions
 */

var permission = {};
var defaultPermission = {
    observe: {
        "leader": null,
        "coreteam": null,
        "widerteam": null,
        "visitor": "No"
    },
    auto_join: {
        "leader": null,
        "coreteam": null,
        "widerteam": null,
        "visitor": "No"
    },
    edit_info: {
        "leader": true,
        "coreteam": true,
        "widerteam": false,
        "visitor": null
    },
    invite_member: {
        "leader": true,
        "coreteam": true,
        "widerteam": true,
        "visitor": null
    },
    manage_member: {
        "leader": true,
        "coreteam": false,
        "widerteam": false,
        "visitor": null
    },
    create_task: {
        "leader": true,
        "coreteam": false,
        "widerteam": false,
        "visitor": null
    },
    manage_task: {
        "leader": true,
        "coreteam": false,
        "widerteam": false,
        "visitor": null
    },
    upload_resource: {
        "leader": true,
        "coreteam": true,
        "widerteam": true,
        "visitor": null
    },
    use_resource: {
        "leader": true,
        "coreteam": true,
        "widerteam": true,
        "visitor": null
    },
    manage_resource: {
        "leader": true,
        "coreteam": false,
        "widerteam": false,
        "visitor": null
    },
    manage_workspace_type: {
        "leader": true,
        "coreteam": false,
        "widerteam": false,
        "visitor": null
    },
    import_tool: {
        "leader": true,
        "coreteam": true,
        "widerteam": false,
        "visitor": null
    },
    use_tool: {
        "leader": true,
        "coreteam": true,
        "widerteam": true,
        "visitor": null
    },
    manage_tool: {
        "leader": true,
        "coreteam": false,
        "widerteam": false,
        "visitor": null
    },
    manage_child_activity: {
        "leader": true,
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
            roleLevel = "leader";
        };
        case "researcher":
        case "expert":
        case "core-member": {
            roleLevel = "coreteam";
        };
        case "decision-maker":
        case "stakeholder":
        case "consultant": {
            roleLevel = "widerteam";
        };
        case "visitor": {
            roleLevel = "visitor";
        }
    }
    return roleLevel;
}

// Get default permission
function getDefault() {
    permission = defaultPermission;
    return permission;
}

// Set current permission
function setPermission(currentPermission) {
    permission = currentPermission;
}

// Permission: from Json to Array
function permissionJson2Array() {
    if (permission == {})
        permission = defaultPermission;

    var permissionArray = [];
    if (permission.observe != undefined) {
        var row = {
            operation: "Learn more about the activity",
            leader: permission.observe.leader,
            coreteam: permission.observe.coreteam,
            widerteam: permission.observe.widerteam,
            visitor: permission.observe.visitor
        };
        permissionList.push(row);
    }
    if (
        permission.auto_join != undefined &&
        privacy == "Public"
    ) {
        var row = {
            operation: "Join the activity automatically",
            leader: permission.auto_join.leader,
            coreteam: permission.auto_join.coreteam,
            widerteam: permission.auto_join.widerteam,
            visitor: permission.auto_join.visitor
        };
        permissionList.push(row);
    }
    if (permission.edit_info != undefined) {
        var row = {
            operation: "Edit the information of the activity",
            leader: permission.edit_info.leader,
            coreteam: permission.edit_info.coreteam,
            widerteam: permission.edit_info.widerteam,
            visitor: permission.edit_info.visitor
        };
        permissionList.push(row);
    }
    if (permission.invite_member != undefined) {
        var row = {
            operation: "Invite someone to join the activity",
            leader: permission.invite_member.leader,
            coreteam: permission.invite_member.coreteam,
            widerteam: permission.invite_member.widerteam,
            visitor: permission.invite_member.visitor
        };
        permissionList.push(row);
    }
    if (permission.manage_member != undefined) {
        var row = {
            operation: "manage members",
            leader: permission.manage_member.leader,
            coreteam: permission.manage_member.coreteam,
            widerteam: permission.manage_member.widerteam,
            visitor: permission.manage_member.visitor
        };
        permissionList.push(row);
    }
    if (permission.create_task != undefined) {
        var row = {
            operation: "Create activity tasks",
            leader: permission.create_task.leader,
            coreteam: permission.create_task.coreteam,
            widerteam: permission.create_task.widerteam,
            visitor: permission.create_task.visitor
        };
        permissionList.push(row);
    }
    if (permission.manage_task != undefined) {
        var row = {
            operation: "Manage activity tasks",
            leader: permission.manage_task.leader,
            coreteam: permission.manage_task.coreteam,
            widerteam: permission.manage_task.widerteam,
            visitor: permission.manage_task.visitor
        };
        permissionList.push(row);
    }
    if (permission.upload_resource != undefined) {
        var row = {
            operation: "Upload resources",
            leader: permission.upload_resource.leader,
            coreteam: permission.upload_resource.coreteam,
            widerteam: permission.upload_resource.widerteam,
            visitor: permission.upload_resource.visitor
        };
        permissionList.push(row);
    }
    if (permission.use_resource != undefined) {
        var row = {
            operation: "Use resources",
            leader: permission.use_resource.leader,
            coreteam: permission.use_resource.coreteam,
            widerteam: permission.use_resource.widerteam,
            visitor: permission.use_resource.visitor
        };
        permissionList.push(row);
    }
    if (permission.manage_resource != undefined) {
        var row = {
            operation: "Manage resources",
            leader: permission.manage_resource.leader,
            coreteam: permission.manage_resource.coreteam,
            widerteam: permission.manage_resource.widerteam,
            visitor: permission.manage_resource.visitor
        };
        permissionList.push(row);
    }
    if (permission.manage_workspace_type != undefined) {
        var row = {
            operation: "Switch the type of workspace",
            leader: permission.manage_workspace_type.leader,
            coreteam: permission.manage_workspace_type.coreteam,
            widerteam: permission.manage_workspace_type.widerteam,
            visitor: permission.manage_workspace_type.visitor
        };
        permissionList.push(row);
    }
    if (permission.import_tool != undefined) {
        var row = {
            operation: "Import tools or toolsets",
            leader: permission.import_tool.leader,
            coreteam: permission.import_tool.coreteam,
            widerteam: permission.import_tool.widerteam,
            visitor: permission.import_tool.visitor
        };
        permissionList.push(row);
    }
    if (permission.use_tool != undefined) {
        var row = {
            operation: "Use tools or toolsets",
            leader: permission.use_tool.leader,
            coreteam: permission.use_tool.coreteam,
            widerteam: permission.use_tool.widerteam,
            visitor: permission.use_tool.visitor
        };
        permissionList.push(row);
    }
    if (permission.manage_tool != undefined) {
        var row = {
            operation: "Manage tools or toolsets",
            leader: permission.manage_tool.leader,
            coreteam: permission.manage_tool.coreteam,
            widerteam: permission.manage_tool.widerteam,
            visitor: permission.manage_tool.visitor
        };
        permissionList.push(row);
    }
    if (permission.manage_child_activity != undefined) {
        var row = {
            operation: "Manage child activity",
            leader: permission.manage_child_activity.leader,
            coreteam: permission.manage_child_activity.coreteam,
            widerteam: permission.manage_child_activity.widerteam,
            visitor: permission.manage_child_activity.visitor
        };
        permissionList.push(row);
    }
    return permissionArray;
}

function permissionIdentity(currentPermission, role, operation) {

    if (currentPermission == undefined || currentPermission == {}) {
        return false;
    }
    else {
        permission = currentPermission;
    }

    var roleLevel = roleLevelIdentify(role);

    if (operation === "edit_info") {
        if (roleLevel == "leader") {
            return permission.edit_info.leader;
        } else if (roleLevel == "coreteam") {
            return permission.edit_info.coreteam;
        } else if (roleLevel == "widerteam") {
            return permission.edit_info.widerteam;
        } else if (roleLevel == "visitor") {
            return permission.edit_info.visitor;
        }
    }
    else if (operation === "invite_member") {
        if (roleLevel == "leader") {
            return permission.invite_member.leader;
        } else if (roleLevel == "coreteam") {
            return permission.invite_member.coreteam;
        } else if (roleLevel == "widerteam") {
            return permission.invite_member.widerteam;
        } else if (roleLevel == "visitor") {
            return permission.invite_member.visitor;
        }
    }
    else if (operation === "manage_member") {
        if (roleLevel == "leader") {
            return permission.manage_member.leader;
        } else if (roleLevel == "coreteam") {
            return permission.manage_member.coreteam;
        } else if (roleLevel == "widerteam") {
            return permission.manage_member.widerteam;
        } else if (roleLevel == "visitor") {
            return permission.manage_member.visitor;
        }
    }
    else if (operation === "create_task") {
        if (roleLevel == "leader") {
            return permission.create_task.leader;
        } else if (roleLevel == "coreteam") {
            return permission.create_task.coreteam;
        } else if (roleLevel == "widerteam") {
            return permission.create_task.widerteam;
        } else if (roleLevel == "visitor") {
            return permission.create_task.visitor;
        }
    }
    else if (operation === "manage_task") {
        if (roleLevel == "leader") {
            return permission.manage_task.leader;
        } else if (roleLevel == "coreteam") {
            return permission.manage_task.coreteam;
        } else if (roleLevel == "widerteam") {
            return permission.manage_task.widerteam;
        } else if (roleLevel == "visitor") {
            return permission.manage_task.visitor;
        }
    }
    else if (operation === "upload_resource") {
        if (roleLevel == "leader") {
            return permission.upload_resource.leader;
        } else if (roleLevel == "coreteam") {
            return permission.upload_resource.coreteam;
        } else if (roleLevel == "widerteam") {
            return permission.upload_resource.widerteam;
        } else if (roleLevel == "visitor") {
            return permission.upload_resource.visitor;
        }
    }
    else if (operation === "use_resource") {
        if (roleLevel == "leader") {
            return permission.use_resource.leader;
        } else if (roleLevel == "coreteam") {
            return permission.use_resource.coreteam;
        } else if (roleLevel == "widerteam") {
            return permission.use_resource.widerteam;
        } else if (roleLevel == "visitor") {
            return permission.use_resource.visitor;
        }
    }
    else if (operation === "manage_resource") {
        if (roleLevel == "leader") {
            return permission.manage_resource.leader;
        } else if (roleLevel == "coreteam") {
            return permission.manage_resource.coreteam;
        } else if (roleLevel == "widerteam") {
            return permission.manage_resource.widerteam;
        } else if (roleLevel == "visitor") {
            return permission.manage_resource.visitor;
        }
    }
    else if (operation === "manage_workspace_type") {
        if (roleLevel == "leader") {
            return permission.manage_workspace_type.leader;
        } else if (roleLevel == "coreteam") {
            return permission.manage_workspace_type.coreteam;
        } else if (roleLevel == "widerteam") {
            return permission.manage_workspace_type.widerteam;
        } else if (roleLevel == "visitor") {
            return permission.manage_workspace_type.visitor;
        }
    }
    else if (operation === "import_tool") {
        if (roleLevel == "leader") {
            return permission.import_tool.leader;
        } else if (roleLevel == "coreteam") {
            return permission.import_tool.coreteam;
        } else if (roleLevel == "widerteam") {
            return permission.import_tool.widerteam;
        } else if (roleLevel == "visitor") {
            return permission.import_tool.visitor;
        }
    }
    else if (operation === "use_tool") {
        if (roleLevel == "leader") {
            return permission.edit_info.leader;
        } else if (roleLevel == "coreteam") {
            return permission.edit_info.coreteam;
        } else if (roleLevel == "widerteam") {
            return permission.edit_info.widerteam;
        } else if (roleLevel == "visitor") {
            return permission.edit_info.visitor;
        }
    }
    else if (operation === "manage_tool") {
        if (roleLevel == "leader") {
            return permission.manage_tool.leader;
        } else if (roleLevel == "coreteam") {
            return permission.manage_tool.coreteam;
        } else if (roleLevel == "widerteam") {
            return permission.manage_tool.widerteam;
        } else if (roleLevel == "visitor") {
            return permission.manage_tool.visitor;
        }
    }
    else if (operation === "manage_child_activity") {
        if (roleLevel == "leader") {
            return permission.manage_child_activity.leader;
        } else if (roleLevel == "coreteam") {
            return permission.manage_child_activity.coreteam;
        } else if (roleLevel == "widerteam") {
            return permission.manage_child_activity.widerteam;
        } else if (roleLevel == "visitor") {
            return permission.manage_child_activity.visitor;
        }
    }
}


export { roleIdentify, getDefault, setPermission, permissionJson2Array, permissionIdentity }
