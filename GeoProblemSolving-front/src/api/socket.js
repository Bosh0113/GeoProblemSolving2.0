var websock = null;
var global_callback = null;
var serverPort = '5000';	//webSocket连接端口
var timer = null;


function getWebIP() {
    var curIP = window.location.hostname;
    return curIP;
}

function initWebSocket(para,IP_Port) { //初始化websocket
    // var wsurl = "ws://"+IP_Port+"/GeoProblemSolving/" + para;
    var wsurl = "ws://localhost:8081/GeoProblemSolving/" + para;
    //switch 使用时提供一个参数type
    websock = new WebSocket(wsurl);
    websock.onmessage = function (e) {
        websocketonmessage(e);
    }
    websock.onclose = function (e) {
        websocketclose(e);
        removeTimer();
    }
    websock.onopen = function () {
        websocketOpen();
        setTimer();
    }

    //连接发生错误的回调方法
    websock.onerror = function () {
        console.log("WebSocket连接发生错误");
        removeTimer();
    }

}

function close(){
    websock.close();
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
    var data = JSON.parse(e.data);
    if(data.type != "ping"){
        if (global_callback != null && global_callback != "" && global_callback != undefined) {
            global_callback(data);
        }
    }
}

//数据发送
function websocketsend(agentData) {
    websock.send(JSON.stringify(agentData));
}

//关闭
function websocketclose(e) {
    console.log("connection closed (" + e.code + ")");
}

function websocketOpen(e) {
    console.log("连接成功");
}

function setTimer() {
    timer = setInterval(() => {
      var messageJson = {};
      messageJson["type"] = "ping";
      messageJson["message"] = "ping";
      websocketsend(messageJson);
    }, 20000);
  }

function removeTimer() {
  clearInterval(timer);
}


// initWebSocket();

export { sendSock, initWebSocket, close }
