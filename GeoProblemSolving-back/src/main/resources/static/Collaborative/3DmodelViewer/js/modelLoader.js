var modeldata = null;
var modelType = "";


var wsTModel = null;
var socket_content = null;

var reader = new FileReader();

$("#modelType").change(function () {

    var typeValue = $("#modelType").val();
    switch (typeValue) {
        case "*.obj model" :
            modelType = "obj";
            break;
        case "*.stl model" :
            modelType = "stl";
            break;
        case "*.json model" :
            modelType = "json";
            break;
        case "*.3ds model" :
            modelType = "3ds";
            break;
        case "*.x model" :
            modelType = "x";
            break;
    }
});

$("#modelFile").change(function () {

    modeldata = $("#modelFile")[0].files[0];
});


function uploadFun() {

    sessionStorage.setItem("projectId","NWU4ZDA2ZjktZTk2OC00YjkzLWI1NTMtZDNjM2IxMDQ1N2Q2OTI=");
    sessionStorage.setItem("subProjectId","21aee7ab-d004-4203-a687-64076e4d93f5");
    sessionStorage.setItem("moduleId","f99e1aa9-311d-4e8f-9f19-de479f4d9d4a");

    var formData = new FormData();
    var userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
    formData.append("file", modeldata);
    formData.append("description", "3D viewer tool");
    formData.append("type", "data");
    formData.append("uploaderId", userInfo.userId);
    formData.append("belong", userInfo.userName);
    var scopeObject = {
        projectId: sessionStorage.getItem("projectId"),
        subProjectId: sessionStorage.getItem("subProjectId"),
        moduleId: sessionStorage.getItem("moduleId")
    };
    formData.append("scope", JSON.stringify(scopeObject));

    $.ajax({
        type: "POST",
        url: "/GeoProblemSolving/resource/upload",
        data: formData,
        cache: false,        //不设置缓存
        processData: false,  // 不处理数据
        contentType: false,  // 不设置内容类型
        success: function (data) {
            if (data != "Size over" && data.length > 0) {

                socket_content= {
                    "dataName":data[0].fileName,
                    "dataFiles":data[0].zipFiles,
                    "modelType":modelType,
                    "operate":"dataupload"
                };
                wsTModel.send(JSON.stringify(socket_content));


                // 加载模型
                addModelFun(data[0].fileName, data[0].zipFiles);
            }
        },
        error: function () {
            alert("fail");
        }
    });
}

$(document).ready(function() {

    var typeValue = $("#modelType").val();
    switch (typeValue) {
        case "*.obj model" :
            modelType = "obj";
            break;
        case "*.stl model" :
            modelType = "stl";
            break;
        case "*.json model" :
            modelType = "json";
            break;
        case "*.3ds model" :
            modelType = "3ds";
            break;
        case "*.x model" :
            modelType = "x";
            break;
    }

    var roomId = sessionStorage.getItem("moduleId");
    if (WebSocket) {
        var wsUrl = `${window.location.protocol === 'https:' ? 'wss://' : 'ws://'}` + window.location.host+"/GeoProblemSolving/3DviewerServer/"+roomId;
        if(window.location.host === "localhost:8080"){
            wsUrl = "ws://localhost:8081/GeoProblemSolving/3DviewerServer/"+roomId;
        }
        wsTModel = new WebSocket(wsUrl);
    }
    else {
        alert("浏览器不支持websocket！");
    }

    wsTModel.onopen = function () {
    };

    wsTModel.onmessage = function (ev) {

        var msgJson = JSON.parse(ev.data);

        if(msgJson.operate === "dataupload") {
            var files = msgJson.dataFiles;
            var fileName = msgJson.dataName;
            modelType = msgJson.modelType;

            addModelFun(fileName, files);
        }
    };
    wsTModel.onclose = function () {
    };
    wsTModel.onerror = function () {
    };
});

function addModelFun(fileName, files) {

    if (modelType !== "") {

        if (modelType === "3ds") {
            loadTDSmodel(fileName, files);
        }
        else if (modelType === "obj") {
            loadOBJmodel(fileName, files);
        }
        else if (modelType === "x") {
            loadXmodel(fileName, files);
        }
        else if (modelType === "stl") {
            loadSTLmodel(fileName, files);
        }
        else if (modelType === "json") {
            loadJSONmodel(fileName, files);
        }
        else {
            alert("The type of " + modelType + " is not supported!");
        }
    }
    else {
        alert("Please input model!");
    }
}

