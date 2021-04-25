/**
 * 提供操作记录的存储、删除、查询方法
 * @ 2021/3/29
 * @ mzy
 */
var xmlDoc = null;
var strDoc = null;

//修改时间格式使其统一
Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S+": this.getMilliseconds(), //毫毛
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(
            RegExp.$1,
            (this.getFullYear() + "").substr(4 - RegExp.$1.length)
        );
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(
                RegExp.$1,
                RegExp.$1.length == 1
                    ? o[k]
                    : ("00" + o[k]).substr(("" + o[k]).length)
            );
    return fmt;
};

// 加载活动文档
function loadActivityDoc() {

    try //Internet Explorer
    {
        xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
        xmlDoc.async = "false";
        xmlDoc.loadXML(strDoc);
    }
    catch (e) {
        try //Firefox, Mozilla, Opera, etc.
        {
            let parser = new DOMParser();
            xmlDoc = parser.parseFromString(strDoc, "text/xml");
        }
        catch (e) { alert(e.message) }
    }
}

//guid
function guid() {
    function S4() {
        return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
    }
    return (S4() + S4() + "-" + S4() + "-4" + S4().substr(0, 3) + "-" + S4() + "-" + S4() + S4() + S4()).toLowerCase();
}

// 创建活动时的活动文档初始化
export function activityDocInit(activityInfo, user) {

    strDoc = `<Activity></Activity>`;
    loadActivityDoc();
    let Activity = xmlDoc.getElementsByTagName("Activity")[0];
    if (Activity == undefined) return;

    // attr
    Activity.setAttribute("id", activityInfo.aid);
    Activity.setAttribute("name", activityInfo.name);
    Activity.setAttribute("description", activityInfo.description);
    Activity.setAttribute("type", activityInfo.type);

    // children
    let Participants = xmlDoc.createElement('Participants');
    let Person = xmlDoc.createElement('Person');
    Person.setAttribute("id", user.userId);
    Person.setAttribute("name", user.name);
    Person.setAttribute("role", "Manager");
    Participants.appendChild(Person);
    Activity.appendChild(Participants);


    if (activityInfo.type === "Activity_Unit") {

        let ResourceCollection = xmlDoc.createElement('ResourceCollection');
        Activity.appendChild(ResourceCollection);

        let ToolBox = xmlDoc.createElement('ToolBox');
        Activity.appendChild(ToolBox);

        // Operation
        let OperationRecords = xmlDoc.createElement('OperationRecords');
        Activity.appendChild(OperationRecords);

        let TaskList = xmlDoc.createElement('TaskList');
        Activity.appendChild(TaskList);

        let TaskDependency = xmlDoc.createElement('TaskDependency');
        Activity.appendChild(TaskDependency);

    } else if (activityInfo.type === "Activity_Group") {

        let ChildActivities = xmlDoc.createElement('ChildActivities');
        Activity.appendChild(ChildActivities);

        let ActivityDependencies = xmlDoc.createElement('ActivityDependencies');
        Activity.appendChild(ActivityDependencies);

        // Operation
        let OperationRecords = xmlDoc.createElement('OperationRecords');
        Activity.appendChild(OperationRecords);
    }

    saveActivityDoc(activityInfo.aid);
}

