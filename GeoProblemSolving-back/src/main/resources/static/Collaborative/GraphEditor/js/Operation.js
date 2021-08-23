var mxGraphSocket = null;
// var foreControllerId = "";
// var controllerId = "";
var graph;

var pageParams = {};
var currentResources = [];
var href = window.location.href;
var url = href.split("&");
var waitingList = [];

// Extends EditorUi to update I/O action states based on availability of backend
(function () {
    getStepInfo();

    var editorUiInit = EditorUi.prototype.init;

    EditorUi.prototype.init = function () {
        editorUiInit.apply(this, arguments);
        this.actions.get('export').setEnabled(false);

        // Updates action states which require a backend
        if (!Editor.useLocalStorage) {
            mxUtils.post(OPEN_URL, '', mxUtils.bind(this, function (req) {
                var enabled = req.getStatus() != 404;
                this.actions.get('open').setEnabled(enabled || Graph.fileSupport);
                this.actions.get('import').setEnabled(enabled || Graph.fileSupport);
                this.actions.get('save').setEnabled(enabled);
                this.actions.get('saveAs').setEnabled(enabled);
                this.actions.get('export').setEnabled(enabled);
            }));
        }
    };

    // Adds required resources (disables loading of fallback properties, this can only
    // be used if we know that all keys are defined in the language specific file)
    mxResources.loadDefaultBundle = false;
    var bundle = mxResources.getDefaultBundle(RESOURCE_BASE, mxLanguage) ||
        mxResources.getSpecialBundle(RESOURCE_BASE, mxLanguage);

    // Fixes possible asynchronous requests
    mxUtils.getAll([bundle, STYLE_PATH + '/default.xml'], function (xhr) {
        // Adds bundle text to resources
        mxResources.parse(xhr[0].getText());

        // Configures the default graph theme
        var themes = new Object();
        themes[Graph.prototype.defaultThemeName] = xhr[1].getDocumentElement();

        // Main
        var editorUI = new EditorUi(new Editor(urlParams['chrome'] == '0', themes));

        //----------------WebSocket-------------------//
        var memberList = '<div id="members" style="float: right;margin-right: 50px;width: 120px;text-align: right;overflow-x: auto;overflow-y: hidden;"></div>';
        $(".geToolbar").append(memberList);
        graph = editorUI.toolbar.editorUi.editor.graph;

            //timer
            window.setInterval(function () {
                // if (controllerId == pageParams.userId) {
                // console.log(graphXML);
                if (getSocketInfo().linked) {
                    let graphXML = getGraphXML();
                    let messageObject = {
                        type: "operation",
                        behavior: "refresh",
                        sender: pageParams.userId,
                        content: graphXML
                    };
                    sendCustomOperation(messageObject);
                }
                // }
            }, 2000);

        //-------------------end----------------------//
    }, function () {
        document.body.innerHTML = '<center style="margin-top:10%;">Error loading resource files. Please check browser console.</center>';
    });
})();


// 获取数据
function getStepInfo() {
    if (componentStatus) {
        // 获取数据
        pageParams.pageId = activityInfo.aid;
        pageParams.userId = userInfo.userId;
        pageParams.userName = userInfo.name;
        currentResources = resources;

        // 绑定函数
        buildSocketChannel(this.getSocketOperation, null, null);
        loadResChannel = this.loadTask;
    } else {
        let _this = this;
        setTimeout(function () {
            getStepInfo();
        }, 1000);
    }
}

function getSocketOperation(data) {
    var messageObject = data;
    if (messageObject.behavior == "refresh") {
        setGraphXML(messageObject.content);
    }
    // else if (messageObject.behavior == "Members") {
    //     //成员变动
    //     foreControllerId = controllerId;
    //     controllerId = messageObject.controllerId;
    //     var memberIds = messageObject.mIds.replace("[", "").replace("]", "").replace(/\s/g, '').split(",");
    //     var memberNames = messageObject.mNames.replace("[", "").replace("]", "").replace(/\s/g, '').split(",");
    //     var membersHtml = "";
    //     for (var i = 0; i < memberIds.length; i++) {
    //         var accountIcon = "";
    //         if (memberIds[i] === messageObject.controllerId) {
    //             accountIcon = "assignment_ind";
    //         }
    //         else {
    //             accountIcon = "account_circle";
    //         }
    //         membersHtml += '<i class="material-icons account"  title="' + memberNames[i] +
    //             '">' + accountIcon + '</i>';
    //     }
    //     $("#members").html(membersHtml);
    //     if (messageObject.requireList != "[]") {
    //         waitingList = messageObject.requireList.replace("[", "").replace("]", "").replace(/\s/g, '').split(",");
    //     } else {
    //         waitingList = [];
    //     }
    //     checkIdentity();
    // }
}

