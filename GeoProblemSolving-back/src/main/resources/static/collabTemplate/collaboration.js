/**
 * require jquery, jTemplates
 */

var activityInfo = null;
var resources = null;
var participants = null;

$(function () {
    // ready - event
    // create tags
    // ...
    // style
    $("#collab-tool-head").append(`<li class="head-logo"></li>`);
    $("#collab-tool-sidebar").append(
        `<ul class="nav flex-column">
            <li class="nav-item">
                <button type="button" class="btn btn-dark active" id="people-btn" title="People">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor"
                        class="bi bi-people-fill" viewBox="0 0 16 16">
                        <path d="M7 14s-1 0-1-1 1-4 5-4 5 3 5 4-1 1-1 1H7zm4-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6z" />
                        <path fill-rule="evenodd"
                            d="M5.216 14A2.238 2.238 0 0 1 5 13c0-1.355.68-2.75 1.936-3.72A6.325 6.325 0 0 0 5 9c-4 0-5 3-5 4s1 1 1 1h4.216z" />
                        <path d="M4.5 8a2.5 2.5 0 1 0 0-5 2.5 2.5 0 0 0 0 5z" />
                    </svg>
                </button>
            </li>
            <li class="nav-item">
                <button type="button" class="btn btn-dark" id="resource-btn" title="Resources">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor"
                        class="bi bi-folder-fill" viewBox="0 0 16 16">
                        <path
                            d="M9.828 3h3.982a2 2 0 0 1 1.992 2.181l-.637 7A2 2 0 0 1 13.174 14H2.825a2 2 0 0 1-1.991-1.819l-.637-7a1.99 1.99 0 0 1 .342-1.31L.5 3a2 2 0 0 1 2-2h3.672a2 2 0 0 1 1.414.586l.828.828A2 2 0 0 0 9.828 3zm-8.322.12C1.72 3.042 1.95 3 2.19 3h5.396l-.707-.707A1 1 0 0 0 6.172 2H2.5a1 1 0 0 0-1 .981l.006.139z" />
                    </svg>
                </button>
            </li>
            <li class="nav-item">
                <button type="button" class="btn btn-dark" id="operation-btn" title="Operations">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor"
                        class="bi bi-ui-checks" viewBox="0 0 16 16">
                        <path
                            d="M7 2.5a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-7a.5.5 0 0 1-.5-.5v-1zM2 1a2 2 0 0 0-2 2v2a2 2 0 0 0 2 2h2a2 2 0 0 0 2-2V3a2 2 0 0 0-2-2H2zm0 8a2 2 0 0 0-2 2v2a2 2 0 0 0 2 2h2a2 2 0 0 0 2-2v-2a2 2 0 0 0-2-2H2zm.854-3.646a.5.5 0 0 1-.708 0l-1-1a.5.5 0 1 1 .708-.708l.646.647 1.646-1.647a.5.5 0 1 1 .708.708l-2 2zm0 8a.5.5 0 0 1-.708 0l-1-1a.5.5 0 0 1 .708-.708l.646.647 1.646-1.647a.5.5 0 0 1 .708.708l-2 2zM7 10.5a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-7a.5.5 0 0 1-.5-.5v-1zm0-5a.5.5 0 0 1 .5-.5h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1-.5-.5zm0 8a.5.5 0 0 1 .5-.5h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1-.5-.5z" />
                    </svg>
                </button>
            </li>
        </ul>
        <div class="nav-content">
            <div class="sidebar-content scrollbar" id="people-list">
            </div>
            <div class="sidebar-content" id="resource-panel" style="display: none;">
                <div class="resource-search form-inline">
                    <div class="resource-input">
                        <input class="form-control form-control-sm" id="resource-search-form" style="width: 100%;"
                            placeholder="Key word">
                    </div>
                    <button class="btn btn-primary btn-sm">Filter</button>
                </div>
                <div class="resource-control">
                    Resource list
                    <div class="resource-control-btn" title="Back">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                            class="bi bi-arrow-left" viewBox="0 0 16 16">
                            <path fill-rule="evenodd"
                                d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8z" />
                        </svg>
                    </div>
                    <div class="resource-control-btn" title="Refresh">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                            class="bi bi-arrow-repeat" viewBox="0 0 16 16">
                            <path
                                d="M11.534 7h3.932a.25.25 0 0 1 .192.41l-1.966 2.36a.25.25 0 0 1-.384 0l-1.966-2.36a.25.25 0 0 1 .192-.41zm-11 2h3.932a.25.25 0 0 0 .192-.41L2.692 6.23a.25.25 0 0 0-.384 0L.342 8.59A.25.25 0 0 0 .534 9z" />
                            <path fill-rule="evenodd"
                                d="M8 3c-1.552 0-2.94.707-3.857 1.818a.5.5 0 1 1-.771-.636A6.002 6.002 0 0 1 13.917 7H12.9A5.002 5.002 0 0 0 8 3zM3.1 9a5.002 5.002 0 0 0 8.757 2.182.5.5 0 1 1 .771.636A6.002 6.002 0 0 1 2.083 9H3.1z" />
                        </svg>
                    </div>
                </div>
                <div class="resource-list scrollbar" id="resource-list">                    
                </div>
                <div class="resource-load">
                    <button class="btn btn-primary btn-sm">Load resource</button>
                </div>
            </div>
            <div class="sidebar-content" id="operation-panel" style="display: none;">            
                <div class="sync-state card">
                    <div class="custom-control custom-switch" title="Collaboration or not">
                        <label class="custom-control-label" for="collaboration-switch">Stop collaboration</label>
                        <input type="checkbox" class="custom-control-input" id="collaboration-switch">
                    </div>
                </div>
                <div class="sync-state card">
                    <div class="custom-control custom-switch" title="Free collaboration or semi-free collaboration">
                        <label class="custom-control-label" for="free-collaboration-switch">Free collaboration</label>
                        <input type="checkbox" class="custom-control-input" id="free-collaboration-switch">
                    </div>
                </div>
                <div class="operation-control">
                    <div class="operation-apply">
                        <div>Operation</div>
                        <button class="btn btn-success btn-sm apply" style="background-color: green;">Apply</button>
                        <button class="btn btn-info btn-sm stop" style="display: none;">Stop</button>
                    </div>
                    <div class="operator">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-joystick" viewBox="0 0 16 16" style="margin-top: -3px; margin-right:5px">
                            <path d="M10 2a2 2 0 0 1-1.5 1.937v5.087c.863.083 1.5.377 1.5.726 0 .414-.895.75-2 .75s-2-.336-2-.75c0-.35.637-.643 1.5-.726V3.937A2 2 0 1 1 10 2z"/>
                            <path d="M0 9.665v1.717a1 1 0 0 0 .553.894l6.553 3.277a2 2 0 0 0 1.788 0l6.553-3.277a1 1 0 0 0 .553-.894V9.665c0-.1-.06-.19-.152-.23L9.5 6.715v.993l5.227 2.178a.125.125 0 0 1 .001.23l-5.94 2.546a2 2 0 0 1-1.576 0l-5.94-2.546a.125.125 0 0 1 .001-.23L6.5 7.708l-.013-.988L.152 9.435a.25.25 0 0 0-.152.23z"/>
                        </svg> Operator:
                        <span class="operator-name">xxxx</span>
                    </div>
                    <div class="operation-waiting">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-person-lines-fill" viewBox="0 0 16 16" style="margin-top: -3px;">
                            <path d="M6 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm-5 6s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H1zM11 3.5a.5.5 0 0 1 .5-.5h4a.5.5 0 0 1 0 1h-4a.5.5 0 0 1-.5-.5zm.5 2.5a.5.5 0 0 0 0 1h4a.5.5 0 0 0 0-1h-4zm2 3a.5.5 0 0 0 0 1h2a.5.5 0 0 0 0-1h-2zm0 3a.5.5 0 0 0 0 1h2a.5.5 0 0 0 0-1h-2z"/>
                          </svg>
                        <span style="margin-left: 5px; color: #007bff;" title="Waiting for operation">X people are waiting</span>
                    </div>
                </div>
                <div class="operation-list scrollbar" id="operation-list"></div>
                <div class="operation-bind">
                    <button class="btn btn-primary btn-sm">Bind to task</button>
                </div>
            </div>
        </div>`);

    $("#people-btn").on("click", function () {
        $("#people-btn").addClass("active");
        $("#resource-btn").removeClass("active");
        $("#operation-btn").removeClass("active");

        $("#people-list").show();
        $("#resource-panel").hide();
        $("#operation-panel").hide();

    })
    $("#resource-btn").on("click", function () {
        $("#people-btn").removeClass("active");
        $("#resource-btn").addClass("active");
        $("#operation-btn").removeClass("active");

        $("#people-list").hide();
        $("#resource-panel").show();
        $("#operation-panel").hide();
    })
    $("#operation-btn").on("click", function () {
        $("#people-btn").removeClass("active");
        $("#resource-btn").removeClass("active");
        $("#operation-btn").addClass("active");

        $("#people-list").hide();
        $("#resource-panel").hide();
        $("#operation-panel").show();

    })

    // message    
    window.addEventListener("message", getActivityInfo, false);
});