// 重建活动文档
export function rebuildActivityDoc(activityInfo) {

    strDoc = `<Activity></Activity>`;
    loadActivityDoc();
    let Activity = xmlDoc.getElementsByTagName("Activity")[0];
    if (Activity == undefined) return;

    // attr
    Activity.setAttribute("id", activityInfo.aid);
    Activity.setAttribute("name", activityInfo.name);
    Activity.setAttribute("description", activityInfo.description);
    Activity.setAttribute("type", activityInfo.type);

    // Participants
    let Participants = xmlDoc.createElement('Participants');
    for (var i = 0; i < activityInfo.members.length; i++) {
        let Person = xmlDoc.createElement('Person');
        Person.setAttribute("id", activityInfo.members[i].userId);
        Person.setAttribute("name", activityInfo.members[i].userId);
        Person.setAttribute("role", "");
        Person.setAttribute("state", "in");
        Participants.appendChild(Person);
    }
    Activity.appendChild(Participants);

    if (activityInfo.type === "Activity_Unit") {

        let ResourceCollection = xmlDoc.createElement('ResourceCollection');
        Activity.appendChild(ResourceCollection);

        let ToolBox = xmlDoc.createElement('ToolBox');
        Activity.appendChild(ToolBox);

        // Operation
        let OperationRecords = xmlDoc.createElement('OperationRecords');
        Activity.appendChild(OperationRecords);

        let TaskList = xmlDoc.createElement('TaskList');
        Activity.appendChild(TaskList);

        let TaskDependency = xmlDoc.createElement('TaskDependency');
        Activity.appendChild(TaskDependency);

    } else if (activityInfo.type === "Activity_Group") {

        // Child activity
        let ChildActivities = xmlDoc.createElement('ChildActivities');
        for (var i = 0; i < activityInfo.children.length; i++) {
            let Child = xmlDoc.createElement('Child');
            Child.setAttribute("id", activityInfo.children[i].aid);
            Child.setAttribute("name", activityInfo.children[i].aid);
            Child.setAttribute("creator", "");
            Child.setAttribute("state", "accessible");
            ChildActivities.appendChild(Child);
        }
        Activity.appendChild(ChildActivities);

        var ActivityDependencies = xmlDoc.createElement('ActivityDependencies');
        Activity.appendChild(ActivityDependencies);

        var OperationRecords = xmlDoc.createElement('OperationRecords');
        Activity.appendChild(OperationRecords);
    }

    saveActivityDoc(activityInfo.aid);
}

// inquiry——打开activity时
export function getActivityDoc(aid) {
    let result;
    $.ajax({
        url: "/GeoProblemSolving/activityDoc?aid=" + aid,
        type: "GET",
        async: false,
        success: function (data) {
            result = data;
        },
        error: function (err) {
            throw err;
        }
    });

    if (result.code === 0) {
        strDoc = result.data;
        loadActivityDoc();
        clearTempOperations();
        return "success";
    } else if (result.code === -1) {
        return "empty";
    } else if (result.code === -2) {
        throw new Error(result.msg);
    }
}

export function getResInfo(resId) {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    let resNode = xmlDoc.SelectSingleNode("Activity/ResourceCollection/Resource[id='" + resId + "']"); // wait for test
    if (resNode == null) return null;

    let resource = {}
    resource["id"] = resNode.getAttribute("id");
    resource["name"] = resNode.getAttribute("name");
    resource["type"] = resNode.getAttribute("type");
    resource["format"] = resNode.getAttribute("format");
    resource["description"] = resNode.getAttribute("description");
    resource["provider"] = resNode.getAttribute("provider");
    resource["href"] = resNode.getAttribute("href");

    return resource;
}

export function getMemberInfo(pid) {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    let personNode = xmlDoc.SelectSingleNode("Activity/Participants/Person[id='" + pid + "']"); // wait for test
    if (personNode == null) return null;

    let member = {}
    member["id"] = personNode.getAttribute("id");
    member["name"] = personNode.getAttribute("name");
    member["role"] = personNode.getAttribute("role");

    return member;
}

export function getToolInfo(tid) {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    let toolNode = xmlDoc.SelectSingleNode("Activity/ToolBox/Tool[id='" + tid + "']"); // wait for test
    if (toolNode == null) return null;

    let tool = {}
    tool["id"] = toolNode.getAttribute("id");
    tool["name"] = toolNode.getAttribute("name");
    tool["type"] = toolNode.getAttribute("type");
    tool["function"] = toolNode.getAttribute("function");
    tool["provider"] = toolNode.getAttribute("provider");
    tool["href"] = toolNode.getAttribute("href");

    return tool;
}

