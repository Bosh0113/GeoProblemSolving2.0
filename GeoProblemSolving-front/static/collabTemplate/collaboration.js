/**
 * variable
 */
// the loading status of the collaboration component
var componentStatus = false;

// basic information
var toolId = "";
var activityInfo = null;

// user related
var userInfo = null;
var participants = null;
var onlineMembers = null;
var UserServer = "http://172.21.212.103:8088/userServer";

// resource related
var resources = [];
selectedResources = [];

// operation related
loadResChannel = null;

$(function () {
  // ready - event
  // create tags
  // ...

});

///////////
/// Style
//////////
/**
 * public method
 * init
 */
function loadCollabComponent() {
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
                <div class="resource-list scrollbar" id="resource-list"></div>
                <div class="resource-load">
                    <button class="btn btn-primary btn-sm" id="resource-load">Load resource</button>
                </div>
            </div>
            <div class="sidebar-content" id="operation-panel" style="display: none;">
                <div class="sync-state card" style="height: 40px">
                    <div class="custom-control custom-switch" title="Enable collaboration">
                        <input type="checkbox" class="custom-control-input" id="collaboration-switch" style="cursor: pointer">
                        <label class="custom-control-label" for="collaboration-switch" style="cursor: pointer">Collaboration</label>
                    </div>
                </div>
                <div class="sync-state card">
                    <select class="custom-select custom-select-sm" title="Collaboration mode" id="collaboration-mode" disabled >
                        <option selected value="Free">Free</option>
                        <option value="SemiFree_Occupy">Occupy</option>
                        <option value="SemiFree_Apply">Apply</option>
                    </select>
                </div>
                <div class="operation-control-apply" style="display: none;">
                    <div class="operation-apply">
                        <div>Operation</div>
                        <button class="btn btn-success btn-sm apply" style="background-color: green;" id="operation-apply">Apply</button>
                        <button class="btn btn-info btn-sm stop" style="display: none;" id="operation-stop">Stop</button>
                    </div>
                    <div class="operator" id="operator">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-joystick" viewBox="0 0 16 16" style="margin-top: -3px; margin-right:5px">
                            <path d="M10 2a2 2 0 0 1-1.5 1.937v5.087c.863.083 1.5.377 1.5.726 0 .414-.895.75-2 .75s-2-.336-2-.75c0-.35.637-.643 1.5-.726V3.937A2 2 0 1 1 10 2z"/>
                            <path d="M0 9.665v1.717a1 1 0 0 0 .553.894l6.553 3.277a2 2 0 0 0 1.788 0l6.553-3.277a1 1 0 0 0 .553-.894V9.665c0-.1-.06-.19-.152-.23L9.5 6.715v.993l5.227 2.178a.125.125 0 0 1 .001.23l-5.94 2.546a2 2 0 0 1-1.576 0l-5.94-2.546a.125.125 0 0 1 .001-.23L6.5 7.708l-.013-.988L.152 9.435a.25.25 0 0 0-.152.23z"/>
                        </svg> Operator:
                    </div>
                    <div class="operation-waiting" id="operation-waiting"></div>
                </div>
                <div class="operation-control-occupy" style="display: none;">
                    <div class="operator" id="operator">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-joystick" viewBox="0 0 16 16" style="margin-top: -3px; margin-right:5px">
                            <path d="M10 2a2 2 0 0 1-1.5 1.937v5.087c.863.083 1.5.377 1.5.726 0 .414-.895.75-2 .75s-2-.336-2-.75c0-.35.637-.643 1.5-.726V3.937A2 2 0 1 1 10 2z"/>
                            <path d="M0 9.665v1.717a1 1 0 0 0 .553.894l6.553 3.277a2 2 0 0 0 1.788 0l6.553-3.277a1 1 0 0 0 .553-.894V9.665c0-.1-.06-.19-.152-.23L9.5 6.715v.993l5.227 2.178a.125.125 0 0 1 .001.23l-5.94 2.546a2 2 0 0 1-1.576 0l-5.94-2.546a.125.125 0 0 1 .001-.23L6.5 7.708l-.013-.988L.152 9.435a.25.25 0 0 0-.152.23z"/>
                        </svg> Operator:
                    </div>
                </div>
                <div class="operation-list scrollbar">
                    <span style="margin-left: 10px; font-size: 14px; font-weight:bold">Operations</span>
                    <div id="operation-list"></div>
                </div>
                <div class="operation-bind">
                    <button class="btn btn-primary btn-sm">Bind to task</button>
                </div>
            </div>
        </div>`);

  // events
  addEvents();
}

function addEvents() {
  // message
  window.addEventListener("message", getActivityInfo, false);

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
  $("#resource-load").on("click", function () {
    if (loadResChannel != undefined && typeof loadResChannel == "function") {
      loadResChannel(selectedResources);
    }
  });
  $("#operation-btn").on("click", function () {
    $("#people-btn").removeClass("active");
    $("#resource-btn").removeClass("active");
    $("#operation-btn").addClass("active");

    $("#people-list").hide();
    $("#resource-panel").hide();
    $("#operation-panel").show();
  })

  $("#operation-apply").on("click", operationApply);

  $("#operation-stop").on("click", operationStop);

  $("#collaboration-switch").on("change", () => {
    if ($("#collaboration-switch").is(':checked')) {
      startCollaboration();
    } else {
      socketClose();
    }
  })

  $("#collaboration-mode").on("change", function () {
    let value = $("#collaboration-mode option:checked").val();
    syncCollabMode(value);
    setCollaborationMode(value);
  });
}

function showParticipants() {
  for (let i = 0; i < participants.length; i++) {
    let avatar = ""
    if (participants[i].avatar == undefined || participants[i].avatar == "") {
      avatar = "/static/collabTemplate/img/icon_avatar.png";
    } else {
      avatar = UserServer + participants[i].avatar;
    }
    let peopleElement = `<div class="card participants" id="${participants[i].userId}">
                                <img src="${avatar}" class="participant-avatar" />
                                <div class="participant-info">
                                    <div class="participant-info-name">${participants[i].name}</div>
                                    <div class="participant-info-role">${participants[i].role}</div>
                                </div>
                            </div>`
    $("#people-list").append(peopleElement);
    if (participants[i].userId != userInfo.userId) {
      $(`#${participants[i].userId}`).css("background-color", "lightgrey");
    }
  }
}