function getActivityInfo(event) {
    if (event.data.type === "activity") {
        console.log(event.data);
        activityInfo = event.data.activity;


        getResources();
        getParticipants()

        // socket
        initWebSocket(event.data.aid, event.data.tid)

    }
}

/**
 * Resources
 */
function getResources() {

}

/**
 * Joined members
 */
function getParticipants() {

    let reqUrl = "";
    if (activityInfo.level == 0) {
        reqUrl = "/GeoProblemSolving/project/" + activityInfo.aid + "/user";
    } else if (activityInfo.level == 1) {
        reqUrl = "/GeoProblemSolving/subproject/" + activityInfo.aid + "/user";
    } else if (activityInfo.level > 1) {
        reqUrl = "/GeoProblemSolving/activity/" + activityInfo.aid + "/user";
    }
    $.ajax({
        url: reqUrl,
        type: "GET",
        async: false,
        success: function (result) {
            if (result.code == 0) {
                // creatorInfo = result.data.creator;
                participants = result.data.members;
            } else {
                console.log(result.msg);
            }
        },
        error: function (err) {
            throw err;
        }
    });
}

/**
 * Operations
 */
function a() {

}

/**
 * Socket
 */
var websock = null;
var global_callback = null;
var timer = null;
var websockLinked = false;