export function getOperationInfo(oid) {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    let operationNode = xmlDoc.SelectSingleNode("Activity/OperationRecords/Operation[id='" + oid + "']"); // wait for test
    if (operationNode == null) return null;

    let operation = {};
    operation["id"] = operationNode.getAttribute("id");
    operation["type"] = operationNode.getAttribute("type");

    if (operationNode.getAttribute("type") === "resource") {

        operation["behavior"] = operationNode.getAttribute("behavior");
        operation["resRef"] = operationNode.getAttribute("resRef");
        operation["operator"] = operationNode.getAttribute("operator");
        operation["time"] = operationNode.getAttribute("time");

    } else if (operationNode.getAttribute("type") === "tool") {

        operation["behavior"] = operationNode.getAttribute("behavior");
        operation["toolRef"] = operationNode.getAttribute("toolRef");
        operation["operator"] = operationNode.getAttribute("operator");
        operation["time"] = operationNode.getAttribute("time");

    } else if (operationNode.getAttribute("type") === "communication") {

        operation["toolRef"] = operationNode.getAttribute("toolRef");
        operation["resRef"] = operationNode.getAttribute("resRef");
        operation["time"] = operationNode.getAttribute("time");

        // participants
        let participants = [];
        for (var j = 0; j < operationNode.childNodes.length; j++) {
            let person = operationNode.childNodes[j].getAttribute("id");
            participants.push(person);
        }
        operation["operators"] = participants;

    } else if (operationNode.getAttribute("type") === "geo-analysis") {

        operation["toolRef"] = operationNode.getAttribute("toolRef");
        operation["operator"] = operationNode.getAttribute("operator");
        operation["time"] = operationNode.getAttribute("time");

        // resources
        let resources = {
            inputs: [],
            outputs: [],
            params: []
        };
        let participants = [];
        for (var j = 0; j < operationNode.childNodes.length; j++) {
            let resource = operationNode.childNodes[j];
            if (resource.getAttribute("input") === "input") {
                resources.inputs.push(resource.getAttribute("id"));
            } else if (type === "param") {
                resources.params.push(resource.getAttribute("id"));
            } else if (type === "output") {
                resources.outputs.push(resource.getAttribute("id"));
            } else if (type === "participant") {
                participants.push(resource.getAttribute("id"));
            }
        }
        operation["resesRef"] = resources;
    }

    return operation;
}

export function getTaskList() {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    let taskDoc = xmlDoc.getElementsByTagName("Task");
    if (taskDoc == undefined) return [];

    let tasks = [];
    for (var i = 0; i < taskDoc.length; i++) {
        let taskInfo = {
            taskId: taskDoc[i].getAttribute("id"),
            name: taskDoc[i].getAttribute("name"),
            time: taskDoc[i].getAttribute("timerange"),
            operations: []
        }
        let operations = taskDoc[i].childNodes;
        for (var j = 0; j < operations.length; j++) {
            taskInfo.operations.push(operations[j].getAttribute("id"));
        }
        tasks.push(taskInfo);
    }
    return tasks;
}

export function getTaksDependencies() {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    let dependencyDoc = xmlDoc.getElementsByTagName("Relation");
    if (dependencyDoc == undefined) return [];

    let relations = [];
    for (var i = 0; i < dependencyDoc.length; i++) {
        let relation = {
            id: dependencyDoc[i].getAttribute("id"),
            name: dependencyDoc[i].getAttribute("name"),
            from: "",
            to: ""
        };
        for (var j = 0; j < dependencyDoc[i].childNodes.length; j++) {
            let linkNode = dependencyDoc[i].childNodes[j];
            if (linkNode.tagName == "From") {
                relation.from = linkNode.getAttribute("taskRef");
            } else if (linkNode.tagName == "To") {
                relation.to = linkNode.getAttribute("taskRef");
            }
        }
        relations.push(relation);
    }
    return relations;
}

export function getToollist() {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    let toolsDoc = xmlDoc.getElementsByTagName("Tool");
    if (toolsDoc == undefined) return [];

    let tools = [];
    for (var i = 0; i < toolsDoc.length; i++) {
        if (toolsDoc[i].getAttribute("state") == "accessible") {
            tools.push(toolsDoc[i].getAttribute("id"));
        }
    }
    return tools;
}

export function getReslist() {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    let resDoc = xmlDoc.getElementsByTagName("Resource");
    if (resDoc == undefined) return [];

    let resources = [];
    for (var i = 0; i < resDoc.length; i++) {
        resources.push(resDoc[i].getAttribute("id"));
    }
    return resources;
}