function personOnline(members) {
  for (let i = 0; i < members.length; i++) {
    $(`#${members[i].userId}`).css("background-color", "white");
  }
}

function personOffline(member) {
  $(`#${member.userId}`).css("background-color", "lightgrey");
}

function showResList() {
  if (resources != undefined) {
    for (let i = 0; i < resources.folders.length; i++) {
      addfolder(resources.folders[i]);
    }
    for (let j = 0; j < resources.files.length; j++) {
      addfile(resources.files[j]);
    }
  }
}

function addfolder(folder) {
  let resElement = `<div class="card resource" title="${folder.name}">
                                    <input class="form-check-input" type="checkbox" id="${folder.uid}">
                                    <img src="/static/collabTemplate/mg/folder.png" class="res-icon" />
                                    <div class="res-name">${folder.name}</div>
                                </div>`
  $("#resource-list").append(resElement);
  $(`#${folder.uid}`).on("change", function () {
    selectFile(folder);

    let message = {
      type: "data",
      sender: userInfo.userId,
      behavior: "select",
      content: {
        uid: file.uid,
        name: file.name,
        description: file.description,
        address: file.address,
      }
    }
    websock.send(message);
  });
}

function addfile(file) {
  let resElement = "";
  let fileName = file.name + file.suffix;
  switch (file.type) {
    case "data": {
      resElement = `<div class="card resource" title="${fileName}">
                            <input class="form-check-input" type="checkbox" id="${file.uid}" >
                            <img src="/static/collabTemplate/img/data.png" class="res-icon" />
                            <div class="res-name">${fileName}</div>
                        </div>`
      break;
    }
    case "model": {
      resElement = `<div class="card resource" title="${fileName}">
                            <input class="form-check-input" type="checkbox" id="${file.uid}" >
                            <img src="/static/collabTemplate/img/model.png" class="res-icon" />
                            <div class="res-name">${fileName}</div>
                        </div>`
      break;
    }
    case "paper": {
      resElement = `<div class="card resource" title="${fileName}">
                            <input class="form-check-input" type="checkbox" id="${file.uid}" >
                            <img src="/static/collabTemplate/img/paper.png" class="res-icon" />
                            <div class="res-name">${fileName}</div>
                        </div>`
      break;
    }
    case "document": {
      resElement = `<div class="card resource" title="${fileName}">
                            <input class="form-check-input" type="checkbox" id="${file.uid}">
                            <img src="/static/collabTemplate/img/document.png" class="res-icon" />
                            <div class="res-name">${fileName}</div>
                        </div>`
      break;
    }
    case "image": {
      resElement = `<div class="card resource" title="${fileName}">
                            <input class="form-check-input" type="checkbox" id="${file.uid}" >
                            <img src="/static/collabTemplate/img/image.png" class="res-icon" />
                            <div class="res-name">${fileName}</div>
                        </div>`
      break;
    }
    case "video": {
      resElement = `<div class="card resource" title="${fileName}">
                            <input class="form-check-input" type="checkbox" id="${file.uid}">
                            <img src="/static/collabTemplate/img/video.png" class="res-icon" />
                            <div class="res-name">${fileName}</div>
                        </div>`
      break;
    }
    case "other": {
      resElement = `<div class="card resource" title="${fileName}">
                            <input class="form-check-input" type="checkbox" id="${file.uid}">
                            <img src="/static/collabTemplate/img/otherfile.png" class="res-icon" />
                            <div class="res-name">${fileName}</div>
                        </div>`
      break;
    }
  }
  $("#resource-list").append(resElement);
  $(`#${file.uid}`).on("change", function () {
    selectFile(file);

    let message = {
      type: "data",
      sender: userInfo.userId,
      behavior: "select",
      content: {
        uid: file.uid,
        name: file.name,
        description: file.description,
        address: file.address,
      }
    }
    websock.send(message);
  });
}