//graph->XML
function getGraphXML() {
    var encoder = new mxCodec();
    var node = encoder.encode(graph.getModel());
    return mxUtils.getXml(node);
}

//XML->graph
function setGraphXML(xml) {
    if (xml != null) {
        graph.getModel().beginUpdate();
        var doc = mxUtils.parseXml(xml);
        var dec = new mxCodec(doc);
        dec.decode(doc.documentElement, graph.getModel());
        graph.getModel().endUpdate();
    }
}


function loadTask(data) {
    var selectedTask = {};
    for (let i = 0; i < data.length; i++) {

        window.taskInfo = {
            taskId: data.uid,
            title: data.name,
            description: data.description
        };

        var xhr = new XMLHttpRequest();
        xhr.open("GET", data[i].address, true);
        xhr.onload = function (e) {
            if (this.status == 200) {
                setGraphXML(this.response);
            }
        };
        xhr.send();


    }

}

//演示权限转交
// function pTransfer(newControllerId) {
//     if (controllerId === uspageParams.userIderId) {
//         if (newControllerId === pageParams.userId) {
//             confirm("You already have demo authority.");
//         }
//         else {
//             var r = confirm("Whether to transfer demo authority to " + newControllerId + " ?");
//             if (r === true) {
//                 var messageObject = {};
//                 messageObject["type"] = "Members";
//                 messageObject["content"] = newControllerId;
//                 mxGraphSocket.send(JSON.stringify(messageObject));
//             }
//         }
//     }
//     else {
//         confirm("Sorry, You have no right to transfer authority.");
//     }
//
// }

//申请演示
// function demoRequire() {
//     var messageObject = {};
//     messageObject["type"] = "Authority";
//     messageObject["message"] = "Require";
//     messageObject["userId"] = pageParams.userId;
//     if (mxGraphSocket) {
//         mxGraphSocket.send(JSON.stringify(messageObject));
//     }
// }

//放弃演示
// function demoRelease() {
//     var messageObject = {};
//     messageObject["type"] = "Authority";
//     messageObject["message"] = "Release";
//     messageObject["userId"] = pageParams.userId;
//     if (mxGraphSocket) {
//         mxGraphSocket.send(JSON.stringify(messageObject));
//     }
// }

//检查身份及权限
// function checkIdentity() {
//     if (controllerId == pageParams.userId && waitingList.length < 1) {//若为演示者且不存在申请者
//         $('#release').show();
//         $('#waiting').hide();
//         $('#waitingNum').show();
//         $('#require').hide();
//         $('#waitingNum').html("You are the presenter");
//         if (foreControllerId != controllerId) {//若首次成为演示者
//             window.time = 0;//重新计时
//             console.log("You are the presenter!");
//             graph.setEnabled(true);
//         }
//     }
//     else if (controllerId == pageParams.userId && waitingList.length > 0) {//若为演示者且存在申请者
//         $('#release').show();
//         $('#waiting').show();
//         $('#waitingNum').show();
//         $('#require').hide();
//         if (foreControllerId != controllerId) {//若首次成为演示者
//             window.time = 0;
//             console.log("You are the presenter!");
//             graph.setEnabled(true);
//         }
//         var num = 0;
//         for (var i = 0; i < waitingList.length; i++) {
//             if (waitingList[i] == pageParams.userId) {
//                 continue;
//             }
//             num++;
//         }
//         $('#waitingNum').html(num);
//     } else if (controllerId != pageParams.userId) { //观众
//         var apply = 0;
//         num = 0;
//         for (i = 0; i < waitingList.length; i++) {
//             if (controllerId == waitingList[i]) {//若演示者在队列中，则跳过计数
//                 continue;
//             }
//             if (waitingList[i] == pageParams.userId) {
//                 apply = 1;//已申请
//                 break;
//             }
//             num++;
//         }
//         if (apply) {
//             $('#release').hide();
//             $('#waiting').show();
//             $('#waitingNum').show();
//             $('#require').hide();
//             $('#waitingNum').html(num);
//         }
//         else {//若未申请
//             $('#release').hide();
//             $('#waiting').hide();
//             $('#waitingNum').hide();
//             $('#require').show();
//         }
//         graph.setEnabled(false);
//         graph.setCellsSelectable(true);
//     }
// }

// window.time = 0;
// window.setInterval(function () {
//     window.time++;
//     if (window.time >= 60) {//60秒无动作则触发
//         if (controllerId == pageParams.userId) {//若为演示者则释放权限
//             demoRelease();
//         }
//     }
// }, 1000);


// $("body").mousemove(function () {
//     window.time = 0;
// });
// $("body").keydown(function () {
//     window.time = 0;
// });