// independent operations(Temporary operations)
export function getTempOperations() {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    let Operations = xmlDoc.getElementsByTagName("Operation");
    if (Operations == undefined) return [];

    let tempOperations = [];
    for (var i = 0; i < Operations.length; i++) {

        let operationNode = Operations[i];
        if (operationNode.getAttribute("task") === "") {

            let operation = {};
            operation["id"] = operationNode.getAttribute("id");
            operation["type"] = operationNode.getAttribute("type");

            if (operationNode.getAttribute("type") === "communication") {

                operation["toolRef"] = operationNode.getAttribute("toolRef");
                operation["resRef"] = operationNode.getAttribute("resRef");
                operation["time"] = operationNode.getAttribute("time");

                // participants
                let participants = [];
                for (var j = 0; j < operationNode.childNodes.length; j++) {
                    let person = operationNode.childNodes[j].getAttribute("id");
                    participants.push(person);
                }
                operation["operators"] = participants;

            } else if (operationNode.getAttribute("type") === "geo-analysis") {

                operation["toolRef"] = operationNode.getAttribute("toolRef");
                operation["operator"] = operationNode.getAttribute("operator");
                operation["time"] = operationNode.getAttribute("time");

                // resources
                let resources = {
                    inputs: [],
                    outputs: [],
                    params: []
                };
                let participants = [];
                for (var j = 0; j < operationNode.childNodes.length; j++) {
                    let resource = operationNode.childNodes[j];
                    if (resource.getAttribute("input") === "input") {
                        resources.inputs.push(resource.getAttribute("id"));
                    } else if (type === "param") {
                        resources.params.push(resource.getAttribute("id"));
                    } else if (type === "output") {
                        resources.outputs.push(resource.getAttribute("id"));
                    } else if (type === "participant") {
                        participants.push(resource.getAttribute("id"));
                    }
                }
                operation["resesRef"] = resources;
                operation["operators"] = participants;
            }
            tempOperations.push(operation);
        }
    }
    return tempOperations;
}

// save
function saveActivityDoc(id) {

    let serializer = new XMLSerializer();
    strDoc = serializer.serializeToString(xmlDoc);
    let jsonData = {
        "aid": id,
        "document": strDoc
    }

    $.ajax({
        url: "/GeoProblemSolving/activityDoc",
        type: "POST",
        dataType: "JSON",
        contentType: "application/json",
        data: JSON.stringify(jsonData),
        success: function (result) {
            if (result.code === 0) {
                return "success";
            } else {
                alert(result.msg);
            }
        },
        error: function (err) {
            throw err;
        }
    });
}

// update
// acitivity——type: type/other
export function activityUpdate(updateType, activityInfo) {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    let Activity = xmlDoc.getElementsByTagName('Activity')[0];
    Activity.setAttribute("name", activityInfo.name);
    Activity.setAttribute("description", activityInfo.description);
    Activity.setAttribute("type", activityInfo.type);

    if (updateType === "type") {
        try {
            // clear
            if (xmlDoc.getElementsByTagName('ChildActivities').length > 0) {

                let node = xmlDoc.getElementsByTagName('ChildActivities')[0];
                xmlDoc.documentElement.removeChild(node);

                node = xmlDoc.getElementsByTagName('ActivityDependencies')[0];
                xmlDoc.documentElement.removeChild(node);

                node = xmlDoc.getElementsByTagName('OperationRecords')[0];
                xmlDoc.documentElement.removeChild(node);

            } else if (xmlDoc.getElementsByTagName('ResourceCollection').length > 0) {

                let node = xmlDoc.getElementsByTagName('ResourceCollection')[0];
                xmlDoc.documentElement.removeChild(node);

                node = xmlDoc.getElementsByTagName('ToolBox')[0];
                xmlDoc.documentElement.removeChild(node);

                node = xmlDoc.getElementsByTagName('OperationRecords')[0];
                xmlDoc.documentElement.removeChild(node);

                node = xmlDoc.getElementsByTagName('TaskList')[0];
                xmlDoc.documentElement.removeChild(node);

                node = xmlDoc.getElementsByTagName('TaskDependency')[0];
                xmlDoc.documentElement.removeChild(node);
            }
            // add
            if (activityInfo.type === "Activity_Unit") {

                let ResourceCollection = xmlDoc.createElement('ResourceCollection');
                Activity.appendChild(ResourceCollection);

                let ToolBox = xmlDoc.createElement('ToolBox');
                Activity.appendChild(ToolBox);

                let OperationRecords = xmlDoc.createElement('OperationRecords');
                Activity.appendChild(OperationRecords);

                let TaskList = xmlDoc.createElement('TaskList');
                Activity.appendChild(TaskList);

                let TaskDependency = xmlDoc.createElement('TaskDependency');
                Activity.appendChild(TaskDependency);

            } else if (activityInfo.type === "Activity_Group") {

                let ChildActivities = xmlDoc.createElement('ChildActivities');
                Activity.appendChild(ChildActivities);

                let ActivityDependencies = xmlDoc.createElement('ActivityDependencies');
                Activity.appendChild(ActivityDependencies);

                let OperationRecords = xmlDoc.createElement('OperationRecords');
                Activity.appendChild(OperationRecords);
            }

        } catch (err) {
            throw err;
        }

    }
    saveActivityDoc(activityInfo.aid);
}

