//* 常量
const sagaIp = "172.21.212.75";
const backend_ip = "172.21.213.183";
//const backend_ip = "127.0.0.1";


var model_config = {};
var formData;

//* 从 html 中获取模型配置信息
function getModelConfig() {
    var model_table = $('table')[1];
    var oid = model_table.getAttribute("oid");
    var tool_id = model_table.getAttribute("tool_id");
    var stateId = model_table.getAttribute("stateId");
    var mdlId = model_table.getAttribute("mdlId");
    model_config.oid = oid;
    model_config.tool_id = tool_id;
    model_config.stateId = stateId;
    model_config.mdlId = mdlId;
    // console.log(model_config);
}

function setInputData() {
    let input_tags = $('#input_body input');
    for (let i = 0; i < input_tags.length; i++) {
        let input_config = {};
        input_config.id = input_tags[i].id;
        input_config.optional = input_tags[i].getAttribute("optional");
        input_config.data_type = input_tags[i].getAttribute("data_type");
        input_config.value = input_tags[i].value;
        input_config.files = input_tags[i].files;
        console.log("input_config:",input_config);
        if (input_config.value == "" && input_config.optional == "false") {
            return false;
        } else if (input_config.value != "") {
            for(let j = 0; j<input_config.files.length; j++){
                formData.append(input_config.id, input_config.files[j]);
            }
        }
    }
    return true;
}

function setOptionsData() {
    // let json = [];
    var row = {};
    let index = 0;
    let options_body = $('#options_body');

    $("#options_body").find("tr").each(function () {
        //判断类型是否为choice
        let type = $(this).children().eq(1).html();
        if (type == "Choice") {
            var number = $(this).children().eq(5).find("select option:selected").val();
            // alert(number);
            let key = $(this).children().eq(5).find("select")[0].id;
            row[key] = number;
        } else {
            var number = $(this).children().eq(5).find("input").val();
            // alert(number);
            if (number) {
                let key = $(this).children().eq(5).find("input")[0].id;
                row[key] = number;
            }
        }
        index++;
    });
    var data = JSON.stringify(row);
    console.log("option:", data);
    formData.append("control", data);
}


//将点击下载设置为不可点击，颜色置灰
function setDownUnable() {
    $("#output_body").find("tr").each(function () {
        $(this).children().eq(5).children().eq(0).attr("href", "javascript:return false;");
        $(this).children().eq(5).children().eq(0).attr("class", "btn btn-danger disabled");
        $(this).children().eq(5).children().eq(0).css("color", "#fff");
    });
}

function progressToggle(isRun,progress){
    if(isRun){
        $(".progress").css("display","block");
        if(progress<4){
            $("#progressbar").css("width",`${progress*25}%`);
        }
    }else{
        $("#progressbar").css("width","100%");
        $(".progress").css("display","none");
    }
    
}

function onRunButtonClick() {
    getModelConfig();

    formData = new FormData();
    formData.append("oid", model_config.oid);
    formData.append("stateId", model_config.stateId);
    let inputAllready = false;
    inputAllready = setInputData();
    setOptionsData();
    
    if (inputAllready) { 
        setDownUnable();
        run();
    } else {
        alert("Data Not Ready");
    }
}

function run() {
    $.ajax({
        url: `http://${backend_ip}:9999/api/runSagaModel`,
        type: 'POST',
        cache: false,
        data: formData,
        processData: false,
        contentType: false,
        beforeSend:function(){
            progressToggle(true,1);
        },
        success: function(res){
            console.log(res);//* {code: 0, msg: "success", data: "5d09deb45108af141c3cb879"}
            //todo: show Tool is running.
            if(res.code === 0){
                let recordId = res.data;
                //* 定时器 询问模型是否运行完成：
                let progress = 1;
                let timer = setInterval(()=>{
                    //*设置进度条
                    progressToggle(true,progress++);
                    //*请求模型运行记录
                    queryModelRecord(recordId,timer);
                },3000);
                // queryModelRecord(recordId);
            }else{
                progressToggle(false,1);
                console.log(res.msg);
                alert(msg);
            }
        },
        error:function(err){
            progressToggle(false,1);
            console.error(err);
        }
    })
}


function queryModelRecord(recordId,timer){
    return fetch(`http://${backend_ip}:9999/api/instance?id=${recordId}&ip=${sagaIp}`,{ 
        cache: 'no-cache',
        credentials:'include',
        method:'GET',
        mode:'cors'
    })
    .then(response =>{
        if(response.ok){
            return response.json();
        }
        throw new Error('Network response was not ok.');
    })
    .then(resJson=>{
        if(resJson.code === 0){ 
            let dataJson = JSON.parse(resJson.data);
            console.log("dataJson:",dataJson);
            //* 判断模型是否运行成功
            if(dataJson["data"] && dataJson["data"]["msr_span"]!=null&&dataJson["data"]["msr_span"]!==0){
                //* 1. 结束定时任务
                clearInterval(timer);
                //* 2. 获取输出结果,设置下载地址
                let outputs = dataJson["data"]["msr_output"];
                setDownloadButton(outputs);
                //* 3. 更新界面UI
                progressToggle(false,1);
                setTimeout(() => {
                    alert("Success");
                }, 500); 
                console.log("success");
            }
        }else{
            throw new Error(resJson.msg);
        }
        console.log(resJson);
    })
    .catch(error=>{
        progressToggle(false,1);
        clearInterval(timer);
        console.error('There has been a problem with your fetch operation: ', error.message);
        alert(`There has been a problem with your fetch operation: ${error.message}`);
    })
}

function setDownloadButton(outputs){
    for(let i=0;i<outputs.length;i++){
        let output = outputs[i];
        let id = output.Event;
        let dataId = output.DataId;
        let button = $(`#${id}`); // jquery 对象
        button.attr("href",`http://${sagaIp}:8060/geodata/${dataId}`);
        button = button.get(0); // DOM对象
        // button.attr("href",`http://${sagaIp}:8060/geodata/${dataId}`);
        // button.addC
        // button.href = `http://${sagaIp}:8060/geodata/${dataId}`;
        button.style.color = "#fff";
        button.className = "btn btn-success";
    }
}