function loadTDSmodel(fileName, files) {

    var textureName = "", modelName = "";
    for(var i = 0; i< files.length; i++){
        var type = files[i].substring(files[i].lastIndexOf(".") + 1);
        if(type === "3ds"){
            modelName = files[i];
        } else if (type === "jpg" || type === "png") {
            textureName = files[i];
        }
    }
    var filePath = "/GeoProblemSolving/resource/upload/" + fileName;

    if(modelName !== "") {
        var texture;
        if (textureName !== "") {

            var loader = new THREE.TextureLoader();
            texture = loader.load(filePath + '/' +textureName);

        }
        var loader = new THREE.TDSLoader();
        loader.setResourcePath(filePath + '/');
        loader.load(filePath + '/' + modelName, function (object) {
            object.traverse(function (child) {
                if (child instanceof THREE.Mesh) {
                    child.material.normalMap = texture;
                }
            });
            scene.add(object);
        });
    }

}

function loadSTLmodel(fileName, files) {

    var modelName = "";
    for(var i = 0; i< files.length; i++){
        var type = files[i].substring(files[i].lastIndexOf(".") + 1);
        if(type === "stl"){
            modelName = files[i];
        }
    }
    var filePath = "/GeoProblemSolving/resource/upload/" + fileName;

    if(modelName !== "") {
        var loader = new THREE.STLLoader();
        loader.load(filePath + "/" +modelName, function (geometry) {
            //创建纹理
            var mat = new THREE.MeshLambertMaterial({color: 0xCCCCCC});
            var mesh = new THREE.Mesh(geometry, mat);
            mesh.rotation.x = -0.5 * Math.PI; //将模型摆正
            geometry.center(); //居中显示
            scene.add(mesh);
        })
    }

}

function loadOBJmodel(fileName, files) {

    var mtlName = "", modelName = "";
    for(var i = 0; i< files.length; i++){
        var type = files[i].substring(files[i].lastIndexOf(".") + 1);
        if(type === "obj"){
            modelName = files[i];
        } else if (type === "mtl") {
            mtlName = files[i];
        }
    }
    var filePath = "/GeoProblemSolving/resource/operateZip?key=" + fileName+"&value=";

    if(modelName !== "") {
        if (mtlName !== "") {

            var mtlLoader = new THREE.MTLLoader();
            mtlLoader.setPath(filePath);
            mtlLoader.load(mtlName, function (materials) {

                materials.preload();
                materials.transparent = true;

                var objLoader = new THREE.OBJLoader();
                objLoader.setMaterials(materials);
                objLoader.setPath(filePath);
                objLoader.load(modelName, function (object) {

                    scene.add(object);

                }, onProgress, onError);

            });
        }
        else {
            var loader = new THREE.OBJLoader();
            loader.load(filePath + "/" +modelName, function (obj) {
                var material = new THREE.MeshLambertMaterial({color: 0xCCCCCC});
                obj.traverse(function (child) {
                    if (child instanceof THREE.Mesh) {
                        // child.material.side = THREE.DoubleSide;
                        child.material = material;
                    }
                });
                scene.add(obj);
            });
        }
    }
}

function loadJSONmodel(fileName, files) {

    var modelName = "";
    for(var i = 0; i< files.length; i++){
        var type = files[i].substring(files[i].lastIndexOf(".") + 1);
        if(type === "json"){
            modelName = files[i];
        }
    }
    var filePath = "/GeoProblemSolving/resource/upload/" + fileName;

    if(modelName !== "") {
        var loader = new THREE.JSONLoader();
        loader.load( filePath + "/" +modelName, function( geometry, materials ) {

            materials[0].shading = THREE.FlatShading;
            var mesh = new THREE.Mesh( geometry, new THREE.MultiMaterial( materials ) );
            scene.add( mesh );
        });
    }

}

function loadXmodel(fileName, files) {

    var modelName = "";
    for(var i = 0; i< files.length; i++){
        var type = files[i].substring(files[i].lastIndexOf(".") + 1);
        if(type === "x"){
            modelName = files[i];
        }
    }
    var filePath = "/GeoProblemSolving/resource/upload/" + fileName;

    var manager = new THREE.LoadingManager();
    var Texloader = new THREE.TextureLoader();
    var loader = new THREE.XLoader(manager, Texloader);

    var Models = [];
    loader.load([filePath + '/' +modelName], function (object) {
        for (var i = 0; i < object.models.length; i++) {
            Models.push(object.models[i]);
            scene.add(Models[i]);
        }
        object = null;
    }, onProgress, onError);
}

var onProgress = function (xhr) {

    if (xhr.lengthComputable) {

        var percentComplete = xhr.loaded / xhr.total * 100;
        console.log(Math.round(percentComplete, 2) + '% downloaded');

    }

};

var onError = function (xhr) {
};