function initCollaborationMode() {
  for (let i = 0; i < participants.length; i++) {
    if (participants[i].userId === userInfo.userId && participants[i].role === "manager") {
      $("#collaboration-mode").attr("disabled", false);
    }
  }
}

function setCollaborationMode(mode) {
  if (mode != undefined && mode !== "") {
    if (mode === "Free") {
      $(".operation-control-apply").hide();
      $(".operation-control-occupy").hide();
      $(".operation-list").css("height", "calc(100vh - 200px)");
    } else if (mode === "SemiFree_Apply") {
      $(".operation-control-apply").show();
      $(".operation-control-occupy").hide();
      $(".operation-list").css("height", "calc(100vh - 305px)");
    } else if (mode === "SemiFree_Occupy") {
      $(".operation-control-apply").hide();
      $(".operation-control-occupy").show();
      $(".operation-list").css("height", "calc(100vh - 250px)");
    }
  }
}

function setOperator(operator) {
  if (operator != undefined) {
    $("#operator-name").remove();
    $("#operator").append(`<span class="operator-name" id="operator-name">${operator.name}</span>`);
  }
}

function setWaitingLine(count) {
  if (count != undefined) {
    if (count > 0) {
      $("#operation-waiting").empty();
      $("#operation-waiting").append(`<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-person-lines-fill" viewBox="0 0 16 16" style="margin-top: -3px;">
                                                <path d="M6 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm-5 6s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H1zM11 3.5a.5.5 0 0 1 .5-.5h4a.5.5 0 0 1 0 1h-4a.5.5 0 0 1-.5-.5zm.5 2.5a.5.5 0 0 0 0 1h4a.5.5 0 0 0 0-1h-4zm2 3a.5.5 0 0 0 0 1h2a.5.5 0 0 0 0-1h-2zm0 3a.5.5 0 0 0 0 1h2a.5.5 0 0 0 0-1h-2z"/>
                                            </svg>
                                            <span style="margin-left: 5px; color: #007bff;" title="Waiting for operation">${count} people are waiting</span>`);
    } else if (count == 0) {
      $("#operation-waiting").empty();
      $("#operation-waiting").append(`<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-person-lines-fill" viewBox="0 0 16 16" style="margin-top: -3px;">
                                                <path d="M6 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm-5 6s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H1zM11 3.5a.5.5 0 0 1 .5-.5h4a.5.5 0 0 1 0 1h-4a.5.5 0 0 1-.5-.5zm.5 2.5a.5.5 0 0 0 0 1h4a.5.5 0 0 0 0-1h-4zm2 3a.5.5 0 0 0 0 1h2a.5.5 0 0 0 0-1h-2zm0 3a.5.5 0 0 0 0 1h2a.5.5 0 0 0 0-1h-2z"/>
                                            </svg>
                                            <span style="margin-left: 5px; color: #007bff;" title="Waiting for operation">Apply to operate</span>`);
    }
  }
}

