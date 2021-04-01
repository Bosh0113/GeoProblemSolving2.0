/**
 * 提供操作记录的存储、删除、查询方法
 * @ 2021/3/29
 * @ mzy
 */
var xmlDoc = null;
var strDoc = null;


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
function activityDocInit(activityInfo, user) {

    if (activityInfo.type === "Activity_Default") {
        return;
    }

    strDoc = `<Activity></Activity>`;
    loadActivityDoc();
    let Activity = xmlDoc.getElementsByTagName("Activity")[0];

    // attr
    Activity.set("id", activityInfo.aid);
    Activity.set("name", activityInfo.name);
    Activity.set("description", activityInfo.description);
    Activity.set("type", activityInfo.type);

    // children
    let Participants = xmlDoc.createElement('Participants');
    let Person = xmlDoc.createElement('Person');
    Person.set("id", user.userId);
    Person.set("name", user.name);
    Person.set("role", "Manager");
    Participants.appendChild(Person);
    Activity.appendChild(Participants);

    let ResourceCollection = xmlDoc.createElement('ResourceCollection');
    Activity.appendChild(ResourceCollection);

    let OperationRecords = xmlDoc.createElement('OperationRecords');
    Activity.appendChild(OperationRecords);

    if (activityInfo.type === "Activity_Unit") {

        let ToolBox = xmlDoc.createElement('ToolBox');
        Activity.appendChild(ToolBox);

        let TaskList = xmlDoc.createElement('TaskList');
        Activity.appendChild(TaskList);

        let TaskDependency = xmlDoc.createElement('TaskDependency');
        Activity.appendChild(TaskDependency);

    }

    saveActivityDoc(activityInfo.aid);
}

// 活动类型改变时
function rebuildActivityDoc(activityInfo, participants, resources, tools) {
    if (activityInfo.type === "Activity_Default") {
        return;
    }

    strDoc = `<Activity></Activity>`;
    loadActivityDoc();
    let Activity = xmlDoc.getElementsByTagName("Activity")[0];

    // attr
    Activity.set("id", activityInfo.aid);
    Activity.set("name", activityInfo.name);
    Activity.set("description", activityInfo.description);
    Activity.set("type", activityInfo.type);

    // children
    let Participants = xmlDoc.createElement('Participants');
    for (var i = 0; i < participants.length; i++) {
        let Person = xmlDoc.createElement('Person');
        Person.set("id", participants[i].userId);
        Person.set("name", participants[i].name);
        Person.set("role", participants[i].role);
        Person.set("state", "in");
        Participants.appendChild(Person);
    }
    Activity.appendChild(Participants);

    let ResourceCollection = xmlDoc.createElement('ResourceCollection');
    for (var i = 0; i < resources.length; i++) {
        let Resource = xmlDoc.createElement('Resource');
        Resource.set("id", resources[i].uid);
        Resource.set("name", resources[i].name);
        Resource.set("type", resources[i].type);
        Resource.set("format", resources[i].suffix);
        Resource.set("description", resources[i].description);
        Resource.set("provider", resources[i].uploaderId);
        Resource.set("href", resources[i].address);
        Resource.set("state", "accessible");
        ResourceCollection.appendChild(Resource);
    }
    Activity.appendChild(ResourceCollection);

    var OperationRecords = xmlDoc.createElement('OperationRecords');
    Activity.appendChild(OperationRecords);

    if (activityInfo.type === "Activity_Unit") {

        let ToolBox = xmlDoc.createElement('ToolBox');
        for (var i = 0; i < tools.length; i++) {
            let Tool = xmlDoc.createElement('Tool');
            Tool.set("id", tools[i].tid);
            Tool.set("name", tools[i].toolName);
            Tool.set("function", tools[i].description);
            Tool.set("provider", tools[i].provider);
            Tool.set("href", tools[i].toolUrl);
            Tool.set("state", "accessible");
            ToolBox.appendChild(Tool);
        }
        Activity.appendChild(ToolBox);

        var TaskList = xmlDoc.createElement('TaskList');
        Activity.appendChild(TaskList);

        var TaskDependency = xmlDoc.createElement('TaskDependency');
        Activity.appendChild(TaskDependency);
    }

    saveActivityDoc(activityInfo.aid);
}