export function taskUpdate(aid, behavior, taskInfo) {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    // TaskList
    if (behavior === "create") {
        let TaskList = xmlDoc.getElementsByTagName('TaskList')[0];
        if (TaskList == undefined) return;

        let Task = xmlDoc.createElement('Task');
        Task.setAttribute("id", taskInfo.taskId);
        Task.setAttribute("name", taskInfo.name);
        Task.setAttribute("purpose", taskInfo.description);
        Task.setAttribute("timerange", taskInfo.startTime.Format("yyyy-MM-dd hh:mm") + " - " + taskInfo.endTime.Format("yyyy-MM-dd hh:mm"));
        Task.setAttribute("state", "available");
        TaskList.appendChild(Task);
    } else if (behavior === "remove") {
        let Task = xmlDoc.getElementById(taskInfo.taskId);
        if (Task !== null) {
            Task.setAttribute("state", "removed");
        }
        // remove operation in this task
        let operations = Task.childNodes;
        for (var i = 0; i < operations.length; i++) {

            let oid = operations[i].getAttribute("id");
            let operationNode = xmlDoc.SelectSingleNode("Activity/OperationRecords/Operation[id='" + oid + "']"); // wait for test
            if (operationNode != null) {
                operationNode.setAttribute("task", "");
            }
        }

    } else if (behavior === "update") {
        let Task = xmlDoc.getElementById(taskInfo.taskId);
        Task.setAttribute("name", taskInfo.name);
        Task.setAttribute("purpose", taskInfo.description);
        Task.setAttribute("timerange", taskInfo.startTime.Format("yyyy-MM-dd hh:mm") + " - " + taskInfo.endTime.Format("yyyy-MM-dd hh:mm"));
    }

    saveActivityDoc(aid);
}


export function taskDependencyRecord(aid, behavior, relationId, lasts, nexts) {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    // TaskDependency
    if (behavior === "link") {
        let TaskDependency = xmlDoc.getElementsByTagName("TaskDependency")[0];
        if (TaskDependency == undefined) return;

        let Relation = xmlDoc.createElement('Relation');
        Relation.setAttribute("id", relationId);
        Relation.setAttribute("name", "Task Dependency");
        Relation.setAttribute("state", "used");
        for (var i = 0; i < lasts.length; i++) {
            let From = xmlDoc.createElement('From');
            From.setAttribute("taskRef", lasts[i]);
            Relation.appendChild(From);
        }
        for (var i = 0; i < nexts.length; i++) {
            let To = xmlDoc.createElement('To');
            To.setAttribute("taskRef", nexts[i]);
            Relation.appendChild(To);
        }
        TaskDependency.appendChild(Relation);
    } else if (behavior === "break") {
        let Relation = xmlDoc.getElementById(protocalId);
        if (Relation !== null) {
            Relation.setAttribute("state", "removed");
        }
    }

    saveActivityDoc(aid);
}