///////////
/// Data
//////////
function getActivityInfo(event) {
  if (event.data.type === "activity") {

    activityInfo = event.data.activity;
    userInfo = event.data.user;
    toolId = event.data.tid;

    componentStatus = true;

    getParticipants();
    getResources();

    // socket
    initWebSocket(activityInfo.aid, toolId);
  }
}

/**
 * Resources
 */
function getResources() {

  $.ajax({
    url: "/GeoProblemSolving/rip/" + activityInfo.aid + "/0",
    type: "GET",
    async: false,
    success: function (result) {
      if (result == "Offline") {
        confirm("You are offline, please login.")
      } else if (result.code == 0) {
        let rootRes = result.data;
        resources = resToCurrentFolder(rootRes);
        showResList();
      }
    },
    error: function (err) {
      throw err;
    }
  });
}

function resToCurrentFolder(rootRes) {
  let currentFolder = {
    folders: [],
    files: []
  }
  for (let i = 0; i < rootRes.length; i++) {
    if (rootRes[i].folder) {
      currentFolder.folders.push(rootRes[i]);
    } else {
      currentFolder.files.push(rootRes[i]);
    }
  }
  return currentFolder;
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
        showParticipants();
        initCollaborationMode();
      } else {
        console.log(result.msg);
      }
    },
    error: function (err) {
      throw err;
    }
  });
}

////////////////
/// Operations
////////////////
function startCollaboration() {
  initWebSocket(activityInfo.aid, toolId);
}

function syncCollabMode(mode) {
  let message = {
    type: "mode",
    sender: userInfo.userId,
    content: mode
  }
  websocketSend(message);
}

function operationApply() {
  let message = {
    type: "control-apply",
    sender: userInfo.userId,
  }
  websocketSend(message);
}

function operationStop() {
  let message = {
    type: "control-stop",
    sender: userInfo.userId,
  }
  websocketSend(message);
}

function selectFile(file) {
  if ($(`#${file.uid}`).is(":checked")) {
    selectedResources.push(file);
  } else {
    for (let i = 0; i < selectedResources.length; i++) {
      if (selectedResources[i].uid == file.uid) {
        selectedResources.splice(i, 1);
      }
    }
  }
}


/**
 * public method
 * @param {*} uploadFiles 文件
 * @param {*} description 描述
 * @param {*} type 文件类型
 * @param {*} privacy 获取权限
 */