function initWebSocket(aid, toolId) { //初始化websocket
    let IP_Port = window.location.host;
    var wsurl = `${window.location.protocol === 'https:' ? 'wss://' : 'ws://'}${IP_Port}/GeoProblemSolving/OperationServer/${toolId}/${aid}`;
    if (IP_Port == "localhost:8080") {
        wsurl = `ws://localhost:8081/GeoProblemSolving/OperationServer/${toolId}/${aid}`;
    }
    //switch 使用时提供一个参数type
    websock = new WebSocket(wsurl);
    websock.onopen = function () {
        websocketOpen();
        setTimer();
        websockLinked = true;
    }
    websock.onmessage = function (e) {
        websocketonmessage(e);
        websockLinked = true;
    }
    websock.onclose = function (e) {
        websocketclose(e);
        removeTimer();
        websockLinked = false;
    }

    //连接发生错误的回调方法
    websock.onerror = function () {
        console.log("WebSocket error!");
        removeTimer();
        websockLinked = false;
    }

}

function close() {
    websock.close();
}

function getSocketInfo() {
    return {
        linked: websockLinked
    }
}

// 实际调用的方法
function sendSock(agentData, callback) {
    global_callback = callback;
    if (websock.readyState === websock.OPEN) {
        // 若是ws开启状态
        websocketsend(agentData)
    } else if (websock.readyState === websock.CONNECTING) {
        // 若是 正在开启状态，则等待1s后重新调用
        setTimeout(function () {
            sendSock(agentData, callback);
        }, 1000);
    } else {
        // 若未开启 ，则等待1s后重新调用
        setTimeout(function () {
            sendSock(agentData, callback);
        }, 1000);
    }
}

//数据接收
function websocketonmessage(e) {
    try {
        var data = JSON.parse(e.data);
        if (data.type != "ping") {
            if (global_callback != null && global_callback != "" && global_callback != undefined) {
                global_callback(data);
            }
        }
    } catch (err) { throw err };
}

function websocketOpen(e) {
    console.log("Connect successfully!");
}

//数据发送
function websocketsend(agentData) {
    websock.send(JSON.stringify(agentData));
}

//关闭
function websocketclose(e) {
    console.log("Connection closed (" + e.code + ")");
}

function setTimer() {
    timer = setInterval(() => {
        var messageJson = { type: "ping" };
        websocketsend(messageJson);
    }, 20000);
}

function removeTimer() {
    clearInterval(timer);
}