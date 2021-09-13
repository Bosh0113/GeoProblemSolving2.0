var websockets = {};
var callbacks = {};
var websockLinked = {};
//当前连接
let connectorNum = {};

var socketSites = []

var timer = null;


function initWebSocket(para) { //初始化websocket
  let IP_Port = window.location.host;
  var wsurl = `${window.location.protocol === 'https:' ? 'wss://' : 'ws://'}${IP_Port}/GeoProblemSolving/${para}`;
  if (IP_Port == "localhost:8080") {
    wsurl = `ws://localhost:8081/GeoProblemSolving/${para}`;
  }
  //switch 使用时提供一个参数type
  let websock = new WebSocket(wsurl);
  websock.onmessage = function (e) {
    websocketonmessage(e, para);
    websockLinked[para] = true;
  }
  websock.onclose = function (e) {
    websocketclose(e, para);
    removeTimer();
    websockLinked[para] = false;
  }
  //连接成功的回调函数
  websock.onopen = function () {
    websocketOpen();
    setTimer();
    websockLinked[para] = true;
  }

  //连接发生错误的回调方法
  websock.onerror = function () {
    console.log("WebSocket error");
    removeTimer();
    websockLinked[para] = false;
  }

  socketSites.push[para];
  websockets[para] = websock;
  connectorNum[para] = 1;
}

function close(param) {
  websockets[param].close();
}

function getSocketInfo(param) {
  return {
    linked: websockLinked[param],
    num: connectorNum[param]
  }
}

// 实际调用的方法
function sendSock(param, agentData, callback) {
  callbacks[param] = callback;
  if (websockets[param].readyState === websockets[param].OPEN) {
    // 若是ws开启状态
    if (connectorNum[param] > 1 || agentData.type == "test") {
      websocketsend(param, agentData);
    }
  } else if (websockets[param].readyState === websockets[param].CONNECTING) {
    // 若是 正在开启状态，则等待1s后重新调用
    setTimeout(function () {
      sendSock(param, agentData, callback);
    }, 1000);
  } else {
    // 若未开启 ，则等待1s后重新调用
    setTimeout(function () {
      sendSock(param, agentData, callback);
    }, 1000);
  }
}

//

//数据接收
function websocketonmessage(e, param) {
  let socketCallback = callbacks[param];
  try {
    var data = JSON.parse(e.data);
    if (data.type == "members") {
      connectorNum[param] = data.participants.length;
    }
    if (data.type != "ping") {
      if (socketCallback != null && socketCallback != "" && socketCallback != undefined) {
        socketCallback(data);
      }
    }
  } catch (err) {
  }
  ;
}

//数据发送
function websocketsend(param, agentData) {
  websockets[param].send(JSON.stringify(agentData));
}

//关闭
function websocketclose(e, para) {
  connectorNum[para] -= 1;
  console.log("Connection closed (" + e.code + ")");
}

function websocketOpen(e) {
  console.log("Connect successfully");
}

function setTimer() {
  timer = setInterval(() => {
    var messageJson = {type: "ping"};
    for (let i = 0; i < socketSites.length; i++) {
      websocketsend(socketSites[i], messageJson);
    }
  }, 20000);
}

function removeTimer() {
  clearInterval(timer);
}


// initWebSocket();

export {initWebSocket, sendSock, close, getSocketInfo}