// inquiry——打开activity时
function getActivityDoc(aid) {
    $.ajax({
        url: "/GeoProblemSolving/activityDoc?aid=" + aid,
        type: "GET",
        async: false,
        success: function (result) {
            if (result.code === 0) {
                strDoc = result.data.document;
                return "success";
            } else if (result.code === -1) {
                return "empty";
            } else if (result.code === -2) {
                throw new Error(result.msg);
            }
        },
        error: function (err) {
            throw err;
        }
    });
}

function getTaskRecords(taskId) {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    return xmlDoc.getElementById(taskId);    
}

// save
function saveActivityDoc(aid) {

    let serializer = new XMLSerializer();
    strDoc = serializer.serializeToString(xmlDoc);
    data = {
        aid: aid,
        document: strDoc
    }

    $.ajax({
        url: "/GeoProblemSolving/activityDoc",
        type: "POST",
        data: data,
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

// update
function taskUpdate(behavior, taskInfo) {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    // TaskList
    if (behavior === "create") {
        let TaskList = xmlDoc.getElementsByTagName('TaskList')[0];
        let Task = xmlDoc.createElement('Task');
        Task.set("id", taskInfo.taskId)
        Task.set("name", taskInfo.name)
        Task.set("purpose", taskInfo.description)
        Task.set("purpose", taskInfo.startTime.Format() + " - " + taskInfo.endTime.Format());
        TaskList.appendChild(Task);
    } else if (behavior === "remove") {
        let Task = xmlDoc.getElementById(taskInfo.taskId);
        if (Task !== null) {
            Task.set("state", "removed");
        }
    }
}


function taskDependencyRecord(behavior, relationId, lasts, nexts) {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    // ProtocalList
    if (behavior === "link") {
        let TaskDependency = xmlDoc.getElementsByTagName("ChildActivities")[0];
        let Relation = xmlDoc.createElement('Relation');
        Relation.set("id", relationId);
        Relation.set("name", "Task Dependency");
        Relation.set("state", "used");
        for (var i = 0; i < lasts.length; i++) {
            let From = xmlDoc.createElement('From');
            From.set("taskRef", lasts[i]);
            Relation.appendChild(From);
        }
        for (var i = 0; i < nexts.length; i++) {
            let To = xmlDoc.createElement('To');
            To.set("taskRef", nexts[i]);
            Relation.appendChild(To);
        }
        TaskDependency.appendChild(Relation);
    } else if (behavior === "break") {
        let Relation = xmlDoc.getElementById(protocalId);
        if (Relation !== null) {
            Relation.set("state", "removed");
        }
    }
}


// operation
function resOperationRecord(taskId, behavior, userId, resInfo) {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    //ResourceCollection
    if (behavior === "upload") {
        let ResourceCollection = xmlDoc.getElementsByTagName("ResourceCollection")[0];
        let Resource = xmlDoc.createElement('Resource');
        Resource.set("id", resInfo.uid);
        Resource.set("name", resInfo.name);
        Resource.set("type", resInfo.type);
        Resource.set("format", resInfo.suffix);
        Resource.set("description", resInfo.description);
        Resource.set("provider", resInfo.uploaderId);
        Resource.set("href", resInfo.address);
        Resource.set("state", "accessible");
        ResourceCollection.appendChild(Resource);
    } else if (behavior === "remove") {
        let Resource = xmlDoc.getElementById(resInfo.uid);
        if (Resource !== null) {
            Resource.set("state", "removed");
        }
    }

    //OperationRecords
    let operationId = guid();
    let OperationRecords = xmlDoc.getElementsByTagName("OperationRecords")[0];
    let Operation = xmlDoc.createElement('Operation');
    Operation.set("id", operationId);
    Operation.set("type", "resource");
    Operation.set("behavior", behavior);
    Operation.set("resRef", resInfo.uid);
    Operation.set("operator", userId);
    Operation.set("time", new Date().Format("yyyy-MM-dd HH:mm:ss"));
    OperationRecords.appendChild(Operation);

    // TaskList
    let Task = xmlDoc.getElementById(taskId);
    if (Task !== null) {
        let OperationRef = xmlDoc.createElement('OperationRef');
        Operation.set("id", operationId);
        Task.appendChild(OperationRef);
    }
}

function toolOperationRecord(taskId, behavior, userId, toolInfo) {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    //ToolBox
    if (behavior === "share") {
        let ToolBox = xmlDoc.getElementsByTagName("ToolBox")[0];
        let Tool = xmlDoc.createElement('Tool');
        Tool.set("id", toolInfo.tid);
        Tool.set("name", toolInfo.toolName);
        Tool.set("function", toolInfo.description);
        Tool.set("provider", toolInfo.provider);
        Tool.set("href", toolInfo.toolUrl);
        Tool.set("state", "accessible");
        ToolBox.appendChild(Tool);
    } else if (behavior === "remove") {
        let Tool = xmlDoc.getElementById(toolInfo.tid);
        if (Tool !== null) {
            Tool.set("state", "removed");
        }
    }

    //OperationRecords
    let operationId = guid();
    let OperationRecords = xmlDoc.getElementsByTagName("OperationRecords")[0];
    let Operation = xmlDoc.createElement('Operation');
    Operation.set("id", operationId);
    Operation.set("type", "tool");
    Operation.set("behavior", behavior);
    Operation.set("toolRef", toolInfo.tid);
    Operation.set("operator", userId);
    Operation.set("time", new Date().Format("yyyy-MM-dd HH:mm:ss"));
    OperationRecords.appendChild(Operation);

    // TaskList
    let Task = xmlDoc.getElementById(taskId);
    if (Task !== null) {
        let OperationRef = xmlDoc.createElement('OperationRef');
        Operation.set("id", operationId);
        Task.appendChild(OperationRef);
    }
}

function communicationRecord(taskId, toolId, resId, onlineMembers) {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    //OperationRecords
    let operationId = guid();
    let OperationRecords = xmlDoc.getElementsByTagName("OperationRecords")[0];
    let Operation = xmlDoc.createElement('Operation');
    Operation.set("id", operationId);
    Operation.set("type", "communication");
    Operation.set("toolRef", toolId);
    Operation.set("resRef", resId);
    Operation.set("time", new Date().Format("yyyy-MM-dd HH:mm:ss"));
    for (var i = 0; i < onlineMembers.length; i++) {
        let PersonRef = xmlDoc.createElement('PersonRef');
        PersonRef.set("id", onlineMembers[i]);
        Operation.appendChild(PersonRef);
    }
    OperationRecords.appendChild(Operation);

    // TaskList
    let Task = xmlDoc.getElementById(taskId);
    if (Task !== null) {
        let OperationRef = xmlDoc.createElement('OperationRef');
        Operation.set("id", operationId);
        Task.appendChild(OperationRef);
    }
}

function analysisRecord(taskId, toolId, userId, inputs, outputs, params) {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    //OperationRecords
    let operationId = guid();
    let OperationRecords = xmlDoc.getElementsByTagName("OperationRecords")[0];
    let Operation = xmlDoc.createElement('Operation');
    Operation.set("id", operationId);
    Operation.set("type", "geo-analysis");
    Operation.set("toolRef", toolId);
    Operation.set("operator", userId);
    Operation.set("time", new Date().Format("yyyy-MM-dd HH:mm:ss"));
    for (var i = 0; i < inputs.length; i++) {
        let ResRef = xmlDoc.createElement('ResRef');
        ResRef.set("id", inputs[i]);
        ResRef.set("type", "input");
        Operation.appendChild(ResRef);
    }
    for (var i = 0; i < outputs.length; i++) {
        let ResRef = xmlDoc.createElement('ResRef');
        ResRef.set("id", outputs[i]);
        ResRef.set("type", "output");
        Operation.appendChild(ResRef);
    }
    for (var i = 0; i < params.length; i++) {
        let ResRef = xmlDoc.createElement('ResRef');
        ResRef.set("id", params[i]);
        ResRef.set("type", "param");
        Operation.appendChild(ResRef);
    }
    OperationRecords.appendChild(Operation);

    // TaskList
    let Task = xmlDoc.getElementById(taskId);
    if (Task !== null) {
        let OperationRef = xmlDoc.createElement('OperationRef');
        Operation.set("id", operationId);
        Task.appendChild(OperationRef);
    }
}

// Child Activity
function activityRecord(behavior, userId, activityInfo) {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    // ChildActivities
    if (behavior === "create") {
        let ChildActivities = xmlDoc.getElementsByTagName("ChildActivities")[0];
        let Child = xmlDoc.createElement('Child');
        Child.set("id", activityInfo.aid);
        Child.set("name", activityInfo.name);
        Child.set("description", activityInfo.description);
        Child.set("creator", activityInfo.creator);
        Child.set("state", "accessible");
        ChildActivities.appendChild(Child);
    } else if (behavior === "remove") {
        let Child = xmlDoc.getElementById(activityInfo.aid);
        if (Child !== null) {
            Child.set("state", "removed");
        }
    }

    //OperationRecords
    let operationId = guid();
    let OperationRecords = xmlDoc.getElementsByTagName("OperationRecords")[0];
    let Operation = xmlDoc.createElement('Operation');
    Operation.set("id", operationId);
    Operation.set("type", "activity");
    Operation.set("behavior", behavior);
    Operation.set("operator", userId);
    Operation.set("time", new Date().Format("yyyy-MM-dd HH:mm:ss"));
    OperationRecords.appendChild(Operation);
}

function processRecord(behavior, userId, protocalInfo, lasts, nexts) {
    if (xmlDoc === null) {
        alert("Failed to record operation. Please load activity document first!");
        return;
    }

    // ProtocalList
    if (behavior === "link") {
        let ProtocalList = xmlDoc.getElementsByTagName("ChildActivities")[0];
        let Protocal = xmlDoc.createElement('Protocal');
        Protocal.set("id", protocalInfo.pid);
        Protocal.set("name", protocalInfo.name);
        Protocal.set("description", protocalInfo.description);
        Protocal.set("state", "used");
        for (var i = 0; i < lasts.length; i++) {
            let From = xmlDoc.createElement('From');
            From.set("childRef", lasts[i]);
            Protocal.appendChild(From);
        }
        for (var i = 0; i < nexts.length; i++) {
            let To = xmlDoc.createElement('To');
            To.set("childRef", nexts[i]);
            Protocal.appendChild(To);
        }
        ProtocalList.appendChild(Protocal);
    } else if (behavior === "break") {
        let Protocal = xmlDoc.getElementById(protocalId);
        if (Protocal !== null) {
            Protocal.set("state", "removed");
        }
    }

    //OperationRecords
    let operationId = guid();
    let OperationRecords = xmlDoc.getElementsByTagName("OperationRecords")[0];
    let Operation = xmlDoc.createElement('Operation');
    Operation.set("id", operationId);
    Operation.set("type", "process");
    Operation.set("behavior", behavior);
    Operation.set("protocal", protocalId);
    Operation.set("operator", userId);
    Operation.set("time", new Date().Format("yyyy-MM-dd HH:mm:ss"));
    OperationRecords.appendChild(Operation);
}

// delete
function deleteActivityDoc(aid) {
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

// view
function buildDocView() {
    return;
}