function uploadResources(uploadFiles, description, type, privacy) {
  var formData = new FormData();
  for (var i = 0; i < uploadFiles.length; i++) {
    formData.append("file", uploadFiles[i]);
  }
  formData.append("description", description);
  formData.append("type", type);
  formData.append("privacy", privacy);
  formData.append("aid", activityInfo.aid);
  formData.append("paths", ["0"].toString());

  $.ajax({
    url: "/GeoProblemSolving/rip/file/upload",
    type: "POST",
    data: formData,
    mimeType: "multipart/form-data",
    processData: false,
    contentType: false,
    cache: false,
    async: true,
    success: function (data) {
      if (data != "Fail") {
        let uploadedList = JSON.parse(data).uploaded;
        resourceChanged(uploadedList, "upload")

        for (let i = 0; i < uploadedList.length; i++) {
          let message = {
            type: "data",
            behavior: "upload",
            sender: userInfo.userId,
            content: {
              uid: uploadedList[i].uid,
              name: uploadedList[i].name,
              description: uploadedList[i].description,
              address: uploadedList[i].address,
            }
          }
          websocketSend(message);
        }

        return uploadedList;
      } else {
        alert("Upload fail.");
      }
    },
    error: function (err) {
      throw err;
    }
  });
}

/**
 * public method
 * when resources changed
 */
function resourceChanged(resources, behavior) {
  switch (behavior) {
    case "upload": {
      for (let i = 0; i < resources.length; i++) {
        if (resources[i].folder) {
          addfolder(resources[i]);
        } else {
          addfile(resources[i]);
        }
      }
      break;
    }
    case "delete": {

      break;
    }
  }

}

///////////
/// Socket
//////////
var websock = null;
var timer = null;
var websockLinked = false;
var operationChannel = null;
var dataChannel = null;
var computationChannel = null;