export function participantUpdate(aid, behavior, userId, name, role) {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    // Participants
    if (behavior === "invite") {
        let Participants = xmlDoc.getElementsByTagName('Participants')[0];
        if (Participants == undefined) return;

        let Person = xmlDoc.createElement('Person');
        Person.setAttribute("id", userId);
        Person.setAttribute("name", name);
        Person.setAttribute("role", role);
        Person.setAttribute("state", "in");
        Participants.appendChild(Person);
    } else if (behavior === "remove") {
        let Person = xmlDoc.getElementById(userId);
        if (Person !== null) {
            Person.setAttribute("state", "out");
        }
    } else if (behavior === "role") {
        let Person = xmlDoc.getElementById(userId);
        if (Person !== null) {
            Person.setAttribute("role", role);
        }
    }

    saveActivityDoc(aid);
}

// operation
export function resOperationRecord(aid, taskId, behavior, userId, resInfo) {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    //ResourceCollection
    if (behavior === "upload") {
        let ResourceCollection = xmlDoc.getElementsByTagName("ResourceCollection")[0];
        if (ResourceCollection == undefined) return;

        let Resource = xmlDoc.createElement('Resource');
        Resource.setAttribute("id", resInfo.uid);
        Resource.setAttribute("name", resInfo.name);
        Resource.setAttribute("type", resInfo.type);
        Resource.setAttribute("format", resInfo.suffix);
        Resource.setAttribute("description", resInfo.description);
        Resource.setAttribute("provider", resInfo.uploaderId);
        Resource.setAttribute("href", resInfo.address);
        Resource.setAttribute("state", "accessible");
        ResourceCollection.appendChild(Resource);
    } else if (behavior === "remove") {
        let Resource = xmlDoc.getElementById(resInfo.uid);
        if (Resource !== null) {
            Resource.setAttribute("state", "removed");
        }
    }

    //OperationRecords
    let operationId = guid();
    let OperationRecords = xmlDoc.getElementsByTagName("OperationRecords")[0];
    if (OperationRecords == undefined) return;

    let Operation = xmlDoc.createElement('Operation');
    Operation.setAttribute("id", operationId);
    Operation.setAttribute("type", "resource");
    Operation.setAttribute("behavior", behavior);
    Operation.setAttribute("resRef", resInfo.uid);
    Operation.setAttribute("operator", userId);
    Operation.setAttribute("task", taskId);
    Operation.setAttribute("time", new Date().Format("yyyy-MM-dd HH:mm:ss"));
    OperationRecords.appendChild(Operation);

    // TaskList
    let Task = xmlDoc.getElementById(taskId);
    if (Task !== null) {
        let OperationRef = xmlDoc.createElement('OperationRef');
        Operation.setAttribute("id", operationId);
        Task.appendChild(OperationRef);
    }

    saveActivityDoc(aid);
}

export function toolOperationRecord(aid, taskId, behavior, userId, toolInfo) {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    //ToolBox
    if (behavior === "add") {
        let ToolBox = xmlDoc.getElementsByTagName("ToolBox")[0];
        if (ToolBox == undefined) return;

        let Tool = xmlDoc.getElementById(toolInfo.tid);
        if (Tool != undefined && Tool.getAttribute("state") === "accessible") return;

        Tool = xmlDoc.createElement('Tool');
        Tool.setAttribute("id", toolInfo.tid);
        Tool.setAttribute("name", toolInfo.toolName);
        Tool.setAttribute("type", toolInfo.isToolset ? "toolset" : "tool");
        Tool.setAttribute("function", toolInfo.description);
        Tool.setAttribute("provider", toolInfo.provider);
        Tool.setAttribute("href", toolInfo.toolUrl);
        Tool.setAttribute("state", "accessible");
        ToolBox.appendChild(Tool);
    } else if (behavior === "remove") {
        let Tool = xmlDoc.getElementById(toolInfo.tid);
        if (Tool !== null) {
            Tool.setAttribute("state", "removed");
        }
    }

    //OperationRecords
    let operationId = guid();
    let OperationRecords = xmlDoc.getElementsByTagName("OperationRecords")[0];
    if (OperationRecords == undefined) return;

    let Operation = xmlDoc.createElement('Operation');
    Operation.setAttribute("id", operationId);
    Operation.setAttribute("type", "tool");
    Operation.setAttribute("behavior", behavior);
    Operation.setAttribute("toolRef", toolInfo.tid);
    Operation.setAttribute("operator", userId);
    Operation.setAttribute("task", taskId);
    Operation.setAttribute("time", new Date().Format("yyyy-MM-dd HH:mm:ss"));
    OperationRecords.appendChild(Operation);

    // TaskList
    if (taskId != "") {
        let Task = xmlDoc.getElementById(taskId);
        if (Task !== null) {
            let OperationRef = xmlDoc.createElement('OperationRef');
            Operation.setAttribute("id", operationId);
            Task.appendChild(OperationRef);
        }
    }

    saveActivityDoc(aid);
}