function initWebSocket(aid, toolId) { //初始化websocket
  let IP_Port = window.location.host;
  var wsurl = `${window.location.protocol === 'https:' ? 'wss://' : 'ws://'}${IP_Port}/GeoProblemSolving/OperationServer/${toolId}/${aid}`;
  if (IP_Port == "localhost:8080") {
    wsurl = `ws://localhost:8081/GeoProblemSolving/OperationServer/${toolId}/${aid}`;
  }
  //switch 使用时提供一个参数type
  websock = new WebSocket(wsurl);
  websock.onopen = function () {
    console.log("Connect successfully!");
    setTimer();
    websockLinked = true;

    $("#collaboration-switch").attr('checked', true);
  }
  websock.onmessage = function (e) {
    websocketonmessage(e);
    websockLinked = true;
  }
  websock.onclose = function (e) {
    console.log("Connection closed (" + e.code + ")");
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

function setTimer() {
  timer = setInterval(() => {
    var messageJson = {type: "ping"};
    websocketSend(messageJson);
  }, 20000);
}

function removeTimer() {
  clearInterval(timer);
}

//数据接收
function websocketonmessage(e) {
  try {
    let data = JSON.parse(e.data);
    switch (data.type) {
      case "members": {
        if (data.behavior == "on") {
          personOnline(data.participants);
        } else if (data.behavior == "off") {
          personOffline(activeUser);
        }
        break;
      }
      case "collaboration-init": {
        setCollaborationMode(data.mode);
        setOperator(data.operator);
        setWaitingLine(data.waiting);
        break;
      }
      case "mode": {
        if (data.operator !== userInfo.userId) {
          setCollaborationMode(data.mode);
        }
        setOperator("");
        setWaitingLine(0);
        break;
      }
      case "control-apply": {
        if (data.operator !== userInfo.userId) {
          $("#operation-apply").hide();
          $("#operation-stop").show();
        }
        setOperator(data.operator);
        setWaitingLine(data.waiting);
        break;
      }
      case "control-stop": {
        if (data.sender !== userInfo.userId) {
          $("#operation-apply").show();
          $("#operation-stop").hide();
        }
        setOperator(data.operator);
        setWaitingLine(data.waiting);
        break;
      }
      case "operation": {
        if (operationChannel != undefined && typeof operationChannel == "function") {
          if (data.sender !== userInfo.userId) {
            operationChannel(data);
          }
        }
        break;
      }
      case "data": {
        if (dataChannel != undefined && typeof dataChannel == "function") {
          if (data.sender !== userInfo.userId) {
            selectFile(data.content);
            dataChannel(data);
          }
        }
        break;
      }
      case "computation": {
        if (computationChannel != undefined && typeof computationChannel == "function") {
          computationChannel(data);
        }
        break;
      }
      case "test": {

      }
    }
  } catch (err) {
    throw err
  }
  ;
}


//数据发送
function websocketSend(agentData) {
  websock.send(JSON.stringify(agentData));
}

/**
 * public method
 * collaboration end
 */
function socketClose() {
  websock.close();
}

/**
 * public method
 * send custom operation
 */
function sendCustomOperation(agentData, callback) {
  operationChannel = callback;
  if (websock.readyState === websock.OPEN) {
    // 若是ws开启状态
    websocketSend(agentData)
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

/*
这个层次的工具并不是面向高级用户（开发者）
通用工具是使用模型部署包与数据服务包为后台
开发者只需要写封装代码即可
至于工具生成、调用这一块内容对其为透明的
为此
为了简化代码
没必要将两类工具 Invoke 强行拧在一起

面向开发者定制开发工具
若他在定制工具中使用到了通用工具
如果要进行自定义的话
那我最好还是统一接口
但是既然工具后台服务读取都使用不同的接口
那肯定是能区分是模型服务还是数据服务
这时使用强行拧在一起的接口
开发者更不能很好的理解参数含义
如：dataService 时 servicePort 置空
serviceIp 为 Token
 */
function sendModelOperation(aid, serviceMd5, serviceIp, servicePort, inputs, outputs, callback) {
  computationChannel = callback;
  //若是数据方法的话，则直接将部分参数置空即可
  let invokeForm = {
    serviceMd5: serviceMd5,
    serviceIp: serviceIp,
    servicePort: servicePort,
    computeAbleModel: true,
    type: "computation",
    inputs: inputs,
    outputs: outputs
  };
  console.log("invokeForm", invokeForm)
  if (websock.readyState === websock.OPEN) {
    websocketSend(invokeForm);
  } else if (websock.readyState === websock.CONNECTING) {
    setTimeout(function () {
      sendComputeOperation(aid, serviceId, serviceIp, servicePort, inputs, outputs, callback);
    }, 1000)
  } else {
    //未开启，等待 1s
    setTimeout(function () {
      sendComputeOperation(aid, serviceId, serviceIp, servicePort, inputs, outputs, callback);
    }, 1000)
  }
}

/**
 * data service callback method
 * @param aid
 * @param serviceId
 * @param serviceToken
 * @param inputs
 * @param params
 * @param callback
 */
function sendDataOperation(aid, serviceId, serviceToken, inputs, params, callback) {
  computationChannel = callback;
  let invokeMsg = {
    type: "computation",
    tid: serviceId,
    token: serviceToken,
    urls: inputs,
    params: params,
    computeAbleModel: false
  };
  if (websock.readyState === websock.OPEN) {
    websocketSend(invokeMsg);
  } else if (websock.readyState === websock.CONNECTING) {
    setTimeout(function () {
      sendDataOperation(aid, serviceId, serviceToken, inputs, params, callback);
    }, 1000)
  } else {
    setTimeout(function () {
      sendDataOperation(aid, serviceId, serviceToken, inputs, params, callback);
    }, 1000)
  }
}


/**
 * public method
 * build call back channel
 */
function buildSocketChannel(opeChannel, dataChannel, compChannel) {
  operationChannel = opeChannel;
  dataChannel = dataChannel;
  computationChannel = compChannel;
}

/**
 * public method
 * send computation operation
 */
function sendSock(agentData, callback) {
  operationChannel = callback;
  if (websock.readyState === websock.OPEN) {
    // 若是ws开启状态
    websocketSend(agentData)
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

/**
 * public method
 * get socket status
 */
function getSocketInfo() {
  return {
    linked: websockLinked
  }
}