export function communicationRecord(aid, taskId, toolId, resId, onlineMembers) {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    //OperationRecords
    let operationId = guid();
    let OperationRecords = xmlDoc.getElementsByTagName("OperationRecords")[0];
    if (OperationRecords == undefined) return;

    let Operation = xmlDoc.createElement('Operation');
    Operation.setAttribute("id", operationId);
    Operation.setAttribute("type", "communication");
    Operation.setAttribute("toolRef", toolId);
    Operation.setAttribute("resRef", resId);
    Operation.setAttribute("task", taskId);
    Operation.setAttribute("time", new Date().Format("yyyy-MM-dd HH:mm:ss"));
    for (var i = 0; i < onlineMembers.length; i++) {
        let PersonRef = xmlDoc.createElement('PersonRef');
        PersonRef.setAttribute("id", onlineMembers[i]);
        Operation.appendChild(PersonRef);
    }
    OperationRecords.appendChild(Operation);

    // TaskList
    let Task = xmlDoc.getElementById(taskId);
    if (Task !== null) {
        let OperationRef = xmlDoc.createElement('OperationRef');
        Operation.setAttribute("id", operationId);
        Task.appendChild(OperationRef);
    }

    saveActivityDoc(aid);
}

export function analysisRecord(aid, taskId, toolId, userId, inputs, outputs, params, participants) {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    //OperationRecords
    let operationId = guid();
    let OperationRecords = xmlDoc.getElementsByTagName("OperationRecords")[0];
    if (OperationRecords == undefined) return;

    let Operation = xmlDoc.createElement('Operation');
    Operation.setAttribute("id", operationId);
    Operation.setAttribute("type", "geo-analysis");
    Operation.setAttribute("toolRef", toolId);
    Operation.setAttribute("operator", userId);
    Operation.setAttribute("task", taskId);
    Operation.setAttribute("time", new Date().Format("yyyy-MM-dd HH:mm:ss"));
    for (var i = 0; i < inputs.length; i++) {
        let ResRef = xmlDoc.createElement('ResRef');
        ResRef.setAttribute("id", inputs[i]);
        ResRef.setAttribute("type", "input");
        Operation.appendChild(ResRef);
    }
    for (var i = 0; i < outputs.length; i++) {
        let ResRef = xmlDoc.createElement('ResRef');
        ResRef.setAttribute("id", outputs[i]);
        ResRef.setAttribute("type", "output");
        Operation.appendChild(ResRef);
    }
    for (var i = 0; i < params.length; i++) {
        let ResRef = xmlDoc.createElement('ResRef');
        ResRef.setAttribute("id", params[i]);
        ResRef.setAttribute("type", "param");
        Operation.appendChild(ResRef);
    }
    for (var i = 0; i < participants.length; i++) {
        let PersonRef = xmlDoc.createElement('PersonRef');
        PersonRef.setAttribute("id", participants[i]);
        PersonRef.setAttribute("type", "participant");
        Operation.appendChild(PersonRef);
    }
    OperationRecords.appendChild(Operation);

    // TaskList
    let Task = xmlDoc.getElementById(taskId);
    if (Task !== null) {
        let OperationRef = xmlDoc.createElement('OperationRef');
        Operation.setAttribute("id", operationId);
        Task.appendChild(OperationRef);
    }

    saveActivityDoc(aid);
}

// Child Activity
export function activityRecord(behavior, userId, childInfo) {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    // ChildActivities
    if (behavior === "create") {
        let ChildActivities = xmlDoc.getElementsByTagName("ChildActivities")[0];
        if (ChildActivities == undefined) return;

        let Child = xmlDoc.createElement('Child');
        Child.setAttribute("id", childInfo.aid);
        Child.setAttribute("name", childInfo.name);
        Child.setAttribute("creator", childInfo.creator);
        Child.setAttribute("state", "accessible");
        ChildActivities.appendChild(Child);
    } else if (behavior === "remove") {
        let Child = xmlDoc.getElementById(childInfo.aid);
        if (Child !== null) {
            Child.setAttribute("state", "removed");
        }
    }

    //OperationRecords
    let operationId = guid();
    let OperationRecords = xmlDoc.getElementsByTagName("OperationRecords")[0];
    if (OperationRecords == undefined) return;

    let Operation = xmlDoc.createElement('Operation');
    Operation.setAttribute("id", operationId);
    Operation.setAttribute("type", "activity");
    Operation.setAttribute("behavior", behavior);
    Operation.setAttribute("operator", userId);
    Operation.setAttribute("time", new Date().Format("yyyy-MM-dd HH:mm:ss"));
    OperationRecords.appendChild(Operation);


    saveActivityDoc(childInfo.parent);
}

export function processRecord(aid, behavior, userId, last, next) {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    // ActivityDependencies
    let relationId = guid();
    if (behavior === "link") {
        let ActivityDependencies = xmlDoc.getElementsByTagName("ActivityDependencies")[0];
        if (ActivityDependencies == undefined) return;

        let Relation = xmlDoc.createElement('Relation');
        Relation.setAttribute("id", relationId);
        Relation.setAttribute("name", "ActivityDependency");
        Relation.setAttribute("state", "used");
        let From = xmlDoc.createElement('From');
        From.setAttribute("childRef", last);
        Relation.appendChild(From);
        let To = xmlDoc.createElement('To');
        To.setAttribute("childRef", next);
        Relation.appendChild(To);
        ActivityDependencies.appendChild(Relation);
    } else if (behavior === "break") {
        let Relation = xmlDoc.getElementById(protocalId);
        if (Relation !== null) {
            Relation.setAttribute("state", "removed");
        }
    }

    //OperationRecords
    let operationId = guid();
    let OperationRecords = xmlDoc.getElementsByTagName("OperationRecords")[0];
    if (OperationRecords == undefined) return;

    let Operation = xmlDoc.createElement('Operation');
    Operation.setAttribute("id", operationId);
    Operation.setAttribute("type", "process");
    Operation.setAttribute("behavior", behavior);
    Operation.setAttribute("dependency", relationId);
    Operation.setAttribute("operator", userId);
    Operation.setAttribute("time", new Date().Format("yyyy-MM-dd HH:mm:ss"));
    OperationRecords.appendChild(Operation);


    saveActivityDoc(aid);
}

// delete
export function deleteActivityDoc(aid) {
    $.ajax({
        url: "/GeoProblemSolving/activityDoc?aid=" + aid,
        type: "DELETE",
        async: false,
        success: function (result) {
            if (result.code == 0) {
                return "success";
            } else {
                alert(result.msg);
            }
        },
        error: function (err) {
            throw err;
        }
    });
}

// remove independent operations(Temporary operations) > 48h
function clearTempOperations() {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    let Operations = xmlDoc.getElementsByTagName("Operation");
    if (Operations == undefined) return [];

    for (var i = 0; i < Operations.length; i++) {

        let operationNode = Operations[i];
        if (operationNode.getAttribute("task") === "") {

            let time = new Date(operationNode.getAttribute("time"));
            let current = new Date();
            let threshold = 1000 * 60 * 60 * 48;
            if(current - time > threshold) {
                xmlDoc.documentElement.removeChild(operationNode);
            }
        }
    }
}


